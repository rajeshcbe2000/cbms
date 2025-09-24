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

package com.see.truetransact.clientexception;
import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.StringEncrypter;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
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
 * @author karthik,Bala
 */
public class ClientParseException {
    private  Object[] options = {CommonConstants.OK};
    private static ClientParseException me;
    private FileOutputStream fileOutputStream;
    private final String seperator = "******************************************************************************\r\n";
    
    static{
        me = new ClientParseException();
    }
    
    /** Creates a new instance of ParseException */
    private ClientParseException(){
        //System.out.println("ClientParseException Constructor");
        try {
            fileOutputStream = new FileOutputStream(getErrorLogFile(), true);
        }catch(Exception except) {
            except.printStackTrace();
            //showWarningOptionPane(CommonConstants.OTHEREXCEPTION);
        }
    }
    
    public void closeErrorLogFile(){
        try {
            //System.out.println("Closing file");
            fileOutputStream.close();
        }catch(Exception except) {
            except.printStackTrace();
            //showWarningOptionPane(CommonConstants.OTHEREXCEPTION);
        }
    }
    
    public static ClientParseException getInstance(){
        return me;
    }
    
    /** Logs the exception details in a log file
     * message based on parameter
     * @param exception For getting the exception information
     */
    public void logException(java.lang.Exception exception) {
        try {
            writeException(exception);
        }catch(Exception except) {
            except.printStackTrace();
            //showWarningOptionPane(CommonConstants.OTHEREXCEPTION);
        }
    }
    
    /** Logs the exception details in a log file and displays appropriate error
     * message based on parameter
     * @param exception For getting the exception information
     */
    public int logException(java.lang.Exception exception, boolean displayAlert) {
        int result = -1;
        try {
            logException(exception);
            if( displayAlert ){
                result = displayAlert(exception);
            }
        }catch(Exception except) {
            except.printStackTrace();
            //showWarningOptionPane(CommonConstants.OTHEREXCEPTION);
        }
        setDialogOptions(options);
        return result;
    }
    
    private void writeException(Exception exception) throws Exception{
        fileOutputStream.write(new Date().toString().getBytes());
        fileOutputStream.write("\r\n".getBytes());
        exception.printStackTrace(new PrintStream(fileOutputStream));
        fileOutputStream.write(seperator.getBytes());
    }
    
    /** Returns the logfile with path after checking whether the directory exists or
     * not. Creates the directory, if not exists
     * @return Returns errorLogFile with Path
     */
    public String getErrorLogFile() throws Exception{
        //System.out.println("Getting error log file");
        final StringBuffer logDirectory = new StringBuffer().append(System.getProperty("user.home")).append("/tt/log");
        final File ttLogDirectory = new File(logDirectory.toString());
        //Creates the directory, if not exists
        if( !ttLogDirectory.exists() ) {
            ttLogDirectory.mkdirs();
            
            File f = new File(System.getProperty("user.home")+"\\tt\\"+"TTLicense.dat");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            Date dt = new Date();
            String dtStr = (new StringEncrypter()).encrypt(String.valueOf(dt.getTime()));
            fo.write(dtStr.getBytes());
            fo.close();
        }
        
        final StringBuffer errorLogFile = new StringBuffer().append(logDirectory).append("/errorlog.txt");
        return errorLogFile.toString();
    }
    
