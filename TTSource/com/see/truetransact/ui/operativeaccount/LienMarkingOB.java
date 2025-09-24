/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LienMarkingOB.java
 *
 * Created on August 13, 2003, 4:33 PM
 */

package com.see.truetransact.ui.operativeaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.operativeaccount.LienMarkingTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CObservable;
import java.util.Date;
import org.apache.log4j.Logger;
/**
 *
 * @author  Administrator
 * Modified by Karthik
 * Modified by Rahul
 */
public class LienMarkingOB extends CObservable {
    private String txtLienAmount = "";
    private String tdtLienDate = "";
    private String txtRemarks = "";
    private String txtLienAccountNumber = "";
    private String cboProductID = "";
    private String txtAccountNumber = "";
    
    private String lblLienId = "";
    private String cboLienProduct = "";
    private String lblLienCustName = "";
    private String lblLienAccountHeadDesc = "";
    private String lblAuth = "";
    private String lblLienStatus = "";
    private String lblAuthorizeStatus = "";
    private String LIENACCOUNT;
    private String DATE;
    
    private ComboBoxModel cbmProductID;
    private ComboBoxModel cbmLienProduct;
    
    private EnhancedTableModel tblLienListModel;
    private String accountHeadDesc;
    private String accountHeadCode;
    private int actionType;
    private String customerName;
    private String clearBalance;
    private String clearBalance1;
    private String lienSum;
    private String FreezeSum;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static Logger log = Logger.getLogger(LienMarkingOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private HashMap map;
    private ProxyFactory proxy;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    private HashMap _authorizeMap;
    public boolean OAFlag = false;
    public boolean LOANFlag = false;
//    LienMarkingRB lienMarkingRB = new LienMarkingRB();
    java.util.ResourceBundle lienMarkingRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.LienMarkingRB", ProxyParameters.LANGUAGE);
    
    /*
     * To Store the data for the Lien Objects...
     */
    private ArrayList lienList = new ArrayList();
    private ArrayList lienDeleteList = new ArrayList();
    
    //To store the Lien Details
    private HashMap lienMap;
    private LienMarkingTO objLienTO;
    Date curDate = null;
    
    int row;
    
    /** Creates a new instance of LienMarkingOB */
    public LienMarkingOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
            ArrayList lienListColumn = new ArrayList();
            LienMarkingRB lienMarkingRB = new LienMarkingRB();
            //            lienListColumn.add(lienMarkingRB.getString("tblColumn1"));
            lienListColumn.add(lienMarkingRB.getString("tblColumn2"));
            lienListColumn.add(lienMarkingRB.getString("tblColumn3"));
            lienListColumn.add(lienMarkingRB.getString("tblColumn4"));
            lienListColumn.add(lienMarkingRB.getString("tblColumn5"));
            lienListColumn.add(lienMarkingRB.getString("tblColumn6"));
            lienListColumn.add(lienMarkingRB.getString("tblColumn7"));
            lienMarkingRB = null;
            tblLienListModel = new EnhancedTableModel(null, lienListColumn);
            lienListColumn = null;
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "LienMarkingJNDI");
            map.put(CommonConstants.HOME, "operativeaccount.LienMarkingHome");
            map.put(CommonConstants.REMOTE, "operativeaccount.LienMarking");
            fillDropdown();
            lienList = new ArrayList();
            lienDeleteList = new ArrayList();
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
        
