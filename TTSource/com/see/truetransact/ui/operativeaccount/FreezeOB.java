/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * FreezeOB.java
 *
 * Created on August 13, 2003, 4:32 PM
 */

package com.see.truetransact.ui.operativeaccount;

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
public class FreezeOB extends CObservable {
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
    private String todAmountExist = ""; // Added by nithya
    private boolean todProd = false; // Added by nithya
    private String clearBalance1;
    private String freezeSum;
    private String LienSum;
    private int result;
    private String advances="";
    private HashMap _authorizeMap;
    private int row;
    
    private ArrayList freezeList = new ArrayList();
    private ArrayList freezeDeleteList = new ArrayList();
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    
    private final static Logger log = Logger.getLogger(FreezeOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    //    FreezeRB freezeRB = new FreezeRB();
    java.util.ResourceBundle freezeRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.FreezeRB", ProxyParameters.LANGUAGE);
    
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
    private FreezeTO objFreezeTO;
    
    private final String COMP_FREEZE = "COMPLETE";
    Date curDate = null;
    
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
    public FreezeOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
            final ArrayList freezeListColumn = new ArrayList();
            final FreezeRB freezeRB = new FreezeRB();
            //            freezeListColumn.add(freezeRB.getString("tblColumn1"));
            freezeListColumn.add(freezeRB.getString("tblColumn2"));
            freezeListColumn.add(freezeRB.getString("tblColumn3"));
            freezeListColumn.add(freezeRB.getString("tblColumn4"));
            freezeListColumn.add(freezeRB.getString("tblColumn5"));
            freezeListColumn.add(freezeRB.getString("tblColumn7"));
            freezeListColumn.add(freezeRB.getString("tblColumn8"));
            //freezeListColumn.add(freezeRB.getString("tblColumn6"));
            tblFreezeListModel = new EnhancedTableModel(null, freezeListColumn);
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "FreezeJNDI");
            map.put(CommonConstants.HOME, "operativeaccount.FreezeHome");
            map.put(CommonConstants.REMOTE, "operativeaccount.Freeze");
            
