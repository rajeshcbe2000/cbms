/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransferDAO.java
 *
 * Created on June 18, 2003, 4:14 PM
 */
package com.see.truetransact.serverside.rtgsneftfiletrans;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.deposit.lien.DepositLienDAO;
import com.see.truetransact.serverside.transaction.common.Transaction;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Suresh R
 *
 *
 */
public class RtgsNeftFileTransDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private final Logger log = Logger.getLogger(RtgsNeftFileTransDAO.class);
    private Transaction transModuleBased;
    private String loanParticulars = "";
    private String batchId = "";
    private ArrayList toList;
    private double drAmt = 0;
    private double crAmt = 0;
    private boolean isException = false;
    private HashMap insertMap = new HashMap();
    private boolean forProcCharge = false;
    private boolean forLoanDebitInt = false;
    private HashMap allTermLoanAmt = null;
    private String debitLoanType = null;
    private ArrayList dailyDepList;
    private Date properFormatDate;
    private DepositLienDAO depositLienDAO = new DepositLienDAO();
    private DepositLienTO depositLienTO = new DepositLienTO();
    private double flexiAmount = 0.0;
    private HashMap flexiDeletionMap = new HashMap();
    private boolean depositClosingFlag = false;
    private boolean valueDateFlag = false;
    private HashMap valueDateMap = null;
    private int transferTOID = 0;
    ReconciliationTO reconciliationTO = new ReconciliationTO();
    private String act_closing_min_bal_check = null;
    private String selectedSourceScreen = "";
    private Map corpLoanMap = null; // For Corporate Loan purpose added by Rajesh
    private String user = "";
    HashMap otherChargesMap;
    private String shift = "";
    private String shifttime = "";
    private Map cache;
    private double delayedAmount = 0.0;//used to hold references to Resources for re-use
    private TxTransferTO obj = null;
    private HashMap orgDetails = null;
    private String fromScreen = "";
    private String db_Driver_name = "";
    private HashMap interBranchAllowMap = new HashMap();
    private ReadFilesFromFolder readFile = null;

    public RtgsNeftFileTransDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    /*
     * method for getting the desired data from the database against some query
     */

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        HashMap returnMap = new HashMap();
        db_Driver_name = CommonUtil.convertObjToStr(obj.get(CommonConstants.DB_DRIVER_NAME));
        String map = (String) obj.get(CommonConstants.MAP_NAME);
        System.out.println("############ transferDAO getData Map : " + map + "sqlMap" + sqlMap);
        HashMap whereMap = null;
        String where = null;
        List list = null;
        if (obj.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
            whereMap = (HashMap) obj.get(CommonConstants.MAP_WHERE);
            list = (List) sqlMap.executeQueryForList(map, whereMap);
        } else {
            where = (String) obj.get(CommonConstants.MAP_WHERE);
            list = (List) sqlMap.executeQueryForList(map, where);
        }
        returnMap.put(CommonConstants.DATA, (ArrayList) list);
        obj = null;
        return returnMap;
    }
    /*
     * method which will do INSERT/ UPDATE operation based on the command passed
     */

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        return execute(map, true);
    }

    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        HashMap execReturnMap = new HashMap();
        String type = "";
        System.out.println("############ RTGS/NEFT FileTransDAO execute Map () : " + map);
        try {
            db_Driver_name = CommonUtil.convertObjToStr(map.get(CommonConstants.DB_DRIVER_NAME));
            type = CommonUtil.convertObjToStr(map.get("TYPE"));
            doProcess();        //DO PROCESS
        } catch (Exception e) {
            e.printStackTrace();
            if (isTransaction) {
                sqlMap.rollbackTransaction();
            }
            throw e;
        }
        System.out.println("############ RTGS_NEFT File TransDAO ReturnMap : " + execReturnMap);
        return execReturnMap;
    }

    private void doProcess() throws Exception {
        if (CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).length() > 0
                && CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).equals("ICICI")) {
            System.out.println("############ RTGS_NEFT doProcess() : ");
            String path = "";
            ReadFilesFromFolder readFile = null;
            if (CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_OUTWARD_PATH).length() > 0) {
                readFile = new ReadFilesFromFolder();
                readFile.outward_RTGSNEFT_ICICI("0001");                                    //RTGS/NEFT MANUAL SCREEN TRANSACTIONS
            }
            if (CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_INWARD_PATH).length() > 0) {
                path = CommonConstants.RTGS_NEFT_INWARD_PATH;
                readFile = new ReadFilesFromFolder(path);
                readFile.listFilesForFolder(readFile.folder, sqlMap, "0001", "OUTWARD");    //OUTWARD TRANS (INWARD + INWARD_ACK)
            }
            if (CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_INWARD_PATH).length() > 0) {
                path = CommonConstants.RTGS_NEFT_INWARD_PATH;
                readFile = new ReadFilesFromFolder(path);
                readFile.listFilesForFolder(readFile.folder, sqlMap, "0001", "OUTWARD_RETURN"); //OUTWARD RETURN
            }
        } else if (CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).length() > 0
                && CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).equals("AXIS")) {
            System.out.println("############ RTGS_NEFT AXIS doProcess() : ");
            String path = "";
            ReadFilesFromFolder readFile = null;
            if (CommonUtil.convertObjToStr(CommonConstants.AXIS_RTGS_NEFT_OUTWARD_PATH).length() > 0) {
                readFile = new ReadFilesFromFolder();
                readFile.outward_RTGSNEFT_AXIS("0001");                                    //RTGS/NEFT MANUAL SCREEN TRANSACTIONS
            }
            if (CommonUtil.convertObjToStr(CommonConstants.AXIS_RTGS_NEFT_INWARD_PATH).length() > 0) {
                path = CommonConstants.AXIS_RTGS_NEFT_INWARD_PATH;
                readFile = new ReadFilesFromFolder(path);
                readFile.listFilesForFolder(readFile.folder, sqlMap, "0001", "INWARD");    //INWARD TRANS (INWARD + INWARD_ACK)
            }
            if (CommonUtil.convertObjToStr(CommonConstants.AXIS_RTGS_NEFT_INWARD_RETURN_PATH).length() > 0) {
                path = CommonConstants.AXIS_RTGS_NEFT_INWARD_RETURN_PATH;
                readFile = new ReadFilesFromFolder(path);
                readFile.listFilesForFolder(readFile.folder, sqlMap, "0001", "OUTWARD_RETURN"); //OUTWARD RETURN
            }
        } else if (CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).length() > 0
                && CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).equals("SBI")) {
            System.out.println("############ RTGS_NEFT SBI doProcess() : ");
            String path = "";
            ReadFilesFromFolder readFile = null;
            if (CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_OUTWARD_PATH).length() > 0) {
                readFile = new ReadFilesFromFolder();
                readFile.outward_RTGSNEFT_SBI("0001"); //RTGS/NEFT MANUAL SCREEN TRANSACTIONS
            }
            if (CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_INWARD_PATH).length() > 0) {
                path = CommonConstants.RTGS_NEFT_INWARD_PATH;
                readFile = new ReadFilesFromFolder(path);
                readFile.listFilesForFolder(readFile.folder, sqlMap, "0001", "OUTWARD");    //OUTWARD TRANS (INWARD + INWARD_ACK)
            }            
            if (CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_INWARD_PATH).length() > 0) {
                path = CommonConstants.RTGS_NEFT_INWARD_PATH;
                readFile = new ReadFilesFromFolder(path);
                readFile.listFilesForFolder(readFile.folder, sqlMap, "0001", "OUTWARD_RETURN"); //OUTWARD RETURN
            }
            
        }
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

    private String getFormattedFullDate(String bankType) throws Exception {
        java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat();
        if (bankType.equals("DCCB")) {
            DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy");
        } else {
            DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        }
        return DATE_FORMAT.format(new Date());
    }

    //Added By Suresh   18-March-2014
    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) properFormatDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public Date getProperFormatDate(Object obj) {
        Date currDt = null;
        currDt = properFormatDate;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) currDt.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
}
