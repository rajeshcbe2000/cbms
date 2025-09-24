/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ProductMasterOB.java
 *
 * Created on Mon Jun 20 16:52:36 GMT+05:30 2011
 */
package com.see.truetransact.ui.suspenseaccount;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;


import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.suspenseaccount.SuspenseAccountMasterTO;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author
 */
public class SuspenseAccountMasterOB extends CObservable {

    private String cboSuspenseProdID;
    private String txtSuspenseProdDescription;
    private String txtSuspenseActNum;
    private String tdtSuspenseOpenDate;
    private String txtMemberNumber;
    private String txtCustomerId;
    private String txtName;
    private String txtAddress;
    private String txtPrefix;
    private String txtAccRefNo;
    private ComboBoxModel cbmagentID;
    private ComboBoxModel cbmDealer;//Added By Revathi.L
    private String rdoSalaryRecovery;
    private static SuspenseAccountMasterOB objSuspenseAccountMasterOB;
    private ComboBoxModel cbmSuspenseProdID;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(SuspenseAccountMasterOB.class);
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType = 0;
    private int result = 0;
    HashMap data = new HashMap();
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private HashMap map;
    private ProxyFactory proxy = null;
    private Date currDt = null;
    private String cboagentId;
    private String cboDealer;//Added By Revathi.L
    private TransactionOB transactionOB;
    private double balace = 0.0;
    private String closeAccount = "";
    private String closeWithTransaction = "";
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private String txtAgentID = "";//Added By Revathi.L
    private String txtDealerID = "";
    private String tdtIntCalcUpToDt = "";

    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    public double getBalace() {
        return balace;
    }

    public void setBalace(double balace) {
        this.balace = balace;
    }

    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    public ComboBoxModel getCbmagentID() {
        return cbmagentID;
    }

    public void setCbmagentID(ComboBoxModel cbmagentID) {
        this.cbmagentID = cbmagentID;
    }

    public String getCboagentId() {
        return cboagentId;
    }

    public void setCboagentId(String cboagentId) {
        this.cboagentId = cboagentId;
    }

