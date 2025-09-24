/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 

 * editMigratedData.java
 *
 * Created on January 7, 2004, 5:14 PM
 */

package com.see.truetransact.ui.transaction.editMigratedData;

/**
 *
 * @author Suresh R
 *
 */
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.GregorianCalendar;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;

public class EditMigratedDataOB extends CObservable {
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private HashMap hash;
    private HashMap operationMap;
    private ProxyFactory proxy;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private int result;
    Date currDt = null;
    //Common
    private ComboBoxModel cbmProdType,cbmProdId;
    private String cboProdType = "";
    private String cboProdId = "";
    private String act_Num ="";
    
    //Loan
    private String lblBorrowNo ="";
    private Date tdtLoanIntCalcDt = null;
    private String loanROI ="";
    private ComboBoxModel cbmSanRepaymentType;
    private ComboBoxModel cbmRepayFreq;
    private Date tdtSanctionDate =null;
    private Date tdtFDate =null;
    private Date tdtTDate =null;
    private String txtLimit ="";
    private String txtNoInstallments ="";
    private String cboSanRepaymentType = "";
    private String cboRepayFreq = "";
    
    //Deposit
    private Date tdtDepositOpenDt =null;
    private Date tdtMaturityDt =null;
    private Date tdtDepLastInterestAppDt =null;
    private Date tdtDepNextIntAppDt =null;
    private String txtMaturityAmt ="";
    private String txtRateOfInterest ="";
    private String txtTotalIntAmt ="";
    private String txtPeriodicIntAmt ="";
    private String txtTotalIntDrawn ="";
    private String txtTotalCredit ="";
    private ComboBoxModel cbmIntFrequency;
    private String cboIntFrequency = "";
    private String freeze ="";
    private String txtPeriodOfDeposit_Years = "";
    private String txtPeriodOfDeposit_Months = "";
    private String txtPeriodOfDeposit_Days = "";
    public String productBehavesLike = new String();
    
    //Deposit Interest
    private String tdtInterestDt ="";
    private String txtInterestAmount ="";
    private String cboInterestType ="";
    private ComboBoxModel cbmInterestType;
    private boolean newData = false;
    private LinkedHashMap depositInterestMap;
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblDepositTable;
    private Iterator processLstIterator;
    
    //MDS
    private String txtNetTransID ="";
    private Date tdtTransactionDt = null;
    private String txtInstallmentAmount ="";
    private String txtNoOfInstPay ="";
    private String txtInstPayable ="";
    private String txtInterestAmt ="";
    private String txtBonusAmt ="";
    private String txtDiscountAmt ="";
    private String txtNetAmt ="";
    private String memberName ="";
    
    private static EditMigratedDataOB EditMigratedDataOB;
    static {
        try {
            EditMigratedDataOB = new EditMigratedDataOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    public EditMigratedDataOB() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "EditMigratedDataJNDI");
        operationMap.put(CommonConstants.HOME, "EditMigratedData.EditMigratedDataHome");
        operationMap.put(CommonConstants.REMOTE, "EditMigratedData.EditMigratedData");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        currDt = ClientUtil.getCurrentDate();
        notifyObservers();
        fillDropdown();
        setTableTile();
        tblDepositTable = new EnhancedTableModel(null, tableTitle);
    }
    
    public void setTableTile(){
        tableTitle.add("Sl No");
        tableTitle.add("Interest Date");
        tableTitle.add("Interest Amount");
        tableTitle.add("Interest Type");
        IncVal = new ArrayList();
    }
    
    public static EditMigratedDataOB getInstance() {
        return EditMigratedDataOB;
    }
    
    public void getProductBehaveLike(String param) {
        final HashMap whereMap = new HashMap();
        if (param == null) {
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
        } else {
            whereMap.put("PROD_ID", param);
        }
        final List resultList = ClientUtil.executeQuery("getProductBehavesLike", whereMap);
        HashMap resultProductBehavesLike = (HashMap) resultList.get(0);
        productBehavesLike = CommonUtil.convertObjToStr(resultProductBehavesLike.get("BEHAVES_LIKE"));
        //productInterestType = CommonUtil.convertObjToStr(resultProductBehavesLike.get("INT_TYPE"));
        setBehavesLike(productBehavesLike);
    }
    
