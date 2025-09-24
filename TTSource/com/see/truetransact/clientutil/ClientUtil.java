/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ClientUtil.java
 *
 * Created on August 24, 2003, 12:30 PM
 */
package com.see.truetransact.clientutil;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import com.see.truetransact.transferobject.sysadmin.lookup.LookupMasterTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.uicomponent.*;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.ToDateValidation;
import java.util.Iterator;
import java.util.Set;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.*;

/**
 * ClientUtil is used for clientside (ie User Interface) utilities.
 *
 * @author balachandar
 */
public class ClientUtil {

    private static final CommonRB objCommonRB = new CommonRB();
    private static final Map lookupValuesMap = new HashMap();
    private Iterator processLstIterator;
    /*public static Map getLookupValuesMap() {
     return lookupValuesMap;
     }*/

    /**
     * Creates a new instance of ClientUtil
     */
    private ClientUtil() {
    }  // prevent instanciation

    // To give alert before deletion and getting the confirmation
    public static int deleteAlert() {
        return confirmationAlert(objCommonRB.getString("deleteWarning"));
    }

    // To give alert before closing and getting the confirmation
    public static int closeAlert() {
        return confirmationAlert(objCommonRB.getString("closeWarning"));
    }

    public static boolean checkTotalAmountTallied(double callingAmt, double transactionAmt) {
        boolean tally = false;
        try {
            if (callingAmt != transactionAmt) {
                // If the total amount of issue details and transaction details are not equal
                tally = false;
            } else {
                tally = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tally;
    }

    // To give alert displaying string parameter passed
    public static int confirmationAlert(String alert) {
        final String[] options = {objCommonRB.getString("cDialogYes"), objCommonRB.getString("cDialogNo")};
        final int option = COptionPane.showOptionDialog(null, alert,
                CommonConstants.WARNINGTITLE,
                COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return option;
    }

    // To give alert displaying string parameter passed
    public static int confirmationAlert(String alert, int defaultOption) {
        final String[] options = {objCommonRB.getString("cDialogYes"), objCommonRB.getString("cDialogNo")};
        final int option = COptionPane.showOptionDialog(null, alert,
                CommonConstants.WARNINGTITLE,
                COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[defaultOption]);
        return option;
    }

    public static void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /**
     * Pan Number validation
     */
    public static boolean validatePAN(com.see.truetransact.uicomponent.CTextField txtPANGIRNo) {
        boolean valid = true;
        if (txtPANGIRNo.getText() != null && txtPANGIRNo.getText() != "") {
            txtPANGIRNo.setText(txtPANGIRNo.getText().toUpperCase());
            String panNum = txtPANGIRNo.getText();

            if (panNum.length() < 10) {
                valid = false;
            }

            if (valid) {
                for (int i = 0, j = panNum.length(); i < j; i++) {
                    if (i < 5 || i == 9) {
                        if ((int) panNum.charAt(i) < 65 || (int) panNum.charAt(i) > 91) {
                            valid = false;
                        }
                    }
                    if (i >= 5 && i < 9) {
                        if (panNum.charAt(i) < 48 || panNum.charAt(i) > 57) {
                            valid = false;
                        }
                    }
                }
            }
            if (!valid) {
                txtPANGIRNo.setText("");
            }
        }
        return valid;
    }

    /**
     * Tan Number validation added By Kannan AR on 25-Apr-2017 Ref. Abi
     */
    public static boolean validateTAN(com.see.truetransact.uicomponent.CTextField txtTANNo) {
        boolean valid = true;
        if (txtTANNo.getText() != null && txtTANNo.getText() != "") {
            txtTANNo.setText(txtTANNo.getText().toUpperCase());
            String tanNum = txtTANNo.getText();

            if (tanNum.length() < 10) {
                valid = false;
            }

            if (valid) {
                for (int i = 0, j = tanNum.length(); i < j; i++) {
                    if (i < 4 || i == 9) {
                        if ((int) tanNum.charAt(i) < 65 || (int) tanNum.charAt(i) > 91) {
                            valid = false;
                        }
                    }
                    if (i >= 4 && i < 9) {
                        if (tanNum.charAt(i) < 48 || tanNum.charAt(i) > 57) {
                            valid = false;
                        }
                    }
                }
            }
            if (!valid) {
                txtTANNo.setText("");
            }
        }
        return valid;
    }

    public static boolean validateIFSCCode(com.see.truetransact.uicomponent.CTextField txtIFSCCode) { //Added By Suresh Ref By Abi 24-Dec-2015
        boolean valid = true;
        if (txtIFSCCode.getText() != null && txtIFSCCode.getText() != "") {
            txtIFSCCode.setText(txtIFSCCode.getText().toUpperCase());
            String ifscCode = txtIFSCCode.getText();

            if (ifscCode.length() < 11) {
                valid = false;
            }
            if (valid) {
                for (int i = 0, j = ifscCode.length(); i < j; i++) {
                    if (i < 4) {
                        if ((int) ifscCode.charAt(i) < 65 || (int) ifscCode.charAt(i) > 91) {
                            valid = false;
                        }
                    }
                }
            }
            if (!valid) {
                txtIFSCCode.setText("");
            }
        }
        return valid;
    }

    public static void showMessageWindow(String message) {
        try {
            COptionPane.showMessageDialog(null, message, "Note", COptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Added by Sunil 27/01/2005
    //Displays a alert window with OK button. To be used for displaying 
    //information and warnings,,,
    public static int showAlertWindow(String message) {
        int option = 1;
        try {
            String[] options = {objCommonRB.getString("cDialogOK")};
            option = COptionPane.showOptionDialog(null, message, CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return option;
    }

    /**
     * This Method is used to populate Lookup Data in the ComboBox.
     * <CODE>
     *        HashMap hash = new HashMap();
     *        hash.put("mapname",null);
     *        hash.put("paramforquery", "PERIOD");
     *
     *        HashMap keyValue = ClientUtil.populateLookupData(hash);
     *        ArrayList key = (ArrayList) keyValue.get("KEY");
     *        ArrayList value = (ArrayList) keyValue.get("VALUE");
     *        ComboBoxModel cbmChqPeriod = new ComboBoxModel(key,value);
     * </CODE>
     *
     * Needs hashMap as a parameter. mapname is the SQL Map Name in XML File.
     * paramforquery is the where condition part in the query. Returns HashMap
     * with Key and Value arrayLists.
     *
     * @param whereMap HashMap
     * @return HashMap
     * @see ClientUtil getComboBox method
     */
    public static HashMap populateLookupData(HashMap whereMap) {
        HashMap mapData = null;
        HashMap map = new HashMap();

        map.put(CommonConstants.JNDI, "LookUpJNDI");
        map.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        map.put(CommonConstants.REMOTE, "common.lookup.LookUp");
//        Calendar cal = Calendar.getInstance();
//        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL,
//            DateFormat.MEDIUM);
//        System.out.println("#$#$ Time :"+df.format(cal.getTime()));

        //System.out.println("#$#$ lookupValuesMap:"+lookupValuesMap);
        boolean canGetLookup = false;
        try {
            ArrayList lookupKeyList = null;
            Map tempMap = null;
            //System.out.println("#$#$ whereMap 1st time:"+whereMap);
            if (whereMap.containsKey(CommonConstants.PARAMFORQUERY) && whereMap.get(CommonConstants.PARAMFORQUERY) instanceof ArrayList) {
                lookupKeyList = (ArrayList) whereMap.get(CommonConstants.PARAMFORQUERY);
                //System.out.println("#$#$ lookupKeyList:"+lookupKeyList);
                tempMap = new HashMap();
                for (int i = 0; i < lookupKeyList.size(); i++) {
                    if (lookupValuesMap.containsKey(CommonUtil.convertObjToStr(lookupKeyList.get(i)))) {        //Checking already in Combo Box Key in ClientSide
                        tempMap.put(CommonUtil.convertObjToStr(lookupKeyList.get(i)), lookupValuesMap.get(lookupKeyList.get(i)));   //Storing Combo Data
                        lookupKeyList.remove(i--);                              //Removing Combo Key
                    }
                }
                /*System.out.println("#$#$ lookupKeyList:"+lookupKeyList);
                 System.out.println("#$#$ whereMap 2nd time:"+whereMap);
                 System.out.println("#$#$ tempMap:"+tempMap);*/
                if (lookupKeyList != null && lookupKeyList.size() > 0) {
                    canGetLookup = true;
                } else {
                    canGetLookup = false;
                }
            } else {
                canGetLookup = true;
            }
            if (canGetLookup) {
                mapData = ProxyFactory.createProxy().executeQuery(whereMap, map);   //Calling Dao
            }
            //System.out.println("#$#$ Time 2:"+df.format(cal.getTime()));
            //System.out.println("#$#$ mapData:"+mapData);
            if (whereMap.containsKey(CommonConstants.PARAMFORQUERY) && whereMap.get(CommonConstants.PARAMFORQUERY) instanceof ArrayList) {
                if (mapData == null) {
                    mapData = new HashMap();
                }
                mapData.putAll(tempMap);            // Copying Client Stored CoboBox Details
                if (mapData.size() > 0) {
                    Object keys[] = mapData.keySet().toArray();                 // To Avoid removing Combo Box KEY call By Reference. "mapData" should be check,  Dont Change in Future
                    if (keys != null && keys.length > 0) {
                        for (int j = 0; j < keys.length; j++) {
                            HashMap copyMap = (HashMap) mapData.get(keys[j]);
                            /*Object[] values = ((ArrayList) copyMap.get("KEY")).toArray();     //This also work correctly.
                             ArrayList copyKeyList = new ArrayList();
                             for (int k = 0; k < values.length; k++) {
                             copyKeyList.add(CommonUtil.convertObjToStr(values[k]));
                             }
                             values = ((ArrayList) copyMap.get("VALUE")).toArray();
                             ArrayList copyValueList = new ArrayList();
                             for (int k = 0; k < values.length; k++) {
                             copyValueList.add(CommonUtil.convertObjToStr(values[k]));
                             }*/
                            ArrayList copyKeyList = new ArrayList((ArrayList) copyMap.get("KEY"));       //Taking Key Details
                            ArrayList copyValueList = new ArrayList((ArrayList) copyMap.get("VALUE"));   //Taking Value Details
                            Map copiedMap = new HashMap();                      // Very Important this line, This is not a REFERENCE Map.  Dont Change this line in Future.
                            copiedMap.put("KEY", copyKeyList);
                            copiedMap.put("VALUE", copyValueList);
                            String lookupKey = CommonUtil.convertObjToStr(keys[j]);
                            lookupValuesMap.put(lookupKey, copiedMap);          //Temporary Storing Combo Box Datas in ClientSide.
                        }
                    }
                }
//                System.out.println("#$#$ mapData final:"+mapData);
//                System.out.println("#$#$ lookupValuesMap final:"+lookupValuesMap);
            }
            if (tempMap != null) {
                tempMap.clear();
            }
            tempMap = null;
        } catch (Exception exc) {
            exceptionAlert(exc);
        }
        return mapData;
    }
    
    public static void addLookupValues(ArrayList arrayLookupMasterTabTO) {
        if(arrayLookupMasterTabTO != null && arrayLookupMasterTabTO.size()>0){
            HashMap copiedMap = new HashMap();
            ArrayList copyKeyList = new ArrayList();
            ArrayList copyValueList = new ArrayList();
            for(int i = 0;i<arrayLookupMasterTabTO.size();i++){
                LookupMasterTO lookupMasterTO = (LookupMasterTO)arrayLookupMasterTabTO.get(i);
                if(lookupValuesMap.containsKey(lookupMasterTO.getLookupId())){
                    copiedMap = (HashMap) lookupValuesMap.get(lookupMasterTO.getLookupId());
                    copyKeyList = (ArrayList) copiedMap.get("KEY");
                    copyValueList = (ArrayList) copiedMap.get("VALUE");
                    copyKeyList.add(lookupMasterTO.getLookupRefId());
                    copyValueList.add(lookupMasterTO.getLookupDesc());
                    copiedMap.put("KEY", copyKeyList);
                    copiedMap.put("VALUE", copyValueList);
                    lookupValuesMap.put(lookupMasterTO.getLookupId(), copiedMap);
                }
                //System.out.println("addLookupValues final lookupValuesMap : "+lookupValuesMap);
            }
        }
    }
    
    public static void exceptionAlert(Exception objException) {
        if (objException instanceof javax.naming.CommunicationException
                || objException instanceof java.rmi.ConnectException) {
            final COptionPane objCOptionPane = new COptionPane();
            final String[] options = {CommonConstants.OK};
            objCOptionPane.showOptionDialog(null,
                    ClientConstants.COMMUNICATION_EXCEPTION + "\n" + objException.getMessage(),
                    CommonConstants.INFORMATIONTITLE,
                    COptionPane.DEFAULT_OPTION,
                    COptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
        }
        System.err.println("Exception " + objException.toString() + "Caught");
        objException.printStackTrace();
    }

    /**
     * It returns ComboBox Model which has Key & Value
     * <CODE>
     *
     *    ComboBoxModel cbmBehaves = ClientUtil.getComboBoxModel(null, "BEHAVES");
     *
     * </CODE>
     *
     * @param mapName SQL Map Name which is specified in the XML File
     * @param paramForQuery Where condition
     * @return Returns ComboBoxModel
     */
    public static ComboBoxModel getComboBoxModel(String mapName, String paramForQuery) {
        HashMap hash, keyValue;
        ArrayList key, value, param;

        hash = new HashMap();
        hash.put(CommonConstants.MAP_NAME, mapName);

        if (mapName == null) {
            param = new ArrayList();
            param.add(paramForQuery);

            hash.put(CommonConstants.PARAMFORQUERY, param);
        } else {
            hash.put(CommonConstants.PARAMFORQUERY, paramForQuery);
        }

        keyValue = populateLookupData(hash);

        if (keyValue.containsKey(CommonConstants.DATA)) {
            keyValue = (HashMap) keyValue.get(CommonConstants.DATA);
        }

        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
        return new ComboBoxModel(key, value);
    }

    /**
     * Needs HashMap parameters. Returns HashMap with 2 ArrayLists. One is for
     * TABLEHEAD and another one is for TABLEDATA.
     * <CODE>
     *        HashMap hash= executeTableQuery(whereMap);
     *
     *        ArrayList heading = (ArrayList) hash.get("TABLEHEAD");
     *        ArrayList data = (ArrayList) hash.get("TABLEDATA");
     * </CODE>
     *
     * @param whereMap HashMap
     * @return HashMap
     */
    public static HashMap executeTableQuery(HashMap whereMap) {
        HashMap mapData = null;
        HashMap map = new HashMap();
        map.put(CommonConstants.JNDI, "ViewAllJNDI");
        map.put(CommonConstants.HOME, "common.viewall.ViewAllHome");
        map.put(CommonConstants.REMOTE, "common.viewall.ViewAll");
        try {
            HashMap where = new HashMap();
            if (whereMap.containsKey(CommonConstants.MAP_WHERE)) {
                where = (HashMap) whereMap.get(CommonConstants.MAP_WHERE);
                if (!where.containsKey(CommonConstants.BRANCH_ID)) {
                    where.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                }
                if (whereMap.containsKey("screenName") && whereMap.get("screenName") != null && CommonUtil.convertObjToStr(whereMap.get("screenName")).length() > 0
                        && CommonUtil.convertObjToStr(whereMap.get("screenName")).equals("Transaction") && where.containsKey("ACT_NUM")) {
                    where.remove("BRANCH_CODE");
                    whereMap.remove("BRANCH_CODE");
                }
                // Added by nithya on 15-05-2018 for 7857 [ while locker issue, the customer search not require branch code checking
                if (where.containsKey("LOCKER_JOINT_CUST_ADD") && where.get("LOCKER_JOINT_CUST_ADD") != null && where.get("LOCKER_JOINT_CUST_ADD").equals("LOCKER_JOINT_CUST_ADD")) {                    
                    if (where.containsKey(CommonConstants.BRANCH_ID)) {
                        where.remove(CommonConstants.BRANCH_ID);
                    }
                }
                if (where.containsKey("OMIT_BRANCH_ID") && where.get("OMIT_BRANCH_ID") != null && where.get("OMIT_BRANCH_ID").equals("Y") && where.containsKey(CommonConstants.BRANCH_ID)) {                    
                    where.remove(CommonConstants.BRANCH_ID);                    
                }
                
            } else {
                where.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                if (whereMap.containsKey("screenName") && whereMap.get("screenName") != null && CommonUtil.convertObjToStr(whereMap.get("screenName")).length() > 0
                        && CommonUtil.convertObjToStr(whereMap.get("screenName")).equals("Transaction") && where.containsKey("ACT_NUM")) {
                    where.remove("BRANCH_CODE");
                    whereMap.remove("BRANCH_CODE");
                }
                whereMap.put(CommonConstants.MAP_WHERE, where);
            }
            mapData = ProxyFactory.createProxy().executeQuery(whereMap, map);
        } catch (Exception e) {
            exceptionAlert(e);
        }
//        System.out.println(mapData);
        return mapData;
    }

    /**
     * Sets TableModel for the Table based on the HashMap parameter.
     * <CODE>
     *        HashMap map = new HashMap();
     *        map.put("MAPNAME", "getSelectOperativeAcctProductTOList");
     *        ClientUtil.setTableModel(map, tblData);
     * </CODE>
     *
     * @param whereMap HashMap
     * @param tblData CTable
     */
    public static boolean setTableModel(HashMap whereMap, CTable tblData) {
        return setTableModel(whereMap, tblData, true);
    }

    /**
     * Sets TableModel for the Table based on the HashMap parameter.
     * <CODE>
     *        HashMap map = new HashMap();
     *        map.put("MAPNAME", "getSelectOperativeAcctProductTOList");
     *        ClientUtil.setTableModel(map, tblData);
     * </CODE>
     *
     * @param whereMap HashMap
     * @param tblData CTable
     */
    public static boolean setTableModel(HashMap whereMap, CTable tblData, boolean checkData) {
        ArrayList heading = null;
        ArrayList data = new ArrayList();
        boolean dataExist;
        if (!whereMap.containsKey("KEY")) {
            HashMap hash = executeTableQuery(whereMap);
            heading = (ArrayList) hash.get(CommonConstants.TABLEHEAD);
            data = (ArrayList) hash.get(CommonConstants.TABLEDATA);
        }
        //Added By Suresh  31-Oct-2017
        if (whereMap.containsKey("KEY") && whereMap.containsKey(CommonConstants.MAP_WHERE)) {
            if (whereMap.get("KEY").equals("LOAN")) {       //Added By Suresh
                HashMap dataMap = (HashMap) whereMap.get(CommonConstants.MAP_WHERE);
                if (dataMap != null && dataMap.size() > 0) {
                    String act_Num = "";
                    HashMap singleMap = new HashMap();
                    ArrayList dataArray;
                    Iterator processLstIterator;
                    processLstIterator = dataMap.keySet().iterator();
                    for (int i = 0; i < dataMap.size(); i++) {
                        act_Num = (String) processLstIterator.next();
                        singleMap = (HashMap) dataMap.get(act_Num);
                        dataArray = new ArrayList();
                        dataArray.add(act_Num);
                        dataArray.add(CommonUtil.convertObjToDouble(singleMap.get("PRINCEPLE")));
                        dataArray.add(CommonUtil.convertObjToDouble(singleMap.get("INTEREST")));
                        data.add(i, dataArray);
                    }
                }
                heading = new ArrayList();
                heading.add("ACCOUNT_NO");
                heading.add("PRINCIPLE");
                heading.add("INTEREST");
            }else if (whereMap.get("KEY").equals("RD")) {     //Added By Suresh   22-Sep-2015
                HashMap dataMap = (HashMap) whereMap.get(CommonConstants.MAP_WHERE);
                if (dataMap != null && dataMap.size() > 0) {
                    String act_Num = "";
                    HashMap singleMap = new HashMap();
                    ArrayList dataArray;
                    Iterator processLstIterator;
                    processLstIterator = dataMap.keySet().iterator();
                    for (int i = 0; i < dataMap.size(); i++) {
                        act_Num = (String) processLstIterator.next();
                        singleMap = (HashMap) dataMap.get(act_Num);
                        dataArray = new ArrayList();
                        dataArray.add(act_Num);
                        dataArray.add(singleMap.get("AMOUNT"));
                        data.add(i, dataArray);
                    }
                }
                heading = new ArrayList();
                heading.add("DEPOSIT_NUMBER");
                heading.add("AMOUNT");
            } else if (whereMap.get("KEY").equals("UTR_NUMBER")) {     //Added By Suresh  07-Dec-2016
                HashMap dataMap = (HashMap) whereMap.get(CommonConstants.MAP_WHERE);
                if (dataMap != null && dataMap.size() > 0) {
                    String utrNum = "";
                    HashMap singleMap = new HashMap();
                    ArrayList dataArray;
                    Iterator processLstIterator;
                    processLstIterator = dataMap.keySet().iterator();
                    for (int i = 0; i < dataMap.size(); i++) {
                        utrNum = (String) processLstIterator.next();
                        singleMap = (HashMap) dataMap.get(utrNum);
                        dataArray = new ArrayList();
                        dataArray.add(singleMap.get("RTGS_ID"));
                        dataArray.add(utrNum);
                        data.add(i, dataArray);
                    }
                }
                heading = new ArrayList();
                heading.add("RTGS_ID");
                heading.add("UTR_NUMBER");
            } else if (whereMap.get("KEY").equals("TRADING SALES")) { //Added By Revathi 15-sep-2015
                HashMap dataMap = (HashMap) whereMap.get(CommonConstants.MAP_WHERE);
                if (dataMap != null && dataMap.size() > 0) {
                    String ivalue = "";
                    HashMap singleMap = new HashMap();
                    ArrayList dataArray;
                    Iterator processLstIterator;
                    processLstIterator = dataMap.keySet().iterator();
                    for (int i = 0; i < dataMap.size(); i++) {
                        ivalue = (String) processLstIterator.next();
                        singleMap = (HashMap) dataMap.get(ivalue);
                        dataArray = new ArrayList();
                        dataArray.add(CommonUtil.convertObjToStr(singleMap.get("ACT_NUM")));
                        dataArray.add(CommonUtil.convertObjToStr(singleMap.get("TRANS_DT")));
                        dataArray.add(CommonUtil.convertObjToStr(singleMap.get("PARTICULARS")));
                        dataArray.add(CommonUtil.convertObjToStr(singleMap.get("INST_DT")));
                        dataArray.add(CommonUtil.convertObjToDouble(singleMap.get("DEBIT")));
                        dataArray.add(CommonUtil.convertObjToDouble(singleMap.get("CREDIT")));
                        dataArray.add(CommonUtil.convertObjToDouble(singleMap.get("BALANCE")));
                        data.add(i, dataArray);
                    }
                }
                Collections.reverse(data);
                heading = new ArrayList();
                heading.add("ACT_NUM");
                heading.add("TRANS_DT");
                heading.add("PARTICULARS");
                heading.add("INST_DT");
                heading.add("DEBIT");
                heading.add("CREDIT");
                heading.add("BALANCE");
            } else if (whereMap.get("KEY").equals("AADHAR_MAP")) {     //Added By Suresh  03-Jul-2017
                HashMap dataMap = (HashMap) whereMap.get(CommonConstants.MAP_WHERE);
                if (dataMap != null && dataMap.size() > 0) {
                    String ivalue = "";
                    HashMap singleMap = new HashMap();
                    ArrayList dataArray;
                    Iterator processLstIterator;
                    processLstIterator = dataMap.keySet().iterator();
                    for (int i = 0; i < dataMap.size(); i++) {
                        ivalue = (String) processLstIterator.next();
                        singleMap = (HashMap) dataMap.get(ivalue);
                        dataArray = new ArrayList();
                        dataArray.add(CommonUtil.convertObjToStr(singleMap.get("AADHAR_NO")));
                        data.add(i, dataArray);
                    }
                }
                heading = new ArrayList();
                heading.add("AADHAR_NO");
            }
        } else if (whereMap.containsKey(CommonConstants.MAP_NAME)) {
            if (CommonUtil.convertObjToStr(whereMap.get(CommonConstants.MAP_NAME)).equals("getIssuedChequeDetails")) {
                if (whereMap.containsKey(CommonConstants.MAP_WHERE)) {
                    HashMap where = (HashMap) whereMap.get(CommonConstants.MAP_WHERE);
                    List stopPayList = executeQuery("getStopPaymentList", where);
                    long fromNo = CommonUtil.convertObjToLong(where.get("FROM_CHQ_NO2"));
                    long toNo = CommonUtil.convertObjToLong(where.get("TO_CHQ_NO2"));
                    Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(where.get("TRANS_DT")));
                    int i = 0;
                    ArrayList dataArray;
                    for (; fromNo <= toNo; fromNo++, i++) {
                        String chequeNo = CommonUtil.convertObjToStr(where.get("FROM_CHQ_NO1")) + " " + fromNo;
                        if (!chqNoExistsOrNot(chequeNo, data)) {
                            dataArray = new ArrayList();
                            dataArray.add(chequeNo);
                            String status = chqStopPayExistsOrNot(fromNo, stopPayList);
                            dataArray.add(status);
                            dataArray.add(transDt);
                            data.add(i, dataArray);
                        }
                    }
                    if (heading == null) {
                        heading = new ArrayList();
                        heading.add("Cheque No.");
                        heading.add("Status");
                        heading.add("Trans Dt");
                    }
                    //System.out.println("#%#&#* Data : "+data);
                }
            }
        }

        if (heading != null) {
            dataExist = true;
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(heading);
            tableModel.setData(data);
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblData.setAutoResizeMode(0);
            tblData.doLayout();
            tblData.setModel(tableSorter);
            tblData.revalidate();

        } else {
            dataExist = false;

            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblData);
            TableModel tableModel = new TableModel();
            tableModel.setHeading(new ArrayList());
            tableModel.setData(new ArrayList());
            tableModel.fireTableDataChanged();
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();

            tblData.setModel(tableSorter);
            tblData.revalidate();

            if (checkData == true) {
                noDataAlert();
            }
        }
        return dataExist;
    }

    private static boolean chqNoExistsOrNot(String chqNo, ArrayList chqList) {
        String tempNo;
        if (chqList.size() > 0) {
            for (int i = 0; i < chqList.size(); i++) {
                tempNo = CommonUtil.convertObjToStr(((ArrayList) chqList.get(i)).get(0));
                if (tempNo.equals(chqNo)) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    private static String chqStopPayExistsOrNot(long chqNo, List chqStopList) {
        long fromNo = 0;
        long toNo = 0;
        HashMap tempHash = new HashMap();
        String returnStr = "";
        if (chqStopList != null) {
            if (chqStopList.size() > 0) {
                for (int k = 0; k < chqStopList.size(); k++) {
                    tempHash = (HashMap) chqStopList.get(k);
                    fromNo = CommonUtil.convertObjToLong(tempHash.get("START_CHQ_NO2"));
                    toNo = CommonUtil.convertObjToLong(tempHash.get("END_CHQ_NO2"));
                    if (toNo > 0) {
                        if (chqNo >= fromNo && chqNo <= toNo) {
                            returnStr = CommonUtil.convertObjToStr(tempHash.get("STOP_STATUS"));
                        }
                    } else {
                        if (chqNo == fromNo) {
                            returnStr = CommonUtil.convertObjToStr(tempHash.get("STOP_STATUS"));
                        }
                    }
                }
            }
        }
        if (returnStr.length() == 0) {
            returnStr = "UNUSED";
        }
        tempHash = null;
        return returnStr;
    }

    public static void noDataAlert() {
        final COptionPane objCOptionPane = new COptionPane();
        final String[] options = {CommonConstants.OK};
        Dummy cons = new Dummy();
        objCOptionPane.showOptionDialog(null, ClientConstants.NO_DATA_INFO, CommonConstants.INFORMATIONTITLE,
                COptionPane.DEFAULT_OPTION, COptionPane.PLAIN_MESSAGE,
                new javax.swing.ImageIcon(cons.getClass().getResource("/com/see/truetransact/ui/images/exclamation.gif")), options, options[0]);
    }

    /**
     * This Method is used for retrieving data as a List from the database using
     * SQLMap parameters.
     *
     * @param mapID SQL Map ID
     * @param whereMap Where condition parameter
     * @return Returns List
     */
    public static List executeQuery(String mapID, HashMap whereMap) {
        HashMap selectMap = new HashMap();
        selectMap.put(CommonConstants.MAP_NAME, mapID);

        if (whereMap != null && whereMap.containsKey(CommonConstants.MAP_WHERE)) {
            selectMap.put(CommonConstants.MAP_WHERE, whereMap.get(CommonConstants.MAP_WHERE));
        } else {
            selectMap.put(CommonConstants.MAP_WHERE, whereMap);
        }

        List lstData = null;
        HashMap map = new HashMap();
        map.put(CommonConstants.JNDI, "SelectAllJNDI");
        map.put(CommonConstants.HOME, "common.viewall.SelectAllHome");
        map.put(CommonConstants.REMOTE, "common.viewall.SelectAll");
        try {
            lstData = (List) ProxyFactory.createProxy().executeQuery(selectMap, map).get(CommonConstants.DATA);
        } catch (Exception e) {
            exceptionAlert(e);
        }
        return lstData;
    }

    /**
     * This Method is used for retrieving data as a List from the database using
     * SQLMap parameters.
     *
     * @param mapID SQL Map ID
     * @param whereMap Where condition parameter
     * @return Returns List
     */
    public static void execute(String mapID, HashMap whereMap) {
        HashMap selectMap = new HashMap();
//        boolean isExecuted = true;
        selectMap.put(CommonConstants.MAP_NAME, mapID);
        selectMap.put(CommonConstants.MAP_WHERE, whereMap);

        HashMap map = new HashMap();
        map.put(CommonConstants.JNDI, "SelectAllJNDI");
        map.put(CommonConstants.HOME, "common.viewall.SelectAllHome");
        map.put(CommonConstants.REMOTE, "common.viewall.SelectAll");
        try {
            HashMap proxyResultMap = ProxyFactory.createProxy().execute(selectMap, map);
        } catch (Exception e) {
//            isExecuted  = false;
            exceptionAlert(e);

        }
//        return isExecuted;
    }

    public static void executeWithExceptionHand(String mapID, HashMap whereMap) throws SQLException {
        HashMap selectMap = new HashMap();
//        boolean isExecuted = true;
        selectMap.put(CommonConstants.MAP_NAME, mapID);
        selectMap.put(CommonConstants.MAP_WHERE, whereMap);

        HashMap map = new HashMap();
        map.put(CommonConstants.JNDI, "SelectAllJNDI");
        map.put(CommonConstants.HOME, "common.viewall.SelectAllHome");
        map.put(CommonConstants.REMOTE, "common.viewall.SelectAll");
        try {
            HashMap proxyResultMap = ProxyFactory.createProxy().execute(selectMap, map);
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
//            isExecuted  = false;
            exceptionAlert(e);

        }
//        return isExecuted;
    }

    /**
     * This Method is used for retrieving data as a List from the database using
     * SQLMap parameters.
     *
     * @param mapID SQL Map ID
     * @param whereMap Where condition parameter
     * @return Returns List
     */
    public static boolean executeWithResult(String mapID, HashMap whereMap) {
        HashMap selectMap = new HashMap();
        boolean isExecuted = true;
        selectMap.put(CommonConstants.MAP_NAME, mapID);
        selectMap.put(CommonConstants.MAP_WHERE, whereMap);

        HashMap map = new HashMap();
        map.put(CommonConstants.JNDI, "SelectAllJNDI");
        map.put(CommonConstants.HOME, "common.viewall.SelectAllHome");
        map.put(CommonConstants.REMOTE, "common.viewall.SelectAll");
        try {
            ProxyFactory.createProxy().execute(selectMap, map);
        } catch (Exception e) {
            isExecuted = false;
            exceptionAlert(e);
        }
        return isExecuted;
    }

    /**
     * Clear all the JTextField, JTextArea and JComboBox data
     *
     * @param comp java.awt.Container
     */
    public static void clearAll(java.awt.Container comp) {
        java.awt.Component[] children = comp.getComponents();
        for (int i = 0; i < children.length; i++) {
            try {
                if ((children[i] != null)) {
                    if (children[i] instanceof javax.swing.JTextField) {
                        ((javax.swing.JTextField) children[i]).setText("");
                        /*} else if (children[i] instanceof javax.swing.JLabel &&
                         children[i].getName().indexOf("StatusMsg2") > -1) {
                         ((javax.swing.JLabel) children[i]).setText("");*/
                    } else if (children[i] instanceof javax.swing.JTextArea) {
                        ((javax.swing.JTextArea) children[i]).setText("");
                    } else if (children[i] instanceof javax.swing.JCheckBox) {
                        ((javax.swing.JCheckBox) children[i]).setSelected(false);
                    } else if (children[i] instanceof javax.swing.JRadioButton) {
                        ((javax.swing.JRadioButton) children[i]).setSelected(false);
                    } else if (children[i] instanceof javax.swing.JComboBox) {
                        ((javax.swing.JComboBox) children[i]).setSelectedIndex(
                                ((javax.swing.JComboBox) children[i]).getItemCount() == 0 ? -1 : 0);
                    } else if (children[i] instanceof javax.swing.JTable) {
                        /* If component is a table */
                        ((javax.swing.JTable) children[i]).clearSelection();

                        javax.swing.table.TableModel tm = ((javax.swing.JTable) children[i]).getModel();
                        if (tm instanceof TableSorter) {
                            ((TableSorter) tm).getModel().setData(new Object[0][0]);
                            ((TableSorter) tm).reallocateIndexes();
                            ((TableSorter) tm).getModel().fireTableDataChanged();
                        } else if (tm instanceof TableModel) {
                            ((TableModel) tm).setData(new Object[0][0]);
                            ((TableModel) tm).fireTableDataChanged();
                        } else if (tm instanceof javax.swing.table.DefaultTableModel) {
                            for (int j = tm.getRowCount(); j > -1; j--) {
                                ((javax.swing.table.DefaultTableModel) tm).removeRow(j);
                            }
                        }
                    } else {
                        clearAll((java.awt.Container) children[i]);
                    }
                }
            } catch (Exception exc) {
            }
        }
        return;
    }

    public static void disableAll(java.awt.Container comp, boolean yesno) {
        enableDisable(comp, yesno, false, !yesno);
    }

    public static void enableDisable(java.awt.Container comp, boolean yesno) {
        enableDisable(comp, yesno, false, false);
    }

    public static void enableDisable(java.awt.Container comp, boolean yesno, boolean viewMode) {
        enableDisable(comp, yesno, viewMode, false);
    }

    /**
     * Enables & Disables the Fields
     *
     * @param comp Passing container as a parameter
     * @param yesno Boolean parameter for enabling or disabling a component
     * @param viewMode This is used for View Mode.. If the View Mode is true
     * then save button will be disabled
     * @param disableAll if it is true all the buttons also will be disabled.
     */
    /*    public static void enableDisable(java.awt.Container comp, boolean yesno, boolean viewMode, boolean disableAll) {
     java.awt.Component[] children = comp.getComponents();
     javax.swing.JTextField txtDefault = new javax.swing.JTextField();
     javax.swing.JPanel panelDefault = new javax.swing.JPanel();
        
     CInternalFrame cInt = null;
     if (!disableAll) {
     if (comp instanceof CInternalFrame) {
     cInt = (CInternalFrame) comp;
     if (cInt.getMode() == ClientConstants.ACTIONTYPE_VIEW || cInt.getMode() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
     yesno = false;
     viewMode = true;
     } else if (cInt.getMode() == ClientConstants.ACTIONTYPE_CANCEL){
     viewMode = false;
     }
     }
     }
        
     for (int i = 0; i < children.length; i++) {
     if ((children[i] != null)) {
     if (children[i] instanceof javax.swing.JTextField) {
     if (((javax.swing.JTextField) children[i]).isEditable()) {
     if (yesno==true) {
     children[i].setBackground(txtDefault.getBackground());
     } else {
     children[i].setBackground(panelDefault.getBackground());
     }
     ((javax.swing.JTextField) children[i]).setEnabled(yesno);
     }
     } else if (children[i] instanceof CButton) {
     CButton btn = (CButton) children[i];
                    
     if (disableAll) {
     btn.setEnabled(yesno);
     } else {
     String btnName = btn.getName();
     if (btnName != null) {
     if (viewMode == true) {
     btn.setMode(ClientConstants.ACTIONTYPE_VIEW);
     }else{
     if (yesno == false){
     btn.setMode(ClientConstants.ACTIONTYPE_CANCEL);
     }else{
     btn.setMode(ClientConstants.ACTIONTYPE_EDIT);
     }
     }
                            
     if (btnName.equals("btnNew") || btnName.equals("btnEdit") || btnName.equals("btnDelete")) {
     if (btn.getMode() == ClientConstants.ACTIONTYPE_EDIT || btn.getMode() == ClientConstants.ACTIONTYPE_VIEW) {
     btn.setEnabled(false);
     }else{
     btn.setEnabled(true);
     }
     } else if (btnName.equals("btnAuthorize") || btnName.equals("btnReject") || btnName.equals("btnException")) {
     if (btn.getMode() == ClientConstants.ACTIONTYPE_EDIT) {
     btn.setMode(ClientConstants.ACTIONTYPE_VIEW);
     btn.setEnabled(false);
     } else if (btn.getMode() == ClientConstants.ACTIONTYPE_VIEW) {
     btn.setMode(ClientConstants.ACTIONTYPE_EDIT);
     btn.setEnabled(true);
     } else if (btn.getMode() == ClientConstants.ACTIONTYPE_CANCEL) {
     btn.setEnabled(true);
     }
     } else if (btnName.equals("btnSave")){
     if (btn.getMode() == ClientConstants.ACTIONTYPE_EDIT) {
     btn.setEnabled(true);
     } else if (btn.getMode() == ClientConstants.ACTIONTYPE_VIEW || btn.getMode() == ClientConstants.ACTIONTYPE_CANCEL) {
     btn.setEnabled(false);
     }
     }else if (btnName.equals("btnCancel")){
     if (btn.getMode() == ClientConstants.ACTIONTYPE_CANCEL) {
     btn.setEnabled(false);
     }else if (btn.getMode() == ClientConstants.ACTIONTYPE_VIEW){
     btn.setMode(ClientConstants.ACTIONTYPE_EDIT);
     btn.setEnabled(true);
     }else{
     btn.setEnabled(true);
     }
     }
     }
     }
     } else if (children[i] instanceof javax.swing.JTextArea) {
     if (((javax.swing.JTextArea) children[i]).isEditable()) {
     if (yesno==true) {
     children[i].setBackground(txtDefault.getBackground());
     } else {
     children[i].setBackground(panelDefault.getBackground());
     }
     ((javax.swing.JTextArea) children[i]).setEnabled(yesno);
     }
     } else if (children[i] instanceof javax.swing.JComboBox){
     ((javax.swing.JComboBox)children[i]).setEnabled(yesno);
     } else if (children[i] instanceof javax.swing.JCheckBox){
     ((javax.swing.JCheckBox)children[i]).setEnabled(yesno);
     } else if (children[i] instanceof javax.swing.JRadioButton){
     ((javax.swing.JRadioButton)children[i]).setEnabled(yesno);
     } else if (children[i] instanceof javax.swing.JList){
     ((javax.swing.JList)children[i]).setEnabled(yesno);
     } else if (children[i] instanceof com.see.truetransact.uicomponent.CDateField){
     ((com.see.truetransact.uicomponent.CDateField)children[i]).setEnabled(yesno);
     }else {
     enableDisable((java.awt.Container) children[i], yesno, viewMode, disableAll);
     }
     }
     }
     return;
     }
     */
    public static void enableDisable(java.awt.Container comp, boolean yesno, boolean viewMode, boolean disableAll) {
        java.awt.Component[] children = comp.getComponents();
        javax.swing.JTextField txtDefault = new javax.swing.JTextField();
        javax.swing.JPanel panelDefault = new javax.swing.JPanel();

        CInternalFrame cInt = null;
        if (!disableAll) {
            if (comp instanceof CInternalFrame) {
                cInt = (CInternalFrame) comp;
                if (cInt.getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE) {
                    yesno = false;
                    viewMode = true;
                }
            }
        }

        for (int i = 0; i < children.length; i++) {
            if ((children[i] != null)) {
                if (children[i] instanceof javax.swing.JTextField) {
//                    if (((javax.swing.JTextField) children[i]).isEditable()) {
//                        if (yesno==true) {
//                            children[i].setBackground(txtDefault.getBackground());
//                        } else {
//                            children[i].setBackground(panelDefault.getBackground());
//                        }
                    ((javax.swing.JTextField) children[i]).setEditable(yesno);
                    ((javax.swing.JTextField) children[i]).setEnabled(yesno);
                    if (!yesno) {
//                            ((javax.swing.JTextField) children[i]).setDisabledTextColor(new java.awt.Color(51,51,51));
                        children[i].setBackground(new java.awt.Color(220, 220, 220));
                    } else {
                        children[i].setBackground(txtDefault.getBackground());
                    }
//                    }
                } else if (children[i] instanceof CButton) {
                    CButton btn = (CButton) children[i];

                    if (disableAll) {
                        btn.setEnabled(yesno);
                    } else {
                        String btnName = btn.getName();
                        if (btnName != null) {
                            //btn.setMode(ClientConstants.ACTIONTYPE_EDIT);

                            if (btnName.equals("btnSave") || btnName.equals("btnDelete")) {
                                if (viewMode == true) {
                                    btn.setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                                    btn.setEnabled(false);
                                } else {    // else part added to remove the mode -> ClientConstants.ACTIONTYPE_VIEW
                                    btn.setMode(ClientConstants.ACTIONTYPE_CANCEL);
//                                    btn.setEnabled(yesno);
                                }
                            } else if (btnName.equals("btnAuthorize") || btnName.equals("btnReject") || btnName.equals("btnException")) {
//                                System.out.println("btnName : " + btnName);
//                                System.out.println("getMode : " + btn.getMode() + " : " + ClientConstants.ACTIONTYPE_EDIT);

                                if (btn.getMode() == ClientConstants.ACTIONTYPE_EDIT) {
//                                    System.out.println("getMode .... : " + btn.getMode());
                                    btn.setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                                    btn.setEnabled(false);
                                } else if (btn.getMode() == ClientConstants.ACTIONTYPE_VIEW_MODE) {
                                    btn.setMode(ClientConstants.ACTIONTYPE_EDIT);
                                    btn.setEnabled(true);
                                }
                            }
                        }
                    }
                } else if (children[i] instanceof javax.swing.JTextArea) {
//                    if (((javax.swing.JTextArea) children[i]).isEditable()) {
//                        if (yesno==true) {
//                            children[i].setBackground(txtDefault.getBackground());
//                        } else {
//                            children[i].setBackground(panelDefault.getBackground());
//                        }
                    ((javax.swing.JTextArea) children[i]).setEnabled(yesno);
//                    }
                } else if (children[i] instanceof javax.swing.JComboBox) {
                    ((javax.swing.JComboBox) children[i]).setEnabled(yesno);
                } else if (children[i] instanceof javax.swing.JCheckBox) {
                    ((javax.swing.JCheckBox) children[i]).setEnabled(yesno);
                } else if (children[i] instanceof javax.swing.JRadioButton) {
                    ((javax.swing.JRadioButton) children[i]).setEnabled(yesno);
                } else if (children[i] instanceof javax.swing.JList) {
                    ((javax.swing.JList) children[i]).setEnabled(yesno);
                } else if (children[i] instanceof com.see.truetransact.uicomponent.CDateField) {
                    ((com.see.truetransact.uicomponent.CDateField) children[i]).setEnabled(yesno);
                } else {
                    enableDisable((java.awt.Container) children[i], yesno, viewMode, disableAll);
                }
            }
        }
        return;
    }

    public static void showDialog(CDialog dialog) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Dimension frameSize = dialog.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        dialog.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2 - 60);

        dialog.setModal(true);
        dialog.show();
    }

    public static HashMap getScreenConfigList(String screenName) {
        HashMap whereMap = new HashMap();
        whereMap.put("SCREEN", screenName);
        whereMap.put("GROUP", TrueTransactMain.GROUP_ID);
        whereMap.put("ROLE", TrueTransactMain.ROLE_ID);

        HashMap resultMap = new HashMap();
        HashMap valueMap = null;
        HashMap tmpMap = null;

        List lstFields = executeQuery("getScreenConfig", whereMap);
        //System.out.println(lstFields);
        for (int i = 0, j = lstFields.size(); i < j; i++) {
            tmpMap = (HashMap) lstFields.get(i);
            if (resultMap.containsKey(tmpMap.get("FIELD_NAME"))) {
                valueMap = (HashMap) resultMap.get(tmpMap.get("FIELD_NAME"));
                valueMap.put(tmpMap.get("PROPERTY_ID"), tmpMap.get("PROPERTY_VALUE"));
                resultMap.put(tmpMap.get("FIELD_NAME"), valueMap);
            } else {
                valueMap = new HashMap();
                valueMap.put(tmpMap.get("PROPERTY_ID"), tmpMap.get("PROPERTY_VALUE"));
                resultMap.put(tmpMap.get("FIELD_NAME"), valueMap);
            }
        }
        return resultMap;
    }

    /**
     * Validate for Minor and Minor Under Ward Status ie., for more than 18
     * years and 21 years
     *
     * @param tdtObj is the CDateField for which the validation is to be done
     * @param minorOrMinorUnderWards is the String whose value is "18" or "21"
     * against which we have to validate the CDateField
     */
    public static void validateMinorAndMinor_Under_Wards(CDateField tdtObj, String minorOrMinorUnderWards) {
        if (tdtObj.getDateValue() != null && !tdtObj.getDateValue().equals("")) {
            ToDateValidation minorDateValidation = new ToDateValidation(getCurrentDate(), true, minorOrMinorUnderWards);
            minorDateValidation.setComponent(tdtObj);
            if (!minorDateValidation.validate()) {
                minorDateValidation.setErrorMessage("Minor age should be more than 18 years");
                tdtObj.setDateValue("");
            }
        }
    }

    // Validate for Current Date or Lesser Than Current Date
    public static void validateLTDate(CDateField tdtObj) {
        if (tdtObj.getDateValue() != null && !tdtObj.getDateValue().equals("")) {
            ToDateValidation toDate = new ToDateValidation(getCurrentDate(), true);
            toDate.setComponent(tdtObj);
            String compName = tdtObj.getName();
            compName = compName.substring(3);
            if (!toDate.validate()) {
                String[] options = {"Ok"};
                toDate.setErrorMessage(compName + "      Should be Lesser than or equal to current date");
//                toDate.setErrorMessage(" Entered Date should be Lesser than Current date");
                int option = COptionPane.showOptionDialog(null, toDate.getErrorMessage(), CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                tdtObj.setDateValue("");
            }
        }
    }
    // Validate for wheather entered date is should be less then or equal to strmmddyy date

    public static void validateLessDate(CDateField tdtObj, String strDDMMYY) {
        if (tdtObj.getDateValue() != null && !tdtObj.getDateValue().equals("")) {
            ToDateValidation toDate = new ToDateValidation(DateUtil.getDateMMDDYYYY(strDDMMYY), true);
            toDate.setComponent(tdtObj);
            String compName = tdtObj.getName();
            compName = compName.substring(3);
            if (!toDate.validate()) {
                String[] options = {"Ok"};
                toDate.setErrorMessage(compName + "    Should be Lesser than or equal to :" + strDDMMYY);
//                toDate.setErrorMessage(" Entered Date should be Lesser than Current date");
                int option = COptionPane.showOptionDialog(null, toDate.getErrorMessage(), CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                tdtObj.setDateValue("");
            }
        }
    }

    // Validate for toDate is greater than from date.
    public static void validateToDate(CDateField tdtObj, String strMMDDYYY) {
        if (tdtObj.getDateValue() != null && !tdtObj.getDateValue().equals("") && strMMDDYYY != null && !strMMDDYYY.equals("")) {
            ToDateValidation toDate = new ToDateValidation(DateUtil.getDateMMDDYYYY(strMMDDYYY));
            toDate.setComponent(tdtObj);
            String compName = tdtObj.getName();
            compName = compName.substring(3);

            if (!toDate.validate()) {
                String[] options = {"Ok"};
                toDate.setErrorMessage(compName + "  Should be greater than or equal to :" + strMMDDYYY);
//                toDate.setErrorMessage("Entered Date should be greater than Current Date");
                int option = COptionPane.showOptionDialog(null, toDate.getErrorMessage(), CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                tdtObj.setDateValue("");
            }
        }
    }

    // Validate for toDate is greater than from date.
    public static void validateToDate(CDateField tdtObj, String strMMDDYYY, boolean fromDtDisplay) {
        if (tdtObj.getDateValue() != null && !tdtObj.getDateValue().equals("") && strMMDDYYY != null && !strMMDDYYY.equals("")) {
            ToDateValidation toDate = new ToDateValidation(DateUtil.getDateMMDDYYYY(strMMDDYYY));
            String compName = tdtObj.getName();
            compName = compName.substring(3);
            toDate.setComponent(tdtObj);
            if (!toDate.validate()) {
                String[] options = {"Ok"};
                toDate.setErrorMessage(compName + "  Should be greater than or equal to:" + strMMDDYYY);
                int option = COptionPane.showOptionDialog(null, toDate.getErrorMessage(), CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                tdtObj.setDateValue("");
            }
        }
    }

    // Validate for toDate is greater than from date.
    public static void validateFromDate(CDateField tdtObj, String strMMDDYYY) {
        if (tdtObj.getDateValue() != null && !tdtObj.getDateValue().equals("") && strMMDDYYY != null && !strMMDDYYY.equals("")) {
            ToDateValidation toDate = new ToDateValidation(DateUtil.getDateMMDDYYYY(strMMDDYYY), true);
            toDate.setComponent(tdtObj);
            String compName = tdtObj.getName();
            compName = compName.substring(3);

            if (!toDate.validate()) {
                String[] options = {"Ok"};
//                toDate.setErrorMessage("Entered Date should be greater than current date");
                toDate.setErrorMessage(compName + "  Should be greater than or equal to:" + strMMDDYYY);
                int option = COptionPane.showOptionDialog(null, toDate.getErrorMessage(), CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                tdtObj.setDateValue("");
            }
        }
    }

    // Validate for toDate is greater than from date.
    public static void validateFromDate(CDateField tdtObj, String strMMDDYYY, boolean flag) {
        if (tdtObj.getDateValue() != null && !tdtObj.getDateValue().equals("") && strMMDDYYY != null && !strMMDDYYY.equals("")) {
            ToDateValidation toDate = new ToDateValidation(DateUtil.getDateMMDDYYYY(strMMDDYYY), true);
            toDate.setComponent(tdtObj);
            String compName = tdtObj.getName();
            compName = compName.substring(3);
            if (!toDate.validate()) {
                String[] options = {"Ok"};
                toDate.setErrorMessage(compName + " Should be greater than or equal to :" + strMMDDYYY);
                int option = COptionPane.showOptionDialog(null, toDate.getErrorMessage(), CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                tdtObj.setDateValue("");
            }
        }
    }

    public static String getMainCurrency() {
        String baseCurrency = "";
        List lst = executeQuery("getBaseCurrency", null);
        if (lst != null && lst.size() >= 0) {
            baseCurrency = (String) ((HashMap) lst.get(0)).get("BASE_CURRENCY");
        }
        return baseCurrency;
    }

    public static double convertCurrency(String source, String target, String transType, double amount) {
        double returnAmount = 0.0;
        String baseCurrency = getMainCurrency();
        //System.out.println(baseCurrency);

        HashMap sourceParam;
        HashMap targetParam;

        // Normal Transaction
        if (baseCurrency.equalsIgnoreCase(source) && baseCurrency.equalsIgnoreCase(target)) {
            returnAmount = amount;
            // Target is different
        } else {
            double sourceMiddle = 1;
            double targetMiddle = 1;
            if (baseCurrency.equalsIgnoreCase(source) && !baseCurrency.equalsIgnoreCase(target)) {
                targetMiddle = getMiddleRate(target);

                returnAmount = convertToOther(amount, targetMiddle);

                targetParam = getExchangeRateParam(target, "OTHERS");
                returnAmount = getExchangeRate(targetParam, returnAmount, transType);

            } else if (!baseCurrency.equalsIgnoreCase(source) && baseCurrency.equalsIgnoreCase(target)) {
                sourceMiddle = getMiddleRate(source);
                returnAmount = convertToBase(amount, sourceMiddle);

                sourceParam = getExchangeRateParam(source, "OTHERS");
                returnAmount = getExchangeRate(sourceParam, returnAmount, transType);
            } else if (!baseCurrency.equalsIgnoreCase(source) && !baseCurrency.equalsIgnoreCase(target)) {
                if (source.equalsIgnoreCase(target)) {
                    returnAmount = amount;
                } else {
                    sourceMiddle = getMiddleRate(source);
                    targetMiddle = getMiddleRate(target);

                    sourceParam = getExchangeRateParam(source, "OTHERS");
                    double sourceA = convertToBase(amount, sourceMiddle);
                    sourceA = getExchangeRate(sourceParam, sourceA, transType);

                    targetParam = getExchangeRateParam(target, "OTHERS");
                    double targetA = convertToOther(amount, targetMiddle);
                    targetA = getExchangeRate(targetParam, targetA, transType);

                    returnAmount = convertToOther(convertToBase(amount, sourceA), targetA);
                }
            }
        }

        java.text.DecimalFormat objDecimalFormat = new java.text.DecimalFormat();
        objDecimalFormat.applyPattern("################.##");
        objDecimalFormat.setDecimalSeparatorAlwaysShown(false);
        String returnStr = objDecimalFormat.format(new Double(returnAmount));

        returnAmount = new Double(returnStr).doubleValue();
        //System.out.println("returnAmount : " + returnAmount);

        return returnAmount;
    }

    private static double getExchangeRate(HashMap data, double currAmount, String transType) {
        double percent = 0, doubleExRate = 0;
        if (transType.equals("CREDIT")) {
            percent = ((java.math.BigDecimal) data.get("SELLING_PRICE")).doubleValue();
            percent += 100;
        } else {
            percent = ((java.math.BigDecimal) data.get("BUYING_PRICE")).doubleValue();
            percent = 100 - percent;
        }
        doubleExRate = (currAmount * percent) / 100;
        return doubleExRate;
    }

    private static double convertToOther(double currAmount, double middleRate) {
        return currAmount * middleRate;
    }

    private static double convertToBase(double currAmount, double middleRate) {
        return currAmount / middleRate;
    }

    public static void getExchangeCalculation(String source, String target, String transType, double amount) {
        String baseCurrency = getMainCurrency();

        HashMap map = new HashMap();
        map.put("INPUT", source);
        map.put("OUTPUT", target);

        if (!baseCurrency.equalsIgnoreCase(source)) {
            map.put("INRATE", String.valueOf(getMiddleRate(source)));
        } else {
            map.put("INRATE", "-");
        }

        if (!baseCurrency.equalsIgnoreCase(target)) {
            map.put("OUTRATE", String.valueOf(getMiddleRate(target)));
        } else {
            map.put("OUTRATE", "-");
        }
        map.put("AMOUNT", String.valueOf(amount));
        map.put("MAINCURRENCY", baseCurrency);
        map.put("TRANSTYPE", transType);
        map.put("TOTALAMOUNT", String.valueOf(convertCurrency(source, target, transType, amount)));

        ExchangeRateDialogUI objExchangeRateDialogUI = new ExchangeRateDialogUI(map);
        showDialog(objExchangeRateDialogUI);
    }

    private static HashMap getExchangeRateParam(String currency, String customerType) {
        HashMap whereMap = new HashMap();
        whereMap.put("CURRENCY", currency);
        whereMap.put("CUSTOMERTYPE", customerType);
        List lst = executeQuery("getExchangeRateParam", whereMap);
        whereMap = null;
        if (lst != null && lst.size() >= 0) {
            whereMap = (HashMap) lst.get(0);
        }
        return whereMap;
    }

    private static double getMiddleRate(String currency) {
        HashMap whereMap = new HashMap();
        whereMap.put("CURRENCY", currency);
        List lst = executeQuery("getMiddleRate", whereMap);
        double middleRate = 0.0;
        if (lst != null && lst.size() >= 0) {
            middleRate = ((java.math.BigDecimal) ((HashMap) lst.get(0)).get("MIDDLE_RATE")).doubleValue();
        }
        return middleRate;
    }

    /**
     * Getting Holiday List for given month and year...
     *
     * This method used in CCalendar class...
     */
    public static List getHolidayList(String month, String year) {
        HashMap whereMap = new HashMap();
        whereMap.put("MONTH", month);
        whereMap.put("YEAR", year);

        List lst = ClientUtil.executeQuery("getHolidayList", whereMap);
        //System.out.println(lst.toString());

        return lst;
    }

    public static void showReport(String reportName) {
        TTIntegration.integration(reportName);
    }

    public static void printReport(String reportName, HashMap paramMap) {
        //TTIntegration.integration(reportName, paramMap);
        TTIntegration.integrationForPrint("");
    }

    public static boolean validPeriodMaxLength(com.see.truetransact.uicomponent.CTextField txtField, String strPeriodType) {
        String strTextBoxVal = txtField.getText();
        if (strPeriodType.length() > 0 && strTextBoxVal.length() == 0) {
            txtField.setText("");
            return false;
        } else if (strPeriodType.length() == 0 && strTextBoxVal.length() > 0) {
            txtField.setText("");
            return false;
        } else if ((strPeriodType.equalsIgnoreCase("1") || strPeriodType.equalsIgnoreCase("DAYS")) && strTextBoxVal.length() > 5) {
            txtField.setText("");
            return false;
        } else if (strPeriodType.equalsIgnoreCase("7") && strTextBoxVal.length() > 4) {
            txtField.setText("");
            return false;
        } else if ((strPeriodType.equalsIgnoreCase("30") || strPeriodType.equalsIgnoreCase("MONTHS")) && strTextBoxVal.length() > 3) {
            txtField.setText("");
            return false;
        } else if (strPeriodType.equalsIgnoreCase("90") && strTextBoxVal.length() > 3) {
            txtField.setText("");
            return false;
        } else if (strPeriodType.equalsIgnoreCase("180") && strTextBoxVal.length() > 2) {
            txtField.setText("");
            return false;
        } else if ((strPeriodType.equalsIgnoreCase("360") || strPeriodType.equalsIgnoreCase("365") || strPeriodType.equalsIgnoreCase("YEARS")) && strTextBoxVal.length() > 2) {
            txtField.setText("");
            return false;
        }
        return true;
    }

// Validate for DOB, DOB < current dt. and he should be Major and 
// not greater than Retirement Age
    public static void validateDob(CDateField tdtObj) {
        if (tdtObj.getDateValue() != null && !tdtObj.getDateValue().equals("")) {
            ToDateValidation toDate = new ToDateValidation(getCurrentDate(), true);
            toDate.setComponent(tdtObj);
            if (!toDate.validate()) {
                toDate.setErrorMessage("DOB should be LESSER than today's date");
                tdtObj.setDateValue("");
            } else {
                //--- Check for Major and not greater than retirement age
                List list = (List) ClientUtil.executeQuery("getSelectConfigPasswordTO", null);
                ConfigPasswordTO configPasswordTO = (ConfigPasswordTO) list.get(0);
                int retirementAge = CommonUtil.convertObjToInt(configPasswordTO.getRetirementAge());
                int minorAge = CommonUtil.convertObjToInt(configPasswordTO.getMinorAge());
                java.util.Date tdtObjDate = DateUtil.getDateMMDDYYYY(tdtObj.getDateValue());
                //--- set Minor age
                java.util.Calendar calMin = java.util.Calendar.getInstance();
                calMin.setTime(tdtObjDate);
                calMin.add(java.util.Calendar.YEAR, minorAge);
                //--- set Retirement age
                java.util.Calendar calRet = java.util.Calendar.getInstance();
                calRet.setTime(tdtObjDate);
                calRet.add(java.util.Calendar.YEAR, retirementAge);
                //---if(Dob is b/w minorage and retirement age , then show it else clear it
                if (isTodayBetween(calMin.getTime(), calRet.getTime()) == false) {
                    toDate.setErrorMessage("DOB should be between Minor Age and Retirement Age");
                    tdtObj.setDateValue("");
                }
            }
        }
    }

    public static boolean isTodayBetween(java.util.Date dtFrom, java.util.Date dtTo) {
        boolean retDate = false;
        Date TodaysDt = getCurrentDate();
        //--- If Today is Between the "From date" and "To Date" , then return true.
        if (((TodaysDt.after(dtFrom) || TodaysDt.compareTo(dtFrom) == 0) && (TodaysDt.before((dtTo))) || TodaysDt.compareTo(dtTo) == 0)) {
            retDate = true;
        }
        return retDate;
    }

    public static Date getCurrentDate() {
//        GregorianCalendar calendar = new GregorianCalendar();
//        return calendar.getTime(); 
//       return getDate(2, 6, 2004);//2-JUN-04

//        return calendar.getTime();

////////////////        Date applDate = null;
////////////////        java.util.List lst;
////////////////        try {
////////////////            HashMap whereMap = new HashMap();
////////////////            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
////////////////            lst = ClientUtil.executeQuery("getApplDate", whereMap);
////////////////            applDate = (Date) lst.get(0);
////////////////
////////////////        } catch (Exception cnF) {
////////////////        }
////////////////        return applDate;
        Date applDate = null;
        java.util.List lst;
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            lst = ClientUtil.executeQuery("getApplDateHashMap", whereMap);
            whereMap = (HashMap) lst.get(0);
            applDate = (Date) whereMap.get("CURR_APPL_DT");
//            System.out.println("#### applDate : "+applDate);
        } catch (Exception cnF) {
        }
        return applDate;
    }

    public static Date getCurrentDateProperFormat() {
        Date applDate = null;
        java.util.List lst;
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            lst = ClientUtil.executeQuery("getApplDateHashMap", whereMap);
            whereMap = (HashMap) lst.get(0);
            applDate = (Date) whereMap.get("CURR_APPL_DT");
//            System.out.println("#### applDate : "+applDate);
        } catch (Exception cnF) {
        }
        return applDate;
    }

    public static Date getCurrentDateWithTime() {
        Date applDate = null;
        java.util.List lst;
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            lst = ClientUtil.executeQuery("getApplDate", whereMap);
            applDate = (Date) lst.get(0);
            GregorianCalendar c1 = new GregorianCalendar();
            applDate.setHours(c1.getTime().getHours());
            applDate.setMinutes(c1.getTime().getMinutes());
            applDate.setSeconds(c1.getTime().getSeconds());
//            System.out.println("#### applDate : "+applDate);
        } catch (Exception cnF) {
        }
        return applDate;
    }

    /**
     * gets the Date in DD/MM/YYYY Mode as String
     *
     */
    public static String getCurrentDateinDDMMYYYY() {
        Date dt = getCurrentDate();
        StringBuffer sbDt = new StringBuffer();
        sbDt.append(dt.getDate());
        sbDt.append("/");
        sbDt.append((dt.getMonth() + 1));
        sbDt.append("/");
        sbDt.append((dt.getYear() + 1900));
        return sbDt.toString();
    }

    // returns Maximum days in the month
    public static int getMaxDayInMonth(int month) {
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(getCurrentDate());
        gcal.set(gcal.MONTH, month);
        return gcal.getActualMaximum(gcal.DATE);
    }

    // returns Maximum days/weeks/months from current date
    public static int getActualMaximum(int option) {
        GregorianCalendar gcl = new GregorianCalendar();
        gcl.setTime(getCurrentDate());
        return gcl.getActualMaximum(option);
    }

    /**
     * convertObjToStr used to check the null value in the object.. if it is
     * null, it will return empty string.
     */
    public static String convertObjToCurrency(Object obj) {
        String returnStr = "";
        CurrencyValidation currencyValidationObj = new CurrencyValidation();
        if (obj == null) {
            returnStr = "0";
        } else {
            returnStr = String.valueOf(obj);
        }
        if (returnStr.length() > 0) {
            double value = Double.parseDouble(returnStr);
            boolean isNegative = false;
            if (value < 0) {
                value = -1 * value;
                isNegative = true;
            }
            returnStr = currencyValidationObj.getFormattedText(String.valueOf(value));
            if (isNegative) {
                returnStr = "-" + returnStr;
            }
        }
        return returnStr;
    }

    /**
     * @param str
     */
    public static void main(String str[]) {
        //HashMap lst = ClientUtil.getScreenConfigList("com.see.truetransact.ui.transaction.cash.CashTransactionUI");
        //System.out.println (lst.toString());
        /*ClientUtil.convertCurrency("USD", "USD", "DEBIT", 100);
         ClientUtil.convertCurrency("USD", "GBP", "DEBIT", 100);
         ClientUtil.convertCurrency("GBP", "USD", "DEBIT", 100);
         ClientUtil.convertCurrency("GBP", "EUR", "DEBIT", 100);
         ClientUtil.convertCurrency("USD", "INR", "DEBIT", 100);
         ClientUtil.convertCurrency("INR", "USD", "DEBIT", 100);
         ClientUtil.convertCurrency("INR", "GBP", "DEBIT", 100);*/

//        HashMap wmap = new HashMap();
//        wmap.put("TABLE", "LOOKUP_MASTER");
//        
//        HashMap map = new HashMap();
//        map.put(CommonConstants.MAP_NAME, "selectAnyTable");
//        map.put(CommonConstants.MAP_WHERE, wmap);
//        
//        System.out.println(map.toString());
//        HashMap resultMap = ClientUtil.executeTableQuery(map);
//        System.out.println(resultMap.toString());

//        ClientUtil.showMessageWindow("Test");
        ClientUtil.noDataAlert();
    }

    public static void setLanguage(java.awt.Container comp, java.util.Locale currentLocale) {
//        java.util.Locale currentLocale = java.util.Locale.getDefault();
        //System.out.println("#$#$ currentLocale.getLanguage():"+currentLocale.getLanguage());
        if (currentLocale.getLanguage().equals("ta")) {
            changeLabelLanguage(comp, new java.awt.Font("ELCOT-Tirunelveli", java.awt.Font.PLAIN, 15));
        } 
        else if (currentLocale.getLanguage().equals("hi")) {
            changeLabelLanguage(comp, new java.awt.Font("Kruti Dev 010", java.awt.Font.PLAIN, 16));
        } 
        else if (currentLocale.getLanguage().equals("ml")) {
            changeLabelLanguage(comp, new java.awt.Font("Kartika", java.awt.Font.PLAIN, 16));
        } else {
            changeLabelLanguage(comp, new java.awt.Font("MS Sans Serif", java.awt.Font.PLAIN, 13));
        }
    }

    private static void changeLabelLanguage(java.awt.Container comp, java.awt.Font font) {
        java.awt.Component[] children = comp.getComponents();

        for (int i = 0; i < children.length; i++) {
            if ((children[i] != null)) {
                if (children[i] instanceof javax.swing.JLabel
                        || children[i] instanceof javax.swing.JButton
                        || children[i] instanceof javax.swing.JRadioButton) {
                    children[i].setFont(font);
                } else if (children[i] instanceof javax.swing.JPanel) {
                    if (((javax.swing.JPanel) children[i]).getBorder() instanceof javax.swing.border.TitledBorder) {
                        ((javax.swing.border.TitledBorder) ((javax.swing.JPanel) children[i]).getBorder()).setTitleFont(font);
                    }
                } else if (children[i] instanceof javax.swing.JTable) {
                    ((javax.swing.JTable) children[i]).getTableHeader().setFont(font);
                }
                if (children[i] instanceof java.awt.Container) {
                    changeLabelLanguage((java.awt.Container) children[i], font);
                }

            }

        }
        return;
    }

    public static Date getOtherBranchCurrentDate(String selectedBranchId) {
        Date applDate = null;
        String dayEndStatus = "";
        java.util.List lst;
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", selectedBranchId);
            lst = ClientUtil.executeQuery("getOtherBranchApplDateHashMap", whereMap);
            whereMap = (HashMap) lst.get(0);
            dayEndStatus = (String) whereMap.get("END_DAY_STATUS");
            if (null != dayEndStatus && !dayEndStatus.equals("") && dayEndStatus.equals("COMPLETED")) {
                applDate = null;
            } else {
                applDate = (Date) whereMap.get("CURR_APPL_DT");
            }
        } catch (Exception cnF) {
        }
        return applDate;
    }
}
