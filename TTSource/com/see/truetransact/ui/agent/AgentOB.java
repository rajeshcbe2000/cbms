/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentOB.java
 *
 * Created on Wed Feb 02 12:57:50 IST 2005
 */

package com.see.truetransact.ui.agent;


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

import com.see.truetransact.transferobject.agent.AgentColProdTO;
import com.see.truetransact.transferobject.agent.AgentLeaveDetailsTO;
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

public class AgentOB extends CObservable{
    private HashMap operationMap;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ComboBoxModel cboCollSuspProdtype;
    private ComboBoxModel cboCollSuspProdID;
    
    private ComboBoxModel cboCreditProductType;
    private ComboBoxModel cboCreditProductID;
    private ComboBoxModel cboDailyProductID;
    private ComboBoxModel cboDailyProductType;
    private ComboBoxModel cboAgentRegion;
    private ComboBoxModel cboAgentTransactionType;
    private EnhancedTableModel tblAgentTab;
    private EnhancedTableModel tblAgentLeaveDetailsTab;
    private String cboCollSuspPdType;
    private String getCboCollSuspPDID;
    private String cboCreditPdType;
    private String cboDailyPdType;
    private String getCboCreditPDID;
    private String lblCustNameVal;
    private String lblDepositCustName;
    
    private String cboRegion;
    private String cboTransactionType;
    private EnhancedTableModel tblProdDetails;
    private String DailyProdType = "";
    private String DailyProdID = "";
    private HashMap agentProdMap;
    
    //__ ArrayLists for the Agent Table...
    ArrayList agentTabTitle = new ArrayList();
    ArrayList agentTabLeaveDetails = new ArrayList();
    private ArrayList agentTabRow;
    final ArrayList tableTitle = new ArrayList();
    private boolean newData = false;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(AgentUI.class);
    private String dpacnum="";
    private Date lastComPaidDt=null;
    
    private ProxyFactory proxy = null;
    private Date curDate = null;
//    private final String BRANCH = TrueTransactMain.BRANCH_ID;
    
    private HashMap deletedAgentLeaveDetails;
    private ComboBoxModel cboLRegion;
    private ComboBoxModel cboCRegion;
    private ComboBoxModel cboTxnType;
    private String cboLeaveTakingRegion;
    private String cboLeaveCollectingRegion;
    private String cboLeaveTxnRegion;
    
    private String txtLAgentID;
    private String lblLAgentName;
    private String txtCAgentId;
    private String lblCAgentName;
    private String tdtFromDate;
    private String tdtToDate;
    private String cboLRegionValue;
    private String cboLRValue;
    private HashMap authorizeMap;

    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    public String getCboLRValue() {
        return cboLRValue;
    }

    public void setCboLRValue(String cboLRValue) {
        this.cboLRValue = cboLRValue;
    }
    private String cboCRegionValue;
    private String cboTxnTypeValue;

    public String getCboTxnTypeValue() {
        return cboTxnTypeValue;
    }

    public void setCboTxnTypeValue(String cboTxnTypeValue) {
        this.cboTxnTypeValue = cboTxnTypeValue;
    }
    public String getCboCRegionValue() {
        return cboCRegionValue;
    }

    public void setCboCRegionValue(String cboCRegionValue) {
        this.cboCRegionValue = cboCRegionValue;
    }

    public String getCboLRegionValue() {
        return cboLRegionValue;
    }

    public void setCboLRegionValue(String cboLRegionValue) {
        this.cboLRegionValue = cboLRegionValue;
    }
    private LinkedHashMap agentLeaveDetailsMap;
    private int selectedTab = 0;
//    private final String BRANCH = TrueTransactMain.BRANCH_ID;
    // To get the Value of Column Title and Dialogue Box...
//    final AgentRB objAgentRB = new AgentRB();
    java.util.ResourceBundle objAgentRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.agent.AgentRB", ProxyParameters.LANGUAGE);
    
    private static AgentOB agentOB;
    static {
        try {
            log.info("In AgentOB Declaration");
            agentOB = new AgentOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static AgentOB getInstance() {
        return agentOB;
    }
    
    /** Creates a new instance of InwardClearingOB */
    public AgentOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        cboCollSuspProdtype = new ComboBoxModel();
        cboCreditProductType = new ComboBoxModel();
        cboDailyProductType = new ComboBoxModel();
        cboDailyProductID = new ComboBoxModel();
        cboCollSuspProdID=new ComboBoxModel();
        cboCreditProductID = new ComboBoxModel();
        cboAgentRegion = new ComboBoxModel();
        cboAgentTransactionType = new ComboBoxModel();
        initianSetup();
        fillDropdown();
        setTableTile();
        setTableAgentDetailsTile();
        tblProdDetails = new EnhancedTableModel(null, tableTitle);
        tblAgentLeaveDetailsTab = new EnhancedTableModel(null, agentTabLeaveDetails);
        
    }
    
    private void setTableTile() throws Exception {
        tableTitle.add("ProdType");
        tableTitle.add("ProdId");
    }
    
    private void setTableAgentDetailsTile() throws Exception {
        agentTabLeaveDetails.add("Slno");
        agentTabLeaveDetails.add("LeaveAgentId");
        agentTabLeaveDetails.add("LeaveAgentName");
        agentTabLeaveDetails.add("CollectAgentId");
        agentTabLeaveDetails.add("CollectAgentName");        
        agentTabLeaveDetails.add("FromDate");
        agentTabLeaveDetails.add("ToDate");
        agentTabLeaveDetails.add("Status");
        agentTabLeaveDetails.add("Authorize Status");
    }
    
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        
        setAgentTabTitle();   //__ To set the Title of Table in Agent Tab...
        tblAgentTab = new EnhancedTableModel(null, agentTabTitle);
        tblAgentLeaveDetailsTab = new EnhancedTableModel(null,agentTabLeaveDetails);
    }
    
    // To set the Column title in Table...
    private void setAgentLeaveDetailsTabTitle() throws Exception{
        log.info("In setAgentTabTitle...");
        
        agentTabLeaveDetails.add(objAgentRB.getString("tblColumn1"));
        agentTabLeaveDetails.add(objAgentRB.getString("tblColumn2"));
        agentTabLeaveDetails.add(objAgentRB.getString("tblColumn3"));
        agentTabLeaveDetails.add(objAgentRB.getString("tblColumn4"));
    }
    
