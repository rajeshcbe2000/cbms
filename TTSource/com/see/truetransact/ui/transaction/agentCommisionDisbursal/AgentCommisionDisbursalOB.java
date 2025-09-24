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
package com.see.truetransact.ui.transaction.agentCommisionDisbursal;

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

import com.see.truetransact.transferobject.agent.AgentCommisonTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.agent.AgentTO;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.uicomponent.CTable;
import java.util.Observable;
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
public class AgentCommisionDisbursalOB extends CObservable {

    private HashMap operationMap;
//    private EnhancedTableModel tblAgentTab;
//    
//    //__ ArrayLists for the Agent Table...
//    ArrayList agentTabTitle = new ArrayList();
//    private ArrayList agentTabRow;
//    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private AgentCommisonTO objAgentCommisionTO;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    private EnhancedTableModel tbmAgentCommission;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(AgentCommisionDisbursalUI.class);
    private ProxyFactory proxy = null;
    private String cboAgentId = "";
    private ComboBoxModel cbmAgentId;
    private ComboBoxModel cbmProdtype;
    private ComboBoxModel cbmProdId;
    private double txtAgentcomm;
    private double txtTdsCommission;
    private String cboProdtype = "";
    private String cboProdId = "";
    private String transType = "";
    private String agentCommisionAcHd = "";
    private HashMap productCommisionMap;
    private CTable _tblData;
    private HashMap dataHash;
    private boolean rdoDefaultPercentage = false;
    private boolean rdoAccountBased = false;
    private boolean rdoAmountBased = false;
    private boolean rdoSlabWise = false;
    private boolean rdoManualPercentage = false;
    private double txtCommisionOpenedAct;
    private String tdtCalcCommUpTo = ""; // Added by nithya on 26-10-2016 for 3177

    public String getTdtCalcCommUpTo() {
        return tdtCalcCommUpTo;
    }

    public void setTdtCalcCommUpTo(String tdtCalcCommUpTo) {
        this.tdtCalcCommUpTo = tdtCalcCommUpTo;
    }
    
            
    public double getTxtCommisionOpenedAct() {
        return txtCommisionOpenedAct;
    }

    public void setTxtCommisionOpenedAct(double txtCommisionOpenedAct) {
        this.txtCommisionOpenedAct = txtCommisionOpenedAct;
    }
    public boolean getRdoAmountBased() {
        return rdoAmountBased;
    }

    public void setRdoAmountBased(boolean rdoAmountBased) {
        this.rdoAmountBased = rdoAmountBased;
    }

    public boolean getRdoManualPercentage() {
        return rdoManualPercentage;
    }

    public void setRdoManualPercentage(boolean rdoManualPercentage) {
        this.rdoManualPercentage = rdoManualPercentage;
    }

    public boolean getRdoSlabWise() {
        return rdoSlabWise;
    }

    public void setRdoSlabWise(boolean rdoSlabWise) {
        this.rdoSlabWise = rdoSlabWise;
    }
    public boolean getRdoAccountBased() {
        return rdoAccountBased;
    }

    public void setRdoAccountBased(boolean rdoAccountBased) {
        this.rdoAccountBased = rdoAccountBased;
    }

    public boolean getRdoDefaultPercentage() {
        return rdoDefaultPercentage;
    }

    public void setRdoDefaultPercentage(boolean rdoDefaultPercentage) {
        this.rdoDefaultPercentage = rdoDefaultPercentage;
    }
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
    
    public String getAgentCommisionAcHd() {
        return agentCommisionAcHd;
    }

    public void setAgentCommisionAcHd(String agentCommisionAcHd) {
        this.agentCommisionAcHd = agentCommisionAcHd;
    }
    
    public String getCboProdId() {
        return cboProdId;
    }

    public void setCboProdId(String cboProdId) {
        this.cboProdId = cboProdId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getCboProdtype() {
        return cboProdtype;
    }

    public void setCboProdtype(String cboProdtype) {
        this.cboProdtype = cboProdtype;
    }

    public double getTxtTdsCommission() {
        return txtTdsCommission;
    }

    public void setTxtTdsCommission(double txtTdsCommission) {
        this.txtTdsCommission = txtTdsCommission;
    }

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
    private String txtCommision = "";
    private String txtCommisionDuringThePeriod = "";
    private String txtCommisionForThePeriod = "";
    private String tdtFromDate = "";
    private String tdtToDate = "";
    private String lblNameForAgent = "";
    private String AgentId = "";
    private double totComm = 0.0;
    private double tptCollAmt = 0.0;
    private double tdsAmt = 0.0;
    private double totCommToOA = 0.0;
    private double totCommToTD = 0.0;
    private HashMap agentCommisionMap;
    Date curDate = null;
    private String deposit_closing = "";
    private String deposit_No = "";
    private Double prematurePeriod = null;
    private String depositDate = "";
//    private  monthStartDate ="";
//    private final String BRANCH = TrueTransactMain.BRANCH_ID;
    // To get the Value of Column Title and Dialogue Box...
//    final AgentRB objAgentRB = new AgentRB();
//    java.util.ResourceBundle objAgentRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.AgentCommisionDisbursalRB", ProxyParameters.LANGUAGE);
    private static AgentCommisionDisbursalOB agentCommisionDisbursalOB;

    static {
        try {
            log.info("In AgentOB Declaration");
            agentCommisionDisbursalOB = new AgentCommisionDisbursalOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public static AgentCommisionDisbursalOB getInstance() {
        return agentCommisionDisbursalOB;
    }

    /**
     * Creates a new instance of InwardClearingOB
     */
    public AgentCommisionDisbursalOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
        setTableTile();
        //tbmAgentCommission = new EnhancedTableModel(null, tableTitle);
    }

     private void setTableTile() throws Exception{        
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
        } catch (Exception e) {
            System.out.println("Error In populateData()");
            parseException.logException(e, true);
        }
    }

    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
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
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        this.cbmProdtype= new ComboBoxModel(key  ,value);
    }

    public void cboCreditProductId() {
        if (CommonUtil.convertObjToStr(getCboProdtype()).length() > 0 && !CommonUtil.convertObjToStr(getCboProdtype()).equals("GL")) {
            System.out.println("prodType : setCbmProdIdPISD " + getCboProdtype());
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + getCboProdtype());
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
                cbmProdId= new ComboBoxModel(key,value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            cbmProdId = new ComboBoxModel();
        }
        setChanged();
   }
        
    private void populateOB(HashMap mapData) throws Exception {
        AgentCommisonTO objAgentCommisionTO = null;
        //Taking the Value of Prod_Id from each Table...
        objAgentCommisionTO = new AgentCommisonTO();
        objAgentCommisionTO = (AgentCommisonTO) ((List) mapData.get("AgentCommisonTO")).get(0);
        System.out.println("objAgentCommisionTO :: " + objAgentCommisionTO);
        setAgentCommisonTO(objAgentCommisionTO);

        ttNotifyObservers();
    }

