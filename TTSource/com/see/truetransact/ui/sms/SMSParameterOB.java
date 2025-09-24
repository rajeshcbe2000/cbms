/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SMSParameterOB.java
 *
 * Created on Thu May 03 12:23:57 IST 2012
 */
package com.see.truetransact.ui.sms;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.sms.SMSParameterTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.CTable;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.transferobject.common.lookup.LookUpTO;
import org.apache.log4j.Logger;

/**
 *
 * @author
 */
public class SMSParameterOB extends CObservable {

    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private HashMap authorizeMap;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmShareType;
    private String cboProdId = "";
    private String cboShareType = "";
    private boolean chkDebitClearing = false;
    private boolean chkDebitTransfer = false;
    private boolean chkDebitCash = false;
    private boolean chkCreditClearing = false;
    private boolean chkCreditTransfer = false;
    private boolean chkCreditCash = false;
    private String cboProdType = "";
    private String txtDebitTransferAmt = "";
    private String txtCreditClearingAmt = "";
    private String txtRemarks = "";
    private String txtDebitCashAmt = "";
    private String txtCreditTransferAmt = "";
    private String txtDebitClearingAmt = "";
    private String txtCreditCashAmt = "";
    private String txtTxnAllowed = "";
    private EnhancedTableModel tblData;
    final ArrayList tableTitle = new ArrayList();
    private List finalList = null;
    private ArrayList IncVal = new ArrayList();
    private ComboBoxModel cbmSMSProducts;
    private ComboBoxModel cbmBranch;
    private String cboBranch;
    private String cboSMSProducts;

    public String getTxtTxnAllowed() {
        return txtTxnAllowed;
    }

    public void setTxtTxnAllowed(String txtTxnAllowed) {
        this.txtTxnAllowed = txtTxnAllowed;
    }
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(SMSParameterUI.class);
    private ProxyFactory proxy = null;
    private Date curDate = null;
    private boolean rdoReminderYes = false;
    private boolean rdoReminderNo = false;
    private boolean rdoTxnAllowedYes = false;

    public boolean getRdoTxnAllowedYes() {
        return rdoTxnAllowedYes;
    }

    public void setRdoTxnAllowedYes(boolean rdoTxnAllowedYes) {
        this.rdoTxnAllowedYes = rdoTxnAllowedYes;
    }

    public boolean getRdoTxnAllowedNo() {
        return rdoTxnAllowedNo;
    }

    public void setRdoTxnAllowedNo(boolean rdoTxnAllowedNo) {
        this.rdoTxnAllowedNo = rdoTxnAllowedNo;
    }
    private boolean rdoTxnAllowedNo = false;

    public boolean getRdoReminderNo() {
        return rdoReminderNo;
    }

    public void setRdoReminderNo(boolean rdoReminderNo) {
        this.rdoReminderNo = rdoReminderNo;
    }

    public boolean getRdoReminderYes() {
        return rdoReminderYes;
    }

    public void setRdoReminderYes(boolean rdoReminderYes) {
        this.rdoReminderYes = rdoReminderYes;
    }

    /**
     * Creates a new instance of ChequeBookOB
     */
    public SMSParameterOB() throws Exception {
        initianSetup();
    }

    private void initianSetup() throws Exception {
        log.info("initianSetup");
        curDate = ClientUtil.getCurrentDate();
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
            setTableTitle();
            tblData = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            //e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
    }

