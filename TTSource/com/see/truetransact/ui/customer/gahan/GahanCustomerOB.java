/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerOB.java
 *
 * Created on August 14, 2003, 10:30 AM
 */

package com.see.truetransact.ui.customer.gahan;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
//import java.util.LinkedList;
import java.util.List;
//import java.util.Iterator;
//import com.see.truetransact.transferobject.deposit.JointAccntTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.*;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
//import com.see.truetransact.transferobject.customer.CustomerTO;
//import com.see.truetransact.transferobject.customer.CustomerAddressTO;
//import com.see.truetransact.transferobject.customer.CustomerPhoneTO;
//import com.see.truetransact.transferobject.customer.CustFinanceDetailsTO;
//import com.see.truetransact.transferobject.customer.CustomerGaurdianTO;
//import com.see.truetransact.transferobject.customer.CustomerPassPortTO;
//import com.see.truetransact.transferobject.customer.CustomerIncomeParticularsTO;
//import com.see.truetransact.transferobject.customer.CustomerLandDetailsTO;
//import com.see.truetransact.transferobject.customer.CustomerSuspensionTO;

import com.see.truetransact.transferobject.customer.gahan.GahanCustomerDetailsTO;
import com.see.truetransact.transferobject.customer.gahan.GahanDocumentDetailsTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
//import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.deposit.CommonRB;
//import com.see.truetransact.uicomponent.COptionPane;
//import com.see.truetransact.ui.customer.gahan.GahanCustomerRB;
//import java.io.File;
//import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import java.util.Date;

import org.apache.log4j.Logger;

//import java.io.OutputStream;
//import java.io.InputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.sql.*;
import java.util.Date;
import java.util.*;
import oracle.sql.*;
//import oracle.jdbc.driver.OracleResultSet;
//import com.see.truetransact.commonutil.Dummy;
//import java.util.Set;

/**
 *
 * @author  administrator
 * Modified by Karthik
 * Modified by Annamalai
 * @modified by JK
 */
public class GahanCustomerOB extends CObservable{
    //    JointAcctHolderManipulation objJointAcctHolderManipulation  = new JointAcctHolderManipulation();
    CommonRB objCommRB = new CommonRB();
    
