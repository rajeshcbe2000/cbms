  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * RTGSOtherBankBranchOB.java
   *
   * Created on November 06, 2015, 04:45 PM
   * @author Suresh R
   */

package com.see.truetransact.ui.product.remittance;

import java.util.Observable;
import java.util.HashMap;
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
import com.see.truetransact.transferobject.product.remittance.RTGSOtherBankBranchTo;
/**
 *
 * @author Suresh R
 *
 **/
public class RTGSOtherBankBranchOB extends Observable{
    
    private String txtBankCode = "";
    private String txtIFSCCode = "";
    private String txtMICRCode = "";
    private String txtBranchName = "";
    private String txtAreaAddress = "";
    private String txtContactNo = "";
    private String txtCity = "";
    private String txtDistrict = "";
    private String txtState = "";
    
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblRTGSOtherBankBranchDetails;
    
    private boolean newData = false;
    private LinkedHashMap RTGSMap;
    private LinkedHashMap deletedRTGSMap;
    
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(RTGSOtherBankBranchOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private int actionType;
    private RTGSOtherBankBranchTo objRTGSOtherBankBranchTo = null;
    
    
    /** Creates a new instance of RTGSOtherBankBranchOB */
    public RTGSOtherBankBranchOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "RTGSOtherBankBranchJNDI");
            map.put(CommonConstants.HOME, "RTGSOtherBankBranchJNDIHome");
            map.put(CommonConstants.REMOTE, "RTGSOtherBankBranch");
            setTableTile();
            tblRTGSOtherBankBranchDetails= new EnhancedTableModel(null, tableTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("IFSC Code");
        tableTitle.add("Branch Name");
        tableTitle.add("City");
        tableTitle.add("Auth Status");
        IncVal = new ArrayList();
    }
    
