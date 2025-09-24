/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositFreezeOB.java
 *
 * Created on June 2, 2004, 5:55 PM
 */

package com.see.truetransact.ui.deposit.freeze;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.deposit.freeze.DepositFreezeTO;
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

public class DepositFreezeOB extends CObservable{
    
    private static DepositFreezeOB depositFreezeOB;
    private ProxyFactory proxy;
    private final static ClientParseException parseException
                                            = ClientParseException.getInstance();
    private HashMap map,lookupMap,authorizeMap;
    private ComboBoxModel cbmProductId,cbmSubDepositNos,cbmFreezeType;
    private TableModel tbmFreeze;
    private ArrayList key,value,freezeTOs,deleteFreezeList;
    
    private int actionType;
    private int result;
    
    
    private String lblStatus;
    private String lblClearBalance;
    private String lblExistingFreezeAmt;
    private String lblExistingLienAmt;
    private String lblDepositAmt;
    private String lblShadowFreeze;
    private String freezeAuthorizeStatus;
    
    private String txtDepositNo;
    private String txtFreezeAmount;
    private String tdtFreezeDate;
    private String txtFreezeRemark;
    private String cboFreezeType;
    private String cboSubDepositNo;
    
    private String freezeNo;
    private String freezeStatus;
    private String unFreezeRemark;
    private String lblDepositFreezeNoDesc=""; 
    private String statusBy1="";
    private String AuthorizeStatus1="";
    Date curDate = null;
  
    
    /** Creates a new instance of DepositFreezeOB */
    private DepositFreezeOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "DepositFreezeJNDI");
        map.put(CommonConstants.HOME, "deposit.freeze.DepositFreezeHome");
        map.put(CommonConstants.REMOTE,"deposit.freeze.DepositFreeze");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        fillDropDown();
        setTable();
        freezeTOs = new ArrayList();
        deleteFreezeList = new ArrayList();
    }
    
    static {
        try {
            depositFreezeOB = new DepositFreezeOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public static DepositFreezeOB getInstance(){
        return depositFreezeOB;
    }
    
    private void setTable(){
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Freeze SLNo");
        columnHeader.add("Amount");
        columnHeader.add("Date");
        columnHeader.add("Status");
        columnHeader.add("Authorize Status");
        columnHeader.add("Type");
        ArrayList data = new ArrayList();
        tbmFreeze = new TableModel(data,columnHeader);
    }
    
    private void fillDropDown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        HashMap param = new HashMap();
        
        param.put(CommonConstants.MAP_NAME, "getDepositProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap lookupValues = ClientUtil.populateLookupData(param);
        
        fillData((HashMap)lookupValues.get(CommonConstants.DATA));
        cbmProductId = new ComboBoxModel(key,value);
        
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("FREEZE.FREEZETYPE");
        
        param.put(CommonConstants.MAP_NAME, null);
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        
        lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get("FREEZE.FREEZETYPE"));
        this.cbmFreezeType = new ComboBoxModel(key,value);
        
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
    
    /** Getter for property cbmFreezeType.
     * @return Value of property cbmFreezeType.
     *
     */
    public ComboBoxModel getCbmFreezeType() {
        return cbmFreezeType;
    }
    
    /** Setter for property cbmFreezeType.
     * @param cbmFreezeType New value of property cbmFreezeType.
     *
     */
    public void setCbmFreezeType(ComboBoxModel cbmFreezeType) {
        this.cbmFreezeType = cbmFreezeType;
    }
    
    /** Getter for property cboSubDepositNo.
     * @return Value of property cboSubDepositNo.
     *
     */
    public java.lang.String getCboSubDepositNo() {
        return cboSubDepositNo;
    }
    
    /** Setter for property cboSubDepositNo.
     * @param cboSubDepositNo New value of property cboSubDepositNo.
     *
     */
    public void setCboSubDepositNo(java.lang.String cboSubDepositNo) {
        this.cboSubDepositNo = cboSubDepositNo;
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
    
    /** Getter for property freezeNo.
     * @return Value of property freezeNo.
     *
     */
    public java.lang.String getFreezeNo() {
        return freezeNo;
    }
    
    /** Setter for property freezeNo.
     * @param freezeNo New value of property freezeNo.
     *
     */
    public void setFreezeNo(java.lang.String freezeNo) {
        this.freezeNo = freezeNo;
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
    
    /** Getter for property freezeStatus.
     * @return Value of property freezeStatus.
     *
     */
    public java.lang.String getFreezeStatus() {
        return freezeStatus;
    }
    
    /** Setter for property freezeStatus.
     * @param freezeStatus New value of property freezeStatus.
     *
     */
    public void setFreezeStatus(java.lang.String freezeStatus) {
        this.freezeStatus = freezeStatus;
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
    
    /** Getter for property tbmFreeze.
     * @return Value of property tbmFreeze.
     *
     */
    public TableModel getTbmFreeze() {
        return tbmFreeze;
    }
    
    /** Setter for property tbmFreeze.
     * @param tbmFreeze New value of property tbmFreeze.
     *
     */
    public void setTbmFreeze(TableModel tbmFreeze) {
        this.tbmFreeze = tbmFreeze;
    }
    
    /** Getter for property txtDepositNo.
     * @return Value of property txtDepositNo.
     *
     */
    public java.lang.String getTxtDepositNo() {
        return txtDepositNo;
    }
    
    /** Setter for property txtDepositNo.
     * @param txtDepositNo New value of property txtDepositNo.
     *
     */
    public void setTxtDepositNo(java.lang.String txtDepositNo) {
        this.txtDepositNo = txtDepositNo;
    }
    
    /** Getter for property cboFreezeType.
     * @return Value of property cboFreezeType.
     *
     */
    public java.lang.String getCboFreezeType() {
        return cboFreezeType;
    }
    
    /** Setter for property cboFreezeType.
     * @param cboFreezeType New value of property cboFreezeType.
     *
     */
    public void setCboFreezeType(java.lang.String cboFreezeType) {
        this.cboFreezeType = cboFreezeType;
    }
    
    /** Getter for property tdtFreezeDate.
     * @return Value of property tdtFreezeDate.
     *
     */
    public java.lang.String getTdtFreezeDate() {
        return tdtFreezeDate;
    }
    
    /** Setter for property tdtFreezeDate.
     * @param tdtFreezeDate New value of property tdtFreezeDate.
     *
     */
    public void setTdtFreezeDate(java.lang.String tdtFreezeDate) {
        this.tdtFreezeDate = tdtFreezeDate;
    }
    
    /** Getter for property txtFreezeAmount.
     * @return Value of property txtFreezeAmount.
     *
     */
    public java.lang.String getTxtFreezeAmount() {
        return txtFreezeAmount;
    }
    
    /** Setter for property txtFreezeAmount.
     * @param txtFreezeAmount New value of property txtFreezeAmount.
     *
     */
    public void setTxtFreezeAmount(java.lang.String txtFreezeAmount) {
        this.txtFreezeAmount = txtFreezeAmount;
    }
    
    /** Getter for property txtFreezeRemark.
     * @return Value of property txtFreezeRemark.
     *
     */
    public java.lang.String getTxtFreezeRemark() {
        return txtFreezeRemark;
    }
    
    /** Setter for property txtFreezeRemark.
     * @param txtFreezeRemark New value of property txtFreezeRemark.
     *
     */
    public void setTxtFreezeRemark(java.lang.String txtFreezeRemark) {
        this.txtFreezeRemark = txtFreezeRemark;
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
            String mapName="Freeze.getEditSubDepositNos";
            if(getActionType()==ClientConstants.ACTIONTYPE_NEW){
                mapName="Freeze.getNewSubDepositNos";
            }            
            cbmSubDepositNos = ClientUtil.getComboBoxModel(mapName, depositNo);
        }catch(Exception e){
            e.printStackTrace();
        }
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
        cbmFreezeType.setKeyForSelected("");
        if(this.cbmSubDepositNos!=null)
            this.cbmSubDepositNos.removeAllElements();
        if(freezeTOs!=null)
            freezeTOs.clear();
        if(deleteFreezeList!=null)
            deleteFreezeList.clear();
        this.tbmFreeze.setData(new ArrayList());        
        
        lblClearBalance="";
        lblExistingFreezeAmt="";
        lblExistingLienAmt="";
        lblDepositAmt="";
        lblShadowFreeze="";
        
        txtFreezeAmount="";
        tdtFreezeDate="";
        txtFreezeRemark="";
        cboFreezeType="";
        cboSubDepositNo="";
        txtDepositNo="";
        freezeNo="";
        setChanged();
        notifyObservers();
    }
    public void getAmountDetails(String subDepositNo){
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO",getTxtDepositNo());
        whereMap.put("SUBDEPOSITNO",CommonUtil.convertObjToInt(subDepositNo));
        List list = ClientUtil.executeQuery("getFreezeSubDepositAmount",whereMap);
        if(list!=null && list.size()>0){
            whereMap = (HashMap)list.get(0);
            this.setLblClearBalance(((BigDecimal)whereMap.get("CLEAR")).toString());
            this.setLblExistingFreezeAmt(((BigDecimal)whereMap.get("FREEZEAMT")).toString());
            this.setLblExistingLienAmt(((BigDecimal)whereMap.get("LIENAMT")).toString());
            this.setLblDepositAmt(((BigDecimal)whereMap.get("AMOUNT")).toString());
            this.setLblShadowFreeze(((BigDecimal)whereMap.get("SHADOW_FREEZE")).toString());                        
        }  else {
            this.setLblClearBalance("0.0");
            this.setLblExistingFreezeAmt("0.0");
            this.setLblExistingLienAmt("0.0");
            this.setLblDepositAmt("0.0");
            this.setLblShadowFreeze("0.0");    
        }
        whereMap = null;
    }
    
    public void getFreezeData(String subDepositNo){
        
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO",getTxtDepositNo());
        whereMap.put("SUBDEPOSITNO",subDepositNo);
        whereMap.put("FREEZENO",getLblDepositFreezeNoDesc());
        String mapName="getSelectDepositFreezeTO";
        if(this.getActionType()==ClientConstants.ACTIONTYPE_DELETE)
            mapName="getDeleteDepositFreezeTO";
        if(this.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE ||
            this.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION ||
            this.getActionType()==ClientConstants.ACTIONTYPE_REJECT )
            mapName="getAuthorizeDepositFreezeTO";
       
           
        List list = ClientUtil.executeQuery(mapName,whereMap);
        if(list!=null && list.size()>0){
            freezeTOs=(ArrayList)list;
            setTableData();
        }else {
            resetTabel();
        }
        whereMap = null;
    }
    private void setTableData(){
        ArrayList row;
        ArrayList rows = new ArrayList();
        DepositFreezeTO obj ;
        int size = freezeTOs.size();
        for(int i=0;i<size;i++){
            obj = (DepositFreezeTO)freezeTOs.get(i);
            row = setRow(obj);
            rows.add(row);
        }
        tbmFreeze.setData(rows);
        tbmFreeze.fireTableDataChanged();
        obj=null;
    }
    private ArrayList setRow(DepositFreezeTO obj){
        ArrayList row= new ArrayList();
        row.add(obj.getFslNo());
        row.add(obj.getAmount());
        row.add(obj.getFreezeDt());
        row.add(obj.getStatus());
        row.add(obj.getAuthorizeStatus());
        row.add(obj.getType());
        return row;
    }
    public void populateFreeze(int rowNum) {
        DepositFreezeTO obj =(DepositFreezeTO)freezeTOs.get(rowNum);
        this.setTxtFreezeAmount(CommonUtil.convertObjToStr(obj.getAmount()));
        this.setTdtFreezeDate(DateUtil.getStringDate(obj.getFreezeDt()));
        this.setTxtFreezeRemark(obj.getRemarks());
        this.setCboFreezeType(obj.getType());
        this.setFreezeNo(obj.getFslNo());
        setStatusBy1(obj.getStatusBy());
//        setAuthorizeStatus1(obj.getAuthorizeStatus());
        this.setFreezeAuthorizeStatus(obj.getAuthorizeStatus());
        
        obj=null;
    }
    public void deleteFreezeData(int rowNum) {
        DepositFreezeTO obj = setTOStatus((DepositFreezeTO)freezeTOs.get(rowNum));
        if((obj.getFslNo().compareToIgnoreCase("-")!=0) || obj.getStatus()==CommonConstants.STATUS_UNFREEZE)
            deleteFreezeList.add(obj);
        
        freezeTOs.remove(rowNum);
        
        tbmFreeze.removeRow(rowNum);
        tbmFreeze.fireTableDataChanged();
        
        obj=null;
    }
    public DepositFreezeTO setTOStatus(DepositFreezeTO obj){
        obj.setStatus(this.getFreezeStatus());
        if(obj.getStatus().compareToIgnoreCase(CommonConstants.STATUS_UNFREEZE)==0){
            obj.setUnfreezeDt(curDate);
            obj.setUnfreezeRemarks(this.getUnFreezeRemark());
            obj.setAuthorizeDt(null);
            obj.setAuthorizeBy("");
            obj.setAuthorizeStatus("");
        }
        obj.setStatusBy(TrueTransactMain.USER_ID);
        obj.setStatusDt(curDate);        
        return obj;
    }
    public int insertFreezeData(int rowNo) {
        DepositFreezeTO obj=setFreezeTO();
        if(rowNo==-1){
            obj.setFslNo("-");
            obj=setTOStatus(obj);
            freezeTOs.add(obj);
            ArrayList irRow = this.setRow(obj);
            tbmFreeze.insertRow(tbmFreeze.getRowCount(), irRow);
        }else{
            obj=updateFreezeTO((DepositFreezeTO)freezeTOs.get(rowNo),obj);
            if(obj.getFslNo().compareToIgnoreCase("-")!=0)
                obj=setTOStatus(obj);
            ArrayList irRow = setRow(obj);
            freezeTOs.set(rowNo,obj);
            tbmFreeze.removeRow(rowNo);
            tbmFreeze.insertRow(rowNo,irRow);
        }
        tbmFreeze.fireTableDataChanged();
        obj=null;
        return 0;
    }
    private DepositFreezeTO updateFreezeTO(DepositFreezeTO oldTO,DepositFreezeTO newTO){
        oldTO.setAmount(newTO.getAmount());
        oldTO.setFreezeDt(newTO.getFreezeDt());
        oldTO.setRemarks(newTO.getRemarks());
        oldTO.setType(newTO.getType());
        oldTO.setDepositNo(newTO.getDepositNo());
        oldTO.setDepositSubNo(newTO.getDepositSubNo());       
        oldTO.setStatusBy(newTO.getStatusBy());
        oldTO.setAuthorizeStatus(newTO.getAuthorizeStatus());
        newTO = null;
        
        return oldTO;
    }
    private DepositFreezeTO setFreezeTO(){
        DepositFreezeTO obj = new DepositFreezeTO();
        
        obj.setAmount(CommonUtil.convertObjToDouble(this.getTxtFreezeAmount()));
        Date Dt = DateUtil.getDateMMDDYYYY(this.getTdtFreezeDate());
        if(Dt != null){
        Date dtDate = (Date)curDate.clone();
        dtDate.setDate(Dt.getDate());
        dtDate.setMonth(Dt.getMonth());
        dtDate.setYear(Dt.getYear());
//        obj.setFreezeDt(DateUtil.getDateMMDDYYYY(this.getTdtFreezeDate()));
        obj.setFreezeDt(dtDate);
        }else{
            obj.setFreezeDt(DateUtil.getDateMMDDYYYY(this.getTdtFreezeDate()));
        }
        obj.setRemarks(this.getTxtFreezeRemark());
        obj.setType(this.getCboFreezeType());
        obj.setDepositNo(this.getTxtDepositNo());
        obj.setDepositSubNo(CommonUtil.convertObjToInt(this.getCboSubDepositNo()));
        obj.setStatusBy(this.getStatusBy1());
//        obj.setAuthorizeStatus(this.getAuthorizeStatus1());
        return obj;
    }
    public void doAction(){
        try{
            HashMap objHashMap=null;
            if(authorizeMap==null) {
                freezeTOs.addAll(deleteFreezeList);
                if(freezeTOs.size()>0 && freezeTOs!=null){
                    objHashMap = new HashMap();
                    if ( getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_INSERT );
                    } else if ( getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_UPDATE );
                    } else if ( getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                        objHashMap.put("COMMAND",CommonConstants.TOSTATUS_DELETE );
                        setStatusForTOs();
                    }
                    objHashMap.put("freezeTOs",freezeTOs);
                    objHashMap.put("SHADOWFREEZE",CommonUtil.convertObjToDouble(this.getLblShadowFreeze()));
                    
                }
            }else{
                objHashMap=authorizeMap;
            }
            HashMap proxyResultMap = proxy.execute(objHashMap,map);
            setProxyReturnMap(proxyResultMap);
            setResult(getActionType());
//            objHashMap = null;            
        }catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    private void setStatusForTOs(){
        DepositFreezeTO objTO;
        this.setFreezeStatus(CommonConstants.STATUS_DELETED);
        int size = freezeTOs.size();
        for(int i=0;i<size;i++){
            objTO =(DepositFreezeTO)freezeTOs.get(i);
            objTO=this.setTOStatus(objTO);
            freezeTOs.set(i,objTO);
        }
        this.setLblShadowFreeze("0");
        objTO=null;
    }
    public void populateFreeze(String freezeNo){  
        DepositFreezeTO obj;
        int size = freezeTOs.size();
        for(int i=0;i<size;i++){
            obj = (DepositFreezeTO)freezeTOs.get(i);
            if(obj.getFslNo().equals(freezeNo)){                
                this.setFreezeNo(obj.getFslNo());
                this.populateFreeze(i);
                return;
            }                
        }
    }
    
    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                setTxtDepositNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                cbmProductId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
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
    
     public boolean authorizeStatus(){
         try{
            String authorizeStatus = (String)authorizeMap.get("SELECTED_AUTHORIZE_STATUS");            
            if(authorizeStatus==null || authorizeStatus.length()==0)
                authorizeStatus = this.getAuthorizeStatus();                            
            
            String status;
            double amount=0,shadowFreezeAmt=0,bal=0;
            status=(String)authorizeMap.get("STATUS");      
            
            authorizeMap.put("COMMAND_STATUS",status);
            
            authorizeMap.put("AUTHORIZE_STATUS", authorizeStatus);                        
            authorizeMap.put("USER_ID",TrueTransactMain.USER_ID); 
            authorizeMap.put ("AUTHORIZE_DATE",curDate);                            
            amount=CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue();                
            if((status.compareToIgnoreCase(CommonConstants.STATUS_DELETED)!=0) && (authorizeStatus.compareToIgnoreCase(CommonConstants.STATUS_AUTHORIZED)==0)){                               
                bal = CommonUtil.convertObjToDouble(authorizeMap.get("BALANCE")).doubleValue();                
                if(status.equalsIgnoreCase(CommonConstants.STATUS_UNFREEZE))
                    amount=-amount;               
                if(!status.equalsIgnoreCase(CommonConstants.STATUS_UNFREEZE)){  
                    if(bal<amount){                       
                        return false;
                    }
                    shadowFreezeAmt=amount;
                }                                        
            }          
            if((status.compareToIgnoreCase(CommonConstants.STATUS_DELETED)==0) && (authorizeStatus.compareToIgnoreCase(CommonConstants.STATUS_AUTHORIZED)==0)){   
                amount=0;
            }
            if(authorizeStatus.compareToIgnoreCase(CommonConstants.STATUS_REJECTED)==0){                                                                
                if(status.equalsIgnoreCase(CommonConstants.STATUS_UNFREEZE)){
                    status=CommonConstants.STATUS_MODIFIED;
                    authorizeMap.put("AUTHORIZE_STATUS",CommonConstants.STATUS_AUTHORIZED);                       
                }else if(status.equalsIgnoreCase(CommonConstants.STATUS_DELETED)){
                    status=CommonConstants.STATUS_MODIFIED;
                    authorizeMap.put("AUTHORIZE_STATUS","");   
                    authorizeMap.put ("USER_ID","");                
                    authorizeMap.put ("AUTHORIZE_DATE",null);  
                    shadowFreezeAmt=-amount;
                }else {
                    shadowFreezeAmt=amount;
                }                 
                amount=0;
            }
            authorizeMap.put("STATUS",status);
            authorizeMap.put("FREEZEAMOUNT",new Double(amount));
            authorizeMap.put("SHADOWFREEZE",new Double(shadowFreezeAmt));       
            
            authorizeMap.put("ACTION",authorizeStatus);
            authorizeMap.put("COMMAND", CommonConstants.AUTHORIZEDATA);           
            
            this.doAction();
            authorizeMap=null;              
        }catch(Exception e){
            this.setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
        }        
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
    public HashMap getDepositActIfno(){
        HashMap whereMap= new HashMap();
        whereMap.put("DEPOSITNUM", this.getTxtDepositNo());
        List list = ClientUtil.executeQuery("getDepositActInfo",whereMap);
        if(list!=null && list.size()>0){
            whereMap = (HashMap)list.get(0);
            return whereMap;                      
        }
        return null;        
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
    
    /** Getter for property unFreezeRemark.
     * @return Value of property unFreezeRemark.
     *
     */
    public java.lang.String getUnFreezeRemark() {
        return unFreezeRemark;
    }
    
    /** Setter for property unFreezeRemark.
     * @param unFreezeRemark New value of property unFreezeRemark.
     *
     */
    public void setUnFreezeRemark(java.lang.String unFreezeRemark) {
        this.unFreezeRemark = unFreezeRemark;
    }
    
    /** Getter for property lblShadowFreeze.
     * @return Value of property lblShadowFreeze.
     *
     */
    public java.lang.String getLblShadowFreeze() {
        return lblShadowFreeze;
    }
    
    /** Setter for property lblShadowFreeze.
     * @param lblShadowFreeze New value of property lblShadowFreeze.
     *
     */
    public void setLblShadowFreeze(java.lang.String lblShadowFreeze) {
        this.lblShadowFreeze = lblShadowFreeze;
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
    
    /** Getter for property freezeAuthorizeStatus.
     * @return Value of property freezeAuthorizeStatus.
     *
     */
    public java.lang.String getFreezeAuthorizeStatus() {
        return freezeAuthorizeStatus;
    }
    
    /** Setter for property freezeAuthorizeStatus.
     * @param freezeAuthorizeStatus New value of property freezeAuthorizeStatus.
     *
     */
    public void setFreezeAuthorizeStatus(java.lang.String freezeAuthorizeStatus) {
        this.freezeAuthorizeStatus = freezeAuthorizeStatus;
    }
    public void resetTabel(){
         this.tbmFreeze.setData(new ArrayList());
         this.tbmFreeze.fireTableDataChanged();            
         this.freezeTOs.clear();
   }
    
    /**
     * Getter for property lblDepositFreezeNoDesc.
     * @return Value of property lblDepositFreezeNoDesc.
     */
    public java.lang.String getLblDepositFreezeNoDesc() {
        return lblDepositFreezeNoDesc;
    }    
   
    /**
     * Setter for property lblDepositFreezeNoDesc.
     * @param lblDepositFreezeNoDesc New value of property lblDepositFreezeNoDesc.
     */
    public void setLblDepositFreezeNoDesc(java.lang.String lblDepositFreezeNoDesc) {
        this.lblDepositFreezeNoDesc = lblDepositFreezeNoDesc;
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
    
    /**
     * Getter for property AuthorizeStatus1.
     * @return Value of property AuthorizeStatus1.
     */
    public java.lang.String getAuthorizeStatus1() {
        return AuthorizeStatus1;
    }
    
    /**
     * Setter for property AuthorizeStatus1.
     * @param AuthorizeStatus1 New value of property AuthorizeStatus1.
     */
    public void setAuthorizeStatus1(java.lang.String AuthorizeStatus1) {
        this.AuthorizeStatus1 = AuthorizeStatus1;
    }
    
}
