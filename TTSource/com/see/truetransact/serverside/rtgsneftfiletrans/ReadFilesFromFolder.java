/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ReadFilesFromFolder.java
 */

package com.see.truetransact.serverside.rtgsneftfiletrans;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.rtgsneftfiletrans.NeftRtgsAcknowledgementTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.remittance.rtgs.RtgsRemittanceTO;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Suresh R
 *
 *
 */
public class ReadFilesFromFolder {

    public File folder = new File("E:/MDCCB/Outward/NEFT/N06");
    private static String temp = "";
    private static SqlMap sqlMap = null;
    NeftRtgsAcknowledgementTO neftTO = new NeftRtgsAcknowledgementTO();
    String branchID = "";
    Date currDt = null;
    String type = "";
    private TransactionDAO transactionDAO = null;
    private boolean isInwardExcepRR42 = false;
    private int constants = 2;

    public ReadFilesFromFolder(String path) throws ServiceLocatorException, Exception {
        //System.out.println("############# ReadFilesFromFolder() : " + path);
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        folder = new File(path);
    }

    public ReadFilesFromFolder() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public void listFilesForFolder(final File folder, SqlMap sMap, String _branchCode, String inputString) throws ServiceLocatorException, Exception {
        sqlMap = sMap;
        branchID = _branchCode;
        currDt = ServerUtil.getCurrentDate(branchID);
        if (folder.isDirectory() && folder.list().length > 0) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry, sMap, _branchCode, inputString);
                } else if (fileEntry.isFile()) {
                    temp = fileEntry.getName();
                    if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("txt")) {
                        //System.out.println("########## list file for folder File    = " + folder.getAbsolutePath() + "/" + fileEntry.getName());
                    }
                    if (CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).length() > 0
                            && CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).equals("ICICI")) {
                        //System.out.println("########## fileEntry.getName() : " + fileEntry.getName());
                        if (inputString.equals("OUTWARD") && fileEntry.getName().startsWith(CommonConstants.RTGS_NEFT_ECOLL_CODE)) {                //OUTWARD TRANSACTIONS
                                read_ICICI_InputFile(fileEntry);                //RTGS/NEFT INWARD TRANSACTIONS
                        } else if (inputString.equals("OUTWARD_RETURN") && fileEntry.getName().startsWith(CommonConstants.RTGS_NEFT_CLIENT_CODE)) {   //OUTWARD RETURN (PASS - STATUS UPDATION && FAIL -STATUS UPDATION, REVERSE TRANSACTION)
                            readData_From_Outward_ReturnFile(fileEntry);
                        }
                    } else if (CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).length() > 0
                            && CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).equals("AXIS")) {
                        if (inputString.equals("INWARD")) {                //INWARD TRANSACTIONS
                            readAXISInwardInputFile(fileEntry);                //RTGS/NEFT INWARD TRANSACTIONS
                        } else if (inputString.equals("OUTWARD_RETURN")) {   //OUTWARD RETURN (PASS - STATUS UPDATION && FAIL -STATUS UPDATION, REVERSE TRANSACTION)
                            readAXISDataFromOutwardReturnFile(fileEntry);
                        }
                    } else if (CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).length() > 0
                            && CommonUtil.convertObjToStr(CommonConstants.RTGS_BANK_NAME).equals("SBI")) {
                        System.out.println("########## fileEntry.getName() : " + fileEntry.getName());
                        if (inputString.equals("OUTWARD") && fileEntry.getName().startsWith(CommonConstants.RTGS_NEFT_CLIENT_CODE+"_SBIMIS")) {                //OUTWARD TRANSACTIONS
                                read_SBI_InputFile(fileEntry);                //RTGS/NEFT INWARD TRANSACTIONS
                        }else if (inputString.equals("OUTWARD_RETURN") && fileEntry.getName().startsWith(CommonConstants.RTGS_NEFT_CLIENT_CODE)) {   //OUTWARD RETURN (PASS - STATUS UPDATION && FAIL -STATUS UPDATION, REVERSE TRANSACTION)
                            readSBIData_From_Outward_ReturnFile(fileEntry);
                        }
                    }
                }
            }
        } else {
            System.out.println("Files not exist in this Directory...");
        }
    }
    
    private String getOutwardFileID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "OUTWARD_FILE_ID");
        String outwardFileID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return outwardFileID;
    }

    public void outward_RTGSNEFT_ICICI(String branch_id) throws Exception {     //RTGS/NEFT MANUAL SCREEN TRANSACTIONS
        System.out.println("######## 1 outward_RTGSNEFT_ICICI() PROCESS STARTED : ");
        HashMap dataMap = new HashMap();
        HashMap authMap = new HashMap();
        currDt = ServerUtil.getCurrentDate(branch_id);
        DateFormat years2digit = new SimpleDateFormat("yy");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dtFormtWithOutMin = new SimpleDateFormat("yyyyMMdd");
        DateFormat dtMiddleWithOutMin = new SimpleDateFormat("yyyyddMM");
        DateFormat minOnly = new SimpleDateFormat("hhmm");
        Calendar calender = Calendar.getInstance();
        HashMap singleUTRMap = new HashMap();
        Date dt = new Date();
        String path = "";
        String fileName = "";
        String transAmt = "";
        String pipeDelimiter = "|";
        int daysInYear = 0;
        java.io.FileWriter write = null;
        java.io.PrintWriter print_line = null;
        String neftUniqueId = "", sequnceNum = "", dayinyearStr = "";
        StringBuffer dayInYearbuffer = new StringBuffer();
        StringBuffer utrBuffer = new StringBuffer();
        RtgsRemittanceTO resultTo = null;
        try {
            List RTGSlist = (List) sqlMap.executeQueryForList("getRTGSRemittanceTOForFileCreation", singleUTRMap);
            if (RTGSlist != null && RTGSlist.size() > 0) {
                for (int i = 0; i < RTGSlist.size(); i++) {
                    resultTo = (RtgsRemittanceTO) RTGSlist.get(i);
                    if (resultTo != null) {
                        dateFormat.format(resultTo.getBatchDt());
                        neftUniqueId = CommonUtil.convertObjToStr(resultTo.getUtrNumber());
                        //System.out.println("#### resultTo.getUtrNumber() : " + resultTo.getUtrNumber() + " ###dateFormat : "
                        //+ dateFormat.format(calender.getTime()) + "check" + DateUtil.getStringDate(resultTo.getBatchDt()));
                        if (i == 0) {
                            String str1 = ""; //Added By Suresh 15-Feb-2018 File name changes
                            DateFormat dateFormat1 = new SimpleDateFormat("ddMMyy");
                            Calendar calender1 = Calendar.getInstance();
                            dateFormat1.format(calender1.getTime());
                            str1 = dateFormat1.format(calender1.getTime());
                            //System.out.println("######## generateRandomNumbers 2 : " + str1);
                            String outwardFileID = "";
                            outwardFileID = getOutwardFileID();
                            //System.out.println("######## random_Number  2 : " + outwardFileID);
                            //fileName = CommonConstants.RTGS_NEFT_CLIENT_CODE + "_" + CommonConstants.RTGS_NEFT_FORMAT_CODE
                                    //+ "_" + generateRandomNumbers() + "_" + CommonConstants.RTGS_BANK_NAME + ".TXT";
                            fileName = CommonConstants.RTGS_NEFT_CLIENT_CODE + "_" + CommonConstants.RTGS_NEFT_FORMAT_CODE
                                    + "_" + str1+"_"+ outwardFileID +".TXT";
                            path = CommonConstants.RTGS_NEFT_OUTWARD_PATH + fileName;
                            write = new java.io.FileWriter(path, false);
                            print_line = new java.io.PrintWriter(write);
                            System.out.println("###### Outward File Name Path : " + path);
                        }
                        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                        daysInYear = localCalendar.get(Calendar.DAY_OF_YEAR);
                        dayinyearStr = String.valueOf(daysInYear);
                        if (dayinyearStr.length() == 2) {
                            dayInYearbuffer.append("0" + dayinyearStr);
                        } else if (dayinyearStr.length() == 1) {
                            dayInYearbuffer.append("00" + dayinyearStr);
                        } else {
                            dayInYearbuffer.append(dayinyearStr);
                        }
                        if (neftUniqueId != null && neftUniqueId.length() > 0) {      //TOTAL COLUMNS 15 AS PER FINAL DISCUSSION & MAIL ON 20-NOV-2017
                            print_line.print("I");                              //1 Record_Identifier 	Always "i"
                            //06-Mar-2018 Referred from Ginson Mail i -->I
                            print_line.print(pipeDelimiter);
                            if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("NEFT")) {
                                print_line.print("N");                          //2 Payment Indicator
                            } else if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("RTGS")) {
                                print_line.print("R");
                            } else if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("IMPS")) {
                                print_line.print("M");
                            } else if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("ICICI_FT")) {
                                print_line.print("I");
                            }
                            print_line.print(pipeDelimiter);
                            print_line.print(neftUniqueId);                     //3 UNIQUE ID   -M
                            print_line.print(pipeDelimiter);
                            //BENEFICARY CODE OPTIONAL                          //4 BENEFICARY CODE   O
                            print_line.print(pipeDelimiter);
                            String beneficiaryName = "";
                            beneficiaryName = resultTo.getBeneficiary_Name().replaceAll("[\\.\\/\\[\\{\\}\\;\\:\\`!@#$%^&*()_<>?\\,\\+\\-\\=\\|]\\~\\'", "");
                            print_line.print(beneficiaryName);                  //5 BENEFICARY NAME -M
                            print_line.print(pipeDelimiter);
                            transAmt = CommonUtil.convertObjToStr(resultTo.getAmount());
                            if (transAmt.contains(".")) {
                                String number = transAmt.substring(transAmt.indexOf(".")).substring(1);
                                if (number.length() == 1) {
                                    transAmt = transAmt + "0";
                                }
                            } else {
                                transAmt = transAmt + ".00";
                            }
                            print_line.print(transAmt);                         //6 TRANSACTION AMOUNT  M=MANDATORY
                            print_line.print(pipeDelimiter);
                            String dts = DateUtil.getStringDate(resultTo.getBatchDt());
                            print_line.print(DateUtil.getStringDate(resultTo.getBatchDt())); //7 DATE OF PAYMENT
                            print_line.print(pipeDelimiter);
                            print_line.print(CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_DR_ACT_NUM)); //8 DEBIT ACT NUMBER
                            print_line.print(pipeDelimiter);
                            print_line.print(CommonUtil.convertObjToStr(resultTo.getAccount_No())); //9 BENEFICARY ACT NUMBER
                            print_line.print(pipeDelimiter);
                            if (!CommonUtil.convertObjToStr(resultTo.getProdId()).equals("ICICI_FT")) {
                                print_line.print(CommonUtil.convertObjToStr(resultTo.getBeneficiary_IFSC_Code()));//10 BENEFICARY IFSCODE
                            }
                            print_line.print(pipeDelimiter);
                            //BENEFICARY BANK NAME                              //11 BENEFICARY BANK NAME
                            print_line.print(pipeDelimiter);
                            //BENEFICARY MAIL ID                                //12 BENEFICARY MAIL ID
                            print_line.print(pipeDelimiter);
                            //BENEFICARY MOBILE NO                              //13 BENEFICARY MOBILE NO
                            print_line.print(pipeDelimiter);
                            print_line.print(CommonUtil.convertObjToStr(resultTo.getDebitAcNum()));  //14 REMITTER ACCOUNT NUMBER
                            print_line.println(pipeDelimiter);
                            //REMARKS                                           //15 REMARKS
                            //print_line.println(pipeDelimiter); //06-Mar-2018 Referred from Ginson Mail
                            if (RTGSlist.size() - 1 == i) {
                                utrBuffer.append("'" + neftUniqueId + "'");
                            } else {
                                utrBuffer.append("'" + neftUniqueId + "'" + ",");
                            }
                        }
                    }
                }
                authMap.put("UTR_NUMBER", utrBuffer);
                authMap.put("PROCESS_DT", currDt.clone());
                authMap.put("FILE_STATUS", "DELIVERED");
                System.out.println("########## authMap : " + authMap);
                sqlMap.executeUpdate("updateOutwardRTGSNEFTStatus", authMap);
                if (print_line != null) {
                    print_line.close();
                }
                if (write != null) {
                    write.close();
                }
            }
            System.out.println("######## 1 outward_RTGSNEFT_ICICI() PROCESS COMPLETED : ");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TTException(e);
        }
    }

    private void read_ICICI_InputFile(File file) throws Exception {
        System.out.println("########## 2 read_ICICI_InputFile() PROCESS STARTED : " + file.getName());
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        HashMap duplicateUTRMap = new HashMap();
        String line = "", inwardUTR = "", remarks = "";
        HashMap dataMap = new HashMap();
        HashMap acHeads = new HashMap();
        String pipeDelimiter = "|";
        String creditAcctHeadId = "", customerName = "";
        StringBuffer UTRNumberBuffer = new StringBuffer();
        boolean UTRFlag = false, isTransAllowed = false;
        NeftRtgsAcknowledgementTO neftTO = new NeftRtgsAcknowledgementTO();
        String client_code = "", beneficaryAccNo = "", benficaryName = "", amount = "", date = "", UTRNumber = "",
                remitterAcNo = "", senderIfscode = "", creditacNo = "", remitterName = "", mode = "", beneficiaryBank = "", status = "";
        try {
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                String[] parts = line.split("\\|");
                for (int i = 0; i < parts.length; i++) {
                    inwardUTR = "";
                    String data = CommonUtil.convertObjToStr(parts[i]);
                    if (i == 4) {  //UTR No
                        inwardUTR = data;
                    }
                    if (inwardUTR.length() > 0) {
                        if (UTRNumberBuffer.length()<= 0) {
                            UTRNumberBuffer.append("'" + inwardUTR + "'");
                        } else {
                            UTRNumberBuffer.append("," + "'" + inwardUTR + "'");
                        }
                    }
                }
            }
            if (UTRNumberBuffer.length() > 0) {
                HashMap utrMap = new HashMap();
                utrMap.put("UTR_NUMBER", UTRNumberBuffer);
                utrMap.put("CURR_DATE", currDt.clone());
                List utrList = sqlMap.executeQueryForList("checkUTRNumberExists", utrMap);
                //SELECT * FROM RTGS_NEFT_ACKNOWLEDGEMENT WHERE UTR_NUMBER IN($UTR_NUMBER$)
                if (utrList != null && utrList.size() > 0) {
                    for (int i = 0; i < utrList.size(); i++) {
                        utrMap = (HashMap) utrList.get(i);
                        duplicateUTRMap.put(CommonUtil.convertObjToStr(utrMap.get("UTR_NUMBER")), "DUPLICATE");
                    }
                    System.out.println("###### DUPLICATE UTR NUMBER MAP : " + duplicateUTRMap);
                }
            }
            fis.close();
            bis.close();
            dis.close();

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            String data = "";
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                neftTO = new NeftRtgsAcknowledgementTO();
                String[] parts = line.split("\\|");
                for (int i = 0; i < parts.length; i++) {
                    //System.out.println("############## i VALUE : " + i + "    #### parts.length : " + parts.length);
                    data = CommonUtil.convertObjToStr(parts[i]);
                    //System.out.println("##### Data : " + data);
                    if (i == 0) { //Client Code
                        client_code = data;
                        //System.out.println("###### client_code 0 :" + client_code);
                        continue;
                    }
                    if (i == 1) { //Bene Account Number
                        beneficaryAccNo = data;
                        //System.out.println("###### beneficaryAccNo 1 :" + beneficaryAccNo);
                        continue;
                    }
                    if (i == 2) { //Amount
                        amount = data;
                        //System.out.println("###### amount 2 :" + amount);
                        continue;
                    }
                    if (i == 3) {  //Date
                        date = data;
                        //System.out.println("###### date 3 :" + date);
                        continue;
                    }
                    if (i == 4) {  //UTR No
                        inwardUTR = data;
                        //System.out.println("###### inwardUTR 4 :" + inwardUTR);
                        continue;
                    }
                    if (i == 5) {  //Remitter Account No
                        remitterAcNo = data;
                        //System.out.println("###### remitterAcNo 5 :" + remitterAcNo);
                        continue;
                    }
                    if (i == 6) {  //Sender Bank IFSC
                        senderIfscode = data;
                        //System.out.println("###### senderIfscode 6 :" + senderIfscode);
                        continue;
                    }
                    if (i == 7) {  //Credit Account No.
                        creditacNo = data;
                        //System.out.println("###### senderIfscode 7 :" + creditacNo);
                        continue;
                    }
                    if (i == 8) { //Remitter Name
                        if (data.length() >= 50) {
                            remitterName = data.substring(0, 50);
                        } else {
                            remitterName = data;
                        }
                        //System.out.println("###### remitterName 8 :" + remitterName);
                        continue;
                    }
                    if (i == 9) { //Mode
                        mode = data;
                        //System.out.println("###### mode 9 :" + mode);
                        if (parts.length > 10) {
                            continue;
                        }
                    }
                    if (i == 10) {  //Beneficiary Bank
                        beneficiaryBank = data;
                        //System.out.println("###### beneficiaryBank :" + beneficiaryBank);
                        if (parts.length > 11) {
                            continue;
                        }
                    }
                    if (i == 11) {  //Beneficiary Name
                        benficaryName = data;
                        //System.out.println("###### benficaryName 11 :" + benficaryName);
                    }
                    //System.out.println("####### beneficaryAccNo : " + beneficaryAccNo);
                    if (beneficaryAccNo.length() > 13) {
                        beneficaryAccNo = beneficaryAccNo.substring(beneficaryAccNo.length() - 13, beneficaryAccNo.length());
                        //System.out.println("####### NEW BeneficaryAccNo : " + beneficaryAccNo);
                    }
                    System.out.println("####### client_code : " + client_code + "\n" + " ####### beneficaryAccNo " + beneficaryAccNo
                            + "\n" + " ####### amount : " + amount + "\n" + " ####### inwardUTR : " + inwardUTR
                            + "\n" + " ####### remitterAcNo : " + remitterAcNo + "\n" + " ####### senderIfscode : " + senderIfscode
                            + "\n" + " ####### creditacNo#####" + creditacNo + "\n" + " ####### remitterName : " + remitterName
                            + "\n" + " ####### mode : " + mode + "\n" + " ####### beneficiaryBank : " + beneficiaryBank
                            + "\n" + " ####### benficaryName : " + benficaryName + "\n" + " ####### status : " + status);
                    acHeads.put("ACT_NUM", beneficaryAccNo);
                    acHeads.put("ACC_NUM", beneficaryAccNo);
                    List mapDataList = sqlMap.executeQueryForList("getActNumFromAllProductsADOA", acHeads);
                    if (mapDataList == null || mapDataList.isEmpty()) {
                        isTransAllowed = false;
                        status = "FAIL";
                        remarks = "ACCOUNT NOT EXIST";
                    } else if (mapDataList != null && mapDataList.size() > 0) {
                        HashMap map = (HashMap) mapDataList.get(0);
                        status = "PASS";
                        acHeads.put("CREDIT_PROD_TYPE", map.get("PROD_TYPE"));
                        acHeads.put("CREDIT_BRANCH_ID", map.get("BRANCH_ID"));
                        mapDataList = sqlMap.executeQueryForList("getAccountNumberNameOA", acHeads);
                        if (mapDataList != null && mapDataList.size() > 0) {
                            customerName = CommonUtil.convertObjToStr(((HashMap) mapDataList.get(0)).get(""));
                        }
                        mapDataList = sqlMap.executeQueryForList("getAcctHeadforAcct", map);
                        map = (HashMap) mapDataList.get(0);
                        creditAcctHeadId = CommonUtil.convertObjToStr(map.get("AC_HD_ID"));
                        acHeads.put("CREDIT_PROD_ID", map.get("PROD_ID"));
                        isTransAllowed = true;
                    }
                    neftTO.setAcctNum(beneficaryAccNo);
                    neftTO.setAckDate((Date) DateUtil.getDateMMDDYYYY(date));
                    neftTO.setAmount(new Double(amount));
                    neftTO.setHomeIfsCode("");
                    neftTO.setOtherBrankIfsCode(senderIfscode);
                    neftTO.setInitBranchId("0001");
                    neftTO.setAcctStatus(status);
                    neftTO.setStatus("OUTWARD");
                    neftTO.setInwardUTR(inwardUTR);
                    neftTO.setOtherBankAcctNum(remitterAcNo);
                    neftTO.setOtherBankCustName(remitterName);
                    neftTO.setBeneficiaryName(benficaryName);
                    acHeads.put("CREDIT_ACT_NUM", neftTO.getAcctNum());
                    acHeads.put("CREDIT_ACCT_HEAD_ID", creditAcctHeadId);
                    acHeads.put("CREDIT_AMT", neftTO.getAmount());
                    if (mode.equals("NEFT")) {
                        acHeads.put("NEFT", "NEFT");
                    } else if (mode.equals("RTGS")) {
                        acHeads.put("RTGS", "RTGS");
                    } else if (mode.equals("IMPS")) {
                        //acHeads.put("IMPS", "IMPS"); //Commented By Suresh On 3-Sep-2019  Ref By Jithesh.
                        acHeads.put("NEFT", "NEFT");
                        mode = "NEFT";
                    } else if (mode.equals("ICICI_FT")) {
                        acHeads.put("ICICI_FT", "ICICI_FT");
                    } else if (mode.equals("UPI")) { //Added by nithya on 20-09-2022 fro KD-3471                       
                        acHeads.put("NEFT", "NEFT");
                        mode = "NEFT";
                    } else if (mode.equals("FT")) {  //Added by nithya on 03-02-2025 for KD-3842                
                        acHeads.put("NEFT", "NEFT");
                        mode = "NEFT";
                    }
                    neftTO.setProdid(mode);
                    neftTO.setClient_Code(client_code);
                    neftTO.setCr_Acct_Num(creditacNo);
                    neftTO.setBeneficiary_Bank(beneficiaryBank);
                    UTRFlag = true;
                    if (inwardUTR.length() > 0) {
                        if (duplicateUTRMap.containsKey(inwardUTR)) {
                            UTRFlag = false;
                            System.out.println("############################### DUPLICATE UTR_NUMBER : " + inwardUTR);
                        }
                    }
                    sqlMap.startTransaction();
                    if (UTRFlag) {
                        
                        if(DateUtil.dateDiff(neftTO.getAckDate(), currDt)>4)
                        {
                            neftTO.setAcctStatus("FAIL");
                             remarks = "FUTURE DATE ENT";
                              neftTO.setRemarks(remarks);
                        }
                        else{
                        if (isTransAllowed) {
                            remarks = "ACCOUNT EXIST";
                            neftTO.setRemarks(remarks);
                            System.out.println("############################### readICICI_InputFile() : ");
                            String transId = CommonUtil.convertObjToStr(dotransaction(acHeads, neftTO).get("TRANS_ID"));        //DO TRANSACTION
                            neftTO.setBatch_id(transId);
                        }
                        }
                        neftTO.setStatus("OUTWARD");
                        neftTO.setFileName(file.getName());
                        neftTO.setRemarks(remarks);
                        sqlMap.executeUpdate("insertRtgsNeftAcknowledgementTO", neftTO);
                    }
                    sqlMap.commitTransaction();
                    i = parts.length;    //Added By Suresh R        Ref Mr Abi 16-Mar-2018
                }
            }
            fis.close();
            bis.close();
            dis.close();

            //INWARD ACKNOWLEDGEMENT FILE CREATION
            HashMap inputDataMap = new HashMap();
            HashMap fileDataMap = new HashMap();
            boolean fileCreation = false;
            boolean ackFileCreation = false;
            Calendar calender = Calendar.getInstance();
            DateFormat dtMiddleWithOutMin = new SimpleDateFormat("yyyyddMM");
            inputDataMap.put("FILE_NAME", file.getName());
            inputDataMap.put("CURR_DT", currDt.clone());
            List inwardList = sqlMap.executeQueryForList("getInwardFileData", inputDataMap);
            if (inwardList != null && inwardList.size() > 0) {
                for (int i = 0; i < inwardList.size(); i++) {
                    inputDataMap = (HashMap) inwardList.get(i);
                    fileDataMap.put(CommonUtil.convertObjToStr(inputDataMap.get("UTR_NUMBER")), CommonUtil.convertObjToStr(inputDataMap.get("ACCT_STATUS")));
                    if (CommonUtil.convertObjToStr(inputDataMap.get("ACCT_STATUS")).equals("FAIL")) {
                        ackFileCreation = true;
                    }
                }
                System.out.println("############ fileDataMap : " + fileDataMap + "   ########### ackFileCreation : " + ackFileCreation);
                if (ackFileCreation) {
                    String fileName = file.getName();
                    String file_Name = fileName.substring(0, fileName.lastIndexOf("."));
                    System.out.println("############ file_Name : " + file_Name);
                    String path = CommonConstants.RTGS_NEFT_OUTWARD_PATH + CommonConstants.RTGS_NEFT_ECOLL_CODE + "_"
                            + CommonConstants.RTGS_NEFT_FORMAT_CODE + "_" + generateRandomNumbers() + "_" + file_Name + ".TXT";
                    System.out.println("############## OUTWARD FILE NAME PATH : " + path);
                    java.io.FileWriter write = new java.io.FileWriter(path, false);
                    java.io.PrintWriter print_line = new java.io.PrintWriter(write);
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    dis = new DataInputStream(bis);
                    String transStatus = "";
                    data = "";
                    while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                        neftTO = new NeftRtgsAcknowledgementTO();
                        String[] parts = line.split("\\|");
                        for (int i = 0; i < parts.length; i++) {
                            data = CommonUtil.convertObjToStr(parts[i]);
                            if (i == 0) { //Client Code
                                client_code = data;
                                continue;
                            }
                            if (i == 1) { //Bene Account Number
                                beneficaryAccNo = data;
                                continue;
                            }
                            if (i == 2) { //Amount
                                amount = data;
                                continue;
                            }
                            if (i == 3) {  //Date
                                date = data;
                                continue;
                            }
                            if (i == 4) {  //UTR No
                                inwardUTR = data;
                                continue;
                            }
                            if (i == 5) {  //Remitter Account No
                                remitterAcNo = data;
                                continue;
                            }
                            if (i == 6) {  //Sender Bank IFSC
                                senderIfscode = data;
                                continue;
                            }
                            if (i == 7) {  //Credit Account No.
                                creditacNo = data;
                                continue;
                            }
                            if (i == 8) { //Remitter Name				
                                remitterName = data;
                                continue;
                            }
                            if (i == 9) { //Mode
                                mode = data;
                                if (parts.length > 10) {
                                    continue;
                                }
                            }
                            if (i == 10) {  //Beneficiary Bank
                                beneficiaryBank = data;
                                if (parts.length > 11) {
                                    continue;
                                }
                            }
                            if (i == 11) {  //Beneficiary Name
                                benficaryName = data;
                            }
                            transStatus = "";
                            fileCreation = false;
                            if (inwardUTR.length() > 0) {
                                if (fileDataMap.containsKey(inwardUTR)) {
                                    if (CommonUtil.convertObjToStr(fileDataMap.get(inwardUTR)).equals("PASS")) {
                                        System.out.println("############## Status - PASS");
                                        transStatus = "P" + "|";
                                    } else if (CommonUtil.convertObjToStr(fileDataMap.get(inwardUTR)).equals("FAIL")) {
                                        System.out.println("############## Status - FAIL");
                                        transStatus = "C" + "|";
                                        fileCreation = true;
                                    }
                                } else {
                                    System.out.println("############## Status - EMPTY");
                                    transStatus = " " + "|";
                                }
                            }
                            if (fileCreation) {
                                print_line.print("I");                                  //1 Record_Identifier 	Always "i"
                                print_line.print(pipeDelimiter);
                                if (CommonUtil.convertObjToStr(mode).equals("NEFT")) {  //2 Payment Indicator
                                    print_line.print("N");
                                } else if (CommonUtil.convertObjToStr(mode).equals("RTGS")) {
                                    print_line.print("R");
                                } else if (CommonUtil.convertObjToStr(mode).equals("IMPS")) {
                                    //print_line.print("M"); 
                                    print_line.print("N"); //Added By Suresh On 3-Sep-2019  Ref By Jithesh.
                                } else if (CommonUtil.convertObjToStr(mode).equals("ICICI_FT")) {
                                    print_line.print("I");
                                } else if (CommonUtil.convertObjToStr(mode).equals("UPI")) {
                                    print_line.print("N"); //Added by nithya on 20-09-2022 fro KD-3471 
                                } else if (CommonUtil.convertObjToStr(mode).equals("FT")) {
                                    print_line.print("N"); //Added by nithya on 03-02-2025 for KD-3842
                                }
                                print_line.print(pipeDelimiter);
                                print_line.print(inwardUTR);                    //3 UNIQUE ID   -M
                                print_line.print(pipeDelimiter);
                                //BENEFICARY CODE OPTIONAL                      //4 BENEFICARY CODE   O
                                print_line.print(pipeDelimiter);
                                benficaryName = benficaryName.replaceAll("[\\.\\/\\[\\{\\}\\;\\:\\`!@#$%^&*()_<>?\\,\\+\\-\\=\\|]\\~\\'", "");
                                print_line.print(benficaryName);                //5 BENEFICARY NAME -M
                                print_line.print(pipeDelimiter);
                                //BENEFICARY CODE OPTIONAL
                                print_line.print(pipeDelimiter);
                                print_line.print(amount);                       //6 TRANSACTION AMOUNT  M=MANDATORY
                                print_line.print(pipeDelimiter);
                                print_line.print(DateUtil.getStringDate((Date) currDt.clone())); //7 DATE OF PAYMENT
                                print_line.print(pipeDelimiter);
                                print_line.print(CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_DR_ACT_NUM)); //8 DEBIT ACT NUMBER
                                print_line.print(pipeDelimiter);
                                print_line.print(beneficaryAccNo);              //9 BENEFICARY ACT NUMBER
                                print_line.print(pipeDelimiter);
                                if (!CommonUtil.convertObjToStr(mode).equals("ICICI_FT")) {
                                    print_line.print(senderIfscode);            //10 BENEFICARY IFSCODE
                                }
                                print_line.print(pipeDelimiter);
                                //BENEFICARY BANK NAME                          //11 BENEFICARY BANK NAME
                                print_line.print(pipeDelimiter);
                                //BENEFICARY MAIL ID                            //12 BENEFICARY MAIL ID
                                print_line.print(pipeDelimiter);
                                //BENEFICARY MOBILE NO                          //13 BENEFICARY MOBILE NO
                                print_line.print(pipeDelimiter);
                                print_line.print(remitterAcNo);                 //14 REMITTER ACCOUNT NUMBER
                                print_line.print(pipeDelimiter);
                                print_line.print(inwardUTR + "-" + "ACCOUNT NOT EXIST");//15 REMARKS
                                print_line.println(pipeDelimiter);
                            }
                        }
                    }
                    fis.close();
                    bis.close();
                    dis.close();
                    if (print_line != null) {
                        print_line.close();
                    }
                    if (write != null) {
                        write.close();
                    }
                }
            }
            System.out.println("############# INWARD ACKNOWLEDGEMENT FILE CREATED ");
            //FILE MOVED TO BACKUP FOLDER
            String sourceFilePath = CommonConstants.RTGS_NEFT_INWARD_PATH + file.getName();
            String destinationFilePath = CommonConstants.RTGS_NEFT_INWARD_BACKUP_PATH + "INWARD_" + generateRandomNumbers() + file.getName() + ".Done";
            File file1 = new File(sourceFilePath);
            file1.renameTo(new File(destinationFilePath));
            System.out.println("############# FILE MOVED " + destinationFilePath + "  ##### file.getName() : " + file.getName());
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    private void readData_From_Outward_ReturnFile(File file) throws Exception {//Outward Acknowledgement
        System.out.println("########## 3 readData_From_Outward_ReturnFile() PROCESS STARTED : "+file.getName());
        String line = "", inwardUTR = "", status = "";
        HashMap dataMap = new HashMap();
        HashMap acHeads = new HashMap();
        String pipeDelimiter = "|";
        String creditAcctHeadId = "", customerName = "";
        boolean UTRFlag = false, isTransAllowed = false;
        NeftRtgsAcknowledgementTO neftTO = new NeftRtgsAcknowledgementTO();
        HashMap detailMap = new HashMap();
        HashMap creditAcctMap = new HashMap();
        RtgsRemittanceTO remitTo = null;
        boolean fileNameInsert = true;
        HashMap fileMap = new HashMap();
        try {
            fileMap = new HashMap();
            fileMap.put("FILE_NAME", file.getName());
            List fileList = sqlMap.executeQueryForList("getCheckFileExistOutwardAck", fileMap);
            if (fileList != null && fileList.size() > 0) {
                System.out.println("########## FILE ALREADY PROCESSED");
                sqlMap.executeUpdate("updateRTGSAckFileReceivedCount", fileMap);
            } else {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                    neftTO = new NeftRtgsAcknowledgementTO();
                    detailMap = new HashMap();
                    String[] parts = line.split("\\|");
                    for (int i = 0; i < parts.length; i++) {
                        String data = CommonUtil.convertObjToStr(parts[i]);
                        if (i == 0) {//RECORD_IDENTIFIER 
                            continue;
                        }
                        if (i == 1) {//PAYMENT_INDICATOR       
                            continue;
                        }
                        if (i == 2) {//UTR_NUMBER(UNIQUR REF NO) 
                            inwardUTR = data;
                            continue;
                        }
                        if (i == 3) {//BENE_CODE
                            continue;
                        }
                        if (i == 4) {//BENE_NAME
                            continue;
                        }
                        if (i == 5) {//AMOUNT
                            continue;
                        }
                        if (i == 6) {//DATE
                            continue;
                        }
                        if (i == 7) {//DEBIT A/C NO
                            continue;
                        }
                        if (i == 8) {//BENE_ACCOUNT_NO
                            continue;
                        }
                        if (i == 9) {//IFSC_CODE
                            continue;
                        }
                        if (i == 10) {//BENE_BANK_NAME
                            continue;
                        }
                        if (i == 11) {//BENE_EMAIL_ID
                            continue;
                        }
                        if (i == 12) {//BENE_MOBILE
                            continue;
                        }
                        if (i == 13) {//REMITTER_ACCOUNT_NO
                            continue;
                        }
                        if (i == 14) {//REMARKS
                            continue;
                        }
                        if (i == 15) {//PAYMENT_REF_NO
                            continue;
                        }
                        if (i == 16) {//STATUS
                            status = data;
                            continue;
                        }
                        if (i == 17) {//LIQUIDATION_DATE
                            continue;
                        }
                        if (i == 18) {//CUSTOMER_REF_NO
                            continue;
                        }
                        if (i == 19) {//UTR_NO
                        }
                        //If Condition added Based on Mail from Jithesh (16-Apr-2018)
                        if (CommonUtil.convertObjToStr(status).equals("Paid")||CommonUtil.convertObjToStr(status).equals("Cancelled")) {
                            sqlMap.startTransaction();
                            System.out.println("############ inwardUTR  : " + inwardUTR + "   ############ status     : " + status);
                            if (CommonUtil.convertObjToStr(status).equals("Paid")) {//PASS - NO TRANSACTION, ONLY STATUS UPDATE AS PASS
                                detailMap.put("UTR_NUMBER", inwardUTR);
                                detailMap.put("FILE_STATUS", "PASS");
                            } else {//FAIL - DO TRANSACTION AND STATUS UPDATE AS FAIL
                                detailMap.put("UTR_NUMBER", inwardUTR);
                                detailMap.put("FILE_STATUS", "FAIL");
                                List lst = sqlMap.executeQueryForList("getRTGSRemittransDetailsTO", detailMap);
                                if (lst != null && lst.size() > 0) {
                                    remitTo = (RtgsRemittanceTO) lst.get(0);
                                    detailMap.put("BATCH_DT", remitTo.getBatchDt());
                                    detailMap.put("RTGS_ID", remitTo.getRtgs_ID());
                                    lst = sqlMap.executeQueryForList("getCreditAcctDetails", detailMap);
                                    if (lst != null && lst.size() > 0) {
                                        creditAcctMap = (HashMap) lst.get(0);
                                        neftTO.setAcctNum(CommonUtil.convertObjToStr(creditAcctMap.get("DEBIT_ACCT_NO")));
                                        neftTO.setAckDate((Date) currDt.clone());
                                        neftTO.setAmount(remitTo.getAmount());
                                        neftTO.setHomeIfsCode(remitTo.getIfsc_Code());
                                        neftTO.setOtherBrankIfsCode(remitTo.getBeneficiary_IFSC_Code());
                                        neftTO.setOtherBankAcctNum(remitTo.getAccount_No());
                                        neftTO.setOtherBankCustName(remitTo.getBeneficiary_Name());
                                        neftTO.setInitBranchId(remitTo.getInitiatedBranch());
                                        neftTO.setAcctStatus(status);
                                        neftTO.setInwardUTR(remitTo.getUtrNumber());
                                        acHeads.put("CREDIT_ACT_NUM", neftTO.getAcctNum());
                                        acHeads.put("CREDIT_PROD_ID", CommonUtil.convertObjToStr(creditAcctMap.get("PROD_ID")));
                                        acHeads.put("CREDIT_PROD_TYPE", CommonUtil.convertObjToStr(creditAcctMap.get("PRODUCT_TYPE")));
                                        acHeads.put("CREDIT_BRANCH_ID", CommonUtil.convertObjToStr(creditAcctMap.get("BRANCH_CODE")));
                                        detailMap.put("PROD_ID", remitTo.getProdId());
                                        lst = sqlMap.executeQueryForList("getAcctHeadforAcct", detailMap);
                                        if (lst != null && lst.size() > 0) {
                                            creditAcctMap = (HashMap) lst.get(0);
                                            acHeads.put("CREDIT_ACCT_HEAD_ID", CommonUtil.convertObjToStr(creditAcctMap.get("ISSUE_HD")));
                                        }
                                        acHeads.put("CREDIT_AMT", neftTO.getAmount());
                                        if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("NEFT")) {
                                            acHeads.put("NEFT", "NEFT");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("RTGS")) {
                                            acHeads.put("RTGS", "RTGS");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("IMPS")) {
                                            acHeads.put("IMPS", "IMPS");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("ICICI_FT")) {
                                            acHeads.put("ICICI_FT", "ICICI_FT");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("UPI")) { //Added by nithya on 20-09-2022 fro KD-3471   
                                            acHeads.put("NEFT", "NEFT");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("FT")) {    //Added by nithya on 03-02-2025 for KD-3842
                                            acHeads.put("NEFT", "NEFT");
                                        }
                                        System.out.println("############################### read_DataFromOutwardReturnFile() : ");
                                        dotransaction(acHeads, neftTO);
                                    }
                                }
                            }
                            if (detailMap != null && detailMap.size() > 0) {
                                sqlMap.executeUpdate("updateOutwardResponseFileStatus", detailMap);
                            }
                            sqlMap.commitTransaction();
                        }
                    }
                    if (fileNameInsert) {
                        sqlMap.startTransaction();
                        fileMap = new HashMap();
                        fileMap.put("FILE_NAME", file.getName());
                        fileMap.put("APPL_DT", currDt.clone());
                        fileMap.put("COUNT", CommonUtil.convertObjToInt(1));
                        sqlMap.executeUpdate("insertRTGSAckFileName", fileMap);
                        sqlMap.commitTransaction();
                        fileNameInsert = false;
                    }
                }
                fis.close();
                bis.close();
                dis.close();
            }
            //FILE MOVED TO BACKUP FOLDER
            String sourceFilePath = CommonConstants.RTGS_NEFT_INWARD_PATH + file.getName();
            String destinationFilePath = CommonConstants.RTGS_NEFT_INWARD_BACKUP_PATH + "OUTWARD_RES_" + generateRandomNumbers()+"_"+ file.getName() + ".Done";
            File file1 = new File(sourceFilePath);
            file1.renameTo(new File(destinationFilePath));
            System.out.println("############# FILE MOVED COMPLETED : " + destinationFilePath + "  ##### file.getName() : " + file.getName());
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            System.out.println("############ OUTWARD_ACK ERROR : ");
            throw e;
        }
    }

    private String generateRandomNumbers() {
        String str = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
        Calendar calender = Calendar.getInstance();
        dateFormat.format(calender.getTime());
        long randomNumber = (long) Math.random() * calender.getTimeInMillis();
        str = dateFormat.format(calender.getTime()) + randomNumber + "_";
        //System.out.println("######## generateRandomNumbers : " + str);
        return str;
    }

    private String getCEDGEuNIQUE_NEFT_ID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "CEDGE_ID");
        String rtgs_ID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return rtgs_ID;
    }

    // do the actual transaction and credit into our customer account 
    private HashMap dotransaction(HashMap acHeads, NeftRtgsAcknowledgementTO neftTO) throws Exception {
        System.out.println("########### TRANSACTION_START() - ACCOUNT_HEADS : " + acHeads);
        TxTransferTO objTxTransferTO = new TxTransferTO();
        ReconciliationTO reconciliationTO = new ReconciliationTO();
        TransferTrans trans = new TransferTrans();
        String userid = "", useridAuth = "";
        trans.setInitiatedBranch("0001");  // hard coded
        String actNum = CommonUtil.convertObjToStr(acHeads.get("CREDIT_ACT_NUM"));
        String creditBranch = CommonUtil.convertObjToStr(acHeads.get("CREDIT_BRANCH_ID"));
        Date initiatedBranchAppDt = ServerUtil.getCurrentDate("0001");
        System.out.println("############## creditBranch : " + creditBranch + "neftTO$$$$$$$" + neftTO);
        Date creditBranchAppDt = ServerUtil.getCurrentDate(creditBranch);
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        HashMap txMap = new HashMap();
        double transAmt = CommonUtil.convertObjToDouble(acHeads.get("CREDIT_AMT")).doubleValue();
        System.out.println("######### TRANSACTION_AMOUNT : " + transAmt + " \n ############ actNum : " + actNum + " \n ############ acHeads : " + acHeads
                + " \n ############ initiatedBranchAppDt : " + initiatedBranchAppDt + " \n ############ creditBranchAppDt : " + creditBranchAppDt);
        HashMap creditMap = new HashMap();
        if (CommonUtil.convertObjToStr(acHeads.get("NEFT")).length() > 0) {
            creditMap.put("PROD_ID", CommonUtil.convertObjToStr(acHeads.get("NEFT")));
            userid = "NEFT";
            useridAuth = "NEFT_USER";
        } else if (CommonUtil.convertObjToStr(acHeads.get("RTGS")).length() > 0) {
            creditMap.put("PROD_ID", CommonUtil.convertObjToStr(acHeads.get("RTGS")));
            userid = "RTGS";
            useridAuth = "RTGS_USER";
        } else if (CommonUtil.convertObjToStr(acHeads.get("IMPS")).length() > 0) {
            creditMap.put("PROD_ID", CommonUtil.convertObjToStr(acHeads.get("IMPS")));
            userid = "IMPS";
            useridAuth = "IMPS_USER";
        } else if (CommonUtil.convertObjToStr(acHeads.get("ICICI_FT")).length() > 0) {
            creditMap.put("PROD_ID", CommonUtil.convertObjToStr(acHeads.get("ICICI_FT")));
            userid = "ICICI";
            useridAuth = "ICICI_USER";
        }
        TxTransferTO transferTo = new TxTransferTO();
        List creditList = (List) sqlMap.executeQueryForList("getRTGSAccountHeads", creditMap);
        if (creditList != null && creditList.size() > 0) {
            creditMap = (HashMap) creditList.get(0);
        }
        ArrayList transferList = new ArrayList(); // for local transfer
        if(CommonUtil.convertObjToStr(creditMap.get("RTGS_NEFT_GL_TYPE")).equals("Y")){
            txMap.put(TransferTrans.DR_AC_HD, creditMap.get("ISSUE_HD"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put("TRANS_MOD_TYPE", "GL");
            txMap.put(TransferTrans.DR_BRANCH, branchID);
        }else if(CommonUtil.convertObjToStr(creditMap.get("RTGS_NEFT_GL_TYPE")).equals("N")){
            txMap.put(TransferTrans.DR_ACT_NUM, creditMap.get("RTGS_NEFT_ACT_NUM"));
            txMap.put(TransferTrans.DR_PROD_ID, creditMap.get("RTGS_NEFT_PROD_ID"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.OTHERBANKACTS);
            HashMap headMap =  new HashMap();
            headMap.put("INVESTMENT_ACC_NO", creditMap.get("RTGS_NEFT_ACT_NUM"));
            List achdLst = ServerUtil.executeQuery("getSelectOthrBankAcHd", headMap);
            if(achdLst != null && achdLst.size() > 0){
                headMap = (HashMap)achdLst.get(0);
                if(headMap.containsKey("BRANCH_ID") && headMap.get("BRANCH_ID")!= null){
                    txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(headMap.get("BRANCH_ID")));
                }else{
                   txMap.put(TransferTrans.DR_BRANCH, branchID); 
                }
            }
        }
//        txMap.put(TransferTrans.DR_AC_HD, (String) creditMap.get("ISSUE_HD"));
        //txMap.put(TransferTrans.DR_BRANCH, branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
//        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(CommonConstants.USER_ID, userid);
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put("PARTICULARS", neftTO.getInwardUTR());
        txMap.put("SCREEN_NAME", "RTGS/NEFT Schedule");
        objTxTransferTO = trans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(acHeads.get("CREDIT_AMT")).doubleValue());
        transferList.add(objTxTransferTO);
        txMap = new HashMap();
        if (DateUtil.dateDiff(initiatedBranchAppDt, creditBranchAppDt) == 0) {
            if (CommonUtil.convertObjToStr(acHeads.get("CREDIT_PROD_TYPE")).equals("GL")) { //GL Set code Added By Suresh Ref By Abi 22-Dec-2016
                txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(acHeads.get("CREDIT_PROD_TYPE")));
                txMap.put(TransferTrans.CR_AC_HD, actNum);
                txMap.put(TransferTrans.CR_BRANCH, creditBranch);
                txMap.put("TRANS_MOD_TYPE", "GL");
            } else {
                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(acHeads.get("CREDIT_PROD_ID")));
                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeads.get("CREDIT_ACCT_HEAD_ID")));
                txMap.put(TransferTrans.CR_ACT_NUM, actNum);
                txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(acHeads.get("CREDIT_PROD_TYPE"))); //ransactionFactory.OPERATIVE);
                txMap.put(TransferTrans.CR_BRANCH, actNum.substring(0, 4));
                txMap.put("TRANS_MOD_TYPE", "OA");
            }
            txMap.put(CommonConstants.USER_ID, userid);
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put("PARTICULARS", neftTO.getProdid() + neftTO.getInwardUTR() + "From" + neftTO.getOtherBankCustName() + " " + neftTO.getOtherBankAcctNum());
        } else {
            txMap.put(TransferTrans.CR_AC_HD, (String) creditMap.get("NEFT_RTGS_SUSPENCE_HD"));
            txMap.put(TransferTrans.CR_BRANCH, branchID);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(CommonConstants.USER_ID, userid);
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put("TRANS_MOD_TYPE", "GL");
            txMap.put("PARTICULARS", neftTO.getProdid() + neftTO.getInwardUTR() + "From" + neftTO.getOtherBankCustName() + " " + neftTO.getOtherBankAcctNum());
        }
        txMap.put("SCREEN_NAME", "RTGS/NEFT Schedule");
        objTxTransferTO = trans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(acHeads.get("CREDIT_AMT")).doubleValue());
        transferList.add(objTxTransferTO);
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", transferList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("LINK_BATCH_ID", actNum);
        data.put("INITIATED_BRANCH", "0001");
        data.put(CommonConstants.BRANCH_ID, "0001");
        data.put(CommonConstants.USER_ID, useridAuth);
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "RTGS/NEFT Schedule");
        data.put("MODE", "INSERT");
        if (trans.getAllAmountMap() != null && trans.getAllAmountMap().size() > 0) {
            data.put("ALL_AMOUNT", trans.getAllAmountMap());
            if (trans.getAllAmountMap().containsKey("LOAN_FROM_CHARGESUI")) {
                data.put("LOAN_FROM_CHARGESUI", "");
            }
            if (trans.getAllAmountMap().containsKey("CORP_LOAN_MAP")) {
                data.put("CORP_LOAN_MAP", trans.getAllAmountMap().get("CORP_LOAN_MAP"));
            }
        }
        transferDAO.setForLoanDebitInt(false);
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        HashMap transDetMap = transferDAO.execute(data, false);
        System.out.println("########## transDetMap : " + transDetMap);

        /*if (DateUtil.dateDiff(initiatedBranchAppDt, creditBranchAppDt) != 0) {
         reconciliationTO.setAcHdId(CommonUtil.convertObjToStr(creditMap.get("NEFT_RTGS_SUSPENCE_HD")));
         reconciliationTO.setTransAmount(CommonUtil.convertObjToDouble(acHeads.get("CREDIT_AMT")));
         reconciliationTO.setBalanceAmount(CommonUtil.convertObjToDouble(acHeads.get("CREDIT_AMT")));
         reconciliationTO.setStatus(CommonConstants.STATUS_CREATED);
         reconciliationTO.setStatusBy(userid);
         reconciliationTO.setStatusDt(currDt.clone());
         reconciliationTO.setTransId(CommonUtil.convertObjToStr(transDetMap.get("TRANS_ID")));
         reconciliationTO.setBatchId(CommonUtil.convertObjToStr(transDetMap.get("TRANS_ID")));
         reconciliationTO.setTransMode("TRANSFER");
         reconciliationTO.setInitiatedBranch(CommonUtil.convertObjToStr("0001"));
         reconciliationTO.setBranchId(CommonUtil.convertObjToStr("0001"));
         reconciliationTO.setTransType(CommonUtil.convertObjToStr("CREDIT"));
         reconciliationTO.setTransDt(initiatedBranchAppDt);
         reconciliationTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
         reconciliationTO.setAuthorizeDt(initiatedBranchAppDt);
         reconciliationTO.setAuthorizeBy(useridAuth);
         reconciliationTO.setRecTranDt(initiatedBranchAppDt);
         reconciliationTO.setParticulars(actNum + " by " + neftTO.getInwardUTR());
         sqlMap.executeUpdate("insertReconciliationTO", reconciliationTO);
         }*/
        return transDetMap;
    }
    //N10 file for NEFT

    private String generateLinkID(String act_num) throws Exception {//method added by abi
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, act_num.substring(1, 4));
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    public boolean isIsInwardExcepRR42() {
        return isInwardExcepRR42;
    }

    public void setIsInwardExcepRR42(boolean isInwardExcepRR42) {
        this.isInwardExcepRR42 = isInwardExcepRR42;
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public static void main(String[] args) throws Exception {
    }

    public void outward_RTGSNEFT_AXIS(String branch_id) throws Exception {     //RTGS/NEFT MANUAL SCREEN TRANSACTIONS
        System.out.println("######## outward_RTGSNEFT_AXIS() PROCESS STARTED : ");
        HashMap dataMap = new HashMap();
        HashMap authMap = new HashMap();
        currDt = ServerUtil.getCurrentDate(branch_id);
        DateFormat years2digit = new SimpleDateFormat("yy");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dtFormtWithOutMin = new SimpleDateFormat("yyyyMMdd");
        DateFormat dtMiddleWithOutMin = new SimpleDateFormat("yyyyddMM");
        DateFormat minOnly = new SimpleDateFormat("hhmm");
        Calendar calender = Calendar.getInstance();
        HashMap singleUTRMap = new HashMap();
        java.io.FileWriter write = null;
        java.io.PrintWriter printLine = null;
        Date dt = new Date();
        String path = "";
        String fileName = "";
        String transAmt = "";
        String pipeDelimiter = "|";
        int daysInYear = 0;
        try {
            List RTGSlist = (List) sqlMap.executeQueryForList("getRTGSRemittanceTOForFileCreation", singleUTRMap);
            if (RTGSlist != null && RTGSlist.size() > 0) {
                String neftUniqueId = "", sequnceNum = "", dayinyearStr = "";
                StringBuffer dayInYearbuffer = new StringBuffer();
                StringBuffer utrBuffer = new StringBuffer();
                RtgsRemittanceTO resultTo = null;
                for (int i = 0; i < RTGSlist.size(); i++) {
                    resultTo = (RtgsRemittanceTO) RTGSlist.get(i);
                    if (resultTo != null) {
                        String behavior = "";
                        dateFormat.format(resultTo.getBatchDt());
                        neftUniqueId = CommonUtil.convertObjToStr(resultTo.getUtrNumber());
                        //System.out.println("#### resultTo.getUtrNumber() : " + resultTo.getUtrNumber() + " ###dateFormat : "
                        //+ dateFormat.format(calender.getTime()) + "check" + DateUtil.getStringDate(resultTo.getBatchDt()));
                        if (i == 0) {
                            fileName = CommonConstants.AXIS_CORPORATE_IDENTIFIER + "_" + CommonConstants.AXIS_CORPORATE_IDENTIFIER
                                    + "_" + CommonUtil.convertObjToStr(resultTo.getSequnceNo()) + "_" + generateRandomNumbers()
                                    + CommonConstants.RTGS_BANK_NAME + ".txt";
                            path = CommonConstants.AXIS_RTGS_NEFT_OUTWARD_PATH + fileName;
                            write = new java.io.FileWriter(path, false);
                            printLine = new java.io.PrintWriter(write);
                            System.out.println("###### Outward File Name Path : " + path);
                        }
                        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                        daysInYear = localCalendar.get(Calendar.DAY_OF_YEAR);
                        dayinyearStr = String.valueOf(daysInYear);
                        if (dayinyearStr.length() == 2) {
                            dayInYearbuffer.append("0" + dayinyearStr);
                        } else if (dayinyearStr.length() == 1) {
                            dayInYearbuffer.append("00" + dayinyearStr);
                        } else {
                            dayInYearbuffer.append(dayinyearStr);
                        }
                        if (neftUniqueId != null && neftUniqueId.length() > 0) {      //TOTAL COLUMNS 40 AS PER EXCEL Sheet
                            printLine.print(CommonConstants.AXIS_CHANNEL_IDENTIFIER); //1 M channel identifier from ttproperties file
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonConstants.AXIS_CORPORATE_IDENTIFIER); //2 M Corporate identifier
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonConstants.AXIS_USER_ID_UPLOADER); //3 M UserId uploader
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonConstants.AXIS_USER_ID_AUTHORIZER_1); //4 M UserId Authorizer1
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonConstants.AXIS_USER_ID_AUTHORIZER_2); //5 M UserId Authorizer2
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonConstants.AXIS_USER_ID_AUTHORIZER_3); //6 M UserId Authorizer3
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonConstants.AXIS_USER_ID_AUTHORIZER_4); //7 M UserId Authorizer4
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonConstants.AXIS_USER_ID_AUTHORIZER_5); //8 M UserId Authorizer5
                            printLine.print(pipeDelimiter);
                            if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("NEFT")) {//9 M Payment Method Name
                                printLine.print("N");
                            } else if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("RTGS")) {
                                printLine.print("R");
                            } else if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("IMPS")) {
                                printLine.print("M");
                            } else {
                                printLine.print("I");
                            }
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonConstants.AXIS_CORPORATE_PRODUCT_CODE); //10 M Corporate product code
                            printLine.print(pipeDelimiter);
                            printLine.print(resultTo.getSequnceNo()); //11 M Serial No
                            printLine.print(pipeDelimiter);
                            String beneficiaryName = "";
                            beneficiaryName = resultTo.getBeneficiary_Name().replaceAll("[\\.\\/\\[\\{\\}\\;\\:\\`!@#$%^&*()_<>?\\,\\+\\-\\=\\|]\\~\\'", "");
                            printLine.print(beneficiaryName);                  //12 M Beneficary Name
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonUtil.convertObjToStr(resultTo.getAccount_No())); //13 M Beneficiary Act Num
                            printLine.print(pipeDelimiter);
                            HashMap prodMap = new HashMap();//as discussed with Jose. He confirmed SB - 10 CA - 11 on 11may2018
                            prodMap.put("Prod_ID",resultTo.getDebitProdId());
                            List mapDataList = sqlMap.executeQueryForList("getSelectOperativeAcctProductTOList", prodMap);
                            if(mapDataList != null && mapDataList.size()>0){
                                prodMap = (HashMap) mapDataList.get(0);
                                behavior = CommonUtil.convertObjToStr(prodMap.get("BEHAVIOR"));
                                printLine.print(CommonUtil.convertObjToStr("10")); //14 M Account Type code
                                if(behavior != null && behavior.length()>0 && behavior.equals("CA")){
                                    printLine.print(CommonUtil.convertObjToStr("11")); //14 M Account Type code
                                }                                
                            }
                            printLine.print(pipeDelimiter);
                            transAmt = CommonUtil.convertObjToStr(resultTo.getAmount());
                            if (transAmt.contains(".")) {
                                String number = transAmt.substring(transAmt.indexOf(".")).substring(1);
                                if (number.length() == 1) {
                                    transAmt = transAmt + "0";
                                }
                            } else {
                                transAmt = transAmt + "00";
                            }
                            printLine.print(transAmt);                         //15 M Transaction Amount
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonUtil.convertObjToStr(resultTo.getBeneficiary_IFSC_Code()));//16 M Beneficary IFSC Code
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonUtil.convertObjToStr("")); //17 O AdditionalInfo as i discussed with Jose. He confirmed make it blank always on 11may2018
                            printLine.print(pipeDelimiter);
                            String remitterName = "";
                            remitterName = resultTo.getBeneficiary_Name().replaceAll("[\\.\\/\\[\\{\\}\\;\\:\\`!@#$%^&*()_<>?\\,\\+\\-\\=\\|]\\~\\'", "");
                            printLine.print(remitterName); //18 M Actual Remitter Name
                            printLine.print(pipeDelimiter);
                            printLine.print(resultTo.getDebitAcNum()); //19 M Actual Remitter Account
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonUtil.convertObjToStr("10")); //20 M Actual Remitter Account Type
                            if(behavior != null && behavior.length()>0 && behavior.equals("CA")){
                                printLine.print(CommonUtil.convertObjToStr("11")); //20 M Actual Remitter Account Type
                            }