    public void fillDropdown() throws Exception {
        log.info("fillDropdown");

        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();

        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("OPERATIVEACCTPRODUCT.STATCHRG");//Added by kannan
        lookup_keys.add("SHARE_TYPE");
        lookup_keys.add("SMSPRODUCTS");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

        keyValue = ClientUtil.populateLookupData(lookUpHash);


        getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
        cbmProdType = new ComboBoxModel(key, value);

        getKeyValue((HashMap) keyValue.get("SHARE_TYPE"));
        cbmShareType = new ComboBoxModel(key, value);
        
        getKeyValue((HashMap) keyValue.get("SMSPRODUCTS"));
        cbmSMSProducts = new ComboBoxModel(key, value); 
        
        ArrayList key1 = new ArrayList();
        ArrayList value1 = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue1 = null;
        keyValue1 = ClientUtil.executeQuery("getbranches", mapShare);
        key1.add("");
        value1.add("");
        if (keyValue1 != null && keyValue1.size() > 0) {
            for (int i = 0; i < keyValue1.size(); i++) {
                mapShare = (HashMap) keyValue1.get(i);
                key1.add(mapShare.get("BRANCH_CODE"));
                value1.add(mapShare.get("BRANCH_CODE"));
            }
        }
        cbmBranch = new ComboBoxModel(key1, value1);  
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");

        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "SMSParameterJNDI");
        operationMap.put(CommonConstants.HOME, "sms.SMSParameterHome");
        operationMap.put(CommonConstants.REMOTE, "sms.SMSParameter");
    }

    public void getProductID(String prodType) {
        if (prodType.length() > 1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            cbmProdId = new ComboBoxModel(key, value);
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }

    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public void resetForm() {
        getCbmProdType().setKeyForSelected("");
        if (getCbmProdId() != null) {
            getCbmProdId().setKeyForSelected("");
        }
        setChkDebitCash(false);
        setChkCreditCash(false);
        setChkDebitTransfer(false);
        setChkCreditTransfer(false);
        setChkDebitClearing(false);
        setChkCreditClearing(false);
        setTxtDebitCashAmt("");
        setTxtCreditCashAmt("");
        setTxtDebitTransferAmt("");
        setTxtCreditTransferAmt("");
        setTxtDebitClearingAmt("");
        setTxtCreditClearingAmt("");
        setTxtRemarks("");
        setTxtTxnAllowed("");
        ttNotifyObservers();
        setAuthorizeMap(null);
        resetTableValues();
    }

    public void setTableTitle() {
        tableTitle.add("Select");
        tableTitle.add("Share Act Num");
        tableTitle.add("Mobile No");
        tableTitle.add("Name");
        IncVal = new ArrayList();
    }

    // TESTING THE DOACTION...
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null || getAuthorizeMap() != null) {
                    doActionPerform(null);
                }
            } else {
                log.info("Action Type Not Defined In setChequeBookTO()");
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            System.out.println("Error : " + e.toString());
            parseException.logException(e, true);
        }
    }

    //testing the doaction perform...
    public void doActionPerform(ArrayList list) throws Exception {
        log.info("In doActionPerform()");

        final HashMap data = new HashMap();
        data.put(CommonConstants.SELECTED_BRANCH_ID, ProxyParameters.BRANCH_ID);
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        if (list != null && list.size() > 0) {
//            data.put("CUSTOMER_GROUP", list);
            data.put("CUSTOMER_LIST", list);
        } else {
            data.put("SMSParameterTO", getSMSParameterTO());
            if (getAuthorizeMap() != null) {
                data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            }
        }
        if (list != null && list.size() > 0) {
            data.put("SMS_ACCOUNT_LIST", list);
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
    }

    /* To decide which action Should be performed...*/
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

    private SMSParameterTO getSMSParameterTO() throws Exception {
        SMSParameterTO objSMSParameterTO = new SMSParameterTO();
        objSMSParameterTO.setCommand(getCommand());
        objSMSParameterTO.setProdType(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
        objSMSParameterTO.setProdId(CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
        if (getChkCreditCash() == true) {
            objSMSParameterTO.setCrCash("Y");
        } else {
            objSMSParameterTO.setCrCash("N");
        }

        if (getChkCreditClearing() == true) {
            objSMSParameterTO.setCrClearing("Y");
        } else {
            objSMSParameterTO.setCrClearing("N");
        }

        if (getChkCreditTransfer() == true) {
            objSMSParameterTO.setCrTransfer("Y");
        } else {
            objSMSParameterTO.setCrTransfer("N");
        }

        if (getChkDebitCash() == true) {
            objSMSParameterTO.setDrCash("Y");
        } else {
            objSMSParameterTO.setDrCash("N");
        }

        if (getChkDebitClearing() == true) {
            objSMSParameterTO.setDrClearing("Y");
        } else {
            objSMSParameterTO.setDrClearing("N");
        }

        if (getChkDebitTransfer() == true) {
            objSMSParameterTO.setDrTransfer("Y");
        } else {
            objSMSParameterTO.setDrTransfer("N");
        }

        objSMSParameterTO.setDrCashAmt(CommonUtil.convertObjToDouble(getTxtDebitCashAmt()));
        objSMSParameterTO.setCrCashAmt(CommonUtil.convertObjToDouble(getTxtCreditCashAmt()));
        objSMSParameterTO.setDrTransferAmt(CommonUtil.convertObjToDouble(getTxtDebitTransferAmt()));
        objSMSParameterTO.setCrTransferAmt(CommonUtil.convertObjToDouble(getTxtCreditTransferAmt()));
        objSMSParameterTO.setDrClearingAmt(CommonUtil.convertObjToDouble(getTxtDebitClearingAmt()));
        objSMSParameterTO.setCrClearingAmt(CommonUtil.convertObjToDouble(getTxtCreditClearingAmt()));
        objSMSParameterTO.setRemarks(getTxtRemarks());
        if (getCommand() != null) {
            if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                objSMSParameterTO.setCreatedDt(curDate);
                objSMSParameterTO.setCreatedBy(ProxyParameters.USER_ID);
                objSMSParameterTO.setStatus(CommonConstants.STATUS_CREATED);
            } else if (getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                objSMSParameterTO.setStatus(CommonConstants.STATUS_MODIFIED);
            } else if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                objSMSParameterTO.setStatus(CommonConstants.STATUS_DELETED);
            }
        }
        objSMSParameterTO.setStatusBy(ProxyParameters.USER_ID);
        if (getAuthorizeMap() != null) {
            objSMSParameterTO.setAuthorizeStatus(CommonUtil.convertObjToStr(getAuthorizeMap().get(CommonConstants.AUTHORIZESTATUS)));
            objSMSParameterTO.setAuthorizedBy(ProxyParameters.USER_ID);
            objSMSParameterTO.setAuthorizedDt(curDate);
        }
        // Added by nithya on 11-11-2016
        if (getRdoReminderYes() == true) {
            objSMSParameterTO.setReminder("Y");
        } else {
            objSMSParameterTO.setReminder("N");
        }
        if (getRdoTxnAllowedYes() == true) {
            objSMSParameterTO.setTxnAllowed("Y");
        } else {
            objSMSParameterTO.setTxnAllowed("N");
        }
        return objSMSParameterTO;
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public boolean checkProdIdExistance() {
        if (CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()).length() > 0
                && CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()).length() > 0) {
            HashMap map = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.PRODUCT_TYPE, getCbmProdType().getKeyForSelected());
            whereMap.put(CommonConstants.PRODUCT_ID, getCbmProdId().getKeyForSelected());
            map.put(CommonConstants.MAP_WHERE, whereMap);
            List lst = ClientUtil.executeQuery("getExistanceOfSMSProdId", map);
            if (lst != null && lst.size() > 0 && CommonUtil.convertObjToInt(lst.get(0)) > 0) {
                return true;
            }
        }
        return false;
    }

    public void populateOB(HashMap whereMap) {
        try {
            HashMap resultMap = proxy.executeQuery(whereMap, operationMap);
            System.out.println("resultMap : " + resultMap);
            populateData(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateData(HashMap dataMap) {
        if (dataMap.containsKey("SMSParameterTO") && dataMap.get("SMSParameterTO") != null) {
            setSMSParameter((SMSParameterTO) dataMap.get("SMSParameterTO"));
            ttNotifyObservers();
        }
    }

    public void setSelectAll(CTable table, Boolean selected) {
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            table.setValueAt(selected, i, 0);
            if(CommonUtil.convertObjToStr(table.getValueAt(i, 2)).length() == 0){
                table.setValueAt(new Boolean(false), i, 0);
            }
        }
    }

    public void resetTableValues() {
        tblData.setDataArrayList(null, tableTitle);
    }

    private void setSMSParameter(SMSParameterTO objSMSParameterTO) {
        getCbmProdType().setKeyForSelected(objSMSParameterTO.getProdType());
        getCbmProdId().setKeyForSelected(objSMSParameterTO.getProdId());
        setChkDebitCash(objSMSParameterTO.getDrCash().equals("Y"));
        setChkCreditCash(objSMSParameterTO.getCrCash().equals("Y"));
        setChkDebitTransfer(objSMSParameterTO.getDrTransfer().equals("Y"));
        setChkCreditTransfer(objSMSParameterTO.getCrTransfer().equals("Y"));
        setChkDebitClearing(objSMSParameterTO.getDrClearing().equals("Y"));
        setChkCreditClearing(objSMSParameterTO.getCrClearing().equals("Y"));

        setTxtDebitCashAmt(CommonUtil.convertObjToStr(objSMSParameterTO.getDrCashAmt()));
        setTxtCreditCashAmt(CommonUtil.convertObjToStr(objSMSParameterTO.getCrCashAmt()));
        setTxtDebitTransferAmt(CommonUtil.convertObjToStr(objSMSParameterTO.getDrTransferAmt()));
        setTxtCreditTransferAmt(CommonUtil.convertObjToStr(objSMSParameterTO.getCrTransferAmt()));
        setTxtDebitClearingAmt(CommonUtil.convertObjToStr(objSMSParameterTO.getDrClearingAmt()));
        setTxtCreditClearingAmt(CommonUtil.convertObjToStr(objSMSParameterTO.getCrClearingAmt()));

        setTxtRemarks(objSMSParameterTO.getRemarks());

        setRdoReminderYes(objSMSParameterTO.getReminder().equals("Y")); // Added by nithya
        setRdoReminderNo(objSMSParameterTO.getReminder().equals("N"));  // Added by nithya
        setRdoTxnAllowedYes(objSMSParameterTO.getTxnAllowed().equals("Y"));
        setRdoTxnAllowedNo(objSMSParameterTO.getTxnAllowed().equals("N"));
//        setTxtTxnAllowed(objSMSParameterTO.getTxnAllowed());  
//        setTxtCreatedDt (DateUtil.getStringDate (objSMSParameterTO.getCreatedDt ()));
//        setTxtCreatedBy (objSMSParameterTO.getCreatedBy ());
//        setTxtStatusDt (DateUtil.getStringDate (objSMSParameterTO.getStatusDt ()));
//        setTxtStatusBy (objSMSParameterTO.getStatusBy ());
//        setTxtAuthorizeStatus (objSMSParameterTO.getAuthorizeStatus ());
//        setTxtAuthorizedBy (objSMSParameterTO.getAuthorizedBy ());
//        setTxtAuthorizedDt (DateUtil.getStringDate (objSMSParameterTO.getAuthorizedDt ()));
    }

    public void insertTableData(HashMap whereMap) {
        try {
            ArrayList tableList = new ArrayList();
            HashMap custMap = new HashMap();
            List lst = null;
//            if (whereMap.containsKey("DEPOSIT_INTEREST_SCREEN")) {
//            custMap.put("SCREEN_NAME", "DEPOSIT_INTEREST_SCREEN");
//                custMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtTransDate));
            String prodType = CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE"));
            if(prodType.equalsIgnoreCase("SHARE")){
              lst = ClientUtil.executeQuery("getSelectShareTypeListofAccts", whereMap);
            }else if(prodType.equalsIgnoreCase("OA")){
              lst = ClientUtil.executeQuery("getSelectOATypeListofAccts", whereMap); 
            }else if(prodType.equalsIgnoreCase("MDS")){
              lst = ClientUtil.executeQuery("getSelectMDSTypeListofAccts", whereMap);
            }else if(prodType.equalsIgnoreCase("TD")){
              lst = ClientUtil.executeQuery("getSelectTDTypeListofAccts", whereMap);
            }else if(prodType.equalsIgnoreCase("TL")){
              lst = ClientUtil.executeQuery("getSelectTLTypeListofAccts", whereMap);
            }
            //lst = ClientUtil.executeQuery("getSelectShareTypeListofAccts", whereMap);
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    ArrayList rowList = new ArrayList();
                    HashMap multipletoSingleMap = (HashMap) lst.get(i);
                    rowList.add(new Boolean(false));
                    rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("SHARE_ACCT_NO")));
                    rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("MOBILE_NO")));
                    rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("CUST_NAME")));
