/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CSVFileTask.java
 *
 * Created on May 24, 2005 4:40 PM
 * This file will not handle any DB connections.
 * Read the file in server.
 */
package com.see.truetransact.serverside.fileupload;

import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import java.io.DataInputStream;
import java.util.ArrayList;

/**
 *
 * @author Sunil (152691)
 */
public class CSVFileTask extends FileTask {

    private ArrayList fileFormat;
    private int formatSize = 0;

    /**
     * Creates a new instance of AuthorizationCheck
     */
    public CSVFileTask(FileHeader header) throws Exception {
        setHeader(header);
        fileFormat = FileRead.readMappingFile(header);
        formatSize = fileFormat.size();
    }

    public FileTaskStatus executeTask() throws Exception {
        FileTaskStatus status = new FileTaskStatus();
        status.setStatus(BatchConstants.STARTED);
        try {
            processCSV(header);
            status.setStatus(BatchConstants.COMPLETED);
        } catch (Exception E) {
            status.setStatus(BatchConstants.ERROR);
            throw new TTException(E);
        }
        return status;
    }

    private StringBuffer processCSV(FileHeader header) throws Exception {
        int count = 0;
        String tableName = ""; //Used to store the table name that is read in each iteration
        StringBuffer strFinal = new StringBuffer();
        StringBuffer strColNames;
        StringBuffer strColData;
        MappingTO mapTO = new MappingTO();
        String[] lineReader;
        DataInputStream in = FileRead.readFile(header.getSourceFile());
        while (in.available() != 0) {
            strColNames = new StringBuffer();
            strColData = new StringBuffer();
            lineReader = in.readLine().split(",");
            //            if(lineReader.length != fileFormat.size())
            //                throw new TTException("Invalid File Format");
            //            else{
            for (int i = 0; i < formatSize; i++) {
                count = i;
                mapTO = (MappingTO) fileFormat.get(i);

                if (CommonUtil.convertObjToInt(mapTO.getColumnPosition()) <= lineReader.length) {
                    if (i == 0 || !tableName.equals(mapTO.getTableName())) {
                        if (strColNames.length() > 0 && i == (formatSize - 1)) {
                            strFinal.append(strColNames.substring(0, strColNames.length() - 1)).append(") values (");
                            strFinal.append(strColData.substring(0, strColData.length() - 1)).append(");\n");
                        }
                        strColNames = new StringBuffer();
                        strColData = new StringBuffer();

                        if (i != formatSize - 1) {
                            strColNames.append(" Insert into ").append(mapTO.getDbName()).append(".").append(mapTO.getTableName()).append("(");
                        }
                    }
                    tableName = mapTO.getTableName();
                    strColNames.append(mapTO.getColumnName()).append(",");


                    if (i == formatSize - 1) {
                        strFinal.append(strColNames.substring(0, strColNames.length() - 1)).append(") values (");
                    }

                    if (lineReader[CommonUtil.convertObjToInt(mapTO.getColumnPosition()) - 1].length() == 0) {
                        strColData.append("'").append(mapTO.getDefaultValue()).append("',");
                    } else {
                        strColData.append("'").append(lineReader[CommonUtil.convertObjToInt(mapTO.getColumnPosition()) - 1]).append("',");
                    }
                }
            }
            //            }

            if (strColNames.length() > 0 && tableName.equals(mapTO.getTableName())) {
                strFinal.append(strColData.substring(0, strColData.length() - 1)).append(");\n");
            }

            strColNames = null;
            strColData = null;
        }
        lineReader = null;
        in.close();
        in = null;
        System.out.println("3 = " + strFinal);
        return strFinal;
    }

    public static void main(String args[]) {
        try {
            FileHeader hd = new FileHeader();
            hd.setBranchID("Bran");
            hd.setSourceFile("D:\\DATA.csv");
            hd.setMappingFile("D:\\mapping.config");
            CSVFileTask ft = new CSVFileTask(hd);
            ft.executeTask();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
