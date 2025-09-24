/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositLienOB.java
 *
 * Created on May 26, 2004, 10:22 AM
 */

package com.see.truetransact.ui.deposit.lien;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Observable;
import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  Pinky
 */

public class DepositLienOB extends CObservable{
    
    private static DepositLienOB depositLienOB;
    private ProxyFactory proxy;
    private final static ClientParseException parseException
                                            = ClientParseException.getInstance();
    private HashMap map,lookupMap,authorizeMap;
    private ComboBoxModel cbmProductId,cbmSubDepositNos,cbmLienProductId,cbmCreditType,cbmLOSLoanType;
    private TableModel tbmLien;
    private ArrayList key,value,lienTOs,deleteLienList;
    
    private int actionType;
    private int result;
    
    private String txtDepositNo;
    private String lblStatus;
    private String lblClearBalance;
    private String lblExistingLienAmt;
    private String lblExistingFreezeAmt;
    private String lblDepositAmt;
    private String lblShadowLien;
    private String lienAuthorizeStatus;
    
    private String lienAccountHD;
    private String lienAccountNo;
    private String lienAmount;
    private String lienDate;
    private String lienRemark;
    private String lienCreditType;
    private String lienNo;
    private String lienStatus;
    private String cboSubDepositNo = "";
    private String cboLienProdID;
    private String cboLOSLoanType;
    private Date actOpeningDate;
    private Date depositDate;
    private String unLienRemark;
    private String lblDepositLienDesc=""; 
    private String statusBy1="";
    Date curDate = null;
    public boolean depFlag = false;
    public boolean loanFlag = false;
    private String losLienAcNo;
    private String losLienCustName;
    private String losLienAmount;
    private String losLienDate=null;
    private String losLienRemarks;
    private boolean chkValue=false;

    public boolean getIsMds() {
        return isMds;
    }

    public void setIsMds(boolean isMds) {
        this.isMds = isMds;
    }
    private boolean isMds=false;
    
