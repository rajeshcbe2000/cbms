/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ProductMasterOB.java
 *
 * Created on Mon Jun 20 16:52:36 GMT+05:30 2011
 */

package com.see.truetransact.ui.paddyprocurement;

import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.paddyprocurement.PaddyItemOpeningStockTO;
import org.apache.log4j.Logger;
/**
 *
 * @author
 */

public class PaddyItemOpeningStockOB extends CObservable{
    
    private String txtItemCode = "";
    private String txtItemDesc = "";
    private String txtPurchasePrice = "";
    private String txtSellingPrice = "";
    private String cboUnit = "";
    private String txtQty = "";
    private String txtOrderLevel = "";
    private String txtPurchaseAcHd = "";
    private String txtPurchaseReturnAcHd = "";
    private String txtSalesAcHd = "";
    private String txtSalesReturnAcHd = "";
    private String txtTaxAcHd = "";
    private static PaddyItemOpeningStockOB objPaddyItemOpeningStockOB;
    private ComboBoxModel cbmUnit;
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger _log = Logger.getLogger(PaddyItemOpeningStockOB.class);
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int actionType=0;
    private int result=0;
    HashMap data = new HashMap();
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    private HashMap map;
    private ProxyFactory proxy = null;
    
    private Date currDt = null;
    
    
    private PaddyItemOpeningStockOB() {
        currDt = ClientUtil.getCurrentDate();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "PaddyItemOpeningStockJNDI");
        map.put(CommonConstants.HOME, "serverside.paddyprocurement.PaddyItemOpeningStockHome");
        map.put(CommonConstants.REMOTE, "serverside.paddyprocurement.PaddyItemOpeningStock");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropdown();
//            setBillsChargesTab();
//            tblBillsCharges=new EnhancedTableModel(null,billsChargeTabTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    static {
        try {
            _log.info("Creating BillsOB...");
            objPaddyItemOpeningStockOB = new PaddyItemOpeningStockOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    /**
     * Returns an instance of PaddyLocalityMasterOB.
     * @return  PaddyLocalityMasterOB
     */
    
    public static PaddyItemOpeningStockOB getInstance()throws Exception{
        return objPaddyItemOpeningStockOB;
    }
    private void fillDropdown() throws Exception{
        try{
            _log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PADDY.UNITS");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("PADDY.UNITS"));
            cbmUnit= new ComboBoxModel(key,value);
            
        }catch(NullPointerException e){
            parseException.logException(e,true);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void resetForm(){
        setTxtItemCode("");
        setTxtItemDesc("");
        setTxtOrderLevel("");
        setTxtPurchaseAcHd("");
        setCboUnit("");
        setTxtPurchasePrice("");
        setTxtPurchaseReturnAcHd("");
        setTxtQty("");
        setTxtSalesAcHd("");
        setTxtSalesReturnAcHd("");
        setTxtSellingPrice("");
        setTxtTaxAcHd("");
        ttNotifyObservers();
    }
    public void ttNotifyObservers(){
        notifyObservers();
    }
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    
      /* Executes Query using the TO object */
    public void doAction() {
        try {
            HashMap dataMap = new HashMap();
            dataMap.put(CommonConstants.MODULE, getModule()); 
            dataMap.put(CommonConstants.SCREEN, getScreen());
            dataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            String command = getCommand();
            dataMap.put("PaddyItemOpeningStockTO", getPaddyItemOpeningStockTO(command));
            System.out.println("@#$@#$dataMap:"+dataMap);
            HashMap proxyResultMap = proxy.execute(dataMap, map);
            setResult(actionType);
            actionType = ClientConstants.ACTIONTYPE_CANCEL;
            dataMap = null;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /* To get the type of command */
    private String getCommand() throws Exception{
        String command = null;
        switch (getActionType()) {
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
    public void authorizeLocationMaster(HashMap singleAuthorizeMap) {
        try{
            singleAuthorizeMap.put("AUTH_DATA","AUTH_DATA");
            proxy.executeQuery(singleAuthorizeMap,map);
        }
        catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    
    private PaddyItemOpeningStockTO getPaddyItemOpeningStockTO (String command) {
        PaddyItemOpeningStockTO objPaddyItemOpeningStockTO = new PaddyItemOpeningStockTO();
        objPaddyItemOpeningStockTO.setCommand (command);
        objPaddyItemOpeningStockTO.setTxtItemCode(getTxtItemCode());
        objPaddyItemOpeningStockTO.setTxtItemDesc(getTxtItemDesc());
        objPaddyItemOpeningStockTO.setTxtPurchaseAcHd(getTxtPurchaseAcHd());
        objPaddyItemOpeningStockTO.setTxtPurchasePrice(getTxtPurchasePrice());
        objPaddyItemOpeningStockTO.setCboUnit(CommonUtil.convertObjToStr(getCbmUnit().getKeyForSelected()));
        objPaddyItemOpeningStockTO.setTxtPurchaseReturnAcHd(getTxtPurchaseReturnAcHd());
        objPaddyItemOpeningStockTO.setTxtOrderLevel(getTxtOrderLevel());
        objPaddyItemOpeningStockTO.setTxtQty(getTxtQty());
        objPaddyItemOpeningStockTO.setTxtSalesAcHd(getTxtSalesAcHd());
        objPaddyItemOpeningStockTO.setTxtSalesReturnAcHd(getTxtSalesReturnAcHd());
        objPaddyItemOpeningStockTO.setTxtSellingPrice(getTxtSellingPrice());
        objPaddyItemOpeningStockTO.setTxtTaxAcHd(getTxtTaxAcHd());
        objPaddyItemOpeningStockTO.setStatusBy(TrueTransactMain.USER_ID);
        objPaddyItemOpeningStockTO.setStatusDt(currDt);
        System.out.println("@#$@#$@#objPaddyItemOpeningStockTO"+objPaddyItemOpeningStockTO);
        return objPaddyItemOpeningStockTO;
    }
    
    private void setPaddyItemOpeningStockTO(PaddyItemOpeningStockTO objPaddyItemOpeningStockTO) {
        
        setTxtItemCode(objPaddyItemOpeningStockTO.getTxtItemCode());
        setTxtItemDesc(objPaddyItemOpeningStockTO.getTxtItemDesc());
        setTxtPurchaseAcHd(objPaddyItemOpeningStockTO.getTxtPurchaseAcHd());
        setTxtPurchasePrice(objPaddyItemOpeningStockTO.getTxtPurchasePrice());
        setCboUnit(CommonUtil.convertObjToStr(getCbmUnit().getDataForKey(objPaddyItemOpeningStockTO.getCboUnit())));
        setTxtPurchaseReturnAcHd(objPaddyItemOpeningStockTO.getTxtPurchaseReturnAcHd());
        setTxtOrderLevel(objPaddyItemOpeningStockTO.getTxtOrderLevel());
        setTxtQty(objPaddyItemOpeningStockTO.getTxtQty());
        setTxtSalesAcHd(objPaddyItemOpeningStockTO.getTxtSalesAcHd());
        setTxtSalesReturnAcHd(objPaddyItemOpeningStockTO.getTxtSalesReturnAcHd());
        setTxtSellingPrice(objPaddyItemOpeningStockTO.getTxtSellingPrice());
        setTxtTaxAcHd(objPaddyItemOpeningStockTO.getTxtTaxAcHd());
    }
    
    public void  getData(HashMap whereMap){
        try{
            data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#$@#data : "+data+ " : "+whereMap+ " : "+map);
            if(data.containsKey("PaddyItemOpeningStockTO")){
                PaddyItemOpeningStockTO objPaddyItemOpeningStockTO = (PaddyItemOpeningStockTO)data.get("PaddyItemOpeningStockTO");
                System.out.println("@#$@#PaddyItemOpeningStockTO:"+objPaddyItemOpeningStockTO);
                setPaddyItemOpeningStockTO(objPaddyItemOpeningStockTO);
                ttNotifyObservers();
            }
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }

     public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
        setChanged();
        
    }
    
         /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
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
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    

    /**
     * Getter for property txtItemCode.
     * @return Value of property txtItemCode.
     */
    public java.lang.String getTxtItemCode() {
        return txtItemCode;
    }
    
    /**
     * Setter for property txtItemCode.
     * @param txtItemCode New value of property txtItemCode.
     */
    public void setTxtItemCode(java.lang.String txtItemCode) {
        this.txtItemCode = txtItemCode;
    }
    
    /**
     * Getter for property txtItemDesc.
     * @return Value of property txtItemDesc.
     */
    public java.lang.String getTxtItemDesc() {
        return txtItemDesc;
    }
    
    /**
     * Setter for property txtItemDesc.
     * @param txtItemDesc New value of property txtItemDesc.
     */
    public void setTxtItemDesc(java.lang.String txtItemDesc) {
        this.txtItemDesc = txtItemDesc;
    }
    
    /**
     * Getter for property txtPurchasePrice.
     * @return Value of property txtPurchasePrice.
     */
    public java.lang.String getTxtPurchasePrice() {
        return txtPurchasePrice;
    }
    
    /**
     * Setter for property txtPurchasePrice.
     * @param txtPurchasePrice New value of property txtPurchasePrice.
     */
    public void setTxtPurchasePrice(java.lang.String txtPurchasePrice) {
        this.txtPurchasePrice = txtPurchasePrice;
    }
    
    /**
     * Getter for property txtSellingPrice.
     * @return Value of property txtSellingPrice.
     */
    public java.lang.String getTxtSellingPrice() {
        return txtSellingPrice;
    }
    
    /**
     * Setter for property txtSellingPrice.
     * @param txtSellingPrice New value of property txtSellingPrice.
     */
    public void setTxtSellingPrice(java.lang.String txtSellingPrice) {
        this.txtSellingPrice = txtSellingPrice;
    }
    
    /**
     * Getter for property cboUnit.
     * @return Value of property cboUnit.
     */
    public java.lang.String getCboUnit() {
        return cboUnit;
    }
    
    /**
     * Setter for property cboUnit.
     * @param cboUnit New value of property cboUnit.
     */
    public void setCboUnit(java.lang.String cboUnit) {
        this.cboUnit = cboUnit;
    }
    
    /**
     * Getter for property txtQty.
     * @return Value of property txtQty.
     */
    public java.lang.String getTxtQty() {
        return txtQty;
    }
    
    /**
     * Setter for property txtQty.
     * @param txtQty New value of property txtQty.
     */
    public void setTxtQty(java.lang.String txtQty) {
        this.txtQty = txtQty;
    }
    
    /**
     * Getter for property txtOrderLevel.
     * @return Value of property txtOrderLevel.
     */
    public java.lang.String getTxtOrderLevel() {
        return txtOrderLevel;
    }
    
    /**
     * Setter for property txtOrderLevel.
     * @param txtOrderLevel New value of property txtOrderLevel.
     */
    public void setTxtOrderLevel(java.lang.String txtOrderLevel) {
        this.txtOrderLevel = txtOrderLevel;
    }
    
    /**
     * Getter for property txtPurchaseAcHd.
     * @return Value of property txtPurchaseAcHd.
     */
    public java.lang.String getTxtPurchaseAcHd() {
        return txtPurchaseAcHd;
    }
    
    /**
     * Setter for property txtPurchaseAcHd.
     * @param txtPurchaseAcHd New value of property txtPurchaseAcHd.
     */
    public void setTxtPurchaseAcHd(java.lang.String txtPurchaseAcHd) {
        this.txtPurchaseAcHd = txtPurchaseAcHd;
    }
    
    /**
     * Getter for property txtPurchaseReturnAcHd.
     * @return Value of property txtPurchaseReturnAcHd.
     */
    public java.lang.String getTxtPurchaseReturnAcHd() {
        return txtPurchaseReturnAcHd;
    }
    
    /**
     * Setter for property txtPurchaseReturnAcHd.
     * @param txtPurchaseReturnAcHd New value of property txtPurchaseReturnAcHd.
     */
    public void setTxtPurchaseReturnAcHd(java.lang.String txtPurchaseReturnAcHd) {
        this.txtPurchaseReturnAcHd = txtPurchaseReturnAcHd;
    }
    
    /**
     * Getter for property txtSalesAcHd.
     * @return Value of property txtSalesAcHd.
     */
    public java.lang.String getTxtSalesAcHd() {
        return txtSalesAcHd;
    }
    
    /**
     * Setter for property txtSalesAcHd.
     * @param txtSalesAcHd New value of property txtSalesAcHd.
     */
    public void setTxtSalesAcHd(java.lang.String txtSalesAcHd) {
        this.txtSalesAcHd = txtSalesAcHd;
    }
    
    /**
     * Getter for property txtSalesReturnAcHd.
     * @return Value of property txtSalesReturnAcHd.
     */
    public java.lang.String getTxtSalesReturnAcHd() {
        return txtSalesReturnAcHd;
    }
    
    /**
     * Setter for property txtSalesReturnAcHd.
     * @param txtSalesReturnAcHd New value of property txtSalesReturnAcHd.
     */
    public void setTxtSalesReturnAcHd(java.lang.String txtSalesReturnAcHd) {
        this.txtSalesReturnAcHd = txtSalesReturnAcHd;
    }
    
    /**
     * Getter for property txtTaxAcHd.
     * @return Value of property txtTaxAcHd.
     */
    public java.lang.String getTxtTaxAcHd() {
        return txtTaxAcHd;
    }
    
    /**
     * Setter for property txtTaxAcHd.
     * @param txtTaxAcHd New value of property txtTaxAcHd.
     */
    public void setTxtTaxAcHd(java.lang.String txtTaxAcHd) {
        this.txtTaxAcHd = txtTaxAcHd;
    }
    
    /**
     * Getter for property cbmUnit.
     * @return Value of property cbmUnit.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmUnit() {
        return cbmUnit;
    }
    
    /**
     * Setter for property cbmUnit.
     * @param cbmUnit New value of property cbmUnit.
     */
    public void setCbmUnit(com.see.truetransact.clientutil.ComboBoxModel cbmUnit) {
        this.cbmUnit = cbmUnit;
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
    
}