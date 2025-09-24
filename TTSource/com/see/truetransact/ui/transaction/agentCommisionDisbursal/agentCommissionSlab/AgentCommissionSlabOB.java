/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SHGOB.java
 *
 * Created on Sat Oct 15 11:56:39 IST 2011
 */

package com.see.truetransact.ui.transaction.agentCommisionDisbursal.agentCommissionSlab;

import com.see.truetransact.ui.termloan.groupLoan.*;
import com.see.truetransact.ui.termloan.SHG.*;
import java.util.Observable;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import java.util.GregorianCalendar;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.product.deposits.AgentCommisonSlabTO;
import com.see.truetransact.transferobject.termloan.SHG.SHGTO;
import com.see.truetransact.transferobject.termloan.groupLoan.GroupLoanCustomerTO;

/**
 *
 * @author
 */

public class AgentCommissionSlabOB extends Observable{
    
    private String txtMemberNo = "";
    private String txtGroupId = "";
    private String lblMemberNameVal = "";
    private String lblStreetVal = "";
    private String lblAreaVal = "";
    private String lblCityVal = "";
    private String lblStateVal = "";
    private String txtGroupName = "";
    private String cboProdId = "";
    private String txtAccountNo = "";
    private ComboBoxModel cbmProdId;
    private String custID = "";
    private String limitAmt = "";
    private String groupLoanNo = "";
    private String groupLoanName = "";
    private String custName = "";
    private String creditNo = "";
    private double totalAmount = 0.0;
    private String prodType = "";
    private ComboBoxModel cbmProdTypCr;
    private ComboBoxModel cbmProdIdCr;
    private HashMap lookUpHash;
    private String AcctNo = "";
    private String customerActNum = "";
    private String customerActProdType = "";
    private String customerActProdId= "";
    private Date agentFromDt = null;
    private Date agentToDt = null;
    private double agentFromAmt = 0.0;
    private double agentToAmt =  0.0;
    private double agentCommission =  0.0;
    
        public double getAgentCommission() {
        return agentCommission;
    }

    public void setAgentCommission(double agentCommission) {
        this.agentCommission = agentCommission;
    }

    public double getAgentFromAmt() {
        return agentFromAmt;
    }

    public void setAgentFromAmt(double agentFromAmt) {
        this.agentFromAmt = agentFromAmt;
    }

    public Date getAgentFromDt() {
        return agentFromDt;
    }

    public void setAgentFromDt(Date agentFromDt) {
        this.agentFromDt = agentFromDt;
    }

    public double getAgentToAmt() {
        return agentToAmt;
    }

    public void setAgentToAmt(double agentToAmt) {
        this.agentToAmt = agentToAmt;
    }

    public Date getAgentToDt() {
        return agentToDt;
    }

    public void setAgentToDt(Date agentToDt) {
        this.agentToDt = agentToDt;
    }

    public String getCustomerActNum() {
        return customerActNum;
    }

    public void setCustomerActNum(String customerActNum) {
        this.customerActNum = customerActNum;
    }

    public String getCustomerActProdId() {
        return customerActProdId;
    }

    public void setCustomerActProdId(String customerActProdId) {
        this.customerActProdId = customerActProdId;
    }

    public String getCustomerActProdType() {
        return customerActProdType;
    }

    public void setCustomerActProdType(String customerActProdType) {
        this.customerActProdType = customerActProdType;
    }
    
    public ComboBoxModel getCbmProdIdCr() {
        return cbmProdIdCr;
    }

    public void setCbmProdIdCr(ComboBoxModel cbmProdIdCr) {
        this.cbmProdIdCr = cbmProdIdCr;
    }

    public ComboBoxModel getCbmProdTypCr() {
        return cbmProdTypCr;
    }

    public void setCbmProdTypCr(ComboBoxModel cbmProdTypCr) {
        this.cbmProdTypCr = cbmProdTypCr;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }
    private String prodID = "";
    
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getGroupLoanName() {
        return groupLoanName;
    }

