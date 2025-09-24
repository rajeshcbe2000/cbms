/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MultiLevelControlOB.java
 *
 * Created on September 9, 2004, 12:23 PM
 */

package com.see.truetransact.ui.sysadmin.levelcontrol.multilevel;

/**
 *
 * @author  Pinky
 */

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.expressionevaluate.ExpressionEvaluator;
import com.see.truetransact.commonutil.expressionevaluate.ExpressionEvaluatorException;
import com.see.truetransact.transferobject.sysadmin.levelcontrol.multilevel.MultiLevelMasterTO;
import com.see.truetransact.transferobject.sysadmin.levelcontrol.multilevel.MultiLevelTO;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class MultiLevelControlOB extends Observable{
    
    private static MultiLevelControlOB multiLevelControlOB;
    private HashMap map;
    private ProxyFactory proxy;
    
    private TableModel tbmLevel;
    private ComboBoxModel cbmCondition;
    private ArrayList levelDetailsTO,deletedLevelDetailsTO;
    private HashMap authorizeMap;
    
    private String txtLevelID;
    private String txtNoOfPersons;
    private String condition;
    private String txtExpression;
    private String txtAmount;
    private String cboCondition;
    
    private String chkCashCredit;
    private String chkCashDebit;
    private String chkTransCredit;
    private String chkTransDebit;
    private String chkClearingDebit;
    private String chkClearingCredit;
    
    private String multiLevelID;
    private String multiLevelMasterTOStatus;
    
    private int operation;
    private String status = "";
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final static ClientParseException parseException
                                           = ClientParseException.getInstance();
    static {
        try {
            multiLevelControlOB = new MultiLevelControlOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private MultiLevelControlOB() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "MultiLevelJNDI");
        map.put(CommonConstants.HOME, "com.see.truetransact.serverside.sysadmin.levelcontrol.multilevel.MultiLevelHome");
        map.put(CommonConstants.REMOTE,"com.see.truetransact.serverside.sysadmin.levelcontrol.multilevel.MultiLevel");
        try {
            proxy = ProxyFactory.createProxy();
            fillDropdown();
            setTable();
            this.levelDetailsTO = new ArrayList();
            this.deletedLevelDetailsTO = new ArrayList();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
    }
    
    public static MultiLevelControlOB getInstance() {
        return multiLevelControlOB;
    }
    
    private void setTable(){
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Sr No");
        columnHeader.add("Level ID");
        columnHeader.add("No of Persons");
        columnHeader.add("Condition");
        ArrayList data = new ArrayList();
        tbmLevel = new TableModel(data,columnHeader);
    }
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        ArrayList key = new ArrayList();
        ArrayList value = new ArrayList();
        
        key.add("");
        value.add("");
        
        key.add(">");
        key.add("<");
       // key.add(">=");
        //key.add("<=");
        key.add("==");
        
        value.add("> - greater than");
        value.add("< - less than");
        //value.add(">= - greater than or equal");
        //value.add("<= - less than or equal");
        value.add("== - equal");
        this.cbmCondition = new ComboBoxModel(key,value);
    }
    /** Getter for property cbmCondition.
     * @return Value of property cbmCondition.
     *
     */
    public ComboBoxModel getCbmCondition() {
        return cbmCondition;
    }
    /** Setter for property cbmCondition.
     * @param cbmCondition New value of property cbmCondition.
     *
     */
    public void setCbmCondition(ComboBoxModel cbmCondition) {
        this.cbmCondition = cbmCondition;
    }
    /** Getter for property tbmLevel.
     * @return Value of property tbmLevel.
     *
     */
    public TableModel getTbmLevel() {
        return tbmLevel;
    }
    /** Setter for property tbmLevel.
     * @param tbmLevel New value of property tbmLevel.
     *
     */
    public void setTbmLevel(TableModel tbmLevel) {
        this.tbmLevel = tbmLevel;
    }
    /** Getter for property txtLevelID.
     * @return Value of property txtLevelID.
     *
     */
    public java.lang.String getTxtLevelID() {
        return txtLevelID;
    }
    /** Setter for property txtLevelID.
     * @param txtLevelID New value of property txtLevelID.
     *
     */
    public void setTxtLevelID(java.lang.String txtLevelID) {
        this.txtLevelID = txtLevelID;
    }
    /** Getter for property txtNoOfPersons.
     * @return Value of property txtNoOfPersons.
     *
     */
    public java.lang.String getTxtNoOfPersons() {
        return txtNoOfPersons;
    }
    /** Setter for property txtNoOfPersons.
     * @param txtNoOfPersons New value of property txtNoOfPersons.
     *
     */
    public void setTxtNoOfPersons(java.lang.String txtNoOfPersons) {
        this.txtNoOfPersons = txtNoOfPersons;
    }
    /** Getter for property condition.
     * @return Value of property condition.
     *
     */
    public java.lang.String getCondition() {
        return condition;
    }
    /** Setter for property condition.
     * @param condition New value of property condition.
     *
     */
    public void setCondition(java.lang.String condition) {
        this.condition = condition;
    }
    private MultiLevelTO setMultiLevelDetailTO() {
        MultiLevelTO objLevelTO = new MultiLevelTO();
        
        objLevelTO.setLevelId(this.getTxtLevelID());
        objLevelTO.setNoOfPersons(CommonUtil.convertObjToDouble(this.getTxtNoOfPersons()));
        objLevelTO.setLevelCondition(this.getCondition());
        objLevelTO.setLevelMultiId(this.multiLevelID);
        objLevelTO.setStatus(CommonConstants.STATUS_CREATED);
        
        return objLevelTO;
    }
    private ArrayList setTableRow(MultiLevelTO objLevelTO){
        ArrayList row = new ArrayList();
        row.add(String.valueOf(tbmLevel.getRowCount()+1));
        row.add(objLevelTO.getLevelId());
        row.add(objLevelTO.getNoOfPersons());
        row.add(objLevelTO.getLevelCondition());
        
        return row;
    }
    public void addLevel(int rowNo) {
        MultiLevelTO objLevelTO = this.setMultiLevelDetailTO();
        
        ArrayList row = this.setTableRow(objLevelTO);
        
        if(rowNo == -1) {
            levelDetailsTO.add(objLevelTO);
            tbmLevel.insertRow(tbmLevel.getRowCount(), row);
        }else {
            updateTO(objLevelTO,(MultiLevelTO)levelDetailsTO.get(rowNo));
            System.out.println("new TO"+objLevelTO);
            if(objLevelTO.getLevelOrder()!=null && objLevelTO.getLevelOrder().length()>0)
                objLevelTO.setStatus(CommonConstants.STATUS_MODIFIED);
            levelDetailsTO.set(rowNo,objLevelTO);
            row.set(0,String.valueOf(rowNo+1));
            tbmLevel.removeRow(rowNo);
            tbmLevel.insertRow(rowNo,row);
        }
        tbmLevel.fireTableDataChanged();
        objLevelTO = null;
    }
    private void updateTO(MultiLevelTO newTO,MultiLevelTO oldTO){
        newTO.setLevelOrder(oldTO.getLevelOrder());
    }
    public void resetLevelFields(){
        this.setCondition("");
        this.setTxtLevelID("");
        this.setTxtNoOfPersons("1");
    }
    public void populate(int rowNo){
        MultiLevelTO obj = (MultiLevelTO)this.levelDetailsTO.get(rowNo);
        
        this.setTxtLevelID(obj.getLevelId());
        this.setTxtNoOfPersons(CommonUtil.convertObjToStr(obj.getNoOfPersons()));
    }
    public void delete(int rowNo){
        if(rowNo!=-1) {
            tbmLevel.removeRow(rowNo);
            tbmLevel.fireTableDataChanged();
            
            MultiLevelTO obj = (MultiLevelTO)levelDetailsTO.remove(rowNo);
            if(this.getOperation()==ClientConstants.ACTIONTYPE_EDIT) {
                if(obj.getLevelOrder()!=null && obj.getLevelOrder().length()>0) {
                    obj.setStatus(CommonConstants.STATUS_DELETED);
                    deletedLevelDetailsTO.add(obj);
                }
            }
            rearrangeSNLevels();
            if(this.tbmLevel.getRowCount()>0)
                this.getExpression();
            tbmLevel.fireTableDataChanged();
        }
    }
    public String getLevelName(String levelID){
        HashMap where = new HashMap();
        where.put("LEVELID", levelID);
        List list = ClientUtil.executeQuery("Multi.getLevelName", where);
        if(list!=null){
            where = (HashMap)list.get(0);
            return (String)where.get("LEVEL_NAME");
        }
        return "";
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
     * Getter for property status.
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
    
    /**
     * Getter for property operation.
     * @return Value of property operation.
     */
    public int getOperation() {
        return operation;
    }
    
    /**
     * Setter for property operation.
     * @param operation New value of property operation.
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getOperation()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    public void rearrangeSNLevels(){
        int rows = this.tbmLevel.getRowCount();
        int i = 0;
        while(i<rows){
            tbmLevel.setValueAt(Integer.toString(i+1),i,0);
            i++;
        }
        tbmLevel.fireTableDataChanged();
    }
    public int getExpression(){
        int rowCount = this.tbmLevel.getRowCount();
        int result = 0;
        StringBuffer buff = new StringBuffer();
        
        if(rowCount>0) {
            String lstOperand = (String)this.tbmLevel.getValueAt(rowCount-1, 3);
            int operator=0,operand=0;
            
            if(lstOperand==null || lstOperand.length()==0) {
                for(int i=0;i<rowCount;i++){
                    buff.append(this.tbmLevel.getValueAt(i,1)+" ");
                    operand++;
                    if(((String)tbmLevel.getValueAt(i,3))!=null &&
                    ((String)tbmLevel.getValueAt(i,3)).length()>0) {
                        buff.append(this.tbmLevel.getValueAt(i,3)+" ");
                        operator++;
                    }
                }
                if(operand-1!=operator)
                    result = -1;
                //this.setTxtExpression(buff.toString());
                // return 0;
            }else {
                result = -1;
            }
        }
        this.setTxtExpression(buff.toString());
        return result;
    }
    
    /**
     * Getter for property cboCondition.
     * @return Value of property cboCondition.
     */
    public java.lang.String getCboCondition() {
        return cboCondition;
    }
    
    /**
     * Setter for property cboCondition.
     * @param cboCondition New value of property cboCondition.
     */
    public void setCboCondition(java.lang.String cboCondition) {
        this.cboCondition = cboCondition;
    }
    
    /**
     * Getter for property txtAmount.
     * @return Value of property txtAmount.
     */
    public java.lang.String getTxtAmount() {
        return txtAmount;
    }
    
    /**
     * Setter for property txtAmount.
     * @param txtAmount New value of property txtAmount.
     */
    public void setTxtAmount(java.lang.String txtAmount) {
        this.txtAmount = txtAmount;
    }
    
    /**
     * Getter for property txtExpression.
     * @return Value of property txtExpression.
     */
    public java.lang.String getTxtExpression() {
        return txtExpression;
    }
    
    /**
     * Setter for property txtExpression.
     * @param txtExpression New value of property txtExpression.
     */
    public void setTxtExpression(java.lang.String txtExpression) {
        this.txtExpression = txtExpression;
    }
    private MultiLevelMasterTO getMasterLevelTO(){
        MultiLevelMasterTO obj = new MultiLevelMasterTO();
        obj.setAmount(CommonUtil.convertObjToDouble(getTxtAmount()));
        obj.setCashCredit(getChkCashCredit());
        obj.setCashDebit(getChkCashDebit());
        obj.setClearingCredit(getChkClearingCredit());
        obj.setClearingDebit(getChkClearingDebit());
        obj.setTransferCredit(getChkTransCredit());
        obj.setTransferDebit(getChkTransDebit());
        
        obj.setCondition(this.getCboCondition());
        obj.setPostfixFormula(this.getTxtExpression());
        
        obj.setLevelMultiId(this.multiLevelID);
        
        return obj;
    }
    private void setMasterLevelTO(MultiLevelMasterTO obj){
        this.setTxtAmount(CommonUtil.convertObjToStr(obj.getAmount()));
        this.setChkCashCredit(obj.getCashCredit());
        this.setChkCashDebit(obj.getCashDebit());
        this.setChkClearingCredit(obj.getClearingCredit());
        this.setChkClearingDebit(obj.getClearingDebit());
        this.setChkTransCredit(obj.getTransferCredit());
        this.setChkTransDebit(obj.getTransferDebit());
        
        this.setCboCondition(obj.getCondition());
        this.setTxtExpression(obj.getPostfixFormula());
        
        this.multiLevelID = obj.getLevelMultiId();
        this.multiLevelMasterTOStatus = obj.getStatus();
    }
    public void doAction(){
        /*
        MultiLevelMasterTO obj = new MultiLevelMasterTO();
        obj.setAmount(CommonUtil.convertObjToDouble(getTxtAmount()));
        obj.setCashCredit(getChkCashCredit());
        obj.setCashDebit(getChkCashDebit());
        obj.setClearingCredit(getChkClearingCredit());
        obj.setClearingDebit(getChkClearingDebit());
        obj.setTransferCredit(getChkTransCredit());
        obj.setTransferDebit(getChkTransDebit());
         
        obj.setCondition(this.getCboCondition());
        obj.setPostfixFormula(this.getTxtExpression());      */
        HashMap hash = new HashMap();
        if(this.getAuthorizeMap()==null){
            System.out.println(this.levelDetailsTO);
            System.out.println(this.deletedLevelDetailsTO);
            this.levelDetailsTO.addAll(this.deletedLevelDetailsTO);
            System.out.println(this.levelDetailsTO);
            
            MultiLevelMasterTO obj =this.getMasterLevelTO();
            
            hash.put("LevelMasterTO",obj);
            hash.put("LevelDetailTOs",levelDetailsTO);
            hash.put("COMMAND",getCommand());          
            
            if(checkForDuplicateEntry(obj)){
                MultiLevelControlRB resourceBundle = new MultiLevelControlRB();
                COptionPane.showMessageDialog(null,resourceBundle.getString("WARNING_DUPLICATE_ENTRY"));
                setResult(ClientConstants.ACTIONTYPE_FAILED);
                return;
            }                
                
            System.out.println("sending DAO side"+hash);
        }else {
            hash.put(CommonConstants.AUTHORIZEMAP,this.getAuthorizeMap());
            this.setAuthorizeMap(null);
        }
        try {
            HashMap proxyResultMap = proxy.execute(hash,map);
            setResult(getOperation());
        }catch(Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    private String getCommand(){
        if(getOperation()==ClientConstants.ACTIONTYPE_NEW)
            return CommonConstants.TOSTATUS_INSERT;
        else if(getOperation()==ClientConstants.ACTIONTYPE_EDIT)
            return CommonConstants.TOSTATUS_UPDATE;
        else if(getOperation()==ClientConstants.ACTIONTYPE_DELETE)
            return CommonConstants.TOSTATUS_DELETE;
        return "";
    }
    
    /**
     * Getter for property chkCashDebit.
     * @return Value of property chkCashDebit.
     */
    public java.lang.String getChkCashDebit() {
        return chkCashDebit;
    }
    
    /**
     * Setter for property chkCashDebit.
     * @param chkCashDebit New value of property chkCashDebit.
     */
    public void setChkCashDebit(java.lang.String chkCashDebit) {
        this.chkCashDebit = chkCashDebit;
    }
    
    /**
     * Getter for property chkCashCredit.
     * @return Value of property chkCashCredit.
     */
    public java.lang.String getChkCashCredit() {
        return chkCashCredit;
    }
    
    /**
     * Setter for property chkCashCredit.
     * @param chkCashCredit New value of property chkCashCredit.
     */
    public void setChkCashCredit(java.lang.String chkCashCredit) {
        this.chkCashCredit = chkCashCredit;
    }
    
    /**
     * Getter for property chkClearinCredit.
     * @return Value of property chkClearinCredit.
     */
    public java.lang.String getChkClearingCredit() {
        return chkClearingCredit;
    }
    
    /**
     * Setter for property chkClearinCredit.
     * @param chkClearinCredit New value of property chkClearinCredit.
     */
    public void setChkClearingCredit(java.lang.String chkClearingCredit) {
        this.chkClearingCredit = chkClearingCredit;
    }
    
    /**
     * Getter for property chkClearingDebit.
     * @return Value of property chkClearingDebit.
     */
    public java.lang.String getChkClearingDebit() {
        return chkClearingDebit;
    }
    
    /**
     * Setter for property chkClearingDebit.
     * @param chkClearingDebit New value of property chkClearingDebit.
     */
    public void setChkClearingDebit(java.lang.String chkClearingDebit) {
        this.chkClearingDebit = chkClearingDebit;
    }
    
    /**
     * Getter for property chkTransCredit.
     * @return Value of property chkTransCredit.
     */
    public java.lang.String getChkTransCredit() {
        return chkTransCredit;
    }
    
    /**
     * Setter for property chkTransCredit.
     * @param chkTransCredit New value of property chkTransCredit.
     */
    public void setChkTransCredit(java.lang.String chkTransCredit) {
        this.chkTransCredit = chkTransCredit;
    }
    
    /**
     * Getter for property chkTransDebit.
     * @return Value of property chkTransDebit.
     */
    public java.lang.String getChkTransDebit() {
        return chkTransDebit;
    }
    
    /**
     * Setter for property chkTransDebit.
     * @param chkTransDebit New value of property chkTransDebit.
     */
    public void setChkTransDebit(java.lang.String chkTransDebit) {
        this.chkTransDebit = chkTransDebit;
    }
    public void resetForm(){
        this.setTxtAmount("");
        this.setTxtExpression("");
        this.setCboCondition("");
        this.setChkCashCredit("N");
        this.setChkCashDebit("N");
        this.setChkClearingCredit("N");
        this.setChkClearingDebit("N");
        this.setChkTransCredit("N");
        this.setChkTransDebit("N");
        this.tbmLevel.setData(new ArrayList());
        this.tbmLevel.fireTableDataChanged();
        this.levelDetailsTO.clear();
        this.deletedLevelDetailsTO.clear();
        this.multiLevelID="";
        this.multiLevelMasterTOStatus="";
        this.resetLevelFields();
        setChanged();
        notifyObservers();
    }
    public void populateData(String multiLevelID){
        HashMap hash = new HashMap();
        hash.put(CommonConstants.MAP_NAME,"getSelectMultiLevelMasterTO");
        hash.put(CommonConstants.MAP_WHERE,multiLevelID);
        try {
            HashMap resultMap = proxy.executeQuery(hash,map);
            List list = (List)resultMap.get(CommonConstants.MAP_NAME);
            if(list!=null && list.size()>0) {
                this.setMasterLevelTO((MultiLevelMasterTO)list.get(0));
            }
            hash.put(CommonConstants.MAP_NAME,"getSelectMultiLevelTO");
            
            resultMap = proxy.executeQuery(hash,map);
            list = (List)resultMap.get(CommonConstants.MAP_NAME);
            if(list!=null && list.size()>0) {
                levelDetailsTO = (ArrayList)list;
            }
            
            populateTable();
            setChanged();
            notifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void populateTable(){
        MultiLevelTO objLevelTO;
        ArrayList row;
        
        int size = this.levelDetailsTO.size();
        ArrayList data = new ArrayList();
        //data.addAll(levelDetailsTO);
        for(int i=0;i<size;i++){
            objLevelTO = (MultiLevelTO)this.levelDetailsTO.get(i);
            row = new ArrayList();
            row.add(objLevelTO.getLevelOrder());
            row.add(objLevelTO.getLevelId());
            row.add(objLevelTO.getNoOfPersons());
            row.add(objLevelTO.getLevelCondition());
            data.add(row);
            //data.set(CommonUtil.convertObjToInt(objLevelTO.getLevelOrder())-1,row);
        }
        this.tbmLevel.setData(data);
        TableSorter ts = new TableSorter(this.tbmLevel);
        ts.sortByColumn(0);
        this.tbmLevel = ts.getModel();
        this.rearrangeSNLevels();
        this.tbmLevel.fireTableDataChanged();
    }
    public boolean validateExpression(){
        boolean result=true;
        
        String newExpression1 = this.getTxtExpression();
        if(this.getExpression()!=-1) {
            String oldExpression = this.getTxtExpression();
            
            oldExpression.trim();
            newExpression1.trim();
            
            String newExpression = newExpression1.replaceAll(" ","");
            oldExpression = oldExpression.replaceAll(" ","");
            
            int newSize = newExpression.length();
            int oldSize = oldExpression.length();
            
            System.out.println("newEx :"+newExpression);
            System.out.println("oldEx :"+oldExpression);
            
            if(oldSize>newSize)
                result = false;
            if(!ExpressionEvaluator.isValidExpression(newExpression))
                result = false;
        /*for(int i=0,j=0;i<newSize;i++) {
            if(newExpression.charAt(i)=='(' ||
                    newExpression.charAt(i)==')')
                continue;
            if(newExpression.charAt(i)!=oldExpression.charAt(j))
                result = false;
            j++;
        }*/
        }else
            result = false;
        this.setTxtExpression(newExpression1.trim());
        return result;
    }
    
    /**
     * Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     */
    public HashMap getAuthorizeMap() {
        return authorizeMap;
    }
    
    /**
     * Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }
    
    /**
     * Getter for property multiLevelID.
     * @return Value of property multiLevelID.
     */
    public java.lang.String getMultiLevelID() {
        return multiLevelID;
    }
    
    /**
     * Setter for property multiLevelID.
     * @param multiLevelID New value of property multiLevelID.
     */
    public void setMultiLevelID(java.lang.String multiLevelID) {
        this.multiLevelID = multiLevelID;
    }
    
    /**
     * Getter for property multiLevelMasterTOStatus.
     * @return Value of property multiLevelMasterTOStatus.
     */
    public java.lang.String getMultiLevelMasterTOStatus() {
        return multiLevelMasterTOStatus;
    }
    
    /**
     * Setter for property multiLevelMasterTOStatus.
     * @param multiLevelMasterTOStatus New value of property multiLevelMasterTOStatus.
     */
    public void setMultiLevelMasterTOStatus(java.lang.String multiLevelMasterTOStatus) {
        this.multiLevelMasterTOStatus = multiLevelMasterTOStatus;
    }
     private boolean checkForDuplicateEntry(MultiLevelMasterTO obj){
         HashMap where = new HashMap();
         where.put("AMOUNT",CommonUtil.convertObjToStr(obj.getAmount()));
         where.put("CONDITION",obj.getCondition());
         where.put("CASH_CREDIT",obj.getCashCredit());
         where.put("CASH_DEBIT",obj.getCashDebit());
         where.put("TRANS_CREDIT",obj.getTransferCredit());
         where.put("TRANS_DEBIT",obj.getTransferDebit());
         where.put("CLEARING_CREDIT",obj.getClearingCredit());
         where.put("CLEARING_DEBIT",obj.getClearingDebit());
         where.put("LEVEL_MULTI_ID",obj.getLevelMultiId());
         
         
         System.out.println("where map"+where);         
         System.out.println(ClientUtil.executeQuery("checkDuplicatMultiLevelMasterTO",where));
         List list = ClientUtil.executeQuery("checkDuplicatMultiLevelMasterTO",where);
         
         System.out.println("duplicat list"+list);
         
         if(list!=null && list.size()>0) {             
             if(list.get(0)!=null)                 
                return true;              
         }            
         return false;
     }
}