            fillDropdown();
            freezeList = new ArrayList();
            freezeDeleteList = new ArrayList();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
    }
    /** Creates a new instance of FreezeOB */
    public FreezeOB(String advances) {
        try {
            this.advances=advances;
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            
            final ArrayList freezeListColumn = new ArrayList();
            final FreezeRB freezeRB = new FreezeRB();
            //            freezeListColumn.add(freezeRB.getString("tblColumn1"));
            freezeListColumn.add(freezeRB.getString("tblColumn2"));
            freezeListColumn.add(freezeRB.getString("tblColumn3"));
            freezeListColumn.add(freezeRB.getString("tblColumn4"));
            freezeListColumn.add(freezeRB.getString("tblColumn5"));
            freezeListColumn.add(freezeRB.getString("tblColumn7"));
            freezeListColumn.add(freezeRB.getString("tblColumn8"));
            //freezeListColumn.add(freezeRB.getString("tblColumn6"));
            tblFreezeListModel = new EnhancedTableModel(null, freezeListColumn);
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "FreezeJNDI");
            map.put(CommonConstants.HOME, "operativeaccount.FreezeHome");
            map.put(CommonConstants.REMOTE, "operativeaccount.Freeze");
            
            fillDropdown();
            freezeList = new ArrayList();
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
        if(advances.equals("ADVANCES"))
            param.put(CommonConstants.MAP_NAME,"Charges.getProductDataAD");
        else
            param.put(CommonConstants.MAP_NAME,"getAccProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        HashMap lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("DATA"));
        cbmProductID = new ComboBoxModel(key,value);
        
        param.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookupKeys = new ArrayList(1);
        lookupKeys.add("FREEZE.FREEZETYPE");
        param.put(CommonConstants.PARAMFORQUERY, lookupKeys);
        lookupValues = populateData(param);
        fillData((HashMap)lookupValues.get("FREEZE.FREEZETYPE"));
        cbmType = new ComboBoxModel(key,value);
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
    
    void setTxtAmount(String txtAmount){
        this.txtAmount = txtAmount;
        setChanged();
    }
    String getTxtAmount(){
        return this.txtAmount;
    }
    
    void setTdtFreezeDate(String tdtFreezeDate){
        this.tdtFreezeDate = tdtFreezeDate;
        setChanged();
    }
    String getTdtFreezeDate(){
        return this.tdtFreezeDate;
    }
    
    void setCboType(String cboType){
        this.cboType = cboType;
        setChanged();
    }
    String getCboType(){
        return this.cboType;
    }
    
    void setTxtRemarks(String txtRemarks){
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    String getTxtRemarks(){
        return this.txtRemarks;
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
    
    void setTblFreezeListModel(EnhancedTableModel tblFreezeListModel){
        this.tblFreezeListModel = tblFreezeListModel;
        setChanged();
    }
    EnhancedTableModel getTblFreezeListModel(){
        return this.tblFreezeListModel;
    }
    
    void setCbmProductID(ComboBoxModel cbmProductID){
        this.cbmProductID = cbmProductID;
        setChanged();
    }
    ComboBoxModel getCbmProductID(){
        return this.cbmProductID;
    }
    
    void setCbmType(ComboBoxModel cbmType){
        this.cbmType = cbmType;
        setChanged();
    }
    ComboBoxModel getCbmType(){
        return this.cbmType;
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
    
    public String getFreezeSum() {
        return this.freezeSum;
    }
    
    public void setFreezeSum(String freezeSum) {
        this.freezeSum = freezeSum;
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
        freezeMap = null;
        objFreezeTO = null;
    }
    
    public void resetFreezeListTable(){
        for(int i = tblFreezeListModel.getRowCount(); i > 0; i--){
            tblFreezeListModel.removeRow(0);
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
        this.setFreezeSum("");
        this.setLienSum("");
        this.setTodAmountExist(""); // Added by nithya
        this.setTodProd(false); // Added by nithya
    }
    
    public void resetAccountDetails(){
        this.setTxtAccountNumber("");
        this.setCustomerName("");
        this.setClearBalance("");
        this.setClearBalance1("");
        this.setFreezeSum("");
        this.setLienSum("");
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
                setAccountHeadCode(CommonUtil.convertObjToStr(value.get(1)));
                setAccountHeadDesc(CommonUtil.convertObjToStr(key.get(1)));
                
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
        whereMap.put("ACCTNUMBER", getTxtAccountNumber());
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
                        setTxtAccountNumber("");
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
            if(whereMap.containsKey("ADVANCES") && whereMap.get("ADVANCES").equals("ADVANCES"))
                 resultList = ClientUtil.executeQuery("getFreezeProductDetailsAD", whereMap);
            else
                 resultList = ClientUtil.executeQuery("getFreezeProductDetails", whereMap);
            
            final HashMap resultMap = (HashMap)resultList.get(0);
            setPartialCustomerInfo(resultMap);
            setCboProductID((String) getCbmProductID().getDataForKey(CommonUtil.convertObjToStr(resultMap.get("PROD_ID"))));
            getCbmProductID().setKeyForSelected(CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
            setAccountHeadCode(CommonUtil.convertObjToStr(resultMap.get("AC_HD_DESC")));
            setAccountHeadDesc(CommonUtil.convertObjToStr(resultMap.get("AC_HD_ID")));
            setTxtAccountNumber(CommonUtil.convertObjToStr(whereMap.get("ACCTNUMBER")));
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    /** To set the mentioned display fileds */
    private void setPartialCustomerInfo(HashMap resultMap) throws Exception{
        
        System.out.println("CustomerInfo: " + resultMap);
        
        if( resultMap.get("FREEZE_SUM") != null){
            setFreezeSum(CommonUtil.convertObjToStr(resultMap.get("FREEZE_SUM")));
        }
         if( resultMap.get("LIEN_SUM") != null){
            setLienSum(CommonUtil.convertObjToStr(resultMap.get("LIEN_SUM")));
        }
        setCustomerName(CommonUtil.convertObjToStr(resultMap.get("Customer Name")));
        setClearBalance(CommonUtil.convertObjToStr(resultMap.get("AVAILABLE_BALANCE")));
        setClearBalance1(CommonUtil.convertObjToStr(resultMap.get("CLEAR_BALANCE")));
        
        System.out.println("product id ::" + CommonUtil.convertObjToStr(getCbmProductID().getKeyForSelected()));
        // TOD Amount to be taken
            HashMap todCheckmap = new HashMap();
            todCheckmap.put("PROD_ID", CommonUtil.convertObjToStr(getCbmProductID().getKeyForSelected()));
            List todList = ClientUtil.executeQuery("isTODSetForProduct", todCheckmap);
            if (todList != null && todList.size() > 0) {
                 HashMap todMap = (HashMap) todList.get(0);
                if (todMap.containsKey("TEMP_OD_ALLOWED")) {
                    if (CommonUtil.convertObjToStr(todMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                       setTodProd(true);                        
                       HashMap actDetailMap = new HashMap();
                       actDetailMap.put("ACT_NUM",getTxtAccountNumber());
                       List actDetailLst = ClientUtil.executeQuery("getLatestSBODLimitForAccNum", actDetailMap);
                       System.out.println("actDetailLst ::" + actDetailLst);                       
                       if(actDetailLst != null && actDetailLst.size() > 0){
                           HashMap actDetailLstMap = (HashMap)actDetailLst.get(0);
                           double clearBal = CommonUtil.convertObjToDouble(actDetailLstMap.get("CLEAR_BALANCE"));
                           double availBal = CommonUtil.convertObjToDouble(actDetailLstMap.get("AVAILABLE_BALANCE"));
                           double todLimit = CommonUtil.convertObjToDouble(actDetailLstMap.get("TOD_LIMIT"));
                           if(clearBal < 0){
                               double bal = todLimit + clearBal;
                               setTodAmountExist(String.valueOf(bal));
                           }else{
                               setTodAmountExist(String.valueOf(todLimit));                               
                           }
                       }
                    }
                }    
            }        
    }
    
    public boolean isNewAllowed(){
        boolean returnType = false;
        ArrayList data = tblFreezeListModel.getDataArrayList();
        int size = data.size();
        if(size <= 0){
            returnType = false;
        }else{
            for(int i = 0; i< size; i++){
                System.out.println("Freeze Type in i Row: " + CommonUtil.convertObjToStr(tblFreezeListModel.getValueAt(i,0)));
                if(CommonUtil.convertObjToStr(tblFreezeListModel.getValueAt(i,0)).equalsIgnoreCase(COMP_FREEZE) ){
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
            objFreezeTO = new FreezeTO();
            
            objFreezeTO.setFreezeId(getLblFreezeId());
            objFreezeTO.setActNum( this.getTxtAccountNumber() );
            objFreezeTO.setFreezeAmt(CommonUtil.convertObjToDouble(getTxtAmount()) );
            
            Date Dt = DateUtil.getDateMMDDYYYY(tdtFreezeDate);
            if(Dt != null){
                Date dtDate = (Date)curDate.clone();
                dtDate.setDate(Dt.getDate());
                dtDate.setMonth(Dt.getMonth());
                dtDate.setYear(Dt.getYear());
                //            objFreezeTO.setFreezeDt(DateUtil.getDateMMDDYYYY(tdtFreezeDate));
                objFreezeTO.setFreezeDt(dtDate);
            }else{
                objFreezeTO.setFreezeDt(DateUtil.getDateMMDDYYYY(tdtFreezeDate));
            }
            
            objFreezeTO.setFreezeType((String)cbmType.getKeyForSelected());
            objFreezeTO.setRemarks(getTxtRemarks());
            objFreezeTO.setAuthorizeStatus("");
            objFreezeTO.setFreezeStatus(CommonConstants.STATUS_FREEZE);
            /*
             * To Check each row for the Duplication..
             */
            ArrayList data = tblFreezeListModel.getDataArrayList();
            int dataSize = data.size();
            boolean exist = false;
            ArrayList freezeData = new ArrayList();
            
            ArrayList freezeTabRow = new ArrayList();
            freezeTabRow.add((String)cbmType.getKeyForSelected());
            System.out.println("(String)cbmType.getKeyForSelected(): "+ (String)cbmType.getKeyForSelected());
            freezeTabRow.add(lblFreezeId);
            freezeTabRow.add(txtAmount);
            freezeTabRow.add(tdtFreezeDate);
            freezeTabRow.add(lblAuthorizeStatus);
            freezeTabRow.add(lblFreezeStatus);
            FreezeTO objFreezeTOchk = null;
            // Duplicate checking
            for (int i=0;i<dataSize;i++){
                objFreezeTOchk = (FreezeTO) freezeList.get(i);
                if (!CommonUtil.convertObjToStr(objFreezeTOchk.getAuthorizeStatus()).equals("AUTHORIZED")){
                    freezeData = (ArrayList)data.get(i);
                    DATE = CommonUtil.convertObjToStr(freezeData.get(3));
                    TYPE = CommonUtil.convertObjToStr(freezeData.get(0));
                    REMARKS = CommonUtil.convertObjToStr(objFreezeTOchk.getRemarks());
                    String FREEZENO = CommonUtil.convertObjToStr(freezeData.get(1));
                    // To check for Duplication of the rows...
                    // Whether it is already entered or not...
                    
                    if (DATE.equals(tdtFreezeDate) &&
                    //                    TYPE.equals((String)cbmType.getKeyForSelected()) &&
                    FREEZENO.equals(lblFreezeId)&&
                    REMARKS.equals(txtRemarks)){
                        if(updateTab == i){
                            System.out.println("data Updated...");
                            freezeList.remove(i);
                            freezeList.add(i,objFreezeTO);
                            tblFreezeListModel.removeRow(i);
                            tblFreezeListModel.insertRow(i,freezeTabRow);
                            option = 1;
                            break;
                        }else{
                            // Checking if the particular row already exists.
                            exist = true;
                            String[] options = {freezeRB.getString("cDialogYes"),freezeRB.getString("cDialogNo"),freezeRB.getString("cDialogCancel")};
                            option = COptionPane.showOptionDialog(null, freezeRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                            COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                            if (option == 0){
                                // Modifying the Existing Row...
                                updateFreezeTab(i, objFreezeTO);
                            }else if( option == 1){
                                resetFreezeDetails();
                            }
                            break;
                        }
                    }else if(updateTab != -1){
                        //                        freezeList.remove(i);
                        //                        freezeList.add(i,objFreezeTO);
                        //                        tblFreezeListModel.removeRow(i);
                        //                        tblFreezeListModel.insertRow(i,freezeTabRow);
                        option = 1;
                    }
                }
            }
            
            if (!exist && updateTab == -1){
                tblFreezeListModel.addRow(freezeTabRow);
                freezeList.add(dataSize, objFreezeTO);
            }
            setChanged();
            freezeTabRow = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return option;
    }
    
     /*
      * To Update the Row in the Table...
      */
    private void updateFreezeTab(int row, FreezeTO objFreezeTO){
        freezeList.remove(row);
        freezeList.add(row,objFreezeTO);
        tblFreezeListModel.setValueAt(txtAmount, row, 2);
    }
    
     /*
      * To delete the Row(s) in the Interest Table...
      */
    public void deleteFreezeTab(int selectedRow){
        log.info("In deleteInterestTab...");
        
        String FREEZEID =  CommonUtil.convertObjToStr(((FreezeTO)freezeList.get(selectedRow)).getFreezeId());
        if((getActionType() == ClientConstants.ACTIONTYPE_EDIT) && (FREEZEID.length() > 0) ){
            FreezeTO objFreezeTO =   (FreezeTO)freezeList.get(selectedRow);
            freezeDeleteList.add(objFreezeTO);
        }
        freezeList.remove(selectedRow);
        tblFreezeListModel.removeRow(selectedRow);
        
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
        //                        objFreezeTO =   (FreezeTO)freezeList.get(i);
        ////                        objFreezeTO.setStatusBy(TrueTransactMain.USER_ID);
        ////                        objFreezeTO.setStatusDt(currDt.clone());
        //                        freezeDeleteList.add(objFreezeTO);
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
        FreezeTO objFreezeTO = (FreezeTO)freezeList.get(selectedRow);
        if( objFreezeTO != null ){
            freezeList.remove(selectedRow);
            
            objFreezeTO.setFreezeStatus(CommonConstants.STATUS_UNFREEZE);
            objFreezeTO.setUnfreezeRemarks(unfreezeRemarks);
            objFreezeTO.setUnfreezeDt(curDate);
            
            objFreezeTO.setStatusBy(ProxyParameters.USER_ID);
            objFreezeTO.setStatusDt(curDate);
            
            objFreezeTO.setAuthorizeStatus(null);
            freezeList.add(selectedRow,objFreezeTO);
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
        //                    objFreezeTO = (FreezeTO)freezeList.get(i);
        //                    if( objFreezeTO != null ){
        //                        freezeList.remove(i);
        //
        //                        objFreezeTO.setFreezeStatus(CommonConstants.STATUS_UNFREEZE);
        //                        objFreezeTO.setUnfreezeRemarks(unfreezeRemarks);
        //                        objFreezeTO.setUnfreezeDt(currDt);
        //
        //                        objFreezeTO.setStatusBy(TrueTransactMain.USER_ID);
        //                        objFreezeTO.setStatusDt(currDt);
        //
        //                        objFreezeTO.setAuthorizeStatus(null);
        //                        freezeList.add(i,objFreezeTO);
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
            
            freezeDataList = (ArrayList) freezeMap.get("FreezeTO");
            int length = freezeDataList.size();
            
            for(int i=0; i<length; i++){
                FreezeTO objFreezeTO = new FreezeTO();
                ArrayList freezeTabRow = new ArrayList();
                objFreezeTO = (FreezeTO)freezeDataList.get(i);
                
                freezeList.add(objFreezeTO);
                
                freezeTabRow.add(CommonUtil.convertObjToStr(objFreezeTO.getFreezeType()));
                freezeTabRow.add(CommonUtil.convertObjToStr(objFreezeTO.getFreezeId()));
                freezeTabRow.add(CommonUtil.convertObjToStr(objFreezeTO.getFreezeAmt()));
                freezeTabRow.add(DateUtil.getStringDate(objFreezeTO.getFreezeDt()));
                freezeTabRow.add(CommonUtil.convertObjToStr(objFreezeTO.getAuthorizeStatus()));
                freezeTabRow.add(CommonUtil.convertObjToStr(objFreezeTO.getFreezeStatus()));
                tblFreezeListModel.addRow(freezeTabRow);
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
            objFreezeTO = (FreezeTO)freezeList.get(row);
            populateFreezeData(objFreezeTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** To set the corresponding Freeze observable instance variables */
    private void populateFreezeData(FreezeTO objFreezeTO) throws Exception{
        this.setLblFreezeId(CommonUtil.convertObjToStr(objFreezeTO.getFreezeId()));
        this.setTxtAmount(CommonUtil.convertObjToStr(objFreezeTO.getFreezeAmt()));
        this.setTdtFreezeDate(DateUtil.getStringDate(objFreezeTO.getFreezeDt()));
        this.setCboType((String) getCbmType().getDataForKey(CommonUtil.convertObjToStr(objFreezeTO.getFreezeType())));
        this.setTxtRemarks(CommonUtil.convertObjToStr(objFreezeTO.getRemarks()));
        
        this.setLblAuth(CommonUtil.convertObjToStr(objFreezeTO.getAuthorizeStatus()));
        this.setLblFreezeStatus(CommonUtil.convertObjToStr(objFreezeTO.getFreezeStatus()));
        setStatusBy(objFreezeTO.getStatusBy());
        setAuthorizeStatus(objFreezeTO.getAuthorizeStatus());
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
                populateFreezeData((FreezeTO)freezeList.get(i));
                setAuthRow(i);
                break;
            }
        }
        ttNotifyObservers();
    }
    
    public void resetFreezeDetails(){
        this.setLblFreezeId("");
        this.setTxtAmount("");
        this.setTdtFreezeDate("");
        this.setCboType("");
        this.setTxtRemarks("");
        this.setLblAuth("");
        this.setLblFreezeStatus("");
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
            data.put("FreezeTO",freezeList);
            data.put("COMMAND",getCommand());
            /* To Maintain a List which Containd
             * the Data for the records deleted at the time of Edit
             */
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                data.put("FreezeDelete",freezeDeleteList);
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
        freezeList = null;
        freezeDeleteList = null;
        freezeList = new ArrayList();
        freezeDeleteList = new ArrayList();
    }
    
    
    public void resetForm(){
        resetVariables();
        //        resetFreezeDetails();
        resetFreezeListTable();
        resetProductPanel();
        //        ttNotifyObservers();
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
        this.setTxtAmount("");
        this.setCboType("");
        this.setTxtRemarks("");
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
        freezeList = new ArrayList();
        freezeDeleteList = new ArrayList();
    }
    
    public int rowCount(){
        int rowCount = 0;
        try{
            rowCount = tblFreezeListModel.getRowCount();
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
    
    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                if(mapData.get("PROD_TYPE").equals("OA")){
                    setTxtAccountNumber(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                    cbmProductID.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                    isExists = true;
                }else{
                    cbmProductID.setKeyForSelected("");
                    isExists = false;
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

    public String getTodAmountExist() {
        return todAmountExist;
    }

    public void setTodAmountExist(String todAmountExist) {
        this.todAmountExist = todAmountExist;
    }

    public boolean isTodProd() {
        return todProd;
    }

    public void setTodProd(boolean todProd) {
        this.todProd = todProd;
    }
 
}
