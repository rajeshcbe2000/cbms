/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ReadReportServerConfig.java
 *
 * Created on May 17, 2005, 12:42 PM
 */
package com.see.truetransact.serverside.common.report;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

import com.see.iie.engine.dao.datasources.SeEDataSource;
import com.see.iie.engine.dao.datasources.DataSourceFactory;
import com.see.iie.engine.dao.DBCache;
import com.see.rep.net.ClientContext;
import com.see.rep.util.Constants;
import com.see.iie.utils.XML_DIE;
import com.see.common.tools.treebuilder.util.ParameterBean;
import com.see.truetransact.commonutil.Dummy;

/**
 *
 * @author 152684
 */
public class ReadReportServerConfig {

    private com.see.iie.ui.IIEServerConfig iieServerConfig;

    /**
     * Creates a new instance of ReadReportServerConfig
     */
    public ReadReportServerConfig(com.see.iie.ui.IIEServerConfig iieServerConfig) {
        this.iieServerConfig = iieServerConfig;
    }

    private LinkedHashMap readInputCols() {
        if (iieServerConfig == null) {
            System.out.println("iieServerConfig   NULL");
        }

        DefaultListModel inputColModel = iieServerConfig.getInputColModel();
        LinkedHashMap inputColMap = new LinkedHashMap();
        if (inputColModel != null && inputColModel.getSize() > 0) {
            int intcount = inputColModel.getSize();
            for (int inti = 0; inti < intcount; inti++) {
                inputColMap.put(inti + "", inputColModel.getElementAt(inti));
            }
        }
        return inputColMap;
    }

    private LinkedHashMap readJoinCondition() {
        DefaultListModel keyColModel = iieServerConfig.getKeyColModel();
        LinkedHashMap joinCondtion = new LinkedHashMap();
        if (keyColModel != null && keyColModel.getSize() > 0) {
            int intcount = keyColModel.getSize();
            for (int inti = 0; inti < intcount; inti++) {
                joinCondtion.put(inti + "", keyColModel.getElementAt(inti));
            }
        }
        return joinCondtion;
    }

    public LinkedHashMap readSearchCondition() {
        DefaultListModel lstSearchModel = iieServerConfig.getWhereModel();
        LinkedHashMap searchmap = new LinkedHashMap();
        if (lstSearchModel != null && lstSearchModel.getSize() > 0) {
            int intcount = lstSearchModel.getSize();
            for (int inti = 0; inti < intcount; inti++) {
                searchmap.put(inti + "", lstSearchModel.getElementAt(inti));
            }
        }
        return searchmap;
    }

    public LinkedHashMap readOutputColumns() {
        System.out.println("####Into readOutputColumns()");
        LinkedHashMap outputcolsMap = new LinkedHashMap();
        if (iieServerConfig != null) {
            DefaultTableModel tblModelFormula = new DefaultTableModel(iieServerConfig.getFormulaDataVector(), iieServerConfig.getFormulaColVector());
            System.out.println("####tblModelFormula : " + tblModelFormula);
            System.out.println("####tblModelFormula.getRowCount() : " + tblModelFormula.getRowCount());
            System.out.println("####tblModelFormula.getColumnCount() : " + tblModelFormula.getColumnCount());
            if (tblModelFormula != null) {
                int intRowCount = tblModelFormula.getRowCount();
                for (int inti = 0; inti < intRowCount; inti++) {
                    String strName = tblModelFormula.getValueAt(inti, 0).toString();
                    ParameterBean parambean = (ParameterBean) tblModelFormula.getValueAt(inti, 1);
                    outputcolsMap.put(strName, parambean);
                }
            }
        }
        return outputcolsMap;
    }

    private HashMap readOrderby() {
        DefaultListModel lstOrderbyModel = iieServerConfig.getOrderbyModel();
        HashMap orderbyMap = new HashMap();
        if (lstOrderbyModel != null && lstOrderbyModel.getSize() > 0) {
            int intcount = lstOrderbyModel.getSize();
            for (int inti = 0; inti < intcount; inti++) {
                orderbyMap.put(inti + "", lstOrderbyModel.getElementAt(inti));
            }
        }
        return orderbyMap;
    }

