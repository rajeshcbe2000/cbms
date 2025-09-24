/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ActTransOB.java
 *
 * Created on December 23, 2004, 4:47 PM
 */

package com.see.truetransact.ui.termloan.accounttransfer;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.accounttransfer.ActTransTO;
import com.see.truetransact.transferobject.termloan.settlement.SettlementTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
/**
 *
 * @author  152713
 */
public class ActTransOB extends CObservable{
    
    /** Creates a new instance of PowerOfAttorneyOB */
    public ActTransOB(String strModule){
        settlementOB(strModule);
    }
    
     private HashMap operationMap;
     private ProxyFactory proxy;
    private       static ActTransOB actTransOB;
    private final static Logger log = Logger.getLogger(ActTransOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final   java.util.ResourceBundle objactTransRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.accounttransfer.ActTransRB", ProxyParameters.LANGUAGE);
//    private final   PowerOfAttorneyRB objPowerOfAttorneyRB = new PowerOfAttorneyRB();
    
//    public          String  strMaxDelSlNoMapName = "";
//    private final   String  ADDR_TYPE = "ADDR_TYPE";
//    private final   String  ALL_VALUES = "ALL_VALUES";
//    private final   String  AREA = "AREA";
//    private final   String  CITY = "CITY";
//    private final   String  COMMAND = "COMMAND";
//    private final   String  COUNTRY = "COUNTRY";
//    private final   String  COUNTRY_CODE = "COUNTRY_CODE";
//    private final   String  ONBEHALFOF = "ONBEHALFOF";
//    private final   String  CUSTOMER_ID = "CUSTOMER_ID";
//    private final   String  FROM = "FROM";
//    private final   String  HOLDERNAME = "HOLDERNAME";
//    private final   String  INSERT = "INSERT";
//    private final   String  OPTION = "OPTION";
//    private final   String  PHONE = "PHONE";
//    private final   String  PIN = "PIN";
//    private final   String  PIN_CODE = "PIN_CODE";
//    private final   String  POANO = "POANO";
//    
//    private final   String  STATE = "STATE";
//    private final   String  STREET = "STREET";
//    private final   String  TABLE_VALUES = "TABLE_VALUES";
//    private final   String  TO = "TO";
//    private final   String  UPDATE = "UPDATE";
//    
//    //settle
//    private final   String  SLNO = "SLNO";
//    private final   String  CHQFROM = "CHQFROM";
//    private final   String  CHQTO = "CHQTO";
//    private final   String  QTY = "QTY";
//    private final   String  CHQDT = "CHQDT";
//    private final   String  CHQAMT = "CHQAMT";
//    private final   String  CLRDT = "CLRDT";
////    private final   String  REMARK = "REMARK";
//    private final   String  REMARK = "REMARK";
//    
//    private final   ArrayList poaTabTitle = new ArrayList();
//    private ArrayList poaEachTabRecords;
//    private ArrayList poaAllTabRecords;                                 // ArrayList of ArrayList to display in PoA
//    
//    private LinkedHashMap poaAllRecords = new LinkedHashMap();          // Both displayed and hidden values in the table
//    
    private ArrayList key;
    private ArrayList value;
    private HashMap lookUpHash;
//    private HashMap poaEachRecords;
    private HashMap keyValue;
//    
//    private EnhancedTableModel tblPoATab;
//    
//    private ComboBoxModel cbmCity_PowerAttroney;
//    private ComboBoxModel cbmState_PowerAttroney;
//    private ComboBoxModel cbmCountry_PowerAttroney;
//    private ComboBoxModel cbmPoACust;
//    private ComboBoxModel cbmAddrType_PoA;
//    
//    private TableUtil tableUtilPoA = new TableUtil();
//    
//    private String borrowerNo = "";
//    private String txtCustID_PoA = "";
//    private String cboPoACust = "";
//    private String cboAddrType_PoA = "";
//    private String txtPoANo = "";
//    private String txtPoaHolderName = "";
//    private String txtStreet_PowerAttroney = "";
//    private String txtArea_PowerAttroney = "";
//    private String cboCity_PowerAttroney = "";
//    private String cboState_PowerAttroney = "";
//    private String cboCountry_PowerAttroney = "";
//    private String txtPin_PowerAttroney = "";
//    private String txtPhone_PowerAttroney = "";
//    private String tdtPeriodFrom_PowerAttroney = "";
//    private String tdtPeriodTo_PowerAttroney = "";
//    private String txtRemark_PowerAttroney = "";
    private Date curDate = null;
    
    //settlement
    
    private ComboBoxModel cbmBankName;
    private ComboBoxModel cbmBranchName;
//    private ComboBoxModel cbmBounReason;
    
    
    private String cboBankName = "";
    private String cboBranchName = "";
    private String txtRefNo = "";
    private String txtAmt = "";
    private String txtSecDepRec = "";
    private String txtPoDdNo = "";
    private String tdtPoDdDate = "";
    private String txtPoDdAmt = "";
    private String actCommand="";
    private String acctNum="";
//    private String tdtClearingDt = "";
//    private String cboBounReason = "";
    private String txtRemarks = "";
//    private boolean rdoActype_Sb = false;
//    private boolean rdoActype_Ca = false;
//    private boolean rdoActype_Od = false;
//    private boolean rdoSetMode_Ecs = false;
//    private boolean rdoSetMode_Pdc = false;
//    private boolean rdoSetMode_Othrs = false;
//    private boolean rdoChqBounce_Yes = false;
//    private boolean rdoChqBounce_No = false;
//    private boolean rdoReturnChq_Yes = false;
//    private boolean rdoReturnChq_No = false;
//    
//    private CTable _tblData;
//    
//     private ArrayList _heading;
//    private HashMap dataHash;
//    private ArrayList data;
//     private boolean _isAvailable = true;
//    private TableModel _tableModel;
    
    private void settlementOB(String strModule){
        try{
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            curDate = ClientUtil.getCurrentDate();
            fillDropdown();
//            setPoATabTitle();
//            tableUtilPoA.setAttributeKey(POANO);
//            tblPoATab = new EnhancedTableModel(null, poaTabTitle);
//            setStrMaxDelSlNoMapName(strModule);
        }catch(Exception e){
            System.out.println("Exception in powerOfAttorneyOB()..."+e);
            parseException.logException(e,true);
        }
    }
    private void setOperationMap() throws Exception{      
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "ExchangeRateJNDI");
        operationMap.put(CommonConstants.HOME, "forex.ExchangeRateHome");
        operationMap.put(CommonConstants.REMOTE, "forex.ExchangeRate");
    }
//    private void setPoATabTitle() throws Exception{
//        try {
//            poaTabTitle.add(objSettlementRB.getString("tblColumnPoA1"));
//            poaTabTitle.add(objSettlementRB.getString("tblColumnPoA2"));
//            poaTabTitle.add(objSettlementRB.getString("tblColumnPoA3"));
//            poaTabTitle.add(objSettlementRB.getString("tblColumnPoA4"));
//            poaTabTitle.add(objSettlementRB.getString("tblColumnPoA5"));
////            poaTabTitle.add(objSettlementRB.getString("tblColumnPoA6"));
//        }catch(Exception e) {
//            System.out.println("Exception in setPoATabTitle()..."+e);
//            parseException.logException(e,true);
//        }
//    }
    
//    public void setStrMaxDelSlNoMapName(String module){
//        this.strMaxDelSlNoMapName = "getSelectPowerAttorneyMaxSLNO" + module;
//    }
//    
//    private String getStrMaxDelSlNoMapName(){
//        return this.strMaxDelSlNoMapName;
//    }
    
    /**
     * To populate the appropriate keys and values of all the combo
     * boxes in the screen at the time of TermLoanOB instance creation
     * @throws Exception will throw it to the TermLoanOB constructor
     */
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        
        ArrayList lookup_keys = new ArrayList();
//        lookup_keys.add("INWARD.BOUNCING_REASON");
//        lookup_keys.add("CUSTOMER.CITY");
//        lookup_keys.add("CUSTOMER.STATE");
//        lookup_keys.add("CUSTOMER.COUNTRY");
//        
//        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
////        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
//        getKeyValue((HashMap)keyValue.get("INWARD.BOUNCING_REASON"));
//        setCbmBounReason(new ComboBoxModel(key, value));
//        
//        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
//        setCbmCity_PowerAttroney(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
//        setCbmState_PowerAttroney(new ComboBoxModel(key,value));
//        
//        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
//        setCbmCountry_PowerAttroney(new ComboBoxModel(key,value));
        
        lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getBank");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        cbmBankName = new ComboBoxModel(key,value);
//        setBlankKeyValue();
//        setCbmPoACust(new ComboBoxModel(key, value));
        
        lookup_keys = null;
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void setBlankKeyValue(){
        key = new ArrayList();
        key.add("");
        value = new ArrayList();
        value.add("");
    }
    
//    private void resetPowerOfAttorneyTable(){
//        tblPoATab = new EnhancedTableModel(null, poaTabTitle);
//        ttNotifyObservers();
//    }
    
    public void resetAllFieldsInPoA(){
        resetPoAForm();
        destroyObjects();
        createObject();
//        clearCboPoACust_ID();
//        resetPowerOfAttorneyTable();
    }
    
