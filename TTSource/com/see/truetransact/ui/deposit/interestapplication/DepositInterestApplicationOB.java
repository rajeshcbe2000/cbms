/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositInterestApplicationOB.java
 *
 * Created on Mon Jun 13 18:24:58 IST 2011
 */

package com.see.truetransact.ui.deposit.interestapplication;


import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import javax.swing.DefaultListModel;

/**
 *
 * @author
 */

public class DepositInterestApplicationOB extends CObservable{
    
    
    
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    
    private EnhancedTableModel tblDepositInterestApplication;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(DepositInterestApplicationOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private List finalList = null;
    private List finalTableList = null;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList rdList;
    private String txtProductID = "";
    private String txtTokenNo = "";
    private List calFreqAccountList = null;
    private String cboSIProductId = "";
    private ComboBoxModel cbmSIProductId;
    private ComboBoxModel cbmOAProductID;
    private DefaultListModel  lstSelectedTransaction=new  DefaultListModel();
    private LinkedHashMap newTransactionMap=new LinkedHashMap();
    HashMap cashtoTransferMap =new HashMap();
    private ComboBoxModel cbmProdType;
    private HashMap lookUpHash;
    
    public DepositInterestApplicationOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DepositInterestApplicationJNDI");
            map.put(CommonConstants.HOME, "deposit.interestapplication.DepositInterestApplicationHome");
            map.put(CommonConstants.REMOTE, "deposit.interestapplication.DepositInterestApplication");
            setDepositInterestTableTitle();
            fillDropdown();
            tblDepositInterestApplication = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void setDepositInterestTableTitle(){
        tableTitle.add("Select");
        tableTitle.add("Cust ID");
        tableTitle.add("Account No");
        tableTitle.add("Name");
        tableTitle.add("Dep Amt");
        tableTitle.add("Dep Date");
        tableTitle.add("Mat Date");
        tableTitle.add("From Date");
        tableTitle.add("To Date");
        tableTitle.add("Interest");
        tableTitle.add("SI A/c No");
        tableTitle.add("Cal Freq");
        tableTitle.add("Dep Type");
        tableTitle.add("Status");
        tableTitle.add("TDS");// Added by nithya on 27-01-2021 for KD-2648
        IncVal = new ArrayList();
    }
    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("In getKeyValue()");

        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }
     public void setCbmProdId(String prodType) {
  
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } 
           else {
                try {
                  /*  lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));*/
                     param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"Cash.getAccProduct"+prodType);
            param.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmOAProductID= new ComboBoxModel(key,value);
                   
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
      //  cbmOAProductID = new ComboBoxModel(key, value);
     //   this.cbmOAProductID = cbmOAProductID;
     //   setChanged();
        
        
    }
  public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
        setChanged();
    }

    ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    private void fillDropdown() throws Exception{
        try{
             lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdType = new ComboBoxModel(key, value);
            //cbmProdType.removeKeyAndElement("SA");
            //cbmProdType.removeKeyAndElement("AB");
            cbmProdType.removeKeyAndElement("AD");
            cbmProdType.removeKeyAndElement("TD");
            cbmProdType.removeKeyAndElement("TL");
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"getSIProducts");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmSIProductId = new ComboBoxModel(key,value);
            
            
         /* param = new HashMap();
            param.put(CommonConstants.MAP_NAME,"Cash.getAccProductOA");
            param.put(CommonConstants.PARAMFORQUERY, null);
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap)keyValue.get(CommonConstants.DATA));
            cbmOAProductID= new ComboBoxModel(key,value);*/
            lstSelectedTransaction = new DefaultListModel();
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
   
    public void insertTableData(HashMap whereMap){
        try{
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            dataMap.putAll(whereMap);
            ArrayList list=new ArrayList();
            System.out.println("dataMap : "+dataMap+"whereMap : "+whereMap+"map : "+map);
            whereMap.put("BRANCH_ID",TrueTransactMain.selBranch);
            HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
            if (proxyResultMap != null && proxyResultMap.size()>0) {
                tableList = (ArrayList) proxyResultMap.get("INTEREST_DATA");
                list=(ArrayList) proxyResultMap.get("RD_DATA");
               if(dataMap.containsKey("EXCLUDE_LIEN")){
                  /* for(int k=0;k<tableList.size();k++){
                     ArrayList  interestList = (ArrayList) tableList.get(k);
                      String  depNo = CommonUtil.convertObjToStr(interestList.get(2));
                      for(int m=0;m<list.size();m++){
                          HashMap lienMap=(HashMap) list.get(m);
                          if(lienMap.get("CHANGE_COLOR").toString().equals("TRUE")){
                              if(depNo.equals(lienMap.get("ACT_NUM").toString())){
                                  tableList.remove(k);
                                  list.remove(m);
                                  k=k-1;
                                  m=m-1;
                              }
                          }
                      }
                      if(CommonUtil.convertObjToDouble(interestList.get(9))<=0.0){
                          tableList.remove(k);
                           k=k-1;
                      }
                   }*/
                setAccountsList(list);
                setFinalList(tableList);
               }else{
                    for(int k=0;k<tableList.size();k++){
                     ArrayList  interestList = (ArrayList) tableList.get(k);
                      if(CommonUtil.convertObjToDouble(interestList.get(9))<=0.0){
                          tableList.remove(k);
                           k=k-1;
                      }
                    }
                   setAccountsList(list);
                   setFinalList(tableList);
               }
            }
//            ProxyParameters.BRANCH_ID = TrueTransactMain.selBranch;
          //tblDepositInterestApplication= new EnhancedTableModel((ArrayList)tableList, tableTitle);
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
    public void updateInterestData(){
        tblDepositInterestApplication= new EnhancedTableModel((ArrayList)finalList, tableTitle);
    }
    public void setAccountsList(ArrayList rdList){
        this.rdList=rdList;
    }
    public ArrayList getAccountsList(){
        return rdList;
    }
    
    /** To perform the necessary operation */
    public void doAction(List finalTableList) {
        TTException exception = null;
        log.info("In doAction()");
        try {
            doActionPerform(finalTableList);
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if(e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            parseException.logException(exception, true);
            setResult(actionType);
        }
    }

     public void addTargetTransactionList(String selectTransaction){
         if (lstSelectedTransaction.contains(selectTransaction)) {
             ClientUtil.showMessageWindow("This deposit no has been already added, kindly select different deposit no!!!!!!!!!");
             return;
         } else {
             lstSelectedTransaction.addElement(selectTransaction);
             HashMap singleMap = new HashMap();
             singleMap.put("SELECTED_DEPOSITS", selectTransaction);
             newTransactionMap.put(selectTransaction, singleMap);
         }
    }
      public void removeTargetALLTransactionList()
      {
           lstSelectedTransaction.removeAllElements();
      }
     
     public void removeTargetTransactionList(int selectTransaction){
     
         lstSelectedTransaction.removeElementAt(selectTransaction);
         HashMap singleMap=new HashMap();
         singleMap.put("SELECTED_DEPOSITS",selectTransaction);
         newTransactionMap.put(selectTransaction,singleMap);
     }

     public String getListDeposits(){
         StringBuffer buffer =new StringBuffer();
         if(lstSelectedTransaction !=null && lstSelectedTransaction.size()>0){
             for(int i=0;i<lstSelectedTransaction.size();i++){
                 buffer.append("'"+CommonUtil.convertObjToStr(lstSelectedTransaction.get(i))+"'");
                 if(i !=lstSelectedTransaction.size()-1)
                    buffer.append(",");
             }
         }
         return buffer.toString();
     }
     
    
    /** To perform the necessary action */
    private void doActionPerform(List finalTableList) throws Exception{
        final HashMap data = new HashMap();
        if(finalTableList!= null && finalTableList.size()>0){
            data.put("ACCOUNT_LIST",finalTableList);
        }else  if(getCalFreqAccountList()!= null && getCalFreqAccountList().size()>0){
            
                data.put("CAL_FREQ_ACCOUNT_LIST", getCalFreqAccountList());
            }
        if(cashtoTransferMap !=null && cashtoTransferMap.size()>0){
            data.put("CASHTOTRANSFER",cashtoTransferMap);
            data.put("ACT_NO_LIST",getListDeposits());
            
        }
            data.put(CommonConstants.PRODUCT_ID,getTxtProductID());
            data.put("DO_TRANSACTION",new Boolean(true));
            data.put("TOKEN_NO", getTxtTokenNo());
        try {
            HashMap proxyResultMap = proxy.execute(data, map);
            setProxyReturnMap(proxyResultMap);
            setResult(getActionType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void resetForm(){
        setFinalList(null);
        resetTableValues();
        setChanged();
    }
     
     public void resetTableValues(){
        tblDepositInterestApplication.setDataArrayList(null,tableTitle);
    }
    
    /**
     * Getter for property tblDepositInterestApplication.
     * @return Value of property tblDepositInterestApplication.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDepositInterestApplication() {
        return tblDepositInterestApplication;
    }    
      
    /**
     * Setter for property tblDepositInterestApplication.
     * @param tblDepositInterestApplication New value of property tblDepositInterestApplication.
     */
    public void setTblDepositInterestApplication(com.see.truetransact.clientutil.EnhancedTableModel tblDepositInterestApplication) {
        this.tblDepositInterestApplication = tblDepositInterestApplication;
    }    
    
    /**
     * Getter for property rdoPrizedMember_Yes.
     * @return Value of property rdoPrizedMember_Yes.
     */
    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }
    
    /**
     * Setter for property rdoPrizedMember_Yes.
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }
    
    /**
     * Getter for property rdoPrizedMember_No.
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }
    
    /**
     * Setter for property rdoPrizedMember_No.
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }
    
    /**
     * Getter for property finalList.
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }    
    
    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }    
    
    /**
     * Getter for property txtProductID.
     * @return Value of property txtProductID.
     */
    public java.lang.String getTxtProductID() {
        return txtProductID;
    }
    
    /**
     * Setter for property txtProductID.
     * @param txtProductID New value of property txtProductID.
     */
    public void setTxtProductID(java.lang.String txtProductID) {
        this.txtProductID = txtProductID;
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
    
    /**
     * Getter for property tableTitle.
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }
    
    /**
     * Getter for property txtTokenNo.
     * @return Value of property txtTokenNo.
     */
    public java.lang.String getTxtTokenNo() {
        return txtTokenNo;
    }
    
    /**
     * Setter for property txtTokenNo.
     * @param txtTokenNo New value of property txtTokenNo.
     */
    public void setTxtTokenNo(java.lang.String txtTokenNo) {
        this.txtTokenNo = txtTokenNo;
    }
    
    /**
     * Getter for property calFreqAccountList.
     * @return Value of property calFreqAccountList.
     */
    public java.util.List getCalFreqAccountList() {
        return calFreqAccountList;
    }
    
    /**
     * Setter for property calFreqAccountList.
     * @param calFreqAccountList New value of property calFreqAccountList.
     */
    public void setCalFreqAccountList(java.util.List calFreqAccountList) {
        this.calFreqAccountList = calFreqAccountList;
    }
    
    /**
     * Getter for property cboSIProductId.
     * @return Value of property cboSIProductId.
     */
    public java.lang.String getCboSIProductId() {
        return cboSIProductId;
    }
    
    /**
     * Setter for property cboSIProductId.
     * @param cboSIProductId New value of property cboSIProductId.
     */
    public void setCboSIProductId(java.lang.String cboSIProductId) {
        this.cboSIProductId = cboSIProductId;
    }
    
    /**
     * Getter for property cbmSIProductId.
     * @return Value of property cbmSIProductId.
     */
    public ComboBoxModel getCbmSIProductId() {
        return cbmSIProductId;
    }
    
    /**
     * Getter for property lstSelectedTransaction.
     * @return Value of property lstSelectedTransaction.
     */
    public javax.swing.DefaultListModel getLstSelectedTransaction() {
        return lstSelectedTransaction;
    }
    
    /**
     * Setter for property lstSelectedTransaction.
     * @param lstSelectedTransaction New value of property lstSelectedTransaction.
     */
    public void setLstSelectedTransaction(javax.swing.DefaultListModel lstSelectedTransaction) {
        this.lstSelectedTransaction = lstSelectedTransaction;
    }
    
    /**
     * Getter for property cashtoTransferMap.
     * @return Value of property cashtoTransferMap.
     */
    public HashMap getCashtoTransferMap() {
        return cashtoTransferMap;
    }
    
    /**
     * Setter for property cashtoTransferMap.
     * @param cashtoTransferMap New value of property cashtoTransferMap.
     */
    public void setCashtoTransferMap(HashMap cashtoTransferMap) {
        this.cashtoTransferMap = cashtoTransferMap;
    }
    
    /**
     * Getter for property cbmOAProductID.
     * @return Value of property cbmOAProductID.
     */
    public ComboBoxModel getCbmOAProductID() {
        return cbmOAProductID;
    }
    
    /**
     * Setter for property cbmOAProductID.
     * @param cbmOAProductID New value of property cbmOAProductID.
     */
    public void setCbmOAProductID(ComboBoxModel cbmOAProductID) {
        this.cbmOAProductID = cbmOAProductID;
    }
       //added by rishad for checking account number 08/09/2016
      public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData = new HashMap();
        boolean isExists = false;
        try {//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                actNum = CommonUtil.convertObjToStr(mapData.get("ACT_NUM"));
                isExists = true;
            } else {
                isExists = false;
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    
}