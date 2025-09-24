/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentOB.java
 *
 * Created on Wed Feb 02 12:57:50 IST 2005
 */
package com.see.truetransact.ui.transaction.appagentcollectiondetails;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
//import com.see.truetransact.transferobject.agent.AgentTO;

//import com.see.truetransact.transferobject.agent.AgentCommisonTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
//import com.see.truetransact.transferobject.agent.AgentTO;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.CTable;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author
 */
public class AppAgentCollectionDetailsOB extends CObservable {

    private HashMap operationMap;
//    private EnhancedTableModel tblAgentTab;
//    
//    //__ ArrayLists for the Agent Table...
//    ArrayList agentTabTitle = new ArrayList();
//    private ArrayList agentTabRow;
//    
    private HashMap lookUpHash;
    private HashMap keyValue;
//    private AgentCommisonTO objAgentCommisionTO;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private EnhancedTableModel tbmAgentCommission;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(AppAgentCollectionDetailsUI.class);
    private ProxyFactory proxy = null;
    private String cboAgentId = "";
    private ComboBoxModel cbmAgentId;
    private ComboBoxModel cbmProdtype;
    private ComboBoxModel cbmProdId;
    private double txtAgentcomm;
    private double txtTdsCommission;
    private CTable _tblData;
    private HashMap dataHash;
    
    public HashMap getDataHash() {
        return dataHash;
    }

    public void setDataHash(HashMap dataHash) {
        this.dataHash = dataHash;
    }
    private ArrayList data;
    private ArrayList _heading;
    private int dataSize;

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
    ArrayList tableTitle = new ArrayList();
    ArrayList tableList = new ArrayList();

    public double getTxtAgentcomm() {
        return txtAgentcomm;
    }

    public void setTxtAgentcomm(double txtAgentcomm) {
        this.txtAgentcomm = txtAgentcomm;
    }

    public ComboBoxModel getCbmProdtype() {
        return cbmProdtype;
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    public void setCbmProdtype(ComboBoxModel cbmProdtype) {
        this.cbmProdtype = cbmProdtype;
    }
    private String lblNameForAgent = "";
    private String AgentId = "";
    Date curDate = null;
    private static AppAgentCollectionDetailsOB appAgentCollectionDetailsOB;

    static {
        try {
            log.info("In AgentOB Declaration");
            appAgentCollectionDetailsOB = new AppAgentCollectionDetailsOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public static AppAgentCollectionDetailsOB getInstance() {
        return appAgentCollectionDetailsOB;
    }

    /**
     * Creates a new instance of InwardClearingOB
     */
    public AppAgentCollectionDetailsOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
        setTableTile();
        //tbmAgentCommission = new EnhancedTableModel(null, tableTitle);
    }

    private void setTableTile() throws Exception {
        tableTitle.add("Select");
        tableTitle.add("Product");
        tableTitle.add("Comm. Rate");
        tableTitle.add("From Period");
        tableTitle.add("To Period");
        tableTitle.add("Collection Amt");
        tableTitle.add("Commission");
        tableTitle.add("TDS");
        System.out.println("####tableTitle : " + tableTitle);
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
        fillDropDown();
        // setAgentTabTitle();   //__ To set the Title of Table in Agent Tab...
//        tblAgentTab = new EnhancedTableModel(null, agentTabTitle);
    }

    // To set the Column title in Table...
    private void setAgentTabTitle() throws Exception {
        log.info("In setAgentTabTitle...");

//        agentTabTitle.add(objAgentRB.getString("tblColumn1"));
//        agentTabTitle.add(objAgentRB.getString("tblColumn2"));
//        agentTabTitle.add(objAgentRB.getString("tblColumn3"));
//        agentTabTitle.add(objAgentRB.getString("tblColumn4"));
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");

        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "AppAgentCollectionDetailsJNDI");
        operationMap.put(CommonConstants.HOME, "appagentcollectiondetails.AppAgentCollectionDetailsHome");
        operationMap.put(CommonConstants.REMOTE, "appagentcollectiondetails.AppAgentCollectionDetails");
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
            parseException.logException(e, true);
        }
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private void getMap(List list) throws Exception {
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 0, j = list.size(); i < j; i++) {
            key.add(((HashMap) list.get(i)).get("KEY"));
            value.add(((HashMap) list.get(i)).get("VALUE"));
        }
        System.out.println("####Key : " + key);
        System.out.println("####value : " + value);

    }

