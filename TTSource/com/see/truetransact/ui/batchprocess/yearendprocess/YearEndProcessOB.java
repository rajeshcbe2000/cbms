/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * YearEndProcessOB.java
 *
 * Created on Thu Jan 20 15:43:27 IST 2005
 */

package com.see.truetransact.ui.batchprocess.yearendprocess;

import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.TrueTransactMain;

/**
 *
 * @author Ashok Vijayakumar
 */

public class YearEndProcessOB extends CObservable{
    
    /** Variables Declaration - Corresponding each Variable is in YearEndProcessUI*/
    private String txtProfitAcHead = "";
    private String txtLossAcHead = "";
    private String txtTotalIncome = "0";
    private String txtTotalExpenditure = "0";
    private String txtProfitLoss = "";
    private String lblProfitOrLoss = "";
    private static Date currDt = null;
    /** Other Varibales Declartion */
    private ProxyFactory proxy;
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private static YearEndProcessOB objYearEndProcessOB;//Singleton Object Reference
    private final static Logger log = Logger.getLogger(YearEndProcessOB.class);//Creating Instace of Log
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
//    private  YearEndProcessTO objYearEndProcessTO;//Reference for the EntityBean YearEndProcessTO
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private YearEndProcessRB objYearEndProcessRB = new YearEndProcessRB();
    private EnhancedTableModel tbmIncome;
    private EnhancedTableModel tbmExpenditure;
    private ArrayList tblIncomeTitle = new ArrayList();
    private ArrayList tblExpenditureTitle = new ArrayList();
    
