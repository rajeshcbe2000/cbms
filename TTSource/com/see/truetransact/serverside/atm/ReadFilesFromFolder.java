/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ReadFilesFromFolder.java
 */

package com.see.truetransact.serverside.atm;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
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
import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import com.see.truetransact.transferobject.transaction.ATMTrans.ATMAcknowledgementTO;
import com.see.truetransact.transferobject.transaction.ATMTrans.SponsorBankDetailsTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Sathiya
 */
public class ReadFilesFromFolder {

    public File folder = new File("E:/MDCCB/Outward/NEFT/N06");
    private static String temp = "";
    private static SqlMap sqlMap = null;
    ATMAcknowledgementTO atmTO = new ATMAcknowledgementTO();
    String debitbranchID = "";
    Date debitBranchcurrDt = null;
    Date creditBranchcurrDt = null;
    String type = "";
    boolean readN02orR41_R42 = false;
    private TransactionDAO transactionDAO = null;
    private boolean isInwardExcepRR42 = false;
    private int constants = 2;
    private HashMap headOffMap = null;
    String branchID = "";
    Date currDt = null;
    
    public ReadFilesFromFolder(String type, String path) throws ServiceLocatorException, Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        folder = new File(path);
        this.type = type;
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public static void main(String[] args) throws Exception {
// TODO Auto-generated method stub
// System.out.println("Reading files under the folder "+ folder.getAbsolutePath());
// listFilesForFolder(folder);
    }

    public void listFilesForFolder(final File folder, SqlMap sMap, String debitCredit) throws ServiceLocatorException, Exception {
        sqlMap = sMap;
        System.out.println("listFilesForFolder : " + folder + " type : " + type);
        branchID = "0001";
        currDt = ServerUtil.getCurrentDate(branchID);
//        reportGenerationSpecifiedTime();
//        writeTransactionReportATMFile();
        createTxntoInsufficientATMBalanceActs();
        writeTOPUPATMFile();
        if (folder.isDirectory() && folder.list().length > 0) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
// System.out.println("Reading files under the folder "+folder.getAbsolutePath());
                    listFilesForFolder(fileEntry, sMap, debitCredit);
                } else {
                    if (fileEntry.isFile()) {
                        temp = fileEntry.getName();
                        if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("txt")) {
//                        System.out.println("list file for folder File    = " + folder.getAbsolutePath() + "/" + fileEntry.getName());
                        }
                        if (type.equals("TRANSACTION")) {// transaction triggered from ATM or POS AND FILE WILL COME FROM Sponsor BANK 
                            readTransDataFromSponsorBank(fileEntry);
                        }
                        if (type.equals("TOPUP_DEBIT_TOPUP_ACK")) {// topup debit topup acknowledgement transaction
                            readDataforTopupDebitAck(fileEntry, debitCredit);
                        }
                        if (type.equals("STOP_REVOKE")) {
                            readDataForUpdateStopRevokeStatus(fileEntry, debitCredit);
                        }
                    }
                }
            }
        } else {
            System.out.println("Files not exist in this Directory...");
        }
    }

    //OUTPUT