//                    rowList.add(CommonUtil.convertObjToStr(multipletoSingleMap.get("STATUS")));
//                        rowList.add("Not Completed");
                    tableList.add(rowList);
                }
                setFinalList(tableList);
            }
//            }
            lst = null;
            custMap = null;
            System.out.println("#$# tableList:" + tableList);
            tblData = new EnhancedTableModel((ArrayList) tableList, tableTitle);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("getKeyValue");

        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    // Setter method for cboProdId
    void setCboProdId(String cboProdId) {
        this.cboProdId = cboProdId;
        setChanged();
    }
    // Getter method for cboProdId

    String getCboProdId() {
        return this.cboProdId;
    }

    // Setter method for chkDebitClearing
    void setChkDebitClearing(boolean chkDebitClearing) {
        this.chkDebitClearing = chkDebitClearing;
        setChanged();
    }
    // Getter method for chkDebitClearing

    boolean getChkDebitClearing() {
        return this.chkDebitClearing;
    }

    // Setter method for chkDebitTransfer
    void setChkDebitTransfer(boolean chkDebitTransfer) {
        this.chkDebitTransfer = chkDebitTransfer;
        setChanged();
    }
    // Getter method for chkDebitTransfer

    boolean getChkDebitTransfer() {
        return this.chkDebitTransfer;
    }

    // Setter method for chkDebitCash
    void setChkDebitCash(boolean chkDebitCash) {
        this.chkDebitCash = chkDebitCash;
        setChanged();
    }
    // Getter method for chkDebitCash

    boolean getChkDebitCash() {
        return this.chkDebitCash;
    }

    // Setter method for chkCreditClearing
    void setChkCreditClearing(boolean chkCreditClearing) {
        this.chkCreditClearing = chkCreditClearing;
        setChanged();
    }
    // Getter method for chkCreditClearing

    boolean getChkCreditClearing() {
        return this.chkCreditClearing;
    }

    // Setter method for chkCreditTransfer
    void setChkCreditTransfer(boolean chkCreditTransfer) {
        this.chkCreditTransfer = chkCreditTransfer;
        setChanged();
    }
    // Getter method for chkCreditTransfer

    boolean getChkCreditTransfer() {
        return this.chkCreditTransfer;
    }

    // Setter method for chkCreditCash
    void setChkCreditCash(boolean chkCreditCash) {
        this.chkCreditCash = chkCreditCash;
        setChanged();
    }
    // Getter method for chkCreditCash

    boolean getChkCreditCash() {
        return this.chkCreditCash;
    }

    // Setter method for cboProdType
    void setCboProdType(String cboProdType) {
        this.cboProdType = cboProdType;
        setChanged();
    }
    // Getter method for cboProdType

    String getCboProdType() {
        return this.cboProdType;
    }

    // Setter method for txtDebitTransferAmt
    void setTxtDebitTransferAmt(String txtDebitTransferAmt) {
        this.txtDebitTransferAmt = txtDebitTransferAmt;
        setChanged();
    }
    // Getter method for txtDebitTransferAmt

    String getTxtDebitTransferAmt() {
        return this.txtDebitTransferAmt;
    }

    // Setter method for txtCreditClearingAmt
    void setTxtCreditClearingAmt(String txtCreditClearingAmt) {
        this.txtCreditClearingAmt = txtCreditClearingAmt;
        setChanged();
    }
    // Getter method for txtCreditClearingAmt

    String getTxtCreditClearingAmt() {
        return this.txtCreditClearingAmt;
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

    // Setter method for txtDebitCashAmt
    void setTxtDebitCashAmt(String txtDebitCashAmt) {
        this.txtDebitCashAmt = txtDebitCashAmt;
        setChanged();
    }
    // Getter method for txtDebitCashAmt

    String getTxtDebitCashAmt() {
        return this.txtDebitCashAmt;
    }

    // Setter method for txtCreditTransferAmt
    void setTxtCreditTransferAmt(String txtCreditTransferAmt) {
        this.txtCreditTransferAmt = txtCreditTransferAmt;
        setChanged();
    }
    // Getter method for txtCreditTransferAmt

    String getTxtCreditTransferAmt() {
        return this.txtCreditTransferAmt;
    }

    // Setter method for txtDebitClearingAmt
    void setTxtDebitClearingAmt(String txtDebitClearingAmt) {
        this.txtDebitClearingAmt = txtDebitClearingAmt;
        setChanged();
    }
    // Getter method for txtDebitClearingAmt

    String getTxtDebitClearingAmt() {
        return this.txtDebitClearingAmt;
    }

    // Setter method for txtCreditCashAmt
    void setTxtCreditCashAmt(String txtCreditCashAmt) {
        this.txtCreditCashAmt = txtCreditCashAmt;
        setChanged();
    }
    // Getter method for txtCreditCashAmt

    String getTxtCreditCashAmt() {
        return this.txtCreditCashAmt;
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
     * Getter for property cbmProdType.
     *
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    /**
     * Setter for property cbmProdType.
     *
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    /**
     * Getter for property cbmProdId.
     *
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    /**
     * Setter for property cbmProdId.
     *
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    /**
     * Getter for property authorizeMap.
     *
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    /**
     * Setter for property authorizeMap.
     *
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
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
        setChanged();
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
        setChanged();
    }

    /**
     * Getter for property cboShareType.
     *
     * @return Value of property cboShareType.
     */
    public java.lang.String getCboShareType() {
        return cboShareType;
    }

    /**
     * Setter for property cboShareType.
     *
     * @param cboShareType New value of property cboShareType.
     */
    public void setCboShareType(java.lang.String cboShareType) {
        this.cboShareType = cboShareType;
    }

    /**
     * Getter for property cbmShareType.
     *
     * @return Value of property cbmShareType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmShareType() {
        return cbmShareType;
    }

    /**
     * Setter for property cbmShareType.
     *
     * @param cbmShareType New value of property cbmShareType.
     */
    public void setCbmShareType(com.see.truetransact.clientutil.ComboBoxModel cbmShareType) {
        this.cbmShareType = cbmShareType;
    }

    /**
     * Getter for property finalList.
     *
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }

    /**
     * Setter for property finalList.
     *
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }

    /**
     * Getter for property tblData.
     *
     * @return Value of property tblData.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblData() {
        return tblData;
    }

    /**
     * Setter for property tblData.
     *
     * @param tblData New value of property tblData.
     */
    public void setTblDdata(com.see.truetransact.clientutil.EnhancedTableModel tblData) {
        this.tblData = tblData;
    }

    /**
     * Getter for property tableTitle.
     *
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }

    public ComboBoxModel getCbmBranch() {
        return cbmBranch;
    }

    public void setCbmBranch(ComboBoxModel cbmBranch) {
        this.cbmBranch = cbmBranch;
    }

    public ComboBoxModel getCbmSMSProducts() {
        return cbmSMSProducts;
    }

    public void setCbmSMSProducts(ComboBoxModel cbmSMSProducts) {
        this.cbmSMSProducts = cbmSMSProducts;
    }

    public String getCboBranch() {
        return cboBranch;
    }

    public void setCboBranch(String cboBranch) {
        this.cboBranch = cboBranch;
    }

    public String getCboSMSProducts() {
        return cboSMSProducts;
    }

    public void setCboSMSProducts(String cboSMSProducts) {
        this.cboSMSProducts = cboSMSProducts;
    }
    
      public boolean populateShareTypeCombo(String prodType,String selectedBranch) {
        boolean dataExists = false;
        List prodLst = new ArrayList();
        HashMap prodMap = new HashMap();
        LookUpTO objLookUpTO = new LookUpTO();
        prodMap.put("PROD_TYPE", prodType);
        if(selectedBranch.length() > 0){
          prodMap.put(CommonConstants.BRANCH_ID,selectedBranch);
        }
        if(prodType.equalsIgnoreCase("OA")){
          prodLst = ClientUtil.executeQuery("getAccProducts", prodMap);
        }else if(prodType.equalsIgnoreCase("TL")){
          prodLst = ClientUtil.executeQuery("getLoanProducts", prodMap);
        }else if(prodType.equalsIgnoreCase("MDS")){
          prodLst = ClientUtil.executeQuery("getBranchwiseMdsSchems", prodMap);
        }else if(prodType.equalsIgnoreCase("SHARE")){
          prodLst = ClientUtil.executeQuery("getShareType", prodMap);
        }else if(prodType.equalsIgnoreCase("TD")) {
          prodLst = ClientUtil.executeQuery("deposit_getProdId", prodMap);  
        }
        key = new ArrayList();
        value = new ArrayList();
        if (prodLst != null && prodLst.size() > 0) {
            dataExists = true;
            for (int i = 0; i < prodLst.size(); i++) {
                objLookUpTO = (LookUpTO) prodLst.get(i);
                System.out.println("objLookUpTO..... " + objLookUpTO);
                String key1 = objLookUpTO.getLookUpRefID();
                String val1 = objLookUpTO.getLookUpDesc();
                cbmShareType = new ComboBoxModel(key, value);
                if (i == 0) {
                    cbmShareType.addKeyAndElement("", "");
                }
                cbmShareType.addKeyAndElement(key1, val1);
            }
        }
        return dataExists;
    }
    
}