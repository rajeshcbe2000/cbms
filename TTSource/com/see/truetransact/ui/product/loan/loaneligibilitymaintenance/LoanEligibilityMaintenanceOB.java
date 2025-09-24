/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FreezeOB.java
 *
 * Created on August 13, 2003, 4:32 PM
 */

package com.see.truetransact.ui.product.loan.loaneligibilitymaintenance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.operativeaccount.FreezeTO;
import com.see.truetransact.transferobject.product.loan.loaneligibilitymaintenance.LoanEligibilityTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CObservable;

import org.apache.log4j.Logger;

/**
 *
 * @author  Administrator
 * modified by Karthik
 *public class FreezeOB extends java.util.CObservable
 */
public class LoanEligibilityMaintenanceOB extends CObservable {
    
    private String cboCropType ="";
    private ComboBoxModel cbmCropType=null;
    private String txtEligibileAmount ="";
    private String tdtFromDate="";
    private String tdtToDate="";
    private EnhancedTableModel tblEligibilityList; 
       

    
    
    private String txtAmount = "";
    private String tdtFreezeDate = "";
    private String cboType = "";
    /*private boolean rdoCreditDebit_Credit = false;
    private boolean rdoCreditDebit_Debit = false;*/
    private String txtRemarks = "";
    private String cboProductID = "";
    private String txtAccountNumber = "";
    private String lblFreezeId = "";
    private String lblAuth = "";
    private String lblFreezeStatus = "";
    private String lblAuthorizeStatus = "";
    private String REMARKS;
    private String TYPE;
    private String DATE;
    
    private EnhancedTableModel tblFreezeListModel;
    private ComboBoxModel cbmProductID;
    private ComboBoxModel cbmType;
    private String accountHeadDesc;
    private String accountHeadCode;
    private int actionType;
    private String customerName;
    private String clearBalance;
    private String clearBalance1;
    private String freezeSum;
    private String LienSum;
    private int result;
    private String advances="";
    private HashMap _authorizeMap;
    private int row;
    
    private ArrayList cropList = new ArrayList();
    private ArrayList freezeDeleteList = new ArrayList();
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    
    private final static Logger log = Logger.getLogger(LoanEligibilityMaintenanceOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private  ArrayList freezeListColumn = new ArrayList();
    //    FreezeRB freezeRB = new FreezeRB();
    java.util.ResourceBundle freezeRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.LoanEligibilityMaintenanceRB", ProxyParameters.LANGUAGE);
    
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    //To store the Freeze Details
    private HashMap freezeMap;
    private LoanEligibilityTO ObjLoanEligibilityTO;
    
    private final String COMP_FREEZE = "COMPLETE";
    Date curDate = null;
    private int slno=-1;
    
    /*static {
        try {
            log.info("Creating EJB Client Proxy...");
            me = new FreezeOB();
        } catch(Exception e) {
            log.error(e);
        }
    }
     
    static public FreezeOB getInstance() {
        return me;
    }*/
    
    /** Creates a new instance of FreezeOB */
    public LoanEligibilityMaintenanceOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
           
            final LoanEligibilityMaintenanceRB loanEligibilityMaintenanceRB = new LoanEligibilityMaintenanceRB();
            
            //            freezeListColumn.add(freezeRB.getString("tblColumn1"));
            freezeListColumn.add(loanEligibilityMaintenanceRB.getString("tblColumn2"));
            freezeListColumn.add(loanEligibilityMaintenanceRB.getString("tblColumn3"));
            freezeListColumn.add(loanEligibilityMaintenanceRB.getString("tblColumn4"));
            freezeListColumn.add(loanEligibilityMaintenanceRB.getString("tblColumn5"));
            freezeListColumn.add(loanEligibilityMaintenanceRB.getString("tblColumn7"));
//            freezeListColumn.add(loanEligibilityMaintenanceRB.getString("tblColumn8"));
         
            tblEligibilityList = new EnhancedTableModel(null, freezeListColumn);
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "LoanEligibilityJNDI");
            map.put(CommonConstants.HOME, "product.loan.loaneligibilitymaintenance.LoanEligibilityMaintenanceBean");
            map.put(CommonConstants.REMOTE, "product.loan.loaneligibilitymaintenance.LoanEligibilityMaintenance");
            
