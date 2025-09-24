/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DailyLoanTransOB.javagetDailyLoanTableTransEnquiry
 *
 * Created on Tue Oct 18 12:40:45 IST 2011
 */

package com.see.truetransact.ui.transaction.dailyDepositTrans;

import com.see.truetransact.ui.termloan.dailyLoanTrans.*;
import java.text.ParseException;
import java.util.Observable;
import java.util.HashMap;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.dailyDepositTrans.DailyAccountTransTO;
import com.see.truetransact.transferobject.transaction.dailyDepositTrans.DailyDepositTransTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.CTable;
import java.sql.SQLException;

//import com.see.truetransact.ui.common.transaction.TransactionOB ;
/**
 *
 * @author  Suresh
 */

public class DailyAccountTransOB extends CObservable{
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType;
    private int result;
    ArrayList tableTitle = new ArrayList();
    ArrayList adjustmentTableTitle = new ArrayList();
    ArrayList authAdjustTableTitle = new ArrayList();
    ArrayList tableList = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private HashMap _authorizeMap;
    private ProxyFactory proxy;
    private HashMap map;
    private List finalList = null;
    private List adjustmentList = null;
    private final static Logger log = Logger.getLogger(DailyAccountTransOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private ArrayList key;
    private ArrayList value;
    private Date currDt = null;
    private EnhancedTableModel tblLoanAdjustmentTable;
    private String agentID = "";
    private String loanCollectionNo = "";
    private String adjustmentAgentID = "";
    private String loanAdjustmentNo = "";
    private String penalwaiveoff="";
    private double commAmount = 0;
    private double totalPaymentAmount = 0;
    private HashMap agentMap;
    private Date collectionDt = null;
    private HashMap importDataMap;
    private EnhancedTableModel tblImportData;
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data;
    private ArrayList _heading;
    private int dataSize;
    private EnhancedTableModel clearTable;
    private ArrayList finaList;
    private String batchId="";
    private String cboAgentType;
    
       private java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy");
    private ComboBoxModel cbmAgentType,cbmLoanAdjustmentAgentType;

    /** Creates a new instance of TDS MiantenanceOB */
    public DailyAccountTransOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "dailyAccountTransJNDI");
            map.put(CommonConstants.HOME, "dailyAccountTransHome");
            map.put(CommonConstants.REMOTE, "dailyAccountTrans");
            //setTableTile();
            fillDropDown();
            tblLoanAdjustmentTable = new EnhancedTableModel(null, adjustmentTableTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    } 
    
   // To Fill the Combo boxes in the UI
    private void fillDropDown() throws Exception {
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List lst = (List)ClientUtil.executeQuery("getAgentMasterDetails", where);
        getMap(lst);
        setCbmAgentType(new ComboBoxModel(key,value));
        setCbmLoanAdjustmentAgentType(new ComboBoxModel(key,value));
    }
    
    private void getMap(List list) throws Exception{
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i=0, j=list.size(); i < j; i++) {
            key.add(((HashMap)list.get(i)).get("KEY"));
            value.add(((HashMap)list.get(i)).get("VALUE"));
        }
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        final List lstData;
        try {
            System.out.println("### wheremap.... : " + whereMap);
            HashMap dailyMap = new HashMap();
            DailyDepositTransTO dailyTo;
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put("TRANS_DT", currDt.clone());
            HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            finaList = new ArrayList();
            if (data != null && data.size() > 0) {
                ArrayList list = (ArrayList)data.get("DAILY_TRANS_DATA");
                for (int k = 0; k < list.size(); k++) {
                    dailyTo = new DailyDepositTransTO();
                    dailyTo = (DailyDepositTransTO) list.get(k);
                    finaList.add(dailyTo);
                }
            }else{
                ClientUtil.showMessageWindow("Data List Is Empty!!");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error In populateData()" + e);
            e.printStackTrace();
        }
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }
    
