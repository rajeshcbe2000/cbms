/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RtgsTransactionDetailsOB.java
 *
 * Created on October 26th, 2015, 03:40 PM
 */
package com.see.truetransact.ui.remittance.rtgs;

/**
 *
 * @author Suresh R
 */

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import javax.swing.DefaultListModel;
/**
 *
 * @author Suresh R
 */
public class RtgsTransactionDetailsOB extends CObservable {

    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblRTGSDetails;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(RtgsTransactionDetailsOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private ComboBoxModel cbmProdType = null;
    private ComboBoxModel cbmProdId = null;
    private ComboBoxModel cbmFileStatus = null;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private List finalList = null;

    public RtgsTransactionDetailsOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "RtgsTransactionDetailsJNDI");
            map.put(CommonConstants.HOME, "RtgsTransactionDetailsHome");
            map.put(CommonConstants.REMOTE, "RtgsTransactionDetails");
            setTableTitle();
            fillDropdown();
            tblRTGSDetails = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setTableTitle() {
        tableTitle.add("TYPE");
        tableTitle.add("RTGS_ID");
        tableTitle.add("BATCH_ID");
        tableTitle.add("BATCH_DT");
        tableTitle.add("PROD_ID");
        tableTitle.add("SENDER IFSC_CODE");
        tableTitle.add("SENDER ACT_NO");
        tableTitle.add("SENDER NAME");
        tableTitle.add("AMOUNT");
        tableTitle.add("BENEF_BANK_CODE");
        tableTitle.add("BENEF_BRANCH_CODE");
        tableTitle.add("BENEF_IFSC_CODE");
        tableTitle.add("BENEF_ACT_NO");
        tableTitle.add("BENEFICIARY_NAME");
        tableTitle.add("UTR NUMBER");
        tableTitle.add("SEQ NUMBER");
        tableTitle.add("PROCESS_STATUS");
        tableTitle.add("STATUS_DATE");
        tableTitle.add("INWARD_FAIL_ACK_UTR");
        tableTitle.add("F27_STATUS_DT");
        tableTitle.add("N09_STATUS_DT");
        tableTitle.add("N10_STATUS_DT");
        IncVal = new ArrayList();
    }

    private void fillDropdown() throws Exception {
        try {
            HashMap lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PRODUCTTYPE");
            lookup_keys.add("RTGS_FILE_STATUS");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdType = new ComboBoxModel(key, value);
            cbmProdType.removeKeyAndElement("GL");
            cbmProdType.removeKeyAndElement("SA");
            cbmProdId = new ComboBoxModel();
            getKeyValue((HashMap) keyValue.get("RTGS_FILE_STATUS"));
            cbmFileStatus = new ComboBoxModel(key, value);
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
    /**
     * To perform the necessary operation
     */
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

    /**
     * To perform the necessary action
     */
    private void doActionPerform(List finalTableList) throws Exception {
        final HashMap data = new HashMap();
        
        System.out.println("Data in RTGS Transaction Details OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }

    public void resetForm() {
        resetTableValues();
        setChanged();
    }

    public void resetTableValues() {
        tblRTGSDetails.setDataArrayList(null, tableTitle);
    }

    public com.see.truetransact.clientutil.EnhancedTableModel getTblRTGSDetails() {
        return tblRTGSDetails;
    }

    public void setTblRTGSDetails(com.see.truetransact.clientutil.EnhancedTableModel tblRTGSDetails) {
        this.tblRTGSDetails = tblRTGSDetails;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }

    public ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    public void setCbmProdId(ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    public ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }

    public List getFinalList() {
        return finalList;
    }

    public void setFinalList(List finalList) {
        this.finalList = finalList;
    }

    public ComboBoxModel getCbmFileStatus() {
        return cbmFileStatus;
    }

    public void setCbmFileStatus(ComboBoxModel cbmFileStatus) {
        this.cbmFileStatus = cbmFileStatus;
    }

    public void setCbmProdId(String prodType) {
        try {
            if (!prodType.equals("")) {
                HashMap lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cbmProdId = new ComboBoxModel(key, value);
        setChanged();
    }

    public void displayTableData(HashMap whereMap) {
        List transList = ClientUtil.executeQuery("getRTGSTransactionDetails", whereMap);
        if (transList != null && transList.size() > 0) {
            List rowLst = new ArrayList();
            List tableList = new ArrayList();
            for (int i = 0; i < transList.size(); i++) {
                rowLst = new ArrayList();
                whereMap = new HashMap();
                whereMap = (HashMap) transList.get(i);
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("TYPE")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("RTGS_ID")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("BATCH_ID")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("BATCH_DT")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("PROD_ID")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("SENDER_IFSC_CODE")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("SENDER_ACT_NO")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("SENDER_NAME")));
                //rowLst.add(String.valueOf(CommonUtil.convertObjToDouble(whereMap.get("AMOUNT"))));
                rowLst.add(String.valueOf(ClientUtil.convertObjToCurrency(String.valueOf(whereMap.get("AMOUNT")))));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("BENEFICIARY_BANK_CODE")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("BENEFICIARY_BRANCH_CODE")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("BENEFICIARY_IFSC_CODE")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("BENEFICIARY_ACCT_NO")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("BENEFICIARY_NAME")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("UTR_NUMBER")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("SEQUNCE_NO")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("PROCESS_STATUS")));
                rowLst.add(String.valueOf(whereMap.get("STATUS_DATE")));
                rowLst.add(CommonUtil.convertObjToStr(whereMap.get("INWARD_FAIL_ACK_UTR")));
                rowLst.add(String.valueOf(whereMap.get("F27_STATUS_DT")));
                rowLst.add(String.valueOf(whereMap.get("N09_STATUS_DT")));
                rowLst.add(String.valueOf(whereMap.get("N10_STATUS_DT")));
                tableList.add(rowLst);
            }
            setFinalList(tableList);
        }else{
            ClientUtil.showMessageWindow("No Data for Selected Filter !!!");
        }
    }
}

