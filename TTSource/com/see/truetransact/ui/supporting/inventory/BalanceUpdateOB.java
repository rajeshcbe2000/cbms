/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BalanceUpdateOB.java
 *
 * Created on March 17, 2014, 11:41 AM
 */
package com.see.truetransact.ui.supporting.inventory;

import com.see.truetransact.ui.supporting.inventory.BalanceUpdateUI.*;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.supporting.balanceupdate.BalanceUpdateTO;
import java.util.List;
import org.apache.log4j.Logger;

public class BalanceUpdateOB extends CObservable {

    private HashMap authMap = new HashMap();
    final ArrayList tableTitle = new ArrayList();
    private EnhancedTableModel tmbBalanceUpdate;
    private ArrayList columnList;
    private String selectedButton = "";
    private String cbobranch;
    private ComboBoxModel cbmbranch;
    private ComboBoxModel cbmAccountType;
    private ComboBoxModel cbmAccountHead;
    private int actionType;
    private Date tdtFromDate;
    private String cboFinalActType = " ";
    private String cboAccountHead = " ";
    private ProxyFactory proxy = null;
    private List finalList;
    private HashMap map;
    private HashMap operationMap;
    private int result;
    private HashMap dataMap = new HashMap();
    private HashMap balanceList = null;
    private List fullList = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(BalanceUpdateUI.class);
    HashMap proxyReturnMap = null;
    String frmDate = "";
    String frmFinalDate = "";
    int tabIndex = 0;
    String balSheetId = "";
    String deleteKey = "";
    String finalAccType = "";
    String selBranchId="";
    private EnhancedTableModel tblBalanceData ;
    private HashMap addMap = new HashMap();

    public HashMap getAddMap() {
        return addMap;
    }

    public void setAddMap(HashMap addMap) {
        this.addMap = addMap;
    }

    public EnhancedTableModel getTblBalanceData() {
        return tblBalanceData;
    }

    public void setTblBalanceData(EnhancedTableModel tblBalanceData) {
        this.tblBalanceData = tblBalanceData;
    }

    
        
    public String getSelBranchId() {
        return selBranchId;
    }

    public void setSelBranchId(String selBranchId) {
        this.selBranchId = selBranchId;
    }
    public String getFinalAccType() {
        return finalAccType;
    }

    public void setFinalAccType(String finalAccType) {
        this.finalAccType = finalAccType;
    }

    public String getDeleteKey() {
        return deleteKey;
    }

    public void setDeleteKey(String deleteKey) {
        this.deleteKey = deleteKey;
    }

    public String getBalSheetId() {
        return balSheetId;
    }

    public void setBalSheetId(String balSheetId) {
        this.balSheetId = balSheetId;
    }
    private double netProfit = 0;

    public double getNetLoss() {
        return netLoss;
    }

    public void setNetLoss(double netLoss) {
        this.netLoss = netLoss;
    }

    public double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(double netProfit) {
        this.netProfit = netProfit;
    }
    private double netLoss = 0;

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getFinalFrmDate() {
        return frmFinalDate;
    }

    public void setFinalFrmDate(String frmDate) {
        this.frmFinalDate = frmDate;
    }

    public String getFrmDate() {
        return frmDate;
    }

    public void setFrmDate(String frmDate) {
        this.frmDate = frmDate;
    }

    public ComboBoxModel getCbmAccountHead() {
        return cbmAccountHead;
    }

    public void setCbmAccountHead(ComboBoxModel cbmAccountHead) {
        this.cbmAccountHead = cbmAccountHead;
    }

    public ComboBoxModel getCbmAccountType() {
        return cbmAccountType;
    }

    public void setCbmAccountType(ComboBoxModel cbmAccountType) {
        this.cbmAccountType = cbmAccountType;
    }

    public ComboBoxModel getCbmbranch() {
        return cbmbranch;
    }

