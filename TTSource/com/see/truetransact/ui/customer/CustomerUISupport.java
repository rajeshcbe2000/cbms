/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerUISupport.java
 *
 * Created on April 2, 2004, 3:51 PM
 */

package com.see.truetransact.ui.customer;

import javax.swing.JFileChooser;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.CButton;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.ImageFileFilter;
import java.io.FileInputStream;
import java.io.File;
import javax.swing.ImageIcon;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import java.net.URL;
import javax.swing.JComponent;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientexception.ClientParseException;

/**
 *
 * @author  amathan
 */
public class CustomerUISupport {
    private final static ClientParseException parseException = ClientParseException.getInstance();
//    private final CustomerRB objCustomerRB = new CustomerRB();
    private java.util.ResourceBundle objCustomerRB = null;
    CustomerOB observable;
    private final String PHOTO = "lblPhoto";
    private final String SIGN = "lblSign";
    private final String PROOF_PHOTO = "lblProofPhoto";
    private int photoAction = 0;   // action=1  if New,  action=0  if Edit
    private int signAction = 0;
    private int fileSelected = 0;
    
    /** Creates a new instance of CustomerUI */
    public CustomerUISupport() {
    }
    
    public CustomerUISupport(CustomerOB observable) {
        this.observable = observable;
    }
    
    /**
     * Activities needed for loading an jpeg or gif file
     */
    public void loadActivities(CLabel argLabel,CButton btnRemove){
        final JFileChooser objJFileChooser = new JFileChooser();
        objJFileChooser.setAccessory(new com.see.truetransact.clientutil.ImagePreview(objJFileChooser));
        final ImageFileFilter objImageFileFilter = new ImageFileFilter();
        final File selFile;
        byte[] byteArray;
        StringBuffer filePath;
        String fileName;
        
        objJFileChooser.setFileFilter(objImageFileFilter);
        objJFileChooser.removeChoosableFileFilter(objJFileChooser.getAcceptAllFileFilter());
        if (objJFileChooser.showOpenDialog(null) == objJFileChooser.APPROVE_OPTION){
            setFileSelected(1);
            selFile = objJFileChooser.getSelectedFile();
            filePath = new StringBuffer(selFile.getAbsolutePath());
            try{
                argLabel.setIcon(new ImageIcon(CommonUtil.convertObjToStr(filePath)));
//                if (observable.getScreen().equals("Corporate"))
//                    fileName = filePath.toString();
//                else
                    fileName = filePath.substring(filePath.lastIndexOf("."));                
                final FileInputStream reader = new FileInputStream(selFile);
                final int size = reader.available();
                byteArray = new byte[size];
                reader.read(byteArray);
                reader.close();                
                if (argLabel.getName().equals(PHOTO)){
                    observable.setPhotoFile(fileName);
                    observable.setPhotoByteArray(byteArray);
                }else if (argLabel.getName().equals(SIGN)){
                    observable.setSignFile(fileName);
                    observable.setSignByteArray(byteArray);
                }else if(argLabel.getName().equals(PROOF_PHOTO)){ //28-11-2020
                    observable.setProofPhotoFile(fileName);
                    observable.setProofPhotoByteArray(byteArray);
                }
                btnRemove.setEnabled(true);
                byteArray = null;
            } catch (Exception e) {
                parseException.logException(e,true);
            }
        }
        else setFileSelected(0);
    }
    
    /**
     * Returns true if atleast one contact is there and the main address type is set 
     * and false otherwise
     */
    public boolean chkMinAddrTypeCommAddr(CTable tblContactList){
        boolean commAddr = true;
        boolean minAddrType = true;
        if (tblContactList.getRowCount() == 0){
            minAddrType = false;
            displayAlert(objCustomerRB.getString("minAddrType"));
        } else if(tblContactList.getRowCount() == 1){
            observable.setCommAddrType(CommonUtil.convertObjToStr(tblContactList.getValueAt(0,0)));
        }else if (observable.getCommAddrType().length() == 0 ){
            commAddr = false;
            displayAlert(objCustomerRB.getString("commAddrAlert"));
        }
        return (commAddr && minAddrType);
    }
     public boolean chkIncomeParticulars(CTable tblIncParticulars){
          boolean inc = false;
        if (tblIncParticulars.getRowCount() == 0){
            displayAlert(objCustomerRB.getString("IncomePar"));
            inc = true;
    }
          return inc;
     }
     
      public boolean chkLandDetails(CTable tblCustomerLandDetails){
          boolean land = false;
        if (tblCustomerLandDetails.getRowCount() == 0){
            displayAlert(objCustomerRB.getString("LandPar"));
            land = true;
    }
          return land;
     }
    
