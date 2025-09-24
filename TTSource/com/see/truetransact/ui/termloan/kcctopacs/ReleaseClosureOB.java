/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReleaseClosureOB.java
 * 
 * Created on Fri Feb 15 13:45:38 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.termloan.GoldLoanOB;
import com.see.truetransact.uicomponent.CObservable;
import java.util.*;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import javax.swing.event.TableModelEvent;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientConstants;

/**
 *
 * @author
 */
public class ReleaseClosureOB extends CObservable {

    private String cboKCCProdId = "";
    private ComboBoxModel cbmKCCProdId = null;
    private String txtAccNo = "";
    private String lblAccNameValue = "";
    private String txtFromReleaseNo = "";
    private String txtToReleaseNo = "";
    private String closeNumber = "";
    private HashMap operationMap;
    private Date curDate;
    private ProxyFactory proxy = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(ReleaseClosureOB.class);
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private EnhancedTableModel tblReleaseDet;
    private List finalList = null;
    final ArrayList tableTitle = new ArrayList();
    private HashMap _authorizeMap;
    private int result;
    private static ReleaseClosureOB ClosureOB;

    static {
        try {
            ClosureOB = new ReleaseClosureOB();
        } catch (Exception e) {
        }
    }

    public static ReleaseClosureOB getInstance() {
        return ClosureOB;
    }
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];

    public ReleaseClosureOB() {
        try {
            setOperationMap();
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            ReleaseClosureOB();
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
            log.info("ReleaseClosureOB..." + e);
        }
    }

    private void ReleaseClosureOB() throws Exception {
        setTableTile();
        tblReleaseDet = new EnhancedTableModel(null, tableTitle);
        fillDropdown();
        notifyObservers();
    }

    public void setTableTile() {
        tableTitle.add("Select");
        tableTitle.add("Release No");
        tableTitle.add("Release Date");
        tableTitle.add("Release Amount");
    }

    public void resetReleaseTableValues() {
        tblReleaseDet.setDataArrayList(null, tableTitle);
    }

    /**
     * To populate the appropriate keys and values of all the combo boxes in the
     * screen at the time of TermLoanOB instance creation
     *
     * @throws Exception will throw it to the TermLoanOB constructor
     */
    public void fillDropdown() throws Exception {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);

        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProductAD");
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("DATA"));
        cbmKCCProdId = new ComboBoxModel(key, value);
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
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

    public void insertTableData(HashMap whereMap) {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();            
            List releaseList = ClientUtil.executeQuery("displyReleaseDetails", whereMap);
            System.out.println("#$@$@$#@$@#$@ List : " + releaseList);
            if (releaseList != null && releaseList.size() > 0) {
                for (int i = 0; i < releaseList.size(); i++) {
                    dataMap = (HashMap) releaseList.get(i);
                    rowList = new ArrayList();
                    rowList.add(new Boolean(false));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("RELEASE_NO")));
                    rowList.add(dataMap.get("RELEASE_DATE"));
                    rowList.add(dataMap.get("RELEASE_AMOUNT"));
                    tableList.add(rowList);
                }
                setFinalList(releaseList);
                tblReleaseDet = new EnhancedTableModel((ArrayList) tableList, tableTitle);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ReleaseClosureJNDI");
        operationMap.put(CommonConstants.HOME, "ReleaseClosureHome");
        operationMap.put(CommonConstants.REMOTE, "ReleaseClosure");
    }

    public String getCloseNumber() {
        return closeNumber;
    }

    public void setCloseNumber(String closeNumber) {
        this.closeNumber = closeNumber;
    }

    public java.util.List getFinalList() {
        return finalList;
    }

    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }

    public String getTxtFromReleaseNo() {
        return txtFromReleaseNo;
    }

    public void setTxtFromReleaseNo(String txtFromReleaseNo) {
        this.txtFromReleaseNo = txtFromReleaseNo;
    }

    public String getTxtToReleaseNo() {
        return txtToReleaseNo;
    }

    public void setTxtToReleaseNo(String txtToReleaseNo) {
        this.txtToReleaseNo = txtToReleaseNo;
    }

    public EnhancedTableModel getTblReleaseDet() {
        return tblReleaseDet;
    }

    public void setTblReleaseDet(EnhancedTableModel tblReleaseDet) {
        this.tblReleaseDet = tblReleaseDet;
    }

    public ComboBoxModel getCbmKCCProdId() {
        return cbmKCCProdId;
    }

    public void setCbmKCCProdId(ComboBoxModel cbmKCCProdId) {
        this.cbmKCCProdId = cbmKCCProdId;
    }

    public String getCboKCCProdId() {
        return cboKCCProdId;
    }

    public void setCboKCCProdId(String cboKCCProdId) {
        this.cboKCCProdId = cboKCCProdId;
    }

    public String getLblAccNameValue() {
        return lblAccNameValue;
    }

    public void setLblAccNameValue(String lblAccNameValue) {
        this.lblAccNameValue = lblAccNameValue;
    }

    public String getTxtAccNo() {
        return txtAccNo;
    }

    public void setTxtAccNo(String txtAccNo) {
        this.txtAccNo = txtAccNo;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    /**
     * It will call the notifyObservers()
     */
    public void ttNotifyObservers() {
        notifyObservers();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    public void resetForm() {
        setTxtAccNo("");
        setTxtFromReleaseNo("");
        setTxtToReleaseNo("");
        setCboKCCProdId("");
        setLblAccNameValue("");
        resetReleaseTableValues();
    }

    public void resetProdid() {
        setTxtAccNo("");
        setTxtFromReleaseNo("");
        setTxtToReleaseNo("");
        setLblAccNameValue("");
        resetReleaseTableValues();
    }

    public void resetAccNum() {
        setTxtFromReleaseNo("");
        setTxtToReleaseNo("");
        resetReleaseTableValues();
    }

    public void resetFromRelNum() {
        setTxtToReleaseNo("");
        resetReleaseTableValues();
    }

    public void resetToRelNum() {
        resetReleaseTableValues();
    }

    public void displayAccNumberProdId(HashMap Map) {
        List accountLst = (List) ClientUtil.executeQuery("getAccNumFromReleaseNo", Map);
        if (accountLst != null && accountLst.size() > 0) {
            Map = (HashMap) accountLst.get(0);
            setTxtAccNo((CommonUtil.convertObjToStr(Map.get("KCC_ACT_NUM"))));
            setCboKCCProdId((String) getCbmKCCProdId().getDataForKey(CommonUtil.convertObjToStr(Map.get("KCC_PROD_ID"))));
        }
    }

    public void doAction(List finalTableList) {
        TTException exception = null;
        log.info("In doAction()");
        try {
            doActionPerform(finalTableList);
        } catch (Exception e) {
            System.out.println("##$$$##$#$#$#$# Exception e : " + e);
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            parseException.logException(exception, true);
            setResult(actionType);
        }
    }

    private void doActionPerform(List finalTableList) throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (get_authorizeMap() == null) {
            if (finalTableList != null && finalTableList.size() > 0) {
                data.put("RELEASE_CLOSING_DETAILS", finalTableList);
            }
        } else {
            if (finalTableList != null && finalTableList.size() > 0) {
                data.put("RELEASE_CLOSING_AUTH_DETAILS", finalTableList);
            }
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        data.put("STATUS_BY", TrueTransactMain.USER_ID);
        System.out.println("Data in ReleaseClosure OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }

    public void getData(HashMap whereMap) {
        try {
            System.out.println("%%%%%%%%%%%%whereMap" + whereMap);
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, operationMap);
            System.out.println("#@@%@@#%@#data" + data);
            if (data.containsKey("REL_CLOSED_LIST")) {
                ArrayList rowList = new ArrayList();
                ArrayList tableList = new ArrayList();                
                HashMap dataMap = new HashMap();
                List closedList = (List) data.get("REL_CLOSED_LIST");
                System.out.println("^^^^^^^^closedList" + closedList);
                if (closedList != null && closedList.size() > 0) {
                    for (int i = 0; i < closedList.size(); i++) {
                        dataMap = (HashMap) closedList.get(i);
                        rowList = new ArrayList();
                        rowList.add(new Boolean(true));
                        rowList.add(CommonUtil.convertObjToStr(dataMap.get("RELEASE_NO")));
                        rowList.add(dataMap.get("RELEASE_DATE"));
                        rowList.add(dataMap.get("RELEASE_AMOUNT"));
                        tableList.add(rowList);
                        if (i == 0) {                            
                            setTxtFromReleaseNo(CommonUtil.convertObjToStr(dataMap.get("RELEASE_NO")));
                        }
                        if (i == closedList.size() - 1) {                            
                            setTxtToReleaseNo(CommonUtil.convertObjToStr(dataMap.get("RELEASE_NO")));
                        }
                    }
                    setFinalList(closedList);
                    tblReleaseDet = new EnhancedTableModel((ArrayList) tableList, tableTitle);                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
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

    private String getCommand() {
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
        return command;
    }
}
