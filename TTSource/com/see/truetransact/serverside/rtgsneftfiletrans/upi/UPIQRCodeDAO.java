/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SelectAllDAO.java
 *
 * Created on August 18, 2003, 4:19 PM
 */
package com.see.truetransact.serverside.rtgsneftfiletrans.upi;

import com.see.truetransact.serverside.common.viewall.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.sysadmin.servicetax.ServiceTaxMaintenanceGroupDAO;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.LoanClosedAccountInterestCalcTask;
import com.see.truetransact.commonutil.Dummy;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import com.see.truetransact.serverutil.ServerConstants;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.OracleResultSet;
import com.see.truetransact.commonutil.Dummy;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.rmi.RemoteException;

import java.sql.*;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.ui.common.viewall.QRCodeUI;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.see.truetransact.commonutil.StringEncrypter;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import static javax.swing.Spring.width;
import org.apache.log4j.Logger;

/**
 *
 * @author Balachandar
 */
public class UPIQRCodeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private String dataBaseURL;
    private String userName;
    private String passWord;
    private String SERVER_ADDRESS;
    private String tableName;
    private String driverName;
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private String cmd;
    private String tableCondition;
    private int isMore = -1;
    private String addCondition;
    private byte[] photoByteArray;
    private File qrCodeFile;
    private HashMap qrCodeMap;
    private String whereCondition;
    private HashMap mapQRCode;
    private FileOutputStream writer = null;
    private File signFile;
    private String customerID;
    private Date currDt = null;
    private StringEncrypter encrypt = null;
    
    private HashMap getData(HashMap map) throws Exception {
        System.out.println("map in UPIQRCodeDAO getdata :: " + map);
        StringEncrypter encrypt = new StringEncrypter();
        String mapName = (String) map.get(CommonConstants.MAP_NAME);
        return getQRFile(map);
    }


    /**
     * Creates a new instance of ViewAllDAO
     */
    public UPIQRCodeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UPIQRCodeDAO dao = new UPIQRCodeDAO();           
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("map in UPIQRCodeDAO execute " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        ServerUtil.callingSMSSchedule(_branchCode);
        return storeQRCode(map);
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("!!!!!obj -----> "+obj);
        HashMap returnMap=new HashMap();
        returnMap = getData(obj);        
        return returnMap;
    }
    
    private HashMap storeQRCode(HashMap qrCodeMap) throws Exception {
        setDriver();
        HashMap resultMap = new HashMap();
        List lst = sqlMap.executeQueryForList("getUPIRecordExistorNotForAccount", qrCodeMap);
        if (lst != null && lst.size() > 0) {
            resultMap.put("RE","Image Exist");
            System.out.println("image already exist...");
        } else {
            sqlMap.startTransaction();
            SmsConfigDAO smsDAO = new SmsConfigDAO();
            StringEncrypter encrypt = new StringEncrypter();    
            qrCodeMap.put("ACT_NUM",qrCodeMap.get("QR_ACT_NUM"));
            resultMap.put("QR_BANK", CommonUtil.convertObjToStr(qrCodeMap.get("QR_BANK")));
            qrCodeMap.put("STATUS",CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertUPIQRDetailsMap", qrCodeMap);
            sqlMap.commitTransaction();
            generateQRFileImage(qrCodeMap);
            System.out.println("New image started inserting qrCodeMap : " + qrCodeMap);
            DriverManager.registerDriver(((java.sql.Driver) Class.forName(driverName).newInstance()));
            customerID = CommonUtil.convertObjToStr(qrCodeMap.get("QR_ACT_NUM"));
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
            conn.setAutoCommit(false);
            if (driverName.equals("com.ibm.db2.jcc.DB2Driver")) {
                try {
                    String photoString = "";
                    String signString = "";
                    if (photoByteArray != null) {
                        qrCodeFile = new File(ServerConstants.SERVER_PATH + "/customer/" + customerID + "_photo.jpg");
                    } else {
                        qrCodeFile = null;
                    }
                    System.out.println("#$#$ photoFile : " + qrCodeFile);
                    
                    if (qrCodeFile != null) {
                        photoString = "?";
                    } else {
                        photoString = "empty_blob()";
                    }
                    
                    String st = "UPDATE " + tableName + " SET PHOTO_FILE = " + photoString
                            + ", SIGNATURE_FILE = " + signString + " WHERE " + tableCondition + "='" + customerID + "'";
                    PreparedStatement ps = conn.prepareStatement(st);
                    FileInputStream photoFis = null;
                    if (qrCodeFile != null) {
                        int fileLength = (int) qrCodeFile.length();
                        photoFis = new FileInputStream(qrCodeFile);
                        ps.setBinaryStream(1, photoFis, fileLength);
                        System.out.println("#$#$ photoFile...");
                    }
                    
                    System.out.println("#$#$ Update statement is : " + st);
                    //        ps.setString(3, customerID);
                    ps.executeUpdate();
                    ps.close();
                    conn.commit();
                    conn.close();
                    conn = null;
                    photoByteArray = null;
//                    signByteArray = null;
                    if (qrCodeFile != null) {
                        photoFis.close();
                    }
                } catch (Exception se) {
                    se.printStackTrace();
                    System.out.println("SQL Exception : " + se);
                    conn.close();
                    conn = null;
                    qrCodeFile = null;
                    signFile = null;
                    //            writer.flush();
                    //            writer.close();
                    addCondition = "";
//                if (cmd.equals(CommonConstants.TOSTATUS_INSERT)) {
//                    customerTO = null;
//                }
                }
            } else if (driverName.equals("oracle.jdbc.OracleDriver") || driverName.equals("oracle.jdbc.driver.OracleDriver")) {
                try {
                    stmt = conn.createStatement();
                    boolean b = false;
                    String st;
                    tableName = "UPI_QR_DETAILS";
                    tableCondition = "QR_ACT_NUM";
                    String qrbank = CommonUtil.convertObjToStr(qrCodeMap.get("QR_BANK"));
                    st = "UPDATE " + tableName + " SET QR_FILE = empty_blob() WHERE " + tableCondition + "='" + customerID + "' AND QR_BANK = '"+ qrbank +"'";
                    System.out.println("Update Statement executed : \n\t" + st);
                    b = stmt.execute(st);
                    System.out.println("##### cmd.equals(CommonConstants.TOSTATUS_INSERT) so, update Statement executed...");   // Print col 1

                    st = "SELECT QR_FILE, " + tableCondition + " FROM " + tableName + " WHERE " + tableCondition + "='" + customerID + "'";
                    st = st + " FOR UPDATE";
                    System.out.println("Statement execute query : \n\t" + st);
                    rset = stmt.executeQuery(st);
                    System.out.println("Statement execute query : " + rset);
                    rset.next();
                    System.out.println("rset.next()... QRFile.... ");
                    BLOB oracleBlob = ((OracleResultSet) rset).getBLOB(1);
                    System.out.println("#$#$ QRFile : " + qrCodeFile);
                    System.out.println("selected file length = " + (qrCodeFile == null ? "null" : qrCodeFile.length()));
                    System.out.println("cust_id = " + rset.getString(2));
                    FileInputStream reader = null;
                    OutputStream outstream = null;
                    int size = 0;
                    byte[] buffer;
                    int length = 0;
                    if (qrCodeFile != null) {
                        reader = new FileInputStream(qrCodeFile);
                        System.out.println("reader initialized ");
                        outstream = oracleBlob.getBinaryOutputStream();
                        System.out.println("outstream initialized ");
                        size = oracleBlob.getBufferSize();
                        buffer = new byte[size];
                        length = -1;
                        while ((length = reader.read(buffer)) != -1) {
                            System.out.println("length : " + length);
                            outstream.write(buffer, 0, length);
                        }
                        resultMap.put("QR_ACT_NUM", qrCodeMap.get("QR_ACT_NUM"));
                        resultMap.put("QR_BANK", getActualQRBankCode());
                        resultMap.put("QR_DETAILS", qrCodeMap.get("QR_DETAILS"));
//                        resultMap.put("QR_FILE", qrCodeFile);
                        resultMap.put("QR_FILE", buffer);
                        System.out.println("outstream written ");
                        System.out.println("#$#$ signFile...");
                        reader.close();
                        outstream.close();
                    }
                    System.out.println("#$#$ Update statement is : " + st);
                    System.out.println("outstream closed ");
                    stmt.close();
                    System.out.println("stmt closed ");
                    conn.commit();
                    conn.close();
                    conn = null;
                    rset.close();
                    outstream = null;
                    reader = null;
                    oracleBlob = null;
                } catch (Exception se) {
                    se.printStackTrace();
                    System.out.println("SQL Exception : " + se);
                    conn.close();
                    stmt.close();
                    conn = null;
                    rset.close();
                    qrCodeFile = null;
                    signFile = null;
                    addCondition = "";
                }
            }else if (driverName.equals("org.postgresql.Driver")) {
                System.out.println("Execute here for PostgreSQL driver");

                try {
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();

                    String tableName = "UPI_QR_DETAILS";
                    String tableCondition = "QR_ACT_NUM";
                    String qrbank = CommonUtil.convertObjToStr(qrCodeMap.get("QR_BANK"));

                    // Step 1: Clear existing QR file for the record
                    String updateSql = "UPDATE " + tableName
                            + " SET QR_FILE = NULL WHERE " + tableCondition + " = '" + customerID
                            + "' AND QR_BANK = '" + qrbank + "'";
                    System.out.println("Update Statement executed: \n\t" + updateSql);
                    stmt.executeUpdate(updateSql);

                    // Step 2: Prepare to update the binary file using PreparedStatement
                    String selectSql = "SELECT QR_FILE, " + tableCondition + " FROM " + tableName
                            + " WHERE " + tableCondition + " = ? FOR UPDATE";
                    System.out.println("Statement execute query: \n\t" + selectSql);

                    PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
                    selectPstmt.setString(1, customerID);
                    ResultSet rset = selectPstmt.executeQuery();

                    if (rset.next() && qrCodeFile != null) {
                        System.out.println("Record found, starting binary file update...");
                        FileInputStream fis = new FileInputStream(qrCodeFile);

                        String binaryUpdateSql = "UPDATE " + tableName
                                + " SET QR_FILE = ? WHERE " + tableCondition + " = ? AND QR_BANK = ?";
                        PreparedStatement binaryPstmt = conn.prepareStatement(binaryUpdateSql);
                        binaryPstmt.setBinaryStream(1, fis, (int) qrCodeFile.length());
                        binaryPstmt.setString(2, customerID);
                        binaryPstmt.setString(3, qrbank);

                        int rowsUpdated = binaryPstmt.executeUpdate();
                        System.out.println("Binary file updated successfully. Rows affected: " + rowsUpdated);

                        // Build resultMap
                        resultMap.put("QR_ACT_NUM", qrCodeMap.get("QR_ACT_NUM"));
                        resultMap.put("QR_BANK", qrbank);
                        resultMap.put("QR_DETAILS", qrCodeMap.get("QR_DETAILS"));
                        resultMap.put("QR_FILE", Files.readAllBytes(qrCodeFile.toPath()));

                        fis.close();
                        binaryPstmt.close();
                    } else {
                        System.out.println("No record found to update or QR file is null.");
                    }

                    rset.close();
                    selectPstmt.close();
                    stmt.close();
                    conn.commit();
                    conn.close();
                    conn = null;

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception occurred: " + e.getMessage());

                    try {
                        if (conn != null) {
                            conn.rollback();
                        }
                    } catch (SQLException rollbackEx) {
                        System.out.println("Rollback failed: " + rollbackEx.getMessage());
                    }

                    try {
                        if (conn != null) {
                            conn.close();
                        }
                        if (stmt != null) {
                            stmt.close();
                        }
                    } catch (SQLException closeEx) {
                        System.out.println("Closing connection failed: " + closeEx.getMessage());
                    }

                    conn = null;
                    rset = null;
                    qrCodeFile = null;
                    signFile = null;
                    addCondition = "";
                }
            }
        }
        return resultMap;
    }    
   
    private void setDriver() throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            dataBaseURL = serverProperties.getProperty("url");
            userName = serverProperties.getProperty("username");
            passWord = serverProperties.getProperty("password");
            driverName = serverProperties.getProperty("driver");
            Class.forName(serverProperties.getProperty("driver"));
            serverProperties = new java.util.Properties();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            SERVER_ADDRESS = "http://" + CommonConstants.SERVER_IP_ADDR + ":" + serverProperties.getProperty("HTTP_PORT") + "/" + serverProperties.getProperty("HTTP_CONTEXT");
            System.out.println("#### SERVER_ADDRESS : " + SERVER_ADDRESS);
            serverProperties = null;
            cons = null;
        } catch (Exception ex) {
        }
    }
    
    private HashMap getQRFile(HashMap qrFileMap) throws Exception {
        HashMap resultMap = new HashMap();
        FileOutputStream out = null;
        InputStream in = null;
        File f1 = null;
        try {
            setDriver();
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
            conn.setAutoCommit(false);

            String tableName = "UPI_QR_DETAILS";
            String tableCondition = "QR_ACT_NUM";

            String qrbank = CommonUtil.convertObjToStr(qrFileMap.get("QR_BANK"));
            String whereCondition = CommonUtil.convertObjToStr(qrFileMap.get("QR_ACT_NUM"));

            String sql = "SELECT " + tableCondition + ", QR_FILE FROM " + tableName
                    + " WHERE " + tableCondition + " = ? AND QR_BANK = ?";
            System.out.println("#### Prepared statement to be executed: " + sql);

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, whereCondition);
            pstmt.setString(2, qrbank);

            ResultSet rset = pstmt.executeQuery();

            if (rset.next()) {
                System.out.println("#### Record found");

                byte[] blobBytes = rset.getBytes("QR_FILE");

                if (blobBytes != null && blobBytes.length > 0) {
                    System.out.println("#### Cust ID: " + rset.getString(1));
                    System.out.println("QR file byte length: " + blobBytes.length);

                    // Create file path
                    String fileName = whereCondition + "_" + qrbank + "_QRCode.jpg";
                    f1 = new File(ServerConstants.SERVER_PATH + File.separator + "QRCode" + File.separator + fileName);
                    f1.getParentFile().mkdirs();  // Ensure directory exists
                    f1.createNewFile();

                    // Write bytes to file
                    out = new FileOutputStream(f1);
                    out.write(blobBytes);
                    out.flush();

                    resultMap.put("QR_FILE", blobBytes);
                    resultMap.put("QR_FILE_PATH", f1.getAbsolutePath());
                }
            }

            resultMap.put("QR_ACT_NUM", qrFileMap.get("QR_ACT_NUM"));
            resultMap.put("QR_BANK", qrbank);
            resultMap.put("QR_DETAILS", qrFileMap.get("QR_DETAILS"));

            // Cleanup
            if (out != null) {
                out.close();
            }
            if (rset != null) {
                rset.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SQL Exception: " + e.getMessage());

            try {
                if (out != null) {
                    out.close();
                }
                if (rset != null) {
                    rset.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                System.out.println("Cleanup failed: " + ex.getMessage());
            }
        }
        return resultMap;
    }

    private void generateQRFileImage(HashMap qrCodeMap){
        System.out.println("qrCodeMap passed:: " + qrCodeMap);
        try {
            String qrbank = CommonUtil.convertObjToStr(qrCodeMap.get("QR_BANK"));
            StringEncrypter encrypt = new StringEncrypter();                             
            String filePath = ServerConstants.SERVER_PATH+"/QRCode/"+qrCodeMap.get("QR_ACT_NUM")+"_"+qrbank +"_QRCode.png";
            String logoPath = ServerConstants.SERVER_PATH+"/QRCode/fincuro_solutions_pvt_ltd_logo.jpg";
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);            
            String upiUri = String.format("upi://pay?pa=%s&pn=%s&cu=INR", CommonUtil.convertObjToStr(qrCodeMap.get("UPI_ID")), CommonUtil.convertObjToStr(qrCodeMap.get("UPI_NAME")));
            //createQRCode_withimage(upiUri, filePath, charset, hintMap, 300, 300,qrCodeMap,logoPath);
            createQRCode(upiUri, filePath, charset, hintMap, 300, 300,qrCodeMap);
            qrCodeFile = new File(ServerConstants.SERVER_PATH+"/QRCode/"+qrCodeMap.get("QR_ACT_NUM")+"_"+qrbank +"_QRCode.png");
            System.out.println("filePath : "+filePath+" qrCodeFile : "+qrCodeFile);              
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(QRCodeUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void createQRCode_withimage(String qrCodeData, String filePath,
            String charset, Map hintMap, int qrCodeHeight, int qrCodeWidth,
            HashMap qrCodeMap, String logoPath) // added logoPath
            throws WriterException, IOException, SQLException {

   
        BitMatrix matrix = new MultiFormatWriter()
                .encode(new String(qrCodeData.getBytes(charset), charset),
                        BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight, hintMap);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);


        String[] footerLines = {
            "Name : " + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_NAME")),
            "A/c No : " + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_BANK_CODE"))
            + CommonUtil.convertObjToStr(qrCodeMap.get("QR_ACT_NUM")),
            "IFSC : " + CommonUtil.convertObjToStr(qrCodeMap.get("QR_IFSC")),
            "UPI ID : " + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_ID"))
        };

 
        int fontSize = 12;
        Font plainFont = new Font("Arial", Font.PLAIN, fontSize);
        Font colonFont = new Font("Arial", Font.BOLD, fontSize);
        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D gtmp = tmp.createGraphics();
        FontMetrics fmPlain = gtmp.getFontMetrics(plainFont);
        gtmp.dispose();

        int lineHeight = fmPlain.getHeight();
        int textBlockHeight = footerLines.length * lineHeight + fmPlain.getDescent();

 
        BufferedImage logo = ImageIO.read(new File(logoPath));
        int logoWidth = Math.min(logo.getWidth(), qrCodeWidth / 3);
        int logoHeight = (logo.getHeight() * logoWidth) / logo.getWidth();

     
        int finalWidth = qrCodeWidth;
        int finalHeight = qrCodeHeight + textBlockHeight + 5 + logoHeight + 5;
        BufferedImage output = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = output.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, finalWidth, finalHeight);
        g.drawImage(qrImage, 0, 0, null);

  
        int y = qrCodeHeight + fmPlain.getAscent();
        for (String line : footerLines) {
            int cp = line.indexOf(":") + 1;
            String first = line.substring(0, cp), rest = line.substring(cp);

            g.setFont(colonFont);
            FontMetrics fmColon = g.getFontMetrics();
            int wFirst = fmColon.stringWidth(first);
            int wRest = fmPlain.stringWidth(rest);
            int xStart = (finalWidth - (wFirst + wRest)) / 2;

            g.setColor(Color.RED);
            g.drawString(first, xStart, y);

            g.setFont(plainFont);
            g.setColor(Color.BLACK);
            g.drawString(rest, xStart + wFirst, y);

            y += lineHeight;
        }

   
        int logoX = (finalWidth - logoWidth) / 2;
        int logoY = qrCodeHeight + textBlockHeight + 5;
        g.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);

        g.dispose();
        ImageIO.write(output, "PNG", new File(filePath));
    }

    
    public static void createQRCode(String qrCodeData, String filePath,
            String charset, Map hintMap, int qrCodeheight, int qrCodewidth, HashMap qrCodeMap)
            throws WriterException, IOException, SQLException {

        // Generate QR code image
        BitMatrix matrix = new MultiFormatWriter()
                .encode(new String(qrCodeData.getBytes(charset), charset),
                        BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);

        // Prepare footer lines
        String[] footerLines = {
            "Name : " + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_NAME")),
            "A/c No : " + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_BANK_CODE")) + CommonUtil.convertObjToStr(qrCodeMap.get("QR_ACT_NUM")),
            "IFSC : " + CommonUtil.convertObjToStr(qrCodeMap.get("QR_IFSC")),
            "UPI ID : " + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_ID"))
        };

        int fontSize = 12;
        Font plainFont = new Font("Arial", Font.PLAIN, fontSize);
        Font colonFont = new Font("Arial", Font.BOLD, fontSize);
        BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D gt = temp.createGraphics();
        gt.setFont(plainFont);
        FontMetrics fmPlain = gt.getFontMetrics();
        gt.dispose();

        int lineHeight = fmPlain.getHeight();
        int totalTextHeight = footerLines.length * lineHeight + fmPlain.getDescent();
        int finalW = qrCodewidth;
        int finalH = qrCodeheight + totalTextHeight + 10;
        BufferedImage out = new BufferedImage(finalW, finalH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, finalW, finalH);
        g.drawImage(MatrixToImageWriter.toBufferedImage(
                new MultiFormatWriter().encode(
                        new String(qrCodeData.getBytes(charset), charset),
                        BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap)),
                (finalW - qrCodewidth) / 2, 0, null);

        int y = qrCodeheight + fmPlain.getAscent() - 2;
        for (String line : footerLines) {
            int colPos = line.indexOf(":") + 1;  // include colon
            String first = line.substring(0, colPos);
            String rest = line.substring(colPos);

            // Compute positions
            g.setFont(colonFont);
            FontMetrics fmColon = g.getFontMetrics();
            int widthFirst = fmColon.stringWidth(first);
            int widthRest = fmPlain.stringWidth(rest);
            int xStart = (finalW - (widthFirst + widthRest)) / 2;

            // Draw colon part in bold
            g.setColor(Color.RED);
            g.drawString(first, xStart, y);

            // Draw rest part in plain
            g.setFont(plainFont);
            g.setColor(Color.BLACK);
            g.drawString(rest, xStart + widthFirst, y);

            y += lineHeight;
        }

        g.dispose();
        ImageIO.write(out, "PNG", new File(filePath));
    }

    
    
    public static void createQRCode_backup(String qrCodeData, String filePath,
            String charset, Map hintMap, int qrCodeheight, int qrCodewidth, HashMap qrCodeMap)
            throws WriterException, IOException, SQLException {

        System.out.println("qrCodeData :: " + qrCodeData);

        // Get bank name
        String bankName = "";
        List bankList = sqlMap.executeQueryForList("getSelectBankTOList", null);
        if (bankList != null && bankList.size() > 0) {
            HashMap bankMap = (HashMap) bankList.get(0);
            bankName = CommonUtil.convertObjToStr(bankMap.get("BANK_NAME"));
        }

        // Create QR code image
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);

        // Prepare footer lines
        String[] footerLines = {
            "Name : " + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_NAME")),
            "A/c No : " + CommonUtil.convertObjToStr(qrCodeMap.get("QR_ACT_NUM")),
            "IFSC : " + CommonUtil.convertObjToStr(qrCodeMap.get("QR_ACT_NUM")),
            "UPI ID : " + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_ID"))
            
        };

        // Use real Graphics2D to calculate font metrics
        int fontSize = 16;
        Font font = new Font("Arial", Font.PLAIN, fontSize);

        // Create temporary image for accurate FontMetrics
        BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D tempGraphics = tempImg.createGraphics();
        tempGraphics.setFont(font);
        FontMetrics metrics = tempGraphics.getFontMetrics();

        int maxTextWidth = qrCodewidth - 20;

        // Reduce font size if necessary
        for (String line : footerLines) {
            while (metrics.stringWidth(line) > maxTextWidth && fontSize > 8) {
                fontSize--;
                font = new Font("Arial", Font.PLAIN, fontSize);
                tempGraphics.setFont(font);
                metrics = tempGraphics.getFontMetrics();
            }
        }

        int lineHeight = metrics.getHeight();
        int totalTextHeight = footerLines.length * lineHeight + metrics.getDescent();

        tempGraphics.dispose();

        // Final image with room for footer
        int finalImageWidth = qrCodewidth;
        int finalImageHeight = qrCodeheight + totalTextHeight + 10;
        BufferedImage finalImage = new BufferedImage(finalImageWidth, finalImageHeight, BufferedImage.TYPE_INT_RGB);

        // Draw everything
        Graphics2D g = finalImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, finalImageWidth, finalImageHeight);

        g.drawImage(qrImage, (finalImageWidth - qrCodewidth) / 2, 0, null);

        g.setColor(Color.BLACK);
        g.setFont(font);
        metrics = g.getFontMetrics();  // Important: use actual graphics context

        int y = qrCodeheight + metrics.getAscent() - 2;
        for (String line : footerLines) {
            int lineWidth = metrics.stringWidth(line);
            g.drawString(line, (finalImageWidth - lineWidth) / 2, y);
            y += lineHeight;
        }

        g.dispose();

        // Save the image
        ImageIO.write(finalImage, "PNG", new File(filePath));
    }

    
    
    public static void createQRCode_old(String qrCodeData, String filePath,
            String charset, Map hintMap, int qrCodeheight, int qrCodewidth, HashMap qrCodeMap)
            throws WriterException, IOException, SQLException {
        System.out.println("qrCodeData :: " + qrCodeData);
        String bankName = "";
        List bankList = sqlMap.executeQueryForList("getSelectBankTOList", null);
        if (bankList != null && bankList.size() > 0) {
            HashMap bankMap = (HashMap) bankList.get(0);
            bankName = CommonUtil.convertObjToStr(bankMap.get("BANK_NAME"));
        }
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        //MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), new File(filePath));
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);

        // Text to add below QR code
        String footerText = CommonUtil.convertObjToStr(qrCodeMap.get("QR_ACT_NUM")) +"\n" + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_ID")) 
                +"\n" + CommonUtil.convertObjToStr(qrCodeMap.get("UPI_NAME"));
        System.out.println("footerText :: " + footerText);
