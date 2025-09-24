/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentCommisionSlabRateOB.java
 *
 * Created on Wed Feb 02 12:57:50 IST 2005
 */
package com.see.truetransact.ui.agent.agentcommisionslabrate;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.transferobject.agent.AgentTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.agent.agentcommisionslabrate.AgentCommisionSlabRateTO;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import java.util.LinkedHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author
 */
public class AgentCommisionSlabRateOB extends CObservable {

    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ComboBoxModel cbmProductType;
    private ComboBoxModel cbmProdId;
    private String cboProductType;
    private String cboProdId;
    private String txtCommisionCreditedTo;
    private String txtCommisionCollectTo;
    private String txtActIntroCommisionHd;
    private String txtTdsHd;
    private String txtFromAmount;
    private String txtToAmount;
    private String txtCommFromBank;
    private String txtCommFromActHolder;
    private String txtActIntroCommisionAmt;
    private String txtTds;
    private String tdtFromDate;
    private String tdtToDate;
    private EnhancedTableModel tblAgentCommisionTab;
    private HashMap agentCommisionMap;
    private HashMap deletedMap;
    ArrayList agentTabTitle = new ArrayList();
    ArrayList agentTabCommisionDetails = new ArrayList();
    final ArrayList tableTitle = new ArrayList();
    private boolean newData = false;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(AgentCommisionSlabRateUI.class);
    private ProxyFactory proxy = null;
    private Date curDate = null;
    private final String BRANCH = TrueTransactMain.BRANCH_ID;
    private int selectedTab = 0;
    AgentCommisionSlabRateOB agentCommisionSlabRateOB;
    private String lblCommCreditValue;
    private String lblCommCollectValue;
    private String lblActIntroValue;
    private String lblTdsValue;
    private int serialCount = 0;
    private String slabId = "";
    private HashMap authorizeMap;

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    public String getLblActIntroValue() {
        return lblActIntroValue;
    }

    public void setLblActIntroValue(String lblActIntroValue) {
        this.lblActIntroValue = lblActIntroValue;
    }

    public String getLblCommCollectValue() {
        return lblCommCollectValue;
    }

    public void setLblCommCollectValue(String lblCommCollectValue) {
        this.lblCommCollectValue = lblCommCollectValue;
    }

    public String getLblCommCreditValue() {
        return lblCommCreditValue;
    }

    public void setLblCommCreditValue(String lblCommCreditValue) {
        this.lblCommCreditValue = lblCommCreditValue;
    }

    public String getLblTdsValue() {
        return lblTdsValue;
    }

    public void setLblTdsValue(String lblTdsValue) {
        this.lblTdsValue = lblTdsValue;
    }

    public String getTxtCommFromActHolder() {
        return txtCommFromActHolder;
    }

    public void setTxtCommFromActHolder(String txtCommFromActHolder) {
        this.txtCommFromActHolder = txtCommFromActHolder;
    }

    public String getTxtCommFromBank() {
        return txtCommFromBank;
    }

    public void setTxtCommFromBank(String txtCommFromBank) {
        this.txtCommFromBank = txtCommFromBank;
    }

    public ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    public void setCbmProdId(ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    public ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }

    public void setCbmProductType(ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }

    public String getCboProdId() {
        return cboProdId;
    }

    public void setCboProdId(String cboProdId) {
        this.cboProdId = cboProdId;
    }

    public String getCboProductType() {
        return cboProductType;
    }

    public void setCboProductType(String cboProductType) {
        this.cboProductType = cboProductType;
    }

    public String getTxtActIntroCommisionAmt() {
        return txtActIntroCommisionAmt;
    }

    public void setTxtActIntroCommisionAmt(String txtActIntroCommisionAmt) {
        this.txtActIntroCommisionAmt = txtActIntroCommisionAmt;
    }

    public String getTxtActIntroCommisionHd() {
        return txtActIntroCommisionHd;
    }

    public void setTxtActIntroCommisionHd(String txtActIntroCommisionHd) {
        this.txtActIntroCommisionHd = txtActIntroCommisionHd;
    }

    public String getTxtCommisionCollectTo() {
        return txtCommisionCollectTo;
    }