    public void setCbmbranch(ComboBoxModel cbmbranch) {
        this.cbmbranch = cbmbranch;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(String selectedButton) {
        this.selectedButton = selectedButton;
    }

    public String getCbobranch() {
        return cbobranch;
    }

    public void setCbobranch(String cbobranch) {
        this.cbobranch = cbobranch;
    }

    public String getCboAccountHead() {
        return cboAccountHead;
    }

    public void setCboAccountHead(String cboAccountHead) {
        this.cboAccountHead = cboAccountHead;
    }

    public String getCboFinalActType() {
        return cboFinalActType;
    }

    public void setCboFinalActType(String cboFinalActType) {
        this.cboFinalActType = cboFinalActType;
    }

    public EnhancedTableModel getTmbBalanceUpdate() {
        return tmbBalanceUpdate;
    }

    public void setTmbBalanceUpdate(EnhancedTableModel tmbBalanceUpdate) {
        this.tmbBalanceUpdate = tmbBalanceUpdate;
    }

    public Date getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(Date tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    public HashMap getAuthMap() {
        return authMap;
    }

    public void setAuthMap(HashMap authMap) {
        this.authMap = authMap;
    }

    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }

    public HashMap getBalanceList() {
        return balanceList;
    }

    public void setBalanceList(HashMap balanceList) {
        this.balanceList = balanceList;
    }

    public void setResultStatus() {
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public void updateInterestData() {
        tmbBalanceUpdate = new EnhancedTableModel((ArrayList) finalList, tableTitle);
    }

    public HashMap getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap dataMap) {
        this.dataMap = dataMap;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public java.util.HashMap getProxyReturnMap() {
        return proxyReturnMap;
    }

    public void setProxyReturnMap(java.util.HashMap proxyReturnMap) {
        this.proxyReturnMap = proxyReturnMap;
    }

    public BalanceUpdateOB() throws Exception {
        setOperationMap();
        proxy = ProxyFactory.createProxy();
        createTableModel();
        initUIComboBoxModel();
        createColumnList();
        tblBalanceData = new EnhancedTableModel();
    }

    private void createTableModel() throws Exception {
        tmbBalanceUpdate = new EnhancedTableModel(null, columnList);

    }

    public List getFullList() {
        return fullList;
    }

    public void setFullList(List fullList) {
        this.fullList = fullList;
    }

    private void createColumnList() {
        columnList = new ArrayList();
        columnList.add(tmbBalanceUpdate.getColumnName(1));
        columnList.add(tmbBalanceUpdate.getColumnName(2));
        columnList.add(tmbBalanceUpdate.getColumnName(3));

    }

    public void initUIComboBoxModel() {
        cbmAccountHead = new ComboBoxModel();
        cbmAccountType = new ComboBoxModel();
        cbmbranch = new ComboBoxModel();

    }

    public void resetForm() {
        setCbobranch("");
        ttNotifyObservers();
        setAddMap(null);
    }

    private void setOperationMap() throws Exception {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "BalanceUpdateJNDI");
        operationMap.put(CommonConstants.HOME, "balanceupdate.BalanceUpdateHome");
        operationMap.put(CommonConstants.REMOTE, "balanceupdate.BalanceUpdate");
    }

   public void addRecord() throws Exception {
        Object selectedRow;
        boolean rowExists = false;         
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add("MANUAL");
            tblBalanceData.insertRow(tblBalanceData.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }
          
    public void doActionPerform(HashMap dataMap1) throws Exception {
        TTException exception = null;
        HashMap proxyResultMap = new HashMap();
        HashMap term = new HashMap();
        System.out.println("dataMapdataMap" + dataMap);
        term.put("BALANCE_LIST", getBalanceList());
        term.put("BALANCEUPDATE", dataMap);
        if (getTabIndex() == 0) {
            term.put("DATE_BALANCE", getFrmDate());
        }
        if (getTabIndex() == 1) {
            term.put("DATE_BALANCE", getFinalFrmDate());
        }
        term.put("TAB_INDEX", getTabIndex());
        term.put("DELETE_KEY", getDeleteKey());
        term.put("FINAL_ACC_TYPE", getFinalAccType());
        term.put("SEL_BRANCH_ID",getSelBranchId());
        if (getTabIndex() == 1) {
            if (getNetLoss() > 0) {
                term.put("NET_LOSS", getNetLoss());
            }
            if (getNetProfit() > 0) {
                term.put("NET_PROFIT", getNetProfit());
            }
        }
        String action = "";
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
        } else if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
        } else if (getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
        }
        //adde by sreekrishnan for manual entry
        if(getAddMap()!=null && getAddMap().size()>0){
            term.put("MANUAL_DATA", getAddMap());
        }
        term.put("COMMAND", action);
        System.out.println("term ---- " + term + " dataMap-----" + dataMap);
        proxyResultMap = proxy.execute(term, operationMap);

        System.out.println("dataMap data :" + dataMap1);
    }