    // To Enter the values in the UI fields, from the database...
    private void setAgentCommisonTO(AgentCommisonTO objAgentCommisionTO) throws Exception {
        log.info("In setAgentCommisionTO()");
        setCboAgentId(objAgentCommisionTO.getAgentId());
        setTxtCommision(objAgentCommisionTO.getCommision());
        setTxtCommisionDuringThePeriod(objAgentCommisionTO.getCommisionDuringThePeriod());
        setTxtCommisionForThePeriod(objAgentCommisionTO.getCommisionForThePeriod());
        setTdtFromDate(DateUtil.getStringDate(objAgentCommisionTO.getFromDate()));
        setTdtToDate(DateUtil.getStringDate(objAgentCommisionTO.getToDate()));


    }

    // set for a transferobject from the observable....
    private AgentCommisonTO setAgentCommision() {
        log.info("In setAgentCommision()");
        objAgentCommisionTO = new AgentCommisonTO();
        try {
            objAgentCommisionTO.setAgentId(getCboAgentId());
            objAgentCommisionTO.setCommision(getTxtCommision());
            objAgentCommisionTO.setCommisionDuringThePeriod(getTxtCommisionDuringThePeriod());
            objAgentCommisionTO.setCommisionForThePeriod(getTxtCommisionForThePeriod());

            Date TdFrDt = DateUtil.getDateMMDDYYYY(getTdtFromDate());
            if (TdFrDt != null) {
                Date tdfrDate = (Date) curDate.clone();
                tdfrDate.setDate(TdFrDt.getDate());
                tdfrDate.setMonth(TdFrDt.getMonth());
                tdfrDate.setYear(TdFrDt.getYear());
//            objAgentCommisionTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDate()));
                objAgentCommisionTO.setFromDate(tdfrDate);
            } else {
                objAgentCommisionTO.setFromDate(DateUtil.getDateMMDDYYYY(getTdtFromDate()));
            }

            Date TdToDt = DateUtil.getDateMMDDYYYY(getTdtToDate());
            if (TdToDt != null) {
                Date tdtoDate = (Date) curDate.clone();
                tdtoDate.setDate(TdToDt.getDate());
                tdtoDate.setMonth(TdToDt.getMonth());
                tdtoDate.setYear(TdToDt.getYear());
//            objAgentCommisionTO.setToDate(DateUtil.getDateMMDDYYYY(getTdtToDate()));
                objAgentCommisionTO.setToDate(tdtoDate);
            } else {
                objAgentCommisionTO.setToDate(DateUtil.getDateMMDDYYYY(getTdtToDate()));
            }

            objAgentCommisionTO.setComToOAacc(new Double(getTotCommToOA()));
            objAgentCommisionTO.setComToTDacc(new Double(getTotCommToTD()));
            objAgentCommisionTO.setTdsAmt(new Double(getTdsAmt()));
            objAgentCommisionTO.setTransType(getTransType());
            System.out.println("AgentCommisionTO : " + objAgentCommisionTO);
        } catch (Exception e) {
            parseException.logException(e, true);
            //e.printStackTrace();
        }
        return objAgentCommisionTO;
    }
    
