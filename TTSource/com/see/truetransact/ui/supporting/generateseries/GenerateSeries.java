/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GenerateSeries.java
 *
 * Created on June 20, 2005, 9:22 AM
 */

package com.see.truetransact.ui.supporting.generateseries;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;

import com.see.truetransact.clientutil.ClientUtil;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.supporting.inventory.InventoryDetailsDAO;
import com.see.truetransact.transferobject.supporting.inventory.InventoryDetailsTO;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author  152721
 */
public class GenerateSeries {
    private static SqlMap sqlMap = null;
    
    private String prodType = "";
    private String actNum = "";
    private String userID = "";
    private String branchCode= "";
    private String transID = "";
    private String itemType = "";
    private String itemSubType = "";
    private long books = 0;
    private long leaves = 0;
    
    private final String TRANSID= "TRANSACTION_ID";
    
    
    /** Creates a new instance of GenerateSeries */
    public GenerateSeries(String itemType, String branchID, String userID) throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        
        //__ Initialize the data...
        setItemType(itemType);
        setBranchCode(branchID);
        setUserID(userID);
    }
    
    
    //__ To get The Values for the Serial Numbers and the Leaves No...
    private HashMap getBookSeriesData(HashMap dataMap) throws Exception{
        HashMap result = new HashMap();
        System.out.println("dataMap in getBookSeriesData():  " + dataMap);
        
        final List transOut = ClientUtil.executeQuery("GS.getTransSeriesOut", dataMap);
        if(transOut.size() > 0){
            final List transBoth = ClientUtil.executeQuery("GS.getTransSeries", dataMap);
            int size = transBoth.size();
            if(size > 0){
                HashMap transBothMap = (HashMap)transBoth.get(0);
                System.out.println("Data Map : " + transBothMap);
                
                HashMap transOutMap = (HashMap)transOut.get(0);
                
                long bothBookFrom = CommonUtil.convertObjToLong(transBothMap.get("BOOK_SLNO_FROM"));
                long bothBookTo = CommonUtil.convertObjToLong(transBothMap.get("BOOK_SLNO_TO"));
                
                long outBookTo = CommonUtil.convertObjToLong(transOutMap.get("BOOK_SLNO_TO"));
                long outLeavesFrom = CommonUtil.convertObjToLong(transOutMap.get("LEAVES_SLNO_FROM"));
                
                System.out.println("bothBookFrom: "+ bothBookFrom);
                System.out.println("bothBookTo: "+ bothBookTo);
                System.out.println("outBookTo: "+ outBookTo);
                
                //__ Checking is already done in the Query...
                //__ If the Current Series, still has some Books to be issued...
                
                //__ if the current Series is already in use...
                if( (outBookTo >= bothBookFrom) && (outBookTo <= bothBookTo)){
                    result.put("BOOK_SLNO_FROM",String.valueOf(outBookTo + 1));
                    result.put("LEAVES_SLNO_FROM",String.valueOf(outLeavesFrom+1));
                    
                }else{
                    result.put("BOOK_SLNO_FROM",String.valueOf(bothBookFrom));
                    result.put("LEAVES_SLNO_FROM",String.valueOf(transBothMap.get("LEAVES_SLNO_FROM")));
                }
                
                result.put("BOOK_SLNO_TO",CommonUtil.convertObjToStr(transBothMap.get("BOOK_SLNO_TO")));
                result.put("INSTRUMENT_PREFIX",CommonUtil.convertObjToStr(transBothMap.get("INSTRUMENT_PREFIX")));
                result.put("TRANS_ID", CommonUtil.convertObjToStr(transBothMap.get("TRANS_ID")));
                setTransID(CommonUtil.convertObjToStr(transBothMap.get("TRANS_ID")));
            }
        }else{
            final List transIn = ClientUtil.executeQuery("GS.getTransSeriesIn", dataMap);
            if(transIn.size() > 0){
                result = (HashMap)transIn.get(0);
                System.out.println("Trans Id: "+ CommonUtil.convertObjToStr(result.get("TRANS_ID")));
                setTransID(CommonUtil.convertObjToStr(result.get("TRANS_ID")));
            }
        }
        System.out.println("ResultMap Generated: " + result);
        
        return result;
    }
    
    
    //__ Decoration of the data ...
    public HashMap seriesData(HashMap inputMap) throws Exception{
//        HashMap inputMap = new HashMap();
//        inputMap.put("ITEM_TYPE",getItemType());
//        inputMap.put("ITEM_SUB_TYPE",getItemSubType());
//        inputMap.put("LEAVES",String.valueOf(getLeaves()));;
//        inputMap.put(CommonConstants.BRANCH_ID,getBranchCode());
        
        setProdType(CommonUtil.convertObjToStr(inputMap.get("PRODUCT TYPE")));
        setActNum(CommonUtil.convertObjToStr(inputMap.get("ACCOUNT NUMBER")));
        setItemSubType(CommonUtil.convertObjToStr(inputMap.get("USAGE")));
        setBooks(CommonUtil.convertObjToLong(inputMap.get("NO OF BOOKS")));
        setLeaves(CommonUtil.convertObjToLong(inputMap.get("NO OF LEAVES")));
        
        //__ To set the Item Type...
        inputMap.put("ITEM_TYPE",getItemType());
        
        HashMap resultMap = new HashMap();
        resultMap = getBookSeriesData(inputMap);
        
        if(resultMap.containsKey("BOOK_SLNO_FROM")){
            long chequeBooks = getBooks();
            long seriesFrom = CommonUtil.convertObjToLong(resultMap.get("BOOK_SLNO_FROM"));
            long seriesTo = CommonUtil.convertObjToLong(resultMap.get("BOOK_SLNO_TO"));
            
            long difference = (seriesTo - seriesFrom) + 1;
            
            System.out.println("In Generate Series...");
            
            System.out.println("chequeBooks: " + chequeBooks);
            System.out.println("seriesFrom: " + seriesFrom);
            System.out.println("seriesTo: " + seriesTo);
            System.out.println("difference: " + difference);
            
            if(chequeBooks > 0){
                if(difference >=  chequeBooks){
                    final String prefix = CommonUtil.convertObjToStr(resultMap.get("INSTRUMENT_PREFIX"));
                    long leavesFrom = CommonUtil.convertObjToLong(resultMap.get("LEAVES_SLNO_FROM"));
                    long chequeLeaves = getLeaves();
                    long toSeries = seriesFrom + chequeBooks - 1;
                    
                    System.out.println("leavesFrom: " + leavesFrom);
                    System.out.println("chequeLeaves: " + chequeLeaves);
                    System.out.println("toSeries: " + toSeries);
                    
                    resultMap.put("BK_SERIES_FROM", String.valueOf(seriesFrom));
                    resultMap.put("BK_SERIES_TO", String.valueOf(toSeries));
                    resultMap.put("START_LEAF_NO1", prefix);
                    resultMap.put("START_LEAF_NO2", String.valueOf(leavesFrom));
                    resultMap.put("END_LEAF_NO1", prefix);
                    resultMap.put("END_LEAF_NO2", String.valueOf(leavesFrom - 1 + (chequeLeaves * chequeBooks)));
                    
                    
                    //__ if the Series is Over...
                    if(toSeries >= seriesTo){
                        resultMap.put("SERIES_OVER", "YES");
                        //__ To update the data in Inventory Details...
                        sqlMap.executeUpdate("InventoryDetail.seriesOver", resultMap);
                    }
                    
                    //__ To Enter the Row in Inventory Details...
                    final String transID = setInventoryDetailsData(resultMap);
                    resultMap.put("TRANS_OUT_ID",transID);
                    resultMap.put("TRANSACTION ID",transID);
                    
                    //__ To Authorize the data entered in the inventory Detail...
                    sqlMap.executeUpdate("authInventoryDetails", resultMap);
                    
                    long availBooks = getBooks();
                    availBooks = -availBooks;
                    resultMap.put("BOOK QUANTITY", availBooks);
                    
                    //__ To update the data in the Inventory Master...
                    sqlMap.executeUpdate("updateInventoryMasterAvailBooks", resultMap);
                }else{
                    resultMap.put("AVAILABLE_BOOKS",String.valueOf(difference));
                }
            }
        }
        System.out.println("Result in seriesData(): " + resultMap);
        
        
        return resultMap;
    }
    
    
    private String setInventoryDetailsData(HashMap dataMap) throws Exception{
        InventoryDetailsTO objInventoryDetailsTO = new InventoryDetailsTO();
        String itemId = "";
        HashMap itemMap = new HashMap();
        itemMap.put("ITEM_SUB_TYPE", getItemSubType());
        itemMap.put("LEAVES", String.valueOf(getLeaves()));
        itemMap.put(CommonConstants.BRANCH_ID, getBranchCode());
        final List list = (List) sqlMap.executeQueryForList("getInventoryMasterItemID", itemMap);
        if(list.size() > 0){
            HashMap resultMap = (HashMap)list.get(0);
            itemId = CommonUtil.convertObjToStr(resultMap.get("ITEM_ID"));
        }
        
        objInventoryDetailsTO.setItemId(itemId);
        objInventoryDetailsTO.setTransType("TRANS_OUT");
        objInventoryDetailsTO.setTransDt(ServerUtil.getCurrentDate(getBranchCode()));
        objInventoryDetailsTO.setBookQuantity(CommonUtil.convertObjToDouble(String.valueOf(getBooks())));
        objInventoryDetailsTO.setBookSlnoFrom(CommonUtil.convertObjToDouble(dataMap.get("BK_SERIES_FROM")));
        objInventoryDetailsTO.setBookSlnoTo(CommonUtil.convertObjToDouble(dataMap.get("BK_SERIES_TO")));
        objInventoryDetailsTO.setLeavesSlnoFrom(CommonUtil.convertObjToDouble(dataMap.get("START_LEAF_NO2")));
        objInventoryDetailsTO.setLeavesSlnoTo(CommonUtil.convertObjToDouble(dataMap.get("END_LEAF_NO2")));
        
        objInventoryDetailsTO.setProdType(getProdType());
        objInventoryDetailsTO.setActNum(getActNum());
        //        objInventoryDetailsTO.setTransInId(TRAN_ID);
        objInventoryDetailsTO.setTransInId(CommonUtil.convertObjToStr(dataMap.get("TRANS_ID")));
        
        
        objInventoryDetailsTO.setCreatedBy(getUserID());
        objInventoryDetailsTO.setCreatedDt(ServerUtil.getCurrentDate(getBranchCode()));
        
        objInventoryDetailsTO.setStatusBy(getUserID());
        objInventoryDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        objInventoryDetailsTO.setStatusDt(ServerUtil.getCurrentDate(getBranchCode()));
        
        TOHeader toHeader = new TOHeader();
        toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
        objInventoryDetailsTO.setTOHeader(toHeader);
        
        HashMap hash = new HashMap();
        hash.put(CommonConstants.BRANCH_ID, getBranchCode());
        hash.put(CommonConstants.USER_ID, getUserID());
        hash.put("InventoryDetailsTO",objInventoryDetailsTO);
        InventoryDetailsDAO dao = new InventoryDetailsDAO();
        itemMap = dao.execute(hash, false);
        
        System.out.println("itemMap: " +itemMap);
        
        final String TRANSACTIONID = CommonUtil.convertObjToStr(itemMap.get(TRANSID));
        
        hash = null;
        itemMap = null;
        
        return TRANSACTIONID;
    }
    
    
    
    public String getProdType() {
        return prodType;
    } 
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }    
    
   
    public String getActNum() {
        return actNum;
    } 
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }    
    
    public String getUserID() {
        return userID;
    } 
    public void setUserID(String userID) {
        this.userID = userID;
    }    
    
    
    public String getBranchCode() {
        return branchCode;
    }
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
    
    
    public String getTransID() {
        return transID;
    }
    public void setTransID(String transID) {
        this.transID = transID;
    }
    
    
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    
    public String getItemSubType() {
        return itemSubType;
    }
    public void setItemSubType(String itemSubType) {
        this.itemSubType = itemSubType;
    }
    
    
    
    public long getLeaves() {
        return leaves;
    }
    public void setLeaves(long leaves) {
        this.leaves = leaves;
    }
    
    
    public long getBooks() {
        return books;
    }
    public void setBooks(long books) {
        this.books = books;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            HashMap map = new HashMap();
            //            map.put("ITEM_TYPE", "CHEQUES");
            //            map.put("ITEM_SUB_TYPE", "SB_CHEQUES");
            
            map.put("ITEM_TYPE", "DEMAND_DRAFTS");
            map.put("ITEM_SUB_TYPE", "TT_SERIES");
            
            map.put("LEAVES", "1");
            map.put(CommonConstants.BRANCH_ID, "ABC50001");
            
            GenerateSeries gs = new GenerateSeries("", "ABC50001", "Pempo");
            
            HashMap outPutMap = gs.seriesData(map);
            
            System.out.println("outPutMap: " + outPutMap);
            
        }catch(Exception e){
            System.out.println("Error in main()...");
            e.printStackTrace();
        }
        
        
    }
    
    
    
    
}
