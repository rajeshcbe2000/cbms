/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FileRead.java
 *
 * Created on May 25, 2005, 11:00 AM
 */
package com.see.truetransact.serverside.fileupload;

/**
 *
 * @author 152691
 */
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import com.see.truetransact.serverutil.ServerUtil;

public class FileRead {

    /**
     * This method is used to read the input source file
     */
    public static DataInputStream readFile(File fileObject) throws FileNotFoundException {
        FileInputStream fstream = new FileInputStream(fileObject.getPath());
        return new DataInputStream(fstream);
    }

    /**
     * This method is used to read the mapping file @returns ArrayList
     * containing MappingTO
     */
    public static ArrayList readMappingFile(FileHeader header) throws FileNotFoundException, IOException {
        ArrayList output = new ArrayList();
        FileInputStream fstream = new FileInputStream(header.getMappingFile().getPath());
        DataInputStream in = new DataInputStream(fstream);
        String[] lineReader;
        while (in.available() != 0) {
            lineReader = in.readLine().split(",");
            if (lineReader != null) {
                output.add(populateMappingTO(lineReader, header.getBranchID()));
            }
        }
        lineReader = null;
        in.close();
        in = null;
        fstream = null;
        return output;
    }

    /**
     * This method is used to format the date to the format specified @returns
     * String object representation of the date
     */
    private static String formatDate(String format, String branchId) {
        return new SimpleDateFormat(format).format(ServerUtil.getCurrentDate(branchId));
    }

    /**
     * This method is used to populate the MappingTO @returns MappingTO
     */
    private static MappingTO populateMappingTO(String[] lineReader, String branchId) {
        MappingTO mapping = new MappingTO();
        mapping.setPosition(lineReader[0]);
        mapping.setDbName(lineReader[1]);
        mapping.setTableName(lineReader[2]);
        mapping.setColumnName(lineReader[3]);
        mapping.setTargetDataType(lineReader[4]);
        mapping.setSourceFormat(lineReader[5]);
        if (lineReader[6] != null
                && lineReader[6].indexOf('<') != -1
                && lineReader[6].indexOf('>') != -1) {
            if (mapping.getTargetDataType().equalsIgnoreCase("DATE")) {
                lineReader[6] = formatDate(mapping.getSourceFormat(), branchId);
            }
        }
        mapping.setDefaultValue(lineReader[6]);
        mapping.setColumnPosition(lineReader[7]);
        mapping.setStartPosition(lineReader[8]);
        mapping.setEndPosition(lineReader[9]);
        mapping.setTagName(lineReader[10]);
        //System.out.println("mapping : " + mapping);
        return mapping;
    }

    public static File decrypt(File sourceFile, String key) {
        //TODO : Do decrypt here.
        return sourceFile;
    }

    public static void main(String args[]) {
//        String fileName = "D:\\mappingCSV.config";
//        try {
//            FileHeader f1 = new FileHeader();
//            f1.setMappingFile(fileName);
//            f1.setBranchID("Bran");
//            //System.out.println(readMappingFile(f1));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("File input error");
//        }

        String src = "this,is,the,,,first,word,in,the,query,\n";
        String arr[] = src.split(",");
        for (int i = 0; i < arr.length; i++) {
            System.out.println("arr[" + i + "] = " + arr[i]);
        }
    }
}