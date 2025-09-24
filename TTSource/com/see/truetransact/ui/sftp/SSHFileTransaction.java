/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SSHFileTransaction.java
 */
package com.see.truetransact.ui.sftp;

import com.jcraft.jsch.*;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.FileUtils;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class SSHFileTransaction {

    String user = "";
    String password = "";
    String host = "";
    int port = 0;
    ChannelSftp sftpChannel = null;
    private ProxyFactory proxy = null;
    private HashMap proxyReturnMap = null;
    Session session = null;

    SSHFileTransaction() {
        try {
            user = CommonConstants.SFTP_USER_ID;
            password = CommonConstants.SFTP_PASSWORD;
            host = CommonConstants.SFTP_IP_ADDRESS;
            port = CommonUtil.convertObjToInt(CommonConstants.SFTP_PORT);
            System.out.println("SSHFileTransaction IP Address : " + host + " userId : " + user + " password : " + password + " port : " + port);
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SSHFileTransaction exception part");
        }
    }

    public void getListofFoldersForATM(String neft) {
        try {
            String sourcepath = "";
            String destinationPath = "";
            String backupFolder = "";

            sourcepath = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_OUTWARD_TOPUP);//our production server ok
            destinationPath = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_OUTWARD_TOPUP);//their production server ok
            backupFolder = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_BACKUP_OUTWARD_TOPUP);//"E:/backup/KKC/Outward/ATM/TOPUP/"; ok
            uploadATMFile(sourcepath, destinationPath, backupFolder);
            System.out.println("TOPUP sourcepath : " + sourcepath);
            System.out.println("TOPUP destinationPath : " + destinationPath);
            System.out.println("TOPUP backupFolder : " + backupFolder);

//            // CREDIT INTO SPONCER BANK ACCOUNT RETURN ACKNOWLEDGEMENT 
            sourcepath = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_INWARD_TOPUP_ACK);//"/pacs_inf/Koo004/TOPUPACK/";//production server ok
            destinationPath = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_INWARD_TOPUP_ACK);//"E:/KKC/Inward/ATM/TOPUP_ACK/"; ok
            backupFolder = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_BACKUP_INWARD_TOPUP_ACK);
            downloadATMFile(sourcepath, destinationPath, backupFolder);
            System.out.println("TOPUP_ACK sourcepath : " + sourcepath);
            System.out.println("TOPUP_ACK destinationPath : " + destinationPath);
            System.out.println("TOPUP_ACK backupFolder : " + backupFolder);

            // STOP INTO SPONCER BANK ACCOUNT
            sourcepath = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_OUTWARD_STOPAC);//"E:/KKC/Outward/ATM/STOPAC/";//our production server ok
            destinationPath = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_OUTWARD_STOPAC);//"/pacs_inf/Koo004/STOPAC/";//their production server  ok
            backupFolder = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_BACKUP_OUTWARD_STOPAC);//"E:/backup/KKC/Outward/ATM/STOPAC/"; ok
            uploadATMFile(sourcepath, destinationPath, backupFolder);
            System.out.println("STOPAC sourcepath : " + sourcepath);
            System.out.println("STOPAC destinationPath : " + destinationPath);
            System.out.println("STOPAC backupFolder : " + backupFolder);

            // CREDIT INTO SPONCER BANK ACCOUNT RETURN ACKNOWLEDGEMENT 
            sourcepath = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_INWARD_STOP_ACK);//"/pacs_inf/Koo004/STOPACACK/";//production server  ok
            destinationPath = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_INWARD_STOP_ACK);//"E:/KKC/Inward/ATM/STOP_ACK/"; ok
            backupFolder = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_INWARD_STOP_ACK);
            downloadATMFile(sourcepath, destinationPath, backupFolder);
            System.out.println("STOPACK sourcepath : " + sourcepath);
            System.out.println("STOPACK destinationPath : " + destinationPath);
            System.out.println("STOPACK backupFolder : " + backupFolder);

            sourcepath = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_INWARD_TRANSACTION);//"/pacs_inf/Koo004/TRANSACTIONS/";//production server ok
            destinationPath = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_INWARD_TRANSACTION);//"E:/KKC/Inward/ATM/TRANSACTION/"; ok
            backupFolder = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_INWARD_TRANSACTION_ARCHIVE);
            downloadATMFile(sourcepath, destinationPath, backupFolder);
            System.out.println("TRANSACTION sourcepath : " + sourcepath);
            System.out.println("TRANSACTION destinationPath : " + destinationPath);
            System.out.println("TRANSACTION backupFolder : " + backupFolder);
            getConnectATMFileTransDAO();

