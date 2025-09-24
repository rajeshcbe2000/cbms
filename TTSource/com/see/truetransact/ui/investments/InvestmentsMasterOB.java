/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductOB.java
 *
 * Created on Fri Jan 07 12:01:51 IST 2005
 */

package com.see.truetransact.ui.investments;

import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import java.util.LinkedHashMap;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.transferobject.investments.InvestmentsMasterTO;
import com.see.truetransact.transferobject.investments.InvestmentsOperativeTO;
import com.see.truetransact.transferobject.investments.InvestmentsDepositTO;
import com.see.truetransact.transferobject.investments.InvestmentsShareTO;
import com.see.truetransact.transferobject.investments.InvestmentsRFTO;
import com.see.truetransact.transferobject.investments.InvestmentsChequeTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import java.util.*;
import org.apache.log4j.Logger;



/**
 *
 * @author Ashok Vijayakumar
 */

public class InvestmentsMasterOB extends CObservable{
    
    Date curDate = ClientUtil.getCurrentDate();
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(InvestmentsMasterOB.class);
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.investments.InvestmentsMasterRB", ProxyParameters.LANGUAGE);
    private ProxyFactory proxy = null;
    private ComboBoxModel  cbmInvestmentBehaves,cbmIntPayFreq,cbmState,cbmFDInterestPaymentFrequency,cbmInvestmentTypeSBorCA;
    private ComboBoxModel  cbmRenewalInvestmentBehaves,cbmRenewalFDInterestPaymentFrequency,cbmRenewalInvestmentTypeSBorCA;
    private ComboBoxModel cbmRenewalDepTransModel,cbmRenewalDepTransProdType,cbmRenewalDepTransProdId;
    private String cboRenewalDepTransModel = "",cboRenewalDepTransProdType = "",cboRenewalDepTransProdId = "";
       
    private HashMap map,lookUpHash,keyValue;
    private int _result,_actionType;
    private ArrayList key,value;
    private static InvestmentsMasterOB objInvestmentsMasterOB;//Singleton Object
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String cboIntPayFreq="";
    private String cboState="";
    private Date IssueDt=null;
    private double years=0.0;
    private double months=0.0;
    private double days=0.0;
    private Date maturityDate=null;
    private double faceValue=0.0;
    private double couponRate=0.0;
    private String SLR="";
    private String callOption="";
    private String putOption="";
    private String setUpOption="";
    private double availableNoOfUnits=0.0 ;
    private Date lastIntPaidDate=null;
    private double totalPremiumPaid=0.0;
    private double totalPremiumCollected=0.0;
    private double totalInterestPaid=0.0;
    private double totalInterestCollected=0.0;
    private String txtBranchCode="";
    private String txtBankCode="";
    private String classification="";
    private Date initiatedDate=null;
    private Double outstandingAmount=null;
    private Double maturityAmount=null;
    private Double putOptionNoofYears=null;
    private Double callOptionNoofYears =null;
    private Double setUpOptionNoofYears =null;
    private String rdoSecurityType="";
    private String cboSecurityTypeCode="";
    private String txtRemarks="";
    private String txtOtherName="";
    private String preCloserRate="";
    private String closerType="";
    private String amortizationAmt="";
    private Date closerDate=null;
    private TransactionOB transactionOB;
    private LinkedHashMap transactionDetailsTO ;
    private LinkedHashMap allowedTransactionDetailsTO;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    
    //Added By Suresh
    //Common
    private String cboInvestmentBehaves = "";
    private String  investmentID="";
    private String investmentName="";
    private String callingTransAcctNo ="";
    private String rdoSBorCA="";
    private String cboInvestmentTypeSBorCA="";
    private String txtInvestmentIDTransSBorCA="";
    private String txtInvestmentRefNoTrans="";
    private String txtInvestmentInternalNoTrans="";
    private String txtChequeNo="";
    private String txtNarration="";
    private Double txtInvestmentAmount=null;
    private String batch_Id="";
    private String depositInternalAccNo="";
    
    //SB or CA
    private String txtSBAgencyName="";
    private String txtSBAccountRefNO="";
    private String txtSBInternalAccNo="";
    private Date tdtSBAccOpenDt=null;
    private String txtSBAreaOperatorDetails="";
    private String rdoCheckBookAllowed="";
    
    //FD , CCD or RD
    private String txtFDAgencyName="";
    private String txtFDAccountRefNO="";
    private String txtFDInternalAccNo="";
    private String txtFDPricipalAmt="";
    private String txtRenewalWithdrawalAmt="";
    private String txtFDInvestmentPeriod_Years="";
    private String txtFDInvestmentPeriod_Months="";
    private String txtFDInvestmentPeriod_Days="";
    private String txtFDRateOfInt="";
    private String cboFDInterestPaymentFrequency = "";
    private String txtFDMaturityAmt="";
    private String txtFDInterestReceivable="";
    private String txtFDInterestReceived="";
    private String txtFDPeriodicIntrest="";
    private Date tdtFDAccOpenDt=null;
    private Date tdtFDMaturityDt=null;
    private Date tdtFDEffectiveDt=null;
    private Date tdtFDIntReceivedTillDt=null;
    private String rdoWithPrincipal="";
    private String rdoRenewal="";
    private String rdoWithInterest="";
    private String rdoRenewalSameNumber="";
    private String rdoRenewalNewNumber="";
    private String rdoRenewalDiffProdNumber="";
    private String rdoRenewalWithInterest="";
    private String rdoRenewalWithOutInterest="";
    private String rdoRenewalpartialInterest="";
    private Date tdtRenewalDt ;
    
    //Renewal
    private String cboRenewalInvestmentBehaves = "";
    private String renewalInvestmentID="";
    private String renewalInvestmentName="";
    private String renewalInterestAmount="";
    private String rdoRenewalSBorCA="";
    private String cboRenewalInvestmentTypeSBorCA="";
    private String txtRenewalInvestmentIDTransSBorCA="";
    private String txtRenewalInvestmentRefNoTrans="";
    private String txtRenewalInvestmentInternalNoTrans="";
    private String txtRenewalNarration="";
    private String txtRenewalInvestmentAmount="";
    private String txtRenewalDepTransAmtValue = "";
    
    
    private String txtRenewalFDAgencyName="";
    private String txtRenewalFDAccountRefNO="";
    private String txtRenewalFDInternalAccNo="";
    private String txtRenewalFDPricipalAmt="";
    private String txtRenewalFDInvestmentPeriod_Years="";
    private String txtRenewalFDInvestmentPeriod_Months="";
    private String txtRenewalFDInvestmentPeriod_Days="";
    private String txtRenewalFDRateOfInt="";
    private String cboRenewalFDInterestPaymentFrequency = "";
    private String txtRenewalFDMaturityAmt="";
    private String txtRenewalFDInterestReceivable="";
    private String txtRenewalFDInterestReceived="";
    private String txtRenewalFDPeriodicIntrest="";
    private Date tdtRenewalFDAccOpenDt=null;
    private Date tdtRenewalFDMaturityDt=null;
    private Date tdtRenewalFDEffectiveDt=null;
    private Date tdtRenewalFDIntReceivedTillDt=null;
    private String chkAddMountToDeposit = "";
    private String txtRenewalDepositID = "";
    
    //SHARE
    private String txtShareAgencyName="";
    private String txtShareType="";
    private String txtShareMemberID="";
    private String txtShareInternalAccNo="";
    private String txtNoOfShares="";
    private String txtShareValue="";
    private String txtShareFaceValue="";
    private String txtFeesPaid="";
    private Date tdtShareAccOpenDt=null;
    
    //RF
    private String txtRFAgencyName="";
    private String txtRFAccountRefNO="";
    private String txtRFInternalAccNo="";
    private String txtRFPricipalAmt="";
    private Date tdtRFAccOpenDt=null;
    
    private InvestmentsOperativeTO objInvestmentsOperativeTO;
    private InvestmentsDepositTO objInvestmentsDepositTO;
    private InvestmentsShareTO objInvestmentsShareTO;
    private InvestmentsRFTO objInvestmentsRFTO;
    private InvestmentsChequeTO objInvestmentsChequeTO;
    
    //Cheque Book Table
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblCheckBookTable;
    private String txtFromNO="";
    private String txtToNO="";
    private String txtNoOfCheques="";
    private Date tdtChequeIssueDt=null;
    private boolean newData = false;
    private LinkedHashMap chequeMap;
    private LinkedHashMap deletedChequeMap;
    private String txtSlNo="";
    private HashMap authorizeMap;
    private double txtInvestTDS=0;
    private Date tdtTransactionDt=null;

    public Date getTdtTransactionDt() {
        return tdtTransactionDt;
    }

    public void setTdtTransactionDt(Date tdtTransactionDt) {
        this.tdtTransactionDt = tdtTransactionDt;
    }

    public double getTxtInvestTDS() {
        return txtInvestTDS;
    }

    public void setTxtInvestTDS(double txtInvestTDS) {
        this.txtInvestTDS = txtInvestTDS;
    }
    
    /** Creates a new instance of ShareProductOB */
    private InvestmentsMasterOB() {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "InvestmentsMasterJNDI");
        map.put(CommonConstants.HOME, "serverside.investments.InvestmentsMasterHome");
        map.put(CommonConstants.REMOTE, "serverside.investments.InvestmentsMaster");
        try {
            proxy = ProxyFactory.createProxy();
            initUIComboBoxModel();
            fillDropdown();
            setTableTile();
            tblCheckBookTable = new EnhancedTableModel(null, tableTitle);
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("Sl No");
        tableTitle.add("Issue Dt");
        tableTitle.add("From No");
        tableTitle.add("To No");
        tableTitle.add("Quantity");
        IncVal = new ArrayList();
    }
    
    static {
        try {
            _log.info("Creating InvestmentsMasterOB...");
            objInvestmentsMasterOB= new InvestmentsMasterOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** Creating Instances of ComboBoxModel */
    private void initUIComboBoxModel(){
        cbmInvestmentBehaves=new ComboBoxModel();
    }
    
    /* Filling up the the ComboBoxModel with key, value */
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("INVESTMENT");
            lookup_keys.add("INVESTMENT_RENEWAL");
            lookup_keys.add("DEPOSITSPRODUCT.DEPOSITPERIOD");
            lookup_keys.add("CUSTOMER.STATE");
            lookup_keys.add("REMITTANCE_PAYMENT.PAY_MODE");
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("INVESTMENT"));
            cbmInvestmentBehaves = new ComboBoxModel(key,value);
            makeNull();
            
            lookUpHash = new HashMap();
            lookUpHash.putAll((HashMap)keyValue.get("INVESTMENT_RENEWAL"));
            getKeyValue(lookUpHash);
//            HashMap transProdType = lookUpHash
            cbmRenewalInvestmentBehaves = new ComboBoxModel(key,value);
//            cbmRenewalInvestmentBehaves.removeKeyAndElement("OTHER_BANK_CA");
//            cbmRenewalInvestmentBehaves.removeKeyAndElement("OTHER_BANK_SB");
//            cbmRenewalInvestmentBehaves.removeKeyAndElement("OTHER_BANK_RD");
//            cbmRenewalInvestmentBehaves.removeKeyAndElement("OTHER_BANK_SPD");
//            cbmRenewalInvestmentBehaves.removeKeyAndElement("RESERVE_FUND_DCB");
//            cbmRenewalInvestmentBehaves.removeKeyAndElement("SHARES_DCB");
//            cbmRenewalInvestmentBehaves.removeKeyAndElement("SHARE_OTHER_INSTITUTIONS");
            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
//        	key.add("RM");
//        	value.add("Remittance");
//        	key.add("INV");
            cbmRenewalDepTransProdType = new ComboBoxModel(key, value);
            for (int i = 0; i < key.size(); i++) {
                String productKey = CommonUtil.convertObjToStr(cbmRenewalDepTransProdType.getKeys().get(i));
                if(key.get(i).equals("AD")){
                cbmRenewalDepTransProdType.removeKeyAndElement("AD");
                }
                if(key.get(i).equals("GL")){
                cbmRenewalDepTransProdType.removeKeyAndElement("GL");
                }
                if(key.get(i).equals("MDS")){
                    cbmRenewalDepTransProdType.removeKeyAndElement("MDS");
                }
               if(key.get(i).equals("MDS")){
                    cbmRenewalDepTransProdType.removeKeyAndElement("MDS");
                }
                if(key.get(i).equals("OA")){
                    cbmRenewalDepTransProdType.removeKeyAndElement("OA");
                }
                if(key.get(i).equals("SA")){
                    cbmRenewalDepTransProdType.removeKeyAndElement("SA");
                }
                if(key.get(i).equals("TD")){
                    cbmRenewalDepTransProdType.removeKeyAndElement("TD");
                }
                if(key.get(i).equals("TL")){
                    cbmRenewalDepTransProdType.removeKeyAndElement("TL");
                }
            }
//            cbmRenewalDepTransProdType.removeKeyAndElement("AD");
//            cbmRenewalDepTransProdType.removeKeyAndElement("GL");
//            cbmRenewalDepTransProdType.removeKeyAndElement("MDS");
//            cbmRenewalDepTransProdType.removeKeyAndElement("MDS");
//            cbmRenewalDepTransProdType.removeKeyAndElement("OA");
//            cbmRenewalDepTransProdType.removeKeyAndElement("SA");
//            cbmRenewalDepTransProdType.removeKeyAndElement("TD");
//            cbmRenewalDepTransProdType.removeKeyAndElement("TL");
        
            getKeyValue((HashMap) keyValue.get("REMITTANCE_PAYMENT.PAY_MODE"));
            cbmRenewalDepTransModel = new ComboBoxModel(key, value);

            getKeyValue((HashMap) keyValue.get("DEPOSITSPRODUCT.DEPOSITPERIOD"));
            cbmIntPayFreq = new ComboBoxModel(key, value);
            cbmFDInterestPaymentFrequency = new ComboBoxModel(key, value);
            cbmRenewalFDInterestPaymentFrequency = new ComboBoxModel(key, value);

            getKeyValue((HashMap) keyValue.get("CUSTOMER.STATE"));
            cbmState = new ComboBoxModel(key, value);


            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME, "getInvestmentOtherBankSBorCARenewal");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap) keyValue.get(CommonConstants.DATA));
            cbmInvestmentTypeSBorCA = new ComboBoxModel(key, value);
            cbmRenewalInvestmentTypeSBorCA = new ComboBoxModel(key, value);

            
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
        //setCbmRenewalDepTransModel(CommonUtil.convertObjToStr(getCboRenewalDepTransModel().getDataForKey("Cash")));
    }
