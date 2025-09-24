/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSBankAdvanceOB.java
 *
 * Created on Mon Jun 13 18:24:58 IST 2011
 */

package com.see.truetransact.ui.gdsapplication.gdsbankadvance;


import com.see.truetransact.ui.mdsapplication.mdsbankadvance.*;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.clientexception.ClientParseException;
import java.util.*;
/**
 *
 * @author Suresh
 */

public class GDSBankAdvanceOB extends CObservable{
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private HashMap _authorizeMap;
    private EnhancedTableModel tblMDSBankAdvance;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(GDSBankAdvanceOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private List finalList = null;
    private String txtSchemeName = "";
    private String txtTotalAmount = "";
    private String totalBonusAmount = "";
    private String bankAdvId = "";
    private static Date currDt = null;
    private String txtGroupNo = "";
    private String commonScheme = "";
    private int schemeCount = 0;
    
    public GDSBankAdvanceOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "GDSBankAdvanceJNDI");
            map.put(CommonConstants.HOME, "GDSBankAdvanceHome");
            map.put(CommonConstants.REMOTE, "GDSBankAdvance");
            setStandingTableTile();
            tblMDSBankAdvance = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void setStandingTableTile(){
        tableTitle.add("Chittal No");
        tableTitle.add("Sub No");
        tableTitle.add("Member No");
        tableTitle.add("Member Name");
        tableTitle.add("Installment Amount");
        tableTitle.add("Bonus Amout");
        IncVal = new ArrayList();
    }
    public void insertTableDataEdit(HashMap whereMap){
        try{
            String bank_AdvId = CommonUtil.convertObjToStr(whereMap.get("BANK_ADV_ID"));
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            List bankAdvanceList = ClientUtil.executeQuery("getGDSBankAdvDetailsEdit", whereMap);
            System.out.println("#$@$@$#@$@#$@ List : "+bankAdvanceList);
            if(bankAdvanceList!=null && bankAdvanceList.size()>0){
                for (int i = 0;i<bankAdvanceList.size();i++){
                    dataMap = (HashMap) bankAdvanceList.get(i);
                    double instAmount = CommonUtil.convertObjToDouble(dataMap.get("INST_AMT"))*schemeCount;
                    double bonusAmount = CommonUtil.convertObjToDouble(dataMap.get("BONUS_AMT"))*schemeCount;
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("CHITTAL_NO"));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("SUB_NO")));
                    rowList.add(dataMap.get("MEMBER_NO"));
                    rowList.add(dataMap.get("MEMBER_NAME"));
                    rowList.add(instAmount);
                    rowList.add(bonusAmount);
                    tableList.add(rowList);
                }
                System.out.println("#$# bankAdvList:"+bankAdvanceList);
                setFinalList(bankAdvanceList);
                tblMDSBankAdvance= new EnhancedTableModel((ArrayList)tableList, tableTitle);
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
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
   