    public LinkedHashMap readIIEConfig() throws Exception {
        LinkedHashMap props = new LinkedHashMap();
        LinkedHashMap tempMap = new LinkedHashMap();

        //read select cols
        tempMap = new LinkedHashMap();
        tempMap = readInputCols();
        if (tempMap.size() > 0) {
            props.put("SelectCols", tempMap);
        }

        //read output cols
        tempMap = new LinkedHashMap();
        tempMap = readOutputColumns();
        if (tempMap.size() > 0) {
            props.put("OutputCols", tempMap);
        }

        //read joins key cols
        tempMap = new LinkedHashMap();
        tempMap = readJoinCondition();
        if (tempMap.size() > 0) {
            props.put("Joins", tempMap);
        }

        //read search condtion
        tempMap = new LinkedHashMap();
        tempMap = readSearchCondition();
        if (tempMap.size() > 0) {
            props.put("Searches", tempMap);
        }

        // orderby list
        HashMap orderbyMap = new HashMap();
        orderbyMap = readOrderby();
        if (orderbyMap.size() > 0) {
            props.put("ORDERBY", orderbyMap);
        }

        return props;
    }

    private ArrayList addToTables(ArrayList arrListTables, String strTableName) {
        if (!arrListTables.contains(strTableName)) {
            arrListTables.add(strTableName);
        }
        return arrListTables;
    }