//            sourcepath = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_OUTWARD_TRANSACTION_ACK);//"E:/KKC/Outward/ATM/TRANSACTION_ACK/"; ok
//            destinationPath = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_OUTWARD_TRANSACTION_ACK);//"/pacs_inf/Koo004/TRANSACTIONSACK/";//production server   ok
//            backupFolder = CommonUtil.convertObjToStr(CommonConstants.ATM_OUR_BACKUP_OUTWARD_TRANSACTION_ACK);//"E:/backup/KKC/Outward/ATM/TRANSACTION_ACK/";   ok
//            upload(sourcepath, destinationPath, backupFolder);
//            System.out.println("TRANSACTION_ACK sourcepath : "+sourcepath);
//            System.out.println("TRANSACTION_ACK destinationPath : "+destinationPath);
//            System.out.println("TRANSACTION_ACK backupFolder : "+backupFolder);

//            sourcepath = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_INWARD_TRANSACTION);//"/pacs_inf/Koo004/TRANSACTIONS/";//production server   ok
//            destinationPath = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_INWARD_TRANSACTION_ARCHIVE);//"/pacs_inf/Koo004/TRANSACTIONSArchive/";//production server   ok
//            backupFolder = CommonUtil.convertObjToStr(CommonConstants.ATM_THEIR_INWARD_TRANSACTION_ARCHIVE);//"/pacs_inf/Koo004/TRANSACTIONSArchive/";//production server   ok
//            uploadATMFileProcessed(sourcepath, destinationPath, backupFolder);
//            System.out.println("TRANSACTIONS_ARCHIVE sourcepath : "+sourcepath);
//            System.out.println("TRANSACTIONS_ARCHIVE destinationPath : "+destinationPath);
//            System.out.println("TRANSACTIONS_ARCHIVE destinationPath : "+backupFolder);

            sftpChannel.disconnect();
            session.disconnect();
            sftpChannel.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getListofFoldersForRTGSNEFT(String neft) {
        try {
            //System.out.println("Inside getListofFoldersForRTGSNEFT() : ");
            String serverAddress = CommonConstants.SFTP_IP_ADDRESS;
            String userId = CommonConstants.SFTP_USER_ID;
            String password = CommonConstants.SFTP_PASSWORD;
            String port = CommonConstants.SFTP_PORT;
            System.out.println("connectToFTPS IP Address : " + serverAddress + " userId : " + userId + " password : " + password + " port : " + port);

            //System.out.println("######## FileMovingPart 1 Started : ");
            //FileMovingPart();//Existing Files Moved (Outward Return/Ack)
            //System.out.println("######## FileMovingPart 1 Completed : ");

            //System.out.println("######## File Creation Started : ");
            getConnectRTGSNEFTTransDAO(neft);
            //System.out.println("######## File Creation Completed : ");

            //System.out.println("######## FileMovingPart Started : ");
            FileMovingPart();//Fresh Outward Files Moved
            //System.out.println("######## FileMovingPart Completed : ");

            sftpChannel.disconnect();
            session.disconnect();
            sftpChannel.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getConnectATMFileTransDAO() {
        try {
            proxyReturnMap = new HashMap();
            HashMap map = new HashMap();
            map.put(CommonConstants.JNDI, "ATMFileTransJNDI");
            map.put(CommonConstants.HOME, "atm.ATMFileTransHome");
            map.put(CommonConstants.REMOTE, "atm.AtmFileTrans");
            proxy = ProxyFactory.createProxy();
            proxyReturnMap = proxy.execute(proxyReturnMap, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Added By Kannan AR 
    public void FileMovingPart() {
        try {
            String sourcepath = "";
            String destinationPath = "";
            String backupFolder = "";
            sourcepath = CommonUtil.convertObjToStr(CommonConstants.ICICI_RTGS_NEFT_OUTWARD_PATH);
            destinationPath = CommonUtil.convertObjToStr(CommonConstants.ICICI_RTGS_NEFT_OUTWARD_MOVED_NW_PATH);
            backupFolder = CommonUtil.convertObjToStr(CommonConstants.ICICI_RTGS_NEFT_OUTWARD_BACKUP_PATH);
            //System.out.println("##### DownLoad RTGS/NEFT sourcepath 1: " + sourcepath);
            //System.out.println("##### DownLoad RTGS/NEFT destinationPath 2: " + destinationPath);
            //System.out.println("##### DownLoad RTGS/NEFT backupFolder 3: " + backupFolder);
            //System.out.println("sftpChannel  " + sftpChannel.isConnected());
            if (sourcepath.length() > 0 && destinationPath.length() > 0 && backupFolder.length() > 0) {
                downloadFileLocally(sourcepath, destinationPath, backupFolder);
            } else {
                System.out.println("RTGS/NEFT Path not mentioned...");
            }

            sourcepath = CommonUtil.convertObjToStr(CommonConstants.ICICI_RTGS_NEFT_INWARD_PATH);
            destinationPath = CommonUtil.convertObjToStr(CommonConstants.ICICI_RTGS_NEFT_INWARD_MOVED_NW_PATH);
            backupFolder = CommonUtil.convertObjToStr(CommonConstants.ICICI_RTGS_NEFT_INWARD_BACKUP_PATH);
            //System.out.println("### Upload RTGS/NEFT sourcepath : " + sourcepath);
            //System.out.println("### Upload RTGS/NEFT destinationPath : " + destinationPath);
            //System.out.println("### Upload RTGS/NEFT backupFolder : " + backupFolder);
            if (sourcepath.length() > 0 && destinationPath.length() > 0 && backupFolder.length() > 0) {
                uploadFileLocally(sourcepath, destinationPath, backupFolder);
            } else {
                System.out.println("RTGS/NEFT Path not mentioned...");
            }
        } catch (Exception e) {
        }
    }

    private void getConnectRTGSNEFTTransDAO(String type) {
        try {
            proxyReturnMap = new HashMap();
            HashMap map = new HashMap();
            map.put(CommonConstants.JNDI, "RtgsNeftFileTransJNDI");
            map.put(CommonConstants.HOME, "rtgsneftfiletrans.RtgsNeftFileTransHome");
            map.put(CommonConstants.REMOTE, "rtgsneftfiletrans.RtgsNeftFileTransRemote");
            proxy = ProxyFactory.createProxy();
            proxyReturnMap.put("BRANCH_CODE", "8000");
            type = "NEFT";
            proxyReturnMap.put("TYPE", type);
            proxyReturnMap = proxy.execute(proxyReturnMap, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void download(String source, String destination, String backupFolder) throws Exception {
        try {
            sftpChannel.cd(source);
            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*.txt");
            for (ChannelSftp.LsEntry entry : list) {
                sftpChannel.get(entry.getFilename(), destination + entry.getFilename());
                sftpChannel.rename(entry.getFilename(), backupFolder + entry.getFilename() + ".Done");
                System.out.println(entry.getFilename());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upload(String source, String destination, String backupFolder) throws Exception {
        String tranfile = "";
        FileInputStream inputStream = null;
        try {
            sftpChannel.cd(destination);
            File folder = new File(source);
            File backupfolder = new File(backupFolder);
            if (folder.listFiles() != null) {
                for (final File fileEntry : folder.listFiles()) {
                    inputStream = new FileInputStream(fileEntry);
                    sftpChannel.put(inputStream, fileEntry.getName());
                    inputStream.close();
                    tranfile = String.valueOf(backupfolder.getAbsolutePath() + "\\" + fileEntry.getName() + ".Done");
                    System.out.println("tranfile##" + tranfile);
                    if (fileEntry.exists()) {
                        fileEntry.renameTo(new File(tranfile));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadATMFile(String source, String destination, String backupFolder) throws Exception {
        try {
            sftpChannel.cd(source);
            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*.txt");
            for (ChannelSftp.LsEntry entry : list) {
                System.out.println("entry.getFilename() : " + entry.getFilename() + " destination : " + destination + " source : " + source + " backupFolder : " + backupFolder);
                sftpChannel.get(entry.getFilename(), destination + entry.getFilename());
                if (entry.getFilename().contains(".Done")) {
                    sftpChannel.rename(entry.getFilename(), backupFolder + entry.getFilename().replace("req", "res"));
                } else {
                    sftpChannel.rename(entry.getFilename(), backupFolder + entry.getFilename() + ".Done");
                }
                System.out.println(entry.getFilename());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadATMFile(String source, String destination, String backupFolder) throws Exception {
        String tranfile = "";
        FileInputStream inputStream = null;
        try {
            sftpChannel.cd(destination);
            File folder = new File(source);
            File backupfolder = new File(backupFolder);
            if (folder.listFiles() != null) {
                for (final File fileEntry : folder.listFiles()) {
                    inputStream = new FileInputStream(fileEntry);
                    sftpChannel.put(inputStream, fileEntry.getName());
                    inputStream.close();
                    tranfile = String.valueOf(backupfolder.getAbsolutePath() + "\\" + fileEntry.getName() + ".Done");
                    System.out.println("tranfile##" + tranfile);
                    if (fileEntry.exists()) {
                        fileEntry.renameTo(new File(tranfile));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadFileLocally(String source, String destination, String backupFolder) throws Exception {
        String tranfile = "";
        FileInputStream inputStream = null;
        final boolean[] ready = {false};
        final File[] fileArray = new File[0];
        try {
            sftpChannel.cd(destination);
            File folder = new File(source);
            File backupfolder = new File(backupFolder);
			if (folder.listFiles() != null) {
	            for (final File fileEntry : folder.listFiles()) {
	                System.out.println("sftpChannel.isConnected()   :" + sftpChannel.isConnected());
	                ready[0] = false;
	                if (sftpChannel.isConnected()) {
	                    System.out.println("BEFORE MOVE TO SFTP  :" + fileEntry.getName());	
	                    inputStream = new FileInputStream(fileEntry);
	                    System.out.println("File not exists..." + new java.util.Date());	
	                    System.out.println("#$#$ ready:" + ready[0]);
	                    SftpProgressMonitor progress = new SftpProgressMonitor() {
	                        @Override
	                        public void init(int arg0, String arg1, String arg2, long arg3) {
	                            System.out.println("File transfer begin..");
	                        }	
	                        @Override
	                        public void end() {
	                            System.out.println("Upload of file " + fileEntry.getName() + " succeeded.");
	                            ready[0] = true;
	                            //      fileArray[0]=fileEntry;
	                        }	
	                        @Override
	                        public boolean count(long i) {
	                            return false;
	                        }
	                    };
	                    sftpChannel.put(inputStream, fileEntry.getName(), progress);
	                    inputStream.close();	
	                    System.out.println("#$#$ before if ready:" + ready[0]);
	                    if (ready[0]) {
	                        System.out.println("backupfolder path  :" + backupfolder + "\\" + fileEntry.getName());
	                        System.out.println("backupfolder.getAbsolutePath() fileEntry.getName():" + backupfolder.getAbsolutePath() + "\\" + fileEntry.getName());
	                        System.out.println("File exists..." + new java.util.Date());
	                        tranfile = String.valueOf(backupfolder + "/" + fileEntry.getName() + ".Done");
	                        System.out.println(" Move file into backup tranfile :" + tranfile);
	                        fileEntry.renameTo(new File(tranfile));
	                        //Date dt =new Date();	
	                    }
	                }
	                System.out.println("#### File Upload Part Completed ");
	            }
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFileLocally(String source, String destination, String backupFolder) throws Exception {
        try {
            sftpChannel.cd(source);
            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*.TXT");
            for (ChannelSftp.LsEntry entry : list) {
                sftpChannel.get(entry.getFilename(), destination + entry.getFilename());
                sftpChannel.rename(entry.getFilename(), backupFolder + "/" + entry.getFilename() + ".Done");
                System.out.println(entry.getFilename());
            }
            System.out.println("#### File DownLoad Part Completed " + sftpChannel.isConnected());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadATMFileProcessed(String source, String destination, String backupFolder) throws Exception {
        String tranfile = "";
        FileInputStream inputStream = null;
        try {
            sftpChannel.cd(destination);
            File folder = new File(source);
            File backupfolder = new File(backupFolder);
            if (folder.listFiles() != null) {
                for (final File fileEntry : folder.listFiles()) {
                    inputStream = new FileInputStream(fileEntry);
                    sftpChannel.put(inputStream, fileEntry.getName());
                    inputStream.close();
                    tranfile = String.valueOf(backupfolder.getAbsolutePath() + "\\" + fileEntry.getName() + ".Done");
                    System.out.println("tranfile##" + tranfile);
                    if (fileEntry.exists()) {
                        fileEntry.renameTo(new File(tranfile));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String user = "infsprod";
        String password = "cedge1234";
        String host = "172.30.4.9";
        int port = 22;
        String remoteFile = "sample.txt";
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            InputStream out = null;
            sftpChannel.cd("/infsftp_prod/WPPH2H/Fincuro/MDCCB/Inward/NEFT/N02");
            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("TEST.TXT");
            for (ChannelSftp.LsEntry entry : list) {
//        sftpChannel.get(entry.getFilename(), "D:\\abi\\"+ entry.getFilename());
                sftpChannel.rename(entry.getFilename(), "/infsftp_prod/WPPH2H/Fincuro/MDCCB/Inward/NEFT/N02/" + entry.getFilename() + ".Done");
                System.out.println(entry.getFilename());
            }
            sftpChannel.disconnect();
            session.disconnect();
        } //catch(JSchException || SftpException || IOException e)
        catch (Exception e) {
            System.out.println(e);
        }
    }
}