    public SuspenseAccountMasterOB() {
        currDt = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "SuspenseAccountMasterJNDI");
        map.put(CommonConstants.HOME, "serverside.paddyprocurement.SuspenseAccountMasterHome");
        map.put(CommonConstants.REMOTE, "serverside.paddyprocurement.SuspenseAccountMaster");
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
//            setBillsChargesTab();
//            tblBillsCharges=new EnhancedTableModel(null,billsChargeTabTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void initUIComboBoxModel() {
        cbmSuspenseProdID = new ComboBoxModel();
    }

    static {
        try {
            _log.info("Creating BillsOB...");
            objSuspenseAccountMasterOB = new SuspenseAccountMasterOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * Returns an instance of PaddyLocalityMasterOB.
     *
     * @return PaddyLocalityMasterOB
     */
    public static SuspenseAccountMasterOB getInstance() throws Exception {
        return objSuspenseAccountMasterOB;
    }

    private void fillDropdown() throws Exception {
        try {
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();


            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME, "getSuspenseProductID");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap) keyValue.get(CommonConstants.DATA));
            cbmSuspenseProdID = new ComboBoxModel(key, value);
            // setChanged();
            // notifyObservers();
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        //keyValue = ClientUtil.executeQuery("getAgentID", mapShare);
        mapShare.put("TYPE", "A");
        keyValue = ClientUtil.executeQuery("getAgentIDNew", mapShare);
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("AGENT_ID"));
                value.add(mapShare.get("FNAME"));
            }
        }
        cbmagentID = new ComboBoxModel(key,value);
        //Added By Revathi.L
        key = new ArrayList();
        value = new ArrayList();
        mapShare = new HashMap();
        keyValue = null;
        //keyValue = ClientUtil.executeQuery("getAgentID", mapShare);
        mapShare.put("TYPE", "D");
        keyValue = ClientUtil.executeQuery("getAgentIDNew", mapShare);
        key.add("");
        value.add("");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("AGENT_ID"));
                value.add(mapShare.get("FNAME"));
            }
        }
        cbmDealer = new ComboBoxModel(key,value);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
        } catch (NullPointerException e) {
            parseException.logException(e, true);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    public void resetForm() {
        setTxtCustomerId("");
        setTxtMemberNumber("");
        setTxtSuspenseProdDescription("");
        setCboSuspenseProdID("");
//        setCboagentId("");
        setTxtSuspenseActNum("");
        setTxtName("");
        setTxtAddress("");
        setTdtSuspenseOpenDate("");
        setTxtPrefix("");
        setTxtAccRefNo("");
        setTxtAgentID("");
        setTxtDealerID("");
        setTdtIntCalcUpToDt("");
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }
//    private void getKeyValue(HashMap keyValue)  throws Exception{
//        key = (ArrayList)keyValue.get(CommonConstants.KEY);
//        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
//    }

    /* Executes Query using the TO object */
    public void doAction() {
        try {
            HashMap dataMap = new HashMap();
            dataMap.put(CommonConstants.MODULE, getModule());
            dataMap.put(CommonConstants.SCREEN, getScreen());
            dataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            String command = getCommand();
            dataMap.put("SuspenseAccountMasterTO", getSuspenseAccountMasterTO(command));
            //Added by sreekrishnan
            if(getCloseAccount()!=null && getCloseAccount().equals("Y") && getCloseWithTransaction()!=null && getCloseWithTransaction().equals("Y")){
                if (transactionDetailsTO == null)
                    transactionDetailsTO = new LinkedHashMap();
                if (deletedTransactionDetailsTO != null) {
                    transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                    deletedTransactionDetailsTO = null;
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                allowedTransactionDetailsTO = null;        
                dataMap.put("TransactionTO",transactionDetailsTO);
                dataMap.put("ACCOUNT_CLOSED_TRANSACTION","ACCOUNT_CLOSED_TRANSACTION");
            }else if(getCloseAccount()!=null && getCloseAccount().equals("Y") && getCloseWithTransaction()!=null && getCloseWithTransaction().equals("N")){
                dataMap.put("ACCOUNT_CLOSED_ONLY","ACCOUNT_CLOSED_ONLY");
            }
            System.out.println("@#$@#$dataMap:" + dataMap);
            HashMap proxyResultMap = proxy.execute(dataMap, map);
            if (proxyResultMap != null && proxyResultMap.size() > 0 && proxyResultMap.containsKey("NO DATA IN BRANCH_ACNO_MAINTENANCE")) {
                ClientUtil.showMessageWindow("There are no Records for this Product-ID in  Account Maintance !!!");
            } else {
                setResult(actionType);
                setProxyReturnMap(proxyResultMap);
            }
            actionType = ClientConstants.ACTIONTYPE_CANCEL;
            dataMap = null;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /* To get the type of command */
    private String getCommand() throws Exception {
        String command = null;
        switch (getActionType()) {
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

    
    public void authorizeSuspenseMaster(HashMap singleAuthorizeMap) {
        try {
            singleAuthorizeMap.put("AUTH_DATA", "AUTH_DATA");
            if(transactionOB.getAllowedTransactionDetailsTO()!=null && transactionOB.getAllowedTransactionDetailsTO().size()>0){
                if (transactionDetailsTO == null)
                    transactionDetailsTO = new LinkedHashMap();
                if (deletedTransactionDetailsTO != null) {
                    transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                    deletedTransactionDetailsTO = null;
                }
                System.out.println("transactionOB.getAllowedTransactionDetailsTO()$@$@#$"+transactionOB.getAllowedTransactionDetailsTO());
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,transactionOB.getAllowedTransactionDetailsTO());
                allowedTransactionDetailsTO = null;        
                singleAuthorizeMap.put("TransactionTO",transactionDetailsTO);
            }
            proxy.executeQuery(singleAuthorizeMap, map);
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }
    private SuspenseAccountMasterTO getSuspenseAccountMasterTO(String command) {
        SuspenseAccountMasterTO objSuspenseAccountMasterTO = new SuspenseAccountMasterTO();
        objSuspenseAccountMasterTO.setTxtSuspenseProdDescription(getTxtSuspenseProdDescription());
        objSuspenseAccountMasterTO.setCboSuspenseProdID(getCboSuspenseProdID());
        objSuspenseAccountMasterTO.setTxtSuspenseActNum(getTxtSuspenseActNum());
        objSuspenseAccountMasterTO.setTxtAccRefNo(getTxtAccRefNo());
        objSuspenseAccountMasterTO.setTxtPrefix(getTxtPrefix());
        objSuspenseAccountMasterTO.setTxtMemberNumber(getTxtMemberNumber());
        objSuspenseAccountMasterTO.setTxtCustomerId(getTxtCustomerId());
        objSuspenseAccountMasterTO.setTxtName(getTxtName());
        objSuspenseAccountMasterTO.setTxtAddress(getTxtAddress());
        objSuspenseAccountMasterTO.setTdtSuspenseOpenDate(CommonUtil.getProperDate(DateUtil.getDateMMDDYYYY(getTdtSuspenseOpenDate()),DateUtil.getDateMMDDYYYY(getTdtSuspenseOpenDate())));
        objSuspenseAccountMasterTO.setCommand(command);
        objSuspenseAccountMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objSuspenseAccountMasterTO.setStatusDt(currDt);
        objSuspenseAccountMasterTO.setBranchCode(TrueTransactMain.BRANCH_ID);
        objSuspenseAccountMasterTO.setRdoSalRecovery(getRdoSalaryRecovery());
//        objSuspenseAccountMasterTO.setCboAgentID(getCboagentId());
//        objSuspenseAccountMasterTO.setCboDealer(getCboDealer());//Added By Revathi.L
        objSuspenseAccountMasterTO.setCboAgentID(getTxtAgentID());
        objSuspenseAccountMasterTO.setCboDealer(getTxtDealerID());//Added By Revathi.L
        objSuspenseAccountMasterTO.setIsAuction("N");
        objSuspenseAccountMasterTO.setTotalBalace(new Double(0));
        objSuspenseAccountMasterTO.setIntCalcUpToDt(DateUtil.getDateMMDDYYYY(getTdtIntCalcUpToDt()));
        System.out.println("@#$@#$@#objSuspenseAccountMasterTO" + objSuspenseAccountMasterTO);
        return objSuspenseAccountMasterTO;
    }

    private void setSuspenseAccountMasterTO(SuspenseAccountMasterTO objSuspenseAccountMasterTO) {
        setTxtAddress(objSuspenseAccountMasterTO.getTxtAddress());
        setTxtCustomerId(objSuspenseAccountMasterTO.getTxtCustomerId());
        setTxtMemberNumber(objSuspenseAccountMasterTO.getTxtMemberNumber());
        setTxtName(objSuspenseAccountMasterTO.getTxtName());
        setTxtSuspenseProdDescription(objSuspenseAccountMasterTO.getTxtSuspenseProdDescription());
        setCboSuspenseProdID(CommonUtil.convertObjToStr(objSuspenseAccountMasterTO.getCboSuspenseProdID()));
        setTxtSuspenseActNum(objSuspenseAccountMasterTO.getTxtSuspenseActNum());
        setTdtSuspenseOpenDate(CommonUtil.convertObjToStr(objSuspenseAccountMasterTO.getTdtSuspenseOpenDate()));
        setTxtPrefix(objSuspenseAccountMasterTO.getTxtPrefix());
        setTxtAccRefNo(objSuspenseAccountMasterTO.getTxtAccRefNo());
        setRdoSalaryRecovery(objSuspenseAccountMasterTO.getRdoSalRecovery());
        //setCboagentId(objSuspenseAccountMasterTO.getCboAgentID());
        setTxtAgentID(objSuspenseAccountMasterTO.getCboAgentID());
        setTxtDealerID(objSuspenseAccountMasterTO.getCboDealer());//Added By Revathi.L
        setBalace(objSuspenseAccountMasterTO.getTotalBalace());
        setTdtIntCalcUpToDt(CommonUtil.convertObjToStr(objSuspenseAccountMasterTO.getIntCalcUpToDt()));
    }

    public void getData(HashMap whereMap) {
        try {
            System.out.println("#@$%#$%#$%map:" + map);
            data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("@#$@#$@#$@#data : " + data + " : " + whereMap + " : " + map);
            if (data.containsKey("SuspenseAccountMasterTO")) {
                SuspenseAccountMasterTO objSuspenseAccountMasterTO = (SuspenseAccountMasterTO) data.get("SuspenseAccountMasterTO");
                System.out.println("@#$@#SuspenseAccountMasterTO:" + objSuspenseAccountMasterTO);
                setSuspenseAccountMasterTO(objSuspenseAccountMasterTO);
                ttNotifyObservers();
            }
            if(data.containsKey("TRANSACTION_LIST")){
                List list = (List) data.get("TRANSACTION_LIST");
                System.out.println(" list#^#^"+ list);
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
             System.out.println(" observable.getTransactionOB()#^#^"+ getTransactionOB());
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
        setChanged();

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
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
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
     * Getter for property txtSuspenseActNum.
     *
     * @return Value of property txtSuspenseActNum.
     */
    public java.lang.String getTxtSuspenseActNum() {
        return txtSuspenseActNum;
    }

    /**
     * Setter for property txtSuspenseActNum.
     *
     * @param txtSuspenseActNum New value of property txtSuspenseActNum.
     */
    public void setTxtSuspenseActNum(java.lang.String txtSuspenseActNum) {
        this.txtSuspenseActNum = txtSuspenseActNum;
    }

    /**
     * Getter for property tdtSuspenseOpenDate.
     *
     * @return Value of property tdtSuspenseOpenDate.
     */
    public java.lang.String getTdtSuspenseOpenDate() {
        return tdtSuspenseOpenDate;
    }

    /**
     * Setter for property tdtSuspenseOpenDate.
     *
     * @param tdtSuspenseOpenDate New value of property tdtSuspenseOpenDate.
     */
    public void setTdtSuspenseOpenDate(java.lang.String tdtSuspenseOpenDate) {
        this.tdtSuspenseOpenDate = tdtSuspenseOpenDate;
    }

    /**
     * Getter for property txtMemberNumber.
     *
     * @return Value of property txtMemberNumber.
     */
    public java.lang.String getTxtMemberNumber() {
        return txtMemberNumber;
    }

    /**
     * Setter for property txtMemberNumber.
     *
     * @param txtMemberNumber New value of property txtMemberNumber.
     */
    public void setTxtMemberNumber(java.lang.String txtMemberNumber) {
        this.txtMemberNumber = txtMemberNumber;
    }

    /**
     * Getter for property txtCustomerId.
     *
     * @return Value of property txtCustomerId.
     */
    public java.lang.String getTxtCustomerId() {
        return txtCustomerId;
    }

    /**
     * Setter for property txtCustomerId.
     *
     * @param txtCustomerId New value of property txtCustomerId.
     */
    public void setTxtCustomerId(java.lang.String txtCustomerId) {
        this.txtCustomerId = txtCustomerId;
    }

    /**
     * Getter for property txtName.
     *
     * @return Value of property txtName.
     */
    public java.lang.String getTxtName() {
        return txtName;
    }

    /**
     * Setter for property txtName.
     *
     * @param txtName New value of property txtName.
     */
    public void setTxtName(java.lang.String txtName) {
        this.txtName = txtName;
    }

    /**
     * Getter for property txtAddress.
     *
     * @return Value of property txtAddress.
     */
    public java.lang.String getTxtAddress() {
        return txtAddress;
    }

    /**
     * Setter for property txtAddress.
     *
     * @param txtAddress New value of property txtAddress.
     */
    public void setTxtAddress(java.lang.String txtAddress) {
        this.txtAddress = txtAddress;
    }

    /**
     * Getter for property cboSuspenseProdID.
     *
     * @return Value of property cboSuspenseProdID.
     */
    public java.lang.String getCboSuspenseProdID() {
        return cboSuspenseProdID;
    }

    /**
     * Setter for property cboSuspenseProdID.
     *
     * @param cboSuspenseProdID New value of property cboSuspenseProdID.
     */
    public void setCboSuspenseProdID(java.lang.String cboSuspenseProdID) {
        this.cboSuspenseProdID = cboSuspenseProdID;
    }

    /**
     * Getter for property cbmSuspenseProdID.
     *
     * @return Value of property cbmSuspenseProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSuspenseProdID() {
        return cbmSuspenseProdID;
    }

    /**
     * Setter for property cbmSuspenseProdID.
     *
     * @param cbmSuspenseProdID New value of property cbmSuspenseProdID.
     */
    public void setCbmSuspenseProdID(com.see.truetransact.clientutil.ComboBoxModel cbmSuspenseProdID) {
        this.cbmSuspenseProdID = cbmSuspenseProdID;
    }

    /**
     * Getter for property txtSuspenseProdDescription.
     *
     * @return Value of property txtSuspenseProdDescription.
     */
    public java.lang.String getTxtSuspenseProdDescription() {
        return txtSuspenseProdDescription;
    }

    /**
     * Setter for property txtSuspenseProdDescription.
     *
     * @param txtSuspenseProdDescription New value of property
     * txtSuspenseProdDescription.
     */
    public void setTxtSuspenseProdDescription(java.lang.String txtSuspenseProdDescription) {
        this.txtSuspenseProdDescription = txtSuspenseProdDescription;
    }

    /**
     * Getter for property txtPrefix.
     *
     * @return Value of property txtPrefix.
     */
    public String getTxtPrefix() {
        return txtPrefix;
    }

    /**
     * Setter for property txtPrefix.
     *
     * @param txtPrefix New value of property txtPrefix.
     */
    public void setTxtPrefix(String txtPrefix) {
        this.txtPrefix = txtPrefix;
    }

    /**
     * Getter for property txtAccRefNo.
     *
     * @return Value of property txtAccRefNo.
     */
    public String getTxtAccRefNo() {
        return txtAccRefNo;
    }

    /**
     * Setter for property txtAccRefNo.
     *
     * @param txtAccRefNo New value of property txtAccRefNo.
     */
    public void setTxtAccRefNo(String txtAccRefNo) {
        this.txtAccRefNo = txtAccRefNo;
    }

    /**
     * Getter for property rdoSalaryRecovery.
     *
     * @return Value of property rdoSalaryRecovery.
     */
    public String getRdoSalaryRecovery() {
        return rdoSalaryRecovery;
    }

    /**
     * Setter for property rdoSalaryRecovery.
     *
     * @param rdoSalaryRecovery New value of property rdoSalaryRecovery.
     */
    public void setRdoSalaryRecovery(String rdoSalaryRecovery) {
        this.rdoSalaryRecovery = rdoSalaryRecovery;
    }

    public String getCloseAccount() {
        return closeAccount;
    }

    public void setCloseAccount(String closeAccount) {
        this.closeAccount = closeAccount;
    }

    public String getCloseWithTransaction() {
        return closeWithTransaction;
    }

    public void setCloseWithTransaction(String closeWithTransaction) {
        this.closeWithTransaction = closeWithTransaction;
    }

    public ComboBoxModel getCbmDealer() {
        return cbmDealer;
    }

    public void setCbmDealer(ComboBoxModel cbmDealer) {
        this.cbmDealer = cbmDealer;
    }

    public String getCboDealer() {
        return cboDealer;
    }

    public void setCboDealer(String cboDealer) {
        this.cboDealer = cboDealer;
    }

    public String getTxtAgentID() {
        return txtAgentID;
    }

    public void setTxtAgentID(String txtAgentID) {
        this.txtAgentID = txtAgentID;
    }

    public String getTxtDealerID() {
        return txtDealerID;
    }

    public void setTxtDealerID(String txtDealerID) {
        this.txtDealerID = txtDealerID;
    }
    
    public String getTdtIntCalcUpToDt() {
        return tdtIntCalcUpToDt;
    }
    
    public void setTdtIntCalcUpToDt(String tdtIntCalcUpToDt) {
        this.tdtIntCalcUpToDt = tdtIntCalcUpToDt;
    }
    
}