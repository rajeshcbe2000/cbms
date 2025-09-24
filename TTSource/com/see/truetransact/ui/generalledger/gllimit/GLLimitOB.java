/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLLimitOB.java
 *
 * Created on Wed Jun 08 10:53:01 GMT+05:30 2005
 */

package com.see.truetransact.ui.generalledger.gllimit;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.generalledger.gllimit.GLLimitTO;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;

/**
 *
 * @author Ashok Vijayakumar
 */

public class GLLimitOB extends Observable{
    
    private String txtGLGroup = "";
    private String txtAccountHead = "";
    private String txtLimit = "";
    private String lblStatus = "";
    private String txtAnnualLimit = "";
    private String txtOverDraw = "";
    private boolean chkInterBranchTransAllowed = false;
    private static GLLimitOB glLimitOB;
    private int actionType;
    private int result;
    private HashMap map;
    private ProxyFactory proxy;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.generalledger.gllimit.GLLimitRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private ArrayList tableHeader = new ArrayList();
    private TableModel tmlActHeadDetails;
    private final String EMPTY = "";//This is used to check whether the data entered in the particular column of the 
    //table in the UI is empty and if is so making the TO varible related to that column as null
    private ArrayList updatedList = null;//updatedList is used to identify which row of the table in the UI 
    //has been modified and sendign only those list to the serverside for modification.
    private final String YES = "Y";//variable used to check particular column data has the value "Y"
    private Date currDt = null;
    private String frmPeriod="";
    private String toPeriod="";
    private String table="";
    private int tableSize=0;
    private String slNo="";
    private ArrayList deletedList=null;
        
