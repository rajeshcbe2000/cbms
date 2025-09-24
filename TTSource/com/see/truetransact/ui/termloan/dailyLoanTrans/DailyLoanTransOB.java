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

package com.see.truetransact.ui.termloan.dailyLoanTrans;

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
import com.see.truetransact.uicomponent.CObservable;

//import com.see.truetransact.ui.common.transaction.TransactionOB ;
/**
 *
 * @author  Suresh
 */

public class DailyLoanTransOB extends CObservable{
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
    private final static Logger log = Logger.getLogger(DailyLoanTransOB.class);
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

       private java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy");
    private ComboBoxModel cbmAgentType,cbmLoanAdjustmentAgentType;

    /** Creates a new instance of TDS MiantenanceOB */
    public DailyLoanTransOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DailyLoanTransJNDI");
            map.put(CommonConstants.HOME, "DailyLoanTransJNDIHome");
            map.put(CommonConstants.REMOTE, "DailyLoanTrans");
            setTableTile();
            fillDropDown();
            setAdjustmentTableTile();
            setAuthAdjustmentTableTile();
            tblLoanAdjustmentTable = new EnhancedTableModel(null, adjustmentTableTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("Loan No");
        tableTitle.add("Name");
        tableTitle.add("Limit");
        tableTitle.add("Last Int Dt");
        tableTitle.add("Balance");
        tableTitle.add("Princ Due");
        tableTitle.add("Interest");
        tableTitle.add("Penal");
        tableTitle.add("Charge");
        tableTitle.add("Total Due");
        tableTitle.add("Repayment");
        tableTitle.add("Commission");
        IncVal = new ArrayList();
    }
    
    private void setAdjustmentTableTile() throws Exception{
        adjustmentTableTitle.add("Select");
        adjustmentTableTitle.add("Loan No");
        adjustmentTableTitle.add("Name");
        adjustmentTableTitle.add("Limit");
        adjustmentTableTitle.add("Last Int Dt");
        adjustmentTableTitle.add("Balance");
        adjustmentTableTitle.add("Princ Due");
        adjustmentTableTitle.add("Interest");
        adjustmentTableTitle.add("Penal");
        adjustmentTableTitle.add("Charge");
        adjustmentTableTitle.add("Total Due");
        adjustmentTableTitle.add("Repayment");
        IncVal = new ArrayList();
    }
    
    private void setAuthAdjustmentTableTile() throws Exception{
        authAdjustTableTitle.add("Loan No");
        authAdjustTableTitle.add("Name");
        authAdjustTableTitle.add("Limit");
        authAdjustTableTitle.add("Last Int Dt");
        authAdjustTableTitle.add("Balance");
        authAdjustTableTitle.add("Princ Due");
        authAdjustTableTitle.add("Interest");
        authAdjustTableTitle.add("Penal");
        authAdjustTableTitle.add("Charge");
        authAdjustTableTitle.add("Total Due");
        authAdjustTableTitle.add("Repayment");
        IncVal = new ArrayList();
    }
    
