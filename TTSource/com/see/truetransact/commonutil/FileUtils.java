package com.see.truetransact.commonutil;

/**
 * @(#)FileUtils.java
 *
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition..
 * 
 */

/**
 * Description : File Utilities like Creating a Directory,
 * Copy File or Copy the full Directory.
 *
 * <p>
 * The following is one example of the use of the FileUtils.java The code:
 *
 * Copying the Full Directory :
 * <blockquote><pre>
 * FileUtils.dirCopy("C:/Test", "D:/Temp")
 * FileUtils.dirCopy("C:/Test", "D:/Temp", true) - Checks Existness
 * </pre></blockquote>
 *
 * Copy a File:
 * <blockquote><pre>
 * FileUtils.fileCopy("C:/Test/abc.doc", "D:/Temp/xyz.doc")
 * FileUtils.fileCopy("C:/Test/abc.doc", "D:/Temp/xyz.doc", true) - Checks Existness
 * </pre></blockquote>
 * <p>
 *
 *
 * @author: Balachandar
 */
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileUtils {
    /**
     * FileUtils constructor comment.
     */
    public FileUtils() {
        super();
    }
    /**
     * Description : This method is used to Create the Directory in the Server
     */
    public static boolean createDir(String dir) {
        boolean result = false;
        try {
            File newDir = new File(dir);
            if (newDir.mkdir()) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception ioe) {
            result = false;
        }
        return result;
    }
    /**
     * Description : Listing all the files and Directories and creating the same in target place
     *
     * @return boolean		(success or not.)
     * @param source File  	(Source Directory Name)
     * @param sourcelen int (Length of the Source String)
     * @param target File	(Target Directory Name)
     * @param checkflag boolean (Checking the Existness based on this flag)
     */
    
    private static boolean dirCopy(String sourcestr, int sourcelen, String targetstr, boolean checkflag) {
        try {
            String str = "", currdir = "";
            File source = new File(sourcestr);
            File target = new File(targetstr);
            
            // Getting all the files and Directories
            File files[] = source.listFiles();
            
            for (int i = 0, j = files.length; i < j; i++) {
                
                // Creating Directory
                if (files[i].isDirectory()) {
                    currdir = target.getPath() + files[i].getPath().substring(source.getPath().length());
                    File targetDir = new File(currdir);
                    targetDir.mkdir();
                    
                    // Recursive calling
                    dirCopy(files[i].getPath(), sourcelen, target.getPath(), checkflag);
                    
                } else { // if
                    // Creating the file.
                    String chk = files[i].getName();
                    currdir = target.getPath() + "\\" + files[i].getPath().substring(sourcelen);
                    fileCopy(files[i].getAbsolutePath(), currdir, checkflag);
                } // else
            } // for
            return true;
        } catch (Exception exc) { // try
            return false;
        } // catch
    } // dirList
    /**
     * Description : Listing all the files and Directories and creating the same in target place
     *
     * @return boolean		(success or not.)
     *
     * @param source String (Source Directory Name)
     * @param target String	(Target Directory Name)
     */
    
    public static boolean dirCopy(String sourcestr, String targetstr) {
        return dirCopy(sourcestr.trim(), sourcestr.trim().length(), targetstr.trim(), false);
    }
    /**
     * Description : Listing all the files and Directories and creating the same in target place
     *
     * @return boolean		(success or not.)
     *
     * @param sourcestr String 	(Source Directory Name)
     * @param targetstr String 	(Target Directory Name)
     * @param checkflag boolean	(Target Directory Name)
     */
    
    public static boolean dirCopy(String sourcestr, String targetstr, boolean existCheck) {
        return dirCopy(sourcestr.trim(), sourcestr.trim().length(), targetstr, existCheck);
    }
    /**
     * Description : Coping Source file to Target place.
     * @param source String		(Source File with full Path)
     * @param target String 	(Target File Name)
     */
    public static void fileCopy(String source, String target) throws Exception {
        fileCopy(source, target, false);
    }
    /**
     * Description : Coping Source file to Target place.
     * @param source String		(Source File with full Path)
     * @param target String 	(Target File Name)
     * @param checkflag boolean (Checking the Existness based on this flag)
     */
    public static void fileCopy(String source, String target, boolean checkflag) throws Exception {
        try {
            // Target file Existness checking
            if (!new File(target).exists() || checkflag != true) {
                FileReader istr = new FileReader(source);
                FileWriter fstr = new FileWriter(target);
                
                // Reading the file
                char[] buf = new char[8192];
                int count = istr.read(buf);
                while (count != -1) {
                    if (count > 0)
                        fstr.write(buf, 0, count);
                    count = istr.read(buf);
                }
                fstr.flush();
                fstr.close();
            } else { // if !exists
                throw new Exception("File Creation Exception");
            } // else
        } catch (Exception exc) { // try
            throw new Exception();
        } // catch
    } // fileCopy
    
    /**
     * Starts the application.
     * @param args an array of command-line arguments
     */
    public static void main(java.lang.String[] args) {
        // Insert code to start the application here.
        try {
            System.out.println(FileUtils.dirCopy("C:/test/", "D:/server/test/"));
            // FileUtils.fileCopy("c:/xml/test.xml", UNSConstants.INITDIR + "/" + projId + "ttt.xml", false);
        } catch (Exception ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}