    /** Consturctor Declaration  for  YearEndProcessOB */
    private YearEndProcessOB() {
        try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            setTblIncomeTitle();
            setTblExpenditureTitle();
            tbmIncome = new EnhancedTableModel(null, tblIncomeTitle);
            tbmExpenditure = new EnhancedTableModel(null, tblExpenditureTitle);
            populateData(new HashMap());
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    static {
        try {
            log.info("Creating ParameterOB...");
            currDt = ClientUtil.getCurrentDate();
            objYearEndProcessOB = new YearEndProcessOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "YearEndProcessJNDI");
        map.put(CommonConstants.HOME, "batchprocess.yearendprocess.YearEndProcessHomee");
        map.put(CommonConstants.REMOTE, "batchprocess.yearendprocess.YearEndProcess");
    }
    
    /** This method sets the Title for the tbmIncome Table in the UI **/
    public void setTblIncomeTitle() throws Exception{
        tblIncomeTitle.add(objYearEndProcessRB.getString("lblIncomeHeads"));
        tblIncomeTitle.add(objYearEndProcessRB.getString("lblIncomeHeadDescription"));
        tblIncomeTitle.add(objYearEndProcessRB.getString("lblIncomeBalance"));
    }
    
    /** This method sets the Title for the tbmExpenditure Table in the UI **/
    public void setTblExpenditureTitle() throws Exception{
        tblExpenditureTitle.add(objYearEndProcessRB.getString("lblExpenditureHeads"));
        tblExpenditureTitle.add(objYearEndProcessRB.getString("lblExpenditureHeadDescription"));
        tblExpenditureTitle.add(objYearEndProcessRB.getString("lblExpenditureBalance"));
    }
   
// This method removes the row from the tbmIncome & tbmExpenditure in UI
    public void removeTblRow(){
        int i = tbmIncome.getRowCount() - 1;
        while(i >= 0){
            tbmIncome.removeRow(i);
            i-=1;
        }
        i = tbmExpenditure.getRowCount() - 1;
        while(i >= 0){
            tbmExpenditure.removeRow(i);
            i-=1;
        }
    }
    
    /** This will sets the all the elements of the ArrayList in to tbmExpenditure row **/
    public void setTbmIncome(ArrayList list){
        ArrayList tblRowList = null;
        HashMap columnData = null;
        int size=list.size();
        for (int i=0;i<size;i++){
            tblRowList = new ArrayList();
            columnData =(HashMap) list.get(i);
            tblRowList.add(columnData.get("AC_HD_ID"));
            tblRowList.add(columnData.get("AC_HD_DESC"));
            tblRowList.add(ClientUtil.convertObjToCurrency(columnData.get("BALANCE")));
            tbmIncome.insertRow(0,tblRowList);
        }
        tblRowList = null;
        columnData = null;
    }
    
    /** This will sets the all the elements of the ArrayList in to tbmExpenditure row **/
    public void setTbmExpenditure(ArrayList list){
        ArrayList tblRowList = null;
        HashMap columnData = null;
        int size=list.size();
        for (int i=0;i<size;i++){
            tblRowList = new ArrayList();
            columnData =(HashMap) list.get(i);
            tblRowList.add(columnData.get("AC_HD_ID"));
            tblRowList.add(columnData.get("AC_HD_DESC"));
            tblRowList.add(ClientUtil.convertObjToCurrency(columnData.get("BALANCE")));
            tbmExpenditure.insertRow(0,tblRowList);
        }
        tblRowList = null;
        columnData = null;
    }
    
    
    /**
     * Returns an instance of YearEndProcessOB.
     * @return  YearEndProcessOB
     */
    
    public static YearEndProcessOB getInstance()throws Exception{
        return objYearEndProcessOB;
    }
    
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
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
    
    public int getResult(){
        return _result;
    }
    
    /** Creates an Instance of YearEndProcessTO Bean and sets its variables with OBMethods */
//    private YearEndProcessTO getYearEndProcessTO(String command){
//        YearEndProcessTO objYearEndProcessTO = new YearEndProcessTO();
//        objYearEndProcessTO.setCommand(command);
//        objYearEndProcessTO.setConfigId(getTxtYearEndProcessId());
//        objYearEndProcessTO.setTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getKeyForSelected()));
//        objYearEndProcessTO.setSeriesNo(getTxtSeriesNo());
//        objYearEndProcessTO.setTokenStartNo(new Double(getTxtStartingTokenNo()));
//        objYearEndProcessTO.setTokenEndNo(new Double(getTxtEndingTokenNo()));
//        objYearEndProcessTO.setBranchId(TrueTransactMain.BRANCH_ID);
//        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
//            objYearEndProcessTO.setCreatedBy(TrueTransactMain.USER_ID);
//            objYearEndProcessTO.setCreatedDt(currDt);
//        }
//        objYearEndProcessTO.setStatusBy(TrueTransactMain.USER_ID);
//        objYearEndProcessTO.setStatusDt(currDt);
//        return objYearEndProcessTO;
//    }
//    
//    private void setYearEndProcessTO(YearEndProcessTO objYearEndProcessTO){
//        setTxtYearEndProcessId(objYearEndProcessTO.getConfigId());
//        setCboTokenType(CommonUtil.convertObjToStr(getCbmTokenType().getDataForKey(objYearEndProcessTO.getTokenType())));
//        setTxtSeriesNo(objYearEndProcessTO.getSeriesNo());
//        setTxtStartingTokenNo(CommonUtil.convertObjToStr(objYearEndProcessTO.getTokenStartNo()));
//        setTxtEndingTokenNo(CommonUtil.convertObjToStr(objYearEndProcessTO.getTokenEndNo()));
//        setStatusBy(objYearEndProcessTO.getStatusBy());
//         setAuthorizeStatus(objYearEndProcessTO.getAuthorizeStatus());
//        notifyObservers();
//    }
    
    /** Resets all the UI Fields */
    public void resetForm(){
        setTxtProfitAcHead("");
        setTxtLossAcHead("");
        setTxtTotalIncome("");
        setTxtTotalExpenditure("");
        setTxtProfitLoss("");
        notifyObservers();
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            HashMap yearYendProcessMap = new HashMap();
            yearYendProcessMap.put(CommonConstants.MODULE, getModule());
            yearYendProcessMap.put(CommonConstants.SCREEN, getScreen());
            yearYendProcessMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            yearYendProcessMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            yearYendProcessMap.put("PROFIT_AC_HEAD", getTxtProfitAcHead());
            if (getTxtLossAcHead()!=null && getTxtLossAcHead().length()>0) {
                yearYendProcessMap.put("LOSS_AC_HEAD", getTxtLossAcHead());
            } else {
                yearYendProcessMap.put("LOSS_AC_HEAD", getTxtProfitAcHead());
            }
            yearYendProcessMap.put("INCOME_LIST", tbmIncome.getDataArrayList());
            yearYendProcessMap.put("EXPENDITURE_LIST", tbmExpenditure.getDataArrayList());
            yearYendProcessMap.put("TOTAL_INCOME", new Double(txtTotalIncome));
            yearYendProcessMap.put("TOTAL_EXPENDITURE", new Double(txtTotalIncome));
            yearYendProcessMap.put("PROFIT_OR_LOSS", lblProfitOrLoss);
            yearYendProcessMap.put("PROFIT_OR_LOSS_VALUE", new Double(txtProfitLoss));

            HashMap proxyReturnMap = proxy.execute(yearYendProcessMap, map);
            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
            resetForm();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /* Populates the TO object by executing a Query */
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
//            System.out.println("#$#$ mapData:"+mapData);
            setTbmIncome((ArrayList) mapData.get("INCOME_LIST"));
            setTbmExpenditure((ArrayList) mapData.get("EXPENDITURE_LIST"));
            setProfitLoss((HashMap)mapData.get("PROFIT_OR_LOSS"));
            setChanged();
            ttNotifyObservers();
//            YearEndProcessTO objYearEndProcessTO =
//            (YearEndProcessTO) ((List) mapData.get("YearEndProcessTO")).get(0);
//            setYearEndProcessTO(objYearEndProcessTO);
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            
        }
    }
    
