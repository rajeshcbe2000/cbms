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
package com.see.truetransact.serverside.common.report;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;

import com.see.truetransact.commonutil.Dummy;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.Vector;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Date;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

//import com.see.truetransact.transferobject.sysadmin.passbookparams.PassBookParamsTO;
import com.see.reporttool.bean.ReportBean;
import com.see.common.tools.treebuilder.util.ParameterBean;
import com.see.iie.utils.EnvironmentVariables;
import com.see.iie.utils.Postfix;
import com.see.iie.utils.ParsingError;
import com.see.iie.utils.UNGStringTokenizer;
import com.see.iie.utils.ServerConstants;
import com.see.iie.engine.dao.datasources.SeEDataSource;
import com.see.iie.engine.dao.datasources.DataSourceFactory;

import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Balachandar
 */
public class GenerateReportDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private EnvironmentVariables env = new EnvironmentVariables();
    private String envStr = (String) env.get("IIE_HOME");
    ArrayList dataType;
    ArrayList data;
    LinkedHashMap index;
    ArrayList colNames = new ArrayList();
    ArrayList outputColumnNames;
    ArrayList outputDataTypes;
    Date currDt = null;
    String branchID;

    private HashMap getData(HashMap map) throws Exception {
        String mapName = (String) map.get(CommonConstants.MAP_NAME);
        branchID = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(branchID);

        LinkedHashMap where = null;
        String strWhere = "";
        if (map.containsKey(CommonConstants.MAP_WHERE)) {
            if (map.get(CommonConstants.MAP_WHERE) instanceof HashMap) {
                where = (LinkedHashMap) map.get(CommonConstants.MAP_WHERE);
            } else {
                strWhere = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE));
            }
        }

        if (mapName.trim().length() == 0) {
            throw new TTException("Map Name is Null");
        }

        List list = null;
        byte[] sheet = null;
        System.out.println("!!!!! mapName : " + mapName + " / where : " + where + " / strWhere : " + strWhere);
        if (mapName.equals("GET_REPORT_TABLE_MODEL")) {
            list = getReportTableModel(where);
        } else if (mapName.equals("GET_REPORT_PARAMS")) {
            list = getReportParams(where);
        } else if (mapName.equals("GET_JASPER_REPORT_PARAMS")) {
            list = getJasperReportParams(where);
        } else if (mapName.equals("GET_JASPER_REPORT_PRINT")) {
            JasperDesign jasperDesign = (JasperDesign) map.get("JASPER_DESIGN");
            list = getJasperReportPrint(where, jasperDesign);
            if(map.containsKey("CHECK_BOX") && CommonUtil.convertObjToStr(map.get("CHECK_BOX")).equals("true")){
                sheet = getJasperReportExcelPrint(where, jasperDesign,CommonUtil.convertObjToStr(map.get("REPORT_NAME")),CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            }
        } else if (mapName.equals("GET_JASPER_REPORT_PARAMS_FOR_PASSBOOK")) {
            list = getJasperReportPassbook(where);
        } else if (mapName.equals("GET_JASPER_REPORT_PARAMS_FOR_RP")) {
            list = getJasperReportPassbook(where);
        }
        where = new LinkedHashMap();
        where.put(CommonConstants.DATA, list);
        if(map.containsKey("CHECK_BOX") && CommonUtil.convertObjToStr(map.get("CHECK_BOX")).equals("true")){
            where.put("EXCEL_OUTPUT", sheet);
        }
        if (mapName.equals("GET_REPORT_TABLE_MODEL")) {
            System.out.println("!!!!! returning where : " + where);
        }
        return where;
    }

    private List getJasperReportParams(HashMap where) throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        com.see.truetransact.commonutil.Dummy cons = new com.see.truetransact.commonutil.Dummy();
        serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            cons = null;
//            java.sql.Connection jdbcConnection = connectDB(serverProperties.getProperty("url"),
                    serverProperties.getProperty("REPORT_TEMPLATE");
//                    serverProperties.getProperty("password"));
        String clientTemplatePath = serverProperties.getProperty("REPORT_TEMPLATE");//ServerConstants.TT_TEMPLATE_PATH + "\\" + bankCode;
//        String clientTemplatePath = ServerConstants.TT_TEMPLATE_PATH + "\\" + bankCode;

        System.out.println("where########:" + where);
        System.out.println("clientTemplatePath:" + clientTemplatePath);
        String reportname = CommonUtil.convertObjToStr(where.get("REPORT_NAME"));
        String roleID = CommonUtil.convertObjToStr(where.get("ROLE_ID"));
//        Changed by Nikhil
        String userID = CommonUtil.convertObjToStr(where.get("USER_ID"));
        System.out.println("@#$@#$@#$userID:" + userID);
        boolean loanKey = false;
        if (where.containsKey("LOAN_NOTICE_KEY")) {
            loanKey = true;
//            System.out.println ("inside if check");
        }
//        JasperDesign jasperDesign = JRXmlLoader.load(clientTemplatePath + "\\" + reportname + ".jrxml");
//        File file = new File(clientTemplatePath + "\\" + reportname + ".jrxml");
          JasperDesign jasperDesign = JRXmlLoader.load(clientTemplatePath + reportname + ".jrxml");
          File file = new File(clientTemplatePath + reportname + ".jrxml");
          System.out.println("AbsolutePath"+file.getAbsolutePath());
          System.out.println("getPath"+file.getPath());
       
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        String query = "";
        String finalQuery = "";
        fis = new FileInputStream(file);

        // Here BufferedInputStream is added for fast reading.
        bis = new BufferedInputStream(fis);
        dis = new DataInputStream(bis);

        // dis.available() returns 0 if the file does not have more lines.
        while (dis.available() != 0) {

            // this statement reads the line from the file and print it to
            // the console.
            query += dis.readLine() + "\n";
        }
        System.out.println("Finished reading XML file...\n");
        query = query.substring(query.indexOf("<queryString>") + "<queryString>".length(),
                query.indexOf("</queryString>")).trim();
