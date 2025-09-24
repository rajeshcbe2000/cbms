/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LoanChargesOB.java
 *
 * Created on Mon Aug 29 13:22:57 IST 2011
 */

package com.see.truetransact.ui.common.charges;

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
//import com.see.truetransact.transferobject.common.charges.DepositChargesTO;
import com.see.truetransact.transferobject.common.charges.LoanChargesTO;
import com.see.truetransact.transferobject.common.charges.LoanSlabChargesTO;
import java.util.Date;
/**
 *
 * @author
 */

public class DepositChargesOB extends Observable{
    
    private String txtToSlabAmount = "";
    private String txtFlatCharges = "";
    private String txtDivisibleBy = "";
    private String cboChargeBase = "";
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
    
    private String rdoDeductionAcc = "";
    private String rdoMandatory = "";
    
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(LoanChargesOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private LoanChargesTO objLoanChargeTO;
    private LoanSlabChargesTO objLoanSlabChargesTO;
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
    private boolean chkEditable=false;
    private Date currDt = null;
    public boolean isChkEditable() {
        return chkEditable;
    }

    public void setChkEditable(boolean chkEditable) {
        this.chkEditable = chkEditable;
    }
    /** Creates a new instance of TDS MiantenanceOB */
    public DepositChargesOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DepositChargesJNDI");
            map.put(CommonConstants.HOME, "DepositChargesJNDIHome");
            map.put(CommonConstants.REMOTE, "DepositCharges");
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
            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap)keyValue.get("CHARGE BASE"));
            cbmChargeBase = new ComboBoxModel(key,value);
            makeNull();
            
            LinkedHashMap transactionMap = new LinkedHashMap();
            HashMap retrieve = new HashMap();
            ArrayList keyList = new ArrayList();
            keyList.add("");
            ArrayList valList = new ArrayList();
            valList.add("");
              /// List resultList = ClientUtil.executeQuery("InterMaintenance.getProductDataTL", transactionMap);
            List resultList = ClientUtil.executeQuery("InterMaintenance.getProductDataTLForDepositCharge", transactionMap);
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
            data.put("LoanChargesDetails",setLoanChargeDetailsData());
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
    public LoanChargesTO setLoanChargeDetailsData() {
        
        final LoanChargesTO objDepositChargeTO = new LoanChargesTO();
        try{
            objDepositChargeTO.setSchemeId(getCboSchemeId());
            objDepositChargeTO.setChargeId(getTxtChargeId());
            objDepositChargeTO.setChargeDesc(getTxtChargeDesc());
            objDepositChargeTO.setAccHead(getTxtAccGroupId());
            objDepositChargeTO.setMandatory(getRdoMandatory());
            objDepositChargeTO.setDeductionAccu(getRdoDeductionAcc());
            objDepositChargeTO.setChargeBase(getCboChargeBase());
            objDepositChargeTO.setFromSlabAmt(getTxtFromSlabAmount());
            objDepositChargeTO.setToSlabAmt(getTxtToSlabAmount());
            objDepositChargeTO.setFlatCharge(CommonUtil.convertObjToDouble(getTxtFlatCharges()));
            objDepositChargeTO.setChargeRate(getTxtChargeRate());
            objDepositChargeTO.setDivisibleBy(getTxtDivisibleBy());
            objDepositChargeTO.setRoundOffType(getCboRoundOffType());
            objDepositChargeTO.setMinChargeAmount(getTxtMinimumChrgAmt());
            objDepositChargeTO.setMaxChargeAmount(getTxtMaximumChrgAmt());
            objDepositChargeTO.setStatus(getAction());
            objDepositChargeTO.setStatusBy(TrueTransactMain.USER_ID);
            objDepositChargeTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objDepositChargeTO.setIsDepositOrLoan(CommonConstants.DEPOSITS);
            if(isChkEditable()==true)
            objDepositChargeTO.setEditable("Y");
            else
                objDepositChargeTO.setEditable("N");
        }catch(Exception e){
            log.info("Error In setLoanChargeDetailsData()");
            e.printStackTrace();
        }
        return objDepositChargeTO;
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("#@@%@@#%@#data"+data);
            objLoanChargeTO = (LoanChargesTO) ((List) data.get("ChargeDetailsTO")).get(0);
            populateChargeDetailsData(objLoanChargeTO);
            if(data.containsKey("SlabAmountListTO")){
                slabAmountMap= (LinkedHashMap)data.get("SlabAmountListTO");
                ArrayList addList =new ArrayList(slabAmountMap.keySet());
                for(int i=0;i<addList.size();i++){
                    LoanSlabChargesTO  objLoanSlabChargesTO = (LoanSlabChargesTO)  slabAmountMap.get(addList.get(i));
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
    
    private void populateChargeDetailsData(LoanChargesTO objLoanChargeTO) throws Exception{
        this.setCboSchemeId(CommonUtil.convertObjToStr(objLoanChargeTO.getSchemeId()));
        this.setTxtChargeId(CommonUtil.convertObjToStr(objLoanChargeTO.getChargeId()));
        this.setTxtChargeDesc(CommonUtil.convertObjToStr(objLoanChargeTO.getChargeDesc()));
        this.setTxtAccGroupId(CommonUtil.convertObjToStr(objLoanChargeTO.getAccHead()));
        this.setRdoMandatory(CommonUtil.convertObjToStr(objLoanChargeTO.getMandatory()));
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
         System.out.println("aaaa"+objLoanChargeTO.getEditable());
        //this.setChkEditable(CommonUtil.convertObjTo(objLoanChargeTO.getEditable()));
        if(CommonUtil.convertObjToStr(objLoanChargeTO.getEditable()).equals("Y")){
            this.setChkEditable(true);
        }else{
            System.out.println("ssssss"+objLoanChargeTO.getEditable());
            this.setChkEditable(false);
        }
        setChanged();
        notifyObservers();
    }
    
    public void addSlabAmountTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final LoanSlabChargesTO objLoanSlabChargesTO = new LoanSlabChargesTO();
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
            objLoanSlabChargesTO.setSlNo(slno);
            objLoanSlabChargesTO.setChargeId(getTxtChargeId());
            objLoanSlabChargesTO.setFromSlabAmt(CommonUtil.convertObjToDouble(getTxtFromSlabAmount()));
            objLoanSlabChargesTO.setToSlabAmt(CommonUtil.convertObjToDouble(getTxtToSlabAmount()));
            objLoanSlabChargesTO.setChargeRate(CommonUtil.convertObjToDouble(getTxtChargeRate()));
            objLoanSlabChargesTO.setDivisibleBy(CommonUtil.convertObjToDouble(getTxtDivisibleBy()));
            objLoanSlabChargesTO.setRoundOffType(getCboRoundOffType());
            objLoanSlabChargesTO.setMinChargeAmount(CommonUtil.convertObjToDouble(getTxtMinimumChrgAmt()));
            objLoanSlabChargesTO.setMaxChargeAmount(CommonUtil.convertObjToDouble(getTxtMaximumChrgAmt()));
            slabAmountMap.put(objLoanSlabChargesTO.getSlNo(),objLoanSlabChargesTO);
            String sno=String.valueOf(slno);
            updateSlabAmountDetails(rowSel,sno,objLoanSlabChargesTO);
//            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void updateSlabAmountDetails(int rowSel, String sno, LoanSlabChargesTO objLoanSlabChargesTO)  throws Exception{
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
    
    public void populateSlabDetails(int row){
        try{
            resetSlabDetails();
            final LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO)slabAmountMap.get(row);
            populateTableData(objLoanSlabChargesTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTableData(LoanSlabChargesTO objLoanSlabChargesTO)  throws Exception{
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
    
    public void deleteTableData(int val, int row){
        if(deletedSlabAmountMap== null){
            deletedSlabAmountMap = new LinkedHashMap();
        }
        LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO) slabAmountMap.get(val);
        objLoanSlabChargesTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedSlabAmountMap.put(CommonUtil.convertObjToInt(tblCharge.getValueAt(row,0)),slabAmountMap.get(val));
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
            LoanSlabChargesTO objLoanSlabChargesTO = (LoanSlabChargesTO) slabAmountMap.get(addList.get(i));
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
    
}