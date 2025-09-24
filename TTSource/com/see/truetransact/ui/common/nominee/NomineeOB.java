/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * NomineeOB.java
 *
 * Created on Fri Dec 24 10:26:24 IST 2004
 */

package com.see.truetransact.ui.common.nominee;


import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientexception.ClientParseException;

import com.see.truetransact.transferobject.common.nominee.NomineeTO;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 *
 * @author
 */

public class NomineeOB extends CObservable{
    
    /* To Ste the Titles for the Tables...*/
    ArrayList nomineeTabTitle = new ArrayList();
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    
    private ArrayList key;
    private ArrayList value;
    Date curDate = null;
    /**
     * To Store the Data for the Nomination...
     */
    private ArrayList nomimeeList = new ArrayList();
    /**
     * To Store the Data for the deleted Record...
     */
    private ArrayList deleteNomineeList = new ArrayList();
    
    private EnhancedTableModel tblNominee;
    
    private ComboBoxModel cbmGCity;
    private ComboBoxModel cbmGCountry;
    private ComboBoxModel cbmGState;
    private ComboBoxModel cbmNCity;
    private ComboBoxModel cbmNCountry;
    private ComboBoxModel cbmNState;
    private ComboBoxModel cbmNomineeRelationNO;
    private ComboBoxModel cbmRelationNO;
    private ComboBoxModel cbmNomineeStatus;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    /** To get the Value of Column Title and Dialogue Box...*/
//    final NomineeRB objNomineeRB = new NomineeRB();
    java.util.ResourceBundle objNomineeRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.nominee.NomineeRB", ProxyParameters.LANGUAGE);
    
//    private static NomineeOB nomineeOB;
//    static {
//        try {
//            nomineeOB = new NomineeOB();
//        } catch(Exception e) {
//            parseException.logException(e,true);
//        }
//    }
    
    public static NomineeOB getInstance() throws Exception{
        NomineeOB nomineeOB = new NomineeOB();
        return nomineeOB;
    }
    
    /** Creates a new instance of NomineeOB */
    public NomineeOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        fillDropdown();     //__ To Fill all the Combo Boxes
        setNomineeTabTitle();    //__ To set the Title of Table in Charges Tab...
        tblNominee = new EnhancedTableModel(null, nomineeTabTitle);
        /**
         * To Initialize the ArrayList(s) for Insert And Delete Purpose...
         */
        nomimeeList = new ArrayList();
        deleteNomineeList = new ArrayList();
    }
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("MAJOR_MINOR");
        lookup_keys.add("RELATIONSHIP");
        lookup_keys.add("GUARDIAN_RELATIONSHIP");
        lookup_keys.add("CUSTOMER.COUNTRY");
        lookup_keys.add("CUSTOMER.STATE");
        lookup_keys.add("CUSTOMER.CITY");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("MAJOR_MINOR"));
        cbmNomineeStatus = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("RELATIONSHIP"));
        cbmNomineeRelationNO = new ComboBoxModel(key,value);
        