//        Font font = new Font("Arial", Font.PLAIN, 16);
//        FontMetrics metrics = new Canvas().getFontMetrics(font);
//        int textHeight = metrics.getHeight();
//        int textWidth = metrics.stringWidth(footerText);
        
        int maxTextWidth = qrCodewidth - 20; // leave some padding
        int fontSize = 16;
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        FontMetrics metrics = new Canvas().getFontMetrics(font);
        int textWidth = metrics.stringWidth(footerText);

// Decrease font size until text fits
        while (textWidth > maxTextWidth && fontSize > 8) {
            fontSize--;
            font = new Font("Arial", Font.PLAIN, fontSize);
            metrics = new Canvas().getFontMetrics(font);
            textWidth = metrics.stringWidth(footerText);
        }
        int textHeight = metrics.getHeight();

        // Create final image with space for text
        int finalImageWidth = Math.max(qrCodewidth, textWidth + 20);
        int finalImageHeight = qrCodeheight + textHeight + 10;
        BufferedImage finalImage = new BufferedImage(finalImageWidth, finalImageHeight, BufferedImage.TYPE_INT_RGB);

        // Draw on the final image
        Graphics2D g = finalImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, finalImageWidth, finalImageHeight);
        g.drawImage(qrImage, (finalImageWidth - qrCodewidth) / 2, 0, null);

        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString(footerText, (finalImageWidth - textWidth) / 2, qrCodeheight + textHeight); // draw below QR

        g.dispose();

        // Save the final image
        ImageIO.write(finalImage, "PNG", new File(filePath));
    }

    public static String readQRCode(String filePath, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                hintMap);
        return qrCodeResult.getText();
    }
    
    private String generateOTP(){
        Random r = new Random();
        String otp = new String();
        for(int i=0 ; i < 6 ; i++) {
            otp += r.nextInt(10);
        }
        return otp;
    }
    
    private String getActualQRBankCode() throws Exception{//This query to get actual bank code from branch_master table, for that hardcoded this branch code
        String qrBankCode = "";
        HashMap qrBankCodeMap = new HashMap();
        List lst = sqlMap.executeQueryForList("getBankCodeDetails", null);
        if (lst != null && lst.size() > 0) {
            qrBankCodeMap = (HashMap)lst.get(0);
            qrBankCode = CommonUtil.convertObjToStr(qrBankCodeMap.get("BANK_CODE"));
            System.out.println("qrBankCode : "+qrBankCode);
        }
        return qrBankCode;
    }
}
