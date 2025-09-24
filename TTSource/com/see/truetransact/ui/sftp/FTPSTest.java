/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FTPSTest.java
 */

package com.see.truetransact.ui.sftp;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class FTPSTest implements Serializable {

    private ProxyFactory proxy = null;
    private HashMap proxyReturnMap = null;
// FTPSTest ftp = null;

    public FTPSTest() throws Exception {
        System.out.println(" test");
//  ftp = this;
//    connectToFTPS(this,"");
    }

//  public void putFile(String host,
//                      int port,
//                      String username,
//                      String password,
//                      String localFilename,
//                      String remoteFilename) {
//    try {
//      FTPSClient ftpClient = new FTPSClient(false);
//      // Connect to host
//      if(!ftpClient.isConnected()){
//        ftpClient.connect(host, port);
//      }
//      int reply = ftpClient.getReplyCode();
//      if (FTPReply.isPositiveCompletion(reply)) {
//
//        // Login
//        if (ftpClient.login(username, password)) {
//
//          // Set protection buffer size
//          ftpClient.execPBSZ(0);
//          // Set data channel protection to private
//          ftpClient.execPROT("P");
//          // Enter local passive mode
//          ftpClient.enterLocalPassiveMode();
//
//          // Store file on host
//	  InputStream is = new FileInputStream(localFilename);
//	  if (ftpClient.storeFile(remoteFilename, is)) {
//	    is.close();
//	  } else {
//	    System.out.println("Could not store file");
//	  }
//	  // Logout
//	  ftpClient.logout();
//
//        } else {
//          System.out.println("FTP login failed");
//        }
//
//        // Disconnect
//    	ftpClient.disconnect();
//
//      } else {
//        System.out.println("FTP connect to host failed");
//      }
//    } catch (IOException ioe) {
//      System.out.println("FTP client received network error");
//    }
////    catch (NoSuchAlgorithmException nsae) {
////      System.out.println("FTP client could not use SSL algorithm");
////    }
//  }
//
//  public void getFile(String host,
//                      int port,
//                      String username,
//                      String password,
//                      String localFilename,
//                      String remoteFilename) {
//    try {
//      FTPSClient ftpClient = new FTPSClient(false);
//      // Connect to host
//      ftpClient.connect(host, port);
//      int reply = ftpClient.getReplyCode();
//      if (FTPReply.isPositiveCompletion(reply)) {
//
//        // Login
//        if (ftpClient.login(username, password)) {
//
//          // Set protection buffer size
//          ftpClient.execPBSZ(0);
//          // Set data channel protection to private
//          ftpClient.execPROT("P");
//          // Enter local passive mode
//          ftpClient.enterLocalPassiveMode();
//          remoteFilename="/ftp_server/07022013.xlsx";
//           localFilename ="C:/pigmy/07022013.xlsx";
//          // Store file on host
//          // String remoteFile1 = "/test/video.mp4";
//            //File downloadFile1 = new File("D:/Downloads/video.mp4");
//            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(localFilename));
//            boolean success = ftpClient.retrieveFile(remoteFilename, outputStream1);
//            outputStream1.close();
//
//            if (success) {
//                System.out.println("File #1 has been downloaded successfully.");
//            }
////	  InputStream is = ftpClient.retrieveFileStream(remoteFilename);
////	  if (ftpClient.storeFile(localFilename, is)) {
////	    is.close();
////	  } else {
////	    System.out.println("Could not store file");
////	  }
//	  // Logout
//	  ftpClient.logout();
//
//        } else {
//          System.out.println("FTP login failed");
//        }
//
//        // Disconnect
//    	ftpClient.disconnect();
//
//      } else {
//        System.out.println("FTP connect to host failed");
//      }
//    }
//    catch (IOException ioe) {
//      System.out.println("FTP client received network error");
//    }
////    catch (NoSuchAlgorithmException nsae) {
////      System.out.println("FTP client could not use SSL algorithm");
////    }
//  }
    public void connectToFTPS(FTPSTest ftps, String transType) throws Exception {
//String serverfilePath ="/FTP"+"/";

        //java.util.List list = ClientUtil.executeQuery("getServerTime", new HashMap());
//  getConnectrtgsDAO();
        System.out.println("connectToFTPS transType : " + transType);
        Properties props = new Properties();
        try {
            if (transType.equals("ATM")) {
                SSHFileTransaction ssh = new SSHFileTransaction();
                ssh.getListofFoldersForATM(transType);
            } 
            if (transType.equals("RTGSNEFT")) {
                //System.out.println("After If in FTP Test Inside SSHFileTransaction() : ");
                SSHFileTransaction ssh = new SSHFileTransaction();
                ssh.getListofFoldersForRTGSNEFT(transType);
            }
// ftps.putFile(serverAddress.toString(), 21, userId.toString(), password.toString(), localfilename, serverfilePath);
// ftps.getFile(serverAddress.toString(), 21, userId.toString(), password.toString(), localfilename.toString(), serverfilePath.toString());
//ftp.putFile("180.92.173.117", 21, "fincuro-f1", "F1n@#@!", localfilename, serverfilePath);
//ftp.getFile("180.92.173.117", 21, "fincuro-f1", "F1n@#@!", localfilename, serverfilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String arg[]) {
        System.out.println("FTPSTest object created:");
        try {
            FTPSTest ftp = new FTPSTest();
// ftp =new FTPSTest(ftp);
//String serverfilePath ="/FTP"+"/";
//    connectToFTPS(ftp);
//  java.util.List list = ClientUtil.executeQuery("getServerTime", new HashMap());
            if (true) {
                return;
            }
//  System.out.println("list#######"+list);
            Properties props = new Properties();

// props.load(new FileInputStream("properties/" + "ftps"));
            File f1 = new File("TestFile.txt");
            System.out.println("File directory:" + f1.getAbsolutePath());
//            props.load(new FileInputStream("ftps.properties/"));
//            String serverAddress = props.getProperty("SERVER_IP_ADDRESS").trim();
//            String userId = props.getProperty("SERVER_USER_NAME").trim();
//            String password = props.getProperty("SERVER_PASSWORD").trim();
//            String port = props.getProperty("SERVER_PORT").trim();
            String serverAddress = CommonConstants.SFTP_IP_ADDRESS;
            String userId = CommonConstants.SFTP_USER_ID;
            String password = CommonConstants.SFTP_PASSWORD;
            String port = CommonConstants.SFTP_PORT;
// String remoteDirectory = props.getProperty("remoteDirectory").trim();
//String localDirectory = props.getProperty("localDirectory").trim();
            System.out.println("serverAddress" + serverAddress + "userId" + userId + "password" + password + "port" + port);
//    String serverfilePath = "/ftp_server/07022013.xlsx";

            String serverfilePath = "/home/infSFTPUat/WPPH2H/Fincuro/MDCCB/Outward/NEFT/N06/NEFT20150826013944.txt";    //CommonConstants.NEFT_OUTWARD_N06+"/"+"NEFT20150826013944.txt";
//  String localfilename = "C:/pigmy/07022013.xlsx";
            String localfilename = "E:/MDCCB/Outward/NEFT/N06/"; //
            String fileName = "NEFT20150812072436.txt";
// String remoteDirectory = props.getProperty("remoteDirectory").trim();
//String localDirectory = props.getProperty("localDirectory").trim();
// String serverfilePath ="/ftp_server/07022013.xlsx";
//  String localfilename ="C:/pigmy/07022013.xlsx";
// ftp.putFile(serverAddress, 21, userId,password, localfilename, serverfilePath);
//      ftp.putFile("180.92.173.117", 21, "fincuro-f1", "F1n@#@!", localfilename, serverfilePath);
//     ftp.getFile("180.92.173.117", 21, "fincuro-f1", "F1n@#@!", localfilename, serverfilePath);
            //  SendMyFile sendMyFiles = new SendMyFile("neft", serverAddress, userId, password, port, serverfilePath, localfilename,"backuppath", "UPLOAD");
            //   sendMyFiles.startFTP(serverAddress, userId, password, port, serverfilePath, localfilename, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    
}