    public void resetPoAForm(){
//        setCboPoACust("");
//        setCboAddrType_PoA("");
//        setTxtPoANo("");
//        setCboCountry_PowerAttroney("");
//        setCboCity_PowerAttroney("");
//        setTdtPeriodFrom_PowerAttroney("");
//        setTdtPeriodTo_PowerAttroney("");
//        setTxtArea_PowerAttroney("");
//        setTxtPhone_PowerAttroney("");
//        setTxtPin_PowerAttroney("");
//        setTxtPoaHolderName("");
//        setTxtRemark_PowerAttroney("");
//        setCboState_PowerAttroney("");
//        setTxtStreet_PowerAttroney("");
//        setTxtCustID_PoA("");
//        setCboBankName("");
//        setCboBranchName("");
//        setTxtActNo("");
//        setRdoActype_Ca(false);
//        setRdoActype_Od(false);
//        setRdoActype_Sb(false);
//        setRdoSetMode_Ecs(false);
//        setRdoSetMode_Othrs(false);
//        setRdoSetMode_Pdc(false);
//        setRdoChqBounce_No(false);
//        setRdoChqBounce_Yes(false);
//        setRdoReturnChq_No(false);
//        setRdoReturnChq_Yes(false);
//        setTxtFromChqNo("");
//        setTxtToChqNo("");
//        setTxtQty("");
//        setTdtChqDate("");
//        setTxtRemarks("");
//        setTdtClearingDt("");
//        setTxtChqAmt("");
        ttNotifyObservers();
        
    }
     public void resetSetBnkForm(){
        setCboBankName("");
        setCboBranchName("");
//        setTxtActNo("");
//        setRdoActype_Ca(false);
//        setRdoActype_Od(false);
//        setRdoActype_Sb(false);
//        setRdoSetMode_Ecs(false);
//        setRdoSetMode_Othrs(false);
//        setRdoSetMode_Pdc(false);
        ttNotifyObservers();
    }
     
    public void setActTransTO(HashMap dataMap){
        try{
            List  list = (List) dataMap.get("ActTransTO");
            ActTransTO objActTransTO = (ActTransTO) (list.get(0));
//            ((String) getCbmCrossing().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getCrossing())));
//            ((String) getCbmCrossing().getDataForKey(CommonUtil.convertObjToStr(objRemittanceIssueTO.getCrossing())));
            setCboBankName((String) getCbmBankName().getDataForKey(CommonUtil.convertObjToStr(objActTransTO.getBankName())));
            
            getBranchData(CommonUtil.convertObjToStr(objActTransTO.getBankName()));
            setCboBranchName((String) getCbmBranchName().getDataForKey(CommonUtil.convertObjToStr(objActTransTO.getBranchName())));
            setTxtRefNo(objActTransTO.getToRefNo());
            setTxtSecDepRec(objActTransTO.getSecDocRec());
            setTxtAmt(CommonUtil.convertObjToStr(objActTransTO.getToAmt()));
            setTxtPoDdNo(objActTransTO.getPoddNo());
            setTxtPoDdAmt(CommonUtil.convertObjToStr(objActTransTO.getPoddAmt()));
            setTdtPoDdDate(CommonUtil.convertObjToStr(objActTransTO.getPoddDt()));
            setTxtRemarks(objActTransTO.getRemarks());
            setActCommand("UPDATE");
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            System.out.println("Error In setTermLoanPowerAttorneyTO..."+e);
            parseException.logException(e,true);
        }
    }
    
//    private void setMax_Del_PoA_No(String borrowNo){
//        try{
//            HashMap transactionMap = new HashMap();
//            HashMap retrieve = new HashMap();
//            transactionMap.put("borrowNo", borrowNo);
//            List resultList = ClientUtil.executeQuery(getStrMaxDelSlNoMapName(), transactionMap);
//            if (resultList.size() > 0){
//                retrieve = (HashMap) resultList.get(0);
//                tableUtilPoA.setStr_MAX_DEL_SL_NO(CommonUtil.convertObjToStr(retrieve.get("MAX_POA_NO")));
//            }
//            retrieve = null;
//            transactionMap = null;
//            resultList = null;
//        }catch(Exception e){
//            System.out.println("Error In setMax_Del_PoA_No: "+e);
//            parseException.logException(e,true);
//        }
//    }
//    
     
    public HashMap setAcctTrans(){
    ActTransTO objActTransTO = new ActTransTO();
    HashMap poaTOList = new HashMap();
    objActTransTO.setBankName(CommonUtil.convertObjToStr((String)cbmBankName.getKeyForSelected()));
//    getBranchData();
    objActTransTO.setBranchName(CommonUtil.convertObjToStr((String)cbmBranchName.getKeyForSelected()));
    objActTransTO.setToAmt(CommonUtil.convertObjToDouble(getTxtAmt()));
    objActTransTO.setToRefNo(getTxtRefNo());
    objActTransTO.setSecDocRec(getTxtSecDepRec());
    objActTransTO.setPoddNo(getTxtPoDdNo());
    objActTransTO.setPoddAmt(CommonUtil.convertObjToDouble(getTxtPoDdAmt()));
    objActTransTO.setAcctNum(getAcctNum());
//    objActTransTO.setPoddDt(
    Date PoDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtPoDdDate()));
                if(PoDt != null){
                Date toDate = (Date)curDate.clone();
                toDate.setDate(PoDt.getDate());
                toDate.setMonth(PoDt.getMonth());
                toDate.setYear(PoDt.getYear());
                objActTransTO.setPoddDt(toDate);  
                }else{
                  objActTransTO.setPoddDt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtPoDdDate()))));
                }
    objActTransTO.setRemarks(getTxtRemarks());
    
    if(getActCommand().equals("UPDATE"))
        objActTransTO.setCommand("UPDATE");
    else
        objActTransTO.setCommand("INSERT");
    poaTOList.put(String.valueOf(0), objActTransTO);
    return poaTOList;
    }
    
    
    public void resetAcctTransfer(){
        setCboBankName("");
        setCboBranchName("");
        setTxtRefNo("");
        setTxtAmt("");
        setTxtSecDepRec("");
        setTxtPoDdNo("");
        setTdtPoDdDate("");
        setTxtPoDdAmt("");
        setTxtRemarks("");
        setActCommand("");
        setChanged();
        ttNotifyObservers();
    }
    //    public HashMap setSettlementBank(){