    // To set the Column title in Table...
    private void setAgentTabTitle() throws Exception{
        log.info("In setAgentTabTitle...");
        
        agentTabTitle.add(objAgentRB.getString("tblColumnLeave1"));
        agentTabTitle.add(objAgentRB.getString("tblColumnLeave2"));
        agentTabTitle.add(objAgentRB.getString("tblColumnLeave3"));
        agentTabTitle.add(objAgentRB.getString("tblColumnLeave4"));
        agentTabTitle.add(objAgentRB.getString("tblColumnLeave5"));
        agentTabTitle.add(objAgentRB.getString("tblColumnLeave6"));
        agentTabTitle.add(objAgentRB.getString("tblColumnLeave7"));
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "AgentJNDI");
        operationMap.put(CommonConstants.HOME, "agent.AgentHome");
        operationMap.put(CommonConstants.REMOTE, "agent.Agent");
    }
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            populateOB(mapData);
        } catch( Exception e ) {
            System.out.println("Error In populateData()");
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        AgentTO objAgentTO = null;
        //Taking the Value of Prod_Id from each Table...
        if(mapData.containsKey("AgentTO")){
            objAgentTO = (AgentTO) ((List) mapData.get("AgentTO")).get(0);
            setAgentTO(objAgentTO);
        }
        if (mapData.containsKey("AGENT_PROD_LIST")) {
            agentProdMap = (LinkedHashMap) mapData.get("AGENT_PROD_LIST"); 
            populateTable();
        }     
        if (mapData.containsKey("AGENT_LEAVE_DETAILS")) {
            agentLeaveDetailsMap = (LinkedHashMap) mapData.get("AGENT_LEAVE_DETAILS"); 
            populateAgentLeaveTable();
        }  
        ttNotifyObservers();
    }
    
        public void addToTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final AgentColProdTO agentColProdTO = new AgentColProdTO();
            if (agentProdMap == null) {
                agentProdMap = new LinkedHashMap();
            }
            agentColProdTO.setAgentId(getTxtAgentID());
            agentColProdTO.setProdId(getDailyProdID());
            agentColProdTO.setProdType(getDailyProdType());
            agentColProdTO.setLastColDt(curDate);
            agentColProdTO.setLastIntPaidDt(curDate);
            agentProdMap.put(agentColProdTO.getProdId(), agentColProdTO);
            updateScheduleDetails(rowSel, agentColProdTO);
            System.out.println("agentProdMap^#^#^#addToTable"+agentProdMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
      private void updateScheduleDetails(int rowSel, AgentColProdTO agentColProdTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        for (int i = tblProdDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblProdDetails.getDataArrayList().get(j)).get(0);
            if (agentColProdTO.getProdId().equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblProdDetails.getDataArrayList();
                data.remove(rowSel);                
                IncParRow.add(getDailyProdType());
                IncParRow.add(getDailyProdID());
                tblProdDetails.insertRow(rowSel, IncParRow);
                IncParRow = null;
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getDailyProdType());
            IncParRow.add(getDailyProdID());
            tblProdDetails.insertRow(tblProdDetails.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }
       
    public void deleteTableData(String val, int row) {        
        AgentColProdTO agentColProdTO = (AgentColProdTO) agentProdMap.get(val);
        Object obj;
        obj = val;
        agentProdMap.remove(val);
        System.out.println("agentProdMap^#^#^#deleteTableData"+agentProdMap);         
        resetProdTable();
        try {
            populateTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    public void updatTabelData(String row) {   
        final AgentColProdTO agentColProdTO = (AgentColProdTO) agentProdMap.get(row);
        System.out.println("AgentColProdTO in updateee%#%#%#%"+agentColProdTO);
        setDailyProdType(agentColProdTO.getProdType());
        setDailyProdID(agentColProdTO.getProdId());
    }
    private void populateTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(agentProdMap.keySet());
        ArrayList addList = new ArrayList(agentProdMap.keySet());
        int length = incDataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            AgentColProdTO agentColProdTO = (AgentColProdTO) agentProdMap.get(addList.get(i));
            System.out.println("agentColProdTO^#^#^#^##^^"+agentColProdTO);
            if(agentColProdTO!=null) {
                incTabRow.add(agentColProdTO.getProdType());                
                incTabRow.add(agentColProdTO.getProdId());
                tblProdDetails.addRow(incTabRow);
            }
        }
        //notifyObservers();
   }
    
    public void resetProdTable() {
        tblProdDetails.setDataArrayList(null, tableTitle);
    }
    
    public void resetAgentLeaveTable() {
        tblAgentLeaveDetailsTab.setDataArrayList(null, agentTabLeaveDetails);
    }
        
    // To Enter the values in the UI fields, from the database...
    private void setAgentTO(AgentTO objAgentTO) throws Exception{
        log.info("In setAgentTO()");
        
        setTxtAgentID(CommonUtil.convertObjToStr(objAgentTO.getAgentId()));
        //        setLblBranch(CommonUtil.convertObjToStr(objAgentTO.getBranchId()));
        setTdtApptDate(DateUtil.getStringDate(objAgentTO.getAppointedDt()));
        setTxtRemarks(CommonUtil.convertObjToStr(objAgentTO.getRemarks()));
        setAgentType(CommonUtil.convertObjToStr(objAgentTO.getAgentType()));//Added By Revathi.L
        setOperativeAcc(CommonUtil.convertObjToStr(objAgentTO.getOperativeAcNo()));
        setStatusBy(objAgentTO.getStatusBy());
        setAuthorizeStatus(objAgentTO.getAuthorizedStatus());
        //setLastComPaidDt(objAgentTO.getLastComPaidDt());
        HashMap glMap=new HashMap();
         
        if(objAgentTO.getCollSuspProdtype()!=null && objAgentTO.getCollSuspProdtype().equals("GL") && objAgentTO.getCollSuspACNum()!=null){
            glMap.put("AC_HD_ID",objAgentTO.getCollSuspACNum());
            glMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            List gllst=ClientUtil.executeQuery("getGlhead", glMap);
            glMap=null;
            glMap=(HashMap)gllst.get(0);
            setLblCustNameVal(CommonUtil.convertObjToStr(glMap.get("AC_HD_DESC")));
        } else if(objAgentTO.getCollSuspACNum()!=null){
            glMap.put("ACC_NUM",objAgentTO.getCollSuspACNum());
            glMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            List gllst=ClientUtil.executeQuery("getAccountNumberName"+objAgentTO.getCollSuspProdtype(), glMap);
            glMap=null;
            glMap=(HashMap)gllst.get(0);
            setLblCustNameVal(CommonUtil.convertObjToStr(glMap.get("CUSTOMER_NAME")));
        }
        if(objAgentTO.getDpProdType()!=null && objAgentTO.getDpProdType().equals("GL") && objAgentTO.getDpacnum()!=null){
            glMap.put("AC_HD_ID",objAgentTO.getDpacnum());
            glMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            List gllst=ClientUtil.executeQuery("getGlhead", glMap);
            glMap=null;
            glMap=(HashMap)gllst.get(0);
            setLblDepositCustName(CommonUtil.convertObjToStr(glMap.get("AC_HD_DESC")));                
        } else if(objAgentTO.getDpacnum()!=null && objAgentTO.getDpacnum().length()>0){
            glMap.put("ACC_NUM",objAgentTO.getDpacnum());
            glMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            List gllst=ClientUtil.executeQuery("getAccountNumberName"+objAgentTO.getDpProdType(), glMap);
            glMap=null;
            glMap=(HashMap)gllst.get(0);
            setLblDepositCustName(CommonUtil.convertObjToStr(glMap.get("CUSTOMER_NAME")));                
        }
        if(objAgentTO.getOperativeAcNo()!=null && objAgentTO.getOperativeAcNo().length()>0){
            glMap.put("ACC_NUM",objAgentTO.getOperativeAcNo());
            glMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
            List gllst=ClientUtil.executeQuery("getAccountNumberName"+"OA", glMap);
            glMap=null;
            glMap=(HashMap)gllst.get(0);
            setLblProdIdlName(CommonUtil.convertObjToStr(glMap.get("CUSTOMER_NAME")));                
        }
        setTxtCollSuspACNum(objAgentTO.getCollSuspACNum());
        setTxtCollSuspProdID(objAgentTO.getCollSuspProdID());
        setTxtCollSuspProdtype(objAgentTO.getCollSuspProdtype());
        
        setTxtDepositCreditedTo(objAgentTO.getDpProdType());
        setTxtDepositCreditedProdId(objAgentTO.getDpProdId());
        setDpacnum(objAgentTO.getDpacnum());
//        setDepositAcc(CommonUtil.convertObjToStr(objAgentTO.getDepsoitNo()));
//        setLblProdIdlName(CommonUtil.convertObjToStr(objAgentTO.getProdIdlName()));
//        setLblDepositName(CommonUtil.convertObjToStr(objAgentTO.getDepositProdId()));
        
        //        setTxtCreatedDt(DateUtil.getStringDate(objAgentTO.getCreatedDt()));
        //        setTxtCreatedBy(objAgentTO.getCreatedBy());
        //        setTxtStatus(objAgentTO.getStatus());
        //        setTxtStatusBy(objAgentTO.getStatusBy());
        //        setTxtStatusDt(DateUtil.getStringDate(objAgentTO.getStatusDt()));
        //        setTxtAuthorizedStatus(objAgentTO.getAuthorizedStatus());
        //        setTxtAuthorizedDt(DateUtil.getStringDate(objAgentTO.getAuthorizedDt()));
        //        setTxtAuthorizedBy(objAgentTO.getAuthorizedBy());
        
        setAuthStatus(CommonUtil.convertObjToStr(objAgentTO.getAuthorizedStatus()));
        //Added By Revathi.L
        if(CommonUtil.convertObjToStr(objAgentTO.getRegion()).length()>0){
            setCboRegion(CommonUtil.convertObjToStr(objAgentTO.getRegion()));
        }
        if(CommonUtil.convertObjToStr(objAgentTO.getTxnType()).length()>0){
            setCboTransactionType(CommonUtil.convertObjToStr(objAgentTO.getTxnType()));
        }
//        setTxtInitiatedBranch (objAgentTO.getInitiatedBranch ());
    }
    
    public void getOperativeProdId(String OAaccountNo){
        HashMap accountMap = new HashMap();
        HashMap prodMap = new HashMap();
        accountMap.put("ACT_NUM",OAaccountNo);
        List lst = ClientUtil.executeQuery("getOperativeAccProd", accountMap);
        if(lst.size()>0){
            prodMap = (HashMap)lst.get(0);
            lst = ClientUtil.executeQuery("getOperativeAccHeadId", prodMap);
            accountMap = (HashMap)lst.get(0);
            setLblProdIdlName(CommonUtil.convertObjToStr(accountMap.get("PROD_DESC")));
        }
    }
    
    public void getDepositProdId(String depositNo){
        HashMap accountMap = new HashMap();
        HashMap prodMap = new HashMap();
        accountMap.put("ACT_NUM",depositNo);
        List lst = ClientUtil.executeQuery("getDepositAccProd", accountMap);
        if(lst.size()>0){
            prodMap = (HashMap)lst.get(0);
            lst = ClientUtil.executeQuery("getDepositAccHeadId", prodMap);
            accountMap = (HashMap)lst.get(0);
            setLblDepositName(CommonUtil.convertObjToStr(accountMap.get("PROD_DESC")));
        }
        
    }
    
    private AgentTO setAgent() {
        log.info("In setAgent()");
        
        final AgentTO objAgentTO = new AgentTO();
        try{
            objAgentTO.setAgentId(getTxtAgentID());
            objAgentTO.setBranchId(getSelectedBranchID());
            
            Date AppDt = DateUtil.getDateMMDDYYYY(getTdtApptDate());
            if(AppDt != null){
                Date appDate = (Date) curDate.clone();
                appDate.setDate(AppDt.getDate());
                appDate.setMonth(AppDt.getMonth());
                appDate.setYear(AppDt.getYear());
                //            objAgentTO.setAppointedDt(DateUtil.getDateMMDDYYYY(getTdtApptDate()));
                objAgentTO.setAppointedDt(appDate);
            }else
                objAgentTO.setAppointedDt(DateUtil.getDateMMDDYYYY(getTdtApptDate()));
            objAgentTO.setRemarks(getTxtRemarks());
            objAgentTO.setAgentType(getAgentType());//Added By Revathi.L
            objAgentTO.setOperativeAcNo(getOperativeAcc());
            objAgentTO.setCollSuspACNum(getTxtCollSuspACNum());
            objAgentTO.setCollSuspProdID(getTxtCollSuspProdID());
            objAgentTO.setCollSuspProdtype(getTxtCollSuspProdtype());
            objAgentTO.setDpacnum(getDpacnum());
            if(getTxtDepositCreditedTo().equals("GL"))
                objAgentTO.setDpProdId("");
            else            
                objAgentTO.setDpProdId(getTxtDepositCreditedProdId());
            objAgentTO.setDpProdType(getTxtDepositCreditedTo());
            //            objAgentTO.setOAccId(getOperativeAcc());
            //            objAgentTO.setDepsoitNo(getDepositAcc());
            //            objAgentTO.setProdIdlName(getLblProdIdlName());
            //            objAgentTO.setDepositProdId(getLblDepositName());
            //            objAgentTO.setCreatedDt(DateUtil.getDateMMDDYYYY(getTxtCreatedDt()));
            //            objAgentTO.setCreatedBy(getTxtCreatedBy());
            //            objAgentTO.setStatus(getTxtStatus());
            //            objAgentTO.setStatusBy(getTxtStatusBy());
            //            objAgentTO.setStatusDt(DateUtil.getDateMMDDYYYY(getTxtStatusDt()));
            //            objAgentTO.setAuthorizedStatus(getTxtAuthorizedStatus());
            //            objAgentTO.setAuthorizedDt(DateUtil.getDateMMDDYYYY(getTxtAuthorizedDt()));
            //            objAgentTO.setAuthorizedBy(getTxtAuthorizedBy());
            
            
            objAgentTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objAgentTO.setLastComPaidDt(getLastComPaidDt());
            objAgentTO.setRegion(CommonUtil.convertObjToStr(getCboAgentRegion().getSelectedItem()));
            objAgentTO.setTxnType(CommonUtil.convertObjToStr(getCboAgentTransactionType().getSelectedItem()));
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objAgentTO;
    }
    
    
    
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined In setChequeBookTO()");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    public void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        if(selectedTab == 0){
            final AgentTO objAgentTO = setAgent();
            objAgentTO.setCommand(getCommand());
            data.put("AgentTO",objAgentTO);
        }
        if(agentProdMap!=null && agentProdMap.size()>0 ){
            data.put("AGENT_PROD_DETAILS",agentProdMap);
        }
        if(selectedTab == 1 ){
            if(agentLeaveDetailsMap != null && agentLeaveDetailsMap.size()>0 ){
                data.put("AGENT_LEAVE_DETAILS",agentLeaveDetailsMap);
                data.put("COMMAND",getCommand());
            }
            if(deletedAgentLeaveDetails != null && deletedAgentLeaveDetails.size()>0 ){
                data.put("AGENT_DELETED_LEAVE_DETAILS",deletedAgentLeaveDetails);
                data.put("COMMAND",getCommand());
            }
            if(getAuthorizeMap() != null){
                data.put("AUTHORIZE_LEAVE_MAP",getAuthorizeMap());
            }
        }
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setResult(actionType);
        setProxyReturnMap(proxyResultMap);
        resetForm();
    }
    
    // to decide which action Should be performed...
    private String getCommand() throws Exception{
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
    public int getActionType(){
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
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        setChanged();
        notifyObservers();
    }
    
    public void resetProdValues(){
        setDailyProdType("");
        setDailyProdID("");
    }
    
    public void resetForm(){
        setTxtAgentID("");
        setTdtApptDate("");
        setTxtRemarks("");
        setAgentType("");
        setLblName("");
        setDepositAcc("");
        setOperativeAcc("");
        setLblProdIdlName("");
        setLblDepositName("");
        setTxtCollSuspProdtype("");
        setTxtDepositCreditedTo("");
        setTxtCollSuspProdID("");
        setDpacnum("");
        setTxtCollSuspACNum("");
        //__ reset Auth Status...
        setAuthStatus("");
        agentProdMap =null;
        deletedAgentLeaveDetails = null;
        agentLeaveDetailsMap = null;
    }
    
    public void resetAgentLeaveDetails(){
//        setTxtLAgentID("");
        setTxtCAgentId("");
//        setLblLAgentName("");
        setLblCAgentName("");
        setTdtFromDate("");
        setTdtToDate("");
    }
    
    
    private String txtAgentID = "";
    private String tdtApptDate = "";
    private String txtRemarks = "";
    private String agentType = "";//Added By Revathi.L
    private String lblName = "";
    private String authStatus = "";
    private String operativeAcc ="";
    private String depositAcc="";
    private String lblProdIdlName ="";
    private String lblDepositName ="";
    private String txtCollSuspProdtype="";
    private String txtCollSuspProdID="";
    private String txtCollSuspACNum="";
    private String txtDepositCreditedTo = "";
    private String txtDepositCreditedProdId = "";
    private String txtDepositCreditedActNum = "";
    // Setter method for txtAgentID
    void setTxtAgentID(String txtAgentID){
        this.txtAgentID = txtAgentID;
        setChanged();
    }
    // Getter method for txtAgentID
    String getTxtAgentID(){
        return this.txtAgentID;
    }
    
    // Setter method for tdtApptDate
    void setTdtApptDate(String tdtApptDate){
        this.tdtApptDate = tdtApptDate;
        setChanged();
    }
    // Getter method for tdtApptDate
    String getTdtApptDate(){
        return this.tdtApptDate;
    }
    
    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    // Setter method for lblName
    void setLblName(String lblName){
        this.lblName = lblName;
        setChanged();
    }
    // Getter method for lblName
    String getLblName(){
        return this.lblName;
    }
    
    public void setAgentTabData(){
        final HashMap dataMap = new HashMap();
        dataMap.put("CUSTID", getTxtAgentID());
//        dataMap.put("BRANCHID", selectedBranch);
        
        System.out.println("dataMap: " + dataMap);
        final List agentDataList = ClientUtil.executeQuery("Agent.getAgentDepositsDetails", dataMap);
        System.out.println("dataMap#####: " + dataMap);

        int size = agentDataList.size();
        
        //__ If the Data Exists...
        if(size > 0){
            for (int i=0 ; i < size ; i++){
                System.out.println("Agent's Deposit Data Exists...");
                agentTabRow = new ArrayList();
                final HashMap resultMap = (HashMap)agentDataList.get(i);
                
                agentTabRow.add(CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_NO")));
                agentTabRow.add(CommonUtil.convertObjToStr(resultMap.get("NAME")));
                agentTabRow.add(DateUtil.getStringDate((Date)resultMap.get("DEPOSIT_DT")));
                agentTabRow.add(CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_AMT")));
                
                tblAgentTab.addRow(agentTabRow);
                agentTabRow = null;
            }
        }
    }
    
    /* Set and GET METHODS FOR THE tABLE...*/
    void setTblAgent(EnhancedTableModel tblAgentTab){
        this.tblAgentTab = tblAgentTab;
        setChanged();
    }
    
    EnhancedTableModel getTblAgent(){
        return this.tblAgentTab;
    }
        
    void setTblAgentLeaveDetails(EnhancedTableModel tblAgentLeaveDetailsTab){
        this.tblAgentLeaveDetailsTab = tblAgentLeaveDetailsTab;
//        setChanged();
    }
    
    EnhancedTableModel getTblAgentLeaveDetails(){
        return this.tblAgentLeaveDetailsTab;
    }
     
    
    /** TO RESET THE TABLE...*/
    public void resetTable(){
        try{
            ArrayList data = tblAgentTab.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblAgentTab.removeRow(i-1);
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetTable():");
        }
    }
    
    // Setter method for authStatus
    void setAuthStatus(String authStatus){
        this.authStatus = authStatus;
        setChanged();
    }
    // Getter method for authStatus
    String getAuthStatus(){
        return this.authStatus;
    }
    
    /**
     * Getter for property operativeAcc.
     * @return Value of property operativeAcc.
     */
    public java.lang.String getOperativeAcc() {
        return operativeAcc;
    }
    
    /**
     * Setter for property operativeAcc.
     * @param operativeAcc New value of property operativeAcc.
     */
    public void setOperativeAcc(java.lang.String operativeAcc) {
        this.operativeAcc = operativeAcc;
    }
    
    /**
     * Getter for property depositAcc.
     * @return Value of property depositAcc.
     */
    public java.lang.String getDepositAcc() {
        return depositAcc;
    }
    
    /**
     * Setter for property depositAcc.
     * @param depositAcc New value of property depositAcc.
     */
    public void setDepositAcc(java.lang.String depositAcc) {
        this.depositAcc = depositAcc;
    }
    
    /**
     * Getter for property lblProdIdlName.
     * @return Value of property lblProdIdlName.
     */
    public java.lang.String getLblProdIdlName() {
        return lblProdIdlName;
    }    
    
    /**
     * Setter for property lblProdIdlName.
     * @param lblProdIdlName New value of property lblProdIdlName.
     */
    public void setLblProdIdlName(java.lang.String lblProdIdlName) {
        this.lblProdIdlName = lblProdIdlName;
    }
    
    /**
     * Getter for property lblDepositName.
     * @return Value of property lblDepositName.
     */
    public java.lang.String getLblDepositName() {
        return lblDepositName;
    }
    
    /**
     * Setter for property lblDepositName.
     * @param lblDepositName New value of property lblDepositName.
     */
    public void setLblDepositName(java.lang.String lblDepositName) {
        this.lblDepositName = lblDepositName;
    }
    
    /**
     * Getter for property txtCollSuspProdtype.
     * @return Value of property txtCollSuspProdtype.
     */
    public java.lang.String getTxtCollSuspProdtype() {
        return txtCollSuspProdtype;
    }
    
    /**
     * Setter for property txtCollSuspProdtype.
     * @param txtCollSuspProdtype New value of property txtCollSuspProdtype.
     */
    public void setTxtCollSuspProdtype(java.lang.String txtCollSuspProdtype) {
        this.txtCollSuspProdtype = txtCollSuspProdtype;
    }
    
    /**
     * Getter for property txtCollSuspProdID.
     * @return Value of property txtCollSuspProdID.
     */
    public java.lang.String getTxtCollSuspProdID() {
        return txtCollSuspProdID;
    }
    
    /**
     * Setter for property txtCollSuspProdID.
     * @param txtCollSuspProdID New value of property txtCollSuspProdID.
     */
    public void setTxtCollSuspProdID(java.lang.String txtCollSuspProdID) {
        this.txtCollSuspProdID = txtCollSuspProdID;
    }
    
    /**
     * Getter for property txtCollSuspACNum.
     * @return Value of property txtCollSuspACNum.
     */
    public java.lang.String getTxtCollSuspACNum() {
        return txtCollSuspACNum;
    }
    
    /**
     * Setter for property txtCollSuspACNum.
     * @param txtCollSuspACNum New value of property txtCollSuspACNum.
     */
    public void setTxtCollSuspACNum(java.lang.String txtCollSuspACNum) {
        this.txtCollSuspACNum = txtCollSuspACNum;
    }
     public void fillDropdown(){
        try{
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            
            lookup_keys.add("CASH.INST_TYPE");
            lookup_keys.add("PRODUCTTYPE");
            lookup_keys.add("AGENT_APP_TRANSACTION_TYPE");
            lookup_keys.add("AGENT_APP_REGION");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);

            //__ Data for the ClearingType Combo-Box...
//            lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getInwardClearingType");
//            lookUpHash.put(CommonConstants.PARAMFORQUERY, ProxyParameters.BRANCH_ID);
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//            cboCollSuspProdtype = new ComboBoxModel(key,value);
//            
////            getKeyValue((HashMap)keyValue.get("CASH.INST_TYPE"));
           
            
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
            this.cboCollSuspProdtype= new ComboBoxModel(key  ,value);
            
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));            
            this.cboCreditProductType = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));            
            this.cboDailyProductType = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("AGENT_APP_REGION"));            
            this.cboAgentRegion = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("AGENT_APP_TRANSACTION_TYPE"));            
            this.cboAgentTransactionType = new ComboBoxModel(key,value);                    
            
            getKeyValue((HashMap)keyValue.get("AGENT_APP_REGION"));            
            this.cboLRegion = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("AGENT_APP_REGION"));            
            this.cboCRegion = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("AGENT_APP_TRANSACTION_TYPE"));            
            this.cboTxnType = new ComboBoxModel(key,value);
            
