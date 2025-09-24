/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSMemberReceiptEntryDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.mdsapplication.mdsmemberreceiptentry;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.mdsapplication.mdsreceiptentry.MDSReceiptEntryDAO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;
import com.see.truetransact.transferobject.mdsapplication.mdsmemberreceiptentry.MDSMemberReceiptEntryTO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * This is used for MDSMemberReceiptEntryDAO Data Access.
 *
 * @author Suresh
 *
 *
 */
public class MDSMemberReceiptEntryDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    private MDSReceiptEntryDAO mdsReceiptEntryDAO = null;
    private MDSReceiptEntryTO mdsReceiptEntryTO = null;
    private MDSMemberReceiptEntryTO mdsMemberReceiptEntryTO = null;
    TransferDAO transferDAO = new TransferDAO();
    private Date currDt = null;
    private Map returnMap = null;
    private Iterator addressIterator1;
    private HashMap memberMap = null;
    private String generateSingleTransId="";
    private String isSplitMDSTransaction="";
    private HashMap wholeSplitMap = new HashMap();
    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public MDSMemberReceiptEntryDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("########" + map);
        HashMap transMap = new HashMap();
        String where = (String) map.get("RECEIPT_ID");
        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where);
        if (list != null && list.size() > 0) {
            transMap.put("TRANSACTION_LIST", list);
            list = null;
        }
        return transMap;
    }
	private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in DAO: " + map);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        returnMap = new HashMap();
        memberMap = new HashMap();
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        System.out.println("$#$#$#$#$ : Command : " + command);
        if (map.containsKey("MDS_MEMBER_DATA")) {
            memberMap = (HashMap) map.get("MDS_MEMBER_DATA");
        }
        if(map.containsKey("IS_SPLIT_MDS_TRANSACTION") && map.get("IS_SPLIT_MDS_TRANSACTION")!=null){
            isSplitMDSTransaction = CommonUtil.convertObjToStr(map.get("IS_SPLIT_MDS_TRANSACTION"));
        }
        try {
            sqlMap.startTransaction();
            objLogTO.setStatus(command);
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                generateSingleTransId = generateLinkID();
                insertData(map, objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map, objLogDAO, objLogTO);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(map, objLogDAO, objLogTO);
            } else if (map.containsKey("AUTHORIZEMAP")) {
                authorize(map);
            } else {
                throw new NoCommandException();
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
        objLogDAO = null;
        objLogTO = null;
        map = null;
        destroyObjects();
        return (HashMap) returnMap;
    }

    private String getMemberReceiptId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MDS_MEMBER_TRANS_ID");
        String memberTransId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return memberTransId;
    }

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {

            if (memberMap != null && memberMap.size() > 0) {
                System.out.println("@##$#$% memberMap #### :" + memberMap);
                String memberTransID = "";
                memberTransID = getMemberReceiptId();
                String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
                double totalAmt = CommonUtil.convertObjToDouble(map.get("TOTAL_NET_AMOUNT")).doubleValue();
                //String memberNo = CommonUtil.convertObjToStr(map.get("MEMBER_NO"));
                addressIterator1 = memberMap.keySet().iterator();
                String key1 = "";
                String transType = "";
                int size = memberMap.size();
                        for (int i = 0; i < size; i++) {
                            mdsReceiptEntryTO = new MDSReceiptEntryTO();
                            mdsMemberReceiptEntryTO = new MDSMemberReceiptEntryTO();
                            key1 = (String) addressIterator1.next();
                            System.out.println("###### key1 ###### : " + key1);
                            HashMap eachChittalMap = new HashMap();
                            eachChittalMap = (HashMap) memberMap.get(key1);
                            mdsReceiptEntryTO.setCommand(CommonUtil.convertObjToStr(map.get("COMMAND")));
                            mdsReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(eachChittalMap.get("SCHEME_NAME")));
                            mdsReceiptEntryTO.setChittalNo(CommonUtil.convertObjToStr(eachChittalMap.get("CHITTAL_NO")));
                            mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(eachChittalMap.get("SUB_NO")));
                            mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(eachChittalMap.get("MEMBER_NAME")));
                            mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(eachChittalMap.get("DIVISION_NO")));
                            mdsReceiptEntryTO.setChitStartDt(setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachChittalMap.get("CHIT_START_DT")))));// Added by nithya on 07-11-2017 for 0006559: Member receipt entry issue in Linux
                            mdsReceiptEntryTO.setChitEndDt(setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachChittalMap.get("SCHEME_END_DT")))));// Added by nithya on 07-11-2017 for 0006559: Member receipt entry issue in Linux
                            mdsReceiptEntryTO.setPaidDate(setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachChittalMap.get("PAID_DATE")))));// Added by nithya on 07-11-2017 for 0006559: Member receipt entry issue in Linux
                            mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(eachChittalMap.get("NO_OF_INSTALLMENTS")));
                            mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(eachChittalMap.get("CURR_INST")));
                            mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(eachChittalMap.get("PENDING_INST")));
                            mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("INST_AMT")));
                            mdsReceiptEntryTO.setTotalInstDue(CommonUtil.convertObjToDouble(eachChittalMap.get("PENDING_DUE_AMT")));
                            mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(eachChittalMap.get("NO_OF_INST_PAY")));
                            mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(eachChittalMap.get("INST_AMT_PAYABLE")));
                            mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(eachChittalMap.get("PAID_INST")));
                            mdsReceiptEntryTO.setPrizedMember(CommonUtil.convertObjToStr(eachChittalMap.get("PRIZED_MEMBER")));
                            mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(eachChittalMap.get("BONUS")));
                            mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("DISCOUNT")));
                            mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(eachChittalMap.get("PENAL")));
                            mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("NOTICE_AMOUNT")));
                            mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("ARBITRATION_AMOUNT")));
                            mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("NET_AMOUNT")));
                            mdsReceiptEntryTO.setNarration(CommonUtil.convertObjToStr(eachChittalMap.get("NARRATION")));
                            mdsReceiptEntryTO.setBankPay("N");
                            mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                            mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                            mdsReceiptEntryTO.setStatusDt(currDt);
                            mdsReceiptEntryTO.setBranchCode(CommonUtil.convertObjToStr(eachChittalMap.get("BRANCH_CODE")));
                            mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(eachChittalMap.get("INITIATED_BRANCH")));
                            mdsReceiptEntryTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                            mdsReceiptEntryTO.setSingleTransId(generateSingleTransId);
                            mdsMemberReceiptEntryTO.setMemberReceiptId(memberTransID);
                            mdsMemberReceiptEntryTO.setMemberNo(CommonUtil.convertObjToStr(eachChittalMap.get("MEMBER_NO")));
                            mdsMemberReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(eachChittalMap.get("SCHEME_NAME")));
                            mdsMemberReceiptEntryTO.setChittalNo(CommonUtil.convertObjToStr(eachChittalMap.get("CHITTAL_NO")));
                            mdsMemberReceiptEntryTO.setSubNo(CommonUtil.convertObjToInt(eachChittalMap.get("SUB_NO")));
                            mdsMemberReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(eachChittalMap.get("PENDING_INST")));
                            mdsMemberReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(eachChittalMap.get("NO_OF_INST_PAY")));
                            mdsMemberReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("TOTAL_PAYABLE")));
                            mdsMemberReceiptEntryTO.setBonusAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("BONUS")));
                            mdsMemberReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("DISCOUNT")));
                            mdsMemberReceiptEntryTO.setTotalPayable(CommonUtil.convertObjToDouble(eachChittalMap.get("INST_AMT_PAYABLE")));
                            mdsMemberReceiptEntryTO.setPenalAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("PENAL")));
                            mdsMemberReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("NOTICE_AMOUNT")));
                            mdsMemberReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("ARBITRATION_AMOUNT")));
                            mdsMemberReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(eachChittalMap.get("NET_AMOUNT")));
                            mdsMemberReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                            mdsMemberReceiptEntryTO.setStatusDt(currDt);
                            mdsMemberReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));

                            map.put("INSTALLMENT_MAP", eachChittalMap.get("EACH_MONTH_DATA"));
                            map.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                            List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", eachChittalMap);
                            if (closureList != null && closureList.size() > 0) {
                                map.put("MDS_CLOSURE", "MDS_CLOSURE");
                            } else {
                                map.remove("MDS_CLOSURE");
                            }
                            map.put("FROM_MDS_MEMBER_RECEIPT", "FROM_MDS_MEMBER_RECEIPT");
                            map.put("MEMBER_RECEIPT_SINGLE_ID",generateSingleTransId);   
                            if (map.containsKey("mdsReceiptSplitEntryTOList") && map.get("mdsReceiptSplitEntryTOList") != null) {
                                List mdssReceiptList = (List) map.get("mdsReceiptSplitEntryTOList"); 
                                //System.out.println("mdsReceiptSplitEntryTOList111111%##%#%#%"+mdssReceiptList);
                                List splitMDSReceiptEntryLst = new ArrayList();
                                List MDSReceiptEntryLst = new ArrayList();
                                if (mdssReceiptList != null && mdssReceiptList.size() > 0) {
                                    //for (int j = 0; j < mdssReceiptList.size(); j++) {
                                    splitMDSReceiptEntryLst.add(mdssReceiptList.get(i));
                                    //System.out.println("splitMDSReceiptEntryLst^^^#^#^#^#^"+splitMDSReceiptEntryLst);
                                    //System.out.println("splitMDSReceiptEntryLst^^^#^#^#^#^"+splitMDSReceiptEntryLst.get(0));                                    
                                    List receiptList = (List) splitMDSReceiptEntryLst.get(0);
                                    int size1 = receiptList.size();
                                    for(int j = 0; j < size1; j++){                                        
                                        //System.out.println("receiptList^^^#^#^#^#^"+receiptList);
                                        MDSReceiptEntryTO MDSTo = (MDSReceiptEntryTO) receiptList.get(j);
                                        //System.out.println("MDSTo%##%#%#%"+MDSTo);
                                       
                                        MDSReceiptEntryLst.add(MDSTo);
                                    }
                                    map.put("mdsReceiptEntryTOList",MDSReceiptEntryLst); 
                                    //}
                                }
                            }                            
                            HashMap transMap = new HashMap();
                            mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                            transMap = mdsReceiptEntryDAO.execute(map, false);
                            // INSERT_TRANSACTION
                            System.out.println("################# transMap : " + transMap);
                            if (i == 0) {
                                if (map.containsKey("TransactionTO")) {
                                    TransactionTO transactionTO = new TransactionTO();
                                    HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                                    LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                                    if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                        allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                    }
                                    transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                                    transType = CommonUtil.convertObjToStr(transactionTO.getTransType());
                                    transactionTO.setBatchId(memberTransID);
                                    transactionTO.setBatchDt(currDt);
                                    transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                    transactionTO.setTransId(memberTransID);
                                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                                }
                            }
                            if (transType.equals("TRANSFER")) {
                                  //if(i==0 ){
                                  //	returnMap.put("TRANSFER_FROM_ID",transMap.get("BATCH_ID"));
                                 // }
                                //  if(i == memberMap.size()-1 ){
                                String transferSingleId = "";
                                HashMap wMap = new HashMap();
                                wMap.put("BATCH_ID", transMap.get("BATCH_ID"));
                                wMap.put("TRANS_DT", currDt);
                                List aList = ServerUtil.executeQuery("getSingleIdTr", wMap);
                                if (aList != null && aList.size() > 0) {
                                    HashMap mapop = (HashMap) aList.get(0);
                                    transferSingleId = mapop.get("SINGLE_TRANS_ID").toString();
                                    returnMap.put("SINGLE_TANS_ID", transferSingleId);
                                    returnMap.put("TRANSFER", "TRANSFER");
                                }
                            }
                            if (transType.equals("CASH")) {
                               // if(i==0 ){
                                //     returnMap.put("CASH_FROM_ID",transMap.get("BATCH_ID"));
                                //    returnMap.put("TRANSFER_FROM_ID",transMap.get("BONUS_TRANS_ID"));
                               // }
                                String cashSingleId = "";
                                HashMap wMap = new HashMap();
                                wMap.put("BATCH_ID", transMap.get("BATCH_ID"));
                                wMap.put("TRANS_DT", currDt);
                                List aList = ServerUtil.executeQuery("getSingleIdCash", wMap);
                                if (aList != null && aList.size() > 0) {
                                    //if(i==0 ){
                                    HashMap mapop = (HashMap) aList.get(0);
                                    cashSingleId = mapop.get("SINGLE_TRANS_ID").toString();
                                    returnMap.put("SINGLE_TANS_ID", cashSingleId);
                                    returnMap.put("CASH", "CASH");
                             //       returnMap.put("TRANSFER_TO_ID",transMap.get("BONUS_TRANS_ID"));
                            }
                        }
                     sqlMap.executeUpdate("insertMDSMemberReceiptEntryTO", mdsMemberReceiptEntryTO);
                     //if(isSplitMDSTransaction!=null && isSplitMDSTransaction.equals("Y")){
                       //  break;//Added by sreekrishnan
                     //}
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void deleteData(HashMap chargesMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
    }

    private void authorize(HashMap map) throws Exception {
        try{
        HashMap authMap = new HashMap();
        mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
        authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthorizeMap = new HashMap();
        AuthorizeMap = (HashMap) selectedList.get(0);
        HashMap memberRecMap = new HashMap();
        memberRecMap.put("MEMBER_RECEIPT_ID", map.get("MEMBER_RECEIPT_ID"));
        memberRecMap.put("PAID_DATE", currDt);
        List memberReclst = (List) sqlMap.executeQueryForList("getMDSMemberReceiptDetails", memberRecMap);
        if (memberReclst != null && memberReclst.size() > 0) {
            for (int i = 0; i < memberReclst.size(); i++) {
                memberRecMap = new HashMap();
                memberRecMap = (HashMap) memberReclst.get(i);
                mdsReceiptEntryTO = new MDSReceiptEntryTO();
                mdsReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(memberRecMap.get("SCHEME_NAME")));
                mdsReceiptEntryTO.setChittalNo(CommonUtil.convertObjToStr(memberRecMap.get("CHITTAL_NO")));
                mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(memberRecMap.get("SUB_NO")));
                mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(memberRecMap.get("NO_OF_INST_PAY")));
                mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(memberRecMap.get("INST_AMT")));
                mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(memberRecMap.get("PENAL_AMT_PAYABLE")));
                mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(memberRecMap.get("BONUS_AMT_PAYABLE")));
                mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(memberRecMap.get("DISCOUNT_AMT")));
                mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(memberRecMap.get("NET_AMT")));
                mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(memberRecMap.get("CURR_INST")));
                mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(memberRecMap.get("PENDING_INST")));
                mdsReceiptEntryTO.setBankPay(CommonUtil.convertObjToStr(memberRecMap.get("BANK_PAY")));
                if(memberRecMap.containsKey("NOTICE_AMT") && memberRecMap.get("NOTICE_AMT") != null){
                  mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(memberRecMap.get("NOTICE_AMT")));  
                }if(memberRecMap.containsKey("ARBITRATION_AMT") && memberRecMap.get("ARBITRATION_AMT") != null){
                  mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(memberRecMap.get("ARBITRATION_AMT")));  
                }                     
                HashMap authorizeMap = new HashMap();
                authorizeMap = (HashMap) map.get("AUTHORIZEMAP");
                authorizeMap.put("NET_TRANS_ID", memberRecMap.get("NET_TRANS_ID"));
                authorizeMap.put("USER_ID", map.get("USER_ID"));
                authorizeMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                String authorizeStatus = CommonUtil.convertObjToStr(authorizeMap.get("AUTHORIZESTATUS"));
                mdsReceiptEntryTO.setCommand(CommonUtil.convertObjToStr(memberRecMap.get("authorizeStatus")));
                map.put("AUTHORIZEMAP", authorizeMap);
                map.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                map.put("SCHEME_NAME", CommonUtil.convertObjToStr(memberRecMap.get("SCHEME_NAME")));
                if (map.containsKey("IS_SPLIT_MDS_TRANSACTION") && map.get("IS_SPLIT_MDS_TRANSACTION") != null) {
                    isSplitMDSTransaction = CommonUtil.convertObjToStr(map.get("IS_SPLIT_MDS_TRANSACTION"));
                    if(isSplitMDSTransaction!=null && isSplitMDSTransaction.equals("Y")){
                        HashMap editMap = new HashMap();
                        editMap.put("TRANS_ID", CommonUtil.convertObjToStr(memberRecMap.get("NET_TRANS_ID")));
                        editMap.put("TRANS_DT", currDt);
                        editMap.put("INITIATED_BRANCH", _branchCode);
                        List list = (List) sqlMap.executeQueryForList("getMDSReceiptEntryTO", editMap);
                        wholeSplitMap.put("MDSReceiptEntryTO", list);
                        System.out.println("wholeSplitMap^$^$^$^$^$^$^$^^"+wholeSplitMap);
                        map.put("WHOLE_SPLIT_MAP", wholeSplitMap);
                        map.put("IS_SPLIT_MDS_TRANSACTION",isSplitMDSTransaction);
                    }
                }                
                System.out.println("map^$^$^$^$^$^$^$^^"+map);
                mdsReceiptEntryDAO.execute(map, false);  
            }
            sqlMap.executeUpdate("authorizeMemberReceiptDetails", AuthorizeMap);
        }
        map = null;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }
    
    private Date setProperDtFormat(Date dt) {   //Added by nithya
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void destroyObjects() {
    }

    public static void main(String str[]) {
        try {
            MDSMemberReceiptEntryDAO dao = new MDSMemberReceiptEntryDAO();
            HashMap inputMap = new HashMap();
            System.out.println(sqlMap.executeQueryForList("OperativeAcctProduct.getSelectAcctHeadTOList", null));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