    //--- Declarations for Joint Account Table
    private ProxyFactory proxy;
    HashMap map =null;
    private int actionType;
    private int result;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final   java.util.ResourceBundle objGahanCustomerRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.customer.gahan.GahanCustomerRB", ProxyParameters.LANGUAGE);
    private final static Logger log = Logger.getLogger(GahanCustomerOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private Date curDate = null;
    private ComboBoxModel cbmConstitution;
    private ComboBoxModel cbmNature;
    private ComboBoxModel cbmPledge;
    private ComboBoxModel cbmRight;
    private ComboBoxModel cbmDocumentType;
    private ComboBoxModel cbmVillage;
    private ComboBoxModel cbmDocumentsSubmitted;
    
    private EnhancedTableModel gahanTable ;
    private EnhancedTableModel tblLoansAvailedAgainstSecurity;
    private EnhancedTableModel tblGahanLandDetails ;
    private ArrayList gahanCustomerTableTitle;
    private ArrayList gahanLandTitle;
    private ArrayList gahanLoansAgainstSecurityTitle;
    private String cboDocumentType="";
    private String tdtDocumentDt="";
    private String txtRegisteredOffice="";
    private String tdtPledgeDate="";
    private String tdtGahanExpDate ="";
    private boolean gahanReleaseYes=false;
    private boolean gahanReleaseNo=false;
    private String cboNature="";
    private String cboPledge="";
    private String cboRight="";
    private String tdtGahanReleasExpDate="";
    private String txtGahanReleaseNo = "";
    private String txtPledgeNo="";
    private String txtPledgeAmount="";
    private String txtVillage ="";
    private String txtSurveryNo="";
    private String sno="";
    private String txtTotalArea="";
    private String txtARS="";









    public String getTxtARS() {
        return txtARS;
    }

    public void setTxtARS(String txtARS) {
        this.txtARS = txtARS;
    }
    private String lblMemberNameVal="";
    private String lblCityVal="";
    private String lblPlaceVal="";
    private String lblPinCodeVal="";
    private String lblHouseNameVal="";
    private String txtDocumentNo="";
    private String documentGenId="";
    private String txtMemberNo ="";
    private String txtOwnerMemberNumber ="";
    private String no="";
    private String cusid="";
    private String txtOwnerNo="";
    private String txtOwnerNo2="";// Added by nithya on 30-08-2018 for KD 191 - Add in Gahan Customer owner property name one more members
    private List itemsSubmittingList = new ArrayList();

    public List getItemsSubmittingList() {
        return itemsSubmittingList;
    }

    public void setItemsSubmittingList(List itemsSubmittingList) {
        this.itemsSubmittingList = itemsSubmittingList;
    }

    public String getTxtOwnerNo() {
        return txtOwnerNo;
    }

    public void setTxtOwnerNo(String txtOwnerNo) {
        this.txtOwnerNo = txtOwnerNo;
    }

    // Added by nithya on 30-08-2018 for KD 191 - Add in Gahan Customer owner property name one more members
    public String getTxtOwnerNo2() {
        return txtOwnerNo2;
    }

    public void setTxtOwnerNo2(String txtOwnerNo2) {
        this.txtOwnerNo2 = txtOwnerNo2;
    }
    //End
    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
    private String txtReSurveryNo="";
    private String nature="";
    private String right="";
    public String getTxtReSurveryNo() {
        return txtReSurveryNo;
    }

    public void setTxtReSurveryNo(String txtReSurveryNo) {
        this.txtReSurveryNo = txtReSurveryNo;
    }
    private HashMap gahanCustomerMap;
    HashMap changedLand=null;
    private LinkedHashMap gahanDetailsMap;
    private String txtRemarks="";
        List gahanDocDetails=new ArrayList();

    public List getGahanDocDetails() {
        return gahanDocDetails;
    }

    public void setGahanDocDetails(List gahanDocDetails) {
        this.gahanDocDetails = gahanDocDetails;
    }
    /**
     * Creates a new instance of CustomerOB
     * If the parameter is '1' then the customer type is INDIVIDUAL
     * If the parameter is '2' then the customer type is CUSTOMER
     */
    public GahanCustomerOB() {
        
        try{
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "GahanCustomerJNDI");
            map.put(CommonConstants.HOME, "customer.gahan.GahanCustomerHome");
            map.put(CommonConstants.REMOTE, "customer.gahan.GahanCustomer");
            setGhanCustomerTitle();
            setGhanLandTitle();
            setGahanLoansAgainstSecurityTitle();
            gahanTable = new EnhancedTableModel(null, gahanCustomerTableTitle);
            tblGahanLandDetails=new EnhancedTableModel(null,gahanLandTitle);
            tblLoansAvailedAgainstSecurity=new EnhancedTableModel(null,gahanLoansAgainstSecurityTitle);
            fillDropdown();
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }
    
  


    private void setGhanCustomerTitle() throws Exception{
        try {
            gahanCustomerTableTitle=new ArrayList();
            gahanCustomerTableTitle.add(objGahanCustomerRB.getString("tblColumnBorrowerName"));
            gahanCustomerTableTitle.add(objGahanCustomerRB.getString("tblColumnBorrowerCustID"));
            gahanCustomerTableTitle.add(objGahanCustomerRB.getString("tblColumnBorrowerType"));
            gahanCustomerTableTitle.add(objGahanCustomerRB.getString("tblColumnBorrowerMain/Joint"));
        }catch(Exception e) {
            log.info("Exception in setSanctionFacilityTitle: "+e);
            parseException.logException(e,true);
        }
    }
    private void setGahanLoansAgainstSecurityTitle()throws Exception{
        try{
            gahanLoansAgainstSecurityTitle=new ArrayList();
            gahanLoansAgainstSecurityTitle.add(objGahanCustomerRB.getString("tblCollumnLoanAccoountNo"));
            gahanLoansAgainstSecurityTitle.add(objGahanCustomerRB.getString("tblCollumnProductId"));
            gahanLoansAgainstSecurityTitle.add(objGahanCustomerRB.getString("tblCollumnCustomerName"));
            gahanLoansAgainstSecurityTitle.add(objGahanCustomerRB.getString("tblCollumnLoanAmount"));
            gahanLoansAgainstSecurityTitle.add(objGahanCustomerRB.getString("tblCollumnPledgeAmount"));
            gahanLoansAgainstSecurityTitle.add(objGahanCustomerRB.getString("tblCollumnExpiryDate"));
            gahanLoansAgainstSecurityTitle.add(objGahanCustomerRB.getString("tblCollumnOutStandingBalance"));
        }catch(Exception e) {
            log.info("Exception in setGahanLoansAgainstSecurityTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    private void setGhanLandTitle() throws Exception{
        try {
            gahanLandTitle=new ArrayList();
            gahanLandTitle.add(objGahanCustomerRB.getString("sno"));
            gahanLandTitle.add(objGahanCustomerRB.getString("lblVillage"));
            gahanLandTitle.add(objGahanCustomerRB.getString("lblSurveryNo"));
            gahanLandTitle.add(objGahanCustomerRB.getString("lblARS"));
            gahanLandTitle.add(objGahanCustomerRB.getString("lblTotalArea"));
            gahanLandTitle.add(objGahanCustomerRB.getString("lblResurvey"));
             gahanLandTitle.add(objGahanCustomerRB.getString("lblNature"));
             gahanLandTitle.add(objGahanCustomerRB.getString("lblRight"));
             gahanLandTitle.add(objGahanCustomerRB.getString("lblOwnerNo"));
             gahanLandTitle.add(objGahanCustomerRB.getString("lblOwnerNo2"));
            
        }catch(Exception e) {
            log.info("Exception in setSanctionFacilityTitle: "+e);
            parseException.logException(e,true);
        }
    }
    
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        param = new HashMap();
        param.put(CommonConstants.MAP_NAME,null);
        
        final ArrayList lookupKey = new ArrayList();
        lookupKey.add("CONSTITUTION");
        lookupKey.add("SECURITY.NATURE");
        lookupKey.add("SECURITY.PLEDGE");
        lookupKey.add("SECURITY.RIGHT");
        lookupKey.add("TERMLOAN.DOCUMENT_TYPE");
        lookupKey.add("TERMLOAN.VILLAGE");
        lookupKey.add("GAHAN.DOCUMENTS_SUBMITTED");
        param.put(CommonConstants.PARAMFORQUERY, lookupKey);
        final HashMap lookupValues = ClientUtil.populateLookupData(param);
        fillData((HashMap)lookupValues.get("CONSTITUTION"));
        setCbmConstitution(new ComboBoxModel(key,value));
        
        fillData((HashMap)lookupValues.get("SECURITY.NATURE"));
        setCbmNature(new ComboBoxModel(key,value));
        
        fillData((HashMap)lookupValues.get("SECURITY.PLEDGE"));
        setCbmPledge(new ComboBoxModel(key,value));
        
        fillData((HashMap)lookupValues.get("SECURITY.RIGHT"));
        setCbmRight(new ComboBoxModel(key,value));
        
        fillData((HashMap)lookupValues.get("TERMLOAN.DOCUMENT_TYPE"));
        setCbmDocumentType(new ComboBoxModel(key,value));
        
        fillData((HashMap)lookupValues.get("TERMLOAN.VILLAGE"));
        setCbmVillage(new ComboBoxModel(key,value));
        
        fillData((HashMap)lookupValues.get("GAHAN.DOCUMENTS_SUBMITTED"));
        setCbmDocumentsSubmitted(new ComboBoxModel(key,value));
    }
    
    
    /** To set the key & value for comboboxes */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    /** To get data for comboboxes */
    private HashMap populateData(HashMap obj)  throws Exception{
        keyValue = proxy.executeQuery(obj,lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }
    
    /** To get the data from server for a particular customer & populate */
    public void getData(HashMap whereMap,IntroducerOB introducerOB) {
        try{
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            HashMap data=new HashMap();
            data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("##### CustomerOB getData() data : "+data);
            String introType = null;
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public ArrayList setGahanaPropertyDetails(){
        ArrayList gahanDocumentList=new ArrayList();
        try{
            System.out.println("tblGahanLandDetails.getRowCount()inOOBBB"+tblGahanLandDetails.getRowCount());
            for(int i=0;i<tblGahanLandDetails.getRowCount();i++){
                GahanDocumentDetailsTO gahanDocumentDetailsTO =new GahanDocumentDetailsTO();
                setSno(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,0)));
                setTxtVillage(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,1)));
                setTxtSurveryNo(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,2)));
                setTxtARS(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 3)));
                setTxtTotalArea(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,4)));
                setTxtReSurveryNo(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,5)));