//Reading files under the folder E:\MDCCB\Inward\NEFT\F27
    private void readDataforTopupDebitAck(File file, String debitCredit) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        boolean n06Return = false, n02return = false, first2020 = false, n09file = false;
        String tranfile = "", topup = "";
        HashMap acHeads = new HashMap();
        String fileName = file.getName();
        ATMAcknowledgementTO atmTO = new ATMAcknowledgementTO();
        String bit = "", status = "", remarks = "", act_num = "", card_Act_num = "", debitAcctHeadId = "", inwardUTR = "", otherBankAcctNum = "";
        double transAmt = 0;
        String line = "", fileact_num = "", creditAcctHeadId = "", creditAcctNumber = "", creditprodId = "", creditProdType = "", seqenceno = "", seqNo = "", headOfficeBranchId = "";
        HashMap detailMap = new HashMap();
        HashMap creditAcctMap = new HashMap();
        topup = fileName.substring(15, 20);
        System.out.println("readDataforTopupDebitTopup topup : " + topup);
        if (topup.equals("topupdebt")) {
            seqenceno = fileName.substring(38, 44);
        } else if (topup.equals("topup")) {
            seqenceno = fileName.substring(34, 40);
        }
        List list = (List) sqlMap.executeQueryForList("getPanAmount", null);
        if (list != null && list.size() > 0) {
            headOffMap = (HashMap) list.get(0);
            headOfficeBranchId = CommonUtil.convertObjToStr(headOffMap.get("HEAD_OFFICE"));
        }
        if (dis != null) {
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                if (line.length() <= 5) {
                    continue;
                }
                if (line.length() >= 5) {
                    card_Act_num = CommonUtil.convertObjToStr(line.substring(0, 35));
                    transAmt = CommonUtil.convertObjToDouble(line.substring(36, 52)).doubleValue();
                    bit = CommonUtil.convertObjToStr(line.substring(53, 54));
                    remarks = CommonUtil.convertObjToStr(line.substring(56, line.length() - 1));
                    transAmt = transAmt / 100;
                    acHeads.put("CARD_ACCT_NUM", card_Act_num);
                    acHeads.put("SEQUENCE_NO", seqenceno);
                    List mapDataList = sqlMap.executeQueryForList("getSelectATMAcknowledgementTO", acHeads);
                    if (mapDataList != null && mapDataList.size() > 0) {
                        atmTO = (ATMAcknowledgementTO) mapDataList.get(0);
                        atmTO.setTran_status(bit);
                        atmTO.setRemarks(remarks);
                        status = bit;
                        if (bit.equals("F")) {
                            String errorCode = CommonUtil.convertObjToStr(line.substring(61, 65));
                            n02return = true;
                            acHeads.put("DEBIT_ACCT_NUM", atmTO.getAcctNum());
                            acHeads.put("PROD_ID", (atmTO.getAcctNum().substring(4, 7)));
                            acHeads.put("ACT_NUM", atmTO.getAcctNum());
                            debitbranchID = CommonUtil.convertObjToStr(atmTO.getAcctNum()).substring(0, 4);
                            debitBranchcurrDt = ServerUtil.getCurrentDate(debitbranchID);
                            mapDataList = sqlMap.executeQueryForList("getAcctHeadforAcct", acHeads);
                            if (mapDataList != null && mapDataList.size() > 0) {
                                detailMap = (HashMap) mapDataList.get(0);
                                debitAcctHeadId = CommonUtil.convertObjToStr(detailMap.get("AC_HD_ID"));
                                acHeads.put("DEBIT_PROD_ID", detailMap.get("PROD_ID"));
                                acHeads.put("DEBIT_ACCT_HEAD_ID", debitAcctHeadId);
                                atmTO.setInitiatedBranch(headOfficeBranchId);
                            }
                            acHeads.put("CARD_ACCT_NUM", card_Act_num);
                            mapDataList = sqlMap.executeQueryForList("getCardAcctDetails", acHeads);
                            if (mapDataList != null && mapDataList.size() > 0) {
                                detailMap = (HashMap) mapDataList.get(0);
                                String creditActNum = CommonUtil.convertObjToStr(detailMap.get("LINKING_ACT_NUM"));
                                atmTO.setInitiatedBranch(headOfficeBranchId);
                                acHeads.put("PROD_ID", (creditActNum.substring(4, 7)));
                                mapDataList = sqlMap.executeQueryForList("getAcctHeadforAcct", acHeads);
                                if (mapDataList != null && mapDataList.size() > 0) {
                                    detailMap = (HashMap) mapDataList.get(0);
                                    creditAcctHeadId = CommonUtil.convertObjToStr(detailMap.get("AC_HD_ID"));
                                    acHeads.put("CREDIT_ACCT_NUM", creditActNum);
                                    acHeads.put("CREDIT_PROD_ID", detailMap.get("PROD_ID"));
                                    acHeads.put("CREDIT_ACCT_HEAD_ID", creditAcctHeadId);
                                }
                            }
                            if (debitCredit.equals("TOPUP")) {
//                                doTransaction(acHeads, atmTO);
//                            } else {
                                doDebitTransaction(acHeads, atmTO);
                            }
                            if(errorCode != null && errorCode.length()>0 && (errorCode.equals("0550") || errorCode.equals("1804"))){
                                HashMap errorCodeMap = new HashMap();
                                errorCodeMap.put("ACT_NUM", atmTO.getAcctNum());
                                sqlMap.executeUpdate("updateSposnerBankErrorStatus", errorCodeMap);                                
                            }
                        } else {
                            System.out.println("file is success : " + acHeads);
                        }
                        sqlMap.executeUpdate("UPDATEAtmAcknowledgement", atmTO);
                    }
                }
            }
            remarks = "ACCOUNT NOT EXIST";
            fis.close();
            bis.close();
            dis.close();
            tranfile = String.valueOf(CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_BACKUP_INWARD_TOPUP_ACK) + file.getName() + ".Done");
            System.out.println("readDataforTopupDebitTopup : " + tranfile + "file.getName() " + file.getName());
            file.renameTo(new File(tranfile));
        }
    }

    private void readDataForUpdateStopRevokeStatus(File file, String debitCredit) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        boolean n06Return = false, n02return = false, first2020 = false, n09file = false;
        String tranfile = "";
        HashMap acHeads = new HashMap();
        String fileName = file.getName();
        ATMAcknowledgementTO atmTO = new ATMAcknowledgementTO();
        String bit = "", status = "", remarks = "", act_num = "", card_Act_num = "", debitAcctHeadId = "", inwardUTR = "", otherBankAcctNum = "";
        double transAmt = 0;
        String line = "", fileact_num = "", creditAcctHeadId = "", creditAcctNumber = "", creditprodId = "", creditProdType = "", seqenceno = "", seqNo = "", headOfficeBranchId = "";
        HashMap detailMap = new HashMap();
        HashMap creditAcctMap = new HashMap();
        seqenceno = fileName.substring(35, 41);
        if (dis != null) {
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                if (line.length() >= 5) {
                    card_Act_num = CommonUtil.convertObjToStr(line.substring(0, 35));
                    bit = CommonUtil.convertObjToStr(line.substring(36, 37));
                    remarks = CommonUtil.convertObjToStr(line.substring(38, line.length() - 1));
                    acHeads.put("CARD_ACCT_NUM", card_Act_num);
                    acHeads.put("SEQUENCE_NO", seqenceno);
                    List mapDataList = sqlMap.executeQueryForList("getSelectATMAcknowledgementTO", acHeads);
                    if (mapDataList != null && mapDataList.size() > 0) {
                        atmTO = (ATMAcknowledgementTO) mapDataList.get(0);
                        atmTO.setTran_status(bit);
                        atmTO.setRemarks(remarks);
                        status = bit;
                    }
                    if (bit.equals("F")) {
                        n02return = true;
                        acHeads.put("DEBIT_ACCT_NUM", atmTO.getAcctNum());
                        acHeads.put("PROD_ID", (atmTO.getAcctNum().substring(4, 7)));
                        acHeads.put("ACT_NUM", atmTO.getAcctNum());
                        debitbranchID = CommonUtil.convertObjToStr(atmTO.getAcctNum()).substring(0, 4);
                        debitBranchcurrDt = ServerUtil.getCurrentDate(debitbranchID);
//                    System.out.println("acHeads######" + acHeads);
                        mapDataList = sqlMap.executeQueryForList("getAcctHeadforAcct", acHeads);
                        detailMap = (HashMap) mapDataList.get(0);
                        debitAcctHeadId = CommonUtil.convertObjToStr(detailMap.get("AC_HD_ID"));
                        acHeads.put("DEBIT_PROD_ID", detailMap.get("PROD_ID"));
                        acHeads.put("DEBIT_ACCT_HEAD_ID", debitAcctHeadId);
                        atmTO.setInitiatedBranch(headOfficeBranchId);
                        if (debitCredit.equals("TOPUP")) {
                            doTransaction(acHeads, atmTO);
                        } else {
                            doDebitTransaction(acHeads, atmTO);
                        }
                    } else {
                        System.out.println("stop fies are processed : ");
                    }
                }
            }
            remarks = "ACCOUNT NOT EXIST";
            sqlMap.executeUpdate("UPDATEAtmAcknowledgement", atmTO);
            fis.close();
            bis.close();
            dis.close();
            tranfile = String.valueOf(CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_BACKUP_INWARD_STOP_ACK) + file.getName() + ".Done");
            System.out.println("readDataForUpdateStopRevokeStatus : " + tranfile + "file.getName() " + file.getName());
            file.renameTo(new File(tranfile));
        }
    }