    public void populateData(HashMap mapID, CTable tblData) {
        _tblData = tblData;
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
        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
        System.out.println("_heading===" + _heading);
        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
          System.out.println("Datas :"+data.size());
        ArrayList tblDatanew = new ArrayList();
        ArrayList tblFinalData = new ArrayList();
        tableTitle = new ArrayList();
        for (int j = 0; j <=_heading.size()-1; j++) {
              tableTitle.add(_heading.get(j));
        }
        for (int i = 0; i <=data.size()-1; i++) {
            List tmpList = (List) data.get(i);
            ArrayList newList = new ArrayList();
            for (int j = 0; j <=_heading.size()-1; j++) {                    
                newList.add(tmpList.get(j));
            }               
            tblDatanew.add(newList);                
        }
        
        tblFinalData.add(tblDatanew);        
        tblImportData = new EnhancedTableModel((ArrayList) tblDatanew, tableTitle);
        setTblImportData(tblImportData);
        setDataSize(data.size());        
    } 
    
    public void clearTable() {
        clearTable = new EnhancedTableModel((ArrayList) null, null);        
    }
          
    private String getCommand(){
        String command = null;
        System.out.println("actionType : " + actionType);
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
    
    private String getAction(){
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

    public  boolean setTableDetails(ArrayList totCustomerList,String custId) throws TTException{              // NEW ACTION
        boolean exceptionFlag = false;
        try{
            ArrayList dataList =  new ArrayList();
            ArrayList glbList= new ArrayList();
            DailyAccountTransTO accountTo;
            List loanList = null;
            String actNosNotFound = "";
            importDataMap = new HashMap();
            for(int j=0;j<totCustomerList.size();j++)
            {               
                ArrayList singleList =(ArrayList)totCustomerList.get(j);                 
                accountTo = new DailyAccountTransTO();
                //dataList = new ArrayList();
                accountTo.setAgentId(custId);
                accountTo.setColDate(getCollectionDt());
                accountTo.setInitiatedBranch(ProxyParameters.BRANCH_ID);
                //System.out.println("singleList^#^#^#"+singleList);
                for(int i=0;i<singleList.size();i++)
                {
                   if(i==0)
                        accountTo.setCol1(CommonUtil.convertObjToStr(singleList.get(i))); 
                   if(i==1)
                        accountTo.setCol2(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==2)
                        accountTo.setCol3(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==3)
                        accountTo.setCol4(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==4)
                        accountTo.setCol5(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==5)
                        accountTo.setCol6(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==6)
                        accountTo.setCol7(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==7)
                        accountTo.setCol8(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==8)
                        accountTo.setCol9(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==9)
                        accountTo.setCol10(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==10)
                        accountTo.setCol11(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==11)
                        accountTo.setCol12(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==12)
                        accountTo.setCol13(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==13)
                        accountTo.setCol14(CommonUtil.convertObjToStr(singleList.get(i)));
                   if(i==14)
                        accountTo.setCol15(CommonUtil.convertObjToStr(singleList.get(i)));                 
                }
                //System.out.println("accountTo%#^#^^#^^$^$"+accountTo);
                dataList.add(accountTo);
            } 
            if(dataList!=null && dataList.size()>0){
                importDataMap.put("IMPORT_DATA_LIST", dataList);
                importDataMap.put("COMMAND", "IMPORT");
                doActionPerform(importDataMap);
                exceptionFlag = true;
            }
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
        return exceptionFlag;
    }
    
    public  boolean getUnauthorizedAgentData(String agentId) throws TTException{
        boolean authFlag = false;
        HashMap authMap = new HashMap();
        authMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        authMap.put("AGENT_ID", agentId);
        authMap.put("TRANS_DT", currDt.clone());
        List authList = ClientUtil.executeQuery("getUnauthorizedAgentTransaction", authMap);
        if(authList!=null && authList.size()>0){
            authFlag = true;
        }else{
            authFlag = false;
        }
        return authFlag;
    }
    
    public  boolean getImportedAgentExistData(String agentId) throws TTException{
        boolean existFlag = false;
        HashMap existMap = new HashMap();
        existMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        existMap.put("AGENT_ID", agentId);
        existMap.put("TABLE_NAME", getAgentMap().get("IMP_QUERY"));  
        List existList = ClientUtil.executeQuery("getImportDailyAccountData", existMap);
        if(existList!=null && existList.size()>0){
            existFlag = true;
        }else{
            existFlag = false;
        }
        return existFlag;
    }
    
    public DailyDepositTransTO SetDailydepositTrans(HashMap where) {
        DailyDepositTransTO daily = new DailyDepositTransTO();
        try{            
            daily.setColl_dt(getCollectionDt());
            daily.setAcct_num(CommonUtil.convertObjToStr(where.get("CBMS_ACTNUM")));        
            daily.setProd_Type(CommonUtil.convertObjToStr(where.get("PROD_TYPE")));
            daily.setAgent_no(CommonUtil.convertObjToStr(where.get("AGENT_ID")));
            daily.setAmount(CommonUtil.convertObjToDouble(where.get("AMT")));
            daily.setCreated_by(ProxyParameters.USER_ID);
            daily.setCreated_dt((Date)currDt.clone());
            daily.setInitiatedBranch(CommonUtil.convertObjToStr(where.get("INITIATED_BRANCH")));
            daily.setTrn_dt((Date)currDt.clone());
            daily.setTrans_mode(CommonConstants.TX_TRANSFER);
            daily.setTrans_type(CommonConstants.CREDIT);
            daily.setStatus(CommonConstants.STATUS_CREATED);
            daily.setConsolidated(CommonUtil.convertObjToStr(where.get("CONSOLIDATE_TRANS")));
        }catch(Exception e){
            e.printStackTrace();
        }
        return daily;
    }
    
    public HashMap SetingForDailydepositTrans(HashMap dataMap) {
        System.out.println("dataMap in SetingForDailydepositTrans$^^&"+dataMap);
        HashMap finalMap = new HashMap();
        try{
            HashMap whereMap = new HashMap();   
            double cmmissionAmount = 0.0;
            ArrayList translst = new ArrayList();
            ArrayList depositlst = new ArrayList();
            ArrayList cashList = new ArrayList();
            //Credit Transfer
            dataMap.put("TABLE_NAME", getAgentMap().get("IMP_QUERY"));   
            System.out.println("dataMap%#$%#$%$#+dataMap"+dataMap);
            List consolidateList = ClientUtil.executeQuery("getImportDailyAccountConsolidateDataDetails", dataMap);            
            if(consolidateList!=null && consolidateList.size()>0){
                 //Added for consolidated transaction
                for (int i = 0; i < consolidateList.size(); i++) {
                    whereMap = (HashMap) consolidateList.get(i);   
                    System.out.println("whereMap%#$%#$%$#+consolidateList"+whereMap);
                    if(whereMap!=null && !whereMap.get("CONSOLIDATE_TRANS").equals("") && whereMap.get("CONSOLIDATE_TRANS").equals("Y")){
                            translst.add(setConsolidCreditTransferTo(whereMap));
                            //Commission credit transaction                
                            translst.add(setCommissionCreditTransferTo(whereMap));
                            System.out.println("translst%#$%#$%$#+consolidateList"+translst);
                            dataMap.put("PROD_ID", whereMap.get("PROD_ID")); 
                            dataMap.put("BRANCH_ID", whereMap.get("BRANCH_ID")); 
                            List creditList = ClientUtil.executeQuery("getImportDailyAccountDataDetails", dataMap);
                            if(creditList!=null && creditList.size()>0){
                                HashMap depositMap = new HashMap(); 
                                for (int j = 0; j < creditList.size(); j++) {
                                    depositMap = (HashMap) creditList.get(j) ; 
                                    depositMap.put("CONSOLIDATE_TRANS",whereMap.get("CONSOLIDATE_TRANS"));
                                    System.out.println("depositMap%#$%#$%$#+depositMap"+translst);
                                    depositlst.add(SetDailydepositTrans(depositMap));
                                }
                            }
                    }else{
                        dataMap.put("PROD_ID", whereMap.get("PROD_ID")); 
                        dataMap.put("BRANCH_ID", whereMap.get("BRANCH_ID")); 
                        List creditList1 = ClientUtil.executeQuery("getImportDailyAccountDataDetails", dataMap);
                        HashMap creditListMap = new HashMap(); 
                        if(creditList1!=null && creditList1.size()>0){
                            for (int j = 0; j < creditList1.size(); j++) {
                                creditListMap = (HashMap) creditList1.get(j) ;   
                                System.out.println("creditListMap%#$%#$%$#+creditListMap"+creditListMap);
                                translst.add(setCreditTransferTo(creditListMap));
                                //Commission credit transaction  
                                creditListMap.put("CONSOLIDATE_TRANS_NO","CONSOLIDATE_TRANS_NO");// Added by nithya on 12-03-2019 for KD 433 - Creating new report for agent commission collection details
                                translst.add(setCommissionCreditTransferTo(creditListMap));
                                creditListMap.put("CONSOLIDATE_TRANS","N"); // some time field value null so hardcoded to N( else always  N)
                                depositlst.add(SetDailydepositTrans(creditListMap));
                            }
                        }
                    }
                }                
            }else{
                throw new TTException("Credit Transfer Transactions setting error!!!");
            }
            
            //Debit Transfer
            List debitList = ClientUtil.executeQuery("getImportDailyAccountDataDetailsForCash", dataMap);
            if(debitList!=null && debitList.size()>0){
                for (int i = 0; i < debitList.size(); i++) {
                    whereMap = (HashMap) debitList.get(i);
                    System.out.println("debit whereMap$#^#^#^"+whereMap);
                    translst.add(setDebitTransferTo(whereMap)); 
                    //Cash Transaction
                    cashList.add(setCashTo(whereMap));
                }
            }else{
                throw new TTException("Debit Transfer Transactions setting error!!!");
            }
            if(translst!=null && translst.size()>0 && depositlst!=null && depositlst.size()>0){
                finalMap.put("TxTransferTO", translst);
                finalMap.put("ACTION", "NEW");
                finalMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                finalMap.put("DAILYDEPOSITTRANSTO", depositlst);
                if (cashList != null && cashList.size() > 0) {
                    finalMap.put("CashTransactionTO", cashList); 
                    finalMap.put("CASH_DAILY","CASH_DAILY");
                }else{
                    throw new TTException("Cash Transaction setting error!!!");
                }

            }else{
                 throw new TTException("Transaction setting error!!!");
            }
            //}        
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
        return finalMap;
    }
    
    public CashTransactionTO setCashTo(HashMap cashMap){
        CashTransactionTO cashTO = new CashTransactionTO();
        try{
            if(cashMap.containsKey("COL_PROD_TYPE") && CommonUtil.convertObjToStr(cashMap.get("COL_PROD_TYPE"))!=null &&
                    CommonUtil.convertObjToStr(cashMap.get("COL_PROD_TYPE")).equalsIgnoreCase("GL")){
                    cashTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get("COL_AC_HD_ID")));
                    cashTO.setProdType(CommonUtil.convertObjToStr(cashMap.get("COL_PROD_TYPE")));
                    cashTO.setTransModType(CommonUtil.convertObjToStr(cashMap.get("COL_PROD_TYPE")));
                    cashTO.setBranchId(ProxyParameters.BRANCH_ID);
            }else{
                cashTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get("DEBIT_AC_HD")));
                cashTO.setActNum(CommonUtil.convertObjToStr(cashMap.get("COL_AC_HD_ID")));
                cashTO.setProdId(CommonUtil.convertObjToStr(cashMap.get("COL_PROD_ID")));
                cashTO.setTransModType(CommonUtil.convertObjToStr(cashMap.get("COL_PROD_TYPE")));
                cashTO.setProdType(CommonUtil.convertObjToStr(cashMap.get("COL_PROD_TYPE")));
                cashTO.setBranchId(CommonUtil.convertObjToStr(cashMap.get("BRANCH_ID")));
            }
            cashTO.setInitiatedBranch(CommonUtil.convertObjToStr(cashMap.get("INITIATED_BRANCH")));
            cashTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMT"))+CommonUtil.convertObjToDouble(cashMap.get("COMM_AMT")));
            cashTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMT"))+CommonUtil.convertObjToDouble(cashMap.get("COMM_AMT")));            
            cashTO.setTransType(CommonConstants.CREDIT);
            cashTO.setTransDt((Date)currDt.clone());
            cashTO.setInitTransId(ProxyParameters.USER_ID);
            cashTO.setStatus(CommonConstants.STATUS_CREATED);
            cashTO.setStatusBy(ProxyParameters.USER_ID);
            cashTO.setStatusDt((Date)currDt.clone());;
            cashTO.setParticulars("Agent Collection  "+CommonUtil.convertObjToStr(cashMap.get("AGENT_ID")));
            cashTO.setInstType("VOUCHER");
            cashTO.setGlTransActNum(CommonUtil.convertObjToStr(cashMap.get("AGENT_ID")));
            cashTO.setCommand(CommonUtil.convertObjToStr(getCommand()));                   
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("cashTO%#%@%"+cashTO);
        return cashTO;
    }
    
    public TxTransferTO setCreditTransferTo(HashMap where){
        TxTransferTO trnsferTO = new TxTransferTO();
        try{
            trnsferTO.setProdType(CommonUtil.convertObjToStr(where.get("PROD_TYPE")));
            trnsferTO.setProdId(CommonUtil.convertObjToStr(where.get("PROD_ID")));
            trnsferTO.setTransModType(CommonUtil.convertObjToStr(where.get("PROD_TYPE")));
            trnsferTO.setAmount(CommonUtil.convertObjToDouble(where.get("AMT")));
            trnsferTO.setInpAmount(CommonUtil.convertObjToDouble(where.get("AMT")));
            trnsferTO.setTransType(CommonConstants.CREDIT);
            trnsferTO.setActNum(CommonUtil.convertObjToStr(where.get("CBMS_ACTNUM")));
            trnsferTO.setAcHdId(CommonUtil.convertObjToStr(where.get("CR_ACHD"))); 
            trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
            trnsferTO.setBranchId(CommonUtil.convertObjToStr(where.get("BRANCH_ID")));
            trnsferTO.setInitiatedBranch(CommonUtil.convertObjToStr(where.get("INITIATED_BRANCH")));
            trnsferTO.setTransDt((Date)currDt.clone());
            trnsferTO.setParticulars(CommonUtil.convertObjToStr(where.get("AGENT_ID")) +" Agent Collection From "+CommonUtil.convertObjToStr(where.get("CBMS_ACTNUM")));
            trnsferTO.setLinkBatchId(CommonUtil.convertObjToStr(where.get("CBMS_ACTNUM")));
            trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
            trnsferTO.setStatusBy(ProxyParameters.USER_ID);
            trnsferTO.setStatusDt((Date)currDt.clone());
            trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(where.get("AGENT_ID")));
            trnsferTO.setAuthorizeStatus("DAILY");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Credit trnsferTO%#%@%"+trnsferTO);
        return trnsferTO;
    }   
        
    public TxTransferTO setConsolidCreditTransferTo(HashMap where){
        TxTransferTO trnsferTO = new TxTransferTO();
        try{
            trnsferTO.setProdType(TransactionFactory.GL);
//            trnsferTO.setTransModType(CommonUtil.convertObjToStr(where.get("PROD_TYPE")));
            trnsferTO.setTransModType(TransactionFactory.GL);
            trnsferTO.setAmount(CommonUtil.convertObjToDouble(where.get("AMT")));
            trnsferTO.setInpAmount(CommonUtil.convertObjToDouble(where.get("AMT")));
            trnsferTO.setTransType(CommonConstants.CREDIT);
            trnsferTO.setAcHdId(CommonUtil.convertObjToStr(where.get("CR_ACHD"))); 
            trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
            trnsferTO.setBranchId(CommonUtil.convertObjToStr(where.get("BRANCH_ID")));
            trnsferTO.setInitiatedBranch(CommonUtil.convertObjToStr(where.get("INITIATED_BRANCH")));
            trnsferTO.setTransDt((Date)currDt.clone());
            trnsferTO.setParticulars(CommonUtil.convertObjToStr(where.get("AGENT_ID")) +" Agent Collection");           
            trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
            trnsferTO.setStatusBy(ProxyParameters.USER_ID);
            trnsferTO.setStatusDt((Date)currDt.clone());
            trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(where.get("AGENT_ID")));
            trnsferTO.setAuthorizeStatus("DAILY");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Credit consolidate trnsferTO%#%@%"+trnsferTO);
        return trnsferTO;
    }   
    
        public TxTransferTO setCommissionCreditTransferTo(HashMap where){
        TxTransferTO trnsferTO = new TxTransferTO();
        try{
            System.out.println("inside setCommissionCreditTransferTo where" + where);
            trnsferTO.setProdType(TransactionFactory.GL);
            trnsferTO.setTransModType(TransactionFactory.GL);
            trnsferTO.setAmount(CommonUtil.convertObjToDouble(where.get("COMM_AMT")));
            trnsferTO.setInpAmount(CommonUtil.convertObjToDouble(where.get("COMM_AMT")));
            trnsferTO.setTransType(CommonConstants.CREDIT);            
            trnsferTO.setAcHdId(CommonUtil.convertObjToStr(where.get("COMM_COL_AC_HD_ID"))); 
            trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
            trnsferTO.setBranchId(CommonUtil.convertObjToStr(where.get("BRANCH_ID")));
            trnsferTO.setInitiatedBranch(CommonUtil.convertObjToStr(where.get("INITIATED_BRANCH")));
            trnsferTO.setTransDt((Date)currDt.clone());
            trnsferTO.setParticulars(CommonUtil.convertObjToStr(where.get("AGENT_ID")) +" Agent Collection Commission");
            //trnsferTO.setLinkBatchId(getCboAgentType());
            // Added by nithya on 12-03-2019 for KD 433 - Creating new report for agent commission collection details
            if (where.containsKey("CONSOLIDATE_TRANS_NO") && where.get("CONSOLIDATE_TRANS_NO") != null && CommonUtil.convertObjToStr(where.get("CONSOLIDATE_TRANS_NO")).equalsIgnoreCase("CONSOLIDATE_TRANS_NO")) {
                if (where.containsKey("CBMS_ACTNUM") && where.get("CBMS_ACTNUM") != null) {
                    trnsferTO.setLinkBatchId(CommonUtil.convertObjToStr(where.get("CBMS_ACTNUM")));
                }
            }
            trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
            trnsferTO.setStatusBy(ProxyParameters.USER_ID);
            trnsferTO.setStatusDt((Date)currDt.clone());
            trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(where.get("AGENT_ID")));
            trnsferTO.setAuthorizeStatus("DAILY");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Credit commission trnsferTO%#%@%"+trnsferTO);
        return trnsferTO;
    }
        
    public TxTransferTO setDebitTransferTo(HashMap where){
        TxTransferTO trnsferTO = new TxTransferTO();
        try{
            if(where.containsKey("COL_PROD_TYPE") && CommonUtil.convertObjToStr(where.get("COL_PROD_TYPE"))!=null &&
                    CommonUtil.convertObjToStr(where.get("COL_PROD_TYPE")).equalsIgnoreCase("GL")){
                    trnsferTO.setAcHdId(CommonUtil.convertObjToStr(where.get("COL_AC_HD_ID")));
                    trnsferTO.setProdType(CommonUtil.convertObjToStr(where.get("COL_PROD_TYPE")));
                    trnsferTO.setTransModType(CommonUtil.convertObjToStr(where.get("COL_PROD_TYPE")));
                    trnsferTO.setBranchId(ProxyParameters.BRANCH_ID);
            }else{
                trnsferTO.setAcHdId(CommonUtil.convertObjToStr(where.get("DEBIT_AC_HD")));
                trnsferTO.setActNum(CommonUtil.convertObjToStr(where.get("COL_AC_HD_ID")));
                trnsferTO.setProdId(CommonUtil.convertObjToStr(where.get("COL_PROD_ID")));
                trnsferTO.setTransModType(CommonUtil.convertObjToStr(where.get("COL_PROD_TYPE")));
                trnsferTO.setProdType(CommonUtil.convertObjToStr(where.get("COL_PROD_TYPE")));
                trnsferTO.setBranchId(CommonUtil.convertObjToStr(where.get("BRANCH_ID")));
            }
            trnsferTO.setAmount(CommonUtil.convertObjToDouble(where.get("AMT"))+CommonUtil.convertObjToDouble(where.get("COMM_AMT")));
            trnsferTO.setInpAmount(CommonUtil.convertObjToDouble(where.get("AMT"))+CommonUtil.convertObjToDouble(where.get("COMM_AMT")));
            trnsferTO.setTransType(CommonConstants.DEBIT);        
            trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);            
            trnsferTO.setInitiatedBranch(CommonUtil.convertObjToStr(where.get("INITIATED_BRANCH")));
            trnsferTO.setTransDt((Date)currDt.clone());
            trnsferTO.setParticulars("Agent Collection From "+CommonUtil.convertObjToStr(where.get("AGENT_ID")));
            //trnsferTO.setLinkBatchId(getCboAgentType());
            trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
            trnsferTO.setStatusBy(ProxyParameters.USER_ID);
            trnsferTO.setStatusDt((Date)currDt.clone());
            trnsferTO.setInstType("VOUCHER");
            trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(where.get("AGENT_ID")));
            trnsferTO.setAuthorizeStatus("DAILY");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Debit trnsferTO%#%@%"+trnsferTO);
        return trnsferTO;
    }
        
