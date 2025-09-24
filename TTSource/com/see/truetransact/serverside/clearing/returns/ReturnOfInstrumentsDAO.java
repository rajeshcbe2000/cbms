/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReturnOfInstrumentsDAO.java
 *
 * Created on Tue Apr 06 17:27:23 GMT+05:30 2004
 */
package com.see.truetransact.serverside.clearing.returns;

/**
 *
 * @author Ashok
 */
import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Date;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.clearing.returns.ReturnOfInstrumentsTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import java.util.Date;
/**
 * ReturnOfInstruments DAO.
 *
 */
public class ReturnOfInstrumentsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ReturnOfInstrumentsTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger _log = Logger.getLogger(ReturnOfInstrumentsDAO.class);
    private String returnId = "";
    private String userId = "";
    private HashMap returnRemarksMap = new HashMap();
    private Date currDt = null;
    /**
     * Creates a new instance of ReturnOfInstrumentsDAO
     */
    public ReturnOfInstrumentsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * Gets the Data by executing the Query based upon the MappedStatement Name
     */
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        ArrayList head = null;
        ArrayList data = null;
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectReturnOfInstrumentsTO", where);
        returnMap.put("ReturnOfInstrumentsTO", list);
        list = null;
        where = null;
        return returnMap;
    }

    /**
     * Gets the ReturnId
     */
    private String getReturnId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "OUTWARD.RETURN_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * Inserts data in to OUTWARD_RETURN table
     */
    private void insertData() throws Exception {
        returnId = getReturnId();
        objTO.setReturnId(returnId);
        objTO.setStatus(CommonConstants.STATUS_CREATED);
        objTO.setStatusBy(userId);
        objTO.setStatusDt(currDt);

        sqlMap.executeUpdate("insertReturnOfInstrumentsTO", objTO);

        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());

        logDAO.addToLog(logTO);
    }

    /**
     * Updataes the Data in OUTWARD_RETURN TABLE
     */
    private void updateData() throws Exception {
        objTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objTO.setStatusBy(userId);
        objTO.setStatusDt(currDt);

        sqlMap.executeUpdate("updateReturnOfInstrumentsTO", objTO);

        logTO.setStatus(objTO.getCommand());
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logDAO.addToLog(logTO);
    }

    /**
     * Sets the status field of OUTWARD_RETURN table as DELETED
     */
    private void deleteData() throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());

        objTO.setStatus(CommonConstants.STATUS_DELETED);
        objTO.setStatusBy(userId);
        objTO.setStatusDt(currDt);
        sqlMap.executeUpdate("deleteReturnOfInstrumentsTO", objTO);
        logDAO.addToLog(logTO);
    }

    public static void main(String str[]) {
    }

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    /* Executes the Insert,Update,Delete Query based on the Command */
    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("map : " + map);

        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
        logTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        userId = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));

        try {
            if (isTransaction) {
                sqlMap.startTransaction();
            }

            if (map.containsKey("ReturnOfInstrumentsTO")) {
                objTO = (ReturnOfInstrumentsTO) map.get("ReturnOfInstrumentsTO");
                final String command = objTO.getCommand();
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData();
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData();
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                } else {
                    throw new NoCommandException();
                }
            }

            //__ if the Map Contains the Data for the Authorization...
            if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                //__ It the AuthMap is not Null...

                if (!authMap.containsKey(CommonConstants.AUTHORIZEDATA)) {
                    ArrayList lstReturn = new ArrayList();
                    HashMap lstMap = new HashMap();
                    lstMap.put("RET_ID", returnId);
                    lstMap.put("ACCT_NO", objTO.getAcctNo());
                    lstMap.put("BRANCH_CODE", _branchCode);
                    lstMap.put("USER_ID", logTO.getUserId());
                    lstMap.put("AUTHORIZEDT", logTO.getApplDt());
                    lstMap.put("TRANS_ID", map.get("TRANS_ID"));
                    lstReturn.add(lstMap);
                    authMap.put(CommonConstants.AUTHORIZEDATA, lstReturn);
                }

                if (authMap != null) {
                    authorize(authMap, logDAO, logTO);
                }
            }

            if (isTransaction) {
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            if (isTransaction) {
                sqlMap.rollbackTransaction();
            }
            _log.error(e);
            throw e;
        }

        logDAO = null;
        logTO = null;
        map = null;
        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;

        for (int i = 0, j = selectedList.size(); i < j; i++) {
            dataMap = (HashMap) selectedList.get(i);
            dataMap.put(CommonConstants.STATUS, status);

            System.out.println("Data Map in Clearing DAO: " + dataMap);
            sqlMap.executeUpdate("authorizeReturnInstruments", dataMap);

            if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                dataMap.put("INITIATED_BRANCH", _branchCode);
                dataMap.put("TRANS_DT", currDt.clone());
                List transList = sqlMap.executeQueryForList("Return.getTransactionData", dataMap);
                System.out.println("transList: " + transList);
                if (transList.size() > 0) {
                    HashMap resultMap = (HashMap) transList.get(0);
                    resultMap.put("TRANS_ID", dataMap.get("TRANS_ID"));
                    System.out.println("resultMap: " + resultMap);
                    updateClearingAdjustment(resultMap);
                    updateTranCharges(resultMap, dataMap);
                }
            }


            objLogTO.setData(dataMap.toString());
            objLogTO.setPrimaryKey(CommonUtil.convertObjToStr(dataMap.get("RET_ID")));
            objLogTO.setStatus(status);

            objLogDAO.addToLog(objLogTO);
        }
    }

    private void updateClearingAdjustment(HashMap resultMap) throws Exception {
        System.out.println(" ReturnOfInstrumentsDAO : updateClearingAdjustment : resultMap : " + resultMap);
        HashMap map = new HashMap();
        HashMap loanAdvMap = new HashMap();
        map.put(TransferTrans.DR_INSTRUMENT_2, objTO.getInstrumentNo2());//chandra
        map.put(TransferTrans.PARTICULARS, "Oclg Return");//chandra
        map.put(TransferTrans.DR_INST_TYPE, "CLEARING");
//        if(CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals(TransactionFactory.ADVANCES) ||CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals(TransactionFactory.LOANS)){
//            List lst=null;
//            resultMap.put("ACCOUNTNO",resultMap.get("ACCT_NO"));
//            if(CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals(TransactionFactory.ADVANCES))
//                lst=sqlMap.executeQueryForList("getAdvPaidDetails",resultMap);
//            
//            else
//                lst=sqlMap.executeQueryForList("getTermLoanPaidDetails",resultMap);
//            if(lst !=null && lst.size()>0)
//            loanAdvMap=(HashMap)lst.get(0);
//            double totamount=CommonUtil.convertObjToDouble(loanAdvMap.get("PRINCIPLE")).doubleValue()+CommonUtil.convertObjToDouble(loanAdvMap.get("INTEREST")).doubleValue();
//            loanAdvMap.put("PRINCIPLE",new Double(totamount));
//            //FOR ACHD
//            List lstachd=sqlMap.executeQueryForList("getLoanAccountClosingHeads",resultMap.get("ACCT_NO"));
//            HashMap achd=(HashMap)lstachd.get(0);
//            if(CommonUtil.convertObjToInt(loanAdvMap.get("PRINCIPLE"))>0) {
//                map.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
//                map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(resultMap.get("PROD_ID")) ); // Inward clearing a/c productid
////                map.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(resultMap.get("ACCT_NO"))) ; // ic a/c no
//                map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(achd.get("ACCT_HEAD"))) ; // prod a/c head
//                map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))) ; // ic a/c no branch
//                //        map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                //        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("CLEARING_SUSPENSE_HD"))) ; // prod a/c head
//                //        map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"))) ; // ic a/c no branch
//                map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))) ; // local logged branch
//                map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
//                map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("CLEARING_HD")) ); // charge a/c get it from clearing_bank_param
//                map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // gl
//                
//                TransferTrans trans = new TransferTrans();
//                trans.setTransMode(CommonConstants.TX_CLEARING);
//                
//                ArrayList batchList = new ArrayList();
//                double amt = CommonUtil.convertObjToDouble(loanAdvMap.get("PRINCIPLE")).doubleValue();
//                batchList.add(trans.getDebitTransferTO(map, amt)) ;
//                batchList.add(trans.getCreditTransferTO(map, amt)) ;
//                
//                trans.doDebitCredit(batchList, _branchCode);
//            } if(CommonUtil.convertObjToInt(loanAdvMap.get("INTEREST"))>0){
//                map.put(TransferTrans.DR_PROD_TYPE,  TransactionFactory.GL); //CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
////                map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(resultMap.get("PROD_ID")) ); // Inward clearing a/c productid
////                map.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(resultMap.get("ACCT_NO"))) ; // ic a/c no
//                map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(achd.get("AC_DEBIT_INT"))) ; // prod a/c head
//                map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))) ; // ic a/c no branch
//                //        map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                //        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("CLEARING_SUSPENSE_HD"))) ; // prod a/c head
//                //        map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"))) ; // ic a/c no branch
//                map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))) ; // local logged branch
//                map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
//                map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(achd.get("INT_PAYABLE_ACHD")) ); // charge a/c get it from clearing_bank_param
//                map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // gl
//                
//                TransferTrans trans = new TransferTrans();
//                trans.setTransMode(CommonConstants.TX_CLEARING);
//                
//                ArrayList batchList = new ArrayList();
//                double amt = CommonUtil.convertObjToDouble(loanAdvMap.get("INTEREST")).doubleValue();
//                batchList.add(trans.getDebitTransferTO(map, amt)) ;
//                batchList.add(trans.getCreditTransferTO(map, amt)) ;
//                System.out.println("Inside updateClearingAdjustment"+map);
//                
//                trans.doDebitCredit(batchList, _branchCode);
//            }
//            if(CommonUtil.convertObjToInt(loanAdvMap.get("PENAL"))>0){
//                map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL); //CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
////                map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(resultMap.get("PROD_ID")) ); // Inward clearing a/c productid
////                map.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(resultMap.get("ACCT_NO"))) ; // ic a/c no
//                map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(achd.get("AC_DEBIT_INT"))) ; // prod a/c head
//                map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))) ; // ic a/c no branch
//                //        map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
//                //        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("CLEARING_SUSPENSE_HD"))) ; // prod a/c head
//                //        map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"))) ; // ic a/c no branch
//                map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))) ; // local logged branch
//                map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
//                map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(achd.get("INT_PAYABLE_ACHD")) ); // charge a/c get it from clearing_bank_param
//                map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // gl
////                map.put(TransferTrans.DR_INSTRUMENT_2,objTO.getInstrumentNo2());//chandra
////                map.put(TransferTrans.PARTICULARS,"Oclg Return");//chandra
//                TransferTrans trans = new TransferTrans();
//                trans.setTransMode(CommonConstants.TX_CLEARING);
//                
//                ArrayList batchList = new ArrayList();
//                double amt = CommonUtil.convertObjToDouble(loanAdvMap.get("PENAL")).doubleValue();
//                batchList.add(trans.getDebitTransferTO(map, amt)) ;
//                batchList.add(trans.getCreditTransferTO(map, amt)) ;
//                
//                trans.doDebitCredit(batchList, _branchCode);
//            }}else{
        map.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
        map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(resultMap.get("PROD_ID"))); // Inward clearing a/c productid
        map.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(resultMap.get("ACCT_NO"))); // ic a/c no
        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("AC_HD_ID"))); // prod a/c head
        map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))); // ic a/c no branch
        //        map.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        //        map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(dataMap.get("CLEARING_SUSPENSE_HD"))) ; // prod a/c head
        //        map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(dataMap.get("BRANCH_ID"))) ; // ic a/c no branch
        map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))); // local logged branch
        map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
        map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("CLEARING_HD"))); // charge a/c get it from clearing_bank_param
        map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // gl

        TransferTrans trans = new TransferTrans();
        trans.setInitiatedBranch(_branchCode);
        trans.setTransMode(CommonConstants.TX_CLEARING);
        if (CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals("TL") || CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals("AD") || CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals("AAD") || CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals("ATL")) {
            trans.setLoanDebitInt("CLEARING_BOUNCED");
        }
        ArrayList batchList = new ArrayList();
        double amt = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
        batchList.add(trans.getDebitTransferTO(map, amt));
        batchList.add(trans.getCreditTransferTO(map, amt));

        trans.doDebitCredit(batchList, _branchCode);
        trans.setLoanDebitInt("");
        batchList = null;
