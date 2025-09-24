/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 *
 * @author Revathi L
 */

package com.see.truetransact.serverside.trading.damage;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.see.truetransact.transferobject.trading.tradingstock.TradingStockTO;
import com.see.truetransact.transferobject.trading.damage.DamageTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.Date;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;

//import com.transversalnet.db.DatabaseConnection;
//import com.transversalnet.error.TENException;
//import com.transversalnet.services.Group;
//import com.transversalnet.services.ShopMaster;
//import com.transversalnet.services.Supplier;
public class DamageDAO extends TTDAO {
    private static SqlMap sqlMap = null;
    private DamageTO objDamageTO;
    private LogTO logTO;
    private LogDAO logDAO;
    HashMap returnMap = new HashMap();
    private LinkedHashMap damageTblDetails = null;
    private LinkedHashMap deletedDamageMap = null;
    private final static Logger log = Logger.getLogger(DamageDAO.class);
    private Date currDt = null;
    String damageID = "";
    private TransactionDAO transactionDAO = null;
    
    public DamageDAO() throws ServiceLocatorException{
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("Map in DAO: " + map);
         _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        if (map.containsKey("DAMAGE_DATA")) {
            damageTblDetails = (LinkedHashMap) map.get("DAMAGE_DATA");
        }
        
        if (map.containsKey("DELETE_DATA")) {
            deletedDamageMap = (LinkedHashMap) map.get("DELETE_DATA");
        }

        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
//            deleteData();
        }else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap,map);
            }
        }else {
            throw new NoCommandException();
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT) && damageID.length()>0) {
                returnMap.put("DAMAGE_ID", damageID);
        }
        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }
    
    private HashMap getData(HashMap map) throws Exception {
       HashMap returnMap = new HashMap();
        System.out.println("#### GetData map : " + map);
        returnMap = new HashMap();
            map.put("DAMAGE_ID", map.get("DAMAGE_ID"));
            List damageLst = (List) sqlMap.executeQueryForList("getTradingDamageDetailsTO", map);
            if (damageLst != null && damageLst.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                for (int i = damageLst.size(), j = 0; i > 0; i--, j++) {
                    String st = ((DamageTO) damageLst.get(j)).getDamageID();
                    ParMap.put(j + 1, damageLst.get(j));
                }
                returnMap.put("DAMAGE_DATA", ParMap);
            }
        
         return returnMap;
    }
    
    public void insertData(HashMap map) throws Exception {
        try {
            damageID = "";
                damageID = getDamageID();
                if (damageTblDetails != null) {
                ArrayList addList = new ArrayList(damageTblDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    DamageTO objDamageTO = (DamageTO) damageTblDetails.get(addList.get(i));
                    objDamageTO.setDamageID(damageID);
                    sqlMap.executeUpdate("insertTradingDamageTO", objDamageTO);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }
    
    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (deletedDamageMap != null && deletedDamageMap.size()>0) {
                ArrayList addList = new ArrayList(deletedDamageMap.keySet());
                DamageTO objDamageTO = null;
                for (int i = 0; i < deletedDamageMap.size(); i++) {
                    objDamageTO = new DamageTO();
                    objDamageTO = (DamageTO) deletedDamageMap.get(addList.get(i));
                    sqlMap.executeUpdate("deleteTradingDamageDetailsTO", objDamageTO);
                }
            }
            if (damageTblDetails != null && damageTblDetails.size()>0) {
                ArrayList addList = new ArrayList(damageTblDetails.keySet());
                DamageTO objDamageTO = null;
                for (int i = 0; i < damageTblDetails.size(); i++) {
                    objDamageTO = new DamageTO();
                    objDamageTO = (DamageTO) damageTblDetails.get(addList.get(i));
                    if (objDamageTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        sqlMap.executeUpdate("insertTradingDamageTO", objDamageTO);
                    } else {
                        sqlMap.executeUpdate("updateTradingDamageTO", objDamageTO);
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }
    
    private void authorize(HashMap map,HashMap returnDetMap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        try {
            if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("AUTHORIZED") && damageTblDetails!=null && damageTblDetails.size()>0) {
                insertTransactionData(AuthMap);
                updateTradingStock(map);
            }
            sqlMap.executeUpdate("authorizeTradingDamage", AuthMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    private void updateTradingStock(HashMap map) throws Exception {
        HashMap stockMap = new HashMap();
        ArrayList addList = new ArrayList(damageTblDetails.keySet());
        for (int i = 0; i < addList.size(); i++) {
            stockMap = new HashMap();
            DamageTO objDamageTO = (DamageTO) damageTblDetails.get(addList.get(i));
            stockMap.put("RETURN_QTY", CommonUtil.convertObjToStr(objDamageTO.getDamageQty()));
            stockMap.put("STOCK_ID", CommonUtil.convertObjToStr(objDamageTO.getStockID()));
            stockMap.put("PRODUCT_ID", CommonUtil.convertObjToStr(objDamageTO.getProdID()));
            sqlMap.executeUpdate("updateReturnStockQuant", stockMap);
        } 
    }
    
    private void insertTransactionData(HashMap map) throws Exception {
        try {
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            HashMap txMap = new HashMap();
            HashMap transMap = new HashMap();
            String damageID = "";
            damageID = CommonUtil.convertObjToStr(map.get("DAMAGE_ID"));
            HashMap acHeadMap = new HashMap();
            HashMap damageMap = new HashMap();
            double totAmount = 0.0;
            double transAmount = 0.0;
            if (damageID.length() > 0) {
                List headList = (List) sqlMap.executeQueryForList("getSelectTradingAcHead", acHeadMap);
                if (headList != null && headList.size() > 0) {
                    acHeadMap = (HashMap) headList.get(0);
                    damageMap.put("DAMAGE_ID", damageID);
                    List damageLst = (List) sqlMap.executeQueryForList("getTradingDamageGrandTotal", damageMap);
                    if (damageLst != null && damageLst.size() > 0) {
                        damageMap = (HashMap) damageLst.get(0);
                        totAmount = CommonUtil.convertObjToDouble(damageMap.get("GRAND_TOTAL"));
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        TransferTrans objTransferTrans = new TransferTrans();
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.setLinkBatchID(damageID);
                        HashMap tansactionMap = new HashMap();
                        TxTransferTO transferTo = new TxTransferTO();
                        ArrayList TxTransferTO = new ArrayList();
                            if (totAmount > 0) {
                                transAmount = totAmount;
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("STOCK"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading Damage -" + damageID);
                                txMap.put("AUTHORIZEREMARKS", "Debit");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                                txMap = new HashMap();
                                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("DAMAGES"));
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading Damage -" + damageID);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                                if (transferList != null && transferList.size() > 0) {
                                    doDebitCredit(transferList, _branchCode, map, damageID);
                                }
                            }
                    }
                    getTransDetails(damageID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    private void doDebitCredit(ArrayList batchList, String branchCode, HashMap map, String salesNo) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", map.get("COMMAND"));
        data.put("INITIATED_BRANCH", _branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, map.get("USER_ID"));
        data.put(CommonConstants.MODULE, "Trading");
        data.put(CommonConstants.SCREEN, "Trading Sales");
        data.put("MODE", "MODE");
        data.put("LINK_BATCH_ID", salesNo);
        HashMap transMap = new HashMap();
        transMap = transferDAO.execute(data, false);
        String authorizeStatus = CommonUtil.convertObjToStr(map.get("STATUS"));
        HashMap transAuthMap = new HashMap();
        transAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
        transAuthMap.put("DAILY", "DAILY");
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        TransactionDAO.authorizeCashAndTransfer(salesNo, authorizeStatus, transAuthMap);
    }
    
    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsWithAccHeadDesc", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
    }

    private String getDamageID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "DAMAGE_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }
    
    private void destroyObjects() {
        objDamageTO = null;
    }
	
}
