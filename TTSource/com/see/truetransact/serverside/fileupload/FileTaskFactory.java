/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FileTaskFactory.java
 *
 * Created on May 24, 2005, 4:23 PM
 */
package com.see.truetransact.serverside.fileupload;

/**
 *
 * @author Sunil
 */
public class FileTaskFactory implements java.io.Serializable {

    /**
     * Creates a new instance of TaskFactory
     */
    public FileTaskFactory() {
    }

    /**
     * Returns a Task based on configuration.
     *
     * @throws Exception Throws Exception based on the error
     * @return Returns Task based on Configuration
     */
    public static FileTask createTask(FileHeader header) throws Exception {
        if (header.getFileType().equals("TXT")) {
            return new FixedWidthTask(header);
        } else if (header.getFileType().equals("CSV")) {
            return new CSVFileTask(header);
        }

        System.out.println("Task Not Found : " + header.getFileType());
        throw new FileTaskNotFoundException("Task Not Found " + header.getFileType());
    }

    public static void main(String arg[]) {
        try {
            FileHeader header = new FileHeader();
            header.setFileType("CSV");
            header.setSourceFile("D:\\abc.csv");
            FileTask tsk = FileTaskFactory.createTask(header);
            FileTaskStatus status = tsk.executeTask();
            System.out.println(status.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