//OUTPUT
// sponcer bank transaction file ex debit Sponsor bank customer account under the folder E:/KKC/Inward/ATM/TRANSACTION/
    private void readTransDataFromSponsorBank(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        boolean n06Return = false, n02return = false, first2020 = false;
        String fileName = file.getName();
        HashMap acHeads = new HashMap();
        ATMAcknowledgementTO atmTO = new ATMAcknowledgementTO();
        String customerName = "", status = "", remarks = "", card_Act_num = "", homeBranchIfscode = "", otherBranchIfscode = "",
                acctCount = "", otherBankAcctNum = "";
        double transAmt = 0, chargeAmt = 0;
        String line = "", rrn = "", debitAcctHeadId = "", seqenceno = "", station_id = "", headOfficeBranchId = "";
        Date postDt = null;
        String mop = "";
        String transStatus = "";
        String trncode = "";
        String postTime = "";
        String strdt = "";
        String netWorkType = "";
        String messageType = "";
        long recordCount = 1;
        HashMap cardStatusMap = new HashMap();
        HashMap map = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getPanAmount", null);
        if (list != null && list.size() > 0) {
            headOffMap = (HashMap) list.get(0);
            headOfficeBranchId = CommonUtil.convertObjToStr(headOffMap.get("HEAD_OFFICE"));
        }
        while (dis.available() > 0 && (line = dis.readLine()).length() > 0 && recordCount > 0) {
            atmTO = new ATMAcknowledgementTO();
            if (line.length() <= 5) {
//                System.out.println("line.substring(0, 1)" + line.substring(0, 1));
                acctCount = line.substring(0, 5);
                recordCount = Long.parseLong(acctCount);
//                System.out.println("line.substring(0, 1)" + line.substring(0, 1) + "recordCount####" + recordCount);
                continue;
            }
            if (line.length() >= 5) {
//n06Return=true; if (line.substring(1, 5).equals("5561")) {
                seqenceno = fileName.substring(36, 42);
                card_Act_num = CommonUtil.convertObjToStr(line.substring(0, 35));
//                System.out.println("card_Act_num : " + card_Act_num);
                transAmt = CommonUtil.convertObjToDouble(line.substring(36, 52)).doubleValue();
//                System.out.println("beforetransAmt" + transAmt + "diviide" + transAmt / 100 + "module" + transAmt % 100 + "acctCount###" + acctCount + "recordCount$$$$" + recordCount);
                transAmt = transAmt / 100;
                //rrn is 12 digit
                rrn = CommonUtil.convertObjToStr(line.substring(53, 65));
                //station id is 16 digit
                station_id = CommonUtil.convertObjToStr(line.substring(66, 82));
                //Posting date (ddmmyyyy)
                strdt = CommonUtil.convertObjToStr(line.substring(83, 91));
                postDt = new Date();

                //Posting time (8) hhmmssss
                postTime = CommonUtil.convertObjToStr(line.substring(92, 100));
                //Trancode (6) (PACS needs to map trancode mapping i.e 4060 -
                //withdrawal,8160-reversal.This mapping will be provided by CEDGE)
                trncode = CommonUtil.convertObjToStr(line.substring(101, 107));

                //MESSAGE TYPE (4)
                // (h.1)In case of Topup and TopupDebit "FUND" message is printed in the Transaction File.
                messageType = CommonUtil.convertObjToStr(line.substring(108, 112));

                //NETWORK TYPE (3)
                netWorkType = CommonUtil.convertObjToStr(line.substring(113, 116));

                // •	MOP(3) PAC or ATM:For All Transactions MOP is PAC or ATM except POS transaction via debit cards
                mop = CommonUtil.convertObjToStr(line.substring(117, 120));

                //•	Transaction Status(4)
                //k.1)PROS:In case of Non-reversal Transactions the Transaction Status is Printed as 'PROS'.
                //(k.2)RVRS:In case of Reversal Transactions the Transaction Status is Printed as 'RVRS'.
                transStatus = CommonUtil.convertObjToStr(line.substring(121, 125));

                //Charges bit (1/0) (CBS will store charge bit in its table)
                String chargeBit = CommonUtil.convertObjToStr(line.substring(126, 127));
                //Charge Amount(16)(in paisa) (left padded zeroes)

                chargeAmt = CommonUtil.convertObjToDouble(line.substring(128, 144)).doubleValue();
                atmTO.setChargeBit(chargeBit);
                if (chargeBit.equals("1")) {
                    atmTO.setChargeAmt(String.valueOf(chargeAmt / 100));
                } else {
                    atmTO.setChargeAmt(String.valueOf(0));
                }
                //act_num = CommonUtil.convertObjToStr(line.substring(9, line.length() - 2));
                System.out.println("File Read Line : " + line + line.substring(1, 5) + " fileName : " + fileName);
                System.out.println(" seqenceno : " + seqenceno + " card_Act_num : " + card_Act_num + " AMOUNT : " + line.substring(37, 53)
                        + " AFTER DIVIDE transAmt : " + transAmt + " rrn : " + rrn + " station_id : " + station_id + " postDtstrdt : " + strdt);
                System.out.println("postTime : " + postTime + " trncode : " + trncode + " messageType : " + messageType + " netWorkType : "
                        + netWorkType + " mop : " + mop + " chargeBit : " + chargeBit + " chargeAmt : " + chargeAmt);
                acHeads.put("CARD_ACCT_NUM", card_Act_num);
                //  acHeads.put("ACC_NUM", act_num);
            }
            if (trncode != null && trncode.length() > 0 && (trncode.equals("004060") || trncode.equals("004061"))) {//ATM withdrawal & Pos transactions
                HashMap existorNotMap = new HashMap();
                existorNotMap.put("SEQUENCE_NO", seqenceno);
                existorNotMap.put("RRN", rrn);
                List recordExistorNot = sqlMap.executeQueryForList("getSequencNoRRNExistorNot", existorNotMap);
                if (recordExistorNot != null && recordExistorNot.size() > 0) {
                    System.out.println("Record exist with same Sequence No : " + seqenceno + " RRNumber : " + rrn);
                } else {
                    List mapDataList = sqlMap.executeQueryForList("getCardAcctDetails", acHeads);
                    if (mapDataList == null || mapDataList.isEmpty()) {
                        n02return = true;
                        status = "F";
                    } else if (mapDataList != null && mapDataList.size() > 0) {
                        map = (HashMap) mapDataList.get(0);
                        status = "P";
                        n02return = false;
                        acHeads.put("DEBIT_ACCT_NUM", map.get("ACT_NUM"));
                        debitbranchID = CommonUtil.convertObjToStr(map.get("ACT_NUM")).substring(0, 4);
                        debitBranchcurrDt = ServerUtil.getCurrentDate(debitbranchID);
                        mapDataList = sqlMap.executeQueryForList("getAcctHeadforAcct", map);
                        map = (HashMap) mapDataList.get(0);
                        debitAcctHeadId = CommonUtil.convertObjToStr(map.get("AC_HD_ID"));
                        acHeads.put("DEBIT_PROD_ID", map.get("PROD_ID"));
                        acHeads.put("DEBIT_ACCT_HEAD_ID", debitAcctHeadId);
                    }
                    atmTO.setAcctNum(CommonUtil.convertObjToStr(acHeads.get("DEBIT_ACCT_NUM"))); //DEBIT ACCOUNT NUMBER
                    atmTO.setTransDt(debitBranchcurrDt); //APPLICATION DATE
                    atmTO.setAmount(new Double(transAmt));//TRANSACTION AMOUNT
                    atmTO.setCardAcctNum(card_Act_num);//CUSTOMER CARD ACCOUNT NUMBER
                    atmTO.setInitiatedBranch(headOfficeBranchId); //INITIATED BRANCH
                    atmTO.setTransType("DEBIT");  //TRANS TYPE EITHER DEBIT OR CREDIT
                    atmTO.setStatus(transStatus);  //PROS  OR RVRS
                    atmTO.setSequenceNo(seqenceno); // FILE SEQUENCE NUMBER
                    atmTO.setTran_status(status); // TRANSACTION STATUS P OR F
                    atmTO.setAtmType(messageType);
                    atmTO.setStation_code(station_id);  //ATM STATION ID
                    atmTO.setRrn(rrn);  //ATM RRN
                    atmTO.setMop(mop);  // PAC OR ATM
                    atmTO.setError_code(trncode);
                    atmTO.setAtmDtStr(strdt);
                    atmTO.setNetWorkType(netWorkType);
                    atmTO.setAtm_postTime(postTime);
                    //atmTO.setAtmType(type);
                    acHeads.put("NEFT", "NEFT");
                    //            System.out.println("branchID : " + debitbranchID + "currDt : " + headOfficeBranchId + "atmTO : " + atmTO + "acHeads : " + acHeads);
                    if (!n02return) {
                        remarks = "ACCOUNT EXIST";
                        String transId = "";
                        if (trncode != null && trncode.length() > 0 && (trncode.equals("004060") || trncode.equals("004061"))) {//ATM withdrawal & Pos transactions
                            transId = CommonUtil.convertObjToStr(doTransaction(acHeads, atmTO).get("TRANS_ID"));
                        }else if(trncode != null && trncode.length() > 0 && (trncode.equals("008160") || trncode.equals("004161"))){//ATM withdrawal Reversal Transaction via ATM machines && POS Reversal Transaction via Debit card.
                            transId = CommonUtil.convertObjToStr(doDebitTransaction(acHeads, atmTO).get("TRANS_ID"));
                        }
                        atmTO.setTransId(transId);
                    }
                    sqlMap.executeUpdate("insertATMAcknowledgementTO", atmTO);
                }
                --recordCount;
            }
        }
        if (CommonUtil.convertObjToStr(acctCount).length() > 0) {
            atmAcknowledgeMentFileCreation(seqenceno, acctCount);
        }
        fis.close();
        bis.close();
        dis.close();
        String tranfile = String.valueOf(CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_BACKUP_INWARD_TRANSACTION) + file.getName() + ".Done");
        file.renameTo(new File(tranfile));
        System.out.println("single tranfile : " + tranfile + " file.getName() " + file.getName());
    }