    private void fillDropDown() throws Exception {
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        //List list = ClientUtil.executeQuery("getAgentIdName", where);
        List list = ClientUtil.executeQuery("getAgentNameID", where);
        System.out.println("$$$$$$AgentId : " + list);
        getMap(list);
        cbmAgentId = new ComboBoxModel(key, value);
//        List list1 = ClientUtil.executeQuery("agent.getProductDataTD", where);
//        getMap(list1);
//        cbmProdtype = new ComboBoxModel(key, value);
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
        this.cbmProdtype = new ComboBoxModel(key, value);
    }
    
    private void populateOB(HashMap mapData) throws Exception {
//        AgentCommisonTO objAgentCommisionTO = null;
//        //Taking the Value of Prod_Id from each Table...
//        objAgentCommisionTO = new AgentCommisonTO();
//        objAgentCommisionTO = (AgentCommisonTO) ((List) mapData.get("AgentCommisonTO")).get(0);
//
//        setAgentCommisonTO(objAgentCommisionTO);

        ttNotifyObservers();
    }

//agents Name to getting for the customer table....
    public String agentName(String agent) {
        HashMap agentList = new HashMap();
        agentList.put("AGENT_ID", agent);
        List list = ClientUtil.executeQuery("getAgentDetailsName", agentList);
        System.out.println("ob###### agentList: " + agentList);
        if (list.size() > 0) {
            agentList = (HashMap) list.get(0);
        }
        String name = CommonUtil.convertObjToStr(agentList.get("AGENT_NAME"));
        return name;
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) curDate.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    public boolean populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
        boolean flag = false;
        // Adding Where Condition
        HashMap whereMap = null;
        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
            } else {
                System.out.println("Convert other data type to HashMap:" + mapID);
            }
        } else {
            whereMap = new HashMap();
        }

        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        }
        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
            whereMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        }

        mapID.put(CommonConstants.MAP_WHERE, whereMap);

        System.out.println("Screen   : " + getClass());
        System.out.println("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
        System.out.println("Map      : " + mapID);

//        _isAvailable = ClientUtil.setTableModel(mapID, tblData);
        dataHash = ClientUtil.executeTableQuery(mapID);
        setDataHash(dataHash);
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        System.out.println("_heading===" + _heading);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
        System.out.println("Datas :" + data.size());
        ArrayList tblDatanew = new ArrayList();
        ArrayList tblFinalData = new ArrayList();
        tableTitle = new ArrayList();
        tableTitle.add("SELECT");
        
        if (_heading != null && _heading.size() > 0) {
            for (int j = 0; j <= _heading.size() - 1; j++) {
                tableTitle.add(_heading.get(j));
                flag = true;
            }
        }
        if (data != null && data.size() > 0) {
            for (int i = 0; i <= data.size() - 1; i++) {
                List tmpList = (List) data.get(i);
                ArrayList newList = new ArrayList();
                if(whereMap.containsKey("AUTHORIZE")){
                    newList.add(new Boolean(true));
                }else{
                    newList.add(new Boolean(false));
                }
                for (int j = 0; j <= _heading.size() - 1; j++) {
                    newList.add(tmpList.get(j));
                }
                tblDatanew.add(newList);
                flag = true;
            }
        }
        tblFinalData.add(tblDatanew);
        tbmAgentCommission = new EnhancedTableModel((ArrayList) tblDatanew, tableTitle);
        setTbmAgentCommission(tbmAgentCommission);        
        setDataSize(data.size());
        setData(data);
        return flag;
    }

    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null) {
                    doActionPerform();
                    setResult(actionType);
                    resetForm();
                }
            } else {
                log.info("Action Type Not Defined.... ()");
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    public void doAllAction(HashMap map) {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null) {
                    map.put("COMMAND", getCommand());
                    doAllActionPerform(map);
                    setResult(actionType);
                    //resetForm();
                }
            } else {
                log.info("Action Type Not Defined.... ()");
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
    public void doAllActionPerform(HashMap map) throws Exception {
        log.info("In doActionPerform()");
        TTException exception = null;
        try {
            HashMap data = new HashMap();
            HashMap dataMap = new HashMap();
            System.out.println("data list 4234234234" + map);
            if (map != null && map.size() > 0) {
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.putAll(map);
                if(map.containsKey("AUTHORIZE")){                    
                    data.put("AUTHORIZEDATA", setTransferTo());
                    data.put(CommonConstants.STATUS, map.get(CommonConstants.STATUS));
                    data.put(CommonConstants.AUTHORIZEMAP, data);
                    System.out.println("data :" + data);
                }else{
                    data.put("COLLECTING_AGENT_ID", getLblNameForAgent());
                    ArrayList output = setTransferTo();
                    if(output != null && output.size()>0){
                        data.put("TxTransferTO", output);
                    }else{
                        ClientUtil.showAlertWindow("Agent Collection Account Details Not Set(AGENT_MASTER)");
                        return;
                    }
                }
                HashMap proxyResultMap = proxy.execute(data, operationMap);
                System.out.println("proxyResultMap*****" + proxyResultMap);
                setProxyReturnMap(proxyResultMap);
                
                  if (this.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    //lblStatus1.setText("POSTED");
                    if (proxyResultMap != null && proxyResultMap.size() > 0) {
                        if (proxyResultMap.containsKey("BATCH_ID")) {
                            ClientUtil.showMessageWindow("Batch ID : " + CommonUtil.convertObjToStr(proxyResultMap.get("BATCH_ID")));
                        }
                        int yesNo = 0;
                        String[] options = {"Yes", "No"};
                        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                        //System.out.println("#$#$$ yesNo : " + yesNo);
                        if (yesNo == 0) {
                            TTIntegration ttIntgration = null;
                            HashMap paramMap = new HashMap();
                            paramMap.put("TransId", CommonUtil.convertObjToStr(proxyResultMap.get("BATCH_ID")));
                            paramMap.put("TransDt", curDate);
                            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                            ttIntgration.setParam(paramMap);
                            ttIntgration.integrationForPrint("ReceiptPayment", false);
                        }
                    }
                }  
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

    public void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("data :" + data);
        HashMap proxyResultMap = proxy.execute(data, operationMap);
        System.out.println("proxyResultMap*****" + proxyResultMap);
        setProxyReturnMap(proxyResultMap);
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

    public void resetForm() {
        setAgentId("");
        setCboAgentId("");
        setTxtAgentcomm(new Double(0).doubleValue());
        tbmAgentCommission = new TableModel();
    }

    /**
     * Getter for property cboAgentId.
     *
     * @return Value of property cboAgentId.
     */
    public java.lang.String getCboAgentId() {
        return cboAgentId;
    }

    /**
     * Setter for property cboAgentId.
     *
     * @param cboAgentId New value of property cboAgentId.
     */
    public void setCboAgentId(java.lang.String cboAgentId) {
        this.cboAgentId = cboAgentId;
    }

    /**
     * Getter for property cbmAgentId.
     *
     * @return Value of property cbmAgentId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAgentId() {
        return cbmAgentId;
    }

    /**
     * Setter for property cbmAgentId.
     *
     * @param cbmAgentId New value of property cbmAgentId.
     */
    public void setCbmAgentId(com.see.truetransact.clientutil.ComboBoxModel cbmAgentId) {
        this.cbmAgentId = cbmAgentId;
    }

    /**
     * Getter for property lblNameForAgent.
     *
     * @return Value of property lblNameForAgent.
     */
    public java.lang.String getLblNameForAgent() {
        return lblNameForAgent;
    }

    /**
     * Setter for property lblNameForAgent.
     *
     * @param lblNameForAgent New value of property lblNameForAgent.
     */
    public void setLblNameForAgent(java.lang.String lblNameForAgent) {
        this.lblNameForAgent = lblNameForAgent;
    }

    /**
     * Getter for property AgentId.
     *
     * @return Value of property AgentId.
     */
    public java.lang.String getAgentId() {
        return AgentId;
    }

    /**
     * Setter for property AgentId.
     *
     * @param AgentId New value of property AgentId.
     */
    public void setAgentId(java.lang.String AgentId) {
        this.AgentId = AgentId;
    }

    /**
     * Getter for property tbmAgentCommission.
     *
     * @return Value of property tbmAgentCommission.
     */
    public EnhancedTableModel getTbmAgentCommission() {
        return tbmAgentCommission;
    }

    /**
     * Setter for property tbmAgentCommission.
     *
     * @param tbmAgentCommission New value of property tbmAgentCommission.
     */
    public void setTbmAgentCommission(EnhancedTableModel tbmAgentCommission) {
        this.tbmAgentCommission = tbmAgentCommission;
    }

    public double higher(double number, int roundingFactor) {
        double mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    public double getNearest(double number, int roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        double mod = number % roundingFactor;
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public double lower(double number, int roundingFactor) {
        double mod = number % roundingFactor;
        return number - mod;
    }

    public ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    public void setCbmProdId(ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }

    public ArrayList setTransferTo() {
        try {
            ArrayList lst = new ArrayList();
            TxTransferTO transferTO = new TxTransferTO();
            double creditAmount = 0;
            double debitAmount = 0;
            for (int i = 0; i < tbmAgentCommission.getRowCount(); i++) {
                if ((Boolean) tbmAgentCommission.getValueAt(i, 0)) {
                    transferTO = new TxTransferTO();
                    transferTO.setAmount(CommonUtil.convertObjToDouble(tbmAgentCommission.getValueAt(i, 6)));
                    transferTO.setInpAmount(CommonUtil.convertObjToDouble(tbmAgentCommission.getValueAt(i, 6)));
                    HashMap acctHeadMap = new HashMap();
                    acctHeadMap.put("ACCOUNT_NO",CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)));
                    List list = ClientUtil.executeQuery("getAcctHeadUsingActNum", acctHeadMap);
                    if(list != null && list.size()>0){
                        acctHeadMap = (HashMap)list.get(0);
                        transferTO.setAcHdId(CommonUtil.convertObjToStr(acctHeadMap.get("ACCT_HEAD")));
//                        cashTO.setProdId(CommonUtil.convertObjToStr(acctHeadMap.get("PROD_ID")));
                    }
                    acctHeadMap.put("ACCOUNT_NO",CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)));
                    list = ClientUtil.executeQuery("getProdIdUsingActNum", acctHeadMap);
                    if(list != null && list.size()>0){
                        acctHeadMap = (HashMap)list.get(0);
                        transferTO.setProdId(CommonUtil.convertObjToStr(acctHeadMap.get("PROD_ID")));
                    }
                    if(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 4)).equals(CommonConstants.CREDIT)){
                        creditAmount += CommonUtil.convertObjToDouble(tbmAgentCommission.getValueAt(i, 6)).doubleValue();
                    }else if(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 4)).equals(CommonConstants.DEBIT)){
                        debitAmount += CommonUtil.convertObjToDouble(tbmAgentCommission.getValueAt(i, 6)).doubleValue();
                    }
                    transferTO.setActNum(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)));
                    transferTO.setLinkBatchId(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)));
                    transferTO.setGlTransActNum(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)));
                    if(tbmAgentCommission.getValueAt(i, 5).equals("TD")){
                        transferTO.setActNum(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)+"_1"));
                        transferTO.setLinkBatchId(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)+"_1"));
                        transferTO.setGlTransActNum(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)+"_1"));
                    }
                    transferTO.setTransType(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 4)));
                    transferTO.setTransDt((Date) curDate.clone());
                    transferTO.setInitTransId(ProxyParameters.USER_ID);
                      // Check for interbranch
                    if (CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)).length() > 0) {
                        HashMap interBranchCodeMap = new HashMap();
                        if (tbmAgentCommission.getValueAt(i, 5).equals("TD")) {
                            interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3) + "_1"));
                        } else {
                            interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)));
                        }
                        List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
                        if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                            interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                            String newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                            System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
                            if (!newAcctBranchCode.equals(TrueTransactMain.BRANCH_ID)) {
                                transferTO.setBranchId(newAcctBranchCode);
                            } else {
                                transferTO.setBranchId(TrueTransactMain.BRANCH_ID);
                            }
                        } else {
                            transferTO.setBranchId(TrueTransactMain.BRANCH_ID);
                        }
                    } else {
                        transferTO.setBranchId(TrueTransactMain.BRANCH_ID);
                    }

                    //transferTO.setBranchId(TrueTransactMain.BRANCH_ID);
                    transferTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
                    transferTO.setInitChannType("MOBILE_APP");
                    //cashTO.setInstDt(getTdtInstrumentDate());
                    transferTO.setProdType(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 5)));
                    transferTO.setStatus(CommonConstants.STATUS_CREATED);
                    transferTO.setStatusBy(ProxyParameters.USER_ID);
                    transferTO.setTransModType(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 5)));
                    transferTO.setStatusDt((Date) curDate.clone());
                    transferTO.setParticulars(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 8)));
                    transferTO.setInstType("VOUCHER");
                    transferTO.setScreenName(CommonUtil.convertObjToStr("App Agent Collection Details"));
                    transferTO.setTransAllId(CommonUtil.convertObjToStr(lblNameForAgent));//used for storing agent_id from app agent collection details screen