    private void setProfitLoss(HashMap profitLossMap) {
        setTxtTotalIncome(CommonUtil.convertObjToStr(profitLossMap.get("INCOME")));
        setTxtTotalExpenditure(CommonUtil.convertObjToStr(profitLossMap.get("EXPENDITURE")));
        setTxtProfitLoss(CommonUtil.convertObjToStr(profitLossMap.get("PROFIT_OR_LOSS")));
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    /**
     * Getter for property txtProfitAcHead.
     * @return Value of property txtProfitAcHead.
     */
    public java.lang.String getTxtProfitAcHead() {
        return txtProfitAcHead;
    }
    
    /**
     * Setter for property txtProfitAcHead.
     * @param txtProfitAcHead New value of property txtProfitAcHead.
     */
    public void setTxtProfitAcHead(java.lang.String txtProfitAcHead) {
        this.txtProfitAcHead = txtProfitAcHead;
    }
    
    /**
     * Getter for property txtLossAcHead.
     * @return Value of property txtLossAcHead.
     */
    public java.lang.String getTxtLossAcHead() {
        return txtLossAcHead;
    }
    
    /**
     * Setter for property txtLossAcHead.
     * @param txtLossAcHead New value of property txtLossAcHead.
     */
    public void setTxtLossAcHead(java.lang.String txtLossAcHead) {
        this.txtLossAcHead = txtLossAcHead;
    }
    
    /**
     * Getter for property txtProfitLoss.
     * @return Value of property txtProfitLoss.
     */
    public java.lang.String getTxtProfitLoss() {
        return txtProfitLoss;
    }
    
    /**
     * Setter for property txtProfitLoss.
     * @param txtProfitLoss New value of property txtProfitLoss.
     */
    public void setTxtProfitLoss(java.lang.String txtProfitLoss) {
        this.txtProfitLoss = txtProfitLoss;
        setChanged();
    }
    
    /**
     * Getter for property tbmIncome.
     * @return Value of property tbmIncome.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmIncome() {
        return tbmIncome;
    }
    
    /**
     * Setter for property tbmIncome.
     * @param tbmIncome New value of property tbmIncome.
     */
    public void setTbmIncome(com.see.truetransact.clientutil.EnhancedTableModel tbmIncome) {
        this.tbmIncome = tbmIncome;
    }
    
    /**
     * Getter for property tbmExpenditure.
     * @return Value of property tbmExpenditure.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmExpenditure() {
        return tbmExpenditure;
    }
    
    /**
     * Setter for property tbmExpenditure.
     * @param tbmExpenditure New value of property tbmExpenditure.
     */
    public void setTbmExpenditure(com.see.truetransact.clientutil.EnhancedTableModel tbmExpenditure) {
        this.tbmExpenditure = tbmExpenditure;
    }
    
    /**
     * Getter for property txtTotalIncome.
     * @return Value of property txtTotalIncome.
     */
    public java.lang.String getTxtTotalIncome() {
        return txtTotalIncome;
    }
    
    /**
     * Setter for property txtTotalIncome.
     * @param txtTotalIncome New value of property txtTotalIncome.
     */
    public void setTxtTotalIncome(java.lang.String txtTotalIncome) {
        this.txtTotalIncome = txtTotalIncome;
    }
    
    /**
     * Getter for property txtTotalExpenditure.
     * @return Value of property txtTotalExpenditure.
     */
    public java.lang.String getTxtTotalExpenditure() {
        return txtTotalExpenditure;
    }
    
    /**
     * Setter for property txtTotalExpenditure.
     * @param txtTotalExpenditure New value of property txtTotalExpenditure.
     */
    public void setTxtTotalExpenditure(java.lang.String txtTotalExpenditure) {
        this.txtTotalExpenditure = txtTotalExpenditure;
    }
    
    /**
     * Getter for property lblProfitOrLoss.
     * @return Value of property lblProfitOrLoss.
     */
    public java.lang.String getLblProfitOrLoss() {
        return lblProfitOrLoss;
    }
    
    /**
     * Setter for property lblProfitOrLoss.
     * @param lblProfitOrLoss New value of property lblProfitOrLoss.
     */
    public void setLblProfitOrLoss(java.lang.String lblProfitOrLoss) {
        this.lblProfitOrLoss = lblProfitOrLoss;
    }
    
}