    public void fillDropDown() {
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        HashMap mapShare = new HashMap();
        List keyValue = null;
        keyValue = ClientUtil.executeQuery("getbranchesForBalanceSheet", mapShare);
        key.add("");
        value.add("ALL");
        if (keyValue != null && keyValue.size() > 0) {
            for (int i = 0; i < keyValue.size(); i++) {
                mapShare = (HashMap) keyValue.get(i);
                key.add(mapShare.get("BRANCH_CODE"));
                value.add(mapShare.get("BRANCH_NAME"));
            }
        }
        cbmbranch = new ComboBoxModel(key, value);
        key = null;
        value = null;
        keyValue.clear();
        keyValue = null;
        mapShare.clear();
        mapShare = null;
    }

    public void doAction() {
        TTException exception = null;
        log.info("In doAction()");
        try {

            doActionPerform(dataMap);
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

    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("BALANCE_LIST", getBalanceList());
            if (!command.equals(CommonConstants.AUTHORIZESTATUS)) {
                term.put("BalanceUpdateTO", getbaBalanceUpdateTO(command));

            }
            System.out.println("getAuthMap 998====" + getAuthMap());
            if (getAuthMap() != null && getAuthMap().size() > 0) {
                if (getAuthMap() != null) {
                    term.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
                }
            }
            System.out.println("term 9sdfsa98====" + term);
            System.out.println("getBalanceList" + getBalanceList());

//            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //parseException.logException(e,true);
            System.out.println("Error in execute():" + e);
        }
    }

    private BalanceUpdateTO setBalanceUpdateTO(BalanceUpdateTO objTO) {

        // System.out.println("tblBalanceUpdate.getRowCount()"+tmbBalanceUpdate.getRowCount());
        objTO = new BalanceUpdateTO();
        objTO.setBranchcode(getCbobranch());
        objTO.setFrmdate(getTdtFromDate());
        objTO.setSubacttype(getCboAccountHead());
        objTO.setFinalacttype(getCboFinalActType());

        System.out.println("dataMapv" + objTO);

        notifyObservers();
        return objTO;
    }

    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            BalanceUpdateTO objTO = (BalanceUpdateTO) ((List) mapData.get("BalanceUpdateTO")).get(0);
            setBalanceUpdateTO(objTO);
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.out.println("Error in populateData():" + e);
        }
    }

    private BalanceUpdateTO getbaBalanceUpdateTO(String command) {
        BalanceUpdateTO objTO = new BalanceUpdateTO();
        objTO.setCommand(command);
        objTO.setFrmdate(getTdtFromDate());
        objTO.setFinalacttype(getCboFinalActType());
        objTO.setBranchcode(getCbobranch());
        objTO.setSubacttype(getCboAccountHead());
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            objTO.setAuthorize("");
            objTO.setAuthorizedBy("");
            objTO.setAuthorizedDt(null);
            objTO.setStatus("CREATED");
        }

        return objTO;
    }
}