//                            printLine.print(resultTo.getDebitProdType()); //20 M Actual Remitter Account Type
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //21 O Email
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //22 O Email Body
                            printLine.print(pipeDelimiter);
                            printLine.print(beneficiaryName); //23 R Payee Name (Request)
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //24 R Print Branch
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //25 O Bene Address 1
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //26 O Bene Address 2
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //27 O Bene Address 3
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //28 O Bene City
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //29 O Bene State
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //30 O Postal Code
                            printLine.print(pipeDelimiter);
                            printLine.print(CommonConstants.AXIS_RTGS_NEFT_DR_ACT_NUM); //31 M Debit Account No
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //32 O Bene Bank Address 1
                            printLine.print(pipeDelimiter);
                            printLine.print(""); //33 R Payable Location
                            printLine.print(pipeDelimiter);
                            String dts = DateUtil.getStringDate(resultTo.getBatchDt());
                            printLine.print(DateUtil.getStringDate(resultTo.getBatchDt())); //34 O Activation Date
                            printLine.print(pipeDelimiter);
                            printLine.print(neftUniqueId);                     //35 M CRN No
                            printLine.print(pipeDelimiter);
                            printLine.print("");                     //36 R Short Account No
                            printLine.print(pipeDelimiter);
                            printLine.print("");                     //37 R Instrument No
                            printLine.print(pipeDelimiter);
                            printLine.print("");                     //38 R Instrument Date
                            printLine.print(pipeDelimiter);
                            printLine.print("");                     //39 O PaymentDetail1
                            printLine.print(pipeDelimiter);
                            printLine.print("" + "\n");                     //40 O PaymentDetail2