//        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("GUARDIAN_RELATIONSHIP"));
        cbmRelationNO = new ComboBoxModel(key,value);

        getKeyValue((HashMap)keyValue.get("CUSTOMER.COUNTRY"));
        cbmGCountry = new ComboBoxModel(key,value);
        cbmNCountry = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.STATE"));
        cbmGState = new ComboBoxModel(key,value);
        cbmNState = new ComboBoxModel(key,value);
        
        
        getKeyValue((HashMap)keyValue.get("CUSTOMER.CITY"));
        cbmGCity = new ComboBoxModel(key,value);
        cbmNCity = new ComboBoxModel(key,value);
    }
    
    /* To set the Column title in Table(s)...*/
    private void setNomineeTabTitle() throws Exception{
        nomineeTabTitle.add(objNomineeRB.getString("tabNominee1"));
        nomineeTabTitle.add(objNomineeRB.getString("tabNominee2"));
        nomineeTabTitle.add(objNomineeRB.getString("tabNominee3"));
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To set the Data in the TransferObjects...
     */
    private NomineeTO setAccountNominee() {
        final NomineeTO objAccountNomineeTO = new NomineeTO();
        try{
            objAccountNomineeTO.setActNum(getActNum());
            objAccountNomineeTO.setNomineeId(CommonUtil.convertObjToStr(getNomineeId()));
            objAccountNomineeTO.setNomineeName(CommonUtil.convertObjToStr(txtNomineeNameNO));
            
            objAccountNomineeTO.setCustId(CommonUtil.convertObjToStr(lblCustNo));
            
            objAccountNomineeTO.setRelationship(CommonUtil.convertObjToStr(cbmNomineeRelationNO.getKeyForSelected()));
            objAccountNomineeTO.setStreet(CommonUtil.convertObjToStr(txtNStreet));
            objAccountNomineeTO.setArea(CommonUtil.convertObjToStr(txtNArea));
            objAccountNomineeTO.setCountryCode(CommonUtil.convertObjToStr(cbmNCountry.getKeyForSelected()));
            objAccountNomineeTO.setCity(CommonUtil.convertObjToStr(cbmNCity.getKeyForSelected()));
            objAccountNomineeTO.setState(CommonUtil.convertObjToStr(cbmNState.getKeyForSelected()));
            objAccountNomineeTO.setPinCode(CommonUtil.convertObjToStr(txtNPinCode));
            objAccountNomineeTO.setAreaCode(CommonUtil.convertObjToStr(txtNomineeACodeNO));
            objAccountNomineeTO.setPhNo(CommonUtil.convertObjToStr(txtNomineePhoneNO));
            objAccountNomineeTO.setSharePer(CommonUtil.convertObjToDouble(txtNomineeShareNO));
            
            
//            System.out.println("getRdoStatus_MajorNO: " + getRdoStatus_MajorNO());
//            System.out.println("getRdoStatus_MinorNO: " + getRdoStatus_MinorNO());
            objAccountNomineeTO.setNomineeStatus(CommonUtil.convertObjToStr(cbmNomineeStatus.getKeyForSelected()));
//            if (getCboNomineeStatus().equals("Major")) {
//                objAccountNomineeTO.setNomineeStatus(CommonUtil.convertObjToStr("Y"));
//            } else 
            if(!CommonUtil.convertObjToStr(cbmNomineeStatus.getKeyForSelected()).equals("0")){
//                if(!getCboNomineeStatus().equals("Major")){
//                objAccountNomineeTO.setNomineeStatus(CommonUtil.convertObjToStr("N"));
                
                Date IsDt = DateUtil.getDateMMDDYYYY(tdtMinorDOBNO);
                if(IsDt != null){
                Date isDate = (Date)curDate.clone();
                isDate.setDate(IsDt.getDate());
                isDate.setMonth(IsDt.getMonth());
                isDate.setYear(IsDt.getYear());
                objAccountNomineeTO.setNomineeDob(isDate);
                }else{
                    objAccountNomineeTO.setNomineeDob(DateUtil.getDateMMDDYYYY(tdtMinorDOBNO));
                }
//                objAccountNomineeTO.setNomineeDob(DateUtil.getDateMMDDYYYY(tdtMinorDOBNO));
                
                objAccountNomineeTO.setGuardianName(CommonUtil.convertObjToStr(txtGuardianNameNO));
                objAccountNomineeTO.setGStreet(CommonUtil.convertObjToStr(txtGStreet));
                objAccountNomineeTO.setGArea(CommonUtil.convertObjToStr(txtGArea));
                objAccountNomineeTO.setGCity(CommonUtil.convertObjToStr(cbmGCity.getKeyForSelected()));
                objAccountNomineeTO.setGState(CommonUtil.convertObjToStr(cbmGState.getKeyForSelected()));
                objAccountNomineeTO.setGPinCode(CommonUtil.convertObjToStr(txtGPinCode));
                objAccountNomineeTO.setGAreaCode(CommonUtil.convertObjToStr(txtGuardianACodeNO));
                objAccountNomineeTO.setGPhNo(CommonUtil.convertObjToStr(txtGuardianPhoneNO));
                objAccountNomineeTO.setGRelationship(CommonUtil.convertObjToStr(cbmRelationNO.getKeyForSelected()));
                objAccountNomineeTO.setGCountryCode(CommonUtil.convertObjToStr(cbmGCountry.getKeyForSelected()));
            }
            /*objAccountNomineeTO.setStatus(getTxtStatus());
            objAccountNomineeTO.setStatusBy(getTxtStatusBy());
            objAccountNomineeTO.setStatusDt(DateUtil.getDateMMDDYYYY(getTxtStatusDt()));*/
            
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objAccountNomineeTO;
    }
    
    
    /** To Set the data from the TransferObjects and set 'em in the OB...
     */
    private void setAccountNomineeTO(NomineeTO objAccountNomineeTO) throws Exception{
        
        System.out.println("Nominee Status: " + objAccountNomineeTO.getNomineeStatus());
        setActNum(CommonUtil.convertObjToStr(objAccountNomineeTO.getActNum()));
        setNomineeId(CommonUtil.convertObjToStr(objAccountNomineeTO.getNomineeId()));
        setTxtMaxNominees(getTxtMaxNominees());
        setTxtNomineeNameNO(CommonUtil.convertObjToStr(objAccountNomineeTO.getNomineeName()));
        
        setLblCustNo(CommonUtil.convertObjToStr(objAccountNomineeTO.getCustId()));
        
        setCboNomineeRelationNO((String) getCbmNomineeRelationNO().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getRelationship())));
        setTxtNStreet(CommonUtil.convertObjToStr(objAccountNomineeTO.getStreet()));
        setTxtNArea(CommonUtil.convertObjToStr(objAccountNomineeTO.getArea()));
        setCboNCity((String) getCbmNCity().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getCity())));
        setCboNState((String) getCbmNState().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getState())));
        setCboNCountry((String) getCbmNCountry().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getCountryCode())));
        
        setTxtNPinCode(CommonUtil.convertObjToStr(objAccountNomineeTO.getPinCode()));
        setTxtNomineeACodeNO(CommonUtil.convertObjToStr(objAccountNomineeTO.getAreaCode()));
        setTxtNomineePhoneNO(CommonUtil.convertObjToStr(objAccountNomineeTO.getPhNo()));
        setTxtNomineeShareNO(CommonUtil.convertObjToStr(objAccountNomineeTO.getSharePer()));
        setCboNomineeStatus((String) getCbmNomineeStatus().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getNomineeStatus())));