            fillDropdown();
            cropList = new ArrayList();
            freezeDeleteList = new ArrayList();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
    }

    
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        param = new java.util.HashMap();
         HashMap lookupValues =new HashMap();
        param.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookupKeys = new ArrayList(1);
        lookupKeys.add("CROP_TYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKeys);
        lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("CROP_TYPE"));
        cbmCropType = new ComboBoxModel(key,value);
        lookupValues = null;
    }
    
    /** To set the key & value for comboboxes */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    /** To get data for comboboxes */
    private HashMap populateData(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public void resetVariables(){
        cropList = null;
        ObjLoanEligibilityTO = null;
    }
    
    public void resetFreezeListTable(){
        for(int i = tblEligibilityList.getRowCount(); i > 0; i--){
            tblEligibilityList.removeRow(0);
        }
    }
    
    public void resetProductPanel(){
      
        cbmCropType.setKeyForSelected("");
        setTxtEligibileAmount("");
        setTdtFromDate("");
        setTdtToDate("");
    }
    
    public void resetAccountDetails(){
        this.getCbmCropType().setKeyForSelected("");
        this.setTdtFromDate("");
        this.setTdtToDate("");
        this.setTxtEligibileAmount("");
    }
    /** set the value of Account head ID and description based on the product selected
     * in the UI
     */
    public void getAccountHeadForProduct() {
        
        /* based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen
         * same LookUp bean will be used for this purpose
         */
        param.put(CommonConstants.MAP_NAME,"getAccHead");
        //        param.put(CommonConstants.PARAMFORQUERY, getCboProductID());
        param.put(CommonConstants.PARAMFORQUERY,CommonUtil.convertObjToStr(cbmProductID.getKeyForSelected()));
        try {
            final HashMap lookupValues = populateData(param);
            fillData((HashMap)lookupValues.get("DATA"));
            /*key = (ArrayList)keyValue.get("KEY");
            value = (ArrayList)keyValue.get("VALUE");*/
            //If the returned ArrayList has got proper value, then set the variables
            if( value.size() > 1 ){
//                setAccountHeadCode(CommonUtil.convertObjToStr(value.get(1)));
//                setAccountHeadDesc(CommonUtil.convertObjToStr(key.get(1)));
                
            }
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** based on the account number, customer details will be fetched from database and
     * displayed on screen ;same LookUp bean will be used for this purpose
     */
    public void getCustomerDetails() {
        
        final HashMap whereMap = new HashMap();
//        whereMap.put("ACCTNUMBER", getTxtAccountNumber());
        List resultList=null;
        try {
            if(advances.equals("ADVANCES"))
                resultList = ClientUtil.executeQuery("getFreezeCustomerDetailsOD", whereMap);
            else
                resultList = ClientUtil.executeQuery("getFreezeCustomerDetails", whereMap);
            
            final HashMap resultMap = (HashMap)resultList.get(0);
            if(advances.equals("ADVANCES"))
                if(resultMap !=null && resultMap.containsKey("CLEAR_BALANCE"))
                    if(CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE")).doubleValue()<=0){
                        ClientUtil.showMessageWindow("Cant Freeze amt  :"+CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE")).doubleValue());
//                        setTxtAccountNumber("");
                        ttNotifyObservers();
                        return;
                    }
            
            setPartialCustomerInfo(resultMap);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
    }
    
    /** based on the selection from the product combo box, Product Details will be
     * fetched from database and displayed on screen;same LookUp bean will be used for this purpose
     */
    public void getProductDetails(HashMap whereMap) {
        
        try {
            List resultList=null;
//            if(whereMap.containsKey("ADVANCES") && whereMap.get("ADVANCES").equals("ADVANCES"))
//                 resultList = ClientUtil.executeQuery("getFreezeProductDetailsAD", whereMap);
//            else
                 resultList = ClientUtil.executeQuery("getFreezeProductDetails", whereMap);
            
            final HashMap resultMap = (HashMap)resultList.get(0);
//            setPartialCustomerInfo(resultMap);
//            setCboProductID((String) getCbmProductID().getDataForKey(CommonUtil.convertObjToStr(resultMap.get("PROD_ID"))));
//            getCbmProductID().setKeyForSelected(CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
//            setAccountHeadCode(CommonUtil.convertObjToStr(resultMap.get("AC_HD_DESC")));
//            setAccountHeadDesc(CommonUtil.convertObjToStr(resultMap.get("AC_HD_ID")));
//            setTxtAccountNumber(CommonUtil.convertObjToStr(whereMap.get("ACCTNUMBER")));
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To set the mentioned display fileds */
    private void setPartialCustomerInfo(HashMap resultMap) throws Exception{
//        
//        System.out.println("CustomerInfo: " + resultMap);
//        
//        if( resultMap.get("FREEZE_SUM") != null){
////            setFreezeSum(CommonUtil.convertObjToStr(resultMap.get("FREEZE_SUM")));
//        }
//         if( resultMap.get("LIEN_SUM") != null){
////            setLienSum(CommonUtil.convertObjToStr(resultMap.get("LIEN_SUM")));
//        }
//        setCustomerName(CommonUtil.convertObjToStr(resultMap.get("Customer Name")));
//        setClearBalance(CommonUtil.convertObjToStr(resultMap.get("AVAILABLE_BALANCE")));
//        setClearBalance1(CommonUtil.convertObjToStr(resultMap.get("CLEAR_BALANCE")));
    }
    
    public boolean isNewAllowed(){
        boolean returnType = false;
        ArrayList data = tblEligibilityList.getDataArrayList();
        int size = data.size();
        if(size <= 0){
            returnType = false;
        }else{
            for(int i = 0; i< size; i++){
                System.out.println("Freeze Type in i Row: " + CommonUtil.convertObjToStr(tblEligibilityList.getValueAt(i,0)));
                if(CommonUtil.convertObjToStr(tblEligibilityList.getValueAt(i,0)).equalsIgnoreCase(COMP_FREEZE) ){
                    returnType = true;
                }
            }
        }
        return returnType;
    }
    
    
    public int addFreeze(int updateTab){
        int option = -1;
        try{
/*
 * To Decide which process out of Insert And Update is to be performed
 * depending on the Choice, set the Status of the operation...
 */
            ObjLoanEligibilityTO = new LoanEligibilityTO();
            //
            ObjLoanEligibilityTO.setCropType(CommonUtil.convertObjToStr(getCbmCropType().getKeyForSelected()));
            ObjLoanEligibilityTO.setEligibleAmt(CommonUtil.convertObjToDouble(txtEligibileAmount));
            ObjLoanEligibilityTO.setFromDate(getProperDateFormat(getTdtFromDate()));
            ObjLoanEligibilityTO.setToDate(getProperDateFormat(getTdtToDate()));
            //            ObjLoanEligibilityTO.setStatus(CommonConstants. );
            //
            //            /*
            //             * To Check each row for the Duplication..
            //             */
            ArrayList data = tblEligibilityList.getDataArrayList();
            int dataSize = data.size();
            boolean exist = false;
            ArrayList freezeData = new ArrayList();
            
            ArrayList singleRow = new ArrayList();
            updateToDate(updateTab);
//            singleRow.add((String)cbmCropType.getKeyForSelected());
            System.out.println("(String)cbmCropType.getKeyForSelected(): "+ (String)cbmCropType.getKeyForSelected());
            singleRow.add(CommonUtil.convertObjToStr(getCbmCropType().getKeyForSelected()));
            singleRow.add(CommonUtil.convertObjToDouble(txtEligibileAmount));
            singleRow.add(getTdtFromDate());
            singleRow.add(getTdtToDate());
            
            FreezeTO ObjLoanEligibilityTOchk = null;
             int size =tblEligibilityList.getDataArrayList().size();
            
            if(updateTab == -1){
                System.out.println("data inserted...");
                //                            cropList.remove(i);
                if(row!=0){
                    size=row;
                    row++;
                }
              
//                int size =tblEligibilityList.getDataArrayList().size();
                singleRow.add(size+1);
                ObjLoanEligibilityTO.setSlno(String.valueOf(size+1));
                cropList.add(size,ObjLoanEligibilityTO);
                //                            tblFreezeListModel.removeRow(i);
                tblEligibilityList.insertRow(size,singleRow);
                option = 1;
                //                            break;
            }else if (!exist && updateTab != -1){
                ArrayList totList=(ArrayList)tblEligibilityList.getDataArrayList();
                ArrayList selectList=(ArrayList)tblEligibilityList.getDataArrayList().get(updateTab);
                
                singleRow.add(selectList.get(4));
                totList.set(updateTab,singleRow);
               
//                if(slno==-1)
//                 singleRow.add(size+1);
//                else
//                    singleRow.add(slno+1);
                 ObjLoanEligibilityTO.setSlno(String.valueOf(selectList.get(4)));
                cropList.set(CommonUtil.convertObjToInt(selectList.get(4)), ObjLoanEligibilityTO);
            }
            
            
            
            
            
            setChanged();
            singleRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
      return option;
    }
    
    public void updateToDate(int updateTab ){
        ArrayList singleList =new ArrayList();
        ArrayList totList =new ArrayList();
        totList =(ArrayList)tblEligibilityList.getDataArrayList();
        if(updateTab ==-1){
            if(!tblEligibilityList.getDataArrayList().isEmpty()){
                singleList =(ArrayList)tblEligibilityList.getDataArrayList().get(totList.size()-1);
                singleList.set(3,CommonUtil.convertObjToStr(DateUtil.addDays(DateUtil.getDateMMDDYYYY(getTdtFromDate()),-1)));
                totList.set(totList.size()-1,singleList);
            }
//            cropList.add(size,ObjLoanEligibilityTO);
            
        }else{
            singleList =(ArrayList)tblEligibilityList.getDataArrayList().get(updateTab);
            LoanEligibilityTO ObjLoanEligibilityTO  =(LoanEligibilityTO)cropList.get(updateTab);
            ObjLoanEligibilityTO.setToDate(getProperDateFormat(getTdtToDate()));
            singleList.set(3,CommonUtil.convertObjToStr(getTdtToDate()));
            totList.set(updateTab,singleList);
            cropList.add(updateTab,ObjLoanEligibilityTO);
        }
        tblEligibilityList.setDataArrayList(totList,freezeListColumn);
    }
    
     public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt=(Date)curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    
     /*
      * To Update the Row in the Table...
      */
    private void updateFreezeTab(int row, FreezeTO ObjLoanEligibilityTO){
        cropList.remove(row);
        cropList.add(row,ObjLoanEligibilityTO);
        tblFreezeListModel.setValueAt(txtAmount, row, 2);
    }
    
     /*
      * To delete the Row(s) in the Interest Table...
      */
    public void deleteFreezeTab(int selectedRow){
        log.info("In deleteInterestTab...");
        
        
        if((getActionType() == ClientConstants.ACTIONTYPE_EDIT)){
            LoanEligibilityTO ObjLoanEligibilityTO =   (LoanEligibilityTO)cropList.get(selectedRow);
            freezeDeleteList.add(ObjLoanEligibilityTO);
        }
        cropList.remove(selectedRow);
        tblEligibilityList.removeRow(selectedRow);
        
        //        try{
        //            ArrayList data = tblFreezeListModel.getDataArrayList();
        //            final int dataSize = data.size();
        //            for (int i=0;i<dataSize;i++){
        //                ArrayList freezeDelete = new ArrayList();
        //
        //                freezeDelete = (ArrayList)data.get(i);
        //                DATE = CommonUtil.convertObjToStr(freezeDelete.get(3));
        //                TYPE = CommonUtil.convertObjToStr(freezeDelete.get(0));
        //
        //                if (DATE.equals(tdtFreezeDate) && TYPE.equals((String)cbmType.getKeyForSelected())){
        //                    final String FREEZEID =  CommonUtil.convertObjToStr(((FreezeTO)freezeList.get(i)).getFreezeId());
        //                    if((getActionType() == ClientConstants.ACTIONTYPE_EDIT) && (FREEZEID.length() > 0) ){
        //                        ObjLoanEligibilityTO =   (FreezeTO)freezeList.get(i);
        ////                        ObjLoanEligibilityTO.setStatusBy(TrueTransactMain.USER_ID);
        ////                        ObjLoanEligibilityTO.setStatusDt(curDate);
        //                        freezeDeleteList.add(ObjLoanEligibilityTO);
        //                    }
        //                    freezeList.remove(i);
        //                    tblFreezeListModel.removeRow(i);
        //                    break;
        //                }
        //            }
        //        }catch(Exception e){
        //            log.info("The error in deleteChargesTab()");
        //            parseException.logException(e,true);
        //            //e.printStackTrace();
        //        }
    }
    
    
    /** To set the status of UnLien Lien object in the lienMap */
    public void unFreeze(String unfreezeRemarks, int selectedRow){
        FreezeTO ObjLoanEligibilityTO = (FreezeTO)cropList.get(selectedRow);
        if( ObjLoanEligibilityTO != null ){
            cropList.remove(selectedRow);
            
            ObjLoanEligibilityTO.setFreezeStatus(CommonConstants.STATUS_UNFREEZE);
            ObjLoanEligibilityTO.setUnfreezeRemarks(unfreezeRemarks);
            ObjLoanEligibilityTO.setUnfreezeDt(curDate);
            
            ObjLoanEligibilityTO.setStatusBy(ProxyParameters.USER_ID);
            ObjLoanEligibilityTO.setStatusDt(curDate);
            
            ObjLoanEligibilityTO.setAuthorizeStatus(null);
            cropList.add(selectedRow,ObjLoanEligibilityTO);
        }
        
        //
        //        try{
        //            ArrayList data = tblFreezeListModel.getDataArrayList();
        //            final int dataSize = data.size();
        //            for (int i=0;i<dataSize;i++){
        //                ArrayList freezeDelete = new ArrayList();
        //
        //                freezeDelete = (ArrayList)data.get(i);
        //                DATE = CommonUtil.convertObjToStr(freezeDelete.get(3));
        //                TYPE = CommonUtil.convertObjToStr(freezeDelete.get(0));
        //
        //                if (DATE.equals(tdtFreezeDate) && TYPE.equals((String)cbmType.getKeyForSelected())){
        //                    ObjLoanEligibilityTO = (FreezeTO)freezeList.get(i);
        //                    if( ObjLoanEligibilityTO != null ){
        //                        freezeList.remove(i);
        //
        //                        ObjLoanEligibilityTO.setFreezeStatus(CommonConstants.STATUS_UNFREEZE);
        //                        ObjLoanEligibilityTO.setUnfreezeRemarks(unfreezeRemarks);
        //                        ObjLoanEligibilityTO.setUnfreezeDt(curDate);
        //
        //                        ObjLoanEligibilityTO.setStatusBy(TrueTransactMain.USER_ID);
        //                        ObjLoanEligibilityTO.setStatusDt(curDate);
        //
        //                        ObjLoanEligibilityTO.setAuthorizeStatus(null);
        //                        freezeList.add(i,ObjLoanEligibilityTO);
        //                        break;
        //                    }
        //                }
        //            }
        //        }catch(Exception e){
        //            log.info("The error in deleteChargesTab()");
        //            parseException.logException(e,true);
        //            //e.printStackTrace();
        //        }
    }
    
    
    /** To get the data for a specific Lien details */
    public void getData(HashMap whereMap) {
        try{
            ArrayList freezeDataList = new ArrayList();
            whereMap.put(CommonConstants.STATUS, new Integer(getActionType()));
            System.out.println("#### map before executeQuery : "+whereMap);
            freezeMap = (HashMap)proxy.executeQuery(whereMap,map);
            if(cropList==null)
                cropList=new ArrayList();
            freezeDataList = (ArrayList) freezeMap.get("LoanEligibilityTO");
            int length = freezeDataList.size();
            
            for(int i=0; i<length; i++){
                LoanEligibilityTO ObjLoanEligibilityTO = new LoanEligibilityTO();
                ArrayList freezeTabRow = new ArrayList();
                ObjLoanEligibilityTO = (LoanEligibilityTO)freezeDataList.get(i);
                
                cropList.add(ObjLoanEligibilityTO);
                
                freezeTabRow.add(CommonUtil.convertObjToStr(ObjLoanEligibilityTO.getCropType()));
                freezeTabRow.add(CommonUtil.convertObjToStr(ObjLoanEligibilityTO.getEligibleAmt()));
                freezeTabRow.add(CommonUtil.convertObjToStr(ObjLoanEligibilityTO.getFromDate()));
                freezeTabRow.add(DateUtil.getStringDate(ObjLoanEligibilityTO.getToDate()));
                freezeTabRow.add(CommonUtil.convertObjToStr(ObjLoanEligibilityTO.getSlno()));
                row = CommonUtil.convertObjToInt(ObjLoanEligibilityTO.getSlno());
                tblEligibilityList.addRow(freezeTabRow);
            }
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** While selecting a row from the table, the corresponding Freeze detail will be
     * populated in the fields
     */
    public void populateFreezeDetails(int row){
        try{
            ObjLoanEligibilityTO = (LoanEligibilityTO)cropList.get(row);
            populateFreezeData(ObjLoanEligibilityTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To set the corresponding Freeze observable instance variables */
    private void populateFreezeData(LoanEligibilityTO ObjLoanEligibilityTO) throws Exception{
//        this.setLblFreezeId(CommonUtil.convertObjToStr(ObjLoanEligibilityTO.getFreezeId()));
        this.getCbmCropType().setKeyForSelected(CommonUtil.convertObjToStr(ObjLoanEligibilityTO.getCropType()));
        this.setTxtEligibileAmount(CommonUtil.convertObjToStr(ObjLoanEligibilityTO.getEligibleAmt()));
        this.setTdtFromDate(DateUtil.getStringDate(ObjLoanEligibilityTO.getFromDate()));
        this.setTdtToDate(DateUtil.getStringDate(ObjLoanEligibilityTO.getToDate()));
    }
    
    /**
     * To fill the Data for the record, Selected for Authorization.
     */
    public void  setAuthRowData(String freezeId)throws Exception{
        ArrayList freezeDataList = new ArrayList();
        freezeDataList = tblFreezeListModel.getDataArrayList();
        int length = freezeDataList.size();
        for(int i =0; i< length; i++){
            if( ((String)tblFreezeListModel.getValueAt(i, 1)).equalsIgnoreCase(freezeId)){
                populateFreezeData((LoanEligibilityTO)cropList.get(i));
                setAuthRow(i);
                break;
            }
        }
        ttNotifyObservers();
    }
    
    public void resetFreezeDetails(){
//        this.setLblFreezeId("");
//        this.setTxtAmount("");
//        this.setTdtFreezeDate("");
//        this.setCboType("");
//        this.setTxtRemarks("");
//        this.setLblAuth("");
//        this.setLblFreezeStatus("");
    }
    
    
    /** To perform necessary action */
    public boolean doAction() {
        boolean result = true;
        try {
            //If actionType such as NEW, EDIT, DELETE , then proceed
            if( getActionType() != ClientConstants.ACTIONTYPE_CANCEL || getAuthorizeMap() != null){
                doActionPerform();
                resetForm();
            }
        }catch (Exception e) {
            if( e instanceof TTException ){
                TTException tte = (TTException)e;
                System.out.println("TTException Thrown...");
                
            }
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            result = false;
            parseException.logException(e,true);
        }
        return result;
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        /* If there is no Authorrization map available, proceed with the normal operations
         * to be performed; else, do the Authorization...
         */
        if(getAuthorizeMap() == null){
            data.put("LoanEligibilityTO",cropList);
            data.put("COMMAND",getCommand());
            /* To Maintain a List which Containd
             * the Data for the records deleted at the time of Edit
             */
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                data.put("LoanEligibilityTODelete",freezeDeleteList);
            }
        }
        if(advances.equals("ADVANCES"))
            data.put("ADVANCES", "ADVANCES");
        data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
        HashMap proxyResultMap = proxy.execute(data,map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
        setResult(actionType);
    }
    
    private String getCommand() throws Exception{
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
    
    public void resetObjects(){
        cropList = null;
        freezeDeleteList = null;
        cropList = new ArrayList();
        freezeDeleteList = new ArrayList();
    }
    
    
    public void resetForm(){
        resetVariables();
        //        resetFreezeDetails();
        resetFreezeListTable();
        resetProductPanel();
           ttNotifyObservers();
    }
    
    /** Called by CellRenderer UI to find whether a freeze data is deleted or not */
    public boolean isDeleted(int row){
        //        final Object selectedRow = ((ArrayList)tblFreezeListModel.getDataArrayList().get(row)).get(0);
        //        final FreezeTO delFreezeTO = (FreezeTO)freezeMap.get(new Integer(selectedRow.toString()));
        //        if( delFreezeTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE) ) {
        //            return true;
        //        }
        return false;
    }
    
    public void resetData() {
        this.getCbmCropType().setKeyForSelected("");
        this.setTxtEligibileAmount("");
        this.setTdtFromDate("");
        this.setTdtToDate("");
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
    
    
    public String getLblFreezeId() {
        return this.lblFreezeId;
    }
    
    public void setLblFreezeId(String lblFreezeId) {
        this.lblFreezeId = lblFreezeId;
        setChanged();
    }
    
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }
    
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    public void setAuthRow(int row){
        this.row = row;
    }
    
    public int getAuthRow(){
        return this.row;
    }
    
    public String getLblAuth() {
        return this.lblAuth;
    }
    
    public void setLblAuth(String lblAuth) {
        this.lblAuth = lblAuth;
        setChanged();
    }
    
    public String getLblFreezeStatus() {
        return this.lblFreezeStatus;
    }
    
    public void setLblFreezeStatus(String lblFreezeStatus) {
        this.lblFreezeStatus = lblFreezeStatus;
        setChanged();
    }
    
    public void setNewObj(){
        cropList = new ArrayList();
        freezeDeleteList = new ArrayList();
    }
    
    public int rowCount(){
        int rowCount = 0;
        try{
            rowCount = tblEligibilityList.getRowCount();
        }catch(Exception e){
            e.printStackTrace();
        }
        return rowCount;
    }
    
    public boolean verifyAccountDate(HashMap dataMap){
        boolean val = false;
        try {
            System.out.println("dataMap : " +dataMap);
            final List resultList = ClientUtil.executeQuery("Freeze.getAccountOpeningDate", dataMap);
            if(resultList != null && resultList.size() > 0){
                final HashMap resultMap = (HashMap)resultList.get(0);
                System.out.println("resultMap for Acct Opening Date: " + resultMap);
                //__ If a valid Date...
                if(CommonUtil.convertObjToStr(resultMap.get("ACCT_NAME")).length() > 0){
                    val = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error in verifyAccountDate() ");
            parseException.logException(e,true);
        }
        
        return val;
    }
    
//    public boolean checkAcNoWithoutProdType(String actNum) {
//        HashMap mapData=new HashMap();
//        boolean isExists = false;
//        try{//dont delete chck selectalldao
//            mapData.put("ACT_NUM", actNum);
//            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
//            System.out.println("#### mapDataList :"+mapDataList);
//            if (mapDataList!=null && mapDataList.size()>0) {
//                mapData=(HashMap)mapDataList.get(0);
//                if(mapData.get("PROD_TYPE").equals("OA")){
//                    setTxtAccountNumber(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
//                    cbmProductID.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
//                    isExists = true;
//                }else{
//                    cbmProductID.setKeyForSelected("");
//                    isExists = false;
//                }
//            } else {
////                ArrayList key=new ArrayList();
////                ArrayList value=new ArrayList();
////                key.add("");
////                value.add("");   
////                setCbmProdId("");
//                isExists = false;
////                key = null;
////                value = null;
//                isExists = false;
//            }
//            mapDataList = null;
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        mapData = null;
//        return isExists;
//    }
    
    
    /**
     * Getter for property lblAuthorizeStatus.
     * @return Value of property lblAuthorizeStatus.
     */
    public java.lang.String getLblAuthorizeStatus() {
        return lblAuthorizeStatus;
    }
    
    /**
     * Setter for property lblAuthorizeStatus.
     * @param lblAuthorizeStatus New value of property lblAuthorizeStatus.
     */
    public void setLblAuthorizeStatus(java.lang.String lblAuthorizeStatus) {
        this.lblAuthorizeStatus = lblAuthorizeStatus;
    }
    
    public double FreezeTabSum(){
        double amt = 0;
        try{
            int rowCount = tblFreezeListModel.getRowCount();
            for(int i=0; i< rowCount ; i++){
                String FreezeNo = CommonUtil.convertObjToStr(tblFreezeListModel.getValueAt(i, 1));
                if (FreezeNo.length()>=0)
                    amt = amt + CommonUtil.convertObjToDouble(tblFreezeListModel.getValueAt(i, 2)).doubleValue();
            }
        }catch(Exception e){
            System.out.println("error in FreezeTabSum()");
            e.printStackTrace();
        }
        
        return amt;
    }
    
    /**
     * Getter for property advances.
     * @return Value of property advances.
     */
    public java.lang.String getAdvances() {
        return advances;
    }
    
    /**
     * Setter for property advances.
     * @param advances New value of property advances.
     */
    public void setAdvances(java.lang.String advances) {
        this.advances = advances;
    }
    
    /**
     * Getter for property clearBalance1.
     * @return Value of property clearBalance1.
     */
    public java.lang.String getClearBalance1() {
        return clearBalance1;
    }
    
    /**
     * Setter for property clearBalance1.
     * @param clearBalance1 New value of property clearBalance1.
     */
    public void setClearBalance1(java.lang.String clearBalance1) {
        this.clearBalance1 = clearBalance1;
    }
    
    /**
     * Getter for property LienSum.
     * @return Value of property LienSum.
     */
    public java.lang.String getLienSum() {
        return LienSum;
    }
    
    /**
     * Setter for property LienSum.
     * @param LienSum New value of property LienSum.
     */
    public void setLienSum(java.lang.String LienSum) {
        this.LienSum = LienSum;
    }
    
    /**
     * Getter for property cboCropType.
     * @return Value of property cboCropType.
     */
    public java.lang.String getCboCropType() {
        return cboCropType;
    }
    
    /**
     * Setter for property cboCropType.
     * @param cboCropType New value of property cboCropType.
     */
    public void setCboCropType(java.lang.String cboCropType) {
        this.cboCropType = cboCropType;
    }
    
    /**
     * Getter for property cbmCropType.
     * @return Value of property cbmCropType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCropType() {
        return cbmCropType;
    }
    
    /**
     * Setter for property cbmCropType.
     * @param cbmCropType New value of property cbmCropType.
     */
    public void setCbmCropType(com.see.truetransact.clientutil.ComboBoxModel cbmCropType) {
        this.cbmCropType = cbmCropType;
    }
    
    /**
     * Getter for property txtEligibileAmount.
     * @return Value of property txtEligibileAmount.
     */
    public java.lang.String getTxtEligibileAmount() {
        return txtEligibileAmount;
    }
    
    /**
     * Setter for property txtEligibileAmount.
     * @param txtEligibileAmount New value of property txtEligibileAmount.
     */
    public void setTxtEligibileAmount(java.lang.String txtEligibileAmount) {
        this.txtEligibileAmount = txtEligibileAmount;
        setChanged();
    }
    
    /**
     * Getter for property tdtFromDate.
     * @return Value of property tdtFromDate.
     */
    public java.lang.String getTdtFromDate() {
        return tdtFromDate;
    }
    
    /**
     * Setter for property tdtFromDate.
     * @param tdtFromDate New value of property tdtFromDate.
     */
    public void setTdtFromDate(java.lang.String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
         setChanged();
    }
    
    /**
     * Getter for property tdtToDate.
     * @return Value of property tdtToDate.
     */
    public java.lang.String getTdtToDate() {
        return tdtToDate;
    }
    
    /**
     * Setter for property tdtToDate.
     * @param tdtToDate New value of property tdtToDate.
     */
    public void setTdtToDate(java.lang.String tdtToDate) {
        this.tdtToDate = tdtToDate;
         setChanged();
    }
    
    /**
     * Getter for property tblEligibilityList.
     * @return Value of property tblEligibilityList.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblEligibilityList() {
        return tblEligibilityList;
    }
    
    /**
     * Setter for property tblEligibilityList.
     * @param tblEligibilityList New value of property tblEligibilityList.
     */
    public void setTblEligibilityList(com.see.truetransact.clientutil.EnhancedTableModel tblEligibilityList) {
        this.tblEligibilityList = tblEligibilityList;
    }
    
    /**
     * Getter for property freezeListColumn.
     * @return Value of property freezeListColumn.
     */
    public ArrayList getFreezeListColumn() {
        return freezeListColumn;
    }
    
    /**
     * Setter for property freezeListColumn.
     * @param freezeListColumn New value of property freezeListColumn.
     */
    public void setFreezeListColumn(ArrayList freezeListColumn) {
        this.freezeListColumn = freezeListColumn;
    }
    
    /**
     * Getter for property cropList.
     * @return Value of property cropList.
     */
    public ArrayList getCropList() {
        return cropList;
    }
    
    /**
     * Setter for property cropList.
     * @param cropList New value of property cropList.
     */
    public void setCropList(ArrayList cropList) {
        this.cropList = cropList;
    }
    
}