//        SettlementBankTO objSettlementBankTO = new SettlementBankTO();
//        HashMap poaTOList = new HashMap();
//        HashMap poaEachRecord;
//        try {
//           objSettlementBankTO.setBankName(CommonUtil.convertObjToStr((String)cbmBankName.getKeyForSelected()));
//           objSettlementBankTO.setBranchName(CommonUtil.convertObjToStr((String)cbmBranchName.getKeyForSelected()));
//           if(isRdoActype_Sb()){
//            objSettlementBankTO.setActType("SB");
//           }else if(isRdoActype_Ca()){
//               objSettlementBankTO.setActType("CA");
//           }else{
//                objSettlementBankTO.setActType("OD/CC");
//           }
//           if(isRdoSetMode_Ecs()){
//            objSettlementBankTO.setSetMode("ECS");
//           }else if(isRdoSetMode_Pdc()){
//               objSettlementBankTO.setSetMode("PDC");
//           }else{
//                objSettlementBankTO.setSetMode("OTHERS");
//           }
//           objSettlementBankTO.setActNo(getTxtActNo());
//           objSettlementBankTO.setCommand(INSERT);
//           poaTOList.put(String.valueOf(0), objSettlementBankTO);
//        }catch(Exception e){
//            System.out.println("Error In setTermLoanPowerAttorney..."+e);
//            parseException.logException(e,true);
//        }
//        objSettlementBankTO = null;
//        return poaTOList;
//    }
//     public ArrayList populateData(HashMap mapID, CTable tblData) {
//        _tblData = tblData;
//        
//        // Adding Where Condition
//        HashMap whereMap = null;
//        if (mapID.containsKey(CommonConstants.MAP_WHERE)) {
//            if (mapID.get(CommonConstants.MAP_WHERE) instanceof HashMap)
//                whereMap = (HashMap) mapID.get(CommonConstants.MAP_WHERE);
//            else 
//                System.out.println ("Convert other data type to HashMap:" + mapID);
//        } else {
//            whereMap = new HashMap();
//        }
//        
//        if (!whereMap.containsKey(CommonConstants.BRANCH_ID)) {
//            whereMap.put (CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
//        }
//        if (!whereMap.containsKey(CommonConstants.USER_ID)) {
//            whereMap.put (CommonConstants.USER_ID, ProxyParameters.USER_ID);
//        }
//        
//        mapID.put (CommonConstants.MAP_WHERE, whereMap);
//        
//        System.out.println ("Screen   : " + getClass());
//        System.out.println ("Map Name : " + mapID.get(CommonConstants.MAP_NAME));
//        System.out.println ("Map      : " + mapID);
//        
//        dataHash = ClientUtil.executeTableQuery(mapID);
//        _heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
//        data = (ArrayList) dataHash.get(CommonConstants.TABLEDATA);
//        
////        poaAllRecords = (LinkedHashMap)dataHash.get(CommonConstants.TABLEDATA);
//        if (_heading!=null && _heading.size()>0)
//            _heading.add(0, "Select");
//        ArrayList arrList = new ArrayList();
//        if (data!=null && data.size()>0)
//            for (int i=0; i<data.size();i++) {
//                arrList = (ArrayList)data.get(i);
//                arrList.add(0, new Boolean(false));
//                data.set(i, arrList);
//            }
//        System.out.println("### Data : "+data);
//        populateTable();
//        whereMap = null;
//        return _heading;
//        
//    }
//     
//      public void populateTable() {
////        ArrayList heading = (ArrayList) dataHash.get(CommonConstants.TABLEHEAD);
//        boolean dataExist;
//        if (_heading != null){
//            _isAvailable = true;
//            dataExist = true;         
//            setTblModel(_tblData, data, _heading);     
//        }else{
//            _isAvailable = false;
//            dataExist = false;
//            
//            TableSorter tableSorter = new TableSorter();
//            tableSorter.addMouseListenerToHeaderInTable(_tblData);
//            TableModel tableModel = new TableModel();
//            tableModel.setHeading(new ArrayList());
//            tableModel.setData(new ArrayList());
//            tableModel.fireTableDataChanged();
//            tableSorter.setModel(tableModel);
//            tableSorter.fireTableDataChanged();
//            
//            _tblData.setModel(tableSorter);
//            _tblData.revalidate();
//            
//            ClientUtil.noDataAlert();
//        }
//      
//    }
//    
//    private void setTblModel(CTable tbl, ArrayList tblData, ArrayList head) {
//        TableSorter tableSorter = new TableSorter();
//        tableSorter.addMouseListenerToHeaderInTable(tbl);
//        TableModel tableModel = new TableModel(tblData, head) {
//                public boolean isCellEditable(int rowIndex, int mColIndex) {
//                    if (mColIndex == 0) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            };
//
//        tableModel.fireTableDataChanged();
//        tableSorter.setModel(tableModel);
//        tableSorter.fireTableDataChanged();
//
//        tbl.setModel(tableSorter);
//        tbl.revalidate();        
//    }
//        
//    
//    public void setTable(CTable tbl) {
//        _tblData = tbl;
//    }
//    
//    public HashMap setSettlement(){
//        SettlementTO objSettlementTO = new SettlementTO();
//        HashMap poaTOList = new HashMap();
//        HashMap poaEachRecord;
//        try {
//            java.util.Set keySet =  poaAllRecords.keySet();
//            Object[] objKeySet = (Object[]) keySet.toArray();
//            // To set the values for Power of Attorney Transfer Object
//            if(poaAllRecords!=null)
//            for (int i = poaAllRecords.size() - 1, j = 0;i >= 0;--i,++j){
//                poaEachRecord = (HashMap) poaAllRecords.get(objKeySet[j]);
//                objSettlementTO = new SettlementTO();
//                objSettlementTO.setSlNo(CommonUtil.convertObjToStr( poaEachRecord.get(SLNO)));
//                objSettlementTO.setBankNameSet(CommonUtil.convertObjToStr((String)cbmBankName.getKeyForSelected()));
//                objSettlementTO.setBranchNameSet(CommonUtil.convertObjToStr((String)cbmBranchName.getKeyForSelected()));
//                objSettlementTO.setFromChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CHQFROM)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CHQTO)));
//                objSettlementTO.setQty(CommonUtil.convertObjToStr( poaEachRecord.get(QTY)));
//                objSettlementTO.setChqDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(poaEachRecord.get(CHQDT))));
//                objSettlementTO.setChqAmt(CommonUtil.convertObjToDouble(poaEachRecord.get(CHQAMT)));
//                objSettlementTO.setClearingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(poaEachRecord.get(CLRDT))));
//                objSettlementTO.setBounReason(CommonUtil.convertObjToStr((String)cbmBounReason.getKeyForSelected()));
//                objSettlementTO.setRemark(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
//                objSettlementTO.setChqBoun(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
//                objSettlementTO.setReturnChq(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
//                
////                objTermLoanPowerAttorneyTO.setCountryCode(CommonUtil.convertObjToStr( poaEachRecord.get(COUNTRY)));
//                
//                Date IsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(FROM)));
//                if(IsDt != null){
//                Date isDate = (Date)curDate.clone();
//                isDate.setDate(IsDt.getDate());
//                isDate.setMonth(IsDt.getMonth());
//                isDate.setYear(IsDt.getYear());
//                objSettlementTO.setChqDate(isDate);  
//                }else{
//                  objSettlementTO.setChqDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(FROM))));  
//                }
//                
//                Date ToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(TO)));
//                if(ToDt != null){
//                Date toDate = (Date)curDate.clone();
//                toDate.setDate(ToDt.getDate());
//                toDate.setMonth(ToDt.getMonth());
//                toDate.setYear(ToDt.getYear());
//                objSettlementTO.setClearingDt(toDate);  
//                }else{
//                  objSettlementTO.setClearingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(TO))));
//                }
                
                
//                objTermLoanPowerAttorneyTO.setPeriodFrom((Date)poaEachRecord.get(FROM));
//                objTermLoanPowerAttorneyTO.setPeriodTo((Date)poaEachRecord.get(TO));
//                objTermLoanPowerAttorneyTO.setPhone(CommonUtil.convertObjToStr( poaEachRecord.get(PHONE)));
//                objTermLoanPowerAttorneyTO.setPincode(CommonUtil.convertObjToStr( poaEachRecord.get(PIN)));
//                objTermLoanPowerAttorneyTO.setAddrType(CommonUtil.convertObjToStr(poaEachRecord.get(ADDR_TYPE)));
//                objTermLoanPowerAttorneyTO.setPoaHolderName(CommonUtil.convertObjToStr( poaEachRecord.get(HOLDERNAME)));
//                objTermLoanPowerAttorneyTO.setState(CommonUtil.convertObjToStr( poaEachRecord.get(STATE)));
//                objTermLoanPowerAttorneyTO.setStreet(CommonUtil.convertObjToStr( poaEachRecord.get(STREET)));
//                objTermLoanPowerAttorneyTO.setPoaNo(CommonUtil.convertObjToDouble( poaEachRecord.get(POANO)));
//                objTermLoanPowerAttorneyTO.setRemarks(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
//                objTermLoanPowerAttorneyTO.setToWhom(CommonUtil.convertObjToStr( poaEachRecord.get(ONBEHALFOF)));
//                objSettlementTO.setCommand(CommonUtil.convertObjToStr( poaEachRecord.get(COMMAND)));
//                if (poaEachRecord.get(COMMAND).equals(INSERT)){
//                    objSettlementTO.setStatus(CommonConstants.STATUS_CREATED);
//                }else if (poaEachRecord.get(COMMAND).equals(UPDATE)){
//                    objSettlementTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                }
////                objTermLoanPowerAttorneyTO.setStatusBy(TrueTransactMain.USER_ID);
////                objTermLoanPowerAttorneyTO.setStatusDt(ClientUtil.getCurrentDate());
//                poaTOList.put(objSettlementTO.getFromChqNo(), objSettlementTO);
//                poaEachRecord = null;
//                objSettlementTO = null;
//            }
//            // To set the values for Power of Attorney Transfer Object
//            // where as the existing records in Database are deleted in client side
//            // useful for updating the status
//            for (int i = tableUtilPoA.getRemovedValues().size() - 1,j = 0;i >= 0;--i,++j){
//                poaEachRecord = (HashMap) tableUtilPoA.getRemovedValues().get(j);
//                objSettlementTO = new SettlementTO();
//                objSettlementTO.setBankNameSet(CommonUtil.convertObjToStr(getCbmBankName()));
//                objSettlementTO.setBranchNameSet(CommonUtil.convertObjToStr(getCbmBranchName()));
//                objSettlementTO.setFromChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(COUNTRY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                objSettlementTO.setToChqNo(CommonUtil.convertObjToStr( poaEachRecord.get(CITY)));
//                
////                objTermLoanPowerAttorneyTO.setCountryCode(CommonUtil.convertObjToStr( poaEachRecord.get(COUNTRY)));
//                
//                Date IsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(FROM)));
//                if(IsDt != null){
//                Date isDate = (Date)curDate.clone();
//                isDate.setDate(IsDt.getDate());
//                isDate.setMonth(IsDt.getMonth());
//                isDate.setYear(IsDt.getYear());
//                objSettlementTO.setChqDate(isDate);  
//                }else{
//                  objSettlementTO.setChqDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(FROM))));  
//                }
//                
//                Date ToDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(TO)));
//                if(ToDt != null){
//                Date toDate = (Date)curDate.clone();
//                toDate.setDate(ToDt.getDate());
//                toDate.setMonth(ToDt.getMonth());
//                toDate.setYear(ToDt.getYear());
//                objSettlementTO.setClearingDt(toDate);  
//                }else{
//                  objSettlementTO.setClearingDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr( poaEachRecord.get(TO))));
//                }
//                
//                
////                objTermLoanPowerAttorneyTO.setPeriodFrom((Date)poaEachRecord.get(FROM));
////                objTermLoanPowerAttorneyTO.setPeriodTo((Date)poaEachRecord.get(TO));
////                objTermLoanPowerAttorneyTO.setPhone(CommonUtil.convertObjToStr( poaEachRecord.get(PHONE)));
////                objTermLoanPowerAttorneyTO.setPincode(CommonUtil.convertObjToStr( poaEachRecord.get(PIN)));
////                objTermLoanPowerAttorneyTO.setAddrType(CommonUtil.convertObjToStr(poaEachRecord.get(ADDR_TYPE)));
////                objTermLoanPowerAttorneyTO.setPoaHolderName(CommonUtil.convertObjToStr( poaEachRecord.get(HOLDERNAME)));
////                objTermLoanPowerAttorneyTO.setState(CommonUtil.convertObjToStr( poaEachRecord.get(STATE)));
////                objTermLoanPowerAttorneyTO.setStreet(CommonUtil.convertObjToStr( poaEachRecord.get(STREET)));
////                objTermLoanPowerAttorneyTO.setPoaNo(CommonUtil.convertObjToDouble( poaEachRecord.get(POANO)));
////                objTermLoanPowerAttorneyTO.setRemarks(CommonUtil.convertObjToStr( poaEachRecord.get(REMARK)));
////                objTermLoanPowerAttorneyTO.setToWhom(CommonUtil.convertObjToStr( poaEachRecord.get(ONBEHALFOF)));
//                objSettlementTO.setCommand(CommonUtil.convertObjToStr( poaEachRecord.get(COMMAND)));
//                objSettlementTO.setStatus(CommonConstants.STATUS_DELETED);
////                objTermLoanPowerAttorneyTO.setStatusBy(TrueTransactMain.USER_ID);
////                objTermLoanPowerAttorneyTO.setStatusDt(ClientUtil.getCurrentDate());
//                poaTOList.put(objSettlementTO.getFromChqNo(), objSettlementTO);
//                poaEachRecord = null;
//                objSettlementTO = null;
//            }
//        }catch(Exception e){
//            System.out.println("Error In setTermLoanPowerAttorney..."+e);
//            parseException.logException(e,true);
//        }
//        objSettlementTO = null;
//        return poaTOList;
//    }
////    
//    public void changeStatusPoA(int resultType){
//        try{
//            if (resultType != 2){
//                //If the Main Save Button pressed
//                tableUtilPoA.getRemovedValues().clear();
//            }
//            java.util.Set keySet =  poaAllRecords.keySet();
//            Object[] objKeySet = (Object[]) keySet.toArray();
//            HashMap oneRecord;
//            // To change the Insert command to Update after Save Buttone Pressed
//            // Power of Attorney Details
//            for (int i = keySet.size() - 1,j = 0;i >= 0;--i,++j){
//                oneRecord = (HashMap) poaAllRecords.get(objKeySet[j]);
//                if (oneRecord.get(COMMAND).equals(INSERT)){
//                    // If the status is in Insert Mode then change the mode to Update
//                    oneRecord.put(COMMAND, UPDATE);
//                    poaAllRecords.put(objKeySet[j], oneRecord);
//                }
//                oneRecord = null;
//            }
//            
//            tableUtilPoA.setAllValues(poaAllRecords);
//            keySet = null;
//            objKeySet = null;
//        }catch(Exception e){
//            System.out.println("Error In changeStatusPoA..."+e);
//            parseException.logException(e,true);
//        }
//    }
//    
//    public void setBorrowerNo(String borrowerNo){
//        this.borrowerNo = borrowerNo;
//        setChanged();
//    }
//    
//    public String getBorrowerNo(){
//        return this.borrowerNo;
//    }
//    
//    public void setTblPoA(EnhancedTableModel tblPoATab){
//        this.tblPoATab = tblPoATab;
//        setChanged();
//    }
//    
//    public EnhancedTableModel getTblPoA(){
//        return this.tblPoATab;
//    }
//    
//    void setCboPoACust(String cboPoACust){
//        this.cboPoACust = cboPoACust;
//        setChanged();
//    }
//    String getCboPoACust(){
//        return this.cboPoACust;
//    }
//    
//    void setCbmPoACust(ComboBoxModel cbmPoACust){
//        this.cbmPoACust = cbmPoACust;
//        setChanged();
//    }
//    
//    public ComboBoxModel getCbmPoACust(){
//        return this.cbmPoACust;
//    }
//    
//    void setCboAddrType_PoA(String cboAddrType_PoA){
//        this.cboAddrType_PoA = cboAddrType_PoA;
//        setChanged();
//    }
//    String getCboAddrType_PoA(){
//        return this.cboAddrType_PoA;
//    }
//    
//    void setCbmAddrType_PoA(ComboBoxModel cbmAddrType_PoA){
//        this.cbmAddrType_PoA = cbmAddrType_PoA;
//        setChanged();
//    }
//    
//    ComboBoxModel getCbmAddrType_PoA(){
//        return this.cbmAddrType_PoA;
//    }
//    
//    void setTxtPoANo(String txtPoANo){
//        this.txtPoANo = txtPoANo;
//        setChanged();
//    }
//    
//    String getTxtPoANo(){
//        return this.txtPoANo;
//    }
//    
//    void setTxtPoaHolderName(String txtPoaHolderName){
//        this.txtPoaHolderName = txtPoaHolderName;
//        setChanged();
//    }
//    String getTxtPoaHolderName(){
//        return this.txtPoaHolderName;
//    }
//    
//    void setTxtStreet_PowerAttroney(String txtStreet_PowerAttroney){
//        this.txtStreet_PowerAttroney = txtStreet_PowerAttroney;
//        setChanged();
//    }
//    String getTxtStreet_PowerAttroney(){
//        return this.txtStreet_PowerAttroney;
//    }
//    
//    void setTxtArea_PowerAttroney(String txtArea_PowerAttroney){
//        this.txtArea_PowerAttroney = txtArea_PowerAttroney;
//        setChanged();
//    }
//    String getTxtArea_PowerAttroney(){
//        return this.txtArea_PowerAttroney;
//    }
//    
//    void setCbmCity_PowerAttroney(ComboBoxModel cbmCity_PowerAttroney){
//        this.cbmCity_PowerAttroney = cbmCity_PowerAttroney;
//        setChanged();
//    }
//    
//    ComboBoxModel getCbmCity_PowerAttroney(){
//        return this.cbmCity_PowerAttroney;
//    }
//    
//    void setCboCity_PowerAttroney(String cboCity_PowerAttroney){
//        this.cboCity_PowerAttroney = cboCity_PowerAttroney;
//        setChanged();
//    }
//    String getCboCity_PowerAttroney(){
//        return this.cboCity_PowerAttroney;
//    }
//    
//    void setCbmState_PowerAttroney(ComboBoxModel cbmState_PowerAttroney){
//        this.cbmState_PowerAttroney = cbmState_PowerAttroney;
//        setChanged();
//    }
//    
//    ComboBoxModel getCbmState_PowerAttroney(){
//        return this.cbmState_PowerAttroney;
//    }
//    
//    void setCboState_PowerAttroney(String cboState_PowerAttroney){
//        this.cboState_PowerAttroney = cboState_PowerAttroney;
//        setChanged();
//    }
//    String getCboState_PowerAttroney(){
//        return this.cboState_PowerAttroney;
//    }
//    
//    void setCbmCountry_PowerAttroney(ComboBoxModel cbmCountry_PowerAttroney){
//        this.cbmCountry_PowerAttroney = cbmCountry_PowerAttroney;
//        setChanged();
//    }
//    
//    ComboBoxModel getCbmCountry_PowerAttroney(){
//        return this.cbmCountry_PowerAttroney;
//    }
//    
//    void setCboCountry_PowerAttroney(String cboCountry_PowerAttroney){
//        this.cboCountry_PowerAttroney = cboCountry_PowerAttroney;
//        setChanged();
//    }
//    String getCboCountry_PowerAttroney(){
//        return this.cboCountry_PowerAttroney;
//    }
//    
//    void setTxtPin_PowerAttroney(String txtPin_PowerAttroney){
//        this.txtPin_PowerAttroney = txtPin_PowerAttroney;
//        setChanged();
//    }
//    String getTxtPin_PowerAttroney(){
//        return this.txtPin_PowerAttroney;
//    }
//    
//    void setTxtPhone_PowerAttroney(String txtPhone_PowerAttroney){
//        this.txtPhone_PowerAttroney = txtPhone_PowerAttroney;
//        setChanged();
//    }
//    String getTxtPhone_PowerAttroney(){
//        return this.txtPhone_PowerAttroney;
//    }
//    
//    void setTdtPeriodFrom_PowerAttroney(String tdtPeriodFrom_PowerAttroney){
//        this.tdtPeriodFrom_PowerAttroney = tdtPeriodFrom_PowerAttroney;
//        setChanged();
//    }
//    String getTdtPeriodFrom_PowerAttroney(){
//        return this.tdtPeriodFrom_PowerAttroney;
//    }
//    
//    void setTdtPeriodTo_PowerAttroney(String tdtPeriodTo_PowerAttroney){
//        this.tdtPeriodTo_PowerAttroney = tdtPeriodTo_PowerAttroney;
//        setChanged();
//    }
//    String getTdtPeriodTo_PowerAttroney(){
//        return this.tdtPeriodTo_PowerAttroney;
//    }
//    
//    void setTxtRemark_PowerAttroney(String txtRemark_PowerAttroney){
//        this.txtRemark_PowerAttroney = txtRemark_PowerAttroney;
//        setChanged();
//    }
//    String getTxtRemark_PowerAttroney(){
//        return this.txtRemark_PowerAttroney;
//    }
//    
    public void ttNotifyObservers(){
        this.notifyObservers();
    }