// do the actual transaction and credit into our customer account 
    private HashMap doTransaction(HashMap acHeads, ATMAcknowledgementTO atmTO) throws Exception {
        TxTransferTO objTxTransferTO = new TxTransferTO();
        TransferTrans trans = new TransferTrans();
        String userid = "", useridAuth = "";
        double charges = CommonUtil.convertObjToDouble(atmTO.getChargeAmt()).doubleValue();

        trans.setInitiatedBranch(atmTO.getInitiatedBranch());  // hard coded
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        HashMap txMap = new HashMap();
        String actNum = CommonUtil.convertObjToStr(acHeads.get("DEBIT_ACCT_NUM"));
        double transAmt = CommonUtil.convertObjToDouble(acHeads.get("CREDIT_AMT")).doubleValue();
//        System.out.println("transAmt#$#$" + transAmt + "actNum@#$%" + actNum + "acHeads" + acHeads + "atmTO#$$$##  " + atmTO);
//  String generateLinkIdrise=generateLinkID(actNum);
        HashMap creditMap = new HashMap();
        userid = "ATM";
        useridAuth = "ATM_USER";
        TxTransferTO transferTo = new TxTransferTO();
        List creditList = (List) sqlMap.executeQueryForList("getAccountClosingHeads", actNum);
        if (creditList != null && creditList.size() > 0) {
            creditMap = (HashMap) creditList.get(0);
        }
        ArrayList transferList = new ArrayList(); // for local transfer
        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_ACCT_HEAD_ID"));
        txMap.put(TransferTrans.DR_BRANCH, debitbranchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
        txMap.put(TransferTrans.DR_ACT_NUM, actNum);
        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(acHeads.get("DEBIT_PROD_ID")));
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
        txMap.put(CommonConstants.USER_ID, userid);
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put("AUTHORIZEREMARKS", "ATM_REVERSE_TRANSACTION");
        txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
        txMap.put("PARTICULARS", "ATM TRANSACTION");
        objTxTransferTO = trans.getDebitTransferTO(txMap, atmTO.getAmount().doubleValue() + charges);
        transferList.add(objTxTransferTO);

        //  txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(acHeads.get("CREDIT_PROD_ID")));
        txMap.put(TransferTrans.CR_AC_HD, creditMap.get("ATM_GL"));//CommonUtil.convertObjToStr(acHeads.get("CREDIT_ACCT_HEAD_ID")));
        // txMap.put(TransferTrans.CR_ACT_NUM, actNum);
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.CR_BRANCH, atmTO.getInitiatedBranch());
        txMap.put(CommonConstants.USER_ID, userid);
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put("PARTICULARS", "ATM TRANSACTION");
        txMap.put("AUTHORIZEREMARKS", "ATM_REVERSE_TRANSACTION");//atm transaction  and debit topup debit  reverse transaction acknowledgement use "ATM_REVERSE_TRANSACTION"
        txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
        objTxTransferTO = trans.getCreditTransferTO(txMap, atmTO.getAmount().doubleValue() + charges);
        transferList.add(objTxTransferTO);

        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", transferList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("LINK_BATCH_ID", actNum);
        data.put("INITIATED_BRANCH", atmTO.getInitiatedBranch());
        data.put(CommonConstants.BRANCH_ID, atmTO.getInitiatedBranch());
        data.put(CommonConstants.USER_ID, useridAuth);
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "INSERT");

        transferDAO.setForLoanDebitInt(false);
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
//        System.out.println("data###" + data);
        HashMap transDetMap = transferDAO.execute(data, false);
        return transDetMap;
    }

    private HashMap doDebitTransaction(HashMap acHeads, ATMAcknowledgementTO atmTO) throws Exception {
        TxTransferTO objTxTransferTO = new TxTransferTO();
        TransferTrans trans = new TransferTrans();
        String userid = "", useridAuth = "";
        double charges = CommonUtil.convertObjToDouble(atmTO.getChargeAmt()).doubleValue();

        trans.setInitiatedBranch(atmTO.getInitiatedBranch());  // hard coded
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        HashMap txMap = new HashMap();
        String actNum = CommonUtil.convertObjToStr(acHeads.get("DEBIT_ACCT_NUM"));
        String creditActNum = CommonUtil.convertObjToStr(acHeads.get("CREDIT_ACCT_NUM"));
        double transAmt = CommonUtil.convertObjToDouble(acHeads.get("CREDIT_AMT")).doubleValue();
        System.out.println("transAmt#$#$" + transAmt + "actNum@#$%" + actNum + "acHeads : " + acHeads + "atmTO#$$$##  " + atmTO);
//  String generateLinkIdrise=generateLinkID(actNum);
//        HashMap creditMap = new HashMap();
        userid = "ATM";
        useridAuth = "ATM_USER";
//        TxTransferTO transferTo = new TxTransferTO();
//        List creditList = (List) sqlMap.executeQueryForList("getAccountClosingHeads", actNum);
//        if (creditList != null && creditList.size() > 0) {
//            creditMap = (HashMap) creditList.get(0);
//        }
        ArrayList transferList = new ArrayList(); // for local transfer
        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_ACCT_HEAD_ID"));
        txMap.put(TransferTrans.DR_BRANCH, debitbranchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
        txMap.put(TransferTrans.DR_ACT_NUM, actNum);
        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(acHeads.get("DEBIT_PROD_ID")));
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
        txMap.put(CommonConstants.USER_ID, userid);
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put("AUTHORIZEREMARKS", "ATM REVERSE TRANSACTION");
        txMap.put("PARTICULARS", "ATM TRANSACTION");
        txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
        objTxTransferTO = trans.getDebitTransferTO(txMap, atmTO.getAmount().doubleValue() + charges);
        transferList.add(objTxTransferTO);

        txMap = new HashMap();
        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHeads.get("CREDIT_ACCT_HEAD_ID")));
        txMap.put(TransferTrans.CR_ACT_NUM, creditActNum);
        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(acHeads.get("CREDIT_PROD_ID")));
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
        txMap.put(TransferTrans.CR_BRANCH, atmTO.getInitiatedBranch());
        txMap.put(CommonConstants.USER_ID, userid);
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put("PARTICULARS", "ATM TRANSACTION");
        txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
        objTxTransferTO = trans.getCreditTransferTO(txMap, atmTO.getAmount().doubleValue() + charges);
        transferList.add(objTxTransferTO);

        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", transferList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("LINK_BATCH_ID", actNum);
        data.put("INITIATED_BRANCH", atmTO.getInitiatedBranch());
        data.put(CommonConstants.BRANCH_ID, atmTO.getInitiatedBranch());
        data.put(CommonConstants.USER_ID, useridAuth);
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "INSERT");

        transferDAO.setForLoanDebitInt(false);
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        System.out.println("data###" + data);
        HashMap transDetMap = transferDAO.execute(data, false);
        return transDetMap;
    }
    //N10 file for NEFT

    private void atmAcknowledgeMentFileCreation(String sourceFileSequenceNo, String acctCount) throws Exception {
        HashMap dataMap = new HashMap();
        DateFormat dateFormat = new SimpleDateFormat("ddmmyyyyhhmm");
        Calendar calender = Calendar.getInstance();
        String accountNotExist = "ACCOUNT DOES NOT EXIST";
        Date dt = new Date();
        String path = "";
        try {
//            System.out.println("atmAcknowledgeMentFileCreation acctCount : " + acctCount + " dateFormat : " + dateFormat.format(calender.getTime()) + " sourceFileSequenceNo : " + sourceFileSequenceNo);
//            path = "E:/KKC/Outward/ATM/Transaction_Ack/" + CommonConstants.PACS_ID + "_Txn_" + dateFormat.format(calender.getTime()) + "_" + sourceFileSequenceNo + "_" + "res" + ".txt";
            path = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_OUTWARD_TRANSACTION_ACK) + CommonConstants.PACS_ID + "_Txn_" + dateFormat.format(calender.getTime()) + "_" + sourceFileSequenceNo + "_" + "res" + ".txt";
            java.io.FileWriter write = new java.io.FileWriter(path, false);
            java.io.PrintWriter print_line = new java.io.PrintWriter(write);
            print_line.print(acctCount);
            if (print_line != null) {
                print_line.close();
            }
            if (write != null) {
                write.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private String getCedgeUniqueATMID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "CEDGE_ATMID");
        String rtgs_ID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return rtgs_ID;
    }

    public boolean isIsInwardExcepRR42() {
        return isInwardExcepRR42;
    }

    public void setIsInwardExcepRR42(boolean isInwardExcepRR42) {
        this.isInwardExcepRR42 = isInwardExcepRR42;
    }

    private void writeTOPUPATMFile() throws Exception {
        SponsorBankDetailsTO sponsorBankDetailsTO = new SponsorBankDetailsTO();
//        HashMap dataMap = new HashMap();
        DateFormat years2digit = new SimpleDateFormat("yy");
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
        DateFormat dateFormatdate = new SimpleDateFormat("ddMMyyyy");
        DateFormat dateFormathhmmss = new SimpleDateFormat("hhmmssss");
//        DateFormat dtFormtWithOutMin = new SimpleDateFormat("yyyyMMdd");
//        DateFormat dtMiddleWithOutMin = new SimpleDateFormat("yyyyddMM");
//        DateFormat minOnly = new SimpleDateFormat("hhmm");
        Calendar calender = Calendar.getInstance();
        dateFormat.format(calender.getTime());

        Date dt = new Date();
        String pathDr, pathDrBackup = "";
        String pathCr, pathCrBackup = "";
//        String transAmt = "";
        String pipeLine = "|";//{:
//        String closeBrace = "}";//}:
//        String mandatoryField = "^M";
        int daysInYear = 0, amtcount = 16, actualamtcnt = 0;
        String neftUniqueId = "", neftgenId = "", sequnceNum = "";
        String cardAcctNum = "", cardOrgNo = "";
//        System.out.println("writeTOPUPATMFile : " + "dtFormtWithMin" + dateFormat.format(calender.getTime()));
        List lst = sqlMap.executeQueryForList("getSponsorBankNotProcessedRecord", null);
        if (lst != null && lst.size() > 0) {
            java.io.FileWriter writeDr = null;
            java.io.PrintWriter printWriterDr = null;
            java.io.FileWriter writeCr = null;
            java.io.PrintWriter printWriterCr = null;
            boolean flagDr = false;
            boolean flagCr = false;
            neftgenId = getCedgeUniqueATMID();
            pathDr = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_OUTWARD_TOPUP) + CommonConstants.PACS_ID + "_topupdebt_" + dateFormat.format(calender.getTime()) + "_" + neftgenId.substring(4, neftgenId.length()) + "_" + "req" + ".txt";
            pathCr = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_OUTWARD_TOPUP) + CommonConstants.PACS_ID + "_topup_" + dateFormat.format(calender.getTime()) + "_" + neftgenId.substring(4, neftgenId.length()) + "_" + "req" + ".txt";