    public HashMap setAmountsAccROI(HashMap detailsHash, HashMap param) {
        HashMap amtDetHash = new HashMap();
        long period = 0;
        long cummPeriod = 0;
        long cummMonth = 0;
        double totalAmt = 0.0;
        if (param == null) {
            if (detailsHash.get("BEHAVES_LIKE").equals("FIXED")) {
                if (detailsHash.get("INTEREST_TYPE").equals("MONTHLY")) {
                    if (detailsHash.containsKey("EXTENSION")) {
                        period = CommonUtil.convertObjToLong(detailsHash.get("PERIOD"));
                    } else {
                        period = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Months()) * 30;
                        period = period + CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Years()) * 360;
                    }
                } else {
                    Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDepositOpenDt));
                    Date endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtMaturityDt));
                    period = DateUtil.dateDiff(startDt, endDt);
                    if (!detailsHash.containsKey("EXTENSION")) {
                        int count = 0;
                        while (DateUtil.dateDiff(startDt, endDt) > 0) {
                            int month = startDt.getMonth();
                            int startYear = startDt.getYear() + 1900;
                            if (month == 1 && startYear % 4 == 0) {
                                count++;
                            }
                            startDt = DateUtil.addDays(startDt, 30);
                        }
                        period -= count;
                    }
                }
            } else {
                if ((getTxtPeriodOfDeposit_Days() != null) && (!getTxtPeriodOfDeposit_Days().equals(""))) {
                    if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                        period = period + Integer.parseInt(getTxtPeriodOfDeposit_Days());
                        cummPeriod = period;
                    }
                }
                if ((getTxtPeriodOfDeposit_Months() != null) && (!getTxtPeriodOfDeposit_Months().equals(""))) {
                    period = period + Integer.parseInt(getTxtPeriodOfDeposit_Months()) * 30;
                    if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                        cummMonth = Integer.parseInt(getTxtPeriodOfDeposit_Months());
                    }
                }
                if ((getTxtPeriodOfDeposit_Years() != null) && (!getTxtPeriodOfDeposit_Years().equals(""))) {
                    period = period + Integer.parseInt(getTxtPeriodOfDeposit_Years()) * 360;
                }
            }
            if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                long fullPeriod = 0;
                fullPeriod = period;
                double princAmt = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
                double simpleAmt = 0.0;
                double completeAmt = 0.0;
                cummPeriod = cummPeriod % 30;
                cummMonth = cummMonth % 3;
                //Added By Suresh       Deposit Period Checking Below One Year
                long depositPeriod = 0;
                boolean pnrCalculation = false;
                Date startDepDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDepositOpenDt));
                Date endDepDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtMaturityDt));
                depositPeriod = DateUtil.dateDiff(startDepDt, endDepDt);
                HashMap prodIntMap = new HashMap();
                detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
                List prodIntList = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                if (prodIntList != null && prodIntList.size() > 0) {
                    prodIntMap.putAll((HashMap) prodIntList.get(0));
                    if (CommonUtil.convertObjToStr(prodIntMap.get("PNR_CALCULATION")).equals("Y")) {
                        pnrCalculation = true;
                    } else {
                        pnrCalculation = false;
                    }
                }
                if (depositPeriod < 365 && pnrCalculation) {        //Below one Year
                    if (param == null) {
                        //detailsHash.put("CATEGORY_ID", cbmCategory.getKeyForSelected());
                        detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
                    } else {
                        detailsHash.putAll(param);
                    }
                    detailsHash.put("BELOW_ONE_YEAR", "BELOW_ONE_YEAR");
                    detailsHash.put("BEHAVES_LIKE", "FIXED");
                    detailsHash.put("PEROID", String.valueOf(depositPeriod));
                    InterestCalc interestCalc = new InterestCalc();
                    amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                } else {
                    if (cummPeriod > 0 || cummMonth > 0) {
                        cummMonth = cummMonth * 30;
                        cummPeriod = cummPeriod + cummMonth;
                    }
                    if (fullPeriod > 0) {
                        period = fullPeriod - cummPeriod;
                        detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                        detailsHash.put("PEROID", String.valueOf(period));
                        //detailsHash.put("CATEGORY_ID", cbmCategory.getKeyForSelected());
                        detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
                        List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                        if (list.size() > 0) {
                            detailsHash.putAll((HashMap) list.get(0));
                            InterestCalc interestCalc = new InterestCalc();
                            amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                            completeAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                        }
                        detailsHash.put("AMOUNT", amtDetHash.get("AMOUNT"));
                        detailsHash.remove("PEROID");
                        int yearPer = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Years());
                        yearPer = yearPer * 12;
                        int monthPer = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Months());
                        monthPer = (monthPer + yearPer) / 3;
                        int totMonth = monthPer * 3;
                        int tot = 0;
                        Date endDt = null;
                        Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDepositOpenDt));
                        if (monthPer >= 1 || cummPeriod > 0) {
                            int yearTobeAdded = 1900;
                            int totYr = 0;
                            int bal = 0;
                            int day = 0;
                            monthPer = CommonUtil.convertObjToInt(getTxtPeriodOfDeposit_Months());
                            int month = 0;
                            int year = 0;
                            if (totMonth >= 12) {
                                totYr = totMonth / 12;
                                year = year + totYr;
                            } else {
                                bal = totMonth;
                            }
                            if (totYr >= 1) {
                                totYr = totYr * 12;
                                bal = totMonth - totYr;
                            }
                            GregorianCalendar cal = new GregorianCalendar((startDt.getYear() + yearTobeAdded), startDt.getMonth(), startDt.getDate());
                            cal.add(GregorianCalendar.YEAR, year);
                            cal.add(GregorianCalendar.MONTH, bal);
                            cal.add(GregorianCalendar.DAY_OF_MONTH, day);
                            String depDt = DateUtil.getStringDate(cal.getTime());
                            Date date = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depDt));
                            if (detailsHash.containsKey("EXTENSION")) {
                                endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt));
                            } else {
                                endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtMaturityDt));
                            }
                            period = DateUtil.dateDiff(date, endDt);
                        }
                    }
                    if (cummPeriod > 0) {
                        detailsHash.put("BEHAVES_LIKE", "FIXED");
                        detailsHash.put("PEROID", String.valueOf(cummPeriod));
                        //detailsHash.put("CATEGORY_ID", cbmCategory.getKeyForSelected());
                        detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
                        List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                        if (list.size() > 0) {
                            detailsHash.putAll((HashMap) list.get(0));
                            InterestCalc interestCalc = new InterestCalc();
                            detailsHash.put("INTEREST_TYPE", "YEARLY");
                            detailsHash.put("INTEREST_TYPE", "YEARLY");
                            detailsHash.put("PERIOD_MONTHS", 0.0);
                            detailsHash.put("PERIOD_DAYS", cummPeriod);
                            detailsHash.put("PERIOD_YEARS", 0.0);
                            amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                            simpleAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                        }
                    }
                    detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                    double interest = simpleAmt + completeAmt;
                    amtDetHash.put("INTEREST", new Double(interest));
                    amtDetHash.put("AMOUNT", new Double(princAmt + interest));
                }
            } else {
                detailsHash.put("PEROID", String.valueOf(period));
            }
        }
        if (!detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            //--- If the param is NUll then put category and Prod ,else put all the param value to the detailHash
            if (param == null) {
                //detailsHash.put("CATEGORY_ID", cbmCategory.getKeyForSelected());
                detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
            } else {
                detailsHash.putAll(param);
            }
            //Added By Suresh
            if (detailsHash.get("BEHAVES_LIKE").equals("FIXED") && period < 365) {
                detailsHash.put("BELOW_ONE_YEAR", "BELOW_ONE_YEAR");
            }
            HashMap discountedMap = new HashMap();
            discountedMap.put("PROD_ID", detailsHash.get("PROD_ID"));
            List lstDiscounted = ClientUtil.executeQuery("getDepProdDetails", discountedMap);
            if (lstDiscounted != null && lstDiscounted.size() > 0) {
                discountedMap = (HashMap) lstDiscounted.get(0);
                detailsHash.put("DISCOUNTED_RATE", discountedMap.get("DISCOUNTED_RATE"));
            }
            List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
            if (list.size() > 0) {
                detailsHash.putAll((HashMap) list.get(0));
                InterestCalc interestCalc = new InterestCalc();
                amtDetHash = interestCalc.calcAmtDetails(detailsHash);
            }
        }
        System.out.println("###################### amtDetHash : " + amtDetHash);
        return amtDetHash;
    }

    public int getIncrementType() {
        int incType = 0;
        try {
            Double incVal = CommonUtil.convertObjToDouble(cbmRepayFreq.getKeyForSelected());
            incType = incVal.intValue();
            incVal = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return incType;
    }

    public int getInstallNo(String strNoInstalls, int incType) {
        int instNo = 0;
        try {
            Double insNo = CommonUtil.convertObjToDouble(strNoInstalls);
            instNo = insNo.intValue();// - 1;
            if (incType == 7) {
                instNo = instNo * 7;
            } else if (incType >= 90) {
                instNo = ((int) (incType / 30)) * instNo;
            }
            insNo = null;
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return instNo;
    }
    
    public void setBehavesLike(String productBehavesLike) {
        this.productBehavesLike = productBehavesLike;
    }

    public String getBehavesLike() {
        return productBehavesLike;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        notifyObservers();
    }
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        notifyObservers();
    }
    public int getResult(){
        return result;
    }
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public void setActionType(int action) {
        actionType = action;
        setChanged();
    }
    public int getActionType(){
        return actionType;
    }
    
    /** A method to set the combo box values */
    private void fillDropdown() throws Exception{
        try{
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PRODUCTTYPE");
            lookup_keys.add("DEPOSITSPRODUCT.DEPOSITPERIOD");
            lookup_keys.add("ACCTHEADMAIN.BALTYPE");
            lookup_keys.add("TERM_LOAN.REPAYMENT_TYPE");
            lookup_keys.add("LOAN.FREQUENCY");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            lookUpHash = null;
            getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.DEPOSITPERIOD"));
            cbmIntFrequency = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));
            cbmSanRepaymentType = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("LOAN.FREQUENCY"));
            cbmRepayFreq = new ComboBoxModel(key,value);
            cbmRepayFreq.removeKeyAndElement("1");
            
            getKeyValue((HashMap)keyValue.get("ACCTHEADMAIN.BALTYPE"));
            cbmInterestType = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
