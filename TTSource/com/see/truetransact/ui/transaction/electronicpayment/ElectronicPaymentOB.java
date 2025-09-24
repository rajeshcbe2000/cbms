/*
 * ElectronicPaymentOB.java
 *
 * Created on Nov 12, 2019, 12:15 AM
 */
package com.see.truetransact.ui.transaction.electronicpayment;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uivalidation.CurrencyValidation;

/**
 *
 * @author Sathiya
 */
public class ElectronicPaymentOB extends CObservable {

    private ProxyFactory proxy = null;
    ArrayList electrionicPaymentTableTitle = new ArrayList();
    ArrayList electrionicInquiryTableTitle = new ArrayList();
    HashMap lookupValues;
    private HashMap operationMap;
    private HashMap _authorizeMap;
    Date curDate = null;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String AuthorizeStatus;
    private EnhancedTableModel electrionicPaymentTable;
    private EnhancedTableModel electrionicInquiryTable;
    private List finalList = null;
    private final static Logger log = Logger.getLogger(ElectronicPaymentUI.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    HashMap finalMap = new HashMap();
    ArrayList tableList = new ArrayList();
    ArrayList tableInquiryList = new ArrayList();
    ArrayList electronicPaymentInquiryList = new ArrayList();
    ArrayList electronicPaymentList = new ArrayList();

    public ArrayList getElectronicPaymentList() {
        return electronicPaymentList;
    }

    public void setElectronicPaymentList(ArrayList electronicPaymentList) {
        this.electronicPaymentList = electronicPaymentList;
    }
    public double totalAmount = 0;
    public int totalCount = 0;
    public String lblElectronicPaymentIDVal = "";
    public String lblElectronicPaymentDtVal = "";
    private ArrayList key, value;
    private String cboElectronicType;
    private ComboBoxModel cbmElectronicType;

    /**
     * Creates a new instance of InterestMaintenanceOB
     */
    public ElectronicPaymentOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            initianSetup();
            notifyObservers();
        } catch (Exception e) {

        }
    }

    private void initianSetup() throws Exception {
        log.info("In initianSetup()");
        try {
            setOperationMap();
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        fillDropdown();     //__ To Fill all the Combo Boxes     
        setElectrionicPaymentTabTitle();
        setElectrionicInquiryTabTitle();
        electrionicPaymentTable = new EnhancedTableModel(null, electrionicPaymentTableTitle);
        electrionicInquiryTable = new EnhancedTableModel(null, electrionicInquiryTableTitle);
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ElectronicPaymentJNDI");
        operationMap.put(CommonConstants.HOME, "ElectronicPaymentHome");
        operationMap.put(CommonConstants.REMOTE, "ElectronicPayment");
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    // To Fill the Combo boxes in the UI

    public void fillDropdown() throws Exception {
        log.info("In fillDropdown()");
        HashMap lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

        HashMap param = new HashMap();
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("ELECTRONICTYPE");
        lookupKey.add("RECONTYPE");
        lookupKey.add("RECON_STATUS");
        param.put(CommonConstants.MAP_NAME, null);
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap) lookupValues.get("ELECTRONICTYPE"));
        cbmElectronicType = new ComboBoxModel(key, value);
        fillData((HashMap) lookupValues.get("RECONTYPE"));
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

    public void resetTableValues() {
        electrionicPaymentTable.setDataArrayList(null, electrionicPaymentTableTitle);
        tableList = new ArrayList();
        finalList = new ArrayList();
    }

    public void resetInquiryTableValues() {
        tableInquiryList = new ArrayList();
        electronicPaymentInquiryList = new ArrayList();
        electrionicInquiryTable.setDataArrayList(null, electrionicInquiryTableTitle);
    }

    public void doAction(Object param) {
        TTException exception = null;
        try {
//            if (get_authorizeMap() != null) {
//                doActionPerform(param);
//            } else {
            finalMap = (HashMap) param;
            finalMap.put("FINAL_PAYMENT_LIST", finalList);
            System.out.println("finalMap : " + finalMap);
            doActionPerform(finalMap);
//            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    private void doActionPerform(HashMap paramMap) throws Exception {
//        HashMap data = new HashMap();
//        if (get_authorizeMap() != null) {
//            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
//        }
        paramMap.put(CommonConstants.MODULE, getModule());
        paramMap.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(paramMap, operationMap);
        System.out.println("proxyResultMap..." + proxyResultMap);
        if (proxyResultMap != null && proxyResultMap.containsKey("FROM_BATCH_ID")) {
            int processedCount = CommonUtil.convertObjToInt(proxyResultMap.get("PROCESSED_COUNT"));
            if (processedCount == 1) {
                ClientUtil.showMessageWindow("Process Completed : \n"
                        + "From Batch id : " + CommonUtil.convertObjToStr(proxyResultMap.get("FROM_BATCH_ID")) + "\n"
                        + "To Batch id : " + CommonUtil.convertObjToStr(proxyResultMap.get("FROM_BATCH_ID")) + "\n"
                        + "Count : " + CommonUtil.convertObjToStr(proxyResultMap.get("PROCESSED_COUNT")) + "\n"
                        + "Amount : " + CommonUtil.convertObjToStr(proxyResultMap.get("PROCESSED_AMT")));
            } else {
                ClientUtil.showMessageWindow("Process Completed : \n"
                        + "From Batch id : " + CommonUtil.convertObjToStr(proxyResultMap.get("FROM_BATCH_ID")) + "\n"
                        + "To Batch id : " + CommonUtil.convertObjToStr(proxyResultMap.get("TO_BATCH_ID")) + "\n"
                        + "Count : " + CommonUtil.convertObjToStr(proxyResultMap.get("PROCESSED_COUNT")) + "\n"
                        + "Amount : " + CommonUtil.convertObjToStr(proxyResultMap.get("PROCESSED_AMT")));
            }
//            tranIdList += proxyReturnMap.get(CommonConstants.TRANS_ID) + "\n";
        } else if (proxyResultMap != null && proxyResultMap.containsKey("NOT_REGISTERED")) {
            ClientUtil.showMessageWindow(CommonUtil.convertObjToStr(proxyResultMap.get("NOT_REGISTERED")));
        }
        setProxyReturnMap(proxyResultMap);
//        System.out.println("proxyResultMap..."+proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }

    public void setElectrionicPaymentTabTitle() {
        electrionicPaymentTableTitle.add("Select");
        electrionicPaymentTableTitle.add("Sl. No");
        electrionicPaymentTableTitle.add("Account No");
        electrionicPaymentTableTitle.add("Trans Dt");
        electrionicPaymentTableTitle.add("Name");
        electrionicPaymentTableTitle.add("Product Id");
        electrionicPaymentTableTitle.add("Amount");
        electrionicPaymentTableTitle.add("UTR Number");
        electrionicPaymentTableTitle.add("Particulars");
        electrionicPaymentTableTitle.add("IFS Code");
        electrionicPaymentTableTitle.add("Sender Act Num");
        electrionicPaymentTableTitle.add("Bank Name");
        electrionicPaymentTableTitle.add("Branch Name");
    }

    public void setElectrionicInquiryTabTitle() {
        electrionicInquiryTableTitle.add("Account No");
        electrionicInquiryTableTitle.add("Trans Dt");
        electrionicInquiryTableTitle.add("Name");
        electrionicInquiryTableTitle.add("Amount");
        electrionicInquiryTableTitle.add("Our UTR Number");
        electrionicInquiryTableTitle.add("Payment Accepted Dt");
        electrionicInquiryTableTitle.add("API Response");
        electrionicInquiryTableTitle.add("Bank UTR Number");
        electrionicInquiryTableTitle.add("Bank Description");
        electrionicInquiryTableTitle.add("Particulars");
        electrionicInquiryTableTitle.add("Payment Status");
        electrionicInquiryTableTitle.add("Inquiry Status");
        electrionicInquiryTableTitle.add("Screen Name");
    }

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

    private void setWarning(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private String getAction() {
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

    public void populateTableData(HashMap param) {
        try {
            HashMap depMap = new HashMap();
            ArrayList rowList = new ArrayList();
            electronicPaymentList = new ArrayList();
            int slNo = 0;
            System.out.println("param : " + param);
            depMap.put("TRANS_ID", param.get("TRANS_ID"));
            depMap.put("FROM_UTR", param.get("FROM_UTR"));
            depMap.put("TO_UTR", param.get("TO_UTR"));
            depMap.put("SOURCE_SCREEN", param.get("SOURCE_SCREEN"));
            depMap.put("BATCH_DT", getProperDateFormat(param.get("BATCH_DT")));
            List depList = ClientUtil.executeQuery("getElectronicPaymentDetails", depMap);
            if (depList != null && depList.size() > 0) {
                for (int i = 0; i < depList.size(); i++) {
                    depMap = (HashMap) depList.get(i);
                    rowList = new ArrayList();
                    slNo++;
                    String transType = "";
                    rowList.add(false);
                    rowList.add(slNo);
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("ACCT_NUM")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("TRANS_DT")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("NAME")).replaceAll("[^a-zA-Z0-9]", " "));
                    double transAmt = CommonUtil.convertObjToDouble(depMap.get("AMOUNT"));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("INST_TYPE")));
                    totalAmount += CommonUtil.convertObjToDouble(depMap.get("AMOUNT"));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("AMOUNT")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("UTR_NUMBER")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("PARTICUALRS")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("IFSC_CODE")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("ACCOUNT_NO")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("BANK_NAME")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("BRANCH_NAME")));
                    tableList.add(rowList);
                    setLblElectronicPaymentIDVal(CommonUtil.convertObjToStr(depMap.get("TRANS_ID")));
                    setLblElectronicPaymentDtVal(CommonUtil.convertObjToStr(depMap.get("TRANS_DT")));
                    electronicPaymentList.add(rowList);
                }
            }
            setTotalCount(slNo);
            electrionicPaymentTable = new EnhancedTableModel((ArrayList) tableList, electrionicPaymentTableTitle);
        } catch (Exception e) {
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }

    public void resetForm() {
        lblElectronicPaymentIDVal = "";
        lblElectronicPaymentDtVal = "";
        totalAmount = 0;
        totalCount = 0;
        finalMap = new HashMap();
        resetTableValues();
    }

    public boolean populateInquiryTableData(HashMap param) {
        boolean recordExist = false;
        try {
            electronicPaymentInquiryList = new ArrayList();
            tableInquiryList = new ArrayList();
            HashMap depMap = new HashMap();
            ArrayList rowList = new ArrayList();
            System.out.println("param : " + param);
            List depList = null;
            String inquiryStatus = "";
            if (param.containsKey("INQUIRY_STATUS")) {
                inquiryStatus = CommonUtil.convertObjToStr(param.get("INQUIRY_STATUS"));
            }
            if (param.containsKey("INQUIRY_STATUS") && (CommonUtil.convertObjToStr(param.get("INQUIRY_STATUS")).equals("SUCCESS")
                    || CommonUtil.convertObjToStr(param.get("INQUIRY_STATUS")).equals("REJECTED")
                    || CommonUtil.convertObjToStr(param.get("INQUIRY_STATUS")).equals("In Progress"))) {
                if (CommonUtil.convertObjToStr(param.get("INQUIRY_STATUS")).equals("SUCCESS")
                        || CommonUtil.convertObjToStr(param.get("INQUIRY_STATUS")).equals("In Progress")) {
                    param.put("INQUIRY_STATUS_SUCCESS", "INQUIRY_STATUS_SUCCESS");
                    param.put("INQUIRY_STATUS", "'" + "SUCCESS" + "'" + "," + "'" + "BANK NOT RESPONDED" + "'" + "," + "'" + "IN PROGRESS" + "'");
                } else if (CommonUtil.convertObjToStr(param.get("INQUIRY_STATUS")).equals("REJECTED")) {
                    param.put("INQUIRY_STATUS_REJECTED", "INQUIRY_STATUS_REJECTED");
                    param.put("INQUIRY_STATUS", "'" + "REJECTED" + "'");
                }
                depList = ClientUtil.executeQuery("getElectronicPaymentInquiryWithDescDeatils", param);
            } else if (param.containsKey("PAYMENT_STATUS") && CommonUtil.convertObjToStr(param.get("PAYMENT_STATUS")).equals("ACCEPTED")) {
                depList = ClientUtil.executeQuery("getElectronicPaymentInquiryDeatils", param);
            } else {
                depList = ClientUtil.executeQuery("getElectronicPaymentNotProcessedInquiryDeatils", param);
            }
            if (depList != null && depList.size() > 0) {
                for (int i = 0; i < depList.size(); i++) {
                    depMap = (HashMap) depList.get(i);
                    rowList = new ArrayList();
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("ACCT_NUM")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("TRANS_DT")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("CUSTOMER_NAME")).replaceAll("[^a-zA-Z0-9]", " "));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("AMOUNT")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("UTR_NUMBER")));
                    if (inquiryStatus != null && (CommonUtil.convertObjToStr(inquiryStatus).equals("SUCCESS")
                            || CommonUtil.convertObjToStr(inquiryStatus).equals("REJECTED")
                            || CommonUtil.convertObjToStr(inquiryStatus).equals("In Progress"))) {
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("PAYMENT_ACCEPTED_DT")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("API_RESPONSE_STATUS")));
                        rowList.add(CommonUtil.convertObjToStr(depMap.get("BANK_INQUIRY_REF_NO")));
                        rowList.add("");
                    } else {
                        rowList.add("");
                        rowList.add("");
                        rowList.add("");
                        rowList.add("");
                    }
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("PARTICUALRS")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("PAYMENT_STATUS")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("INQUIRY_STATUS")));
                    rowList.add(CommonUtil.convertObjToStr(depMap.get("SOURCE")));
                    tableInquiryList.add(rowList);
                    electronicPaymentInquiryList.add(rowList);
                    recordExist = true;
                }
            }
            electrionicInquiryTable = new EnhancedTableModel((ArrayList) tableInquiryList, electrionicInquiryTableTitle);
        } catch (Exception e) {
        }
        return recordExist;
    }

    /**
     * To set and change the Status of the lable Status
     */
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

    /**
     * To reset the Value of lblStatus after each save action...
     */
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.
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
        notifyObservers();
    }

    public static void main(String st[]) {
        ArrayList list = new ArrayList();
        list.add(new Integer("23"));
        list.add(new Integer("2"));
        list.add(new Integer("12"));
        Object obj[] = list.toArray();
        java.util.Arrays.sort(obj);
    }

    /**
     * Getter for property AuthorizeStatus.
     *
     * @return Value of property AuthorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return AuthorizeStatus;
    }

    /**
     * Setter for property AuthorizeStatus.
     *
     * @param AuthorizeStatus New value of property AuthorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String AuthorizeStatus) {
        this.AuthorizeStatus = AuthorizeStatus;
    }

    /**
     * Getter for property cboElectronicType.
     *
     * @return Value of property cboElectronicType.
     *
     */
    public java.lang.String getCboElectronicType() {
        return cboElectronicType;
    }

    /**
     * Setter for property cboElectronicType.
     *
     * @param cboElectronicType New value of property cboElectronicType.
     *
     */
    public void setCboElectronicType(java.lang.String cboElectronicType) {
        this.cboElectronicType = cboElectronicType;
    }

    /**
     * Getter for property cbmElectronicType.
     *
     * @return Value of property cbmElectronicType.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmElectronicType() {
        return cbmElectronicType;
    }

    /**
     * Setter for property cbmElectronicType.
     *
     * @param cbmElectronicType New value of property cbmElectronicType.
     *
     */
    public void setCbmElectronicType(com.see.truetransact.clientutil.ComboBoxModel cbmElectronicType) {
        this.cbmElectronicType = cbmElectronicType;
    }

    public ArrayList getElectronicPaymentInquiryList() {
        return electronicPaymentInquiryList;
    }

    public void setElectronicPaymentInquiryList(ArrayList electronicPaymentInquiryList) {
        this.electronicPaymentInquiryList = electronicPaymentInquiryList;
    }

    public String getLblElectronicPaymentIDVal() {
        return lblElectronicPaymentIDVal;
    }

    public void setLblElectronicPaymentIDVal(String lblElectronicPaymentIDVal) {
        this.lblElectronicPaymentIDVal = lblElectronicPaymentIDVal;
    }

    public String getLblElectronicPaymentDtVal() {
        return lblElectronicPaymentDtVal;
    }

    public void setLblElectronicPaymentDtVal(String lblElectronicPaymentDtVal) {
        this.lblElectronicPaymentDtVal = lblElectronicPaymentDtVal;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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

    public EnhancedTableModel getElectrionicPaymentTable() {
        return electrionicPaymentTable;
    }

    public void setElectrionicPaymentTable(EnhancedTableModel electrionicPaymentTable) {
        this.electrionicPaymentTable = electrionicPaymentTable;
    }

    public EnhancedTableModel getElectrionicInquiryTable() {
        return electrionicInquiryTable;
    }

    public void setElectrionicInquiryTable(EnhancedTableModel electrionicInquiryTable) {
        this.electrionicInquiryTable = electrionicInquiryTable;
    }

    public List getFinalList() {
        return finalList;
    }

    public void setFinalList(List finalList) {
        this.finalList = finalList;
    }
}
