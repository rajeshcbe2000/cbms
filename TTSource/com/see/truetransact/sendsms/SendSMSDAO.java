/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SMSParameterDAO.java
 *
 * Created on Fri May 04 13:31:47 IST 2012
 */

package com.see.truetransact.sendsms;

import java.util.HashMap;

import com.see.truetransact.serverside.TTDAO;
import java.net.HttpURLConnection;


/**
 * SMSParameter DAO.
 *
 */

public class SendSMSDAO extends TTDAO {
//    private static SqlMap sqlMap = null;
//    private SMSParameterTO objTO;
    private String url = null;
    
    /** Creates a new instance of SMSParameterDAO */
    public SendSMSDAO() throws Exception {
//        ServiceLocator locate = ServiceLocator.getInstance();
//        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
//        List list = (List) sqlMap.executeQueryForList("getSelectSMSParameter", map);
//        if (list!=null && list.size()>0) {
//            returnMap.put("SMSParameterTO", (SMSParameterTO)list.get(0));
//        }
        return returnMap;
    }
    
    private void insertData() throws Exception {
//        try {
//            sqlMap.startTransaction();
//            sqlMap.executeUpdate("insertSMSParameter", objTO);
//            sqlMap.commitTransaction();
//        } catch (SQLException e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
//        }
    }
    
    private void updateData() throws Exception {
//        try {
//            sqlMap.startTransaction();
//            sqlMap.executeUpdate("updateSMSParameter", objTO);
//            sqlMap.commitTransaction();
//        } catch (SQLException e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
//        }
    }
    
    private void deleteData() throws Exception {
//        try {
//            sqlMap.startTransaction();
//            sqlMap.executeUpdate("deleteSMSParameter", objTO);
//            sqlMap.commitTransaction();
//        } catch (SQLException e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
//        }
    }
    
    public static void main(String str[]) {
        try {
            SendSMSDAO dao = new SendSMSDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public HashMap execute(HashMap map)  throws Exception {
        url = (String)map.get("URL");
        HashMap returnMap = new HashMap();
        String reply="";        
        java.io.BufferedReader in = null;
        try {
            System.out.println("-->> Inside sendSMS...");
            
            System.out.println("#$#$ URL "+url);
            // Uncomment the following block for sending SMS
            java.net.URL myURL = new java.net.URL(url);
            System.out.println("myURL...replay..."+myURL);
            HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
            System.out.println("in...replay..."+in);
            String inputLine="";
            while((inputLine = in.readLine()) != null) {
                reply=inputLine;
                System.out.println("sendSMS...replay..."+reply);
                returnMap.put("REPLY",reply);
            }
            if (in!=null) {
                in.close();
                myURL = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("REPLY","EXCEPTION");
            try {
                if (in!=null) {
                    in.close();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
//        if (reply.length()>0) {
//            try{
//                int Intreply = Integer.parseInt(reply);
//                returnMap.put("REPLY", "DELIVERD");
//            }catch(NumberFormatException e){
//                returnMap.put("REPLY", "UNDELIVERD");
//            } 
//        }else{
//                returnMap.put("REPLY", "UNDELIVERD");
//        }
        //return new HashMap();
        return returnMap;
    }
    
    private void authorize(HashMap map) throws Exception {
//        try {
//            sqlMap.startTransaction();
//            sqlMap.executeUpdate("authorizeSMSParameter", objTO);
//            sqlMap.commitTransaction();
//        } catch (SQLException e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
//        }
    }
    
    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }
    
    private void destroyObjects() {
//        objTO = null;
    }
}