    public void setTxtCommisionCollectTo(String txtCommisionCollectTo) {
        this.txtCommisionCollectTo = txtCommisionCollectTo;
    }

    public String getTxtCommisionCreditedTo() {
        return txtCommisionCreditedTo;
    }

    public void setTxtCommisionCreditedTo(String txtCommisionCreditedTo) {
        this.txtCommisionCreditedTo = txtCommisionCreditedTo;
    }

    public String getTxtFromAmount() {
        return txtFromAmount;
    }

    public void setTxtFromAmount(String txtFromAmount) {
        this.txtFromAmount = txtFromAmount;
    }

    public String getTxtTds() {
        return txtTds;
    }

    public void setTxtTds(String txtTds) {
        this.txtTds = txtTds;
    }

    public String getTxtTdsHd() {
        return txtTdsHd;
    }

    public void setTxtTdsHd(String txtTdsHd) {
        this.txtTdsHd = txtTdsHd;
    }

    public String getTxtToAmount() {
        return txtToAmount;
    }

    public void setTxtToAmount(String txtToAmount) {
        this.txtToAmount = txtToAmount;
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    /**
     * Creates a new instance of InwardClearingOB
     */
    public AgentCommisionSlabRateOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        cbmProductType = new ComboBoxModel();
        cbmProdId = new ComboBoxModel();
        initianSetup();
        fillDropdown();
        setTableAgentCommisionDetailsTile();
        tblAgentCommisionTab = new EnhancedTableModel(null, agentTabCommisionDetails);
    }

    private void setTableAgentCommisionDetailsTile() throws Exception {
        agentTabCommisionDetails.add("Comm From Bank");
        agentTabCommisionDetails.add("Comm From Act Holder");
        agentTabCommisionDetails.add("Act Intro Comm");
        agentTabCommisionDetails.add("Tds %");
        agentTabCommisionDetails.add("From Dt");
        agentTabCommisionDetails.add("To Dt");
        agentTabCommisionDetails.add("From Amt");
        agentTabCommisionDetails.add("To Amt");
        agentTabCommisionDetails.add("Status");
        agentTabCommisionDetails.add("Authorize Status");
    }