    public void setGroupLoanName(String groupLoanName) {
        this.groupLoanName = groupLoanName;
    }
    
    public String getGroupLoanNo() {
        return groupLoanNo;
    }

    public void setGroupLoanNo(String groupLoanNo) {
        this.groupLoanNo = groupLoanNo;
    }
    
    public String getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(String limitAmt) {
        this.limitAmt = limitAmt;
    }
    
    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblSHGDetails;
    
    private boolean newData = false;
    private LinkedHashMap groupLoancustMap;
    private LinkedHashMap deletedgroupLoancustMap;
    
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(AgentCommissionSlabOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private int actionType;
    private AgentCommisonSlabTO agentCommisonSlabTO = null;
    
    
    /** Creates a new instance of TDS MiantenanceOB */
    public AgentCommissionSlabOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();            
            map.put(CommonConstants.JNDI, "AgentJNDI");
            map.put(CommonConstants.HOME, "agent.AgentHome");
            map.put(CommonConstants.REMOTE, "agent.Agent");
            setTableTile();
            tblSHGDetails= new EnhancedTableModel(null, tableTitle);
            fillDropdown();
            fillDropdownAct();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("From Date");
        tableTitle.add("To Date");
        tableTitle.add("From Amount");
        tableTitle.add("To Amount");
        tableTitle.add("Commission");
        IncVal = new ArrayList();
    }
    