//                  transferTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 8)));
                    transferTO.setAuthorizeStatus_2("ENTERED_AMOUNT");//Added By Kannan AR should not list in transfer screen
                    transferTO.setInpCurr(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 8)));
                    transferTO.setInstDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 10))));
                    transferTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
                    lst.add(transferTO);
                }
            }
            if(debitAmount>0){
                   ArrayList ar =new ArrayList();
                   ar.add(setGLTransferTo(CommonConstants.CREDIT,debitAmount));
                    if(ar.isEmpty()){
                            lst.add(ar);
                        }else{
                            lst.add(setGLTransferTo(CommonConstants.CREDIT,debitAmount));
                    }
            }
            if(creditAmount>0){
               TxTransferTO ar =new TxTransferTO();
               ar = setGLTransferTo(CommonConstants.DEBIT,creditAmount);
//                       ar.add(setGLTransferTo(CommonConstants.DEBIT,creditAmount));
                        if(ar!=null){
                            lst.add(setGLTransferTo(CommonConstants.DEBIT,creditAmount));                            
                        }else{
                            lst = new ArrayList();
                        }
            }

            System.out.println("lst : " + lst+"lst size : " + lst.size());
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public TxTransferTO setGLTransferTo(String transType,double amount) {
        try {
            ArrayList lst = new ArrayList();
            TxTransferTO transferTO = new TxTransferTO();
            transferTO.setAmount(amount);
            transferTO.setInpAmount(amount);
            HashMap acctHeadMap = new HashMap();
             acctHeadMap.put("AGENT_ID", cboAgentId);
            List list = ClientUtil.executeQuery("getAgentAccountdet", acctHeadMap);
//            if(list.isEmpty()){
//               return null;
//            }
            if(list != null && list.size()>0){
                acctHeadMap = (HashMap)list.get(0);
                if(acctHeadMap.get("DP_PROD_TYPE").equals(CommonUtil.convertObjToStr("GL"))&& acctHeadMap.containsKey("DP_PROD_TYPE")){
                    transferTO.setAcHdId(CommonUtil.convertObjToStr(acctHeadMap.get("DP_ACT_NUM")));
                    transferTO.setTransModType(CommonUtil.convertObjToStr(acctHeadMap.get("DP_PROD_TYPE")));
                    transferTO.setProdType(CommonUtil.convertObjToStr(acctHeadMap.get("DP_PROD_TYPE")));
                }else{
                    transferTO.setActNum(CommonUtil.convertObjToStr(acctHeadMap.get("DP_ACT_NUM")));
                    transferTO.setProdId(CommonUtil.convertObjToStr(acctHeadMap.get("DP_PROD_ID")));
                    transferTO.setAcHdId(CommonUtil.convertObjToStr(acctHeadMap.get("AC_HD_ID")));
                    transferTO.setLinkBatchId(CommonUtil.convertObjToStr(acctHeadMap.get("DP_ACT_NUM")));
                    transferTO.setTransModType(CommonUtil.convertObjToStr(acctHeadMap.get("DP_PROD_TYPE")));
                    transferTO.setProdType(CommonUtil.convertObjToStr(acctHeadMap.get("DP_PROD_TYPE")));
                }
            }else{
//                transferTO = new TxTransferTO();
//                    ClientUtil.showAlertWindow("Please configure \"AGENT_MASTER\" table");
                    return null;
                }            
            transferTO.setTransType(transType);
            transferTO.setTransDt((Date) curDate.clone());
            transferTO.setInitTransId(ProxyParameters.USER_ID);
            transferTO.setBranchId(TrueTransactMain.BRANCH_ID);
            transferTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
            transferTO.setInitChannType("MOBILE_APP");
            transferTO.setTransAllId(CommonUtil.convertObjToStr(lblNameForAgent));
            transferTO.setScreenName(CommonUtil.convertObjToStr("App Agent Collection Details"));
            //cashTO.setInstDt(getTdtInstrumentDate());
//            transferTO.setProdType("GL");
//            transferTO.setLinkBatchId(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)));
            transferTO.setStatus(CommonConstants.STATUS_CREATED);
            transferTO.setStatusBy(ProxyParameters.USER_ID);
//            transferTO.setTransModType("GL");
            transferTO.setStatusDt((Date) curDate.clone());
//            transferTO.setParticulars(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 8)));
            transferTO.setInstType("VOUCHER");
            //cashTO.setAuthorizeStatus("DAILY");
//            transferTO.setGlTransActNum(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)));
            transferTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            transferTO.setAuthorizeStatus_2("ENTERED_AMOUNT");//Added By Kannan AR should not list in transfer screen
//            lst.add(transferTO);
            System.out.println("lst : " + lst+"lst size : " + lst.size());
//            System.out.println("lst************" + lst.size());
            return transferTO;
//        }
            }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void setCancelSyncedEntry() {
        try {
            for (int i = 0; i < tbmAgentCommission.getRowCount(); i++) {
                if ((Boolean) tbmAgentCommission.getValueAt(i, 0)) {
                    HashMap cancelSyncedMap = new HashMap();
                    cancelSyncedMap.put("ACT_NUM",CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 3)));                    
                    cancelSyncedMap.put("APP_ID",CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 8)));
                    cancelSyncedMap.put("TRANS_DT",(Date) curDate.clone());
                    cancelSyncedMap.put("VALUE_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tbmAgentCommission.getValueAt(i, 10))));
                    cancelSyncedMap.put("USER_ID", CommonUtil.convertObjToStr(ProxyParameters.USER_ID));
                    ClientUtil.execute("UpdateCancelSyncedEntryAppAgentCollectionDate", cancelSyncedMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
