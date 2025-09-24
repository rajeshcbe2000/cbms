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
package com.see.truetransact.serverside.sms;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.SQLException;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.serverside.termloan.charges.TermLoanChargesDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.sms.SMSParameterTO;
import com.see.truetransact.commonutil.CommonUtil;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * SMSParameter DAO.
 *
 */
public class SMSParameterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private SMSParameterTO objTO;

    /**
     * Creates a new instance of SMSParameterDAO
     */
    public SMSParameterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectSMSParameter", map);
        if (list != null && list.size() > 0) {
            returnMap.put("SMSParameterTO", (SMSParameterTO) list.get(0));
        }
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertSMSParameter", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateSMSParameter", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteSMSParameter", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            SMSParameterDAO dao = new SMSParameterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        if (map != null && map.containsKey("CUSTOMER_GROUP") && map.get("CUSTOMER_GROUP") != null) {

            sendSMS(map);
        }else if (map != null && map.containsKey("SMS_ACCOUNT_LIST") && map.get("SMS_ACCOUNT_LIST") != null) {
            sendShareBulkSMS(map);
        } else {
            objTO = (SMSParameterTO) map.get("SMSParameterTO");
            final String command = objTO.getCommand();

            if (command != null) {
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData();
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData();
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData();
                } else {
                    throw new NoCommandException();
                }
            }
            if (map.containsKey("AUTHORIZEMAP")) {
                authorize(map);
            }
        }
        destroyObjects();
        return new HashMap();
    }

    private void authorize(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("authorizeSMSParameter", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }

    private void sendSMS(HashMap dataMap) throws Exception {
        StringBuffer buffer = new StringBuffer();
        StringBuffer mobileNobuffer = new StringBuffer();
        String message = "";
        TermLoanChargesDAO chargesDAO = new TermLoanChargesDAO();
        SmsConfigDAO confDAO = new SmsConfigDAO();
        ArrayList list = (ArrayList) dataMap.get("CUSTOMER_GROUP");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                HashMap resultMap = (HashMap) list.get(i);
                buffer.append("'");
                buffer.append(CommonUtil.convertObjToStr(resultMap.get("CUSTOMER_GROUP")));
                buffer.append("'");
                message = CommonUtil.convertObjToStr(resultMap.get("MESSAGE"));
                if (list.size() - 1 != i) { //&& i !=0
                    buffer.append(",");
                }

            }
        }
        System.out.println("buffer.toString()#### 0000" + buffer.toString() + "message####" + message);

        dataMap.put("CUST_GROUP", buffer.toString());
        List lst = sqlMap.executeQueryForList("getSelectGroupMobileParameter", dataMap);
        if (lst != null && lst.size() > 0) {
            buffer = new StringBuffer();
            for (int i = 0; i < lst.size(); i++) {
                HashMap phoneMap = (HashMap) lst.get(i);
                buffer.append("'");
                buffer.append(CommonUtil.convertObjToStr(phoneMap.get("PHONE_NUMBER")));
                mobileNobuffer.append(CommonUtil.convertObjToStr(phoneMap.get("PHONE_NUMBER")));
                buffer.append("'");
                if (lst.size() - 1 != i) {
                    buffer.append(",");
                    mobileNobuffer.append(",");

                }

            }
            System.out.println(mobileNobuffer + "buffer.toString()####" + buffer.toString() + "message####" + message);
            if (buffer != null && buffer.length() > 0) {
                String mobieBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                //message += " - " + bankSMSDescription; //KD-2442
                //message += " - " + mobieBankingSenderId;
                confDAO.sendSMS(message, mobileNobuffer.toString(),"","","","GroupCustomer");
            }
        }


    }

    private void destroyObjects() {
        objTO = null;
    }
    
    public String readFromTemplate(String smsTag) throws SAXException {
        String smsString = "";
        try {
            //String hostDir = "D:\\TTCBS\\jboss-3.2.7\\bin\\template\\" + fileName;
            //System.out.println("fileName@$#$@#"+fileName);
            //File file = new File(fileName);
            //String absoluteFilePath = file.getAbsolutePath();
            String smsTemplatePath = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("SMS_TEMPLATE_PATH"));
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(smsTemplatePath));
            doc.getDocumentElement().normalize();
            NodeList listOfMapping = doc.getElementsByTagName(smsTag);
            int totalMappings = listOfMapping.getLength();
//            System.out.println("readFromTemplate Total no of message : " + totalMappings);
            Element ModuleElement = (Element) listOfMapping.item(0);
            NodeList textMList = ModuleElement.getChildNodes();
            smsString = ((Node) textMList.item(0)).getNodeValue().trim();
//            System.out.println("readFromTemplate smsString : " + smsString);
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line "
                    + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return smsString;
    }
    
    private List sendShareBulkSMS(HashMap dataMap) throws Exception {
        List finalTableList = null;
        List returnList = null;
        finalTableList = (List) dataMap.get("SMS_ACCOUNT_LIST");
        SmsConfigDAO smsConfigDAO = new SmsConfigDAO();
        if (finalTableList != null && finalTableList.size() > 0) {
            HashMap multipletoSingleMap = new HashMap();
            for (int i = 0; i < finalTableList.size(); i++) {
                try{
                    multipletoSingleMap = (HashMap) finalTableList.get(i);
                    multipletoSingleMap.put("BRANCH_ID", dataMap.get("BRANCH_CODE"));
                    multipletoSingleMap.put("ACT_NUM", multipletoSingleMap.get("CUSTOMER_LIST"));
                    multipletoSingleMap.put("SMS_MODULE","Sharemessage");
                    System.out.println("multipletoSingleMap : " + multipletoSingleMap);
                    smsConfigDAO.ShareCustomerSendingSMS(multipletoSingleMap);
                    multipletoSingleMap.put("STATUS","Completed");
                }catch (Exception e){
                    multipletoSingleMap.put("STATUS","Error");
                    returnList.add(multipletoSingleMap);
                }
            }
        }
        System.out.println("returnList : " + returnList);
        return returnList;
    }
}
