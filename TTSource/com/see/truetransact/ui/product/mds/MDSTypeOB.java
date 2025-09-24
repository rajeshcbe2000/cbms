/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSTypeOB.java
 *
 * Created on Thu Jun 02 13:10:56 IST 2011
 */

package com.see.truetransact.ui.product.mds;


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
import com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO;
import com.see.truetransact.transferobject.product.mds.MDSProductAcctHeadTO;
import com.see.truetransact.transferobject.product.loan.LoanProductChargesTabTO;
import com.see.truetransact.transferobject.product.mds.MDSProductInstallmentScheduleTO;
import com.see.truetransact.transferobject.sms.SMSParameterTO;

/**
 *
 * @author
 */

public class MDSTypeOB extends CObservable{
    
    private int txtNoofDivision = 0;
    private int txtNoofMemberPer = 0;
    private int txtNoofAuctions = 0;
    private int txtNoofInst = 0;
    private int txtNoofDraws = 0;
    private double txtInstAmt = 0.0;
    private int txtNoofMemberScheme = 0;
    private double txtTotAmtPerDivision = 0.0;
    private String tdtSchemeStDt = "";
    private String tdtSchemeEndDt = "";
    private String cboInstFreq = "";
    private ComboBoxModel cbmInstFreq;
    private String txtAcctHead = "";
    private String cboProductId = "";
    private String prod_Desc = "";
    private ComboBoxModel cbmProductId;
    private String txtResolutionNo = "";
    private String tdtlResolutionDate = "";
    private String txtSchemeName = "";
    private String txtSchemeDesc = "";
    private double txtTotAmtUnderScheme = 0.0;
    private String txtApplicable2 = "";
    private String txtApplicable4 = "";
    private String txtApplicable1 = "";
    private String txtApplicable3 = "";
    private String tdtDate = "";
    private int day = 0;
    private int txtInstallments = 0;  //AJITH set default value
    private String tdtInstallmentDt = "";
    private double txtAmount = 0.0;
    private double txtBonus = 0.0;
    private double txtPaymentAmount = 0.0;
    private String txtChittalNumberPattern = "";
    private int txtSuffix = 1;
    private int txtNextChittalNumber = 1;
    private int cboInstallmentDay = 0;
    private int cboAuctionDay = 0;
    private ComboBoxModel cbmInstallmentDay;
    private ComboBoxModel cbmAuctionDay;
    private int txtSlNo = 0;
    
    private String rdoMunnalAllowed = "";
    private String rdoApplicableDivision = "";
    private String rdoThalayalAllowed = "";
    private String rdoPaymentDone = "";
    private String rdoPredefinitonInst = "";
    private String rdoMultipleMembers = "";
    private double txtNoofCoChittals = 0.0;
    private int txtNoofCoInstallments = 0; 
    private int txtMaxNoofMemberCoChittals = 0;
    
    private String txtReceiptHead = "";
    private String txtPaymentHead = "";
    private String txtSuspenseHead = "";
    private String txtSuspenseAccNo = "";
    private String txtMiscellaneousHead = "";
    private String txtCommisionHead = "";
    private String txtBonusPayableHead = "";
    private String txtBonusReceivableHead = "";
    private String txtPenalHead = "";
    private String txtThalayalReceiptsHead = "";
    private String txtThalayalBonusHead = "";
    private String txtMunnalBonusHead = "";
    private String txtMunnalReceiptsHead = "";
    private String txtBankingHead = "";
    private String txtNoticeChargesHead = "";
    private String txtChargeHead = "";
    private String txtStampAdvanceHead = "";
    private String txtARCCostHead = "";
    private String txtARCExpenseHead = "";
    private String txtEACostHead = "";
    private String txtEAExpenseHead = "";
    private String txtEPCostHead = "";
    private String txtEPExpenseHead = "";
    private String txtPostageHead = "";
    private String txtCaseExpensesHead = "";
    private String txtDiscountHead = "";
    private String txtMDSPayableHead = "";
    private String txtMDSReceivableHead = "";
    private String txtSundryReceiptHead = "";    
    private String txtSundryPaymentHead = "";
    private String txtForFeitedPaymentHead = "";
    private String txtPostageAdvHead = "";
    private String isRevPostAdv = "";
    private String mdsType = "";
    private String chkIsASpecialScheme = "N";  //AJITH set default value
    private String chkSMSAlert = "N";
    private String chkBonusPrint = "";
    private String autionTime = "";
	private String chkCreditStampAdvance = "";
    private String txtLegalChrgHead = ""; // Added by nithya on 02-08-2017
    private String chkBankSettlement = "N";// Added by nithya on 11-08-2017 for 7145
    private int txtSchemeGraceperiod = 0;
    
    private ComboBoxModel cbmGroupNo;
    private String cboGroupNo="";
    
    private String txtPartPayBonusRecoveryHead = ""; // Done by nithya on 27-08-2019 for KD 575 - MDS Money Payment With Bonus Recovery Head Option.
	 private String txtOtherChrgeAcHd = "";
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

 

    public ComboBoxModel getCbmGroupNo() {
        return cbmGroupNo;
    }

    public void setCbmGroupNo(ComboBoxModel cbmGroupNo) {
        this.cbmGroupNo = cbmGroupNo;
    }

    public String getCboGroupNo() {
        return cboGroupNo;
    }

    public void setCboGroupNo(String cboGroupNo) {
        this.cboGroupNo = cboGroupNo;
    }   
    
    SMSParameterTO smsParameterTO;
    private Date currDt = null;
    public String gettxtForFeitedPaymentHead() {
        return txtForFeitedPaymentHead;
    }
    public void settxtForFeitedPaymentHead(String txtForFeitedPaymentHead) {
        this.txtForFeitedPaymentHead = txtForFeitedPaymentHead;
    }
    public String getIsRevPostAdv() {
        return isRevPostAdv;
    }

    public void setIsRevPostAdv(String isRevPostAdv) {
        this.isRevPostAdv = isRevPostAdv;
    }

    public String getTxtPostageAdvHead() {
        return txtPostageAdvHead;
    }

    public void setTxtPostageAdvHead(String txtPostageAdvHead) {
        this.txtPostageAdvHead = txtPostageAdvHead;
    }
    private String cboSuspenseProdID;
    private ComboBoxModel cbmSuspenseProdID;
    private String rdoGLorAccNo = "";
    private String txtClosureRate="";
    private String txtSancNo="";
    private String tdtSancDt="";
    private String txaRemarks="";
    
    public String getAutionTime() {
        return autionTime;
    }

    public void setAutionTime(String autionTime) {
        this.autionTime = autionTime;
    }
    
    public String getTxtSancNo() {
        return txtSancNo;
    }

    public void setTxtSancNo(String txtSancNo) {
        this.txtSancNo = txtSancNo;
    }

    public String getTdtSancDt() {
        return tdtSancDt;
    }

    public void setTdtSancDt(String tdtSancDt) {
        this.tdtSancDt = tdtSancDt;
    }

    public String getTxaRemarks() {
        return txaRemarks;
    }

    public void setTxaRemarks(String txaRemarks) {
        this.txaRemarks = txaRemarks;
    }
    

    public String getTxtClosureRate() {
        return txtClosureRate;
    }

    public void setTxtClosureRate(String txtClosureRate) {
        this.txtClosureRate = txtClosureRate;
    }
    public String getMdsType() {
        return mdsType;
    }
    public void setMdsType(String mdsType) {
        this.mdsType = mdsType;
    }
    
    
    //Notice Charge
    private String cboNoticeType="";
    private String txtNoticeChargeAmt="";
    private String txtPostageChargeAmt="";
    private ComboBoxModel cbmNoticeType;
    private LinkedHashMap NoticeTypeTO=null;//contain not deleted records
    private ArrayList entireNoticeTypeRow=null;
    private static int notice_Charge_No=1;
    private EnhancedTableModel tblNoticeCharge;
    final ArrayList noticeChargeTabTitle=new ArrayList();
    private LinkedHashMap deletedNoticeType=null;
    private static int deleted_NoticeCharge=1;
    private ArrayList chargeTabRow;
    LoanProductChargesTabTO objLoanProductChargesTabTO;
    ArrayList arrayLoanProductChargesTabTO = null;
    
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblScheduleDetails;
    
    private boolean newData = false;
    private LinkedHashMap scheduleMap;
    private LinkedHashMap deletedScheduleMap;
    
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(MDSTypeOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();

    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private MDSProductAcctHeadTO mdsProductAcctHeadTO = null;
    private MDSProductInstallmentScheduleTO objScheduleTO;
    private MDSProductSchemeTO objSchemeTO;
    /** Creates a new instance of TDS MiantenanceOB */
    
    private String chkDiscountFirstInst = "N";
    private double txtDiscountAmt = 0.0;
    
    public MDSTypeOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "MDSTypeJNDI");
            map.put(CommonConstants.HOME, "MDSTypeJNDIHome");
            map.put(CommonConstants.REMOTE, "MDSType");
            setTableTile();
            tblScheduleDetails = new EnhancedTableModel(null, tableTitle);
            setNoticeChargeTab();
            tblNoticeCharge=new EnhancedTableModel(null,noticeChargeTabTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("Installments No");
        tableTitle.add("Installment Date");
        tableTitle.add("Amount");
        tableTitle.add("Bonus");
        tableTitle.add("Payment Amount");
        IncVal = new ArrayList();
    }
    
    private void setNoticeChargeTab()throws Exception{
        noticeChargeTabTitle.add("Notice Type");
        noticeChargeTabTitle.add("Notice Amt");
        noticeChargeTabTitle.add("Postage Amt");
    }
    
    private void fillDropdown() throws Exception{
        try{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("OPERATIVEACCTPRODUCT.PRODFREQ");
            lookup_keys.add("TERM_LOAN.NOTICE_TYPE");
            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.PRODFREQ"));
            cbmInstFreq = new ComboBoxModel(key,value);
            cbmInstFreq.removeKeyAndElement("1");
            makeNull();
//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("TERM_LOAN.NOTICE_TYPE"));
            cbmNoticeType= new ComboBoxModel(key,value);
            makeNull();
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
            for(int i=1; i<=31; i++){
                key.add(String.valueOf(i));
                value.add(String.valueOf(i));
                cbmInstallmentDay = new ComboBoxModel(key,value);
                cbmAuctionDay = new ComboBoxModel(key,value);
            }
            makeNull();
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"getProductIdFromMDSProduct");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmProductId = new ComboBoxModel(key,value);
            param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"Charges.getProductDataSA");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmSuspenseProdID = new ComboBoxModel(key,value);
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void makeNull(){
        key=null;
        value=null;
    }
    