   // To Fill the Combo boxes in the UI
    private void fillDropDown() throws Exception {
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List lst = (List)ClientUtil.executeQuery("getAgentIdName", where);
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
        try{
            if (whereMap.containsKey("DAILY_LOAN_COLLECTION")) {
                getEditTableDetails(whereMap);
            }else if (whereMap.containsKey("LOAN_ADJUSTMENT")) {
                getAdjustmentTableDetails(whereMap);
            }
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    public void getAdjustmentTableDetails(HashMap whereMap){
        try{
            ArrayList rowList = new ArrayList();
            HashMap dataMap = new HashMap();
            List adjustmentList = null;
            System.out.println("#$@$@$#@$@#$@ whereMap : " + whereMap);
            adjustmentList = ClientUtil.executeQuery("getDailyLoanAdjustmentTransEnquiry", whereMap);
            System.out.println("#$@$@$#@$@#$@ editList : " + adjustmentList);
            if (adjustmentList != null && adjustmentList.size() > 0) {
                for (int i = 0; i < adjustmentList.size(); i++) {
                    dataMap = (HashMap) adjustmentList.get(i);
                    rowList = new ArrayList();
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("MEMBER_NAME")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("LIMIT")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("LAST_INT_CALC_DT")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("BALANCE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PRINC_DUE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("INTEREST")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PENAL")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("CHARGE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("TOTAL_DUE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PAYMENT")));
                    tableList.add(rowList);
                }
                setFinalList(null);
                setAdjustmentList(adjustmentList);
                tblLoanAdjustmentTable = new EnhancedTableModel((ArrayList) tableList, authAdjustTableTitle);
            }else{
                ClientUtil.displayAlert("No Record !!! ");
            }
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    public void getEditTableDetails(HashMap whereMap){
        try{
            ArrayList rowList = new ArrayList();
            HashMap dataMap = new HashMap();
            List editList = null;
            System.out.println("#$@$@$#@$@#$@ whereMap : " + whereMap);
            editList = ClientUtil.executeQuery("getDailyLoanTableTransEnquiry", whereMap);
            System.out.println("#$@$@$#@$@#$@ editList : " + editList);
            if (editList != null && editList.size() > 0) {
                for (int i = 0; i < editList.size(); i++) {
                    dataMap = (HashMap) editList.get(i);
                    rowList = new ArrayList();
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("MEMBER_NAME")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("LIMIT")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("LAST_INT_CALC_DT")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("BALANCE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PRINC_DUE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("INTEREST")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PENAL")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("CHARGE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("TOTAL_DUE")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("PAYMENT")));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("COMMISSION")));
                    tableList.add(rowList);
                }
                setAdjustmentList(null);
                setFinalList(editList);
            }else{
                ClientUtil.displayAlert("No Record !!! ");
            }
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
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
    
    public void insertAdjustmentTableData(HashMap whereMap){
        try{
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
             List adjustmentLst=null;
            whereMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
            whereMap.put("ASON_DT",currDt.clone());
             if(CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)){
                 adjustmentLst = ClientUtil.executeQuery("getAdjustmentDailyLoanDetailsimp", whereMap);
             }else{
                 adjustmentLst = ClientUtil.executeQuery("getAdjustmentDailyLoanDetails", whereMap);
             }
            System.out.println("############ adjustmentLst : "+adjustmentLst);
            if(adjustmentLst!=null && adjustmentLst.size()>0){
                for (int i = 0;i<adjustmentLst.size();i++){
                    double princ_Due = 0.0;
                    double int_Due = 0.0;
                    double penal = 0.0;
                    double charge = 0.0;
                    double total_Due = 0.0;
                    dataMap = (HashMap) adjustmentLst.get(i);
                    rowList = new ArrayList();
                    rowList.add(new Boolean(false));
                    rowList.add(dataMap.get("ACT_NUM"));
                    rowList.add(dataMap.get("FNAME"));
                    rowList.add(dataMap.get("LIMIT"));
                    rowList.add(dataMap.get("LAST_INT_CALC_DT"));
                    rowList.add(dataMap.get("BALANCE"));
                    rowList.add(dataMap.get("PRINC_DUE"));
                    if((CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION))){
                        int_Due =CommonUtil.convertObjToDouble(dataMap.get("INT_DUE")).doubleValue();
                        penal =CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
                    }else{
                    HashMap hash = interestCalculationTLAD(dataMap.get("ACT_NUM"), dataMap.get("PROD_ID"));
                    if (hash!=null && hash.size()>0) {
                        int_Due = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                        hash.put("ACT_NUM", dataMap.get("ACT_NUM"));
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
                        hash.put("TO_DATE", currDt);
                        List facilitylst=ClientUtil.executeQuery("getPaidPrinciple",hash);
                        if(facilitylst!=null && facilitylst.size()>0){
                            hash=(HashMap)facilitylst.get(0);
                            int_Due -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                    }  
                    }
                    int_Due = int_Due<=0 ? 0 : int_Due;
                    penal = penal<=0 ? 0 : penal;
                    rowList.add(new Double(int_Due));
                    rowList.add(new Double(penal));
                    dataMap.put("INT_DUE", new Double(int_Due));
                    dataMap.put("PENAL", new Double(penal));
                    rowList.add(dataMap.get("CHARGES"));
                    princ_Due = CommonUtil.convertObjToDouble(dataMap.get("PRINC_DUE")).doubleValue();
                    charge = CommonUtil.convertObjToDouble(dataMap.get("CHARGES")).doubleValue();
                    total_Due = princ_Due + int_Due + penal + charge;
                    dataMap.put("TOTAL_DUE",String.valueOf(total_Due));
                    rowList.add(String.valueOf(total_Due));
                    rowList.add(dataMap.get("PAYMENT"));
                    tableList.add(rowList);
                }
                setAdjustmentList(adjustmentLst);
                tblLoanAdjustmentTable = new EnhancedTableModel((ArrayList) tableList, adjustmentTableTitle);
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void getTableDetails(ArrayList totCustomerList,String custId) throws ParseException{              // NEW ACTION
        try{
            ArrayList rowList = new ArrayList();
            ArrayList glbList= new ArrayList();
            List loanList = null;
            String actNosNotFound = "";
            for(int i=0;i<totCustomerList.size();i++)
            {
               double princ_Due = 0.0;
               double int_Due = 0.0;
               double penal = 0.0;
               double charge = 0.0;
               double total_Due = 0.0;
               ArrayList singleList =(ArrayList)totCustomerList.get(i);
               System.out.println("singleList#PERINGANDUR####"+singleList);
               String ProdType=CommonUtil.convertObjToStr(singleList.get(0));
               String Name=CommonUtil.convertObjToStr(singleList.get(1));
               String AcNo=CommonUtil.convertObjToStr(singleList.get(2));//+"_1"; 
               double Amount=CommonUtil.convertObjToDouble(singleList.get(3));  //rs.getString("AMT");
               double commisionAmount=CommonUtil.convertObjToDouble(singleList.get(4));
               String stringDate=CommonUtil.convertObjToStr(singleList.get(5));
               Date date=DATE_FORMAT.parse(stringDate);
               System.out.println("Name##PERINGANDUR###"+Name+"AcNo   "+AcNo+" Amount"+Amount +"date   "+date);
               Date instDt = new Date(date.getYear(), date.getMonth(), date.getDate());
               System.out.println("instDt###PERINGANDUR##"+instDt);
               rowList = new ArrayList();
               rowList.add(AcNo);
               rowList.add(Name);
               HashMap whereMap = new HashMap();
               whereMap.put("AGENT_ID",custId);
               whereMap.put("BRANCH_CODE",CommonUtil.convertObjToStr(AcNo.substring(0, 4)));
               whereMap.put("ACT_NUM",AcNo);  
                loanList = ClientUtil.executeQuery("getDailyLoanDetails1", whereMap);
               System.out.println("#$@$@$#@$@#$@ List : 666666"+loanList);
               glbList.add(loanList);
               HashMap dataMap= new HashMap();
               for (int j = 0;j<loanList.size();j++){
                     dataMap = (HashMap) loanList.get(j);
                     rowList.add(dataMap.get("LIMIT"));
                     rowList.add(dataMap.get("LAST_INT_CALC_DT"));
                     rowList.add(dataMap.get("BALANCE"));
                     rowList.add(dataMap.get("PRINC_DUE"));
                     HashMap hash = interestCalculationTLAD(dataMap.get("ACT_NUM"), dataMap.get("PROD_ID"));
                    if (hash!=null && hash.size()>0) {
                        int_Due = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                        hash.put("ACT_NUM", dataMap.get("ACT_NUM"));
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
                        hash.put("TO_DATE", currDt);
                        List facilitylst=ClientUtil.executeQuery("getPaidPrinciple",hash);
                        if(facilitylst!=null && facilitylst.size()>0){
                            hash=(HashMap)facilitylst.get(0);
                            int_Due -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                    }//
                    int_Due = int_Due<=0 ? 0 : int_Due;
                    penal = penal<=0 ? 0 : penal;
                    rowList.add(new Double(int_Due));
                    rowList.add(new Double(penal));
                    dataMap.put("INT_DUE", new Double(int_Due));
                    dataMap.put("PENAL", new Double(penal));
                    rowList.add(dataMap.get("CHARGES"));
                    princ_Due = CommonUtil.convertObjToDouble(dataMap.get("PRINC_DUE")).doubleValue();
                    charge = CommonUtil.convertObjToDouble(dataMap.get("CHARGES")).doubleValue();
                    total_Due = princ_Due + int_Due + penal + charge;
                    dataMap.put("TOTAL_DUE",String.valueOf(total_Due));
                    rowList.add(String.valueOf(total_Due));
                   
                     }
                    rowList.add(String.valueOf(Amount));
                    rowList.add(String.valueOf(commisionAmount));
                    tableList.add(rowList);
                 }
            System.out.println("glbList ------------------"+glbList);
             //whereMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
             HashMap whereMap1 = new HashMap();
               whereMap1.put("AGENT_ID",custId);
               //whereMap1.put("BRANCH_CODE",CommonUtil.convertObjToStr(AcNo.substring(0, 4) ));
               whereMap1.put("ASON_DT",currDt.clone());
            List loanList1 = ClientUtil.executeQuery("getDailyLoanDetails", whereMap1);
                 setFinalList(loanList1);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    } 
    public void getTableDetails(HashMap whereMap){              // NEW ACTION
        try{
            ArrayList rowList = new ArrayList();
            HashMap dataMap = new HashMap();
            whereMap.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
            whereMap.put("ASON_DT",currDt.clone());
            List loanList = ClientUtil.executeQuery("getDailyLoanDetails", whereMap);
            System.out.println("#$@$@$#@$@#$@ List : "+loanList);
            if(loanList!=null && loanList.size()>0){
                for (int i = 0;i<loanList.size();i++){
                    double princ_Due = 0.0;
                    double int_Due = 0.0;
                    double penal = 0.0;
                    double charge = 0.0;
                    double total_Due = 0.0;
                    dataMap = (HashMap) loanList.get(i);
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("ACT_NUM"));
                    rowList.add(dataMap.get("FNAME"));
                    rowList.add(dataMap.get("LIMIT"));
                    rowList.add(dataMap.get("LAST_INT_CALC_DT"));
                    rowList.add(dataMap.get("BALANCE"));
                    rowList.add(dataMap.get("PRINC_DUE"));
                    HashMap hash = interestCalculationTLAD(dataMap.get("ACT_NUM"), dataMap.get("PROD_ID"));
                    if (hash!=null && hash.size()>0) {
                        int_Due = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                        hash.put("ACT_NUM", dataMap.get("ACT_NUM"));
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date)hash.get("FROM_DT")),2));
                        hash.put("TO_DATE", currDt);
                        List facilitylst=ClientUtil.executeQuery("getPaidPrinciple",hash);
                        if(facilitylst!=null && facilitylst.size()>0){
                            hash=(HashMap)facilitylst.get(0);
                            int_Due -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                    }
                    if(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equals("AD")){
                        int_Due = CommonUtil.convertObjToDouble(dataMap.get("INT_DUE")).doubleValue();
                        penal = CommonUtil.convertObjToDouble(dataMap.get("PENAL")).doubleValue();
                    }
                    int_Due = int_Due<=0 ? 0 : int_Due;
                    penal = penal<=0 ? 0 : penal;
                    rowList.add(new Double(int_Due));
                    rowList.add(new Double(penal));
                    dataMap.put("INT_DUE", new Double(int_Due));
                    dataMap.put("PENAL", new Double(penal));
                    rowList.add(dataMap.get("CHARGES"));
                    princ_Due = CommonUtil.convertObjToDouble(dataMap.get("PRINC_DUE")).doubleValue();
                    charge = CommonUtil.convertObjToDouble(dataMap.get("CHARGES")).doubleValue();
                    total_Due = princ_Due + int_Due + penal + charge;
                    dataMap.put("TOTAL_DUE",String.valueOf(total_Due));
                    rowList.add(String.valueOf(total_Due));
                    rowList.add(String.valueOf(0));
                    rowList.add(String.valueOf(0));
                    tableList.add(rowList);
                }
                System.out.println("########### DailyLoanList : "+loanList);
                setFinalList(loanList);
            }else{
                ClientUtil.displayAlert("No Record !!! ");
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private HashMap interestCalculationTLAD(Object accountNo, Object prod_id){
        HashMap map=new HashMap();
        HashMap hash=null;
        try{
            map.put("ACT_NUM",accountNo);
            map.put("PROD_ID",prod_id);
            map.put("TRANS_DT", currDt);
            map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            String mapNameForCalcInt = "IntCalculationDetail";
            List lst=ClientUtil.executeQuery(mapNameForCalcInt, map);
            if(lst !=null && lst.size()>0){
                hash=(HashMap)lst.get(0);
                if(hash.get("AS_CUSTOMER_COMES")!=null  && hash.get("AS_CUSTOMER_COMES").equals("N")){
                    hash=new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING","LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", currDt);
//                System.out.println("map before intereest###"+map);
                lst = ClientUtil.executeQuery("",map);
                if (lst!=null && lst.size()>0) {
                    hash=(HashMap)lst.get(0);
                    if(hash==null) {
                        hash=new HashMap();
                    }
//                    System.out.println("hashinterestoutput###"+hash);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return hash;
    }
    
    
    /** To perform the appropriate operation */
    public void doAction(String agentID, String tabName) {
        try {
            doActionPerform(agentID,tabName);
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /**
     * To perform the necessary action
     */
    private void doActionPerform(String agentID, String tabName) throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if(tabName.equals("DAILY_LOAN_COLLECTION")) {
            if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                data.put("LOAN_COLLECTION_NO", getLoanCollectionNo());
            }
            if (getFinalList() != null && getFinalList().size() > 0) {
                data.put("DAILY_LOAN_TABLE_DATA", getFinalList());
                data.put("AGENT_ID", agentID);
            }
            data.put("DAILY_LOAN_COLLECTION","DAILY_LOAN_COLLECTION");
            //data.put("TOTAL_PAYMENT_AMOUNT",getTotalPaymentAmount());
            //data.put("TOTAL_COMMISION_AMOUNT",getCommAmount());
        }else{
            if (getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                data.put("LOAN_ADJUSTMENT_NO", getLoanAdjustmentNo());
            }
            if (getAdjustmentList() != null && getAdjustmentList().size() > 0) {
                data.put("ADJUSTMENT_LOAN_TABLE_DATA", getAdjustmentList());
                data.put("AGENT_ID", agentID);
                data.put("PENAL_WAIVE_OFF",getPenalwaiveoff());
            }
            data.put("LOAN_ADJUSTMENT", "LOAN_ADJUSTMENT");
        }
        data.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
       System.out.println("#$########## data : "+ data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        System.out.println("proxyResultMap###### : " + proxyResultMap);
        if (proxyResultMap.containsKey("LOAN_COLLECTION_NO")) {
//            setLoanCollectionNo(CommonUtil.convertObjToStr(proxyResultMap.get("LOAN_COLLECTION_NO")));
        }
        setResult(actionType);
        _authorizeMap = null;
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
    
}