//                            printLine.print(pipeDelimiter);
                            if (RTGSlist.size() - 1 == i) {
                                utrBuffer.append("'" + neftUniqueId + "'");
                            } else {
                                utrBuffer.append("'" + neftUniqueId + "'" + ",");
                            }
                        }
                    }
                }
                authMap.put("UTR_NUMBER", utrBuffer);
                authMap.put("PROCESS_DT", currDt.clone());
                authMap.put("FILE_STATUS", "DELIVERED");
//                System.out.println("########## authMap : " + authMap);
                sqlMap.executeUpdate("updateOutwardRTGSNEFTStatus", authMap);
                if (printLine != null) {
                    printLine.close();
                }
                if (write != null) {
                    write.close();
                }
            }
//            System.out.println("######## outward_RTGSNEFT_AXIS() PROCESS COMPLETED : ");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TTException(e);
        }
    }

    private void readAXISInwardInputFile(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        HashMap duplicateUTRMap = new HashMap();
        String line = "", inwardUTR = "", remarks = "";
        HashMap dataMap = new HashMap();
        HashMap acHeads = new HashMap();
        String pipeDelimiter = "|";
        String creditAcctHeadId = "", customerName = "";
        StringBuffer UTRNumberBuffer = new StringBuffer();
        boolean UTRFlag = false, isTransAllowed = false;
        NeftRtgsAcknowledgementTO neftTO = new NeftRtgsAcknowledgementTO();
        String mode = "", UTRNumber = "", senderIFSC = "", senderActType = "", senderActNumber = "", senderName = "", senderAddress = "",
                benefciaryIFSCCode = "", beneficaryAccNo = "", senderInfo = "", valueDate = "", transactionDate = "", beneficiaryActType = "",
                reference = "", beneficiaryAddress = "", corporateCode = "", clientCode = "", creditActNumber = "", setteledTransTime = "",
                masterName = "", client_code = "", benficaryName = "", amount = "", date = "",
                remitterAcNo = "", senderIfscode = "", creditacNo = "", remitterName = "", beneficiaryBank = "", status = "";
        try {
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                String[] parts = line.split("\\|");
                for (int i = 0; i < parts.length; i++) {
                    //System.out.println(parts[i]);
                    String data = CommonUtil.convertObjToStr(parts[i]);
                    if (i == 1) {  //UTR No
                        inwardUTR = data;
                    }
                    System.out.println("inwardUTR : " + inwardUTR);
                    if (i == 0) {
                        UTRNumberBuffer.append("'" + inwardUTR + "'");
                    } else {
                        UTRNumberBuffer.append("," + "'" + inwardUTR + "'");
                    }
                }
            }
            System.out.println("UTRNumberBuffer : " + UTRNumberBuffer);
            if (UTRNumberBuffer.length() > 0) {
                HashMap utrMap = new HashMap();
                utrMap.put("UTR_NUMBER", UTRNumberBuffer);
                utrMap.put("CURR_DATE", currDt.clone());
                List utrList = sqlMap.executeQueryForList("checkUTRNumberExists", utrMap);
                //SELECT * FROM RTGS_NEFT_ACKNOWLEDGEMENT WHERE UTR_NUMBER IN($UTR_NUMBER$)
                if (utrList != null && utrList.size() > 0) {
                    for (int i = 0; i < utrList.size(); i++) {
                        utrMap = (HashMap) utrList.get(i);
                        duplicateUTRMap.put(CommonUtil.convertObjToStr(utrMap.get("UTR_NUMBER")), "DUPLICATE");
                    }
                    System.out.println("###### DUPLICATE UTR NUMBER MAP : " + duplicateUTRMap);
                }
            }
            fis.close();
            bis.close();
            dis.close();

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                neftTO = new NeftRtgsAcknowledgementTO();
                String[] parts = line.split("\\|");
                for (int i = 0; i < parts.length; i++) {
                    System.out.println("after UTR while loop executed..." + i);
                    String data = CommonUtil.convertObjToStr(parts[i]);
                    System.out.println("after UTR while loop executed..." + data);
                    if (i == 0) {  //Message Type
                        mode = data;
                        continue;
                    }
                    if (i == 1) {  //UTR Number
                        UTRNumber = data;
                        continue;
                    }
                    if (i == 2) { //Sender IFSC
                        senderIFSC = data;
                        continue;
                    }
                    if (i == 3) { //Sender Acc Type
                        senderActType = data;
                        continue;
                    }
                    if (i == 4) { //Sender Account Number
                        senderActNumber = data;
                        continue;
                    }
                    if (i == 5) {  //Sender Name
                        senderName = data;
                        continue;
                    }
                    if (i == 6) {  //Sender Address 1
                        senderAddress = data;
                        continue;
                    }
                    if (i == 7) {  //Beneficiary IFSC
                        benefciaryIFSCCode = data;
                        continue;
                    }
                    if (i == 8) { //Beneficiary Account Number
                        beneficaryAccNo = data;
                        continue;
                    }
                    if (i == 9) {  //Beneficiary Account Name
                        benficaryName = data;
                        continue;
                    }
                    if (i == 10) {  //Sender Information
                        senderInfo = data;
                        continue;
                    }
                    if (i == 11) {  //Amount
                        amount = data;
                        continue;
                    }
                    if (i == 12) {//Value Date - NEFT/RTGS
                        valueDate = data;
                        continue;
                    }
                    if (i == 13) {//Transaction Date
                        transactionDate = data;
                        continue;
                    }
                    if (i == 14) {//Beneficiary Account Type
                        beneficiaryActType = data;
                        continue;
                    }
                    if (i == 15) {//Related Reference
                        reference = data;
                        continue;
                    }
                    if (i == 16) {//Beneficiary Address 1
                        beneficiaryAddress = data;
                        continue;
                    }
                    if (i == 17) {//Corporate Code
                        corporateCode = data;
                        continue;
                    }
                    if (i == 18) {//Client Code - Master
                        clientCode = data;
                        continue;
                    }
                    if (i == 19) {//Credit Acc Number - NEFT/RTGS
                        creditActNumber = data;
                        continue;
                    }
                    if (i == 20) {//Settled Trans Time  - NEFT/RTGS
                        setteledTransTime = data;
                        continue;
                    }
                    if (i == 21) {//Name - Master
                        masterName = data;
//                        continue;
                    }
                    System.out.println("####### beneficaryAccNo : " + beneficaryAccNo);
                    if (beneficaryAccNo.length() > 13) {
                        beneficaryAccNo = beneficaryAccNo.substring(beneficaryAccNo.length() - 13, beneficaryAccNo.length());
                        System.out.println("####### NEW BeneficaryAccNo : " + beneficaryAccNo);
                    }
                    System.out.println("mode : " + mode + " UTRNumber : " + UTRNumber + " senderIFSC : " + senderIFSC + " senderActType : " + senderActType
                            + " senderActNumber : " + senderActNumber + " senderName : " + senderName + " senderAddress : " + senderAddress + " benefciaryIFSCCode : " + benefciaryIFSCCode
                            + " beneficaryAccNo : " + beneficaryAccNo + " benficaryName : " + benficaryName + "senderInfo : " + senderInfo + "amount : " + amount + " valueDate : " + valueDate
                            + " transactionDate : " + transactionDate + " beneficiaryActType : " + beneficiaryActType + "reference : " + reference + " beneficiaryAddress :" + beneficiaryAddress
                            + " corporateCode : " + corporateCode + " clientCode : " + clientCode + " creditActNumber : " + creditActNumber + " setteledTransTime : " + setteledTransTime + " masterName : " + masterName);
                    acHeads.put("ACT_NUM", beneficaryAccNo);
                    acHeads.put("ACC_NUM", beneficaryAccNo);
                    List mapDataList = sqlMap.executeQueryForList("getActNumFromAllProductsADOA", acHeads);
                    if (mapDataList == null || mapDataList.isEmpty()) {
                        isTransAllowed = false;
                        status = "FAIL";
                        remarks = "ACCOUNT NOT EXIST";
                    } else if (mapDataList != null && mapDataList.size() > 0) {
                        HashMap map = (HashMap) mapDataList.get(0);
                        status = "PASS";
                        acHeads.put("CREDIT_PROD_TYPE", map.get("PROD_TYPE"));
                        acHeads.put("CREDIT_BRANCH_ID", map.get("BRANCH_ID"));
                        mapDataList = sqlMap.executeQueryForList("getAccountNumberNameOA", acHeads);
                        if (mapDataList != null && mapDataList.size() > 0) {
                            customerName = CommonUtil.convertObjToStr(((HashMap) mapDataList.get(0)).get(""));
                        }
                        mapDataList = sqlMap.executeQueryForList("getAcctHeadforAcct", map);
                        map = (HashMap) mapDataList.get(0);
                        creditAcctHeadId = CommonUtil.convertObjToStr(map.get("AC_HD_ID"));
                        acHeads.put("CREDIT_PROD_ID", map.get("PROD_ID"));
                        isTransAllowed = true;
                    }
                    neftTO.setAcctNum(beneficaryAccNo);
                    neftTO.setProdid(mode);
                    neftTO.setInwardUTR(inwardUTR);
                    neftTO.setOtherBrankIfsCode(benefciaryIFSCCode);
                    neftTO.setOtherBankAcctNum(beneficaryAccNo);
                    neftTO.setOtherBankCustName(senderName);
                    neftTO.setOtherBrankIfsCode(benefciaryIFSCCode);
                    neftTO.setAckDate((Date) currDt.clone());
                    neftTO.setAmount(new Double(amount));
                    neftTO.setHomeIfsCode("");
                    neftTO.setInitBranchId("0001");
                    neftTO.setAcctStatus(status);
                    neftTO.setStatus("OUTWARD");
//                    neftTO.setValueDate(getProperFormatDate((Object)DateUtil.getDateMMDDYYYY(valueDate)));
                    neftTO.setClient_Code(clientCode);
                    neftTO.setCr_Acct_Num(creditActNumber);
                    neftTO.setCorporateCode(corporateCode);
                    neftTO.setBeneficiary_Bank(benefciaryIFSCCode);
                    neftTO.setBeneficiaryName(benficaryName);
                    acHeads.put("CREDIT_ACT_NUM", neftTO.getAcctNum());
                    acHeads.put("CREDIT_ACCT_HEAD_ID", creditAcctHeadId);
                    acHeads.put("CREDIT_AMT", neftTO.getAmount());
                    if (mode.equals("NEFT")) {
                        acHeads.put("NEFT", "NEFT");
                    } else if (mode.equals("RTGS")) {
                        acHeads.put("RTGS", "RTGS");
                    }
                    neftTO.setClient_Code(client_code);
                    neftTO.setCr_Acct_Num(creditacNo);
                    neftTO.setBeneficiary_Bank(beneficiaryBank);
                    UTRFlag = true;
                    if (inwardUTR.length() > 0) {
                        if (duplicateUTRMap.containsKey(inwardUTR)) {
                            UTRFlag = false;
                            System.out.println("############################### DUPLICATE UTR_NUMBER : " + inwardUTR);
                        }
                    }
                    sqlMap.startTransaction();
                    if (UTRFlag) {
                        if (isTransAllowed) {
                            remarks = "ACCOUNT EXIST";
                            neftTO.setRemarks(senderInfo);
                            System.out.println("############################### readICICI_InputFile() : ");
                            String transId = CommonUtil.convertObjToStr(dotransaction(acHeads, neftTO).get("TRANS_ID"));        //DO TRANSACTION
                            neftTO.setBatch_id(transId);
                        }
                        neftTO.setStatus("OUTWARD");
                        neftTO.setFileName(file.getName());
                        neftTO.setRemarks(senderInfo);
                        sqlMap.executeUpdate("insertRtgsNeftAcknowledgementAxisBankTO", neftTO);
                    }
                    sqlMap.commitTransaction();
                }
            }
            fis.close();
            bis.close();
            dis.close();

            //INWARD ACKNOWLEDGEMENT FILE CREATION
            HashMap inputDataMap = new HashMap();
            HashMap fileDataMap = new HashMap();
            boolean fileCreation = false;
            boolean ackFileCreation = false;
            Calendar calender = Calendar.getInstance();
            DateFormat dtMiddleWithOutMin = new SimpleDateFormat("yyyyddMM");
            inputDataMap.put("FILE_NAME", file.getName());
            inputDataMap.put("CURR_DT", currDt.clone());
            List inwardList = sqlMap.executeQueryForList("getInwardFileData", inputDataMap);
            if (inwardList != null && inwardList.size() > 0) {
                for (int i = 0; i < inwardList.size(); i++) {
                    inputDataMap = (HashMap) inwardList.get(i);
                    fileDataMap.put(CommonUtil.convertObjToStr(inputDataMap.get("UTR_NUMBER")), CommonUtil.convertObjToStr(inputDataMap.get("ACCT_STATUS")));
                    if (CommonUtil.convertObjToStr(inputDataMap.get("ACCT_STATUS")).equals("FAIL")) {
                        ackFileCreation = true;
                    }
                }
                System.out.println("############ fileDataMap : " + fileDataMap + "   ########### ackFileCreation : " + ackFileCreation);
                if (ackFileCreation) {
                    String fileName = file.getName();
                    String file_Name = fileName.substring(0, fileName.lastIndexOf("."));
                    System.out.println("############ file_Name : " + file_Name);
                    String path = CommonConstants.RTGS_NEFT_OUTWARD_PATH + CommonConstants.RTGS_NEFT_CLIENT_CODE + "_"
                            + CommonConstants.RTGS_NEFT_FORMAT_CODE + "_" + generateRandomNumbers() + "_" + file_Name + ".txt";
                    System.out.println("############## OUTWARD FILE NAME PATH : " + path);
                    java.io.FileWriter write = new java.io.FileWriter(path, false);
                    java.io.PrintWriter print_line = new java.io.PrintWriter(write);
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    dis = new DataInputStream(bis);
                    String transStatus = "";
                    while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                        neftTO = new NeftRtgsAcknowledgementTO();
                        String[] parts = line.split("\\|");
                        for (int i = 0; i < parts.length; i++) {
                            String data = CommonUtil.convertObjToStr(parts[i]);
                            if (i == 0) {  //Message Type
                                mode = data;
                                continue;
                            }
                            if (i == 1) {  //UTR Number
                                UTRNumber = data;
                                continue;
                            }
                            if (i == 2) { //Sender IFSC
                                senderIFSC = data;
                                continue;
                            }
                            if (i == 3) { //Sender Acc Type
                                senderActType = data;
                                continue;
                            }
                            if (i == 4) { //Sender Account Number
                                senderActNumber = data;
                                continue;
                            }
                            if (i == 5) {  //Sender Name
                                senderName = data;
                                continue;
                            }
                            if (i == 6) {  //Sender Address 1
                                senderAddress = data;
                                continue;
                            }
                            if (i == 7) {  //Beneficiary IFSC
                                benefciaryIFSCCode = data;
                                continue;
                            }
                            if (i == 8) { //Beneficiary Account Number
                                beneficaryAccNo = data;
                                continue;
                            }
                            if (i == 9) {  //Beneficiary Account Name
                                benficaryName = data;
                                continue;
                            }
                            if (i == 10) {  //Sender Information
                                senderInfo = data;
                                continue;
                            }
                            if (i == 11) {  //Amount
                                amount = data;
                                continue;
                            }
                            if (i == 12) {//Value Date - NEFT/RTGS
                                valueDate = data;
                                continue;
                            }
                            if (i == 13) {//Transaction Date
                                transactionDate = data;
                                continue;
                            }
                            if (i == 14) {//Beneficiary Account Type
                                beneficiaryActType = data;
                                continue;
                            }
                            if (i == 15) {//Related Reference
                                reference = data;
                                continue;
                            }
                            if (i == 16) {//Beneficiary Address 1
                                beneficiaryAddress = data;
                                continue;
                            }
                            if (i == 17) {//Corporate Code
                                corporateCode = data;
                                continue;
                            }
                            if (i == 18) {//Client Code - Master
                                clientCode = data;
                                continue;
                            }
                            if (i == 19) {//Credit Acc Number - NEFT/RTGS
                                creditActNumber = data;
                                continue;
                            }
                            if (i == 20) {//Settled Trans Time  - NEFT/RTGS
                                setteledTransTime = data;
                                continue;
                            }
                            if (i == 21) {//Name - Master
                                masterName = data;
                            }
                            transStatus = "";
                            fileCreation = false;
                            if (inwardUTR.length() > 0) {
                                if (fileDataMap.containsKey(inwardUTR)) {
                                    if (CommonUtil.convertObjToStr(fileDataMap.get(inwardUTR)).equals("PASS")) {
                                        System.out.println("############## Status - PASS");
                                        transStatus = "P" + "|";
                                    } else if (CommonUtil.convertObjToStr(fileDataMap.get(inwardUTR)).equals("FAIL")) {
                                        System.out.println("############## Status - FAIL");
                                        transStatus = "C" + "|";
                                        fileCreation = true;
                                    }
                                } else {
                                    System.out.println("############## Status - EMPTY");
                                    transStatus = " " + "|";
                                }
                            }
                            if (fileCreation) {
                                print_line.print("i");                                  //1 Record_Identifier 	Always "i"
                                print_line.print(pipeDelimiter);
                                if (CommonUtil.convertObjToStr(mode).equals("NEFT")) {  //2 Payment Indicator
                                    print_line.print("N");
                                } else if (CommonUtil.convertObjToStr(mode).equals("RTGS")) {
                                    print_line.print("R");
                                } else if (CommonUtil.convertObjToStr(mode).equals("IMPS")) {
                                    print_line.print("M");
                                } else {
                                    print_line.print("I");
                                }
                                print_line.print(pipeDelimiter);
                                print_line.print(inwardUTR);                    //3 UNIQUE ID   -M
                                print_line.print(pipeDelimiter);
                                //BENEFICARY CODE OPTIONAL                      //4 BENEFICARY CODE   O
                                print_line.print(pipeDelimiter);
                                benficaryName = benficaryName.replaceAll("[\\.\\/\\[\\{\\}\\;\\:\\`!@#$%^&*()_<>?\\,\\+\\-\\=\\|]\\~\\'", "");
                                print_line.print(benficaryName);                //5 BENEFICARY NAME -M
                                print_line.print(pipeDelimiter);
                                //BENEFICARY CODE OPTIONAL
                                print_line.print(pipeDelimiter);
                                print_line.print(amount);                       //6 TRANSACTION AMOUNT  M=MANDATORY
                                print_line.print(pipeDelimiter);
                                print_line.print(DateUtil.getStringDate((Date) currDt.clone())); //7 DATE OF PAYMENT
                                print_line.print(pipeDelimiter);
                                print_line.print(CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_DR_ACT_NUM)); //8 DEBIT ACT NUMBER
                                print_line.print(pipeDelimiter);
                                print_line.print(beneficaryAccNo);              //9 BENEFICARY ACT NUMBER
                                print_line.print(pipeDelimiter);
                                print_line.print(senderIfscode);                //10 BENEFICARY IFSCODE
                                print_line.print(pipeDelimiter);
                                //BENEFICARY BANK NAME                          //11 BENEFICARY BANK NAME
                                print_line.print(pipeDelimiter);
                                //BENEFICARY MAIL ID                            //12 BENEFICARY MAIL ID
                                print_line.print(pipeDelimiter);
                                //BENEFICARY MOBILE NO                          //13 BENEFICARY MOBILE NO
                                print_line.print(pipeDelimiter);
                                print_line.print(remitterAcNo);                 //14 REMITTER ACCOUNT NUMBER
                                print_line.print(pipeDelimiter);
                                print_line.print(inwardUTR + "-" + "ACCOUNT NOT EXIST");//15 REMARKS
                                print_line.println(pipeDelimiter);
                            }
                        }
                    }
                    fis.close();
                    bis.close();
                    dis.close();
                    if (print_line != null) {
                        print_line.close();
                    }
                    if (write != null) {
                        write.close();
                    }
                }
            }
            System.out.println("############# INWARD ACKNOWLEDGEMENT FILE CREATED ");
            //FILE MOVED TO BACKUP FOLDER
            String sourceFilePath = CommonConstants.AXIS_RTGS_NEFT_INWARD_PATH + file.getName();
            String destinationFilePath = CommonConstants.AXIS_RTGS_NEFT_INWARD_BACKUP_PATH + "INWARD_" + generateRandomNumbers() + file.getName() + ".Done";
            File file1 = new File(sourceFilePath);
            file1.renameTo(new File(destinationFilePath));
            System.out.println("############# FILE MOVED " + destinationFilePath + "  ##### file.getName() : " + file.getName());
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    private void readAXISDataFromOutwardReturnFile(File file) throws Exception {
        System.out.println("read_AXIS_Data_From_Outward_ReturnFile STARTED : ");
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        String line = "", inwardUTR = "", status = "";
        HashMap dataMap = new HashMap();
        HashMap acHeads = new HashMap();
        String pipeDelimiter = "|";
        String creditAcctHeadId = "", customerName = "", sequenceNo = "", corporateName = "", nextWorkingDayDate = "", corporateRefNo = "",
                batchNo = "", corporateProduct = "", paymentMethod = "", debitActNum = "", beneficiaryName = "", beneficiaryActNum = "",
                beneficiaryAmount = "", ifscCode = "", beneficiaryBank = "",initiatedDate = "",settlementDate = "",rejectionReason = "",
                reversalReason = "",subMemberIFSCcode = "",activationDate = "",corporateProductID = "";
        boolean UTRFlag = false, isTransAllowed = false;
        NeftRtgsAcknowledgementTO neftTO = new NeftRtgsAcknowledgementTO();
        HashMap detailMap = new HashMap();
        HashMap creditAcctMap = new HashMap();
        RtgsRemittanceTO remitTo = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                neftTO = new NeftRtgsAcknowledgementTO();
                detailMap = new HashMap();
                String[] parts = line.split("\\|");
                for (int i = 0; i < parts.length; i++) {
                    String data = CommonUtil.convertObjToStr(parts[i]);
                    if (i == 0) {       //SLNo
                        sequenceNo = data;
                        continue;
                    }
                    if (i == 1) {       //Corporate Name
                        corporateName = data;
                        continue;
                    }
                    if (i == 2) {       //Nextworking day date
                        nextWorkingDayDate = data;
                        continue;
                    }
                    if (i == 3) {       //Corporate reference no
                        corporateRefNo = data;
                        continue;
                    }
                    if (i == 4) {       //Batch No
                        batchNo = data;
                        continue;
                    }
                    if (i == 5) {       //Corporate product
                        corporateProduct = data;
                        continue;
                    }
                    if (i == 6) {       //Corporate productID
                        corporateProductID = data;
                        continue;
                    }
                    if (i == 7) {       //Payment method
                        paymentMethod = data;
                        continue;
                    }
                    if (i == 8) {       //Debit AccountNo
                        debitActNum = data;
                        continue;
                    }
                    if (i == 9) {       //Beneficiary Name
                        beneficiaryName = data;
                        continue;
                    }
                    if (i == 10) {       //Beneficiary ActNum
                        beneficiaryActNum = data;
                        continue;
                    }
                    if (i == 11) {      //Amount Payable
                        beneficiaryAmount = data;
                        continue;
                    }
                    if (i == 12) {      //IFSC Code
                        ifscCode = data;
                        continue;
                    }
                    if (i == 13) {      //Beneficary Bank
                        beneficiaryBank = data;
                        continue;
                    }
                    if (i == 14) {      //UTR Number
                        inwardUTR = data;
                        continue;
                    }
                    if (i == 15) {      //Status
                        status = data;
                        continue;
                    }
                    if (i == 16) {      //initiatedDate
                        initiatedDate = data;
                        continue;
                    }
                    if (i == 17) {      //SettlementDate
                        settlementDate = data;
                        continue;
                    }
                    if (i == 18) {      //Rejection Reason
                        rejectionReason = data;
                        continue;
                    }
                    if (i == 19) {      //Reversal Reason
                        reversalReason = data;
                        continue;
                    }
                    if (i == 20) {      //SUB Member IFSC Code
                        subMemberIFSCcode = data;
                        continue;
                    }
                    if (i == 21) {      //Activation Date
                        activationDate = data;
//                        continue;
                    }

                    sqlMap.startTransaction();
                    System.out.println("sequenceNo : " + sequenceNo + " corporateName : " + corporateName + " nextWorkingDayDate  : " + nextWorkingDayDate + 
                            " corporateRefNo : " + corporateRefNo + " batchNo : " + batchNo + " corporateProduct : " + corporateProduct + 
                            " paymentMethod : " + paymentMethod + " debitActNum : " + debitActNum + " beneficiaryName : " + beneficiaryName + 
                            " beneficiaryName : " + beneficiaryName + " beneficiaryActNum : " + beneficiaryActNum + " beneficiaryAmount : " + beneficiaryAmount + 
                            " ifscCode : " + ifscCode + " beneficiaryBank : " + beneficiaryBank + "inwardUTR : " + inwardUTR + 
                            " status : " + status+" initiatedDate : "+initiatedDate + " settlementDate : " + settlementDate + 
                            " rejectionReason : " + rejectionReason + "reversalReason : " + reversalReason + "subMemberIFSCcode : " + 
                            subMemberIFSCcode + " activation date : "+activationDate);
                    if (CommonUtil.convertObjToStr(status).equals("PAID")) {
                        detailMap.put("UTR_NUMBER", inwardUTR);
                        detailMap.put("FILE_STATUS", "PASS");
                    } else if (CommonUtil.convertObjToStr(status).equals("RETURN")) {
                        detailMap.put("UTR_NUMBER", inwardUTR);
                        detailMap.put("FILE_STATUS", "FAIL");
                        List lst = sqlMap.executeQueryForList("getRTGSRemittransDetailsTO", detailMap);
                        if (lst != null && lst.size() > 0) {
                            remitTo = (RtgsRemittanceTO) lst.get(0);
                            detailMap.put("BATCH_DT", remitTo.getBatchDt());
                            detailMap.put("RTGS_ID", remitTo.getRtgs_ID());
                            lst = sqlMap.executeQueryForList("getCreditAcctDetails", detailMap);
                            if (lst != null && lst.size() > 0) {
                                creditAcctMap = (HashMap) lst.get(0);
                                neftTO.setAcctNum(CommonUtil.convertObjToStr(creditAcctMap.get("DEBIT_ACCT_NO")));
                                neftTO.setAckDate((Date) currDt.clone());
                                neftTO.setAmount(remitTo.getAmount());
                                neftTO.setHomeIfsCode(remitTo.getIfsc_Code());
                                neftTO.setOtherBrankIfsCode(remitTo.getBeneficiary_IFSC_Code());
                                neftTO.setOtherBankAcctNum(remitTo.getAccount_No());
                                neftTO.setOtherBankCustName(remitTo.getBeneficiary_Name());
                                neftTO.setInitBranchId(remitTo.getInitiatedBranch());
                                neftTO.setAcctStatus(status);
                                neftTO.setInwardUTR(remitTo.getUtrNumber());
                                acHeads.put("CREDIT_ACT_NUM", neftTO.getAcctNum());
                                acHeads.put("CREDIT_PROD_ID", CommonUtil.convertObjToStr(creditAcctMap.get("PROD_ID")));
                                acHeads.put("CREDIT_PROD_TYPE", CommonUtil.convertObjToStr(creditAcctMap.get("PRODUCT_TYPE")));
                                acHeads.put("CREDIT_BRANCH_ID", CommonUtil.convertObjToStr(creditAcctMap.get("BRANCH_CODE")));
                                detailMap.put("PROD_ID", remitTo.getProdId());
                                lst = sqlMap.executeQueryForList("getAcctHeadforAcct", detailMap);
                                if (lst != null && lst.size() > 0) {
                                    creditAcctMap = (HashMap) lst.get(0);
                                    acHeads.put("CREDIT_ACCT_HEAD_ID", CommonUtil.convertObjToStr(creditAcctMap.get("ISSUE_HD")));
                                }
                                acHeads.put("CREDIT_AMT", neftTO.getAmount());
                                if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("NEFT")) {
                                    acHeads.put("NEFT", "NEFT");
                                } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("RTGS")) {
                                    acHeads.put("RTGS", "RTGS");
                                }
                                System.out.println("read_DataFromOutwardReturnFile() : ");
                                dotransaction(acHeads, neftTO);
                            }
                        }
                    }
                    if (detailMap != null && detailMap.size() > 0) {
                        sqlMap.executeUpdate("updateOutwardResponseFileStatus", detailMap);
                    }
                    sqlMap.commitTransaction();
                }
            }
            fis.close();
            bis.close();
            dis.close();
            //FILE MOVED TO BACKUP FOLDER
            String sourceFilePath = CommonConstants.AXIS_RTGS_NEFT_INWARD_RETURN_PATH + file.getName();
            String destinationFilePath = CommonConstants.AXIS_RTGS_NEFT_INWARD_BACKUP_RETURN_PATH + "OUTWARD_RES_" + generateRandomNumbers() + file.getName() + ".Done";
            File file1 = new File(sourceFilePath);
            file1.renameTo(new File(destinationFilePath));
            System.out.println("############# FILE MOVED " + destinationFilePath + "  ##### file.getName() : " + file.getName());
            System.out.println("############ read_DataFromOutwardReturnFile COMPLETED : ");
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }
    
    public Date getProperFormatDate(Object obj) {
        Date curDt = null;
        // currDt = properFormatDate;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curDt = (Date) currDt.clone();
            curDt.setDate(tempDt.getDate());
            curDt.setMonth(tempDt.getMonth());
            curDt.setYear(tempDt.getYear());
        }
        return curDt;
    }
    
  
    // Code changes for SBI RTGS/NEFT
    
        public void outward_RTGSNEFT_SBI(String branch_id) throws Exception {     //RTGS/NEFT MANUAL SCREEN TRANSACTIONS
        System.out.println("######## 1 outward_RTGSNEFT_SBI() PROCESS STARTED : ");
        HashMap dataMap = new HashMap();
        HashMap authMap = new HashMap();
        currDt = ServerUtil.getCurrentDate(branch_id);
        DateFormat years2digit = new SimpleDateFormat("yy");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dtFormtWithOutMin = new SimpleDateFormat("yyyyMMdd");
        DateFormat dtMiddleWithOutMin = new SimpleDateFormat("yyyyddMM");
        DateFormat minOnly = new SimpleDateFormat("hhmm");
        Calendar calender = Calendar.getInstance();
        HashMap singleUTRMap = new HashMap();
        Date dt = new Date();
        String path = "";
        String fileName = "";
        String transAmt = "";
        String pipeDelimiter = "~";
        int daysInYear = 0;
        java.io.FileWriter write = null;
        java.io.PrintWriter print_line = null;
        String neftUniqueId = "", sequnceNum = "", dayinyearStr = "";
        StringBuffer dayInYearbuffer = new StringBuffer();
        StringBuffer utrBuffer = new StringBuffer();
        RtgsRemittanceTO resultTo = null;
        try {
            List RTGSlist = (List) sqlMap.executeQueryForList("getRTGSRemittanceTOForFileCreation", singleUTRMap);
            if (RTGSlist != null && RTGSlist.size() > 0) {
                for (int i = 0; i < RTGSlist.size(); i++) {
                    resultTo = (RtgsRemittanceTO) RTGSlist.get(i);
                    if (resultTo != null) {
                        dateFormat.format(resultTo.getBatchDt());
                        neftUniqueId = CommonUtil.convertObjToStr(resultTo.getUtrNumber());
                        //System.out.println("#### resultTo.getUtrNumber() : " + resultTo.getUtrNumber() + " ###dateFormat : "
                        //+ dateFormat.format(calender.getTime()) + "check" + DateUtil.getStringDate(resultTo.getBatchDt()));
                        if (i == 0) {
                            String str1 = ""; //Added By Suresh 15-Feb-2018 File name changes
                            DateFormat dateFormat1 = new SimpleDateFormat("ddMMyy");
                            Calendar calender1 = Calendar.getInstance();
                            dateFormat1.format(calender1.getTime());
                            str1 = dateFormat1.format(calender1.getTime());
                            //System.out.println("######## generateRandomNumbers 2 : " + str1);
                            String outwardFileID = "";
                            outwardFileID = getOutwardFileID();
                            //System.out.println("######## random_Number  2 : " + outwardFileID);
                            //fileName = CommonConstants.RTGS_NEFT_CLIENT_CODE + "_" + CommonConstants.RTGS_NEFT_FORMAT_CODE
                            //+ "_" + generateRandomNumbers() + "_" + CommonConstants.RTGS_BANK_NAME + ".TXT";
                            fileName = CommonConstants.RTGS_NEFT_CLIENT_CODE + "_" + CommonConstants.RTGS_NEFT_FORMAT_CODE
                                    + "_" + str1 + "_" + outwardFileID + ".TXT";
                            path = CommonConstants.RTGS_NEFT_OUTWARD_PATH + fileName;
                            write = new java.io.FileWriter(path, false);
                            print_line = new java.io.PrintWriter(write);
                            System.out.println("###### Outward File Name Path : " + path);
                        }
                        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                        daysInYear = localCalendar.get(Calendar.DAY_OF_YEAR);
                        dayinyearStr = String.valueOf(daysInYear);
                        if (dayinyearStr.length() == 2) {
                            dayInYearbuffer.append("0" + dayinyearStr);
                        } else if (dayinyearStr.length() == 1) {
                            dayInYearbuffer.append("00" + dayinyearStr);
                        } else {
                            dayInYearbuffer.append(dayinyearStr);
                        }
                        if (neftUniqueId != null && neftUniqueId.length() > 0) {    
                            print_line.print(CommonConstants.RTGS_NEFT_CLIENT_CODE);      //Clarify                         
                            print_line.print(pipeDelimiter);
                            print_line.print(CommonConstants.RTGS_NEFT_FORMAT_CODE);
                            print_line.print(pipeDelimiter);
                            print_line.print(CommonUtil.convertObjToStr(CommonConstants.RTGS_NEFT_DR_ACT_NUM));
                            print_line.print(pipeDelimiter);
                            if (resultTo.getBeneficiary_IFSC_Code().startsWith("SBIN")) {
                                print_line.print("DCR");
                            } else {
                                if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("NEFT")) {
                                    print_line.print("NEFT");
                                } else if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("RTGS")) {
                                    print_line.print("RTGS");
                                } else if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("IMPS")) {
                                    print_line.print("IMPS");
                                } else if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("UPI")) {
                                    print_line.print("UPI");
                                } else if (CommonUtil.convertObjToStr(resultTo.getProdId()).equals("DCR")) {
                                    print_line.print("DCR");
                                }
                            }
                            print_line.print(pipeDelimiter);
                            transAmt = CommonUtil.convertObjToStr(resultTo.getAmount());
                            if (transAmt.contains(".")) {
                                String number = transAmt.substring(transAmt.indexOf(".")).substring(1);
                                if (number.length() == 1) {
                                    transAmt = transAmt + "0";
                                }
                            } else {
                                transAmt = transAmt + ".00";
                            }
                            print_line.print(transAmt);                       
                            print_line.print(pipeDelimiter);
                            String beneficiaryName = "";
                            beneficiaryName = resultTo.getBeneficiary_Name().replaceAll("[\\.\\/\\[\\{\\}\\;\\:\\`!@#$%^&*()_<>?\\,\\+\\-\\=\\|]\\~\\'", "");
                            print_line.print(beneficiaryName);                  
                            print_line.print(pipeDelimiter);
                            print_line.print(CommonUtil.convertObjToStr(resultTo.getBeneficiary_IFSC_Code()));
                            print_line.print(pipeDelimiter);
                            print_line.print(CommonUtil.convertObjToStr(resultTo.getAccount_No())); 
                            print_line.print(pipeDelimiter);
                            print_line.print(CommonUtil.convertObjToStr(beneficiaryName)); 
                            //SBI~CBS~123456789012345~NEFT~1500.50~John Doe~SBIN0001234~1234567890~John Doe
                            //////  end---                           
                            if (RTGSlist.size() - 1 == i) {
                                utrBuffer.append("'" + neftUniqueId + "'");
                            } else {
                                utrBuffer.append("'" + neftUniqueId + "'" + ",");
                            }
                            //SBI~CBS~123456789012345~NEFT~1500.50~John Doe~SBIN0001234~1234567890~John Doe
                        }
                    }
                }
                authMap.put("UTR_NUMBER", utrBuffer);
                authMap.put("PROCESS_DT", currDt.clone());
                authMap.put("FILE_STATUS", "DELIVERED");
                System.out.println("########## authMap : " + authMap);
                sqlMap.executeUpdate("updateOutwardRTGSNEFTStatus", authMap);
                if (print_line != null) {
                    print_line.close();
                }
                if (write != null) {
                    write.close();
                }
            }
            System.out.println("######## 1 outward_RTGSNEFT_SBI() PROCESS COMPLETED : ");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TTException(e);
        }
    }
          
     
    private void read_SBI_InputFile(File file) throws Exception {
        System.out.println("########## 2 read_SBI_InputFile() PROCESS STARTED : " + file.getName());
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        HashMap duplicateUTRMap = new HashMap();
        String line = "", inwardUTR = "", remarks = "";
        HashMap dataMap = new HashMap();
        HashMap acHeads = new HashMap();
        String pipeDelimiter = "|";
        String creditAcctHeadId = "", customerName = "";
        StringBuffer UTRNumberBuffer = new StringBuffer();
        boolean UTRFlag = false, isTransAllowed = false;
        NeftRtgsAcknowledgementTO neftTO = new NeftRtgsAcknowledgementTO();
        String client_code = "", beneficaryAccNo = "", benficaryName = "", amount = "", date = "", UTRNumber = "",
                remitterAcNo = "", senderIfscode = "", creditacNo = "", remitterName = "", mode = "", beneficiaryBank = "", status = "",dealerCode = "",
                creditCancelDate = "",uniqueRefNo = "",txnStatus = "";
        try {
            
//            if (dis.available() > 0) {
//                line = dis.readLine(); // Reads and discards the first line (usually header)
//            } 
            
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                String[] parts = line.split("\\,");
                for (int i = 0; i < parts.length; i++) {
                    inwardUTR = "";
                    String data = CommonUtil.convertObjToStr(parts[i]);
                    if (i == 6) {  //UTR No
                        inwardUTR = data;
                    }
                    if (i == 8) {  
                        txnStatus = data;
                        System.out.println("###### txnStatus :" + txnStatus);
                    }
                    if (inwardUTR.length() > 0) {
                        if (UTRNumberBuffer.length()<= 0) {
                            UTRNumberBuffer.append("'" + inwardUTR + "'");
                        } else {
                            UTRNumberBuffer.append("," + "'" + inwardUTR + "'");
                        }
                    }
                }
            }
            if (UTRNumberBuffer.length() > 0) {
                HashMap utrMap = new HashMap();
                utrMap.put("UTR_NUMBER", UTRNumberBuffer);
                utrMap.put("CURR_DATE", currDt.clone());
                List utrList = sqlMap.executeQueryForList("checkUTRNumberExists", utrMap);
                //SELECT * FROM RTGS_NEFT_ACKNOWLEDGEMENT WHERE UTR_NUMBER IN($UTR_NUMBER$)
                if (utrList != null && utrList.size() > 0) {
                    for (int i = 0; i < utrList.size(); i++) {
                        utrMap = (HashMap) utrList.get(i);
                        duplicateUTRMap.put(CommonUtil.convertObjToStr(utrMap.get("UTR_NUMBER")), "DUPLICATE");
                    }
                    System.out.println("###### DUPLICATE UTR NUMBER MAP : " + duplicateUTRMap);
                }
            }
            fis.close();
            bis.close();
            dis.close();

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            String data = "";
            
//            if (dis.available() > 0) {
//                line = dis.readLine(); //Reads and discards the first line (usually header)
//            } 
            
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                neftTO = new NeftRtgsAcknowledgementTO();
                String[] parts = line.split("\\,");
                for (int i = 0; i < parts.length; i++) {
                    System.out.println("############## i VALUE : " + i + "    #### parts.length : " + parts.length);
                    data = CommonUtil.convertObjToStr(parts[i]);
                    System.out.println("##### Data : " + data);                    
                    if (i == 0) { 
                        mode = data;
                        if(mode.equalsIgnoreCase("Online Transfer")){
                            mode = "NEFT";
                        }
                        System.out.println("###### mode 0 :" + mode);
                        continue;
                    }
                    if (i == 1) { 
                        beneficaryAccNo = data;
                        System.out.println("###### beneficaryAccNo 1 :" + beneficaryAccNo);
                        continue;
                    }
                    if (i == 2) { 
                        date = data;
                        System.out.println("###### date 2 :" + date);
                        continue;
                    }
                    if (i == 3) { 
                        dealerCode = data;
                        System.out.println("###### dealerCode 3 :" + dealerCode);
                        continue;
                    }
                    if (i == 4) {  
                        amount = data;
                        System.out.println("###### amount 4 :" + amount);
                        continue;
                    }
                    if (i == 5) {  
                        creditCancelDate = data;
                        System.out.println("###### creditCancelDate 5 :" + creditCancelDate);
                        continue;
                    }
                    if (i == 6) {  
                        inwardUTR = data;
                        System.out.println("###### inwardUTR 6 :" + inwardUTR);
                        continue;
                    }                    
                    if (i == 7) {  
                        uniqueRefNo = data;
                        System.out.println("###### uniqueRefNo 7 :" + uniqueRefNo);
                        continue;
                    }
                    if (i == 8) {  
                        txnStatus = data;
                        System.out.println("###### txnStatus 8 :" + txnStatus);
                        continue;
                    }
                    if (i == 9) {  
                        creditacNo = data;
                        System.out.println("###### creditacNo 9 :" + creditacNo);
                        continue;
                    }
                    if (i == 10) { 
                        if (data.length() >= 50) {
                            remitterName = data.substring(0, 50);
                        } else {
                            remitterName = data;
                        }
                        System.out.println("###### remitterName 10 :" + remitterName);
                        if (parts.length > 11) {
                            continue;
                        }
                    }
                    
                    if (i == 11) {  
                        remitterAcNo = data;
                        System.out.println("###### remitterAcNo 11 :" + remitterAcNo);   
                    }
                   
                    if (beneficaryAccNo.length() > 13) {
                        beneficaryAccNo = beneficaryAccNo.substring(beneficaryAccNo.length() - 13, beneficaryAccNo.length());
                        System.out.println("####### NEW BeneficaryAccNo : " + beneficaryAccNo);
                    }
                    System.out.println("####### client_code : " + client_code + "\n" + " ####### beneficaryAccNo " + beneficaryAccNo
                            + "\n" + " ####### amount : " + amount + "\n" + " ####### inwardUTR : " + inwardUTR
                            + "\n" + " ####### remitterAcNo : " + remitterAcNo + "\n" + " ####### senderIfscode : " + senderIfscode
                            + "\n" + " ####### creditacNo#####" + creditacNo + "\n" + " ####### remitterName : " + remitterName
                            + "\n" + " ####### mode : " + mode + "\n" + " ####### beneficiaryBank : " + beneficiaryBank
                            + "\n" + " ####### benficaryName : " + benficaryName + "\n" + " ####### status : " + status+"date"+date);
                    acHeads.put("ACT_NUM", beneficaryAccNo);
                    acHeads.put("ACC_NUM", beneficaryAccNo);
                    List mapDataList = sqlMap.executeQueryForList("getActNumFromAllProductsADOA", acHeads);
                    if (mapDataList == null || mapDataList.isEmpty()) {
                        isTransAllowed = false;
                        status = "FAIL";
                        remarks = "ACCOUNT NOT EXIST";
                    } else if (mapDataList != null && mapDataList.size() > 0) {
                        HashMap map = (HashMap) mapDataList.get(0);
                        status = "PASS";
                        acHeads.put("CREDIT_PROD_TYPE", map.get("PROD_TYPE"));
                        acHeads.put("CREDIT_BRANCH_ID", map.get("BRANCH_ID"));
                        mapDataList = sqlMap.executeQueryForList("getAccountNumberNameOA", acHeads);
                        if (mapDataList != null && mapDataList.size() > 0) {
                            customerName = CommonUtil.convertObjToStr(((HashMap) mapDataList.get(0)).get(""));
                        }
                        mapDataList = sqlMap.executeQueryForList("getAcctHeadforAcct", map);
                        map = (HashMap) mapDataList.get(0);
                        creditAcctHeadId = CommonUtil.convertObjToStr(map.get("AC_HD_ID"));
                        acHeads.put("CREDIT_PROD_ID", map.get("PROD_ID"));
                        isTransAllowed = true;
                    }
                    neftTO.setAcctNum(beneficaryAccNo);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    neftTO.setAckDate((Date) sdf.parse(date));
                    //neftTO.setAckDate((Date) DateUtil.getDateMMDDYYYY(date));
                    neftTO.setAmount(new Double(amount));
                    neftTO.setHomeIfsCode("");
                    neftTO.setOtherBrankIfsCode(senderIfscode);
                    neftTO.setInitBranchId("0001");
                    neftTO.setAcctStatus(status);
                    neftTO.setStatus("OUTWARD");
                    neftTO.setInwardUTR(inwardUTR);
                    neftTO.setOtherBankAcctNum(remitterAcNo);
                    neftTO.setOtherBankCustName(remitterName);
                    neftTO.setBeneficiaryName(benficaryName);
                    acHeads.put("CREDIT_ACT_NUM", neftTO.getAcctNum());
                    acHeads.put("CREDIT_ACCT_HEAD_ID", creditAcctHeadId);
                    acHeads.put("CREDIT_AMT", neftTO.getAmount());
                    if (mode.equals("NEFT")) {
                        acHeads.put("NEFT", "NEFT");
                    } else if (mode.equals("RTGS")) {
                        acHeads.put("RTGS", "RTGS");
                    } else if (mode.equals("IMPS")) {
                        //acHeads.put("IMPS", "IMPS"); //Commented By Suresh On 3-Sep-2019  Ref By Jithesh.
                        acHeads.put("NEFT", "NEFT");
                        mode = "NEFT";
                    } else if (mode.equals("ICICI_FT")) {
                        acHeads.put("ICICI_FT", "ICICI_FT");
                    } else if (mode.equals("UPI")) { //Added by nithya on 20-09-2022 fro KD-3471                       
                        acHeads.put("NEFT", "NEFT");
                        mode = "NEFT";
                    } else if (mode.equals("FT")) { //Added by nithya on 03-02-2025 for KD-3842                  
                        acHeads.put("NEFT", "NEFT");
                        mode = "NEFT";
                    }
                    neftTO.setProdid(mode);
                    neftTO.setClient_Code(client_code);
                    neftTO.setCr_Acct_Num(creditacNo);
                    neftTO.setBeneficiary_Bank(beneficiaryBank);
                    UTRFlag = true;
                    if (inwardUTR.length() > 0) {
                        if (duplicateUTRMap.containsKey(inwardUTR)) {
                            UTRFlag = false;
                            System.out.println("############################### DUPLICATE UTR_NUMBER : " + inwardUTR);
                        }
                    }
                    sqlMap.startTransaction();
                    if (UTRFlag) {
                        System.out.println("ack date:"+neftTO.getAckDate()+": DateUtil.dateDiff(neftTO.getAckDate(), currDt):="+DateUtil.dateDiff(neftTO.getAckDate(), currDt));
                        if(DateUtil.dateDiff(neftTO.getAckDate(), currDt)>4)
                        {
                            neftTO.setAcctStatus("FAIL");
                             remarks = "FUTURE DATE ENT";
                              neftTO.setRemarks(remarks);
                        }
                        else{
                        if (isTransAllowed) {
                            remarks = "ACCOUNT EXIST";
                            neftTO.setRemarks(remarks);
                            System.out.println("############################### read_SBI_InputFile() : ");
                            String transId = CommonUtil.convertObjToStr(dotransaction(acHeads, neftTO).get("TRANS_ID"));        //DO TRANSACTION
                            neftTO.setBatch_id(transId);
                        }
                        }
                        neftTO.setStatus("OUTWARD");
                        neftTO.setFileName(file.getName());
                        neftTO.setRemarks(remarks);
                        sqlMap.executeUpdate("insertRtgsNeftAcknowledgementTO", neftTO);
                    }
                    sqlMap.commitTransaction();
                    i = parts.length;    //Added By Suresh R        Ref Mr Abi 16-Mar-2018
                }
            }
            fis.close();
            bis.close();
            dis.close();

            //INWARD ACKNOWLEDGEMENT FILE CREATION - To be done
         
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }
     
    private void readSBIData_From_Outward_ReturnFile(File file) throws Exception {//Outward Acknowledgement
        System.out.println("########## 3 readSBIData_From_Outward_ReturnFile() PROCESS STARTED : "+file.getName());
        String line = "", inwardUTR = "", status = "";
        HashMap dataMap = new HashMap();
        HashMap acHeads = new HashMap();
        String pipeDelimiter = "|";
        String creditAcctHeadId = "", customerName = "";
        boolean UTRFlag = false, isTransAllowed = false;
        NeftRtgsAcknowledgementTO neftTO = new NeftRtgsAcknowledgementTO();
        HashMap detailMap = new HashMap();
        HashMap creditAcctMap = new HashMap();
        RtgsRemittanceTO remitTo = null;
        boolean fileNameInsert = true;
        HashMap fileMap = new HashMap();
        try {
            fileMap = new HashMap();
            fileMap.put("FILE_NAME", file.getName());
            List fileList = sqlMap.executeQueryForList("getCheckFileExistOutwardAck", fileMap);
            if (fileList != null && fileList.size() > 0) {
                System.out.println("########## FILE ALREADY PROCESSED");
                sqlMap.executeUpdate("updateRTGSAckFileReceivedCount", fileMap);
            } else {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                    neftTO = new NeftRtgsAcknowledgementTO();
                    detailMap = new HashMap();
                    String[] parts = line.split("\\~");
                    for (int i = 0; i < parts.length; i++) {
                        String data = CommonUtil.convertObjToStr(parts[i]);
                        if (i == 0) {//RECORD_IDENTIFIER 
                            continue;
                        }
                        if (i == 1) {//PAYMENT_INDICATOR       
                            continue;
                        }
                        if (i == 2) {//UTR_NUMBER(UNIQUR REF NO) 
                            inwardUTR = data;
                            continue;
                        }
                        if (i == 3) {//BENE_CODE
                            continue;
                        }
                        if (i == 4) {//BENE_NAME
                            continue;
                        }
                        if (i == 5) {//AMOUNT
                            continue;
                        }
                        if (i == 6) {//DATE
                            continue;
                        }
                        if (i == 7) {//DEBIT A/C NO
                            continue;
                        }
                        if (i == 8) {//BENE_ACCOUNT_NO
                            continue;
                        }
                        if (i == 9) {//IFSC_CODE
                            continue;
                        }
                        if (i == 10) {//BENE_BANK_NAME
                            status = data;
                            continue;
                        }
                        if (i == 11) {//BENE_EMAIL_ID                          
                            if (parts.length > 12) {
                                continue;
                            }
                        }
                        if (i == 12) {//BENE_MOBILE                 
                        }
                        
                        //If Condition added Based on Mail from Jithesh (16-Apr-2018)
                        if (CommonUtil.convertObjToStr(status).equals("SUCCESS")||CommonUtil.convertObjToStr(status).equalsIgnoreCase("Cancelled")) {
                            sqlMap.startTransaction();
                            System.out.println("############ inwardUTR  : " + inwardUTR + "   ############ status     : " + status);
                            if (CommonUtil.convertObjToStr(status).equals("SUCCESS")) {//PASS - NO TRANSACTION, ONLY STATUS UPDATE AS PASS
                                detailMap.put("UTR_NUMBER", inwardUTR);
                                detailMap.put("FILE_STATUS", "PASS");
                            } else {//FAIL - DO TRANSACTION AND STATUS UPDATE AS FAIL
                                detailMap.put("UTR_NUMBER", inwardUTR);
                                detailMap.put("FILE_STATUS", "FAIL");
                                List lst = sqlMap.executeQueryForList("getRTGSRemittransDetailsTO", detailMap);
                                if (lst != null && lst.size() > 0) {
                                    remitTo = (RtgsRemittanceTO) lst.get(0);
                                    detailMap.put("BATCH_DT", remitTo.getBatchDt());
                                    detailMap.put("RTGS_ID", remitTo.getRtgs_ID());
                                    lst = sqlMap.executeQueryForList("getCreditAcctDetails", detailMap);
                                    if (lst != null && lst.size() > 0) {
                                        creditAcctMap = (HashMap) lst.get(0);
                                        neftTO.setAcctNum(CommonUtil.convertObjToStr(creditAcctMap.get("DEBIT_ACCT_NO")));
                                        neftTO.setAckDate((Date) currDt.clone());
                                        neftTO.setAmount(remitTo.getAmount());
                                        neftTO.setHomeIfsCode(remitTo.getIfsc_Code());
                                        neftTO.setOtherBrankIfsCode(remitTo.getBeneficiary_IFSC_Code());
                                        neftTO.setOtherBankAcctNum(remitTo.getAccount_No());
                                        neftTO.setOtherBankCustName(remitTo.getBeneficiary_Name());
                                        neftTO.setInitBranchId(remitTo.getInitiatedBranch());
                                        neftTO.setAcctStatus(status);
                                        neftTO.setInwardUTR(remitTo.getUtrNumber());
                                        acHeads.put("CREDIT_ACT_NUM", neftTO.getAcctNum());
                                        acHeads.put("CREDIT_PROD_ID", CommonUtil.convertObjToStr(creditAcctMap.get("PROD_ID")));
                                        acHeads.put("CREDIT_PROD_TYPE", CommonUtil.convertObjToStr(creditAcctMap.get("PRODUCT_TYPE")));
                                        acHeads.put("CREDIT_BRANCH_ID", CommonUtil.convertObjToStr(creditAcctMap.get("BRANCH_CODE")));
                                        detailMap.put("PROD_ID", remitTo.getProdId());
                                        lst = sqlMap.executeQueryForList("getAcctHeadforAcct", detailMap);
                                        if (lst != null && lst.size() > 0) {
                                            creditAcctMap = (HashMap) lst.get(0);
                                            acHeads.put("CREDIT_ACCT_HEAD_ID", CommonUtil.convertObjToStr(creditAcctMap.get("ISSUE_HD")));
                                        }
                                        acHeads.put("CREDIT_AMT", neftTO.getAmount());
                                        if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("NEFT")) {
                                            acHeads.put("NEFT", "NEFT");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("RTGS")) {
                                            acHeads.put("RTGS", "RTGS");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("IMPS")) {
                                            acHeads.put("IMPS", "IMPS");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("ICICI_FT")) {
                                            acHeads.put("ICICI_FT", "ICICI_FT");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("UPI")) { //Added by nithya on 20-09-2022 fro KD-3471   
                                            acHeads.put("NEFT", "NEFT");
                                        } else if (CommonUtil.convertObjToStr(remitTo.getProdId()).equals("FT")) { //Added by nithya on 03-02-2025 for KD-3842     
                                            acHeads.put("NEFT", "NEFT");
                                        }
                                        System.out.println("############################### readSBIData_From_Outward_ReturnFile() : ");
                                        dotransaction(acHeads, neftTO);
                                    }
                                }
                            }
                            if (detailMap != null && detailMap.size() > 0) {
                                sqlMap.executeUpdate("updateOutwardResponseFileStatus", detailMap);
                            }
                            sqlMap.commitTransaction();
                        }
                    }
                    if (fileNameInsert) {
                        sqlMap.startTransaction();
                        fileMap = new HashMap();
                        fileMap.put("FILE_NAME", file.getName());
                        fileMap.put("APPL_DT", currDt.clone());
                        fileMap.put("COUNT", CommonUtil.convertObjToInt(1));
                        sqlMap.executeUpdate("insertRTGSAckFileName", fileMap);
                        sqlMap.commitTransaction();
                        fileNameInsert = false;
                    }
                }
                fis.close();
                bis.close();
                dis.close();
            }
            //FILE MOVED TO BACKUP FOLDER
            String sourceFilePath = CommonConstants.RTGS_NEFT_INWARD_PATH + file.getName();
            String destinationFilePath = CommonConstants.RTGS_NEFT_INWARD_BACKUP_PATH + "OUTWARD_RES_" + generateRandomNumbers()+"_"+ file.getName() + ".Done";
            File file1 = new File(sourceFilePath);
            file1.renameTo(new File(destinationFilePath));
            System.out.println("############# FILE MOVED COMPLETED : " + destinationFilePath + "  ##### file.getName() : " + file.getName());
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            System.out.println("############ OUTWARD_ACK ERROR : ");
            throw e;
        }
    }      
    
    
}
