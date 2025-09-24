/*
 * Copyright 2004 SeE Consulting (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of SeE Consulting (P) Ltd..
 * Use is subject to license terms.
 *
 * ElectronicReconciliationRequestDAO.java
 *
 * Created on Wed Nov 13 13:59:17 IST 2019
 * 
 * Created by Sathiya
 */
package com.see.truetransact.serverside.transaction.electronicreconciliation;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.*;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.prowidesoftware.swift.model.SwiftTagListBlock;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.Field61;
import com.prowidesoftware.swift.model.mt.mt9xx.MT940;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

/**
 * ElectronicReconciliationRequestDAO
 *
 */
public class ElectronicReconciliationDAO extends TTDAO {

    private SqlMap sqlMap = null;
    private String command = "";
    private LogDAO logDAO;
    private LogTO logTO;
    private String branchID = "";
    private Date currDt = null;
    HashMap returnMap = new HashMap();
    int processedCount = 0;
    double porcessedAmount = 0;
    boolean callFromOtherDAO = false;

    /**
     * Creates a new instance of ElectronicPaymentDAO
     */
    public ElectronicReconciliationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        return returnMap;
    }

    public static void main(String str[]) {
        try {
            ElectronicReconciliationDAO dao = new ElectronicReconciliationDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        HashMap returnData = null;
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("ElectronicReconciliationRequestDAO ###### : " + map);
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));

        if (map.containsKey("HDFC_REFERENCE_NO") && map.containsKey("OUR_UTR_NUMBER")) {
            HashMap whereMap = new HashMap();
            returnData = new HashMap();
            whereMap.put("HDFC_REFERENCE_NO",CommonUtil.convertObjToStr(map.get("HDFC_REFERENCE_NO")));
            whereMap.put("PAYMENT_TRANS_DT",getProperFormatDate(map.get("PAYMENT_TRANS_DT")));
            whereMap.put("OUR_UTR_NUMBER",CommonUtil.convertObjToStr(map.get("OUR_UTR_NUMBER")));
            whereMap.put("INITIATED_TRANS_DT",getProperFormatDate(map.get("INITIATED_TRANS_DT")));
            whereMap.put("RECONB_ID", getTransactionId());
            sqlMap.executeUpdate("updateHDFCManualMatch", whereMap);
            sqlMap.executeUpdate("updateNEFTManualMatch", whereMap);
            returnData.put("RECON_PROCESS_ID",whereMap.get("RECONB_ID"));
        } else {
            String filePath = CommonUtil.convertObjToStr(map.get("FILE_PATH"));
            String fileUploadType = CommonUtil.convertObjToStr(map.get("FILE_UPLOAD_TYPE"));
            if (fileUploadType != null && fileUploadType.equals("MT940")) {
                List lst = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
                String msg = "";
                Iterator objIterator = lst.iterator();
                while (objIterator.hasNext()) {
                    msg += objIterator.next() + "\n";
                }
                System.out.println(msg);
                /*
                 * Parse the String content into a SWIFT message object
                 */
                MT940 mt = MT940.parse(msg);

                /*
                 * Get the loop between 60[F,M] and 62[F,M]
                 */
                Tag start = mt.getSwiftMessage().getBlock4().getTagByNumber(60);
                Tag end = mt.getSwiftMessage().getBlock4().getTagByNumber(62);
                SwiftTagListBlock loop = mt.getSwiftMessage().getBlock4().getSubBlock(start, end);
                System.out.println("Started : ");
                for (int i = 0; i < loop.size(); i++) {
                    Tag t = loop.getTag(i);
                    if (t.getName().equals("61")) {
                        Field61 tx = (Field61) t.asField();
                        System.out.println("---------------------------");
                        System.out.println("Trans date: " + tx.getComponent(Field61.VALUE_DATE));
                        System.out.println("Cr/Dr code: " + tx.getComponent(Field61.DC_MARK));
                        System.out.println("Id code: " + tx.getComponent(Field61.IDENTIFICATION_CODE));
                        System.out.println("Amount: " + tx.getComponent(Field61.AMOUNT));
                        System.out.println("Transaction Type: " + tx.getComponent(Field61.TRANSACTION_TYPE));
                        System.out.println("Identification: " + tx.getComponent(Field61.IDENTIFICATION_CODE)); //since version 7.8
                        System.out.println("REFERENCE_FOR_THE_ACCOUNT_OWNER : " + tx.getComponent(Field61.REFERENCE_FOR_THE_ACCOUNT_OWNER));
                        System.out.println("REFERENCE_OF_THE_ACCOUNT_SERVICING_INSTITUTION : " + tx.getComponent(Field61.REFERENCE_OF_THE_ACCOUNT_SERVICING_INSTITUTION));
                        System.out.println("Reference Acc SRU : " + tx.getComponent(Field61.SRU));
                        System.out.println("SUPPLEMENTARY_DETAILS : " + tx.getComponent(Field61.SUPPLEMENTARY_DETAILS));
//                System.out.println("COMPONENTS_PATTERN : " + CommonUtil.convertObjToInt(tx.getComponent(Field61.COMPONENTS_PATTERN)));
//                System.out.println("PARSER_PATTERN : " + tx.getComponent(Field61.PARSER_PATTERN));
                        String transType = tx.getComponent(Field61.DC_MARK);
                        System.out.println("transType : " + transType);
                        /*
                         * look ahead for field 86
                         */
                        HashMap mt940Map = new HashMap();
                        if (i + 1 < loop.size() && loop.getTag(i + 1).getName().equals("86")) {
                            System.out.println("Description: " + loop.getTag(i + 1).getValue());
                            mt940Map.put("DESCRIPTION", loop.getTag(i + 1).getValue());
                            i++;
                        }
                        mt940Map.put("TRANS_DT", tx.getComponent(Field61.VALUE_DATE));
                        mt940Map.put("BRANCH", "");
                        mt940Map.put("REFERENCE_NO", tx.getComponent(Field61.SUPPLEMENTARY_DETAILS));
                        mt940Map.put("VALUE", tx.getComponent(Field61.VALUE_DATE));
                        if (CommonUtil.convertObjToStr(transType).equals("D")) {
                            mt940Map.put("DEBITS", tx.getComponent(Field61.AMOUNT).replace(",", "."));
                            mt940Map.put("CREDITS", "0");
                        } else {
                            mt940Map.put("DEBITS", "0");
                            mt940Map.put("CREDITS", tx.getComponent(Field61.AMOUNT).replace(",", "."));
                        }
                        mt940Map.put("BALANCE", "");
                        System.out.println("mt940Map : " + mt940Map);
//                sqlMap.executeUpdate("deleteReconElectronicPaymentMT940", mt940Map);
                        sqlMap.executeUpdate("insertReconElectronicPaymentMT940", mt940Map);
                    }
                }
                System.out.println("MT940 Completed : ");
            } else {
                System.out.println("1PAY Completed : ");
            }
        }
        return returnData;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    public Date getProperFormatDate(Object obj) {
        Date dt = null;
        dt = currDt;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            dt = (Date) currDt.clone();
            dt.setDate(tempDt.getDate());
            dt.setMonth(tempDt.getMonth());
            dt.setYear(tempDt.getYear());
        }
        return dt;
    }
    
    private String getTransactionId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RECON_PROCESS_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    
    private void destroyObjects() {
        processedCount = 0;
        porcessedAmount = 0;
    }
}
