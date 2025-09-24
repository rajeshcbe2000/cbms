/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanChargesOB.java
 *
 * Created on Mon Aug 29 13:22:57 IST 2011
 */

package com.see.truetransact.ui.supporting.arccharges;

import java.util.Observable;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.supporting.arccharges.ArcChargesTO;
import com.see.truetransact.transferobject.supporting.arccharges.ArcSlabChargesTO;
import java.util.Date;
/**
 *
 * @author
 */

public class ARCChargesOB extends Observable{
    
    private String txtToSlabAmount = "";
    private String txtFlatCharges = "";
    private String txtDivisibleBy = "";
    private String cboChargeBase = "";
    private String cboChargeType = "";
    private String txtChargeId = "";
    private String txtChargeDesc = "";
    private String txtFromSlabAmount = "";
    private String txtMinimumChrgAmt = "";
    private String txtMaximumChrgAmt = "";
    private String txtChargeRate = "";
    private String cboRoundOffType = "";
    private String cboSchemeId = "";
    private String txtAccGroupId = "";
    private ComboBoxModel cbmRoundOffType;
    private ComboBoxModel cbmChargeBase;
    private ComboBoxModel cbmProductId;
    private ComboBoxModel cbmChargeType;
    
    private String rdoDeductionAcc = "";
    private String rdoMandatory = "";
    
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(ARCChargesOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private ArcChargesTO objLoanChargeTO;
    private ArcSlabChargesTO objLoanSlabChargesTO;
    private boolean slabAmountData = false;
    private LinkedHashMap slabAmountMap;
    private LinkedHashMap deletedSlabAmountMap;
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblCharge;
    
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private Date currDt = null;
    /** Creates a new instance of TDS MiantenanceOB */
    public ARCChargesOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ARCChargesJNDI");
            map.put(CommonConstants.HOME, "ARCChargesJNDIHome");
            map.put(CommonConstants.REMOTE, "ARCCharges");
            setTableTile();
            tblCharge = new EnhancedTableModel(null, tableTitle);
            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void setTableTile() throws Exception{
        tableTitle.add("SL NO");
        tableTitle.add("FromAmount");
        tableTitle.add("ToAmount");
        IncVal = new ArrayList();
    }
    
    private void fillDropdown() throws Exception{
        try{
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.MAP_NAME,null);
            ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("OPERATIVEACCTPRODUCT.ROUNDOFF");
            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.ROUNDOFF"));
            cbmRoundOffType = new ComboBoxModel(key,value);
            makeNull();
            
            lookup_keys = new ArrayList();
            lookup_keys.add("CHARGE BASE");
            lookup_keys.add("TERM_LOAN.CASE_TYPE");
            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("CHARGE BASE"));
            cbmChargeBase = new ComboBoxModel(key,value);
            makeNull();
            
            getKeyValue((HashMap)keyValue.get("TERM_LOAN.CASE_TYPE"));
            cbmChargeType = new ComboBoxModel(key,value);
            makeNull();
            
            
            LinkedHashMap transactionMap = new LinkedHashMap();
            HashMap retrieve = new HashMap();
            ArrayList keyList = new ArrayList();
            keyList.add("");
            ArrayList valList = new ArrayList();
            valList.add("");
            List resultList = ClientUtil.executeQuery("InterMaintenance.getProductDataTL", transactionMap);
            for (int i = resultList.size() - 1, j = 0;i >= 0;--i,++j){
                // If the result contains atleast one record
                retrieve = (HashMap) resultList.get(j);
                keyList.add(retrieve.get("PROD_ID"));
                valList.add(retrieve.get("PROD_DESC"));
            }
            cbmProductId = new ComboBoxModel(keyList, valList);
            transactionMap = null;
            resultList = null;
            keyList = null;
            valList = null;
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
        if(get_authorizeMap() == null){
            data.put("ARCChargesDetails",setArcChargeDetailsData());
            if(slabAmountMap!=null && slabAmountMap.size()>0 ){
                data.put("SlabAmountTableDetails",slabAmountMap);
            }
            if(deletedSlabAmountMap!=null && deletedSlabAmountMap.size()>0 ){
                data.put("deletedSlabAmountTableDetails",deletedSlabAmountMap);
            }
        }
        else{
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in FixedAssets OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        //        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }
    
   
    
    /** To populate data into the screen */
    public ArcChargesTO setArcChargeDetailsData() {
        
        final ArcChargesTO objLoanChargeTO = new ArcChargesTO();
        try{
            objLoanChargeTO.setChargeId(getTxtChargeId());
            objLoanChargeTO.setChargeType(CommonUtil.convertObjToStr(cbmChargeType.getKeyForSelected()));
            objLoanChargeTO.setDeductionAccu(getRdoDeductionAcc());
            objLoanChargeTO.setChargeBase(getCboChargeBase());
            objLoanChargeTO.setFromSlabAmt(getTxtFromSlabAmount());
            objLoanChargeTO.setToSlabAmt(getTxtToSlabAmount());
            objLoanChargeTO.setFlatCharge(getTxtFlatCharges());
            objLoanChargeTO.setChargeRate(getTxtChargeRate());
            objLoanChargeTO.setDivisibleBy(getTxtDivisibleBy());
            objLoanChargeTO.setRoundOffType(getCboRoundOffType());
            objLoanChargeTO.setMinChargeAmount(getTxtMinimumChrgAmt());
            objLoanChargeTO.setMaxChargeAmount(getTxtMaximumChrgAmt());
            objLoanChargeTO.setStatus(getAction());
            objLoanChargeTO.setStatusBy(TrueTransactMain.USER_ID);
            objLoanChargeTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            
        }catch(Exception e){
            log.info("Error In setLoanChargeDetailsData()");
            e.printStackTrace();
        }
        return objLoanChargeTO;
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            objLoanChargeTO = (ArcChargesTO) ((List) data.get("ChargeDetailsTO")).get(0);
            populateChargeDetailsData(objLoanChargeTO);
            if(data.containsKey("SlabAmountListTO")){
                slabAmountMap= (LinkedHashMap)data.get("SlabAmountListTO");
                ArrayList addList =new ArrayList(slabAmountMap.keySet());
                for(int i=0;i<addList.size();i++){
                    ArcSlabChargesTO  objLoanSlabChargesTO = (ArcSlabChargesTO)  slabAmountMap.get(addList.get(i));
                    objLoanSlabChargesTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getSlNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getFromSlabAmt()));
                    incTabRow.add(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getToSlabAmt()));
                    tblCharge.addRow(incTabRow);
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateChargeDetailsData(ArcChargesTO objLoanChargeTO) throws Exception{
        this.setTxtChargeId(CommonUtil.convertObjToStr(objLoanChargeTO.getChargeId()));
//        cbmChargeType.setKeyForSelected(CommonUtil.convertObjToStr(objLoanChargeTO.getChargeType()));
        this.setCboChargeType(CommonUtil.convertObjToStr(objLoanChargeTO.getChargeType()));
        this.setRdoDeductionAcc(CommonUtil.convertObjToStr(objLoanChargeTO.getDeductionAccu()));
        this.setCboChargeBase(CommonUtil.convertObjToStr(objLoanChargeTO.getChargeBase()));
        this.setTxtFromSlabAmount(CommonUtil.convertObjToStr(objLoanChargeTO.getFromSlabAmt()));
        this.setTxtToSlabAmount(CommonUtil.convertObjToStr(objLoanChargeTO.getToSlabAmt()));
        this.setTxtFlatCharges(CommonUtil.convertObjToStr(objLoanChargeTO.getFlatCharge()));
        this.setTxtChargeRate(CommonUtil.convertObjToStr(objLoanChargeTO.getChargeRate()));
        this.setTxtDivisibleBy(CommonUtil.convertObjToStr(objLoanChargeTO.getDivisibleBy()));
        this.setCboRoundOffType(CommonUtil.convertObjToStr(objLoanChargeTO.getRoundOffType()));
        this.setTxtMinimumChrgAmt(CommonUtil.convertObjToStr(objLoanChargeTO.getMinChargeAmount()));
        this.setTxtMaximumChrgAmt(CommonUtil.convertObjToStr(objLoanChargeTO.getMaxChargeAmount()));
        setChanged();
        notifyObservers();
    }
    
    public void addSlabAmountTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final ArcSlabChargesTO objLoanSlabChargesTO = new ArcSlabChargesTO();
            if( slabAmountMap == null ){
                slabAmountMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isSlabAmountData()){
                    objLoanSlabChargesTO.setStatusDt(currDt);
                    objLoanSlabChargesTO.setStatusBy(TrueTransactMain.USER_ID);
                    objLoanSlabChargesTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objLoanSlabChargesTO.setStatusDt(currDt);
                    objLoanSlabChargesTO.setStatusBy(TrueTransactMain.USER_ID);
                    objLoanSlabChargesTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            }else{
                objLoanSlabChargesTO.setStatusDt(currDt);
                objLoanSlabChargesTO.setStatusBy(TrueTransactMain.USER_ID);
                objLoanSlabChargesTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            int  slno=0;
            int nums[]= new int[150];
            int max=nums[0];
            if(!updateMode){
                ArrayList data = tblCharge.getDataArrayList();
                slno=serialNo(data);
            }
            else{
                if(isSlabAmountData()){
                    ArrayList data = tblCharge.getDataArrayList();
                    slno=serialNo(data);
                }
                else{
                    int b=CommonUtil.convertObjToInt(tblCharge.getValueAt(rowSelected,0));
                    slno=b;
                }
            }
            objLoanSlabChargesTO.setSlNo(String.valueOf(slno));
            objLoanSlabChargesTO.setChargeId(getTxtChargeId());
            objLoanSlabChargesTO.setFromSlabAmt(getTxtFromSlabAmount());
            objLoanSlabChargesTO.setToSlabAmt(getTxtToSlabAmount());
            objLoanSlabChargesTO.setChargeRate(getTxtChargeRate());
            objLoanSlabChargesTO.setDivisibleBy(getTxtDivisibleBy());
            objLoanSlabChargesTO.setRoundOffType(getCboRoundOffType());
            objLoanSlabChargesTO.setMinChargeAmount(getTxtMinimumChrgAmt());
            objLoanSlabChargesTO.setMaxChargeAmount(getTxtMaximumChrgAmt());
            slabAmountMap.put(objLoanSlabChargesTO.getSlNo(),objLoanSlabChargesTO);
            String sno=String.valueOf(slno);
            updateSlabAmountDetails(rowSel,sno,objLoanSlabChargesTO);
//            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateSlabAmountDetails(int rowSel, String sno, ArcSlabChargesTO objLoanSlabChargesTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblCharge.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblCharge.getDataArrayList().get(j)).get(0);
            if(sno.equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblCharge.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(sno);
                IncParRow.add(getTxtFromSlabAmount());
                IncParRow.add(getTxtToSlabAmount());
                tblCharge.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(sno);
            IncParRow.add(getTxtFromSlabAmount());
            IncParRow.add(getTxtToSlabAmount());
            tblCharge.insertRow(tblCharge.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    public int serialNo(ArrayList data){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(tblCharge.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    
    public void populateSlabDetails(String row){
        try{
            resetSlabDetails();
            final ArcSlabChargesTO objLoanSlabChargesTO = (ArcSlabChargesTO)slabAmountMap.get(row);
            populateTableData(objLoanSlabChargesTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTableData(ArcSlabChargesTO objLoanSlabChargesTO)  throws Exception{
        setTxtFromSlabAmount(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getFromSlabAmt()));
        setTxtToSlabAmount(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getToSlabAmt()));
        setTxtChargeRate(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getChargeRate()));
        setTxtDivisibleBy(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getDivisibleBy()));
        setCboRoundOffType(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getRoundOffType()));
        setTxtMinimumChrgAmt(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getMinChargeAmount()));
        setTxtMaximumChrgAmt(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getMaxChargeAmount()));
        setChanged();
        notifyObservers();
    }
    
    public void deleteTableData(String val, int row){
        if(deletedSlabAmountMap== null){
            deletedSlabAmountMap = new LinkedHashMap();
        }
        ArcSlabChargesTO objLoanSlabChargesTO = (ArcSlabChargesTO) slabAmountMap.get(val);
        objLoanSlabChargesTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedSlabAmountMap.put(CommonUtil.convertObjToStr(tblCharge.getValueAt(row,0)),slabAmountMap.get(val));
        Object obj;
        obj=val;
        slabAmountMap.remove(val);
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
        incDataList = new ArrayList(slabAmountMap.keySet());
        ArrayList addList =new ArrayList(slabAmountMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            ArcSlabChargesTO objLoanSlabChargesTO = (ArcSlabChargesTO) slabAmountMap.get(addList.get(i));
            IncVal.add(objLoanSlabChargesTO);
            if(!objLoanSlabChargesTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getSlNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getFromSlabAmt()));
                incTabRow.add(CommonUtil.convertObjToStr(objLoanSlabChargesTO.getToSlabAmt()));
                tblCharge.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    public void resetSlabDetails() {
        setTxtFromSlabAmount("");
        setTxtToSlabAmount("");
        setTxtChargeRate("");
        setTxtDivisibleBy("");
        setCboRoundOffType("");
        setTxtMaximumChrgAmt("");
        setTxtMinimumChrgAmt("");
        setChanged();
//        ttNotifyObservers();
    }
    
    public void resetTableValues(){
        tblCharge.setDataArrayList(null,tableTitle);
    }
    
    public void resetForm(){
        slabAmountMap = null;
        deletedSlabAmountMap = null;
    }
    
    // Setter method for txtToSlabAmount
    void setTxtToSlabAmount(String txtToSlabAmount){
        this.txtToSlabAmount = txtToSlabAmount;
        setChanged();
    }
    // Getter method for txtToSlabAmount
    String getTxtToSlabAmount(){
        return this.txtToSlabAmount;
    }
    
    // Setter method for txtFlatCharges
    void setTxtFlatCharges(String txtFlatCharges){
        this.txtFlatCharges = txtFlatCharges;
        setChanged();
    }
    // Getter method for txtFlatCharges
    String getTxtFlatCharges(){
        return this.txtFlatCharges;
    }
    
    // Setter method for txtDivisibleBy
    void setTxtDivisibleBy(String txtDivisibleBy){
        this.txtDivisibleBy = txtDivisibleBy;
        setChanged();
    }
    // Getter method for txtDivisibleBy
    String getTxtDivisibleBy(){
        return this.txtDivisibleBy;
    }
    
    // Setter method for cboChargeBase
    void setCboChargeBase(String cboChargeBase){
        this.cboChargeBase = cboChargeBase;
        setChanged();
    }
    // Getter method for cboChargeBase
    String getCboChargeBase(){
        return this.cboChargeBase;
    }
    
    // Setter method for txtChargeId
    void setTxtChargeId(String txtChargeId){
        this.txtChargeId = txtChargeId;
        setChanged();
    }
    // Getter method for txtChargeId
    String getTxtChargeId(){
        return this.txtChargeId;
    }
    
    // Setter method for txtChargeDesc
    void setTxtChargeDesc(String txtChargeDesc){
        this.txtChargeDesc = txtChargeDesc;
        setChanged();
    }
    // Getter method for txtChargeDesc
    String getTxtChargeDesc(){
        return this.txtChargeDesc;
    }
    
    // Setter method for txtFromSlabAmount
    void setTxtFromSlabAmount(String txtFromSlabAmount){
        this.txtFromSlabAmount = txtFromSlabAmount;
        setChanged();
    }
    // Getter method for txtFromSlabAmount
    String getTxtFromSlabAmount(){
        return this.txtFromSlabAmount;
    }
    
    // Setter method for txtMinimumChrgAmt
    void setTxtMinimumChrgAmt(String txtMinimumChrgAmt){
        this.txtMinimumChrgAmt = txtMinimumChrgAmt;
        setChanged();
    }
    // Getter method for txtMinimumChrgAmt
    String getTxtMinimumChrgAmt(){
        return this.txtMinimumChrgAmt;
    }
    
    // Setter method for txtMaximumChrgAmt
    void setTxtMaximumChrgAmt(String txtMaximumChrgAmt){
        this.txtMaximumChrgAmt = txtMaximumChrgAmt;
        setChanged();
    }
    // Getter method for txtMaximumChrgAmt
    String getTxtMaximumChrgAmt(){
        return this.txtMaximumChrgAmt;
    }
    
    // Setter method for txtChargeRate
    void setTxtChargeRate(String txtChargeRate){
        this.txtChargeRate = txtChargeRate;
        setChanged();
    }
    // Getter method for txtChargeRate
    String getTxtChargeRate(){
        return this.txtChargeRate;
    }
    
    // Setter method for cboRoundOffType
    void setCboRoundOffType(String cboRoundOffType){
        this.cboRoundOffType = cboRoundOffType;
        setChanged();
    }
    // Getter method for cboRoundOffType
    String getCboRoundOffType(){
        return this.cboRoundOffType;
    }
    
    
    // Setter method for cboSchemeId
    void setCboSchemeId(String cboSchemeId){
        this.cboSchemeId = cboSchemeId;
        setChanged();
    }
    // Getter method for cboSchemeId
    String getCboSchemeId(){
        return this.cboSchemeId;
    }
    
    // Setter method for txtAccGroupId
    void setTxtAccGroupId(String txtAccGroupId){
        this.txtAccGroupId = txtAccGroupId;
        setChanged();
    }
    // Getter method for txtAccGroupId
    String getTxtAccGroupId(){
        return this.txtAccGroupId;
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
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        //        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        //        ttNotifyObservers();
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
     * Getter for property cbmRoundOffType.
     * @return Value of property cbmRoundOffType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRoundOffType() {
        return cbmRoundOffType;
    }
    
    /**
     * Setter for property cbmRoundOffType.
     * @param cbmRoundOffType New value of property cbmRoundOffType.
     */
    public void setCbmRoundOffType(com.see.truetransact.clientutil.ComboBoxModel cbmRoundOffType) {
        this.cbmRoundOffType = cbmRoundOffType;
    }
    
    /**
     * Getter for property cbmChargeBase.
     * @return Value of property cbmChargeBase.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmChargeBase() {
        return cbmChargeBase;
    }
    
    /**
     * Setter for property cbmChargeBase.
     * @param cbmChargeBase New value of property cbmChargeBase.
     */
    public void setCbmChargeBase(com.see.truetransact.clientutil.ComboBoxModel cbmChargeBase) {
        this.cbmChargeBase = cbmChargeBase;
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
     * Getter for property rdoDeductionAcc.
     * @return Value of property rdoDeductionAcc.
     */
    public java.lang.String getRdoDeductionAcc() {
        return rdoDeductionAcc;
    }
    
    /**
     * Setter for property rdoDeductionAcc.
     * @param rdoDeductionAcc New value of property rdoDeductionAcc.
     */
    public void setRdoDeductionAcc(java.lang.String rdoDeductionAcc) {
        this.rdoDeductionAcc = rdoDeductionAcc;
    }
    
    /**
     * Getter for property rdoMandatory.
     * @return Value of property rdoMandatory.
     */
    public java.lang.String getRdoMandatory() {
        return rdoMandatory;
    }
    
    /**
     * Setter for property rdoMandatory.
     * @param rdoMandatory New value of property rdoMandatory.
     */
    public void setRdoMandatory(java.lang.String rdoMandatory) {
        this.rdoMandatory = rdoMandatory;
    }
    
    /**
     * Getter for property slabAmountData.
     * @return Value of property slabAmountData.
     */
    public boolean isSlabAmountData() {
        return slabAmountData;
    }
    
    /**
     * Setter for property slabAmountData.
     * @param slabAmountData New value of property slabAmountData.
     */
    public void setSlabAmountData(boolean slabAmountData) {
        this.slabAmountData = slabAmountData;
    }
    
    /**
     * Getter for property tblCharge.
     * @return Value of property tblCharge.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblCharge() {
        return tblCharge;
    }
    
    /**
     * Setter for property tblCharge.
     * @param tblCharge New value of property tblCharge.
     */
    public void setTblCharge(com.see.truetransact.clientutil.EnhancedTableModel tblCharge) {
        this.tblCharge = tblCharge;
    }
    
    /**
     * Getter for property cbmChargeType.
     * @return Value of property cbmChargeType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmChargeType() {
        return cbmChargeType;
    }
    
    /**
     * Setter for property cbmChargeType.
     * @param cbmChargeType New value of property cbmChargeType.
     */
    public void setCbmChargeType(com.see.truetransact.clientutil.ComboBoxModel cbmChargeType) {
        this.cbmChargeType = cbmChargeType;
    }
    
    /**
     * Getter for property cboChargeType.
     * @return Value of property cboChargeType.
     */
    public java.lang.String getCboChargeType() {
        return cboChargeType;
    }
    
    /**
     * Setter for property cboChargeType.
     * @param cboChargeType New value of property cboChargeType.
     */
    public void setCboChargeType(java.lang.String cboChargeType) {
        this.cboChargeType = cboChargeType;
        setChanged();
    }
    
}