//    private void removeProductKey(String productKey){
//        cbmRenewalDepTransProdType.removeKeyAndElement(productKey);
//    }
    public void setCbmRenewalDepTransProdId(String prodType) {
        System.out.println("Product Type   "+prodType);
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
        cbmRenewalDepTransProdId = new ComboBoxModel(key, value);
        this.cbmRenewalDepTransProdId = cbmRenewalDepTransProdId;
        setChanged();
    }
    private void makeNull(){
        key = null;
        value = null;
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    public void resetChequeDetails() {
        setTdtChequeIssueDt(null);
        setTxtFromNO("");
        setTxtToNO("");
        setTxtNoOfCheques("");
    }
    
    
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final InvestmentsChequeTO objInvestmentsChequeTO = new InvestmentsChequeTO();
            if( chequeMap == null ){
                chequeMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objInvestmentsChequeTO.setStatusDt(curDate);
                    objInvestmentsChequeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objInvestmentsChequeTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objInvestmentsChequeTO.setStatusDt(curDate);
                    objInvestmentsChequeTO.setStatusBy(TrueTransactMain.USER_ID);
                    objInvestmentsChequeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objInvestmentsChequeTO.setStatusDt(curDate);
                objInvestmentsChequeTO.setStatusBy(TrueTransactMain.USER_ID);
                objInvestmentsChequeTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            int  slno=0;
            int nums[]= new int[150];
            int max=nums[0];
            if(!updateMode){
                ArrayList data = tblCheckBookTable.getDataArrayList();
                slno=serialNo(data);
            }
            else{
                if(isNewData()){
                    ArrayList data = tblCheckBookTable.getDataArrayList();
                    slno=serialNo(data);
                }
                else{
                    int b=CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(rowSelected,0));
                    slno=b;
                }
            }
            objInvestmentsChequeTO.setSlNo(String.valueOf(slno));
            objInvestmentsChequeTO.setInvestmentId(getTxtSBInternalAccNo());
            objInvestmentsChequeTO.setIssueDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtChequeIssueDt())));
            objInvestmentsChequeTO.setFromNo(getTxtFromNO());
            objInvestmentsChequeTO.setToNo(getTxtToNO());
            objInvestmentsChequeTO.setNoOfCheques(getTxtNoOfCheques());
            chequeMap.put(objInvestmentsChequeTO.getSlNo(),objInvestmentsChequeTO);
            String sno=String.valueOf(slno);
            updateChequeDetails(rowSel,sno,objInvestmentsChequeTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateChequeDetails(int rowSel, String sno, InvestmentsChequeTO objInvestmentsChequeTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblCheckBookTable.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblCheckBookTable.getDataArrayList().get(j)).get(0);
            if(sno.equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblCheckBookTable.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(sno);
                IncParRow.add(getTdtChequeIssueDt());
                IncParRow.add(getTxtFromNO());
                IncParRow.add(getTxtToNO());
                IncParRow.add(getTxtNoOfCheques());
                tblCheckBookTable.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(sno);
            IncParRow.add(getTdtChequeIssueDt());
            IncParRow.add(getTxtFromNO());
            IncParRow.add(getTxtToNO());
            IncParRow.add(getTxtNoOfCheques());
            tblCheckBookTable.insertRow(tblCheckBookTable.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public void populateChequeDetails(String row){
        try{
            resetChequeDetails();
            final InvestmentsChequeTO objInvestmentsChequeTO = (InvestmentsChequeTO) chequeMap.get(row);
            populateTableData(objInvestmentsChequeTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTableData(InvestmentsChequeTO objInvestmentsChequeTO)  throws Exception{
        setTxtSlNo(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getSlNo()));
        setTdtChequeIssueDt(objInvestmentsChequeTO.getIssueDt());
        setTxtFromNO(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getFromNo()));
        setTxtToNO(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getToNo()));
        setTxtNoOfCheques(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getNoOfCheques()));
    }
    
    public void deleteTableData(String val, int row){
        if(deletedChequeMap== null){
            deletedChequeMap = new LinkedHashMap();
        }
        InvestmentsChequeTO objInvestmentsChequeTO = (InvestmentsChequeTO) chequeMap.get(val);
        objInvestmentsChequeTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedChequeMap.put(CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(row,0)),chequeMap.get(val));
        Object obj;
        obj=val;
        chequeMap.remove(val);
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
        incDataList = new ArrayList(chequeMap.keySet());
        ArrayList addList =new ArrayList(chequeMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            InvestmentsChequeTO objInvestmentsChequeTO = (InvestmentsChequeTO) chequeMap.get(addList.get(i));
            IncVal.add(objInvestmentsChequeTO);
            if(!objInvestmentsChequeTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getSlNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getIssueDt()));
                incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getFromNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getToNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getNoOfCheques()));
                tblCheckBookTable.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    public String callForBehavesSBorCATrans(){
        return CommonUtil.convertObjToStr(cbmInvestmentTypeSBorCA.getKeyForSelected());
    }
    
    public String callForRenewalBehavesSBorCATrans(){
        return CommonUtil.convertObjToStr(cbmRenewalInvestmentTypeSBorCA.getKeyForSelected());
    }
    
    public int serialNo(ArrayList data){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    
    /* Return the key,value(Array List) to be used up in ComboBoxModel */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    /**
     * Returns an instance of ShareProductOB.
     * @return  ShareProductOB
     */
    
    public static InvestmentsMasterOB getInstance()throws Exception{
        return objInvestmentsMasterOB;
    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    public String callForBehaves(){
        return CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected());
    }
    public String callForRenewalBehaves(){
        return CommonUtil.convertObjToStr(cbmRenewalInvestmentBehaves.getKeyForSelected());
    }
    public String CallForRenewalProdModel() {
        return CommonUtil.convertObjToStr(cbmRenewalDepTransModel.getKeyForSelected());
    }

    public String CallForRenewalDepProductID() {
        return CommonUtil.convertObjToStr(cbmRenewalDepTransProdId.getKeyForSelected());
    }

    public String CallForRenewalDepProductType() {
        return CommonUtil.convertObjToStr(cbmRenewalDepTransProdType.getKeyForSelected());
    }
    /** Returns an Instance of InvestmentMaster */
    public InvestmentsMasterTO getInvestmentsMasterTO(String command){
        InvestmentsMasterTO objgetInvestmentsMasterTO= new InvestmentsMasterTO();
        final String yes="Y";
        final String no="N";
        objgetInvestmentsMasterTO.setCommand(command);
        if(objgetInvestmentsMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsMasterTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsMasterTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsMasterTO.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsMasterTO.setCboIntPayFreq(CommonUtil.convertObjToStr(cbmIntPayFreq.getKeyForSelected()));
        objgetInvestmentsMasterTO.setInvestmentID(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsMasterTO.setInvestmentName(CommonUtil.convertObjToStr(getInvestmentName()));
//        objgetInvestmentsMasterTO.setInvestmentName(CommonUtil.convertObjToStr(getInvestmentName()));
        if(getTdtTransactionDt()!=null && !getTdtTransactionDt().equals("")){
            objgetInvestmentsMasterTO.setIssueDt(getProperDateFormat(getTdtTransactionDt()));
        }else{
            objgetInvestmentsMasterTO.setIssueDt((Date)curDate.clone());
        }
        objgetInvestmentsMasterTO.setYears(new Double(getYears()));
        objgetInvestmentsMasterTO.setMonths(new Double(getMonths()));
        objgetInvestmentsMasterTO.setDays(new Double(getDays()));
        objgetInvestmentsMasterTO.setMaturityDate(getProperDateFormat(getMaturityDate()));
        objgetInvestmentsMasterTO.setFaceValue(new Double(getFaceValue()));
        objgetInvestmentsMasterTO.setCouponRate(new Double(getCouponRate()));
        objgetInvestmentsMasterTO.setSLR(CommonUtil.convertObjToStr(getSLR()));
        objgetInvestmentsMasterTO.setCallOption(CommonUtil.convertObjToStr(getCallOption()));
        objgetInvestmentsMasterTO.setPutOption(CommonUtil.convertObjToStr(getPutOption()));
        objgetInvestmentsMasterTO.setSetUpOption(CommonUtil.convertObjToStr(getSetUpOption()));
        objgetInvestmentsMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsMasterTO.setStatusDt(curDate);
        objgetInvestmentsMasterTO.setLastIntPaidDate(getLastIntPaidDate());
        objgetInvestmentsMasterTO.setAvailableNoOfUnits(new Double(getAvailableNoOfUnits()));
        objgetInvestmentsMasterTO.setTotalPremiumCollected(new Double(getTotalPremiumCollected()));
        objgetInvestmentsMasterTO.setTotalPremiumPaid(new Double(getTotalPremiumPaid()));
        objgetInvestmentsMasterTO.setTotalInterestPaid(new Double(getTotalInterestPaid()));
        objgetInvestmentsMasterTO.setTotalInterestCollected(new Double(getTotalInterestCollected()));
        objgetInvestmentsMasterTO.setInitiatedDate(getInitiatedDate());
        objgetInvestmentsMasterTO.setClassification(CommonUtil.convertObjToStr(getClassification()));
        objgetInvestmentsMasterTO.setOutstandingAmount(getOutstandingAmount());
        objgetInvestmentsMasterTO.setMaturityAmount(getMaturityAmount());
        objgetInvestmentsMasterTO.setCallOptionNoofYears(getCallOptionNoofYears());
        objgetInvestmentsMasterTO.setPutOptionNoofYears(getPutOptionNoofYears());
        objgetInvestmentsMasterTO.setSetUpOptionNoofYears(getSetUpOptionNoofYears());
        objgetInvestmentsMasterTO.setTxtBankCode(getTxtBankCode());
        objgetInvestmentsMasterTO.setTxtBranchCode(getTxtBranchCode());
        objgetInvestmentsMasterTO.setRdoSecurityType(getRdoSecurityType());
        objgetInvestmentsMasterTO.setTxtOtherName(getTxtOtherName());
        objgetInvestmentsMasterTO.setTxtRemarks(getTxtRemarks());
        objgetInvestmentsMasterTO.setCboSecurityTypeCode(CommonUtil.convertObjToStr(cbmState.getKeyForSelected()));
        
        
        
        
        return objgetInvestmentsMasterTO;
        
    }
    
    /** Sets all the InvsetmentMaster values to the OB varibles  there by populatin the UI fields */
    private void setInvestmentsMasterTO(InvestmentsMasterTO objInvestmentsMasterTO){
        
        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objInvestmentsMasterTO.getCboInvestmentBehaves())));
        setCboIntPayFreq(CommonUtil.convertObjToStr(getCbmIntPayFreq().getDataForKey(objInvestmentsMasterTO.getCboIntPayFreq())));
        setInvestmentID(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getInvestmentID()));
        setInvestmentName(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getInvestmentName()));
        setIssueDt(objInvestmentsMasterTO.getIssueDt());
        setYears(objInvestmentsMasterTO.getYears().doubleValue());
        setMonths(objInvestmentsMasterTO.getMonths().doubleValue());
        setDays(objInvestmentsMasterTO.getDays().doubleValue());
        setMaturityDate(objInvestmentsMasterTO.getMaturityDate());
        setFaceValue(objInvestmentsMasterTO.getFaceValue().doubleValue());
        setCouponRate(objInvestmentsMasterTO.getCouponRate().doubleValue());
        setSLR(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getSLR()));
        setCallOption(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getCallOption()));
        setPutOption(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getPutOption()));
        setSetUpOption(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getSetUpOption()));
        setLastIntPaidDate(objInvestmentsMasterTO.getLastIntPaidDate());
        setAvailableNoOfUnits(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getAvailableNoOfUnits()).doubleValue());
        setTotalInterestCollected(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalInterestCollected()).doubleValue());
        setTotalInterestPaid(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalInterestPaid()).doubleValue());
        setTotalPremiumPaid(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalPremiumPaid()).doubleValue());
        setTotalPremiumCollected(CommonUtil.convertObjToDouble(objInvestmentsMasterTO.getTotalPremiumCollected()).doubleValue());
        setInitiatedDate(objInvestmentsMasterTO.getInitiatedDate());
        setClassification(CommonUtil.convertObjToStr(objInvestmentsMasterTO.getClassification()));
        setOutstandingAmount(objInvestmentsMasterTO.getOutstandingAmount());
        setMaturityAmount(objInvestmentsMasterTO.getMaturityAmount());
        setCallOptionNoofYears(objInvestmentsMasterTO.getCallOptionNoofYears());
        setPutOptionNoofYears(objInvestmentsMasterTO.getPutOptionNoofYears());
        setSetUpOptionNoofYears(objInvestmentsMasterTO.getSetUpOptionNoofYears());
        setTxtBankCode(objInvestmentsMasterTO.getTxtBankCode());
        setTxtBranchCode(objInvestmentsMasterTO.getTxtBranchCode());
        setCboSecurityTypeCode(CommonUtil.convertObjToStr(getCbmState().getDataForKey(objInvestmentsMasterTO.getCboSecurityTypeCode())));
        setRdoSecurityType(objInvestmentsMasterTO.getRdoSecurityType());
        setTxtRemarks(objInvestmentsMasterTO.getTxtRemarks());
        setTxtOtherName(objInvestmentsMasterTO.getTxtOtherName());
        
        
        notifyObservers();
    }
    
    public InvestmentsTransTO getInvestmentsTransTO(String command){
        InvestmentsTransTO objgetInvestmentsTransTO= new InvestmentsTransTO();
        objgetInvestmentsTransTO.setCommand(command);
        if(objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsTransTO.setCboInvestmentType(CommonUtil.convertObjToStr(cbmInvestmentTypeSBorCA.getKeyForSelected()));
        objgetInvestmentsTransTO.setTxtInvestmentIDTransSBorCA(CommonUtil.convertObjToStr(getTxtInvestmentIDTransSBorCA()));
        objgetInvestmentsTransTO.setTxtInvestmentRefNoTrans(CommonUtil.convertObjToStr(getTxtInvestmentRefNoTrans()));
        objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(getDepositInternalAccNo()));
        objgetInvestmentsTransTO.setTxtInvestmentInternalNoTrans(CommonUtil.convertObjToStr(getTxtInvestmentInternalNoTrans()));
        objgetInvestmentsTransTO.setTxtChequeNo(CommonUtil.convertObjToStr(getTxtChequeNo()));
        objgetInvestmentsTransTO.setNarration(CommonUtil.convertObjToStr(getTxtNarration()));
        objgetInvestmentsTransTO.setRdoSBorCA(CommonUtil.convertObjToStr(getRdoSBorCA()));
        objgetInvestmentsTransTO.setInvestTDS(getTxtInvestTDS());
        objgetInvestmentsTransTO.setTrnCode("Deposit");
        String investmentType = "";
        investmentType = CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected());
        if(investmentType.equals("OTHER_BANK_SB") || investmentType.equals("OTHER_BANK_CA") || investmentType.equals("OTHER_BANK_SPD")){
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(getTxtSBAccountRefNO()));
        }else if(investmentType.equals("OTHER_BANK_CCD") || investmentType.equals("OTHER_BANK_FD")
        || investmentType.equals("OTHER_BANK_SSD") || investmentType.equals("OTHER_BANK_RD")){
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(getTxtFDAccountRefNO()));
        }else if(investmentType.equals("SHARES_DCB") || investmentType.equals("SHARE_OTHER_INSTITUTIONS")){
            objgetInvestmentsTransTO.setTrnCode("Purchase");
            objgetInvestmentsTransTO.setPurchaseRate(CommonUtil.convertObjToDouble(getTxtFeesPaid()));
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(getTxtShareMemberID()));
        }else if(investmentType.equals("RESERVE_FUND_DCB")){
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(getTxtRFAccountRefNO()));
        }
        objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(getBatch_Id()));
        if(getTdtTransactionDt()!=null && !getTdtTransactionDt().equals("")){
            objgetInvestmentsTransTO.setTransDT(getProperDateFormat(getTdtTransactionDt()));
        }else{
            objgetInvestmentsTransTO.setTransDT((Date)curDate.clone());
        }
        objgetInvestmentsTransTO.setTransType(CommonConstants.DEBIT);
        objgetInvestmentsTransTO.setAmount(new Double(0.0));
        objgetInvestmentsTransTO.setPurchaseDt(curDate);
        objgetInvestmentsTransTO.setInvestmentAmount(getTxtInvestmentAmount());
        objgetInvestmentsTransTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsTransTO.setStatusDt(curDate);
        objgetInvestmentsTransTO.setDividendAmount(new Double(0));
        objgetInvestmentsTransTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
        return objgetInvestmentsTransTO;
    }
    
    public InvestmentsTransTO getRenewalInvestmentsTransTO(String command){
        InvestmentsTransTO objgetInvestmentsTransTO= new InvestmentsTransTO();
        objgetInvestmentsTransTO.setCommand(command);
        if(objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsTransTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(cbmRenewalInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(getRenewalInvestmentID()));
        objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(getTxtRenewalFDInternalAccNo()));
        objgetInvestmentsTransTO.setPurchaseMode("SHARE_PAYMENT");
        
        objgetInvestmentsTransTO.setCboInvestmentType(CommonUtil.convertObjToStr(cbmRenewalInvestmentTypeSBorCA.getKeyForSelected()));
        objgetInvestmentsTransTO.setTxtInvestmentIDTransSBorCA(CommonUtil.convertObjToStr(getTxtInvestmentIDTransSBorCA()));
        objgetInvestmentsTransTO.setTxtInvestmentRefNoTrans(CommonUtil.convertObjToStr(getTxtInvestmentRefNoTrans()));
        objgetInvestmentsTransTO.setTxtInvestmentInternalNoTrans(CommonUtil.convertObjToStr(getTxtInvestmentInternalNoTrans()));
        objgetInvestmentsTransTO.setNarration(CommonUtil.convertObjToStr(getTxtNarration()));
        objgetInvestmentsTransTO.setRdoSBorCA(CommonUtil.convertObjToStr(getRdoSBorCA()));
        objgetInvestmentsTransTO.setInvestTDS(getTxtInvestTDS());
        objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(getTxtRenewalFDAccountRefNO()));
        objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(getRenewalInvestmentName()));
        if(getTdtTransactionDt()!=null && !getTdtTransactionDt().equals("")){
            objgetInvestmentsTransTO.setTransDT(getProperDateFormat(getTdtTransactionDt()));
        }else{
            objgetInvestmentsTransTO.setTransDT((Date)curDate.clone());
        }
        objgetInvestmentsTransTO.setTransType(CommonConstants.DEBIT);
        objgetInvestmentsTransTO.setAmount(new Double(0.0));
        objgetInvestmentsTransTO.setPurchaseDt(curDate);
        objgetInvestmentsTransTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsTransTO.setStatusDt(curDate);
        objgetInvestmentsTransTO.setDividendAmount(new Double(0));
        objgetInvestmentsTransTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
        return objgetInvestmentsTransTO;
    }
    
    
    //Added By Suresh
    /** Returns an Instance of InvestmentMaster */
    public InvestmentsOperativeTO getInvestmentsOperativeTO(String command){
        InvestmentsOperativeTO objgetInvestmentsOperativeTO= new InvestmentsOperativeTO();
        objgetInvestmentsOperativeTO.setCommand(command);
        if(objgetInvestmentsOperativeTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsOperativeTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsOperativeTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsOperativeTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsOperativeTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsOperativeTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsOperativeTO.setInvestmentId(CommonUtil.convertObjToStr(getTxtSBInternalAccNo()));
        objgetInvestmentsOperativeTO.setInvestmentType(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsOperativeTO.setInvestmentProdId(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsOperativeTO.setInvestmentProdDesc(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsOperativeTO.setAgencyName(CommonUtil.convertObjToStr(getTxtSBAgencyName()));
        objgetInvestmentsOperativeTO.setInvestmentRefNo(CommonUtil.convertObjToStr(getTxtSBAccountRefNO()));
        objgetInvestmentsOperativeTO.setInvestmentIssueDt(getProperDateFormat(getTdtSBAccOpenDt()));
        objgetInvestmentsOperativeTO.setOperatorDetails(CommonUtil.convertObjToStr(getTxtSBAreaOperatorDetails()));
        objgetInvestmentsOperativeTO.setCheckAllowed(CommonUtil.convertObjToStr(getRdoCheckBookAllowed()));
        objgetInvestmentsOperativeTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsOperativeTO.setStatusDt(curDate);
        return objgetInvestmentsOperativeTO;
    }
    
    public InvestmentsDepositTO getInvestmentsDepositTO(String command){
        InvestmentsDepositTO objgetInvestmentsDepositTO= new InvestmentsDepositTO();
        objgetInvestmentsDepositTO.setCommand(command);
        if(objgetInvestmentsDepositTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsDepositTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsDepositTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsDepositTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsDepositTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsDepositTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsDepositTO.setInvestmentId(CommonUtil.convertObjToStr(getTxtFDInternalAccNo()));
        objgetInvestmentsDepositTO.setInvestmentType(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsDepositTO.setInvestmentProdId(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsDepositTO.setInvestmentProdDesc(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsDepositTO.setAgencyName(CommonUtil.convertObjToStr(getTxtFDAgencyName()));
        if(getTdtTransactionDt()!=null && !getTdtTransactionDt().equals("")){
            objgetInvestmentsDepositTO.setInvestmentIssueDt(getProperDateFormat(getTdtTransactionDt()));
        }else{
            objgetInvestmentsDepositTO.setInvestmentIssueDt((Date)curDate.clone());
        }
        objgetInvestmentsDepositTO.setInvestmentEffectiveDt(getProperDateFormat(getTdtFDEffectiveDt()));
        objgetInvestmentsDepositTO.setInvestmentRefNo(CommonUtil.convertObjToStr(getTxtFDAccountRefNO()));
        objgetInvestmentsDepositTO.setPrincipalAmount(CommonUtil.convertObjToDouble(getTxtFDPricipalAmt()));
        objgetInvestmentsDepositTO.setRenewalPartialAmt(CommonUtil.convertObjToDouble(getTxtRenewalWithdrawalAmt()));
        objgetInvestmentsDepositTO.setInvestmentPeriodYy(CommonUtil.convertObjToDouble(getTxtFDInvestmentPeriod_Years()));
        objgetInvestmentsDepositTO.setInvestmentPeriodMm(CommonUtil.convertObjToDouble(getTxtFDInvestmentPeriod_Months()));
        objgetInvestmentsDepositTO.setInvestmentPeriodDd(CommonUtil.convertObjToDouble(getTxtFDInvestmentPeriod_Days()));
        objgetInvestmentsDepositTO.setRateOfInterest(CommonUtil.convertObjToDouble(getTxtFDRateOfInt()));
        objgetInvestmentsDepositTO.setMaturityDt(getProperDateFormat(getTdtFDMaturityDt()));
        objgetInvestmentsDepositTO.setMaturityAmount(CommonUtil.convertObjToDouble(getTxtFDMaturityAmt()));
        objgetInvestmentsDepositTO.setIntpayFreq(CommonUtil.convertObjToDouble(cbmFDInterestPaymentFrequency.getKeyForSelected()));
        objgetInvestmentsDepositTO.setWithPrincipal(CommonUtil.convertObjToStr(getRdoWithPrincipal()));
        objgetInvestmentsDepositTO.setRenewal(CommonUtil.convertObjToStr(getRdoRenewal()));
        objgetInvestmentsDepositTO.setRenewalSameNo(CommonUtil.convertObjToStr(getRdoRenewalSameNumber()));
        objgetInvestmentsDepositTO.setRenewalNewNo(CommonUtil.convertObjToStr(getRdoRenewalNewNumber()));
        objgetInvestmentsDepositTO.setRenewalDiffProdNo(CommonUtil.convertObjToStr(getRdoRenewalDiffProdNumber()));
        objgetInvestmentsDepositTO.setRenewalWithInterest(CommonUtil.convertObjToStr(getRdoRenewalWithInterest()));
        objgetInvestmentsDepositTO.setRenewalWithOutInterest(CommonUtil.convertObjToStr(getRdoRenewalWithOutInterest()));
        objgetInvestmentsDepositTO.setRenewalPartialInterest(CommonUtil.convertObjToStr(getRdoRenewalpartialInterest()));
        
        String renewal = CommonUtil.convertObjToStr(getRdoRenewal());
        if(renewal.length()>0  && renewal.equals("Y")){
        objgetInvestmentsDepositTO.setWithInterest(CommonUtil.convertObjToStr(getRdoWithInterest()));
        }else{
            objgetInvestmentsDepositTO.setWithInterest("");
        }
        objgetInvestmentsDepositTO.setInterestReceivable(CommonUtil.convertObjToDouble(getTxtFDInterestReceivable()));
        objgetInvestmentsDepositTO.setInterestReceived(CommonUtil.convertObjToDouble(getTxtFDInterestReceived()));
        objgetInvestmentsDepositTO.setPeriodicIntrest(CommonUtil.convertObjToDouble(getTxtFDPeriodicIntrest()));
        
        objgetInvestmentsDepositTO.setIntRecTillDt(getProperDateFormat(getTdtFDIntReceivedTillDt()));
        objgetInvestmentsDepositTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsDepositTO.setStatusDt(curDate);
        objgetInvestmentsDepositTO.setRenewalDt(getTdtRenewalDt());
        return objgetInvestmentsDepositTO;
    }
    
    public InvestmentsDepositTO getRenewalInvestmentsDepositTO(String command){
        InvestmentsDepositTO objgetInvestmentsDepositTO = new InvestmentsDepositTO();
        objgetInvestmentsDepositTO.setCommand(command);
        objgetInvestmentsDepositTO.setStatus(CommonConstants.STATUS_CREATED);
        objgetInvestmentsDepositTO.setInvestmentId(CommonUtil.convertObjToStr(getTxtRenewalFDInternalAccNo()));
        objgetInvestmentsDepositTO.setInvestmentType(CommonUtil.convertObjToStr(cbmRenewalInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsDepositTO.setInvestmentProdId(CommonUtil.convertObjToStr(getRenewalInvestmentID()));
        objgetInvestmentsDepositTO.setInvestmentProdDesc(CommonUtil.convertObjToStr(getRenewalInvestmentName()));
        objgetInvestmentsDepositTO.setAgencyName(CommonUtil.convertObjToStr(getTxtRenewalFDAgencyName()));
        objgetInvestmentsDepositTO.setInvestmentIssueDt(getProperDateFormat(getTdtRenewalFDAccOpenDt()));
        objgetInvestmentsDepositTO.setInvestmentEffectiveDt(getProperDateFormat(getTdtRenewalFDEffectiveDt()));
        objgetInvestmentsDepositTO.setInvestmentRefNo(CommonUtil.convertObjToStr(getTxtRenewalFDAccountRefNO()));
        objgetInvestmentsDepositTO.setPrincipalAmount(CommonUtil.convertObjToDouble(getTxtRenewalFDPricipalAmt()));
        objgetInvestmentsDepositTO.setInvestmentPeriodYy(CommonUtil.convertObjToDouble(getTxtRenewalFDInvestmentPeriod_Years()));
        objgetInvestmentsDepositTO.setInvestmentPeriodMm(CommonUtil.convertObjToDouble(getTxtRenewalFDInvestmentPeriod_Months()));
        objgetInvestmentsDepositTO.setInvestmentPeriodDd(CommonUtil.convertObjToDouble(getTxtRenewalFDInvestmentPeriod_Days()));
        objgetInvestmentsDepositTO.setRateOfInterest(CommonUtil.convertObjToDouble(getTxtRenewalFDRateOfInt()));
        objgetInvestmentsDepositTO.setMaturityDt(getProperDateFormat(getTdtRenewalFDMaturityDt()));
        objgetInvestmentsDepositTO.setMaturityAmount(CommonUtil.convertObjToDouble(getTxtRenewalFDMaturityAmt()));
        objgetInvestmentsDepositTO.setIntpayFreq(CommonUtil.convertObjToDouble(cbmRenewalFDInterestPaymentFrequency.getKeyForSelected()));
        objgetInvestmentsDepositTO.setRenewal("");
        objgetInvestmentsDepositTO.setRenewalPartialAmt(CommonUtil.convertObjToDouble(getTxtRenewalWithdrawalAmt()));
        objgetInvestmentsDepositTO.setInterestReceivable(CommonUtil.convertObjToDouble(getTxtRenewalFDInterestReceivable()));
        objgetInvestmentsDepositTO.setPeriodicIntrest(CommonUtil.convertObjToDouble(getTxtRenewalFDPeriodicIntrest()));
        objgetInvestmentsDepositTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsDepositTO.setStatusDt(curDate);
        objgetInvestmentsDepositTO.setRenewalDt(getTdtRenewalDt());
        return objgetInvestmentsDepositTO;
    }
    
    public InvestmentsShareTO getInvestmentsShareTO(String command){
        InvestmentsShareTO objgetInvestmentsShareTO= new InvestmentsShareTO();
        objgetInvestmentsShareTO.setCommand(command);
        if(objgetInvestmentsShareTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsShareTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsShareTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsShareTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsShareTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsShareTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsShareTO.setInvestmentId(CommonUtil.convertObjToStr(getTxtShareInternalAccNo()));
        objgetInvestmentsShareTO.setInvestmentType(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsShareTO.setInvestmentProdId(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsShareTO.setInvestmentProdDesc(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsShareTO.setShareType(CommonUtil.convertObjToStr(getTxtShareType()));
        objgetInvestmentsShareTO.setInvestmentRefNo(CommonUtil.convertObjToStr(getTxtShareMemberID()));
        objgetInvestmentsShareTO.setInvestmentIssueDt(getTdtShareAccOpenDt());
        objgetInvestmentsShareTO.setNoOfShares(CommonUtil.convertObjToDouble(getTxtNoOfShares()));
        objgetInvestmentsShareTO.setShareValue(CommonUtil.convertObjToDouble(getTxtShareValue()));
        objgetInvestmentsShareTO.setFaceValue(CommonUtil.convertObjToDouble(getTxtShareFaceValue()));
        objgetInvestmentsShareTO.setFeesPaid(CommonUtil.convertObjToDouble(getTxtFeesPaid()));
        objgetInvestmentsShareTO.setAgencyName(CommonUtil.convertObjToStr(getTxtShareAgencyName()));
        objgetInvestmentsShareTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsShareTO.setStatusDt(curDate);
        return objgetInvestmentsShareTO;
    }
    
    public InvestmentsRFTO getInvestmentsReserveFundTO(String command){
        InvestmentsRFTO objgetInvestmentsRFTO= new InvestmentsRFTO();
        objgetInvestmentsRFTO.setCommand(command);
        if(objgetInvestmentsRFTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
            objgetInvestmentsRFTO.setStatus(CommonConstants.STATUS_CREATED);
        }else  if(objgetInvestmentsRFTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
            objgetInvestmentsRFTO.setStatus(CommonConstants.STATUS_MODIFIED);
        }else if(objgetInvestmentsRFTO.getCommand().equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
            objgetInvestmentsRFTO.setStatus(CommonConstants.STATUS_DELETED);
        }
        objgetInvestmentsRFTO.setInvestmentId(CommonUtil.convertObjToStr(getTxtRFInternalAccNo()));
        objgetInvestmentsRFTO.setInvestmentType(CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
        objgetInvestmentsRFTO.setInvestmentProdId(CommonUtil.convertObjToStr(getInvestmentID()));
        objgetInvestmentsRFTO.setInvestmentProdDesc(CommonUtil.convertObjToStr(getInvestmentName()));
        objgetInvestmentsRFTO.setAgencyName(CommonUtil.convertObjToStr(getTxtRFAgencyName()));
        objgetInvestmentsRFTO.setInvestmentRefNo(CommonUtil.convertObjToStr(getTxtRFAccountRefNO()));
        objgetInvestmentsRFTO.setInvestmentIssueDt(getTdtRFAccOpenDt());
        objgetInvestmentsRFTO.setAmount(CommonUtil.convertObjToDouble(getTxtRFPricipalAmt()));
        objgetInvestmentsRFTO.setStatusBy(TrueTransactMain.USER_ID);
        objgetInvestmentsRFTO.setStatusDt(curDate);
        return objgetInvestmentsRFTO;
    }
    
    
    /* Executes Query using the TO object */
    //nidhin     private String txtRenewalDepTransAmtValue = "";
    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
//            term.put("InvestmentsMasterTO", getInvestmentsMasterTO(command));
            String investmentType = "";
            investmentType = CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected());
            term.put("COMMAND",command);
            term.put("INVESTMENT_TYPE",investmentType);
            if(investmentType.equals("OTHER_BANK_SB") || investmentType.equals("OTHER_BANK_CA") || investmentType.equals("OTHER_BANK_SPD")){
                term.put("InvestmentsOperativeTO", getInvestmentsOperativeTO(command));
                if(chequeMap!=null && chequeMap.size()>0 ){
                    term.put("chequeBookDetails",chequeMap);
                }
                if(deletedChequeMap!=null && deletedChequeMap.size()>0 ){
                    term.put("deletedChequeBookDetails",deletedChequeMap);
                }
            }else if(investmentType.equals("OTHER_BANK_CCD") || investmentType.equals("OTHER_BANK_FD") 
            || investmentType.equals("OTHER_BANK_SSD") || investmentType.equals("OTHER_BANK_RD")){
                term.put("InvestmentsDepositTO", getInvestmentsDepositTO(command));
                String renewal = CommonUtil.convertObjToStr(getRdoRenewal());
                if(renewal.length()>0  && renewal.equals("Y")&&CommonUtil.convertObjToDouble(getTxtRenewalFDPricipalAmt())>0){
                    term.put("RENEWAL","RENEWAL");
                    term.put("RenewalInvestmentsDepositTO", getRenewalInvestmentsDepositTO(command));
                    term.put("RenewalInvestmentsTransTO", getRenewalInvestmentsTransTO(command));
                    if(CommonUtil.convertObjToDouble(getRenewalInterestAmount()).doubleValue()>0){
                        term.put("RENEWAL_INTEREST_AMOUNT",getRenewalInterestAmount());
                    }else{
                            term.put("RENEWAL_INTEREST_AMOUNT",CommonUtil.convertObjToStr(0));
                        }
                    if(CommonUtil.convertObjToDouble(getTxtRenewalFDPricipalAmt()).doubleValue()>0){
                        term.put("PRINCIPAL_AMOUNT",getTxtFDPricipalAmt());
                    }
                    
                    //Added by nithya on 04-02-2022 for KD-3287
                    //Check for renewal without transaction
                    String renewWithoutTransaction = "N";
                    HashMap renewMap = new HashMap();
                    renewMap.put("PROD_ID",getInvestmentID());
                    List renewList = ClientUtil.executeQuery("getInvestmentRenewalWithoutTransaction", renewMap);
                    if(renewList != null && renewList.size() > 0){
                        renewMap = (HashMap)renewList.get(0);
                        if(renewMap.containsKey("RENEW_WITHOUT_TRANSACTION") && renewMap.get("RENEW_WITHOUT_TRANSACTION") != null && renewMap.get("RENEW_WITHOUT_TRANSACTION").equals("Y")){
                            renewWithoutTransaction = "Y";
                        }
                    }
                    // End   
                    
                    if(CommonUtil.convertObjToStr(getRdoRenewalSameNumber()).equals("Y")){
                        term.put("RENEWAL_NUMBER_TYPE","SAME_NO");
                    }else if(CommonUtil.convertObjToStr(getRdoRenewalNewNumber()).equals("Y")){
                        term.put("RENEWAL_NUMBER_TYPE","NEW_NO");
                    }else if(CommonUtil.convertObjToStr(getRdoRenewalDiffProdNumber()).equals("Y")){
                        term.put("RENEWAL_NUMBER_TYPE","DIFFERENT_PROD_ID");
                        term.put("OLD_INVESTMENT_ID",getInvestmentID());
                        term.put("OLD_INVESTMENT_TYPE",CommonUtil.convertObjToStr(cbmInvestmentBehaves.getKeyForSelected()));
                    }
                    
                    if(CommonUtil.convertObjToStr(getRdoRenewalWithInterest()).equals("Y")){
                        term.put("RENEWAL_INTEREST_TYPE","WITH_INTEREST");
                    }else if(CommonUtil.convertObjToStr(getRdoRenewalWithOutInterest()).equals("Y")){
                        term.put("RENEWAL_INTEREST_TYPE","WITHOUT_INTEREST");
                    }else if(CommonUtil.convertObjToStr(getRdoRenewalpartialInterest()).equals("Y")){
                        term.put("RENEWAL_INTEREST_TYPE","PARTIAL_INTEREST");
                        term.put("PARTIAL_INTEREST_AMOUNT",CommonUtil.convertObjToDouble(getTxtRenewalWithdrawalAmt()));
                        double intAmt =0.0;
                        intAmt = CommonUtil.convertObjToDouble(getRenewalInterestAmount()).doubleValue()-CommonUtil.convertObjToDouble(getTxtRenewalWithdrawalAmt()).doubleValue();
                        if(intAmt>0){
                            term.put("RENEWAL_INTEREST_AMOUNT",String.valueOf(intAmt));
                        }else{
                            term.put("RENEWAL_INTEREST_AMOUNT",String.valueOf("0"));
                        }
                    }
                    
                    //Added by nithya on 04-02-2022 for KD-3287
                    if(CommonUtil.convertObjToStr(getRdoRenewalSameNumber()).equals("Y")){
                        if(CommonUtil.convertObjToStr(getRdoRenewalWithInterest()).equals("Y") || CommonUtil.convertObjToStr(getRdoRenewalWithOutInterest()).equals("Y")){
                            if(renewWithoutTransaction.equals("Y")){
                                term.put("RENEWAL_WITHOUT_TRANSACTION","Y");
                            }
                        }
                    }
                    
                    transactionDetailsTO=new LinkedHashMap();
                    if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0 && (CommonUtil.convertObjToStr(getRdoRenewalWithOutInterest()).equals("Y") || 
                    CommonUtil.convertObjToStr(getRdoRenewalpartialInterest()).equals("Y"))){                        
                        transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                        allowedTransactionDetailsTO = null; // Added by nithya on 21-03-2020 for KD 1659 - INVESTMENT COULD NOT AUTHORIZE - BATCH NOT TALLY
                        term.put("TransactionTO",transactionDetailsTO);
                    }
                    
                }
            }else if(investmentType.equals("SHARES_DCB") || investmentType.equals("SHARE_OTHER_INSTITUTIONS")){
                term.put("InvestmentsShareTO", getInvestmentsShareTO(command));
            }else if(investmentType.equals("RESERVE_FUND_DCB")){
                term.put("InvestmentsReserveFundTO", getInvestmentsReserveFundTO(command));
            } 
            //Set Investment TransTo
            if(command.equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
                term.put("InvestmentsTransTO", getInvestmentsTransTO(command));
                transactionDetailsTO=new LinkedHashMap();
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                term.put("TransactionTO",transactionDetailsTO);
                allowedTransactionDetailsTO = null; // Added by nithya on 21-03-2020 for KD 1659 - INVESTMENT COULD NOT AUTHORIZE - BATCH NOT TALLY
            }
            if(command.equals("AUTHORIZE")){
                term.put(CommonConstants.AUTHORIZEMAP,getAuthorizeMap());
                transactionDetailsTO=new LinkedHashMap();
                if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                    term.put("TransactionTO",transactionDetailsTO);
                    allowedTransactionDetailsTO = null; // Added by nithya on 21-03-2020 for KD 1659 - INVESTMENT COULD NOT AUTHORIZE - BATCH NOT TALLY
                }
                term.put("InvestmentsTransTO", getInvestmentsTransTO(command));
            
                //Renewal
                if(investmentType.equals("OTHER_BANK_CCD") || investmentType.equals("OTHER_BANK_FD")
                || investmentType.equals("OTHER_BANK_SSD")){
                    term.put("InvestmentsDepositTO", getInvestmentsDepositTO(command));
                    String renewal = CommonUtil.convertObjToStr(getRdoRenewal());
                    if(renewal.length()>0  && renewal.equals("Y")){
                        term.put("RENEWAL","RENEWAL");
                        term.put("RenewalInvestmentsDepositTO", getRenewalInvestmentsDepositTO(command));
                        term.put("RenewalInvestmentsTransTO", getRenewalInvestmentsTransTO(command));
                        if(getTxtInvestmentInternalNoTrans().length()>0){
                            term.put("DEBIT_ACCT_NO", getTxtInvestmentInternalNoTrans());
                        }
                        if(CommonUtil.convertObjToDouble(getRenewalInterestAmount()).doubleValue()>0){
                            term.put("RENEWAL_INTEREST_AMOUNT",getRenewalInterestAmount());
                        }
                        
                        
                        if(CommonUtil.convertObjToStr(getRdoRenewalWithInterest()).equals("Y")){
                            term.put("RENEWAL_INTEREST_TYPE","WITH_INTEREST");
                            term.put("AVAILABLE_BALANCE",String.valueOf(CommonUtil.convertObjToDouble(getTxtFDPricipalAmt()).doubleValue()+CommonUtil.convertObjToDouble(getRenewalInterestAmount()).doubleValue()));
                        }else if(CommonUtil.convertObjToStr(getRdoRenewalWithOutInterest()).equals("Y")){
                            term.put("RENEWAL_INTEREST_TYPE","WITHOUT_INTEREST");
                            term.put("AVAILABLE_BALANCE",String.valueOf(CommonUtil.convertObjToDouble(getTxtFDPricipalAmt()).doubleValue()));
                        }else if(CommonUtil.convertObjToStr(getRdoRenewalpartialInterest()).equals("Y")){
                            term.put("RENEWAL_INTEREST_TYPE","PARTIAL_INTEREST");
                            term.put("PARTIAL_INTEREST_AMOUNT",CommonUtil.convertObjToDouble(getTxtRenewalWithdrawalAmt()));
                            double intAmt =0.0;
                            intAmt = CommonUtil.convertObjToDouble(getRenewalInterestAmount()).doubleValue()-CommonUtil.convertObjToDouble(getTxtRenewalWithdrawalAmt()).doubleValue();
                            if(intAmt>0){
                                term.put("RENEWAL_INTEREST_AMOUNT",String.valueOf(intAmt));
                            }else{
                                term.put("RENEWAL_INTEREST_AMOUNT",String.valueOf("0"));
                            }
                            term.put("AVAILABLE_BALANCE",String.valueOf(CommonUtil.convertObjToDouble(getTxtFDPricipalAmt()).doubleValue()+intAmt));
                        }
                    }
                }
                
                
            }
            if (getRdoRenewalNewNumber().equals("Y")) {
                term.put("NEW_NUM", "NEW_NUM");
            }
            if (getRdoRenewalDiffProdNumber().equals("Y")) {
                term.put("DIFF_NUM", "DIFF_NUM");
            }
            if (getChkAddMountToDeposit().equals("Y")) {
                term.put("IS_AMOUNT_ADDED", getChkAddMountToDeposit());
                term.put("ADD_AMOUNT_TO_DEPOSIT", getTxtRenewalDepTransAmtValue());
                term.put("RENEWAL_DEP_TRANSMODE", getCbmRenewalDepTransModel());
                term.put("RENEWAL_DEP_TRANSPROD", CallForRenewalDepProductType());
                term.put("RENEWAL_DEP_TRANSID", getCbmRenewalDepTransProdType());
                term.put("RENEWAL_DEP_ID", getTxtRenewalDepositID());
                term.put("FD_INTERNAL_ACC_NO", getTxtFDInternalAccNo());
            }
            if(getRdoRenewalNewNumber().equals("Y")){
                term.put("RENEWAL_WITH_NEW_NUMBER","RENEWAL_WITH_NEW_NUMBER");
                term.put("OLD_REF_ID", getTxtFDAccountRefNO());
            }
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            if(getTdtTransactionDt()!=null && !getTdtTransactionDt().equals("")){
                if(DateUtil.dateDiff(getTdtTransactionDt(), curDate) != 0){
                    term.put("TRANS_DATE", getProperDateFormat(getTdtTransactionDt()));
                    term.put("BACK_DATED_TRANSACTION", "BACK_DATED_TRANSACTION");
                }else{
                    term.put("TRANS_DATE", getProperDateFormat(getTdtTransactionDt()));
                }
            }else{
                term.put("TRANS_DATE", curDate.clone());
            }
            int countRec=0;
            HashMap proxyResultMap=null;
            if(countRec==0){
                proxyResultMap = proxy.execute(term, map);
                setProxyReturnMap(proxyResultMap);
                System.out.println("######## proxyResultMap :"+proxyResultMap);
            }else
                ClientUtil.displayAlert("THIS PRODUCT HAVING ACCOUNT");
            setResult(getActionType());   
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    public Date getProperDateFormat(Object obj) {
        Date properDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            properDt = (Date) curDate.clone();
            properDt.setDate(tempDt.getDate());
            properDt.setMonth(tempDt.getMonth());
            properDt.setYear(tempDt.getYear());
        }
        return properDt;
    }
    
    /** Resetting all tbe Fields of UI */
    public  void resetForm(){
        setCboInvestmentBehaves("");
        setCboIntPayFreq("");
        setInvestmentID("");
        setInvestmentName("");
        setIssueDt(null);
        setYears(new Double(0).doubleValue());
        setMonths(new Double(0).doubleValue());
        setDays(new Double(0).doubleValue());
        setCouponRate(new Double(0).doubleValue());
        setFaceValue(new Double(0).doubleValue());
        setMaturityDate(null);
        setSLR("");
        setCallOption("");
        setSetUpOption("");
        setTxtBankCode("");
        setTxtBranchCode("");
        setCboSecurityTypeCode("");
        setRdoSecurityType("");
        setTxtRemarks("");
        setTxtOtherName("");
        setCboInvestmentTypeSBorCA("");
        setTxtInvestmentIDTransSBorCA("");
        setTxtInvestmentRefNoTrans("");
        setTxtChequeNo("");
        setTxtInvestmentAmount(null);
        setTxtNarration("");
        setRdoRenewal("");
        setRdoRenewalDiffProdNumber("");
        setRdoRenewalSameNumber("");
        setRdoRenewalNewNumber("");
        setRdoRenewalWithInterest("");
        setRdoRenewalWithOutInterest("");
        setRdoRenewalpartialInterest("");
        setRenewalInterestAmount("");
        setTxtRenewalFDInternalAccNo("");
        setTxtRenewalWithdrawalAmt("");
        chequeMap = null;
        deletedChequeMap= null;
        setCbmRenewalDepTransModel("");
        setCbmRenewalDepTransProdType("");
        setCboRenewalDepTransProdId("");
        setTxtRenewalDepTransAmtValue("");
        setChkAddMountToDeposit("");
        setTxtRenewalDepositID("");
        setTxtInvestTDS(0);
        setTdtTransactionDt(null);
        setTdtRenewalDt(null);
        notifyObservers();
    }
    
    public void resetTableValues(){
        tblCheckBookTable.setDataArrayList(null,tableTitle);
    }
    
     
    
    
    /** This checks whether user entered sharetype already exists if it exists
     * it returns true otherwise false */
    public boolean isInvsetMentMasterTypeExists(String InvestmentName){
        boolean exists = false;
        ArrayList resultList =(ArrayList) ClientUtil.executeQuery("getSelectInvestmentMaster",null);
        if(resultList != null){
            for(int i=0; i<resultList.size(); i++){
                HashMap resultMap = (HashMap)resultList.get(i);
                String investProdType =CommonUtil.convertObjToStr(resultMap.get("INVESTMENT_NAME"));
                if(investProdType.equalsIgnoreCase(InvestmentName)){
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            
            mapData = proxy.executeQuery(whereMap, map);
            
            System.out.println("####### data : "+mapData);
            if(mapData.containsKey("InvestmentsOperativeTO")){
                objInvestmentsOperativeTO = (InvestmentsOperativeTO) ((List) mapData.get("InvestmentsOperativeTO")).get(0);
                setInvestmentsOperativeTO(objInvestmentsOperativeTO);
                if(mapData.containsKey("chequeListTO")){        // CHEQUE_DETAILS
                    if( chequeMap == null ){
                        chequeMap = new LinkedHashMap();
                    }
                    chequeMap = (LinkedHashMap)mapData.get("chequeListTO");
                    ArrayList addList =new ArrayList(chequeMap.keySet());
                    for(int i=0;i<addList.size();i++){
                        InvestmentsChequeTO  objInvestmentsChequeTO = (InvestmentsChequeTO)  chequeMap.get(addList.get(i));
                        objInvestmentsChequeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        ArrayList incTabRow = new ArrayList();
                        incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getSlNo()));
                        incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getIssueDt()));
                        incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getFromNo()));
                        incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getToNo()));
                        incTabRow.add(CommonUtil.convertObjToStr(objInvestmentsChequeTO.getNoOfCheques()));
                        tblCheckBookTable.addRow(incTabRow);
                    }
                }
            }
            if(mapData.containsKey("InvestmentsDepositTO")){
                objInvestmentsDepositTO = (InvestmentsDepositTO) ((List) mapData.get("InvestmentsDepositTO")).get(0);
                setInvestmentsDepositTO(objInvestmentsDepositTO);
            }
            
            if(mapData.containsKey("RenewalInvestmentsDepositTO")){
                objInvestmentsDepositTO = (InvestmentsDepositTO) ((List) mapData.get("RenewalInvestmentsDepositTO")).get(0);
                setRenewalInvestmentsDepositTO(objInvestmentsDepositTO);
            }
            
            
            if(mapData.containsKey("InvestmentsShareTO")){
                objInvestmentsShareTO = (InvestmentsShareTO) ((List) mapData.get("InvestmentsShareTO")).get(0);
                setInvestmentsShareTO(objInvestmentsShareTO);
            }
            if(mapData.containsKey("InvestmentsRFTO")){
                objInvestmentsRFTO = (InvestmentsRFTO) ((List) mapData.get("InvestmentsRFTO")).get(0);
                setInvestmentsReserveFundTO(objInvestmentsRFTO);
            }
            if(mapData.containsKey("InvestmentsTransTO")){
                InvestmentsTransTO objInvestmentsTransTO= new InvestmentsTransTO();
                objInvestmentsTransTO = (InvestmentsTransTO) ((List) mapData.get("InvestmentsTransTO")).get(0);
                HashMap dataMap= new HashMap();
                dataMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(ProxyParameters.BRANCH_ID));
                dataMap.put("INVEST_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
                dataMap.put("BATCH_ID", CommonUtil.convertObjToStr(objInvestmentsTransTO.getBatchID()));
                List dataLst = ClientUtil.executeQuery("getTDSInvestmentRenewal", dataMap);
                if(dataLst!=null && dataLst.size()>0){
                    dataMap= (HashMap) dataLst.get(0);
                    objInvestmentsTransTO.setInvestTDS(CommonUtil.convertObjToDouble(dataMap.get("TDS_AMOUNT")));                            
                }
                setInvestmentsTransTO(objInvestmentsTransTO);
                //Set Trans_Details
                List  list = (List) mapData.get("TransactionTO");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e,true);
            
        }
    }
    
    private void setInvestmentsTransTO(InvestmentsTransTO objInvestmentsTransTO){
        setBatch_Id(CommonUtil.convertObjToStr(objInvestmentsTransTO.getBatchID()));
        setTxtInvestmentAmount(objInvestmentsTransTO.getInvestmentAmount());
        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objInvestmentsTransTO.getInvestmentBehaves())));
        setInvestmentID(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentID()));
        setInvestmentName(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestmentName()));
        setCboInvestmentTypeSBorCA(CommonUtil.convertObjToStr(getCbmInvestmentTypeSBorCA().getDataForKey(objInvestmentsTransTO.getCboInvestmentType())));
        setTxtInvestmentIDTransSBorCA(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtInvestmentIDTransSBorCA()));
        setTxtInvestmentRefNoTrans(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtInvestmentRefNoTrans()));
        setRdoSBorCA(CommonUtil.convertObjToStr(objInvestmentsTransTO.getRdoSBorCA()));
        setDepositInternalAccNo(CommonUtil.convertObjToStr(objInvestmentsTransTO.getInvestment_internal_Id()));
        setTxtInvestmentInternalNoTrans(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtInvestmentInternalNoTrans()));
        setTxtChequeNo(CommonUtil.convertObjToStr(objInvestmentsTransTO.getTxtChequeNo()));
        setTxtNarration(CommonUtil.convertObjToStr(objInvestmentsTransTO.getNarration()));
        System.out.println("INVEST TDS1111:"+objInvestmentsTransTO.getInvestTDS());
        setTxtInvestTDS(objInvestmentsTransTO.getInvestTDS());
        setTdtTransactionDt(objInvestmentsTransTO.getTransDT());
        setChanged();
        notifyObservers();
    }
    
    /** Sets all the InvsetmentMaster values to the OB varibles  there by populatin the UI fields */
    private void setInvestmentsOperativeTO(InvestmentsOperativeTO objInvestmentsOperativeTO){
        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objInvestmentsOperativeTO.getInvestmentType())));
        setTxtSBInternalAccNo(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentId()));
        setInvestmentID(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentProdId()));
        setInvestmentName(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentProdDesc()));
        setTxtSBAgencyName(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getAgencyName()));
        setTxtSBAccountRefNO(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getInvestmentRefNo()));
        setTdtSBAccOpenDt(objInvestmentsOperativeTO.getInvestmentIssueDt());
        setTxtSBAreaOperatorDetails(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getOperatorDetails()));
        setRdoCheckBookAllowed(CommonUtil.convertObjToStr(objInvestmentsOperativeTO.getCheckAllowed()));
    }
    
    private void setInvestmentsDepositTO(InvestmentsDepositTO objgetInvestmentsDepositTO){
        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objgetInvestmentsDepositTO.getInvestmentType())));
        setTxtFDInternalAccNo(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentId()));
        setInvestmentID(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentProdId()));
        setInvestmentName(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentProdDesc()));
        setTxtFDAgencyName(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getAgencyName()));
        setTdtFDAccOpenDt(objgetInvestmentsDepositTO.getInvestmentIssueDt());
        setTdtFDEffectiveDt(objgetInvestmentsDepositTO.getInvestmentEffectiveDt());
        setTxtFDAccountRefNO(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentRefNo()));
        setTxtFDPricipalAmt(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getPrincipalAmount()));
        setTxtRenewalWithdrawalAmt(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRenewalPartialAmt()));        
        setTxtFDInvestmentPeriod_Years(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentPeriodYy()));
        setTxtFDInvestmentPeriod_Months(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentPeriodMm()));
        setTxtFDInvestmentPeriod_Days(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentPeriodDd()));
        setTxtFDRateOfInt(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRateOfInterest()));
        setTdtFDMaturityDt(objgetInvestmentsDepositTO.getMaturityDt());
        setTxtFDMaturityAmt(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getMaturityAmount()));
        setCboFDInterestPaymentFrequency(CommonUtil.convertObjToStr(getCbmFDInterestPaymentFrequency().getDataForKey(String.valueOf(CommonUtil.convertObjToInt(objgetInvestmentsDepositTO.getIntpayFreq()))))); 
        setRdoWithPrincipal(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getWithPrincipal()));
        setRdoRenewal(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRenewal()));
        setRdoRenewalSameNumber(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRenewalSameNo()));
        setRdoRenewalNewNumber(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRenewalNewNo()));
        setRdoRenewalDiffProdNumber(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRenewalDiffProdNo()));
        setRdoRenewalWithInterest(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRenewalWithInterest()));
        setRdoRenewalWithOutInterest(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRenewalWithOutInterest()));
        setRdoRenewalpartialInterest(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRenewalPartialInterest()));
        setRdoWithInterest(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getWithInterest()));
        setTxtFDInterestReceivable(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInterestReceivable()));
        setTxtFDInterestReceived(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInterestReceived()));
        setTxtFDPeriodicIntrest(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getPeriodicIntrest()));
        setTdtFDIntReceivedTillDt(objgetInvestmentsDepositTO.getIntRecTillDt());
        setTdtRenewalDt(objgetInvestmentsDepositTO.getRenewalDt());
    }
    
    private void setRenewalInvestmentsDepositTO(InvestmentsDepositTO objgetInvestmentsDepositTO){
        setTxtRenewalFDPeriodicIntrest(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getPeriodicIntrest()));
        setCboRenewalInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objgetInvestmentsDepositTO.getInvestmentType())));
        setTxtRenewalFDInternalAccNo(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentId()));
        setRenewalInvestmentID(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentProdId()));
        setRenewalInvestmentName(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentProdDesc()));
        setTxtRenewalFDAgencyName(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getAgencyName()));
        setTdtRenewalFDAccOpenDt(objgetInvestmentsDepositTO.getInvestmentIssueDt());
        setTdtRenewalFDEffectiveDt(objgetInvestmentsDepositTO.getInvestmentEffectiveDt());
        setTxtRenewalFDAccountRefNO(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentRefNo()));
        setTxtRenewalFDPricipalAmt(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getPrincipalAmount()));
        setTxtRenewalFDInvestmentPeriod_Years(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentPeriodYy()));
        setTxtRenewalFDInvestmentPeriod_Months(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentPeriodMm()));
        setTxtRenewalFDInvestmentPeriod_Days(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInvestmentPeriodDd()));
        setTxtRenewalFDRateOfInt(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getRateOfInterest()));
        setTdtRenewalFDMaturityDt(objgetInvestmentsDepositTO.getMaturityDt());
        setTxtRenewalFDMaturityAmt(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getMaturityAmount()));
        setTxtRenewalFDInterestReceivable(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getInterestReceivable()));
        setTdtRenewalDt(objgetInvestmentsDepositTO.getRenewalDt());
        setCboRenewalFDInterestPaymentFrequency(CommonUtil.convertObjToStr(getCbmFDInterestPaymentFrequency().getDataForKey(String.valueOf(CommonUtil.convertObjToInt(objgetInvestmentsDepositTO.getIntpayFreq()))))); 