    public void insertTableData(HashMap whereMap){
        try{
            String isBonusTrans = "";
            String scheme_Name = CommonUtil.convertObjToStr(whereMap.get("SCHEME_NAME"));
            HashMap schemeMap = new HashMap();
            HashMap productMap = new HashMap();
            schemeMap.put("SCHEME_NAME",getCommonScheme());
            List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead",schemeMap);
            if(lst!=null && lst.size()>0){
                productMap = (HashMap)lst.get(0);
            }
            HashMap checkMap = new HashMap();
            checkMap.put("SCHEME_NAME", commonScheme);
            List list = ClientUtil.executeQuery("getifBonusTransactionRequired", checkMap);
            if (list != null && list.size() > 0) {
                checkMap = (HashMap) list.get(0);
                if (checkMap.containsKey("IS_BONUS_TRANSFER") && checkMap.get("IS_BONUS_TRANSFER") != null && !"".equalsIgnoreCase(CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER")))) {
                    isBonusTrans = CommonUtil.convertObjToStr(checkMap.get("IS_BONUS_TRANSFER"));
                }
            }
            double totBonusAmt = 0;
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            Date currDate = (Date) currDt.clone();
            whereMap.put("CURRENT_DT",currDate);
            whereMap.put("SCHEME_COUNT",getSchemeCount());
            String advCollection="N";
            List advColl= ClientUtil.executeQuery("checkMdsAdv", whereMap);
            if(advColl!=null && advColl.size()>0)
            {
                HashMap amap=new HashMap();
                amap=(HashMap)advColl.get(0);
                if(amap!=null && amap.containsKey("ADVANCE_COLLECTION") && amap.get("ADVANCE_COLLECTION")!=null)
                {
                advCollection=amap.get("ADVANCE_COLLECTION").toString();
               }
            }
            List bankAdvList;
            if(advCollection.equals("N"))
            {
                  bankAdvList = ClientUtil.executeQuery("getGDSBankAdvanceDetailsForNormal", whereMap);
            }else
            {
             bankAdvList = ClientUtil.executeQuery("getGDSBankAdvanceDetails", whereMap);
            
            }
            System.out.println("#$@$@$#@$@#$@ List : "+bankAdvList);
            if(bankAdvList!=null && bankAdvList.size()>0){
                for (int i = 0;i<bankAdvList.size();i++){
                    dataMap = (HashMap) bankAdvList.get(i);
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("CHITTAL_NO"));
                    rowList.add(CommonUtil.convertObjToStr(dataMap.get("SUB_NO")));
                    rowList.add(dataMap.get("MEMBER_NO"));
                    rowList.add(dataMap.get("MEMBER_NAME"));                    
                    totBonusAmt = CommonUtil.convertObjToDouble(dataMap.get("BONUS")).doubleValue();
                    if(!productMap.get("MULTIPLE_MEMBER").equals("") && (productMap.get("MULTIPLE_MEMBER").equals("Y"))){
                        whereMap = new HashMap();
                        int noOfCoChittal = 0;
                        whereMap.put("CHITTAL_NUMBER",dataMap.get("CHITTAL_NO"));
                        whereMap.put("SCHEME_NAME",scheme_Name);
                        List applicationLst=ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
                        if(applicationLst!=null && applicationLst.size()>0){
                            noOfCoChittal = applicationLst.size();
                            totBonusAmt=totBonusAmt/noOfCoChittal;
                            dataMap.put("BONUS",String.valueOf(totBonusAmt));
                        }
                    }
                    if(productMap.get("BONUS_ROUNDING")!=null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt>0 ){
                        Rounding rod =new Rounding();
                        if(productMap.get("BONUS_ROUNDOFF")!=null &&  productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")){
                        totBonusAmt = (double)rod.getNearest((long)(totBonusAmt *100),100)/100;
                        }else{
                            totBonusAmt = (double)rod.lower((long)(totBonusAmt *100),100)/100;
                        }
                    }
                    dataMap.put("BONUS",String.valueOf(totBonusAmt));
                    if(isBonusTrans.equalsIgnoreCase("N")){
                        double installmentAmount = CommonUtil.convertObjToDouble((dataMap.get("INST_AMT"))) - totBonusAmt;
                        rowList.add(installmentAmount);
                    }else{
                        rowList.add(dataMap.get("INST_AMT"));
                    }
                    
                    if(isBonusTrans.equalsIgnoreCase("N")){
                       totBonusAmt = 0; 
                    }
                    rowList.add(String.valueOf(totBonusAmt));
                    tableList.add(rowList);
                }
                System.out.println("#$# bankAdvList:"+bankAdvList);
                setFinalList(bankAdvList);
                tblMDSBankAdvance= new EnhancedTableModel((ArrayList)tableList, tableTitle);
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    /** To perform the necessary operation */
     public void doAction() {
        try {
            doActionPerform(finalList);
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }

    
    /** To perform the necessary action */
    private void doActionPerform(List finalTableList) throws Exception{
        final HashMap data = new HashMap();
        data.put("COMMAND",getCommand());
        if(get_authorizeMap() != null && get_authorizeMap().size()>0){
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
            data.put("BANK_ADV_ID", getBankAdvId());
        }
        if(finalTableList!= null && finalTableList.size()>0){
            data.put("MDS_BANK_ADVANCE",finalTableList);
            data.put("TOTAL_AMOUNT",getTxtTotalAmount());
            data.put("BONUS_AMOUNT",getTotalBonusAmount());
            data.put("SCHEME_NAME",getCommonScheme());
            data.put("GROUP_NO",getTxtGroupNo());
            data.put(CommonConstants.SCREEN, getScreen());
        }
        System.out.println("Data in MDS BAND ADVANCE OB: " + data);
        // comment to be removed
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        if(proxyResultMap.containsKey("BANK_ADV_ID")){
            setBankAdvId(CommonUtil.convertObjToStr(proxyResultMap.get("BANK_ADV_ID")));
        }
        setResult(getActionType());
    }
    
    public void resetForm(){
        setTxtSchemeName("");
        resetTableValues();
        setChanged();
    }
     
     public void resetTableValues(){
        tblMDSBankAdvance.setDataArrayList(null,tableTitle);
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
     * Getter for property tblMDSBankAdvance.
     * @return Value of property tblMDSBankAdvance.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblMDSBankAdvance() {
        return tblMDSBankAdvance;
    }
    
    /**
     * Setter for property tblMDSBankAdvance.
     * @param tblMDSBankAdvance New value of property tblMDSBankAdvance.
     */
    public void setTblMDSBankAdvance(com.see.truetransact.clientutil.EnhancedTableModel tblMDSBankAdvance) {
        this.tblMDSBankAdvance = tblMDSBankAdvance;
    }
    
    /**
     * Getter for property txtSchemeName.
     * @return Value of property txtSchemeName.
     */
    public java.lang.String getTxtSchemeName() {
        return txtSchemeName;
    }
    
    /**
     * Setter for property txtSchemeName.
     * @param txtSchemeName New value of property txtSchemeName.
     */
    public void setTxtSchemeName(java.lang.String txtSchemeName) {
        this.txtSchemeName = txtSchemeName;
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
    
     /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
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
     * Getter for property txtTotalAmount.
     * @return Value of property txtTotalAmount.
     */
    public java.lang.String getTxtTotalAmount() {
        return txtTotalAmount;
    }
    
    /**
     * Setter for property txtTotalAmount.
     * @param txtTotalAmount New value of property txtTotalAmount.
     */
    public void setTxtTotalAmount(java.lang.String txtTotalAmount) {
        this.txtTotalAmount = txtTotalAmount;
    }
    
    /**
     * Getter for property totalBonusAmount.
     * @return Value of property totalBonusAmount.
     */
    public java.lang.String getTotalBonusAmount() {
        return totalBonusAmount;
    }
    
    /**
     * Setter for property totalBonusAmount.
     * @param totalBonusAmount New value of property totalBonusAmount.
     */
    public void setTotalBonusAmount(java.lang.String totalBonusAmount) {
        this.totalBonusAmount = totalBonusAmount;
    }
    
    /**
     * Getter for property bankAdvId.
     * @return Value of property bankAdvId.
     */
    public java.lang.String getBankAdvId() {
        return bankAdvId;
    }
    
    /**
     * Setter for property bankAdvId.
     * @param bankAdvId New value of property bankAdvId.
     */
    public void setBankAdvId(java.lang.String bankAdvId) {
        this.bankAdvId = bankAdvId;
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

    public String getTxtGroupNo() {
        return txtGroupNo;
    }

    public void setTxtGroupNo(String txtGroupNo) {
        this.txtGroupNo = txtGroupNo;
    }

    public String getCommonScheme() {
        return commonScheme;
    }

    public void setCommonScheme(String commonScheme) {
        this.commonScheme = commonScheme;
    }

    public int getSchemeCount() {
        return schemeCount;
    }

    public void setSchemeCount(int schemeCount) {
        this.schemeCount = schemeCount;
    }
    
}