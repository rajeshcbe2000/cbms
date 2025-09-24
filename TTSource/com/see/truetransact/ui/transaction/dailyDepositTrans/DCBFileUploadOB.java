/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * DCBFileUploadUI.java
 *
 * Created on Aug 10th, 2017, 11:23 AM
 *
 * @author Suresh R
 */
package com.see.truetransact.ui.transaction.dailyDepositTrans;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.TrueTransactMain;
import java.io.FileInputStream;

/**
 *
 * @author Suresh R
 */
public class DCBFileUploadOB extends CObservable {

    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblFileDataDetails, tblFileDownLoadDetails;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(DCBFileUploadOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private List editFileDataList = null;
    private String fileID ="";
    private String fileName ="";
     private String debitAccount ="";
     private String getSelectedTab = "";

    public DCBFileUploadOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "FileUploadJNDI");
            map.put(CommonConstants.HOME, "FileUploadHome");
            map.put(CommonConstants.REMOTE, "FileUpload");
            setTableTitle();
            fillDropdown();
            tblFileDataDetails = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setTableTitle() {
        tableTitle.add("SOC_BRACH_ID");
        tableTitle.add("TRANS_DT");
        tableTitle.add("TRANS_TIME");
        tableTitle.add("RECORD_NO");
        tableTitle.add("DR/CR");
        tableTitle.add("CTQ");
        tableTitle.add("TRANS_AMOUNT");
        tableTitle.add("SOC_AC_NUM");
        tableTitle.add("EDCB_AC_NUM");
        tableTitle.add("CHQ_NUM");
        tableTitle.add("CHQ_DT");
        tableTitle.add("REF_NUM");
        tableTitle.add("PARTICULARS");
        tableTitle.add("CLIENT_ID");
        tableTitle.add("USER");
        tableTitle.add("TRANS_STATUS");
        IncVal = new ArrayList();
    }

    private void fillDropdown() throws Exception {
        try {
            HashMap param = new HashMap();
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
    
    public void processTempAadhar(HashMap tempMap) {
        HashMap data = new HashMap();
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("AADHAAR_NO_LIST", tempMap);
            data = (HashMap) proxy.executeQuery(whereMap, map);
            setProxyReturnMap(data);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            //System.out.println("######### OB Data : " + data);
            if (data.containsKey("FILE_DATA")) {
                whereMap = new HashMap();
                editFileDataList = new ArrayList();
                List fileDataList = (List) data.get("FILE_DATA");
                //System.out.println("######## inwardDataList : "+inwardDataList);
                if (fileDataList != null && fileDataList.size() > 0) {
                    List rowLst = new ArrayList();
                    for (int i = 0; i < fileDataList.size(); i++) {
                        rowLst = new ArrayList();
                        whereMap = new HashMap();
                        whereMap = (HashMap) fileDataList.get(i);
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL1")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL2")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL3")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL4")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL5")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL6")));
                        rowLst.add(String.valueOf(ClientUtil.convertObjToCurrency(String.valueOf(whereMap.get("COL7")))));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL8")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL9")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL10")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL11")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL12")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL13")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL14")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL15")));
                        rowLst.add(CommonUtil.convertObjToStr(whereMap.get("COL16")));
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
    
    public void doProcess(List fileNameList) {
        HashMap data = new HashMap();
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("DOWN_LOAD_FILES", fileNameList);
            data = (HashMap) proxy.execute(whereMap, map);
            setProxyReturnMap(data);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    /**
     * To perform the necessary operation
     */
    public void doAction(List finalTableList) {
        TTException exception = null;
        try {
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL || getAuthorizeMap() != null) {
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
            if (getAuthorizeMap() == null) {
                if(getGetSelectedTab().equalsIgnoreCase("RTGS_FILE_UPLOAD"))
                {
                if (finalTableList != null && finalTableList.size() > 0) {
                    data.put("RTGS_FILE_DATA", finalTableList);
                    data.put("DEBIT_ACCOUNT", getDebitAccount());
                }
                }
                else{
                if (finalTableList != null && finalTableList.size() > 0) {
                    data.put("FILE_DATA", finalTableList);
                }
                }
            } else {
                data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                data.put("INWARD_ID", getFileID());
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                data.put("FILE_NAME", getFileName());
            }
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            HashMap proxyResultMap = proxy.execute(data, map);
            setProxyReturnMap(proxyResultMap);
            setResult(getActionType());
            _authorizeMap = null;
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
    
    private String getCommand(){
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
    
    private String getAction(){
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
        resetTableValues();
        fileID ="";
        fileName ="";
        editFileDataList = null;
        setChanged();
    }

    public void resetTableValues() {
        tblFileDataDetails.setDataArrayList(null, tableTitle);
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

    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public EnhancedTableModel getTblFileDownLoadDetails() {
        return tblFileDownLoadDetails;
    }

    public void setTblFileDownLoadDetails(EnhancedTableModel tblFileDownLoadDetails) {
        this.tblFileDownLoadDetails = tblFileDownLoadDetails;
    }

    public List getEditFileDataList() {
        return editFileDataList;
    }

    public void setEditFileDataList(List editFileDataList) {
        this.editFileDataList = editFileDataList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGetSelectedTab() {
        return getSelectedTab;
    }

    public void setGetSelectedTab(String getSelectedTab) {
        this.getSelectedTab = getSelectedTab;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }
    
    
    
}