    public void setTableFinalMap() {   
        if(dataHash!=null && !dataHash.isEmpty() && dataHash.size()>0){   
            System.out.println("dataHash^$^#^^^#^"+dataHash);
            HashMap key = new HashMap();
            HashMap value = new HashMap();
            List keyList = (List) dataHash.get(CommonConstants.TABLEHEAD);
            List valueList = (List) dataHash.get(CommonConstants.TABLEDATA);
            for(int i=0;i<keyList.size();i++){
                for(int j=0;j<valueList.size();j++){
                    key.put(keyList.get(i), valueList.get(j));
                }               
            }
            //setFinLaMap(dataHash);        
        }
    }
    /** To perform the appropriate operation */
    public void doAction(HashMap map) {
        try {
            doActionPerform(map);
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /**
     * To perform the necessary action
     */
    private void doActionPerform(HashMap dataMap) throws Exception {
        final HashMap data = new HashMap();  
        dataMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        try{
        if(dataMap.containsKey("COMMAND") && dataMap.get("COMMAND").equals("IMPORT")){
            data.put("IMPORT_DATA_LIST", dataMap.get("IMPORT_DATA_LIST"));
            data.put("COMMAND",dataMap.get("COMMAND"));
        }else if(dataMap.containsKey("COMMAND") && dataMap.get("COMMAND").equals(CommonConstants.TOSTATUS_INSERT)){
            data.putAll(SetingForDailydepositTrans(dataMap));
            data.put("AGENT_ID", dataMap.get("AGENT_ID"));
            data.put("COL_DATE", dataMap.get("COL_DATE"));
        }else if(dataMap.containsKey("COMMAND") && dataMap.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)){
            data.put("COMMAND",dataMap.get("COMMAND"));
            data.put("AGENT_ID", dataMap.get("AGENT_ID"));
            data.put("COL_DATE", dataMap.get("COL_DATE"));
        }else if(dataMap.containsKey("COMMAND") && dataMap.get("COMMAND").equals(CommonConstants.AUTHORIZESTATUS) && getAuthorizeMap()!=null){
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            _authorizeMap.put("DAILY", "DAILY");
            data.put("AGENT_ID", dataMap.get("AGENT_ID"));
            data.put("COL_DATE", dataMap.get("COL_DATE"));
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            System.out.println("finaList^^^^^^^^^^^$$$" + finaList);
            data.put("DAILYDEPOSITTRANSTO", finaList);
            System.out.println("data in auth^^^^^^^^^$$$" + data);
            data.put("BATCH_ID",getBatchId());  
            data.put("COMMAND",dataMap.get("COMMAND"));
        }
        data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        System.out.println("#$########## data : "+ data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        System.out.println("proxyResultMap###### : " + proxyResultMap);        
        setResult(actionType);
        _authorizeMap = null;
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
            e.printStackTrace();
            ClientUtil.showAlertWindow(e.getMessage());
        }
    }
    
    public String getAdjustmentAgentID() {
        return adjustmentAgentID;
    }

    public void setAdjustmentAgentID(String adjustmentAgentID) {
        this.adjustmentAgentID = adjustmentAgentID;
    }

    public String getLoanAdjustmentNo() {
        return loanAdjustmentNo;
    }

    public void setLoanAdjustmentNo(String loanAdjustmentNo) {
        this.loanAdjustmentNo = loanAdjustmentNo;
    }
    
    public void resetAdjustmentTableValues(){
        tblLoanAdjustmentTable.setDataArrayList(null,adjustmentTableTitle);
    }
    
    public ComboBoxModel getCbmLoanAdjustmentAgentType() {
        return cbmLoanAdjustmentAgentType;
    }

    public void setCbmLoanAdjustmentAgentType(ComboBoxModel cbmLoanAdjustmentAgentType) {
        this.cbmLoanAdjustmentAgentType = cbmLoanAdjustmentAgentType;
    }
    
    public EnhancedTableModel getTblLoanAdjustmentTabls() {
        return tblLoanAdjustmentTable;
    }

    public void setTblLoanAdjustmentTable(EnhancedTableModel tblLoanAdjustmentTable) {
        this.tblLoanAdjustmentTable = tblLoanAdjustmentTable;
    }
    
    public void resetForm(){
        ArrayList tableList = new ArrayList();
        setChanged();
        setLoanCollectionNo("");
        setLoanAdjustmentNo("");
        setProxyReturnMap(null);
        //setAgentManullp(null);
        //setFinLaMap(null);
    }
    
    public List getAdjustmentList() {
        return adjustmentList;
    }

    public void setAdjustmentList(List adjustmentList) {
        this.adjustmentList = adjustmentList;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /**
     * Getter for property tableTitle.
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }
    
    /**
     * Setter for property tableTitle.
     * @param tableTitle New value of property tableTitle.
     */
    public void setTableTitle(java.util.ArrayList tableTitle) {
        this.tableTitle = tableTitle;
    }
    
    /**
     * Getter for property tableList.
     * @return Value of property tableList.
     */
    public java.util.ArrayList getTableList() {
        return tableList;
    }
    
    /**
     * Setter for property tableList.
     * @param tableList New value of property tableList.
     */
    public void setTableList(java.util.ArrayList tableList) {
        this.tableList = tableList;
    }
    
   
    
    /**
     * Getter for property finalList.
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }
    
    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }
     public void setFinalList1(ArrayList finalList) {
        for(int i=0;i<finalList.size();i++)
        {
            List lst= (List)finalList.get(i);
        }
    }
    /**
     * Getter for property cbmAgentType.
     * @return Value of property cbmAgentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAgentType() {
        return cbmAgentType;
    }    
    
    /**
     * Setter for property cbmAgentType.
     * @param cbmAgentType New value of property cbmAgentType.
     */
    public void setCbmAgentType(com.see.truetransact.clientutil.ComboBoxModel cbmAgentType) {
        this.cbmAgentType = cbmAgentType;
    }    
    
    /**
     * Getter for property loanCollectionNo.
     * @return Value of property loanCollectionNo.
     */
    public java.lang.String getLoanCollectionNo() {
        return loanCollectionNo;
    }
    
    /**
     * Setter for property loanCollectionNo.
     * @param loanCollectionNo New value of property loanCollectionNo.
     */
    public void setLoanCollectionNo(java.lang.String loanCollectionNo) {
        this.loanCollectionNo = loanCollectionNo;
    }
    
    /**
     * Getter for property agentID.
     * @return Value of property agentID.
     */
    public java.lang.String getAgentID() {
        return agentID;
    }
    
    /**
     * Setter for property agentID.
     * @param agentID New value of property agentID.
     */
    public void setAgentID(java.lang.String agentID) {
        this.agentID = agentID;
    }
    public String getPenalwaiveoff() {
        return penalwaiveoff;
    }

    public void setPenalwaiveoff(String penalwaiveoff) {
        this.penalwaiveoff = penalwaiveoff;
    }

    public double getCommAmount() {
        return commAmount;
    }

    public void setCommAmount(double commAmount) {
        this.commAmount = commAmount;
    }

    public double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    public HashMap getAgentMap() {
        return agentMap;
    }

    public void setAgentMap(HashMap agentMap) {
        this.agentMap = agentMap;
    }

    public Date getCollectionDt() {
        return collectionDt;
    }

    public void setCollectionDt(Date collectionDt) {
        this.collectionDt = collectionDt;
    }

    public EnhancedTableModel getTblImportData() {
        return tblImportData;
    }

    public void setTblImportData(EnhancedTableModel tblImportData) {
        this.tblImportData = tblImportData;
    }
    
    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
    
    public EnhancedTableModel getClearTable() {
        return clearTable;
    }

    public void setClearTable(EnhancedTableModel clearTable) {
        this.clearTable = clearTable;
    }

    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getCboAgentType() {
        return cboAgentType;
    }

    public void setCboAgentType(String cboAgentType) {
        this.cboAgentType = cboAgentType;
    }
    
}