    public java.sql.ResultSet queryFormation(LinkedHashMap props, ArrayList colList, LinkedHashMap parameters) throws Exception {
        LinkedHashMap tempMap = new LinkedHashMap();
        LinkedHashMap groupbyMap = new LinkedHashMap();
        ParameterBean paramBean = new ParameterBean();
        SeEDataSource dataSource = null;
        String strTable = "";
        String strColumn = "";
        String strOutput = "";
        //String strFromTables = "";
        ArrayList arrlistFromTables = new ArrayList();

        String strTmpQuery = "";
        String noOfRows = ((ParameterBean) parameters.get(com.see.rep.util.Constants.NUM_OF_ROWS_TO_FETCH)).getParamValue();
        String strNoOfRows = "";
        String dbtype = "ORACLE", db = "", user = "", pwd = "", schema = "";

        int intCount;
        int intGroupby = 0;
        boolean blnJoins = false;
        boolean blnSearches = false;
        boolean blnGroupBy = false;

        StringBuffer strInputBuff = new StringBuffer();

        strInputBuff.append("Select ");
        LinkedHashMap hMapOutputCols = (LinkedHashMap) props.get("OutputCols");

//            java.util.Vector argumentList = iieServerConfig.getDatabaseConnections();
//            LinkedHashMap lnkHashArgList = new LinkedHashMap();
//            int intArgCount = argumentList.size();
//            for(int inti=0;inti<intArgCount;inti++){
//                String strArr[]=argumentList.get(inti).toString().split("#");
//                lnkHashArgList.put(strArr[0].toUpperCase()+"#"+strArr[1].toUpperCase(),strArr[2]+"#"+strArr[3]);
//            }
        java.util.Properties serverProperties = new java.util.Properties();
        Dummy cons = new Dummy();
        serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
        String dataBaseURL = serverProperties.getProperty("url");
        String userName = serverProperties.getProperty("username").toUpperCase();
        String passWord = serverProperties.getProperty("password");
//            System.out.println("dbtype = "+dbtype);
//            System.out.println("dataBaseURL = "+dataBaseURL);
//            System.out.println("userName = "+userName);
//            System.out.println("passWord  = "+passWord );

        //read output  cols
        if (hMapOutputCols != null && hMapOutputCols.size() > 0) {
            intCount = hMapOutputCols.size();
            Object strArrOutput[] = hMapOutputCols.keySet().toArray();
            int intCountOutput = strArrOutput.length;
            for (int intj = 0; intj < intCountOutput; intj++) {
                paramBean = (ParameterBean) hMapOutputCols.get(strArrOutput[intj]);
//                    if(intj==0){
//                        dbtype=paramBean.getParamDataBase().toUpperCase();
//                        dataSource = DataSourceFactory.create(dbtype);
//                        // top given number of rows will be returned
//                        strNoOfRows = dataSource.rowCount(noOfRows, dbtype,"START");
//                        if(strNoOfRows.length()>0)
//                            strInputBuff.append(strNoOfRows);
//                        XML_DIE xmldie = new XML_DIE();
//                        ArrayList arrlistArgs = xmldie.getDIEProperties("Database",dbtype);
//                        db=arrlistArgs.get(5).toString(); //take from die properties
//                        schema= paramBean.getParamSchema();
//                        user  = paramBean.getParamSchema();
//                        if(lnkHashArgList.get(dbtype+"#"+user)!=null){
//                            if(dbtype.startsWith("MS SQL"))
//                                db = user; // take from configuration list
//                            String strTmpArr[]= lnkHashArgList.get(dbtype+"#"+user).toString().split("#");
//                            user = strTmpArr[0];
//                            pwd  = strTmpArr[1];
//                        }
//                        System.out.println("dbtype = "+dbtype);
//                        System.out.println("db = "+db);
//                        System.out.println("user = "+user);
//                        System.out.println("pwd  = "+pwd );
//                    }
                colList.add(paramBean.getParamDataBase() + "."
                        + paramBean.getParamSchema() + "."
                        + paramBean.getParamTableName() + "."
                        + paramBean.getParamColumnName());
                String strOutputCols = paramBean.toString();
                String strAliasName = strArrOutput[intj].toString();
                strColumn = paramBean.getParamColumnName();
                strTable = paramBean.getParamTableName();
                arrlistFromTables = addToTables(arrlistFromTables, strTable);

                //System.out.println("aggregate :"+paramBean.getAggregateFunc());
                ArrayList arrlistAggr = new ArrayList();
                arrlistAggr = paramBean.getAggregateFunc();

                if (arrlistAggr != null && arrlistAggr.size() > 0) {
                    blnGroupBy = true;
                }
                System.out.println("in readiieserver strColumn :" + strColumn);
                // if it is a subquery
                if (strColumn.startsWith("(")) {

                    java.util.Set Keys = parameters.keySet();
                    java.util.Iterator ItrIterator = Keys.iterator();

                    while (ItrIterator.hasNext()) {
                        String strItr = ItrIterator.next().toString();
                        strColumn = strColumn.toUpperCase();
                        if (strColumn.indexOf("?" + strItr) != -1) //if subquery has paramter
                        {
                            strColumn = strColumn.replaceAll("[?]" + strItr, getParamValue(parameters, strItr));
                        }
                    }

                    // to remove the data type
                    if (strColumn.indexOf(dbtype) >= 0) {
                        strColumn = strColumn.replaceAll(dbtype + ".", "");
                    }

                    // to remove the schema
                    if (strColumn.indexOf(userName + ".") >= 0) {
                        strColumn = strColumn.replaceAll(userName + ".", "");
                    }

                    // to remove the table name
                    if (strColumn.indexOf(strTable + ".(") >= 0) {
                        strColumn = strColumn.replaceAll(strTable + ".", "");
                    }

                    replaceWithAggrFunc(arrlistAggr, strColumn);
                    //System.out.println("strColumn with aggr startwith ( "+strColumn);
                    strTmpQuery = strTmpQuery + strColumn + " as " + strAliasName + " , ";
                    if (arrlistAggr == null) {
                        //System.out.println("intGroupby "+intGroupby);
                        groupbyMap.put(intGroupby + "", strColumn);
                        intGroupby = intGroupby + 1;
                    }
                } else {
//                        System.out.println("@@##@@$$ strColumn : "+strColumn+"  dbtype : "+dbtype+"  userName : "+userName);
                    if (strColumn.indexOf(dbtype) >= 0) {
                        strColumn = strColumn.replaceAll(dbtype + ".", "");
                        strColumn = strColumn.replaceAll(userName + ".", "");
                    }
                    String strTmpCol = replaceWithAggrFunc(arrlistAggr, strTable + "." + strColumn);
                    //System.out.println("strColumn with aggr "+strColumn);
                    if (arrlistAggr == null) {
                        //System.out.println("intGroupby      "+intGroupby);
                        groupbyMap.put(intGroupby + "", strTmpCol);
                        intGroupby = intGroupby + 1;
                    }
                    strTmpQuery = strTmpQuery + strTmpCol + " as " + strAliasName + " , ";
//                        System.out.println("@@##@@$$ strTmpQuery : "+strTmpQuery);
                }
            }

            strTmpQuery = strTmpQuery.substring(0, strTmpQuery.length() - 2); // to remove the last " , "
        }
        strInputBuff.append(strTmpQuery);
        String strJoin = "";
        String strTmpTable = "";
        String strTmpColumn = "";
        String strOptional = "";
        boolean isWhere = false;

        //read joins
        tempMap = (LinkedHashMap) props.get("Joins");
        strTmpQuery = "";
        if (tempMap != null && tempMap.size() > 0) {
            intCount = tempMap.size();
            isWhere = true;
            strTmpQuery = " where ";
            for (int inti = 0; inti < intCount; inti++) {
                paramBean = (ParameterBean) tempMap.get(inti + "");
                strTable = paramBean.getParamTableName();
                arrlistFromTables = addToTables(arrlistFromTables, strTable);
                strColumn = paramBean.getParamColumnName();
                //System.out.println("paramBean===  "+paramBean);
                if (paramBean.getJoinCondition() != null && paramBean.getJoinCondition().length() > 0) {
                    for (int intj = inti + 1; intj < intCount; intj++) {
                        ParameterBean tmpParamBean = (ParameterBean) tempMap.get(intj + "");
                        if (paramBean.getJoinCondition().equals(tmpParamBean.getJoinCondition())) {
                            blnJoins = true;
                            strJoin = strTable + "." + strColumn;
                            strTmpTable = tmpParamBean.getParamTableName();
                            strTmpColumn = tmpParamBean.getParamColumnName();
                            strJoin = strJoin + " = " + strTmpTable + "." + strTmpColumn;
                            if (strTmpQuery.indexOf(strJoin) == -1) {
                                strTmpQuery = strTmpQuery + strJoin + " and ";
                            }
                        }
                    }
                } else { // for primary key and normal key joins
                    //System.out.println("Primary key constraints");
                    for (int intj = inti + 1; intj < intCount; intj++) {
                        ParameterBean tmpParamBean = (ParameterBean) tempMap.get(intj + "");
                        //System.out.println("Name "+tmpParamBean.getParamName());
                        //System.out.println("NULL or NOT"+tmpParamBean.getJoinCondition());
                        if (tmpParamBean.getJoinCondition() == null) {
                            strJoin = strTable + "." + strColumn;
                            strTmpTable = tmpParamBean.getParamTableName();
                            strTmpColumn = tmpParamBean.getParamColumnName();
                            if (!strTable.equalsIgnoreCase(strTmpTable) && strColumn.equalsIgnoreCase(strTmpColumn)) {
                                blnJoins = true;
                                strJoin = strJoin + " = " + strTmpTable + "." + strTmpColumn;
                                if (strTmpQuery.indexOf(strJoin) == -1) {
                                    strTmpQuery = strTmpQuery + strJoin + " and ";
                                }
                            }
                        }
                    }
                }
            }
            if (strTmpQuery.endsWith(" and ")) {
                strTmpQuery = strTmpQuery.substring(0, strTmpQuery.length() - 4); // to remove the last " and "
            }
            if (strTmpQuery.endsWith(" where ")) {
                strTmpQuery = strTmpQuery.substring(0, strTmpQuery.length() - 6); // to remove the where string if no join condition  was there
                isWhere = false;
            }
        }

        int intArrCount = arrlistFromTables.size();
        String strTmpTables = "";
        for (int intk = 0; intk < intArrCount; intk++) {
            strTmpTables = strTmpTables + arrlistFromTables.get(intk).toString() + " , ";
        }
        if (strTmpTables.length() > 0) {
            strTmpTables = strTmpTables.substring(0, strTmpTables.length() - 2); // to remove the last " , "
        }
        strInputBuff.append(" From " + strTmpTables + " ");

        //adding joins to the buffer
        strInputBuff.append(strTmpQuery);
        strTmpQuery = "";
        //read search condtion
        tempMap = (LinkedHashMap) props.get("Searches");
        if (tempMap != null && tempMap.size() > 0) {
            intCount = tempMap.size();
            if (isWhere) {
                strTmpQuery = " and ";
            } else {
                strTmpQuery = " where ";
                //isWhere = true;
            }
            for (int inti = 0; inti < intCount; inti++) {
                paramBean = (ParameterBean) tempMap.get(inti + "");
                strOptional = paramBean.getParamType();
                strTable = paramBean.getParamTableName();
                strColumn = strTable + "." + paramBean.getParamColumnName();
                String strSearchParam = paramBean.getParamWhere().toUpperCase();
                int intDataType = paramBean.getDataType();
                // to change the format of the parameter
                if ((intDataType == 93 || intDataType == 91) && !(strSearchParam.startsWith("("))) {
                    dataSource = DataSourceFactory.create(paramBean.getParamDataBase());
                    String strTmpResult = dataSource.convertToDate(strSearchParam, paramBean.getParamCondition(), strColumn);
                    String strArr[] = strTmpResult.split("~~");
                    strSearchParam = strArr[0];
                    strColumn = strArr[1];
                }

                java.util.Set Keys = parameters.keySet();
                java.util.Iterator ItrIterator = Keys.iterator();
                int ischar = strSearchParam.indexOf("'");
                if (intDataType == 12) // if it is a char then add '
                {
                    ischar = 0;
                }
                String strTmpResult = "";
                String strItr = "";
                while (ItrIterator.hasNext()) {
                    strItr = ItrIterator.next().toString();
                    strTmpResult = ReturnQueryData(ischar, strSearchParam, parameters, strColumn, strItr);
                    if (strTmpResult.length() > 0) {
                        strSearchParam = strTmpResult;
                    }
                }
                //System.out.println("strSearchParam :"+strSearchParam);
                //System.out.println("strOptional  :"+strOptional);
                if (strOptional == null) {
                    strOptional = "M";
                }
                if (strOptional.equals("M") || strSearchParam.indexOf("?") == -1) {
                    strTmpQuery = strTmpQuery + strColumn + "  " + paramBean.getParamCondition() + " " + strSearchParam;
                    strTmpQuery = strTmpQuery + " and ";
                    blnSearches = true;
                }
            }
            if (!blnSearches) {
                strTmpQuery = "";
                //isWhere = false;
            }
            if (strTmpQuery.endsWith(" and ")) {
                strTmpQuery = strTmpQuery.substring(0, strTmpQuery.length() - 4); // to remove the last " and "
            }
        }

        strInputBuff.append(strTmpQuery);
        // top given number of rows will be returned
//                strNoOfRows = dataSource.rowCount(noOfRows, dbtype,"END");
        if (!noOfRows.equalsIgnoreCase("all")) {
            strNoOfRows = " rownum<= " + noOfRows + " ";
        }

        if (strNoOfRows.length() > 0 && !strNoOfRows.equalsIgnoreCase("all")) {
            if ((blnSearches || blnJoins)) {
                strInputBuff.append(" and ");
            } else//        if(!isWhere)
            {
                strInputBuff.append(" where ");
            }
            strInputBuff.append(strNoOfRows);
        }

        strTmpQuery = "";
        //-----------------------------
        // groupby  list
        if (blnGroupBy && groupbyMap.size() > 0) {
            strTmpQuery = " group by ";
            intCount = groupbyMap.size();
            for (int inti = 0; inti < intCount; inti++) {
                String strgroupby = groupbyMap.get(inti + "").toString();
                strTmpQuery = strTmpQuery + strgroupby + " , ";
            }
            strTmpQuery = strTmpQuery.substring(0, strTmpQuery.length() - 2); // to remove the last " and "
            strInputBuff.append(strTmpQuery);
        }
        //-----------------------------

        strTmpQuery = "";
        // orderby list 
        HashMap tempHash = (HashMap) props.get("ORDERBY");
        if (tempHash != null && tempHash.size() > 0) {
            strTmpQuery = " order by ";
            intCount = tempHash.size();
            for (int inti = 0; inti < intCount; inti++) {
                paramBean = (ParameterBean) tempHash.get(inti + "");
                strTable = paramBean.getParamTableName();
                strColumn = paramBean.getParamColumnName();
                strTmpQuery = strTmpQuery + strTable + "." + strColumn + " , ";
            }
            strTmpQuery = strTmpQuery.substring(0, strTmpQuery.length() - 2); // to remove the last " and "
        }
        tempHash = null;
        strInputBuff.append(strTmpQuery);

        //System.out.println("blnGroupBy "+blnGroupBy);
        //System.out.println("groupbyMap "+groupbyMap);



        System.out.println("Query Formed:");
        System.out.println("-------------");
        System.out.println(strInputBuff.toString());
        System.out.println("-------------");
//        DBCache cache = DBCache.getInstance();    
//        Connection con = cache.getConnection(dbtype,db,user,pwd);
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        Connection con = DriverManager.getConnection(dataBaseURL, userName, passWord);
        Statement stat = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rsQryOutput = stat.executeQuery(strInputBuff.toString());

//        con.close();
//        con = null;
//        stat.close();
//        stat = null;
        cons = null;
        serverProperties = null;
        return rsQryOutput;
    }