//            System.out.println("pathDr : " + pathDr);
//            System.out.println("pathCr : " + pathCr);
            String newLine = System.getProperty("line.separator");
            for (int i = 0; i < lst.size(); i++) {
                sponsorBankDetailsTO = (SponsorBankDetailsTO) lst.get(i);
                try {
                    cardAcctNum = CommonUtil.convertObjToStr(sponsorBankDetailsTO.getCardAcctNum());
                    cardOrgNo = CommonUtil.convertObjToStr(sponsorBankDetailsTO.getAcctNum());
//                    System.out.println(" neft initialted years2digit" + years2digit.format(calender.getTime()));
                    Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                    daysInYear = localCalendar.get(Calendar.DAY_OF_YEAR);
                    DecimalFormat df = new DecimalFormat("#.00");
                    String amt = df.format(sponsorBankDetailsTO.getAmount().doubleValue());
                    System.out.println("amt : " + amt);
                    if (amt.contains(".")) {
                        amt = amt.replace(".", "");
                    }
                    System.out.println("amt : " + amt);
//                    System.out.println(" neft started: " + neftgenId + "neftgenId.substring(3, 9)" + neftgenId.substring(3, 9)
//                            + "amt######$#$#$" + SponsorBankDetailsTO.getAmount().toString().length());
                    if (neftgenId != null && neftgenId.length() > 0) {
                        actualamtcnt = amtcount - amt.toString().length();
                        if (CommonUtil.convertObjToStr(sponsorBankDetailsTO.getTransType()).equals("DEBIT")) {
                            if (!flagDr) {
                                writeDr = new java.io.FileWriter(pathDr, false);
                                printWriterDr = new java.io.PrintWriter(writeDr);
                                flagDr = true;
                            }
                            printWriterDr.write(cardAcctNum + pipeLine + padLeftZeros(amt, 16) + newLine);
                        } else {
                            if (!flagCr) {
                                writeCr = new java.io.FileWriter(pathCr, false);
                                printWriterCr = new java.io.PrintWriter(writeCr);
                                flagCr = true;
                            }
                            printWriterCr.write(cardAcctNum + pipeLine + padLeftZeros(amt, 16) + newLine);
                        }
                    }
                    HashMap processedMap = new HashMap();
                    processedMap.put("ACT_NUM", sponsorBankDetailsTO.getAcctNum());
                    processedMap.put("CARD_ACT_NUM", padLeftZeros(sponsorBankDetailsTO.getCardAcctNum(), 35));
                    processedMap.put("TRANS_DT", sponsorBankDetailsTO.getTransDt());
                    processedMap.put("TRANS_ID", sponsorBankDetailsTO.getTransId());
                    sqlMap.executeUpdate("UpdateSponsorBankProcessedRecord", processedMap);

                    atmTO.setAcctNum(CommonUtil.convertObjToStr(sponsorBankDetailsTO.getAcctNum())); //DEBIT ACCOUNT NUMBER
                    atmTO.setTransDt(getProperFormatDate(sponsorBankDetailsTO.getTransDt())); //APPLICATION DATE
                    atmTO.setAmount(new Double(sponsorBankDetailsTO.getAmount()));//TRANSACTION AMOUNT
                    atmTO.setCardAcctNum(padLeftZeros(sponsorBankDetailsTO.getCardAcctNum(), 35));//CUSTOMER CARD ACCOUNT NUMBER
                    atmTO.setInitiatedBranch(sponsorBankDetailsTO.getInitiatedBranch()); //INITIATED BRANCH
                    atmTO.setTransType(sponsorBankDetailsTO.getTransType());  //TRANS TYPE EITHER DEBIT OR CREDIT
                    atmTO.setStatus(CommonConstants.STATUS_CREATED);  //PROS  OR RVRS
                    atmTO.setSequenceNo(neftgenId.substring(4, neftgenId.length())); // FILE SEQUENCE NUMBER
                    atmTO.setTransId(sponsorBankDetailsTO.getTransId());
                    atmTO.setTran_status("P"); // TRANSACTION STATUS P OR F
                    atmTO.setAtmType("");
                    atmTO.setStation_code("");  //ATM STATION ID
                    atmTO.setRrn("");  //ATM RRN
                    atmTO.setMop("");  // PAC OR ATM
                    atmTO.setError_code("");
                    atmTO.setAtmDtStr(dateFormatdate.format(calender.getTime()));
                    atmTO.setNetWorkType("");
                    atmTO.setAtm_postTime(dateFormathhmmss.format(calender.getTime()));
                    sqlMap.executeUpdate("insertATMAcknowledgementTO", atmTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new TTException(e);
                }
            }
            if (printWriterDr != null && writeDr != null) {
                printWriterDr.close();
                writeDr.close();
            }
            if (printWriterCr != null && writeCr != null) {
                printWriterCr.close();
                writeCr.close();
            }
        }
    }

    private void writeTransactionReportATMFile() throws Exception {
//        DateFormat years2digit = new SimpleDateFormat("yy");
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
//        DateFormat dtFormtWithOutMin = new SimpleDateFormat("yyyyMMdd");
//        DateFormat dtMiddleWithOutMin = new SimpleDateFormat("yyyyddMM");
//        DateFormat minOnly = new SimpleDateFormat("hhmm");
        Calendar calender = Calendar.getInstance();
        dateFormat.format(calender.getTime());

        Date dt = new Date();
        String path, headOfficeBranchId = "";
        String pipeLine = "|";//{:
        int daysInYear = 0, amtcount = 16, actualamtcnt = 0;
        String neftUniqueId = "", neftgenId = "", sequnceNum = "";
        String cardAcctNum = "", cardOrgNo = "";
        Date transDt = null;
//        System.out.println("writeTOPUPATMFile : " + "dtFormtWithMin" + dateFormat.format(calender.getTime())+" dt : "+dt);
        List list = (List) sqlMap.executeQueryForList("getPanAmount", null);
        if (list != null && list.size() > 0) {
            headOffMap = (HashMap) list.get(0);
            headOfficeBranchId = CommonUtil.convertObjToStr(headOffMap.get("HEAD_OFFICE"));
            transDt = ServerUtil.getCurrentDate(headOfficeBranchId);
        }
        HashMap transactionReportMap = new HashMap();
        transactionReportMap.put("TRANS_DT", transDt);
        List lst = sqlMap.executeQueryForList("getTransactionReportFile", transactionReportMap);
        if (lst != null && lst.size() > 0) {
            neftgenId = getCedgeUniqueATMID();
            path = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_OUTWARD_TRANSACTION_REPORT) + CommonConstants.PACS_ID + dateFormat.format(calender.getTime()) + "_" + neftgenId.substring(3, neftgenId.length()) + "_" + "req" + ".txt";
            java.io.FileWriter write = null;
            java.io.PrintWriter printWriter = null;
            write = new java.io.FileWriter(path, false);
            printWriter = new java.io.PrintWriter(write);
            String newLine = System.getProperty("line.separator");
            for (int i = 0; i < lst.size(); i++) {
                transactionReportMap = (HashMap) lst.get(i);
                try {
                    String stationNo = CommonUtil.convertObjToStr(transactionReportMap.get("STATION_NO"));
                    String accountNo = CommonUtil.convertObjToStr(transactionReportMap.get("ACCOUNT_NO"));
                    String rrn = CommonUtil.convertObjToStr(transactionReportMap.get("RRN"));
                    String stationId = CommonUtil.convertObjToStr(transactionReportMap.get("STATION_ID"));
                    String tranDate = CommonUtil.convertObjToStr(transactionReportMap.get("TRAN_DATE"));
                    String tranTime = CommonUtil.convertObjToStr(transactionReportMap.get("TRAN_TIME"));
                    String tranCode = CommonUtil.convertObjToStr(transactionReportMap.get("TRAN_CODE"));
                    double tranCharges = CommonUtil.convertObjToDouble(transactionReportMap.get("TRAN_CHARGES"));
                    double tranAmount = CommonUtil.convertObjToDouble(transactionReportMap.get("TRAN_AMOUNT"));
                    String cardNo = CommonUtil.convertObjToStr(transactionReportMap.get("CARD_NO"));
                    String currentAccountNo = CommonUtil.convertObjToStr(transactionReportMap.get("CURRENT_ACCOUNT_NO"));
                    double rbiAtmCharges = CommonUtil.convertObjToDouble(transactionReportMap.get("RBI_ATM_CHARGE"));
                    String mop = CommonUtil.convertObjToStr(transactionReportMap.get("MOP"));
                    String network = CommonUtil.convertObjToStr(transactionReportMap.get("NETWORK"));
                    String msgType = CommonUtil.convertObjToStr(transactionReportMap.get("MSG_TYPE"));
                    String tranStatus = CommonUtil.convertObjToStr(transactionReportMap.get("STATUS"));
                    String transId = CommonUtil.convertObjToStr(transactionReportMap.get("TRAN_ID"));
//                    System.out.println(" neft initialted years2digit" + years2digit.format(calender.getTime()));
                    Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                    daysInYear = localCalendar.get(Calendar.DAY_OF_YEAR);
                    String amt = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(transactionReportMap.get("TRAN_AMOUNT")).doubleValue() * 100);
                    String chargeAmt = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(transactionReportMap.get("TRAN_CHARGES")).doubleValue() * 100);
//                    System.out.println(" stationNo : " + stationNo + " accountNo : " + padLeftZeros(accountNo,17) + " rrn : " + rrn + " stationId : "+stationId + " tranDate : "+tranDate + " tranTime : " + tranTime + " tranCode : "+tranCode + " tranCharges : " + tranCharges + " tranAmount : "+tranAmount + " cardNo : "+cardNo+" currentAccountNo : "+currentAccountNo+" rbiAtmCharges : " + rbiAtmCharges + " mop : "+mop+" network : "+network+" msgType : "+msgType+" transStatus : "+tranStatus);
                    printWriter.write(stationNo + pipeLine + padLeftZeros(accountNo, 17) + pipeLine + rrn + pipeLine + stationId + pipeLine + tranDate + pipeLine + tranTime + pipeLine + tranCode + pipeLine + padLeftZeros(chargeAmt, 16) + pipeLine + padLeftZeros(amt, 16) + pipeLine + padLeftZeros(cardNo, 35) + pipeLine + currentAccountNo + pipeLine + rbiAtmCharges + pipeLine + mop + pipeLine + network + pipeLine + msgType + pipeLine + tranStatus + newLine);
                    HashMap processedMap = new HashMap();
                    processedMap.put("ACT_NUM", accountNo);
                    processedMap.put("CARD_ACT_NUM", padLeftZeros(cardNo, 35));
                    processedMap.put("TRANS_DT", transDt);
                    processedMap.put("TRANS_ID", transId);
                    sqlMap.executeUpdate("UpdateTxnGeneratedFlag", processedMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new TTException(e);
                }
            }
            if (printWriter != null) {
                printWriter.close();
            }
            if (write != null) {
                write.close();
            }
        }
    }

    public static String padLeftSpaces(String str, int n) {
        return String.format("%1$" + n + "s", str);
    }

    public static String padLeftZeros(String str, int n) {
        return String.format("%1$" + n + "s", str).replace(' ', '0');
    }

    public void reportGenerationSpecifiedTime() throws SQLException {
        java.util.List list = sqlMap.executeQueryForList("getServerTime", new HashMap());
        if (list != null && list.size() > 0) {
            Date serverTimeMap = (Date) list.get(0);
//            String ServerTime = CommonUtil.convertObjToStr(DateUtil.getDateWithoutMinitues(serverTimeMap)serverTimeMap);
//            System.out.println("Manual process started... : "+ServerTime);
//            System.out.println("Manual process started... : "+ServerTime.substring(10, 16));
//            if (ServerTime.substring(10,16).equals("15:13")) {
            System.out.println("Automatic process started... : ");
//        Calendar date = Calendar.getInstance();
//        date.set(
//                Calendar.DAY_OF_WEEK,
//                Calendar.SUNDAY);
//        date.set(Calendar.HOUR, 0);
//        date.set(Calendar.MINUTE, 0);
//        date.set(Calendar.SECOND, 0);
//        date.set(Calendar.MILLISECOND, 0);
//        // Schedule to run every Sunday in midnight
//        timer.schedule(new ReportGenerator(),
//                date.getTime(),1000 * 60 * 60 * 24 * 1);            
//            }else{
//                System.out.println("Manual process started... : ");
//            }
        }
    }

    public class ReportGenerator extends TimerTask {

        public void run() {
            System.out.println("Generating report");
        }
    }

    private void createTxntoInsufficientATMBalanceActs() throws Exception {
//        DateFormat years2digit = new SimpleDateFormat("yy");
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
//        DateFormat dtFormtWithOutMin = new SimpleDateFormat("yyyyMMdd");
//        DateFormat dtMiddleWithOutMin = new SimpleDateFormat("yyyyddMM");
//        DateFormat minOnly = new SimpleDateFormat("hhmm");
        Calendar calender = Calendar.getInstance();
        dateFormat.format(calender.getTime());

        Date dt = new Date();
        String creditBranchID = "";
        Date transDt = null;
        HashMap transactionReportMap = new HashMap();
        transactionReportMap.put("TRANS_DT", transDt);
        List lst = sqlMap.executeQueryForList("getAccountATMMinBalanceList", transactionReportMap);
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap acHeads = new HashMap();
                HashMap insufficientMap = (HashMap) lst.get(i);
                acHeads.put("DEBIT_ACCT_NUM", insufficientMap.get("LINKING_ACT_NUM"));
                acHeads.put("CREDIT_ACCT_NUM", insufficientMap.get("ACT_NUM"));
                acHeads.put("PROD_ID", CommonUtil.convertObjToStr(insufficientMap.get("LINKING_ACT_NUM")).substring(4, 7));
                acHeads.put("CARD_ACCT_NUM", CommonUtil.convertObjToStr(insufficientMap.get("CARD_ACCT_NUM")));
//                acHeads.put("ACT_NUM", atmTO.getAcctNum());
                debitbranchID = CommonUtil.convertObjToStr(insufficientMap.get("LINKING_ACT_NUM")).substring(0, 4);
                creditBranchID = CommonUtil.convertObjToStr(insufficientMap.get("ACT_NUM")).substring(0, 4);
                debitBranchcurrDt = ServerUtil.getCurrentDate(debitbranchID);
                creditBranchcurrDt = ServerUtil.getCurrentDate(creditBranchID);
                System.out.println("debitBranchcurrDt : "+debitBranchcurrDt+" creditBranchcurrDt : "+creditBranchcurrDt);
                if(debitBranchcurrDt != null && creditBranchcurrDt != null && DateUtil.dateDiff(debitBranchcurrDt, creditBranchcurrDt) != 0){
                    if(DateUtil.dateDiff(debitBranchcurrDt, creditBranchcurrDt)<0){
                        System.out.println("<0 DATE : Actual Act num : "+insufficientMap.get("ACT_NUM")+"linking act num : "+insufficientMap.get("LINKING_ACT_NUM"));
                        acHeads.put("ATMTRANS_CREDIT_BRANCH_ID", creditBranchID);
                    }else{
                        System.out.println(">0 DATE : Actual Act num : "+insufficientMap.get("ACT_NUM")+" linking act num : "+insufficientMap.get("LINKING_ACT_NUM"));
                        acHeads.put("ATMTRANS_DEBIT_BRANCH_ID", debitbranchID);
                    }
                }
                double minBalLimit = CommonUtil.convertObjToDouble(insufficientMap.get("ATM_LIMIT_AMT")).doubleValue();
                double outstandingBalance = CommonUtil.convertObjToDouble(insufficientMap.get("TOTAL_BALANCE")).doubleValue();
                double txnAmount = minBalLimit - outstandingBalance;
//                        System.out.println("acHeads######" + acHeads);
                List mapDataList = sqlMap.executeQueryForList("getAcctHeadforAcct", acHeads);
                if (mapDataList != null && mapDataList.size() > 0) {
                    HashMap detailMap = (HashMap) mapDataList.get(0);
                    String debitAcctHeadId = CommonUtil.convertObjToStr(detailMap.get("AC_HD_ID"));
                    acHeads.put("DEBIT_PROD_ID", detailMap.get("PROD_ID"));
                    acHeads.put("DEBIT_ACCT_HEAD_ID", debitAcctHeadId);
                    acHeads.put("DEBIT_BRANCH_ID", debitbranchID);
                }
                acHeads.put("PROD_ID", CommonUtil.convertObjToStr(insufficientMap.get("ACT_NUM")).substring(4, 7));
                mapDataList = sqlMap.executeQueryForList("getAcctHeadforAcct", acHeads);
                if (mapDataList != null && mapDataList.size() > 0) {
                    HashMap detailMap = (HashMap) mapDataList.get(0);
                    String debitAcctHeadId = CommonUtil.convertObjToStr(detailMap.get("AC_HD_ID"));
                    acHeads.put("CREDIT_PROD_ID", detailMap.get("PROD_ID"));
                    acHeads.put("CREDIT_ACCT_HEAD_ID", debitAcctHeadId);
                    acHeads.put("CREDIT_BRANCH_ID", creditBranchID);
                }
                doTRBetweenATMLinkedActTxn(acHeads, txnAmount);
            }
        }
    }

    private HashMap doTRBetweenATMLinkedActTxn(HashMap acHeads, double txnAmount) throws Exception {
        TxTransferTO objTxTransferTO = new TxTransferTO();
        TransferTrans trans = new TransferTrans();
        String userid = "", useridAuth = "";
//        double charges = CommonUtil.convertObjToDouble(atmTO.getChargeAmt()).doubleValue();
        trans.setInitiatedBranch(CommonUtil.convertObjToStr(acHeads.get("DEBIT_BRANCH_ID")));  // hard coded
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        HashMap txMap = new HashMap();
        userid = "ATM";
        useridAuth = "ATM_USER";
        TxTransferTO transferTo = new TxTransferTO();

        ArrayList transferList = new ArrayList(); // for local transfer
        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("DEBIT_ACCT_HEAD_ID"));
        txMap.put(TransferTrans.DR_BRANCH, acHeads.get("DEBIT_BRANCH_ID"));
        txMap.put(TransferTrans.CURRENCY, "INR");
        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(acHeads.get("DEBIT_ACCT_NUM")));
        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(acHeads.get("DEBIT_PROD_ID")));
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
        txMap.put(CommonConstants.USER_ID, userid);
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put("AUTHORIZEREMARKS", "ATM_INSUFFICIENT_TRANSACTION");
        txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
        txMap.put("PARTICULARS", "ATM TRANSACTION");
        if(acHeads.containsKey("ATMTRANS_DEBIT_BRANCH_ID")){
            txMap.put("ATMTRANS_BRANCH_ID", acHeads.get("ATMTRANS_DEBIT_BRANCH_ID"));
        }
        if(acHeads.containsKey("ATMTRANS_CREDIT_BRANCH_ID")){
            txMap.put("ATMTRANS_BRANCH_ID", acHeads.get("ATMTRANS_CREDIT_BRANCH_ID"));
        }
        objTxTransferTO = trans.getDebitTransferTO(txMap, txnAmount);
        objTxTransferTO.setStatusBy("ATM");
        objTxTransferTO.setAuthorizeRemarks("ATM_INSUFFICIENT_TRANSACTION");
        transferList.add(objTxTransferTO);

        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CREDIT_ACCT_HEAD_ID"));
        txMap.put(TransferTrans.CR_BRANCH, acHeads.get("CREDIT_BRANCH_ID"));
        txMap.put(TransferTrans.CURRENCY, "INR");
        txMap.put(TransferTrans.CR_ACT_NUM, (String) acHeads.get("CREDIT_ACCT_NUM"));
        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(acHeads.get("CREDIT_PROD_ID")));
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
        txMap.put(CommonConstants.USER_ID, userid);
        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
        txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
        txMap.put("AUTHORIZEREMARKS", "ATM_INSUFFICIENT_TRANSACTION");
        objTxTransferTO = trans.getCreditTransferTO(txMap, txnAmount);
        objTxTransferTO.setStatusBy("ATM");
        objTxTransferTO.setAuthorizeRemarks("ATM_INSUFFICIENT_TRANSACTION");
        transferList.add(objTxTransferTO);

        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", transferList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(acHeads.get("DEBIT_ACCT_NUM")));
        data.put("INITIATED_BRANCH", acHeads.get("DEBIT_BRANCH_ID"));
        data.put(CommonConstants.BRANCH_ID, acHeads.get("DEBIT_BRANCH_ID"));
        if(acHeads.containsKey("ATMTRANS_CREDIT_BRANCH_ID")){
            data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(acHeads.get("CREDIT_ACCT_NUM")));
            data.put("INITIATED_BRANCH", acHeads.get("CREDIT_BRANCH_ID"));
            data.put(CommonConstants.BRANCH_ID, acHeads.get("CREDIT_BRANCH_ID"));
        }
        data.put(CommonConstants.USER_ID, useridAuth);
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "INSERT");

        transferDAO.setForLoanDebitInt(false);
        HashMap authorizeMap = new HashMap();
        authorizeMap.put("BATCH_ID", null);
        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
        data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