//        System.out.println("query is : "+query);
        query = query.substring(query.indexOf("<![CDATA[") + "<![CDATA[".length(),
                query.indexOf("]]>")).toUpperCase();
        System.out.println("final query is : " + query);
        finalQuery += query;
        StringBuffer errorMsg = new StringBuffer("");
        String parameters[] = null;
        String tempParam[] = null;
        LinkedHashMap servParamMap = new LinkedHashMap();
        if (query.lastIndexOf("WHERE") != -1) {
            if (query.lastIndexOf("ORDER") != -1) {
                query = query.substring(query.lastIndexOf("WHERE") + "WHERE".length(),
                        query.lastIndexOf("ORDER")).trim();
            } else {
                query = query.substring(query.lastIndexOf("WHERE") + "WHERE".length()).trim();
            }
            System.out.println("\nparameters inside the query are : " + query);
            parameters = query.split("AND");
//            ArrayList paramList = new ArrayList(parameters);
            JRQueryChunk[] chunks = jasperDesign.getQuery().getChunks();
            java.util.Map parametersMap = jasperDesign.getParametersMap();
            if (parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    System.out.println("parameter " + i + " : " + parameters[i]);
                    if (parameters[i].indexOf("$P") != -1) {
                        String temp = parameters[i];
                        String strForOptional = parameters[i];
                        int k = 0;
                        while (temp.indexOf("$P") != temp.lastIndexOf("$P")) {
                            if (k == 0) {
                                parameters[i] = parameters[i].substring(0, parameters[i].indexOf("}") + 1);
                            } else {
                                parameters[parameters.length] = parameters[i].substring(0, parameters[i].indexOf("}") + 1);
                            }
                            temp = temp.substring(temp.indexOf(parameters[i]) + parameters[i].length(), temp.length());
                            k++;
                        }
                        if (k != 0) {
                            tempParam = new String[parameters.length + 1];
                            System.arraycopy(parameters, 0, tempParam, 0, parameters.length);
                            tempParam[parameters.length] = temp;
                            parameters = tempParam;
                        }
                        String paramName = parameters[i].substring(parameters[i].indexOf("{") + 1, parameters[i].indexOf("}"));
                        for (int j = 0; j < chunks.length; j++) {
                            JRQueryChunk queryChunk = chunks[j];
                            if ((queryChunk.getType() == JRQueryChunk.TYPE_PARAMETER
                                    || queryChunk.getType() == JRQueryChunk.TYPE_PARAMETER_CLAUSE)
                                    && queryChunk.getText().toUpperCase().equals(paramName)) {
                                JRParameter parameter = (JRParameter) parametersMap.get(queryChunk.getText());
//                                    System.out.println("\nJRQueryChunk.TYPE_PARAMETER : " + queryChunk.getText()+
//                                                        "\nqueryChunk.getType() : " + queryChunk.getType());
//                                    System.out.println("\nparameter : " + parameter.getName()+
//                                                        "\nparameter.getValueClassName() : " + parameter.getValueClassName());
                                JRExpression jre = null;
                                HashMap paramMap = new HashMap();
                                String tableName = "";
                                String columnName = "";
                                if (queryChunk.getType() == JRQueryChunk.TYPE_PARAMETER_CLAUSE && parameter.getDescription() != null) {
                                    jre = parameter.getDefaultValueExpression();
//                                        System.out.println("#$#$# jre.getText() : "+jre.getText());
//                                        String desc = parameter.getDescription();
                                    String desc = jre.getText();
                                    System.out.println("#$#$# desc : " + desc);
                                    if (desc.indexOf(".") != -1) {
                                        tableName = desc.substring(0, desc.indexOf(".")).trim();
                                        tableName = tableName.replace("\"", "").replaceAll("AND", "").replaceAll("OR", "").trim();
                                        columnName = desc.substring(desc.indexOf(".") + 1, desc.indexOf("=")).trim();
                                    } else {
                                        errorMsg.append("Table name not mentioned for parameter ");
                                        errorMsg.append(parameter.getName() + "\n");
                                        continue;
                                    }
                                }
                                if (queryChunk.getType() != JRQueryChunk.TYPE_PARAMETER_CLAUSE) {
                                    if (parameters[i].indexOf(".") != -1) {
                                        tableName = parameters[i].substring(0, parameters[i].indexOf(".")).trim();
                                        if (parameters[i].indexOf(">=") != -1) {
                                            columnName = parameters[i].substring(parameters[i].indexOf(".") + 1, parameters[i].indexOf(">=")).trim();
                                        } else if (parameters[i].indexOf("<=") != -1) {
                                            columnName = parameters[i].substring(parameters[i].indexOf(".") + 1, parameters[i].indexOf("<=")).trim();
                                        } else if (parameters[i].indexOf("<>") != -1) {
                                            columnName = parameters[i].substring(parameters[i].indexOf(".") + 1, parameters[i].indexOf("<>")).trim();
                                        } else if (parameters[i].indexOf(">") != -1) {
                                            columnName = parameters[i].substring(parameters[i].indexOf(".") + 1, parameters[i].indexOf(">")).trim();
                                        } else if (parameters[i].indexOf("<") != -1) {
                                            columnName = parameters[i].substring(parameters[i].indexOf(".") + 1, parameters[i].indexOf("<")).trim();
                                        } else if (parameters[i].indexOf("!=") != -1) {
                                            columnName = parameters[i].substring(parameters[i].indexOf(".") + 1, parameters[i].indexOf("!=")).trim();
                                        } else if (parameters[i].indexOf("=") != -1) {
                                            columnName = parameters[i].substring(parameters[i].indexOf(".") + 1, parameters[i].indexOf("=")).trim();
                                        } else if (parameters[i].toUpperCase().indexOf("IS") != -1) {
                                            columnName = parameters[i].substring(parameters[i].indexOf(".") + 1, parameters[i].toUpperCase().indexOf("IS")).trim();
                                        }
                                    } else {
                                        errorMsg.append("Table name not mentioned for parameter ");
                                        errorMsg.append(parameter.getName()+"\n");
                                        if (parameters[i].indexOf(">=")!=-1)
                                            columnName = parameters[i].substring(parameters[i].indexOf(".")+1,parameters[i].indexOf(">=")).trim();
                                        else if (parameters[i].indexOf("<=")!=-1)
                                            columnName = parameters[i].substring(parameters[i].indexOf(".")+1,parameters[i].indexOf("<=")).trim();
                                        else if (parameters[i].indexOf("<>")!=-1)
                                            columnName = parameters[i].substring(parameters[i].indexOf(".")+1,parameters[i].indexOf("<>")).trim();
                                        else if (parameters[i].indexOf(">")!=-1)
                                            columnName = parameters[i].substring(parameters[i].indexOf(".")+1,parameters[i].indexOf(">")).trim();
                                        else if (parameters[i].indexOf("<")!=-1)
                                            columnName = parameters[i].substring(parameters[i].indexOf(".")+1,parameters[i].indexOf("<")).trim();
                                        else if (parameters[i].indexOf("!=")!=-1)
                                            columnName = parameters[i].substring(parameters[i].indexOf(".")+1,parameters[i].indexOf("!=")).trim();
                                        else if (parameters[i].indexOf("=")!=-1)
                                            columnName = parameters[i].substring(parameters[i].indexOf(".")+1,parameters[i].indexOf("=")).trim();
                                        else if (parameters[i].toUpperCase().indexOf("IS")!=-1)
                                            columnName = parameters[i].substring(parameters[i].indexOf(".")+1,parameters[i].toUpperCase().indexOf("IS")).trim();
                                        System.out.println("!@!@ columnName without tablename:"+columnName);
//                                        continue;
                                    }
                                }
                                int dataType = 0;
                                if (parameter.getValueClassName().equals("java.lang.String")) {
                                    dataType = 12;
                                    if (!(columnName.equals("BRANCH_CODE") || columnName.equals("INITIATED_BRANCH")
                                            || columnName.equals("BRANCH_ID") || columnName.equals("ACT_NUM")
                                            || columnName.equals("ACCOUNT_NUMBER") || columnName.equals("ACC_NO")
                                            || columnName.equals("CUSTOMER_ID") || columnName.equals("CUST_ID")
                                            || columnName.equals("DEPOSIT_NO")||columnName.equals("ACCT_NUM"))) {
//                                                ||columnName.equals("CUSTOMER_ID")||columnName.equals("CUST_ID")||(loanKey))){
                                        System.out.println("laonKe@@@@y" + loanKey+":columnname:"+columnName);
                                        if (!loanKey && tableName.length() > 0) {
                                            HashMap whereMap = new HashMap();
                                            whereMap.put("TABLE_NAME", tableName);
                                            whereMap.put("COLUMN_NAME", columnName);
                                            ArrayList repData = (ArrayList) sqlMap.executeQueryForList("getReportData", whereMap);
                                            if (repData != null && repData.size() > 0) {
                                                paramMap.put("PARAM_LIST", repData);
                                            }
                                            repData = null;
                                        }
                                    }
                                    if (columnName.equals("BRANCH_CODE") || columnName.equals("INITIATED_BRANCH")
                                            || columnName.equals("BRANCH_ID")) {
                                        paramMap.put("PARAM_VALUE", branchID);
                                    }
                                } else if (parameter.getValueClassName().equals("java.util.Date")) {
                                    dataType = 93;
                                    String strDate = DateUtil.getStringDate(currDt);
                                    paramMap.put("PARAM_VALUE", strDate);
                                } else if (parameter.getValueClassName().equals("java.lang.Boolean")) {
                                    dataType = 1;
                                } else if (columnName.equals("USER_ID")) {
                                    //ADDED BY NIKHIL
                                    paramMap.put("PARAM_VALUE", userID);
                                } else {
                                    dataType = 2;
                                }
                                paramMap.put("DATA_TYPE", new Integer(dataType));
                                paramMap.put("TABLE_NAME", tableName);
                                paramMap.put("COLUMN_NAME", columnName);
                                paramMap.put("VALUE_CLASS", parameter.getValueClassName());
                                paramMap.put("OPTIONAL", new Boolean(parameter.isForPrompting()));
                                paramMap.put("STR_FOR_OPTIONAL", strForOptional.replaceAll(parameter.getName().toUpperCase(), parameter.getName()));
                                paramMap.put("PARAM_NAME", parameter.getName());
                                servParamMap.put(parameter.getName(), paramMap);
                            }
                        }
                    }
                }
            }
        }