    public void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /**
     * Fills the image on Photo and Sign label(s) (if the image is there) and enables the Remove button(s) aptly
     */
    public void fillPhotoSign(CLabel lblPhoto,CLabel lblSign,CButton btnPhotoRemove,CButton btnSignRemove){
//        if (observable.getPhotoFile() != null && observable.getPhotoFile().length() > 0 ){
        if (observable.getPhotoFile() != null){
            fillImage(lblPhoto, btnPhotoRemove);
        }
//        if (observable.getSignFile() != null && observable.getSignFile().length() > 0 ){
        if (observable.getSignFile() != null){
            fillImage(lblSign, btnSignRemove);
        }
    }
    
    /**
     * Called if the image is present to fill it in Label and enable the corresponding the Remove button
     */
    public void fillImage(CLabel lblImage,CButton btnRemove){
        StringBuffer fileName = new StringBuffer(ClientConstants.SERVER_ROOT);
        int url = 0;
        if (lblImage.getName().equals(PHOTO)){
            if (getPhotoAction()==0) {
                fileName.append("customer/photos/").append(observable.getPhotoFile());
                lblImage.setIcon(new ImageIcon(observable.getPhotoFile()));
                url = 1; 
            }    
//            else 
//                fileName = new StringBuffer(observable.getPhotoFile());
        }else if (lblImage.getName().equals(SIGN)){
            if (getSignAction()==0) {
                fileName.append("customer/signatures/").append(observable.getSignFile());
                lblImage.setIcon(new ImageIcon(observable.getSignFile()));
                url = 1; 
            }    
//            else
//                fileName = new StringBuffer(observable.getSignFile());
        }else if (lblImage.getName().equals(PROOF_PHOTO)){
            if (getSignAction()==0) {
                fileName.append("customer/proof/").append(observable.getProofPhotoFile());
                lblImage.setIcon(new ImageIcon(observable.getProofPhotoFile()));
                url = 1; 
            }    
//            else
//                fileName = new StringBuffer(observable.getSignFile());
        }
//        try{
//            if (url==1)
//                lblImage.setIcon(new ImageIcon(new URL(CommonUtil.convertObjToStr(fileName))));
//            else
//                lblImage.setIcon(new ImageIcon(CommonUtil.convertObjToStr(fileName)));
//        }catch(Exception e){
//            parseException.logException(e,true);
//        }
        if (btnRemove!=null)
            btnRemove.setEnabled(true);
    }
    
    /**
     * Called if the image is present to fill it in Label and enable the corresponding the Remove button
     */
    public void zoomImage(CLabel lblImage){
            javax.swing.JWindow photoFrame = new javax.swing.JWindow();
            javax.swing.JLabel photoLabel = new javax.swing.JLabel();
            photoLabel.setIcon(lblImage.getIcon());
            photoFrame.getContentPane().add(photoLabel);
            photoFrame.setSize(lblImage.getIcon().getIconWidth(), lblImage.getIcon().getIconHeight());
            photoFrame.addMouseListener(new MyMouseListener(photoFrame));
            com.see.truetransact.ui.TrueTransactMain.showWindow(photoFrame);        
    }

    class MyMouseListener implements java.awt.event.MouseListener {
        javax.swing.JWindow imageWindow = null;
        public MyMouseListener(javax.swing.JWindow imageWindow) {
            this.imageWindow = imageWindow;
        }
        
        public void mouseClicked(java.awt.event.MouseEvent e) {
            imageWindow.setVisible(false);
        }
        
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }
        
        public void mouseExited(java.awt.event.MouseEvent e) {
        }
        
        public void mousePressed(java.awt.event.MouseEvent e) {
        }
        