//        setRdoRenewalWithPrincipal(CommonUtil.convertObjToStr(objgetInvestmentsDepositTO.getWithPrincipal()));
    }
    
    private void setInvestmentsShareTO(InvestmentsShareTO objgetInvestmentsShareTO){
        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objgetInvestmentsShareTO.getInvestmentType())));
        setTxtShareInternalAccNo(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getInvestmentId()));
        setInvestmentID(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getInvestmentProdId()));
        setInvestmentName(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getInvestmentProdDesc()));
        setTxtShareType(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getShareType()));
        setTxtShareMemberID(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getInvestmentRefNo()));
        setTdtShareAccOpenDt(objgetInvestmentsShareTO.getInvestmentIssueDt());
        setTxtNoOfShares(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getNoOfShares()));
        setTxtShareValue(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getShareValue()));
        setTxtShareFaceValue(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getFaceValue()));
        setTxtFeesPaid(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getFeesPaid()));
        setTxtShareAgencyName(CommonUtil.convertObjToStr(objgetInvestmentsShareTO.getAgencyName()));
    }
    
    private void setInvestmentsReserveFundTO(InvestmentsRFTO objgetInvestmentsRFTO){
        setCboInvestmentBehaves(CommonUtil.convertObjToStr(getCbmInvestmentBehaves().getDataForKey(objgetInvestmentsRFTO.getInvestmentType())));
        setTxtRFInternalAccNo(CommonUtil.convertObjToStr(objgetInvestmentsRFTO.getInvestmentId()));
        setInvestmentID(CommonUtil.convertObjToStr(objgetInvestmentsRFTO.getInvestmentProdId()));
        setInvestmentName(CommonUtil.convertObjToStr(objgetInvestmentsRFTO.getInvestmentProdDesc()));
        setTxtRFAgencyName(CommonUtil.convertObjToStr(objgetInvestmentsRFTO.getAgencyName()));
        setTxtRFAccountRefNO(CommonUtil.convertObjToStr(objgetInvestmentsRFTO.getInvestmentRefNo()));
        setTdtRFAccOpenDt(objgetInvestmentsRFTO.getInvestmentIssueDt());
        setTxtRFPricipalAmt(CommonUtil.convertObjToStr(objgetInvestmentsRFTO.getAmount()));
    }

    public ComboBoxModel getCboRenewalDepTransModel() {
        return cbmRenewalDepTransModel;
    }

    public void setCboRenewalDepTransModel(ComboBoxModel cboRenewalDepTransModel) {
        this.cbmRenewalDepTransModel = cboRenewalDepTransModel;
    }

    public String getCbmRenewalDepTransModel() {
        return cboRenewalDepTransModel;
    }

    public void setCbmRenewalDepTransModel(String cbmRenewalDepTransModel) {
        this.cboRenewalDepTransModel = cbmRenewalDepTransModel;
    }

    public String getCbmRenewalDepTransProdType() {
        return cboRenewalDepTransProdType;
    }

    public void setCbmRenewalDepTransProdType(String cbmRenewalDepTransProdType) {
        this.cboRenewalDepTransProdType = cbmRenewalDepTransProdType;
    }

    public ComboBoxModel getCboRenewalDepTransProdType() {
        return cbmRenewalDepTransProdType;
    }

    public void setCboRenewalDepTransProdType(ComboBoxModel cboRenewalDepTransProdType) {
        this.cbmRenewalDepTransProdType = cboRenewalDepTransProdType;
    }