    /** To return the type of exception passed, for instance, if a
     * NullPointerException is passed, this will return
     * java.lang.NullPointerException
     * @param exception Thrown Exception
     * @return exceptiontype
     */
    private String getExceptionType(java.lang.Exception exception) throws Exception{
        final String exceptionType = exception.toString();
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
    private String getExceptionName(String exceptionType)  throws Exception{
        final int index = exceptionType.indexOf(":");
        final String exceptionName;
        if( index > 0 )
            exceptionName = exceptionType.substring(0, index);
        else
            exceptionName = exceptionType;
        return exceptionName;
    }
    
    public void setDialogOptions(Object[] options) {
        this.options = options;
    }
    
    /** To show an user friendly display
     * @param exceptionType Exception Type such as java.lang.NullPointerException
     */
    public int showWarningOptionPane(String exceptionType){
        
        return JOptionPane.showOptionDialog(null, exceptionType, CommonConstants.WARNINGTITLE,
        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
    
    /** To display the alert message for the exception passed. This will inturn call
     * showWarningOptionPane
     * @param exception Exception thrown
     */
    public int displayAlert(java.lang.Exception exception) {
        int result = -1;
        try {
            String exceptionType = getExceptionType(exception);
            //To check if the exception is of type TTException and if so process it or
            //simply show the warning message
            if( exceptionType.equals("com.see.truetransact.commonutil.TTException") ){
                System.out.println("exceptionType:" + exceptionType);
                result = processTTException(exception);
            }else{
                System.out.println("exceptionType:" + exceptionType);
                //showWarningOptionPane( getWarningMessage(exceptionType) );
            }
        }catch(Exception except){
            except.printStackTrace();
            //showWarningOptionPane(CommonConstants.OTHEREXCEPTION);
        }
        return result;
    }
    
    private int processTTException(Exception exception){
        int result = -1;
        TTException tte = (TTException)exception;
        HashMap exceptionMap = tte.getExceptionHashMap();
        StringBuffer exceptionBuffer = new StringBuffer();
        if(exceptionMap != null){
            System.out.println("exceptionMap:" + exceptionMap);
            ArrayList exceptionList = (ArrayList)exceptionMap.get(CommonConstants.EXCEPTION_LIST);
            String exceptionConstantClass = (String)exceptionMap.get(CommonConstants.CONSTANT_CLASS);
            ExceptionHashMap objExceptionHashMap = null;
            try{
                objExceptionHashMap = (ExceptionHashMap)Class.forName(exceptionConstantClass).newInstance();
            } catch(Exception e){
                e.printStackTrace();
            }
            HashMap exceptionValueHashMap = objExceptionHashMap.getExceptionHashMap();
            int exceptionListSize = exceptionList.size();
            
            String strExc = null, strData = "";
            HashMap excMap = null;
            for (int i = 0; i < exceptionListSize; i++){
                strData = "";
                if (exceptionList.get(i) instanceof HashMap) {
                    excMap = (HashMap) exceptionList.get(i);
                    strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE));
                    strData = " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";

                } else {
                    strExc = (String) exceptionList.get(i);
                }
                exceptionBuffer.append((String)exceptionValueHashMap.get( strExc ));
                exceptionBuffer.append(strData);
                exceptionBuffer.append("\n");
            }
            
            // Based on Exception Type it appends value...
            if (exceptionMap.containsKey(CommonConstants.EXCEPTION_TYPE)) {
                String strExcType = (String) exceptionMap.get(CommonConstants.EXCEPTION_TYPE);
                if (strExcType.equals(CommonConstants.CONCAT_EXCEPTION)) {
                    exceptionBuffer.append(exceptionMap.get(CommonConstants.SERVER_VALUE));
                }
            }
        }
        else{
            exceptionBuffer.append(tte.getMessage());
        }
        
        if (exceptionBuffer.toString().length() > 0)
            result = showWarningOptionPane(exceptionBuffer.toString());
            
        return result;
    }
    
    /** To get the warning message from the Resource bundle for the given exceptionName
     * @param exceptionName To get the corresponding value from Resource bundle
     * @return Warning message
     */
    private String getWarningMessage(java.lang.String exceptionName) throws Exception{
        ClientExceptionRB exceptionResources = new ClientExceptionRB();
        return exceptionResources.getString(getExceptionResourceName(exceptionName));
    }
    
    /** To get the ExceptionResourceName if a non TTException occurs
     * @param exceptionName Exception name like java.lang.NumberFormatException
     * @return ExceptionResourceName if a non TTException occurs
     */
    private String getExceptionResourceName(String exceptionName)  throws Exception{
        if( !exceptionName.startsWith(CommonConstants.TTPACKAGE)) {
            exceptionName = CommonConstants.OTHEREXCEPTION;
        }
        return exceptionName;
    }
    
}
