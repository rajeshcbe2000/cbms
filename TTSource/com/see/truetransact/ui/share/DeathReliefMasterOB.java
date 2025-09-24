/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterOB.java
 *
 * Created on Fri Aug 05 13:20:23 GMT+05:30 2011
 */

package com.see.truetransact.ui.share;

import java.util.Observable;
import com.see.truetransact.transferobject.share.DeathReliefMasterTO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.*;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.TableUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.deposit.CommonRB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import com.see.truetransact.ui.share.*;
import java.util.Date;

import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
import com.see.truetransact.commonutil.Dummy;
import java.util.Set;

/**
 *
 * @author
 */

public class DeathReliefMasterOB extends CObservable{
    
    private String txtDrfAmount = "";
    private String txtPaymentAmount = "";
    private String tdtDrfFromDt = "";
    private String tdtDrfToDt = "";
    private String txtActHeadName = "";
    private String txtProductId = "";
    private String txtDrfName = "";
    private String txtPaymentHeadName = "";
    private int result=0;
    private boolean newDrfMaster = false;
    private HashMap drfMasterMap=new LinkedHashMap();
    private final static Logger log = Logger.getLogger(DeathReliefMasterOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    HashMap map= new HashMap();
    private DeathReliefMasterTO objDeathReliefMasterTO = new DeathReliefMasterTO();
    private DeathReliefMasterTO objDrfMasterTO = new DeathReliefMasterTO();
    private Date curDate = null;
    private ProxyFactory proxy;
    private int screenCustType;
    HashMap lookupMap = new HashMap();
    HashMap param = new HashMap();
    HashMap data = new HashMap();
    private ArrayList key;
    private ArrayList value;
    private HashMap keyValue = new HashMap();
    private final int ADDRTYPE_COLNO = 0;
    private int actionType=0;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private EnhancedTableModel tblDrfProdDesc;
    private String drfSlNo = "";
    HashMap deletedDrfMasterMap = new LinkedHashMap();
    public boolean drfTypeExists = false;
     //////added by me////////////////
    private String txtRecoveryHead="";
    private String rdAmountRecovery="";
    private String txtRecoveryAmount="";
    
    private String cboCalculationFrequency="";
    private String cboCalculationCriteria="";
    private String cboProductFrequency="";
    private String txtDebitHead="";
    private String tdtCalculatedDt="";
    private String tdtFromDt="";
    private String txtToDt="";
    private String txtInterestRate="";
    private String InterestID="";
    private EnhancedTableModel tblDrfProdDesc1;
    //Added By Revathi.L
    private boolean nominee = false;
    private String noNominee = "";
    //////added by me////////////////
    private DeathReliefMasterRB resourceBundle = new DeathReliefMasterRB();
    private List buffer=new ArrayList();
    public DeathReliefMasterOB(int param) {
        screenCustType = param;
        try{
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DeathReliefMasterJNDI");
            map.put(CommonConstants.HOME, "serverside.share.DeathReliefMasterHome");
            map.put(CommonConstants.REMOTE, "serverside.share.DeathReliefMaster");
            createDrfTable();
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public DeathReliefMasterOB()
    {
        
    }
    
    
    private void createDrfTable() throws Exception{
        final ArrayList drfMasterColumn = new ArrayList();
        drfMasterColumn.add("Sl No.");
        drfMasterColumn.add("Recovery Enabled ");
        drfMasterColumn.add("Recovery Amount");
        drfMasterColumn.add("From Date");
        drfMasterColumn.add("Amount");
        drfMasterColumn.add("Payment Amt");
        tblDrfProdDesc = new EnhancedTableModel(null, drfMasterColumn);
    }
    
    public void resetDrfMaster(){
        setTxtRecoveryHead("");
        setRdAmountRecovery("");
        setTxtRecoveryAmount("");
        setTdtDrfFromDt("");
        setTdtDrfToDt("");
        setTxtActHeadName("");
        setTxtDrfAmount("");
        setTxtDrfName("");
        setTxtPaymentAmount("");
        setTxtPaymentHeadName("");
        setTxtProductId("");
        setDrfSlNo("");
        
        setCboCalculationFrequency("");
        setCboCalculationCriteria("");
        setCboProductFrequency("");
        setTxtDebitHead("");
        setTdtCalculatedDt("");
        setTdtFromDt("");
        setTxtToDt("");
        setTxtInterestRate("");
        //Added By Revathi.L
        setNoNominee("");
        setNominee(false);
       // setBuffer(null);
        buffer.clear();
    }
    
    public void resetDrfDetails(){
        setRdAmountRecovery("");
        setTxtRecoveryAmount("");
        setTdtDrfFromDt("");
        setTdtDrfToDt("");
        setTxtDrfAmount("");
        setTxtPaymentAmount("");
        setDrfSlNo("");
    }
    public void deleteDrfMaster(int row){
        if(deletedDrfMasterMap == null){
            deletedDrfMasterMap= new LinkedHashMap();
        }
        DeathReliefMasterTO objDeathReliefMasterTO = (DeathReliefMasterTO)drfMasterMap.get(CommonUtil.convertObjToStr(tblDrfProdDesc.getValueAt(row,ADDRTYPE_COLNO)));
        System.out.println("IN OB DELETE DRF..."+objDeathReliefMasterTO.getProdId());
        objDeathReliefMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        objDeathReliefMasterTO.setStatusDate(curDate);
        objDeathReliefMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        deletedDrfMasterMap.put(CommonUtil.convertObjToStr(tblDrfProdDesc.getValueAt(row,ADDRTYPE_COLNO)),drfMasterMap.get(CommonUtil.convertObjToStr(tblDrfProdDesc.getValueAt(row,ADDRTYPE_COLNO))));
        drfMasterMap.remove(tblDrfProdDesc.getValueAt(row,ADDRTYPE_COLNO));
        resetDeleteDrfMaster();
    }
    
    public void populateDrfMaster(int row){
        try{
            drfMasterChanged(CommonUtil.convertObjToStr(tblDrfProdDesc.getValueAt(row,ADDRTYPE_COLNO)));
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void drfMasterChanged(String selectedItem){
        try{
            
            drfTypeExists = true;
            final DeathReliefMasterTO objDeathReliefMasterTO = (DeathReliefMasterTO)drfMasterMap.get(selectedItem);
            populateDrfMasterData(objDeathReliefMasterTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    public void authorizeDrfMaster(HashMap singleAuthorizeMap) {
        try{
            singleAuthorizeMap.put("AUTH_DATA","AUTH_DATA");
            proxy.executeQuery(singleAuthorizeMap,map);
        }
        catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    private void populateDrfMasterData(DeathReliefMasterTO objDeathReliefMasterTO)  throws Exception{
        try{
            if(objDeathReliefMasterTO != null){
                setDrfSlNo(objDeathReliefMasterTO.getDrfSlNo());
                setRdAmountRecovery(objDeathReliefMasterTO.getRdAmountRecovery());
                setTxtRecoveryAmount(objDeathReliefMasterTO.getRecoveryAmount());
//                setTxtDrfName(objDeathReliefMasterTO.getProdName());
//                setTxtProductId(objDeathReliefMasterTO.getProdId());
//                setTxtActHeadName(objDeathReliefMasterTO.getDrfAchd());
//                setTxtPaymentHeadName(objDeathReliefMasterTO.getDrfPaymentAchd());
                setTdtDrfFromDt(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTdtDrfFromDt()));
                setTdtDrfToDt(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTdtDrfToDt()));
                setTxtPaymentAmount(objDeathReliefMasterTO.getTxtPaymentAmount());
                setTxtDrfAmount(objDeathReliefMasterTO.getTxtDrfAmount());
                
               /// setCboCalculationCriteria(
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void resetDeleteDrfMaster(){
        try{
            
            //            resetDrfMaster();
            resetDrfDetails();
            resetDrfListTable();
            populateDrfMasterTable();
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    public void  getData(HashMap whereMap){
        try{
            data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println("@#$@#$@#$@#data : "+data+ " : "+whereMap+ " : "+map);
//            objDrfMasterTO = new DeathReliefMasterTO();
            if(data.containsKey("DRFMASTERTO")){
                DeathReliefMasterTO objDrfMasterTO = (DeathReliefMasterTO)data.get("DRFMASTERTO");
                System.out.println("@#$@#$objDrfMasterTO:"+objDrfMasterTO);
                pouplateDrfMaster(objDrfMasterTO);
            }
            if(data.containsKey("DRFMASTERTO1")){
                DeathReliefMasterTO objDrfMasterTO1 = (DeathReliefMasterTO)data.get("DRFMASTERTO1");
                System.out.println("@#$@#$objDrfMasterTO:"+objDrfMasterTO);
                pouplateDrfMaster1(objDrfMasterTO1);
            }
            if(data.containsKey("DRFPRODUCT")){
                drfMasterMap = (LinkedHashMap)data.get("DRFPRODUCT");
                ArrayList addList =new ArrayList(drfMasterMap.keySet());
                for(int i=0;i<addList.size();i++){
                    DeathReliefMasterTO objDeathReliefMasterTO = (DeathReliefMasterTO) drfMasterMap.get(addList.get(i));
                    System.out.println("$#%#$%#$objDeathReliefMasterTO :"+objDeathReliefMasterTO);
                    objDeathReliefMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objDeathReliefMasterTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDeathReliefMasterTO.setStatusDate(curDate);
                    drfMasterMap.put(objDeathReliefMasterTO.getDrfSlNo(), objDeathReliefMasterTO);
                }
                populateDrfMasterTable();
                 if(data.containsKey("DRFPRODUCT1")){
               // drfMasterMap = (LinkedHashMap)data.get("DRFPRODUCT");
                      
                     List ll=(List)data.get("DRFPRODUCT1");
                     System.out.println("OBBB TAB SIZEEEE110211  "+ll.size());
////                     for(int i=0;i<ll.size();i++)
////                     {
////                         System.out.println("hhhhh999");
////                        //  ArrayList aList =(ArrayList)ll.get(i);
////                         System.out.println("hhhhh888");
////                         // for(int k=0;k<aList.size();k++)
////                          //{
////                              HashMap m=new HashMap();
////                              m.put("FROM",aL.get(0).toString());
////                              m.put("TO",aList.get(1).toString());
////                              m.put("INTEREST",aList.get(2).toString());
////                              buffer.add(m);
////                         // }
////                     }
//                ArrayList addList =new ArrayList(drfMasterMap.keySet());
//                for(int i=0;i<addList.size();i++){
//                    DeathReliefMasterTO objDeathReliefMasterTO = (DeathReliefMasterTO) drfMasterMap.get(addList.get(i));
//                    System.out.println("$#%#$%#$objDeathReliefMasterTO :"+objDeathReliefMasterTO);
//                    objDeathReliefMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                    objDeathReliefMasterTO.setStatusBy(TrueTransactMain.USER_ID);
//                    objDeathReliefMasterTO.setStatusDate(curDate);
//                    drfMasterMap.put(objDeathReliefMasterTO.getDrfSlNo(), objDeathReliefMasterTO);
               setBuffer(ll);
               System.out.println("OBBB TAB SIZEEEE11  "+buffer.size());
                 }
                
            }
        }
        
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    private void pouplateDrfMaster(DeathReliefMasterTO objDrfMasterTO){
        setTxtPaymentHeadName(objDrfMasterTO.getDrfPaymentAchd());
        setTxtDrfName(objDrfMasterTO.getProdName());
        setTxtProductId(objDrfMasterTO.getProdId());
        setTxtActHeadName(objDrfMasterTO.getDrfAchd());  
        setTxtRecoveryHead(objDrfMasterTO.getRecoveryHead());
        if(CommonUtil.convertObjToStr(objDrfMasterTO.getNominee()).equals("Y")){//Added By Revathi.L
            setNominee(true);
            setNoNominee(CommonUtil.convertObjToStr(objDrfMasterTO.getNoOfNominee()));
        }else{
            setNominee(false);
        }
    }
    
     private void pouplateDrfMaster1(DeathReliefMasterTO objDrfMasterTO1){
         System.out.println("Pooppplllatttee mmmasstteerr11");
      setCboCalculationCriteria(objDrfMasterTO1. getCalculationCriteria());
      setCboCalculationFrequency(objDrfMasterTO1.getCalculationFrequency());
      setCboProductFrequency(objDrfMasterTO1.getProductFrequency());
      setTxtDebitHead(objDrfMasterTO1.getDebitHead());  
      setTdtCalculatedDt(CommonUtil.convertObjToStr(objDrfMasterTO1.getLastCalculatedDate()));
      setInterestID(objDrfMasterTO1.getDrfInterestID ());
    }
    private void populateDrfMasterTable()  throws Exception{
        //added from here
        ArrayList academicDataLst = new ArrayList();
        academicDataLst = new ArrayList(drfMasterMap.keySet());
        ArrayList addList =new ArrayList(drfMasterMap.keySet());
        int length = academicDataLst.size();
        for(int i=0; i<length; i++){
            ArrayList acadTabRow = new ArrayList();
            DeathReliefMasterTO objDeathReliefMasterTO = (DeathReliefMasterTO) drfMasterMap.get(addList.get(i));
            acadTabRow = new ArrayList();
            acadTabRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getDrfSlNo()));
            acadTabRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getRdAmountRecovery()));
            acadTabRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getRecoveryAmount()));
            
            acadTabRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTdtDrfFromDt()));
            acadTabRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTxtDrfAmount()));
            acadTabRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTxtPaymentAmount()));
            tblDrfProdDesc.insertRow(tblDrfProdDesc.getRowCount(),acadTabRow);
            System.out.println("POPULAteTABL....");
        }
        
    }
    public void resetDrfListTable(){
        for(int i = tblDrfProdDesc.getRowCount(); i > 0; i--){
            tblDrfProdDesc.removeRow(0);
            System.out.println("reeessetDrfList");
        }
    }
    
    public  int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogYes"), resourceBundle.getString("CDialogNo")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.YES_NO_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    public void  drfMaster(int row,boolean drmMasterFlag){
        try{
            final DeathReliefMasterTO objDeathReliefMasterTO = new DeathReliefMasterTO();
            if( drfMasterMap == null ){
                drfMasterMap = new LinkedHashMap();
            }
            
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewDrfMaster()){
                    objDeathReliefMasterTO.setStatus(CommonConstants.STATUS_CREATED);
                    objDeathReliefMasterTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDeathReliefMasterTO.setStatusDate(curDate);
                }else{
                    objDeathReliefMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objDeathReliefMasterTO.setStatusBy(TrueTransactMain.USER_ID);
                    objDeathReliefMasterTO.setStatusDate(curDate);
                }
            }else{
                objDeathReliefMasterTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            if( actionType == ClientConstants.ACTIONTYPE_EDIT ){
                //                objDeathReliefMasterTO.setTxtEmpId(sysId);
            }
            int slno;
            slno=0;
            
            if(!drmMasterFlag){
                
                ArrayList data = tblDrfProdDesc.getDataArrayList();
                slno=serialNo(data,tblDrfProdDesc);
            }
            else if(isNewDrfMaster()){
                int b=CommonUtil.convertObjToInt(tblDrfProdDesc.getValueAt(row,0));
                slno= b + tblDrfProdDesc.getRowCount();
            }
            else{
                slno = CommonUtil.convertObjToInt(tblDrfProdDesc.getValueAt(row,0));
            }
            objDeathReliefMasterTO.setDrfSlNo(String.valueOf(slno));
            objDrfMasterTO.setDrfSlNo(String.valueOf(slno));
            objDeathReliefMasterTO.setProdId(getTxtProductId());
            objDrfMasterTO.setProdId(getTxtProductId());
            objDeathReliefMasterTO.setDrfAchd(getTxtActHeadName());
            objDrfMasterTO.setDrfAchd(getTxtActHeadName());
            objDeathReliefMasterTO.setDrfPaymentAchd(getTxtPaymentHeadName());
            objDrfMasterTO.setDrfPaymentAchd(getTxtPaymentHeadName());
            objDeathReliefMasterTO.setProdName(getTxtDrfName());
            objDrfMasterTO.setProdName(getTxtDrfName());
            
            objDeathReliefMasterTO.setRecoveryHead(getTxtRecoveryHead());
            objDrfMasterTO.setRecoveryHead(getTxtRecoveryHead());
            
            objDeathReliefMasterTO.setRdAmountRecovery(getRdAmountRecovery());
            //objDrfMasterTO.setRdAmountRecovery(getRdAmountRecovery());
            
            objDeathReliefMasterTO.setRecoveryAmount(getTxtRecoveryAmount());
           // objDrfMasterTO.setRecoveryAmount(getTxtRecoveryAmount());
            
            objDeathReliefMasterTO.setTdtDrfFromDt(DateUtil.getDateMMDDYYYY(getTdtDrfFromDt()));
            objDeathReliefMasterTO.setTdtDrfToDt(DateUtil.getDateMMDDYYYY(getTdtDrfToDt()));
            objDeathReliefMasterTO.setTxtDrfAmount(getTxtDrfAmount());
            objDeathReliefMasterTO.setTxtPaymentAmount(getTxtPaymentAmount());
            objDeathReliefMasterTO.setStatusBy(TrueTransactMain.USER_ID);
            //
            objDeathReliefMasterTO.setRecoveryHead(getTxtRecoveryHead());
            objDeathReliefMasterTO.setRdAmountRecovery(getRdAmountRecovery());
            objDeathReliefMasterTO.setRecoveryAmount(getTxtRecoveryAmount());
            
            drfMasterMap.put(CommonUtil.convertObjToStr(String.valueOf(slno)), objDeathReliefMasterTO);
            updateTblDrfMasterList(row,objDeathReliefMasterTO);
            ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
        }
        
    }
    private void updateTblDrfMasterList(int row,DeathReliefMasterTO objDeathReliefMasterTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        for(int i = tblDrfProdDesc.getRowCount(), j = 0; i > 0; i--,j++){
            selectedRow = ((ArrayList)tblDrfProdDesc.getDataArrayList().get(j)).get(0);
            //   if( (CommonUtil.convertObjToStr(cbmReleationShip.getKeyForSelected())).equals(CommonUtil.convertObjToStr(selectedRow))){
            if(CommonUtil.convertObjToStr(getDrfSlNo()).equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
            }
        }
        ArrayList addressRow = new ArrayList();
        if(row == -1){
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getDrfSlNo()));
            ///
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getRdAmountRecovery()));
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getRecoveryAmount()));
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTdtDrfFromDt()));
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTxtDrfAmount()));
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTxtPaymentAmount()));
            tblDrfProdDesc.insertRow(tblDrfProdDesc.getRowCount(),addressRow);
            addressRow = null;
        }else{
            tblDrfProdDesc.removeRow(row);
            //addressRow.add(CommonUtil.convertObjToStr(getDependentId()));
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getDrfSlNo()));
            ///
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getRdAmountRecovery()));
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getRecoveryAmount()));
            
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTdtDrfFromDt()));
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTxtDrfAmount()));
            addressRow.add(CommonUtil.convertObjToStr(objDeathReliefMasterTO.getTxtPaymentAmount()));
            tblDrfProdDesc.insertRow(row,addressRow);
            addressRow = null;
        }
    }
    public int serialNo(ArrayList data, EnhancedTableModel table_name){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(table_name.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        return slno;
    }
    private void setData() {
        data = new HashMap();
        data.put("DRFMASTERTO",objDrfMasterTO);
        if(drfMasterMap!=null && drfMasterMap.size()>0 )
            data.put("DRFMASTER",drfMasterMap);
        if(deletedDrfMasterMap!=null && deletedDrfMasterMap.size()>0)
          data.put("DELETEDDRFMASTER",deletedDrfMasterMap);
        deletedDrfMasterMap = null;
        drfMasterMap=null;
    }
    private void insertData() throws Exception{
        objDrfMasterTO.setProdId(getTxtProductId());
        objDrfMasterTO.setDrfAchd(getTxtActHeadName());
        objDrfMasterTO.setDrfPaymentAchd(getTxtPaymentHeadName());
        objDrfMasterTO.setProdName(getTxtDrfName());
        
        objDrfMasterTO.setRecoveryHead(getTxtRecoveryHead());
        objDrfMasterTO.setCommand(CommonConstants.TOSTATUS_INSERT);
        objDrfMasterTO.setStatus(CommonConstants.STATUS_CREATED );
        objDrfMasterTO.setStatusDate(curDate);
        objDrfMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        
        objDrfMasterTO.setCalculationFrequency(getCboCalculationFrequency());
        objDrfMasterTO.setCalculationCriteria(getCboCalculationCriteria());
        objDrfMasterTO.setProductFrequency(getCboProductFrequency());
        objDrfMasterTO.setDebitHead(getTxtDebitHead());
        objDrfMasterTO.setLastCalculatedDate(DateUtil.getDateMMDDYYYY(getTdtCalculatedDt()));
        if(nominee==true){//Added By Revathi.L
            objDrfMasterTO.setNominee("Y");
            objDrfMasterTO.setNoOfNominee(CommonUtil.convertObjToInt(noNominee));
        }else{
            objDrfMasterTO.setNominee("N");
            objDrfMasterTO.setNoOfNominee(CommonUtil.convertObjToInt(noNominee));
        }
          //System.out.println("DDDOB1");
        //objDrfMasterTO.setBufferTO(getBuffer());
         // System.out.println("#@#@#@#@#@#@#   objDrfMasterTO : "+objDrfMasterTO);
        setData();
    }
    private void updateData() throws Exception{
        objDrfMasterTO.setProdId(getTxtProductId());
        objDrfMasterTO.setDrfAchd(getTxtActHeadName());
        objDrfMasterTO.setDrfPaymentAchd(getTxtPaymentHeadName());;
        objDrfMasterTO.setProdName(getTxtDrfName());
        objDrfMasterTO.setRecoveryHead(getTxtRecoveryHead());
        objDrfMasterTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
        objDrfMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objDrfMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objDrfMasterTO.setStatusDate(curDate);
      
        
        objDrfMasterTO.setDrfInterestID(getInterestID());
        objDrfMasterTO.setCalculationFrequency(getCboCalculationFrequency());
        System.out.println("OBBBB CBBBBCOOOO  "+objDrfMasterTO.getCalculationFrequency());
        objDrfMasterTO.setCalculationCriteria(getCboCalculationCriteria());
        objDrfMasterTO.setProductFrequency(getCboProductFrequency());
        System.out.println("OBBBB CBBBBCOOOO  "+objDrfMasterTO.getProductFrequency());
        objDrfMasterTO.setDebitHead(getTxtDebitHead());
        objDrfMasterTO.setLastCalculatedDate(DateUtil.getDateMMDDYYYY(getTdtCalculatedDt()));
        if(nominee==true){//Added By Revathi.L
            objDrfMasterTO.setNominee("Y");
            objDrfMasterTO.setNoOfNominee(CommonUtil.convertObjToInt(noNominee));
        }else{
            objDrfMasterTO.setNominee("N");
            objDrfMasterTO.setNoOfNominee(CommonUtil.convertObjToInt(noNominee));
        }
         // System.out.println("DDDOB1dd");
        //objDrfMasterTO.setBufferTO(getBuffer());
         // System.out.println("DDDOB2dd");
        setData();
    }
    private void deleteData() throws Exception{
        objDrfMasterTO.setProdId(getTxtProductId());
        objDrfMasterTO.setDrfAchd(getTxtActHeadName());
        objDrfMasterTO.setDrfPaymentAchd(getTxtPaymentHeadName());
        objDrfMasterTO.setProdName(getTxtDrfName());
          objDrfMasterTO.setRecoveryHead(getTxtRecoveryHead());
        objDrfMasterTO.setCommand(CommonConstants.TOSTATUS_DELETE);
        objDrfMasterTO.setCommand(CommonConstants.TOSTATUS_DELETE);
        objDrfMasterTO.setCommand(CommonConstants.TOSTATUS_DELETE);
        objDrfMasterTO.setStatus(CommonConstants.STATUS_DELETED);
        objDrfMasterTO.setStatusBy(TrueTransactMain.USER_ID);
        objDrfMasterTO.setStatusDate(curDate);
        objDrfMasterTO.setCalculationFrequency(getCboCalculationFrequency());
        objDrfMasterTO.setCalculationCriteria(getCboCalculationCriteria());
        objDrfMasterTO.setProductFrequency(getCboProductFrequency());
        objDrfMasterTO.setDebitHead(getTxtDebitHead());
        objDrfMasterTO.setLastCalculatedDate(DateUtil.getDateMMDDYYYY(getTdtCalculatedDt()));
        //Added By Revathi.L
        if(nominee==true){
          objDrfMasterTO.setNominee("Y");
        }else{
          objDrfMasterTO.setNominee("Y"); 
        }
        objDrfMasterTO.setNoOfNominee(CommonUtil.convertObjToInt(noNominee));
          System.out.println("DDDOB1dd");
        //objDrfMasterTO.setBufferTO(getBuffer());
          System.out.println("DDDOB2dd");
        setData();
    }
    
    public HashMap doAction(){
        HashMap proxyResultMap = new HashMap();
        try{
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                switch(actionType) {
                    case ClientConstants.ACTIONTYPE_NEW:
                        insertData();
                        if(!getBuffer().isEmpty())
                        {
                            data.put("MYLIST",getBuffer());
                        }
                        break;
                    case ClientConstants.ACTIONTYPE_EDIT:
                        updateData();
                        if(!getBuffer().isEmpty())
                        {
                            data.put("MYLIST",getBuffer());
                        }
                        break;
                    case ClientConstants.ACTIONTYPE_DELETE:
                        deleteData();
                         if(!getBuffer().isEmpty())
                        {
                            data.put("MYLIST",getBuffer());
                        }
                        break;
                    default:
                        // throw new ActionNotFoundException();
                }
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                //                data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                proxyResultMap = proxy.execute(data, map);
                //                txtEmpId = CommonUtil.convertObjToStr(proxyResultMap.get("CUST_ID"));
                setProxyReturnMap(proxyResultMap);
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        }catch(Exception e){
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
        return proxyResultMap;
    }
    
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
        setChanged();
        
    }
        /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    public void ttNotifyObservers(){
        notifyObservers();
        //        setChanged();
    }
    // Setter method for txtDrfAmount
    void setTxtDrfAmount(String txtDrfAmount){
        this.txtDrfAmount = txtDrfAmount;
        setChanged();
    }
    // Getter method for txtDrfAmount
    String getTxtDrfAmount(){
        return this.txtDrfAmount;
    }
    
    // Setter method for txtPaymentAmount
    void setTxtPaymentAmount(String txtPaymentAmount){
        this.txtPaymentAmount = txtPaymentAmount;
        setChanged();
    }
    // Getter method for txtPaymentAmount
    String getTxtPaymentAmount(){
        return this.txtPaymentAmount;
    }
    
    // Setter method for tdtDrfFromDt
    void setTdtDrfFromDt(String tdtDrfFromDt){
        this.tdtDrfFromDt = tdtDrfFromDt;
        setChanged();
    }
    // Getter method for tdtDrfFromDt
    String getTdtDrfFromDt(){
        return this.tdtDrfFromDt;
    }
    
    // Setter method for txtActHeadName
    void setTxtActHeadName(String txtActHeadName){
        this.txtActHeadName = txtActHeadName;
        setChanged();
    }
    // Getter method for txtActHeadName
    String getTxtActHeadName(){
        return this.txtActHeadName;
    }
    
    // Setter method for txtProductId
    void setTxtProductId(String txtProductId){
        this.txtProductId = txtProductId;
        setChanged();
    }
    // Getter method for txtProductId
    String getTxtProductId(){
        return this.txtProductId;
    }
    
    // Setter method for txtDrfName
    void setTxtDrfName(String txtDrfName){
        this.txtDrfName = txtDrfName;
        setChanged();
    }
    // Getter method for txtDrfName
    String getTxtDrfName(){
        return this.txtDrfName;
    }
    
    // Setter method for txtPaymentHeadName
    void setTxtPaymentHeadName(String txtPaymentHeadName){
        this.txtPaymentHeadName = txtPaymentHeadName;
        setChanged();
    }
    // Getter method for txtPaymentHeadName
    String getTxtPaymentHeadName(){
        return this.txtPaymentHeadName;
    }
    
    /**
     * Getter for property newDrfMaster.
     * @return Value of property newDrfMaster.
     */
    public boolean isNewDrfMaster() {
        return newDrfMaster;
    }
    
    /**
     * Setter for property newDrfMaster.
     * @param newDrfMaster New value of property newDrfMaster.
     */
    public void setNewDrfMaster(boolean newDrfMaster) {
        this.newDrfMaster = newDrfMaster;
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
     * Getter for property drfSlNo.
     * @return Value of property drfSlNo.
     */
    public java.lang.String getDrfSlNo() {
        return drfSlNo;
    }
    
    /**
     * Setter for property drfSlNo.
     * @param drfSlNo New value of property drfSlNo.
     */
    public void setDrfSlNo(java.lang.String drfSlNo) {
        this.drfSlNo = drfSlNo;
    }
    
    /**
     * Getter for property tblDrfProdDesc.
     * @return Value of property tblDrfProdDesc.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDrfProdDesc() {
        return tblDrfProdDesc;
    }
    
    /**
     * Setter for property tblDrfProdDesc.
     * @param tblDrfProdDesc New value of property tblDrfProdDesc.
     */
    public void setTblDrfProdDesc(com.see.truetransact.clientutil.EnhancedTableModel tblDrfProdDesc) {
        this.tblDrfProdDesc = tblDrfProdDesc;
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
     * Getter for property tdtDrfToDt.
     * @return Value of property tdtDrfToDt.
     */
    public java.lang.String getTdtDrfToDt() {
        return tdtDrfToDt;
    }
    
    /**
     * Setter for property tdtDrfToDt.
     * @param tdtDrfToDt New value of property tdtDrfToDt.
     */
    public void setTdtDrfToDt(java.lang.String tdtDrfToDt) {
        this.tdtDrfToDt = tdtDrfToDt;
    }
    
     public java.lang.String getTxtRecoveryHead() {
        return txtRecoveryHead;
    }
    
    /**
     * Setter for property txtRecoveryHead.
     * @param txtRecoveryHead New value of property txtRecoveryHead.
     */
    public void setTxtRecoveryHead(java.lang.String txtRecoveryHead) {
        this.txtRecoveryHead = txtRecoveryHead;
    }
    
    /**
     * Getter for property rdAmountRecovery.
     * @return Value of property rdAmountRecovery.
     */
    public java.lang.String getRdAmountRecovery() {
        return rdAmountRecovery;
    }
    
    /**
     * Setter for property rdAmountRecovery.
     * @param rdAmountRecovery New value of property rdAmountRecovery.
     */
    public void setRdAmountRecovery(java.lang.String rdAmountRecovery) {
        this.rdAmountRecovery = rdAmountRecovery;
    }
    
    /**
     * Getter for property txtRecoveryAmount.
     * @return Value of property txtRecoveryAmount.
     */
    public java.lang.String getTxtRecoveryAmount() {
        return txtRecoveryAmount;
    }
    
    /**
     * Setter for property txtRecoveryAmount.
     * @param txtRecoveryAmount New value of property txtRecoveryAmount.
     */
    public void setTxtRecoveryAmount(java.lang.String txtRecoveryAmount) {
        this.txtRecoveryAmount = txtRecoveryAmount;
    }
    
    /**
     * Getter for property cboCalculationFrequency.
     * @return Value of property cboCalculationFrequency.
     */
    public String getCboCalculationFrequency() {
        return cboCalculationFrequency;
    }
    
    /**
     * Setter for property cboCalculationFrequency.
     * @param cboCalculationFrequency New value of property cboCalculationFrequency.
     */
    public void setCboCalculationFrequency(String cboCalculationFrequency) {
        this.cboCalculationFrequency = cboCalculationFrequency;
    }
    
    /**
     * Getter for property cboCalculationCriteria.
     * @return Value of property cboCalculationCriteria.
     */
    public String getCboCalculationCriteria() {
        return cboCalculationCriteria;
    }
    
    /**
     * Setter for property cboCalculationCriteria.
     * @param cboCalculationCriteria New value of property cboCalculationCriteria.
     */
    public void setCboCalculationCriteria(String cboCalculationCriteria) {
        this.cboCalculationCriteria = cboCalculationCriteria;
    }
    
    /**
     * Getter for property cboProductFrequency.
     * @return Value of property cboProductFrequency.
     */
    public String getCboProductFrequency() {
        return cboProductFrequency;
    }
    
    /**
     * Setter for property cboProductFrequency.
     * @param cboProductFrequency New value of property cboProductFrequency.
     */
    public void setCboProductFrequency(String cboProductFrequency) {
        this.cboProductFrequency = cboProductFrequency;
    }
    
    /**
     * Getter for property txtDebitHead.
     * @return Value of property txtDebitHead.
     */
    public String getTxtDebitHead() {
        return txtDebitHead;
    }
    
    /**
     * Setter for property txtDebitHead.
     * @param txtDebitHead New value of property txtDebitHead.
     */
    public void setTxtDebitHead(String txtDebitHead) {
        this.txtDebitHead = txtDebitHead;
    }
    
    /**
     * Getter for property tdtCalculatedDt.
     * @return Value of property tdtCalculatedDt.
     */
    public String getTdtCalculatedDt() {
        return tdtCalculatedDt;
    }
    
    /**
     * Setter for property tdtCalculatedDt.
     * @param tdtCalculatedDt New value of property tdtCalculatedDt.
     */
    public void setTdtCalculatedDt(String tdtCalculatedDt) {
        this.tdtCalculatedDt = tdtCalculatedDt;
    }
    
    /**
     * Getter for property tdtFromDt.
     * @return Value of property tdtFromDt.
     */
    public String getTdtFromDt() {
        return tdtFromDt;
    }
    
    /**
     * Setter for property tdtFromDt.
     * @param tdtFromDt New value of property tdtFromDt.
     */
    public void setTdtFromDt(String tdtFromDt) {
        this.tdtFromDt = tdtFromDt;
    }
    
    /**
     * Getter for property txtToDt.
     * @return Value of property txtToDt.
     */
    public String getTxtToDt() {
        return txtToDt;
    }
    
    /**
     * Setter for property txtToDt.
     * @param txtToDt New value of property txtToDt.
     */
    public void setTxtToDt(String txtToDt) {
        this.txtToDt = txtToDt;
    }
    
    /**
     * Getter for property txtInterestRate.
     * @return Value of property txtInterestRate.
     */
    public String getTxtInterestRate() {
        return txtInterestRate;
    }
    
    /**
     * Setter for property txtInterestRate.
     * @param txtInterestRate New value of property txtInterestRate.
     */
    public void setTxtInterestRate(String txtInterestRate) {
        this.txtInterestRate = txtInterestRate;
    }
    
    
      public String  getInterestID() {
        return InterestID;
    }
    
     public void setInterestID(String InterestID) {
        this.InterestID = InterestID;
    }
    
   
    
    
    
    public List getBuffer() {
        return buffer;
    }
    
    /**
     * Setter for property txtInterestRate.
     * @param txtInterestRate New value of property txtInterestRate.
     */
    public void setBuffer(List buffer) {
        this.buffer = buffer;
    }

    public boolean isNominee() {
        return nominee;
    }

    public void setNominee(boolean nominee) {
        this.nominee = nominee;
    }

    public String getNoNominee() {
        return noNominee;
    }

    public void setNoNominee(String noNominee) {
        this.noNominee = noNominee;
    }
    
    
    
    /**
     * Getter for property txtIncrementRate.
     * @return Value of property txtIncrementRate.
     */
//    public String getTxtIncrementRate() {
//        return txtIncrementRate;
//    }
//    
//    /**
//     * Setter for property txtIncrementRate.
//     * @param txtIncrementRate New value of property txtIncrementRate.
//     */
//    public void setTxtIncrementRate(String txtIncrementRate) {
//        this.txtIncrementRate = txtIncrementRate;
//    }
//    
}