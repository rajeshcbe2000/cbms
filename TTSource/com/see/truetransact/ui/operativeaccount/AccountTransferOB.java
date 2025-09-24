/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AccountTransferOB.java
 *
 * Created on August 12, 2003, 6:58 PM
 */

package com.see.truetransact.ui.operativeaccount;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.apache.log4j.Logger;

/**
 *
 * @author  annamalai
 */
public class AccountTransferOB extends Observable {
    private String _cboProductId = "";
    private ComboBoxModel _cbmProductId;
    private String _lblAccountHeadDisplay;
    private boolean _selectAll = false;  
    private String _lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private int _actionType;
    private int _result;
    
//    private final AccountTransferRB _accountTransferRB = new AccountTransferRB();
    java.util.ResourceBundle _accountTransferRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.AccountTransferRB", ProxyParameters.LANGUAGE);
    
    private HashMap _operationMap;
    private ArrayList _key ;
    private ArrayList _value ;
    private ProxyFactory _proxy;
    
    private com.see.truetransact.clientutil.TableModel tblAccountTransferModel = new com.see.truetransact.clientutil.TableModel();
    
    private static AccountTransferOB _objAccountTransferOB; // singleton object
    
    private final static Logger _log = Logger.getLogger(AccountTransferOB.class);    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    static {
        try {
            _log.info("Creating AccountTransferOB...");
            _objAccountTransferOB = new AccountTransferOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }    
    
    /** Creates a new instance of AccountTransferOB */
    private AccountTransferOB() throws Exception{
        _proxy = ProxyFactory.createProxy();
        setTblAccountTransferModel(new com.see.truetransact.clientutil.TableModel(null, getTableHeader()));
        setOperationMap();
        fillDropdown();
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{        
        _operationMap = new HashMap();
        _operationMap.put(CommonConstants.JNDI, "AccountTransferJNDI");
        _operationMap.put(CommonConstants.HOME, "operativeaccount.AccountTransferHome");
        _operationMap.put(CommonConstants.REMOTE, "operativeaccount.AccountTransfer");
    }
    
    private void fillDropdown() throws Exception{
        final HashMap lookUpHash = new HashMap();
        final HashMap keyValue;
        
        lookUpHash.put(CommonConstants.MAP_NAME,"getAccProducts");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
        _cbmProductId = new ComboBoxModel(_key,_value);
        makeNull();
    }
    
    private void getKeyValue(HashMap keyValue) throws Exception{
        _key = (ArrayList)keyValue.get(CommonConstants.KEY);
        _value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /* To make certain class variables null*/
    private void makeNull(){
        _key = null;
        _value = null;
    }
    
    /**
     * Returns an instance of AccountTransferOB.
     * @return  AccountTransferOB
     */
    public static AccountTransferOB getInstance() {
        return _objAccountTransferOB;
    }    
    
    /** To retrive Account Head details based on Product Id */
    public void getAccountHeadForProduct() {
        try {
            final HashMap accountHeadMap = new HashMap();
            accountHeadMap.put("PROD_ID",getCboProductId());
            final List resultList = ClientUtil.executeQuery("getAccountHeadForProduct", accountHeadMap);
            final HashMap resultMap = (HashMap)resultList.get(0);
            setLblAccountHeadDisplay(CommonUtil.convertObjToStr(new StringBuffer(CommonUtil.convertObjToStr(resultMap.get("AC_HEAD"))).append(" ").append(resultMap.get("AC_HEAD_DESC"))));
            _log.info("resultMap.get(AC_HEAD):"+resultMap.get("AC_HEAD"));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private ArrayList getTableHeader() throws Exception{
        //AccountTransferRB accountTransferRB = new AccountTransferRB();
        final ArrayList header = new ArrayList();
        header.add(_accountTransferRB.getString("tblColumn1"));
        header.add(_accountTransferRB.getString("tblColumn2"));
        header.add(_accountTransferRB.getString("tblColumn3"));
        header.add(_accountTransferRB.getString("tblColumn4"));
        header.add(_accountTransferRB.getString("tblColumn5"));
        header.add(_accountTransferRB.getString("tblColumn6"));
        
        return header;
    }
        
    public void fillTable(HashMap whereMap) {
        try {
            System.out.println("@#@#@#@#@ where map : "+whereMap);
            final HashMap data = _proxy.executeQuery(whereMap, _operationMap);
            populateTable(data);
        } catch( Exception e ) {
            //e.printStackTrace();
            parseException.logException(e,true);
        }
        finally{
           ttNotifyObservers();
        }
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void setCbmProductId(ComboBoxModel cbmProductId){
        this._cbmProductId = cbmProductId;
        setChanged();        
    }  
    public ComboBoxModel getCbmProductId(){
        return this._cbmProductId;
    }
    
    public void setCboProductId(String cboProductId){
        this._cboProductId = cboProductId;
        setChanged();
    }
    String getCboProductId(){
        return this._cboProductId;
    }
    
    public void setLblAccountHeadDisplay(String lblAccountHeadDisplay){
        this._lblAccountHeadDisplay = lblAccountHeadDisplay;
        setChanged();
    }
    public String getLblAccountHeadDisplay(){
        return this._lblAccountHeadDisplay;
    }
    
    public void setTblAccountTransferModel(com.see.truetransact.clientutil.TableModel tblAccountTransferModel){
        this.tblAccountTransferModel = tblAccountTransferModel;
        setChanged();
    }    
    com.see.truetransact.clientutil.TableModel getTblAccountTransferModel(){
        return this.tblAccountTransferModel;
    }
    
    public void setLblStatus(String lblStatus) {
        _lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return _lblStatus;
    }    
      
    public void setActionType(int actionType) {
        _actionType = actionType;
        setChanged();
    }
    public int getActionType(){
        return _actionType;
    } 
    
    public void setResult(int result) {
        _result = result;
        setChanged();
    }    
    public int getResult(){
        return _result;
    }   
    
    public void resetForm(){
        resetCbmProductId();
        resetAccTransferData();
        resetAccountTransferTable();
        ttNotifyObservers();
    }
    
    public void resetCbmProductId(){
        this._cbmProductId.setSelectedItem(_cbmProductId.getElementAt(0));
    }
    
    public void resetAccTransferData(){
        this.setLblAccountHeadDisplay("");
        this.setChkSelectAll(false);
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public void resetAccountTransferTable(){
        try{
            final int rowCount = tblAccountTransferModel.getRowCount();
            for(int i = 0; i < rowCount; i++){
                tblAccountTransferModel.removeRow(0);
            }
        }catch(Exception e){            
            parseException.logException(e,true);
        }
    }
    
    public void populateTable(HashMap hash){
        try{
            final LinkedHashMap data = (LinkedHashMap)hash.get("AccountTransferDetails");
//            System.out.println("@@@@ AccountTransferDetails : "+data);
            final Object[] keyList = (data.keySet()).toArray();
            ArrayList rowList;
            final ArrayList allRows = new ArrayList();
            HashMap individualRow;
            final int keyListLength = keyList.length;
            System.out.println("keylist length = " + keyListLength);

            for (int i = 0;i<keyListLength;i++){
                individualRow = (HashMap)data.get((String)(keyList[i]));
//                if (i<=5) {
                    System.out.println("!!## individualRow:" + individualRow);
//                }
                rowList = new ArrayList();
                rowList.add(new Boolean(_selectAll));
                rowList.add(individualRow.get(_accountTransferRB.getString("tblColumn2").toUpperCase()));
                rowList.add(individualRow.get(_accountTransferRB.getString("tblColumn3").toUpperCase()));
                rowList.add(individualRow.get(_accountTransferRB.getString("tblColumn4").toUpperCase()));
                rowList.add(individualRow.get(_accountTransferRB.getString("tblColumn5").toUpperCase()));
                rowList.add(individualRow.get(_accountTransferRB.getString("tblColumn6").toUpperCase()));
                allRows.add(rowList);
                rowList = null;
                individualRow = null;
            }

            /* overriding the isCellEditable() method, because the default implementation
             * allows editing of all the cells, but in this scenario we need to edit only
             * the first cell, that has the Check box
             */
            setTblAccountTransferModel(new com.see.truetransact.clientutil.TableModel(allRows, getTableHeader()) {
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    if (mColIndex == 0) {
                        return true;
                    }
                    return false;
                }
            });
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    public void setChkSelectAll(boolean selectAll) {
        this._selectAll = selectAll;
        setChanged();
        ttNotifyObservers();
    }
    public boolean getChkSelectAll() {
        return _selectAll;
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        _log.info("OB/getLblStatus:"+getLblStatus());
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
        ttNotifyObservers();
    }
    
    public void doAction() {
        try {
            final HashMap actTransferMap = new HashMap();
            final HashMap statusMap=new HashMap();
            final ArrayList actNumList = new ArrayList();
//            final ArrayList statusList = new ArrayList();
            final int rowCount = tblAccountTransferModel.getRowCount();
            for (int i = 0;i<rowCount;i++){
                if (((Boolean)tblAccountTransferModel.getValueAt(i, 0)).booleanValue()){
                    
                    actNumList.add(tblAccountTransferModel.getValueAt(i, 1));
                    statusMap.put(tblAccountTransferModel.getValueAt(i, 1),tblAccountTransferModel.getValueAt(i, 5));
//                    statusList.add(statusMap);
                }
            }
            actTransferMap.put("ACT_NUMBER",actNumList);
            actTransferMap.put("ACT_STATUS",statusMap);
            HashMap proxyResultMap = _proxy.execute(actTransferMap,_operationMap);
            setResult(_actionType);
            _actionType = ClientConstants.ACTIONTYPE_CANCEL;
            resetForm();
        } catch (Exception e) {
            parseException.logException(e,true);
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        }
    }
}