        public void mouseReleased(java.awt.event.MouseEvent e) {
        }
        
    }
    
    /** To check mandatoryness of a specific component, mostly a Panel */
    public String checkMandatory(String className,JComponent component){
        return new MandatoryCheck().checkMandatory(className, component);
    }
    
    /** To check mandatoryness of a specific component, mostly a Panel */
    public String putMandatoryMarks(String className,JComponent component){
        return new MandatoryCheck().putMandatoryMarks(className, component);
    }
    
    public void setContactButtonEnableDisable(CButton btnContactNew, CButton btnContactDelete, CButton btnContactToMain){
        btnContactNew.setEnabled(true);
        btnContactDelete.setEnabled(false);
        btnContactToMain.setEnabled(false);
    }
     public void setProofAddEnableDisable(boolean proof,CButton btnProofAdd){
       btnProofAdd.setEnabled(proof);
    }
    public void setProofButtonEnableDisable(CButton btnProofNew, CButton btnProofAdd, CButton btnProofDelete){
        btnProofNew.setEnabled(true);
        btnProofAdd.setEnabled(false);
        btnProofDelete.setEnabled(false);
    }
    
     public void setProofButtonEnableDisableDefault(boolean enableDisable,CButton btnProofNew, CButton btnProofDelete, CButton btnProofAdd){
        btnProofNew.setEnabled(enableDisable);
        btnProofDelete.setEnabled(enableDisable);
        btnProofAdd.setEnabled(enableDisable);
    }
    
    
    public void setContactButtonEnableDisableDefault(boolean enableDisable,CButton btnContactNew, CButton btnContactDelete, CButton btnContactToMain){
        btnContactNew.setEnabled(enableDisable);
        btnContactDelete.setEnabled(enableDisable);
        btnContactToMain.setEnabled(enableDisable);
    }
    
    public void setContactAddEnableDisable(boolean contact,CButton btnContactAdd){
        btnContactAdd.setEnabled(contact);
    }
    
    public void setPhoneButtonEnableDisable(boolean enableDisable,CButton btnPhoneNew, CButton btnContactNoAdd, CButton btnPhoneDelete){
        btnPhoneNew.setEnabled(enableDisable);
        btnContactNoAdd.setEnabled(enableDisable);
        btnPhoneDelete.setEnabled(enableDisable);
    }
     public void setIncomeButtonEnableDisable(boolean enableDisable,CButton btnIncNew, CButton btnIncDelete, CButton btnIncSave){
        btnIncNew.setEnabled(enableDisable);
        btnIncDelete.setEnabled(enableDisable);
        btnIncSave.setEnabled(enableDisable);
    }
      public void setLandDetailsButtonEnableDisable(boolean enableDisable,CButton btnLandNew, CButton btnLandDelete, CButton btnLandSave){
        btnLandNew.setEnabled(enableDisable);
        btnLandDelete.setEnabled(enableDisable);
        btnLandSave.setEnabled(enableDisable);
    }
    
    public void setPhoneButtonEnableDisableNew(CButton btnPhoneNew, CButton btnContactNoAdd, CButton btnPhoneDelete){
        btnPhoneNew.setEnabled(true);
        btnContactNoAdd.setEnabled(true);
        btnPhoneDelete.setEnabled(false);
    }
    
    public void setPhoneButtonEnableDisableDefault(CButton btnPhoneNew, CButton btnContactNoAdd, CButton btnPhoneDelete){
        btnPhoneNew.setEnabled(true);
        btnContactNoAdd.setEnabled(false);
        btnPhoneDelete.setEnabled(false);
    }
    
    public void setPhotoSignEnableDisableDefault(CButton btnPhotoLoad, CButton btnSignLoad, CButton btnPhotoRemove, CButton btnSignRemove){
        setPhotoSignLoadEnableDisable(false,btnPhotoLoad, btnSignLoad);
        btnPhotoRemove.setEnabled(false);
        btnSignRemove.setEnabled(false);
    }
    
    public void setPhotoSignLoadEnableDisable(boolean photoSignLoad, CButton btnPhotoLoad, CButton btnSignLoad){
        btnPhotoLoad.setEnabled(photoSignLoad);
        btnSignLoad.setEnabled(photoSignLoad);
    }
    
    public void setLblPhotoSignDefault(CLabel lblPhoto,CLabel lblSign){
        lblPhoto.setIcon(null);
        lblSign.setIcon(null);
    }
    
    /**
     * Getter for property photoAction.
     * @return Value of property photoAction.
     */
    public int getPhotoAction() {
        return photoAction;
    }    

    /**
     * Setter for property photoAction.
     * @param photoAction New value of property photoAction.
     */
    public void setPhotoAction(int photoAction) {
        this.photoAction = photoAction;
    }    
    
    /**
     * Getter for property signAction.
     * @return Value of property signAction.
     */
    public int getSignAction() {
        return signAction;
    }
    
    /**
     * Setter for property signAction.
     * @param signAction New value of property signAction.
     */
    public void setSignAction(int signAction) {
        this.signAction = signAction;
    }

    /**
     * Getter for property fileSelected.
     * @return Value of property fileSelected.
     */
    public int getFileSelected() {
        return fileSelected;
    }
    
    /**
     * Setter for property fileSelected.
     * @param fileSelected New value of property fileSelected.
     */
    public void setFileSelected(int fileSelected) {
        this.fileSelected = fileSelected;
    }
    
    /**
     * Getter for property objCustomerRB.
     * @return Value of property objCustomerRB.
     */
    public java.util.ResourceBundle getObjCustomerRB() {
        return objCustomerRB;
    }
    
    /**
     * Setter for property objCustomerRB.
     * @param objCustomerRB New value of property objCustomerRB.
     */
    public void setObjCustomerRB(java.util.ResourceBundle objCustomerRB) {
        this.objCustomerRB = objCustomerRB;
    }
    
    public void fillProofPhoto(CLabel lblPhoto,CButton btnPhotoRemove){
//        if (observable.getPhotoFile() != null && observable.getPhotoFile().length() > 0 ){
        if (observable.getProofPhotoByteArray() != null){
            fillImage(lblPhoto, btnPhotoRemove);
        }
    }
    
}