    private void fillDropdown() throws Exception{
        try{
            HashMap param = new HashMap();
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
    
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            //System.out.println("#@@%@@#%@#data"+data);
            if(data.containsKey("RTGSTO_DATA")){
                RTGSMap = (LinkedHashMap)data.get("RTGSTO_DATA");
                ArrayList addList =new ArrayList(RTGSMap.keySet());
                for(int i=0;i<addList.size();i++){
                    RTGSOtherBankBranchTo  objRTGSOtherBankBranchTo = (RTGSOtherBankBranchTo)  RTGSMap.get(addList.get(i));
                    objRTGSOtherBankBranchTo.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtIFSCCode()));
                    incTabRow.add(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtBranchName()));
                    incTabRow.add(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtCity()));
                    incTabRow.add(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getAuthorizedStatus()));
                    tblRTGSOtherBankBranchDetails.addRow(incTabRow);
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private String getCommand(){
        String command = null;
        //System.out.println("actionType : " + actionType);
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
    
    
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final RTGSOtherBankBranchTo objRTGSOtherBankBranchTo = new RTGSOtherBankBranchTo();
            if( RTGSMap== null ){
                RTGSMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objRTGSOtherBankBranchTo.setStatusDt(ClientUtil.getCurrentDate());
                    objRTGSOtherBankBranchTo.setStatusBy(TrueTransactMain.USER_ID);
                    objRTGSOtherBankBranchTo.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objRTGSOtherBankBranchTo.setStatusDt(ClientUtil.getCurrentDate());
                    objRTGSOtherBankBranchTo.setStatusBy(TrueTransactMain.USER_ID);
                    objRTGSOtherBankBranchTo.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objRTGSOtherBankBranchTo.setStatusDt(ClientUtil.getCurrentDate());
                objRTGSOtherBankBranchTo.setStatusBy(TrueTransactMain.USER_ID);
                objRTGSOtherBankBranchTo.setStatus(CommonConstants.STATUS_CREATED);
            }
            
            if(isNewData()){
                ArrayList data = tblRTGSOtherBankBranchDetails.getDataArrayList();
            }
            objRTGSOtherBankBranchTo.setTxtBankCode(CommonUtil.convertObjToInt(getTxtBankCode()));
            objRTGSOtherBankBranchTo.setTxtIFSCCode(getTxtIFSCCode());
            objRTGSOtherBankBranchTo.setTxtMICRCode(getTxtMICRCode());
            objRTGSOtherBankBranchTo.setTxtBranchName(getTxtBranchName());
            objRTGSOtherBankBranchTo.setTxtAreaAddress(getTxtAreaAddress());
            objRTGSOtherBankBranchTo.setTxtContactNo(getTxtContactNo());
            objRTGSOtherBankBranchTo.setTxtCity(getTxtCity());
            objRTGSOtherBankBranchTo.setTxtDistrict(getTxtDistrict());
            objRTGSOtherBankBranchTo.setTxtState(getTxtState());
            RTGSMap.put(objRTGSOtherBankBranchTo.getTxtIFSCCode(),objRTGSOtherBankBranchTo);
            updateTableDetails(rowSel,objRTGSOtherBankBranchTo);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateTableDetails(int rowSel, RTGSOtherBankBranchTo objRTGSOtherBankBranchTo)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblRTGSOtherBankBranchDetails.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblRTGSOtherBankBranchDetails.getDataArrayList().get(j)).get(0);
            if(objRTGSOtherBankBranchTo.getTxtIFSCCode().equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblRTGSOtherBankBranchDetails.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(getTxtIFSCCode());
                IncParRow.add(getTxtBranchName());
                IncParRow.add(getTxtCity());
                IncParRow.add("");
                tblRTGSOtherBankBranchDetails.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if (!rowExists) {
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(getTxtIFSCCode());
            IncParRow.add(getTxtBranchName());
            IncParRow.add(getTxtCity());
            IncParRow.add("");
            tblRTGSOtherBankBranchDetails.insertRow(tblRTGSOtherBankBranchDetails.getRowCount(), IncParRow);
            IncParRow = null;
        }
    }
    
    public void populateRTGSDetails(String row){
        try{
            resetRTGSDetails();
            final RTGSOtherBankBranchTo objRTGSOtherBankBranchTo = (RTGSOtherBankBranchTo)RTGSMap.get(row);
            populateTableData(objRTGSOtherBankBranchTo);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteTableData(String val, int row){
        if(deletedRTGSMap== null){
            deletedRTGSMap = new LinkedHashMap();
        }
        RTGSOtherBankBranchTo objRTGSOtherBankBranchTo = (RTGSOtherBankBranchTo) RTGSMap.get(val);
        objRTGSOtherBankBranchTo.setStatus(CommonConstants.STATUS_DELETED);
        deletedRTGSMap.put(CommonUtil.convertObjToStr(tblRTGSOtherBankBranchDetails.getValueAt(row,0)),RTGSMap.get(val));
        Object obj;
        obj=val;
        RTGSMap.remove(val);
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
        incDataList = new ArrayList(RTGSMap.keySet());
        ArrayList addList =new ArrayList(RTGSMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            RTGSOtherBankBranchTo objRTGSOtherBankBranchTo = (RTGSOtherBankBranchTo) RTGSMap.get(addList.get(i));
            IncVal.add(objRTGSOtherBankBranchTo);
            if(!objRTGSOtherBankBranchTo.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtIFSCCode()));
                incTabRow.add(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtBranchName()));
                incTabRow.add(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtCity()));
                incTabRow.add(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getAuthorizedStatus()));
                tblRTGSOtherBankBranchDetails.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    private void populateTableData(RTGSOtherBankBranchTo objRTGSOtherBankBranchTo) throws Exception {
        setTxtBankCode(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtBankCode()));
        setTxtIFSCCode(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtIFSCCode()));
        setTxtMICRCode(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtMICRCode()));
        setTxtBranchName(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtBranchName()));
        setTxtAreaAddress(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtAreaAddress()));
        setTxtContactNo(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtContactNo()));
        setTxtCity(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtCity()));
        setTxtDistrict(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtDistrict()));
        setTxtState(CommonUtil.convertObjToStr(objRTGSOtherBankBranchTo.getTxtState()));
        setChanged();
        notifyObservers();
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
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() == null) {
            if (RTGSMap != null && RTGSMap.size() > 0) {
                data.put("RTGSTableDetails", RTGSMap);
            }
            if (deletedRTGSMap != null && deletedRTGSMap.size() > 0) {
                data.put("deletedRTGSTableDetails", deletedRTGSMap);
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        data.put("OTHER_BANK_CODE", CommonUtil.convertObjToInt(getTxtBankCode()));
        data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        //System.out.println("### Data in RTGS OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        _authorizeMap = null;
        setResult(getActionType());
    }
    
    
    
    public void resetForm(){
        RTGSMap = null;
        deletedRTGSMap= null;
        resetTableValues();
        txtBankCode = "";
        resetRTGSDetails();
        setChanged();
    }
    
    public void resetRTGSDetails() {
        txtIFSCCode = "";
        txtMICRCode = "";
        txtBranchName = "";
        txtAreaAddress = "";
        txtContactNo = "";
        txtCity = "";
        txtDistrict = "";
        txtState = "";
    }

    public void resetTableValues(){
        tblRTGSOtherBankBranchDetails.setDataArrayList(null,tableTitle);
    }
    /**
     * Getter for property tblRTGSOtherBankBranchDetails.
     * @return Value of property tblRTGSOtherBankBranchDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblRTGSOtherBankBranchDetails() {
        return tblRTGSOtherBankBranchDetails;
    }
    
    /**
     * Setter for property tblRTGSOtherBankBranchDetails.
     * @param tblRTGSOtherBankBranchDetails New value of property tblRTGSOtherBankBranchDetails.
     */
    public void setTblRTGSOtherBankBranchDetails(com.see.truetransact.clientutil.EnhancedTableModel tblRTGSOtherBankBranchDetails) {
        this.tblRTGSOtherBankBranchDetails = tblRTGSOtherBankBranchDetails;
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
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    public String getTxtBankCode() {
        return txtBankCode;
    }

    public void setTxtBankCode(String txtBankCode) {
        this.txtBankCode = txtBankCode;
    }

    public String getTxtIFSCCode() {
        return txtIFSCCode;
    }

    public void setTxtIFSCCode(String txtIFSCCode) {
        this.txtIFSCCode = txtIFSCCode;
    }

    public String getTxtMICRCode() {
        return txtMICRCode;
    }

    public void setTxtMICRCode(String txtMICRCode) {
        this.txtMICRCode = txtMICRCode;
    }

    public String getTxtBranchName() {
        return txtBranchName;
    }

    public void setTxtBranchName(String txtBranchName) {
        this.txtBranchName = txtBranchName;
    }

    public String getTxtAreaAddress() {
        return txtAreaAddress;
    }

    public void setTxtAreaAddress(String txtAreaAddress) {
        this.txtAreaAddress = txtAreaAddress;
    }

    public String getTxtContactNo() {
        return txtContactNo;
    }

    public void setTxtContactNo(String txtContactNo) {
        this.txtContactNo = txtContactNo;
    }

    public String getTxtCity() {
        return txtCity;
    }

    public void setTxtCity(String txtCity) {
        this.txtCity = txtCity;
    }

    public String getTxtDistrict() {
        return txtDistrict;
    }

    public void setTxtDistrict(String txtDistrict) {
        this.txtDistrict = txtDistrict;
    }

    public String getTxtState() {
        return txtState;
    }

    public void setTxtState(String txtState) {
        this.txtState = txtState;
    }
    
    
}