//        if (CommonUtil.convertObjToStr(objAccountNomineeTO.getNomineeStatus()).equals("Y")){
//            setRdoStatus_MajorNO(true);
//        } else if(CommonUtil.convertObjToStr(objAccountNomineeTO.getNomineeStatus()).equals("N")){ 
//            setRdoStatus_MinorNO(true);
//        }
        
//        System.out.println("setRdoStatus_MajorNO: " + getRdoStatus_MajorNO());
//        System.out.println("setRdoStatus_MinorNO: " + getRdoStatus_MinorNO());
        
//        if(!getCboNomineeStatus().equals("Major")){
        if(!CommonUtil.convertObjToStr(cbmNomineeStatus.getKeyForSelected()).equals("0")){
            setTdtMinorDOBNO(DateUtil.getStringDate(objAccountNomineeTO.getNomineeDob()));
            setTxtGuardianNameNO(CommonUtil.convertObjToStr(objAccountNomineeTO.getGuardianName()));
            setTxtGStreet(CommonUtil.convertObjToStr(objAccountNomineeTO.getGStreet()));
            setTxtGArea(CommonUtil.convertObjToStr(objAccountNomineeTO.getGArea()));
            setCboGCity((String) getCbmGCity().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getGCity())));
            setCboGState((String) getCbmGState().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getGState())));
            setTxtGPinCode(CommonUtil.convertObjToStr(objAccountNomineeTO.getGPinCode()));
            setTxtGuardianACodeNO(CommonUtil.convertObjToStr(objAccountNomineeTO.getGAreaCode()));
            setTxtGuardianPhoneNO(CommonUtil.convertObjToStr(objAccountNomineeTO.getGPhNo()));
            setCboRelationNO((String) getCbmRelationNO().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getGRelationship())));
            setCboGCountry((String) getCbmGCountry().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getGCountryCode())));
        }
    }
    
    /**
     * To get the Max(ID) of the Deleted Nominee Details...
     */
    public void maxDelNominee(String actNum){
        int maxId = 0;
        try{
            final HashMap data = new HashMap();
            data.put("ACTNUM",actNum);
            final List resultList = ClientUtil.executeQuery("getMaxNomineeID", data);
            if(resultList.size() > 0){
                final HashMap resultMap = (HashMap)resultList.get(0);
                maxId = CommonUtil.convertObjToInt(resultMap.get("MAXID"));
            }
            setMaxId(maxId);  //__ To Set the Max-Id of the Deleted Records...
        }catch(Exception e){
            System.out.println("Error in maxDelNominee");
            parseException.logException(e,true);
        }
    }
    
    /**
     * To generate the Id for the Nominee...
     */
    public int nomineeId(){
        int j, rowCount, tempId=0, id;
        rowCount = getRowCount();
        /**
         * To get the Max Nominee-Id from the data in the Table...
         */
        if(nomimeeList!=null){
            if(nomimeeList.size()>0){
                for( j = 0; j < rowCount; j++){
                    id = CommonUtil.convertObjToInt(((NomineeTO)nomimeeList.get(j)).getNomineeId());
                    if(tempId<id){
                        tempId = id;
                    }
                }
            }
        }
        
        /**
         * To get the MaxNominee-Id from the deleted Data...
         */
        if(tempId < getMaxId()){
            return (getMaxId()+1);
        }else{
            return (tempId+1);
        }
    }
    
    
    /**
     * To get the Total Share of the Nominees...
     */
    public double nomineeShare(){
        int i;
        double sum = 0;
        i = getRowCount();
        if(nomimeeList!=null){
            if(nomimeeList.size()>0){
                System.out.println("%#$# nomimeeList : "+nomimeeList);
                for(int j = 0; j<i; j++){
                    sum = sum + CommonUtil.convertObjToDouble(((NomineeTO)nomimeeList.get(j)).getSharePer()).doubleValue();
                }
            }
        }
        return sum;
    }
    
    /**
     * To Add the Nominee data in to the Table...
     */
    public void addNomineeData(int updateValue, int rowNo){
        //__ generate the TO Object...
        
        NomineeTO objAccountNomineeTO = setAccountNominee();
        /**
         * Create an ArrayList so as to enter the Data in the Nominee Table...
         */
        ArrayList nomineeTabRow = new ArrayList();
        //final String NOMINEEID = CommonUtil.convertObjToStr(String.valueOf(nomineeId()));
        if(updateValue!=1){
            setNomineeId(CommonUtil.convertObjToStr(String.valueOf(nomineeId())));
        }
        
        nomineeTabRow.add(getNomineeId());
        nomineeTabRow.add(txtNomineeNameNO);
        //nomineeTabRow.add(CommonUtil.convertObjToStr(cbmNomineeRelationNO.getKeyForSelected()));
        nomineeTabRow.add(CommonUtil.convertObjToStr(cbmNomineeRelationNO.getDataForKey(cbmNomineeRelationNO.getKeyForSelected())));
        /**
         * To insert a new row in the Table...
         */
        if(updateValue!=1){
            tblNominee.addRow(nomineeTabRow);
//            ArrayList data = tblNominee.getDataArrayList();
            //__ Add the Nominee Id to the To Object...
            objAccountNomineeTO.setNomineeId(getNomineeId());
            nomimeeList.add(objAccountNomineeTO);
//            nomimeeList.add(data.size()-1, objAccountNomineeTO);
        }else{
            tblNominee.setValueAt(txtNomineeNameNO, rowNo, 1);
            //tblNominee.setValueAt(CommonUtil.convertObjToStr(cbmNomineeRelationNO.getKeyForSelected()), rowNo, 2);
            tblNominee.setValueAt(CommonUtil.convertObjToStr(cbmNomineeRelationNO.getDataForKey(cbmNomineeRelationNO.getKeyForSelected())), rowNo, 2);
            nomimeeList.remove(rowNo);
            nomimeeList.add(rowNo, objAccountNomineeTO);
        }
    }
    
    public double getSharedata(int rowNo){
        double share = 0;
        share = CommonUtil.convertObjToDouble(((NomineeTO)nomimeeList.get(rowNo)).getSharePer()).doubleValue();
        return share;
    }
    
    /** To delete the Row(s) in the Nominee Table...
     */
    public void deleteNomineeTab(int rowNo){
        try{
            /**
             * To Check if the Data is already in the Database...
             * If yes, put the deleted Record in the ArrayList of deleted Objects
             * else, proceed Normally...
             */
            String status = "";
            status = CommonUtil.convertObjToStr(((NomineeTO)nomimeeList.get(rowNo)).getStatus());
            if(status.equalsIgnoreCase("")){
                tblNominee.removeRow(rowNo);
                nomimeeList.remove(rowNo);
            }else{
                tblNominee.removeRow(rowNo);
                deleteNomineeList.add((NomineeTO)nomimeeList.get(rowNo));
                nomimeeList.remove(rowNo);
                
                System.out.println("deleteNomineeList: " + deleteNomineeList);
            }
        }catch(Exception e){
            parseException.logException(e,true);
            //e.printStackTrace();
        }
    }
    
    /** To display the data in the Table when Some row is selected...
     */
    public void populateNomineeTab(int row){
        try{
            setAccountNomineeTO((NomineeTO)nomimeeList.get(row));
        }catch( Exception e ) {
            parseException.logException(e,true);
        }
    }
    
    /**
     * To display the data in the Table at the time of Edit/delete...
     */
    
    public void setNomineeTabData(){
        int rowCount = getNomimeeList().size();
        NomineeTO objAccountNomineeTO;
        double totalShare = 0;
        for(int i =0; i< rowCount; i++){
            ArrayList nomineeTabRow = new ArrayList();
            objAccountNomineeTO = (NomineeTO)getNomimeeList().get(i);
            nomineeTabRow.add(CommonUtil.convertObjToStr(objAccountNomineeTO.getNomineeId()));
            nomineeTabRow.add(CommonUtil.convertObjToStr(objAccountNomineeTO.getNomineeName()));
            nomineeTabRow.add((String) getCbmNomineeRelationNO().getDataForKey(CommonUtil.convertObjToStr(objAccountNomineeTO.getRelationship())));
            //nomineeTabRow.add(CommonUtil.convertObjToStr(objAccountNomineeTO.getRelationship()));
            totalShare = totalShare + CommonUtil.convertObjToDouble(objAccountNomineeTO.getSharePer()).doubleValue();
            
            tblNominee.addRow(nomineeTabRow);
        }
        setTxtTotalShareNO(String.valueOf(totalShare));
        ttNotifyObservers();
    }
    
    /**
     * To Know the number of rows in the Table...
     */
    public int getRowCount(){
        int i=0;
        ArrayList data = tblNominee.getDataArrayList();
        i = data.size();
        data = null;  //__ To Make the Object as null...
        return i;
    }
    
    /**
     * To Reset the Nominee Table...
     */
    public void resetNomineeTab(){
        int rowCount = getRowCount();
        for(int i=rowCount; i>0; i--){
            tblNominee.removeRow(i-1);
        }
    }
    
    
    private String txtNomineeNameNO = "";
    private String cboNomineeRelationNO = "";
    private String txtNomineeACodeNO = "";
    private String txtNomineePhoneNO = "";
    private String txtNomineeShareNO = "";
    private String txtMinNominees = "";
    private String txtNStreet = "";
    private String txtNArea = "";
    private String cboNCountry = "";
    private String cboNState = "";
    private String cboNCity = "";
    private String txtNPinCode = "";
    private String tdtMinorDOBNO = "";
    private String cboRelationNO = "";
    private String cboNomineeStatus = "";
    private String txtGuardianNameNO = "";
    private String txtGuardianACodeNO = "";
    private String txtGuardianPhoneNO = "";
    private String txtGStreet = "";
    private String txtGArea = "";
    private String cboGCountry = "";
    private String cboGState = "";
    private String cboGCity = "";
    private String txtGPinCode = "";
    private String txtTotalShareNO = "";
    private int txtMaxNominees=0;
    
    private String lblCustNo = "";
    
    // To Set the Account No in Table...
    private String actNum = "";
    private String nomineeId = "";
    private int maxId;
    
    
    /**
     * To Reset the Fields in the Screen...
     */
    public void resetNomineeFields(){
        setTxtNomineeNameNO("");
        setTxtNomineeACodeNO("");
        setTxtNomineePhoneNO("");
        setCboNomineeStatus("");
        setTxtNomineeShareNO("");
        setTxtNStreet("");
        setTxtNArea ("");
        setCboNCountry("");
        setCboNState("");
        setCboNCity("");
        setTxtNPinCode("");
        setTdtMinorDOBNO ("");
        setCboRelationNO("");
        setTxtGuardianNameNO("");
        setTxtGuardianACodeNO ("");
        setTxtGuardianPhoneNO ("");
        setTxtGStreet("");
        setTxtGArea("");
        setCboGCountry("");
        setCboGState("");
        setCboGCity("");
        setTxtGPinCode ("");
        setNomineeId("");
        setLblCustNo("");
        setCboNomineeRelationNO ("");
    }
    
    
    public void resetGuardian(){
        setTdtMinorDOBNO ("");
        setCboRelationNO("");
        setTxtGuardianNameNO("");
        setTxtGuardianACodeNO ("");
        setTxtGuardianPhoneNO ("");
        setTxtGStreet("");
        setTxtGArea("");
        setCboGCountry("");
        setCboGState("");
        setCboGCity("");
        setTxtGPinCode ("");
    }
    
    
    
    public void resetLists(){
        nomimeeList =null;
        deleteNomineeList = null;
        
        nomimeeList = new ArrayList();;
        deleteNomineeList = new ArrayList();
    }
    
    //__ To get the Addr of the Customer selected...
    public HashMap getCustAddr(HashMap dataMap){
        List list = ClientUtil.executeQuery("Nominee.getCustAddr", dataMap);
        if(list.size() > 0){
            dataMap = (HashMap)list.get(0);
        }
        return dataMap;
    }
    
    //__ To get the Addr of the Customer selected...
    public HashMap getGuardianDetails(HashMap dataMap){
        List list = ClientUtil.executeQuery("Nominee.getCustGuardian", dataMap);
        if(list.size() > 0){
            dataMap = (HashMap)list.get(0);
        }
        return dataMap;
    }
    
    // Setter method for txtNomineeNameNO
    void setTxtNomineeNameNO(String txtNomineeNameNO){
        this.txtNomineeNameNO = txtNomineeNameNO;
        setChanged();
    }
    // Getter method for txtNomineeNameNO
    String getTxtNomineeNameNO(){
        return this.txtNomineeNameNO;
    }
    
    // Setter method for cboNomineeRelationNO
    void setCboNomineeRelationNO(String cboNomineeRelationNO){
        this.cboNomineeRelationNO = cboNomineeRelationNO;
        setChanged();
    }
    // Getter method for cboNomineeRelationNO
    String getCboNomineeRelationNO(){
        return this.cboNomineeRelationNO;
    }
    
    // Setter method for txtNomineeACodeNO
    void setTxtNomineeACodeNO(String txtNomineeACodeNO){
        this.txtNomineeACodeNO = txtNomineeACodeNO;
        setChanged();
    }
    // Getter method for txtNomineeACodeNO
    String getTxtNomineeACodeNO(){
        return this.txtNomineeACodeNO;
    }
    
    // Setter method for txtNomineePhoneNO
    void setTxtNomineePhoneNO(String txtNomineePhoneNO){
        this.txtNomineePhoneNO = txtNomineePhoneNO;
        setChanged();
    }
    // Getter method for txtNomineePhoneNO
    String getTxtNomineePhoneNO(){
        return this.txtNomineePhoneNO;
    }
    
   
    // Setter method for txtNomineeShareNO
    void setTxtNomineeShareNO(String txtNomineeShareNO){
        this.txtNomineeShareNO = txtNomineeShareNO;
        setChanged();
    }
    // Getter method for txtNomineeShareNO
    String getTxtNomineeShareNO(){
        return this.txtNomineeShareNO;
    }
    
    // Setter method for txtMinNominees
    void setTxtMinNominees(String txtMinNominees){
        this.txtMinNominees = txtMinNominees;
        setChanged();
    }
    // Getter method for txtMinNominees
    String getTxtMinNominees(){
        return this.txtMinNominees;
    }
    
    // Setter method for txtNStreet
    void setTxtNStreet(String txtNStreet){
        this.txtNStreet = txtNStreet;
        setChanged();
    }
    // Getter method for txtNStreet
    String getTxtNStreet(){
        return this.txtNStreet;
    }
    
    // Setter method for txtNArea
    void setTxtNArea(String txtNArea){
        this.txtNArea = txtNArea;
        setChanged();
    }
    // Getter method for txtNArea
    String getTxtNArea(){
        return this.txtNArea;
    }
    
    // Setter method for cboNCountry
    void setCboNCountry(String cboNCountry){
        this.cboNCountry = cboNCountry;
        setChanged();
    }
    // Getter method for cboNCountry
    String getCboNCountry(){
        return this.cboNCountry;
    }
    
    // Setter method for cboNState
    void setCboNState(String cboNState){
        this.cboNState = cboNState;
        setChanged();
    }
    // Getter method for cboNState
    String getCboNState(){
        return this.cboNState;
    }
    
    // Setter method for cboNCity
    void setCboNCity(String cboNCity){
        this.cboNCity = cboNCity;
        setChanged();
    }
    // Getter method for cboNCity
    String getCboNCity(){
        return this.cboNCity;
    }
    
    // Setter method for txtNPinCode
    void setTxtNPinCode(String txtNPinCode){
        this.txtNPinCode = txtNPinCode;
        setChanged();
    }
    // Getter method for txtNPinCode
    String getTxtNPinCode(){
        return this.txtNPinCode;
    }
    
    // Setter method for tdtMinorDOBNO
    void setTdtMinorDOBNO(String tdtMinorDOBNO){
        this.tdtMinorDOBNO = tdtMinorDOBNO;
        setChanged();
    }
    // Getter method for tdtMinorDOBNO
    String getTdtMinorDOBNO(){
        return this.tdtMinorDOBNO;
    }
    
    // Setter method for cboRelationNO
    void setCboRelationNO(String cboRelationNO){
        this.cboRelationNO = cboRelationNO;
        setChanged();
    }
    // Getter method for cboRelationNO
    String getCboRelationNO(){
        return this.cboRelationNO;
    }
    
    // Setter method for txtGuardianNameNO
    void setTxtGuardianNameNO(String txtGuardianNameNO){
        this.txtGuardianNameNO = txtGuardianNameNO;
        setChanged();
    }
    // Getter method for txtGuardianNameNO
    String getTxtGuardianNameNO(){
        return this.txtGuardianNameNO;
    }
    
    // Setter method for txtGuardianACodeNO
    void setTxtGuardianACodeNO(String txtGuardianACodeNO){
        this.txtGuardianACodeNO = txtGuardianACodeNO;
        setChanged();
    }
    // Getter method for txtGuardianACodeNO
    String getTxtGuardianACodeNO(){
        return this.txtGuardianACodeNO;
    }
    
    // Setter method for txtGuardianPhoneNO
    void setTxtGuardianPhoneNO(String txtGuardianPhoneNO){
        this.txtGuardianPhoneNO = txtGuardianPhoneNO;
        setChanged();
    }
    // Getter method for txtGuardianPhoneNO
    String getTxtGuardianPhoneNO(){
        return this.txtGuardianPhoneNO;
    }
    
    // Setter method for txtGStreet
    void setTxtGStreet(String txtGStreet){
        this.txtGStreet = txtGStreet;
        setChanged();
    }
    // Getter method for txtGStreet
    String getTxtGStreet(){
        return this.txtGStreet;
    }
    
    // Setter method for txtGArea
    void setTxtGArea(String txtGArea){
        this.txtGArea = txtGArea;
        setChanged();
    }
    // Getter method for txtGArea
    String getTxtGArea(){
        return this.txtGArea;
    }
    
    // Setter method for cboGCountry
    void setCboGCountry(String cboGCountry){
        this.cboGCountry = cboGCountry;
        setChanged();
    }
    // Getter method for cboGCountry
    String getCboGCountry(){
        return this.cboGCountry;
    }
    
    // Setter method for cboGState
    void setCboGState(String cboGState){
        this.cboGState = cboGState;
        setChanged();
    }
    // Getter method for cboGState
    String getCboGState(){
        return this.cboGState;
    }
    
    // Setter method for cboGCity
    void setCboGCity(String cboGCity){
        this.cboGCity = cboGCity;
        setChanged();
    }
    // Getter method for cboGCity
    String getCboGCity(){
        return this.cboGCity;
    }
    
    // Setter method for txtGPinCode
    void setTxtGPinCode(String txtGPinCode){
        this.txtGPinCode = txtGPinCode;
        setChanged();
    }
    // Getter method for txtGPinCode
    String getTxtGPinCode(){
        return this.txtGPinCode;
    }
    
    // Setter method for txtTotalShareNO
    void setTxtTotalShareNO(String txtTotalShareNO){
        this.txtTotalShareNO = txtTotalShareNO;
        setChanged();
    }
    // Getter method for txtTotalShareNO
    String getTxtTotalShareNO(){
        return this.txtTotalShareNO;
    }
    
    // Setter method for AccountNo
    void setActNum(String actNum){
        this.actNum = actNum;
        setChanged();
    }
    // Getter method for AccountNo
    String getActNum(){
        return this.actNum;
    }
    
    // Getter method for cbmNomineeRelationNO
    ComboBoxModel getCbmNomineeRelationNO(){
        return cbmNomineeRelationNO;
    }
    
    // Getter method for cbmRelationNO
    ComboBoxModel getCbmRelationNO(){
        return cbmRelationNO;
    }
    
    // Getter method for cbmGCountry
    ComboBoxModel getCbmGCountry(){
        return cbmGCountry;
    }
    
    // Getter method for cbmNCountry
    ComboBoxModel getCbmNCountry(){
        return cbmNCountry;
    }
    
    // Getter method for cbmGState
    ComboBoxModel getCbmGState(){
        return cbmGState;
    }
    
    // Getter method for cbmNState
    ComboBoxModel getCbmNState(){
        return cbmNState;
    }
    
    // Getter method for cbmGCity
    ComboBoxModel getCbmGCity(){
        return cbmGCity;
    }
    
    // Getter method for cbmNCity
    ComboBoxModel getCbmNCity(){
        return cbmNCity;
    }
    
    void setTblNominee(EnhancedTableModel tblNominee){
        this.tblNominee = tblNominee;
        setChanged();
    }
    
    EnhancedTableModel getTblNominee(){
        return this.tblNominee;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    // Setter method for nomineeId
    void setNomineeId(String nomineeId){
        this.nomineeId = nomineeId;
        setChanged();
    }
    // Getter method for nomineeId
    String getNomineeId(){
        return this.nomineeId;
    }
    
    // Setter method for maxId
    void setMaxId(int maxId){
        this.maxId = maxId;
        setChanged();
    }
    // Getter method for maxId
    int getMaxId(){
        return this.maxId;
    }
    
    
    // Setter method for nomimeeList
    public void setNomimeeList(ArrayList nomimeeList){
        this.nomimeeList = nomimeeList;
        setChanged();
    }
    // Getter method for nomimeeList
    public ArrayList getNomimeeList(){
        return this.nomimeeList;
    }
    
    // Setter method for deleteNomineeList
    void setDeleteNomimeeList(ArrayList deleteNomineeList){
        this.deleteNomineeList = deleteNomineeList;
        setChanged();
    }
    // Getter method for deleteNomineeList
    public ArrayList getDeleteNomimeeList(){
        return this.deleteNomineeList;
    }
    
    // Setter method for lblCustNo
    public String getLblCustNo() {
        return lblCustNo;
    }  
    // Getter method for lblCustNo
    void setLblCustNo(String lblCustNo) {
        this.lblCustNo = lblCustNo;
    }
    
    /**
     * Getter for property cboNomineeStatus.
     * @return Value of property cboNomineeStatus.
     */
    public java.lang.String getCboNomineeStatus() {
        return cboNomineeStatus;
    }
    
    /**
     * Setter for property cboNomineeStatus.
     * @param cboNomineeStatus New value of property cboNomineeStatus.
     */
    public void setCboNomineeStatus(java.lang.String cboNomineeStatus) {
        this.cboNomineeStatus = cboNomineeStatus;
    }
    
    /**
     * Getter for property cbmNomineeStatus.
     * @return Value of property cbmNomineeStatus.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNomineeStatus() {
        return cbmNomineeStatus;
    }
    
    /**
     * Setter for property cbmNomineeStatus.
     * @param cbmNomineeStatus New value of property cbmNomineeStatus.
     */
    public void setCbmNomineeStatus(com.see.truetransact.clientutil.ComboBoxModel cbmNomineeStatus) {
        this.cbmNomineeStatus = cbmNomineeStatus;
    }

    public int getTxtMaxNominees() {
        return txtMaxNominees;
    }

    public void setTxtMaxNominees(int txtMaxNominees) {
        this.txtMaxNominees = txtMaxNominees;
    }
    
}