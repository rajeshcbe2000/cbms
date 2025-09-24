/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingOB.java
 *
 * Created on 16 September, 2011, 12:45 PM
 */

package com.see.truetransact.ui.trading;

import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import com.see.truetransact.commonutil.CommonConstants;
import java.util.Date;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.trading.TradingTO;
import java.util.*;
import com.see.truetransact.commonutil.CommonUtil;
/**
 *
 * @author  aravind
 */
public class TradingOB extends java.util.Observable{
    
    /**
     *
     */
     private double fMass = 0.0d;
   private final static Logger log = Logger.getLogger(TradingOB.class);//Creating Instace of Log
   private final static ClientParseException parseException = ClientParseException.getInstance();
   private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private  String dataFileName ;
    private  TradingTO objTradingTO;
    public ArrayList listTradingTo ;
    private static TradingOB objTradingOB;//Singleton Object Reference
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
     private ProxyFactory proxy;
      private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
      ArrayList aList = new ArrayList();
      ArrayList dataTransList= new ArrayList();
       ArrayList dataCashList= new ArrayList();
       Date curDate = null;
    /** Creates a new instance of ImportPageOB */
    public TradingOB() {
       objTradingTO = new TradingTO();
       listTradingTo = new ArrayList();
       curDate = ClientUtil.getCurrentDate();       //Added By Suresh R
         try {
            proxy = ProxyFactory.createProxy();
            setOperationMap();
           // initUIComboBoxModel();
          //  fillDropdown();
        }catch(Exception e){
            parseException.logException(e,true);
        }
      
    }
     static {
        try {
            log.info("Creating ParameterOB...");
            objTradingOB = new TradingOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
     // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "TradingJNDI");
        map.put(CommonConstants.HOME, "trading.TradingHome");
        map.put(CommonConstants.REMOTE, "trading.Trading");
    }
	public String getFilePath() {
		return dataFileName;
	}
	public void setFilePath(String dataFileName) {
		dataFileName = dataFileName;
	}
        public ArrayList getCashDataList()
        {
            return dataCashList;
        }
        public void setCashDataList(ArrayList dataCashList) {
		dataCashList = dataCashList;
	}
         public ArrayList getTransDataList()
        {
            return dataTransList;
        }
        public void setTransDataList(ArrayList dataTransList) {
		dataTransList = dataTransList;
	}
    public boolean parseImportData(){
        boolean flag = false;
        // System.out.println("File:"+fName);
        try{
         /**
         * Creating a buffered reader to read the file
         */
        BufferedReader bReader = new BufferedReader(
                new FileReader(dataFileName));
           //  BufferedReader bReader = new BufferedReader(
              //  new FileReader(fName));
        
        String line;
      String pattern = "dd/MM/yyyy";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        /**
         * Looping the read block until all lines in the file are read.
         */
       while ((line = bReader.readLine()) != null) {
       // if ((line = bReader.readLine()) != null) {
            objTradingTO = new TradingTO();
            /**
             * Splitting the content of tabbed separated line
             */
            String datavalue[] = line.split("\t");
            System.out.println("datavalue[] 13 IN======"+datavalue[12]);
            String mode=datavalue[12];
            if(mode.equalsIgnoreCase("CASH"))
            {
            objTradingTO.setMode(datavalue[12]);
            objTradingTO.setTransID(datavalue[0]);
            objTradingTO.setAchdID(datavalue[1]);
            objTradingTO.setAMOUNT(Double.valueOf(datavalue[2]));
            double val=0;
            objTradingTO.setInpAMOUNT(val);
            objTradingTO.setTrans_DT(format.parse(datavalue[3]));
            objTradingTO.setTransTYPE(datavalue[4]);
            objTradingTO.setPARTICULARS(datavalue[5]);
            objTradingTO.setSTATUS(datavalue[6]);
            objTradingTO.setAuthorizeSTATUS(datavalue[7]);
            objTradingTO.setStatusBY(datavalue[8]);
            objTradingTO.setBranchID(datavalue[9]);
            objTradingTO.setStatusDT(format.parse(datavalue[10]));
            objTradingTO.setInitiatedBRANCH(datavalue[11]);
           // objTradingTO.setTransMode("TRANSFER");
            listTradingTo.add(objTradingTO);
            setCashDataList(listTradingTo);
            System.out.println("listTradingTo IN TO FILE==="+listTradingTo);
            }
            else
            {
                String accNo = datavalue[2];
             objTradingTO.setAchdID(datavalue[1]);   
            objTradingTO.setMode(datavalue[12]);//16
            objTradingTO.setTransID(datavalue[0]);//0
            //set ac hd id
             //set ac hd id
            String acNo=datavalue[2];
            if(acNo!=null && !acNo.equalsIgnoreCase(""))
            {
                      HashMap singleAuthorizeMap = new HashMap();
                     singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                     if(acNo.length()>6)
                     {
                         String prodNp=acNo.substring(4,7);
                         singleAuthorizeMap.put("PROD_ID", prodNp);
                         singleAuthorizeMap.put("PROD_TYPE", "OA");
                         singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
                         List aList= ClientUtil.executeQuery("getAccHeadHashMap", singleAuthorizeMap);
                         for(int i=0;i<aList.size();i++)
                         {
                             HashMap map=(HashMap)aList.get(i);
                             String Ac_Hd_id=map.get("AC_HD_ID").toString();
                             objTradingTO.setAchdID(Ac_Hd_id);
                         }
                     }
                     else
                     {
                         objTradingTO.setAchdID(datavalue[1]);  
                     }
            }
                     ///
            ////
           // objTradingTO.setAchdID(datavalue[1]);
            objTradingTO.setActNUM(datavalue[2]);
           
            aList.add(datavalue[2]);
            objTradingTO.setAMOUNT(Double.valueOf(datavalue[3]));
            objTradingTO.setTrans_DT(format.parse(datavalue[4]));
            objTradingTO.setTransTYPE(datavalue[5]);
            double val=0;
             objTradingTO.setInpAMOUNT(val);
            objTradingTO.setPARTICULARS(datavalue[6]);
            objTradingTO.setSTATUS(datavalue[7]);
            objTradingTO.setAuthorizeSTATUS(datavalue[8]);
            objTradingTO.setStatusBY(datavalue[9]);
            objTradingTO.setBranchID(datavalue[10]);
            objTradingTO.setStatusDT(format.parse(datavalue[11]));
            objTradingTO.setInitiatedBRANCH(datavalue[14]);
            objTradingTO.setTransMode("TRANSFER");
            objTradingTO.setProdTYPE(datavalue[12]);
            listTradingTo.add(objTradingTO);
             System.out.println("listTradingTo IN TO FILE222222222==="+listTradingTo);
            setTransDataList(listTradingTo);
            
            }
            String value1 = datavalue[0];
            String value2 = datavalue[1];
            String value3 = datavalue[2];
            String value4 = datavalue[3];
            String value5 = datavalue[4];
            String value6 = datavalue[5];
            String value7 = datavalue[6];
            String value8 = datavalue[7];
            String value9 = datavalue[8];
            String value10 = datavalue[9];
            String value11 = datavalue[10];
            String value12 = datavalue[11];
           // int value3 = Integer.parseInt(datavalue[2]);
           // double value4 = Double.parseDouble(datavalue[3]);
 
            /**
             * Printing the value read from file to the console
             */
            System.out.println(value1 + "\t" + value2 + "\t" + value3 + "\t" + value4+ "\t"+value5 + "\t" + value6 + "\t" + value7 + "\t" + value8+ "\t"+value9 + "\t" + value10 + "\t" + value11 + "\t" + value12);
        }
        bReader.close();
        flag = true;
    }
      catch(Exception e){
          System.out.println("Exce occ");
          e.printStackTrace();
          flag = false;
          
    }
        return flag;
    }
   public boolean  getAccountNos()
   {
       try
       {
           boolean check1=false;
           System.out.println("aList.6666666666666666666== getAccountNos:"+aList);
           if(aList.size()>0)
           {
                for(int i=0;i<aList.size();i++)
                {
                     String accId=aList.get(i).toString();
                     if(accId==null || accId.equalsIgnoreCase(""))
                     {
                        continue;
                     }
                      System.out.println("accId getAccountNos:"+accId);
                     HashMap singleAuthorizeMap = new HashMap();
                     singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                     singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, (Date)curDate.clone());
                     singleAuthorizeMap.put("ACT_NUM",accId);
                     singleAuthorizeMap.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
                     List list= ClientUtil.executeQuery("getAccNoExitsImportHashMap", singleAuthorizeMap);
                        System.out.println("list 09090890790890===:"+list); 
                      for(int j=0;j<list.size();j++)
                      {
                                  HashMap map=(HashMap)list.get(j);
                                
                                  int countNo=CommonUtil.convertObjToInt(map.get("COUNT"));
                                   System.out.println("accId countNo 09090890790890===:"+countNo); 
                                  if(countNo==0)
                                  {
                                      check1=true;
                                  }
                       }
                    if(check1){
                        System.out.println("accid inside check1"+accId ); 
                      HashMap singleAuthorizeMap1 = new HashMap();
                     singleAuthorizeMap1.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                     singleAuthorizeMap1.put(CommonConstants.AUTHORIZEDT, (Date)curDate.clone());
                     singleAuthorizeMap1.put("AC_HD_ID",accId);
                     singleAuthorizeMap1.put(CommonConstants.BRANCH_ID,  ProxyParameters.BRANCH_ID);
                     System.out.println("achdid===="+singleAuthorizeMap1.get("AC_HD_ID"));
                     List list1= ClientUtil.executeQuery("getAcHdIdExitsImportHashMap", singleAuthorizeMap1);
                      for(int j=0;j<list1.size();j++)
                      {
                                  HashMap map=(HashMap)list1.get(j);
                                
                                  int countNo=CommonUtil.convertObjToInt(map.get("COUNT"));
                                   System.out.println("accId countNo 09090890790890===:"+countNo); 
                                  if(countNo==0)
                                  {
                                      System.out.println("falsee");
                                      return false;
                                   
                                  }
                       }
                    }
                }
           }
        
           
       }
       catch(Exception e)
       {
           e.printStackTrace();
           
       }
         return true;
   }
     /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            System.out.println("Command:"+command);
            HashMap term = new HashMap();
          //  term.put(CommonConstants.MODULE, getModule());
           // term.put(CommonConstants.SCREEN, getScreen());
          //  term.put(CommonConstants.USER_ID, "0001");
          //  term.put(CommonConstants.BRANCH_ID, "0001");
            for(int i =0;i<listTradingTo.size();i++){
               objTradingTO = (TradingTO)listTradingTo.get(i);
               objTradingTO.setCommand(command);
               System.out.println("LIST: "+objTradingTO.getTransID());
           
            term.put("TradingTO", objTradingTO);
            HashMap proxyReturnMap = proxy.execute(term, map);
//            setProxyReturnMap(proxyReturnMap);
            setResult(getActionType());
             }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
     private TradingTO getTradingTO(String command){
    //  TradingTO objTradingTO = new TradingTO();
       // objTradingTO.setCommand(command);
       // parseImportData();
         objTradingTO.setCommand(command);
      System.out.println("Ret Val new: "+objTradingTO.getTransID());
      
         return objTradingTO;
     }
     public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
       public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }
      /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    
    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    public int getResult(){
        return _result;
    }
    
    public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}
        
         public void resetForm(){
             setDataFileName("");
             objTradingTO = null;
         }
 public static void main(String args[]) throws Exception {
        /**
         * Source file to read data from.
         */
    	 String dataFileName = "/home/jobish/expo.txt";
        new TradingOB().parseImportData();
      }
}