//            }
    }

    private void updateTranCharges(HashMap resultMap, HashMap dataMap) throws Exception {
        Double chrg = CommonUtil.convertObjToDouble(resultMap.get("OUTWARD_RETURN_CHRG"));
        double charges = chrg.doubleValue();
        returnRemarksMap = new HashMap();
//        Double chrg = CommonUtil.convertObjToDouble(resultMap.get("INWARD_RETURN_CHRG"));
//        double charges = chrg.doubleValue();
        String actNum = CommonUtil.convertObjToStr(resultMap.get("ACCT_NO"));
        resultMap.put("ACCOUNTNO", actNum);
        String prodType = CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE"));
        if (CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals("OA") || CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals("AD")) {
            if (actNum.length() > 0) {
                List list = (List) sqlMap.executeQueryForList("get" + prodType + "Balance", resultMap);
                HashMap outputMap = (HashMap) list.get(0);

                double availableBalance = CommonUtil.convertObjToDouble(outputMap.get("AVAILABLE_BALANCE")).doubleValue();
                double limit = CommonUtil.convertObjToDouble(outputMap.get("TOD_LIMIT")).doubleValue();
                String status = CommonUtil.convertObjToStr(outputMap.get("STATUS"));
                Date lastTransDt = (Date) outputMap.get("LAST_TRANS_DT");

                if (status.equals("COMP_FREEZE")) {
                    returnRemarksMap.put("REMARKS", TransactionConstants.COMP_FREEZE);
                    charges = 0;
                } else {
                    if ((availableBalance + limit) < charges) {
                        returnRemarksMap.put("REMARKS", TransactionConstants.INSUFFICIENT_BALANCE);
                        charges = 0;
                    }
                }

            }
        } else {
            charges = 0;
        }

        if (charges > 0) {
            HashMap map = new HashMap();
            map.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")));
            map.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(resultMap.get("PROD_ID"))); // Inward clearing a/c productid
            map.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(resultMap.get("ACCT_NO"))); // ic a/c no
            map.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("AC_HD_ID"))); // prod a/c head
            map.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))); // ic a/c no branch
            map.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(resultMap.get("BRANCH_ID"))); // local logged branch
            map.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);
            map.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(resultMap.get("OUTWARD_RETURN_HD"))); // charge a/c get it from clearing_bank_param
            map.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // gl
            map.put(TransferTrans.DR_INSTRUMENT_2, objTO.getInstrumentNo2());
            map.put(TransferTrans.PARTICULARS, "Inst Return Chg");
            TransferTrans trans = new TransferTrans();
            ArrayList batchList = new ArrayList();
            trans.setInitiatedBranch(_branchCode);
            if (CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals("AD") || CommonUtil.convertObjToStr(resultMap.get("PROD_TYPE")).equals("TL")) {
                trans.setLoanDebitInt("OTHERCHARGES");
            }
            batchList.add(trans.getDebitTransferTO(map, charges));
            batchList.add(trans.getCreditTransferTO(map, charges));

            trans.doDebitCredit(batchList, _branchCode);
            batchList = null;
            trans.setLoanDebitInt("");
            dataMap.put("CHRG", chrg);
            sqlMap.executeUpdate("updateReturnCharge", dataMap);
        }
    }

    private void destroyObjects() {
        objTO = null;
        returnId = "";
    }
}
