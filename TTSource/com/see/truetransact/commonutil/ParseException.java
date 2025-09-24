/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParseException.java
 *
 * Created on July 9, 2003, 4:37 PM
 */

package com.see.truetransact.commonutil;
//import org.apache.log4j.Logger;
import javax.swing.JOptionPane;
import com.see.truetransact.commonutil.CommonConstants;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/** ParseException class is used to populate the exception details in a file.
 *
 * Usage:
 * This class is used to be called in a top level class, say A, which initiates a method
 * in other class B, and if any subsequent exceptions occur in B, that will be
 * caught over here and logged.
 *
 * public void logon()
 * {
 *    try
 *    {
 *        Statements..
 *    }catch(Exception exception)
 *    {
 *        new ParseException().logException(exception);
 *    }
 *
 * }
 * @author karthik
 */
public class ParseException {
    /** To get the logger for logging the Exception details */    
   // private final static Logger log = Logger.getLogger(ParseException.class);
    /** Creates a new instance of ParseException */
    public ParseException(){
    }
    
    /** Logs the exception details in a log file 
     * @param exception For getting the exception information
     */    
    public void logException(java.lang.Exception exception) {
        //exception.printStackTrace();
       /* StackTraceElement[] stackTraceElementArray = exception.getStackTrace();
        int stackTraceElementArrayLength = stackTraceElementArray.length;
        StringBuffer stackTrace = new StringBuffer();
        
        stackTrace.append(exception.toString()).append("\n");
        for(int i = stackTraceElementArrayLength, j = 0; i > 0; i--, j++)
        {
            stackTrace.append(" at ").append(stackTraceElementArray[j].toString()).append("\n");
        }
        stackTrace.append("**************************************************************************************************");
        log.error(stackTrace.toString());
        stackTrace = null;
        stackTraceElementArray = null;*/
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(getErrorLogFile(), true);
            Date currentDate = new Date();
            fileOutputStream.write(currentDate.toString().getBytes());
            fileOutputStream.write("\r\n".getBytes());
            exception.printStackTrace(new PrintStream(fileOutputStream));
            String seperator = "******************************************************************************\r\n";
            fileOutputStream.write(seperator.getBytes());
            
        }catch(Exception except)
        {
            except.printStackTrace();
            showWarningOptionPane(CommonConstants.OTHEREXCEPTION);
        }
    }
    
    /** Returns the logfile with path after checking whether the directory exists or
     * not. Creates the directory, if not exists
     * @return Returns errorLogFile with Path
     */    
    private String getErrorLogFile()
    {
        StringBuffer logDirectory = new StringBuffer().append(System.getProperty("user.home")).append("/tt/log");
        File ttLogDirectory = new File(logDirectory.toString());
        if( !ttLogDirectory.exists() ) {
            ttLogDirectory.mkdirs();
        }
        StringBuffer errorLogFile = new StringBuffer().append(logDirectory).append("/errorlog.txt");
        return errorLogFile.toString();
    }
    
    /** To return the type of exception passed, for instance, if a
     * NullPointerException is passed, this will return
     * java.lang.NullPointerException
     * @param exception Thrown Exception
     * @return exceptiontype
     */    
    public String getExceptionType(java.lang.Exception exception) {
        
        String exceptionType = exception.toString();
        return getExceptionName(exceptionType);
    }
    
    /** To parse the Exception Name from a string with Exception message. If there is an
     * exception with message, it will be displayed like,
     * java.lang.NullPointerException: Appliaction Error
     *
     * Here, before ":" is the actual exception & after ":" is the message.
     * This method is used to return the before ":" part if the message also exists,
     * otherwise simply returns the same string
     * @param exceptionType ExceptionType with Exception Name & message
     * @return Exception Name
     */    
    private String getExceptionName(String exceptionType){
        int index = exceptionType.indexOf(":");
        String exceptionName;
        if( index > 0 )
            exceptionName = exceptionType.substring(0, index);
        else
            exceptionName = exceptionType;
        return exceptionName;
    }
    
    /** To show an user friendly display
     * @param exceptionType Exception Type such as java.lang.NullPointerException
     */    
    public void showWarningOptionPane(String exceptionType){
        Object[] options = { CommonConstants.OK};
        JOptionPane.showOptionDialog(null, exceptionType, CommonConstants.WARNINGTITLE,
        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
        null, options, options[0]);
    }
   
    /** To display the alert message for the exception passed. This will inturn call
     * showWarningOptionPane
     * @param exception Exception thrown
     */    
    public void displayAlert(java.lang.Exception exception) {
        String exceptionType = getExceptionType(exception);
        System.out.println(exceptionType);
        
        showWarningOptionPane( getWarningMessage(exceptionType) );
    }    
    
    /** To get the warning message from the Resource bundle for the given exceptionName
     * @param exceptionName To get the corresponding value from Resource bundle
     * @return Warning message
     */    
    public String getWarningMessage(java.lang.String exceptionName) {
        ExceptionResources exceptionResources = new ExceptionResources();
        return exceptionResources.getString(getExceptionResourceName(exceptionName));
    }
    
    /** To get the ExceptionResourceName if a non TTException occurs
     * @param exceptionName Exception name like java.lang.NumberFormatException
     * @return ExceptionResourceName if a non TTException occurs
     */    
    private String getExceptionResourceName(String exceptionName){
        if( !exceptionName.startsWith(CommonConstants.TTPACKAGE))
        {
            exceptionName = CommonConstants.OTHEREXCEPTION;
        }
        return exceptionName;
    }
    
}