     public void saveNoticeCharge(boolean tableClicked,int rowClick){
        try{
            LoanProductChargesTabTO objloanProductChargesTabTO=setLoanProductNoticeChargesTO();
            if(NoticeTypeTO==null)
                NoticeTypeTO=new LinkedHashMap();
            if(entireNoticeTypeRow==null)
                entireNoticeTypeRow=new ArrayList();
            if(tableClicked) {
                entireNoticeTypeRow.set(rowClick, setColumnValueNoticeCharge((rowClick+1), objloanProductChargesTabTO));
                NoticeTypeTO.put(String.valueOf(rowClick+1), objloanProductChargesTabTO);
                setLoanProductNoticeChargeOB(objloanProductChargesTabTO);
            }else{
                entireNoticeTypeRow.add(setColumnValueNoticeCharge(notice_Charge_No,objloanProductChargesTabTO));
                NoticeTypeTO.put(String.valueOf(notice_Charge_No),objloanProductChargesTabTO);
                notice_Charge_No++;
            }
            objloanProductChargesTabTO=null;
            tblNoticeCharge.setDataArrayList(entireNoticeTypeRow, noticeChargeTabTitle);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public ArrayList setColumnValueNoticeCharge(int rowClicked,LoanProductChargesTabTO objLoanProductChargesTabTO){
        ArrayList row =new ArrayList();
        row.add((String) getCbmNoticeType().getDataForKey(objLoanProductChargesTabTO.getNoticeType()));
        row.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeChargeAmt()));
        row.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getPostageAmt()));
        return row;
    }
    
    public LoanProductChargesTabTO setLoanProductNoticeChargesTO(){
        LoanProductChargesTabTO objLoanProductChargesTabTO=new  LoanProductChargesTabTO();
        objLoanProductChargesTabTO.setNoticeType(CommonUtil.convertObjToStr(getCbmNoticeType().getKeyForSelected()));
        objLoanProductChargesTabTO.setNoticeChargeAmt(new Double(Double.parseDouble(getTxtNoticeChargeAmt())));
        objLoanProductChargesTabTO.setPostageAmt(new Double(Double.parseDouble(getTxtPostageChargeAmt())));
        return objLoanProductChargesTabTO;
    }
    
    public void setLoanProductNoticeChargeOB(LoanProductChargesTabTO objLoanProductChargesTabTO){
        setCboNoticeType((String) getCbmNoticeType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeType())));
        setTxtNoticeChargeAmt(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeChargeAmt()));
        setTxtPostageChargeAmt(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getPostageAmt()));
    }
    
    public void resetNoticeChargeValues(){
        setCboNoticeType("");
        setTxtPostageChargeAmt("");
        setTxtNoticeChargeAmt("");
    }
    
    public void populateNoticeCharge(int row){
        ArrayList NoticeCharge=(ArrayList)tblNoticeCharge.getDataArrayList().get(row);
        setCboNoticeType(CommonUtil.convertObjToStr(NoticeCharge.get(0)));
        setTxtNoticeChargeAmt(CommonUtil.convertObjToStr(NoticeCharge.get(1)));
        setTxtPostageChargeAmt(CommonUtil.convertObjToStr(NoticeCharge.get(2)));
    }
    
     public void deleteNoticeCharge(int index) {
        log.info("deleteDocuments Invoked...");
        try {
            if (NoticeTypeTO != null) {
                LoanProductChargesTabTO loanProductChargesTabTO = (LoanProductChargesTabTO) NoticeTypeTO.remove(String.valueOf(index+1));
                //                if( ( LoanProductChargesTabTO.getStatus().length()>0 ) && ( LoanProductChargesTabTO.getStatus() != null ) && !(LoanProductChargesTabTO.getStatus().equals(""))) {
                if (deletedNoticeType == null)
                    deletedNoticeType = new LinkedHashMap();
                deletedNoticeType.put(String.valueOf(deleted_NoticeCharge++), loanProductChargesTabTO);
                if (NoticeTypeTO != null) {
                    for(int i = index+1,j=NoticeTypeTO.size();i<=j;i++) {
                        NoticeTypeTO.put(String.valueOf(i),(LoanProductChargesTabTO)NoticeTypeTO.remove(String.valueOf((i+1))));
                    }
                }
                loanProductChargesTabTO = null;
                deleted_NoticeCharge--;
                // Reset table data
                entireNoticeTypeRow.remove(index);
                 /* Orders the serial no in the arraylist (tableData) after the removal
               of selected Row in the table */
                System.out.println("DeleteNoticeCharge####"+NoticeTypeTO);
                for(int i=0,j = entireNoticeTypeRow.size();i<j;i++){
                    ( (ArrayList) entireNoticeTypeRow.get(i)).set(0,String.valueOf(i+1));
                }
                tblNoticeCharge.setDataArrayList(entireNoticeTypeRow,noticeChargeTabTitle);
            }
        } catch (Exception  e){
            parseException.logException(e,true);
        }
    }
     
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final MDSProductInstallmentScheduleTO objScheduleTO = new MDSProductInstallmentScheduleTO();
            if( scheduleMap == null ){
                scheduleMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objScheduleTO.setStatusDt((Date)currDt.clone());
                    objScheduleTO.setStatusBy(TrueTransactMain.USER_ID);
                    objScheduleTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objScheduleTO.setStatusDt((Date)currDt.clone());
                    objScheduleTO.setStatusBy(TrueTransactMain.USER_ID);
                    objScheduleTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objScheduleTO.setStatusDt((Date)currDt.clone());
                objScheduleTO.setStatusBy(TrueTransactMain.USER_ID);
                objScheduleTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            int  slno=0;
            int nums[]= new int[150];
            int max=nums[0];
            if(!updateMode){
                ArrayList data = tblScheduleDetails.getDataArrayList();
                slno=serialNo(data);
            }
            else{
                if(isNewData()){
                    ArrayList data = tblScheduleDetails.getDataArrayList();
                    slno=serialNo(data);
                }
                else{
                    int b=CommonUtil.convertObjToInt(tblScheduleDetails.getValueAt(rowSelected,0));
                    slno=b;
                }
            }
            objScheduleTO.setInstallmentNo(slno);  //AJITH Changed From String.valueOf(slno)
            objScheduleTO.setProdId(getCboProductId());
            objScheduleTO.setSchemeName(getTxtSchemeName());
            objScheduleTO.setInstallmentDt(DateUtil.getDateMMDDYYYY(getTdtInstallmentDt()));
            objScheduleTO.setAmount(getTxtAmount());
            objScheduleTO.setBonus(getTxtBonus());
            objScheduleTO.setPaymentAmount(getTxtPaymentAmount());
            objScheduleTO.setDividion(getTxtNoofDivision()); //AJITH Changed From CommonUtil.convertObjToDouble(getTxtNoofDivision())
            objScheduleTO.setCommission(CommonUtil.convertObjToDouble("0"));
            objScheduleTO.setTotalMembers(getTxtNoofMemberPer());  //AJITH Changed From setTotalMembers(CommonUtil.convertObjToDouble(getTxtNoofMemberPer()));
            System.out.println("tototottoto"+objScheduleTO.getDividion());
            scheduleMap.put(objScheduleTO.getInstallmentNo(),objScheduleTO);
            String sno=String.valueOf(slno);
            updateScheduleDetails(rowSel,sno,objScheduleTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateScheduleDetails(int rowSel, String sno, MDSProductInstallmentScheduleTO objScheduleTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblScheduleDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblScheduleDetails.getDataArrayList().get(j)).get(0);
            if(sno.equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblScheduleDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(sno);
                IncParRow.add(getTdtInstallmentDt());
                IncParRow.add(getTxtAmount());
                IncParRow.add(getTxtBonus());
                IncParRow.add(getTxtPaymentAmount());
                tblScheduleDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(sno);
            IncParRow.add(getTdtInstallmentDt());
            IncParRow.add(getTxtAmount());
            IncParRow.add(getTxtBonus());
            IncParRow.add(getTxtPaymentAmount());
            tblScheduleDetails.insertRow(tblScheduleDetails.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void populateSchedileDetails(int row){
        try{
            resetScheduleDetails();
            final MDSProductInstallmentScheduleTO objScheduleTO = (MDSProductInstallmentScheduleTO)scheduleMap.get(row); 
            populateTableData(objScheduleTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTableData(MDSProductInstallmentScheduleTO objScheduleTO)  throws Exception{
        setTxtSlNo(objScheduleTO.getInstallmentNo()); //AJITH
        setTxtSchemeName(CommonUtil.convertObjToStr(objScheduleTO.getSchemeName()));
        setTdtInstallmentDt(CommonUtil.convertObjToStr(objScheduleTO.getInstallmentDt()));
        setTxtAmount(objScheduleTO.getAmount()); //AJITH Changed From CommonUtil.convertObjToStr(objScheduleTO.getAmount())
        setTxtBonus(objScheduleTO.getBonus());  //AJITH
        setTxtPaymentAmount(objScheduleTO.getPaymentAmount());  //AJITH
        setChanged();
        notifyObservers();
    }
    
    public int serialNo(ArrayList data){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(tblScheduleDetails.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    
    public void resetScheduleDetails() {
        setTdtInstallmentDt("");
        setTxtAmount(0.0);
        setTxtBonus(0.0);
        setTxtPaymentAmount(0.0);
        setChanged();
        ttNotifyObservers();
    }
    
    public void resetForm(){
        scheduleMap = null;
        deletedScheduleMap = null;
        resetTableValues();
        resetAcctHeadValues();
        setChanged();
        ttNotifyObservers();
    }
    public void resetAcctHeadValues(){
        setTxtReceiptHead("");
        setTxtPaymentHead("");
        setTxtSuspenseHead("");
        setTxtSuspenseAccNo("");
        setCboSuspenseProdID("");
        setTxtMiscellaneousHead("");
        setTxtCommisionHead("");
        setTxtBonusPayableHead("");
        setTxtBonusReceivableHead("");
        setTxtPenalHead("");
        setTxtThalayalReceiptsHead("");
        setTxtThalayalBonusHead("");
        setTxtMunnalBonusHead("");
        setTxtMunnalReceiptsHead("");
        setTxtBankingHead("");
        setTxtNoticeChargesHead("");
        setTxtChargeHead("");
        setTxtStampAdvanceHead("");
        setTxtARCCostHead("");
        setTxtARCExpenseHead("");
        setTxtEPCostHead("");
        setTxtEPExpenseHead("");
        setTxtPostageHead("");
        setTxtEACostHead("");
        setTxtEAExpenseHead("");
        setTxtCaseExpensesHead("");
        setTxtDiscountHead("");
        setTxtMDSPayableHead("");
        setTxtMDSReceivableHead("");
        setTxtSundryReceiptHead("");
        setTxtSundryPaymentHead("");
        setTxtPostageAdvHead("");
        setTxtNoofCoChittals(0.0);
        setTxtNoofCoInstallments(0);
        setTxtMaxNoofMemberCoChittals(0);
        setRdoMultipleMembers("");
        setRdoGLorAccNo("");
        resetNoticeTypeCharges();
        settxtForFeitedPaymentHead("");
        setMdsType("");
        setTxtLegalChrgHead(""); // Added by nithya on 02-08-2017
        setChkBankSettlement("N");// Added by nithya on 11-08-2017 for 7145
        setTxtPartPayBonusRecoveryHead(""); // Done by nithya on 27-08-2019 for KD 575 - MDS Money Payment With Bonus Recovery Head Option.
        setTxtOtherChrgeAcHd("");
        setTxtSchemeGraceperiod(0); //AJITH // Added by nithya on 21-04-2020 for KD-1611
        setChkDiscountFirstInst("N");
        setTxtDiscountAmt(0.0);
        setChkCreditStampAdvance("");
    }
    public void resetTableValues(){
        tblScheduleDetails.setDataArrayList(null,tableTitle);
    }
    
    public void resetNoticeTypeCharges(){
        resetNoticeChargeValues();
        NoticeTypeTO=null;
        entireNoticeTypeRow=null;
        tblNoticeCharge.setDataArrayList(null,noticeChargeTabTitle);
        notice_Charge_No=1;
    }
    
    public void deleteTableData(int val, int row){
        if(deletedScheduleMap == null){
            deletedScheduleMap = new LinkedHashMap();
        }
        MDSProductInstallmentScheduleTO objScheduleTO = (MDSProductInstallmentScheduleTO) scheduleMap.get(val);
        objScheduleTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedScheduleMap.put(CommonUtil.convertObjToStr(tblScheduleDetails.getValueAt(row,0)),scheduleMap.get(val));
        Object obj;
        obj=val;
        scheduleMap.remove(val);
        resetTableValues();
        try{
            populateTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
   private HashMap setCopiedInstallments()  throws Exception{
       try{
            ArrayList incDataList = new ArrayList();
            incDataList = new ArrayList(scheduleMap.keySet());
            ArrayList addList =new ArrayList(scheduleMap.keySet());
            int length = incDataList.size();
            for(int i=0; i<length; i++){
                MDSProductInstallmentScheduleTO objScheduleTO = (MDSProductInstallmentScheduleTO) scheduleMap.get(addList.get(i));                
                if(!objScheduleTO.getSchemeName().equals("") && objScheduleTO.getSchemeName()!=null){
                    objScheduleTO.setSchemeName(getTxtSchemeName());       
                    objScheduleTO.setDividion(getTxtNoofDivision());  //AJITH Changed From CommonUtil.convertObjToDouble(getTxtNoofDivision())
                    objScheduleTO.setCommission(CommonUtil.convertObjToDouble("0"));
                    objScheduleTO.setTotalMembers(getTxtNoofMemberPer()); //AJITH
                }
            }
       }catch(Exception e){
            parseException.logException(e,true);
       }
       return scheduleMap;
    }
    
    private void populateTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(scheduleMap.keySet());
        ArrayList addList =new ArrayList(scheduleMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            MDSProductInstallmentScheduleTO objScheduleTO = (MDSProductInstallmentScheduleTO) scheduleMap.get(addList.get(i));
            IncVal.add(objScheduleTO);
            if(!objScheduleTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getInstallmentNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getInstallmentDt()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getBonus()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getPaymentAmount()));
                tblScheduleDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    public void setInstAmount(String amount, int rowCount){
        tblScheduleDetails.setDataArrayList(null,tableTitle);
        for(int i=0; i<rowCount; i++){
            ArrayList addList =new ArrayList(scheduleMap.keySet());
            ArrayList incTabRow = new ArrayList();
            MDSProductInstallmentScheduleTO objScheduleTO = (MDSProductInstallmentScheduleTO) scheduleMap.get(addList.get(i));
            objScheduleTO.setAmount(CommonUtil.convertObjToDouble(amount));  //AJITH
            double bonus = CommonUtil.convertObjToDouble(objScheduleTO.getBonus()).doubleValue();
            double payment = CommonUtil.convertObjToDouble(amount).doubleValue()- bonus;
            objScheduleTO.setPaymentAmount(payment);  //AJITH Changed From String.valueOf(payment)
            IncVal.add(objScheduleTO);
            if(!objScheduleTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getInstallmentNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getInstallmentDt()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getBonus()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getPaymentAmount()));
                tblScheduleDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
        
    }
    
    public void setInstallmentDate(String startDt,int rowCount){
        tblScheduleDetails.setDataArrayList(null,tableTitle);
        Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startDt));
        for(int i=0; i<rowCount; i++){
            GregorianCalendar cal = new GregorianCalendar((stDt.getYear()+1900),stDt.getMonth(),stDt.getDate());
            ArrayList addList =new ArrayList(scheduleMap.keySet());
            ArrayList incTabRow = new ArrayList();
            cal.add(GregorianCalendar.MONTH, i);
            MDSProductInstallmentScheduleTO objScheduleTO = (MDSProductInstallmentScheduleTO) scheduleMap.get(addList.get(i));
            objScheduleTO.setInstallmentDt(DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(cal.getTime())));
            objScheduleTO.setDividion(getTxtNoofDivision());  //AJITH Changed From CommonUtil.convertObjToDouble(getTxtNoofDivision())
            objScheduleTO.setCommission(CommonUtil.convertObjToDouble("0"));
            objScheduleTO.setTotalMembers(getTxtNoofMemberPer());  //AJITH
            IncVal.add(objScheduleTO);
            if(!objScheduleTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getInstallmentNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getInstallmentDt()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getAmount()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getBonus()));
                incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getPaymentAmount()));
                tblScheduleDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
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
    
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            objSchemeTO = (MDSProductSchemeTO) ((List) data.get("SchemeListTO")).get(0);
            populateSchemeData(objSchemeTO);
            if (data.containsKey("SMSListTO")) {
                if (data.get("SMSListTO") != null) {
                    smsParameterTO = (SMSParameterTO) ((List) data.get("SMSListTO")).get(0);
                    if (smsParameterTO != null) {
                        populateSMSData(smsParameterTO);
                    }
                }
            }
            if(data.containsKey("getMDSProductAcctHeadTO")){
                mdsProductAcctHeadTO = (MDSProductAcctHeadTO) ((List) data.get("getMDSProductAcctHeadTO")).get(0);
                populateAccHeadData(mdsProductAcctHeadTO);
            }
            if(data.containsKey("ScheduleListTO")){
                scheduleMap = (LinkedHashMap)data.get("ScheduleListTO");
                ArrayList addList =new ArrayList(scheduleMap.keySet());
                for(int i=0;i<addList.size();i++){
                    MDSProductInstallmentScheduleTO  objScheduleTO = (MDSProductInstallmentScheduleTO)  scheduleMap.get(addList.get(i));
                    objScheduleTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objScheduleTO.setAuthorizedStatus("");
                    objScheduleTO.setAuthorizedDt(DateUtil.getDateMMDDYYYY(""));
                    objScheduleTO.setAuthorizedBy("");
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getInstallmentNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getInstallmentDt()));
                    incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getAmount()));
                    incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getBonus()));
                    incTabRow.add(CommonUtil.convertObjToStr(objScheduleTO.getPaymentAmount()));
                    tblScheduleDetails.addRow(incTabRow);
                }
            }
            if(data.containsKey("MDSProductNoticeTO")){
                arrayLoanProductChargesTabTO =  (ArrayList) (data.get("MDSProductNoticeTO"));
                setLoanProductChargesTabTO(arrayLoanProductChargesTabTO);
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateSMSData(SMSParameterTO objSMSTO) throws Exception{
        this.setChkSMSAlert("Y");
        setChanged();
        notifyObservers();
    } 
        
    private void populateSchemeData(MDSProductSchemeTO objSchemeTO) throws Exception{
        this.setIsRevPostAdv(CommonUtil.convertObjToStr(objSchemeTO.getIsRevPostAdv()));
        this.setCboProductId(CommonUtil.convertObjToStr(objSchemeTO.getProdId()));
        this.setTxtSchemeName(CommonUtil.convertObjToStr(objSchemeTO.getSchemeName()));
        this.setTxtSchemeDesc(CommonUtil.convertObjToStr(objSchemeTO.getSchemeDesc()));
        this.setTxtResolutionNo(CommonUtil.convertObjToStr(objSchemeTO.getResolutionNo()));
        this.setTdtlResolutionDate(CommonUtil.convertObjToStr(objSchemeTO.getResolutionDt()));
        this.setTxtNoofDivision(objSchemeTO.getNoOfDivisions());  //AJITH Changed From CommonUtil.convertObjToStr(objSchemeTO.getNoOfDivisions())
        this.setTxtNoofAuctions(objSchemeTO.getNoOfAuctions()); //AJITH
        this.setTxtNoofDraws(objSchemeTO.getNoOfDraws());  //AJITH
        this.setTxtNoofMemberPer(objSchemeTO.getNoOfMemberPerDivision());  //AJITH
        this.setTxtNoofMemberScheme(objSchemeTO.getTotalNoOfMembers()); //AJITH
        this.setTxtInstAmt(objSchemeTO.getInstallmentAmount()); //AJITH
        this.setTxtTotAmtPerDivision(objSchemeTO.getTotalAmountDivision()); //AJITH
        this.setCboInstFreq(CommonUtil.convertObjToStr(objSchemeTO.getInstallmentFrequency()));
        this.setTdtSchemeStDt(CommonUtil.convertObjToStr(objSchemeTO.getSchemeStartDt()));
        this.setTdtSchemeEndDt(CommonUtil.convertObjToStr(objSchemeTO.getSchemeEndDt()));
        this.setCboInstallmentDay(objSchemeTO.getInstallmentDay());  //AJITH
        this.setCboAuctionDay(objSchemeTO.getDrawAuctDay());  //AJITH
        this.setRdoThalayalAllowed(CommonUtil.convertObjToStr(objSchemeTO.getThalayal()));
        this.setRdoMunnalAllowed(CommonUtil.convertObjToStr(objSchemeTO.getMunnal()));
        this.setRdoPredefinitonInst(CommonUtil.convertObjToStr(objSchemeTO.getPredefinitionInstallment()));
        this.setRdoApplicableDivision(CommonUtil.convertObjToStr(objSchemeTO.getAllDivision()));
        this.setTxtApplicable1(CommonUtil.convertObjToStr(objSchemeTO.getDivision1()));
        this.setTxtApplicable2(CommonUtil.convertObjToStr(objSchemeTO.getDivision2()));
        this.setTxtApplicable3(CommonUtil.convertObjToStr(objSchemeTO.getDivision3()));
        this.setTxtApplicable4(CommonUtil.convertObjToStr(objSchemeTO.getDivision4()));
        this.setRdoPaymentDone(CommonUtil.convertObjToStr(objSchemeTO.getAfterPayment()));
        this.setTxtInstallments(CommonUtil.convertObjToInt(objSchemeTO.getInstallments()));
        this.setDay(objSchemeTO.getDay());
        this.setTxtTotAmtUnderScheme(objSchemeTO.getTotalAmountScheme());  //AJITH
        this.setTxtNoofInst(CommonUtil.convertObjToInt(objSchemeTO.getNoOfInstallments())); //AJITH
        this.setProd_Desc(CommonUtil.convertObjToStr(objSchemeTO.getProdDesc()));
        this.setTxtChittalNumberPattern(CommonUtil.convertObjToStr(objSchemeTO.getChittal_No_Pattern()));
        this.setTxtSuffix(objSchemeTO.getSuffix_No());  //AJITH
        this.setTxtNextChittalNumber(objSchemeTO.getNext_Chittal_No());  //AJITH
        this.setRdoMultipleMembers(CommonUtil.convertObjToStr(objSchemeTO.getMultipleMember()));
        this.setTxtNoofCoChittals(CommonUtil.convertObjToInt(objSchemeTO.getNoOfChittals())); //AJITH
        this.setTxtNoofCoInstallments(CommonUtil.convertObjToInt(objSchemeTO.getCoNoOfInstallments()));  //AJITH
        this.setTxtMaxNoofMemberCoChittals(CommonUtil.convertObjToInt(objSchemeTO.getMaxNoOfMembers())); //AJITH
        this.setTxtClosureRate(CommonUtil.convertObjToStr(objSchemeTO.getClosureRate()));
        this.setTxtSancNo(CommonUtil.convertObjToStr(objSchemeTO.getSancNo()));
        this.setTdtSancDt(CommonUtil.convertObjToStr(objSchemeTO.getSancDt()));
        this.setTxaRemarks(CommonUtil.convertObjToStr(objSchemeTO.getRemarks()));
        this.setAutionTime(CommonUtil.convertObjToStr(objSchemeTO.getAutionTime()));
        this.setMdsType(CommonUtil.convertObjToStr(objSchemeTO.getMdsType()));
        this.setChkIsASpecialScheme(CommonUtil.convertObjToStr(objSchemeTO.getIsSpecialScheme()));
        this.setChkBonusPrint(CommonUtil.convertObjToStr(objSchemeTO.getChkBonusPrint()));
        this.setChkCreditStampAdvance(CommonUtil.convertObjToStr(objSchemeTO.getCreditStampAdvance()));
        this.setChkBankSettlement(CommonUtil.convertObjToStr(objSchemeTO.getBankSettlement())); // Added by nithya on 11-08-2017 for 7145
        this.setCboGroupNo(CommonUtil.convertObjToStr(objSchemeTO.getGroupNo()));
        this.setTxtSchemeGraceperiod(CommonUtil.convertObjToInt(objSchemeTO.getSchemeGracePeriod())); //AJITH  // Added by nithya on 21-04-2020 for KD-1611
        this.setChkDiscountFirstInst(CommonUtil.convertObjToStr(objSchemeTO.getDiscountFirstInst()));
        this.setTxtDiscountAmt(CommonUtil.convertObjToDouble(objSchemeTO.getDiscountAmt()));  //AJITH
        setChanged();
        notifyObservers();
    }
    
    private void populateAccHeadData(MDSProductAcctHeadTO mdsProductAcctHeadTO) throws Exception{
        setTxtReceiptHead(mdsProductAcctHeadTO.getReceiptHead());
        setTxtPaymentHead(mdsProductAcctHeadTO.getPaymentHead());
        setTxtSuspenseHead(mdsProductAcctHeadTO.getSuspenseHead());
        setTxtSuspenseAccNo(mdsProductAcctHeadTO.getSuspenseAccNo());
        setCboSuspenseProdID(CommonUtil.convertObjToStr(getCbmSuspenseProdID().getDataForKey(CommonUtil.convertObjToStr(mdsProductAcctHeadTO.getSuspenseProdId()))));
        setRdoGLorAccNo(mdsProductAcctHeadTO.getSuspenseGLorAccount());
        setTxtMiscellaneousHead(mdsProductAcctHeadTO.getMiscellaneousHead());
        setTxtCommisionHead(mdsProductAcctHeadTO.getCommisionHead());
        setTxtBonusPayableHead(mdsProductAcctHeadTO.getBonusPayableHead());
        setTxtBonusReceivableHead(mdsProductAcctHeadTO.getBonusReceivableHead());
        setTxtPenalHead(mdsProductAcctHeadTO.getPenalInterestHead());
        setTxtThalayalReceiptsHead(mdsProductAcctHeadTO.getThalayalRepPayHead());
        setTxtThalayalBonusHead(mdsProductAcctHeadTO.getThalayalBonusHead());
        setTxtMunnalBonusHead(mdsProductAcctHeadTO.getMunnalBonusHead());
        setTxtMunnalReceiptsHead(mdsProductAcctHeadTO.getMunnalRepPayHead());
        setTxtBankingHead(mdsProductAcctHeadTO.getBankingRepPayHead());
        setTxtNoticeChargesHead(mdsProductAcctHeadTO.getNoticeChargesHead());
        setTxtChargeHead(mdsProductAcctHeadTO.getChargeHead());
        setTxtStampAdvanceHead(mdsProductAcctHeadTO.getStampRecoveryHead());
        setTxtARCCostHead(mdsProductAcctHeadTO.getArcCostHead());
        setTxtARCExpenseHead(mdsProductAcctHeadTO.getArcExpenseHead());
        setTxtEACostHead(mdsProductAcctHeadTO.getEaCostHead());
        setTxtEAExpenseHead(mdsProductAcctHeadTO.getEaExpenseHead());
        setTxtEPCostHead(mdsProductAcctHeadTO.getEpCostHead());
        setTxtEPExpenseHead(mdsProductAcctHeadTO.getEpExpenseHead());
        setTxtPostageHead(mdsProductAcctHeadTO.getPostageHead());
        setTxtCaseExpensesHead(mdsProductAcctHeadTO.getCaseExpenseHead());
        setTxtDiscountHead(mdsProductAcctHeadTO.getDiscountHead());
        setTxtMDSPayableHead(mdsProductAcctHeadTO.getMdsPayableHead());
        setTxtMDSReceivableHead(mdsProductAcctHeadTO.getMdsReceivableHead());
        setTxtSundryReceiptHead(mdsProductAcctHeadTO.getSundryHead());
        setTxtSundryPaymentHead(mdsProductAcctHeadTO.getSundryPaymentHead());
        settxtForFeitedPaymentHead(mdsProductAcctHeadTO.getForFeitedHead());
        setTxtPostageAdvHead(mdsProductAcctHeadTO.getPostageAdvHead());    
        setTxtLegalChrgHead(mdsProductAcctHeadTO.getLegalChrgActHead());// Added by nithya on 02-08-2017
        setTxtPartPayBonusRecoveryHead(mdsProductAcctHeadTO.getPartPayBonusRecoveryHead());// Done by nithya on 27-08-2019 for KD 575 - MDS Money Payment With Bonus Recovery Head Option.
        setTxtOtherChrgeAcHd(mdsProductAcctHeadTO.getOtherChargeHead());
        setChanged();
        notifyObservers();
    }
    
    public ArrayList setLoanProductChargesTab() {
        log.info("In setLoanProductChargesTab...");
        ArrayList data=tblNoticeCharge.getDataArrayList();
        ArrayList charges = new ArrayList();
        chargeTabRow = new ArrayList();
        final int dataSize = data.size();
        for (int i=0;i<dataSize;i++){
            try{
                objLoanProductChargesTabTO = new LoanProductChargesTabTO();
                objLoanProductChargesTabTO.setProdId(CommonUtil.convertObjToStr(getTxtSchemeName()));
                charges = (ArrayList)data.get(i);
                objLoanProductChargesTabTO.setNoticeType(CommonUtil.convertObjToStr(charges.get(0)));
                objLoanProductChargesTabTO.setNoticeChargeAmt(CommonUtil.convertObjToDouble(charges.get(1)));
                objLoanProductChargesTabTO.setPostageAmt(CommonUtil.convertObjToDouble(charges.get(2)));
                chargeTabRow.add(objLoanProductChargesTabTO);
            }catch(Exception e){
                log.info("Error in setLoanProductChargesTab()");
                parseException.logException(e,true);
                e.printStackTrace();
            }
        }
        return chargeTabRow;
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
    
    public void createHead(HashMap acHdMap) throws Exception{
         HashMap rerurnMap = proxy.execute(acHdMap,map);
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null){
            data.put("SchemeDetailsTOData",setSchemeTOData());
            data.put("mdsProductAcctHeadTO",setMDSProductAcctHeadTO());
            data.put("SMSParameterTo",setSMSParameterTO());
            System.out.println("scheduleMap#%#%#%"+scheduleMap);
            if(getActionType()==ClientConstants.ACTIONTYPE_NEW){
                data.put("ScheduleTableDetails",setCopiedInstallments());
            }else{
                data.put("TOTAL_MEMBERS", getTxtNoofMemberPer());
                data.put("ScheduleTableDetails",scheduleMap);
            }
            data.put("SCHEME_DIVISION",getTxtNoofDivision());
            data.put("SCHEME_NAME",getTxtSchemeName());
            if(deletedScheduleMap!=null && deletedScheduleMap.size()>0 ){
                data.put("deletedFixedAssetsDescription",deletedScheduleMap);
            }
            final ArrayList arrayLoanProductChargesTabTO = setLoanProductChargesTab();
            if(arrayLoanProductChargesTabTO!=null && arrayLoanProductChargesTabTO.size()>0 ){
                data.put("LoanProductChargesTabTO", arrayLoanProductChargesTabTO);
            }
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        System.out.println("data in FixedAssets OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }
    
    private void setLoanProductChargesTabTO(ArrayList arrayLoanProductChargesTabTO) throws Exception{
        log.info("In setLoanProductChargesTabTO...");
        System.out.println("setLoanproductcharge###"+arrayLoanProductChargesTabTO);
        LoanProductChargesTabTO objLoanProductChargesTabTO= new LoanProductChargesTabTO();
        NoticeTypeTO=new LinkedHashMap();
        entireNoticeTypeRow=new ArrayList();
        for (int i=0, j= arrayLoanProductChargesTabTO.size();i<j;i++){
            chargeTabRow = new ArrayList();
            objLoanProductChargesTabTO = (LoanProductChargesTabTO)arrayLoanProductChargesTabTO.get(i);
            NoticeTypeTO.put(String.valueOf(notice_Charge_No),objLoanProductChargesTabTO);
            entireNoticeTypeRow.add(setColumnValueNoticeCharge(notice_Charge_No, objLoanProductChargesTabTO));
            notice_Charge_No++;            
            objLoanProductChargesTabTO = (LoanProductChargesTabTO)arrayLoanProductChargesTabTO.get(i);
            chargeTabRow.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeType()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objLoanProductChargesTabTO.getNoticeChargeAmt()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objLoanProductChargesTabTO.getPostageAmt()));
            tblNoticeCharge.insertRow(i,chargeTabRow);
        }
    }
    
    /** To populate data into the screen */
    public MDSProductSchemeTO setSchemeTOData() {
        
        final MDSProductSchemeTO objSchemeTO = new MDSProductSchemeTO();
        try{
            //rish
             objSchemeTO.setIsRevPostAdv(getIsRevPostAdv());
            objSchemeTO.setProdId(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
            objSchemeTO.setSchemeName(getTxtSchemeName());
            objSchemeTO.setSchemeDesc(getTxtSchemeDesc());           
            objSchemeTO.setResolutionNo(getTxtResolutionNo());
            objSchemeTO.setResolutionDt(DateUtil.getDateMMDDYYYY(getTdtlResolutionDate()));
            objSchemeTO.setNoOfDivisions(getTxtNoofDivision());
            objSchemeTO.setNoOfAuctions(getTxtNoofAuctions());
            objSchemeTO.setNoOfDraws(getTxtNoofDraws());
            objSchemeTO.setNoOfMemberPerDivision(getTxtNoofMemberPer());
            objSchemeTO.setTotalNoOfMembers(getTxtNoofMemberScheme());
            objSchemeTO.setInstallmentAmount(getTxtInstAmt());
            objSchemeTO.setNoOfInstallments(getTxtNoofInst());
            objSchemeTO.setTotalAmountDivision(getTxtTotAmtPerDivision());
            objSchemeTO.setTotalAmountScheme(getTxtTotAmtUnderScheme());
            objSchemeTO.setInstallmentFrequency(CommonUtil.convertObjToStr(getCbmInstFreq().getKeyForSelected()));
            objSchemeTO.setSchemeStartDt(DateUtil.getDateMMDDYYYY(getTdtSchemeStDt()));
            objSchemeTO.setSchemeEndDt(DateUtil.getDateMMDDYYYY(getTdtSchemeEndDt()));
            objSchemeTO.setInstallmentDay(CommonUtil.convertObjToInt(getCbmInstallmentDay().getKeyForSelected()));  //AJITH
            objSchemeTO.setDrawAuctDay(CommonUtil.convertObjToInt(getCbmAuctionDay().getKeyForSelected()));  //AJITH
            objSchemeTO.setThalayal(getRdoThalayalAllowed());
            objSchemeTO.setMunnal(getRdoMunnalAllowed());
            objSchemeTO.setPredefinitionInstallment(getRdoPredefinitonInst());
            objSchemeTO.setAllDivision(getRdoApplicableDivision());
            objSchemeTO.setDivision1(getTxtApplicable1());
            objSchemeTO.setDivision2(getTxtApplicable2());
            objSchemeTO.setDivision3(getTxtApplicable3());
            objSchemeTO.setDivision4(getTxtApplicable4());
            objSchemeTO.setAfterPayment(getRdoPaymentDone());
            objSchemeTO.setInstallments(getTxtInstallments());
            objSchemeTO.setDay(getDay());
            objSchemeTO.setStatus(getAction());
            objSchemeTO.setStatusBy(TrueTransactMain.USER_ID);
            objSchemeTO.setStatusDt((Date)currDt.clone());
            objSchemeTO.setProdDesc(getProd_Desc());
            objSchemeTO.setChittal_No_Pattern(getTxtChittalNumberPattern());
            objSchemeTO.setSuffix_No(getTxtSuffix());
            objSchemeTO.setNext_Chittal_No(getTxtNextChittalNumber());
            objSchemeTO.setMultipleMember(getRdoMultipleMembers());
            objSchemeTO.setNoOfChittals(CommonUtil.convertObjToDouble(getTxtNoofCoChittals()));
            objSchemeTO.setCoNoOfInstallments(getTxtNoofCoInstallments());
            objSchemeTO.setMaxNoOfMembers(getTxtMaxNoofMemberCoChittals());
            objSchemeTO.setClosureRate(getTxtClosureRate());
            objSchemeTO.setSancNo(getTxtSancNo());
            objSchemeTO.setSancDt(DateUtil.getDateMMDDYYYY(getTdtSancDt()));
            objSchemeTO.setRemarks(getTxaRemarks());
            objSchemeTO.setAutionTime(getAutionTime());
            objSchemeTO.setMdsType(getMdsType());
            objSchemeTO.setBranchCode(ProxyParameters.BRANCH_ID);
            objSchemeTO.setIsSpecialScheme(getChkIsASpecialScheme());
            objSchemeTO.setChkBonusPrint(getChkBonusPrint());
            objSchemeTO.setCreditStampAdvance(getChkCreditStampAdvance());
            objSchemeTO.setBankSettlement(getChkBankSettlement()); // Added by nithya on 11-08-2017 for 7145 
            objSchemeTO.setGroupNo(getCboGroupNo());
            objSchemeTO.setSchemeGracePeriod(getTxtSchemeGraceperiod()); //AJITH  // Added by nithya on 21-04-2020 for KD-1611
            objSchemeTO.setDiscountFirstInst(getChkDiscountFirstInst());
            objSchemeTO.setDiscountAmt(getTxtDiscountAmt()); //AJITH
        }catch(Exception e){
            log.info("Error In setMDSProductSchemeTOData()");
            e.printStackTrace();
        }
        return objSchemeTO;
    }
    
    
    /** To populate data into the screen */
    public MDSProductAcctHeadTO setMDSProductAcctHeadTO() {
        
        final MDSProductAcctHeadTO mdsProductAcctHeadTO = new MDSProductAcctHeadTO();
        try{
            //rish            
            mdsProductAcctHeadTO.setPostageAdvHead(getTxtPostageAdvHead()); 
            mdsProductAcctHeadTO.setProdId(CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
            mdsProductAcctHeadTO.setSchemeName(getTxtSchemeName());
            mdsProductAcctHeadTO.setReceiptHead(getTxtReceiptHead());
            mdsProductAcctHeadTO.setPaymentHead(getTxtPaymentHead());
            mdsProductAcctHeadTO.setSuspenseHead(getTxtSuspenseHead());
            mdsProductAcctHeadTO.setSuspenseAccNo(getTxtSuspenseAccNo());
            mdsProductAcctHeadTO.setSuspenseProdId(CommonUtil.convertObjToStr(getCbmSuspenseProdID().getKeyForSelected()));
            mdsProductAcctHeadTO.setSuspenseGLorAccount(getRdoGLorAccNo());
            mdsProductAcctHeadTO.setMiscellaneousHead(getTxtMiscellaneousHead());
            mdsProductAcctHeadTO.setCommisionHead(getTxtCommisionHead());
            mdsProductAcctHeadTO.setBonusPayableHead(getTxtBonusPayableHead());
            mdsProductAcctHeadTO.setBonusReceivableHead(getTxtBonusReceivableHead());
            mdsProductAcctHeadTO.setPenalInterestHead(getTxtPenalHead());
            mdsProductAcctHeadTO.setThalayalRepPayHead(getTxtThalayalReceiptsHead());
            mdsProductAcctHeadTO.setThalayalBonusHead(getTxtThalayalBonusHead());
            mdsProductAcctHeadTO.setMunnalBonusHead(getTxtMunnalBonusHead());
            mdsProductAcctHeadTO.setMunnalRepPayHead(getTxtMunnalReceiptsHead());
            mdsProductAcctHeadTO.setBankingRepPayHead(getTxtBankingHead());
            mdsProductAcctHeadTO.setNoticeChargesHead(getTxtNoticeChargesHead());
            mdsProductAcctHeadTO.setChargeHead(getTxtChargeHead());
            mdsProductAcctHeadTO.setStampRecoveryHead(getTxtStampAdvanceHead());
            mdsProductAcctHeadTO.setArcCostHead(getTxtARCCostHead());
            mdsProductAcctHeadTO.setArcExpenseHead(getTxtARCExpenseHead());
            mdsProductAcctHeadTO.setEaCostHead(getTxtEACostHead());
            mdsProductAcctHeadTO.setEaExpenseHead(getTxtEAExpenseHead());
            mdsProductAcctHeadTO.setEpCostHead(getTxtEPCostHead());
            mdsProductAcctHeadTO.setEpExpenseHead(getTxtEPExpenseHead());
            mdsProductAcctHeadTO.setPostageHead(getTxtPostageHead());
            mdsProductAcctHeadTO.setCaseExpenseHead(getTxtCaseExpensesHead());
            mdsProductAcctHeadTO.setDiscountHead(getTxtDiscountHead());
            mdsProductAcctHeadTO.setMdsPayableHead(getTxtMDSPayableHead());
            mdsProductAcctHeadTO.setMdsReceivableHead(getTxtMDSReceivableHead());
            mdsProductAcctHeadTO.setSundryHead(getTxtSundryReceiptHead());
            mdsProductAcctHeadTO.setSundryPaymentHead(getTxtSundryPaymentHead());
            mdsProductAcctHeadTO.setPostageAdvHead(getTxtPostageAdvHead());
            mdsProductAcctHeadTO.setForFeitedHead(gettxtForFeitedPaymentHead());
            mdsProductAcctHeadTO.setLegalChrgActHead(getTxtLegalChrgHead()); // Added by nithya on 02-08-2017
            mdsProductAcctHeadTO.setPartPayBonusRecoveryHead(getTxtPartPayBonusRecoveryHead()); // Done by nithya on 27-08-2019 for KD 575 - MDS Money Payment With Bonus Recovery Head Option.
            mdsProductAcctHeadTO.setOtherChargeHead(getTxtOtherChrgeAcHd());
        }catch(Exception e){
            log.info("Error In setMDSProductAcctHeadTOData()");
            e.printStackTrace();
        }
        return mdsProductAcctHeadTO;
    }
    
    
   public SMSParameterTO setSMSParameterTO() {
        
        final SMSParameterTO smsParameterTO = new SMSParameterTO();
        try{
            smsParameterTO.setProdType("MDS");
            smsParameterTO.setProdId(getTxtSchemeName());
            smsParameterTO.setDrCash("Y");
            smsParameterTO.setCrCash("Y");
            smsParameterTO.setDrTransfer("Y");
            smsParameterTO.setCrTransfer("Y");
            smsParameterTO.setDrClearing("Y");
            smsParameterTO.setCrClearing("Y");
            smsParameterTO.setDrCashAmt(0.0);
            smsParameterTO.setCrCashAmt(0.0);
            smsParameterTO.setDrTransferAmt(0.0);
            smsParameterTO.setCrTransferAmt(0.0);
            smsParameterTO.setCrClearingAmt(0.0);
            smsParameterTO.setDrClearingAmt(0.0);
            smsParameterTO.setCreatedDt((Date)currDt.clone());
            smsParameterTO.setStatusBy(TrueTransactMain.USER_ID);
            smsParameterTO.setCreatedBy(TrueTransactMain.USER_ID);
            smsParameterTO.setStatus(CommonConstants.STATUS_CREATED);
            smsParameterTO.setSmsAlert(getChkSMSAlert());            
        }catch(Exception e){
            log.info("Error In SMS PArameter creation()"+e);
            e.printStackTrace();
        }
        return smsParameterTO;
    }
    
    
     public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try{
            final HashMap data = new HashMap();
            data.put("ACCT_HD",accountHead.getText());
            data.put(CommonConstants.MAP_NAME , mapName);
            HashMap proxyResultMap = proxy.execute(data,map);
        }catch(Exception e){
            System.out.println("Error in verifyAcctHead");
            accountHead.setText("");
            parseException.logException(e,true);
        }
    }
    
    
    public void ttNotifyObservers(){
        notifyObservers();
    }

    public int getTxtNoofDivision() {
        return txtNoofDivision;
    }

    public void setTxtNoofDivision(int txtNoofDivision) {
        this.txtNoofDivision = txtNoofDivision;
        setChanged();
    }

    public int getTxtNoofMemberPer() {
        return txtNoofMemberPer;
    }

    public void setTxtNoofMemberPer(int txtNoofMemberPer) {
        this.txtNoofMemberPer = txtNoofMemberPer;
        setChanged();
    }

    public int getTxtNoofAuctions() {
        return txtNoofAuctions;
    }

    public void setTxtNoofAuctions(int txtNoofAuctions) {
        this.txtNoofAuctions = txtNoofAuctions;
        setChanged();
    }

    public int getTxtNoofInst() {
        return txtNoofInst;
    }

    public void setTxtNoofInst(int txtNoofInst) {
        this.txtNoofInst = txtNoofInst;
        setChanged();
    }

    public int getTxtNoofDraws() {
        return txtNoofDraws;
    }

    public void setTxtNoofDraws(int txtNoofDraws) {
        this.txtNoofDraws = txtNoofDraws;
        setChanged();
    }

    public double getTxtInstAmt() {
        return txtInstAmt;
    }

    public void setTxtInstAmt(double txtInstAmt) {
        this.txtInstAmt = txtInstAmt;
        setChanged();
    }

    public int getTxtNoofMemberScheme() {
        return txtNoofMemberScheme;
    }

    public void setTxtNoofMemberScheme(int txtNoofMemberScheme) {
        this.txtNoofMemberScheme = txtNoofMemberScheme;
        setChanged();
    }

    public double getTxtTotAmtPerDivision() {
        return txtTotAmtPerDivision;
    }

    public void setTxtTotAmtPerDivision(double txtTotAmtPerDivision) {
        this.txtTotAmtPerDivision = txtTotAmtPerDivision;
        setChanged();
    }

    // Setter method for tdtSchemeStDt
    void setTdtSchemeStDt(String tdtSchemeStDt){
        this.tdtSchemeStDt = tdtSchemeStDt;
        setChanged();
    }
    // Getter method for tdtSchemeStDt
    String getTdtSchemeStDt(){
        return this.tdtSchemeStDt;
    }
    
    // Setter method for tdtSchemeEndDt
    void setTdtSchemeEndDt(String tdtSchemeEndDt){
        this.tdtSchemeEndDt = tdtSchemeEndDt;
        setChanged();
    }
    // Getter method for tdtSchemeEndDt
    String getTdtSchemeEndDt(){
        return this.tdtSchemeEndDt;
    }
    
    // Setter method for cboInstFreq
    void setCboInstFreq(String cboInstFreq){
        this.cboInstFreq = cboInstFreq;
        setChanged();
    }
    // Getter method for cboInstFreq
    String getCboInstFreq(){
        return this.cboInstFreq;
    }
    
    // Setter method for txtAcctHead
    void setTxtAcctHead(String txtAcctHead){
        this.txtAcctHead = txtAcctHead;
        setChanged();
    }
    // Getter method for txtAcctHead
    String getTxtAcctHead(){
        return this.txtAcctHead;
    }
    
    // Setter method for cboProductId
    void setCboProductId(String cboProductId){
        this.cboProductId = cboProductId;
        setChanged();
    }
    // Getter method for cboProductId
    String getCboProductId(){
        return this.cboProductId;
    }
    
    // Setter method for txtResolutionNo
    void setTxtResolutionNo(String txtResolutionNo){
        this.txtResolutionNo = txtResolutionNo;
        setChanged();
    }
    // Getter method for txtResolutionNo
    String getTxtResolutionNo(){
        return this.txtResolutionNo;
    }
    
    // Setter method for tdtlResolutionDate
    void setTdtlResolutionDate(String tdtlResolutionDate){
        this.tdtlResolutionDate = tdtlResolutionDate;
        setChanged();
    }
    // Getter method for tdtlResolutionDate
    String getTdtlResolutionDate(){
        return this.tdtlResolutionDate;
    }
    
    
    
    // Setter method for txtSchemeName
    void setTxtSchemeName(String txtSchemeName){
        this.txtSchemeName = txtSchemeName;
        setChanged();
    }
    // Getter method for txtSchemeName
    String getTxtSchemeName(){
        return this.txtSchemeName;
    }

    public double getTxtTotAmtUnderScheme() {
        return txtTotAmtUnderScheme;
    }

    public void setTxtTotAmtUnderScheme(double txtTotAmtUnderScheme) {
        this.txtTotAmtUnderScheme = txtTotAmtUnderScheme;
        setChanged();
    }

    // Setter method for txtApplicable2
    void setTxtApplicable2(String txtApplicable2){
        this.txtApplicable2 = txtApplicable2;
        setChanged();
    }
    // Getter method for txtApplicable2
    String getTxtApplicable2(){
        return this.txtApplicable2;
    }
    
    // Setter method for txtApplicable4
    void setTxtApplicable4(String txtApplicable4){
        this.txtApplicable4 = txtApplicable4;
        setChanged();
    }
    // Getter method for txtApplicable4
    String getTxtApplicable4(){
        return this.txtApplicable4;
    }
    
    // Setter method for txtApplicable1
    void setTxtApplicable1(String txtApplicable1){
        this.txtApplicable1 = txtApplicable1;
        setChanged();
    }
    // Getter method for txtApplicable1
    String getTxtApplicable1(){
        return this.txtApplicable1;
    }
    
    // Setter method for txtApplicable3
    void setTxtApplicable3(String txtApplicable3){
        this.txtApplicable3 = txtApplicable3;
        setChanged();
    }
    // Getter method for txtApplicable3
    String getTxtApplicable3(){
        return this.txtApplicable3;
    }
    
    
    // Setter method for tdtDate
    void setTdtDate(String tdtDate){
        this.tdtDate = tdtDate;
        setChanged();
    }
    // Getter method for tdtDate
    String getTdtDate(){
        return this.tdtDate;
    }

    public int getTxtInstallments() {
        return txtInstallments;
    }

    public void setTxtInstallments(int txtInstallments) {
        this.txtInstallments = txtInstallments;
    }

    public int getCboInstallmentDay() {
        return cboInstallmentDay;
    }

    public void setCboInstallmentDay(int cboInstallmentDay) {
        this.cboInstallmentDay = cboInstallmentDay;
        setChanged();
    }

    public int getCboAuctionDay() {
        return cboAuctionDay;
    }

    public void setCboAuctionDay(int cboAuctionDay) {
        this.cboAuctionDay = cboAuctionDay;
        setChanged();
    }

    // Setter method for tdtInstallmentDt
    void setTdtInstallmentDt(String tdtInstallmentDt){
        this.tdtInstallmentDt = tdtInstallmentDt;
        setChanged();
    }
    // Getter method for tdtInstallmentDt
    String getTdtInstallmentDt(){
        return this.tdtInstallmentDt;
    }

    public double getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(double txtAmount) {
        this.txtAmount = txtAmount;
        setChanged();
    }

    public double getTxtBonus() {
        return txtBonus;
    }

    public void setTxtBonus(double txtBonus) {
        this.txtBonus = txtBonus;
        setChanged();
    }

    public double getTxtPaymentAmount() {
        return txtPaymentAmount;
    }

    public void setTxtPaymentAmount(double txtPaymentAmount) {
        this.txtPaymentAmount = txtPaymentAmount;
        setChanged();
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
    
    /**
     * Getter for property cbmInstallmentDay.
     * @return Value of property cbmInstallmentDay.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInstallmentDay() {
        return cbmInstallmentDay;
    }
    
    /**
     * Setter for property cbmInstallmentDay.
     * @param cbmInstallmentDay New value of property cbmInstallmentDay.
     */
    public void setCbmInstallmentDay(com.see.truetransact.clientutil.ComboBoxModel cbmInstallmentDay) {
        this.cbmInstallmentDay = cbmInstallmentDay;
    }
    
    /**
     * Getter for property cbmAuctionDay.
     * @return Value of property cbmAuctionDay.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAuctionDay() {
        return cbmAuctionDay;
    }
    
    /**
     * Setter for property cbmAuctionDay.
     * @param cbmAuctionDay New value of property cbmAuctionDay.
     */
    public void setCbmAuctionDay(com.see.truetransact.clientutil.ComboBoxModel cbmAuctionDay) {
        this.cbmAuctionDay = cbmAuctionDay;
    }
    
    /**
     * Getter for property cbmInstFreq.
     * @return Value of property cbmInstFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInstFreq() {
        return cbmInstFreq;
    }
    
    /**
     * Setter for property cbmInstFreq.
     * @param cbmInstFreq New value of property cbmInstFreq.
     */
    public void setCbmInstFreq(com.see.truetransact.clientutil.ComboBoxModel cbmInstFreq) {
        this.cbmInstFreq = cbmInstFreq;
    }
    
    /**
     * Getter for property cbmProductId.
     * @return Value of property cbmProductId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }
    
    /**
     * Setter for property cbmProductId.
     * @param cbmProductId New value of property cbmProductId.
     */
    public void setCbmProductId(com.see.truetransact.clientutil.ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
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
     * Getter for property tblScheduleDetails.
     * @return Value of property tblScheduleDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblScheduleDetails() {
        return tblScheduleDetails;
    }
    
    /**
     * Setter for property tblScheduleDetails.
     * @param tblScheduleDetails New value of property tblScheduleDetails.
     */
    public void setTblScheduleDetails(com.see.truetransact.clientutil.EnhancedTableModel tblScheduleDetails) {
        this.tblScheduleDetails = tblScheduleDetails;
    }

    public int getTxtSlNo() {
        return txtSlNo;
    }

    public void setTxtSlNo(int txtSlNo) {
        this.txtSlNo = txtSlNo;
    }

    /**
     * Getter for property rdoMunnalAllowed.
     * @return Value of property rdoMunnalAllowed.
     */
    public java.lang.String getRdoMunnalAllowed() {
        return rdoMunnalAllowed;
    }
    
    /**
     * Setter for property rdoMunnalAllowed.
     * @param rdoMunnalAllowed New value of property rdoMunnalAllowed.
     */
    public void setRdoMunnalAllowed(java.lang.String rdoMunnalAllowed) {
        this.rdoMunnalAllowed = rdoMunnalAllowed;
    }
    
    /**
     * Getter for property rdoApplicableDivision.
     * @return Value of property rdoApplicableDivision.
     */
    public java.lang.String getRdoApplicableDivision() {
        return rdoApplicableDivision;
    }
    
    /**
     * Setter for property rdoApplicableDivision.
     * @param rdoApplicableDivision New value of property rdoApplicableDivision.
     */
    public void setRdoApplicableDivision(java.lang.String rdoApplicableDivision) {
        this.rdoApplicableDivision = rdoApplicableDivision;
    }
    
    /**
     * Getter for property rdoThalayalAllowed.
     * @return Value of property rdoThalayalAllowed.
     */
    public java.lang.String getRdoThalayalAllowed() {
        return rdoThalayalAllowed;
    }
    
    /**
     * Setter for property rdoThalayalAllowed.
     * @param rdoThalayalAllowed New value of property rdoThalayalAllowed.
     */
    public void setRdoThalayalAllowed(java.lang.String rdoThalayalAllowed) {
        this.rdoThalayalAllowed = rdoThalayalAllowed;
    }
    
    /**
     * Getter for property rdoPaymentDone.
     * @return Value of property rdoPaymentDone.
     */
    public java.lang.String getRdoPaymentDone() {
        return rdoPaymentDone;
    }
    
    /**
     * Setter for property rdoPaymentDone.
     * @param rdoPaymentDone New value of property rdoPaymentDone.
     */
    public void setRdoPaymentDone(java.lang.String rdoPaymentDone) {
        this.rdoPaymentDone = rdoPaymentDone;
    }
    
    /**
     * Getter for property rdoPredefinitonInst.
     * @return Value of property rdoPredefinitonInst.
     */
    public java.lang.String getRdoPredefinitonInst() {
        return rdoPredefinitonInst;
    }
    
    /**
     * Setter for property rdoPredefinitonInst.
     * @param rdoPredefinitonInst New value of property rdoPredefinitonInst.
     */
    public void setRdoPredefinitonInst(java.lang.String rdoPredefinitonInst) {
        this.rdoPredefinitonInst = rdoPredefinitonInst;
    }
    
    /**
     * Getter for property rdoPredefinitonInst.
     * @return Value of property rdoPredefinitonInst.
     */
    public java.lang.String getRdoMultipleMembers() {
        return rdoMultipleMembers;
    }
    
    /**
     * Setter for property rdoMultipleMembers.
     * @param rdoMultipleMembers New value of property rdoMultipleMembers.
     */
    public void setRdoMultipleMembers(java.lang.String rdoMultipleMembers) {
        this.rdoMultipleMembers = rdoMultipleMembers;
    }
    
    /**
     * Getter for property prod_Desc.
     * @return Value of property prod_Desc.
     */
    public java.lang.String getProd_Desc() {
        return prod_Desc;
    }
    
    /**
     * Setter for property prod_Desc.
     * @param prod_Desc New value of property prod_Desc.
     */
    public void setProd_Desc(java.lang.String prod_Desc) {
        this.prod_Desc = prod_Desc;
    }
    
    /**
     * Getter for property txtChittalNumberPattern.
     * @return Value of property txtChittalNumberPattern.
     */
    public java.lang.String getTxtChittalNumberPattern() {
        return txtChittalNumberPattern;
    }
    
    /**
     * Setter for property txtChittalNumberPattern.
     * @param txtChittalNumberPattern New value of property txtChittalNumberPattern.
     */
    public void setTxtChittalNumberPattern(java.lang.String txtChittalNumberPattern) {
        this.txtChittalNumberPattern = txtChittalNumberPattern;
    }

    public int getTxtSuffix() {
        return txtSuffix;
    }

    public void setTxtSuffix(int txtSuffix) {
        this.txtSuffix = txtSuffix;
    }

    public int getTxtNextChittalNumber() {
        return txtNextChittalNumber;
    }

    public void setTxtNextChittalNumber(int txtNextChittalNumber) {
        this.txtNextChittalNumber = txtNextChittalNumber;
    }

    /**
     * Getter for property txtReceiptHead.
     * @return Value of property txtReceiptHead.
     */
    public java.lang.String getTxtReceiptHead() {
        return txtReceiptHead;
    }
    
    /**
     * Setter for property txtReceiptHead.
     * @param txtReceiptHead New value of property txtReceiptHead.
     */
    public void setTxtReceiptHead(java.lang.String txtReceiptHead) {
        this.txtReceiptHead = txtReceiptHead;
    }
    
    /**
     * Getter for property txtPaymentHead.
     * @return Value of property txtPaymentHead.
     */
    public java.lang.String getTxtPaymentHead() {
        return txtPaymentHead;
    }
    
    /**
     * Setter for property txtPaymentHead.
     * @param txtPaymentHead New value of property txtPaymentHead.
     */
    public void setTxtPaymentHead(java.lang.String txtPaymentHead) {
        this.txtPaymentHead = txtPaymentHead;
    }
    
    /**
     * Getter for property txtSuspenseHead.
     * @return Value of property txtSuspenseHead.
     */
    public java.lang.String getTxtSuspenseHead() {
        return txtSuspenseHead;
    }
    
    /**
     * Setter for property txtSuspenseHead.
     * @param txtSuspenseHead New value of property txtSuspenseHead.
     */
    public void setTxtSuspenseHead(java.lang.String txtSuspenseHead) {
        this.txtSuspenseHead = txtSuspenseHead;
    }
    
    /**
     * Getter for property txtMiscellaneousHead.
     * @return Value of property txtMiscellaneousHead.
     */
    public java.lang.String getTxtMiscellaneousHead() {
        return txtMiscellaneousHead;
    }
    
    /**
     * Setter for property txtMiscellaneousHead.
     * @param txtMiscellaneousHead New value of property txtMiscellaneousHead.
     */
    public void setTxtMiscellaneousHead(java.lang.String txtMiscellaneousHead) {
        this.txtMiscellaneousHead = txtMiscellaneousHead;
    }
    
    /**
     * Getter for property txtCommisionHead.
     * @return Value of property txtCommisionHead.
     */
    public java.lang.String getTxtCommisionHead() {
        return txtCommisionHead;
    }
    
    /**
     * Setter for property txtCommisionHead.
     * @param txtCommisionHead New value of property txtCommisionHead.
     */
    public void setTxtCommisionHead(java.lang.String txtCommisionHead) {
        this.txtCommisionHead = txtCommisionHead;
    }
    
    /**
     * Getter for property txtBonusPayableHead.
     * @return Value of property txtBonusPayableHead.
     */
    public java.lang.String getTxtBonusPayableHead() {
        return txtBonusPayableHead;
    }
    
    /**
     * Setter for property txtBonusPayableHead.
     * @param txtBonusPayableHead New value of property txtBonusPayableHead.
     */
    public void setTxtBonusPayableHead(java.lang.String txtBonusPayableHead) {
        this.txtBonusPayableHead = txtBonusPayableHead;
    }
    
    /**
     * Getter for property txtBonusReceivableHead.
     * @return Value of property txtBonusReceivableHead.
     */
    public java.lang.String getTxtBonusReceivableHead() {
        return txtBonusReceivableHead;
    }
    
    /**
     * Setter for property txtBonusReceivableHead.
     * @param txtBonusReceivableHead New value of property txtBonusReceivableHead.
     */
    public void setTxtBonusReceivableHead(java.lang.String txtBonusReceivableHead) {
        this.txtBonusReceivableHead = txtBonusReceivableHead;
    }
    
    /**
     * Getter for property txtPenalHead.
     * @return Value of property txtPenalHead.
     */
    public java.lang.String getTxtPenalHead() {
        return txtPenalHead;
    }
    
    /**
     * Setter for property txtPenalHead.
     * @param txtPenalHead New value of property txtPenalHead.
     */
    public void setTxtPenalHead(java.lang.String txtPenalHead) {
        this.txtPenalHead = txtPenalHead;
    }
    
    /**
     * Getter for property txtThalayalReceiptsHead.
     * @return Value of property txtThalayalReceiptsHead.
     */
    public java.lang.String getTxtThalayalReceiptsHead() {
        return txtThalayalReceiptsHead;
    }
    
    /**
     * Setter for property txtThalayalReceiptsHead.
     * @param txtThalayalReceiptsHead New value of property txtThalayalReceiptsHead.
     */
    public void setTxtThalayalReceiptsHead(java.lang.String txtThalayalReceiptsHead) {
        this.txtThalayalReceiptsHead = txtThalayalReceiptsHead;
    }
    
    /**
     * Getter for property txtThalayalBonusHead.
     * @return Value of property txtThalayalBonusHead.
     */
    public java.lang.String getTxtThalayalBonusHead() {
        return txtThalayalBonusHead;
    }
    
    /**
     * Setter for property txtThalayalBonusHead.
     * @param txtThalayalBonusHead New value of property txtThalayalBonusHead.
     */
    public void setTxtThalayalBonusHead(java.lang.String txtThalayalBonusHead) {
        this.txtThalayalBonusHead = txtThalayalBonusHead;
    }
    
    /**
     * Getter for property txtMunnalBonusHead.
     * @return Value of property txtMunnalBonusHead.
     */
    public java.lang.String getTxtMunnalBonusHead() {
        return txtMunnalBonusHead;
    }
    
    /**
     * Setter for property txtMunnalBonusHead.
     * @param txtMunnalBonusHead New value of property txtMunnalBonusHead.
     */
    public void setTxtMunnalBonusHead(java.lang.String txtMunnalBonusHead) {
        this.txtMunnalBonusHead = txtMunnalBonusHead;
    }
    
    /**
     * Getter for property txtMunnalReceiptsHead.
     * @return Value of property txtMunnalReceiptsHead.
     */
    public java.lang.String getTxtMunnalReceiptsHead() {
        return txtMunnalReceiptsHead;
    }
    
    /**
     * Setter for property txtMunnalReceiptsHead.
     * @param txtMunnalReceiptsHead New value of property txtMunnalReceiptsHead.
     */
    public void setTxtMunnalReceiptsHead(java.lang.String txtMunnalReceiptsHead) {
        this.txtMunnalReceiptsHead = txtMunnalReceiptsHead;
    }
    
    /**
     * Getter for property txtBankingHead.
     * @return Value of property txtBankingHead.
     */
    public java.lang.String getTxtBankingHead() {
        return txtBankingHead;
    }
    
    /**
     * Setter for property txtBankingHead.
     * @param txtBankingHead New value of property txtBankingHead.
     */
    public void setTxtBankingHead(java.lang.String txtBankingHead) {
        this.txtBankingHead = txtBankingHead;
    }
    
    /**
     * Getter for property txtNoticeChargesHead.
     * @return Value of property txtNoticeChargesHead.
     */
    public java.lang.String getTxtNoticeChargesHead() {
        return txtNoticeChargesHead;
    }
    
    /**
     * Setter for property txtNoticeChargesHead.
     * @param txtNoticeChargesHead New value of property txtNoticeChargesHead.
     */
    public void setTxtNoticeChargesHead(java.lang.String txtNoticeChargesHead) {
        this.txtNoticeChargesHead = txtNoticeChargesHead;
    }
    
    /**
     * Getter for property txtCaseExpensesHead.
     * @return Value of property txtCaseExpensesHead.
     */
    public java.lang.String getTxtCaseExpensesHead() {
        return txtCaseExpensesHead;
    }
    
    /**
     * Setter for property txtCaseExpensesHead.
     * @param txtCaseExpensesHead New value of property txtCaseExpensesHead.
     */
    public void setTxtCaseExpensesHead(java.lang.String txtCaseExpensesHead) {
        this.txtCaseExpensesHead = txtCaseExpensesHead;
    }
    
    /**
     * Getter for property txtSchemeDesc.
     * @return Value of property txtSchemeDesc.
     */
    public java.lang.String getTxtSchemeDesc() {
        return txtSchemeDesc;
    }
    
    /**
     * Setter for property txtSchemeDesc.
     * @param txtSchemeDesc New value of property txtSchemeDesc.
     */
    public void setTxtSchemeDesc(java.lang.String txtSchemeDesc) {
        this.txtSchemeDesc = txtSchemeDesc;
    }
    
    /**
     * Getter for property txtDiscountHead.
     * @return Value of property txtDiscountHead.
     */
    public java.lang.String getTxtDiscountHead() {
        return txtDiscountHead;
    }
    
    /**
     * Setter for property txtDiscountHead.
     * @param txtDiscountHead New value of property txtDiscountHead.
     */
    public void setTxtDiscountHead(java.lang.String txtDiscountHead) {
        this.txtDiscountHead = txtDiscountHead;
    }
    
    /**
     * Getter for property txtMDSPayableHead.
     * @return Value of property txtMDSPayableHead.
     */
    public java.lang.String getTxtMDSPayableHead() {
        return txtMDSPayableHead;
    }
    
    /**
     * Setter for property txtMDSPayableHead.
     * @param txtMDSPayableHead New value of property txtMDSPayableHead.
     */
    public void setTxtMDSPayableHead(java.lang.String txtMDSPayableHead) {
        this.txtMDSPayableHead = txtMDSPayableHead;
    }
    
    /**
     * Getter for property txtMDSReceivableHead.
     * @return Value of property txtMDSReceivableHead.
     */
    public java.lang.String getTxtMDSReceivableHead() {
        return txtMDSReceivableHead;
    }
    
    /**
     * Setter for property txtMDSReceivableHead.
     * @param txtMDSReceivableHead New value of property txtMDSReceivableHead.
     */
    public void setTxtMDSReceivableHead(java.lang.String txtMDSReceivableHead) {
        this.txtMDSReceivableHead = txtMDSReceivableHead;
    }
    
    /**
     * Getter for property txtSundryReceiptHead.
     * @return Value of property txtSundryReceiptHead.
     */
    public java.lang.String getTxtSundryReceiptHead() {
        return txtSundryReceiptHead;
    }
    
    /**
     * Setter for property txtSundryReceiptHead.
     * @param txtSundryReceiptHead New value of property txtSundryReceiptHead.
     */
    public void setTxtSundryReceiptHead(java.lang.String txtSundryReceiptHead) {
        this.txtSundryReceiptHead = txtSundryReceiptHead;
    }
    
    /**
     * Getter for property txtSundryPaymentHead.
     * @return Value of property txtSundryPaymentHead.
     */
    public java.lang.String getTxtSundryPaymentHead() {
        return txtSundryPaymentHead;
    }
    
    /**
     * Setter for property txtSundryPaymentHead.
     * @param txtSundryPaymentHead New value of property txtSundryPaymentHead.
     */
    public void setTxtSundryPaymentHead(java.lang.String txtSundryPaymentHead) {
        this.txtSundryPaymentHead = txtSundryPaymentHead;
    }

    public double getTxtNoofCoChittals() {
        return txtNoofCoChittals;
    }

    public void setTxtNoofCoChittals(double txtNoofCoChittals) {
        this.txtNoofCoChittals = txtNoofCoChittals;
    }

    public int getTxtNoofCoInstallments() {
        return txtNoofCoInstallments;
    }

    public void setTxtNoofCoInstallments(int txtNoofCoInstallments) {
        this.txtNoofCoInstallments = txtNoofCoInstallments;
    }

    public int getTxtMaxNoofMemberCoChittals() {
        return txtMaxNoofMemberCoChittals;
    }

    public void setTxtMaxNoofMemberCoChittals(int txtMaxNoofMemberCoChittals) {
        this.txtMaxNoofMemberCoChittals = txtMaxNoofMemberCoChittals;
    }

    /**
     * Getter for property cboNoticeType.
     * @return Value of property cboNoticeType.
     */
    public java.lang.String getCboNoticeType() {
        return cboNoticeType;
    }
    
    /**
     * Setter for property cboNoticeType.
     * @param cboNoticeType New value of property cboNoticeType.
     */
    public void setCboNoticeType(java.lang.String cboNoticeType) {
        this.cboNoticeType = cboNoticeType;
    }
    
    /**
     * Getter for property txtNoticeChargeAmt.
     * @return Value of property txtNoticeChargeAmt.
     */
    public java.lang.String getTxtNoticeChargeAmt() {
        return txtNoticeChargeAmt;
    }
    
    /**
     * Setter for property txtNoticeChargeAmt.
     * @param txtNoticeChargeAmt New value of property txtNoticeChargeAmt.
     */
    public void setTxtNoticeChargeAmt(java.lang.String txtNoticeChargeAmt) {
        this.txtNoticeChargeAmt = txtNoticeChargeAmt;
    }
    
    /**
     * Getter for property txtPostageChargeAmt.
     * @return Value of property txtPostageChargeAmt.
     */
    public java.lang.String getTxtPostageChargeAmt() {
        return txtPostageChargeAmt;
    }
    
    /**
     * Setter for property txtPostageChargeAmt.
     * @param txtPostageChargeAmt New value of property txtPostageChargeAmt.
     */
    public void setTxtPostageChargeAmt(java.lang.String txtPostageChargeAmt) {
        this.txtPostageChargeAmt = txtPostageChargeAmt;
    }
    
    /**
     * Getter for property cbmNoticeType.
     * @return Value of property cbmNoticeType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNoticeType() {
        return cbmNoticeType;
    }
    
    /**
     * Setter for property cbmNoticeType.
     * @param cbmNoticeType New value of property cbmNoticeType.
     */
    public void setCbmNoticeType(com.see.truetransact.clientutil.ComboBoxModel cbmNoticeType) {
        this.cbmNoticeType = cbmNoticeType;
    }
       
    /**
     * Getter for property tblNoticeCharge.
     * @return Value of property tblNoticeCharge.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblNoticeCharge() {
        return tblNoticeCharge;
    }
    
    /**
     * Setter for property tblNoticeCharge.
     * @param tblNoticeCharge New value of property tblNoticeCharge.
     */
    public void setTblNoticeCharge(com.see.truetransact.clientutil.EnhancedTableModel tblNoticeCharge) {
        this.tblNoticeCharge = tblNoticeCharge;
    }
    
    /**
     * Getter for property cboSuspenseProdID.
     * @return Value of property cboSuspenseProdID.
     */
    public java.lang.String getCboSuspenseProdID() {
        return cboSuspenseProdID;
    }
    
    /**
     * Setter for property cboSuspenseProdID.
     * @param cboSuspenseProdID New value of property cboSuspenseProdID.
     */
    public void setCboSuspenseProdID(java.lang.String cboSuspenseProdID) {
        this.cboSuspenseProdID = cboSuspenseProdID;
    }
    
    /**
     * Getter for property cbmSuspenseProdID.
     * @return Value of property cbmSuspenseProdID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmSuspenseProdID() {
        return cbmSuspenseProdID;
    }
    
    /**
     * Setter for property cbmSuspenseProdID.
     * @param cbmSuspenseProdID New value of property cbmSuspenseProdID.
     */
    public void setCbmSuspenseProdID(com.see.truetransact.clientutil.ComboBoxModel cbmSuspenseProdID) {
        this.cbmSuspenseProdID = cbmSuspenseProdID;
    }
    
    /**
     * Getter for property txtSuspenseAccNo.
     * @return Value of property txtSuspenseAccNo.
     */
    public java.lang.String getTxtSuspenseAccNo() {
        return txtSuspenseAccNo;
    }
    
    /**
     * Setter for property txtSuspenseAccNo.
     * @param txtSuspenseAccNo New value of property txtSuspenseAccNo.
     */
    public void setTxtSuspenseAccNo(java.lang.String txtSuspenseAccNo) {
        this.txtSuspenseAccNo = txtSuspenseAccNo;
    }
    
    /**
     * Getter for property rdoGLorAccNo.
     * @return Value of property rdoGLorAccNo.
     */
    public java.lang.String getRdoGLorAccNo() {
        return rdoGLorAccNo;
    }
    
    /**
     * Setter for property rdoGLorAccNo.
     * @param rdoGLorAccNo New value of property rdoGLorAccNo.
     */
    public void setRdoGLorAccNo(java.lang.String rdoGLorAccNo) {
        this.rdoGLorAccNo = rdoGLorAccNo;
    }
    
    /**
     * Getter for property txtStampAdvanceHead.
     * @return Value of property txtStampAdvanceHead.
     */
    public java.lang.String getTxtStampAdvanceHead() {
        return txtStampAdvanceHead;
    }
    
    /**
     * Setter for property txtStampAdvanceHead.
     * @param txtStampAdvanceHead New value of property txtStampAdvanceHead.
     */
    public void setTxtStampAdvanceHead(java.lang.String txtStampAdvanceHead) {
        this.txtStampAdvanceHead = txtStampAdvanceHead;
    }
    
    /**
     * Getter for property txtARCCostHead.
     * @return Value of property txtARCCostHead.
     */
    public java.lang.String getTxtARCCostHead() {
        return txtARCCostHead;
    }
    
    /**
     * Setter for property txtARCCostHead.
     * @param txtARCCostHead New value of property txtARCCostHead.
     */
    public void setTxtARCCostHead(java.lang.String txtARCCostHead) {
        this.txtARCCostHead = txtARCCostHead;
    }
    
    /**
     * Getter for property txtARCExpenseHead.
     * @return Value of property txtARCExpenseHead.
     */
    public java.lang.String getTxtARCExpenseHead() {
        return txtARCExpenseHead;
    }
    
    /**
     * Setter for property txtARCExpenseHead.
     * @param txtARCExpenseHead New value of property txtARCExpenseHead.
     */
    public void setTxtARCExpenseHead(java.lang.String txtARCExpenseHead) {
        this.txtARCExpenseHead = txtARCExpenseHead;
    }
    
    /**
     * Getter for property txtEACostHead.
     * @return Value of property txtEACostHead.
     */
    public java.lang.String getTxtEACostHead() {
        return txtEACostHead;
    }
    
    /**
     * Setter for property txtEACostHead.
     * @param txtEACostHead New value of property txtEACostHead.
     */
    public void setTxtEACostHead(java.lang.String txtEACostHead) {
        this.txtEACostHead = txtEACostHead;
    }
    
    /**
     * Getter for property txtEAExpenseHead.
     * @return Value of property txtEAExpenseHead.
     */
    public java.lang.String getTxtEAExpenseHead() {
        return txtEAExpenseHead;
    }
    
    /**
     * Setter for property txtEAExpenseHead.
     * @param txtEAExpenseHead New value of property txtEAExpenseHead.
     */
    public void setTxtEAExpenseHead(java.lang.String txtEAExpenseHead) {
        this.txtEAExpenseHead = txtEAExpenseHead;
    }
    
    /**
     * Getter for property txtEPCostHead.
     * @return Value of property txtEPCostHead.
     */
    public java.lang.String getTxtEPCostHead() {
        return txtEPCostHead;
    }
    
    /**
     * Setter for property txtEPCostHead.
     * @param txtEPCostHead New value of property txtEPCostHead.
     */
    public void setTxtEPCostHead(java.lang.String txtEPCostHead) {
        this.txtEPCostHead = txtEPCostHead;
    }
    
    /**
     * Getter for property txtEPExpenseHead.
     * @return Value of property txtEPExpenseHead.
     */
    public java.lang.String getTxtEPExpenseHead() {
        return txtEPExpenseHead;
    }
    
    /**
     * Setter for property txtEPExpenseHead.
     * @param txtEPExpenseHead New value of property txtEPExpenseHead.
     */
    public void setTxtEPExpenseHead(java.lang.String txtEPExpenseHead) {
        this.txtEPExpenseHead = txtEPExpenseHead;
    }
    
    /**
     * Getter for property txtPostageHead.
     * @return Value of property txtPostageHead.
     */
    public java.lang.String getTxtPostageHead() {
        return txtPostageHead;
    }
    
    /**
     * Setter for property txtPostageHead.
     * @param txtPostageHead New value of property txtPostageHead.
     */
    public void setTxtPostageHead(java.lang.String txtPostageHead) {
        this.txtPostageHead = txtPostageHead;
    }
    
    
    public String getTxtChargeHead() {
        return txtChargeHead;
    }

    public void setTxtChargeHead(String txtChargeHead) {
        this.txtChargeHead = txtChargeHead;
    }

    public String getChkIsASpecialScheme() {
        return chkIsASpecialScheme;
    }

    public void setChkIsASpecialScheme(String chkIsASpecialScheme) {
        this.chkIsASpecialScheme = chkIsASpecialScheme;
    }

    public String getChkSMSAlert() {
        return chkSMSAlert;
    }

    public void setChkSMSAlert(String chkSMSAlert) {
        this.chkSMSAlert = chkSMSAlert;
    }

    public String getChkBonusPrint() {
        return chkBonusPrint;
    }

    public void setChkBonusPrint(String chkBonusPrint) {
        this.chkBonusPrint = chkBonusPrint;
    }

    // Added by nithya on 02-08-2017 for mantis 7319
    
    public String getTxtLegalChrgHead() {
        return txtLegalChrgHead;
    }

    public void setTxtLegalChrgHead(String txtLegalChrgHead) {
        this.txtLegalChrgHead = txtLegalChrgHead;
    }
    
    // Added by nithya on 11-08-2017 for 7145

    public String getChkBankSettlement() {
        return chkBankSettlement;
    }

    public void setChkBankSettlement(String chkBankSettlement) {
        this.chkBankSettlement = chkBankSettlement;
    }
     public boolean populateGroupDepositCombo(String prodId){
                   // For populating deposit group 
          boolean dataExists = false;
          HashMap groupData=new HashMap();
          groupData.put("PROD_ID",prodId);
          groupData.put("BRANCH_ID",CommonUtil.convertObjToStr(ProxyParameters.BRANCH_ID));
          if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
             groupData.put("EDIT","EDIT"); 
          }else{
             groupData.put("NEW","NEW");   
          }
          List groupDataList = ClientUtil.executeQuery("getAllGDSGroup", groupData);
            //system.out.println("#### mapDataList :"+mapDataList);
           key=new ArrayList();
           value=new ArrayList();
            if (groupDataList != null && groupDataList.size() > 0) {
                dataExists = true;
                for(int i=0;i< groupDataList.size();i++){
                groupData = (HashMap) groupDataList.get(i);
                String key1=CommonUtil.convertObjToStr(groupData.get("GROUP_NO"));
                String val1=CommonUtil.convertObjToStr(groupData.get("GROUP_NO"));
                 cbmGroupNo = new ComboBoxModel(key, value);
                 if(i==0)
                     cbmGroupNo.addKeyAndElement("", "");
                     cbmGroupNo.addKeyAndElement(key1, val1);
                }
            }
           return dataExists; 
     }

    public String getTxtPartPayBonusRecoveryHead() {
        return txtPartPayBonusRecoveryHead;
    }

    public void setTxtPartPayBonusRecoveryHead(String txtPartPayBonusRecoveryHead) {
        this.txtPartPayBonusRecoveryHead = txtPartPayBonusRecoveryHead;
    }

    public int getTxtSchemeGraceperiod() {
        return txtSchemeGraceperiod;
    }

    public void setTxtSchemeGraceperiod(int txtSchemeGraceperiod) {
        this.txtSchemeGraceperiod = txtSchemeGraceperiod;
    }

    public String getChkDiscountFirstInst() {
        return chkDiscountFirstInst;
    }

    public void setChkDiscountFirstInst(String chkDiscountFirstInst) {
        this.chkDiscountFirstInst = chkDiscountFirstInst;
    }

    public double getTxtDiscountAmt() {
        return txtDiscountAmt;
    }

    public void setTxtDiscountAmt(double txtDiscountAmt) {
        this.txtDiscountAmt = txtDiscountAmt;
    }

    public String getTxtOtherChrgeAcHd() {
        return txtOtherChrgeAcHd;
    }

    public void setTxtOtherChrgeAcHd(String txtOtherChrgeAcHd) {
        this.txtOtherChrgeAcHd = txtOtherChrgeAcHd;
    }

    public String getChkCreditStampAdvance() {
        return chkCreditStampAdvance;
    }

    public void setChkCreditStampAdvance(String chkCreditStampAdvance) {
        this.chkCreditStampAdvance = chkCreditStampAdvance;
    }
    
    

}