/*
 * Copyright 2004 SeE Consulting (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of SeE Consulting (P) Ltd..
 * Use is subject to license terms.
 *
 * ElectronicReconciliationOB.java
 * 
 * Created on Thu Sep 30 10:51:55 IST 2019
 */
package com.see.truetransact.ui.transaction.electronicreconciliation;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.Date;

/**
 *
 * @author Sathiya
 *
 */
public class ElectronicReconciliationOB extends CObservable {

    private final static Logger log = Logger.getLogger(ElectronicReconciliationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblFileDataDetails;
    private EnhancedTableModel electronicMatchStatusTable;
    private EnhancedTableModel electronicManualMatchOursTable;
    private EnhancedTableModel electrionicReconTable;
    private EnhancedTableModel electrionicReconRejectTable;
    private ProxyFactory proxy = null;
    private HashMap map;
    private HashMap operationMap;
    HashMap lookupValues;
    ArrayList electronicMatchStatusTableTitle = new ArrayList();
    ArrayList electronicManualMatchOursTableTitle = new ArrayList();
    ArrayList electronicManualMatchTheirsTableTitle = new ArrayList();
    ArrayList electronicManualMatchOursList = new ArrayList();
    ArrayList tableManualMatchOursList = new ArrayList();
    ArrayList electronicManualMatchTheirsList = new ArrayList();
    ArrayList tableManualMatchTheirsList = new ArrayList();
    ArrayList electrionicReconTableTitle = new ArrayList();
    private int actionType;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList editFileDataList = null;
    private String bulkID = "";
    private ComboBoxModel cbmProductID = null;
    private String cboProductID = "";
//    private String txtDebitGL = "";
    private String txtTotalAmount = "";
    private String txtFileName = "";
//    private String txtBeneficiaryName = "";
//    private String txtNarration = "";
    public ArrayList finalAutoReconList = new ArrayList();
    public ArrayList ourManualReconList = new ArrayList();
    public ArrayList theirsManualReconList = new ArrayList();
    private String cboMatchStatusType, cboUnMatchStatusType, cboReconTypeMatch, cboReconTypeManual,cboMatchTransType,cboReconTransType,cboFileUploadType = "";
    private ComboBoxModel cbmMatchStatusType, cbmUnMatchStatusType, cbmReconTypeMatch, cbmReconTypeManual, cbmSourceRejectType,
            cbmReconRejectType, cbmReconSourceType, cbmReconClassifiedCategory,cbmMatchTransType,cbmReconTransType,cbmFileUploadType;

    public ComboBoxModel getCbmFileUploadType() {
        return cbmFileUploadType;
    }

    public void setCbmFileUploadType(ComboBoxModel cbmFileUploadType) {
        this.cbmFileUploadType = cbmFileUploadType;
    }

    public String getCboFileUploadType() {
        return cboFileUploadType;
    }

    public void setCboFileUploadType(String cboFileUploadType) {
        this.cboFileUploadType = cboFileUploadType;
    }
    private Date currDate = null;
    public String getCboMatchTransType() {
        return cboMatchTransType;
    }

    public void setCboMatchTransType(String cboMatchTransType) {
        this.cboMatchTransType = cboMatchTransType;
    }

    public String getCboReconTransType() {
        return cboReconTransType;
    }

    public void setCboReconTransType(String cboReconTransType) {
        this.cboReconTransType = cboReconTransType;
    }

    public ComboBoxModel getCbmMatchTransType() {
        return cbmMatchTransType;
    }

    public void setCbmMatchTransType(ComboBoxModel cbmMatchTransType) {
        this.cbmMatchTransType = cbmMatchTransType;
    }

    public ComboBoxModel getCbmReconTransType() {
        return cbmReconTransType;
    }

    public void setCbmReconTransType(ComboBoxModel cbmReconTransType) {
        this.cbmReconTransType = cbmReconTransType;
    }
    ArrayList tableMatchStatusList = new ArrayList();
    ArrayList electronicMatchStatusList = new ArrayList();
    public double matchCount = 0;
    public double matchAmount = 0;
    public double unMatchOursCount = 0;
    public double unMatchOursAmount = 0;
    public double unMatchTheirsCount = 0;
    public double unMatchTheirsAmount = 0;
    public double lblBankTxnAmountValue = 0;
    public double lblBankTxnCountValue = 0;
    public double lblRejectCountValue, lblRejectAmountValue = 0;
    ArrayList electronicReconRejectList = new ArrayList();
    ArrayList electrionicReconRejectTableTitle = new ArrayList();
    ArrayList tableReconList = new ArrayList();
    ArrayList tableReconRejectList = new ArrayList();
    ArrayList electronicPaymentInquiryList = new ArrayList();
    ArrayList electronicReconList = new ArrayList();

    public ArrayList getTableReconList() {
        return tableReconList;
    }

    public void setTableReconList(ArrayList tableReconList) {
        this.tableReconList = tableReconList;
    }

    public ComboBoxModel getCbmSourceRejectType() {
        return cbmSourceRejectType;
    }

    public void setCbmSourceRejectType(ComboBoxModel cbmSourceRejectType) {
        this.cbmSourceRejectType = cbmSourceRejectType;
    }

    public ComboBoxModel getCbmReconRejectType() {
        return cbmReconRejectType;
    }

    public void setCbmReconRejectType(ComboBoxModel cbmReconRejectType) {
        this.cbmReconRejectType = cbmReconRejectType;
    }

    public ComboBoxModel getCbmReconSourceType() {
        return cbmReconSourceType;
    }

    public void setCbmReconSourceType(ComboBoxModel cbmReconSourceType) {
        this.cbmReconSourceType = cbmReconSourceType;
    }

    public ComboBoxModel getCbmReconClassifiedCategory() {
        return cbmReconClassifiedCategory;
    }

    public void setCbmReconClassifiedCategory(ComboBoxModel cbmReconClassifiedCategory) {
        this.cbmReconClassifiedCategory = cbmReconClassifiedCategory;
    }

    public ArrayList getElectrionicReconRejectTableTitle() {
        return electrionicReconRejectTableTitle;
    }

    public void setElectrionicReconRejectTableTitle(ArrayList electrionicReconRejectTableTitle) {
        this.electrionicReconRejectTableTitle = electrionicReconRejectTableTitle;
    }

    public ArrayList getElectronicReconRejectList() {
        return electronicReconRejectList;
    }

    public void setElectronicReconRejectList(ArrayList electronicReconRejectList) {
        this.electronicReconRejectList = electronicReconRejectList;
    }

    public String getCboReconTypeMatch() {
        return cboReconTypeMatch;
    }

    public void setCboReconTypeMatch(String cboReconTypeMatch) {
        this.cboReconTypeMatch = cboReconTypeMatch;
    }

    public String getCboReconTypeManual() {
        return cboReconTypeManual;
    }

    public void setCboReconTypeManual(String cboReconTypeManual) {
        this.cboReconTypeManual = cboReconTypeManual;
    }

    public ComboBoxModel getCbmReconTypeMatch() {
        return cbmReconTypeMatch;
    }

    public void setCbmReconTypeMatch(ComboBoxModel cbmReconTypeMatch) {
        this.cbmReconTypeMatch = cbmReconTypeMatch;
    }

    public ComboBoxModel getCbmReconTypeManual() {
        return cbmReconTypeManual;
    }

    public void setCbmReconTypeManual(ComboBoxModel cbmReconTypeManual) {
        this.cbmReconTypeManual = cbmReconTypeManual;
    }

    public ElectronicReconciliationOB() {
        try {
            setOperationMap();
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            setTableTitle();
            currDate = ClientUtil.getCurrentDate();
            tblFileDataDetails = new EnhancedTableModel(null, tableTitle);
            setElectronicMatchStatusTabTitle();
            electronicMatchStatusTable = new EnhancedTableModel(null, electronicMatchStatusTableTitle);
            setElectronicManualMatchOursTabTitle();
            electronicManualMatchOursTable = new EnhancedTableModel(null, electronicManualMatchOursTableTitle);
            setElectronicManualMatchTheirsTabTitle();
            electronicManualMatchTheirsTable = new EnhancedTableModel(null, electronicManualMatchTheirsTableTitle);
            setElectrionicReconTabTitle();
            setElectrionicReconRejectTabTitle();
            electrionicReconTable = new EnhancedTableModel(null, electrionicReconTableTitle);
            electrionicReconRejectTable = new EnhancedTableModel(null, electrionicReconRejectTableTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setTableTitle() {
        tableTitle.add("TRANS_DT");
        tableTitle.add("BRANCH");
        tableTitle.add("DESCRIPTION");
        tableTitle.add("REFERENCE NO");
        tableTitle.add("VALUE_DT");
        tableTitle.add("DEBIT");
        tableTitle.add("CREDIT");
        tableTitle.add("BALANCE");
        IncVal = new ArrayList();
    }

    public void setElectrionicReconTabTitle() {
        electrionicReconTableTitle.add("Payment Dt");
        electrionicReconTableTitle.add("Description");
        electrionicReconTableTitle.add("Reference No");
        electrionicReconTableTitle.add("Debits");
        electrionicReconTableTitle.add("Credits");
        electrionicReconTableTitle.add("Recon ID");
        electrionicReconTableTitle.add("Branch");
    }

    public void setElectrionicReconRejectTabTitle() {
        electrionicReconRejectTableTitle.add("Payment Dt");
        electrionicReconRejectTableTitle.add("Description");
        electrionicReconRejectTableTitle.add("Reference No");
        electrionicReconRejectTableTitle.add("Cust Name");
        electrionicReconRejectTableTitle.add("Acct No");
        electrionicReconRejectTableTitle.add("Mobile No");
        electrionicReconRejectTableTitle.add("Amount");
        electrionicReconRejectTableTitle.add("Recon ID");
        electrionicReconRejectTableTitle.add("Branch");
    }

// Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ElectronicReconciliationJNDI");
        operationMap.put(CommonConstants.HOME, "ElectronicReconciliationHome");
        operationMap.put(CommonConstants.REMOTE, "ElectronicReconciliation");
    }

    private void fillDropdown() throws Exception {
        try {
            log.info("In fillDropdown()");
            HashMap lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

            HashMap param = new HashMap();
            final ArrayList lookupKey = new ArrayList();
            lookupKey.add("RECONTYPE");//
            lookupKey.add("RECON_STATUS");
            lookupKey.add("ACCTHEADMAIN.BALTYPE");
            lookupKey.add("RECON_FILE_UPLOAD_TYPE");
            param.put(CommonConstants.MAP_NAME, null);
            param.put(CommonConstants.PARAMFORQUERY, lookupKey);
            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap) lookupValues.get("RECON_STATUS"));
            cbmMatchStatusType = new ComboBoxModel(key, value);
            cbmUnMatchStatusType = new ComboBoxModel(key, value);
            cbmSourceRejectType = new ComboBoxModel(key, value);
            cbmReconSourceType = new ComboBoxModel(key, value);
            fillData((HashMap) lookupValues.get("RECONTYPE"));
            cbmReconTypeMatch = new ComboBoxModel(key, value);
            cbmReconTypeManual = new ComboBoxModel(key, value);
            cbmReconRejectType = new ComboBoxModel(key, value);
            cbmReconClassifiedCategory = new ComboBoxModel(key, value);
            fillData((HashMap) lookupValues.get("ACCTHEADMAIN.BALTYPE"));
            cbmMatchTransType = new ComboBoxModel(key, value);
            cbmReconTransType = new ComboBoxModel(key, value);
            
            fillData((HashMap) lookupValues.get("RECON_FILE_UPLOAD_TYPE"));
            cbmFileUploadType = new ComboBoxModel(key, value);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            //System.out.println("######### OB Data : " + data);
            if (data.containsKey("FILE_DATA")) {
                whereMap = new HashMap();
                editFileDataList = new ArrayList();
                List depositList = (List) data.get("FILE_DATA");
                if (depositList != null && depositList.size() > 0) {
                    List rowLst = new ArrayList();
                    for (int i = 0; i < depositList.size(); i++) {
                        rowLst = new ArrayList();
                        whereMap = new HashMap();
                        whereMap = (HashMap) depositList.get(i);
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("CUST_ID")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("DEPOSIT_NO")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("CHILD_NAME")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("DEPOSIT_DT")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("MATURITY_DT")));
                        rowLst.add(String.valueOf(ClientUtil.convertObjToCurrency(String.valueOf(whereMap.get("AMOUNT")))));
                        rowLst.add(CommonUtil.convertObjToDouble(whereMap.get("INTEREST_RATE")));
                        rowLst.add(String.valueOf(ClientUtil.convertObjToCurrency(String.valueOf(whereMap.get("TOTAL_INTEREST")))));
                        editFileDataList.add(rowLst);
                    }
                }
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public HashMap getFileNames() {
        HashMap data = new HashMap();
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("DOWN_LOAD_FILES", "DOWN_LOAD_FILES");
            data = (HashMap) proxy.executeQuery(whereMap, map);
            //System.out.println("######### OB File Name Data : " + data);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return data;
    }

    public void doProcess(List fileNameList, String filePath,String fileUploadType) {
        HashMap data = new HashMap();
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("DOWN_LOAD_FILES", fileNameList);
            whereMap.put("FILE_PATH", filePath);
            whereMap.put("FILE_UPLOAD_TYPE", fileUploadType);
            data = (HashMap) proxy.execute(whereMap, operationMap);
            setProxyReturnMap(data);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setElectronicMatchStatusTabTitle() {
        electronicMatchStatusTableTitle.add("Trans Dt");
        electronicMatchStatusTableTitle.add("Description");
        electronicMatchStatusTableTitle.add("Reference NO");
        electronicMatchStatusTableTitle.add("Debits");
        electronicMatchStatusTableTitle.add("Credits");
        electronicMatchStatusTableTitle.add("IFS Code");
        electronicMatchStatusTableTitle.add("Bank Act Num");
        electronicMatchStatusTableTitle.add("Cust Name");
        electronicMatchStatusTableTitle.add("Recon ID");
        electronicMatchStatusTableTitle.add("Branch");
    }

    public void setElectronicManualMatchOursTabTitle() {
        electronicManualMatchOursTableTitle.add("Select");
        electronicManualMatchOursTableTitle.add("Trans Dt");
        electronicManualMatchOursTableTitle.add("Bank Reference NO");
        electronicManualMatchOursTableTitle.add("Description");
        electronicManualMatchOursTableTitle.add("Amount");
        electronicManualMatchOursTableTitle.add("Recon ID");
        electronicManualMatchOursTableTitle.add("Branch");
    }

    public void setElectronicManualMatchTheirsTabTitle() {
        electronicManualMatchTheirsTableTitle.add("Select");
        electronicManualMatchTheirsTableTitle.add("Trans Dt");
        electronicManualMatchTheirsTableTitle.add("Bank Reference NO");
        electronicManualMatchTheirsTableTitle.add("Description");
        electronicManualMatchTheirsTableTitle.add("Amount");
        electronicManualMatchTheirsTableTitle.add("Recon ID");
        electronicManualMatchTheirsTableTitle.add("Branch");
    }

    /**
     * To perform the necessary operation
     */
    public void doAction(List finalTableList) {
        TTException exception = null;
        try {
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                doActionPerform(finalTableList);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
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

    /**
     * To perform the necessary action
     */
    private void doActionPerform(List finalTableList) throws Exception {
        TTException exception = null;
        try {
            final HashMap data = new HashMap();
            data.put("COMMAND", getCommand());
//            if (getAuthorizeMap() == null) {
            if (finalTableList != null && finalTableList.size() > 0) {
                data.put("FILE_DATA", finalTableList);
            }
            data.put("PROD_ID", CommonUtil.convertObjToStr(cbmProductID.getKeyForSelected()));
            data.put("TOTAL_AMOUNT", getTxtTotalAmount());
            data.put("FILE_NAME", getTxtFileName());
            data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            data.put("STATUS", getAction());
            data.put("STATUS_BY", TrueTransactMain.USER_ID);
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            HashMap proxyResultMap = proxy.execute(data, map);
            setProxyReturnMap(proxyResultMap);
            setResult(getActionType());
//            _authorizeMap = null;
            finalTableList = null;
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

    public boolean populateMatchStatusTableData(HashMap param) {
        boolean recordExist = false;
        try {
            electronicMatchStatusList = new ArrayList();
            tableMatchStatusList = new ArrayList();
            HashMap depMap = new HashMap();
            ArrayList rowList = new ArrayList();
            System.out.println("param : " + param);
            List depList = null;
            
            if (param.containsKey("TYPE_OF_RECON") && (CommonUtil.convertObjToStr(param.get("TYPE_OF_RECON")).equals("Processing Bank Entries"))) {
                if(param.containsKey("CLASIFIED_CAT") && CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("1PAY") || 
                        CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("RTGS COLLECTION")){
                    param.put("MATCH_STATUS", "M");
                    if(CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("RTGS COLLECTION")){
//                        param.put("MATCH_CONDINTION", "RTGS_FMS");
                        param.put("MATCH_CONDINTION", "'" + "RTGS_FMS" + "'");
                    }else{
                        param.put("MATCH_CONDINTION", "'" + "1PAY_FMS" + "'");
                    }
                    depList = ClientUtil.executeQuery("getONLINEProcessedRecords", param);
                }else{
                    param.put("MATCH_CONDITION", "HDFC_RTGS");
                    depList = ClientUtil.executeQuery("getMatchStatusHDFCNEFTRTGSNEFTPayment", param);
                }
            } else if (param.containsKey("TYPE_OF_RECON") && (CommonUtil.convertObjToStr(param.get("TYPE_OF_RECON")).equals("System Entries"))) {
                if(param.containsKey("CLASIFIED_CAT") && CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("1PAY") || 
                        CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("RTGS COLLECTION")){
                    param.put("MATCH_STATUS", "M");
                    if(CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("RTGS COLLECTION")){
//                        param.put("MATCH_CONDINTION", "FMS_RTGS");
                        param.put("MATCH_CONDINTION", "'" + "FMS_RTGS" + "'");
                    }else{
                        param.put("MATCH_CONDINTION", "'" + "FMS_1PAY" + "'");
                    }
                    depList = ClientUtil.executeQuery("getONLINEProcessedRecords", param);
                }else{
                    param.put("MATCH_CONDITION", "NEFT_RTGS");
                    depList = ClientUtil.executeQuery("getMatchStatusHDFCNEFTRTGSNEFTPayment", param);
                }
            }
            matchCount = 0;
            matchAmount = 0;
            if (depList != null && depList.size() > 0) {
                for (int i = 0; i < depList.size(); i++) {
                    depMap = (HashMap) depList.get(i);
                    rowList = new ArrayList();
                    matchCount = i + 1;
                    if (param.containsKey("TYPE_OF_RECON") && (CommonUtil.convertObjToStr(param.get("TYPE_OF_RECON")).equals("Processing Bank Entries"))) {
                        matchAmount = matchAmount + CommonUtil.convertObjToDouble(depMap.get("DEBITS"));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("TRANS_DT")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("DESCRIPTION")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("REFERENCE_NO")));
                        rowList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(depMap.get("DEBITS"))));
                        rowList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(depMap.get("CREDITS"))));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("IFS_CODE")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("BANK_ACT_NUM")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("CUST_NAME")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("RECONB_ID")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("BRANCH")));
                    } else {
                        matchAmount = matchAmount + CommonUtil.convertObjToDouble(depMap.get("DEBITS"));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("TRANS_DT")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("DESCRIPTION")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("REFERENCE_NO")));
                        rowList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(depMap.get("DEBITS"))));
                        rowList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(depMap.get("CREDITS"))));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("IFS_CODE")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("BANK_ACT_NUM")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("CUST_NAME")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("RECONB_ID")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("")));
                    }
                    tableMatchStatusList.add(rowList);
                    electronicMatchStatusList.add(rowList);
                    recordExist = true;
                }
            }
            electronicMatchStatusTable = new EnhancedTableModel((ArrayList) tableMatchStatusList, electronicMatchStatusTableTitle);
        } catch (Exception e) {
        }
        return recordExist;
    }

    private String getCommand() {
        String command = null;
        //System.out.println("actionType : " + actionType);
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
        // System.out.println("command : " + command);
        return action;
    }

    public void resetForm() {
        bulkID = "";
        cboProductID = "";
//        txtDebitGL = "";
        txtTotalAmount = "";
//        txtNarration = "";
        txtFileName = "";
//        txtBeneficiaryName = "";
        editFileDataList = null;
        resetTableValues();
        setChanged();
    }

    public void resetReconTableValues() {
        tableReconList = new ArrayList();
        electronicReconList = new ArrayList();
        electrionicReconTable.setDataArrayList(null, electrionicReconTableTitle);
    }

    public void resetTableValues() {
        tblFileDataDetails.setDataArrayList(null, tableTitle);
    }

    public void resetMatchStatusTableValues() {
        tableMatchStatusList = new ArrayList();
        electronicMatchStatusList = new ArrayList();
        electronicMatchStatusTable.setDataArrayList(null, electronicMatchStatusTableTitle);
    }

    public void resetManualMatchTableValues() {
        tableManualMatchOursList = new ArrayList();
        electronicManualMatchOursList = new ArrayList();
        electronicManualMatchOursTable.setDataArrayList(null, electronicManualMatchOursTableTitle);
        tableManualMatchTheirsList = new ArrayList();
        electronicManualMatchTheirsList = new ArrayList();
        electronicManualMatchTheirsTable.setDataArrayList(null, electronicManualMatchTheirsTableTitle);
    }

    public boolean populateManualMatchTableData(HashMap param) {
        boolean recordExist = false;
        try {
            electronicManualMatchOursList = new ArrayList();
            tableManualMatchOursList = new ArrayList();
            electronicManualMatchTheirsList = new ArrayList();
            tableManualMatchTheirsList = new ArrayList();
            HashMap depMap = new HashMap();
            ArrayList rowList = new ArrayList();
            System.out.println("populateManualMatchTableData param : " + param);
            List unMatchHDFCList = null;
            List unMatchOursList = null;
            unMatchOursCount = 0;
            unMatchOursAmount = 0;
            unMatchTheirsCount = 0;
            unMatchTheirsAmount = 0;
            param.put("MATCH_CONDITION", "HDFC_RTGS");
            unMatchHDFCList = ClientUtil.executeQuery("getUnMatchStatusHDFCRTGSNEFTPayment", param);
            if (unMatchHDFCList != null && unMatchHDFCList.size() > 0) {
                for (int i = 0; i < unMatchHDFCList.size(); i++) {
                    depMap = (HashMap) unMatchHDFCList.get(i);
                    rowList = new ArrayList();
                    rowList.add(false);
                    unMatchTheirsCount = i + 1;
                    unMatchTheirsAmount = unMatchTheirsAmount + CommonUtil.convertObjToDouble(depMap.get("DEBITS"));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("TRANS_DT")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("REFERENCE_NO")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("DESCRIPTION")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("DEBITS")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("RECONB_ID")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("BRANCH")));
                    tableManualMatchTheirsList.add(rowList);
                    electronicManualMatchTheirsList.add(rowList);
                    recordExist = true;
                }
                electronicManualMatchTheirsTable = new EnhancedTableModel((ArrayList) electronicManualMatchTheirsList, electronicManualMatchTheirsTableTitle);
            }
            param.put("MATCH_CONDITION", "NEFT_RTGS");
            unMatchOursList = ClientUtil.executeQuery("getUnMatchStatusHDFCRTGSNEFTPayment", param);
            if (unMatchOursList != null && unMatchOursList.size() > 0) {
                for (int i = 0; i < unMatchOursList.size(); i++) {
                    depMap = (HashMap) unMatchOursList.get(i);
                    rowList = new ArrayList();
                    rowList.add(false);
                    unMatchOursCount = i + 1;
                    unMatchOursAmount = unMatchOursAmount + CommonUtil.convertObjToDouble(depMap.get("DEBITS"));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("TRANS_DT")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("REFERENCE_NO")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("DESCRIPTION")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("DEBITS")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("RECONB_ID")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("")));
                    tableManualMatchOursList.add(rowList);
                    electronicManualMatchOursList.add(rowList);
                    recordExist = true;
                }
                electronicManualMatchOursTable = new EnhancedTableModel((ArrayList) tableManualMatchOursList, electronicManualMatchOursTableTitle);
            }
        } catch (Exception e) {
        }
        return recordExist;
    }

    public boolean populateReconTableData(HashMap param) {
        boolean recordExist = false;
        try {
            electronicReconList = new ArrayList();
            tableReconList = new ArrayList();
            HashMap depMap = new HashMap();
            ArrayList rowList = new ArrayList();
            List depList = null;
            lblBankTxnAmountValue = 0;
            lblBankTxnCountValue = 0;
            param.put("FILTERED_LIST", "");
            System.out.println("param : " + param);
            if (param.containsKey("SOURCE") && (CommonUtil.convertObjToStr(param.get("SOURCE")).equals("Processing Bank Entries"))) {
                if(param.containsKey("CLASIFIED_CAT") && (CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("1PAY")) ||
                        CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("RTGS COLLECTION")) {
                    if(CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("RTGS COLLECTION")){
                        param.put("MATCH_CONDINTION", "'" + "RTGS_RTGS" + "'" + "," + "'" + "RTGS_FMS" + "'");
                    }else{
                        param.put("MATCH_CONDINTION", "'" + "1PAY_FMS" + "'");
                    }
                    depList = ClientUtil.executeQuery("getONLINEProcessedRecords", param);
                }else{
                    depList = ClientUtil.executeQuery("getHDFCProcessedRecords", param);
                }
            } else {
                if(param.containsKey("CLASIFIED_CAT") && (CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("1PAY")) ||
                        CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("RTGS COLLECTION")) {
                    if(CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("RTGS COLLECTION")){
                        param.put("MATCH_CONDINTION", "'" + "FMS_FMS" + "'" + "," + "'" + "FMS_RTGS" + "'");
                    }else{
                        param.put("MATCH_CONDINTION", "'" + "1PAY_FMS" + "'");
                    }
                    depList = ClientUtil.executeQuery("getONLINEProcessedRecords", param);
                }else{
                    depList = ClientUtil.executeQuery("getNEFTProcessedRecords", param);
                }
            }
            if (depList != null && depList.size() > 0) {
                for (int i = 0; i < depList.size(); i++) {
                    depMap = (HashMap) depList.get(i);
                    rowList = new ArrayList();
                    lblBankTxnCountValue = i + 1;
                    if (CommonUtil.convertObjToDouble(depMap.get("DEBITS")) > 0) {
                        lblBankTxnAmountValue = lblBankTxnAmountValue + CommonUtil.convertObjToDouble(depMap.get("DEBITS"));
                    } else {
                        lblBankTxnAmountValue = lblBankTxnAmountValue + CommonUtil.convertObjToDouble(depMap.get("CREDITS"));
                    }
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("TRANS_DT")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("DESCRIPTION")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("REFERENCE_NO")));
                    rowList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(depMap.get("DEBITS"))));
                    rowList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(depMap.get("CREDITS"))));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("RECONB_ID")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("BRANCH")));
                    tableReconList.add(rowList);
                    electronicReconList.add(rowList);
                    recordExist = true;
                }
            }
            electrionicReconTable = new EnhancedTableModel((ArrayList) tableReconList, electrionicReconTableTitle);
        } catch (Exception e) {
        }
        return recordExist;
    }

    public boolean populateReconRejectTableData(HashMap param) {
        boolean recordExist = false;
        try {
            electronicReconRejectList = new ArrayList();
            tableReconRejectList = new ArrayList();
            HashMap depMap = new HashMap();
            ArrayList rowList = new ArrayList();
            System.out.println("param : " + param);
            List depList = null;
            lblRejectCountValue = 0;
            lblRejectAmountValue = 0;
            if (param.containsKey("SOURCE") && (CommonUtil.convertObjToStr(param.get("SOURCE")).equals("Processing Bank Entries"))) {
                if(param.containsKey("CLASIFIED_CAT") && CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("1PAY")){
                    param.put("MATCH_CONDINTION", "1PAY_FMS");
                    param.put("MATCH_STATUS", "U");
                    depList = ClientUtil.executeQuery("getONLINEProcessedRecords", param);
                }else{
                    param.put("MATCH_STATUS", "REV");
                    param.put("MATCH_CONDITION", "REV_HDFC_C");
                    depList = ClientUtil.executeQuery("getRejectedHDFCNEFTRTGSNEFTPayment", param);
                }
            } else {
                if(param.containsKey("CLASIFIED_CAT") && CommonUtil.convertObjToStr(param.get("CLASIFIED_CAT")).equals("1PAY")){
                    param.put("MATCH_CONDINTION", "FMS_1PAY");
                    param.put("MATCH_STATUS", "U");
                    depList = ClientUtil.executeQuery("getONLINEProcessedRecords", param);
                }else{                
                    param.put("MATCH_STATUS", "REJ");
                    param.put("MATCH_CONDITION", "NEFT_REJ");
                    depList = ClientUtil.executeQuery("getRejectedHDFCNEFTRTGSNEFTPayment", param);
                }
            }
            depList = ClientUtil.executeQuery("getRejectedHDFCNEFTRTGSNEFTPayment", param);
            if (depList != null && depList.size() > 0) {
                for (int i = 0; i < depList.size(); i++) {
                    lblRejectCountValue = i + 1;
                    depMap = (HashMap) depList.get(i);
                    rowList = new ArrayList();
                    if (param.containsKey("SOURCE") && (CommonUtil.convertObjToStr(param.get("SOURCE")).equals("Processing Bank Entries"))) {
                        lblRejectAmountValue = lblRejectAmountValue + CommonUtil.convertObjToDouble(depMap.get("CREDITS"));
                    }else{
                        lblRejectAmountValue = lblRejectAmountValue + CommonUtil.convertObjToDouble(depMap.get("DEBITS"));
                    }
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("TRANS_DT")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("DESCRIPTION")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("REFERENCE_NO")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("CUST_NAME")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("ACCT_NUM")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("CUST_MOBILE")));
                    if (param.containsKey("SOURCE") && (CommonUtil.convertObjToStr(param.get("SOURCE")).equals("Processing Bank Entries"))) {
                        rowList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(depMap.get("CREDITS"))));
                    }else{
                        rowList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(depMap.get("DEBITS"))));
                    }
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("RECONB_ID")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("BRANCH")));
                    tableReconRejectList.add(rowList);
                    electronicReconRejectList.add(rowList);
                    recordExist = true;
                }
            }
            electrionicReconRejectTable = new EnhancedTableModel((ArrayList) tableReconRejectList, electrionicReconRejectTableTitle);
        } catch (Exception e) {
        }
        return recordExist;
    }
    
    public void updateMatausStausRecord(String hdfcUTRNumber, String ourUTRNumber, Date paymentTransDt, Date initiatedTransDt){
        try{
	        HashMap whereMap = new HashMap();
	        whereMap.put("HDFC_REFERENCE_NO",hdfcUTRNumber);
	        whereMap.put("PAYMENT_TRANS_DT",getProperDateFormat(paymentTransDt));
	        whereMap.put("OUR_UTR_NUMBER",ourUTRNumber);
	        whereMap.put("INITIATED_TRANS_DT",getProperDateFormat(initiatedTransDt));
	        HashMap proxyResultMap = (HashMap) proxy.execute(whereMap, operationMap);
	        if(proxyResultMap != null){
	            setProxyReturnMap(proxyResultMap);
	            ClientUtil.showMessageWindow("Manual Recon Id " + ": " + CommonUtil.convertObjToStr(proxyResultMap.get("RECON_PROCESS_ID")));
	        }
        }catch(Exception e){
            
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) currDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
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

    /**
     * Getter for property tableTitle.
     *
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
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
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public EnhancedTableModel getTblFileDataDetails() {
        return tblFileDataDetails;
    }

    public void setTblFileDataDetails(EnhancedTableModel tblFileDataDetails) {
        this.tblFileDataDetails = tblFileDataDetails;
    }

    public ArrayList getEditFileDataList() {
        return editFileDataList;
    }

    public void setEditFileDataList(ArrayList editFileDataList) {
        this.editFileDataList = editFileDataList;
    }

    public ComboBoxModel getCbmProductID() {
        return cbmProductID;
    }

    public void setCbmProductID(ComboBoxModel cbmProductID) {
        this.cbmProductID = cbmProductID;
    }

    public String getCboProductID() {
        return cboProductID;
    }

    public void setCboProductID(String cboProductID) {
        this.cboProductID = cboProductID;
    }

    public String getTxtTotalAmount() {
        return txtTotalAmount;
    }

    public void setTxtTotalAmount(String txtTotalAmount) {
        this.txtTotalAmount = txtTotalAmount;
    }

    public String getTxtFileName() {
        return txtFileName;
    }

    public void setTxtFileName(String txtFileName) {
        this.txtFileName = txtFileName;
    }

    public String getBulkID() {
        return bulkID;
    }

    public void setBulkID(String bulkID) {
        this.bulkID = bulkID;
    }

    public ArrayList getElectronicManualMatchOursList() {
        return electronicManualMatchOursList;
    }

    public void setElectronicManualMatchOursList(ArrayList electronicManualMatchOursList) {
        this.electronicManualMatchOursList = electronicManualMatchOursList;
    }

    public ArrayList getElectronicManualMatchTheirsList() {
        return electronicManualMatchTheirsList;
    }

    public void setElectronicManualMatchTheirsList(ArrayList electronicManualMatchTheirsList) {
        this.electronicManualMatchTheirsList = electronicManualMatchTheirsList;
    }

    public ArrayList getElectronicManualMatchTheirsTableTitle() {
        return electronicManualMatchTheirsTableTitle;
    }

    public void setElectronicManualMatchTheirsTableTitle(ArrayList electronicManualMatchTheirsTableTitle) {
        this.electronicManualMatchTheirsTableTitle = electronicManualMatchTheirsTableTitle;
    }

    public EnhancedTableModel getElectronicManualMatchOursTable() {
        return electronicManualMatchOursTable;
    }

    public void setElectronicManualMatchOursTable(EnhancedTableModel electronicManualMatchOursTable) {
        this.electronicManualMatchOursTable = electronicManualMatchOursTable;
    }
    private EnhancedTableModel electronicManualMatchTheirsTable;

    public EnhancedTableModel getElectronicManualMatchTheirsTable() {
        return electronicManualMatchTheirsTable;
    }

    public void setElectronicManualMatchTheirsTable(EnhancedTableModel electronicManualMatchTheirsTable) {
        this.electronicManualMatchTheirsTable = electronicManualMatchTheirsTable;
    }

    public ArrayList getElectronicManualMatchOursTableTitle() {
        return electronicManualMatchOursTableTitle;
    }

    public void setElectronicManualMatchOursTableTitle(ArrayList electronicManualMatchOursTableTitle) {
        this.electronicManualMatchOursTableTitle = electronicManualMatchOursTableTitle;
    }

    public String getCboUnMatchStatusType() {
        return cboUnMatchStatusType;
    }

    public void setCboUnMatchStatusType(String cboUnMatchStatusType) {
        this.cboUnMatchStatusType = cboUnMatchStatusType;
    }

    public ComboBoxModel getCbmUnMatchStatusType() {
        return cbmUnMatchStatusType;
    }

    public void setCbmUnMatchStatusType(ComboBoxModel cbmUnMatchStatusType) {
        this.cbmUnMatchStatusType = cbmUnMatchStatusType;
    }

    public EnhancedTableModel getElectronicMatchStatusTable() {
        return electronicMatchStatusTable;
    }

    public void setElectronicMatchStatusTable(EnhancedTableModel electronicMatchStatusTable) {
        this.electronicMatchStatusTable = electronicMatchStatusTable;
    }

    public ArrayList getElectronicMatchStatusList() {
        return electronicMatchStatusList;
    }

    public void setElectronicMatchStatusList(ArrayList electronicMatchStatusList) {
        this.electronicMatchStatusList = electronicMatchStatusList;
    }

    public ComboBoxModel getCbmMatchStatusType() {
        return cbmMatchStatusType;
    }

    public void setCbmMatchStatusType(ComboBoxModel cbmMatchStatusType) {
        this.cbmMatchStatusType = cbmMatchStatusType;
    }

    public String getCboMatchStatusType() {
        return cboMatchStatusType;
    }

    public void setCboMatchStatusType(String cboMatchStatusType) {
        this.cboMatchStatusType = cboMatchStatusType;
    }

    public ArrayList getOurManualReconList() {
        return ourManualReconList;
    }

    public void setOurManualReconList(ArrayList ourManualReconList) {
        this.ourManualReconList = ourManualReconList;
    }

    public ArrayList getTheirsManualReconList() {
        return theirsManualReconList;
    }

    public void setTheirsManualReconList(ArrayList theirsManualReconList) {
        this.theirsManualReconList = theirsManualReconList;
    }

    public ArrayList getFinalAutoReconList() {
        return finalAutoReconList;
    }

    public void setFinalAutoReconList(ArrayList finalAutoReconList) {
        this.finalAutoReconList = finalAutoReconList;
    }

    public void resetReconRejectTableValues() {
        tableReconRejectList = new ArrayList();
        electronicReconRejectList = new ArrayList();
        electrionicReconRejectTable.setDataArrayList(null, electrionicReconRejectTableTitle);
    }

    public EnhancedTableModel getElectrionicReconTable() {
        return electrionicReconTable;
    }

    public void setElectrionicReconTable(EnhancedTableModel electrionicReconTable) {
        this.electrionicReconTable = electrionicReconTable;
    }

    public EnhancedTableModel getElectrionicReconRejectTable() {
        return electrionicReconRejectTable;
    }

    public void setElectrionicReconRejectTable(EnhancedTableModel electrionicReconRejectTable) {
        this.electrionicReconRejectTable = electrionicReconRejectTable;
    }
}