//        System.out.println("servParamMap : " + servParamMap);
        // dispose all the resources after using them.
        file = null;
        fis.close();
        bis.close();
        dis.close();

        System.out.println("roleID : " + roleID);
        List lst = new ArrayList();

        where = new LinkedHashMap();
        where.put("PARAM_MAP", servParamMap);
        where.put("CLIENT_OBJECT", jasperDesign);
        lst.add(where);
        Runtime.getRuntime().gc();
        return lst;
    }

    private List getJasperReportPrint(LinkedHashMap where, JasperDesign jasperDesign) throws Exception {
        List lst = new ArrayList();
        try {
            System.out.println("jasperDesign.getQuery() : " + jasperDesign.getQuery().getText());
            JRDesignQuery jrQuery = new JRDesignQuery();
            String queryText = jasperDesign.getQuery().getText();
            String strForOptional = "";
            ArrayList removableKeys = new ArrayList();
            // The following code set added to remove optional parameter condition 
            // if no value given for the optional parameter
            if (where != null && where.size() > 0) {
                Object keys[] = where.keySet().toArray();
                HashMap tempMap = new HashMap();
                for (int i = 0; i < keys.length; i++) {
                    if (where.get(keys[i]) instanceof HashMap) {
                        tempMap = (HashMap) where.get(keys[i]);
                        if (tempMap.containsKey("STR_FOR_OPTIONAL")) {
                            strForOptional = CommonUtil.convertObjToStr(tempMap.get("STR_FOR_OPTIONAL")).trim();
                            queryText = queryText.replace('\n', ' ');
                            while (queryText.indexOf("  ") != -1) {
                                queryText = queryText.replaceAll("  ", " ");
                            }
                            strForOptional = strForOptional.replace('\n', ' ');
                            while (strForOptional.indexOf("  ") != -1) {
                                strForOptional = strForOptional.replaceAll("  ", " ");
                            }
                            // That will look for two or more of any whitespace characters, or a single linefeed 
                            // or carriage return, and replace them with a single space.
//                            System.out.println("#$##$ queryText after removing extra spece, line feed & carriage return : "+queryText);
//                            System.out.println("$#$@ strForOptional : "+strForOptional);
                            if (queryText.toUpperCase().indexOf("WHERE") != -1) {
//                                System.out.println("$#$@ inside if (queryText.toUpperCase().indexOf(\"WHERE\")!=-1)");
                                if (queryText.toUpperCase().indexOf("AND") != -1) {
//                                    System.out.println("$#$@ inside if (queryText.toUpperCase().indexOf(\"AND\")!=-1)");
                                    if (queryText.toUpperCase().indexOf(strForOptional.toUpperCase() + " AND") != -1) {
                                        queryText = queryText.replace(strForOptional + " AND", " ");
                                        queryText = queryText.replace(strForOptional + " and", " ");
//                                        System.out.println("$#$@ inside if (queryText.toUpperCase().indexOf(strForOptional.toUpperCase()+\" AND\")!=-1)");
                                    } else if (queryText.toUpperCase().indexOf("AND " + strForOptional.toUpperCase()) != -1) {
                                        queryText = queryText.replace("AND " + strForOptional, " ");
                                        queryText = queryText.replace("and " + strForOptional, " ");
//                                        System.out.println("$#$@ inside else part of if (queryText.toUpperCase().indexOf(strForOptional.toUpperCase()+\" AND\")!=-1)");
                                    } else // More conditions to be added
                                    {
                                        queryText = queryText;
                                    }
                                } else {
//                                    System.out.println("$#$@ inside else part of if (queryText.toUpperCase().indexOf(\"AND\")!=-1)");
                                    queryText = queryText.replace(strForOptional, " ");
                                    queryText = queryText.toUpperCase().replace("WHERE", "");
                                }
                            }
                            System.out.println("#$##$ queryText after removing optional parameters : " + queryText);
                            removableKeys.add(keys[i]);
                        }
                    }
                }
                jrQuery.setText(queryText);
                jasperDesign.setQuery(jrQuery);
            }
            for (int i = 0; i < removableKeys.size(); i++) {
                where.remove(removableKeys.get(i));
            }
            System.out.println("#$##$ final where : " + where);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            java.util.Properties serverProperties = new java.util.Properties();
            // create the proxy
            com.see.truetransact.commonutil.Dummy cons = new com.see.truetransact.commonutil.Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            cons = null;
            java.sql.Connection jdbcConnection = connectDB(serverProperties.getProperty("url"),
                    serverProperties.getProperty("username"),
                    serverProperties.getProperty("password"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, where, jdbcConnection);
//            net.sf.jasperreports.engine.JRVariable jrv[] = jasperReport.getVariables();
//            for (int jrvI = 0 ; jrvI<jrv.length; jrvI++) {
//                net.sf.jasperreports.engine.JRVariable jr = jrv[jrvI];
//                net.sf.jasperreports.engine.JRExpressionChunk jec[] =  jr.getExpression().getChunks();
//                for (int jrc = 0; jrc<jec.length; jrc++) {
//                    System.out.println("#$#$ @@@ JRVariable "+jrvI+" : "+jec[0].getText());
//                }
//            }
            System.out.println("#$#$###### Inside GenerateReportDAO slno Map : " + com.see.truetransact.uivalidation.CommonReportUtil.getLastSlno());
            lst.add(jasperPrint);
            if (CommonUtil.convertObjToStr(where.get("REPORT_NAME")).equals("PassBook")||CommonUtil.convertObjToStr(where.get("REPORT_NAME")).equals("GN_OA_PASSBKF")
                    ||CommonUtil.convertObjToStr(where.get("REPORT_NAME")).equals("GN_OA_PASSBK")) {
                if (com.see.truetransact.uivalidation.CommonReportUtil.getLastSlno().size() > 0
                        && com.see.truetransact.uivalidation.CommonReportUtil.getLastSlno().containsKey(where.get("Acct_Num"))) {
                    lst.add(com.see.truetransact.uivalidation.CommonReportUtil.getLastSlno().get(where.get("Acct_Num")));
//                    System.out.println("#$#$###### Inside GenerateReportDAO lst before remove : "+lst);
                    com.see.truetransact.uivalidation.CommonReportUtil.getLastSlno().remove(where.get("Acct_Num"));
//                    System.out.println("#$#$###### Inside GenerateReportDAO lst after remove : "+lst);
//                    System.out.println("#$#$###### Inside GenerateReportDAO slno Map after remove : "+com.see.truetransact.uivalidation.CommonReportUtil.getLastSlno());
                }
            }
            jdbcConnection.close();
            jdbcConnection = null;
            cons = null;
            serverProperties = null;
            jasperReport = null;
            Runtime.getRuntime().gc();
        } catch (Exception ex) {
            System.out.println("#$#$ Exception in GenerateReportDAO getReportTableModel() : " + ex);
            ex.printStackTrace();
        }
        return lst;
    }

    private List getJasperReportPassbook(LinkedHashMap where) throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        com.see.truetransact.commonutil.Dummy cons = new com.see.truetransact.commonutil.Dummy();
        serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            cons = null;
//            java.sql.Connection jdbcConnection = connectDB(serverProperties.getProperty("url"),
                    serverProperties.getProperty("REPORT_TEMPLATE");
//                    serverProperties.getProperty("password"));
        String clientTemplatePath = serverProperties.getProperty("REPORT_TEMPLATE");//ServerConstants.TT_TEMPLATE_PATH + "\\" + bankCode;

        System.out.println("clientTemplatePath:" + clientTemplatePath);
        String reportname = CommonUtil.convertObjToStr(where.get("REPORT_NAME"));
        String roleID = CommonUtil.convertObjToStr(where.get("ROLE_ID"));

//        JasperDesign jasperDesign = JRXmlLoader.load(clientTemplatePath + "\\" + reportname + ".jrxml");
          JasperDesign jasperDesign = JRXmlLoader.load(clientTemplatePath + reportname + ".jrxml");
//          List lst = sqlMap.executeQueryForList("getSelectPassBookParamsTO", null);
//          if (lst!=null && lst.size()>0) {
//              PassBookParamsTO objPassBookParamsTO = (PassBookParamsTO) lst.get(0);
//              jasperDesign.setColumnCount(1);
//              jasperDesign.setTopMargin(CommonUtil.convertObjToInt(objPassBookParamsTO.getTopMargin()));
//              jasperDesign.setBottomMargin(CommonUtil.convertObjToInt(objPassBookParamsTO.getBottomMargin()));
//              jasperDesign.setPageHeight(CommonUtil.convertObjToInt(objPassBookParamsTO.getPageHeight()));
//              jasperDesign.setPageWidth(CommonUtil.convertObjToInt(objPassBookParamsTO.getPageWidth()));
//          }
        List lst = getJasperReportPrint(where, jasperDesign);
        return lst;
    }

    /**
     * Takes 3 parameters: databaseName, userName, password and connects to the
     * database.
     *
     * @param databaseName holds database name,
     * @param userName holds user name
     * @param password holds password to connect the database,
     * @return Returns the JDBC connection to the database
     */
    public static java.sql.Connection connectDB(String databaseName, String userName, String password) {
        java.sql.Connection jdbcConnection = null;
        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            java.sql.DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
            jdbcConnection = java.sql.DriverManager.getConnection(databaseName, userName, password);
        } catch (Exception ex) {
            String connectMsg = "Could not connect to the database: " + ex.getMessage() + " " + ex.getLocalizedMessage();
            System.out.println(connectMsg);
        }
        return jdbcConnection;
    }

    private List getReportParams(HashMap where) throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        com.see.truetransact.commonutil.Dummy cons = new com.see.truetransact.commonutil.Dummy();
        serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            cons = null;