        param.put(CommonConstants.MAP_NAME,"getAccProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("DATA"));
        cbmProductID = new ComboBoxModel(key,value);
        
        param.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("LIENMARKING.LIENTYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        
        lookupValues = populateData(param);
        
        lookupMap.put(CommonConstants.MAP_NAME,"Lein.getLienProd");
        lookupMap.put(CommonConstants.PARAMFORQUERY, null);
        keyValue = ClientUtil.populateLookupData(lookupMap);
        fillData((HashMap)keyValue.get(CommonConstants.DATA));
        cbmLienProduct = new ComboBoxModel(key,value);
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
    
    void setTxtLienAmount(String txtLienAmount){
        this.txtLienAmount = txtLienAmount;
        setChanged();
    }
    String getTxtLienAmount(){
        return this.txtLienAmount;
    }
    
    void setTdtLienDate(String tdtLienDate){
        this.tdtLienDate = tdtLienDate;
        setChanged();
    }
    String getTdtLienDate(){
        return this.tdtLienDate;
    }
    
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    String getTxtRemarks(){
        return this.txtRemarks;
    }
    
    void setLblLienAccountHeadDesc(String lblLienAccountHeadDesc){
        this.lblLienAccountHeadDesc = lblLienAccountHeadDesc;
        setChanged();
    }
    String getLblLienAccountHeadDesc(){
        return this.lblLienAccountHeadDesc;
    }
    
    void setTxtLienAccountNumber(String txtLienAccountNumber){
        this.txtLienAccountNumber = txtLienAccountNumber;
        setChanged();
    }
    String getTxtLienAccountNumber(){
        return this.txtLienAccountNumber;
    }
    
    void setCboProductID(String cboProductID){
        this.cboProductID = cboProductID;
        setChanged();
    }
    String getCboProductID(){
        return this.cboProductID;
    }
    
    void setTxtAccountNumber(String txtAccountNumber){
        this.txtAccountNumber = txtAccountNumber;
        setChanged();
    }
    String getTxtAccountNumber(){
        return this.txtAccountNumber;
    }
    
    void setTblLienListModel(EnhancedTableModel tblLienListModel){
        this.tblLienListModel = tblLienListModel;
        setChanged();
    }
    EnhancedTableModel getTblLienListModel(){
        return this.tblLienListModel;
    }
    
    void setCbmProductID(ComboBoxModel cbmProductID){
        this.cbmProductID = cbmProductID;
        setChanged();
    }
    ComboBoxModel getCbmProductID(){
        return this.cbmProductID;
    }
    
    public String getAccountHeadDesc() {
        return accountHeadDesc;
    }
    
    public void setAccountHeadDesc(String accountHeadDesc) {
        this.accountHeadDesc = accountHeadDesc;
        setChanged();
    }
    
    public String getAccountHeadCode() {
        return this.accountHeadCode;
    }
    
    public void setAccountHeadCode(String accountHeadCode) {
        this.accountHeadCode = accountHeadCode;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public String getCustomerName() {
        return this.customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        setChanged();
    }
    
    public String getClearBalance() {
        return this.clearBalance;
    }
    
    public void setClearBalance(String clearBalance) {
        this.clearBalance = clearBalance;
        setChanged();
    }
    
    public String getLienSum() {
        return this.lienSum;
    }
    
    public void setLienSum(String lienSum) {
        this.lienSum = lienSum;
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
    
    public void resetLienListTable(){
        for(int i = tblLienListModel.getRowCount(); i > 0; i--){
            tblLienListModel.removeRow(0);
        }
    }
    
    public void resetProductPanel(){
        this.setCboProductID("");
        this.getCbmProductID().setKeyForSelected("");
        this.setAccountHeadCode("");
        this.setAccountHeadDesc("");
        this.setTxtAccountNumber("");
        this.setCustomerName("");
        this.setClearBalance("");
        this.setClearBalance1("");
        this.setLienSum("");
        this.setFreezeSum("");
    }
    
    public void resetAccountDetails(){
        this.setTxtAccountNumber("");
        this.setCustomerName("");
        this.setClearBalance("");
        this.setClearBalance1("");
        this.setLienSum("");
        this.setFreezeSum("");
    }
    /** set the value of Account head ID and description based on the product selected
     * in the UI
     */
    public void getAccountHeadForProduct() {
        /* based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen
         * same LookUp bean will be used for this purpose
         */
        if(!cbmProductID.getKeyForSelected().equals("")){
            param.put(CommonConstants.MAP_NAME,"getAccHead");
            param.put(CommonConstants.PARAMFORQUERY, CommonUtil.convertObjToStr(cbmProductID.getKeyForSelected()));
            try {
                final HashMap lookupValues = populateData(param);
                fillData((HashMap)lookupValues.get("DATA"));
            /*key = (ArrayList)keyValue.get("KEY");
            value = (ArrayList)keyValue.get("VALUE");*/
                //If the returned ArrayList has got proper value, then set the variables
                if( value.size() > 1 ){
                    setAccountHeadCode((String)value.get(1));
                    setAccountHeadDesc((String)key.get(1));
                }
                ttNotifyObservers();
            } catch (Exception e) {
                parseException.logException(e,true);
            }
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
        whereMap.put("ACCTNUMBER", getTxtAccountNumber());
        try {
            final List resultList = ClientUtil.executeQuery("getLienCustomerDetails", whereMap);
            final HashMap resultMap = (HashMap)resultList.get(0);
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
            final List resultList = ClientUtil.executeQuery("getLienProductDetails", whereMap);
            
            final HashMap resultMap = (HashMap)resultList.get(0);
            setPartialCustomerInfo(resultMap);
            setCboProductID((String) getCbmProductID().getDataForKey(CommonUtil.convertObjToStr(resultMap.get("PROD_ID"))));
            getCbmProductID().setKeyForSelected(CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
            setAccountHeadCode(CommonUtil.convertObjToStr(resultMap.get("AC_HD_ID")));
            setAccountHeadDesc(CommonUtil.convertObjToStr(resultMap.get("AC_HD_DESC")));
            setTxtAccountNumber(CommonUtil.convertObjToStr(whereMap.get("ACCTNUMBER")));
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To set the mentioned display fileds */
    private void setPartialCustomerInfo(HashMap resultMap) throws Exception{
        if( resultMap.get("LIEN_SUM") != null){
            setLienSum(CommonUtil.convertObjToStr(resultMap.get("LIEN_SUM")));
        }
         if( resultMap.get("FREEZE_SUM") != null){
            setFreezeSum(CommonUtil.convertObjToStr(resultMap.get("FREEZE_SUM")));
        }
        setCustomerName(CommonUtil.convertObjToStr(resultMap.get("Customer Name")));
//        setCustomerName(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER NAME")));
        setClearBalance(CommonUtil.convertObjToStr(resultMap.get("AVAILABLE_BALANCE")));
        setClearBalance1(CommonUtil.convertObjToStr(resultMap.get("CLEAR_BALANCE")));
    }
    
    public int addLien(int updateTab, int row){
        int option = -1;
        try{
            /*
             * To Decide which process out of Insert And Update is to be performed
             * depending on the Choice, set the Status of the operation...
             */
            objLienTO = new LienMarkingTO();
            objLienTO.setLienId(this.getLblLienId() );
            
            objLienTO.setActNum( this.getTxtAccountNumber() );
            objLienTO.setLienAmt(CommonUtil.convertObjToDouble(this.getTxtLienAmount()) );
            Date IsDt = DateUtil.getDateMMDDYYYY(this.getTdtLienDate());
            if(IsDt != null){
            Date isDate = (Date)curDate.clone();
            isDate.setDate(IsDt.getDate());
            isDate.setMonth(IsDt.getMonth());
            isDate.setYear(IsDt.getYear());
            objLienTO.setLienDt(isDate);
            }else{
              objLienTO.setLienDt(DateUtil.getDateMMDDYYYY(this.getTdtLienDate()));  
            }
//            objLienTO.setLienDt(DateUtil.getDateMMDDYYYY(this.getTdtLienDate()));
            objLienTO.setLienAcHd(this.getLblLienAccountHeadDesc() );
            objLienTO.setLienActNum(this.getTxtLienAccountNumber() );
            objLienTO.setRemarks(this.getTxtRemarks() );
           
            
            objLienTO.setProdId((String)cbmLienProduct.getKeyForSelected());
            objLienTO.setLienStatus(CommonConstants.STATUS_LIEN);
            /*
             * To Check each row for the Duplication..
             */
            ArrayList data = tblLienListModel.getDataArrayList();
            final int dataSize = data.size();
            boolean exist = false;
            ArrayList lienData = new ArrayList();
            
            ArrayList lienTabRow = new ArrayList();
            lienTabRow.add(tdtLienDate);
            lienTabRow.add(lblLienId);
            lienTabRow.add(txtLienAmount);
            lienTabRow.add(txtLienAccountNumber);
            lienTabRow.add(lblAuthorizeStatus);
            lienTabRow.add(lblLienStatus);
            LienMarkingTO objLienTOchk = null;
            String DATE, TYPE, REMARKS, LIENNO;
            for (int i=0;i<dataSize;i++){

                objLienTOchk = (LienMarkingTO) lienList.get(i);
                if (!CommonUtil.convertObjToStr(objLienTOchk.getAuthorizeStatus()).equals("AUTHORIZED")){
                    lienData = (ArrayList)data.get(i);
                    DATE = CommonUtil.convertObjToStr(lienData.get(0));
                    TYPE = CommonUtil.convertObjToStr(lienData.get(3));
                    REMARKS = CommonUtil.convertObjToStr(objLienTOchk.getRemarks());
                    LIENNO = CommonUtil.convertObjToStr(lienData.get(1));
                    System.out.println("%%%LIENNO"+LIENNO);
                    // To check for Duplication of the rows...
                    // Whether it is already entered or not...
                    if (DATE.equals(tdtLienDate) && 
//                            TYPE.equals((String)cbmLienProduct.getKeyForSelected()) && 
                            REMARKS.equals(txtRemarks)&& LIENNO.equals(lblLienId)){
                        if(updateTab == i){
                            lienList.remove(i);
                            lienList.add(i,objLienTO);
                            tblLienListModel.removeRow(i);
                             tblLienListModel.insertRow(i,lienTabRow);
                            option = 1;
                            break;
                        }else{
                            // Checking if the particular row already exists.
                            exist = true;
                            String[] options = {lienMarkingRB.getString("cDialogYes"),lienMarkingRB.getString("cDialogNo"),lienMarkingRB.getString("cDialogCancel")};
                            option = COptionPane.showOptionDialog(null, lienMarkingRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                            COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                            if (option == 0){
                                // Modifying the Existing Row...
                                updateLienTab(i, objLienTO);
                            }else if( option == 1){
                                resetLienDetails();
                            }
                            break;
                        }
                    }else if(updateTab != -1){
//                        lienList.remove(i); // when we make any changes to any row in edit mode it was showing to be affected to all the rows, so commented
//                        lienList.add(i,objLienTO);
//                        tblLienListModel.removeRow(i);
//                        tblLienListModel.insertRow(i,lienTabRow);
                        option = 1;
                    }
                }
            }
//            //__ Update the Record...
//            if(!exist && updateTab==1){
//                lienList.remove(row);
//                lienList.add(row,objLienTO);
//                tblLienListModel.removeRow(row);
//                tblLienListModel.insertRow(row,lienTabRow);
//                option = 1;
//            }
            
            //__ Insert The Record...
            if (!exist && updateTab == -1){
                tblLienListModel.addRow(lienTabRow);
                lienList.add(dataSize, objLienTO);
            }
            setChanged();
            lienTabRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return option;
    }
    
     /*
      * To Update the Row in the Table...
      */
    private void updateLienTab(int row, LienMarkingTO objLienMarkingTO){
        lienList.remove(row);
        lienList.add(row,objLienMarkingTO);
        tblLienListModel.setValueAt(txtLienAmount, row, 2);
    }
    
    /*
     * To delete the Row(s) in the Interest Table...
     */
    public void deleteLienTab(int selectedRow){
        log.info("In deleteInterestTab...");
        
        String LIENID =  CommonUtil.convertObjToStr(((LienMarkingTO)lienList.get(selectedRow)).getLienId());
        if((getActionType() == ClientConstants.ACTIONTYPE_EDIT) && (LIENID.length() > 0) ){
            lienDeleteList.add(lienList.get(selectedRow));
        }
        lienList.remove(selectedRow);
        tblLienListModel.removeRow(selectedRow);
        
//        try{
//            ArrayList data = tblLienListModel.getDataArrayList();
//            final int dataSize = data.size();
//            for (int i=0;i<dataSize;i++){
//                ArrayList lienDelete = new ArrayList();
//                
//                lienDelete = (ArrayList)data.get(i);
//                DATE = CommonUtil.convertObjToStr(lienDelete.get(0));
//                LIENACCOUNT = CommonUtil.convertObjToStr(lienDelete.get(3));
//                
//                if (DATE.equals(tdtLienDate) && LIENACCOUNT.equals(txtLienAccountNumber)){
//                    final String LIENID =  CommonUtil.convertObjToStr(((LienMarkingTO)lienList.get(i)).getLienId());
//                    if((getActionType() == ClientConstants.ACTIONTYPE_EDIT) && (LIENID.length() > 0) ){
//                        lienDeleteList.add(lienList.get(i));
//                    }
//                    lienList.remove(i);
//                    tblLienListModel.removeRow(i);
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
    public void unLien(String unlienRemarks, int selectedRow){
            LienMarkingTO objLienTO = (LienMarkingTO)lienList.get(selectedRow);
            if( objLienTO != null ){
                lienList.remove(selectedRow);

                objLienTO.setLienStatus(CommonConstants.STATUS_UNLIEN);
                objLienTO.setUnlienRemarks(unlienRemarks);
                objLienTO.setUnlienDt(curDate);

                objLienTO.setStatusBy(ProxyParameters.USER_ID);
                objLienTO.setStatusDt(curDate);

                objLienTO.setAuthorizeStatus(null);
                System.out.println("objLienTO: "+objLienTO);
                lienList.add(selectedRow,objLienTO);
            }
        
        
//        try{
//            ArrayList data = tblLienListModel.getDataArrayList();
//            final int dataSize = data.size();
//            for (int i=0;i<dataSize;i++){
//                ArrayList lienDelete = new ArrayList();
//                
//                lienDelete = (ArrayList)data.get(i);
//                DATE = CommonUtil.convertObjToStr(lienDelete.get(0));
//                LIENACCOUNT = CommonUtil.convertObjToStr(lienDelete.get(3));
//                
//                if (DATE.equals(tdtLienDate) && LIENACCOUNT.equals(txtLienAccountNumber)){
//                    objLienTO = (LienMarkingTO)lienList.get(i);
//                    if( objLienTO != null ){
//                        lienList.remove(i);
//                        
//                        objLienTO.setLienStatus(CommonConstants.STATUS_UNLIEN);
//                        objLienTO.setUnlienRemarks(unlienRemarks);
//                        objLienTO.setUnlienDt(curDate);
//                        
//                        objLienTO.setStatusBy(TrueTransactMain.USER_ID);
//                        objLienTO.setStatusDt(curDate);
//                        
//                        objLienTO.setAuthorizeStatus(null);
//                        System.out.println("objLienTO: "+objLienTO);
//                        lienList.add(i,objLienTO);
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
            ArrayList lienDataList = new ArrayList();
            whereMap.put(CommonConstants.STATUS, new Integer(getActionType()));
            System.out.println("#### map before executeQuery : "+whereMap);
            lienMap = (HashMap)proxy.executeQuery(whereMap,map);
            
            lienDataList = (ArrayList) lienMap.get("LienMarkingTO");
            int length = lienDataList.size();
            
            for(int i=0; i<length; i++){
                LienMarkingTO objLienTO = new LienMarkingTO();
                ArrayList lienTabRow = new ArrayList();
                objLienTO = (LienMarkingTO)lienDataList.get(i);
                
                lienList.add(objLienTO);
                
                lienTabRow.add(DateUtil.getStringDate(objLienTO.getLienDt()));
                lienTabRow.add(CommonUtil.convertObjToStr(objLienTO.getLienId()));
                lienTabRow.add(CommonUtil.convertObjToStr(objLienTO.getLienAmt()));
                lienTabRow.add(CommonUtil.convertObjToStr(objLienTO.getLienActNum()));
                lienTabRow.add(CommonUtil.convertObjToStr(objLienTO.getAuthorizeStatus()));
                lienTabRow.add(CommonUtil.convertObjToStr(objLienTO.getLienStatus()));
                tblLienListModel.addRow(lienTabRow);
            }
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public boolean checkAcNoWithoutProdType(String actNum, boolean oldActNo) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                if(oldActNo){
                    setTxtAccountNumber(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                }else{
                    setTxtLienAccountNumber(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                }
                if(OAFlag == true){
                    if(mapData.get("PROD_TYPE").equals("OA")){
                        cbmProductID.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                        isExists = true;
                    }else{
                        cbmProductID.setKeyForSelected("");
                        isExists = false;
                    }
                }
                if(LOANFlag == true){
                    if(mapData.get("PROD_TYPE").equals("TL")){
                        cbmLienProduct.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                        isExists = true;
                    }else{
                        cbmLienProduct.setKeyForSelected("");
                        isExists = false;
                    }
                }
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
//                setCbmProdId("");
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
    
    /** While selecting a row from the table, the corresponding Lien detail will be
     * populated in the fields
     */
    public void populateLienDetails(int row){
        try{
            objLienTO = (LienMarkingTO)lienList.get(row);
            populateLienData(objLienTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To set the corresponding Lien observable instance variables */
    private void populateLienData(LienMarkingTO objLienTO) throws Exception{
        this.setLblLienId(CommonUtil.convertObjToStr(objLienTO.getLienId()));
        this.setTxtLienAmount(CommonUtil.convertObjToStr(objLienTO.getLienAmt()));
        this.setTdtLienDate(DateUtil.getStringDate(objLienTO.getLienDt()));
        this.setLblLienAccountHeadDesc(CommonUtil.convertObjToStr(objLienTO.getLienAcHd()));
        this.setCboLienProduct((String) getCbmLienProduct().getDataForKey(CommonUtil.convertObjToStr(objLienTO.getProdId())));
        this.setTxtLienAccountNumber(CommonUtil.convertObjToStr(objLienTO.getLienActNum()));
        this.setLblLienLienCustName(setCustName(getTxtLienAccountNumber()));
        this.setTxtRemarks(CommonUtil.convertObjToStr(objLienTO.getRemarks()));
        
        this.setLblAuth(CommonUtil.convertObjToStr(objLienTO.getAuthorizeStatus()));
        this.setLblLienStatus(CommonUtil.convertObjToStr(objLienTO.getLienStatus()));
//        this.setLblLienLienCustName("lblLienCustName");
        setStatusBy(objLienTO.getStatusBy());
        setAuthorizeStatus(objLienTO.getAuthorizeStatus());
    }
    
    /**
     * To fill the Data for the record, Selected for Authorization.
     */
    public void  setAuthRowData(String LienId)throws Exception{
        ArrayList lienDataList = new ArrayList();
        lienDataList = tblLienListModel.getDataArrayList();
        int length = lienDataList.size();
        for(int i =0; i< length; i++){
            if( ((String)tblLienListModel.getValueAt(i, 1)).equalsIgnoreCase(LienId)){
                populateLienData((LienMarkingTO)lienList.get(i));
                setAuthRow(i);
                break;
            }
        }
        ttNotifyObservers();
    }
    
    /**
     * To reset the Fields associated with the Table enteries...
     */
    public void resetLienDetails(){
        this.setLblLienId("");
        this.setTxtLienAmount("");
        this.setTdtLienDate("");
        this.setCboLienProduct("");
        this.setLblLienAccountHeadDesc("");
        this.setTxtLienAccountNumber("");
        this.setLblLienLienCustName("");
        this.setTxtRemarks("");
        
        this.setLblLienStatus("");
        this.setLblAuth("");
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
            result = false;
            if( e instanceof TTException ){
                TTException tte = (TTException)e;
            }
            setResult(ClientConstants.ACTIONTYPE_FAILED);
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
        System.out.println("lienList: "+lienList);
        if(getAuthorizeMap() == null){
            data.put("LienMarkingTO",lienList);
            data.put("COMMAND",getCommand());
            /* To Maintain a List which Containd
             * the Data for the records deleted at the time of Edit
             */
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                data.put("LienMarkingDelete",lienDeleteList);
            }
        }
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
    
    public void resetForm(){
        resetLienListTable();
        resetProductPanel();
    }
    
    public void resetObjects(){
        lienList = null;
        lienDeleteList = null;
        lienList = new ArrayList();
        lienDeleteList = new ArrayList();
    }
    
    /** Called by CellRenderer UI to find whether a lien data is deleted or not */
    public boolean isDeleted(int row){
        //        final Object selectedRow = ((ArrayList)tblLienListModel.getDataArrayList().get(row)).get(0);
        //        final Object selectedRow = (ArrayList)tblLienListModel.getDataArrayList().get(row);
        //        final LienMarkingTO delLienTO = (LienMarkingTO)lienMap.get(new Integer(selectedRow.toString()));
        final LienMarkingTO delLienTO = (LienMarkingTO)lienList.get(row);
        if(delLienTO!=null && delLienTO.getCommand()!=null){
            if( delLienTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE) ) {
                return true;
            }
        }
        return false;
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
    
    void setCboLienProduct(String cboLienProduct){
        this.cboLienProduct = cboLienProduct;
        setChanged();
    }
    String getCboLienProduct(){
        return this.cboLienProduct;
    }
    
    void setLblLienLienCustName(String lblLienCustName){
        this.lblLienCustName = lblLienCustName;
        setChanged();
    }
    String getLblLienCustName(){
        return this.lblLienCustName;
    }
    
    void setCbmLienProduct(ComboBoxModel cbmLienProduct){
        this.cbmLienProduct = cbmLienProduct;
        setChanged();
    }
    ComboBoxModel getCbmLienProduct(){
        return this.cbmLienProduct;
    }
    
    public String setLienAcHead() {
        String achd = "";
        try {
            final HashMap accountHeadMap = new HashMap();
            accountHeadMap.put("PRODID",(String)cbmLienProduct.getKeyForSelected());
            final List resultList = ClientUtil.executeQuery("Lein.getAccountHead", accountHeadMap);
            if (resultList != null && resultList.size() > 0){
                final HashMap resultMap = (HashMap)resultList.get(0);
                achd = CommonUtil.convertObjToStr(resultMap.get("ACCT_HEAD"));
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return achd;
    }
    /*
     * To get the Name of the Customer depending on the Basis of LienAccount Number Selected
     */
    public String setCustName(String AccountNO) {
        String custName = "";
        try {
            final HashMap custNameMap = new HashMap();
            custNameMap.put("ACCTNO",AccountNO);
            final List resultList = ClientUtil.executeQuery("Lein.getCustName", custNameMap);
            if (resultList != null && resultList.size() > 0){
                final HashMap resultMap = (HashMap)resultList.get(0);
                custName = CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_NAME"));
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return custName;
    }
    
    public String getLblLienId() {
        return this.lblLienId;
    }
    
    public void setLblLienId(String lblLienId) {
        this.lblLienId = lblLienId;
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
    
    public String getLblLienStatus() {
        return this.lblLienStatus;
    }
    
    public void setLblLienStatus(String lblLienStatus) {
        this.lblLienStatus = lblLienStatus;
        setChanged();
    }
    
    public void setNewObj(){
        lienList = new ArrayList();
        lienDeleteList = new ArrayList();
    }
    
    public int rowCount(){
        int rowCount = 0;
        try{
            rowCount = tblLienListModel.getRowCount();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return rowCount;
    }
    
    public double lienTabSum(){
        double amt = 0;
        try{
            int rowCount = tblLienListModel.getRowCount();
            for(int i=0; i< rowCount ; i++){
                String lienNo = CommonUtil.convertObjToStr(tblLienListModel.getValueAt(i, 1));
                if (lienNo.length()>=0)
                    amt = amt + CommonUtil.convertObjToDouble(tblLienListModel.getValueAt(i, 2)).doubleValue();
            }
        }catch(Exception e){
            System.out.println("error in lienTabSum()");
            e.printStackTrace();
        }
        
        return amt;
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
                System.out.println("resultMap : " + CommonUtil.convertObjToStr(resultMap.get("ACCT_NAME")).length());
//                if(CommonUtil.convertObjToStr(resultMap.get("ACCT_NAME")).length() > 0){
                    val = true;
//                }
            }           
        } catch (Exception e) {
            System.out.println("Error in verifyAccountDate() ");
            parseException.logException(e,true);
        }
        
        return val;
    }
    
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
     * Getter for property FreezeSum.
     * @return Value of property FreezeSum.
     */
    public java.lang.String getFreezeSum() {
        return FreezeSum;
    }
    
    /**
     * Setter for property FreezeSum.
     * @param FreezeSum New value of property FreezeSum.
     */
    public void setFreezeSum(java.lang.String FreezeSum) {
        this.FreezeSum = FreezeSum;
    }
    
}