    private String replaceWithAggrFunc(ArrayList arrlistAggr, String strcolumn) {
        if (arrlistAggr != null) {
            int intCount = arrlistAggr.size();
            for (int inti = 0; inti < intCount; inti++) {
                //System.out.println("value aggr "+arrlistAggr.get(inti));
                strcolumn = arrlistAggr.get(inti) + "(" + strcolumn + ")";
            }
        }
        return strcolumn;
    }

    public static String getParamValue(HashMap parameters, String strParam) {
        ParameterBean parambean = (ParameterBean) parameters.get(strParam);

        String strReturnValue = "";
        if ((parambean != null) && parambean.getParamValue() != null) {
            strReturnValue = parambean.getParamValue();
        }
        //System.out.println("parameters  strParam-----"+strParam+" =   "+strReturnValue);
        return strReturnValue;
    }
    /*
     replace the values with the column names
     @ischar -  char or not
     @strSearchParam - Data value of the parameter 
     @parameters - parameter map
     @columnName - Table column name
     @strColName -  parameter name - when parameter name = ?+paramvalue
     */

    public static String ReturnQueryData(int ischar, String strSearchParam, HashMap parameters, String columnName, String strColName) {
        String data = "";
        columnName = com.see.rep.util.Util.GetOnlyString(columnName);
        strColName = com.see.rep.util.Util.GetOnlyString(strColName);
        //System.out.println("_____________________________");
        //System.out.println("strColName      "+strColName);
        //System.out.println("columnName      "+columnName);
        //System.out.println("strSearchParam  "+strSearchParam);
        //System.out.println("strSearchParam.indexOf ? + strColName  "+strSearchParam.indexOf("?"+strColName));
        //System.out.println("(strSearchParam.indexOf '?'  "+strSearchParam.indexOf("'?'"));
        if (strSearchParam.indexOf("?") == -1) { //  if ? not exists
            data = strSearchParam;
        } else if ((strSearchParam.indexOf("?" + strColName) >= 0) && (strColName.length() > 0) && getParamValue(parameters, strColName.toUpperCase()).length() > 0) { //  if ? exists with param
            data = strSearchParam.replaceAll("[?]" + strColName, getParamValue(parameters, strColName.toUpperCase()));
            //System.out.println("last but two ");
        } else if ((strSearchParam.indexOf("'?'") >= 0) || (strSearchParam.indexOf("'? 00:") >= 0) || (strSearchParam.indexOf("'? 23:") >= 0) && getParamValue(parameters, columnName.toUpperCase()).length() > 0) { //  if ? exists with out param
            data = strSearchParam.replaceAll("[?]", getParamValue(parameters, columnName.toUpperCase()));
            //System.out.println("last but one ");
        } else if (getParamValue(parameters, columnName.toUpperCase()).length() > 0) {//  if ? only exists
            //System.out.println(" last columnName"+columnName);
            data = getParamValue(parameters, columnName.toUpperCase());
        }

        // if ischar is -1 then there is no single quote in the data
        // else single quote is there
        if ((ischar != -1) && (strSearchParam.indexOf("(") == -1) && data.length() > 0) { // to avoid single quote when function is given
            data = "'" + data + "'";
            data = data.replaceAll("''", "'");
        }
        //System.out.println("data    "+data);
        //System.out.println("_____________________________");
        return data;
    }
}