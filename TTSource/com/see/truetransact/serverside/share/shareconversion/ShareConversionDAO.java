/*
 * Copyright 2004 SeE Consulting (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of SeE Consulting (P) Ltd..
 * Use is subject to license terms.
 *
 * AgentDAO.java
 *
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.share.shareconversion;

import com.see.truetransact.serverside.termloan.arbitration.*;
import com.see.truetransact.serverside.termloan.charges.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.Iterator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.termloan.loansubsidy.TermLoanSubsidyTO;
import com.see.truetransact.transferobject.termloan.TermLoanFacilityTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.termloan.arbitration.TermLoanArbitrationTO;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import javax.print.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Agent DAO.
 *
 */
public class ShareConversionDAO extends TTDAO {

    public static String fileName = "/smstemplate.xml";
    private static SqlMap sqlMap = null;
    private double paid_interest = 0;
    private double paid_penal_int = 0;
    private double paid_principal = 0;
    private Iterator smsIterator;
    private TransactionDAO transactionDAO;
    private HashMap prodMap = new HashMap();
    private Date currDt = null;
    private Date auctionDt = null;
    private String user = "";
    private Map cache;                  //used to hold references to Resources for re-use
    private String chitsNo = "";
    private String arbid = "";
    private HashMap returnMap;
    private boolean epEditChk = false;

    /**
     * Creates a new instance of AgentDAO
     */
    public ShareConversionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            TermLoanChargesDAO dao = new TermLoanChargesDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("ShareConversion Map Dao : " + map);
        returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        user = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        try {
            sqlMap.startTransaction();            
            if (map.containsKey("SHARE_CONVERSION")) {
                updateConvertedShares(map);
            }           
            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw e;
        }
        epEditChk = false;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        returnMap.put("ARC_ID", arbid);
        return (returnMap);
    }

    private void updateConvertedShares(HashMap shareMap) {
        try {
            if (shareMap.containsKey("CONVERTED_SHARE_LIST")) {               
                ArrayList shareList;
                String shareConversionClass = "";
                if(shareMap.containsKey("SHARE_CONVERSION_CLASS") && shareMap.get("SHARE_CONVERSION_CLASS") != null){
                    shareConversionClass = CommonUtil.convertObjToStr(shareMap.get("SHARE_CONVERSION_CLASS"));
                }
                shareList = (ArrayList) shareMap.get("CONVERTED_SHARE_LIST");
                System.out.println("arbList ::" + shareList);
                if (shareList.size() > 0) {
                    for (int i = 0; i < shareList.size(); i++) {
                        ArrayList newList = (ArrayList) shareList.get(i);
                        System.out.println("newList "+ i +" ::" + newList);
                        String oldShare = CommonUtil.convertObjToStr(newList.get(0));
                        String convertedShare = CommonUtil.convertObjToStr(newList.get(6));
                        HashMap shareUpdateMap = new HashMap();
                        shareUpdateMap.put("CONVERTED_SHARE",convertedShare);
                        shareUpdateMap.put("SHARE_ACCT_NO",oldShare);
                        shareUpdateMap.put("SHARE_CONVERSION_CLASS",shareConversionClass);
                        sqlMap.executeUpdate("insertShareAcctConversionDetails", shareUpdateMap);
                        sqlMap.executeUpdate("updateShateAcctAfterConversion", shareUpdateMap);
                        sqlMap.executeUpdate("updateShateAcctDetailsAfterConversion", shareUpdateMap);  
                        sqlMap.executeUpdate("updateMDSApplicationAfterConversion", shareUpdateMap);
                        sqlMap.executeUpdate("updateCustomerAfterShareConversion", shareUpdateMap);                        
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    

   
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    public HashMap getData(HashMap obj) throws Exception {
       return obj; 
    }
   
    
     private Date setProperDtFormat(Date dt) {   // Added by nithya on 03-08-2017 for 0007230: Auction Notice Processing issue.
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
}