    private void fillDropdown() throws Exception{
        try{
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"Cash.getAccProductAD");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmProdId= new ComboBoxModel(key,value);
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void fillDropdownAct() throws Exception {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("GROUP_LOAN_PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            fillData((HashMap) keyValue.get("GROUP_LOAN_PRODUCTTYPE"));
            cbmProdTypCr = new ComboBoxModel(key, value);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            if(data.containsKey("AGENT_COMMISSION_SLAB")){
                groupLoancustMap = (LinkedHashMap)data.get("AGENT_COMMISSION_SLAB");
                ArrayList addList =new ArrayList(groupLoancustMap.keySet());
                for(int i=0;i<addList.size();i++){
                    AgentCommisonSlabTO agentCommisonSlabTO = (AgentCommisonSlabTO)  groupLoancustMap.get(addList.get(i));
                    agentCommisonSlabTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    agentCommisonSlabTO.setAuthorizeStatus("");
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(agentCommisonSlabTO.getFromDt());
                    incTabRow.add(agentCommisonSlabTO.getToDt());
                    incTabRow.add(agentCommisonSlabTO.getFromAmount());
                    incTabRow.add(agentCommisonSlabTO.getToAmount());
                    incTabRow.add(agentCommisonSlabTO.getCommissionAmount());
                    tblSHGDetails.addRow(incTabRow);
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void getCustName(String custId){
        String custName = "";
        HashMap custMap = new HashMap();
        custMap.put("CUST_ID",custId);
        custMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getSelectCustomer",custMap);
        if(lst!=null && lst.size()>0){
            custMap = (HashMap)lst.get(0);
            setCustName(CommonUtil.convertObjToStr(custMap.get("CUSTOMER")));
            setLblMemberNameVal(getCustName());
        }
    } 
    public void getCustNameForLoan(String actNum){
        String custName = "";
        HashMap custMap = new HashMap();
        custMap.put("ACT_NUM",actNum);
        custMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getJoinCustomer",custMap);
        if(lst!=null && lst.size()>0){
            custMap = (HashMap)lst.get(0);
            setGroupLoanName(CommonUtil.convertObjToStr(custMap.get("CUSTOMER")));
            setGroupLoanNo(actNum);
        }
    }
    
    public double setOdAmount(String actNum){
        double amount = 0.0;
        HashMap odMap = new HashMap();
        odMap.put("ACT_NUM",actNum);
        odMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getOdAmount",odMap);
        if(lst!=null && lst.size()>0){
            odMap = (HashMap)lst.get(0);
            amount = CommonUtil.convertObjToDouble(odMap.get("AVAILABLE_BALANCE"));
        }
        return amount;
    }
    
    public boolean isExist(String ccNum){
        boolean ccFlag = false;
        ccNum = ccNum.toUpperCase();
        HashMap odMap = new HashMap();
        odMap.put("CC_NUM",ccNum);
        List lst = ClientUtil.executeQuery("getCCNoPresent",odMap);
        if(lst!=null && lst.size()>0){          
           return true;
        }else{
          return false;
        }
    }
    
    public boolean isDailyGroupLoan(String actNum){
        boolean ccFlag = false;
        HashMap odMap = new HashMap();
        odMap.put("ACT_NUM",actNum);
        List lst = ClientUtil.executeQuery("getDailyGroupLoan",odMap);
        if(lst!=null && lst.size()>0){          
           return true;
        }else{
          return false;
        }
    }
    
    public HashMap isCustActNumExist(String actNum){
        boolean ccFlag = false;
        HashMap odMap = new HashMap();
        odMap.put("ACT_NUM",actNum);
        List lst = ClientUtil.executeQuery("checkCustActExist",odMap);
        if(lst!=null && lst.size()>0){
            odMap = (HashMap) lst.get(0);
           return odMap;
        }
        return null;
    }
    
    public boolean isCustActNumExistInGrid(String actNum)  throws Exception{
        if(groupLoancustMap!=null && groupLoancustMap.size()>0){
            ArrayList addList =new ArrayList(groupLoancustMap.keySet());
            int length = addList.size();
            for(int i=0; i<length; i++){
                ArrayList incTabRow = new ArrayList();
                GroupLoanCustomerTO groupLoanCustomerTO = (GroupLoanCustomerTO) groupLoancustMap.get(addList.get(i));                     
                if(groupLoanCustomerTO.getCustomerActNum().equals(actNum)){
                    System.out.println("groupLoanCustomerTO###########populateTable"+groupLoanCustomerTO);   
                    return true;
                }
            }  
        }
        System.out.println("groupLoanCustomerTO###########populateTable"+actNum);   
        return false;
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
    
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;           
            final AgentCommisonSlabTO agentCommisonSlabTO = new AgentCommisonSlabTO();  
            if( groupLoancustMap== null ){
                groupLoancustMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    agentCommisonSlabTO.setStatusDt(ClientUtil.getCurrentDate());
                    agentCommisonSlabTO.setStatus("CREATED"); 
                }else{
                    agentCommisonSlabTO.setStatusDt(ClientUtil.getCurrentDate());
                    agentCommisonSlabTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                agentCommisonSlabTO.setStatusDt(ClientUtil.getCurrentDate());
                    agentCommisonSlabTO.setStatus("CREATED"); 
            }
            
            if(isNewData()){
                ArrayList data = tblSHGDetails.getDataArrayList();
            }
            agentCommisonSlabTO.setFromDt(getAgentFromDt());
            //agentCommisonSlabTO.setToDt(getAgentToDt());
            agentCommisonSlabTO.setFromAmount(getAgentFromAmt());
            agentCommisonSlabTO.setToAmount(getAgentToAmt());
            agentCommisonSlabTO.setCommissionAmount(getAgentCommission());
            agentCommisonSlabTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            groupLoancustMap.put(agentCommisonSlabTO.getFromDt(),agentCommisonSlabTO);
            updateScheduleDetails(rowSel,agentCommisonSlabTO);
            //totalAmount = totalAmount+CommonUtil.convertObjToDouble(getLimitAmt());
            //setTotalAmount(totalAmount);
            //System.out.println("totalAmount#####"+totalAmount);
//            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    private void updateScheduleDetails(int rowSel, AgentCommisonSlabTO agentCommisonSlab)  throws Exception{
        Object selectedRow,selectedRow1;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblSHGDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblSHGDetails.getDataArrayList().get(j)).get(0);
            selectedRow1 = ((ArrayList)tblSHGDetails.getDataArrayList().get(j)).get(1);
             if(agentCommisonSlab.getFromDt().equals(CommonUtil.convertObjToStr(selectedRow)) && 
                   agentCommisonSlab.getToDt().equals(CommonUtil.convertObjToStr(selectedRow1))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblSHGDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getAgentFromDt());
                IncParRow.add(getAgentToDt());
                IncParRow.add(getAgentFromAmt());
                IncParRow.add(getAgentToAmt());
                IncParRow.add(getAgentCommission());
                tblSHGDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getAgentFromDt());
            IncParRow.add(getAgentToDt());
            IncParRow.add(getAgentFromAmt());
            IncParRow.add(getAgentToAmt());
            IncParRow.add(getAgentCommission());
            tblSHGDetails.insertRow(tblSHGDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void populateSHGDetails(String row){
        try{
            resetSHGDetails();
            final AgentCommisonSlabTO agentCommisonSlabTO = (AgentCommisonSlabTO)groupLoancustMap.get(row);
            populateTableData(agentCommisonSlabTO);            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteTableData(String val, int row){
        if(deletedgroupLoancustMap== null){
            deletedgroupLoancustMap = new LinkedHashMap();
        }
        System.out.println("groupLoancustMap$@$@@$"+groupLoancustMap);
        AgentCommisonSlabTO agentCommisonSlabTO = (AgentCommisonSlabTO) groupLoancustMap.get(val);
        agentCommisonSlabTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedgroupLoancustMap.put(CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(row,0)),groupLoancustMap.get(val));
        Object obj;
        obj=val;
        groupLoancustMap.remove(val);
        resetTableValues();
        try{
            populateTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(groupLoancustMap.keySet());
        ArrayList addList =new ArrayList(groupLoancustMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            GroupLoanCustomerTO groupLoanCustomerTO = (GroupLoanCustomerTO) groupLoancustMap.get(addList.get(i));
            System.out.println("groupLoanCustomerTO###########populateTable"+groupLoanCustomerTO);
            IncVal.add(groupLoanCustomerTO);
            if(!groupLoanCustomerTO.getStatus().equals("DELETED")){
                incTabRow.add(groupLoanCustomerTO.getCustId());
                getCustName(groupLoanCustomerTO.getCustId());
                incTabRow.add(getLblMemberNameVal());
                incTabRow.add(groupLoanCustomerTO.getCreditNo());
                incTabRow.add(groupLoanCustomerTO.getLimitAmt());
                tblSHGDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    private void populateTableData(AgentCommisonSlabTO agentCommisonSlabTO)  throws Exception{
        System.out.println("AgentCommisonSlabTO###########"+agentCommisonSlabTO);
        setAgentFromDt(agentCommisonSlabTO.getFromDt());
        setAgentToDt(agentCommisonSlabTO.getToDt());
        setAgentFromAmt(agentCommisonSlabTO.getFromAmount());
        setAgentToAmt(agentCommisonSlabTO.getToAmount());
        setAgentCommission(agentCommisonSlabTO.getCommissionAmount());
        notifyObservers();
    }
    
    
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        try{
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
            System.out.println("groupLoancustMap#####"+groupLoancustMap);
            data.put("AGENT_COMMISSION_SLAB",groupLoancustMap);
            if(deletedgroupLoancustMap!=null && deletedgroupLoancustMap.size()>0 ){
               // data.put("deletedGroupLoanTableDetails",deletedgroupLoancustMap);
            }
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        if(getTxtGroupId().length()>0){
            data.put("SHG_ID",getTxtGroupId());
        }
        data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        System.out.println("data in SHG OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
//        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
        }catch(Exception e){
            ClientUtil.showMessageWindow((e.getMessage()));
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
    
    
    protected void getCustomerAddressDetails(String value){
        HashMap custAddressMap = new HashMap();
        custAddressMap.put("CUST_ID",value);
        custAddressMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getSelectAccInfoDisplay",custAddressMap);
        if(lst!=null && lst.size()>0){
            custAddressMap = (HashMap)lst.get(0);
            setLblStreetVal(CommonUtil.convertObjToStr(custAddressMap.get("STREET")));
            setLblAreaVal(CommonUtil.convertObjToStr(custAddressMap.get("AREA")));
            setLblCityVal(CommonUtil.convertObjToStr(custAddressMap.get("CITY1")));
            setLblStateVal(CommonUtil.convertObjToStr(custAddressMap.get("STATE1")));
        }
    }
    
    public static boolean validateCreditNo(com.see.truetransact.uicomponent.CTextField txtCCNO){
        boolean valid = false ;
        int alpha = 0;
        int numeric = 0;
        int other = 0;
        if(txtCCNO.getText()!=null && txtCCNO.getText()!=""){
            txtCCNO.setText(txtCCNO.getText().toUpperCase());
            String ccNum = txtCCNO.getText();
            valid = true; 
            if(ccNum.length()<8){
                valid = false;
                System.out.println("exit from here...");
            }
            if(valid){
                for(int i = 0, j = ccNum.length() ; i < j; i++){
                    System.out.println("ccNum.charAt(i)#####"+ccNum.charAt(i));
                      if((int)ccNum.charAt(i) > 64 && (int)ccNum.charAt(i) < 91){
                           alpha+=1;
                           System.out.println("alpha#####"+ccNum.charAt(i));
                      }
                      else if((int)ccNum.charAt(i) > 47 && (int)ccNum.charAt(i) < 58){
                           numeric+=1;
                           System.out.println("numeric#####"+numeric);
                      }else
                      {
                           other+=1;
                           System.out.println("other#####"+other);
                      }
                }
                System.out.println("alpha@@@@"+alpha+"numeric@@@@"+numeric+"other@@@@"+other);
                if(alpha>0 && numeric>0){
                    valid = true ;
                }else
                    valid = false;
                if(other>0){
                    valid = false;
                }
            }
            if(!valid){
                System.out.println("Not valid#####"+valid);
                txtCCNO.setText("");
            }
        }
        System.out.println("valid#####"+valid);
        return valid;
    }
    
    public void resetForm(){
        groupLoancustMap = null;
        deletedgroupLoancustMap= null;
        resetTableValues();
        setTxtGroupId("");
        setTxtGroupName("");
        setCboProdId("");
        setTxtAccountNo("");
        resetSHGDetails();
        setChanged();
    }
    
    public void resetSHGDetails(){
        setTxtMemberNo("");
        setLblAreaVal("");
        setLblCityVal("");
        setLblMemberNameVal("");
        setLblStateVal("");
        setLblStreetVal("");
        setCustID("");
        setGroupLoanName("");
        setLimitAmt("");
        setCreditNo("");
        setCustomerActNum("");
        setCustomerActProdId("");
        setCustomerActProdType("");
     }
    
    public void resetTableValues(){
        tblSHGDetails.setDataArrayList(null,tableTitle);
    }
    
    // Setter method for txtMemberNo
    void setTxtMemberNo(String txtMemberNo){
        this.txtMemberNo = txtMemberNo;
        setChanged();
    }
    // Getter method for txtMemberNo
    String getTxtMemberNo(){
        return this.txtMemberNo;
    }
    
    // Setter method for txtGroupId
    void setTxtGroupId(String txtGroupId){
        this.txtGroupId = txtGroupId;
        setChanged();
    }
    // Getter method for txtGroupId
    String getTxtGroupId(){
        return this.txtGroupId;
    }
    
    /**
     * Getter for property tblSHGDetails.
     * @return Value of property tblSHGDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblSHGDetails() {
        return tblSHGDetails;
    }
    
    /**
     * Setter for property tblSHGDetails.
     * @param tblSHGDetails New value of property tblSHGDetails.
     */
    public void setTblSHGDetails(com.see.truetransact.clientutil.EnhancedTableModel tblSHGDetails) {
        this.tblSHGDetails = tblSHGDetails;
    }
    
    /**
     * Getter for property newData.
     * @return Value of property newData.
     */
    public boolean isNewData() {
        return newData;
    }
    
    /**
     * Setter for property newData.
     * @param newData New value of property newData.
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
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
     * Getter for property lblMemberNameVal.
     * @return Value of property lblMemberNameVal.
     */
    public java.lang.String getLblMemberNameVal() {
        return lblMemberNameVal;
    }
    
    /**
     * Setter for property lblMemberNameVal.
     * @param lblMemberNameVal New value of property lblMemberNameVal.
     */
    public void setLblMemberNameVal(java.lang.String lblMemberNameVal) {
        this.lblMemberNameVal = lblMemberNameVal;
    }
    
    /**
     * Getter for property lblStreetVal.
     * @return Value of property lblStreetVal.
     */
    public java.lang.String getLblStreetVal() {
        return lblStreetVal;
    }
    
    /**
     * Setter for property lblStreetVal.
     * @param lblStreetVal New value of property lblStreetVal.
     */
    public void setLblStreetVal(java.lang.String lblStreetVal) {
        this.lblStreetVal = lblStreetVal;
    }
    
    /**
     * Getter for property lblAreaVal.
     * @return Value of property lblAreaVal.
     */
    public java.lang.String getLblAreaVal() {
        return lblAreaVal;
    }
    
    /**
     * Setter for property lblAreaVal.
     * @param lblAreaVal New value of property lblAreaVal.
     */
    public void setLblAreaVal(java.lang.String lblAreaVal) {
        this.lblAreaVal = lblAreaVal;
    }
    
    /**
     * Getter for property lblCityVal.
     * @return Value of property lblCityVal.
     */
    public java.lang.String getLblCityVal() {
        return lblCityVal;
    }
    
    /**
     * Setter for property lblCityVal.
     * @param lblCityVal New value of property lblCityVal.
     */
    public void setLblCityVal(java.lang.String lblCityVal) {
        this.lblCityVal = lblCityVal;
    }
    
    /**
     * Getter for property lblStateVal.
     * @return Value of property lblStateVal.
     */
    public java.lang.String getLblStateVal() {
        return lblStateVal;
    }
    
    /**
     * Setter for property lblStateVal.
     * @param lblStateVal New value of property lblStateVal.
     */
    public void setLblStateVal(java.lang.String lblStateVal) {
        this.lblStateVal = lblStateVal;
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
     * Getter for property txtGroupName.
     * @return Value of property txtGroupName.
     */
    public java.lang.String getTxtGroupName() {
        return txtGroupName;
    }    
    
    /**
     * Setter for property txtGroupName.
     * @param txtGroupName New value of property txtGroupName.
     */
    public void setTxtGroupName(java.lang.String txtGroupName) {
        this.txtGroupName = txtGroupName;
    }
    
    /**
     * Getter for property cboProdId.
     * @return Value of property cboProdId.
     */
    public java.lang.String getCboProdId() {
        return cboProdId;
    }
    
    /**
     * Setter for property cboProdId.
     * @param cboProdId New value of property cboProdId.
     */
    public void setCboProdId(java.lang.String cboProdId) {
        this.cboProdId = cboProdId;
    }
    
    /**
     * Getter for property txtAccountNo.
     * @return Value of property txtAccountNo.
     */
    public java.lang.String getTxtAccountNo() {
        return txtAccountNo;
    }
    
    /**
     * Setter for property txtAccountNo.
     * @param txtAccountNo New value of property txtAccountNo.
     */
    public void setTxtAccountNo(java.lang.String txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }
    
    /**
     * Getter for property cbmProdId.
     * @return Value of property cbmProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }
    
    /**
     * Setter for property cbmProdId.
     * @param cbmProdId New value of property cbmProdId.
     */
    public void setCbmProdId(com.see.truetransact.clientutil.ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }
    
    public String getAcctNo() {
        return AcctNo;
    }

    public void setAcctNo(String AcctNo) {
        this.AcctNo = AcctNo;
    }
    
}