//    
//    public void clearCboPoACust_ID(){
//        // Remove all keys and values before add
//        try{
//            for (int i = cbmPoACust.getSize() - 1;i >= 0;--i){
//                cbmPoACust.removeKeyAndElement(cbmPoACust.getKey(i));
//            }
//            cbmPoACust.addKeyAndElement("", "");
//            ttNotifyObservers();
//        }catch(Exception e){
//            log.info("Exception Caught in clearCboPoACust_ID: "+e);
//            parseException.logException(e,true);
//        }
//    }
//    
    
//    public void resetPoACustID(String Cust_ID){
//        try{
//            // Remove all keys and values before add
//            for (int i = cbmPoACust.getSize() - 1;i >= 0;--i){
//                cbmPoACust.removeKeyAndElement(cbmPoACust.getKey(i));
//            }
//            // To add the Customer ID in ComboBoxModel in PoA Details
//            if (!cbmPoACust.containsElement("")){
//                cbmPoACust.addKeyAndElement("", "");
//            }
//            if (!cbmPoACust.containsElement(getCustName(Cust_ID))){
//                cbmPoACust.addKeyAndElement(Cust_ID, getCustName(Cust_ID));
//            }
//            ttNotifyObservers();
//        }catch(Exception e){
//            System.out.println("Exception Caught in resetPoACustID: "+e);
//            parseException.logException(e,true);
//        }
//    }
    
