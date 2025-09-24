/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewPhotoSignOB.java
 *
 * Created on August 10, 2004, 4:21 PM
 */

package com.see.truetransact.ui.common.viewphotosign;

import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.ImageIcon;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.commonutil.CommonConstants;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.*;
import org.apache.log4j.Logger;
/**
 *
 * @author  shanmuga
 */
public class ViewPhotoSignOB extends Observable {
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(ViewPhotoSignOB.class);
//    private           static ViewPhotoSignOB objviewPhotoSignOB;
    private boolean isAvailable = true;
    private TableModel tableModel;
    private CTable tblData;
    private final String PHOTO = "lblPhoto";
    private final String SIGN = "lblSign";
    private final String PHOTO_FILE = "PHOTO_FILE";
    private final String SIGNATURE_FILE = "SIGNATURE_FILE";
    
    private byte[] photoFile;
    private byte[] signFile;
    private ProxyFactory proxy;
    private String actNum="";
    
    /** Creates a new instance of ViewPhotoSignOB */
    public ViewPhotoSignOB() {
    }
    
//    static {
//        try {
//            objviewPhotoSignOB = new ViewPhotoSignOB();
//        } catch(Exception e) {
//            System.out.println("try: " + e);
//            e.printStackTrace();
//        }
//    }
    
//    public static ViewPhotoSignOB getInstance() {
//        return objviewPhotoSignOB;
//    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setTable(CTable tbl) {
        tblData = tbl;
    }
    
    public void populateData(HashMap mapID, CTable tblData){
        try{
            this.tblData = tblData;
            isAvailable = ClientUtil.setTableModel(mapID, tblData);
            if (tblData.getModel() instanceof TableSorter) {
                tableModel = ((TableSorter) tblData.getModel()).getModel();
            } else {
                tableModel = (TableModel) tblData.getModel();
            }
        }catch(Exception e){
            System.out.println("Exception caught in populateData:  " + e);
        }
    }
    
    public void populatePhotoSign(int row, String prodType){
        try{
            if (row > -1){
                HashMap transactionMap = new HashMap();
                String strCustID = CommonUtil.convertObjToStr(((ArrayList) tableModel.getDataArrayList().get(row)).get(0));
                transactionMap.put(CommonConstants.MAP_WHERE, strCustID);
                transactionMap.put("PHOTOSIGNONLY", null);
                transactionMap.put("ACT_NUM", getActNum());
                if(prodType.equalsIgnoreCase("LOCKER") || prodType.equalsIgnoreCase("NewActOpening")){
                  transactionMap.put("PHOTO_SIGN_LOCKER_OPERATION", "PHOTO_SIGN_LOCKER_OPERATION");  
                }
                HashMap map;
                HashMap result = new HashMap();
                try{
                    proxy = ProxyFactory.createProxy();
                    map = new HashMap();
                    map.put(CommonConstants.JNDI, "CustomerJNDI");
                    map.put(CommonConstants.HOME, "customer.CustomerHome");
                    map.put(CommonConstants.REMOTE, "customer.Customer");
                    result = proxy.executeQuery(transactionMap, map);
                    if (result!=null)
                        if(result.size()>0) {
                            transactionMap = (HashMap) result.get("PHOTOSIGN");
                            if(transactionMap!=null)
                                if(transactionMap.size()>0) {
                                    setPhotoFile((byte[])transactionMap.get("PHOTO"));
                                    setSignFile((byte[])transactionMap.get("SIGN"));
                                }
                        }
                }catch(Exception e){
                    parseException.logException(e,true);
                }                
//                List resultList = ClientUtil.executeQuery("getCustPhotoSignFile", transactionMap);
//                if (resultList.size() > 0){
//                    // If atleast one Record exist
//                    HashMap resultMap = (HashMap) resultList.get(0);
//                    setPhotoFile(CommonUtil.convertObjToStr(resultMap.get(PHOTO_FILE)));
//                    setSignFile(CommonUtil.convertObjToStr(resultMap.get(SIGNATURE_FILE)));
//                }
                transactionMap = null;
                strCustID = null;
            }
        }catch(Exception e){
            System.out.println("Exception caught in populatePhotoSign: ."+e);
        }
    }
    
    /**
     * Fills the image on Photo and Sign label(s) (if the image is there) and enables the Remove button(s) aptly
     */
    public void fillPhotoSign(CLabel lblPhoto,CLabel lblSign){
        if (getPhotoFile() != null && getPhotoFile().length > 0 ){
            fillImage(lblPhoto);
        }
        if (getSignFile() != null && getSignFile().length > 0 ){
            fillImage(lblSign);
        }
    }
    
    /**
     * Called if the image is present to fill it in Label and enable the corresponding the Remove button
     */
    public void fillImage(CLabel lblImage){
//        final StringBuffer fileName = new StringBuffer(ClientConstants.SERVER_ROOT);
//        if (lblImage.getName().equals(PHOTO)){
//            fileName.append("customer/photos/").append(getPhotoFile());
//        }else if (lblImage.getName().equals(SIGN)){
//            fileName.append("customer/signatures/").append(getSignFile());
//        }
//        try{
//            lblImage.setIcon(new ImageIcon(new URL(CommonUtil.convertObjToStr(fileName))));
//        }catch(Exception e){
//            System.out.println("Exception caught in fillImage: ."+e);
//        }
        if (lblImage.getName().equals(PHOTO)){
            lblImage.setIcon(new ImageIcon(getPhotoFile()));
        }else if (lblImage.getName().equals(SIGN)){
            lblImage.setIcon(new ImageIcon(getSignFile()));
        }
    }
    
//    public String getPhotoFile(){
//        return this.photoFile;
//    }
//    
//    public void setPhotoFile(String photoFile) {
//        this.photoFile = photoFile;
//        setChanged();
//    }
//    
//    public String getSignFile(){
//        return this.signFile;
//    }
//    
//    public void setSignFile(String signFile) {
//        this.signFile = signFile;
//        setChanged();
//    }
    
    public byte[] getPhotoFile(){
        return this.photoFile;
    }
    
    public void setPhotoFile(byte[] photoFile) {
        this.photoFile = photoFile;
        setChanged();
    }
    
    public byte[] getSignFile(){
        return this.signFile;
    }
    
    public void setSignFile(byte[] signFile) {
        this.signFile = signFile;
        setChanged();
    }
    
    /**
     * Getter for property tableModel.
     * @return Value of property tableModel.
     */
    public com.see.truetransact.clientutil.TableModel getTableModel() {
        return tableModel;
    }
    
    /**
     * Setter for property tableModel.
     * @param tableModel New value of property tableModel.
     */
    public void setTableModel(com.see.truetransact.clientutil.TableModel tableModel) {
        this.tableModel = tableModel;
    }
    
    /**
     * Getter for property actNum.
     * @return Value of property actNum.
     */
    public java.lang.String getActNum() {
        return actNum;
    }
    
    /**
     * Setter for property actNum.
     * @param actNum New value of property actNum.
     */
    public void setActNum(java.lang.String actNum) {
        this.actNum = actNum;
    }
    
}
