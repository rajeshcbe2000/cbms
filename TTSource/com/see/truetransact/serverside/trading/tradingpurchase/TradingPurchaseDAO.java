/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingPurchaseDAO.java
 */

package com.see.truetransact.serverside.trading.tradingpurchase;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.trading.tradingpurchase.TradingPurchaseDetailsTO;
import com.see.truetransact.transferobject.trading.tradingpurchase.TradingPurchaseTO;
import com.see.truetransact.transferobject.trading.tradingpurchase.TradingPurchaseReturnTO;
import com.see.truetransact.transferobject.trading.tradingpurchase.TradingPurchaseReturnDetailsTO;
import com.see.truetransact.transferobject.trading.tradingstock.TradingStockTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import com.see.truetransact.serverexception.TransRollbackException;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author Revathi L
 */
//import com.transversalnet.db.DatabaseConnection;
//import com.transversalnet.error.TENException;
//import com.transversalnet.services.Group;
//import com.transversalnet.services.ShopMaster;
//import com.transversalnet.services.Supplier;
public class TradingPurchaseDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TradingPurchaseTO objTradingPurchaseTO;
    private TradingPurchaseDetailsTO objTradingPurchaseDetailsTO;
    private TradingStockTO objTradingStockTO;
    private TradingPurchaseReturnTO objTradingPurchaseReturnTO;
    private TradingPurchaseReturnDetailsTO objTradingPurchaseReturnDetailsTO;
    private LinkedHashMap purchaseTblDetails = null;
    private LinkedHashMap returnTblDetails = null;
    private final static Logger log = Logger.getLogger(TradingPurchaseDAO.class);
    private LogTO logTO;
    private LogDAO logDAO;
    private LinkedHashMap deletedPurchaseMap = null;
    private LinkedHashMap totalPurchaseMap = null;
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";
    private Date curr_dt = null;

    public TradingPurchaseDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("################# TradingPurchase DAO : " + map);
        HashMap returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (map.containsKey("objTradingPurchaseTO")) {      // for Purchase Screen
            if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                objTradingPurchaseTO = (TradingPurchaseTO) map.get("objTradingPurchaseTO");
            }
            if (map.containsKey("PURCHASE_DATA")) {
                totalPurchaseMap = (LinkedHashMap) map.get("PURCHASE_DATA");
            }
            if (map.containsKey("DELETE_DATA")) {
                deletedPurchaseMap = (LinkedHashMap) map.get("DELETE_DATA");
            }
            if (totalPurchaseMap.containsKey("TO_NOT_DELETED_AT_UPDATE_MODE")) {
                purchaseTblDetails = (LinkedHashMap) totalPurchaseMap.get("TO_NOT_DELETED_AT_UPDATE_MODE");
            }
        } else if (map.containsKey("objTradingPurchaseReturnTO")) { //for Purchase Return
            objTradingPurchaseReturnTO = (TradingPurchaseReturnTO) map.get("objTradingPurchaseReturnTO");
        }

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(logDAO, logTO, command, map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, map);
            }
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            if (objTradingPurchaseTO != null) {
                returnMap.put("PURCHASE_NO", objTradingPurchaseTO.getPurchaseNo());
            } else if (objTradingPurchaseReturnTO != null) {
                returnMap.put("PURCHASE_Return_NO", objTradingPurchaseReturnTO.getPurchaseRetNo());
            }
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    public void insertData(HashMap map) throws Exception {
        try {
            if (objTradingPurchaseTO != null) {
                String purchaseNo = "";
                purchaseNo = getPurchaseNo();
                insertPurchaseDetails(purchaseNo);
                objTradingPurchaseTO.setPurchaseNo(purchaseNo);
                sqlMap.executeUpdate("insertTradingPurchaseTO", objTradingPurchaseTO);
                logTO.setData(objTradingPurchaseTO.toString());
                logTO.setPrimaryKey(objTradingPurchaseTO.getKeyData());
                logTO.setStatus(objTradingPurchaseTO.getStatus());
                logDAO.addToLog(logTO);
            } else if (objTradingPurchaseReturnTO != null) {
                if (map.containsKey("RETURN_DATA")) {
                    List purchRetList = (List) map.get("RETURN_DATA");
                    String purchaseRetNo = "";
                    purchaseRetNo = getPurchaseReturnNo();
                    insertPurchaseReturnDetails(purchaseRetNo, purchRetList);
                    objTradingPurchaseReturnTO.setPurchaseRetNo(purchaseRetNo);
                    sqlMap.executeUpdate("insertTradingPurchaseReturnTO", objTradingPurchaseReturnTO);
                    logTO.setData(objTradingPurchaseReturnTO.toString());
                    logTO.setPrimaryKey(objTradingPurchaseReturnTO.getKeyData());
                    logTO.setStatus(objTradingPurchaseReturnTO.getStatus());
                    logDAO.addToLog(logTO);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void insertPurchaseDetails(String purchaseNo) throws Exception {
        try {
            if (purchaseTblDetails != null) {
                ArrayList addList = new ArrayList(purchaseTblDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = (TradingPurchaseDetailsTO) purchaseTblDetails.get(addList.get(i));
                    objTradingPurchaseDetailsTO.setPurchaseNo(purchaseNo);
                    sqlMap.executeUpdate("insertTradingPurchaseDetailsTO", objTradingPurchaseDetailsTO);
                    logTO.setData(objTradingPurchaseDetailsTO.toString());
                    logTO.setPrimaryKey(objTradingPurchaseDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objTradingPurchaseDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    private void insertPurchaseReturnDetails(String purchaseRetNo, List purchRetList) throws Exception {
        try {
            if (purchRetList != null && purchRetList.size() > 0) {
                for (int i = 0; i < purchRetList.size(); i++) {
                    HashMap purchRetMap = new HashMap();
                    purchRetMap = (HashMap) purchRetList.get(i);
                    if (!CommonUtil.convertObjToStr(purchRetMap.get("RETURN_QTY")).equals("0")) {
                        objTradingPurchaseReturnDetailsTO = new TradingPurchaseReturnDetailsTO();
                        objTradingPurchaseReturnDetailsTO.setPurchaseRetNo(purchaseRetNo);
                        objTradingPurchaseReturnDetailsTO.setSlNo(CommonUtil.convertObjToStr(purchRetMap.get("SL_NO")));
                        objTradingPurchaseReturnDetailsTO.setPurchaseNo(CommonUtil.convertObjToStr(purchRetMap.get("PURCHASE_NO")));
                        objTradingPurchaseReturnDetailsTO.setProdName(CommonUtil.convertObjToStr(purchRetMap.get("PRODUCT_NAME")));
                        objTradingPurchaseReturnDetailsTO.setUnitType(CommonUtil.convertObjToStr(purchRetMap.get("UNIT_TYPE")));
                        objTradingPurchaseReturnDetailsTO.setPurchasePrice(CommonUtil.convertObjToStr(purchRetMap.get("PURCHASE_PRICE")));
                        objTradingPurchaseReturnDetailsTO.setSalesPrice(CommonUtil.convertObjToStr(purchRetMap.get("SALES_PRICE")));
                        objTradingPurchaseReturnDetailsTO.setPurchQty(CommonUtil.convertObjToStr(purchRetMap.get("QTY")));
                        objTradingPurchaseReturnDetailsTO.setAvailQty(CommonUtil.convertObjToStr(purchRetMap.get("AVAIL_QTY")));
                        objTradingPurchaseReturnDetailsTO.setReturnQty(CommonUtil.convertObjToStr(purchRetMap.get("RETURN_QTY")));
                        objTradingPurchaseReturnDetailsTO.setPurchTotal(CommonUtil.convertObjToStr(purchRetMap.get("TOTAL")));
                        objTradingPurchaseReturnDetailsTO.setReturnTotal(CommonUtil.convertObjToStr(purchRetMap.get("RETURN_TOTAL")));
                        objTradingPurchaseReturnDetailsTO.setStatus("CREATED");
                        objTradingPurchaseReturnDetailsTO.setMrp(CommonUtil.convertObjToStr(purchRetMap.get("MRP")));
                        objTradingPurchaseReturnDetailsTO.setExpiry_Dt((Date) purchRetMap.get("EXPIRY_DT"));
                        sqlMap.executeUpdate("insertTradingPurchRetDetailsTO", objTradingPurchaseReturnDetailsTO);
                        logTO.setData(objTradingPurchaseReturnDetailsTO.toString());
                        logTO.setPrimaryKey(objTradingPurchaseReturnDetailsTO.getKeyData());
                        logDAO.addToLog(logTO);
                        objTradingPurchaseReturnDetailsTO = null;
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    private void updateData(LogDAO logDAO, LogTO logTO, String command, HashMap map) throws Exception {
        try {
            if (objTradingPurchaseTO != null) {
                objTradingPurchaseTO.setStatus(CommonConstants.STATUS_MODIFIED);
                sqlMap.executeUpdate("updateTradingPurchaseTO", objTradingPurchaseTO);
                logTO.setData(objTradingPurchaseTO.toString());
                logTO.setPrimaryKey(objTradingPurchaseTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                updateSubGroup(logDAO, logTO, command, objTradingPurchaseTO);
            } else if (objTradingPurchaseReturnTO != null
                    && CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getPurchaseRetNo()).length() > 0 && map.containsKey("RETURN_DATA")) {
                List purchRetList = (List) map.get("RETURN_DATA");
                String purchaseRetNo = "";
                purchaseRetNo = CommonUtil.convertObjToStr(objTradingPurchaseReturnTO.getPurchaseRetNo());
                sqlMap.executeUpdate("deleteTradingPurchRetDetailsTO", objTradingPurchaseReturnTO);
                insertPurchaseReturnDetails(purchaseRetNo, purchRetList);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateSubGroup(LogDAO logDAO, LogTO logTO, String command, TradingPurchaseTO objTradingPurchaseTO) throws Exception {
        try {
            String purchaseNo = CommonUtil.convertObjToStr(objTradingPurchaseTO.getPurchaseNo());
            sqlMap.startTransaction();
            if (deletedPurchaseMap != null && deletedPurchaseMap.size() > 0) {
                ArrayList addList = new ArrayList(deletedPurchaseMap.keySet());
                TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = null;
                for (int i = 0; i < deletedPurchaseMap.size(); i++) {
                    objTradingPurchaseDetailsTO = new TradingPurchaseDetailsTO();
                    objTradingPurchaseDetailsTO = (TradingPurchaseDetailsTO) deletedPurchaseMap.get(addList.get(i));
                    sqlMap.executeUpdate("deleteTradingPurchaseDetailsTO", objTradingPurchaseDetailsTO);
                }
            }
            if (purchaseTblDetails != null && purchaseTblDetails.size() > 0) {
                ArrayList addList = new ArrayList(purchaseTblDetails.keySet());
                TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = null;
                for (int i = 0; i < purchaseTblDetails.size(); i++) {
                    objTradingPurchaseDetailsTO = new TradingPurchaseDetailsTO();
                    objTradingPurchaseDetailsTO = (TradingPurchaseDetailsTO) purchaseTblDetails.get(addList.get(i));
//                    if (purchaseNo != null && purchaseNo.length() > 0) {
//                        objTradingPurchaseDetailsTO.setPurchaseNo(purchaseNo);
//                    }
                    if (objTradingPurchaseDetailsTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
//                        String subGroupID = "";
//                        subGroupID = getSubGroupID();
//                        objTradingPurchaseDetailsTO.setSubGroupID(subGroupID);
//                        objTradingPurchaseDetailsTO.setCr_Dt(CurrDt);
                        sqlMap.executeUpdate("insertTradingPurchaseDetailsTO", objTradingPurchaseDetailsTO);
                    } else {
                        sqlMap.executeUpdate("updateTradingPurchaseDetailsTO", objTradingPurchaseDetailsTO);
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }

        //map = null;
    }

    private void authorize(HashMap map, HashMap returnDetMap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        try {
            if (AuthMap.containsKey("PURCHASE")) {
                if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("AUTHORIZED") && purchaseTblDetails != null && purchaseTblDetails.size() > 0) {
                    updateTradingStock(map);
                }
                sqlMap.executeUpdate("authorizeTradingPurchase", AuthMap);
            } else if (AuthMap.containsKey("PURCHASE_RETURN") && returnDetMap.containsKey("RETURN_DATA")) {
                sqlMap.executeUpdate("authorizeTradingPurchaseReturn", AuthMap);
                sqlMap.executeUpdate("deleteUnWantedRecords", AuthMap);
                List purchRetList = (List) returnDetMap.get("RETURN_DATA");
                updatePurchRetStockQty(purchRetList);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateTradingStock(HashMap map) throws Exception {
        HashMap stockMap = new HashMap();
        ArrayList addList = new ArrayList(purchaseTblDetails.keySet());
        for (int i = 0; i < addList.size(); i++) {
            stockMap = new HashMap();
            TradingPurchaseDetailsTO objTradingPurchaseDetailsTO = (TradingPurchaseDetailsTO) purchaseTblDetails.get(addList.get(i));
            TradingStockTO objTradingStockTO = new TradingStockTO();
            objTradingStockTO.setStock_Type(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getUnitType()));
            objTradingStockTO.setStock_Sales_Price(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getSalesPrice()));
            objTradingStockTO.setStock_Purchase_Price(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getPurchasePrice()));
            objTradingStockTO.setProduct_ID(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getProdId()));
            objTradingStockTO.setStock_Quant(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getQty()));
            objTradingStockTO.setSales_Tax(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getTax()));
            objTradingStockTO.setProd_Name(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getProdName()));
            objTradingStockTO.setLoose_Qty(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getQtyUnit()));
            objTradingStockTO.setExpiry_Dt((DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getExpiry_Dt()))));
            objTradingStockTO.setStock_MRP(CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getMrp()));
            stockMap.put("PRODUCT_ID", CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getProdId()));
            stockMap.put("STOCK_TYPE", CommonUtil.convertObjToStr(objTradingPurchaseDetailsTO.getUnitType()));
            List stockList = (List) sqlMap.executeQueryForList("getTradingStockDetails", stockMap);
            if (stockList != null && stockList.size() > 0) {
                stockMap = (HashMap) stockList.get(0);
                Date purExpDt = (Date) objTradingPurchaseDetailsTO.getExpiry_Dt();
                Date stkExpDt = (Date) stockMap.get("EXPIRY_DT");
                if ((CommonUtil.convertObjToDouble(objTradingStockTO.getStock_Purchase_Price()).doubleValue() == CommonUtil.convertObjToDouble(stockMap.get("STOCK_PURCHASE_PRICE")).doubleValue())
                        && (CommonUtil.convertObjToDouble(objTradingStockTO.getStock_Sales_Price()).doubleValue() == CommonUtil.convertObjToDouble(stockMap.get("STOCK_SALES_PRICE")).doubleValue())
                        && (CommonUtil.convertObjToDouble(objTradingStockTO.getStock_MRP()).doubleValue() == CommonUtil.convertObjToDouble(stockMap.get("STOCK_MRP")).doubleValue())
                        && (DateUtil.dateDiff(purExpDt, stkExpDt) == 0) && CommonUtil.convertObjToStr(stockMap.get("STOCK_ID")).length() > 0) {
                    stockMap.put("STOCK_QUANT", CommonUtil.convertObjToStr(objTradingStockTO.getStock_Quant()));
                    stockMap.put("PRODUCT_ID", CommonUtil.convertObjToStr(objTradingStockTO.getProduct_ID()));
                    sqlMap.executeUpdate("updateTradingStockQuant", stockMap);
                } else {
                    String stockID = "";
                    stockID = getStockID();
                    objTradingStockTO.setStockID(stockID);
                    objTradingStockTO.setStatus("CREATED");
                    objTradingStockTO.setAuthorizeStatus("AUTHORIZED");
                    sqlMap.executeUpdate("insertTradingStockTO", objTradingStockTO);
                }
            } else {
                String stockID = "";
                stockID = getStockID();
                objTradingStockTO.setStockID(stockID);
                objTradingStockTO.setStatus("CREATED");
                objTradingStockTO.setAuthorizeStatus("AUTHORIZED");
                sqlMap.executeUpdate("insertTradingStockTO", objTradingStockTO);
            }
            objTradingPurchaseDetailsTO = null;
        }
    }

    private void updatePurchRetStockQty(List purchRetList) throws Exception {
        try {
            HashMap stockMap = new HashMap();
            if (purchRetList != null && purchRetList.size() > 0) {
                for (int i = 0; i < purchRetList.size(); i++) {
                    HashMap purchRetMap = new HashMap();
                    purchRetMap = (HashMap) purchRetList.get(i);
                    if (CommonUtil.convertObjToInt(purchRetMap.get("RETURN_QTY")) > 0) {
                        List prodList = (List) sqlMap.executeQueryForList("getProductIDForStock", purchRetMap);
                        if (prodList != null && prodList.size() > 0) {
                            stockMap = (HashMap) prodList.get(0);
                            stockMap.put("STOCK_TYPE", CommonUtil.convertObjToStr(purchRetMap.get("UNIT_TYPE")));
                            List stockList = (List) sqlMap.executeQueryForList("getTradingStockDetails", stockMap);
                            if (stockList != null && stockList.size() > 0) {
                                for (int j = 0; j < stockList.size(); j++) {
                                    stockMap = (HashMap) stockList.get(j);
                                    objTradingStockTO = new TradingStockTO();
                                    objTradingStockTO.setProduct_ID(CommonUtil.convertObjToStr(stockMap.get("PRODUCT_ID")));
                                    objTradingStockTO.setStock_Purchase_Price(CommonUtil.convertObjToStr(purchRetMap.get("PURCHASE_PRICE")));
                                    objTradingStockTO.setStock_Sales_Price(CommonUtil.convertObjToStr(purchRetMap.get("SALES_PRICE")));
                                    objTradingStockTO.setStock_MRP(CommonUtil.convertObjToStr(purchRetMap.get("MRP")));
                                    objTradingStockTO.setStock_Quant(CommonUtil.convertObjToStr(purchRetMap.get("RETURN_QTY")));
                                    Date purExpDt = (Date) purchRetMap.get("EXPIRY_DT");
                                    Date stkExpDt = (Date) stockMap.get("EXPIRY_DT");
                                    if ((CommonUtil.convertObjToDouble(objTradingStockTO.getStock_Purchase_Price()).doubleValue() == CommonUtil.convertObjToDouble(stockMap.get("STOCK_PURCHASE_PRICE")).doubleValue())
                                            && (CommonUtil.convertObjToDouble(objTradingStockTO.getStock_Sales_Price()).doubleValue() == CommonUtil.convertObjToDouble(stockMap.get("STOCK_SALES_PRICE")).doubleValue())
                                            && (CommonUtil.convertObjToDouble(objTradingStockTO.getStock_MRP()).doubleValue() == CommonUtil.convertObjToDouble(stockMap.get("STOCK_MRP")).doubleValue())
                                            && (DateUtil.dateDiff(purExpDt, stkExpDt) == 0) && CommonUtil.convertObjToStr(stockMap.get("STOCK_ID")).length() > 0) {
                                        stockMap.put("RETURN_QTY", CommonUtil.convertObjToStr(objTradingStockTO.getStock_Quant()));
                                        stockMap.put("PRODUCT_ID", CommonUtil.convertObjToStr(objTradingStockTO.getProduct_ID()));
                                        sqlMap.executeUpdate("updateReturnStockQuant", stockMap);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            if (objTradingPurchaseTO != null) {
                objTradingPurchaseTO.setStatus(CommonUtil.convertObjToStr("DELETED"));
                sqlMap.executeUpdate("deleteTradingPurchaseTO", objTradingPurchaseTO);
                sqlMap.executeUpdate("deleteAllTradingPurchaseDetailsTO", objTradingPurchaseTO);
            } else if (objTradingPurchaseReturnTO != null) {
                objTradingPurchaseReturnTO.setStatus(CommonUtil.convertObjToStr("DELETED"));
                sqlMap.executeUpdate("deleteTradingPurchaseReturnTO", objTradingPurchaseReturnTO);
                sqlMap.executeUpdate("deleteTradingPurchaseRetDetailsTO", objTradingPurchaseReturnTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private Date setproperDate(Date roiDt) {
        if (roiDt != null) {
            Date roiCurrDt = (Date) curr_dt.clone();
            roiCurrDt.setDate(roiDt.getDate());
            roiCurrDt.setMonth(roiDt.getMonth());
            roiCurrDt.setYear(roiDt.getYear());
            return (roiCurrDt);
        }
        return null;
    }

    private String getPurchaseNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PURCHASE_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    private String getPurchaseReturnNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PURCHASE_RET_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    private String getStockID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "STOCK_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        System.out.println("#### Trading Purchase Dao GetData map : " + map);
        returnMap = new HashMap();
        if (map.containsKey("PURCHASE")) {
            List list = (List) sqlMap.executeQueryForList("getSelectTradingPurchaseTO", map);
            if (list != null && list.size() > 0) {
                returnMap.put("objTradingPurchaseTO", list);
            }
            map.put("PURCHASE_NO", map.get("PURCHASE_NO"));
            List subGroupList = (List) sqlMap.executeQueryForList("getTradingPurchaseDetailsTO", map);
            if (subGroupList != null && subGroupList.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                for (int i = subGroupList.size(), j = 0; i > 0; i--, j++) {
                    String st = ((TradingPurchaseDetailsTO) subGroupList.get(j)).getPurchaseNo();
                    ParMap.put(j + 1, subGroupList.get(j));
                }
                returnMap.put("PURCHASE_DATA", ParMap);
            }
        } else if (map.containsKey("PURCHASE_RETURN")) {
            List returnLst = (List) sqlMap.executeQueryForList("getSelectTradingPurchaseReturnTO", map);
            if (returnLst != null && returnLst.size() > 0) {
                returnMap.put("objTradingPurchaseReturnTO", returnLst);
            }
            map.put("PURCHASE_RET_NO", map.get("PURCHASE_RET_NO"));
            List subRetList = (List) sqlMap.executeQueryForList("getTradingPurchaseReturnDetailsTO", map);
            if (subRetList != null && subRetList.size() > 0) {
                LinkedHashMap subMap = new LinkedHashMap();
                for (int i = subRetList.size(), j = 0; i > 0; i--, j++) {
                    //String st = ((TradingPurchaseReturnDetailsTO) subRetList.get(j)).getPurchaseRetNo();
                    //subMap.put(j + 1, subRetList.get(j));
                }
                returnMap.put("RETURN_DATA", subRetList);
                subRetList = null;
            }
        }
        return returnMap;
    }

    private void destroyObjects() {
        objTradingPurchaseDetailsTO = null;
        objTradingPurchaseTO = null;
    }
}