//            lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getBank");
//            lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
//            keyValue = ClientUtil.populateLookupData(lookUpHash);
//            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
//            cbmBankCodeID = new ComboBoxModel(key,value);
            
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /**
     * Getter for property cboCollSuspProdtype.
     * @return Value of property cboCollSuspProdtype.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCboCollSuspProdtype() {
        return cboCollSuspProdtype;
    }
    
    /**
     * Setter for property cboCollSuspProdtype.
     * @param cboCollSuspProdtype New value of property cboCollSuspProdtype.
     */
    public void setCboCollSuspProdtype(com.see.truetransact.clientutil.ComboBoxModel cboCollSuspProdtype) {
        this.cboCollSuspProdtype = cboCollSuspProdtype;
    }
    
    /**
     * Getter for property cboCollSuspProdID.
     * @return Value of property cboCollSuspProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCboCollSuspProdID() {
        return cboCollSuspProdID;
    }
    
    /**
     * Setter for property cboCollSuspProdID.
     * @param cboCollSuspProdID New value of property cboCollSuspProdID.
     */
    public void setCboCollSuspProdID(com.see.truetransact.clientutil.ComboBoxModel cboCollSuspProdID) {
        this.cboCollSuspProdID = cboCollSuspProdID;
    }
    
    /**
     * Getter for property cboCollSuspPdType.
     * @return Value of property cboCollSuspPdType.
     */
    public java.lang.String getCboCollSuspPdType() {
        return cboCollSuspPdType;
    }
    
    /**
     * Setter for property cboCollSuspPdType.
     * @param cboCollSuspPdType New value of property cboCollSuspPdType.
     */
    public void setCboCollSuspPdType(java.lang.String cboCollSuspPdType) {
        this.cboCollSuspPdType = cboCollSuspPdType;
    }
    
    /**
     * Getter for property getCboCollSuspPDID.
     * @return Value of property getCboCollSuspPDID.
     */
    public java.lang.String getGetCboCollSuspPDID() {
        return getCboCollSuspPDID;
    }
    
    /**
     * Setter for property getCboCollSuspPDID.
     * @param getCboCollSuspPDID New value of property getCboCollSuspPDID.
     */
    public void setGetCboCollSuspPDID(java.lang.String getCboCollSuspPDID) {
        this.getCboCollSuspPDID = getCboCollSuspPDID;
    }
    
    /**
     * Getter for property getCboCollSuspPDID.
     * @return Value of property getCboCollSuspPDID.
     */
