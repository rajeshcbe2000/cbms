/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PassbookDataEntryDAO.java
 *
 * 
 */
package com.see.truetransact.serverside.passbookDataEntry;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.passbookDataEntry.PassbookDataEntryTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * PassbookDataEntryDAO
 *
 * @author anjuanand
 *
 */
public class PassbookDataEntryDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    HashMap resultMap = new HashMap();
    private Date curr_dt = null;
    private ArrayList passBookList;
    private PassbookDataEntryTO objOtherBankDataTO;
    private PassbookDataEntryTO objPassBookDataTO;

    /**
     * Creates a new instance of PassbookDataEntryDAO
     */
    public PassbookDataEntryDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        return returnMap;
    }

    public HashMap execute(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
            curr_dt = ServerUtil.getCurrentDate(_branchCode);
            objOtherBankDataTO = (PassbookDataEntryTO) map.get("OtherBankData");
            objPassBookDataTO = (PassbookDataEntryTO) map.get("PassBookTransactionData");
            final String command = CommonUtil.convertObjToStr(objPassBookDataTO.getCommand());
            if (objOtherBankDataTO != null) {
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    if (objPassBookDataTO != null) {
                        passBookList = (ArrayList) map.get("TRANS_DATA");
                        insertPassBookEntry(objOtherBankDataTO, objPassBookDataTO, passBookList);
                    }
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updatePassBookEntry(objOtherBankDataTO, objPassBookDataTO, passBookList);
                } else {
                    throw new NoCommandException();
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void insertPassBookEntry(PassbookDataEntryTO objOtherBankDataTO, PassbookDataEntryTO objPassBookDataTO, ArrayList passBookList) throws Exception {
        try {
            if (passBookList.size() > 0) {
                Date transDate = null;
                transDate = objPassBookDataTO.getTransDate();
                HashMap dataMap = new HashMap();
                dataMap.put("BANK_CODE", objOtherBankDataTO.getBankCode());
                dataMap.put("BRANCH_CODE", objOtherBankDataTO.getBranchCode());
                dataMap.put("TRANSDT", transDate);
                List lst = null;
                lst = (List) sqlMap.executeQueryForList("getPassBookTransDate", dataMap);
                if (lst != null && lst.size() > 0) {
                    HashMap resultMap = new HashMap();
                    resultMap = (HashMap) lst.get(0);
                    Date newTransDt = null;
                    newTransDt = (Date) resultMap.get("TRANS_DATE");
                    HashMap transDtMap = new HashMap();
                    transDtMap.put("TRANS_DATE", newTransDt);
                    List dtList = null;
                    dtList = (List) sqlMap.executeQueryForList("getPassMinMaxSrlNo", transDtMap);
                    if (dtList != null && dtList.size() > 0) {
                        HashMap result = new HashMap();
                        result = (HashMap) dtList.get(0);
                        int minSrl = 0;
                        minSrl = CommonUtil.convertObjToInt(result.get("MIN_SRL"));
                        for (int i = 0; i < passBookList.size(); i++) {
                            objPassBookDataTO = (PassbookDataEntryTO) passBookList.get(i);
                            objPassBookDataTO.setBankCode(objOtherBankDataTO.getBankCode());
                            objPassBookDataTO.setBranchCode(objOtherBankDataTO.getBranchCode());
                            objPassBookDataTO.setSrlNo(minSrl);
                            sqlMap.executeUpdate("insertPassBookDataTO", objPassBookDataTO);
                            minSrl++;
                        }
                        if (transDate.before(newTransDt)) {
                            for (int k = 0; k < lst.size(); k++) {
                                HashMap transDetailsMap = new HashMap();
                                transDetailsMap = (HashMap) lst.get(k);
                                HashMap transDataMap = new HashMap();
                                transDataMap.put("BANK_CODE", transDetailsMap.get("BANK_CODE"));
                                transDataMap.put("BRANCH_CODE", transDetailsMap.get("BRANCH_CODE"));
                                transDataMap.put("TRANS_TYPE", transDetailsMap.get("TRANS_TYPE"));
                                transDataMap.put("TRANS_ID", transDetailsMap.get("TRANS_ID"));
                                transDataMap.put("TRANS_DATE", transDetailsMap.get("TRANS_DATE"));
                                transDataMap.put("SRL_NO", minSrl);
                                sqlMap.executeUpdate("updatePassBookSrl", transDataMap);
                                minSrl++;
                            }
                        }
                    }
                } else {
                    int srl_No = 0;
                    srl_No = objOtherBankDataTO.getSrlNo();
                    if (srl_No == 0) {
                        srl_No++;
                        objPassBookDataTO.setSrlNo(srl_No);
                        objPassBookDataTO.setBankCode(objOtherBankDataTO.getBankCode());
                        objPassBookDataTO.setBranchCode(objOtherBankDataTO.getBranchCode());
                        objPassBookDataTO.setAmount(0.0);
                        objPassBookDataTO.setInstDate(null);
                        objPassBookDataTO.setInstNo1("");
                        objPassBookDataTO.setInstNo2("");
                        objPassBookDataTO.setInstType("");
                        objPassBookDataTO.setTransDate(null);
                        objPassBookDataTO.setTransId("");
                        objPassBookDataTO.setTransType("");
                        objPassBookDataTO.setParticulars("");
                        sqlMap.executeUpdate("insertPassBookDataTO", objPassBookDataTO);
                    }
                    for (int i = 0; i < passBookList.size(); i++) {
                        objPassBookDataTO = (PassbookDataEntryTO) passBookList.get(i);
                        srl_No = srl_No + 1;
                        objPassBookDataTO.setSrlNo(srl_No);
                        objPassBookDataTO.setBankCode(objOtherBankDataTO.getBankCode());
                        objPassBookDataTO.setBranchCode(objOtherBankDataTO.getBranchCode());
                        sqlMap.executeUpdate("insertPassBookDataTO", objPassBookDataTO);
                    }
                }
            }
            HashMap dataMap = new HashMap();
            dataMap.put("BANKCODE", objOtherBankDataTO.getBankCode());
            dataMap.put("BRANCHCODE", objOtherBankDataTO.getBranchCode());
            dataMap.put("ACCTYPE", objOtherBankDataTO.getAccType());
            sqlMap.executeUpdate("updateOtherBankFinalBalance", dataMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updatePassBookEntry(PassbookDataEntryTO objOtherBankDataTO, PassbookDataEntryTO objPassBookDataTO, ArrayList passBookList) throws Exception {
        try {
            objPassBookDataTO.setBankCode(objOtherBankDataTO.getBankCode());
            objPassBookDataTO.setBranchCode(objOtherBankDataTO.getBranchCode());
            sqlMap.executeUpdate("updatePassBookDataTO", objPassBookDataTO);
            HashMap dataMap = new HashMap();
            dataMap.put("BANKCODE", objOtherBankDataTO.getBankCode());
            dataMap.put("BRANCHCODE", objOtherBankDataTO.getBranchCode());
            dataMap.put("ACCTYPE", objOtherBankDataTO.getAccType());
            sqlMap.executeUpdate("updateOtherBankFinalBalance", dataMap);
            sqlMap.commitTransaction();
        } catch (SQLException ex) {
            sqlMap.rollbackTransaction();
            ex.printStackTrace();
            throw new TransRollbackException(ex);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
}