//            key.add("MDS");
//            value.add("MDS");
            cbmProdType = new ComboBoxModel(key,value);
            cbmProdType.removeKeyAndElement("OA");
            cbmProdType.removeKeyAndElement("GL");
            cbmProdType.removeKeyAndElement("SA");
            cbmProdType.removeKeyAndElement("TL");
            cbmProdType.removeKeyAndElement("AB");
            cbmProdType.removeKeyAndElement("AD");
            cbmProdId = new ComboBoxModel();
            keyValue = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            if( depositInterestMap == null ){
                depositInterestMap = new LinkedHashMap();
            }
            int  slno=0;
            int nums[]= new int[150];
            int max=nums[0];
            if(!updateMode){
                ArrayList data = tblDepositTable.getDataArrayList();
                slno=serialNo(data);
            }else{
                if(isNewData()){
                    ArrayList data = tblDepositTable.getDataArrayList();
                    slno=serialNo(data);
                }else{
                    int b=CommonUtil.convertObjToInt(tblDepositTable.getValueAt(rowSelected,0));
                    slno=b;
                }
            }
            ArrayList rowList = new ArrayList();
            HashMap rowMap = new HashMap();
            rowMap.put("SL_NO",String.valueOf(slno));
            rowMap.put("INTEREST_DATE",getTdtInterestDt());
            rowMap.put("INTEREST_AMOUNT",getTxtInterestAmount());
            rowMap.put("INTEREST_TYPE",getCboInterestType());
            String sno=String.valueOf(slno);
            depositInterestMap.put(sno,rowMap);
            addTableDetails(rowSel,sno);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void addTableDetails(int rowSel, String sno)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblDepositTable.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblDepositTable.getDataArrayList().get(j)).get(0);
            if(sno.equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblDepositTable.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(sno);
                IncParRow.add(getTdtInterestDt());
                IncParRow.add(getTxtInterestAmount());
                IncParRow.add(getCboInterestType());
                tblDepositTable.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(sno);
            IncParRow.add(getTdtInterestDt());
            IncParRow.add(getTxtInterestAmount());
            IncParRow.add(getCboInterestType());
            tblDepositTable.insertRow(tblDepositTable.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public int serialNo(ArrayList data){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(tblDepositTable.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    
    public void populateDepositInterestDetails(String row){
        try{
            resetDepositInterestDetails();
            final HashMap selectedMap = (HashMap) depositInterestMap.get(row);
            populateTableData(selectedMap);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTableData(HashMap selectedMap)  throws Exception{
        setTdtInterestDt(CommonUtil.convertObjToStr(selectedMap.get("INTEREST_DATE")));
        setTxtInterestAmount(CommonUtil.convertObjToStr(selectedMap.get("INTEREST_AMOUNT")));
        setCboInterestType(CommonUtil.convertObjToStr(selectedMap.get("INTEREST_TYPE")));
    }
    
    public void resetDepositInterestDetails() {
        setTdtInterestDt("");
        setTxtInterestAmount("");
        setCboInterestType("");
    }
    
    public void deleteTableData(String val, int row){
        depositInterestMap.remove(val);
        resetTableValues();
        try{
            processLstIterator = depositInterestMap.keySet().iterator();
            String key1 = "";
            for(int i=0; i<depositInterestMap.size(); i++){
                key1 = (String)processLstIterator.next();
                HashMap singleMap = new HashMap();
                ArrayList incTabRow = new ArrayList();
                singleMap = (HashMap) depositInterestMap.get(key1);
                if(singleMap!=null && singleMap.size()>0){
                    incTabRow.add(CommonUtil.convertObjToStr(singleMap.get("SL_NO")));
                    incTabRow.add(CommonUtil.convertObjToStr(singleMap.get("INTEREST_DATE")));
                    incTabRow.add(CommonUtil.convertObjToStr(singleMap.get("INTEREST_AMOUNT")));
                    incTabRow.add(CommonUtil.convertObjToStr(singleMap.get("INTEREST_TYPE")));
                    tblDepositTable.addRow(incTabRow);
                }
            }
            notifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void getData(HashMap whereMap){
        List depositIntList = ClientUtil.executeQuery("getDepositInterestDetailsForDataEntry",whereMap);
        if(depositIntList!=null && depositIntList.size() > 0){
            if( depositInterestMap == null ){
                depositInterestMap = new LinkedHashMap();
            }
            int s=1;
            for(int i = depositIntList.size(), j = 0; i > 0; i--,j++){
                depositInterestMap.put(String.valueOf(s),depositIntList.get(j));
                s++;
            }
            ArrayList addList =new ArrayList(depositInterestMap.keySet());
            int slNo=1;
            for(int k=0;k<addList.size();k++){
                HashMap singleMap = new HashMap();
                ArrayList incTabRow = new ArrayList();
                singleMap = (HashMap)  depositInterestMap.get(addList.get(k));
                incTabRow.add(String.valueOf(slNo));
                singleMap.put("SL_NO",String.valueOf(slNo));
                incTabRow.add(CommonUtil.convertObjToStr(singleMap.get("INTEREST_DATE")));
                incTabRow.add(CommonUtil.convertObjToStr(singleMap.get("INTEREST_AMOUNT")));
                incTabRow.add(CommonUtil.convertObjToStr(singleMap.get("INTEREST_TYPE")));
                //BigDecimal Issue
                singleMap.put("INTEREST_AMOUNT",CommonUtil.convertObjToStr(singleMap.get("INTEREST_AMOUNT")));
                tblDepositTable.addRow(incTabRow);
                slNo++;
            }
        }
    }
    
    public void addInterestTable(Date startDate, double interestAmount, int totalRow, int intFreq){
        depositInterestMap = new LinkedHashMap();
        for(int k=1;k<=totalRow;k++){
            HashMap singleMap = new HashMap();
            ArrayList incTabRow = new ArrayList();
            incTabRow.add(String.valueOf(k));
            incTabRow.add(CommonUtil.convertObjToStr(startDate));
            incTabRow.add(CommonUtil.convertObjToStr(interestAmount));
            incTabRow.add("Credit");
            tblDepositTable.addRow(incTabRow);
            singleMap.put("SL_NO",String.valueOf(k));
            singleMap.put("INTEREST_DATE",CommonUtil.convertObjToStr(startDate));
            singleMap.put("INTEREST_AMOUNT",CommonUtil.convertObjToStr(interestAmount));
            singleMap.put("INTEREST_TYPE","Credit");
            depositInterestMap.put(String.valueOf(k),singleMap);
            startDate = DateUtil.addDays(startDate,intFreq);
        }
    }
    
    public void resetTableValues(){
        tblDepositTable.setDataArrayList(null,tableTitle);
    }
    
    
    /** To perform the appropriate operation */
    public void doActionPerform(String status) {
        try {
            HashMap dataMap = new HashMap();
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            String productType = "";
            productType = CommonUtil.convertObjToStr(cbmProdType.getKeyForSelected());
            System.out.println("#### productType : "+productType);
            dataMap.put("PROD_TYPE",productType);
            dataMap.put("ACT_NUM",getAct_Num());
            dataMap.put("PROD_ID",CommonUtil.convertObjToStr(cbmProdId.getKeyForSelected()));
            dataMap.put("MIG_STATUS",status);
            if(productType.equals("TL") || productType.equals("AD")){
                dataMap.put("NEW_LAST_INT_CALC_DT", getTdtLoanIntCalcDt());
                dataMap.put("INTEREST", getLoanROI());
                dataMap.put("SANCTION_DT", getTdtSanctionDate());
                dataMap.put("FROM_DT", getTdtFDate());
                dataMap.put("TO_DT", getTdtTDate());
                dataMap.put("BORROW_NO", getLblBorrowNo());
                dataMap.put("LIMIT", getTxtLimit());
                dataMap.put("NO_OF_INSTALL", getTxtNoInstallments());
                dataMap.put("REPAY_TYPE", getCboSanRepaymentType());
                dataMap.put("REPAY_FREQ", getCboRepayFreq());
            }else if(productType.equals("TD")){
                dataMap.put("DEPOSIT_OPEN_DT", getTdtDepositOpenDt());
                dataMap.put("DEPOSIT_MATURITY_DT", getTdtMaturityDt());
                dataMap.put("DEPOSIT_DAYS", getTxtPeriodOfDeposit_Days());
                dataMap.put("DEPOSIT_MONTHS", getTxtPeriodOfDeposit_Months());
                dataMap.put("DEPOSIT_YEARS", getTxtPeriodOfDeposit_Years());
                dataMap.put("LAST_INT_APPL_DT", getTdtDepLastInterestAppDt());
                dataMap.put("NEXT_INT_APPL_DT", getTdtDepNextIntAppDt());
                dataMap.put("MATURITY_AMT", getTxtMaturityAmt());
                dataMap.put("RATE_OF_INT", getTxtRateOfInterest());
                dataMap.put("TOT_INT_AMT", getTxtTotalIntAmt());
                dataMap.put("PERIODIC_INT_AMT", getTxtPeriodicIntAmt());
                dataMap.put("INTPAY_FREQ", getCboIntFrequency());
                dataMap.put("TOTAL_INT_DRAWN", getTxtTotalIntDrawn());
                dataMap.put("TOTAL_INT_CREDIT", getTxtTotalCredit());
                dataMap.put("FREEZE", getFreeze());
                if(depositInterestMap!=null && depositInterestMap.size()>0){
                    dataMap.put("DEPOSIT_INTEREST_MAP",depositInterestMap);
                }
            }else if(productType.equals("MDS")){
                dataMap.put("NET_TRANS_ID", getTxtNetTransID());
                dataMap.put("TRANS_DT", getTdtTransactionDt());
                dataMap.put("NO_OF_INST", getTxtNoOfInstPay());
                dataMap.put("INST_AMT", getTxtInstallmentAmount());
                dataMap.put("INST_AMT_PAYABLE", getTxtInstPayable());
                dataMap.put("PENAL_AMT", getTxtInterestAmt());
                dataMap.put("BONUS_AMT", getTxtBonusAmt());
                dataMap.put("DISCOUNT_AMT", getTxtDiscountAmt());
                dataMap.put("NET_AMT", getTxtNetAmt());
                dataMap.put("MEMBER_NAME", getMemberName());
            }
            dataMap.put(CommonConstants.MODULE, getModule());
            dataMap.put(CommonConstants.SCREEN, getScreen());
            dataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            HashMap proxyResultMap = proxy.execute(dataMap, operationMap);
            setProxyReturnMap(proxyResultMap);
            dataMap = null;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** Resetting all tbe Fields of UI */
    public  void resetForm(){
        setCboProdType("");
        setCboProdId("");
        setAct_Num("");
        setTdtLoanIntCalcDt(null);
        setLoanROI("");
        setTdtDepLastInterestAppDt(null);
        setLblBorrowNo("");
        setTdtSanctionDate(null);
        setTdtFDate(null);
        setTdtTDate(null);
        setTxtLimit("");
        setTxtNoInstallments("");
        setCboSanRepaymentType("");
        setCboRepayFreq("");
        setTdtDepositOpenDt(null);
        setTdtMaturityDt(null);
        setTxtPeriodOfDeposit_Days("");
        setTxtPeriodOfDeposit_Months("");
        setTxtPeriodOfDeposit_Years("");
        setTdtDepNextIntAppDt(null);
        setTxtMaturityAmt("");
        setTxtRateOfInterest("");
        setTxtTotalIntAmt("");
        setTxtPeriodicIntAmt("");
        setCboIntFrequency("");
        setTxtTotalIntDrawn("");
        setTxtTotalCredit("");
        setFreeze("");
        setTxtNetTransID("");
        setTdtTransactionDt(null);
        setTxtInstallmentAmount("");
        setTxtNoOfInstPay("");
        setTxtInstPayable("");
        setTxtInterestAmt("");
        setTxtBonusAmt("");
        setTxtDiscountAmt("");
        setTxtNetAmt("");
        resetDepositInterestDetails();
        productBehavesLike = "";
        depositInterestMap = null;
        tblDepositTable.setDataArrayList(null,tableTitle);
    }
    
    public void clearInterestMap(){
        depositInterestMap = null;
    }
    
    /** Gets the command issued Insert , Upadate or Delete **/
    private String getCommand() throws Exception{
        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_COPY:
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
    
    
    
    /** Resets the General Remittance Fields to Null  */
    public void resetOBFields(){
        
    }
    
    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 0) {    //If Condition Added By Suresh R 27-Apr-2014
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
            cbmProdId = new ComboBoxModel(key, value);
            this.cbmProdId = cbmProdId;
            setChanged();
        }
    }
    
    
    
    /**
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
    /**
     * Getter for property cboProdType.
     * @return Value of property cboProdType.
     */
    public java.lang.String getCboProdType() {
        return cboProdType;
    }
    
    /**
     * Setter for property cboProdType.
     * @param cboProdType New value of property cboProdType.
     */
    public void setCboProdType(java.lang.String cboProdType) {
        this.cboProdType = cboProdType;
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
     * Getter for property tdtLoanIntCalcDt.
     * @return Value of property tdtLoanIntCalcDt.
     */
    public java.util.Date getTdtLoanIntCalcDt() {
        return tdtLoanIntCalcDt;
    }
    
    /**
     * Setter for property tdtLoanIntCalcDt.
     * @param tdtLoanIntCalcDt New value of property tdtLoanIntCalcDt.
     */
    public void setTdtLoanIntCalcDt(java.util.Date tdtLoanIntCalcDt) {
        this.tdtLoanIntCalcDt = tdtLoanIntCalcDt;
    }
    
    /**
     * Getter for property act_Num.
     * @return Value of property act_Num.
     */
    public java.lang.String getAct_Num() {
        return act_Num;
    }
    
    /**
     * Setter for property act_Num.
     * @param act_Num New value of property act_Num.
     */
    public void setAct_Num(java.lang.String act_Num) {
        this.act_Num = act_Num;
    }
    
    /**
     * Getter for property tdtDepLastInterestAppDt.
     * @return Value of property tdtDepLastInterestAppDt.
     */
    public java.util.Date getTdtDepLastInterestAppDt() {
        return tdtDepLastInterestAppDt;
    }
    
    /**
     * Setter for property tdtDepLastInterestAppDt.
     * @param tdtDepLastInterestAppDt New value of property tdtDepLastInterestAppDt.
     */
    public void setTdtDepLastInterestAppDt(java.util.Date tdtDepLastInterestAppDt) {
        this.tdtDepLastInterestAppDt = tdtDepLastInterestAppDt;
    }
    
    /**
     * Getter for property tdtDepNextIntAppDt.
     * @return Value of property tdtDepNextIntAppDt.
     */
    public java.util.Date getTdtDepNextIntAppDt() {
        return tdtDepNextIntAppDt;
    }
    
    /**
     * Setter for property tdtDepNextIntAppDt.
     * @param tdtDepNextIntAppDt New value of property tdtDepNextIntAppDt.
     */
    public void setTdtDepNextIntAppDt(java.util.Date tdtDepNextIntAppDt) {
        this.tdtDepNextIntAppDt = tdtDepNextIntAppDt;
    }
    
    /**
     * Getter for property txtMaturityAmt.
     * @return Value of property txtMaturityAmt.
     */
    public java.lang.String getTxtMaturityAmt() {
        return txtMaturityAmt;
    }
    
    /**
     * Setter for property txtMaturityAmt.
     * @param txtMaturityAmt New value of property txtMaturityAmt.
     */
    public void setTxtMaturityAmt(java.lang.String txtMaturityAmt) {
        this.txtMaturityAmt = txtMaturityAmt;
    }
    
    /**
     * Getter for property txtRateOfInterest.
     * @return Value of property txtRateOfInterest.
     */
    public java.lang.String getTxtRateOfInterest() {
        return txtRateOfInterest;
    }
    
    /**
     * Setter for property txtRateOfInterest.
     * @param txtRateOfInterest New value of property txtRateOfInterest.
     */
    public void setTxtRateOfInterest(java.lang.String txtRateOfInterest) {
        this.txtRateOfInterest = txtRateOfInterest;
    }
    
    /**
     * Getter for property txtTotalIntAmt.
     * @return Value of property txtTotalIntAmt.
     */
    public java.lang.String getTxtTotalIntAmt() {
        return txtTotalIntAmt;
    }
    
    /**
     * Setter for property txtTotalIntAmt.
     * @param txtTotalIntAmt New value of property txtTotalIntAmt.
     */
    public void setTxtTotalIntAmt(java.lang.String txtTotalIntAmt) {
        this.txtTotalIntAmt = txtTotalIntAmt;
    }
    
    /**
     * Getter for property txtPeriodicIntAmt.
     * @return Value of property txtPeriodicIntAmt.
     */
    public java.lang.String getTxtPeriodicIntAmt() {
        return txtPeriodicIntAmt;
    }
    
    /**
     * Setter for property txtPeriodicIntAmt.
     * @param txtPeriodicIntAmt New value of property txtPeriodicIntAmt.
     */
    public void setTxtPeriodicIntAmt(java.lang.String txtPeriodicIntAmt) {
        this.txtPeriodicIntAmt = txtPeriodicIntAmt;
    }
    
    /**
     * Getter for property txtTotalIntDrawn.
     * @return Value of property txtTotalIntDrawn.
     */
    public java.lang.String getTxtTotalIntDrawn() {
        return txtTotalIntDrawn;
    }
    
    /**
     * Setter for property txtTotalIntDrawn.
     * @param txtTotalIntDrawn New value of property txtTotalIntDrawn.
     */
    public void setTxtTotalIntDrawn(java.lang.String txtTotalIntDrawn) {
        this.txtTotalIntDrawn = txtTotalIntDrawn;
    }
    
    /**
     * Getter for property txtTotalCredit.
     * @return Value of property txtTotalCredit.
     */
    public java.lang.String getTxtTotalCredit() {
        return txtTotalCredit;
    }
    
    /**
     * Setter for property txtTotalCredit.
     * @param txtTotalCredit New value of property txtTotalCredit.
     */
    public void setTxtTotalCredit(java.lang.String txtTotalCredit) {
        this.txtTotalCredit = txtTotalCredit;
    }
    
    /**
     * Getter for property cbmIntFrequency.
     * @return Value of property cbmIntFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntFrequency() {
        return cbmIntFrequency;
    }
    
    /**
     * Setter for property cbmIntFrequency.
     * @param cbmIntFrequency New value of property cbmIntFrequency.
     */
    public void setCbmIntFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmIntFrequency) {
        this.cbmIntFrequency = cbmIntFrequency;
    }
    
    /**
     * Getter for property cboIntFrequency.
     * @return Value of property cboIntFrequency.
     */
    public java.lang.String getCboIntFrequency() {
        return cboIntFrequency;
    }
    
    /**
     * Setter for property cboIntFrequency.
     * @param cboIntFrequency New value of property cboIntFrequency.
     */
    public void setCboIntFrequency(java.lang.String cboIntFrequency) {
        this.cboIntFrequency = cboIntFrequency;
    }
    
    /**
     * Getter for property txtNetTransID.
     * @return Value of property txtNetTransID.
     */
    public java.lang.String getTxtNetTransID() {
        return txtNetTransID;
    }
    
    /**
     * Setter for property txtNetTransID.
     * @param txtNetTransID New value of property txtNetTransID.
     */
    public void setTxtNetTransID(java.lang.String txtNetTransID) {
        this.txtNetTransID = txtNetTransID;
    }
    
    /**
     * Getter for property txtInstallmentAmount.
     * @return Value of property txtInstallmentAmount.
     */
    public java.lang.String getTxtInstallmentAmount() {
        return txtInstallmentAmount;
    }
    
    /**
     * Setter for property txtInstallmentAmount.
     * @param txtInstallmentAmount New value of property txtInstallmentAmount.
     */
    public void setTxtInstallmentAmount(java.lang.String txtInstallmentAmount) {
        this.txtInstallmentAmount = txtInstallmentAmount;
    }
    
    /**
     * Getter for property txtNoOfInstPay.
     * @return Value of property txtNoOfInstPay.
     */
    public java.lang.String getTxtNoOfInstPay() {
        return txtNoOfInstPay;
    }
    
    /**
     * Setter for property txtNoOfInstPay.
     * @param txtNoOfInstPay New value of property txtNoOfInstPay.
     */
    public void setTxtNoOfInstPay(java.lang.String txtNoOfInstPay) {
        this.txtNoOfInstPay = txtNoOfInstPay;
    }
    
    /**
     * Getter for property txtInstPayable.
     * @return Value of property txtInstPayable.
     */
    public java.lang.String getTxtInstPayable() {
        return txtInstPayable;
    }
    
    /**
     * Setter for property txtInstPayable.
     * @param txtInstPayable New value of property txtInstPayable.
     */
    public void setTxtInstPayable(java.lang.String txtInstPayable) {
        this.txtInstPayable = txtInstPayable;
    }
    
    /**
     * Getter for property txtInterestAmt.
     * @return Value of property txtInterestAmt.
     */
    public java.lang.String getTxtInterestAmt() {
        return txtInterestAmt;
    }
    
    /**
     * Setter for property txtInterestAmt.
     * @param txtInterestAmt New value of property txtInterestAmt.
     */
    public void setTxtInterestAmt(java.lang.String txtInterestAmt) {
        this.txtInterestAmt = txtInterestAmt;
    }
    
    /**
     * Getter for property txtBonusAmt.
     * @return Value of property txtBonusAmt.
     */
    public java.lang.String getTxtBonusAmt() {
        return txtBonusAmt;
    }
    
    /**
     * Setter for property txtBonusAmt.
     * @param txtBonusAmt New value of property txtBonusAmt.
     */
    public void setTxtBonusAmt(java.lang.String txtBonusAmt) {
        this.txtBonusAmt = txtBonusAmt;
    }
    
    /**
     * Getter for property txtDiscountAmt.
     * @return Value of property txtDiscountAmt.
     */
    public java.lang.String getTxtDiscountAmt() {
        return txtDiscountAmt;
    }
    
    /**
     * Setter for property txtDiscountAmt.
     * @param txtDiscountAmt New value of property txtDiscountAmt.
     */
    public void setTxtDiscountAmt(java.lang.String txtDiscountAmt) {
        this.txtDiscountAmt = txtDiscountAmt;
    }
    
    /**
     * Getter for property txtNetAmt.
     * @return Value of property txtNetAmt.
     */
    public java.lang.String getTxtNetAmt() {
        return txtNetAmt;
    }
    
    /**
     * Setter for property txtNetAmt.
     * @param txtNetAmt New value of property txtNetAmt.
     */
    public void setTxtNetAmt(java.lang.String txtNetAmt) {
        this.txtNetAmt = txtNetAmt;
    }
    
    /**
     * Getter for property tdtTransactionDt.
     * @return Value of property tdtTransactionDt.
     */
    public java.util.Date getTdtTransactionDt() {
        return tdtTransactionDt;
    }
    
    /**
     * Setter for property tdtTransactionDt.
     * @param tdtTransactionDt New value of property tdtTransactionDt.
     */
    public void setTdtTransactionDt(java.util.Date tdtTransactionDt) {
        this.tdtTransactionDt = tdtTransactionDt;
    }
    
    /**
     * Getter for property memberName.
     * @return Value of property memberName.
     */
    public java.lang.String getMemberName() {
        return memberName;
    }
    
    /**
     * Setter for property memberName.
     * @param memberName New value of property memberName.
     */
    public void setMemberName(java.lang.String memberName) {
        this.memberName = memberName;
    }
    
    /**
     * Getter for property loanROI.
     * @return Value of property loanROI.
     */
    public String getLoanROI() {
        return loanROI;
    }
    
    /**
     * Setter for property loanROI.
     * @param loanROI New value of property loanROI.
     */
    public void setLoanROI(String loanROI) {
        this.loanROI = loanROI;
    }
    
    /**
     * Getter for property txtInterestAmount.
     * @return Value of property txtInterestAmount.
     */
    public java.lang.String getTxtInterestAmount() {
        return txtInterestAmount;
    }
    
    /**
     * Setter for property txtInterestAmount.
     * @param txtInterestAmount New value of property txtInterestAmount.
     */
    public void setTxtInterestAmount(java.lang.String txtInterestAmount) {
        this.txtInterestAmount = txtInterestAmount;
    }
    
    /**
     * Getter for property cboInterestType.
     * @return Value of property cboInterestType.
     */
    public String getCboInterestType() {
        return cboInterestType;
    }
    
    /**
     * Setter for property cboInterestType.
     * @param cboInterestType New value of property cboInterestType.
     */
    public void setCboInterestType(String cboInterestType) {
        this.cboInterestType = cboInterestType;
    }
    
    /**
     * Getter for property cbmInterestType.
     * @return Value of property cbmInterestType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInterestType() {
        return cbmInterestType;
    }
    
    /**
     * Setter for property cbmInterestType.
     * @param cbmInterestType New value of property cbmInterestType.
     */
    public void setCbmInterestType(com.see.truetransact.clientutil.ComboBoxModel cbmInterestType) {
        this.cbmInterestType = cbmInterestType;
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
     * Getter for property tblDepositTable.
     * @return Value of property tblDepositTable.
     */
    public EnhancedTableModel getTblDepositTable() {
        return tblDepositTable;
    }
    
    /**
     * Setter for property tblDepositTable.
     * @param tblDepositTable New value of property tblDepositTable.
     */
    public void setTblDepositTable(EnhancedTableModel tblDepositTable) {
        this.tblDepositTable = tblDepositTable;
    }
    
    /**
     * Getter for property tdtInterestDt.
     * @return Value of property tdtInterestDt.
     */
    public java.lang.String getTdtInterestDt() {
        return tdtInterestDt;
    }
    
    /**
     * Setter for property tdtInterestDt.
     * @param tdtInterestDt New value of property tdtInterestDt.
     */
    public void setTdtInterestDt(java.lang.String tdtInterestDt) {
        this.tdtInterestDt = tdtInterestDt;
    }
    
    /**
     * Getter for property freeze.
     * @return Value of property freeze.
     */
    public String getFreeze() {
        return freeze;
    }
    
    /**
     * Setter for property freeze.
     * @param freeze New value of property freeze.
     */
    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }
    
    public Date getTdtDepositOpenDt() {
        return tdtDepositOpenDt;
    }

    public void setTdtDepositOpenDt(Date tdtDepositOpenDt) {
        this.tdtDepositOpenDt = tdtDepositOpenDt;
    }

    public Date getTdtMaturityDt() {
        return tdtMaturityDt;
    }

    public void setTdtMaturityDt(Date tdtMaturityDt) {
        this.tdtMaturityDt = tdtMaturityDt;
    }

    public String getTxtPeriodOfDeposit_Days() {
        return txtPeriodOfDeposit_Days;
    }

    public void setTxtPeriodOfDeposit_Days(String txtPeriodOfDeposit_Days) {
        this.txtPeriodOfDeposit_Days = txtPeriodOfDeposit_Days;
    }

    public String getTxtPeriodOfDeposit_Months() {
        return txtPeriodOfDeposit_Months;
    }

    public void setTxtPeriodOfDeposit_Months(String txtPeriodOfDeposit_Months) {
        this.txtPeriodOfDeposit_Months = txtPeriodOfDeposit_Months;
    }

    public String getTxtPeriodOfDeposit_Years() {
        return txtPeriodOfDeposit_Years;
    }

    public void setTxtPeriodOfDeposit_Years(String txtPeriodOfDeposit_Years) {
        this.txtPeriodOfDeposit_Years = txtPeriodOfDeposit_Years;
    }
    
    public ComboBoxModel getCbmRepayFreq() {
        return cbmRepayFreq;
    }

    public void setCbmRepayFreq(ComboBoxModel cbmRepayFreq) {
        this.cbmRepayFreq = cbmRepayFreq;
    }

    public ComboBoxModel getCbmSanRepaymentType() {
        return cbmSanRepaymentType;
    }

    public void setCbmSanRepaymentType(ComboBoxModel cbmSanRepaymentType) {
        this.cbmSanRepaymentType = cbmSanRepaymentType;
    }

    public String getCboRepayFreq() {
        return cboRepayFreq;
    }

    public void setCboRepayFreq(String cboRepayFreq) {
        this.cboRepayFreq = cboRepayFreq;
    }

    public String getCboSanRepaymentType() {
        return cboSanRepaymentType;
    }

    public void setCboSanRepaymentType(String cboSanRepaymentType) {
        this.cboSanRepaymentType = cboSanRepaymentType;
    }

    public String getLblBorrowNo() {
        return lblBorrowNo;
    }

    public void setLblBorrowNo(String lblBorrowNo) {
        this.lblBorrowNo = lblBorrowNo;
    }

    public Date getTdtFDate() {
        return tdtFDate;
    }

    public void setTdtFDate(Date tdtFDate) {
        this.tdtFDate = tdtFDate;
    }

    public Date getTdtSanctionDate() {
        return tdtSanctionDate;
    }

    public void setTdtSanctionDate(Date tdtSanctionDate) {
        this.tdtSanctionDate = tdtSanctionDate;
    }

    public Date getTdtTDate() {
        return tdtTDate;
    }

    public void setTdtTDate(Date tdtTDate) {
        this.tdtTDate = tdtTDate;
    }

    public String getTxtLimit() {
        return txtLimit;
    }

    public void setTxtLimit(String txtLimit) {
        this.txtLimit = txtLimit;
    }

    public String getTxtNoInstallments() {
        return txtNoInstallments;
    }

    public void setTxtNoInstallments(String txtNoInstallments) {
        this.txtNoInstallments = txtNoInstallments;
    }
    
}