//    public java.lang.String getGetCboCollSuspPDID() {
//        return getCboCollSuspPDID;
//    }
//    
//    /**
//     * Setter for property getCboCollSuspPDID.
//     * @param getCboCollSuspPDID New value of property getCboCollSuspPDID.
//     */
//    public void setGetCboCollSuspPDID(java.lang.String getCboCollSuspPDID) {
//        this.getCboCollSuspPDID = getCboCollSuspPDID;
//    }
    
    /**
     * Getter for property cboCollSuspProd_type.
     * @return Value of property cboCollSuspProd_type.
     */
//    public ComboBoxModel getCboCollSuspProd_type() {
//        return cboCollSuspProd_type;
//    }
//    
//    /**
//     * Setter for property cboCollSuspProd_type.
//     * @param cboCollSuspProd_type New value of property cboCollSuspProd_type.
//     */
//    public void setCboCollSuspProd_type(ComboBoxModel cboCollSuspProd_type) {
//        this.cboCollSuspProd_type = cboCollSuspProd_type;
//    }
    public void populateCboCollSuspPdId() {
        if (CommonUtil.convertObjToStr(getCboCollSuspPdType()).length() > 0 && !CommonUtil.convertObjToStr(getCboCollSuspPdType()).equals("GL")) {
            System.out.println("prodType : setCbmProdIdPISD " + getCboCollSuspPdType());
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + getCboCollSuspPdType());
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cboCollSuspProdID = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cboCollSuspProdID = new ComboBoxModel();
        }
        setChanged();
   }

    public void cboCreditProductId() {
        if (CommonUtil.convertObjToStr(getCboCreditPdType()).length() > 0 && !CommonUtil.convertObjToStr(getCboCreditPdType()).equals("GL")) {
            System.out.println("prodType : setCbmProdIdPISD " + getCboCreditPdType());
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + getCboCreditPdType());
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cboCreditProductID = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cboCreditProductID = new ComboBoxModel();
        }
        setChanged();
   }
    
    public void cboDailyProductID() {
        if (CommonUtil.convertObjToStr(getDailyProdType()).length() > 0 && !CommonUtil.convertObjToStr(getDailyProdType()).equals("GL")) {
            System.out.println("prodType : getCboDailyPdType " + getDailyProdType());
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + getDailyProdType());
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cboDailyProductID = new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cboDailyProductID = new ComboBoxModel();
        }
        setChanged();
   }
    
    /**
     * Getter for property lblCustNameVal.
     * @return Value of property lblCustNameVal.
     */
    public java.lang.String getLblCustNameVal() {
        return lblCustNameVal;
    }    

    /**
     * Setter for property lblCustNameVal.
     * @param lblCustNameVal New value of property lblCustNameVal.
     */
    public void setLblCustNameVal(java.lang.String lblCustNameVal) {
        this.lblCustNameVal = lblCustNameVal;
    }
    
    /**
     * Getter for property dpacnum.
     * @return Value of property dpacnum.
     */
    public java.lang.String getDpacnum() {
        return dpacnum;
    }
    
    /**
     * Setter for property dpacnum.
     * @param dpacnum New value of property dpacnum.
     */
    public void setDpacnum(java.lang.String dpacnum) {
        this.dpacnum = dpacnum;
    }
    
    /**
     * Getter for property lastComPaidDt.
     * @return Value of property lastComPaidDt.
     */
    public java.util.Date getLastComPaidDt() {
        return lastComPaidDt;
    }
    
    /**
     * Setter for property lastComPaidDt.
     * @param lastComPaidDt New value of property lastComPaidDt.
     */
    public void setLastComPaidDt(java.util.Date lastComPaidDt) {
        this.lastComPaidDt = lastComPaidDt;
    }
    
    /**
     * Getter for property cboCreditProductType.
     * @return Value of property cboCreditProductType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCboCreditProductType() {
        return cboCreditProductType;
    }
    
    /**
     * Setter for property cboCreditProductType.
     * @param cboCreditProductType New value of property cboCreditProductType.
     */
    public void setCboCreditProductType(java.lang.String cboCreditPdType) {
        this.cboCreditPdType = cboCreditPdType;
    }
    
    /**
     * Getter for property cboCreditProductID.
     * @return Value of property cboCreditProductID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCboCreditProductID() {
        return cboCreditProductID;
    }
    
    /**
     * Setter for property cboCreditProductID.
     * @param cboCreditProductID New value of property cboCreditProductID.
     */
    public void setCboCreditProductID(com.see.truetransact.clientutil.ComboBoxModel cboCreditProductID) {
        this.cboCreditProductID = cboCreditProductID;
    }
    
    /**
     * Getter for property txtDepositCreditedTo.
     * @return Value of property txtDepositCreditedTo.
     */
    public java.lang.String getTxtDepositCreditedTo() {
        return txtDepositCreditedTo;
    }
    
    /**
     * Setter for property txtDepositCreditedTo.
     * @param txtDepositCreditedTo New value of property txtDepositCreditedTo.
     */
    public void setTxtDepositCreditedTo(java.lang.String txtDepositCreditedTo) {
        this.txtDepositCreditedTo = txtDepositCreditedTo;
    }
    
    /**
     * Getter for property txtDepositCreditedProdId.
     * @return Value of property txtDepositCreditedProdId.
     */
    public java.lang.String getTxtDepositCreditedProdId() {
        return txtDepositCreditedProdId;
    }
    
    /**
     * Setter for property txtDepositCreditedProdId.
     * @param txtDepositCreditedProdId New value of property txtDepositCreditedProdId.
     */
    public void setTxtDepositCreditedProdId(java.lang.String txtDepositCreditedProdId) {
        this.txtDepositCreditedProdId = txtDepositCreditedProdId;
    }
    
    /**
     * Getter for property txtDepositCreditedActNum.
     * @return Value of property txtDepositCreditedActNum.
     */
    public java.lang.String getTxtDepositCreditedActNum() {
        return txtDepositCreditedActNum;
    }
    
    /**
     * Setter for property txtDepositCreditedActNum.
     * @param txtDepositCreditedActNum New value of property txtDepositCreditedActNum.
     */
    public void setTxtDepositCreditedActNum(java.lang.String txtDepositCreditedActNum) {
        this.txtDepositCreditedActNum = txtDepositCreditedActNum;
    }
    
    /**
     * Getter for property cboCreditPdType.
     * @return Value of property cboCreditPdType.
     */
    public java.lang.String getCboCreditPdType() {
        return cboCreditPdType;
    }
    
    /**
     * Setter for property cboCreditPdType.
     * @param cboCreditPdType New value of property cboCreditPdType.
     */
    public void setCboCreditPdType(java.lang.String cboCreditPdType) {
        this.cboCreditPdType = cboCreditPdType;
    }
    
    /**
     * Getter for property lblDepositCustName.
     * @return Value of property lblDepositCustName.
     */
    public java.lang.String getLblDepositCustName() {
        return lblDepositCustName;
    }
    
    /**
     * Setter for property lblDepositCustName.
     * @param lblDepositCustName New value of property lblDepositCustName.
     */
    public void setLblDepositCustName(java.lang.String lblDepositCustName) {
        this.lblDepositCustName = lblDepositCustName;
    }

    public ComboBoxModel getCboDailyProductType() {
        return cboDailyProductType;
    }

    public void setCboDailyProductType(ComboBoxModel cboDailyProductType) {
        this.cboDailyProductType = cboDailyProductType;
    }

    public ComboBoxModel getCboDailyProductID() {
        return cboDailyProductID;
    }

    public void setCboDailyProductID(ComboBoxModel cboDailyProductID) {
        this.cboDailyProductID = cboDailyProductID;
    }

    public String getDailyProdType() {
        return DailyProdType;
    }

    public void setDailyProdType(String DailyProdType) {
        this.DailyProdType = DailyProdType;
    }

    public String getDailyProdID() {
        return DailyProdID;
    }

    public void setDailyProdID(String DailyProdID) {
        this.DailyProdID = DailyProdID;
    }

    public EnhancedTableModel getTblProdDetails() {
        return tblProdDetails;
    }

    public void setTblProdDetails(EnhancedTableModel tblProdDetails) {
        this.tblProdDetails = tblProdDetails;
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }
    
    
    public com.see.truetransact.clientutil.ComboBoxModel getCboAgentRegion() {
        return cboAgentRegion;
    }

    public void setCboAgentRegion(com.see.truetransact.clientutil.ComboBoxModel cboAgentRegion) {
        this.cboAgentRegion = cboAgentRegion;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCboAgentTransactionType() {
        return cboAgentTransactionType;
    }

    public void setCboAgentTransactionType(com.see.truetransact.clientutil.ComboBoxModel cboAgentTransactionType) {
        this.cboAgentTransactionType = cboAgentTransactionType;
    }
 
    public String getCboRegion() {
        return cboRegion;
    }

    public void setCboRegion(String cboRegion) {
        this.cboRegion = cboRegion;
    }

    public String getCboTransactionType() {
        return cboTransactionType;
    }

    public void setCboTransactionType(String cboTransactionType) {
        this.cboTransactionType = cboTransactionType;
    }
    
    
    public com.see.truetransact.clientutil.ComboBoxModel getCboCRegion() {
        return cboCRegion;
    }

    public void setCboCRegion(com.see.truetransact.clientutil.ComboBoxModel cboCRegion) {
        this.cboCRegion = cboCRegion;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCboLRegion() {
        return cboLRegion;
    }

    public void setCboLRegion(com.see.truetransact.clientutil.ComboBoxModel cboLRegion) {
        this.cboLRegion = cboLRegion;
    }

    public String getCboLeaveCollectingRegion() {
        return cboLeaveCollectingRegion;
    }

    public void setCboLeaveCollectingRegion(String cboLeaveCollectingRegion) {
        this.cboLeaveCollectingRegion = cboLeaveCollectingRegion;
    }

    public String getCboLeaveTakingRegion() {
        return cboLeaveTakingRegion;
    }

    public void setCboLeaveTakingRegion(String cboLeaveTakingRegion) {
        this.cboLeaveTakingRegion = cboLeaveTakingRegion;
    }

    public String getCboLeaveTxnRegion() {
        return cboLeaveTxnRegion;
    }

    public void setCboLeaveTxnRegion(String cboLeaveTxnRegion) {
        this.cboLeaveTxnRegion = cboLeaveTxnRegion;
    }

    public com.see.truetransact.clientutil.ComboBoxModel getCboTxnType() {
        return cboTxnType;
    }

    public void setCboTxnType(com.see.truetransact.clientutil.ComboBoxModel cboTxnType) {
        this.cboTxnType = cboTxnType;
    }    
    
    public String getLblCAgentName() {
        return lblCAgentName;
    }

    public void setLblCAgentName(String lblCAgentName) {
        this.lblCAgentName = lblCAgentName;
    }

    public String getLblLAgentName() {
        return lblLAgentName;
    }

    public void setLblLAgentName(String lblLAgentName) {
        this.lblLAgentName = lblLAgentName;
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

    public String getTxtCAgentId() {
        return txtCAgentId;
    }

    public void setTxtCAgentId(String txtCAgentId) {
        this.txtCAgentId = txtCAgentId;
    }

    public String getTxtLAgentID() {
        return txtLAgentID;
    }

    public void setTxtLAgentID(String txtLAgentID) {
        this.txtLAgentID = txtLAgentID;
    }       
    
    public void addToAgentLeaveDetailsTable(int rowSelected, boolean updateMode){
        try{
            System.out.println("addToAgentLeaveDetailsTable : "+rowSelected+"updateMode : "+updateMode);
            int rowSel = rowSelected;
            final AgentLeaveDetailsTO objTO = new AgentLeaveDetailsTO();
            if( agentLeaveDetailsMap == null ){
                agentLeaveDetailsMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT && rowSelected != -1){
                String status = CommonUtil.convertObjToStr(tblAgentLeaveDetailsTab.getValueAt(CommonUtil.convertObjToInt(rowSelected),7));
                if(isNewData() || status.equals("")){
                    objTO.setStatusDt((Date)curDate.clone());
                    objTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objTO.setStatusDt((Date)curDate.clone());
                    objTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objTO.setStatusDt((Date)curDate.clone());
                objTO.setStatusBy(TrueTransactMain.USER_ID);
                objTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            int slno = 0;
            int nums[]= new int[150];
            int max = nums[0];
            if(!updateMode){
                ArrayList data = tblAgentLeaveDetailsTab.getDataArrayList();
                slno = serialNo(data);
            } else{
                if(isNewData()){                    
                    ArrayList data = tblAgentLeaveDetailsTab.getDataArrayList();
                    slno = serialNo(data);
                } else{
                    int b = CommonUtil.convertObjToInt(tblAgentLeaveDetailsTab.getValueAt(rowSelected,0));
                    slno = b;
                }
            }
            objTO.setSlNo(slno);
            objTO.setlAgentId(getTxtLAgentID());
            objTO.setlAgentName(getLblLAgentName());
            objTO.setlRegion(getCboLRegionValue());
            objTO.setcAgentId(getTxtCAgentId());
            objTO.setcAgentName(getLblCAgentName());
            objTO.setcRegion(getCboCRegionValue());
            objTO.setTxnType(getCboTxnTypeValue());
            objTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDate()));
            objTO.setToDate(DateUtil.getDateMMDDYYYY(getTdtToDate()));
            agentLeaveDetailsMap.put(objTO.getSlNo(),objTO);
            String sno = String.valueOf(slno);
            updateLeaveDetails(rowSel,sno,objTO,updateMode);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public int serialNo(ArrayList data){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize+1;
        for(int i=0;i<data.size();i++){
            a = CommonUtil.convertObjToInt(tblAgentLeaveDetailsTab.getValueAt(i,0));
            nums[i] = a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        System.out.println("sl no...."+slno);
        return slno;
    }
    
    private void updateLeaveDetails(int rowSel, String sno, AgentLeaveDetailsTO objTO,boolean updateMode)  throws Exception{
        System.out.println("rowSel : "+rowSel+" sno : "+sno+" updateMode : "+updateMode);
        boolean rowExists = false;
        if (rowSel != -1 && updateMode) {
            ArrayList IncParRow = new ArrayList();
            ArrayList data = tblAgentLeaveDetailsTab.getDataArrayList();
            data.remove((Object) rowSel);
            IncParRow.add(sno);
            IncParRow.add(getTxtLAgentID());
            IncParRow.add(getLblLAgentName());
            IncParRow.add(getTxtCAgentId());
            IncParRow.add(getLblCAgentName());
            IncParRow.add(getTdtFromDate());
            IncParRow.add(getTdtToDate());                
            IncParRow.add(objTO.getStatus());                
            IncParRow.add(objTO.getAuthorizedStatus());                
            tblAgentLeaveDetailsTab.removeRow(rowSel);
            tblAgentLeaveDetailsTab.insertRow(rowSel,IncParRow);
            IncParRow = null;
        }
        if(!updateMode && rowSel == -1){
            ArrayList IncParRow = new ArrayList();
            System.out.println("fagagaga");
            IncParRow.add(sno);
            IncParRow.add(getTxtLAgentID());
            IncParRow.add(getLblLAgentName());
            IncParRow.add(getTxtCAgentId());
            IncParRow.add(getLblCAgentName());
            IncParRow.add(getTdtFromDate());
            IncParRow.add(getTdtToDate());            
            IncParRow.add("");     
            IncParRow.add("");     
            System.out.println("hahahahaha"+tblAgentLeaveDetailsTab.getRowCount());
            tblAgentLeaveDetailsTab.insertRow(tblAgentLeaveDetailsTab.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    //populate cheque details
    public void populateAgentLeaveDetails(String row){
        try{
//            resetChequeDetails();
            final AgentLeaveDetailsTO objTO = (AgentLeaveDetailsTO) agentLeaveDetailsMap.get(new Integer(row)-1);
            populateTableData(objTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTableData(AgentLeaveDetailsTO objTO)  throws Exception{
//        setTxtSlNo(CommonUtil.convertObjToStr(objTO.getSlNo()));
        setTxtLAgentID(objTO.getlAgentId());
        setLblLAgentName(objTO.getlAgentName());
        setCboLRegionValue(objTO.getlRegion());
        setCboLRValue(objTO.getlRegion());
        setTxtCAgentId(objTO.getcAgentId());
        setLblCAgentName(objTO.getcAgentName());
        setCboCRegionValue(objTO.getcRegion());
        setTdtFromDate(DateUtil.getStringDate(objTO.getFromDate()));
        setTdtToDate(DateUtil.getStringDate(objTO.getToDate()));
        setCboTxnTypeValue(CommonUtil.convertObjToStr(objTO.getTxnType()));
    }
    
    public void deleteAgentLeaveTableData(String val, int row) {        
        try {
            if (deletedAgentLeaveDetails == null) {
                deletedAgentLeaveDetails = new LinkedHashMap();
            }
            AgentLeaveDetailsTO objAgentLeaveDetailsTO = (AgentLeaveDetailsTO) agentLeaveDetailsMap.get(row);
            agentLeaveDetailsMap.remove(row);
            System.out.println("i : " + row +" agentLeaveDetailsMap : " + agentLeaveDetailsMap);
            tblAgentLeaveDetailsTab.removeRow(row);
            deletedAgentLeaveDetails.put(row, objAgentLeaveDetailsTO);
            tblAgentLeaveDetailsTab.fireTableDataChanged();
            HashMap replacingMap = new LinkedHashMap();
            replacingMap = agentLeaveDetailsMap;
            if (replacingMap != null) {
                agentLeaveDetailsMap = new LinkedHashMap();
                ArrayList incDataList = new ArrayList(replacingMap.keySet());
                ArrayList addList = new ArrayList(replacingMap.keySet());
                int length = incDataList.size();
                for (int i = 0; i < length; i++) {
                    AgentLeaveDetailsTO agentCommisionSlabRateTO = (AgentLeaveDetailsTO) replacingMap.get(addList.get(i));
                    if (agentCommisionSlabRateTO != null) {
                        agentLeaveDetailsMap.put(i, agentCommisionSlabRateTO);
                    }
                }
                replacingMap = new LinkedHashMap();
                System.out.println("agentLeaveDetailsMap after replacing : " + agentLeaveDetailsMap);
                System.out.println("deletedAgentLeaveDetails after replacing : " + deletedAgentLeaveDetails);
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }
    
    private void populateAgentLeaveTable() throws Exception {
        ArrayList incDataList = new ArrayList();
        HashMap replacingMap = new LinkedHashMap();
        replacingMap = agentLeaveDetailsMap;
        if (replacingMap != null) {
            incDataList = new ArrayList(replacingMap.keySet());
            ArrayList addList = new ArrayList(replacingMap.keySet());
            int length = incDataList.size();
            agentLeaveDetailsMap = new LinkedHashMap();
            for (int i = 0; i < length; i++) {
                ArrayList incTabRow = new ArrayList();
                AgentLeaveDetailsTO objAgentLeaveDetailsTO = (AgentLeaveDetailsTO) replacingMap.get(addList.get(i));
                System.out.println("agentColProdTO^#^#^#^##^^" + objAgentLeaveDetailsTO);
                if (objAgentLeaveDetailsTO != null) {
                    incTabRow.add(objAgentLeaveDetailsTO.getSlNo());
                    incTabRow.add(objAgentLeaveDetailsTO.getlAgentId());
                    incTabRow.add(objAgentLeaveDetailsTO.getlAgentName());
                    incTabRow.add(objAgentLeaveDetailsTO.getcAgentId());
                    incTabRow.add(objAgentLeaveDetailsTO.getcAgentName());
//                    incTabRow.add(objAgentLeaveDetailsTO.getFromDate());
                    incTabRow.add(CommonUtil.convertObjToStr(DateUtil.getStringDate(objAgentLeaveDetailsTO.getFromDate())));
                    incTabRow.add(CommonUtil.convertObjToStr(DateUtil.getStringDate(objAgentLeaveDetailsTO.getToDate())));
//                    incTabRow.add(objAgentLeaveDetailsTO.getToDate());
                    incTabRow.add(CommonConstants.STATUS_MODIFIED);
                    incTabRow.add(objAgentLeaveDetailsTO.getAuthorizedStatus());
                    tblAgentLeaveDetailsTab.insertRow(i, incTabRow);
                    agentLeaveDetailsMap.put(i, objAgentLeaveDetailsTO);
                }
            }
        }
   }

    public int getSelectedTab(){
        return selectedTab;
    }
    
    public void setSelectedTab(int selectedTab) {
        this.selectedTab = selectedTab;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }
}