//            java.sql.Connection jdbcConnection = connectDB(serverProperties.getProperty("url"),
                    serverProperties.getProperty("REPORT_TEMPLATE");
//                    serverProperties.getProperty("password"));
        String clientTemplatePath = serverProperties.getProperty("REPORT_TEMPLATE");//ServerConstants.TT_TEMPLATE_PATH + "\\" + bankCode;
//        String clientTemplatePath = ServerConstants.TT_TEMPLATE_PATH + "\\" + bankCode;

        System.out.println("clientTemplatePath:" + clientTemplatePath);

        Object obj = null;
        LinkedHashMap dynamicParamMap = null;
        String reportname = CommonUtil.convertObjToStr(where.get("REPORT_NAME"));
        String roleID = CommonUtil.convertObjToStr(where.get("ROLE_ID"));
//        obj = RnWReportTemplate.read(clientTemplatePath+"\\Clientside\\"+reportname+"_ClientConfig");
        String path = clientTemplatePath + "\\Clientside\\" + reportname + "_ClientConfig";
        System.out.println("GenerateReportDAO ... Client template : " + path);
        java.io.FileInputStream filestream = new java.io.FileInputStream(new java.io.File(path));
        java.io.ObjectInputStream oos = null;
        if (filestream != null) {
            oos = new java.io.ObjectInputStream(filestream);
        }
        Object reporttemplate = null;

        //Read serialized templates from disk
        if (oos != null) {
            obj = oos.readObject();
        }
        System.out.println("Client template read successfully..");

        path = clientTemplatePath + "\\Serverside\\" + reportname + "_ServerConfig";
        System.out.println("GenerateReportDAO ... Server template : " + path);
        filestream = new java.io.FileInputStream(new java.io.File(path));
        if (filestream != null) {
            oos = new java.io.ObjectInputStream(filestream);
        }
        Object objServ = null;
        //Read serialized templates from disk
        if (oos != null) {
            objServ = oos.readObject();
        }
        System.out.println("Server template read successfully..");
        System.out.println("GenerateReportDAO ... clientobj : " + obj + "   objServ : " + objServ);
        HashMap servParamMap = new HashMap();
        if (objServ != null && objServ instanceof com.see.iie.ui.IIEServerConfig) {
            com.see.iie.ui.IIEServerConfig iieServConf = (com.see.iie.ui.IIEServerConfig) objServ;
            for (int i = 0; i < iieServConf.getWhereModel().size(); i++) {
                ParameterBean pm = (ParameterBean) iieServConf.getWhereModel().getElementAt(i);
                if (pm != null && pm.getDataType() == 12) {
                    HashMap tempMap = new HashMap();
                    tempMap.put("COLUMN_NAME", pm.getParamColumnName());
                    tempMap.put("TABLE_NAME", pm.getParamTableName());
                    String paramWhere = pm.getParamWhere();
                    paramWhere = paramWhere.substring(1).toUpperCase();
                    servParamMap.put(paramWhere, tempMap);
                    tempMap = null;
                }
                pm = null;
            }
            iieServConf = null;
        } else {
            System.out.println("Server side report template file not found...");
        }
        System.out.println("servParamMap :" + servParamMap);

        if (obj instanceof ReportBean) {
            dynamicParamMap = (LinkedHashMap) ((ReportBean) obj).getReportPropertyBean().getLinkedParamMap();
        }
        System.out.println("roleID : " + roleID);
        List lst = new ArrayList();

        if (dynamicParamMap != null) {
            System.out.println("dynamicParamMap :" + dynamicParamMap);
            Object dynamicParamMapKey[] = null;
            dynamicParamMapKey = dynamicParamMap.keySet().toArray();
            int dynamicParamMapKeyCount = dynamicParamMapKey.length;
            int intCount = 0;
            for (int i = 0; i < dynamicParamMapKeyCount; i++) {
                if (!dynamicParamMapKey[i].toString().equalsIgnoreCase("reportname") && !(dynamicParamMap.get(dynamicParamMapKey[i].toString()) instanceof LinkedHashMap)) {
                    String paramName = dynamicParamMapKey[i].toString();
                    System.out.println("#4 #$ paramName:" + paramName);
                    if (dynamicParamMap.get(paramName) instanceof ParameterBean) {
                        ParameterBean paramBean = (ParameterBean) dynamicParamMap.get(paramName);
                        System.out.println("#$#$ paramBean.getDataType() : " + paramBean.getDataType());
                        if (paramBean.getDataType() == 91 || paramBean.getDataType() == 93) {
                            String strDate = DateUtil.getStringDate(currDt);
                            paramBean.setParamValue(strDate);
                        } else if (paramBean.getDataType() == 12) {
                            if (!(paramName.equals("BRANCH_CODE") || paramName.equals("INITIATED_BRANCH")
                                    || paramName.equals("BRANCH_ID") || paramName.equals("ACT_NUM")
                                    || paramName.equals("ACCOUNT_NUMBER") || paramName.equals("ACC_NO")
                                    || paramName.equals("CUSTOMER_ID") || paramName.equals("CUST_ID") || paramName.equals("ACCT_NUM"))) {
                                if (servParamMap.containsKey(paramName)) {
                                    HashMap whereMap = new HashMap();
                                    whereMap = (HashMap) servParamMap.get(paramName);
                                    ArrayList repData = (ArrayList) sqlMap.executeQueryForList("getReportData", whereMap);
                                    if (repData != null && repData.size() > 0) {
                                        paramBean.setAggregateFunc(repData);
                                    }
                                    repData = null;
//                                System.out.println("#$#$&&&$%# TTIntegration paramBean.getAggregateFunc() : "+paramBean.getAggregateFunc());
                                }
                            }
                        }
                        if (paramName.equals("BRANCH_CODE") || paramName.equals("INITIATED_BRANCH")
                                || paramName.equals("BRANCH_ID")) {
                            paramBean.setParamValue(branchID);
                        }
                        if (paramName.length() <= 0) {
                            dynamicParamMap.remove(paramBean.getParamName());
                        }
                        System.out.println("paramBean:" + paramBean);
                        paramBean = null;
                    }
                }
            }
            dynamicParamMapKey = null;
            where = new LinkedHashMap();
            where.put("PARAM_MAP", dynamicParamMap);
            where.put("CLIENT_OBJECT", obj);
            lst.add(where);
        } else {
            System.out.println("dynamic param is null");
        }
        Runtime.getRuntime().gc();
        return lst;
    }

    private List getReportTableModel(LinkedHashMap where) throws Exception {
        List lst = null;
        try {
            Object obj = null;
            String repname = "";
            LinkedHashMap parameters;
            ResultSet rsData;

            parameters = where;
            if (parameters.get("reportname") instanceof ParameterBean) {
                repname = ((ParameterBean) parameters.get("reportname")).getParamValue();
            } else {
                repname = (String) parameters.get("reportname");
            }

//                ClientContext clcnt = ClientContext.getInstance();
            String path = "";
            if (parameters.get("GroupId_RoleName_UserId") != null) {
                path = ((ParameterBean) parameters.get("GroupId_RoleName_UserId")).getParamValue();
            }

            path = envStr + java.io.File.separatorChar + "FileServer" + java.io.File.separatorChar + path + java.io.File.separatorChar + "ReportTemplates" + java.io.File.separatorChar + "Serverside" + java.io.File.separatorChar;
            path = path + repname + "_ServerConfig";
            System.out.println("path fetchdbdata " + path);
            java.io.FileInputStream filestream = new java.io.FileInputStream(new java.io.File(path));
            java.io.ObjectInputStream oos = null;
            if (filestream != null) {
                oos = new java.io.ObjectInputStream(filestream);
            }
            Object reporttemplate = null;

            //Read serialized templates from disk
            if (oos != null) {
                obj = oos.readObject();
            }

            System.out.println("Server template read successfully in fetchdb..");
            com.see.iie.ui.IIEServerConfig iieServConf = ((com.see.iie.ui.IIEServerConfig) obj);
            ReadReportServerConfig readServerConfig = new ReadReportServerConfig(iieServConf);
            LinkedHashMap lnkprop = new LinkedHashMap();
            lnkprop = readServerConfig.readIIEConfig();
            LinkedHashMap xmlProperties = (LinkedHashMap) lnkprop.get("OutputCols");
//                System.out.println("#$#$ xmlProperties "+xmlProperties);
            System.out.println("parameters " + parameters.getClass());
            System.out.println("(LinkedHashMap)parameters " + (LinkedHashMap) parameters);
            colNames = new ArrayList();
            Runtime.getRuntime().gc();
            rsData = readServerConfig.queryFormation(lnkprop, colNames, (LinkedHashMap) parameters);

            System.out.println("Got ResultSet");
            SeEDataSource dataSource = DataSourceFactory.create("ORACLE");
            dataType = null;
            data = null;
            index = null;
            Runtime.getRuntime().gc();
            dataType = dataSource.updateDataTypeAndColNames(rsData);
            Runtime.getRuntime().gc();
            System.out.println("Finished updateDataTypeAndColNames");
            data = dataSource.generateResult(rsData, colNames, parameters);
            Runtime.getRuntime().gc();
            System.out.println("Generated ResultSet");
            rsData.beforeFirst();
            index = dataSource.updateIndex(rsData, false);
            Runtime.getRuntime().gc();
            System.out.println("#$# data.size() :" + data.size());
            /* process data, to generate the actual evaluated values
             */
            ArrayList data = (ArrayList) processData(xmlProperties);
            Runtime.getRuntime().gc();
            /* update the index count for each primary key value
             */
            updateIndex(data);

            /* if some rows does not have data, fill NULL values
             */
            data = fillNULL(data);
            System.out.println("fillNULL finished ...");

            Runtime.getRuntime().gc();
            /* generate the eventual output and return it
             */
            String tableData = generateOutput(data);
            Runtime.getRuntime().gc();
            lst = new ArrayList();
            lst.add(getTableModel(tableData));
//                processData = null;
            dataType = null;
            data = null;
            iieServConf = null;
            readServerConfig = null;
            dataSource = null;
            lnkprop = null;
            xmlProperties = null;
            index = null;
            filestream = null;
            oos = null;
            Runtime.getRuntime().gc();
        } catch (Exception ex) {
            System.out.println("#$#$ Exception in GenerateReportDAO getReportTableModel() : " + ex);
            ex.printStackTrace();
        }
        return lst;
    }

    /**
     * This mehod will process the hash maps, based on the rules.xml file.
     *
     * @param xmlProperties LinkedHashMap containing output columnname with
     * formula.
     * @return ArrayList containing evaluated formula columns with Datatype.
     *
     */
    private ArrayList processData(LinkedHashMap xmlProperties) {

        ArrayList resultDataArray = new ArrayList();
        Object[] rulesKeys = xmlProperties.keySet().toArray();
        LinkedHashMap dataMap;
        LinkedHashMap evaluatedMap;

        // initialize the output column names and data type arraylist
        outputColumnNames = new ArrayList();
        outputDataTypes = new ArrayList();

        String formula;
        //System.out.println("Data type from IIEData"+dataTypes);
        for (int i = 0; i < rulesKeys.length; i++) {

            /* Commented by Arun : After replacing with XML Beans code Key in hashmap does 
             * not have a formula map instead has formula string
             */
            //formula = (String)((LinkedHashMap)xmlProperties.get((String)rulesKeys[i])).get("formula");
            formula = ((ParameterBean) xmlProperties.get((String) rulesKeys[i])).toString();
            ParameterBean parambean = (ParameterBean) xmlProperties.get((String) rulesKeys[i]);
            outputColumnNames.add((String) rulesKeys[i]);

            boolean blnIsFormula = parambean.isFormula();

            // if formula is used then there won't be datatype with "()" or subquery may be used
            if (formula.lastIndexOf("(") > 0 && !blnIsFormula) {
                formula = formula.substring(0, formula.lastIndexOf("(") - 1);
            }



//            System.out.println("Value :"+parambean.toString());
//            System.out.println("blnIsFormula: "+blnIsFormula);
            if (parambean.getAggregateFunc() != null) {
                ArrayList arrlist = parambean.getAggregateFunc();
                int intCount = arrlist.size();
                for (int inti = 0; inti < intCount; inti++) {
                    String strAggr = arrlist.get(inti).toString();
                    if (formula.indexOf(strAggr + "(") >= 0) {
                        formula = formula.replaceAll(strAggr + "[(]", "");
                    }
                }
            }

            //formula = (String)xmlProperties.get((String)rulesKeys[i]);    
            //System.out.println("------------------------------------------");
//            System.out.println("formula "+formula);
//            System.out.println("blnIsFormula:"+blnIsFormula);
//            System.out.println("isFormula :"+isFormula(formula));
//            System.out.println("blnIsFormula :"+blnIsFormula);
            if (isFormula(formula) && blnIsFormula) {
                // the <formula> tag has a formula in fact
                dataMap = evaluateMap(getResultMap(formula, true));
                outputDataTypes.add(new Integer(Types.DOUBLE));
            } else {

                /* the <formula> tag doesn't have any formula and this column can be added
                 * in the resultMap as it is
                 */
                dataMap = getResultMap(formula, false);
                outputDataTypes.add(dataType.get(getColumnIndex(formula)));
                //System.out.println("Adding datatype in position "+i+ dataTypes.get(getColumnIndex(formula)));
            }
            resultDataArray.add(dataMap);
        }

        return resultDataArray;
    }

    private boolean isFormula(String formula) {
        // In this line you see the legal operators for calculation
        String operators = "+-*/^";

        for (int i = 0; i < operators.length(); i++) {

            if (formula.indexOf(operators.charAt(i)) != -1) {
                return true;
            }
        }

        return false;
    }

    /* use this method to generate the hashmap with the evaluated values,
     * the dataMap, which is generated based on the equation is passed as the
     * pasrameter.
     */
    private LinkedHashMap evaluateMap(LinkedHashMap dataMap) {

        Object[] keyArray = dataMap.keySet().toArray();
        ArrayList result = new ArrayList();
        for (int i = 0; i < keyArray.length; i++) {
            try {
                result = new ArrayList();
                result.add(
                        Postfix.evaluate(
                        Postfix.infixToPostfix(
                        Postfix.stringToInfix(
                        (String) dataMap.get(keyArray[i])))));

                dataMap.put(keyArray[i], result);
            } catch (ParsingError e) {
                System.out.println(e.getMessage());
            }
        }

        return dataMap;
    }

    /**
     * Use this mehtod to generate the result map for the entire equation the
     * equation is passed as the parameter and based on that only, the data will
     * be extranced from data ArrayList and the operations will apply
     *
     * in case the equation is not a formula, that scenario has also been taken
     * care of, in this method
     */
    private LinkedHashMap getResultMap(final String equation, boolean isFormula) {
        //if(logger.isinfoEnabled())
        //cmdPrint("MMMMMMMMMMMMMMMM");
        //cmdPrint("equation "+equation);
        //cmdPrint("columnNames  "+columnNames);
        //cmdPrint("columnNames index equation :"+columnNames.indexOf(equation));
        //cmdPrint("MMMMMMMMMMMMMMMM");
        if (!isFormula) {
            return (LinkedHashMap) (data.get(colNames.indexOf(equation)));
        }
        LinkedHashMap dataValue = new LinkedHashMap();

        // result hash map for one column
        LinkedHashMap dataMap = new LinkedHashMap();

        // this will be used to get the updated equation, whenever required
        String eqToUse = equation;
        StringBuffer buffer = new StringBuffer();
        // got thru all the data columns
        //System.out.println("------------------------------");
//        System.out.println("equation "+equation);
//        System.out.println("#### !!!! data:  "+data);
        for (int i = 0; i < colNames.size(); i++) {
            /* check if this data column is there in the equation, only if the
             * data column is in the equation, we have to proceed, otherwize
             * continue with rest of the columns
             */
//            System.out.println("Column Name:eqToUse:  "+colNames.get(i));
            if (equation.indexOf((String) (colNames.get(i))) != -1) {
                /* check if the datavalue key is already present in the
                 * resultMap.
                 * that key may be there if the equation has gone thru updation
                 * thru the value from previous data columns
                 * if the key is not there, add one, this whole thing will go in a loop
                 */
                //System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
                dataValue = (LinkedHashMap) data.get(i);
                Object[] keyArray = dataValue.keySet().toArray();

                /* we have to go thru all the dataValues for that particular
                 * column
                 */
                // System.out.println("dataValue "+dataValue);
                for (int j = 0; j < dataValue.size(); j++) {
                    /* key is there, get the existing equation, update it
                     * and put it back in the resultMap
                     * otherwize set it to the original equation
                     */
                    if (dataMap.containsKey(keyArray[j])) {
                        eqToUse = (String) (dataMap.get(keyArray[j]));
                    } else {
                        eqToUse = equation;
                    }
                    /* in the case when there is some formula defined, we have to 
                     * take the sum of the column values
                     */
                    Double value = (Double) sumArrayList((ArrayList) (dataValue.get(keyArray[j])));
                    //String strGetColumn  = (String)(columnNames.get(i));
                    //eqToUse = eqToUse.replaceAll(strGetColumn,value.toString()); 
                    //System.out.println("value "+value);
                    //System.out.println("eqToUse "+eqToUse);
                    while (eqToUse.indexOf((String) (colNames.get(i))) >= 0) {
                        int start = eqToUse.indexOf((String) (colNames.get(i)));
                        int end = start + (((String) (colNames.get(i))).length());
                        buffer = new StringBuffer(eqToUse);
                        buffer.replace(start, end, value.toString());
                        eqToUse = buffer.toString();
                    }
                    //System.out.println("eqToUse "+eqToUse);
                    // put the value for the key for this dataValue row
                    dataMap.put(keyArray[j], eqToUse);
                }
            }
        }
        //System.out.println("------------------------------");
        return dataMap;
    }

    /**
     * This method will be used to get the column index of the particular column
     *
     * @param column String representing column name.
     * @return int representing index of the column.
     */
    private int getColumnIndex(String column) {

        for (int i = 0; i < colNames.size(); i++) {

            if (((String) colNames.get(i)).equals(column)) {
                return i;
            }
        }

        return -1;
    }

    private void updateIndex(ArrayList processedData) {
        LinkedHashMap processedColumn;
        Object[] indexKeys;
//        System.out.println("Index before updation is "+index);
        System.out.println("before Index updation");
        // go thru the entire processed data
        for (int i = 0; i < processedData.size(); i++) {
            // get the columnn data 
            processedColumn = (LinkedHashMap) processedData.get(i);


            // get the keys present with us
            indexKeys = index.keySet().toArray();

            if (processedColumn != null) {
                // go thru the entire column and check entries against all the columnKeys
                for (int j = 0; j < indexKeys.length; j++) {

                    // check if the current key is there or not
                    if (processedColumn.containsKey(indexKeys[j])) {

                        /* if and only if the data count is more for any column data, then
                         * update the data count for that particular key in the index hashmap
                         */
                        if (((Integer) index.get(indexKeys[j])).intValue() < ((ArrayList) processedColumn.get(indexKeys[j])).size()) {
                            index.put(indexKeys[j], new Integer(((ArrayList) processedColumn.get(indexKeys[j])).size()));
                        }
                    }
                }
            }
        }
        System.out.println("after Index updation");
    }

    /**
     * This mehod will fillup the arraylist against each index value with NULL,
     * so as to make the no. of items in each arraylist against every index same
     *
     * @param rawArray Columns for which null values to be filled to match the
     * number of rows in both datasources.
     * @return ArrayList having filled with Null values.
     */
    private ArrayList fillNULL(ArrayList rawArray) {

        LinkedHashMap columnMap;
        Object[] indexKeys;
        String tempString = "";

        // get the keys present with us
        indexKeys = index.keySet().toArray();

        // go thru entire raw data set
        for (int i = 0; i < rawArray.size(); i++) {

            // get the single column at a time
            columnMap = (LinkedHashMap) rawArray.get(i);
            // System.out.println("Column Map in fillNULL is :"+columnMap);
            for (int j = 0; j < indexKeys.length; j++) {
                if (columnMap != null) {
                    if (columnMap.get(indexKeys[j]) != null) {
                        while (((ArrayList) columnMap.get(indexKeys[j])).size()
                                < ((Integer) index.get(indexKeys[j])).intValue()) {

                            // add some null data in this arraylist
                            ((ArrayList) columnMap.get(indexKeys[j])).add(((ArrayList) columnMap.get(indexKeys[j])).get(0));

                        }
                    }
                }

            }
        }

        return rawArray;
    }

    /**
     * This method will be used to sum the entire array list items.
     *
     * @param list ArrayList containing formula column data for summing.
     * @return Object result of formula execution.
     */
    private Object sumArrayList(ArrayList list) {

        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                sum = sum + (new Double((String) list.get(i))).doubleValue();
            } catch (Throwable th) {
            }
        }

        return new Double(sum);
    }

    /**
     * this method is generating the eventual output for IIE. It will also
     * exclude the columns not to be displayed for current role.
     *
     * @param dataArray ArrayList containing report data.
     * @return String containing row separator for each column and column
     * separator for each col in each row.
     */
    private String generateOutput(ArrayList dataArray) {
        System.out.println("Into generate Output");
        //System.out.println("outputColumnNames "+outputColumnNames); 
        //System.out.println("roleBasedColumns "+roleBasedColumns);

        StringBuffer output = new StringBuffer();
        ArrayList roleSelectedCols = new ArrayList();
        // go thru all the column names and prepare the first line of output
        for (int i = 0; i < outputColumnNames.size(); i++) {
//            if(!roleBasedColumns.contains(colNames.get(i))){
            output.append(outputColumnNames.get(i)).append(com.see.rep.util.Constants.COL_DELIMITER);
            roleSelectedCols.add(String.valueOf(i));
//            }
        }


        output.append("%%");
        System.out.println("Output datatypes:" + outputDataTypes);
        // go thru all the data types and prepare the second line of output
        for (int i = 0; i < outputDataTypes.size(); i++) {
//            if(!roleBasedColumns.contains(colNames.get(i)))
            output.append(outputDataTypes.get(i)).append(com.see.rep.util.Constants.COL_DELIMITER);
        }

        output.append("%%");

        LinkedHashMap columnMap;
        Object[] indexArray = index.keySet().toArray();
        ArrayList actualData;
        int count = 0;
        boolean isKeyDataNull = false;
        boolean colAdded = false;

        // go thru the entire index list
        for (int i = 0; i < indexArray.length; i++) {
            // initialize the count befor entering the data loop
            count = 0;
            isKeyDataNull = false;
            // go thru the entire data array list

            for (int j = 0; j < dataArray.size();) {
                //get one row of data from dataArray corresponding to the indexArray.
                actualData = (ArrayList) (((LinkedHashMap) dataArray.get(j)).get(indexArray[i]));

                //cmdPrint("actual Data:"+actualData+" Count :"+count);
                if (j > 0 && !isKeyDataNull) {
                    if (colAdded) {
                        output.append(com.see.rep.util.Constants.COL_DELIMITER);
                    }
                }

                // push the data into output
                if (actualData != null && !isKeyDataNull) {
                    if (roleSelectedCols.contains(String.valueOf(j))) {
                        if (actualData.get(count).toString().length() == 0) {
                            output.append("-");
                        } else {
                            output.append(actualData.get(count).toString());
                        }
                        colAdded = true;
                    }
                } else if (j == 0) {
                    isKeyDataNull = true;
                    colAdded = false;
                    break;
                }


                if (j == dataArray.size() - 1) {
                    j = 0;
                    count++;
                    if (!isKeyDataNull) {
                        output.append("%%");
                        colAdded = false;
                    }

                    isKeyDataNull = false;
                    //System.out.println("output :"+output);
                    int actsize = 0;
                    if (actualData != null) {
                        actsize = actualData.size();
                    }
                    if (count == actsize) {
                        colAdded = false;
                        break;
                    }
                    //}
                } else {
                    j++;
                }
                //}//for number of index values
                //else
                //  break;
            }
        }
        //System.out.println("Output string is :"+ output.toString());
        return output.toString();
    }

    private javax.swing.table.DefaultTableModel getTableModel(String tableData) {
//        System.out.println("tableData is "+tableData);
        UNGStringTokenizer tokenizer = new UNGStringTokenizer(tableData, com.see.rep.util.Constants.ROW_DELIMITER);
        Vector colNames = new Vector();
        final Vector colTypes = new Vector();
        Vector dataVector = new Vector();

        String rowString = "";
        int rowtokcount = tokenizer.countTokens();
        for (int row = 0; row < rowtokcount; row++) {
            rowString = tokenizer.nextToken();
            UNGStringTokenizer colTokens = new UNGStringTokenizer(rowString, com.see.rep.util.Constants.COL_DELIMITER);
            Vector rowVector = new Vector();
            int coltokcount = colTokens.countTokens();
            for (int col = 0; col < coltokcount; col++) {
                String colStr = colTokens.nextToken();
                if (row == 0) {
                    colNames.add(colStr);
                } else if (row == 1) {
                    colTypes.add(colStr);
                } else {
                    if (colStr == null) {
                        colStr = "";
                    }
//                    if(col==0) {
//                        System.out.println("#$#$@ colStr : "+colStr);
//                        System.out.println("#$#$@ colTypes.get(col) : "+colTypes.get(col));
//                    }
                    rowVector.add(com.see.rep.util.Util.createObjectForType(colStr, colTypes.get(col)));
//                    if(col==0) {
//                        System.out.println("#$#$@ rowVector) : "+rowVector);
//                    }
                }

            }
            if (rowVector != null && rowVector.size() > 0) {
                dataVector.add(rowVector);
            }
        }
//        System.out.println("#$#$@ dataVector : "+dataVector);
//        System.out.println("#$#$@ colNames : "+colNames);
        return new javax.swing.table.DefaultTableModel(dataVector, colNames);
    }

    /**
     * Creates a new instance of ViewAllDAO
     */
    public GenerateReportDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            GenerateReportDAO dao = new GenerateReportDAO();
            HashMap mapParam = new HashMap();

            /* HashMap where = new HashMap();
             where.put("beh", "CA");
            
             mapParam.put(CommonConstants.MAP_NAME, "getSelectOperativeAcctProductTOList");
             //mapParam.put("WHERE", where);updateDenominationCount
             List rMap = (List) dao.getData(mapParam).get("DATA");
             System.out.println (rMap.toString());*/

            HashMap where = new HashMap();
            where.put("CURRENCY", "INR");
            where.put("DENOMINATION", new Float("500.0"));
            where.put("COUNT", new Double("-5.0"));

            mapParam.put(CommonConstants.MAP_NAME, "updateDenominationCount");
            mapParam.put("WHERE", where);
            dao.execute(mapParam);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        String mapName = (String) map.get(CommonConstants.MAP_NAME);
        HashMap where = null;

        if (map.containsKey(CommonConstants.MAP_WHERE)) {
            where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        }

        if (mapName.trim().length() == 0) {
            throw new TTException("Map Name is Null");
        }

        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate(mapName, where);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            // If you get an error at this point, it matters little what it was. It is going to be
            // unrecoverable and we will want the app to blow up good so we are aware of the
            // problem. You should always log such errors and re-throw them in such a way that
            // you can be made immediately aware of the problem.
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw new TTException("Error initializing SqlConfig class. Cause: " + e);
        }
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("!!!!!obj : " + obj);
        HashMap returnMap = new HashMap();
        returnMap = getData(obj);
        return returnMap;
    }
    //as and when customer comes interest calculaion

    private HashMap asAndWhenCustomer(HashMap obj) throws Exception {
        HashMap returnMap = new HashMap();

        TaskHeader header = new TaskHeader();
        header.setBranchID(CommonUtil.convertObjToStr(obj.get("BRANCH_CODE")));
        InterestCalculationTask interestcalTask = new InterestCalculationTask(header);
        HashMap resultMap = interestcalTask.interestCalcTermLoanAD(obj);
        List lst = new ArrayList();
        lst.add(resultMap);
        returnMap.put(CommonConstants.DATA, lst);
        return returnMap;

    }
    
    // Added by nithya on 13 Mar 2025 for Excel export KD-3934
    private byte[] getJasperReportExcelPrint(LinkedHashMap where, JasperDesign jasperDesign, String reportName, String branchCode) throws Exception {
        String bankName = "";
        String sheetName = reportName;
        HSSFWorkbook wb = new HSSFWorkbook();
        if(reportName.length() > 30){//added by Nithya on 06 Jun 2025 for KD-4149
            sheetName = reportName.substring(0, 30);
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        ArrayList replaceList = new ArrayList();
        byte[] bytes = null;
        try {
            int k1 = 1;
            String queryText = jasperDesign.getQuery().getText();
            String queryText1 = jasperDesign.getQuery().getText();
            String strForOptional = "";
            java.util.Properties serverProperties = new java.util.Properties();
            com.see.truetransact.commonutil.Dummy cons = new com.see.truetransact.commonutil.Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            cons = null;
            Class.forName("org.postgresql.Driver").newInstance();
            Connection connection = java.sql.DriverManager.getConnection(serverProperties.getProperty("url"),
            serverProperties.getProperty("username"),
            serverProperties.getProperty("password"));
            Statement st = connection.createStatement();
            PreparedStatement psmnt = null;
            String parameters[] = null;
            String tempParam[] = null;
            int fromIndex = 0;
            int toIndex = 0;
            while (toIndex != -1) {
                String key = "";
                String value = "";
                toIndex = queryText.indexOf('}', fromIndex);
                if (toIndex - fromIndex > 1) {
                    key = queryText.substring(fromIndex, toIndex);
                    toIndex = queryText.indexOf('$', fromIndex);
                    if (toIndex == -1) {
                        value = queryText.substring(fromIndex, queryText.length());
                    } else {
                        value = queryText.substring(fromIndex, toIndex);
                        fromIndex = toIndex + 1;
                    }
                    if (!key.contains("select") && !key.contains("SELECT")) {
                        String keyOutput = key.substring(2, key.length());
                        queryText1 = queryText1.replace("$P{" + keyOutput + "}", " ? ");
                        replaceList.add(where.get(keyOutput));
                    }
                    fromIndex = toIndex + 1;
                } else {
                    fromIndex = queryText.indexOf('$', toIndex) + 1;
                }
            }
            System.out.println(" replaceList : " + replaceList);
            System.out.println("queryText1 : " + queryText1);
            psmnt = connection.prepareStatement(queryText1);
            HSSFRow rowhead = sheet.createRow((short) 0);
            if (replaceList != null && replaceList.size() > 0) {
                for (int i = 0; i < replaceList.size(); i++) {
                    String outPutValue = CommonUtil.convertObjToStr(replaceList.get(i));
                    System.out.println("outPutValue : "+outPutValue);
                    if (outPutValue.contains("/2")) {//spiliting into date,incase output is date then if will work or else part will work
                        java.util.Date currDt = DateUtil.getDateMMDDYYYY(outPutValue);
                        System.out.println("currDt : "+currDt);
                        java.sql.Date sqlCurrDt = new java.sql.Date(currDt.getTime());
                        System.out.println("sqlCurrDt : "+sqlCurrDt);
                        psmnt.setDate(k1++, sqlCurrDt);
                    } else {
                        String paramStringVal =  CommonUtil.convertObjToStr(replaceList.get(i));
                        if(paramStringVal.equals(null) || paramStringVal.length() == 0){
                            psmnt.setString(k1++, null);
                        }else{
                            psmnt.setString(k1++, CommonUtil.convertObjToStr(replaceList.get(i)));
                        }
                    }
                }
                ResultSet rs = psmnt.executeQuery();
                if (rs != null) {
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY hhmm");
                    DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-YYYY hh:mm");
                    Calendar calender = Calendar.getInstance();
                    dateFormat.format(calender.getTime());
                    int index = 0;
                    int cellHeadingCount = 0;
                    ResultSetMetaData metaData = rs.getMetaData();
                    int colCount = metaData.getColumnCount();
                    HSSFRow row = sheet.createRow((short) index++);
                    List bankList = sqlMap.executeQueryForList("getSelectBankTOList", null);
                    if (bankList != null && bankList.size() > 0) {
                        HashMap bankMap = (HashMap) bankList.get(0);
                        bankName =  CommonUtil.convertObjToStr(bankMap.get("BANK_NAME"));
                    }
                    row.createCell((short) cellHeadingCount++).setCellValue(bankName);
                    cellHeadingCount = 0;
                    if (branchCode != null && branchCode.length() > 0) {
                        HashMap whereMap = new HashMap();
                        whereMap.put("BRANCH CODE", branchCode);
                        List repData = (List) sqlMap.executeQueryForList("getSelectBranchName", whereMap);
                        if (repData != null && repData.size() > 0) {
                            whereMap = (HashMap) repData.get(0);
                            row = sheet.createRow((short) index++);
                            row.createCell((short) cellHeadingCount++).setCellValue(CommonUtil.convertObjToStr(whereMap.get("BRANCH_NAME")) + "[" + branchCode + "]");
                            cellHeadingCount = 0;
                        }
                        row = sheet.createRow((short) index++);
                        row.createCell((short) cellHeadingCount++).setCellValue("Report Name : ");
                        cellHeadingCount = 3;
                        row.createCell((short) cellHeadingCount++).setCellValue(reportName);
                        cellHeadingCount = 5;
                        row.createCell((short) cellHeadingCount++).setCellValue("USER : ");
                        row.createCell((short) cellHeadingCount++).setCellValue(CommonUtil.convertObjToStr(where.get("USER_ID")));
                        cellHeadingCount = 7;
                        row.createCell((short) cellHeadingCount++).setCellValue("Date : ");
                        row.createCell((short) cellHeadingCount++).setCellValue(dateFormat1.format(calender.getTime()));
                        cellHeadingCount = 0;
                        row = sheet.createRow((short) index++);
                        for (int i = 1; i <= metaData.getColumnCount(); i++) {
                            row.createCell((short) cellHeadingCount++).setCellValue(metaData.getColumnName(i));
                        }
                        cellHeadingCount = 0;
                        while (rs.next()) {
                            row = sheet.createRow((short) index++);
                            short colNo = 0;
                            for (short i = 1; i <= colCount; i++) {
                                row.createCell(((short) colNo++)).setCellValue(rs.getString(i));
                            }
                        }
                        cellHeadingCount = 0;
                        row = sheet.createRow((short) index++);
                        row.createCell((short) cellHeadingCount++).setCellValue("Note : This is computer generated statement");
                        cellHeadingCount = 5;
                        row.createCell((short) cellHeadingCount++).setCellValue("Checked By");
                        cellHeadingCount = 7;
                        row.createCell((short) cellHeadingCount++).setCellValue("Manager / Chief Manager / AGM ");
                        rs.close();
                        connection.close();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        wb.write(bos);
                        bytes = bos.toByteArray();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("#$#$ Exception in GenerateReportDAO getReportTableModel() : " + ex);
            ex.printStackTrace();
        }
        return bytes;
    }     
    
    
    
    
}