//        System.out.println("data###" + data);
        HashMap transDetMap = transferDAO.execute(data, false);
        SponsorBankDetailsTO sponsorBankDetailsTO = new SponsorBankDetailsTO();
        String neftgenId = getCedgeUniqueATMID();
        String cardOrgNo = CommonUtil.convertObjToStr(acHeads.get("CARD_ACCT_NUM"));
        sponsorBankDetailsTO.setAcctNum(objTxTransferTO.getActNum());
        sponsorBankDetailsTO.setAmount(objTxTransferTO.getAmount());
        sponsorBankDetailsTO.setCardAcctNum(cardOrgNo);
        sponsorBankDetailsTO.setInitiatedBranch(objTxTransferTO.getInitiatedBranch());
        sponsorBankDetailsTO.setSequenceNo(neftgenId.substring(3, neftgenId.length()));
        sponsorBankDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        sponsorBankDetailsTO.setStatusBy(objTxTransferTO.getStatusBy());
        sponsorBankDetailsTO.setTransDt(objTxTransferTO.getTransDt());
        sponsorBankDetailsTO.setTransType(objTxTransferTO.getTransType());
        sponsorBankDetailsTO.setTransId(objTxTransferTO.getTransId());
        sqlMap.executeUpdate("insertSponsorBankDetailsTO", sponsorBankDetailsTO);
        return transDetMap;
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
}