//    public String getCustName(String custID){
//        //        getSelectAccInfoTblDisplay
//        String returnValue = "";
//        try{
//            HashMap idTransactionMap = new HashMap();
//            HashMap idRetrieve;
//            idTransactionMap.put("CUST_ID", custID);
//            List idResultList = ClientUtil.executeQuery("getSelectAccInfoTblDisplay", idTransactionMap);
//            if (idResultList.size() > 0){
//                // If Product Account Head exist in Database
//                idRetrieve = (HashMap) idResultList.get(0);
//                returnValue = CommonUtil.convertObjToStr(idRetrieve.get("Name"));
//                returnValue = returnValue+"("+custID+")";
//            }
//            idRetrieve = null;
//            idTransactionMap = null;
//            idResultList = null;
//        }catch(Exception e){
//            System.out.println("Exception caught in getCustName: "+e);
//            parseException.logException(e,true);
//        }
//        return returnValue;
//    }
    
    //  Power of Attorney Details.
//    public int addPoATab(int row, boolean update){ // update is the flag to update the records
//        String temp = new String();
//        int option = -1;
//        try{
//              int qty = CommonUtil.convertObjToInt(txtQty);
//              final int chqFrm = CommonUtil.convertObjToInt(txtFromChqNo);
//              final String chqdate = tdtChqDate;
//              
//        for(int i=0 ; i < qty ; i++){
//            poaEachTabRecords = new ArrayList();
//            poaEachRecords = new HashMap();
//            HashMap result = new HashMap();
//            ArrayList data = tblPoATab.getDataArrayList();
//            tblPoATab.setDataArrayList(data,poaTabTitle);
//            final int dataSize = data.size();
//            boolean exist = false;
//            boolean found = false;
//            insertPoA(dataSize+1);
//            if (!update) {
//                // If the table is not in Edit Mode
//                result = tableUtilPoA.insertTableValues(poaEachTabRecords,poaEachRecords);
//                
//                poaAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
//                poaAllRecords = (LinkedHashMap) result.get(ALL_VALUES);
//                option = CommonUtil.convertObjToInt(result.get(OPTION));
//                
//                tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
//            }else{
//                option = updatePoATable(row);
//            }
//            
//            setChanged();
//            result = null;
//            data = null;
//            poaEachTabRecords = null;
//            poaEachRecords = null;
//        }
//              tdtChqDate = chqdate;
//              txtFromChqNo = String.valueOf(chqFrm);
//        }catch(Exception e){
//            System.out.println("The error in addPoATab()"+e);
//            parseException.logException(e,true);
//        }
//        return option;
//    }
//    
    // Details to be inserted or updated in PoA table
//    private void insertPoA(int slno){
//        
////        int qty = CommonUtil.convertObjToInt(txtQty);
////        for(int i=0 ; i < qty ; i++){
////        int chqNo = CommonUtil.convertObjToInt(txtFromChqNo);
////        int finalChq = 0;
//        if(slno == 1){
//            
//        }else{
//            Date chqDt = DateUtil.getDateMMDDYYYY(tdtChqDate);
//            Date finalDt = DateUtil.addDaysProperFormat(chqDt, 30);
//            tdtChqDate = CommonUtil.convertObjToStr(finalDt);
//            int chqNo = CommonUtil.convertObjToInt(txtFromChqNo);
//            int chqFrm = chqNo+1;
//            txtFromChqNo = String.valueOf(chqFrm);
//        }
//        poaEachTabRecords.add(String.valueOf(slno));
////        poaEachTabRecords.add(txtFromChqNo);
//        poaEachTabRecords.add(txtFromChqNo);
//        poaEachTabRecords.add(tdtChqDate);
//        poaEachTabRecords.add(txtChqAmt);
//        poaEachTabRecords.add("Not Passed");
//     
//        
//        poaEachRecords.put(SLNO,String.valueOf(slno));
//        poaEachRecords.put(CHQFROM ,txtFromChqNo);
//        poaEachRecords.put(CHQTO ,txtToChqNo);
//        poaEachRecords.put(QTY ,txtQty);
//        poaEachRecords.put(CHQDT ,tdtChqDate);
//        poaEachRecords.put(CHQAMT ,txtChqAmt);
//        poaEachRecords.put(CLRDT , tdtClearingDt);
////        poaEachRecords.put(REMARK ,CommonUtil.convertObjToStr(cbmCity_PowerAttroney.getKeyForSelected()));
////        poaEachRecords.put(STATE,CommonUtil.convertObjToStr(cbmState_PowerAttroney.getKeyForSelected()));
////        poaEachRecords.put(COUNTRY,CommonUtil.convertObjToStr(cbmCountry_PowerAttroney.getKeyForSelected()));
////        poaEachRecords.put(PIN,txtPin_PowerAttroney);
////        poaEachRecords.put(PHONE,txtPhone_PowerAttroney);
////        poaEachRecords.put(FROM,tdtPeriodFrom_PowerAttroney);
////        poaEachRecords.put(TO,tdtPeriodTo_PowerAttroney);
////        poaEachRecords.put()
//        poaEachRecords.put(REMARK,txtRemarks);
//        poaEachRecords.put(COMMAND, "");
////        return chqFrm;
////        }
//    }
    
    //  Record to display in UI
//    public void populatePoATable(int row){
//        try{
//            HashMap eachRecs = new HashMap();
//            java.util.Set keySet =  poaAllRecords.keySet();
//            Object[] objKeySet = (Object[]) keySet.toArray();
//            ArrayList poaTableValue = (ArrayList)tblPoATab.getDataArrayList().get(row);
//            // To populate the corresponding PoA record in UI
//            for (int i = poaAllRecords.size() - 1,j = 0;i >= 0;--i,++j){
//                if (((HashMap) poaAllRecords.get(objKeySet[j])).get(POANO).equals(poaTableValue.get(0))){
//                    // To populate the Corresponding record from CTable
//                    eachRecs = (HashMap) poaAllRecords.get(objKeySet[j]);
////                    setTxtPoANo(CommonUtil.convertObjToStr(eachRecs.get(POANO)));
////                    setCboPoACust(CommonUtil.convertObjToStr(getCbmPoACust().getDataForKey(eachRecs.get(ONBEHALFOF))));
////                    setTxtCustID_PoA(CommonUtil.convertObjToStr(eachRecs.get(CUSTOMER_ID)));
////                    setCboCity_PowerAttroney(CommonUtil.convertObjToStr(getCbmCity_PowerAttroney().getDataForKey(eachRecs.get(CITY))));
////                    setCboCountry_PowerAttroney(CommonUtil.convertObjToStr(getCbmCountry_PowerAttroney().getDataForKey(eachRecs.get(COUNTRY))));
////                    setTdtPeriodFrom_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(FROM)));
////                    setTdtPeriodTo_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(TO)));
////                    setTxtArea_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(AREA)));
////                    setTxtPhone_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(PHONE)));
////                    setTxtPin_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(PIN)));
////                    setTxtPoaHolderName(CommonUtil.convertObjToStr(eachRecs.get(HOLDERNAME)));
////                    setTxtRemark_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(REMARK)));
////                    setCboAddrType_PoA(CommonUtil.convertObjToStr(getCbmAddrType_PoA().getDataForKey(eachRecs.get(ADDR_TYPE))));
////                    setCboState_PowerAttroney(CommonUtil.convertObjToStr(getCbmState_PowerAttroney().getDataForKey(eachRecs.get(STATE))));
////                    setTxtStreet_PowerAttroney(CommonUtil.convertObjToStr(eachRecs.get(STREET)));
//                    
//                    setTxtFromChqNo(CommonUtil.convertObjToStr(eachRecs.get(CHQFROM)));
//                    setTxtToChqNo(CommonUtil.convertObjToStr(eachRecs.get(CHQTO)));
//                    setTxtQty(CommonUtil.convertObjToStr(eachRecs.get(QTY)));
//                    setTdtChqDate(CommonUtil.convertObjToStr(eachRecs.get(CHQDT)));
//                    setTxtChqAmt(CommonUtil.convertObjToStr(eachRecs.get(CHQAMT)));
//                    setTdtClearingDt(CommonUtil.convertObjToStr(eachRecs.get(CLRDT)));
////                    setCboBounReason(CommonUtil.convertObjToStr(getCbmState_PowerAttroney().getDataForKey(eachRecs.get(STATE))));
//                    setTxtRemarks(CommonUtil.convertObjToStr(eachRecs.get(REMARK)));
//                    setChanged();
//                    ttNotifyObservers();
//                    break;
//                }
//            }
//            eachRecs = null;
//            objKeySet = null;
//            poaTableValue = null;
//            keySet = null;
//        }catch(Exception e){
//            System.out.println("Error in populatePoATable..."+e);
//            parseException.logException(e,true);
//        }
//    }
    
    // Update Power of Attorney Table where row is the Serial Number
//    private int updatePoATable(int row){
//        HashMap result = new HashMap();
//        int option = -1;
//        int count = 0;
//        try{
//            
//            result = tableUtilPoA.updateTableValues(poaEachTabRecords, poaEachRecords, row);
//            
//            poaAllTabRecords = (ArrayList) result.get(TABLE_VALUES);
//            poaAllRecords = (LinkedHashMap) result.get(ALL_VALUES);
//            option = CommonUtil.convertObjToInt(result.get(OPTION));
//            
//            tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
//        }catch(Exception e){
//            System.out.println("Error in updatePoA..."+e);
//            parseException.logException(e,true);
//        }
//        result = null;
//        return option;
//    }
    
    // Delete Power of Attorney Table where r is the Serial Number
