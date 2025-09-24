/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * BusinessRuleExceptionWriter.java
 *
 * Created on December 16, 2003, 10:36 AM
 */

package com.see.truetransact.businessdelegate.ejb;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;

import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.operativeaccount.LienMarkingTO;
/**
 *
 * @author  karthik
 */
public class BusinessRuleExceptionWriter {
    private FileOutputStream fileOutputStream;
    private final StringBuffer logDirectory = new StringBuffer().append(System.getProperty("user.home")).append("/tt/log/businesserror/");
    /** Creates a new instance of BusinessRuleExceptionWriter */
    public BusinessRuleExceptionWriter() {
        try {
            fileOutputStream = new FileOutputStream(getErrorLogFile(), true);
        }catch(Exception except) {
            except.printStackTrace();
        }
    }
    
    /** To close the error log file
     */
    private void closeErrorLogFile(){
        try {
            //System.out.println("Closing file");
            fileOutputStream.close();
        }catch(Exception except) {
            except.printStackTrace();
        }
    }
    /** Returns the logfile with path after checking whether the directory exists or
     * not. Creates the directory, if not exists
     * @return Returns errorLogFile with Path
     */
    private String getErrorLogFile() throws Exception{
        //System.out.println("Getting error log file");
        final File ttLogDirectory = new File(logDirectory.toString());
        //Creates the directory, if not exists
        if( !ttLogDirectory.exists() ) {
            ttLogDirectory.mkdirs();
        }
        final StringBuffer errorLogFile = new StringBuffer().append(logDirectory).append("businesserror.xml");
        return errorLogFile.toString();
    }
    
    
    /** Logs the exception details in a XML file
     * with the object
     * @param exception For getting the exception information
     */
    public void logException(java.lang.Exception exception, HashMap obj) {
        try {
            //System.out.println("HashMap Object value from BusinessRuleExceptionWriter before serialization:" + obj);
           // test(obj);
            
            final long currentTime = System.currentTimeMillis();
            final StringBuffer serFileName = new StringBuffer().append(currentTime).append(".ser");
            FileOutputStream serFileOutputStream = new FileOutputStream(logDirectory.toString()+serFileName);
            ObjectOutputStream serObjectOutputStream = new ObjectOutputStream(serFileOutputStream);
            serObjectOutputStream.writeObject(obj);
            serObjectOutputStream.close();
            serFileOutputStream.close();
            
            
            StringBuffer businessException = new StringBuffer();
            businessException.append("<Exceptions><Exception><InputData>").append(serFileName);
            businessException.append("</InputData></Exception></Exceptions>");
            fileOutputStream.write(businessException.toString().getBytes());
            fileOutputStream.write("\r\n".getBytes());
            // fileOutputStream.write("**************".getBytes());
            /*XML xmlExceptions = new XML("Exceptions");
            XML xmlException = new XML("Exception");
             
            XML xmlInputData = new XML("InputData");
            xmlInputData.addAttribute("data", obj);
            //System.out.println("Cause"+exception.getCause());
            xmlInputData.addElement(obj.toString());
             
            xmlException.addElement(xmlInputData);
            xmlExceptions.addElement(xmlException);
             
            fileOutputStream.write(xmlExceptions.toString().getBytes());
            fileOutputStream.write("\r\n".getBytes());*/
            //writeException(exception);
            
            FileInputStream fis = new FileInputStream(logDirectory.toString()+serFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            // Read object.
            HashMap value = (HashMap) ois.readObject();
            
            //System.out.println("HashMap Object value from BusinessRuleExceptionWriter after serialization:" + value);
            //test(value);
        }catch(Exception except) {
            except.printStackTrace();
        }
    }
    
    private void test(HashMap obj){
        final Iterator iterator = obj.values().iterator();
        // strat the transaction
        LienMarkingTO lto;
        for( int i = obj.size(); i > 0 ; i--){
            lto = (LienMarkingTO)iterator.next();
            System.out.println(lto.getActNum()  );
            System.out.println(lto.getBankId()  );
            System.out.println(lto.getBranchId()  );
            System.out.println( lto.getLienAcHd() );
            System.out.println(lto.getLienActNum()  );
            System.out.println( lto.getLienAmt() );
            System.out.println( lto.getLienDt() );
            System.out.println( lto.getLienId() );
            System.out.println( lto.getLienStatus() );
            System.out.println( lto.getLienType() );
        }
    }
    /*private void writeException(Exception exception) throws Exception{
        fileOutputStream.write(DateUtil.getDateMMDDYYYY(null).toString().getBytes());
        fileOutputStream.write("\r\n".getBytes());
        exception.printStackTrace(new PrintStream(fileOutputStream));
        fileOutputStream.write(seperator.getBytes());
    }*/
    
    protected void finalize(){
        closeErrorLogFile();
    }
    
    
}