    private GLLimitOB()throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "GLLimitJNDI");
        map.put(CommonConstants.HOME, "generalledger.gllimit.GLLimitHome");
        map.put(CommonConstants.REMOTE,"generalledger.gllimit.GLLimit");
        currDt = ClientUtil.getCurrentDate();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        setCoulmnHeader();
        tmlActHeadDetails = new TableModel(null,tableHeader);
        
    }
    
    public static GLLimitOB getInstance()throws Exception {
        glLimitOB = new GLLimitOB();
        return glLimitOB;
    }
    
    private void setCoulmnHeader() {
        tableHeader.add(resourceBundle.getString("tblColumn0"));
        tableHeader.add(resourceBundle.getString("tblColumn1"));
        tableHeader.add(resourceBundle.getString("tblColumn2"));
        tableHeader.add(resourceBundle.getString("tblColumn3"));
        tableHeader.add(resourceBundle.getString("tblColumn4"));
        tableHeader.add(resourceBundle.getString("tblColumn5"));
        tableHeader.add(resourceBundle.getString("tblColumn6"));
        tableHeader.add(resourceBundle.getString("tblColumn7"));
        tableHeader.add(resourceBundle.getString("tblColumn8"));

    }
    
    // Setter method for txtGLGroup
    void setTxtGLGroup(String txtGLGroup){
        this.txtGLGroup = txtGLGroup;
        setChanged();
    }
    // Getter method for txtGLGroup
    String getTxtGLGroup(){
        return this.txtGLGroup;
    }
    
    // Setter method for txtAccountHead
    void setTxtAccountHead(String txtAccountHead){
        this.txtAccountHead = txtAccountHead;
        setChanged();
    }
    // Getter method for txtAccountHead
    String getTxtAccountHead(){
        return this.txtAccountHead;
    }
    
    // Setter method for txtLimit
    void setTxtLimit(String txtLimit){
        this.txtLimit = txtLimit;
        setChanged();
    }
    // Getter method for txtLimit
    String getTxtLimit(){
        return this.txtLimit;
    }
    
    /**
     * Getter for property txtAnnualLimit.
     * @return Value of property txtAnnualLimit.
     */
    public java.lang.String getTxtAnnualLimit() {
        return txtAnnualLimit;
    }
    
    /**
     * Setter for property txtAnnualLimit.
     * @param txtAnnualLimit New value of property txtAnnualLimit.
     */
    public void setTxtAnnualLimit(java.lang.String txtAnnualLimit) {
        this.txtAnnualLimit = txtAnnualLimit;
        setChanged();
    }
    
    /**
     * Getter for property txtOverDraw.
     * @return Value of property txtOverDraw.
     */
    public java.lang.String getTxtOverDraw() {
        return txtOverDraw;
    }
    
    /**
     * Setter for property txtOverDraw.
     * @param txtOverDraw New value of property txtOverDraw.
     */
    public void setTxtOverDraw(java.lang.String txtOverDraw) {
        this.txtOverDraw = txtOverDraw;
        setChanged();
    }
    
      /**
     * Getter for property chkInterBranchTransAllowed.
     * @return Value of property chkInterBranchTransAllowed.
     */
    public boolean getChkInterBranchTransAllowed() {
        return chkInterBranchTransAllowed;
    }    
    
    /**
     * Setter for property chkInterBranchTransAllowed.
     * @param chkInterBranchTransAllowed New value of property chkInterBranchTransAllowed.
     */
    public void setChkInterBranchTransAllowed(boolean chkInterBranchTransAllowed) {
        this.chkInterBranchTransAllowed = chkInterBranchTransAllowed;
    }    
    
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        setChanged();
        notifyObservers();
    }
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
        notifyObservers();
    }
    
    /**
     * Getter for property tmlActHeadDetails.
     * @return Value of property tmlActHeadDetails.
     */
    public com.see.truetransact.clientutil.TableModel getTmlActHeadDetails() {
        return tmlActHeadDetails;
    }
    
    /**
     * Setter for property tmlActHeadDetails.
     * @param tmlActHeadDetails New value of property tmlActHeadDetails.
     */
    public void setTmlActHeadDetails(com.see.truetransact.clientutil.TableModel tmlActHeadDetails) {
        this.tmlActHeadDetails = tmlActHeadDetails;
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
        setStatus();
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
        setResultStatus();
        setChanged();
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
    
    /** This method is used to reset the fields in the UI **/
    public void resetForm(){
        setTxtGLGroup("");
        setTxtAccountHead("");
        setTxtLimit("");
        setTxtAnnualLimit("");
        setTxtOverDraw("");
        setChkInterBranchTransAllowed(false);
        setFrmPeriod("");
        setToPeriod("");
        setSlNo("");
        tmlActHeadDetails = new TableModel(null,tableHeader);
        deletedList=null;
        notifyObservers();
    }
    
    /** This method is used to Populate the tblActHeadDetails in the ui**/
    public void populateData(HashMap whereMap){
        try{
            HashMap resultMap = proxy.executeQuery(whereMap, map);
            ArrayList resultList = (ArrayList) resultMap.get(CommonConstants.MAP_NAME);
           
            populateTmlActHeadDetails(resultList);
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /** Populating the table model tmlActHeadDetails **/
    private void populateTmlActHeadDetails(ArrayList resultList){
        if(resultList!=null){
            if(resultList.size()!=0){
                int size = resultList.size();
                setTableSize(size);
                for(int i=0;i<size;i++){
                    GLLimitTO objTO = (GLLimitTO) resultList.get(i);
                    ArrayList dataList = new ArrayList();
                   
                    if(updatedList==null){
                        updatedList =  new ArrayList();
                        updatedList.add(objTO.getSlNo());
                    }else{
                        updatedList.add(objTO.getSlNo());
                    }
                    dataList.add(objTO.getSlNo());
                    dataList.add(objTO.getAcHdId());
                    dataList.add(objTO.getLimitAmt());
                    dataList.add(objTO.getAnnualLimitAmt());
                    dataList.add(objTO.getOverDrawPer());
                    dataList.add(objTO.getFrmPeriod());
                    dataList.add(objTO.getToPeriod());
                    dataList.add(objTO.getAuthorizeStatus());
                    dataList.add(objTO.getInterBranchAllowed());                   
                    tmlActHeadDetails.insertRow(0, dataList);
                }
            }
        }
    }
    
    /** This is used to populate the selected row to the UI TextFields **/
    public void populateSelectedRow(int rowIndex){
        ArrayList rowData = tmlActHeadDetails.getRow(rowIndex);
        setSlNo(CommonUtil.convertObjToStr(rowData.get(0)));
        setTxtAccountHead(CommonUtil.convertObjToStr(rowData.get(1)));
        setTxtLimit(CommonUtil.convertObjToStr(rowData.get(2)));
        setTxtAnnualLimit(CommonUtil.convertObjToStr(rowData.get(3)));
        setTxtOverDraw(CommonUtil.convertObjToStr(rowData.get(4)));
        setFrmPeriod(CommonUtil.convertObjToStr(rowData.get(5)));
        setToPeriod(CommonUtil.convertObjToStr(rowData.get(6)));
        if(CommonUtil.convertObjToStr(rowData.get(8)).equals(YES)){
            setChkInterBranchTransAllowed(true);
        }else{
            setChkInterBranchTransAllowed(false);
        }
        
        notifyObservers();
    }
    
    /** This method is used to insert the newly updated data in to the table **/
    public void updateRowData(int rowIndex,ArrayList newData){
       
        if(getTable().equals("update")){
            tmlActHeadDetails.removeRow(rowIndex);
        }
        tmlActHeadDetails.insertRow(tmlActHeadDetails.getRowCount(), newData);
    }
    
    public void deleteRowData(int rowIndex){
        if(deletedList==null){
            
            deletedList =  new ArrayList();
            HashMap hamp=new HashMap();
            hamp.put("AC_HD_ID",getTxtAccountHead());
            hamp.put("SLNO",getSlNo());
            hamp.put("USER",TrueTransactMain.USER_ID);
            deletedList.add(hamp);
        }else{
            HashMap hamp=new HashMap();
            hamp.put("AC_HD_ID",getTxtAccountHead());
            hamp.put("SLNO",getSlNo());
            hamp.put("USER",TrueTransactMain.USER_ID);
            deletedList.add(getSlNo());
        }
        tmlActHeadDetails.removeRow(rowIndex);
    }
    
    /** This is used to populate a arraylist with GLLimitData **/
    private ArrayList getGLLimitData(){
        ArrayList rowList = tmlActHeadDetails.getDataArrayList();
        ArrayList tblData = new ArrayList();
        int size = rowList.size();
        GLLimitTO objGLLimitTO = null;
        int tbSize= getTableSize();
        for(int i=0; i<size; i++){
            objGLLimitTO = new GLLimitTO();
            ArrayList rowData = (ArrayList) rowList.get(i);
           
            objGLLimitTO.setBranchGroup(getTxtGLGroup());
            objGLLimitTO.setSlNo(CommonUtil.convertObjToInt(rowData.get(0)));
            objGLLimitTO.setAcHdId(CommonUtil.convertObjToStr(rowData.get(1)));
            if(EMPTY.equals(rowData.get(2)) || rowData.get(2)==null){
                //To check whether particular Column data is null or empty if it is so, setting the
                //TO of the same to be null
                objGLLimitTO.setLimitAmt(null);
            }else{
                //else setting the TO to the amount in the relevant column
                objGLLimitTO.setLimitAmt(CommonUtil.convertObjToDouble(rowData.get(2)));
            }
            if(EMPTY.equals(rowData.get(3)) || rowData.get(3)==null){
                //To check whether particular Column data is null or empty if it is so, setting the
                //TO of the same to be null
                objGLLimitTO.setAnnualLimitAmt(null);
            }else{
                //else setting the TO to the amount in the relevant column
                objGLLimitTO.setAnnualLimitAmt(CommonUtil.convertObjToDouble(rowData.get(3)));
            }
            if(EMPTY.equals(rowData.get(4)) || rowData.get(4)==null){
                //To check whether particular Column data is null or empty if it is so, setting the
                //TO of the same to be null
                objGLLimitTO.setOverDrawPer(null);
            }else{
                //else setting the TO to the amount in the relevant column
                objGLLimitTO.setOverDrawPer(CommonUtil.convertObjToDouble(rowData.get(4)));
            }
            if(EMPTY.equals(rowData.get(5)) || rowData.get(5)==null){
                //To check whether particular Column data is null or empty if it is so, setting the
                //TO of the same to be null
                objGLLimitTO.setFrmPeriod(null);
            }else{
                //else setting the TO to the amount in the relevant column
                objGLLimitTO.setFrmPeriod(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(rowData.get(5))));
            }
            
             if(EMPTY.equals(rowData.get(6)) || rowData.get(6)==null){
                //To check whether particular Column data is null or empty if it is so, setting the
                //TO of the same to be null
                objGLLimitTO.setToPeriod(null);
            }else{
                //else setting the TO to the amount in the relevant column
                objGLLimitTO.setToPeriod(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(rowData.get(6))));
            }
             if(EMPTY.equals(rowData.get(7)) || rowData.get(7)==null){
                //To check whether particular Column data is null or empty if it is so, setting the
                //TO of the same to be null
                objGLLimitTO.setAuthorizeStatus(null);
            }else{
                //else setting the TO to the amount in the relevant column
               objGLLimitTO.setAuthorizeStatus(CommonUtil.convertObjToStr(rowData.get(7)));
            }
             if(EMPTY.equals(rowData.get(8)) || rowData.get(8)==null){
                //To check whether particular Column data is null or empty if it is so, setting the
                //TO of the same to be null
                objGLLimitTO.setInterBranchAllowed(null);
            }else{
                //else setting the TO to the amount in the relevant column
                objGLLimitTO.setInterBranchAllowed(CommonUtil.convertObjToStr(rowData.get(8)));
            }
            
            
            
            objGLLimitTO.setStatusBy(TrueTransactMain.USER_ID);
            objGLLimitTO.setStatusDt(currDt);
            if(getActionType()==ClientConstants.ACTIONTYPE_NEW || getActionType()==ClientConstants.ACTIONTYPE_DELETE ){
                if(getActionType()==ClientConstants.ACTIONTYPE_NEW || getTableSize()==0){
                    objGLLimitTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                }else if(getActionType()==ClientConstants.ACTIONTYPE_DELETE){
                    objGLLimitTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                }
                tblData.add(objGLLimitTO);
            }else if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                boolean dataExists = false;
                String dataDeleted ="";
                for(int j=0;j<updatedList.size();j++){
                    //Getting the Account head of a row data with the updatedList's AccountHead,if both matches
                    //that is going to be the modified row, if any match comes, making the boolean variable as true
                    
                    if(rowData.get(0).equals(updatedList.get(j))){
                        dataExists = true;
                    }
                    
                }
                if(deletedList!=null)
                if(deletedList.size()>0){
                     for(int j=0;j<deletedList.size();j++){
                    //Getting the Account head of a row data with the updatedList's AccountHead,if both matches
                    //that is going to be the modified row, if any match comes, making the boolean variable as true
                    
                    if(rowData.get(0).equals(deletedList.get(j))){
                        dataDeleted ="true";
                    }
                    
                }
                }
                if(dataExists){
                    //if dataExists is true, making only that entry to do modification in the 
                    //Database Table GL_LIMIT
                    
                        objGLLimitTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
                }else{
                    if(dataDeleted.equals("true")){
                        objGLLimitTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    }else{
                        objGLLimitTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                    }
                    }                    
                    tblData.add(objGLLimitTO);
                
            }
            tbSize=tbSize-1;
        }
        return tblData;
    }
    
    /** This method is used to do relevant Database operations insertion, updation or deletion **/
    public void doAction(){
        try{
            HashMap dataMap = new HashMap();
            dataMap.put("GLLimitTO", getGLLimitData());
            updatedList = null;
            if(deletedList!=null)
            if(deletedList.size()>0 && deletedList!=null){
             dataMap.put("DELETED_LIST",deletedList);   
            }
            proxy.execute(dataMap,map);
            setResult(getActionType());
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /**
     * Getter for property frmPeriod.
     * @return Value of property frmPeriod.
     */
    public java.lang.String getFrmPeriod() {
        return frmPeriod;
    }    
  
    /**
     * Setter for property frmPeriod.
     * @param frmPeriod New value of property frmPeriod.
     */
    public void setFrmPeriod(java.lang.String frmPeriod) {
        this.frmPeriod = frmPeriod;
    }
    
    /**
     * Getter for property toPeriod.
     * @return Value of property toPeriod.
     */
    public java.lang.String getToPeriod() {
        return toPeriod;
    }
    
    /**
     * Setter for property toPeriod.
     * @param toPeriod New value of property toPeriod.
     */
    public void setToPeriod(java.lang.String toPeriod) {
        this.toPeriod = toPeriod;
    }
    
    /**
     * Getter for property table.
     * @return Value of property table.
     */
    public java.lang.String getTable() {
        return table;
    }
    
    /**
     * Setter for property table.
     * @param table New value of property table.
     */
    public void setTable(java.lang.String table) {
        this.table = table;
    }
    
    /**
     * Getter for property tableSize.
     * @return Value of property tableSize.
     */
    public int getTableSize() {
        return tableSize;
    }
    
    /**
     * Setter for property tableSize.
     * @param tableSize New value of property tableSize.
     */
    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }
    
    /**
     * Getter for property slNo.
     * @return Value of property slNo.
     */
    public java.lang.String getSlNo() {
        return slNo;
    }
    
    /**
     * Setter for property slNo.
     * @param slNo New value of property slNo.
     */
    public void setSlNo(java.lang.String slNo) {
        this.slNo = slNo;
    }
    
    /**
     * Getter for property deletedList.
     * @return Value of property deletedList.
     */
    public java.util.ArrayList getDeletedList() {
        return deletedList;
    }
    
    /**
     * Setter for property deletedList.
     * @param deletedList New value of property deletedList.
     */
    public void setDeletedList(java.util.ArrayList deletedList) {
        this.deletedList = deletedList;
    }
    
}