//    public void deletePoATable(int row){
//        try{
//            ArrayList tempList1 = new ArrayList();
//            HashMap result = new HashMap();
//            
//            result = tableUtilPoA.deleteTableValues(row);
//            
//            poaAllTabRecords = (ArrayList)result.get(TABLE_VALUES);
//            poaAllRecords = (LinkedHashMap)result.get(ALL_VALUES);
//            
//            tblPoATab.setDataArrayList(poaAllTabRecords, poaTabTitle);
//            result = null;
//        }catch(Exception e){
//            System.out.println("Exception caught in deletePoATable: "+e);
//            parseException.logException(e,true);
//        }
//    }
    
//    public boolean checkCustIDExistInJointAcctAndPoA(String CustID){
//        java.util.Set keySet =  poaAllRecords.keySet();
//        Object[] objKeySet = (Object[]) keySet.toArray();
//        try{
//            HashMap eachPoARec;
//            for (int i = poaAllRecords.size() - 1,j = 0;i >= 0;--i,++j){
//                eachPoARec = (HashMap) poaAllRecords.get(objKeySet[j]);
//                if (CustID.equals(eachPoARec.get(ONBEHALFOF))){
//                    int option = -1;
//                    String[] options = {objSettlementRB.getString("cDialogOk")};
//                    option = COptionPane.showOptionDialog(null, objSettlementRB.getString("existanceCustomerWarningPoA"), CommonConstants.WARNINGTITLE,
//                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
//                    return false;
//                }
//                eachPoARec = null;
//            }
//            keySet = null;
//            objKeySet = null;
//        }catch(Exception e){
//            System.out.println("Exception caught in checkCustIDExistInJointAcctAndPoA: "+e);
//            parseException.logException(e,true);
//        }
//        return true;
//    }
//    
//    public void setPoACustName(String CUSTID){
//        try {
//            HashMap transactionMap = new HashMap();
//            HashMap retrieve = new HashMap();
//            transactionMap.put("custId",CUSTID);
//            List resultList1 = (List) ClientUtil.executeQuery("getSelectCustomerOpenDate", transactionMap);
//            if (resultList1.size() > 0){
//                // If atleast one Record should exist
//                retrieve = (HashMap) resultList1.get(0);
//                if (retrieve.get("CUST_TYPE").equals("CORPORATE")){// If it is the Corporate Customer
//                    setTxtPoaHolderName(CommonUtil.convertObjToStr(retrieve.get("COMP_NAME")));
//                }else{
//                    setTxtPoaHolderName(CommonUtil.convertObjToStr(retrieve.get("CUSTOMER NAME")));
//                }
//            }
//            retrieve = null;
//            transactionMap = null;
//            resultList1 = null;
//        }catch(Exception e){
//            log.info("Exception caught in setPoACustName: "+e);
//            parseException.logException(e,true);
//        }
//    }
    
//    public void setPoACustAddr(String CUSTID){
//        try {
//            HashMap transactionMap = new HashMap();
//            HashMap retrieve = new HashMap();
//            transactionMap.put("custId",CUSTID);
//            List resultList2 = ClientUtil.executeQuery("getSelectCustomerAddress", transactionMap);
//            if (resultList2.size() > 0){
//                // Atleast one Record should exist
//                retrieve = (HashMap) resultList2.get(0);
//                
//                setTxtArea_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(AREA)));
//                setTxtStreet_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(STREET)));
//                setCboCity_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(CITY)));
//                setCboState_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(STATE)));
//                setCboCountry_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(COUNTRY)));
//                setTxtPin_PowerAttroney(CommonUtil.convertObjToStr(retrieve.get(PIN_CODE)));
//                setCboAddrType_PoA(CommonUtil.convertObjToStr(getCbmAddrType_PoA().getDataForKey(CommonUtil.convertObjToStr(retrieve.get(ADDR_TYPE)))));
//            }
//            retrieve = null;
//            transactionMap = null;
//            resultList2 = null;
//        }catch(Exception e){
//            log.info("Exception caught in setPoACustAddr: "+e);
//            parseException.logException(e,true);
//        }
//    }
    