    private void initianSetup() throws Exception {
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
            //e.printStackTrace();
        }
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "AgentCommisionSlabRateJNDI");
        operationMap.put(CommonConstants.HOME, "agent.agentcommisionslabrate.AgentCommisionSlabRateJNDI");
        operationMap.put(CommonConstants.REMOTE, "agent.agentcommisionslabrate.AgentCommisionSlabRate");
    }

    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch (Exception e) {
            System.out.println("Error In populateData()");
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private void populateOB(HashMap mapData) throws Exception {
        //Taking the Value of Prod_Id from each Table...
        if (mapData.containsKey("AGENT_COMMISSION_SLAB")) {
            agentCommisionMap = (LinkedHashMap) mapData.get("AGENT_COMMISSION_SLAB");
            populateAgentCommisionTable();
        }
        ttNotifyObservers();
    }

    private void populateAgentCommisionTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        HashMap replacingMap = new LinkedHashMap();
        replacingMap = agentCommisionMap;
        if (replacingMap != null) {
            incDataList = new ArrayList(replacingMap.keySet());
            ArrayList addList = new ArrayList(replacingMap.keySet());
            int length = incDataList.size();
            agentCommisionMap = new LinkedHashMap();
            for (int i = 0; i < length; i++) {
                ArrayList incTabRow = new ArrayList();
                AgentCommisionSlabRateTO agentCommisionSlabRateTO = (AgentCommisionSlabRateTO) replacingMap.get(addList.get(i));
                System.out.println("populateAgentCommisionTable agentCommisionSlabRateTO : " + agentCommisionSlabRateTO);
                if (agentCommisionSlabRateTO != null) {
                    incTabRow.add(CommonUtil.convertObjToDouble(agentCommisionSlabRateTO.getCommperBank()));
                    incTabRow.add(CommonUtil.convertObjToDouble(agentCommisionSlabRateTO.getCommperAcHoldr()));
                    incTabRow.add(CommonUtil.convertObjToDouble(agentCommisionSlabRateTO.getAcctIntroCommision()));
                    incTabRow.add(CommonUtil.convertObjToDouble(agentCommisionSlabRateTO.getTdsAmt()));
                    incTabRow.add(CommonUtil.convertObjToStr(DateUtil.getStringDate(agentCommisionSlabRateTO.getFromDate())));
                    if (agentCommisionSlabRateTO.getToDate() != null) {
                        incTabRow.add(CommonUtil.convertObjToStr(DateUtil.getStringDate(agentCommisionSlabRateTO.getToDate())));
                    } else {
                        incTabRow.add("");
                    }
                    incTabRow.add(CommonUtil.convertObjToStr(agentCommisionSlabRateTO.getFromAmt()));
                    incTabRow.add(CommonUtil.convertObjToStr(agentCommisionSlabRateTO.getToAmt()));
                    incTabRow.add(CommonUtil.convertObjToStr(agentCommisionSlabRateTO.getStatus()));
                    incTabRow.add(CommonUtil.convertObjToStr(agentCommisionSlabRateTO.getAuthorizeStatus()));
                    tblAgentCommisionTab.insertRow(i, incTabRow);
                    agentCommisionMap.put(i, agentCommisionSlabRateTO);
                }
            }
        }
        //notifyObservers();
    }

    public void resetProdTable() {
        tblAgentCommisionTab.setDataArrayList(null, agentTabCommisionDetails);
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
            } else {
                log.info("Action Type Not Defined In setChequeBookTO()");
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
    public void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        if (agentCommisionMap != null && agentCommisionMap.size() > 0) {
            data.put("COMMAND", getCommand());
            data.put("AGENT_COMMISION_DETAILS", agentCommisionMap);
        }
        if (deletedMap != null && deletedMap.size() > 0) {
            data.put("COMMAND", getCommand());
            data.put("AGENT_COMMISION_DELETED_DETAILS", deletedMap);
        }
        if (getAuthorizeMap() != null) {
            data.put("AGENT_AUTHORIZE_DETAILS", getAuthorizeMap());
        }
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        setResult(actionType);
        setProxyReturnMap(proxyResultMap);
        resetForm();
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

    public void resetAgentCommisionDetails() {
        setTxtCommFromBank("");
        setTxtCommFromActHolder("");
        setTxtFromAmount("");
        setTxtToAmount("");
        setTxtActIntroCommisionAmt("");
    }

    public void resetForm() {
        setTxtCommFromBank("");
        setTxtCommFromActHolder("");
        setTxtCommisionCreditedTo("");
        setTxtCommisionCollectTo("");
        setTxtActIntroCommisionHd("");
        setTxtTdsHd("");
        setTxtFromAmount("");
        setTxtToAmount("");
        setTxtActIntroCommisionAmt("");
        deletedMap = new LinkedHashMap();
        agentCommisionMap = new LinkedHashMap();
    }

    /*
     * Set and GET METHODS FOR THE tABLE...
     */
    void setTblAgentCommision(EnhancedTableModel tblAgentCommisionTab) {
        this.tblAgentCommisionTab = tblAgentCommisionTab;
        setChanged();
    }

    EnhancedTableModel getTblAgentCommision() {
        return this.tblAgentCommisionTab;
    }

    /**
     * TO RESET THE TABLE...
     */
    public void resetTable() {
        try {
            ArrayList data = tblAgentCommisionTab.getDataArrayList();
            for (int i = data.size(); i > 0; i--) {
                tblAgentCommisionTab.removeRow(i - 1);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetTable():");
        }
    }

    public void fillDropdown() {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);

            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            this.cbmProductType = new ComboBoxModel(key, value);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
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
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        cbmProdId = new ComboBoxModel(key, value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public String getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    public String getTdtToDate() {
        return tdtToDate;
    }

    public void setTdtToDate(String tdtToDate) {
        this.tdtToDate = tdtToDate;
    }

    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        int nums[] = new int[150];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt(tblAgentCommisionTab.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        System.out.println("sl no...." + slno);
        return slno;
    }

    public void addToAgentCommisionDetailsTable(int rowSelected, boolean updateMode) {
        try {
            System.out.println("addToAgentLeaveDetailsTable : " + rowSelected + "updateMode : " + updateMode);
            int rowSel = rowSelected;
            final AgentCommisionSlabRateTO objTO = new AgentCommisionSlabRateTO();
            if (agentCommisionMap == null) {
                agentCommisionMap = new LinkedHashMap();
            }
            if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (isNewData()) {
                    objTO.setStatusDt((Date) curDate.clone());
                    objTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTO.setStatus(CommonConstants.STATUS_CREATED);
                } else {
                    objTO.setStatusDt((Date) curDate.clone());
                    objTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objTO.setStatusDt((Date) curDate.clone());
                objTO.setStatusBy(TrueTransactMain.USER_ID);
                objTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            int slno = 0;
            int nums[] = new int[150];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblAgentCommisionTab.getDataArrayList();
                slno = serialNo(data);
            } else {
                if (isNewData()) {
                    ArrayList data = tblAgentCommisionTab.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblAgentCommisionTab.getValueAt(rowSelected, 0));
                    slno = serialCount;
                    serialCount = 0;
                }
            }
            if (rowSelected != -1) {
                if (slno > 0) {
                    objTO.setSlNo(slno);
                }
                if (slabId.length() > 0) {
                    objTO.setSlabId(slabId);
                }
            }
            objTO.setProdType(getCboProductType());
            objTO.setProdId(getCboProdId());
            objTO.setColAchdId(getTxtCommisionCreditedTo());
            objTO.setCommcolAchdId(getTxtCommisionCollectTo());
            objTO.setAcctIntroCommisionHead(getTxtActIntroCommisionHd());
            objTO.setTdsacHd(getTxtTdsHd());
            objTO.setCommperBank(CommonUtil.convertObjToDouble(getTxtCommFromBank()));
            objTO.setCommperAcHoldr(CommonUtil.convertObjToDouble(getTxtCommFromActHolder()));
            objTO.setAcctIntroCommision(CommonUtil.convertObjToDouble(getTxtActIntroCommisionAmt()));
            objTO.setTdsAmt(CommonUtil.convertObjToDouble(getTxtTds()));
            Date Dt = DateUtil.getDateMMDDYYYY(getTdtFromDate());
            if (Dt != null) {
                Date dtDate = (Date) curDate.clone();
                dtDate.setDate(Dt.getDate());
                dtDate.setMonth(Dt.getMonth());
                dtDate.setYear(Dt.getYear());
                objTO.setFromDate(dtDate);
            } else {
                objTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDate()));
            }

            Dt = DateUtil.getDateMMDDYYYY(getTdtToDate());
            if (Dt != null) {
                Date dtDate = (Date) curDate.clone();
                dtDate.setDate(Dt.getDate());
                dtDate.setMonth(Dt.getMonth());
                dtDate.setYear(Dt.getYear());
                objTO.setToDate(dtDate);
            } else {
                objTO.setToDate(null);
            }
            objTO.setFromAmt(CommonUtil.convertObjToDouble(getTxtFromAmount()));
            objTO.setToAmt(CommonUtil.convertObjToDouble(getTxtToAmount()));
//            agentCommisionMap.put(tblAgentCommisionTab.getRowCount(), objTO);
            String sno = String.valueOf(slno);
            updateAgentCommisionDetails(rowSel, sno, objTO);
            slabId = "";
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateAgentCommisionDetails(int rowSel, String sno, AgentCommisionSlabRateTO objTO) throws Exception {
        boolean rowExists = false;
        try {
            //If row already exists update it, else create a new row & append
            System.out.println("i : " + tblAgentCommisionTab.getRowCount() + "rowSel : " + rowSel);
            if (rowSel != -1) {
                ArrayList data = tblAgentCommisionTab.getDataArrayList();
                data.remove((Object) rowSel);
                ArrayList IncParRow = new ArrayList();
                IncParRow.add(CommonUtil.convertObjToDouble(getTxtCommFromBank()));
                IncParRow.add(CommonUtil.convertObjToDouble(getTxtCommFromActHolder()));
                IncParRow.add(CommonUtil.convertObjToDouble(getTxtActIntroCommisionAmt()));
                IncParRow.add(CommonUtil.convertObjToDouble(getTxtTds()));
                IncParRow.add(getTdtFromDate());
                IncParRow.add(getTdtToDate());
                IncParRow.add(CommonUtil.convertObjToStr(getTxtFromAmount()));
                IncParRow.add(CommonUtil.convertObjToStr(getTxtToAmount()));
                IncParRow.add(CommonUtil.convertObjToStr(objTO.getStatus()));
                IncParRow.add(CommonUtil.convertObjToStr(""));
                agentCommisionMap.remove(rowSel);
                agentCommisionMap.put(rowSel, objTO);
                tblAgentCommisionTab.removeRow(rowSel);
                tblAgentCommisionTab.insertRow(rowSel, IncParRow);
                IncParRow = null;
            }
            if (!rowExists && rowSel == -1) {
                ArrayList IncParRow = new ArrayList();
                IncParRow.add(CommonUtil.convertObjToDouble(getTxtCommFromBank()));
                IncParRow.add(CommonUtil.convertObjToDouble(getTxtCommFromActHolder()));
                IncParRow.add(CommonUtil.convertObjToDouble(getTxtActIntroCommisionAmt()));
                IncParRow.add(CommonUtil.convertObjToDouble(getTxtTds()));
                IncParRow.add(getTdtFromDate());
                IncParRow.add(getTdtToDate());
                IncParRow.add(CommonUtil.convertObjToStr(getTxtFromAmount()));
                IncParRow.add(CommonUtil.convertObjToStr(getTxtToAmount()));
                IncParRow.add(CommonUtil.convertObjToStr(CommonConstants.STATUS_CREATED));
                IncParRow.add(CommonUtil.convertObjToStr(""));
                agentCommisionMap.put(tblAgentCommisionTab.getRowCount(), objTO);
                System.out.println("New Record : " + tblAgentCommisionTab.getRowCount());
                tblAgentCommisionTab.insertRow(tblAgentCommisionTab.getRowCount(), IncParRow);
                System.out.println("agentCommisionMap : " + agentCommisionMap);
                IncParRow = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateAgentCommisionData(AgentCommisionSlabRateTO objTO) throws Exception {
        serialCount = objTO.getSlNo();
        slabId = objTO.getSlabId();
        setCboProductType(CommonUtil.convertObjToStr(objTO.getProdType()));
        setCboProdId(CommonUtil.convertObjToStr(objTO.getProdId()));
        setTxtCommisionCreditedTo(CommonUtil.convertObjToStr(objTO.getColAchdId()));
        setLblCommCreditValue(getActHeadDescription(objTO.getCommcolAchdId()));
        setLblCommCollectValue(getActHeadDescription(objTO.getColAchdId()));
        setLblActIntroValue(getActHeadDescription(objTO.getAcctIntroCommisionHead()));
        setLblTdsValue(getActHeadDescription(objTO.getTdsacHd()));
        setTxtCommisionCollectTo(CommonUtil.convertObjToStr(objTO.getCommcolAchdId()));
        setTxtActIntroCommisionHd(CommonUtil.convertObjToStr(objTO.getAcctIntroCommisionHead()));
        setTxtTdsHd(CommonUtil.convertObjToStr(objTO.getTdsacHd()));
    }

    //populate cheque details
    public void populateAgentCommisionDetails(String row) {
        try {
            final AgentCommisionSlabRateTO objTO = (AgentCommisionSlabRateTO) agentCommisionMap.get(new Integer(row));
            populateAgentCommisionTableData(objTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void populateAgentCommisionDetailsPartial(String row) {
        try {
            final AgentCommisionSlabRateTO objTO = (AgentCommisionSlabRateTO) agentCommisionMap.get(new Integer(row));
            populateAgentCommisionData(objTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateAgentCommisionTableData(AgentCommisionSlabRateTO objTO) throws Exception {
        serialCount = objTO.getSlNo();
        slabId = objTO.getSlabId();
        setCboProductType(CommonUtil.convertObjToStr(objTO.getProdType()));
        setCboProdId(CommonUtil.convertObjToStr(objTO.getProdId()));
        setTxtCommisionCreditedTo(CommonUtil.convertObjToStr(objTO.getColAchdId()));
        setLblCommCreditValue(getActHeadDescription(objTO.getCommcolAchdId()));
        setLblCommCollectValue(getActHeadDescription(objTO.getColAchdId()));
        setLblActIntroValue(getActHeadDescription(objTO.getAcctIntroCommisionHead()));
        setLblTdsValue(getActHeadDescription(objTO.getTdsacHd()));
        setTxtCommisionCollectTo(CommonUtil.convertObjToStr(objTO.getCommcolAchdId()));
        setTxtActIntroCommisionHd(CommonUtil.convertObjToStr(objTO.getAcctIntroCommisionHead()));
        setTxtTdsHd(CommonUtil.convertObjToStr(objTO.getTdsacHd()));
        setTxtCommFromBank(CommonUtil.convertObjToStr(objTO.getCommperBank()));
        setTxtCommFromActHolder(CommonUtil.convertObjToStr(objTO.getCommperAcHoldr()));
        setTxtActIntroCommisionAmt(CommonUtil.convertObjToStr(objTO.getAcctIntroCommision()));
        setTxtTds(CommonUtil.convertObjToStr(objTO.getTdsAmt()));
        setTxtFromAmount(CommonUtil.convertObjToStr(objTO.getFromAmt()));
        setTxtToAmount(CommonUtil.convertObjToStr(objTO.getToAmt()));
        if (objTO.getFromDate() != null) {
            setTdtFromDate(DateUtil.getStringDate(objTO.getFromDate()));
        } else {
            setTdtFromDate("");
        }
        if (objTO.getToDate() != null) {
            setTdtToDate(DateUtil.getStringDate(objTO.getToDate()));
        } else {
            setTdtToDate("");
        }
    }

    public void deleteAgentCommisionTableData(int row) {
        if (deletedMap == null) {
            deletedMap = new LinkedHashMap();
        }
        AgentCommisionSlabRateTO objAgentCommisionSlabRateTO = (AgentCommisionSlabRateTO) agentCommisionMap.get(row);
        agentCommisionMap.remove(row);
        System.out.println("i : " + row + " agentCommisionMap : " + agentCommisionMap);
        resetAgentCommisionDetails();
        try {
            tblAgentCommisionTab.removeRow(row);
            deletedMap.put(row, objAgentCommisionSlabRateTO);
            tblAgentCommisionTab.fireTableDataChanged();
            HashMap replacingMap = new LinkedHashMap();
            replacingMap = agentCommisionMap;
            if (replacingMap != null) {
                agentCommisionMap = new LinkedHashMap();
                ArrayList incDataList = new ArrayList(replacingMap.keySet());
                ArrayList addList = new ArrayList(replacingMap.keySet());
                int length = incDataList.size();
                for (int i = 0; i < length; i++) {
                    AgentCommisionSlabRateTO agentCommisionSlabRateTO = (AgentCommisionSlabRateTO) replacingMap.get(addList.get(i));
                    if (agentCommisionSlabRateTO != null) {
                        agentCommisionMap.put(i, agentCommisionSlabRateTO);
                    }
                }
                replacingMap = new LinkedHashMap();
                System.out.println("agentCommisionMap after replacing : " + agentCommisionMap);
                System.out.println("deletedMap after replacing : " + deletedMap);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private String getActHeadDescription(String acHdId) {
        String acHdDesc = "";
        HashMap glDescMap = new HashMap();
        glDescMap.put("AC_HD_ID", acHdId);
        List gllst = ClientUtil.executeQuery("getselectAchdDescription", glDescMap);
        if (gllst != null && gllst.size() > 0) {
            glDescMap = (HashMap) gllst.get(0);
            acHdDesc = CommonUtil.convertObjToStr(glDescMap.get("AC_HD_DESC"));
        }
        return acHdDesc;
    }

    public Date getProperFormatDate(Object obj) {
        Date curDt = null;
        // currDt = properFormatDate;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt = (Date) curDate.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
    }
}