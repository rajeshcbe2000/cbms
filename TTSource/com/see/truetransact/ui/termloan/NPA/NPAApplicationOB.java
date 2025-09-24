/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NPAApplicationOB.java
 *
 * Created on Tue Jul 03 11:12:08 GMT+05:30 2007
 */

package com.see.truetransact.ui.termloan.NPA;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.ui.TrueTransactMain;

/**
 *
 * @author
 */

public class NPAApplicationOB extends CObservable{
    
    private String cboBranches;
    private ComboBoxModel cbmBranches;
    private String cboProductID;
    private ComboBoxModel cbmProductID;
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy;
    private HashMap operationMap;
    private static final Logger log=Logger.getLogger(NPAApplicationOB.class);
    private static NPAApplicationOB npaApplicaionob;
//    static {
//        try{
//            npaApplicaionob=new NPAApplicationOB();
//        }
//        catch(Exception e){
//            
//        }
//    }
    // public  NPAApplicationOB{
    // fillDropDown();
    //}
    
    public NPAApplicationOB(){
        try{
            setOperationMap();
            fillDropdown();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // fillDropDown(){
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        HashMap mapWhere=new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,"getOwnBranches");
        mapWhere.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
        mapWhere.put("BRANCH_GROUP", TrueTransactMain.FOREIGN_BRANCH_GROUP);
        
        //        ArrayList lookup_keys = new ArrayList();
        //        lookup_keys.add("LOAN.FREQUENCY");
        //        lookup_keys.add("TERM_LOAN.REPAYMENT_TYPE");
        getProductType();
//        lookUpHash.put(CommonConstants.PARAMFORQUERY, "");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, mapWhere);
        keyValue = (HashMap)ClientUtil.populateLookupData(lookUpHash).get(CommonConstants.DATA);
        getKeyValue(keyValue);
         key.add("ALL");
        value.add("ALL_BRANCHES");
        setCbmBranches(new ComboBoxModel(key,value));
//        cbmBranches = new ComboBoxModel((ArrayList)keyValue.get(CommonConstants.KEY),(ArrayList)keyValue.get(CommonConstants.VALUE));   
//        HashMap keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
//        getKeyValue(keyValue);
        
        
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    void setOperationMap(){
        try{
            proxy=ProxyFactory.createProxy();
            operationMap = new HashMap();
            operationMap.put(CommonConstants.JNDI, "TermLoanJNDI");
            operationMap.put(CommonConstants.HOME, "termloan.TermLoanHome");
            operationMap.put(CommonConstants.REMOTE, "termloan.TermLoan");
        }
        catch(Exception e){
            e.printStackTrace();
            log.info("TermLoanNPA..."+e);
            
        }
        
        
    }
    
    public static NPAApplicationOB getInstance(){
        return npaApplicaionob;
    }
    
    public void getProductType()throws Exception{
        HashMap keyValue=new HashMap();
        HashMap whereMap=new HashMap();
        whereMap.put(CommonConstants.MAP_NAME,"get_NPA_Product");
        whereMap.put("NPA","NPA");
        HashMap hash=proxy.executeQuery(whereMap, operationMap);
        getMap((List)hash.get("NPAProductList"));
        
        setCbmProductID(new ComboBoxModel(key,value));
//        whereMap.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
//        List branchLst=ClientUtil.executeQuery("getOwnBranches", whereMap);
//        getMap(branchLst);
//        setCbmBranches(new ComboBoxModel(key,value));
//        getKeyValue(keyValue);
        System.out.println("hash###!!!!!!"+hash);
//        if(hash.size()>0)
//            for(int i=0;i<hash.size();i++){
//                HashMap map=(HashMap)hash.get(i);
//                
//            }
    }

    private void getMap(List list) throws Exception{
        key = new ArrayList();
        value = new ArrayList();
        System.out.println("keyvalue oflist  :"+list);
        //The first values in the ArrayList key and value are empty String to display the
        //first row of all dropdowns to be empty String
        key.add("ALL");
        value.add("ALL_PRODUCT");
        for (int i=0, j=list.size(); i < j; i++) {
            key.add(((HashMap)list.get(i)).get("KEY"));
            value.add(((HashMap)list.get(i)).get("VALUE"));
        }
//        keyValue = new HashMap();
//        keyValue.put("KEY", key);
//        keyValue.put("VALUE", value);
//        return keyValue;
    }    
    
    
    public  void doAction()throws Exception{
    
        doActionPerform();
    }
    private void doActionPerform()throws Exception{
        HashMap map =new HashMap();
        if(!getCboBranches().equals("ALL"))
            map.put("BRANCH_CODE",getCboBranches());
        
        if(!getCboProductID().equals("ALL"))
            map.put("PROD_ID",getCboProductID());
        map.put("NPA","NPA");
        map.put("STATUS_BY",TrueTransactMain.USER_ID);
        map.put("TODAY_DT",ClientUtil.getCurrentDate());
        System.out.println("callexecute###"+map);
        HashMap proxyResultMap=proxy.execute(map, operationMap);
    }
    
    /**
     * Getter for property cboBranches.
     * @return Value of property cboBranches.
     */
    public java.lang.String getCboBranches() {
        return cboBranches;
    }
    
    /**
     * Setter for property cboBranches.
     * @param cboBranches New value of property cboBranches.
     */
    public void setCboBranches(java.lang.String cboBranches) {
        this.cboBranches = cboBranches;
    }
    
    /**
     * Getter for property cbmBranches.
     * @return Value of property cbmBranches.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBranches() {
        return cbmBranches;
    }
    
    /**
     * Setter for property cbmBranches.
     * @param cbmBranches New value of property cbmBranches.
     */
    public void setCbmBranches(com.see.truetransact.clientutil.ComboBoxModel cbmBranches) {
        this.cbmBranches = cbmBranches;
    }
    
    /**
     * Getter for property cbmProductID.
     * @return Value of property cbmProductID.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductID() {
        return cbmProductID;
    }
    
    /**
     * Setter for property cbmProductID.
     * @param cbmProductID New value of property cbmProductID.
     */
    public void setCbmProductID(com.see.truetransact.clientutil.ComboBoxModel cbmProductID) {
        this.cbmProductID = cbmProductID;
    }
    
    /**
     * Getter for property cboProductID.
     * @return Value of property cboProductID.
     */
    public java.lang.String getCboProductID() {
        return cboProductID;
    }
    
    /**
     * Setter for property cboProductID.
     * @param cboProductID New value of property cboProductID.
     */
    public void setCboProductID(java.lang.String cboProductID) {
        this.cboProductID = cboProductID;
    }
    public static  void main(String arg[]) {
        NPAApplicationOB npaApplicationOB=new NPAApplicationOB();
        
    }
}