//               getCbmNature().setKeyForSelected(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,5)));
//               getCbmRight().setKeyForSelected(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,6)));
                System.out.println("cbmRight.getKeyForSelected() "+cbmRight.getKeyForSelected()+"cbmNature.getKeyForSelected() "+cbmNature.getKeyForSelected());
               setNature(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,6)));
               setRight(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,7)));
               setTxtOwnerNo(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 8)));
               setTxtOwnerNo2(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 9)));// Added by nithya on 30-08-2018 for KD 191 - Add in Gahan Customer owner property name one more members
               //  setCboNature(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,5)));
                    //  setTxtTotalArea(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i,6)));
                gahanDocumentDetailsTO.setSno(getSno());
                gahanDocumentDetailsTO.setVillage(CommonUtil.convertObjToStr(getTxtVillage()));
                gahanDocumentDetailsTO.setSurveyNo(CommonUtil.convertObjToStr(getTxtSurveryNo()));
                gahanDocumentDetailsTO.setArs(CommonUtil.convertObjToStr(getTxtARS()));
                gahanDocumentDetailsTO.setTotalArea(CommonUtil.convertObjToStr(getTxtTotalArea()));
                gahanDocumentDetailsTO.setDocumentGenId(getDocumentGenId());
                gahanDocumentDetailsTO.setNature(CommonUtil.convertObjToStr(getNature()));;
                gahanDocumentDetailsTO.setRight(CommonUtil.convertObjToStr(getRight()));
                gahanDocumentDetailsTO.setResurveyNo(CommonUtil.convertObjToStr(getTxtReSurveryNo()));
                gahanDocumentDetailsTO.setOwnerNo(CommonUtil.convertObjToStr(getTxtOwnerNo()));
                gahanDocumentDetailsTO.setOwnerNo2(CommonUtil.convertObjToStr(getTxtOwnerNo2()));// Added by nithya on 30-08-2018 for KD 191 - Add in Gahan Customer owner property name one more members
                System.out.println(" setRight "+ gahanDocumentDetailsTO.getRight()+"nnnn"+gahanDocumentDetailsTO.getNature());
                if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                    gahanDocumentDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                    System.out.println("in insert");
                }
                else if(getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
                    gahanDocumentDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    System.out.println("in modified");
                }else{
                    gahanDocumentDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                    System.out.println("in delete");
                }
                gahanDocumentList.add(gahanDocumentDetailsTO);
                System.out.println("the list is"+ gahanDocumentList);
                //
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
        return  gahanDocumentList;
    }
    public void setGahanaTable(int row){
        gahanTable.removeRow(row);
        
    }
    public void setDeleteCust(String cusid){
        this.cusid=cusid;
    }
    public String getDeleteCust(){
        return cusid;
    }
    public void setGahanaPropertyTable(int row){
        tblGahanLandDetails.removeRow(row);
    }
    public void setRecordNo(String no){
        this.no=no;
    }
    public String getRecordNO(){
        return no;
    }
    public GahanCustomerDetailsTO setGahanCustomerDetails(){
        GahanCustomerDetailsTO objGahanCustomerDetailsTO=new GahanCustomerDetailsTO();
        try{
            objGahanCustomerDetailsTO.setCustId(getTxtMemberNo());
            objGahanCustomerDetailsTO.setOwnerMember(getTxtOwnerMemberNumber());
            objGahanCustomerDetailsTO.setConstitution(CommonUtil.convertObjToStr((getCbmConstitution().getKeyForSelected())));
            //If condition added by Jeffin John on 23/6/2014 for Mantis - 9214
            if(getItemsSubmittingList()!=null && getItemsSubmittingList().size()>0){
                String items = "";
                for(int i = 0; i<getItemsSubmittingList().size()-1 ; i++)
                {
                    items+=getItemsSubmittingList().get(i)+",";
                }
                items+=getItemsSubmittingList().get(getItemsSubmittingList().size()-1);
                objGahanCustomerDetailsTO.setItemsSubmitting(items);
            }
            objGahanCustomerDetailsTO.setDocumentNo(CommonUtil.convertObjToStr(getTxtDocumentNo()));
            objGahanCustomerDetailsTO.setDocumentGenId(documentGenId);
            objGahanCustomerDetailsTO.setCommand(getCommand());
            if(getCommand().equals(CommonConstants.TOSTATUS_INSERT))
                objGahanCustomerDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
            else if(getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
                objGahanCustomerDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
            }else{
                objGahanCustomerDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
            }
            objGahanCustomerDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
            objGahanCustomerDetailsTO.setStatusDt(curDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return objGahanCustomerDetailsTO;
    }
    
    public ArrayList setGahanCustomerJointDetails(){
        ArrayList gahanDocumentList=new ArrayList();
        
        //        if(gahanDetailsMap !=null)
        try{
            int n=gahanTable.getRowCount();
            for(int i=1;i<gahanTable.getRowCount();i++){
                GahanCustomerDetailsTO objGahanCustomerDetailsTO =new GahanCustomerDetailsTO();
                setTxtMemberNo(CommonUtil.convertObjToStr(gahanTable.getValueAt(i,1)));
                objGahanCustomerDetailsTO.setOwnerMember(getTxtOwnerMemberNumber());
                objGahanCustomerDetailsTO.setDocumentNo(CommonUtil.convertObjToStr(getTxtDocumentNo()));
                objGahanCustomerDetailsTO.setDocumentGenId(documentGenId);
                
                objGahanCustomerDetailsTO.setConstitution(CommonUtil.convertObjToStr(gahanTable.getValueAt(i,2)));
                objGahanCustomerDetailsTO.setCustId(CommonUtil.convertObjToStr(getTxtMemberNo()));
                objGahanCustomerDetailsTO.setCommand(getCommand());
                if(getCommand().equals(CommonConstants.TOSTATUS_INSERT))
                    objGahanCustomerDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                else if(getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
                    objGahanCustomerDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    
                }else{
                    objGahanCustomerDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                objGahanCustomerDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
                objGahanCustomerDetailsTO.setStatusDt(curDate);
                gahanDocumentList.add(objGahanCustomerDetailsTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return gahanDocumentList;
    }
    
    public GahanDocumentDetailsTO setGahanDocumentDetails(){
        GahanDocumentDetailsTO objGahanDocumentDetailsTO =new GahanDocumentDetailsTO();
        try{
//            objGahanDocumentDetailsTO.setDocumentNo(CommonUtil.convertObjToStr(getTxtDocumentNo()));
//            objGahanDocumentDetailsTO.setDocumentDt(getProperDateFormat(getTdtDocumentDt()));
//            objGahanDocumentDetailsTO.setDocumentType(CommonUtil.convertObjToStr(cbmDocumentType.getKeyForSelected()));
            objGahanDocumentDetailsTO.setNature(CommonUtil.convertObjToStr(cbmNature.getKeyForSelected()));
            objGahanDocumentDetailsTO.setRight(CommonUtil.convertObjToStr(cbmRight.getKeyForSelected()));
            objGahanDocumentDetailsTO.setPledge(CommonUtil.convertObjToStr(cbmPledge.getKeyForSelected()));
            if (objGahanDocumentDetailsTO.getPledge() != null && objGahanDocumentDetailsTO.getPledge().equalsIgnoreCase("Gehan")) {
                objGahanDocumentDetailsTO.setGahanYesNo("Y");
            } else {
                objGahanDocumentDetailsTO.setGahanYesNo("N");
            }
            objGahanDocumentDetailsTO.setPledgeAmt(CommonUtil.convertObjToDouble(getTxtPledgeAmount()));
            objGahanDocumentDetailsTO.setPledgeDt(getProperDateFormat(getTdtPledgeDate()));
            objGahanDocumentDetailsTO.setPledgeNo(CommonUtil.convertObjToStr(getTxtPledgeNo()));
//            objGahanDocumentDetailsTO.setRegistredOffice(CommonUtil.convertObjToStr(getTxtRegisteredOffice()));
            objGahanDocumentDetailsTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
            objGahanDocumentDetailsTO.setBranchCode(TrueTransactMain.BRANCH_ID);
            objGahanDocumentDetailsTO.setRemarks(CommonUtil.convertObjToStr(getTxtRemarks()));
            objGahanDocumentDetailsTO.setOwnerNo(CommonUtil.convertObjToStr(getTxtOwnerNo()));
            
            
            objGahanDocumentDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
            objGahanDocumentDetailsTO.setStatusDt(curDate);
            
            objGahanDocumentDetailsTO.setTdtGahanExpDate(getProperDateFormat(getTdtGahanExpDate()));
            if(isGahanReleaseYes()==true){
                objGahanDocumentDetailsTO.setGahanRelease("Y");
            }else{
                objGahanDocumentDetailsTO.setGahanRelease("N");
            }
            objGahanDocumentDetailsTO.setTdtGahanReleasExpDate(getProperDateFormat(getTdtGahanReleasExpDate()));
            objGahanDocumentDetailsTO.setGahanReleaseNo(getTxtGahanReleaseNo());
            objGahanDocumentDetailsTO.setCommand(getCommand());
            objGahanDocumentDetailsTO.setDocumentGenId(getDocumentGenId());
            System.out.println("command is"+getCommand());
            if(getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                objGahanDocumentDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
                System.out.println("in insert");
            }
            else if(getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
                objGahanDocumentDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
                System.out.println("in modified");
            }else{
                objGahanDocumentDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
                System.out.println("in delete");
            }
        }
        
        catch(Exception e){
            e.printStackTrace();
        }
        return objGahanDocumentDetailsTO;
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt=(Date)curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    
    /** To do the necessary action */
    public HashMap doAction(){
        HashMap proxyResultMap = new HashMap();
        HashMap data=new HashMap();
        try{
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                switch(actionType) {
                    case ClientConstants.ACTIONTYPE_NEW:
                        break;
                    case ClientConstants.ACTIONTYPE_EDIT:
                        break;
                    case ClientConstants.ACTIONTYPE_DELETE:
                        break;
                    default:
                        // throw new ActionNotFoundException();
                }
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                String constitution = CommonUtil.convertObjToStr(getCbmConstitution().getKeyForSelected());
                data.put("COMMAND",getCommand());
                data.put("GahanDocumentDetailsTO",setGahanDocumentDetails());
                data.put("GahanDocDetails",getGahanDocDetails());
                data.put("GahanCustomerDetails",setGahanCustomerDetails());
                if(constitution.equalsIgnoreCase("JOINT_ACCOUNT")){
                    data.put("GahanCustomerJointDetails",setGahanCustomerJointDetails());
                }if(getRecordNO().length()>0){
                    data.put("RECORDNO",getRecordNO());
                }if(getDeleteCust().length()>0){
                    data.put("CUSID",getDeleteCust());
                }
                data.put("GahanaPropertyDetails",setGahanaPropertyDetails());
                
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                System.out.println("datamap is"+data);
                proxyResultMap = proxy.execute(data, map);
                setProxyReturnMap(proxyResultMap);
                setResult(actionType);
                gahanTable.setDataArrayList(null,gahanCustomerTableTitle);
                tblGahanLandDetails.setDataArrayList(null, gahanLandTitle);
                tblLoansAvailedAgainstSecurity.setDataArrayList(null,gahanLoansAgainstSecurityTitle);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
        return proxyResultMap;
    }
    public void populatePropOB(HashMap hmap){
        System.out.println("inside populate propOB");
        setTxtSurveryNo(CommonUtil.convertObjToStr(hmap.get("SARVEY_NO")));
        setTxtARS(CommonUtil.convertObjToStr(hmap.get("ARS")));
        setTxtTotalArea(CommonUtil.convertObjToStr(hmap.get("TOTAL_AREA")));
        setTxtVillage(CommonUtil.convertObjToStr(hmap.get("VILLAGE")));
         setTxtReSurveryNo(CommonUtil.convertObjToStr(hmap.get("RESARVEY_NO")));
         //setTxtOwnerNo(CommonUtil.convertObjToStr(hmap.get("")));
//         setNature(CommonUtil.convertObjToStr(hmap.get("NATURE")));
//         setRight(CommonUtil.convertObjToStr(hmap.get("RIGHT")));
          getCbmNature().setKeyForSelected(CommonUtil.convertObjToStr(hmap.get("NATURE")));
               getCbmRight().setKeyForSelected(CommonUtil.convertObjToStr(hmap.get("RIGHT")));
               
        ttNotifyObservers();
    }
    
    public void populateDocOB(HashMap hmap){
            System.out.println("inside populate docOB");
//        setTxtDocumentNo(CommonUtil.convertObjToStr(hmap.get("DOCUMENT_NO")));
//        setCboDocumentType(CommonUtil.convertObjToStr(hmap.get("DOCUMENT_TYPE")));
//        setTdtDocumentDt(CommonUtil.convertObjToStr(hmap.get("DOCUMENT_DT")));
        setTxtPledgeNo(CommonUtil.convertObjToStr(hmap.get("PLEDGE_NO")));
        
        setTdtPledgeDate(CommonUtil.convertObjToStr(hmap.get("PLEDGE_DT")));
        setTxtPledgeAmount(CommonUtil.convertObjToStr(hmap.get("PLEDGE_AMT")));
        setTxtRemarks(CommonUtil.convertObjToStr(hmap.get("REMARKS")));
        
//        setTxtRegisteredOffice(CommonUtil.convertObjToStr(hmap.get("REGISTRED_OFFICE")));
        setTdtGahanExpDate(CommonUtil.convertObjToStr(hmap.get("GAHAN_EXP_DT")));
        if(CommonUtil.convertObjToStr( hmap.get("GAHAN_RELEASE")).equalsIgnoreCase("Y")){
            setGahanReleaseYes(true);
        }else{
            setGahanReleaseNo(true);
           
        }
        setTdtGahanReleasExpDate(CommonUtil.convertObjToStr(hmap.get("GAHAN_RELEASE_DT")));
        setTxtGahanReleaseNo(CommonUtil.convertObjToStr(hmap.get("GAHAN_RELEASE_NO")));
        getCbmNature().setKeyForSelected(CommonUtil.convertObjToStr(hmap.get("NATURE")));
        getCbmRight().setKeyForSelected(CommonUtil.convertObjToStr(hmap.get("RIGHT")));
        getCbmPledge().setKeyForSelected(CommonUtil.convertObjToStr(hmap.get("PLEDGE")));
        getCbmDocumentType().setKeyForSelected(CommonUtil.convertObjToStr(hmap.get("DOCUMENT_TYPE")));
        ttNotifyObservers();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    public void setChangeLandDetails(HashMap hmap, int n){
        String no=CommonUtil.convertObjToStr(hmap.get("SNO"));
        String village= CommonUtil.convertObjToStr(hmap.get("VILLAGE"));
        String servno= CommonUtil.convertObjToStr(hmap.get("SARVEYNO"));
        String ars=CommonUtil.convertObjToStr(hmap.get("ARS"));
        String area=CommonUtil.convertObjToStr(hmap.get("AREA"));
        String reservno= CommonUtil.convertObjToStr(hmap.get("RESARVEYNO"));
        String nature= CommonUtil.convertObjToStr(hmap.get("NATURE"));
        String right= CommonUtil.convertObjToStr(hmap.get("RIGHT"));
        
        String ownerNo= CommonUtil.convertObjToStr(hmap.get("OWNERNO"));
        String ownerNo2= CommonUtil.convertObjToStr(hmap.get("OWNERNO2"));
        tblGahanLandDetails.setValueAt(no, n,0);
        tblGahanLandDetails.setValueAt(village, n, 1);
        tblGahanLandDetails.setValueAt(servno, n, 2);
        tblGahanLandDetails.setValueAt(ars, n, 3);
        tblGahanLandDetails.setValueAt(area, n, 4);
        tblGahanLandDetails.setValueAt(reservno, n, 5);
        tblGahanLandDetails.setValueAt(nature, n, 6);
        tblGahanLandDetails.setValueAt(right, n, 7);
        tblGahanLandDetails.setValueAt(ownerNo, n, 8);
        tblGahanLandDetails.setValueAt(ownerNo2, n, 9);
        changedLand=new HashMap();
        String s="UPDATE";
        changedLand.put("UPDATE",s);
        
        ttNotifyObservers();
    }
    public void setLandDetail(HashMap hmap){
        
        ArrayList singleList=new ArrayList();
        ArrayList customerTotList=(ArrayList)tblGahanLandDetails.getDataArrayList();
        singleList=new ArrayList();
        
        singleList.add(hmap.get("SNO"));
        singleList.add(hmap.get("VILLAGE"));
        singleList.add(hmap.get("SARVEYNO"));
        singleList.add(hmap.get("ARS"));
        singleList.add(hmap.get("AREA"));
        singleList.add(hmap.get("RESARVEYNO"));
        singleList.add(hmap.get("NATURE"));
        singleList.add(hmap.get("RIGHT"));
        singleList.add(hmap.get("OWNERNO"));
        singleList.add(hmap.get("OWNERNO2"));
        customerTotList.add(singleList);
        tblGahanLandDetails.setDataArrayList(customerTotList, gahanLandTitle);
        ttNotifyObservers();
        
    }
    public void setMain(int row){
        
        int totrow=gahanTable.getRowCount();
        System.out.println("tot row is" +totrow);
        int mainrow=0;
        String main=CommonUtil.convertObjToStr(gahanTable.getValueAt(row,3));
        String m="MAIN";
        String joint="JOINT";
        ArrayList list=null;
        String name="";
        String custid="";
        String  type="";
        String mainjoint="";
        String Name="";
        String Custid="";
        String Type="";
        String MainJoint="";
        
        ArrayList customerTotList=(ArrayList)gahanTable.getDataArrayList();
        //if (main.equals(m)){
        for(int i=0;i<totrow;i++){
            if(i==0){
                list=new ArrayList();
                //                gahanTable.setValueAt(m, row, 3);
                mainrow=gahanTable.getRowCount();
                name=CommonUtil.convertObjToStr(gahanTable.getValueAt(row, 0));
                custid=CommonUtil.convertObjToStr(gahanTable.getValueAt(row, 1));
                type=CommonUtil.convertObjToStr(gahanTable.getValueAt(row, 2));
                mainjoint=CommonUtil.convertObjToStr(gahanTable.getValueAt(row, 3));
                Name=CommonUtil.convertObjToStr(gahanTable.getValueAt(0, 0));
                Custid=CommonUtil.convertObjToStr(gahanTable.getValueAt(0, 1));
                Type=CommonUtil.convertObjToStr(gahanTable.getValueAt(0, 2));
                MainJoint=CommonUtil.convertObjToStr(gahanTable.getValueAt(0, 3));
                gahanTable.setValueAt(name ,0,0);
                gahanTable.setValueAt(custid,0, 1);
                gahanTable.setValueAt(type, 0, 2);
                gahanTable.setValueAt(m, 0, 3);
                gahanTable.setValueAt(Name,row,0);
                gahanTable.setValueAt(Custid,row,1);
                gahanTable.setValueAt(Type,row,2);
                gahanTable.setValueAt(joint,row,3);
            }
        }
    }
    
    public void setJointCustomerId(HashMap hash){
        String cust_id=CommonUtil.convertObjToStr(hash.get("CUST_ID"));
        ArrayList singleList=new ArrayList();
        ArrayList customerTotList=(ArrayList)gahanTable.getDataArrayList();
        if(customerTotList !=null && customerTotList.size()>0){
            for(int i=0;i<customerTotList.size();i++){
                singleList=(ArrayList)customerTotList.get(i);
                if(cust_id.equals(CommonUtil.convertObjToStr(singleList.get(1)))) {
                    ClientUtil.displayAlert("Customer Id Already Exists in Table");
                    return ;
                }
            }
        }
        singleList=new ArrayList();
        singleList.add(hash.get("NAME"));
        singleList.add(hash.get("CUST_ID"));
        singleList.add(hash.get("CUST_TYPE"));
        if(customerTotList!=null && customerTotList.size()==0)
            singleList.add("MAIN");
        else
            singleList.add("JOINT");
        customerTotList.add(singleList);
        gahanTable.setDataArrayList(customerTotList, gahanCustomerTableTitle);
        ttNotifyObservers();
    }
    public void setLoanSecurityTableData(HashMap hash){
        ArrayList singleList;
        ArrayList loanSecurityList=null;
        String genid=CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID"));
        String Yes="Y";
        HashMap hmap=new HashMap();
        hmap.put("GAHAN_YESNO",Yes);
        hmap.put("DOCUMENT_GEN_ID",genid);
        List list= ClientUtil.executeQuery("getSelectLoanScurityDetails" , hmap);
        hmap=null;
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                singleList=new ArrayList();
                loanSecurityList=(ArrayList)tblLoansAvailedAgainstSecurity.getDataArrayList();
                hmap=(HashMap)list.get(i);
                singleList.add(hmap.get("ACCT_NUM"));
                singleList.add(hmap.get("PRODUCT_ID"));
                singleList.add(hmap.get("MEMBER_NAME"));
                singleList.add(hmap.get("LIMIT"));
                singleList.add(hmap.get("PLEDGE_AMOUNT"));
                singleList.add(hmap.get("TO_DT"));
                singleList.add(hmap.get("LOAN_BALANCE_PRINCIPAL"));
                loanSecurityList.add(singleList);
            }
            tblLoansAvailedAgainstSecurity.setDataArrayList(loanSecurityList, gahanLoansAgainstSecurityTitle);
            ttNotifyObservers();
        }
    }
    
    public void setMainCustTable(HashMap hash){
        ArrayList singleList;
        ArrayList customerTotList=null;
        String genid=CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID"));
        
        List lst=ClientUtil.executeQuery("getMainCustdetailsForTable",hash);
        hash=null;
        if(lst.size()>0){
            for(int i=0;i<lst.size();i++){
                singleList=new ArrayList();
                customerTotList=(ArrayList)gahanTable.getDataArrayList();
                hash=(HashMap)lst.get(i);
                singleList.add(hash.get("NAME"));
                singleList.add(hash.get("CUST_ID"));
                singleList.add(hash.get("CONSTITUTION"));
                singleList.add("MAIN");
                customerTotList.add(singleList);
            }
        }
        gahanTable.setDataArrayList(customerTotList, gahanCustomerTableTitle);
        ttNotifyObservers();
    }
    
    public void setTableData(HashMap hash){
        ArrayList singleList;
        ArrayList customerTotList=null;
        String genid=CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID"));
        
        List lst=ClientUtil.executeQuery("getCustdetailsForTable",hash);
        hash=null;
        if(lst.size()>0){
            for(int i=0;i<lst.size();i++){
                singleList=new ArrayList();
                customerTotList=(ArrayList)gahanTable.getDataArrayList();
                hash=(HashMap)lst.get(i);
                singleList.add(hash.get("NAME"));
                singleList.add(hash.get("CUST_ID"));
                singleList.add(hash.get("CONSTITUTION"));
                singleList.add("JOINT");
                customerTotList.add(singleList);
            }
        }
        gahanTable.setDataArrayList(customerTotList, gahanCustomerTableTitle);
        ttNotifyObservers();
    }
    public void setPropertyTableData(List hash){
        ArrayList singleList=new ArrayList();
        ArrayList customerTotList=(ArrayList)tblGahanLandDetails.getDataArrayList();
        HashMap map=new HashMap();
        for(int i=0;i<hash.size();i++){
            singleList=new ArrayList();
            map=(HashMap)hash.get(i);
            singleList.add(map.get("RECORD_NO"));
            singleList.add(map.get("VILLAGE"));
            singleList.add(map.get("SARVEY_NO"));
            singleList.add(map.get("ARS"));
            singleList.add(map.get("TOTAL_AREA"));
            singleList.add(map.get("RESARVEY_NO"));
            singleList.add(map.get("NATURE"));
            singleList.add(map.get("RIGHT"));
            singleList.add(map.get("OWNER_NO"));
            singleList.add(map.get("OWNER_NO2"));
            customerTotList.add(singleList);
        }
        tblGahanLandDetails.setDataArrayList(customerTotList,  gahanLandTitle);
        ttNotifyObservers();
    }
    public void resetFields(){
        txtMemberNo="";
        txtOwnerMemberNumber="";
        txtDocumentNo="";
        cboDocumentType="";
        tdtDocumentDt="";
        txtRegisteredOffice="";
        tdtPledgeDate="";
        txtPledgeNo="";
        txtPledgeAmount="";
        txtVillage="";
        txtSurveryNo="";
        txtARS="";
        txtTotalArea="";
        txtRemarks="";
        lblMemberNameVal="";
        lblHouseNameVal="";
        lblCityVal="";
        lblPinCodeVal="";
        lblPlaceVal="";
        tdtGahanExpDate="";
        gahanReleaseYes=false;
        gahanReleaseNo=false;
        tdtGahanReleasExpDate="";
        txtGahanReleaseNo = "";
        gahanTable.setDataArrayList(null,gahanCustomerTableTitle);
        tblLoansAvailedAgainstSecurity.setDataArrayList(null,gahanLoansAgainstSecurityTitle);
        tblGahanLandDetails.setDataArrayList(null, gahanLandTitle);
        gahanDetailsMap=null;
    }
    
    /* To get the command type according to the Action */
    private String getCommand() throws Exception{
        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_RENEW:
                command = CommonConstants.TOSTATUS_RENEW;
                break;
            default:
        }
        return command;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }
    
    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Getter for property cbmConstitution.
     * @return Value of property cbmConstitution.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmConstitution() {
        return cbmConstitution;
    }
    
    /**
     * Setter for property cbmConstitution.
     * @param cbmConstitution New value of property cbmConstitution.
     */
    public void setCbmConstitution(com.see.truetransact.clientutil.ComboBoxModel cbmConstitution ) {
        this.cbmConstitution = cbmConstitution;
    }
    
    /**
     * Getter for property cbmNature.
     * @return Value of property cbmNature.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNature() {
        return cbmNature;
    }
    
    /**
     * Setter for property cbmNature.
     * @param cbmNature New value of property cbmNature.
     */
    public void setCbmNature(com.see.truetransact.clientutil.ComboBoxModel cbmNature) {
        this.cbmNature = cbmNature;
    }
    
    /**
     * Getter for property cbmPledge.
     * @return Value of property cbmPledge.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPledge() {
        return cbmPledge;
    }
    
    /**
     * Setter for property cbmPledge.
     * @param cbmPledge New value of property cbmPledge.
     */
    public void setCbmPledge(com.see.truetransact.clientutil.ComboBoxModel cbmPledge) {
        this.cbmPledge = cbmPledge;
    }
    
    /**
     * Getter for property cbmRight.
     * @return Value of property cbmRight.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRight() {
        return cbmRight;
    }
    
    /**
     * Setter for property cbmRight.
     * @param cbmRight New value of property cbmRight.
     */
    public void setCbmRight(com.see.truetransact.clientutil.ComboBoxModel cbmRight) {
        this.cbmRight = cbmRight;
    }
    
    /**
     * Getter for property gahanTable.
     * @return Value of property gahanTable.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getGahanTable() {
        return gahanTable;
    }
    
    /**
     * Setter for property gahanTable.
     * @param gahanTable New value of property gahanTable.
     */
    public void setGahanTable(com.see.truetransact.clientutil.EnhancedTableModel gahanTable) {
        this.gahanTable = gahanTable;
    }
    
    public com.see.truetransact.clientutil.EnhancedTableModel getLoansAvailedAgainstSecurity() {
        return tblLoansAvailedAgainstSecurity;
    }
    
    /**
     * Setter for property gahanTable.
     * @param gahanTable New value of property gahanTable.
     */
    public void setLoansAvailedAgainstSecurity(com.see.truetransact.clientutil.EnhancedTableModel loansAvailedAgainstSecurity) {
        this.tblLoansAvailedAgainstSecurity = tblLoansAvailedAgainstSecurity;
    }
    
    
    
    
    public com.see.truetransact.clientutil.EnhancedTableModel getTblGahanLandDetails() {
        return tblGahanLandDetails;
    }
    
    /**
     * Setter for property gahanTable.
     * @param gahanTable New value of property gahanTable.
     */
    public void setTblGahanLandDetails(com.see.truetransact.clientutil.EnhancedTableModel tblGahanLandDetails){
        this.tblGahanLandDetails = tblGahanLandDetails;
    }
    
    /**
     * Getter for property txtDocumentType.
     * @return Value of property txtDocumentType.
     */
    public java.lang.String getCboDocumentType() {
        return cboDocumentType;
    }
    
    /**
     * Setter for property txtDocumentType.
     * @param txtDocumentType New value of property txtDocumentType.
     */
    public void setCboDocumentType(java.lang.String cboDocumentType) {
        this.cboDocumentType = cboDocumentType;
    }
    
    public java.lang.String getCboPledge() {
        return cboPledge;
    }
    
    /**
     * Setter for property txtDocumentType.
     * @param txtDocumentType New value of property txtDocumentType.
     */
    public void setCboPledge(java.lang.String cboPledge) {
        this.cboPledge = cboPledge;
    }
    
    /**
     * Getter for property tdtDocumentDt.
     * @return Value of property tdtDocumentDt.
     */
    public java.lang.String getTdtDocumentDt() {
        return tdtDocumentDt;
    }
    
    /**
     * Setter for property tdtDocumentDt.
     * @param tdtDocumentDt New value of property tdtDocumentDt.
     */
    public void setTdtDocumentDt(java.lang.String tdtDocumentDt) {
        this.tdtDocumentDt = tdtDocumentDt;
    }
    
    /**
     * Getter for property txtRegisteredOffice.
     * @return Value of property txtRegisteredOffice.
     */
    public java.lang.String getTxtRegisteredOffice() {
        return txtRegisteredOffice;
    }
    
    /**
     * Setter for property txtRegisteredOffice.
     * @param txtRegisteredOffice New value of property txtRegisteredOffice.
     */
    public void setTxtRegisteredOffice(java.lang.String txtRegisteredOffice) {
        this.txtRegisteredOffice = txtRegisteredOffice;
    }
    
    /**
     * Getter for property tdtPledgeDate.
     * @return Value of property tdtPledgeDate.
     */
    public java.lang.String getTdtPledgeDate() {
        return tdtPledgeDate;
    }
    
    public void  setTdtGahanExpDate(java.lang.String tdtGahanExpDate) {
        this. tdtGahanExpDate =  tdtGahanExpDate;
    }
    
    /**
     * Getter for property tdtPledgeDate.
     * @return Value of property tdtPledgeDate.
     */
    public java.lang.String getTdtGahanExpDate() {
        return  tdtGahanExpDate;
    }
    
    public void  setGahanReleaseYes(boolean gahanReleaseYes) {
        this.gahanReleaseYes=  gahanReleaseYes;
    }
    
    /**
     * Getter for property tdtPledgeDate.
     * @return Value of property tdtPledgeDate.
     */
    public boolean isGahanReleaseYes() {
        return  gahanReleaseYes;
    }
    
    public void  setGahanReleaseNo(boolean gahanReleaseNo) {
        this.gahanReleaseNo=  gahanReleaseNo;
    }
    
    /**
     * Getter for property tdtPledgeDate.
     * @return Value of property tdtPledgeDate.
     */
    public boolean isGahanReleaseNo() {
        return  gahanReleaseNo;
    }
    
    /**
     * Setter for property tdtPledgeDate.
     * @param tdtPledgeDate New value of property tdtPledgeDate.
     */
    public void setTdtPledgeDate(java.lang.String tdtPledgeDate) {
        this.tdtPledgeDate = tdtPledgeDate;
    }
    
    /**
     * Getter for property txtPledgeNo.
     * @return Value of property txtPledgeNo.
     */
    public java.lang.String getTxtPledgeNo() {
        return txtPledgeNo;
    }
    
    /**
     * Setter for property txtPledgeNo.
     * @param txtPledgeNo New value of property txtPledgeNo.
     */
    public void setTxtPledgeNo(java.lang.String txtPledgeNo) {
        this.txtPledgeNo = txtPledgeNo;
    }
    
    /**
     * Getter for property txtPledgeAmount.
     * @return Value of property txtPledgeAmount.
     */
    public java.lang.String getTxtPledgeAmount() {
        return txtPledgeAmount;
    }
    
    /**
     * Setter for property txtPledgeAmount.
     * @param txtPledgeAmount New value of property txtPledgeAmount.
     */
    public void setTxtPledgeAmount(java.lang.String txtPledgeAmount) {
        this.txtPledgeAmount = txtPledgeAmount;
    }
    
    /**
     * Getter for property txtVillage.
     * @return Value of property txtVillage.
     */
    public java.lang.String getTxtVillage() {
        return txtVillage;
    }
    
    /**
     * Setter for property txtVillage.
     * @param txtVillage New value of property txtVillage.
     */
    public void setTxtVillage(java.lang.String txtVillage) {
        this.txtVillage = txtVillage;
    }
    
    /**
     * Getter for property txtSurveryNo.
     * @return Value of property txtSurveryNo.
     */
    public java.lang.String getTxtSurveryNo() {
        return txtSurveryNo;
    }
    
    public java.lang.String getSno() {
        return sno;
    }
    
    
    public void setSno(java.lang.String sno) {
        this.sno =sno;
    }
    
    /**
     * Setter for property txtSurveryNo.
     * @param txtSurveryNo New value of property txtSurveryNo.
     */
    public void setTxtSurveryNo(java.lang.String txtSurveryNo) {
        this.txtSurveryNo = txtSurveryNo;
    }
    
    /**
     * Getter for property txtTotalArea.
     * @return Value of property txtTotalArea.
     */
    public java.lang.String getTxtTotalArea() {
        return txtTotalArea;
    }
    
    /**
     * Setter for property txtTotalArea.
     * @param txtTotalArea New value of property txtTotalArea.
     */
    public void setTxtTotalArea(java.lang.String txtTotalArea) {
        this.txtTotalArea = txtTotalArea;
    }
    
    /**
     * Getter for property txtDocumentNo.
     * @return Value of property txtDocumentNo.
     */
    public java.lang.String getTxtDocumentNo() {
        return txtDocumentNo;
    }
    
    /**
     * Setter for property txtDocumentNo.
     * @param txtDocumentNo New value of property txtDocumentNo.
     */
    public void setTxtDocumentNo(java.lang.String txtDocumentNo) {
        this.txtDocumentNo = txtDocumentNo;
    }

    public String getTxtGahanReleaseNo() {
        return txtGahanReleaseNo;
    }

    public void setTxtGahanReleaseNo(String txtGahanReleaseNo) {
        this.txtGahanReleaseNo = txtGahanReleaseNo;
    }
    
    
    public java.lang.String getTdtGahanReleasExpDate() {
        return tdtGahanReleasExpDate;
    }
    
    /**
     * Setter for property txtDocumentNo.
     * @param txtDocumentNo New value of property txtDocumentNo.
     */
    public void setTdtGahanReleasExpDate(java.lang.String tdtGahanReleasExpDate) {
        this.tdtGahanReleasExpDate =tdtGahanReleasExpDate;
    }
    
    /**
     * Getter for property txtRemarks.
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }
    
    /**
     * Setter for property txtRemarks.
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }
    
    /**
     * Getter for property documentGenId.
     * @return Value of property documentGenId.
     */
    public java.lang.String getDocumentGenId() {
        return documentGenId;
    }
    
    /**
     * Setter for property documentGenId.
     * @param documentGenId New value of property documentGenId.
     */
    public void setDocumentGenId(java.lang.String documentGenId) {
        this.documentGenId = documentGenId;
    }
    
    
    public java.lang.String getCboNature() {
        return cboNature;
    }
    
    /**
     * Setter for property documentGenId.
     * @param documentGenId New value of property documentGenId.
     */
    public void setCboNature(java.lang.String cboNature) {
        this.cboNature = cboNature;
    }
    
    public java.lang.String getCboRight() {
        return cboRight;
    }
    
    /**
     * Setter for property documentGenId.
     * @param documentGenId New value of property documentGenId.
     */
    public void setCboRight(java.lang.String cboRight) {
        this.cboRight = cboRight;
    }
    /**
     * Getter for property txtMemberNo.
     * @return Value of property txtMemberNo.
     */
    public java.lang.String getTxtMemberNo() {
        return txtMemberNo;
    }
    
    /**
     * Setter for property txtMemberNo.
     * @param txtMemberNo New value of property txtMemberNo.
     */
    public void setTxtMemberNo(java.lang.String txtMemberNo) {
        this.txtMemberNo = txtMemberNo;
        setChanged();
    }
    
    /**
     * Getter for property gahanDetailsMap.
     * @return Value of property gahanDetailsMap.
     */
    public java.util.LinkedHashMap getGahanDetailsMap() {
        return gahanDetailsMap;
    }
    
    /**
     * Setter for property gahanDetailsMap.
     * @param gahanDetailsMap New value of property gahanDetailsMap.
     */
    public void setGahanDetailsMap(java.util.LinkedHashMap gahanDetailsMap) {
        this.gahanDetailsMap = gahanDetailsMap;
    }
    
    /**
     * Getter for property txtOwnerMemberNumber.
     * @return Value of property txtOwnerMemberNumber.
     */
    public java.lang.String getTxtOwnerMemberNumber() {
        return txtOwnerMemberNumber;
    }
    
    /**
     * Setter for property txtOwnerMemberNumber.
     * @param txtOwnerMemberNumber New value of property txtOwnerMemberNumber.
     */
    public void setTxtOwnerMemberNumber(java.lang.String txtOwnerMemberNumber) {
        this.txtOwnerMemberNumber = txtOwnerMemberNumber;
    }
    
    /**
     * Getter for property cbmDocumentsSubmitted.
     * @return Value of property cbmDocumentsSubmitted.
     */

    public com.see.truetransact.clientutil.ComboBoxModel getCbmDocumentsSubmitted() {
        return cbmDocumentsSubmitted;
    }

    public void setCbmDocumentsSubmitted(com.see.truetransact.clientutil.ComboBoxModel cbmDocumentsSubmitted) {
        this.cbmDocumentsSubmitted = cbmDocumentsSubmitted;
    }
    
    /**
     * Getter for property cbmDocumentType.
     * @return Value of property cbmDocumentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDocumentType() {
        return cbmDocumentType;
    }
    
    /**
     * Setter for property cbmDocumentType.
     * @param cbmDocumentType New value of property cbmDocumentType.
     */
    public void setCbmDocumentType(com.see.truetransact.clientutil.ComboBoxModel cbmDocumentType) {
        this.cbmDocumentType = cbmDocumentType;
    }
    
    /**
     * Getter for property cbmVillage.
     * @return Value of property cbmVillage.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmVillage() {
        return cbmVillage;
    }
    
    /**
     * Setter for property cbmVillage.
     * @param cbmVillage New value of property cbmVillage.
     */
    public void setCbmVillage(com.see.truetransact.clientutil.ComboBoxModel cbmVillage) {
        this.cbmVillage = cbmVillage;
    }
    
}
//