    /** Creates a new instance of DepositLienOB */
    private DepositLienOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "DepositLienJNDI");
        map.put(CommonConstants.HOME, "deposit.lien.DepositLienHome");
        map.put(CommonConstants.REMOTE,"deposit.lien.DepositLien");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        fillDropDown();
        setTable();
        lienTOs = new ArrayList();
        deleteLienList = new ArrayList();
    }
    
    static {
        try {
            depositLienOB = new DepositLienOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static DepositLienOB getInstance(){
        return depositLienOB;
    }
    
    private void setTable(){
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Lien No");
        columnHeader.add("Amount");
        columnHeader.add("Date");
        columnHeader.add("Status");
        columnHeader.add("Authorize Status");
        ArrayList data = new ArrayList();
        tbmLien = new TableModel(data,columnHeader);
    }
    
    private void fillDropDown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        HashMap param = new HashMap();
        
        param.put(CommonConstants.MAP_NAME, "getDepositProductsAndMds");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        cbmProductId = new ComboBoxModel(key,value);
       
        param.put(CommonConstants.MAP_NAME, "DepositLien.getLoanProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        lookupValues = ClientUtil.populateLookupData(param);
        
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        int j=value.size();
        for(int i=1;i<j;i++){
            value.set(i,(String)value.get(i)+" ("+(String)key.get(i)+")");
        }
        cbmLienProductId = new ComboBoxModel(key,value);
       
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("DEPOSITLIEN_CREDIT_TYPE");
        
        param.put(CommonConstants.MAP_NAME, null);
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        
        lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get("DEPOSITLIEN_CREDIT_TYPE"));
        this.cbmCreditType = new ComboBoxModel(key,value);
        
        final ArrayList lookupKey1 = new ArrayList();
        lookupKey1.add("LIEN_LOAN_TYPE");
        
        param.put(CommonConstants.MAP_NAME, null);
        param.put(CommonConstants.PARAMFORQUERY, lookupKey1);
        
        lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get("LIEN_LOAN_TYPE"));
        this.cbmLOSLoanType = new ComboBoxModel(key,value);
        
        key =  new ArrayList();
        value = new ArrayList();
        
        this.cbmSubDepositNos = new ComboBoxModel(key,value);
        
        param=null;
        lookupValues=null;
        key=null;
        value=null;
    }
    
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To get data for comboboxes */
    private HashMap populateDataLocal(HashMap obj)  throws Exception{
        HashMap keyValue = proxy.executeQuery(obj,lookupMap);
        return keyValue;
    }
    /** Getter for property cbmProductId.
     * @return Value of property cbmProductId.
     *
     */
    public ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }
    /** Setter for property cbmProductId.
     * @param cbmProductId New value of property cbmProductId.
     *
     */
    public void setCbmProductId(ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
    }
    
    public ComboBoxModel getCbmLOSLoanType() {
        return cbmLOSLoanType;
    }
    /** Setter for property cbmProductId.
     * @param cbmProductId New value of property cbmProductId.
     *
     */
    public void setCbmLOSLoanType(ComboBoxModel cbmLOSLoanType) {
        this.cbmLOSLoanType = cbmLOSLoanType;
    }
    
    public String getAccountHead(String prodId){
        HashMap whereMap= new HashMap();
        whereMap.put("PRODID", prodId);
        List list = ClientUtil.executeQuery("getDepositAcHd",whereMap);
        if(list!=null && list.size()>0){
            whereMap = (HashMap)list.get(0);
            prodId=(String)whereMap.get("ACHDID");
            prodId+="["+(String)whereMap.get("ACHDDESC")+"]";
            whereMap = null;
            return prodId;
        }
        whereMap = null;
        return "";
    }
    public void getSubDepositNos(String depositNo){
        try{
            String mapName="Lien.getEditSubDepositNos";
            if(getActionType()==ClientConstants.ACTIONTYPE_NEW){
                mapName="Lien.getNewSubDepositNos";
            }
            cbmSubDepositNos = ClientUtil.getComboBoxModel(mapName, depositNo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /** Getter for property cbmSubDepositNos.
     * @return Value of property cbmSubDepositNos.
     *
     */
    public ComboBoxModel getCbmSubDepositNos() {
        return cbmSubDepositNos;
    }
    /** Setter for property cbmSubDepositNos.
     * @param cbmSubDepositNos New value of property cbmSubDepositNos.
     *
     */
    public void setCbmSubDepositNos(ComboBoxModel cbmSubDepositNos) {
        this.cbmSubDepositNos = cbmSubDepositNos;
    }
    /** Getter for property actionType.
     * @return Value of property actionType.
     *
     */
    public int getActionType() {
        return actionType;
    }
    /** Setter for property actionType.
     * @param actionType New value of property actionType.
     *
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    /** Getter for property result.
     * @return Value of property result.
     *
     */
    public int getResult() {
        return result;
    }
    /** Setter for property result.
     * @param result New value of property result.
     *
     */
    public void setResult(int result) {
        this.result = result;
        setChanged();
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
    public void setStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        setChanged();
        notifyObservers();
    }
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
        notifyObservers();
    }
    public void resetForm(){
        setLblStatus("");
        cbmProductId.setKeyForSelected("");
        cbmLienProductId.setKeyForSelected("");
        cbmCreditType.setKeyForSelected("");
        cbmLOSLoanType.setKeyForSelected("");
        if(this.cbmSubDepositNos!=null)
            this.cbmSubDepositNos.removeAllElements();
        if(lienTOs!=null)
            lienTOs.clear();
        if(deleteLienList!=null)
            deleteLienList.clear();
        this.tbmLien.setData(new ArrayList());
        
        lienAccountHD="";
        lienAccountNo="";
        lblClearBalance="";
        lblExistingLienAmt="";
        lblDepositAmt="";
        lblExistingFreezeAmt="";
        lblShadowLien="";
        lienAccountHD="";
        lienAccountNo="";
        lienAmount="";
        lienDate="";
        lienRemark="";
        lienCreditType="";
        cboLienProdID="";
        cboSubDepositNo="";
        txtDepositNo="";
        this.lienNo="";
        losLienAcNo="";
        losLienAmount="";
        losLienCustName="";
        losLienDate="";
        losLienRemarks="";
        authorizeMap=null;
        
        setChanged();
        notifyObservers();
    }
    /** Getter for property tbmLien.
     * @return Value of property tbmLien.
     *
     */
    public TableModel getTbmLien() {
        return tbmLien;
    }
    /** Setter for property tbmLien.
     * @param tbmLien New value of property tbmLien.
     *
     */
    public void setTbmLien(TableModel tbmLien) {
        this.tbmLien = tbmLien;
    }
    // Setter method for txtDepositNo
    public void setTxtDepositNo(String txtDepositNo){
        this.txtDepositNo = txtDepositNo;
        setChanged();
    }
    // Getter method for txtDepositNo
    public String getTxtDepositNo(){
        return this.txtDepositNo;
    }
    public void getAmountDetails(String subDepositNo){
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO",getTxtDepositNo());
        whereMap.put("SUBDEPOSITNO",CommonUtil.convertObjToInt(subDepositNo));
        List list = ClientUtil.executeQuery("getLienSubDepositAmount",whereMap);
        if(list!=null && list.size()>0){
            whereMap = (HashMap)list.get(0);
            this.setDepositDate((Date)whereMap.get("DEPOSIT_DT"));
            this.setLblClearBalance(((BigDecimal)whereMap.get("CLEAR")).toString());
            if(getChkLOS()){
               this.setLblExistingLienAmt(((BigDecimal)whereMap.get("LOSLIENAMT")).toString()); 
            }else{
            this.setLblExistingLienAmt(((BigDecimal)whereMap.get("LIENAMT")).toString());
            }
            this.setLblDepositAmt(((BigDecimal)whereMap.get("AMOUNT")).toString());
            this.setLblExistingFreezeAmt(((BigDecimal)whereMap.get("FREEZEAMT")).toString());
            this.setLblShadowLien(((BigDecimal)whereMap.get("SHADOW_LIEN")).toString());
        }  else {
            this.setLblClearBalance("0.0");
            this.setLblExistingLienAmt("0.0");
            this.setLblDepositAmt("0.0");
            this.setLblExistingFreezeAmt("0.0");
            this.setLblShadowLien("0.0");
        }
        whereMap = null;
    }
    
    
    
    
     public void getMdsAmountDts(String chitNo,String subDepositNo,String prod_id){
        HashMap whereMap = new HashMap();
        whereMap.put("CHIT_NO",chitNo);
        whereMap.put("SUB_NO",CommonUtil.convertObjToInt(subDepositNo));
         whereMap.put("PROD_ID",prod_id);
        List list = ClientUtil.executeQuery("getMdsAmountDts",whereMap);
        if(list!=null && list.size()>0){
            whereMap = (HashMap)list.get(0);
            //this.setDepositDate((Date)whereMap.get("DEPOSIT_DT"));
            
            this.setLblDepositAmt(((BigDecimal)whereMap.get("CLEAR")).toString());
              }  else {
//            this.setLblClearBalance("0.0");
//            this.setLblExistingLienAmt("0.0");
            this.setLblDepositAmt("0.0");
//            this.setLblExistingFreezeAmt("0.0");
//            this.setLblShadowLien("0.0");
        }
//            if(getChkLOS()){
//               this.setLblExistingLienAmt(((BigDecimal)whereMap.get("LOSLIENAMT")).toString()); 
//            }else
//            {
//            this.setLblExistingLienAmt(((BigDecimal)whereMap.get("LIENAMT")).toString());
//            }
            HashMap aMap=new HashMap();
           aMap.put("CHIT_NO",chitNo);
           aMap.put("SUB_NO",CommonUtil.convertObjToInt(subDepositNo));
           aMap.put("PROD_ID",prod_id);
            List amountList=ClientUtil.executeQuery("getCurrentAmountFromMDS", aMap);
            if(amountList!=null && amountList.size()>0)
            {
             aMap=(HashMap)amountList.get(0);
                System.out.println("aMap"+aMap);
            this.setLblClearBalance(((BigDecimal)aMap.get("AMOUNT")).toString());
        }
            else
            {
//                  this.setLblDepositAmt("0.0");
//                this.setLblExistingLienAmt("0.0");
//            this.setLblDepositAmt("0.0");
            this.setLblExistingFreezeAmt("0.0");
//            this.setLblShadowLien("0.0");
            }
            
            
          whereMap = new HashMap();
        whereMap.put("DEPOSITNO",getTxtDepositNo());
        whereMap.put("SUBDEPOSITNO",subDepositNo);
        List list1 = ClientUtil.executeQuery("getLeainMdsData",whereMap);
              if(list1!=null && list1.size()>0){
            whereMap = (HashMap)list1.get(0);
//            this.setDepositDate((Date)whereMap.get("DEPOSIT_DT"));
//            this.setLblClearBalance(((BigDecimal)whereMap.get("CLEAR")).toString());
            if(getChkLOS()){
               this.setLblExistingLienAmt(((BigDecimal)whereMap.get("LOSLIENAMT")).toString()); 
            }else{
            this.setLblExistingLienAmt(((BigDecimal)whereMap.get("LIENAMT")).toString());
            }
//            this.setLblDepositAmt(((BigDecimal)whereMap.get("AMOUNT")).toString());
            this.setLblExistingFreezeAmt("0.0");
            this.setLblShadowLien(((BigDecimal)whereMap.get("SHADOW_LIEN")).toString());
            
              }
              else
              {
                    this.setLblExistingLienAmt("0.0");
//            this.setLblDepositAmt("0.0");
            this.setLblShadowLien("0.0");
              }
//            this.setLblExistingFreezeAmt(((BigDecimal)whereMap.get("FREEZEAMT")).toString());
//            this.setLblShadowLien(((BigDecimal)whereMap.get("SHADOW_LIEN")).toString());
      
        whereMap = null;
    }
    
    
    /** Getter for property lblClearBalance.
     * @return Value of property lblClearBalance.
     *
     */
    public java.lang.String getLblClearBalance() {
        return lblClearBalance;
    }
    /** Setter for property lblClearBalance.
     * @param lblClearBalance New value of property lblClearBalance.
     *
     */
    public void setLblClearBalance(java.lang.String lblClearBalance) {
        this.lblClearBalance = lblClearBalance;
    }
    
    /** Getter for property lblExistingLienAmt.
     * @return Value of property lblExistingLienAmt.
     *
     */
    public java.lang.String getLblExistingLienAmt() {
        return lblExistingLienAmt;
    }
    
    /** Setter for property lblExistingLienAmt.
     * @param lblExistingLienAmt New value of property lblExistingLienAmt.
     *
     */
    public void setLblExistingLienAmt(java.lang.String lblExistingLienAmt) {
        this.lblExistingLienAmt = lblExistingLienAmt;
    }
    public void getLienData(String subDepositNo){
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO",getTxtDepositNo());
        whereMap.put("SUBDEPOSITNO",CommonUtil.convertObjToInt(subDepositNo));
        whereMap.put("LIENNO",getLblDepositLienDesc());
        String mapName="getSelectDepositLienTO";
        
        if(this.getActionType()==ClientConstants.ACTIONTYPE_DELETE)
            mapName="getDeleteDepositLienTO";
        
        if(getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE ||
            getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION ||
            getActionType()==ClientConstants.ACTIONTYPE_REJECT )            
            mapName="getAuthorizeDepositLienTO";
        
        List list = ClientUtil.executeQuery(mapName,whereMap);
        if(list!=null && list.size()>0){
            lienTOs=(ArrayList)list;
            setTableData();
        }else {
            resetTabel();
        }
        whereMap = null;
    }
    private void setTableData(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        DepositLienTO obj ;
        int size = lienTOs.size();
        for(int i=0;i<size;i++){
            obj = (DepositLienTO)lienTOs.get(i);
            if(getChkLOS()){
               row = setLOSRow(obj); 
            }else{
            row = setRow(obj);
            }
            rows.add(row);
        }
        tbmLien.setData(rows);
        tbmLien.fireTableDataChanged();
        obj=null;
    }
    private ArrayList setRow(DepositLienTO obj){
        ArrayList row= new ArrayList();
        row.add(obj.getLienNo());
        row.add(obj.getLienAmount());
        row.add(obj.getLienDt());
        row.add(obj.getStatus());
        row.add(obj.getAuthorizeStatus());
        return row;
    }
       private ArrayList setLOSRow(DepositLienTO obj){
        ArrayList row= new ArrayList();
        row.add(obj.getLienNo());
        row.add(obj.getLosLienAmount());
        row.add(obj.getLosLienDt());
        row.add(obj.getStatus());
        row.add(obj.getAuthorizeStatus());
        return row;
    }
    
    public void populateLien(int rowNum) {
        DepositLienTO obj =(DepositLienTO)lienTOs.get(rowNum);
        this.setLienAccountHD(obj.getLienAcHd());
        this.setLienAccountNo(obj.getLienAcNo());
        this.setLienAmount(CommonUtil.convertObjToStr(obj.getLienAmount()));
        this.setLienDate(DateUtil.getStringDate(obj.getLienDt()));
        this.setLienRemark(obj.getRemarks());
        this.setLienCreditType(obj.getCreditLienAcct());
        this.setCboLienProdID(obj.getLienProdId());
        setStatusBy1(obj.getStatusBy());
        this.setLienNo(obj.getLienNo());
        this.setLienAuthorizeStatus(obj.getAuthorizeStatus());
        this.setLOSLienAcNo(obj.getLosLienAcNo());
        this.setLOSLienAmount(CommonUtil.convertObjToStr(obj.getLosLienAmount()));
        this.setLOSLienCustName(obj.getLosLienCustName());
        this.setLOSLienDate( DateUtil.getStringDate(obj.getLosLienDt()));
        this.setLOSLienRemarks(obj.getLosLienRemarks());
          this.setCboLOSLoanType(obj.getLosLienLoanType());  
        
        obj=null;
    }
    public void deleteLienData(int rowNum) {
        DepositLienTO obj = setTOStatus((DepositLienTO)lienTOs.get(rowNum));
        if((obj.getLienNo().compareToIgnoreCase("-")!=0)||
               obj.getStatus()==CommonConstants.STATUS_UNLIEN)
            deleteLienList.add(obj);
        
        lienTOs.remove(rowNum);
        
        tbmLien.removeRow(rowNum);
        tbmLien.fireTableDataChanged();
        
        obj=null;
    }
    public DepositLienTO setTOStatus(DepositLienTO obj){
        obj.setStatus(getLienStatus());
        if(obj.getStatus().compareToIgnoreCase(CommonConstants.STATUS_UNLIEN)==0){
            obj.setUnlienDt(curDate);
            obj.setUnlienRemarks(this.getUnLienRemark());
            obj.setAuthorizeDt(null);
            obj.setAuthorizeBy("");
            obj.setAuthorizeStatus("");
        }
        obj.setStatusBy(TrueTransactMain.USER_ID);
        obj.setStatusDt(curDate);

        return obj;
    }
    public int insertLienData(int rowNo) {
        DepositLienTO obj=setLienTO();
        if(rowNo==-1){
            obj.setLienNo("-");
            obj=setTOStatus(obj);
            lienTOs.add(obj);
            ArrayList irRow = this.setRow(obj);
            tbmLien.insertRow(tbmLien.getRowCount(), irRow);
        }else{
            obj=updateLienTO((DepositLienTO)lienTOs.get(rowNo),obj);
            if(obj.getLienNo().compareToIgnoreCase("-")!=0)
                obj=setTOStatus(obj);
            ArrayList irRow = setRow(obj);
            lienTOs.set(rowNo,obj);
            tbmLien.removeRow(rowNo);
            tbmLien.insertRow(rowNo,irRow);
        }
        tbmLien.fireTableDataChanged();
        obj=null;
        return 0;
    }
      public int insertLOSLienData(int rowNo) {
        DepositLienTO obj=setLienTO();
        if(rowNo==-1){
            obj.setLienNo("-");
            obj=setTOStatus(obj);
            lienTOs.add(obj);
            ArrayList irRow = this.setLOSRow(obj);
            tbmLien.insertRow(tbmLien.getRowCount(), irRow);
        }else{
            obj=updateLienTO((DepositLienTO)lienTOs.get(rowNo),obj);
            if(obj.getLienNo().compareToIgnoreCase("-")!=0)
                obj=setTOStatus(obj);
            ArrayList irRow = setLOSRow(obj);
            lienTOs.set(rowNo,obj);
            tbmLien.removeRow(rowNo);
            tbmLien.insertRow(rowNo,irRow);
        }
        tbmLien.fireTableDataChanged();
        obj=null;
        return 0;
    }
    
    private DepositLienTO updateLienTO(DepositLienTO oldTO,DepositLienTO newTO){
        oldTO.setLienAcHd(newTO.getLienAcHd());
        oldTO.setLienAcNo(newTO.getLienAcNo());
        oldTO.setLienProdId(newTO.getLienProdId());
        oldTO.setLienAmount(newTO.getLienAmount());
        oldTO.setLienDt(newTO.getLienDt());
        oldTO.setRemarks(newTO.getRemarks());
        oldTO.setCreditLienAcct(newTO.getCreditLienAcct());
        oldTO.setDepositNo(newTO.getDepositNo());
        oldTO.setDepositSubNo(newTO.getDepositSubNo());
        
        oldTO.setLosLienAcNo(newTO.getLosLienAcNo());
        oldTO.setLosLienAmount(newTO.getLosLienAmount());
        oldTO.setLosLienCustName(newTO.getLosLienCustName());
        oldTO.setLosLienDt(newTO.getLosLienDt());
        oldTO.setLosLienRemarks(newTO.getLosLienRemarks());
        oldTO.setLosLienLoanType(newTO.getLosLienLoanType());
        newTO = null;
        return oldTO;
    }
    private DepositLienTO setLienTO(){
        DepositLienTO obj = new DepositLienTO();
        obj.setLienAcHd(getLienAccountHD());
        obj.setLienAcNo(this.getLienAccountNo());
        obj.setLienAmount(CommonUtil.convertObjToDouble(this.getLienAmount()));
        Date Dt = DateUtil.getDateMMDDYYYY(this.getLienDate());
        if(Dt != null){
        Date dtDate = (Date)curDate.clone();
        dtDate.setDate(Dt.getDate());
        dtDate.setMonth(Dt.getMonth());
        dtDate.setYear(Dt.getYear());
//        obj.setLienDt(DateUtil.getDateMMDDYYYY(this.getLienDate()));
        obj.setLienDt(dtDate);
        }else{
            obj.setLienDt(DateUtil.getDateMMDDYYYY(this.getLienDate()));
        }
        obj.setRemarks(this.getLienRemark());
        obj.setCreditLienAcct(this.getLienCreditType());
        obj.setDepositNo(this.getTxtDepositNo());
        obj.setDepositSubNo(CommonUtil.convertObjToInt(this.getCboSubDepositNo()));
        obj.setLienProdId(this.getCboLienProdID());
        obj.setStatusBy(this.getStatusBy1());
        
        
        obj.setLosLienAcNo(getLOSLienAcNo());
        obj.setLosLienAmount(CommonUtil.convertObjToDouble(getLOSLienAmount()));
        obj.setLosLienCustName(getLOSLienCustName());
        Date LOSDt = DateUtil.getDateMMDDYYYY(this.getLOSLienDate());
        if(LOSDt != null){
        Date LosDate = (Date)curDate.clone();
        LosDate.setDate(LOSDt.getDate());
        LosDate.setMonth(LOSDt.getMonth());
        LosDate.setYear(LOSDt.getYear());
//        obj.setLienDt(DateUtil.getDateMMDDYYYY(this.getLienDate()));
        obj.setLosLienDt(LosDate);
        }else{
            obj.setLosLienDt(DateUtil.getDateMMDDYYYY(this.getLOSLienDate()));
        }
        obj.setLosLienRemarks(getLOSLienRemarks());
        obj.setLosLienLoanType(this.getCboLOSLoanType());
        
        return obj;
    }
    public boolean checkAuthStatus(){
        if(lienTOs !=null && lienTOs.size()>0){
            for(int i=0;i<lienTOs.size();i++){
                DepositLienTO depositLienTO=(DepositLienTO)lienTOs.get(i);
                if(depositLienTO.getAuthorizeStatus() == null || depositLienTO.getAuthorizeStatus().length()==0)
                    return true;
            }
        }
        return false;
    }
    /** Getter for property lienAmount.
     * @return Value of property lienAmount.
     *
     */
    public java.lang.String getLienAmount() {
        return lienAmount;
    }
    /** Setter for property lienAmount.
     * @param lienAmount New value of property lienAmount.
     *
     */
    public void setLienAmount(java.lang.String lienAmount) {
        this.lienAmount = lienAmount;
    }
    /** Getter for property lienAccountNo.
     * @return Value of property lienAccountNo.
     *
     */
    public java.lang.String getLienAccountNo() {
        return lienAccountNo;
    }
    /** Setter for property lienAccountNo.
     * @param lienAccountNo New value of property lienAccountNo.
     *
     */
    public void setLienAccountNo(java.lang.String lienAccountNo) {
        this.lienAccountNo = lienAccountNo;
    }
    /** Getter for property lienAccountHD.
     * @return Value of property lienAccountHD.
     *
     */
    public java.lang.String getLienAccountHD() {
        return lienAccountHD;
    }
    /** Setter for property lienAccountHD.
     * @param lienAccountHD New value of property lienAccountHD.
     *
     */
    public void setLienAccountHD(java.lang.String lienAccountHD) {
        this.lienAccountHD = lienAccountHD;
    }
    /** Getter for property lienCreditType.
     * @return Value of property lienCreditType.
     *
     */
    public java.lang.String getLienCreditType() {
        return lienCreditType;
    }
    /** Setter for property lienCreditType.
     * @param lienCreditType New value of property lienCreditType.
     *
     */
    public void setLienCreditType(java.lang.String lienCreditType) {
        this.lienCreditType = lienCreditType;
    }
    /** Getter for property lienDate.
     * @return Value of property lienDate.
     *
     */
    public java.lang.String getLienDate() {
        return lienDate;
    }
    /** Setter for property lienDate.
     * @param lienDate New value of property lienDate.
     *
     */
    public void setLienDate(java.lang.String lienDate) {
        this.lienDate = lienDate;
    }
    /** Getter for property lienRemark.
     * @return Value of property lienRemark.
     *
     */
    public java.lang.String getLienRemark() {
        return lienRemark;
    }
    /** Setter for property lienRemark.
     * @param lienRemark New value of property lienRemark.
     *
     */
    public void setLienRemark(java.lang.String lienRemark) {
        this.lienRemark = lienRemark;
    }
    /** Getter for property lienNo.
     * @return Value of property lienNo.
     *
     */
    public java.lang.String getLienNo() {
        return lienNo;
    }
    /** Setter for property lienNo.
     * @param lienNo New value of property lienNo.
     *
     */
    public void setLienNo(java.lang.String lienNo) {
        this.lienNo = lienNo;
    }
    /** Getter for property lienStatus.
     * @return Value of property lienStatus.
     *
     */
    public java.lang.String getLienStatus() {
        return lienStatus;
    }
    
    /** Setter for property lienStatus.
     * @param lienStatus New value of property lienStatus.
     *
     */
    public void setLienStatus(java.lang.String lienStatus) {
        this.lienStatus = lienStatus;
    }

    /** Getter for property cboSubDepositNo.
     * @return Value of property cboSubDepositNo.
     *
     */
   

    
    public void doAction(){
        try{
            HashMap objHashMap=null;
            if(authorizeMap==null) {
                lienTOs.addAll(deleteLienList);
                if(lienTOs!=null && lienTOs.size()>0){
                    objHashMap = new HashMap();
                    if ( getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_INSERT );
                    } else if ( getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_UPDATE );
                    } else if ( getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_DELETE );
                        setStatusForTOs();
                    }
                    objHashMap.put("lienTOs",lienTOs);
                    objHashMap.put("SHADOWLIEN",CommonUtil.convertObjToDouble(this.getLblShadowLien()));
                    if(getChkLOS()){
                    objHashMap.put("CHK_VAL","Y");
                    }
                    else{
                     objHashMap.put("CHK_VAL","N");   
                    }
                }
            }else {
                objHashMap=authorizeMap;
            }
            HashMap proxyResultMap = proxy.execute(objHashMap,map);
            setProxyReturnMap(proxyResultMap);
            setResult(getActionType());
            objHashMap = null;
        }catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    
    public boolean checkAcNoWithoutProdType(String actNum, boolean depositOldNo) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                if(depositOldNo){
                    setTxtDepositNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                }else{
                    setLienAccountNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                }
                if(depFlag == true)
                    cbmProductId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                if(loanFlag == true){
                    cbmLienProductId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                    getLienProductHead(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                }
                isExists = true;
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
//                setCbmProductId("");
                isExists = false;
//                key = null;
//                value = null;
                isExists = false;
            }
            mapDataList = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    
    private void setStatusForTOs(){
        DepositLienTO objTO;
        this.setLienStatus(CommonConstants.STATUS_DELETED);
        int size = lienTOs.size();
        for(int i=0;i<size;i++){
            objTO =(DepositLienTO)lienTOs.get(i);
            objTO=this.setTOStatus(objTO);
            lienTOs.set(i, objTO);
        }
        this.setLblShadowLien("0");
        objTO=null;
    }
    public String getLienProductHead(String prodID){
        HashMap whereMap = new HashMap();
        whereMap.put("PRODID", prodID);
        List list = ClientUtil.executeQuery("getDepositLienProdAcHD", whereMap);
        if(list!=null && list.size()>0){
            whereMap = null;
            return (String)((HashMap)list.get(0)).get("AC_HEAD");
        }
        whereMap = null;
        return "";
    }
    public String getLienActNumCustomer(String lienActNum){
        HashMap whereMap;
        String customerName="";
        if(lienActNum!=null && lienActNum.length()>0){
            whereMap = new HashMap();
            whereMap.put("ACTNUM", lienActNum);
            List list = ClientUtil.executeQuery("getDepositLienAcCustomerID", whereMap);
            if(list!=null && list.size()>0){
                whereMap=(HashMap)list.get(0);
                System.out.println("whreremappp"+whereMap);
                this.setActOpeningDate((Date)whereMap.get("AOD"));
                customerName = ((String)whereMap.get("CUSTOMER_NAME"))+"["+((String)whereMap.get("CUST_ID")+"]");
            }
            list = null;
        }
        whereMap = null;
        System.out.println("customerName+.."+customerName);
        return customerName;
        
    }
    
    /** Getter for property cboLienProdID.
     * @return Value of property cboLienProdID.
     *
     */
    public java.lang.String getCboLienProdID() {
        return cboLienProdID;
    }
    
    /** Setter for property cboLienProdID.
     * @param cboLienProdID New value of property cboLienProdID.
     *
     */
    public void setCboLienProdID(java.lang.String cboLienProdID) {
        this.cboLienProdID = cboLienProdID;
    }
    
    
    
    public java.lang.String getCboLOSLoanType() {
        return cboLOSLoanType;
    }
    
    /** Setter for property cboLienProdID.
     * @param cboLienProdID New value of property cboLienProdID.
     *
     */
    public void setCboLOSLoanType(java.lang.String cboLOSLoanType) {
        this.cboLOSLoanType = cboLOSLoanType;
    }
    
    /** Getter for property cbmLienProductId.
     * @return Value of property cbmLienProductId.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmLienProductId() {
        return cbmLienProductId;
    }
    
    /** Setter for property cbmLienProductId.
     * @param cbmLienProductId New value of property cbmLienProductId.
     *
     */
    public void setCbmLienProductId(com.see.truetransact.clientutil.ComboBoxModel cbmLienProductId) {
        this.cbmLienProductId = cbmLienProductId;
    }
    
    /** Getter for property actOpeningDate.
     * @return Value of property actOpeningDate.
     *
     */
    public java.util.Date getActOpeningDate() {
        return actOpeningDate;
    }
    
    /** Setter for property actOpeningDate.
     * @param actOpeningDate New value of property actOpeningDate.
     *
     */
    public void setActOpeningDate(java.util.Date actOpeningDate) {
        this.actOpeningDate = actOpeningDate;
    }
    
    /** Getter for property depositDate.
     * @return Value of property depositDate.
     *
     */
    public java.util.Date getDepositDate() {
        return depositDate;
    }
    
    /** Setter for property depositDate.
     * @param depositDate New value of property depositDate.
     *
     */
    public void setDepositDate(java.util.Date depositDate) {
        this.depositDate = depositDate;
    }
    
    /** Getter for property cbmCreditType.
     * @return Value of property cbmCreditType.
     *
     */
    public ComboBoxModel getCbmCreditType() {
        return cbmCreditType;
    }
    
    /** Setter for property cbmCreditType.
     * @param cbmCreditType New value of property cbmCreditType.
     *
     */
    public void setCbmCreditType(ComboBoxModel cbmCreditType) {
        this.cbmCreditType = cbmCreditType;
    }
    public HashMap getDepositActIfno(){
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNUM", this.getTxtDepositNo());
        List list = ClientUtil.executeQuery("getDepositActInfo",whereMap);
        if(list!=null && list.size()>0){
            whereMap = (HashMap)list.get(0);
            return whereMap;
        }
        return null;
    }
    
    public HashMap getMDSActIfno(){
        HashMap whereMap = new HashMap();
        whereMap.put("CHITTAL_NO", this.getTxtDepositNo());
        List list = ClientUtil.executeQuery("getMdsActInfo",whereMap);
        if(list!=null && list.size()>0){
            whereMap = (HashMap)list.get(0);
            return whereMap;
        }
        return null;
    }
    
    
    public void populateLien(String lienNo){
        DepositLienTO obj;
        int size = lienTOs.size();
        for(int i=0;i<size;i++){
            obj = (DepositLienTO)lienTOs.get(i);
            if(obj.getLienNo().equals(lienNo)){
                this.setLienNo(obj.getLienNo());
                this.populateLien(i);
                obj=null;
                return;
            }
        }
    }
    public boolean authorizeStatus(){
        String authorizeStatus = (String)authorizeMap.get("SELECTED_AUTHORIZE_STATUS");
        if(authorizeStatus==null || authorizeStatus.length()==0)
            authorizeStatus = this.getAuthorizeStatus();
        
        String status;
        double amount=0,shadowLienAmt=0,bal=0;
        status=(String)authorizeMap.get("STATUS");
        
        authorizeMap.put("COMMAND_STATUS",status);
        
        authorizeMap.put("AUTHORIZE_STATUS", authorizeStatus);
        authorizeMap.put("USER_ID",TrueTransactMain.USER_ID);
        authorizeMap.put("AUTHORIZE_DATE",curDate);
        if(getChkLOS()){
            amount=CommonUtil.convertObjToDouble(authorizeMap.get("LOS_LIEN_AMOUNT")).doubleValue();
        }else{
         amount=CommonUtil.convertObjToDouble(authorizeMap.get("LIEN_AMOUNT")).doubleValue();
        }
//        amount=CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue();
        if((status.compareToIgnoreCase(CommonConstants.STATUS_DELETED)!=0) && (authorizeStatus.compareToIgnoreCase(CommonConstants.STATUS_AUTHORIZED)==0)){
            bal = CommonUtil.convertObjToDouble(authorizeMap.get("BALANCE")).doubleValue();
            if(status.equalsIgnoreCase(CommonConstants.STATUS_UNLIEN))
//                amount=-amount;
                amount=amount;
            if(!status.equalsIgnoreCase(CommonConstants.STATUS_UNLIEN)){
                if(bal<amount){
                    return false;
                }
                shadowLienAmt=amount;
            }
        }
        if((status.compareToIgnoreCase(CommonConstants.STATUS_DELETED)==0) && 
              (authorizeStatus.compareToIgnoreCase(CommonConstants.STATUS_AUTHORIZED)==0)){
            amount=0;
        }
        if(authorizeStatus.compareToIgnoreCase(CommonConstants.STATUS_REJECTED)==0){
            if(status.equalsIgnoreCase(CommonConstants.STATUS_UNLIEN)){
                status=CommonConstants.STATUS_MODIFIED;
                authorizeMap.put("AUTHORIZE_STATUS",CommonConstants.STATUS_AUTHORIZED);
            }else if(status.equalsIgnoreCase(CommonConstants.STATUS_DELETED)){
                status=CommonConstants.STATUS_MODIFIED;
                shadowLienAmt=-amount;
                authorizeMap.put("AUTHORIZE_STATUS","");
                authorizeMap.put("USER_ID","");
                authorizeMap.put("AUTHORIZE_DATE",null);
            }else {
                shadowLienAmt=amount;
            }
            amount=0;
        }
        authorizeMap.put("STATUS",status);
        authorizeMap.put("LIENAMOUNT",new Double(amount));
        authorizeMap.put("SHADOWLIEN",new Double(shadowLienAmt));
        
        authorizeMap.put("ACTION",authorizeStatus);
        authorizeMap.put("COMMAND", CommonConstants.AUTHORIZEDATA);
        this.doAction();
        authorizeMap=null;
        return true;
    }
    public java.lang.String getAuthorizeStatus(){
        if(this.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE)
            return CommonConstants.STATUS_AUTHORIZED;
        else if(this.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION)
            return CommonConstants.STATUS_EXCEPTION;
        else if(this.getActionType()==ClientConstants.ACTIONTYPE_REJECT)
            return CommonConstants.STATUS_REJECTED;
        return "";
    }
    /** Getter for property lblExistingFreezeAmt.
     * @return Value of property lblExistingFreezeAmt.
     *
     */
    public java.lang.String getLblExistingFreezeAmt() {
        return lblExistingFreezeAmt;
    }
    
    /** Setter for property lblExistingFreezeAmt.
     * @param lblExistingFreezeAmt New value of property lblExistingFreezeAmt.
     *
     */
    public void setLblExistingFreezeAmt(java.lang.String lblExistingFreezeAmt) {
        this.lblExistingFreezeAmt = lblExistingFreezeAmt;
    }
    
    /** Getter for property lblDepositAmt.
     * @return Value of property lblDepositAmt.
     *
     */
    public java.lang.String getLblDepositAmt() {
        return lblDepositAmt;
    }
    
    /** Setter for property lblDepositAmt.
     * @param lblDepositAmt New value of property lblDepositAmt.
     *
     */
    public void setLblDepositAmt(java.lang.String lblDepositAmt) {
        this.lblDepositAmt = lblDepositAmt;
    }
    
    /** Getter for property unLienRemark.
     * @return Value of property unLienRemark.
     *
     */
    public java.lang.String getUnLienRemark() {
        return unLienRemark;
    }
    
    /** Setter for property unLienRemark.
     * @param unLienRemark New value of property unLienRemark.
     *
     */
    public void setUnLienRemark(java.lang.String unLienRemark) {
        this.unLienRemark = unLienRemark;
    }
    
    /** Getter for property lblShadowLien.
     * @return Value of property lblShadowLien.
     *
     */
    public java.lang.String getLblShadowLien() {
        return lblShadowLien;
    }
    
    /** Setter for property lblShadowLien.
     * @param lblShadowLien New value of property lblShadowLien.
     *
     */
    public void setLblShadowLien(java.lang.String lblShadowLien) {
        this.lblShadowLien = lblShadowLien;
    }
    
    /** Getter for property lienAuthorizeStatus.
     * @return Value of property lienAuthorizeStatus.
     *
     */
    public java.lang.String getLienAuthorizeStatus() {
        return lienAuthorizeStatus;
    }
    
    /** Setter for property lienAuthorizeStatus.
     * @param lienAuthorizeStatus New value of property lienAuthorizeStatus.
     *
     */
    public void setLienAuthorizeStatus(java.lang.String lienAuthorizeStatus) {
        this.lienAuthorizeStatus = lienAuthorizeStatus;
    }
    
    /** Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     *
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    /** Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     *
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    public void resetTabel(){
        this.tbmLien.setData(new ArrayList());
        this.tbmLien.fireTableDataChanged();
        this.lienTOs.clear();
    }
    
    /**
     * Getter for property lblDepositLienDesc.
     * @return Value of property lblDepositLienDesc.
     */
    public java.lang.String getLblDepositLienDesc() {
        return lblDepositLienDesc;
    }
    
    /**
     * Setter for property lblDepositLienDesc.
     * @param lblDepositLienDesc New value of property lblDepositLienDesc.
     */
    public void setLblDepositLienDesc(java.lang.String lblDepositLienDesc) {
        this.lblDepositLienDesc = lblDepositLienDesc;
    }
    
    /**
     * Getter for property statusBy1.
     * @return Value of property statusBy1.
     */
    public java.lang.String getStatusBy1() {
        return statusBy1;
    }
    
    /**
     * Setter for property statusBy1.
     * @param statusBy1 New value of property statusBy1.
     */
    public void setStatusBy1(java.lang.String statusBy1) {
        this.statusBy1 = statusBy1;
    }
    
    public void setLOSLienAcNo(String losLienAcNo){
        this.losLienAcNo=losLienAcNo;
    }
    public String getLOSLienAcNo(){
        return losLienAcNo;
    }
    
    public void setLOSLienCustName(String losLienCustName){
        this.losLienCustName=losLienCustName;
    }
    public String getLOSLienCustName(){
        return losLienCustName;
    }
    
    public void setLOSLienAmount(String losLienAmount){
        this.losLienAmount=losLienAmount;
    }
    public String getLOSLienAmount(){
        return losLienAmount;
    }
    
    public void setLOSLienDate(String losLienDate){
        this.losLienDate=losLienDate;
    }
    public String getLOSLienDate(){
        return losLienDate;
    }
    
    public void setLOSLienRemarks(String losLienRemarks){
        this.losLienRemarks=losLienRemarks;
    }
    
     public String  getLOSLienRemarks(){
        return losLienRemarks;
    }
    public void setChkLOS(boolean chkValue){
        this.chkValue=chkValue;
    }
     public boolean getChkLOS(){
        return chkValue;
    }

    public String getCboSubDepositNo() {
        return cboSubDepositNo;
    }

    public void setCboSubDepositNo(String cboSubDepositNo) {
        this.cboSubDepositNo = cboSubDepositNo;
    }
     
     
}