//

    public ComboBoxModel getCbmRenewalDepTransProdId() {
        return cbmRenewalDepTransProdId;
    }

    public void setCbmRenewalDepTransProdId(ComboBoxModel cbmRenewalDepTransProdId) {
        this.cbmRenewalDepTransProdId = cbmRenewalDepTransProdId;
    }

    public String getCboRenewalDepTransProdId() {
        return cboRenewalDepTransProdId;
    }

    public void setCboRenewalDepTransProdId(String cboRenewalDepTransProdId) {
        this.cboRenewalDepTransProdId = cboRenewalDepTransProdId;
    }

    public String getTxtRenewalDepTransAmtValue() {
        return txtRenewalDepTransAmtValue;
    }

    public void setTxtRenewalDepTransAmtValue(String txtRenewalDepTransAmtValue) {
        this.txtRenewalDepTransAmtValue = txtRenewalDepTransAmtValue;
    }

    public String getChkAddMountToDeposit() {
        return chkAddMountToDeposit;
    }

    public void setChkAddMountToDeposit(String chkAddMountToDeposit) {
        this.chkAddMountToDeposit = chkAddMountToDeposit;
    }

    public String getTxtRenewalDepositID() {
        return txtRenewalDepositID;
    }

    public void setTxtRenewalDepositID(String txtRenewalDepositID) {
        this.txtRenewalDepositID = txtRenewalDepositID;
    }
    
    
    
    /**
     * Getter for property cbmInvestmentBehaves.
     * @return Value of property cbmInvestmentBehaves.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInvestmentBehaves() {
        return cbmInvestmentBehaves;
    }
    
    /**
     * Setter for property cbmInvestmentBehaves.
     * @param cbmInvestmentBehaves New value of property cbmInvestmentBehaves.
     */
    public void setCbmInvestmentBehaves(com.see.truetransact.clientutil.ComboBoxModel cbmInvestmentBehaves) {
        this.cbmInvestmentBehaves = cbmInvestmentBehaves;
    }
    
    /**
     * Getter for property cboInvestmentBehaves.
     * @return Value of property cboInvestmentBehaves.
     */
    public java.lang.String getCboInvestmentBehaves() {
        return cboInvestmentBehaves;
    }
    
    /**
     * Setter for property cboInvestmentBehaves.
     * @param cboInvestmentBehaves New value of property cboInvestmentBehaves.
     */
    public void setCboInvestmentBehaves(java.lang.String cboInvestmentBehaves) {
        this.cboInvestmentBehaves = cboInvestmentBehaves;
    }
    
    /**
     * Getter for property cbmIntPayFreq.
     * @return Value of property cbmIntPayFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntPayFreq() {
        return cbmIntPayFreq;
    }
    
    /**
     * Setter for property cbmIntPayFreq.
     * @param cbmIntPayFreq New value of property cbmIntPayFreq.
     */
    public void setCbmIntPayFreq(com.see.truetransact.clientutil.ComboBoxModel cbmIntPayFreq) {
        this.cbmIntPayFreq = cbmIntPayFreq;
    }
    
    /**
     * Getter for property IntPayFreq.
     * @return Value of property IntPayFreq.
     */
    
    
    /**
     * Getter for property investmentID.
     * @return Value of property investmentID.
     */
    public java.lang.String getInvestmentID() {
        return investmentID;
    }
    
    /**
     * Setter for property investmentID.
     * @param investmentID New value of property investmentID.
     */
    public void setInvestmentID(java.lang.String investmentID) {
        this.investmentID = investmentID;
    }
    
    /**
     * Getter for property cboIntPayFreq.
     * @return Value of property cboIntPayFreq.
     */
    public java.lang.String getCboIntPayFreq() {
        return cboIntPayFreq;
    }
    
    /**
     * Setter for property cboIntPayFreq.
     * @param cboIntPayFreq New value of property cboIntPayFreq.
     */
    public void setCboIntPayFreq(java.lang.String cboIntPayFreq) {
        this.cboIntPayFreq = cboIntPayFreq;
    }
    
    /**
     * Getter for property investmentName.
     * @return Value of property investmentName.
     */
    public java.lang.String getInvestmentName() {
        return investmentName;
    }
    
    /**
     * Setter for property investmentName.
     * @param investmentName New value of property investmentName.
     */
    public void setInvestmentName(java.lang.String investmentName) {
        this.investmentName = investmentName;
    }
    
    /**
     * Getter for property IssueDt.
     * @return Value of property IssueDt.
     */
    public java.util.Date getIssueDt() {
        return IssueDt;
    }
    
    /**
     * Setter for property IssueDt.
     * @param IssueDt New value of property IssueDt.
     */
    public void setIssueDt(java.util.Date IssueDt) {
        this.IssueDt = IssueDt;
    }
    
    /**
     * Getter for property years.
     * @return Value of property years.
     */
    public double getYears() {
        return years;
    }
    
    /**
     * Setter for property years.
     * @param years New value of property years.
     */
    public void setYears(double years) {
        this.years = years;
    }
    
    /**
     * Getter for property months.
     * @return Value of property months.
     */
    public double getMonths() {
        return months;
    }
    
    /**
     * Setter for property months.
     * @param months New value of property months.
     */
    public void setMonths(double months) {
        this.months = months;
    }
    
    /**
     * Getter for property days.
     * @return Value of property days.
     */
    public double getDays() {
        return days;
    }
    
    /**
     * Setter for property days.
     * @param days New value of property days.
     */
    public void setDays(double days) {
        this.days = days;
    }
    
    /**
     * Getter for property maturityDate.
     * @return Value of property maturityDate.
     */
    public java.util.Date getMaturityDate() {
        return maturityDate;
    }
    
    /**
     * Setter for property maturityDate.
     * @param maturityDate New value of property maturityDate.
     */
    public void setMaturityDate(java.util.Date maturityDate) {
        this.maturityDate = maturityDate;
    }
    
    /**
     * Getter for property faceValue.
     * @return Value of property faceValue.
     */
    public double getFaceValue() {
        return faceValue;
    }
    
    /**
     * Setter for property faceValue.
     * @param faceValue New value of property faceValue.
     */
    public void setFaceValue(double faceValue) {
        this.faceValue = faceValue;
    }
    
    /**
     * Getter for property couponRate.
     * @return Value of property couponRate.
     */
    public double getCouponRate() {
        return couponRate;
    }
    
    /**
     * Setter for property couponRate.
     * @param couponRate New value of property couponRate.
     */
    public void setCouponRate(double couponRate) {
        this.couponRate = couponRate;
    }
    
    /**
     * Getter for property SLR.
     * @return Value of property SLR.
     */
    public java.lang.String getSLR() {
        return SLR;
    }
    
    /**
     * Setter for property SLR.
     * @param SLR New value of property SLR.
     */
    public void setSLR(java.lang.String SLR) {
        this.SLR = SLR;
    }
    
    /**
     * Getter for property callOption.
     * @return Value of property callOption.
     */
    public java.lang.String getCallOption() {
        return callOption;
    }
    
    /**
     * Setter for property callOption.
     * @param callOption New value of property callOption.
     */
    public void setCallOption(java.lang.String callOption) {
        this.callOption = callOption;
    }
    
    /**
     * Getter for property putOption.
     * @return Value of property putOption.
     */
    public java.lang.String getPutOption() {
        return putOption;
    }
    
    /**
     * Setter for property putOption.
     * @param putOption New value of property putOption.
     */
    public void setPutOption(java.lang.String putOption) {
        this.putOption = putOption;
    }
    
    /**
     * Getter for property setUpOption.
     * @return Value of property setUpOption.
     */
    public java.lang.String getSetUpOption() {
        return setUpOption;
    }
    
    /**
     * Setter for property setUpOption.
     * @param setUpOption New value of property setUpOption.
     */
    public void setSetUpOption(java.lang.String setUpOption) {
        this.setUpOption = setUpOption;
    }
    
    /**
     * Getter for property availableNoOfUnits.
     * @return Value of property availableNoOfUnits.
     */
    public double getAvailableNoOfUnits() {
        return availableNoOfUnits;
    }
    
    /**
     * Setter for property availableNoOfUnits.
     * @param availableNoOfUnits New value of property availableNoOfUnits.
     */
    public void setAvailableNoOfUnits(double availableNoOfUnits) {
        this.availableNoOfUnits = availableNoOfUnits;
    }
    
    /**
     * Getter for property lastIntPaidDate.
     * @return Value of property lastIntPaidDate.
     */
    public java.util.Date getLastIntPaidDate() {
        return lastIntPaidDate;
    }
    
    /**
     * Setter for property lastIntPaidDate.
     * @param lastIntPaidDate New value of property lastIntPaidDate.
     */
    public void setLastIntPaidDate(java.util.Date lastIntPaidDate) {
        this.lastIntPaidDate = lastIntPaidDate;
    }
    
    /**
     * Getter for property totalPremiumPaid.
     * @return Value of property totalPremiumPaid.
     */
    public double getTotalPremiumPaid() {
        return totalPremiumPaid;
    }
    
    /**
     * Setter for property totalPremiumPaid.
     * @param totalPremiumPaid New value of property totalPremiumPaid.
     */
    public void setTotalPremiumPaid(double totalPremiumPaid) {
        this.totalPremiumPaid = totalPremiumPaid;
    }
    
    /**
     * Getter for property totalPremiumCollected.
     * @return Value of property totalPremiumCollected.
     */
    public double getTotalPremiumCollected() {
        return totalPremiumCollected;
    }
    
    /**
     * Setter for property totalPremiumCollected.
     * @param totalPremiumCollected New value of property totalPremiumCollected.
     */
    public void setTotalPremiumCollected(double totalPremiumCollected) {
        this.totalPremiumCollected = totalPremiumCollected;
    }
    
    /**
     * Getter for property totalInterestPaid.
     * @return Value of property totalInterestPaid.
     */
    public double getTotalInterestPaid() {
        return totalInterestPaid;
    }
    
    /**
     * Setter for property totalInterestPaid.
     * @param totalInterestPaid New value of property totalInterestPaid.
     */
    public void setTotalInterestPaid(double totalInterestPaid) {
        this.totalInterestPaid = totalInterestPaid;
    }
    
    /**
     * Getter for property tTotalInterestCollected.
     * @return Value of property tTotalInterestCollected.
     */
    public double getTotalInterestCollected() {
        return totalInterestCollected;
    }
    
    /**
     * Setter for property tTotalInterestCollected.
     * @param tTotalInterestCollected New value of property tTotalInterestCollected.
     */
    public void setTotalInterestCollected(double tTotalInterestCollected) {
        this.totalInterestCollected = tTotalInterestCollected;
    }
    
    /**
     * Getter for property classification.
     * @return Value of property classification.
     */
    public java.lang.String getClassification() {
        return classification;
    }
    
    /**
     * Setter for property classification.
     * @param classification New value of property classification.
     */
    public void setClassification(java.lang.String classification) {
        this.classification = classification;
    }
    
    /**
     * Getter for property initiatedDate.
     * @return Value of property initiatedDate.
     */
    public java.util.Date getInitiatedDate() {
        return initiatedDate;
    }
    
    /**
     * Setter for property initiatedDate.
     * @param initiatedDate New value of property initiatedDate.
     */
    public void setInitiatedDate(java.util.Date initiatedDate) {
        this.initiatedDate = initiatedDate;
    }
    
    /**
     * Getter for property outstandingAmount.
     * @return Value of property outstandingAmount.
     */
    public java.lang.Double getOutstandingAmount() {
        return outstandingAmount;
    }
    
    /**
     * Setter for property outstandingAmount.
     * @param outstandingAmount New value of property outstandingAmount.
     */
    public void setOutstandingAmount(java.lang.Double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }
    
    /**
     * Getter for property maturityAmount.
     * @return Value of property maturityAmount.
     */
    public java.lang.Double getMaturityAmount() {
        return maturityAmount;
    }
    
    /**
     * Setter for property maturityAmount.
     * @param maturityAmount New value of property maturityAmount.
     */
    public void setMaturityAmount(java.lang.Double maturityAmount) {
        this.maturityAmount = maturityAmount;
    }
    
    /**
     * Getter for property txtProductID.
     * @return Value of property txtProductID.
     */
    
    
    
    /**
     * Getter for property putOptionNoofYears.
     * @return Value of property putOptionNoofYears.
     */
    public java.lang.Double getPutOptionNoofYears() {
        return putOptionNoofYears;
    }
    
    /**
     * Setter for property putOptionNoofYears.
     * @param putOptionNoofYears New value of property putOptionNoofYears.
     */
    public void setPutOptionNoofYears(java.lang.Double putOptionNoofYears) {
        this.putOptionNoofYears = putOptionNoofYears;
    }
    
    /**
     * Getter for property callOptionNoofYears.
     * @return Value of property callOptionNoofYears.
     */
    public java.lang.Double getCallOptionNoofYears() {
        return callOptionNoofYears;
    }
    
    /**
     * Setter for property callOptionNoofYears.
     * @param callOptionNoofYears New value of property callOptionNoofYears.
     */
    public void setCallOptionNoofYears(java.lang.Double callOptionNoofYears) {
        this.callOptionNoofYears = callOptionNoofYears;
    }
    
    /**
     * Getter for property setUpOptionNoofYears.
     * @return Value of property setUpOptionNoofYears.
     */
    public java.lang.Double getSetUpOptionNoofYears() {
        return setUpOptionNoofYears;
    }
    
    /**
     * Setter for property setUpOptionNoofYears.
     * @param setUpOptionNoofYears New value of property setUpOptionNoofYears.
     */
    public void setSetUpOptionNoofYears(java.lang.Double setUpOptionNoofYears) {
        this.setUpOptionNoofYears = setUpOptionNoofYears;
    }
    
    /**
     * Getter for property txtBranchCode.
     * @return Value of property txtBranchCode.
     */
    public java.lang.String getTxtBranchCode() {
        return txtBranchCode;
    }
    
    /**
     * Setter for property txtBranchCode.
     * @param txtBranchCode New value of property txtBranchCode.
     */
    public void setTxtBranchCode(java.lang.String txtBranchCode) {
        this.txtBranchCode = txtBranchCode;
    }
    
    /**
     * Getter for property txtBankCode.
     * @return Value of property txtBankCode.
     */
    public java.lang.String getTxtBankCode() {
        return txtBankCode;
    }
    
    /**
     * Setter for property txtBankCode.
     * @param txtBankCode New value of property txtBankCode.
     */
    public void setTxtBankCode(java.lang.String txtBankCode) {
        this.txtBankCode = txtBankCode;
    }
    
    /**
     * Getter for property rdoSecurityType.
     * @return Value of property rdoSecurityType.
     */
    public java.lang.String getRdoSecurityType() {
        return rdoSecurityType;
    }
    
    /**
     * Setter for property rdoSecurityType.
     * @param rdoSecurityType New value of property rdoSecurityType.
     */
    public void setRdoSecurityType(java.lang.String rdoSecurityType) {
        this.rdoSecurityType = rdoSecurityType;
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
    }
    
    /**
     * Getter for property cboSecurityTypeCode.
     * @return Value of property cboSecurityTypeCode.
     */
    public java.lang.String getCboSecurityTypeCode() {
        return cboSecurityTypeCode;
    }
    
    /**
     * Setter for property cboSecurityTypeCode.
     * @param cboSecurityTypeCode New value of property cboSecurityTypeCode.
     */
    public void setCboSecurityTypeCode(java.lang.String cboSecurityTypeCode) {
        this.cboSecurityTypeCode = cboSecurityTypeCode;
    }
    
    /**
     * Getter for property cbmState.
     * @return Value of property cbmState.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmState() {
        return cbmState;
    }
    
    /**
     * Setter for property cbmState.
     * @param cbmState New value of property cbmState.
     */
    public void setCbmState(com.see.truetransact.clientutil.ComboBoxModel cbmState) {
        this.cbmState = cbmState;
    }
    
    /**
     * Getter for property txtOtherName.
     * @return Value of property txtOtherName.
     */
    public java.lang.String getTxtOtherName() {
        return txtOtherName;
    }
    
    /**
     * Setter for property txtOtherName.
     * @param txtOtherName New value of property txtOtherName.
     */
    public void setTxtOtherName(java.lang.String txtOtherName) {
        this.txtOtherName = txtOtherName;
    }
    
    /**
     * Getter for property cboState.
     * @return Value of property cboState.
     */
    public java.lang.String getCboState() {
        return cboState;
    }
    
    /**
     * Setter for property cboState.
     * @param cboState New value of property cboState.
     */
    public void setCboState(java.lang.String cboState) {
        this.cboState = cboState;
    }
    
    /**
     * Getter for property preCloserRate.
     * @return Value of property preCloserRate.
     */
    public java.lang.String getPreCloserRate() {
        return preCloserRate;
    }
    
    /**
     * Setter for property preCloserRate.
     * @param preCloserRate New value of property preCloserRate.
     */
    public void setPreCloserRate(java.lang.String preCloserRate) {
        this.preCloserRate = preCloserRate;
    }
    
    /**
     * Getter for property closerType.
     * @return Value of property closerType.
     */
    public java.lang.String getCloserType() {
        return closerType;
    }
    
    /**
     * Setter for property closerType.
     * @param closerType New value of property closerType.
     */
    public void setCloserType(java.lang.String closerType) {
        this.closerType = closerType;
    }
    
    /**
     * Getter for property amortizationAmt.
     * @return Value of property amortizationAmt.
     */
    public java.lang.String getAmortizationAmt() {
        return amortizationAmt;
    }
    
    /**
     * Setter for property amortizationAmt.
     * @param amortizationAmt New value of property amortizationAmt.
     */
    public void setAmortizationAmt(java.lang.String amortizationAmt) {
        this.amortizationAmt = amortizationAmt;
    }
    
    /**
     * Getter for property closerDate.
     * @return Value of property closerDate.
     */
    public java.util.Date getCloserDate() {
        return closerDate;
    }
    
    /**
     * Setter for property closerDate.
     * @param closerDate New value of property closerDate.
     */
    public void setCloserDate(java.util.Date closerDate) {
        this.closerDate = closerDate;
    }
    
    /**
     * Getter for property txtSBAgencyName.
     * @return Value of property txtSBAgencyName.
     */
    public java.lang.String getTxtSBAgencyName() {
        return txtSBAgencyName;
    }
    
    /**
     * Setter for property txtSBAgencyName.
     * @param txtSBAgencyName New value of property txtSBAgencyName.
     */
    public void setTxtSBAgencyName(java.lang.String txtSBAgencyName) {
        this.txtSBAgencyName = txtSBAgencyName;
    }
    
    /**
     * Getter for property txtSBAccountRefNO.
     * @return Value of property txtSBAccountRefNO.
     */
    public java.lang.String getTxtSBAccountRefNO() {
        return txtSBAccountRefNO;
    }
    
    /**
     * Setter for property txtSBAccountRefNO.
     * @param txtSBAccountRefNO New value of property txtSBAccountRefNO.
     */
    public void setTxtSBAccountRefNO(java.lang.String txtSBAccountRefNO) {
        this.txtSBAccountRefNO = txtSBAccountRefNO;
    }
    
    /**
     * Getter for property txtSBInternalAccNo.
     * @return Value of property txtSBInternalAccNo.
     */
    public java.lang.String getTxtSBInternalAccNo() {
        return txtSBInternalAccNo;
    }
    
    /**
     * Setter for property txtSBInternalAccNo.
     * @param txtSBInternalAccNo New value of property txtSBInternalAccNo.
     */
    public void setTxtSBInternalAccNo(java.lang.String txtSBInternalAccNo) {
        this.txtSBInternalAccNo = txtSBInternalAccNo;
    }
    
    /**
     * Getter for property txtSBAreaOperatorDetails.
     * @return Value of property txtSBAreaOperatorDetails.
     */
    public java.lang.String getTxtSBAreaOperatorDetails() {
        return txtSBAreaOperatorDetails;
    }
    
    /**
     * Setter for property txtSBAreaOperatorDetails.
     * @param txtSBAreaOperatorDetails New value of property txtSBAreaOperatorDetails.
     */
    public void setTxtSBAreaOperatorDetails(java.lang.String txtSBAreaOperatorDetails) {
        this.txtSBAreaOperatorDetails = txtSBAreaOperatorDetails;
    }
    
    /**
     * Getter for property rdoCheckBookAllowed.
     * @return Value of property rdoCheckBookAllowed.
     */
    public java.lang.String getRdoCheckBookAllowed() {
        return rdoCheckBookAllowed;
    }
    
    /**
     * Setter for property rdoCheckBookAllowed.
     * @param rdoCheckBookAllowed New value of property rdoCheckBookAllowed.
     */
    public void setRdoCheckBookAllowed(java.lang.String rdoCheckBookAllowed) {
        this.rdoCheckBookAllowed = rdoCheckBookAllowed;
    }
    
    /**
     * Getter for property tdtSBAccOpenDt.
     * @return Value of property tdtSBAccOpenDt.
     */
    public java.util.Date getTdtSBAccOpenDt() {
        return tdtSBAccOpenDt;
    }
    
    /**
     * Setter for property tdtSBAccOpenDt.
     * @param tdtSBAccOpenDt New value of property tdtSBAccOpenDt.
     */
    public void setTdtSBAccOpenDt(java.util.Date tdtSBAccOpenDt) {
        this.tdtSBAccOpenDt = tdtSBAccOpenDt;
    }
    
    /**
     * Getter for property txtFDAgencyName.
     * @return Value of property txtFDAgencyName.
     */
    public java.lang.String getTxtFDAgencyName() {
        return txtFDAgencyName;
    }
    
    /**
     * Setter for property txtFDAgencyName.
     * @param txtFDAgencyName New value of property txtFDAgencyName.
     */
    public void setTxtFDAgencyName(java.lang.String txtFDAgencyName) {
        this.txtFDAgencyName = txtFDAgencyName;
    }
    
    /**
     * Getter for property txtFDAccountRefNO.
     * @return Value of property txtFDAccountRefNO.
     */
    public java.lang.String getTxtFDAccountRefNO() {
        return txtFDAccountRefNO;
    }
    
    /**
     * Setter for property txtFDAccountRefNO.
     * @param txtFDAccountRefNO New value of property txtFDAccountRefNO.
     */
    public void setTxtFDAccountRefNO(java.lang.String txtFDAccountRefNO) {
        this.txtFDAccountRefNO = txtFDAccountRefNO;
    }
    
    /**
     * Getter for property txtFDInternalAccNo.
     * @return Value of property txtFDInternalAccNo.
     */
    public java.lang.String getTxtFDInternalAccNo() {
        return txtFDInternalAccNo;
    }
    
    /**
     * Setter for property txtFDInternalAccNo.
     * @param txtFDInternalAccNo New value of property txtFDInternalAccNo.
     */
    public void setTxtFDInternalAccNo(java.lang.String txtFDInternalAccNo) {
        this.txtFDInternalAccNo = txtFDInternalAccNo;
    }
    
    /**
     * Getter for property txtFDPricipalAmt.
     * @return Value of property txtFDPricipalAmt.
     */
    public java.lang.String getTxtFDPricipalAmt() {
        return txtFDPricipalAmt;
    }
    
    /**
     * Setter for property txtFDPricipalAmt.
     * @param txtFDPricipalAmt New value of property txtFDPricipalAmt.
     */
    public void setTxtFDPricipalAmt(java.lang.String txtFDPricipalAmt) {
        this.txtFDPricipalAmt = txtFDPricipalAmt;
    }
    
    /**
     * Getter for property txtFDInvestmentPeriod_Years.
     * @return Value of property txtFDInvestmentPeriod_Years.
     */
    public java.lang.String getTxtFDInvestmentPeriod_Years() {
        return txtFDInvestmentPeriod_Years;
    }
    
    /**
     * Setter for property txtFDInvestmentPeriod_Years.
     * @param txtFDInvestmentPeriod_Years New value of property txtFDInvestmentPeriod_Years.
     */
    public void setTxtFDInvestmentPeriod_Years(java.lang.String txtFDInvestmentPeriod_Years) {
        this.txtFDInvestmentPeriod_Years = txtFDInvestmentPeriod_Years;
    }
    
    /**
     * Getter for property txtFDInvestmentPeriod_Months.
     * @return Value of property txtFDInvestmentPeriod_Months.
     */
    public java.lang.String getTxtFDInvestmentPeriod_Months() {
        return txtFDInvestmentPeriod_Months;
    }
    
    /**
     * Setter for property txtFDInvestmentPeriod_Months.
     * @param txtFDInvestmentPeriod_Months New value of property txtFDInvestmentPeriod_Months.
     */
    public void setTxtFDInvestmentPeriod_Months(java.lang.String txtFDInvestmentPeriod_Months) {
        this.txtFDInvestmentPeriod_Months = txtFDInvestmentPeriod_Months;
    }
    
    /**
     * Getter for property txtFDInvestmentPeriod_Days.
     * @return Value of property txtFDInvestmentPeriod_Days.
     */
    public java.lang.String getTxtFDInvestmentPeriod_Days() {
        return txtFDInvestmentPeriod_Days;
    }
    
    /**
     * Setter for property txtFDInvestmentPeriod_Days.
     * @param txtFDInvestmentPeriod_Days New value of property txtFDInvestmentPeriod_Days.
     */
    public void setTxtFDInvestmentPeriod_Days(java.lang.String txtFDInvestmentPeriod_Days) {
        this.txtFDInvestmentPeriod_Days = txtFDInvestmentPeriod_Days;
    }
    
    /**
     * Getter for property txtFDRateOfInt.
     * @return Value of property txtFDRateOfInt.
     */
    public java.lang.String getTxtFDRateOfInt() {
        return txtFDRateOfInt;
    }
    
    /**
     * Setter for property txtFDRateOfInt.
     * @param txtFDRateOfInt New value of property txtFDRateOfInt.
     */
    public void setTxtFDRateOfInt(java.lang.String txtFDRateOfInt) {
        this.txtFDRateOfInt = txtFDRateOfInt;
    }
    
    /**
     * Getter for property cboFDInterestPaymentFrequency.
     * @return Value of property cboFDInterestPaymentFrequency.
     */
    public java.lang.String getCboFDInterestPaymentFrequency() {
        return cboFDInterestPaymentFrequency;
    }
    
    /**
     * Setter for property cboFDInterestPaymentFrequency.
     * @param cboFDInterestPaymentFrequency New value of property cboFDInterestPaymentFrequency.
     */
    public void setCboFDInterestPaymentFrequency(java.lang.String cboFDInterestPaymentFrequency) {
        this.cboFDInterestPaymentFrequency = cboFDInterestPaymentFrequency;
    }
    
    /**
     * Getter for property txtFDMaturityAmt.
     * @return Value of property txtFDMaturityAmt.
     */
    public java.lang.String getTxtFDMaturityAmt() {
        return txtFDMaturityAmt;
    }
    
    /**
     * Setter for property txtFDMaturityAmt.
     * @param txtFDMaturityAmt New value of property txtFDMaturityAmt.
     */
    public void setTxtFDMaturityAmt(java.lang.String txtFDMaturityAmt) {
        this.txtFDMaturityAmt = txtFDMaturityAmt;
    }
    
    /**
     * Getter for property txtFDInterestReceivable.
     * @return Value of property txtFDInterestReceivable.
     */
    public java.lang.String getTxtFDInterestReceivable() {
        return txtFDInterestReceivable;
    }
    
    /**
     * Setter for property txtFDInterestReceivable.
     * @param txtFDInterestReceivable New value of property txtFDInterestReceivable.
     */
    public void setTxtFDInterestReceivable(java.lang.String txtFDInterestReceivable) {
        this.txtFDInterestReceivable = txtFDInterestReceivable;
    }
    
    /**
     * Getter for property txtFDInterestReceived.
     * @return Value of property txtFDInterestReceived.
     */
    public java.lang.String getTxtFDInterestReceived() {
        return txtFDInterestReceived;
    }
    
    /**
     * Setter for property txtFDInterestReceived.
     * @param txtFDInterestReceived New value of property txtFDInterestReceived.
     */
    public void setTxtFDInterestReceived(java.lang.String txtFDInterestReceived) {
        this.txtFDInterestReceived = txtFDInterestReceived;
    }
    
    /**
     * Getter for property tdtFDAccOpenDt.
     * @return Value of property tdtFDAccOpenDt.
     */
    public java.util.Date getTdtFDAccOpenDt() {
        return tdtFDAccOpenDt;
    }
    
    /**
     * Setter for property tdtFDAccOpenDt.
     * @param tdtFDAccOpenDt New value of property tdtFDAccOpenDt.
     */
    public void setTdtFDAccOpenDt(java.util.Date tdtFDAccOpenDt) {
        this.tdtFDAccOpenDt = tdtFDAccOpenDt;
    }
    
    /**
     * Getter for property tdtFDMaturityDt.
     * @return Value of property tdtFDMaturityDt.
     */
    public java.util.Date getTdtFDMaturityDt() {
        return tdtFDMaturityDt;
    }
    
    /**
     * Setter for property tdtFDMaturityDt.
     * @param tdtFDMaturityDt New value of property tdtFDMaturityDt.
     */
    public void setTdtFDMaturityDt(java.util.Date tdtFDMaturityDt) {
        this.tdtFDMaturityDt = tdtFDMaturityDt;
    }
    
    /**
     * Getter for property tdtFDEffectiveDt.
     * @return Value of property tdtFDEffectiveDt.
     */
    public java.util.Date getTdtFDEffectiveDt() {
        return tdtFDEffectiveDt;
    }
    
    /**
     * Setter for property tdtFDEffectiveDt.
     * @param tdtFDEffectiveDt New value of property tdtFDEffectiveDt.
     */
    public void setTdtFDEffectiveDt(java.util.Date tdtFDEffectiveDt) {
        this.tdtFDEffectiveDt = tdtFDEffectiveDt;
    }
    
    /**
     * Getter for property tdtFDIntReceivedTillDt.
     * @return Value of property tdtFDIntReceivedTillDt.
     */
    public java.util.Date getTdtFDIntReceivedTillDt() {
        return tdtFDIntReceivedTillDt;
    }
    
    /**
     * Setter for property tdtFDIntReceivedTillDt.
     * @param tdtFDIntReceivedTillDt New value of property tdtFDIntReceivedTillDt.
     */
    public void setTdtFDIntReceivedTillDt(java.util.Date tdtFDIntReceivedTillDt) {
        this.tdtFDIntReceivedTillDt = tdtFDIntReceivedTillDt;
    }
    
    /**
     * Getter for property cbmFDInterestPaymentFrequency.
     * @return Value of property cbmFDInterestPaymentFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFDInterestPaymentFrequency() {
        return cbmFDInterestPaymentFrequency;
    }
    
    /**
     * Setter for property cbmFDInterestPaymentFrequency.
     * @param cbmFDInterestPaymentFrequency New value of property cbmFDInterestPaymentFrequency.
     */
    public void setCbmFDInterestPaymentFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmFDInterestPaymentFrequency) {
        this.cbmFDInterestPaymentFrequency = cbmFDInterestPaymentFrequency;
    }
    
    /**
     * Getter for property txtShareAgencyName.
     * @return Value of property txtShareAgencyName.
     */
    public java.lang.String getTxtShareAgencyName() {
        return txtShareAgencyName;
    }
    
    /**
     * Setter for property txtShareAgencyName.
     * @param txtShareAgencyName New value of property txtShareAgencyName.
     */
    public void setTxtShareAgencyName(java.lang.String txtShareAgencyName) {
        this.txtShareAgencyName = txtShareAgencyName;
    }
    
    /**
     * Getter for property txtShareType.
     * @return Value of property txtShareType.
     */
    public java.lang.String getTxtShareType() {
        return txtShareType;
    }
    
    /**
     * Setter for property txtShareType.
     * @param txtShareType New value of property txtShareType.
     */
    public void setTxtShareType(java.lang.String txtShareType) {
        this.txtShareType = txtShareType;
    }
    
    /**
     * Getter for property txtShareMemberID.
     * @return Value of property txtShareMemberID.
     */
    public java.lang.String getTxtShareMemberID() {
        return txtShareMemberID;
    }
    
    /**
     * Setter for property txtShareMemberID.
     * @param txtShareMemberID New value of property txtShareMemberID.
     */
    public void setTxtShareMemberID(java.lang.String txtShareMemberID) {
        this.txtShareMemberID = txtShareMemberID;
    }
    
    /**
     * Getter for property txtShareInternalAccNo.
     * @return Value of property txtShareInternalAccNo.
     */
    public java.lang.String getTxtShareInternalAccNo() {
        return txtShareInternalAccNo;
    }
    
    /**
     * Setter for property txtShareInternalAccNo.
     * @param txtShareInternalAccNo New value of property txtShareInternalAccNo.
     */
    public void setTxtShareInternalAccNo(java.lang.String txtShareInternalAccNo) {
        this.txtShareInternalAccNo = txtShareInternalAccNo;
    }
    
    /**
     * Getter for property txtNoOfShares.
     * @return Value of property txtNoOfShares.
     */
    public java.lang.String getTxtNoOfShares() {
        return txtNoOfShares;
    }
    
    /**
     * Setter for property txtNoOfShares.
     * @param txtNoOfShares New value of property txtNoOfShares.
     */
    public void setTxtNoOfShares(java.lang.String txtNoOfShares) {
        this.txtNoOfShares = txtNoOfShares;
    }
    
    /**
     * Getter for property txtShareValue.
     * @return Value of property txtShareValue.
     */
    public java.lang.String getTxtShareValue() {
        return txtShareValue;
    }
    
    /**
     * Setter for property txtShareValue.
     * @param txtShareValue New value of property txtShareValue.
     */
    public void setTxtShareValue(java.lang.String txtShareValue) {
        this.txtShareValue = txtShareValue;
    }
    
    /**
     * Getter for property txtFeesPaid.
     * @return Value of property txtFeesPaid.
     */
    public java.lang.String getTxtFeesPaid() {
        return txtFeesPaid;
    }
    
    /**
     * Setter for property txtFeesPaid.
     * @param txtFeesPaid New value of property txtFeesPaid.
     */
    public void setTxtFeesPaid(java.lang.String txtFeesPaid) {
        this.txtFeesPaid = txtFeesPaid;
    }
    
    /**
     * Getter for property tdtShareAccOpenDt.
     * @return Value of property tdtShareAccOpenDt.
     */
    public java.util.Date getTdtShareAccOpenDt() {
        return tdtShareAccOpenDt;
    }
    
    /**
     * Setter for property tdtShareAccOpenDt.
     * @param tdtShareAccOpenDt New value of property tdtShareAccOpenDt.
     */
    public void setTdtShareAccOpenDt(java.util.Date tdtShareAccOpenDt) {
        this.tdtShareAccOpenDt = tdtShareAccOpenDt;
    }
    
    /**
     * Getter for property txtRFAgencyName.
     * @return Value of property txtRFAgencyName.
     */
    public java.lang.String getTxtRFAgencyName() {
        return txtRFAgencyName;
    }
    
    /**
     * Setter for property txtRFAgencyName.
     * @param txtRFAgencyName New value of property txtRFAgencyName.
     */
    public void setTxtRFAgencyName(java.lang.String txtRFAgencyName) {
        this.txtRFAgencyName = txtRFAgencyName;
    }
    
    /**
     * Getter for property txtRFAccountRefNO.
     * @return Value of property txtRFAccountRefNO.
     */
    public java.lang.String getTxtRFAccountRefNO() {
        return txtRFAccountRefNO;
    }
    
    /**
     * Setter for property txtRFAccountRefNO.
     * @param txtRFAccountRefNO New value of property txtRFAccountRefNO.
     */
    public void setTxtRFAccountRefNO(java.lang.String txtRFAccountRefNO) {
        this.txtRFAccountRefNO = txtRFAccountRefNO;
    }
    
    /**
     * Getter for property txtRFInternalAccNo.
     * @return Value of property txtRFInternalAccNo.
     */
    public java.lang.String getTxtRFInternalAccNo() {
        return txtRFInternalAccNo;
    }
    
    /**
     * Setter for property txtRFInternalAccNo.
     * @param txtRFInternalAccNo New value of property txtRFInternalAccNo.
     */
    public void setTxtRFInternalAccNo(java.lang.String txtRFInternalAccNo) {
        this.txtRFInternalAccNo = txtRFInternalAccNo;
    }
    
    /**
     * Getter for property txtRFPricipalAmt.
     * @return Value of property txtRFPricipalAmt.
     */
    public java.lang.String getTxtRFPricipalAmt() {
        return txtRFPricipalAmt;
    }
    
    /**
     * Setter for property txtRFPricipalAmt.
     * @param txtRFPricipalAmt New value of property txtRFPricipalAmt.
     */
    public void setTxtRFPricipalAmt(java.lang.String txtRFPricipalAmt) {
        this.txtRFPricipalAmt = txtRFPricipalAmt;
    }
    
    /**
     * Getter for property tdtRFAccOpenDt.
     * @return Value of property tdtRFAccOpenDt.
     */
    public java.util.Date getTdtRFAccOpenDt() {
        return tdtRFAccOpenDt;
    }
    
    /**
     * Setter for property tdtRFAccOpenDt.
     * @param tdtRFAccOpenDt New value of property tdtRFAccOpenDt.
     */
    public void setTdtRFAccOpenDt(java.util.Date tdtRFAccOpenDt) {
        this.tdtRFAccOpenDt = tdtRFAccOpenDt;
    }
    
    /**
     * Getter for property rdoWithPrincipal.
     * @return Value of property rdoWithPrincipal.
     */
    public java.lang.String getRdoWithPrincipal() {
        return rdoWithPrincipal;
    }
    
    /**
     * Setter for property rdoWithPrincipal.
     * @param rdoWithPrincipal New value of property rdoWithPrincipal.
     */
    public void setRdoWithPrincipal(java.lang.String rdoWithPrincipal) {
        this.rdoWithPrincipal = rdoWithPrincipal;
    }
    
    /**
     * Getter for property tblCheckBookTable.
     * @return Value of property tblCheckBookTable.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCheckBookTable() {
        return tblCheckBookTable;
    }
    
    /**
     * Setter for property tblCheckBookTable.
     * @param tblCheckBookTable New value of property tblCheckBookTable.
     */
    public void setTblCheckBookTable(com.see.truetransact.clientutil.EnhancedTableModel tblCheckBookTable) {
        this.tblCheckBookTable = tblCheckBookTable;
    }
    
    /**
     * Getter for property tdtChequeIssueDt.
     * @return Value of property tdtChequeIssueDt.
     */
    public java.util.Date getTdtChequeIssueDt() {
        return tdtChequeIssueDt;
    }
    
    /**
     * Setter for property tdtChequeIssueDt.
     * @param tdtChequeIssueDt New value of property tdtChequeIssueDt.
     */
    public void setTdtChequeIssueDt(java.util.Date tdtChequeIssueDt) {
        this.tdtChequeIssueDt = tdtChequeIssueDt;
    }
    
    /**
     * Getter for property txtFromNO.
     * @return Value of property txtFromNO.
     */
    public java.lang.String getTxtFromNO() {
        return txtFromNO;
    }
    
    /**
     * Setter for property txtFromNO.
     * @param txtFromNO New value of property txtFromNO.
     */
    public void setTxtFromNO(java.lang.String txtFromNO) {
        this.txtFromNO = txtFromNO;
    }
    
    /**
     * Getter for property txtToNO.
     * @return Value of property txtToNO.
     */
    public java.lang.String getTxtToNO() {
        return txtToNO;
    }
    
    /**
     * Setter for property txtToNO.
     * @param txtToNO New value of property txtToNO.
     */
    public void setTxtToNO(java.lang.String txtToNO) {
        this.txtToNO = txtToNO;
    }
    
    /**
     * Getter for property txtNoOfCheques.
     * @return Value of property txtNoOfCheques.
     */
    public java.lang.String getTxtNoOfCheques() {
        return txtNoOfCheques;
    }
    
    /**
     * Setter for property txtNoOfCheques.
     * @param txtNoOfCheques New value of property txtNoOfCheques.
     */
    public void setTxtNoOfCheques(java.lang.String txtNoOfCheques) {
        this.txtNoOfCheques = txtNoOfCheques;
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
     * Getter for property txtSlNo.
     * @return Value of property txtSlNo.
     */
    public java.lang.String getTxtSlNo() {
        return txtSlNo;
    }
    
    /**
     * Setter for property txtSlNo.
     * @param txtSlNo New value of property txtSlNo.
     */
    public void setTxtSlNo(java.lang.String txtSlNo) {
        this.txtSlNo = txtSlNo;
    }
    
    /**
     * Getter for property rdoRenewal.
     * @return Value of property rdoRenewal.
     */
    public java.lang.String getRdoRenewal() {
        return rdoRenewal;
    }
    
    /**
     * Setter for property rdoRenewal.
     * @param rdoRenewal New value of property rdoRenewal.
     */
    public void setRdoRenewal(java.lang.String rdoRenewal) {
        this.rdoRenewal = rdoRenewal;
    }
    
    /**
     * Getter for property rdoWithInterest.
     * @return Value of property rdoWithInterest.
     */
    public java.lang.String getRdoWithInterest() {
        return rdoWithInterest;
    }
    
    /**
     * Setter for property rdoWithInterest.
     * @param rdoWithInterest New value of property rdoWithInterest.
     */
    public void setRdoWithInterest(java.lang.String rdoWithInterest) {
        this.rdoWithInterest = rdoWithInterest;
    }
    
    /**
     * Getter for property txtFDPeriodicIntrest.
     * @return Value of property txtFDPeriodicIntrest.
     */
    public java.lang.String getTxtFDPeriodicIntrest() {
        return txtFDPeriodicIntrest;
    }
    
    /**
     * Setter for property txtFDPeriodicIntrest.
     * @param txtFDPeriodicIntrest New value of property txtFDPeriodicIntrest.
     */
    public void setTxtFDPeriodicIntrest(java.lang.String txtFDPeriodicIntrest) {
        this.txtFDPeriodicIntrest = txtFDPeriodicIntrest;
    }
    
    /**
     * Getter for property txtShareFaceValue.
     * @return Value of property txtShareFaceValue.
     */
    public java.lang.String getTxtShareFaceValue() {
        return txtShareFaceValue;
    }
    
    /**
     * Setter for property txtShareFaceValue.
     * @param txtShareFaceValue New value of property txtShareFaceValue.
     */
    public void setTxtShareFaceValue(java.lang.String txtShareFaceValue) {
        this.txtShareFaceValue = txtShareFaceValue;
    }
    
    /**
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    /**
     * Getter for property callingTransAcctNo.
     * @return Value of property callingTransAcctNo.
     */
    public java.lang.String getCallingTransAcctNo() {
        return callingTransAcctNo;
    }
    
    /**
     * Setter for property callingTransAcctNo.
     * @param callingTransAcctNo New value of property callingTransAcctNo.
     */
    public void setCallingTransAcctNo(java.lang.String callingTransAcctNo) {
        this.callingTransAcctNo = callingTransAcctNo;
    }
    
    /**
     * Getter for property rdoSBorCA.
     * @return Value of property rdoSBorCA.
     */
    public java.lang.String getRdoSBorCA() {
        return rdoSBorCA;
    }
    
    /**
     * Setter for property rdoSBorCA.
     * @param rdoSBorCA New value of property rdoSBorCA.
     */
    public void setRdoSBorCA(java.lang.String rdoSBorCA) {
        this.rdoSBorCA = rdoSBorCA;
    }
    
    /**
     * Getter for property cboInvestmentTypeSBorCA.
     * @return Value of property cboInvestmentTypeSBorCA.
     */
    public java.lang.String getCboInvestmentTypeSBorCA() {
        return cboInvestmentTypeSBorCA;
    }
    
    /**
     * Setter for property cboInvestmentTypeSBorCA.
     * @param cboInvestmentTypeSBorCA New value of property cboInvestmentTypeSBorCA.
     */
    public void setCboInvestmentTypeSBorCA(java.lang.String cboInvestmentTypeSBorCA) {
        this.cboInvestmentTypeSBorCA = cboInvestmentTypeSBorCA;
    }
    
    /**
     * Getter for property txtInvestmentIDTransSBorCA.
     * @return Value of property txtInvestmentIDTransSBorCA.
     */
    public java.lang.String getTxtInvestmentIDTransSBorCA() {
        return txtInvestmentIDTransSBorCA;
    }
    
    /**
     * Setter for property txtInvestmentIDTransSBorCA.
     * @param txtInvestmentIDTransSBorCA New value of property txtInvestmentIDTransSBorCA.
     */
    public void setTxtInvestmentIDTransSBorCA(java.lang.String txtInvestmentIDTransSBorCA) {
        this.txtInvestmentIDTransSBorCA = txtInvestmentIDTransSBorCA;
    }
    
    /**
     * Getter for property txtInvestmentRefNoTrans.
     * @return Value of property txtInvestmentRefNoTrans.
     */
    public java.lang.String getTxtInvestmentRefNoTrans() {
        return txtInvestmentRefNoTrans;
    }
    
    /**
     * Setter for property txtInvestmentRefNoTrans.
     * @param txtInvestmentRefNoTrans New value of property txtInvestmentRefNoTrans.
     */
    public void setTxtInvestmentRefNoTrans(java.lang.String txtInvestmentRefNoTrans) {
        this.txtInvestmentRefNoTrans = txtInvestmentRefNoTrans;
    }
    
    /**
     * Getter for property txtInvestmentInternalNoTrans.
     * @return Value of property txtInvestmentInternalNoTrans.
     */
    public java.lang.String getTxtInvestmentInternalNoTrans() {
        return txtInvestmentInternalNoTrans;
    }
    
    /**
     * Setter for property txtInvestmentInternalNoTrans.
     * @param txtInvestmentInternalNoTrans New value of property txtInvestmentInternalNoTrans.
     */
    public void setTxtInvestmentInternalNoTrans(java.lang.String txtInvestmentInternalNoTrans) {
        this.txtInvestmentInternalNoTrans = txtInvestmentInternalNoTrans;
    }
    
    /**
     * Getter for property txtChequeNo.
     * @return Value of property txtChequeNo.
     */
    public java.lang.String getTxtChequeNo() {
        return txtChequeNo;
    }
    
    /**
     * Setter for property txtChequeNo.
     * @param txtChequeNo New value of property txtChequeNo.
     */
    public void setTxtChequeNo(java.lang.String txtChequeNo) {
        this.txtChequeNo = txtChequeNo;
    }
    
    /**
     * Getter for property cbmInvestmentTypeSBorCA.
     * @return Value of property cbmInvestmentTypeSBorCA.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInvestmentTypeSBorCA() {
        return cbmInvestmentTypeSBorCA;
    }
    
    /**
     * Setter for property cbmInvestmentTypeSBorCA.
     * @param cbmInvestmentTypeSBorCA New value of property cbmInvestmentTypeSBorCA.
     */
    public void setCbmInvestmentTypeSBorCA(com.see.truetransact.clientutil.ComboBoxModel cbmInvestmentTypeSBorCA) {
        this.cbmInvestmentTypeSBorCA = cbmInvestmentTypeSBorCA;
    }
    
    /**
     * Getter for property txtNarration.
     * @return Value of property txtNarration.
     */
    public java.lang.String getTxtNarration() {
        return txtNarration;
    }
    
    /**
     * Setter for property txtNarration.
     * @param txtNarration New value of property txtNarration.
     */
    public void setTxtNarration(java.lang.String txtNarration) {
        this.txtNarration = txtNarration;
    }
    
    
    /**
     * Setter for property batch_Id.
     * @param batch_Id New value of property batch_Id.
     */
    public void setBatch_Id(java.lang.String batch_Id) {
        this.batch_Id = batch_Id;
    }
    
    /**
     * Getter for property transactionDetailsTO.
     * @return Value of property transactionDetailsTO.
     */
    public java.util.LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }
    
    /**
     * Setter for property transactionDetailsTO.
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(java.util.LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Getter for property txtInvestmentAmount.
     * @return Value of property txtInvestmentAmount.
     */
    public Double getTxtInvestmentAmount() {
        return txtInvestmentAmount;
    }
    
    /**
     * Setter for property txtInvestmentAmount.
     * @param txtInvestmentAmount New value of property txtInvestmentAmount.
     */
    public void setTxtInvestmentAmount(Double txtInvestmentAmount) {
        this.txtInvestmentAmount = txtInvestmentAmount;
    }
    
    /**
     * Getter for property batch_Id.
     * @return Value of property batch_Id.
     */
    public java.lang.String getBatch_Id() {
        return batch_Id;
    }
    
    /**
     * Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    /**
     * Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    /**
     * Getter for property depositInternalAccNo.
     * @return Value of property depositInternalAccNo.
     */
    public java.lang.String getDepositInternalAccNo() {
        return depositInternalAccNo;
    }
    
    /**
     * Setter for property depositInternalAccNo.
     * @param depositInternalAccNo New value of property depositInternalAccNo.
     */
    public void setDepositInternalAccNo(java.lang.String depositInternalAccNo) {
        this.depositInternalAccNo = depositInternalAccNo;
    }
    
    /**
     * Getter for property cbmRenewalInvestmentBehaves.
     * @return Value of property cbmRenewalInvestmentBehaves.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInvestmentBehaves() {
        return cbmRenewalInvestmentBehaves;
    }
    
    /**
     * Setter for property cbmRenewalInvestmentBehaves.
     * @param cbmRenewalInvestmentBehaves New value of property cbmRenewalInvestmentBehaves.
     */
    public void setCbmRenewalInvestmentBehaves(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInvestmentBehaves) {
        this.cbmRenewalInvestmentBehaves = cbmRenewalInvestmentBehaves;
    }
    
    /**
     * Getter for property cbmRenewalFDInterestPaymentFrequency.
     * @return Value of property cbmRenewalFDInterestPaymentFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalFDInterestPaymentFrequency() {
        return cbmRenewalFDInterestPaymentFrequency;
    }
    
    /**
     * Setter for property cbmRenewalFDInterestPaymentFrequency.
     * @param cbmRenewalFDInterestPaymentFrequency New value of property cbmRenewalFDInterestPaymentFrequency.
     */
    public void setCbmRenewalFDInterestPaymentFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalFDInterestPaymentFrequency) {
        this.cbmRenewalFDInterestPaymentFrequency = cbmRenewalFDInterestPaymentFrequency;
    }
    
    /**
     * Getter for property txtRenewalFDAgencyName.
     * @return Value of property txtRenewalFDAgencyName.
     */
    public java.lang.String getTxtRenewalFDAgencyName() {
        return txtRenewalFDAgencyName;
    }
    
    /**
     * Setter for property txtRenewalFDAgencyName.
     * @param txtRenewalFDAgencyName New value of property txtRenewalFDAgencyName.
     */
    public void setTxtRenewalFDAgencyName(java.lang.String txtRenewalFDAgencyName) {
        this.txtRenewalFDAgencyName = txtRenewalFDAgencyName;
    }
    
    /**
     * Getter for property txtRenewalFDAccountRefNO.
     * @return Value of property txtRenewalFDAccountRefNO.
     */
    public java.lang.String getTxtRenewalFDAccountRefNO() {
        return txtRenewalFDAccountRefNO;
    }
    
    /**
     * Setter for property txtRenewalFDAccountRefNO.
     * @param txtRenewalFDAccountRefNO New value of property txtRenewalFDAccountRefNO.
     */
    public void setTxtRenewalFDAccountRefNO(java.lang.String txtRenewalFDAccountRefNO) {
        this.txtRenewalFDAccountRefNO = txtRenewalFDAccountRefNO;
    }
    
    /**
     * Getter for property txtRenewalFDInternalAccNo.
     * @return Value of property txtRenewalFDInternalAccNo.
     */
    public java.lang.String getTxtRenewalFDInternalAccNo() {
        return txtRenewalFDInternalAccNo;
    }
    
    /**
     * Setter for property txtRenewalFDInternalAccNo.
     * @param txtRenewalFDInternalAccNo New value of property txtRenewalFDInternalAccNo.
     */
    public void setTxtRenewalFDInternalAccNo(java.lang.String txtRenewalFDInternalAccNo) {
        this.txtRenewalFDInternalAccNo = txtRenewalFDInternalAccNo;
    }
    
    /**
     * Getter for property txtRenewalFDPricipalAmt.
     * @return Value of property txtRenewalFDPricipalAmt.
     */
    public java.lang.String getTxtRenewalFDPricipalAmt() {
        return txtRenewalFDPricipalAmt;
    }
    
    /**
     * Setter for property txtRenewalFDPricipalAmt.
     * @param txtRenewalFDPricipalAmt New value of property txtRenewalFDPricipalAmt.
     */
    public void setTxtRenewalFDPricipalAmt(java.lang.String txtRenewalFDPricipalAmt) {
        this.txtRenewalFDPricipalAmt = txtRenewalFDPricipalAmt;
    }
    
    /**
     * Getter for property txtRenewalFDInvestmentPeriod_Years.
     * @return Value of property txtRenewalFDInvestmentPeriod_Years.
     */
    public java.lang.String getTxtRenewalFDInvestmentPeriod_Years() {
        return txtRenewalFDInvestmentPeriod_Years;
    }
    
    /**
     * Setter for property txtRenewalFDInvestmentPeriod_Years.
     * @param txtRenewalFDInvestmentPeriod_Years New value of property txtRenewalFDInvestmentPeriod_Years.
     */
    public void setTxtRenewalFDInvestmentPeriod_Years(java.lang.String txtRenewalFDInvestmentPeriod_Years) {
        this.txtRenewalFDInvestmentPeriod_Years = txtRenewalFDInvestmentPeriod_Years;
    }
    
    /**
     * Getter for property txtRenewalFDInvestmentPeriod_Months.
     * @return Value of property txtRenewalFDInvestmentPeriod_Months.
     */
    public java.lang.String getTxtRenewalFDInvestmentPeriod_Months() {
        return txtRenewalFDInvestmentPeriod_Months;
    }
    
    /**
     * Setter for property txtRenewalFDInvestmentPeriod_Months.
     * @param txtRenewalFDInvestmentPeriod_Months New value of property txtRenewalFDInvestmentPeriod_Months.
     */
    public void setTxtRenewalFDInvestmentPeriod_Months(java.lang.String txtRenewalFDInvestmentPeriod_Months) {
        this.txtRenewalFDInvestmentPeriod_Months = txtRenewalFDInvestmentPeriod_Months;
    }
    
    /**
     * Getter for property txtRenewalFDInvestmentPeriod_Days.
     * @return Value of property txtRenewalFDInvestmentPeriod_Days.
     */
    public java.lang.String getTxtRenewalFDInvestmentPeriod_Days() {
        return txtRenewalFDInvestmentPeriod_Days;
    }
    
    /**
     * Setter for property txtRenewalFDInvestmentPeriod_Days.
     * @param txtRenewalFDInvestmentPeriod_Days New value of property txtRenewalFDInvestmentPeriod_Days.
     */
    public void setTxtRenewalFDInvestmentPeriod_Days(java.lang.String txtRenewalFDInvestmentPeriod_Days) {
        this.txtRenewalFDInvestmentPeriod_Days = txtRenewalFDInvestmentPeriod_Days;
    }
    
    /**
     * Getter for property txtRenewalFDRateOfInt.
     * @return Value of property txtRenewalFDRateOfInt.
     */
    public java.lang.String getTxtRenewalFDRateOfInt() {
        return txtRenewalFDRateOfInt;
    }
    
    /**
     * Setter for property txtRenewalFDRateOfInt.
     * @param txtRenewalFDRateOfInt New value of property txtRenewalFDRateOfInt.
     */
    public void setTxtRenewalFDRateOfInt(java.lang.String txtRenewalFDRateOfInt) {
        this.txtRenewalFDRateOfInt = txtRenewalFDRateOfInt;
    }
    
    /**
     * Getter for property cboRenewalFDInterestPaymentFrequency.
     * @return Value of property cboRenewalFDInterestPaymentFrequency.
     */
    public java.lang.String getCboRenewalFDInterestPaymentFrequency() {
        return cboRenewalFDInterestPaymentFrequency;
    }
    
    /**
     * Setter for property cboRenewalFDInterestPaymentFrequency.
     * @param cboRenewalFDInterestPaymentFrequency New value of property cboRenewalFDInterestPaymentFrequency.
     */
    public void setCboRenewalFDInterestPaymentFrequency(java.lang.String cboRenewalFDInterestPaymentFrequency) {
        this.cboRenewalFDInterestPaymentFrequency = cboRenewalFDInterestPaymentFrequency;
    }
    
    /**
     * Getter for property txtRenewalFDMaturityAmt.
     * @return Value of property txtRenewalFDMaturityAmt.
     */
    public java.lang.String getTxtRenewalFDMaturityAmt() {
        return txtRenewalFDMaturityAmt;
    }
    
    /**
     * Setter for property txtRenewalFDMaturityAmt.
     * @param txtRenewalFDMaturityAmt New value of property txtRenewalFDMaturityAmt.
     */
    public void setTxtRenewalFDMaturityAmt(java.lang.String txtRenewalFDMaturityAmt) {
        this.txtRenewalFDMaturityAmt = txtRenewalFDMaturityAmt;
    }
    
    /**
     * Getter for property txtRenewalFDInterestReceivable.
     * @return Value of property txtRenewalFDInterestReceivable.
     */
    public java.lang.String getTxtRenewalFDInterestReceivable() {
        return txtRenewalFDInterestReceivable;
    }
    
    /**
     * Setter for property txtRenewalFDInterestReceivable.
     * @param txtRenewalFDInterestReceivable New value of property txtRenewalFDInterestReceivable.
     */
    public void setTxtRenewalFDInterestReceivable(java.lang.String txtRenewalFDInterestReceivable) {
        this.txtRenewalFDInterestReceivable = txtRenewalFDInterestReceivable;
    }
    
    /**
     * Getter for property txtRenewalFDInterestReceived.
     * @return Value of property txtRenewalFDInterestReceived.
     */
    public java.lang.String getTxtRenewalFDInterestReceived() {
        return txtRenewalFDInterestReceived;
    }
    
    /**
     * Setter for property txtRenewalFDInterestReceived.
     * @param txtRenewalFDInterestReceived New value of property txtRenewalFDInterestReceived.
     */
    public void setTxtRenewalFDInterestReceived(java.lang.String txtRenewalFDInterestReceived) {
        this.txtRenewalFDInterestReceived = txtRenewalFDInterestReceived;
    }
    
    /**
     * Getter for property txtRenewalFDPeriodicIntrest.
     * @return Value of property txtRenewalFDPeriodicIntrest.
     */
    public java.lang.String getTxtRenewalFDPeriodicIntrest() {
        return txtRenewalFDPeriodicIntrest;
    }
    
    /**
     * Setter for property txtRenewalFDPeriodicIntrest.
     * @param txtRenewalFDPeriodicIntrest New value of property txtRenewalFDPeriodicIntrest.
     */
    public void setTxtRenewalFDPeriodicIntrest(java.lang.String txtRenewalFDPeriodicIntrest) {
        this.txtRenewalFDPeriodicIntrest = txtRenewalFDPeriodicIntrest;
    }
    
    /**
     * Getter for property tdtRenewalFDAccOpenDt.
     * @return Value of property tdtRenewalFDAccOpenDt.
     */
    public java.util.Date getTdtRenewalFDAccOpenDt() {
        return tdtRenewalFDAccOpenDt;
    }
    
    /**
     * Setter for property tdtRenewalFDAccOpenDt.
     * @param tdtRenewalFDAccOpenDt New value of property tdtRenewalFDAccOpenDt.
     */
    public void setTdtRenewalFDAccOpenDt(java.util.Date tdtRenewalFDAccOpenDt) {
        this.tdtRenewalFDAccOpenDt = tdtRenewalFDAccOpenDt;
    }
    
    /**
     * Getter for property tdtRenewalFDMaturityDt.
     * @return Value of property tdtRenewalFDMaturityDt.
     */
    public java.util.Date getTdtRenewalFDMaturityDt() {
        return tdtRenewalFDMaturityDt;
    }
    
    /**
     * Setter for property tdtRenewalFDMaturityDt.
     * @param tdtRenewalFDMaturityDt New value of property tdtRenewalFDMaturityDt.
     */
    public void setTdtRenewalFDMaturityDt(java.util.Date tdtRenewalFDMaturityDt) {
        this.tdtRenewalFDMaturityDt = tdtRenewalFDMaturityDt;
    }
    
    /**
     * Getter for property tdtRenewalFDEffectiveDt.
     * @return Value of property tdtRenewalFDEffectiveDt.
     */
    public java.util.Date getTdtRenewalFDEffectiveDt() {
        return tdtRenewalFDEffectiveDt;
    }
    
    /**
     * Setter for property tdtRenewalFDEffectiveDt.
     * @param tdtRenewalFDEffectiveDt New value of property tdtRenewalFDEffectiveDt.
     */
    public void setTdtRenewalFDEffectiveDt(java.util.Date tdtRenewalFDEffectiveDt) {
        this.tdtRenewalFDEffectiveDt = tdtRenewalFDEffectiveDt;
    }
    
    /**
     * Getter for property tdtRenewalFDIntReceivedTillDt.
     * @return Value of property tdtRenewalFDIntReceivedTillDt.
     */
    public java.util.Date getTdtRenewalFDIntReceivedTillDt() {
        return tdtRenewalFDIntReceivedTillDt;
    }
    
    /**
     * Setter for property tdtRenewalFDIntReceivedTillDt.
     * @param tdtRenewalFDIntReceivedTillDt New value of property tdtRenewalFDIntReceivedTillDt.
     */
    public void setTdtRenewalFDIntReceivedTillDt(java.util.Date tdtRenewalFDIntReceivedTillDt) {
        this.tdtRenewalFDIntReceivedTillDt = tdtRenewalFDIntReceivedTillDt;
    }
    
    /**
     * Getter for property cboRenewalInvestmentBehaves.
     * @return Value of property cboRenewalInvestmentBehaves.
     */
    public java.lang.String getCboRenewalInvestmentBehaves() {
        return cboRenewalInvestmentBehaves;
    }
    
    /**
     * Setter for property cboRenewalInvestmentBehaves.
     * @param cboRenewalInvestmentBehaves New value of property cboRenewalInvestmentBehaves.
     */
    public void setCboRenewalInvestmentBehaves(java.lang.String cboRenewalInvestmentBehaves) {
        this.cboRenewalInvestmentBehaves = cboRenewalInvestmentBehaves;
    }
    
    /**
     * Getter for property renewalInvestmentID.
     * @return Value of property renewalInvestmentID.
     */
    public java.lang.String getRenewalInvestmentID() {
        return renewalInvestmentID;
    }
    
    /**
     * Setter for property renewalInvestmentID.
     * @param renewalInvestmentID New value of property renewalInvestmentID.
     */
    public void setRenewalInvestmentID(java.lang.String renewalInvestmentID) {
        this.renewalInvestmentID = renewalInvestmentID;
    }
    
    /**
     * Getter for property renewalInvestmentName.
     * @return Value of property renewalInvestmentName.
     */
    public java.lang.String getRenewalInvestmentName() {
        return renewalInvestmentName;
    }
    
    /**
     * Setter for property renewalInvestmentName.
     * @param renewalInvestmentName New value of property renewalInvestmentName.
     */
    public void setRenewalInvestmentName(java.lang.String renewalInvestmentName) {
        this.renewalInvestmentName = renewalInvestmentName;
    }
    
    /**
     * Getter for property rdoRenewalSameNumber.
     * @return Value of property rdoRenewalSameNumber.
     */
    public java.lang.String getRdoRenewalSameNumber() {
        return rdoRenewalSameNumber;
    }    
    
    /**
     * Setter for property rdoRenewalSameNumber.
     * @param rdoRenewalSameNumber New value of property rdoRenewalSameNumber.
     */
    public void setRdoRenewalSameNumber(java.lang.String rdoRenewalSameNumber) {
        this.rdoRenewalSameNumber = rdoRenewalSameNumber;
    }
    
    /**
     * Getter for property rdoRenewalNewNumber.
     * @return Value of property rdoRenewalNewNumber.
     */
    public java.lang.String getRdoRenewalNewNumber() {
        return rdoRenewalNewNumber;
    }
    
    /**
     * Setter for property rdoRenewalNewNumber.
     * @param rdoRenewalNewNumber New value of property rdoRenewalNewNumber.
     */
    public void setRdoRenewalNewNumber(java.lang.String rdoRenewalNewNumber) {
        this.rdoRenewalNewNumber = rdoRenewalNewNumber;
    }
    
    /**
     * Getter for property rdoRenewalDiffProdNumber.
     * @return Value of property rdoRenewalDiffProdNumber.
     */
    public java.lang.String getRdoRenewalDiffProdNumber() {
        return rdoRenewalDiffProdNumber;
    }
    
    /**
     * Setter for property rdoRenewalDiffProdNumber.
     * @param rdoRenewalDiffProdNumber New value of property rdoRenewalDiffProdNumber.
     */
    public void setRdoRenewalDiffProdNumber(java.lang.String rdoRenewalDiffProdNumber) {
        this.rdoRenewalDiffProdNumber = rdoRenewalDiffProdNumber;
    }
    
    /**
     * Getter for property rdoRenewalWithInterest.
     * @return Value of property rdoRenewalWithInterest.
     */
    public java.lang.String getRdoRenewalWithInterest() {
        return rdoRenewalWithInterest;
    }
    
    /**
     * Setter for property rdoRenewalWithInterest.
     * @param rdoRenewalWithInterest New value of property rdoRenewalWithInterest.
     */
    public void setRdoRenewalWithInterest(java.lang.String rdoRenewalWithInterest) {
        this.rdoRenewalWithInterest = rdoRenewalWithInterest;
    }
    
    /**
     * Getter for property rdoRenewalWithOutInterest.
     * @return Value of property rdoRenewalWithOutInterest.
     */
    public java.lang.String getRdoRenewalWithOutInterest() {
        return rdoRenewalWithOutInterest;
    }
    
    /**
     * Setter for property rdoRenewalWithOutInterest.
     * @param rdoRenewalWithOutInterest New value of property rdoRenewalWithOutInterest.
     */
    public void setRdoRenewalWithOutInterest(java.lang.String rdoRenewalWithOutInterest) {
        this.rdoRenewalWithOutInterest = rdoRenewalWithOutInterest;
    }
    
    /**
     * Getter for property rdoRenewalpartialInterest.
     * @return Value of property rdoRenewalpartialInterest.
     */
    public java.lang.String getRdoRenewalpartialInterest() {
        return rdoRenewalpartialInterest;
    }
    
    /**
     * Setter for property rdoRenewalpartialInterest.
     * @param rdoRenewalpartialInterest New value of property rdoRenewalpartialInterest.
     */
    public void setRdoRenewalpartialInterest(java.lang.String rdoRenewalpartialInterest) {
        this.rdoRenewalpartialInterest = rdoRenewalpartialInterest;
    }
    
    /**
     * Getter for property renewalInterestAmount.
     * @return Value of property renewalInterestAmount.
     */
    public java.lang.String getRenewalInterestAmount() {
        return renewalInterestAmount;
    }
    
    /**
     * Setter for property renewalInterestAmount.
     * @param renewalInterestAmount New value of property renewalInterestAmount.
     */
    public void setRenewalInterestAmount(java.lang.String renewalInterestAmount) {
        this.renewalInterestAmount = renewalInterestAmount;
    }
    
    /**
     * Getter for property rdoRenewalSBorCA.
     * @return Value of property rdoRenewalSBorCA.
     */
    public java.lang.String getRdoRenewalSBorCA() {
        return rdoRenewalSBorCA;
    }
    
    /**
     * Setter for property rdoRenewalSBorCA.
     * @param rdoRenewalSBorCA New value of property rdoRenewalSBorCA.
     */
    public void setRdoRenewalSBorCA(java.lang.String rdoRenewalSBorCA) {
        this.rdoRenewalSBorCA = rdoRenewalSBorCA;
    }
    
    /**
     * Getter for property cboRenewalInvestmentTypeSBorCA.
     * @return Value of property cboRenewalInvestmentTypeSBorCA.
     */
    public java.lang.String getCboRenewalInvestmentTypeSBorCA() {
        return cboRenewalInvestmentTypeSBorCA;
    }
    
    /**
     * Setter for property cboRenewalInvestmentTypeSBorCA.
     * @param cboRenewalInvestmentTypeSBorCA New value of property cboRenewalInvestmentTypeSBorCA.
     */
    public void setCboRenewalInvestmentTypeSBorCA(java.lang.String cboRenewalInvestmentTypeSBorCA) {
        this.cboRenewalInvestmentTypeSBorCA = cboRenewalInvestmentTypeSBorCA;
    }
    
    /**
     * Getter for property txtRenewalInvestmentIDTransSBorCA.
     * @return Value of property txtRenewalInvestmentIDTransSBorCA.
     */
    public java.lang.String getTxtRenewalInvestmentIDTransSBorCA() {
        return txtRenewalInvestmentIDTransSBorCA;
    }
    
    /**
     * Setter for property txtRenewalInvestmentIDTransSBorCA.
     * @param txtRenewalInvestmentIDTransSBorCA New value of property txtRenewalInvestmentIDTransSBorCA.
     */
    public void setTxtRenewalInvestmentIDTransSBorCA(java.lang.String txtRenewalInvestmentIDTransSBorCA) {
        this.txtRenewalInvestmentIDTransSBorCA = txtRenewalInvestmentIDTransSBorCA;
    }
    
    /**
     * Getter for property txtRenewalInvestmentRefNoTrans.
     * @return Value of property txtRenewalInvestmentRefNoTrans.
     */
    public java.lang.String getTxtRenewalInvestmentRefNoTrans() {
        return txtRenewalInvestmentRefNoTrans;
    }
    
    /**
     * Setter for property txtRenewalInvestmentRefNoTrans.
     * @param txtRenewalInvestmentRefNoTrans New value of property txtRenewalInvestmentRefNoTrans.
     */
    public void setTxtRenewalInvestmentRefNoTrans(java.lang.String txtRenewalInvestmentRefNoTrans) {
        this.txtRenewalInvestmentRefNoTrans = txtRenewalInvestmentRefNoTrans;
    }
    
    /**
     * Getter for property txtRenewalInvestmentInternalNoTrans.
     * @return Value of property txtRenewalInvestmentInternalNoTrans.
     */
    public java.lang.String getTxtRenewalInvestmentInternalNoTrans() {
        return txtRenewalInvestmentInternalNoTrans;
    }
    
    /**
     * Setter for property txtRenewalInvestmentInternalNoTrans.
     * @param txtRenewalInvestmentInternalNoTrans New value of property txtRenewalInvestmentInternalNoTrans.
     */
    public void setTxtRenewalInvestmentInternalNoTrans(java.lang.String txtRenewalInvestmentInternalNoTrans) {
        this.txtRenewalInvestmentInternalNoTrans = txtRenewalInvestmentInternalNoTrans;
    }
    
    /**
     * Getter for property cbmRenewalInvestmentTypeSBorCA.
     * @return Value of property cbmRenewalInvestmentTypeSBorCA.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInvestmentTypeSBorCA() {
        return cbmRenewalInvestmentTypeSBorCA;
    }
    
    /**
     * Setter for property cbmRenewalInvestmentTypeSBorCA.
     * @param cbmRenewalInvestmentTypeSBorCA New value of property cbmRenewalInvestmentTypeSBorCA.
     */
    public void setCbmRenewalInvestmentTypeSBorCA(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInvestmentTypeSBorCA) {
        this.cbmRenewalInvestmentTypeSBorCA = cbmRenewalInvestmentTypeSBorCA;
    }
    
    /**
     * Getter for property txtRenewalNarration.
     * @return Value of property txtRenewalNarration.
     */
    public java.lang.String getTxtRenewalNarration() {
        return txtRenewalNarration;
    }
    
    /**
     * Setter for property txtRenewalNarration.
     * @param txtRenewalNarration New value of property txtRenewalNarration.
     */
    public void setTxtRenewalNarration(java.lang.String txtRenewalNarration) {
        this.txtRenewalNarration = txtRenewalNarration;
    }
    
    /**
     * Getter for property txtRenewalInvestmentAmount.
     * @return Value of property txtRenewalInvestmentAmount.
     */
    public java.lang.String getTxtRenewalInvestmentAmount() {
        return txtRenewalInvestmentAmount;
    }
    
    /**
     * Setter for property txtRenewalInvestmentAmount.
     * @param txtRenewalInvestmentAmount New value of property txtRenewalInvestmentAmount.
     */
    public void setTxtRenewalInvestmentAmount(java.lang.String txtRenewalInvestmentAmount) {
        this.txtRenewalInvestmentAmount = txtRenewalInvestmentAmount;
    }
    
    /**
     * Getter for property txtRenewalWithdrawalAmt.
     * @return Value of property txtRenewalWithdrawalAmt.
     */
    public java.lang.String getTxtRenewalWithdrawalAmt() {
        return txtRenewalWithdrawalAmt;
    }
    
    /**
     * Setter for property txtRenewalWithdrawalAmt.
     * @param txtRenewalWithdrawalAmt New value of property txtRenewalWithdrawalAmt.
     */
    public void setTxtRenewalWithdrawalAmt(java.lang.String txtRenewalWithdrawalAmt) {
        this.txtRenewalWithdrawalAmt = txtRenewalWithdrawalAmt;
    }

    public Date getTdtRenewalDt() {
        return tdtRenewalDt;
    }

    public void setTdtRenewalDt(Date tdtRenewalDt) {
        this.tdtRenewalDt = tdtRenewalDt;
    }
    
    
}