    private AgentCommisonTO setAllAgentCommision(HashMap map) {
        log.info("In setAgentCommision()");
        objAgentCommisionTO = new AgentCommisonTO();
        try {
            objAgentCommisionTO.setAgentId(getCboAgentId());
            objAgentCommisionTO.setCommision(CommonUtil.convertObjToStr(map.get("COLLECTION_AMT")));
            objAgentCommisionTO.setCommisionForThePeriod(CommonUtil.convertObjToStr(map.get("COMMISION_AMT")));
            objAgentCommisionTO.setCommisionDuringThePeriod(getTxtCommisionDuringThePeriod());
            objAgentCommisionTO.setFromDate(getProperDateFormat(map.get("FROM_DT")));
            //objAgentCommisionTO.setToDate((Date)curDate.clone()); // commented and added below line by nithya on 26-10-2016 for 3177
            objAgentCommisionTO.setToDate(getProperDateFormat(DateUtil.getDateMMDDYYYY(getTdtCalcCommUpTo()))); // Added by nithya on 26-10-2016 for 3177
            objAgentCommisionTO.setTdsAmt(CommonUtil.convertObjToDouble(map.get("VAT_AMT")));
            objAgentCommisionTO.setComToOAacc(CommonUtil.convertObjToDouble(objAgentCommisionTO.getCommisionForThePeriod()));
            objAgentCommisionTO.setComToTDacc(new Double(getTotCommToTD()));            
            objAgentCommisionTO.setTransType(getTransType());
             System.out.println("AgentCommisionTO : getTransType" + objAgentCommisionTO.getTransType());
            System.out.println("AgentCommisionTO : " + objAgentCommisionTO);
        } catch (Exception e) {
            parseException.logException(e, true);
            //e.printStackTrace();
        }
        return objAgentCommisionTO;
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

// This method get for his agents total amount for certain period.....    
    public List agentsAmount(String agent) {
        HashMap amtDetails = new HashMap();
        amtDetails.put("AGENT_ID", agent);
//       if(!getDeposit_closing().equals("") && getDeposit_closing().equals("CLOSING_SCREEN")){
//           HashMap closingMap = new HashMap();
//           closingMap.put("DEPOSIT_NO",getDeposit_No());
//           List lstClosing = ClientUtil.executeQuery("getLastCommisionPaidDt" , closingMap);
//           if(lstClosing!=null && lstClosing.size()>0){
//               closingMap = (HashMap)lstClosing.get(0);
//               setTdtFromDate(DateUtil.getStringDate((Date)closingMap.get("DEPOSIT_DT")));
//               Date paidDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(closingMap.get("LAST_COM_PAID_DT")));
//               if(paidDt == null && paidDt.getDate() == 0)
//                   setTdtToDate(DateUtil.getStringDate(curDate));
//               else
//                   setTdtToDate(DateUtil.getStringDate((Date)closingMap.get("LAST_COM_PAID_DT")));
//           }
//           amtDetails.put("DEPOSIT_NO",getDeposit_No());
//       }
        Date TdFromDt = DateUtil.getDateMMDDYYYY(tdtFromDate);
        if (TdFromDt != null) {
            Date tdFromDate = (Date) curDate.clone();
            tdFromDate.setDate(TdFromDt.getDate());
            tdFromDate.setMonth(TdFromDt.getMonth());
            tdFromDate.setYear(TdFromDt.getYear());
//       amtDetails.put("MONTH_START_DATE", DateUtil.getDateMMDDYYYY(tdtFromDate));
            amtDetails.put("MONTH_START_DATE", tdFromDate);
        } else {
            amtDetails.put("MONTH_START_DATE", DateUtil.getDateMMDDYYYY(tdtFromDate));
        }

        Date TdtToDt = DateUtil.getDateMMDDYYYY(tdtToDate);
        if (TdtToDt != null) {
            Date tdtToDate = (Date) curDate.clone();
            tdtToDate.setDate(TdtToDt.getDate());
            tdtToDate.setMonth(TdtToDt.getMonth());
            tdtToDate.setYear(TdtToDt.getYear());
//       amtDetails.put("MONTH_END_DATE", DateUtil.getDateMMDDYYYY(tdtToDate));
            amtDetails.put("MONTH_END_DATE", tdtToDate);
        } else {
            amtDetails.put("MONTH_END_DATE", DateUtil.getDateMMDDYYYY(tdtToDate));
        }
        amtDetails.put("PROD_ID", CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
        System.out.println("AgentAmount : " + amtDetails);
        // List lst = ClientUtil.executeQuery("getAgentCommisionDetails", amtDetails);
        List lst = ClientUtil.executeQuery("getAgentCollectionAmt", amtDetails);
        System.out.println("AgentNameAmount : " + amtDetails);
//       String amount = (String) CommonUtil.convertObjToStr(amtDetails.get("AMOUNT"));
//       double txtamount = new Double (amount.length() > 1 ? amount : "0").doubleValue();
        return lst;
    }

//calculating for every months last date.....
    public Date calcuateLastDateOfMonth(Date currDate) {
        //Date currDate = calcuateFirstDateOfMonth(getCboProdId(),getCboAgentId());
        Date dayBeginDt = (Date) curDate.clone();

        currDate.setMonth(currDate.getMonth());
        System.out.println("calcuateLastDateOfMonth : " + currDate);
        Calendar calendar = new GregorianCalendar(currDate.getYear(), currDate.getMonth(), currDate.getDate());
        int lastDate = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
        System.out.print("Calendar lastddate######" + lastDate);
        currDate.setDate(lastDate);
        if (DateUtil.dateDiff(dayBeginDt, currDate) >= 0) {
            currDate = DateUtil.addDays(dayBeginDt, -1);
        }
        return currDate;
    }

    public Date calcuateFirstDateOfMonth(String prodId,String agentId) {
        System.out.println("a^$^$^$^^^$^"+agentId);
        Date currDate = (Date) curDate.clone();
        currDate.setMonth(currDate.getMonth() - 1);
        currDate.setDate(1);
        HashMap aGmap = new HashMap();
        aGmap.put("AGENT_ID", agentId);
        aGmap.put("PROD_ID", prodId);
        List aGlst = ClientUtil.executeQuery("getLastCommisionPaidDt", aGmap);
        //AgentTO agentTo = new AgentTO();
        if (aGlst != null && aGlst.size() > 0) {
            aGmap = (HashMap) aGlst.get(0);            
            Date appDt = getProperDateFormat(aGmap.get("APPOINTED_DT"));
            if(rdoAmountBased){
                currDate = getProperDateFormat(aGmap.get("LAST_COL_DT"));
            }else if(rdoAccountBased){
                currDate = getProperDateFormat(aGmap.get("LAST_INTRO_PAID_DT"));
            }
            setCboAgentId(CommonUtil.convertObjToStr(aGmap.get("AGENT_ID")));
            setAgentId(CommonUtil.convertObjToStr(aGmap.get("AGENT_ID")));
            setAgentCommisionAcHd(CommonUtil.convertObjToStr(aGmap.get("COMM_COL_AC_HD_ID")));
            if (currDate != null && appDt != null) {
                if (DateUtil.dateDiff(appDt, currDate) > 0) {
                    currDate=DateUtil.addDays(currDate,1);
                    return currDate;
                } else {
                    return currDate;
                }
            } else {
                ClientUtil.displayAlert("Last Commission Paid Date is not Set");
            }
        } else {
                ClientUtil.displayAlert("Last Commission Paid Date is not Set");
        }
        return null;
    }
        
//calculating for every months first date....
    public HashMap standingProcessedDetails(String prodId,String agentId) {
        System.out.println("a^$^$^$^^^$^"+agentId);
        HashMap aGmap = new HashMap();
        aGmap.put("AGENT_ID", agentId);
        aGmap.put("PROD_ID", prodId);
        List aGlst = ClientUtil.executeQuery("getStandingProcessedDetails", aGmap);
        if (aGlst != null && aGlst.size() > 0) {            
            aGmap = (HashMap) aGlst.get(0);
            if(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(aGmap.get("LAST_DT")))!=null 
                    && !DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(aGmap.get("LAST_DT"))).equals("")){
                return aGmap;
            }
        } 
        return null;
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
    
    public double getCommissionSlabAmount() {
        HashMap agentList = new HashMap();
        double commission = 0.0;
        agentList.put("PROD_ID", CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
        agentList.put("FROM_DT", DateUtil.getDateMMDDYYYY(tdtFromDate));
        agentList.put("TO_DT", DateUtil.getDateMMDDYYYY(tdtToDate));
        agentList.put("COLLECTION_AMOUNT", tptCollAmt);
        List list = ClientUtil.executeQuery("getSlabWiseCommissionAmount", agentList);
        System.out.println("ob###### agentList: " + agentList);
        if (list.size() > 0 && list!=null) {
            agentList = (HashMap) list.get(0);
            commission = CommonUtil.convertObjToDouble(agentList.get("COMMISSION_AMOUNT"));
        }else{
            ClientUtil.displayAlert("Selected Product Id haven't any commission slab settings!!");
        }     
        System.out.println("###### commission: " + commission);
        return commission;
    }
    
//this method is used for calculating agents Commisions.....
    public double SlabCommisionCalculation() {
        HashMap behavesMap = new HashMap();
        String Agent = getAgentId();
        double coMm = 0.0,amt= 0.0;
        ArrayList agAmtLst = (ArrayList) agentsAmount(Agent);        
        if (agAmtLst != null && agAmtLst.size() > 0) {
            HashMap agMap = new HashMap();
            agMap = (HashMap) agAmtLst.get(0);
            amt = CommonUtil.convertObjToDouble(agMap.get("AMOUNT"));
            tptCollAmt = amt;
        }else{
            ClientUtil.displayAlert("Selected Product Id haven't any daily transaction!!");
        }
        HashMap slabAmount = new HashMap();        
        tdsAmt = 0.0;
        totComm = getCommissionSlabAmount();
        tdsAmt = totComm * txtTdsCommission / 100;
        setTotComm(Math.round(totComm));
        setTdsAmt(tdsAmt);
        setTptCollAmt(amt);
        setTotCommToOA(Math.round(totComm));
        setTotCommToTD(0);
        setTxtCommision(CommonUtil.convertObjToStr(new Double(tptCollAmt)));
        setTxtCommisionForThePeriod(CommonUtil.convertObjToStr(totComm));
        ttNotifyObservers();
        tptCollAmt = 0.0;
        totComm = 0.0;        
        return totComm;
    }
          
//this method is used for calculating agents Commisions.....
    public double commisionCalculation() {
        HashMap behavesMap = new HashMap();
        String Agent = getAgentId();
        double coMm = 0.0;
        ArrayList agAmtLst = (ArrayList) agentsAmount(Agent);
        //System.out.println("");

//        if(agAmtLst!=null && agAmtLst.size()>0){
//            for(int i=0;i<agAmtLst.size();i++){
//                double Amt=0.0;
//                double comRate=0.0;
//                HashMap agMap=new HashMap();
//                agMap=(HashMap)agAmtLst.get(i);
//                System.out.println("aagmap is....."+agMap);
//                Amt=calucateCollAmtInd(agMap);
//                System.out.println("amount is.........."+Amt);
//                if(Amt>0){
//                    
//                  int period=CommonUtil.convertObjToInt(agMap.get("PERIOD"));
//                      //double period=CommonUtil.convertObjToDouble(agMap.get("PERIOD"));
//                    // int period=CommonUtil.convertObjToInt(agMap.get("PERIOD"));
//                  period=period*30;
//                  // behavesMap.put("PERIOD", new java.math.BigDecimal(period));
//                   // System.out.println("period.............is"+period);
//                      //behavesMap.put("PERIOD",CommonUtil.convertObjToDouble(period));
//                  //
////     int  period=30;
//                    behavesMap.put("PERIOD",period);
//                    //                behavesMap.put("PERIOD", agMap.get("PERIOD"));
//                    behavesMap.put("AMOUNT",CommonUtil.convertObjToDouble(Amt));
//                    behavesMap.put("DAILY","DAILY");
//                    System.out.println("map is............"+behavesMap);
//                    List list = ClientUtil.executeQuery("dailyBehavesLike", behavesMap);
//                    behavesMap=null;
//                    if(list!=null && list.size()>0){
//                        behavesMap=(HashMap)list.get(0);
//                        //                    Amt=calucateCollAmtInd(agMap);                        
//                        //                    Amt=CommonUtil.convertObjToDouble(agMap.get("AMOUNT")).doubleValue();
//                        comRate=CommonUtil.convertObjToDouble(behavesMap.get("ROI")).doubleValue();
//                        coMm=(Amt*comRate)/100;
//                        totComm+=coMm;
//                        tptCollAmt+=Amt;
//                        if(!getDeposit_closing().equals("") && getDeposit_closing().equals("CLOSING_SCREEN")){
//                            System.out.println("dpamt######$$$");
//                        }else{
//                            ArrayList tblLst=new ArrayList()  ;
//                            tblLst.add(agMap.get("PERIOD"));
//                            tblLst.add(new Double(comRate));
//                            tblLst.add(new Double(Amt));
//                            tblLst.add(new Double(coMm));
//                            ArrayList data;
//                            ArrayList columnHeader;
//                            columnHeader = new ArrayList();
//                            columnHeader.add("Period");
//                            columnHeader.add("Comm. Rate");
//                            columnHeader.add("Comm. Amt");
//                            columnHeader.add("Commission");
//                            if (tbmAgentCommission==null) {
//                                data = new ArrayList();
//                            } else {
//                                data = tbmAgentCommission.getDataArrayList();
//                            }
//                            tbmAgentCommission = new TableModel(data,columnHeader);
//                            tbmAgentCommission.insertRow(tbmAgentCommission.getRowCount(), tblLst);
//                            tbmAgentCommission.fireTableDataChanged();
//                        }
//                    }
//                }
//            }
//        }
        double amt = 0;
        if (agAmtLst != null && agAmtLst.size() > 0) {
            HashMap agMap = new HashMap();
            agMap = (HashMap) agAmtLst.get(0);
            amt = CommonUtil.convertObjToDouble(agMap.get("AMOUNT"));
        }else{
            ClientUtil.displayAlert("Selected Product Id haven't any daily transaction!!");
        }
        tdsAmt = 0.0;
//        totCommToOA=(totComm*90)/100;
//        totCommToTD=(totComm*10)/100;
//        totCommToOA=getNearest(totCommToOA*100,100)/100;
//        totCommToTD=getNearest(totCommToTD*100,100)/100;
//        totComm=totCommToOA+totCommToTD+tdsAmt;
//        setTdsAmt(tdsAmt);
        totComm = amt * txtAgentcomm / 100;
        tdsAmt = totComm * txtTdsCommission / 100;
        setTotComm(Math.round(totComm));
        setTdsAmt(tdsAmt);
        // setTptCollAmt(tptCollAmt);
        setTptCollAmt(amt);
        setTotCommToOA(Math.round(totComm));
        //setTotCommToOA(totCommToOA);
        setTotCommToTD(0);
        // setTotCommToTD(totCommToTD);
        //  setTxtCommision(CommonUtil.convertObjToStr(new Double(tptCollAmt)));
        setTxtCommision(CommonUtil.convertObjToStr(new Double(tptCollAmt)));
        //  setTxtCommisionForThePeriod(CommonUtil.convertObjToStr(new Double(totComm)));
        setTxtCommisionForThePeriod(CommonUtil.convertObjToStr(totComm));
        ttNotifyObservers();
        tptCollAmt = 0.0;
        totComm = 0.0;
        //        if(list.size() >0) {
        //            behavesMap = (HashMap) list.get(0);
        //            String commision = (String)CommonUtil.convertObjToStr(behavesMap.get("ROI"));
        //            double commisionROI = Double.parseDouble(commision);
        //            String amountTxt = (String) CommonUtil.convertObjToStr(getTxtCommision());
        //            double amount = Double.parseDouble(amountTxt);
        //            String fromAmt = (String) CommonUtil.convertObjToStr(behavesMap.get("FROM_AMOUNT"));
        //            double fromMaxAmt = Double.parseDouble(fromAmt);
        //            String toAmt =(String)CommonUtil.convertObjToStr(behavesMap.get("TO_AMOUNT"));
        //            double toMaxAmt = Double.parseDouble(toAmt);
        //            double agentCommisionAmt = 0.0;
        //            if(toMaxAmt >= amount) {
        //                agentCommisionAmt = (amount * commisionROI /100);
        //                System.out.println("agentCommisionAmt :"+agentCommisionAmt);
        //            } else
        //                agentCommisionAmt = (amount * commisionROI /100);
        //        return agentCommisionAmt;
        //        }
        //        return  0;
        return totComm;
        // return coMm;
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
          System.out.println("Datas :"+data.size());
        ArrayList tblDatanew = new ArrayList();
        ArrayList tblFinalData = new ArrayList();
        tableTitle = new ArrayList();
        tableTitle.add("SELECT");
        if(_heading!=null && _heading.size()>0){
            for (int j = 0; j <=_heading.size()-1; j++) {
                  tableTitle.add(_heading.get(j));
                  flag = true;
            }
        }
        if(data!=null && data.size()>0){
            for (int i = 0; i <=data.size()-1; i++) {            
                List tmpList = (List) data.get(i);
                ArrayList newList = new ArrayList();
                newList.add(new Boolean(false));
                for (int j = 0; j <=_heading.size()-1; j++) { 
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

    private double calucateCollAmtInd(HashMap agMap) {
        double amt = 0.0;
        Date tdToDate;
        Date tdFromDate;
        Date TdFromDt = DateUtil.getDateMMDDYYYY(getTdtFromDate());
        if (TdFromDt != null) {
            tdFromDate = (Date) curDate.clone();
            tdFromDate.setDate(TdFromDt.getDate());
            tdFromDate.setMonth(TdFromDt.getMonth());
            tdFromDate.setYear(TdFromDt.getYear());
            //    Date fromDt=DateUtil.getDateMMDDYYYY(getTdtFromDate());
        } else {
            tdFromDate = DateUtil.getDateMMDDYYYY(getTdtFromDate());
        }
        Date TdToDt = DateUtil.getDateMMDDYYYY(getTdtToDate());
        if (TdToDt != null) {
            tdToDate = (Date) curDate.clone();
            tdToDate.setDate(TdToDt.getDate());
            tdToDate.setMonth(TdToDt.getMonth());
            tdToDate.setYear(TdToDt.getYear());
            //    Date toDt=DateUtil.getDateMMDDYYYY(getTdtToDate());
        } else {
            tdToDate = DateUtil.getDateMMDDYYYY(getTdtToDate());
        }
        Date todayDt = null;
        if (!getDeposit_closing().equals("") && getDeposit_closing().equals("CLOSING_SCREEN")) {
            Date depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
            Date DepositDt = null;
            if (depDate != null) {
                DepositDt = (Date) curDate.clone();
                DepositDt.setDate(depDate.getDate());
                DepositDt.setMonth(depDate.getMonth());
                DepositDt.setYear(depDate.getYear());
                tdFromDate = DepositDt;
            }
//            tdToDate = DateUtil.getDateMMDDYYYY(getTdtToDate());
//            tdToDate = DateUtil.addDays(tdToDate, -1);
//            if(tdToDate!=null){
            todayDt = (Date) curDate.clone();
//                todayDt.setDate(tdToDate.getDate());
//                todayDt.setMonth(tdToDate.getMonth());
//                todayDt.setYear(tdToDate.getYear());
//            }
        }
        long noofDays = 0;
        HashMap sumMap = new HashMap();
        sumMap.put("PERIOD", agMap.get("PERIOD"));
        sumMap.put("MONTH_START_DATE", tdFromDate);
        sumMap.put("AGENT_ID", getAgentId());
        if (!getDeposit_closing().equals("") && getDeposit_closing().equals("CLOSING_SCREEN")) {
            noofDays = DateUtil.dateDiff(tdFromDate, todayDt);
            sumMap.put("MONTH_END_DATE", todayDt);
        } else {
            noofDays = DateUtil.dateDiff(tdFromDate, tdToDate);
            sumMap.put("MONTH_END_DATE", tdToDate);
        }
        //    Date toDt=DateUtil.getDateMMDDYYYY(getTdtToDate());        
        if (!getDeposit_closing().equals("") && getDeposit_closing().equals("CLOSING_SCREEN")) {
            sumMap.put("DEPOSIT_NO", getDeposit_No());
            int prematurePeriod = CommonUtil.convertObjToInt(getPrematurePeriod());
            for (int i = 0; i < prematurePeriod; i++) {
                List accSumlst = ClientUtil.executeQuery("calcualtingthroughDepositClosing", sumMap);
                if (accSumlst != null && accSumlst.size() > 0) {
                    HashMap accMap = new HashMap();
                    accMap = (HashMap) accSumlst.get(0);
                    String depNo = CommonUtil.convertObjToStr(accMap.get("ACC_NUM"));
                    String[] ACCNUM;
                    ACCNUM = CommonUtil.convertObjToStr(accMap.get("ACC_NUM")).split("_");
                    accMap.put("DEPOSIT NO", ACCNUM[0]);
                    accMap.put("DEPOSIT_SUB_NO", ACCNUM[1]);
                    List acctLst = ClientUtil.executeQuery("getSelectDepSubNoAccInfoTO", accMap);
                    if (acctLst != null && acctLst.size() > 0) {
                        HashMap dpMap = new HashMap();
                        dpMap = (HashMap) acctLst.get(0);
                        double collAmt = CommonUtil.convertObjToDouble(accMap.get("AMOUNT")).doubleValue();
                        double dpamt = CommonUtil.convertObjToDouble(dpMap.get("DEPOSIT_AMT")).doubleValue();
                        if (dpamt <= 0) {
                            dpamt = collAmt;
                        }
                        if (noofDays <= 0) {
                            noofDays = 1;
                        }
                        dpamt = dpamt * noofDays;
                        System.out.println("dpamt######$$$" + dpamt + "collAmt######$$$" + collAmt);
                        if (dpamt < collAmt) {
                            amt = amt + dpamt;
                        } else {
                            amt = amt + collAmt;
                        }
                        sumMap.put("MONTH_START_DATE", tdToDate);
                        GregorianCalendar firstdaymonth = new GregorianCalendar(1, tdToDate.getMonth(), tdToDate.getYear() + 1900);
                        int noOfDay = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                        GregorianCalendar lastdaymonth = new GregorianCalendar(tdToDate.getYear() + 1900, tdToDate.getMonth(), noOfDay);
                        sumMap.put("MONTH_END_DATE", lastdaymonth.getTime());
                        tdToDate = DateUtil.addDays(lastdaymonth.getTime(), 1);
                    }
                }
            }
        } else {
            List accSumlst = ClientUtil.executeQuery("selectIndivalAccSudoublemforagCom", sumMap);
            if (accSumlst != null && accSumlst.size() > 0) {
                for (int i = 0; i < accSumlst.size(); i++) {
                    HashMap accMap = new HashMap();
                    accMap = (HashMap) accSumlst.get(i);
                    double collectedAmt = CommonUtil.convertObjToDouble(accMap.get("AMOUNT")).doubleValue();
                    double depositAmt = CommonUtil.convertObjToDouble(accMap.get("DEPOSIT_AMT")).doubleValue();
                    if (depositAmt <= 0) {
                        depositAmt = collectedAmt;
                    }
                    if (noofDays <= 0) {
                        noofDays = 1;
                    }
                    depositAmt = depositAmt * (noofDays + 1);
                    if (accMap.containsKey("DEPOSIT_STATUS") && accMap.get("DEPOSIT_STATUS") != null && accMap.get("DEPOSIT_STATUS").equals("CLOSED")) {
                        Date depositDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(accMap.get("DEPOSIT_DT")));
                        Date closedDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(accMap.get("CLOSE_DT")));
                        long minAgentPeriod = CommonUtil.convertObjToLong(accMap.get("AGENT_COMMISION_PERIOD"));
                        if (DateUtil.dateDiff(depositDt, closedDt) >= minAgentPeriod) {
                            System.out.println("if more than 90 days...");
                            if (depositAmt < collectedAmt) {
                                amt = amt + depositAmt;
                                System.out.println("if more than 90 days collectedAmt :" + collectedAmt + "Tot depositAmt :" + depositAmt + "Excess : " + (depositAmt - collectedAmt) + "totalColelctedAmt :" + amt + "Account No" + accMap.get("ACC_NUM"));
                            } else {
                                amt = amt + collectedAmt;
                                System.out.println("else more than 90 days collectedAmt :" + collectedAmt + "Tot depositAmt :" + depositAmt + "Excess : " + (depositAmt - collectedAmt) + "totalColelctedAmt :" + amt + "Account No" + accMap.get("ACC_NUM"));
                            }
                        } else {
                            System.out.println("less than 90 days : " + accMap.get("ACC_NUM") + collectedAmt + "Tot depositAmt :" + depositAmt + "totalColelctedAmt :" + amt + "Account No" + accMap.get("ACC_NUM"));
                        }
                    } else {
                        System.out.println("depositAmt :" + depositAmt + "collectedAmt :" + collectedAmt + "totalColelctedAmt :" + amt + "Account No" + accMap.get("ACC_NUM"));
                        if (depositAmt < collectedAmt) {
                            amt = amt + depositAmt;
                            System.out.println("forloop If Condition collectedAmt :" + collectedAmt + "Tot depositAmt :" + depositAmt + "Excess : " + (depositAmt - collectedAmt) + "totalColelctedAmt :" + amt + "Account No" + accMap.get("ACC_NUM"));
                        } else {
                            amt = amt + collectedAmt;
                            System.out.println("forloop Else Condition collectedAmt :" + collectedAmt + "Tot depositAmt :" + depositAmt + "Excess : " + (depositAmt - collectedAmt) + "totalColelctedAmt :" + amt + "Account No" + accMap.get("ACC_NUM"));
                        }
                    }
                }
                accSumlst = ClientUtil.executeQuery("selectCurrentMonthOpenedAccts", sumMap);
                if (accSumlst != null && accSumlst.size() > 0) {
                    for (int i = 0; i < accSumlst.size(); i++) {
                        HashMap accMap = new HashMap();
                        accMap = (HashMap) accSumlst.get(i);
                        double collectedAmt = CommonUtil.convertObjToDouble(accMap.get("AMOUNT")).doubleValue();
                        double depositAmt = CommonUtil.convertObjToDouble(accMap.get("DEPOSIT_AMT")).doubleValue();
                        Date depositDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(accMap.get("DEPOSIT_DT")));
                        int originalDay = depositDt.getDate();
                        noofDays = DateUtil.dateDiff(depositDt, tdToDate);
//                        noofDays = noofDays - originalDay;
                        if (depositAmt <= 0) {
                            depositAmt = collectedAmt;
                        }
                        if (noofDays <= 0) {
                            noofDays = 1;
                        }
                        depositAmt = depositAmt * (noofDays + 1);
                        System.out.println("depositAmt :" + depositAmt + "collectedAmt :" + collectedAmt);
                        if (depositAmt < collectedAmt) {
                            amt = amt + depositAmt;
                            System.out.println("currentMonth opened A/c If Part collectedAmt :" + collectedAmt + "Tot depositAmt :" + depositAmt + "Excess : " + (depositAmt - collectedAmt) + "totalColelctedAmt :" + amt + "Account No" + accMap.get("ACC_NUM"));
                        } else {
                            amt = amt + collectedAmt;
                            System.out.println("currentMonth opened A/c else Part collectedAmt :" + collectedAmt + "Tot depositAmt :" + depositAmt + "Excess : " + (depositAmt - collectedAmt) + "totalColelctedAmt :" + amt + "Account No" + accMap.get("ACC_NUM"));
                        }
                    }
                }
            }
        }
        System.out.println("amt======" + amt);
        return amt;
    }

    public AgentTO agentAccountDetails() {
        //    double sbInt=0;
        //    double depInt=0;
        //    System.out.println("commisionAmounts :" +commisionAmt);
        ////    if(commisionAmt > 0) {
        ////        sbInt = (commisionAmt *90 /100);
        ////        depInt = (commisionAmt * 10 /100);
        ////        System.out.println("SbAccount interest : " +sbInt);
        ////        System.out.println("depAccount interest : " +depInt);
        ////    }
        //    agentCommisionMap=new HashMap();
        //    HashMap agentMap = new HashMap();
        //    agentMap.put("AGENT_ID",getAgentId());
        //    System.out.println("#####agent_id :" +agentMap);
        //    agentMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        //    List lst = ClientUtil.executeQuery("getOperativeAccNum", agentMap);
        //    if(lst.size()>0) {
        //        HashMap operativeMap = (HashMap)lst.get(0);
        //        agentCommisionMap.put("OA_ACT_NUM", operativeMap.get("ACT_NUM"));
        //        agentCommisionMap.put("OP_AMT", new Double (commisionAmt));
        ////        agentCommisionMap.put("OP_AMT", new Double (sbInt));
        //        agentCommisionMap.put("PROD_ID", operativeMap.get("PROD_ID"));
        //        agentCommisionMap.put("LINK_BATCH_COMMISION_ID",operativeMap.get("ACT_NUM"));
        ////        agentCommisionMap.put("DEPOSIT_AMT", new Double(depInt));
        //        
        //    }
        HashMap agMap = new HashMap();
        agMap.put("AGENTID", getCboAgentId());
        agMap.put("TYPE","A");// Added by nithya on 15-11-2017 for solving Agent commission transfer issue
        System.out.println("getCbmAgentId()&&&&&" + getCboAgentId());
        List lst = ClientUtil.executeQuery("getSelectAgentTO", agMap);
        AgentTO agTo = new AgentTO();
        if (lst != null && lst.size() > 0) {
            agTo = new AgentTO();
            agTo = (AgentTO) lst.get(0);
            agMap = new HashMap();
            agMap.put("AgentTO", agTo);
            return agTo;
        }
        return agTo;
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
    private void doAllActionPerform(HashMap map) throws Exception {
        log.info("In doActionPerform()");
        try{
            HashMap data = new HashMap();
            HashMap dataMap = new HashMap();
            System.out.println("data list 4234234234"+map);
            if(map!=null && map.size()>0){
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());    
                data.putAll(map);
                if(rdoAmountBased){
                    data.put("AGENT_AMOUNT_BASED_DETAILS", "AGENT_AMOUNT_BASED_DETAILS");    
//                    data.put("AGENT_AMOUNT_BASED_DETAILS", agentAccountDetails());  
                }else if(rdoAccountBased){
                    data.put("AGENT_NEW_ACCOUNT_BASED_DETAILS", "AGENT_NEW_ACCOUNT_BASED_DETAILS");                
                }
                if(map.containsKey("FROM_AGENT_COMM_SCREEN")){
                    data.put("FROM_AGENT_COMM_SCREEN", "FROM_AGENT_COMM_SCREEN");  
                }
                data.put("AGENT_COMMISION_DETAILS", agentAccountDetails());                
                data.put("COMMISION_ACHD", getAgentCommisionAcHd());
                AgentCommisonTO objAgentCommisionTO = (AgentCommisonTO) setAllAgentCommision(map);
                objAgentCommisionTO.setCommand(getCommand());
                data.put("AgentCommisionTO", objAgentCommisionTO);
                System.out.println("data :" + data);
                System.out.println("objAgentCommisionTO :" + objAgentCommisionTO);
                HashMap proxyResultMap = proxy.execute(data, operationMap);
                System.out.println("proxyResultMap*****" + proxyResultMap);
                setProxyReturnMap(proxyResultMap);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void doActionPerform() throws Exception {
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        final AgentCommisonTO objAgentCommisionTO = (AgentCommisonTO) setAgentCommision();
        objAgentCommisionTO.setCommand(getCommand());
        System.out.println("getTxtCommisionOpenedAct() : "+getTxtCommisionOpenedAct());
        if(rdoAmountBased){
            data.put("AGENT_AMOUNT_BASED_DETAILS", "AGENT_AMOUNT_BASED_DETAILS");    
        }else if(rdoAccountBased){
            data.put("AGENT_NEW_ACCOUNT_BASED_DETAILS", "AGENT_NEW_ACCOUNT_BASED_DETAILS");                
            data.put("TOTAL_NEW_ACCOUNT_AMT", getTxtCommisionOpenedAct());                
        }
//        data.put("AGENT_COMMISION_INTERST",agentAccountDetails());
        data.put("AgentCommisionTO", objAgentCommisionTO);
//        data.put("AGENT_COMMISION_INTEREST",commisionCalculation());
        data.put("AGENT_COMMISION_DETAILS", agentAccountDetails());
        data.put("COMMISION_PROD_ID", getCboProdId());
        data.put("COMMISION_ACHD", getAgentCommisionAcHd());
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        System.out.println("data :" + data);
        System.out.println("objAgentCommisionTO :" + objAgentCommisionTO);
//        System.out.println("doActionPerform :"+commisionCalculation()); 

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
        setTransType("");
        setAgentId("");
        setTxtCommision("");
        setTxtCommisionDuringThePeriod("");
        setTxtCommisionForThePeriod("");
        setTdtFromDate("");
        setTdtToDate("");
        setTdsAmt(new Double(0).doubleValue());
        setTotCommToOA(new Double(0).doubleValue());
        setTotCommToTD(new Double(0).doubleValue());
        setTdtFromDate("");
        setTdtToDate("");
        setCboAgentId("");
        setCboProdtype("");
        setCboProdId("");
        setTxtAgentcomm(new Double(0).doubleValue());
        setTxtTdsCommission(new Double(0).doubleValue());
        tbmAgentCommission = new TableModel();
        setAgentCommisionAcHd("");
        objAgentCommisionTO = null;
        setTdtCalcCommUpTo(""); // Added by nithya on 26-10-2016 for 3177
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
     * Getter for property txtCommision.
     *
     * @return Value of property txtCommision.
     */
    public java.lang.String getTxtCommision() {
        return txtCommision;
    }

    /**
     * Setter for property txtCommision.
     *
     * @param txtCommision New value of property txtCommision.
     */
    public void setTxtCommision(java.lang.String txtCommision) {
        this.txtCommision = txtCommision;
    }

    /**
     * Getter for property txtCommisionDuringThePeriod.
     *
     * @return Value of property txtCommisionDuringThePeriod.
     */
    public java.lang.String getTxtCommisionDuringThePeriod() {
        return txtCommisionDuringThePeriod;
    }

    /**
     * Setter for property txtCommisionDuringThePeriod.
     *
     * @param txtCommisionDuringThePeriod New value of property
     * txtCommisionDuringThePeriod.
     */
    public void setTxtCommisionDuringThePeriod(java.lang.String txtCommisionDuringThePeriod) {
        this.txtCommisionDuringThePeriod = txtCommisionDuringThePeriod;
    }

    /**
     * Getter for property tdtFromDate.
     *
     * @return Value of property tdtFromDate.
     */
    public java.lang.String getTdtFromDate() {
        return tdtFromDate;
    }

    /**
     * Setter for property tdtFromDate.
     *
     * @param tdtFromDate New value of property tdtFromDate.
     */
    public void setTdtFromDate(java.lang.String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    /**
     * Getter for property txtCommisionForThePeriod.
     *
     * @return Value of property txtCommisionForThePeriod.
     */
    public java.lang.String getTxtCommisionForThePeriod() {
        return txtCommisionForThePeriod;
    }

    /**
     * Setter for property txtCommisionForThePeriod.
     *
     * @param txtCommisionForThePeriod New value of property
     * txtCommisionForThePeriod.
     */
    public void setTxtCommisionForThePeriod(java.lang.String txtCommisionForThePeriod) {
        this.txtCommisionForThePeriod = txtCommisionForThePeriod;
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
     * Getter for property tdtToDate.
     *
     * @return Value of property tdtToDate.
     */
    public java.lang.String getTdtToDate() {
        return tdtToDate;
    }

    /**
     * Setter for property tdtToDate.
     *
     * @param tdtToDate New value of property tdtToDate.
     */
    public void setTdtToDate(java.lang.String tdtToDate) {
        this.tdtToDate = tdtToDate;
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

    /**
     * Getter for property totComm.
     *
     * @return Value of property totComm.
     */
    public double getTotComm() {
        return totComm;
    }

    /**
     * Setter for property totComm.
     *
     * @param totComm New value of property totComm.
     */
    public void setTotComm(double totComm) {
        this.totComm = totComm;
    }

    /**
     * Getter for property tptCollAmt.
     *
     * @return Value of property tptCollAmt.
     */
    public double getTptCollAmt() {
        return tptCollAmt;
    }

    /**
     * Setter for property tptCollAmt.
     *
     * @param tptCollAmt New value of property tptCollAmt.
     */
    public void setTptCollAmt(double tptCollAmt) {
        this.tptCollAmt = tptCollAmt;
    }

    /**
     * Getter for property tdsAmt.
     *
     * @return Value of property tdsAmt.
     */
    public double getTdsAmt() {
        return tdsAmt;
    }

    /**
     * Setter for property tdsAmt.
     *
     * @param tdsAmt New value of property tdsAmt.
     */
    public void setTdsAmt(double tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    /**
     * Getter for property totCommToOA.
     *
     * @return Value of property totCommToOA.
     */
    public double getTotCommToOA() {
        return totCommToOA;
    }

    /**
     * Setter for property totCommToOA.
     *
     * @param totCommToOA New value of property totCommToOA.
     */
    public void setTotCommToOA(double totCommToOA) {
        this.totCommToOA = totCommToOA;
    }

    /**
     * Getter for property totCommToTD.
     *
     * @return Value of property totCommToTD.
     */
    public double getTotCommToTD() {
        return totCommToTD;
    }

    /**
     * Setter for property totCommToTD.
     *
     * @param totCommToTD New value of property totCommToTD.
     */
    public void setTotCommToTD(double totCommToTD) {
        this.totCommToTD = totCommToTD;
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

    /**
     * Getter for property deposit_closing.
     *
     * @return Value of property deposit_closing.
     */
    public java.lang.String getDeposit_closing() {
        return deposit_closing;
    }

    /**
     * Setter for property deposit_closing.
     *
     * @param deposit_closing New value of property deposit_closing.
     */
    public void setDeposit_closing(java.lang.String deposit_closing) {
        this.deposit_closing = deposit_closing;
    }

    /**
     * Getter for property deposit_No.
     *
     * @return Value of property deposit_No.
     */
    public java.lang.String getDeposit_No() {
        return deposit_No;
    }

    /**
     * Setter for property deposit_No.
     *
     * @param deposit_No New value of property deposit_No.
     */
    public void setDeposit_No(java.lang.String deposit_No) {
        this.deposit_No = deposit_No;
    }

    /**
     * Getter for property prematurePeriod.
     *
     * @return Value of property prematurePeriod.
     */
    public java.lang.Double getPrematurePeriod() {
        return prematurePeriod;
    }

    /**
     * Setter for property prematurePeriod.
     *
     * @param prematurePeriod New value of property prematurePeriod.
     */
    public void setPrematurePeriod(java.lang.Double prematurePeriod) {
        this.prematurePeriod = prematurePeriod;
    }

    /**
     * Getter for property depositDate.
     *
     * @return Value of property depositDate.
     */
    public java.lang.String getDepositDate() {
        return depositDate;
    }

    /**
     * Setter for property depositDate.
     *
     * @param depositDate New value of property depositDate.
     */
    public void setDepositDate(java.lang.String depositDate) {
        this.depositDate = depositDate;
    }

    public ComboBoxModel getCbmProdId() {
        return cbmProdId;
    }

    public void setCbmProdId(ComboBoxModel cbmProdId) {
        this.cbmProdId = cbmProdId;
    }
    
    public double AgentCommisionSlabCalculation() {
        HashMap agentCommisionMap = new HashMap();
        agentCommisionMap.put("PROD_TYPE",getCboProdtype());
        agentCommisionMap.put("PROD_ID",getCboProdId());
        String Agent = getAgentId();
        double amt = 0.0,acctIntroCommision = 0.0, commFromBank = 0.0,commFromAcHolder = 0.0;
        if(!rdoAccountBased){
            ArrayList agAmtLst = (ArrayList) agentsAmount(Agent);        
            if (agAmtLst != null && agAmtLst.size() > 0) {
                HashMap agMap = new HashMap();
                agMap = (HashMap) agAmtLst.get(0);
                amt = CommonUtil.convertObjToDouble(agMap.get("AMOUNT"));
                tptCollAmt = amt;
            }else{
                ClientUtil.displayAlert("Selected Product Id haven't any daily transaction!!");
                return 0;
            }
        }
        List lst = ClientUtil.executeQuery("getSelectAgentCommisionSlabRates", agentCommisionMap);
        if(lst != null && lst.size()>0){
            agentCommisionMap = (HashMap)lst.get(0);            
            System.out.println("agentCommisionMap : " + agentCommisionMap);
            acctIntroCommision = CommonUtil.convertObjToDouble(agentCommisionMap.get("ACCT_INTRO_COMMISION")).doubleValue();
            commFromBank = CommonUtil.convertObjToDouble(agentCommisionMap.get("COMM_FROM_BANK")).doubleValue();
            commFromAcHolder = CommonUtil.convertObjToDouble(agentCommisionMap.get("COMM_FROM_AC_HOLDER")).doubleValue();
            if(rdoAccountBased){
                HashMap actOpenedMap = new HashMap();
                Date TdFromDt = DateUtil.getDateMMDDYYYY(tdtFromDate);
                if (TdFromDt != null) {
                    Date tdFromDate = (Date) curDate.clone();
                    tdFromDate.setDate(TdFromDt.getDate());
                    tdFromDate.setMonth(TdFromDt.getMonth());
                    tdFromDate.setYear(TdFromDt.getYear());
                    actOpenedMap.put("MONTH_START_DATE", tdFromDate);
                } else {
                    actOpenedMap.put("MONTH_START_DATE", DateUtil.getDateMMDDYYYY(tdtFromDate));
                }
                Date TdtToDt = DateUtil.getDateMMDDYYYY(tdtToDate);
                if (TdtToDt != null) {
                    Date tdtToDate = (Date) curDate.clone();
                    tdtToDate.setDate(TdtToDt.getDate());
                    tdtToDate.setMonth(TdtToDt.getMonth());
                    tdtToDate.setYear(TdtToDt.getYear());
                    actOpenedMap.put("MONTH_END_DATE", tdtToDate);
                } else {
                    actOpenedMap.put("MONTH_END_DATE", DateUtil.getDateMMDDYYYY(tdtToDate));
                }
                actOpenedMap.put("AGENT_ID",getAgentId());
                List lst1 = ClientUtil.executeQuery("getSelectAgentIntroCount", actOpenedMap);
                if(lst1 != null && lst1.size()>0){
                    actOpenedMap = (HashMap)lst1.get(0);            
                    System.out.println("actOpenedMap : " + actOpenedMap);
                    double noOfAcctOpened = CommonUtil.convertObjToDouble(actOpenedMap.get("TOTAL_COUNT")).doubleValue();
                    totComm = noOfAcctOpened * acctIntroCommision;
                }
            }else if(rdoDefaultPercentage){
//                HashMap slabAmount = new HashMap();        
                tdsAmt = 0.0;
                double totCommFromBank = 0.0;
                double totCommFromActHolder = 0.0;
//                totComm = getCommissionSlabAmount();
                if(commFromBank>0){
                    totCommFromBank = tptCollAmt * commFromBank / 100;
                    System.out.println("totCommFromBank : "+totComm+" tptCollAmt : "+tptCollAmt+" commFromBank : "+commFromBank);
                }
                if(commFromAcHolder>0){
                    totCommFromActHolder = tptCollAmt * commFromAcHolder / 100;
                    System.out.println("totCommFromActHolder : "+totComm+" tptCollAmt : "+tptCollAmt+" commFromBank : "+commFromBank);
                }
                setTotComm(Math.round(tptCollAmt));
                setTdsAmt(tdsAmt);
                setTptCollAmt(amt);
                setTotCommToOA(Math.round(totCommFromBank+totCommFromActHolder));
                setTotCommToTD(0);
                setTxtCommision(CommonUtil.convertObjToStr(new Double(tptCollAmt)));
                setTxtCommisionForThePeriod(CommonUtil.convertObjToStr(Math.round(totCommFromBank+totCommFromActHolder)));
                ttNotifyObservers();
            }
        }else{
            ClientUtil.displayAlert("Selected Product Id haven't any product settings,Please configure from !!");
        }
        return totComm;
    }
    
    public double calcAgentCommissionUsingSlabPercentage() {       
        HashMap agentMap = new HashMap();
        agentMap.put("AGENT_ID",getAgentId());
        agentMap.put("PROD_ID",getCboProdId());
        agentMap.put("TO_DT",getTdtToDate());
        agentMap.put("VAT",getTxtTdsCommission());
        List commLst = ClientUtil.executeQuery("getAgentCommissionDetailsForProduct", agentMap);
        if(commLst != null && commLst.size() > 0){
            HashMap commDetailsMap = (HashMap)commLst.get(0);
            if(commDetailsMap.containsKey(("COLLECTION_AMOUNT")) && commDetailsMap.get(("COLLECTION_AMOUNT")) != null){
                if(CommonUtil.convertObjToDouble(commDetailsMap.get(("COLLECTION_AMOUNT"))) > 0){
                   tptCollAmt =  CommonUtil.convertObjToDouble(commDetailsMap.get(("COLLECTION_AMOUNT")));
                   if(commDetailsMap.containsKey(("COMMISSION")) && commDetailsMap.get(("COMMISSION")) != null){
                       totComm = CommonUtil.convertObjToDouble(commDetailsMap.get(("COMMISSION")));
                   }if(commDetailsMap.containsKey(("VAT")) && commDetailsMap.get(("VAT")) != null){
                       tdsAmt = CommonUtil.convertObjToDouble(commDetailsMap.get(("VAT")));
                   }
                }else{
                    ClientUtil.displayAlert("Selected Product Id haven't any daily transaction!!");
                }
            }else{
                ClientUtil.displayAlert("Selected Product Id haven't any daily transaction!!");
            }
        }else{
            ClientUtil.displayAlert("Selected Product Id haven't any daily transaction!!");
        }
        setTotComm(Math.round(totComm));
        setTdsAmt(tdsAmt);
        setTptCollAmt(tptCollAmt);
        setTotCommToOA(Math.round(totComm));
        setTotCommToTD(0);
        setTxtCommision(CommonUtil.convertObjToStr(new Double(tptCollAmt)));
        setTxtCommisionForThePeriod(CommonUtil.convertObjToStr(totComm));
        ttNotifyObservers();
        tptCollAmt = 0.0;
        totComm = 0.0;        
        return totComm;
    }
    
}