//    public void setPoACustPhone(String CUSTID){
//        try {
//            HashMap transactionMap = new HashMap();
//            HashMap retrieve;
//            StringBuffer stbPhoneNo = new StringBuffer();
//            transactionMap.put("custId",CUSTID);
//            
//            List resultList2         = ClientUtil.executeQuery("getSelectCustomerPhone", transactionMap);
//            
//            if (resultList2.size() > 0){
//                retrieve = (HashMap) resultList2.get(0);
//                stbPhoneNo.append(CommonUtil.convertObjToStr(retrieve.get("AREA_CODE")));
//                stbPhoneNo.append(CommonUtil.convertObjToStr(retrieve.get("PHONE_NUMBER")));
//                setTxtPhone_PowerAttroney(CommonUtil.convertObjToStr(stbPhoneNo));
//            }
//            else{
//                 setTxtPhone_PowerAttroney("");
//            }
//            transactionMap = null;
//            resultList2 = null;
//            retrieve = null;
//            stbPhoneNo = null;
//        }catch(Exception e){
//            log.info("Exception caught in setPoACustPhone: "+e);
//            parseException.logException(e,true);
//        }
//    }
//    
//    public boolean isThisCustomerOnBehalfofOther(String strCustID, String strOnBehalfOf){
//        boolean isThisCustomerOnBehalfofOther = true;
//        try{
//            if (strCustID.equals(strOnBehalfOf)){
//                showWarningMsg(objSettlementRB.getString("onBehalfOfSameCustWarn"));
//                isThisCustomerOnBehalfofOther = false;
//            }
//            if (isThisCustomerOnBehalfofOther){
//                HashMap eachRecs = new HashMap();
//                String strExistingCustID;
//                String strExistingOnBehalfOf;
//                java.util.Set keySet =  poaAllRecords.keySet();
//                Object[] objKeySet = (Object[]) keySet.toArray();
//                // To find this combination already exits or not
//                for (int i = poaAllRecords.size() - 1,j = 0;i >= 0;--i,++j){
//                        eachRecs = (HashMap) poaAllRecords.get(objKeySet[j]);
//                        strExistingCustID = CommonUtil.convertObjToStr(eachRecs.get(CUSTOMER_ID));
//                        strExistingOnBehalfOf = CommonUtil.convertObjToStr(eachRecs.get(ONBEHALFOF));
//                        if (strExistingCustID.equals(strCustID) && strExistingOnBehalfOf.equals(strOnBehalfOf)){
//                            showWarningMsg(strOnBehalfOf + objSettlementRB.getString("onBehalfOfCombinationExistWarn") + strCustID);
//                            isThisCustomerOnBehalfofOther = false;
//                            break;
//                        }
//                        strExistingCustID = null;
//                        strExistingOnBehalfOf = null;
//                        eachRecs = null;
//                }
//                eachRecs = null;
//                keySet = null;
//                objKeySet = null;
//                strExistingCustID = null;
//                strExistingOnBehalfOf = null;
//            }
//        }catch(Exception e){
//            log.info("Exception caught in isThisCustomerOnBehalfofOther: "+e);
//            parseException.logException(e,true);
//        }
//        return isThisCustomerOnBehalfofOther;
//    }
    public void getBranchData(String bnk){
        System.out.println("getBranchData()");
        try {
            lookUpHash = new HashMap();
            String bankCode;
            if(bnk.equals("")){
            bankCode = CommonUtil.convertObjToStr(cbmBankName.getKeyForSelected());
//            System.out.println("BankSelected: " + bankCode);
            }else{
                bankCode = bnk;
                
            }
            System.out.println("BankSelected: " + bankCode);
            lookUpHash.put(CommonConstants.MAP_NAME,"InwardClearing.getBranch");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, bankCode);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
            cbmBranchName = new ComboBoxModel(key,value);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
//    public int showWarningMsg(String strWarnMsg){
//        int option = -1;
//        String[] options = {objSettlementRB.getString("cDialogOk")};
//        option = COptionPane.showOptionDialog(null, strWarnMsg, CommonConstants.WARNINGTITLE,
//        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
//        return option;
//    }
    
    // To create objects
    public void createObject(){
//        poaAllRecords = new LinkedHashMap();
//        poaAllTabRecords = new ArrayList();
//        tblPoATab = new EnhancedTableModel(null, poaTabTitle);
//        tableUtilPoA = new TableUtil();
//        tableUtilPoA.setAttributeKey(POANO);
    }
    
    // To destroy Objects
    public void destroyObjects(){
//        poaAllRecords = null;
//        poaAllTabRecords = null;
//        tblPoATab = null;
//        poaEachRecords= null;
//        poaEachTabRecords= null;
//        tableUtilPoA = null;
    }
//    
//    /**
//     * Getter for property txtCustID_PoA.
//     * @return Value of property txtCustID_PoA.
//     */
//    public java.lang.String getTxtCustID_PoA() {
//        return txtCustID_PoA;
//    }
//    
//    /**
//     * Setter for property txtCustID_PoA.
//     * @param txtCustID_PoA New value of property txtCustID_PoA.
//     */
//    public void setTxtCustID_PoA(java.lang.String txtCustID_PoA) {
//        this.txtCustID_PoA = txtCustID_PoA;
//    }
//    
//    /**
//     * Getter for property cbmBankName.
//     * @return Value of property cbmBankName.
//     */
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmBankName() {
//        return cbmBankName;
//    }
//    
//    /**
//     * Setter for property cbmBankName.
//     * @param cbmBankName New value of property cbmBankName.
//     */
//    public void setCbmBankName(com.see.truetransact.clientutil.ComboBoxModel cbmBankName) {
//        this.cbmBankName = cbmBankName;
//    }
//    
//    /**
//     * Getter for property cbmBranchName.
//     * @return Value of property cbmBranchName.
//     */
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchName() {
//        return cbmBranchName;
//    }
//    
//    /**
//     * Setter for property cbmBranchName.
//     * @param cbmBranchName New value of property cbmBranchName.
//     */
//    public void setCbmBranchName(com.see.truetransact.clientutil.ComboBoxModel cbmBranchName) {
//        this.cbmBranchName = cbmBranchName;
//    }
//    
//    /**
//     * Getter for property cbmBounReason.
//     * @return Value of property cbmBounReason.
//     */
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmBounReason() {
//        return cbmBounReason;
//    }
//    
//    /**
//     * Setter for property cbmBounReason.
//     * @param cbmBounReason New value of property cbmBounReason.
//     */
//    public void setCbmBounReason(com.see.truetransact.clientutil.ComboBoxModel cbmBounReason) {
//        this.cbmBounReason = cbmBounReason;
//    }
//    
//    /**
//     * Getter for property cboBankName.
//     * @return Value of property cboBankName.
//     */
//    public java.lang.String getCboBankName() {
//        return cboBankName;
//    }
//    
//    /**
//     * Getter for property cboBranchName.
//     * @return Value of property cboBranchName.
//     */
//    public java.lang.String getCboBranchName() {
//        return cboBranchName;
//    }
//    
//    /**
//     * Setter for property cboBranchName.
//     * @param cboBranchName New value of property cboBranchName.
//     */
//    public void setCboBranchName(java.lang.String cboBranchName) {
//        this.cboBranchName = cboBranchName;
//    }
//    
//    /**
//     * Setter for property cboBankName.
//     * @param cboBankName New value of property cboBankName.
//     */
//    public void setCboBankName(java.lang.String cboBankName) {
//        this.cboBankName = cboBankName;
//    }
//    
//    /**
//     * Getter for property txtActNo.
//     * @return Value of property txtActNo.
//     */
//    public java.lang.String getTxtActNo() {
//        return txtActNo;
//    }
//    
//    /**
//     * Setter for property txtActNo.
//     * @param txtActNo New value of property txtActNo.
//     */
//    public void setTxtActNo(java.lang.String txtActNo) {
//        this.txtActNo = txtActNo;
//    }
//    
//    /**
//     * Getter for property txtFromChqNo.
//     * @return Value of property txtFromChqNo.
//     */
//    public java.lang.String getTxtFromChqNo() {
//        return txtFromChqNo;
//    }
//    
//    /**
//     * Setter for property txtFromChqNo.
//     * @param txtFromChqNo New value of property txtFromChqNo.
//     */
//    public void setTxtFromChqNo(java.lang.String txtFromChqNo) {
//        this.txtFromChqNo = txtFromChqNo;
//    }
//    
//    /**
//     * Getter for property tdtChqDate.
//     * @return Value of property tdtChqDate.
//     */
//    public java.lang.String getTdtChqDate() {
//        return tdtChqDate;
//    }
//    
//    /**
//     * Getter for property txtToChqNo.
//     * @return Value of property txtToChqNo.
//     */
////    public java.lang.String getTxtToChqNo() {
////        return txtToChqNo;
////    }
//    
//    /**
//     * Setter for property tdtChqDate.
//     * @param tdtChqDate New value of property tdtChqDate.
//     */
//    public void setTdtChqDate(java.lang.String tdtChqDate) {
//        this.tdtChqDate = tdtChqDate;
//    }
//    
//    /**
//     * Getter for property txtChqAmt.
//     * @return Value of property txtChqAmt.
//     */
//    public java.lang.String getTxtChqAmt() {
//        return txtChqAmt;
//    }
//    
//    /**
//     * Setter for property txtChqAmt.
//     * @param txtChqAmt New value of property txtChqAmt.
//     */
//    public void setTxtChqAmt(java.lang.String txtChqAmt) {
//        this.txtChqAmt = txtChqAmt;
//    }
//    
//    /**
//     * Getter for property tdtClearingDt.
//     * @return Value of property tdtClearingDt.
//     */
////    public java.lang.String getTdtClearingDt() {
////        return tdtClearingDt;
////    }
//    
//    /**
//     * Getter for property txtRemarks.
//     * @return Value of property txtRemarks.
//     */
//    public java.lang.String getTxtRemarks() {
//        return txtRemarks;
//    }
//    
//    /**
//     * Getter for property rdoActype_Sb.
//     * @return Value of property rdoActype_Sb.
//     */
//    public boolean isRdoActype_Sb() {
//        return rdoActype_Sb;
//    }
//    
//    /**
//     * Getter for property rdoActype_Ca.
//     * @return Value of property rdoActype_Ca.
//     */
////    public boolean isRdoActype_Ca() {
////        return rdoActype_Ca;
////    }
//    
//    /**
//     * Setter for property rdoActype_Sb.
//     * @param rdoActype_Sb New value of property rdoActype_Sb.
//     */
//    public void setRdoActype_Sb(boolean rdoActype_Sb) {
//        this.rdoActype_Sb = rdoActype_Sb;
//    }
//    
//    /**
//     * Setter for property txtRemarks.
//     * @param txtRemarks New value of property txtRemarks.
//     */
//    public void setTxtRemarks(java.lang.String txtRemarks) {
//        this.txtRemarks = txtRemarks;
//    }
//    
//    /**
//     * Getter for property rdoSetMode_Pdc.
//     * @return Value of property rdoSetMode_Pdc.
//     */
//    public boolean isRdoSetMode_Pdc() {
//        return rdoSetMode_Pdc;
//    }
//    
//    /**
//     * Setter for property rdoSetMode_Pdc.
//     * @param rdoSetMode_Pdc New value of property rdoSetMode_Pdc.
//     */
//    public void setRdoSetMode_Pdc(boolean rdoSetMode_Pdc) {
//        this.rdoSetMode_Pdc = rdoSetMode_Pdc;
//    }
////    
////    /**
////     * Setter for property rdoSetMode_Pdc.
////     * @param rdoSetMode_Pdc New value of property rdoSetMode_Pdc.
////     */
////    public void setRdoSetMode_Pdc(boolean rdoSetMode_Pdc) {
////        this.rdoSetMode_Pdc = rdoSetMode_Pdc;
////    }
//    
//    /**
//     * Getter for property rdoChqBounce_Yes.
//     * @return Value of property rdoChqBounce_Yes.
//     */
//    public boolean isRdoChqBounce_Yes() {
//        return rdoChqBounce_Yes;
//    }
//    
//    /**
//     * Setter for property rdoChqBounce_Yes.
//     * @param rdoChqBounce_Yes New value of property rdoChqBounce_Yes.
//     */
//    public void setRdoChqBounce_Yes(boolean rdoChqBounce_Yes) {
//        this.rdoChqBounce_Yes = rdoChqBounce_Yes;
//    }
//    
//    /**
//     * Getter for property rdoChqBounce_No.
//     * @return Value of property rdoChqBounce_No.
//     */
//    public boolean isRdoChqBounce_No() {
//        return rdoChqBounce_No;
//    }
//    
//    /**
//     * Setter for property rdoChqBounce_No.
//     * @param rdoChqBounce_No New value of property rdoChqBounce_No.
//     */
//    public void setRdoChqBounce_No(boolean rdoChqBounce_No) {
//        this.rdoChqBounce_No = rdoChqBounce_No;
//    }
//    
//    /**
//     * Getter for property cboBounReason.
//     * @return Value of property cboBounReason.
//     */
//    public java.lang.String getCboBounReason() {
//        return cboBounReason;
//    }
//    
//    /**
//     * Setter for property cboBounReason.
//     * @param cboBounReason New value of property cboBounReason.
//     */
//    public void setCboBounReason(java.lang.String cboBounReason) {
//        this.cboBounReason = cboBounReason;
//    }
//    
//    /**
//     * Getter for property txtQty.
//     * @return Value of property txtQty.
//     */
//    public java.lang.String getTxtQty() {
//        return txtQty;
//    }
//    
//    /**
//     * Setter for property txtQty.
//     * @param txtQty New value of property txtQty.
//     */
//    public void setTxtQty(java.lang.String txtQty) {
//        this.txtQty = txtQty;
//    }
//    
//    /**
//     * Getter for property txtToChqNo.
//     * @return Value of property txtToChqNo.
//     */
//    public java.lang.String getTxtToChqNo() {
//        return txtToChqNo;
//    }
//    
//    /**
//     * Setter for property txtToChqNo.
//     * @param txtToChqNo New value of property txtToChqNo.
//     */
//    public void setTxtToChqNo(java.lang.String txtToChqNo) {
//        this.txtToChqNo = txtToChqNo;
//    }
//    
//    /**
//     * Getter for property tdtClearingDt.
//     * @return Value of property tdtClearingDt.
//     */
//    public java.lang.String getTdtClearingDt() {
//        return tdtClearingDt;
//    }
//    
//    /**
//     * Setter for property tdtClearingDt.
//     * @param tdtClearingDt New value of property tdtClearingDt.
//     */
//    public void setTdtClearingDt(java.lang.String tdtClearingDt) {
//        this.tdtClearingDt = tdtClearingDt;
//    }
    public void doAction(int saveMode) {
        log.info("In doAction...");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
//            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
//                if( getCommand() != null || getAuthorizeMap() != null ){
                    log.info("before doActionPerform...");
                    doActionPerform(saveMode);
//                }
//                else{
//                    log.info("In doAction()-->getCommand() is null:" );
//                }
//            }
//            else
//                log.info("In doAction()-->actionType is null:" );
        } catch (Exception e) {
            System.out.println("e--------->"+e);
            parseException.logException(e,true);
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in doAction():"+e);
        }
    }
    private void doActionPerform(int saveMode) throws Exception{
        if(saveMode == 1){
        HashMap objAcctTransTOMap = setAcctTrans();
//        HashMap objSettlementBankTOMap = setSettlementBank();
        HashMap data = new HashMap();
        data.put("ActTransTO", objAcctTransTOMap);
//        data.put("SettlementBankTO", objSettlementBankTOMap);
        HashMap proxyResultMap = new HashMap();
        proxyResultMap = proxy.execute(data,operationMap);
//        System.out.println("proxyresultmap#$$$$$"+proxyResultMap);
        }else{
//             HashMap mapData=null;
             HashMap data = new HashMap();
             data.put("ActTransTO", "ActTransTO");
        try {
            final HashMap mapData = proxy.executeQuery(data, operationMap);
//            mapData = proxy.executeQuery(data,data);
//            ActTransTO objActTransTO = (ActTransTO) mapData.get("ActTransTO");
            System.out.println("mapData#$$$$$"+mapData);
            setActTransTO(mapData);
        } catch( Exception e ) {
//            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
        }
    }
    
    /**
     * Getter for property cbmBankName.
     * @return Value of property cbmBankName.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBankName() {
        return cbmBankName;
    }
    
    /**
     * Setter for property cbmBankName.
     * @param cbmBankName New value of property cbmBankName.
     */
    public void setCbmBankName(com.see.truetransact.clientutil.ComboBoxModel cbmBankName) {
        this.cbmBankName = cbmBankName;
    }
    
    /**
     * Getter for property cbmBranchName.
     * @return Value of property cbmBranchName.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranchName() {
        return cbmBranchName;
    }
    
    /**
     * Setter for property cbmBranchName.
     * @param cbmBranchName New value of property cbmBranchName.
     */
    public void setCbmBranchName(com.see.truetransact.clientutil.ComboBoxModel cbmBranchName) {
        this.cbmBranchName = cbmBranchName;
    }
    
    /**
     * Getter for property cboBankName.
     * @return Value of property cboBankName.
     */
    public java.lang.String getCboBankName() {
        return cboBankName;
    }
    
    /**
     * Setter for property cboBankName.
     * @param cboBankName New value of property cboBankName.
     */
    public void setCboBankName(java.lang.String cboBankName) {
        this.cboBankName = cboBankName;
        setChanged();
    }
    
    /**
     * Getter for property cboBranchName.
     * @return Value of property cboBranchName.
     */
    public java.lang.String getCboBranchName() {
        return cboBranchName;
    }
    
    /**
     * Setter for property cboBranchName.
     * @param cboBranchName New value of property cboBranchName.
     */
    public void setCboBranchName(java.lang.String cboBranchName) {
        this.cboBranchName = cboBranchName;
        setChanged();
    }
    
    /**
     * Getter for property txtRefNo.
     * @return Value of property txtRefNo.
     */
    public java.lang.String getTxtRefNo() {
        return txtRefNo;
    }
    
    /**
     * Setter for property txtRefNo.
     * @param txtRefNo New value of property txtRefNo.
     */
    public void setTxtRefNo(java.lang.String txtRefNo) {
        this.txtRefNo = txtRefNo;
        setChanged();
    }
    
    /**
     * Getter for property txtAmt.
     * @return Value of property txtAmt.
     */
    public java.lang.String getTxtAmt() {
        return txtAmt;
    }
    
    /**
     * Setter for property txtAmt.
     * @param txtAmt New value of property txtAmt.
     */
    public void setTxtAmt(java.lang.String txtAmt) {
        this.txtAmt = txtAmt;
        setChanged();
    }
    
    /**
     * Getter for property txtSecDepRec.
     * @return Value of property txtSecDepRec.
     */
    public java.lang.String getTxtSecDepRec() {
        return txtSecDepRec;
    }
    
    /**
     * Setter for property txtSecDepRec.
     * @param txtSecDepRec New value of property txtSecDepRec.
     */
    public void setTxtSecDepRec(java.lang.String txtSecDepRec) {
        this.txtSecDepRec = txtSecDepRec;
        setChanged();
    }
    
    /**
     * Getter for property txtPoDdNo.
     * @return Value of property txtPoDdNo.
     */
    public java.lang.String getTxtPoDdNo() {
        return txtPoDdNo;
    }
    
    /**
     * Setter for property txtPoDdNo.
     * @param txtPoDdNo New value of property txtPoDdNo.
     */
    public void setTxtPoDdNo(java.lang.String txtPoDdNo) {
        this.txtPoDdNo = txtPoDdNo;
        setChanged();
    }
    
    /**
     * Getter for property tdtPoDdDate.
     * @return Value of property tdtPoDdDate.
     */
    public java.lang.String getTdtPoDdDate() {
        return tdtPoDdDate;
    }
    
    /**
     * Setter for property tdtPoDdDate.
     * @param tdtPoDdDate New value of property tdtPoDdDate.
     */
    public void setTdtPoDdDate(java.lang.String tdtPoDdDate) {
        this.tdtPoDdDate = tdtPoDdDate;
        setChanged();
    }
    
    /**
     * Getter for property txtPoDdAmt.
     * @return Value of property txtPoDdAmt.
     */
    public java.lang.String getTxtPoDdAmt() {
        return txtPoDdAmt;
    }
    
    /**
     * Setter for property txtPoDdAmt.
     * @param txtPoDdAmt New value of property txtPoDdAmt.
     */
    public void setTxtPoDdAmt(java.lang.String txtPoDdAmt) {
        this.txtPoDdAmt = txtPoDdAmt;
        setChanged();
    }
    
    /**
     * Getter for property txtRemarks.
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }
    
    /**
     * Setter for property txtRemarks.
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    
    /**
     * Getter for property actCommand.
     * @return Value of property actCommand.
     */
    public java.lang.String getActCommand() {
        return actCommand;
    }
    
    /**
     * Setter for property actCommand.
     * @param actCommand New value of property actCommand.
     */
    public void setActCommand(java.lang.String actCommand) {
        this.actCommand = actCommand;
    }
    
    /**
     * Getter for property acctNum.
     * @return Value of property acctNum.
     */
    public java.lang.String getAcctNum() {
        return acctNum;
    }
    
    /**
     * Setter for property acctNum.
     * @param acctNum New value of property acctNum.
     */
    public void setAcctNum(java.lang.String acctNum) {
        this.acctNum = acctNum;
    }
    
//    /**
//     * Getter for property rdoActype_Od.
//     * @return Value of property rdoActype_Od.
//     */
//    public boolean isRdoActype_Od() {
//        return rdoActype_Od;
//    }
//    
//    /**
//     * Setter for property rdoActype_Od.
//     * @param rdoActype_Od New value of property rdoActype_Od.
//     */
//    public void setRdoActype_Od(boolean rdoActype_Od) {
//        this.rdoActype_Od = rdoActype_Od;
//    }
//    
//    /**
//     * Getter for property rdoSetMode_Ecs.
//     * @return Value of property rdoSetMode_Ecs.
//     */
//    public boolean isRdoSetMode_Ecs() {
//        return rdoSetMode_Ecs;
//    }
//    
//    /**
//     * Setter for property rdoSetMode_Ecs.
//     * @param rdoSetMode_Ecs New value of property rdoSetMode_Ecs.
//     */
//    public void setRdoSetMode_Ecs(boolean rdoSetMode_Ecs) {
//        this.rdoSetMode_Ecs = rdoSetMode_Ecs;
//    }
//    
//    /**
//     * Getter for property rdoSetMode_Othrs.
//     * @return Value of property rdoSetMode_Othrs.
//     */
//    public boolean isRdoSetMode_Othrs() {
//        return rdoSetMode_Othrs;
//    }
//    
//    /**
//     * Setter for property rdoSetMode_Othrs.
//     * @param rdoSetMode_Othrs New value of property rdoSetMode_Othrs.
//     */
//    public void setRdoSetMode_Othrs(boolean rdoSetMode_Othrs) {
//        this.rdoSetMode_Othrs = rdoSetMode_Othrs;
//    }
//    
//    /**
//     * Getter for property rdoActype_Ca.
//     * @return Value of property rdoActype_Ca.
//     */
//    public boolean isRdoActype_Ca() {
//        return rdoActype_Ca;
//    }
//    
//    /**
//     * Setter for property rdoActype_Ca.
//     * @param rdoActype_Ca New value of property rdoActype_Ca.
//     */
//    public void setRdoActype_Ca(boolean rdoActype_Ca) {
//        this.rdoActype_Ca = rdoActype_Ca;
//    }
//    
//    /**
//     * Getter for property rdoReturnChq_Yes.
//     * @return Value of property rdoReturnChq_Yes.
//     */
//    public boolean isRdoReturnChq_Yes() {
//        return rdoReturnChq_Yes;
//    }
//    
//    /**
//     * Setter for property rdoReturnChq_Yes.
//     * @param rdoReturnChq_Yes New value of property rdoReturnChq_Yes.
//     */
//    public void setRdoReturnChq_Yes(boolean rdoReturnChq_Yes) {
//        this.rdoReturnChq_Yes = rdoReturnChq_Yes;
//    }
//    
//    /**
//     * Getter for property rdoReturnChq_No.
//     * @return Value of property rdoReturnChq_No.
//     */
//    public boolean isRdoReturnChq_No() {
//        return rdoReturnChq_No;
//    }
//    
//    /**
//     * Setter for property rdoReturnChq_No.
//     * @param rdoReturnChq_No New value of property rdoReturnChq_No.
//     */
//    public void setRdoReturnChq_No(boolean rdoReturnChq_No) {
//        this.rdoReturnChq_No = rdoReturnChq_No;
//    }
//    
}
