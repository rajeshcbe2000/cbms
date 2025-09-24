/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CorporateCustomerUI.java
 *
 * Created on March 22, 2004, 4:31 PM
 */
package com.see.truetransact.ui.customer.gahan;

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
//import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
//import com.see.truetransact.uivalidation.NumericValidation;
//import com.see.truetransact.uivalidation.DefaultValidation;
//import com.see.truetransact.uivalidation.PincodeValidation_IN;
//import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.introducer.*;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.ui.deposit.CommonRB;
//import com.see.truetransact.ui.deposit.CommonMethods;
//import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uimandatory.MandatoryCheck;
//import java.net.URL;
import java.util.ResourceBundle;
//import com.see.truetransact.transferobject.customer.AuthPersonsTO;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.customer.CheckGahanCustomerUI;
import com.see.truetransact.ui.share.DeathReliefMasterOB;
import com.see.truetransact.uivalidation.NumericValidation;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.Date;
/**
 *
 * @author amathan modified by Ashok Viajayakumar
 * @modified JK
 */
public class GahanCustomerUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private GahanCustomerOB observable;
    //    private CustomerUISupport objCustomerUISupport;
    private HashMap mandatoryMap;
//    private HashMap tempMap;
    private HashMap photoSignMap;
    private HashMap totAuthPerMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();
//    private boolean phoneExist;
//    private int phoneRow;
    private final int EDIT_DELETE = 1;
    private final int ENQUIRY = 150;
//    private final int AUTH_CUST_ID = 2;
    private final int JOINT_CUST_ID = 4;
//    private final int SEARCH = 6;
    private final int SHAREID = 6;
    private final int OWNER_ID = 7;
    private final int OWNER_NO = 8;
    private final int OWNER_NO2 = 9;// Added by nithya on 30-08-2018 for KD 191 - Add in Gahan Customer owner property name one more members
    private int viewType;
    final int AUTHORIZE = 3;
    final int REJECT = 0;
    final int DELETE = 1;
    DecimalFormat df = new DecimalFormat("#.##");
    boolean flag = false;
    boolean isFilled = false;
    boolean tabCorpFocused = false;   // Added by Rajesh
    boolean photoCreated = false;     // Added by Rajesh
    boolean signCreated = false;      // Added by Rajesh
    private String mandatoryMessage;
//    private final String CLASSNAME = this.getClass().getName();
    private final String JOINT_ACCOUNT = "JOINT_ACCOUNT";
    static int ct = 0;
    static int hr = 0;
    ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.customer.gahan.GahanCustomerRB", ProxyParameters.LANGUAGE);
    private HashMap bufferMap = new HashMap();
    private List bufferList = new ArrayList();
    private List bufferList1 = new ArrayList();
    private List itemsSubmittingList = null;
    private Object columnNames[] = {"Sl No", "Document No", "Document Type", "Document Date", "Registered Office"};
    private Object columnNames1[] = {"SNo", "Village", "Survey No", "Total Area(In cents)", "Registered Office"};
//    private String search = "";
//    private String CUSTOMERID;//Variable used to get the CustomerId when the popup comes on clicking on Search button in the UI
    //--- Defines the Screen Name for using it in IntroDetials
    final String SCREEN = "CUS";
    private int rejectFlag=0;
    boolean fromAuthorizeUI = false;
    boolean valueSet = false;
    AuthorizeListUI authorizeListUI = null;
    TransactionUI transactionUI = new TransactionUI();
    private Date currDt = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    public GahanCustomerUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartup();
    }

    private void initStartup() {
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        ClientUtil.enableDisable(this, false);
        setMaximumLength();
        setButtonEnableDisable();
        setMandatoryHashMap();
        setMandatoryMarks();
        setHelpMessage();
        btnMember.setEnabled(false);
        btnItemsSubmittingAdd.setEnabled(false);
        btnOwnerMemberNumber.setEnabled(false);
        setMemberEnableDisable(false);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panCompanyAuthPersonInfo);
        initTableData();
        txtOwnerMemberNumber.setVisible(false);
        btnOwnerMemberNumber.setVisible(false);
        lblOwnerMemberNumber.setVisible(false);
        getTableData();
        //Added By Suresh
        btnNew_Property.setEnabled(false);
        btnDelete_Property.setEnabled(false);
        btnlSave_Property.setEnabled(false);
        btnNew_Property1.setEnabled(false);
        btnDelete_Property1.setEnabled(false);
        btnlSave_Property1.setEnabled(false);
    }

    private void initTableData() {
    }

    private void getTableData() {

        Object rowData[][] = new Object[+bufferList.size()][5];
        //  Integer rowData[][] = new  Integer[buffer1.size()][3];
        int j = 0;
        String d1 = "";
        String d2 = "";
        int i = 0;
        System.out.println("BuufferrList  " + bufferList.size());




        for (i = 0; i < bufferList.size(); i++) {
            HashMap m = new HashMap();
            m = (HashMap) bufferList.get(i);
            System.out.println("iii m00001 : " + m.get("SNO"));
            rowData[i][0] = m.get("SNO").toString();
            System.out.println("iii m000011 : " + m.get("DOC_NO"));
            rowData[i][1] = m.get("DOC_NO").toString();
            rowData[i][2] = m.get("DOC_TYPE").toString();
            System.out.println("iii m0000 111: " + m.get("DOC_TYPE"));
            rowData[i][3] = m.get("DOC_DATE").toString();
            rowData[i][4] = m.get("REG_OFFIC").toString();
        }
        System.out.println("iii m0000 222: ");





        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        tblDocument.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return false;
            }
        });
        tblDocument.setVisible(true);

    }

    private void getTableData1() {

        Object rowData[][] = new Object[+bufferList1.size()][5];
        //  Integer rowData[][] = new  Integer[buffer1.size()][3];
        int j = 0;
        String d1 = "";
        String d2 = "";
        int i = 0;
        System.out.println("BuufferrList  " + bufferList1.size());




        for (i = 0; i < bufferList1.size(); i++) {
            HashMap m = new HashMap();
            m = (HashMap) bufferList1.get(i);
            System.out.println("iii m00001 : " + m.get("SNO"));
            rowData[i][0] = m.get("SNO").toString();
            System.out.println("iii m000011 : " + m.get("DOC_NO"));
            rowData[i][1] = m.get("DOC_NO").toString();
            rowData[i][2] = m.get("DOC_TYPE").toString();
            System.out.println("iii m0000 111: " + m.get("DOC_TYPE"));
            rowData[i][3] = m.get("DOC_DATE").toString();
            rowData[i][4] = m.get("REG_OFFIC").toString();
        }
        System.out.println("iii m0000 222: ");





        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        tblDocument.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames1) {
            public boolean isCellEditable(int row, int column) {
                //Only the third column
                return false;
            }
        });
        tblDocument.setVisible(true);

    }

    /**
     * To initialize comboboxes
     */
    private void initComponentData() {
        //        cboTitle.setModel(observable.getCbmTitle());
        cboConstitution.setModel(observable.getCbmConstitution());
        cboNature.setModel(observable.getCbmNature());
        cboPledge.setModel(observable.getCbmPledge());
        cboRight.setModel(observable.getCbmRight());
        cboDocumentType.setModel(observable.getCbmDocumentType());
        cboItemsSubmitting.setModel(observable.getCbmDocumentsSubmitted());
        cboVillage.setModel(observable.getCbmVillage());
        gahanTable.setModel(observable.getGahanTable());
        tblLoansAvailedAgainstSecurity.setModel(observable.getLoansAvailedAgainstSecurity());
        tblGahanLandDetails.setModel(observable.getTblGahanLandDetails());
        //tblGahanLandDetails.setModel(
        //        cboAddressType.setModel(observable.getCbmAddressType());
        //        cboCity.setModel(observable.getCbmCity());
        //        cboState.setModel(observable.getCbmState());
        //        cboPhoneType.setModel(observable.getCbmPhoneType());
        //        cboBusNature.setModel(observable.getCbmBusNature());
        //        cboIntroType.setModel(observable.getCbmIntroType());
        //        cboMembershipClass.setModel(observable.getCbmMembershipClass());
        //        cboAddrProof.setModel(observable.getCbmAddrProof());
        //        cboIdenProof.setModel(observable.getCbmIdenProof());
    }
    //

    private void setObservable() {
        /* Singleton pattern can't be implemented as there are two observers using the same observable*/
        // The parameter '2' indicates that the customer type is CORPORATE
        observable = new GahanCustomerOB();
        observable.addObserver(this);
    }

    private void setMandatoryMarks() {
        //    objCustomerUISupport.putMandatoryMarks(CLASSNAME,panKYC);
        //        objCustomerUISupport.putMandatoryMarks(CLASSNAME,panCompanyInfo);
        //        objCustomerUISupport.putMandatoryMarks(CLASSNAME,panCustomerSide);
        //        objCustomerUISupport.putMandatoryMarks(CLASSNAME,panContactInfo);
    }

    private void setMaximumLength() {
//        txtVillage.setMaxLength(16);
        txtSurveryNo.setMaxLength(50);
        txtTotalArea.setMaxLength(16);
        //txtTotalArea.setAllowNumber(true);
        txtARS.setMaxLength(14);
        txtARS.setAllowNumber(true);
        txtSurveryNo.setAllowAll(true);
        txtPledgeNo.setMaxLength(12);
        txtPledgeNo.setAllowAll(true);
        txtPledgeAmount.setMaxLength(12);
        txtPledgeAmount.setValidation(new CurrencyValidation(14, 2));
        txtTotalArea.setValidation(new NumericValidation(16, 4));
        txtAvailSecurityValue.setAllowAll(true);
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        panGahanData.setName("panGahanData");
        panMemberButton.setName("panMemberButton");
        lblOwnerMemberNo.setName("lblOwnerMemberNo");
        lblOwnerMemberNumber.setName("lblOwnerMemberNumber");
//        lblOwnerMemberName.setName("lblOwnerMemberName");
        lblMemberType.setName("lblMemberType");
        lblConstitution.setName("lblConstitution");
        lblDocumentNo.setName("lblDocumentNo");
        txtDocumentNo.setName("txtDocumentNo");
        lblDocumentType.setName("lblDocumentType");
        cboDocumentType.setName("cboDocumentType");
        cboItemsSubmitting.setName("cboItemsSubmitting");
        lblDocumentDt.setName("lblDocumentDt");
        tdtDocumentDt.setName("tdtDocumentDt");
        lblPledgeDate.setName("lblPledgeDate");
        lblRegisteredOffice.setName("lblRegisteredOffice");
        txtRegisteredOffice.setName("txtRegisteredOffice");
        lblPledge.setName("lblPledge");
        cboPledge.setName("cboPledge");
        lblPledgeDate.setName("lblPledgeDate");
        tdtPledgeDate.setName("tdtPledgeDate");
        lblPledgeNo.setName("lblPledgeNo");
        lblPledgeAmount.setName("lblPledgeAmount");
        txtPledgeNo.setName("txtPledgeNo");
        txtPledgeAmount.setName("txtPledgeAmount");
        lblVillage.setName("lblVillage");
        cboVillage.setName("cboVillage");
        lblSurveryNo.setName("lblSurveryNo");
        txtSurveryNo.setName("txtSurveryNo");
        lblTotalArea.setName("lblTotalArea");
        txtTotalArea.setName("txtTotalArea");
        lblNature.setName("lblNature");
        cboRight.setName("cboNature");
        lblRight.setName("lblRight");
        cboRight.setName("cboRight");
        lblGahanExpDt.setName("lblGahanExpDt");
        lblGahanReleasedDate.setName("lblGahanReleasedDate");
        lblGahanReleasedExpiryDate.setName("lblGahanReleasedExpiryDate");
        tdtGahanExpDt.setName("tdtGahanExpDt");
        rdoGahanReleasedYes.setName("rdoGahanReleasedYes");
        rdoGahanReleasedNo.setName("rdoGahanReleasedNo");
        tdtGahanReleasExpDate.setName("tdtGahanReleasExpDate");
        lblHouseName.setName("lblHouseName");
        lblPlace.setName("lblPlace");
        lblCity.setName("lblCity");
        lbkPinCode.setName("lbkPinCode");
        lblHouseNameVal.setName("lblHouseNameVal");
        lblPlaceVal.setName("lblPlaceVal");
        lblCityVal.setName("lblCityVal");
        lblPinCodeVal.setName("lblPinCodeVal");
        panGahanTableData.setName("panGahanTableData");
        gahanTableScrollPan.setName("gahanTableScrollPan");
        gahanTable.setName("gahanTable");
        tblGahanLandDetails.setName("tblGahanLandDetails");
        tblLoansAvailedAgainstSecurity.setName("tblLoansAvailedAgainstSecurity");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {

        lblOwnerMemberNo.setText(resourceBundle.getString("lblOwnerMemberNo"));
        lblOwnerMemberNumber.setText(resourceBundle.getString("lblOwnerMemberNumber"));
//        lblOwnerMemberName.setText(resourceBundle.getString("lblOwnerMemberName"));
        lblDocumentNo.setText(resourceBundle.getString("lblDocumentNo"));
        lblDocumentType.setText(resourceBundle.getString("lblDocumentType"));
        lblDocumentDt.setText(resourceBundle.getString("lblDocumentDt"));
        lblRegisteredOffice.setText(resourceBundle.getString("lblRegisteredOffice"));
        lblPledge.setText(resourceBundle.getString("lblPledge"));
        lblPledgeDate.setText(resourceBundle.getString("lblPledgeDate"));
        lblPledgeNo.setText(resourceBundle.getString("lblPledgeNo"));
        lblPledgeAmount.setText(resourceBundle.getString("lblPledgeAmount"));
        lblVillage.setText(resourceBundle.getString("lblVillage"));
        lblSurveryNo.setText(resourceBundle.getString("lblSurveryNo"));
        lblTotalArea.setText(resourceBundle.getString("lblTotalArea"));
        lblNature.setText(resourceBundle.getString("lblNature"));
        lblRight.setText(resourceBundle.getString("lblRight"));
        lblGahanExpDt.setText(resourceBundle.getString("lblGahanExpDt"));
        lblGahanReleasedDate.setText(resourceBundle.getString("lblGahanReleasedDate"));
        lblGahanReleasedExpiryDate.setText(resourceBundle.getString("lblGahanReleasedExpiryDate"));
        lblAvailSecurityValue.setText(resourceBundle.getString("lblAvailSecurityValue"));
        lblHouseName.setText(resourceBundle.getString("lblHouseName"));
        lblPlace.setText(resourceBundle.getString("lblPlace"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lbkPinCode.setText(resourceBundle.getString("lbkPinCode"));
        lblMemberType.setText(resourceBundle.getString("lblMemberType"));
        lblConstitution.setText(resourceBundle.getString("lblConstitution"));
        // lblRemarks.setText(resourceBundle.getString("lblRemarks"));
    }

    /* Auto Generated Method - setMandatoryHashMap()
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtGahanExpDt", new Boolean(false));
        mandatoryMap.put("txtDocumentNo", new Boolean(false));
        mandatoryMap.put("txtDocumentType", new Boolean(false));
        mandatoryMap.put("tdtDocumentDt", new Boolean(false));
        mandatoryMap.put("txtRegisteredOffice", new Boolean(false));
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        GahanCustomerMRB objMandatoryRB = new GahanCustomerMRB();
        tdtPledgeDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPledgeDate"));
        cboVillage.setHelpMessage(lblMsg, objMandatoryRB.getString("cboVillage"));
        txtSurveryNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSurveryNo"));
        txtTotalArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalArea"));
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtRemarks.setText(observable.getTxtRemarks());
        txtMemberNo.setText(observable.getTxtMemberNo());
        txtOwnerMemberNumber.setText(observable.getTxtOwnerMemberNumber());
        txtDocumentNo.setText(observable.getTxtDocumentNo());
        txtRegisteredOffice.setText(observable.getTxtRegisteredOffice());
        tdtPledgeDate.setDateValue(observable.getTdtPledgeDate());
        txtPledgeNo.setText(observable.getTxtPledgeNo());
        txtPledgeAmount.setText(observable.getTxtPledgeAmount());
//        cboVillage.setText(observable.getCboVillage());
        txtSurveryNo.setText(observable.getTxtSurveryNo());
        txtTotalArea.setText(observable.getTxtTotalArea());
        txtHector.setText(ToHector(txtTotalArea.getText()));
        tdtDocumentDt.setDateValue(observable.getTdtDocumentDt());
        tdtGahanExpDt.setDateValue(observable.getTdtGahanExpDate());
        rdoGahanReleasedYes.setSelected(observable.isGahanReleaseYes());
        rdoGahanReleasedNo.setSelected(observable.isGahanReleaseNo());
        tdtGahanReleasExpDate.setDateValue(observable.getTdtGahanReleasExpDate());
        txtGahanReleaseNo.setText(observable.getTxtGahanReleaseNo());
        gahanTable.setModel(observable.getGahanTable());
        tblGahanLandDetails.setModel(observable.getTblGahanLandDetails());
        tblLoansAvailedAgainstSecurity.setModel(observable.getLoansAvailedAgainstSecurity());
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtDocumentNo(txtDocumentNo.getText());
        observable.setCboDocumentType(CommonUtil.convertObjToStr(cboDocumentType.getSelectedItem()));
        observable.setTdtDocumentDt(tdtDocumentDt.getDateValue());
        observable.setTxtRegisteredOffice(txtRegisteredOffice.getText());
        observable.setTdtPledgeDate(tdtPledgeDate.getDateValue());
        observable.setTxtPledgeNo(txtPledgeNo.getText());
        observable.setTxtPledgeAmount(txtPledgeAmount.getText());
        observable.setCboPledge(CommonUtil.convertObjToStr(cboPledge.getSelectedItem()));
        observable.setTdtGahanExpDate(tdtGahanExpDt.getDateValue());
        observable.setTdtGahanReleasExpDate(tdtGahanReleasExpDate.getDateValue());
        observable.setTxtGahanReleaseNo(txtGahanReleaseNo.getText());
        observable.setTxtMemberNo(txtMemberNo.getText());
        observable.setTxtOwnerMemberNumber(txtOwnerMemberNumber.getText());
        if (rdoGahanReleasedYes.isSelected() == true) {
            observable.setGahanReleaseYes(true);
            observable.setGahanReleaseNo(false);
        } else {
            observable.setGahanReleaseYes(false);
            observable.setGahanReleaseNo(true);

        }
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setGahanDocDetails(bufferList);
    }

    /**
     * The method used to enable or disable the button after Edit button is
     * Clicked*
     */
    private void enableDisable() {
        ClientUtil.enableDisable(this, true);
    }

    public static void main(String args[]) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.show();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoGender = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMaritalStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgExistingCust = new com.see.truetransact.uicomponent.CButtonGroup();
        panCorporateCustomer = new com.see.truetransact.uicomponent.CPanel();
        tabCorpCust = new com.see.truetransact.uicomponent.CTabbedPane();
        panCompanyAuthPersonInfo = new com.see.truetransact.uicomponent.CPanel();
        panGahanData = new com.see.truetransact.uicomponent.CPanel();
        panCustomerDetails = new com.see.truetransact.uicomponent.CPanel();
        panMemberButton = new com.see.truetransact.uicomponent.CPanel();
        btnMember = new com.see.truetransact.uicomponent.CButton();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        lblConstitution = new com.see.truetransact.uicomponent.CLabel();
        cboConstitution = new com.see.truetransact.uicomponent.CComboBox();
        txtMemberType = new javax.swing.JTextField();
        lblHouseName = new com.see.truetransact.uicomponent.CLabel();
        lblHouseNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblPlace = new com.see.truetransact.uicomponent.CLabel();
        lblPlaceVal = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblCityVal = new com.see.truetransact.uicomponent.CLabel();
        lbkPinCode = new com.see.truetransact.uicomponent.CLabel();
        lblPinCodeVal = new com.see.truetransact.uicomponent.CLabel();
        lblOwnerMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblMemberType = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNameVal = new com.see.truetransact.uicomponent.CLabel();
        panOwnerMemberNumber = new com.see.truetransact.uicomponent.CPanel();
        btnOwnerMemberNumber = new com.see.truetransact.uicomponent.CButton();
        txtOwnerMemberNumber = new com.see.truetransact.uicomponent.CTextField();
        lblOwnerMemberNumber = new com.see.truetransact.uicomponent.CLabel();
        panPropertyDetails = new com.see.truetransact.uicomponent.CPanel();
        txtTotalArea = new com.see.truetransact.uicomponent.CTextField();
        lblTotalArea = new com.see.truetransact.uicomponent.CLabel();
        txtSurveryNo = new com.see.truetransact.uicomponent.CTextField();
        lblSurveryNo = new com.see.truetransact.uicomponent.CLabel();
        lblVillage = new com.see.truetransact.uicomponent.CLabel();
        cboVillage = new com.see.truetransact.uicomponent.CComboBox();
        lblNature = new com.see.truetransact.uicomponent.CLabel();
        lblRight = new com.see.truetransact.uicomponent.CLabel();
        cboRight = new com.see.truetransact.uicomponent.CComboBox();
        lblResurvey = new com.see.truetransact.uicomponent.CLabel();
        cboNature = new com.see.truetransact.uicomponent.CComboBox();
        txtresurvey = new com.see.truetransact.uicomponent.CTextField();
        txtHector = new com.see.truetransact.uicomponent.CTextField();
        lblHector = new com.see.truetransact.uicomponent.CLabel();
        lblOwnerNo = new com.see.truetransact.uicomponent.CLabel();
        txtOwnerNo = new com.see.truetransact.uicomponent.CTextField();
        btnOwnerNo = new com.see.truetransact.uicomponent.CButton();
        lblPropertyMemberNameVal = new com.see.truetransact.uicomponent.CLabel();
        txtARS = new com.see.truetransact.uicomponent.CTextField();
        lblARS = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtOwnerNo2 = new com.see.truetransact.uicomponent.CTextField();
        btnOwnerNo2 = new com.see.truetransact.uicomponent.CButton();
        lblOwnerTwoName = new com.see.truetransact.uicomponent.CLabel();
        panDocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDocumentNo = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentType = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentDt = new com.see.truetransact.uicomponent.CLabel();
        lblRegisteredOffice = new com.see.truetransact.uicomponent.CLabel();
        cboDocumentType = new com.see.truetransact.uicomponent.CComboBox();
        txtDocumentNo = new javax.swing.JTextField();
        tdtDocumentDt = new com.see.truetransact.uicomponent.CDateField();
        txtRegisteredOffice = new javax.swing.JTextField();
        panProperty = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Property = new com.see.truetransact.uicomponent.CButton();
        btnlSave_Property = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Property = new com.see.truetransact.uicomponent.CButton();
        panCustomerJoint = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Member = new com.see.truetransact.uicomponent.CButton();
        btnToMain_Member = new com.see.truetransact.uicomponent.CButton();
        btnDeleteMember = new com.see.truetransact.uicomponent.CButton();
        panProperty1 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Property1 = new com.see.truetransact.uicomponent.CButton();
        btnlSave_Property1 = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Property1 = new com.see.truetransact.uicomponent.CButton();
        panItemsSubmitting = new com.see.truetransact.uicomponent.CPanel();
        lblItemsSubmitting = new com.see.truetransact.uicomponent.CLabel();
        btnItemsSubmittingAdd = new com.see.truetransact.uicomponent.CButton();
        cboItemsSubmitting = new com.see.truetransact.uicomponent.CComboBox();
        btnItemsSubmittingRemove = new com.see.truetransact.uicomponent.CButton();
        panGahanTableData = new com.see.truetransact.uicomponent.CPanel();
        panPledgeDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPledgeNo = new com.see.truetransact.uicomponent.CLabel();
        lblPledge = new com.see.truetransact.uicomponent.CLabel();
        lblPledgeDate = new com.see.truetransact.uicomponent.CLabel();
        lblPledgeAmount = new com.see.truetransact.uicomponent.CLabel();
        cboPledge = new com.see.truetransact.uicomponent.CComboBox();
        tdtPledgeDate = new com.see.truetransact.uicomponent.CDateField();
        txtPledgeAmount = new com.see.truetransact.uicomponent.CTextField();
        txtPledgeNo = new com.see.truetransact.uicomponent.CTextField();
        panOherDetails = new com.see.truetransact.uicomponent.CPanel();
        tdtGahanExpDt = new com.see.truetransact.uicomponent.CDateField();
        rdoGahanReleasedYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGahanReleasedNo = new com.see.truetransact.uicomponent.CRadioButton();
        tdtGahanReleasExpDate = new com.see.truetransact.uicomponent.CDateField();
        lblGahanExpDt = new com.see.truetransact.uicomponent.CLabel();
        lblGahanReleasedDate = new com.see.truetransact.uicomponent.CLabel();
        lblGahanReleasedExpiryDate = new com.see.truetransact.uicomponent.CLabel();
        lblAvailSecurityValue = new com.see.truetransact.uicomponent.CLabel();
        txtAvailSecurityValue = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblGahanReleaseNo = new com.see.truetransact.uicomponent.CLabel();
        txtGahanReleaseNo = new com.see.truetransact.uicomponent.CTextField();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        gahanTableScrollPan = new com.see.truetransact.uicomponent.CScrollPane();
        gahanTable = new com.see.truetransact.uicomponent.CTable();
        gahanTableScrollPan2 = new com.see.truetransact.uicomponent.CScrollPane();
        tblGahanLandDetails = new com.see.truetransact.uicomponent.CTable();
        documentScrollPan = new com.see.truetransact.uicomponent.CScrollPane();
        tblDocument = new com.see.truetransact.uicomponent.CTable();
        panCompanyAuthPersonInfo1 = new com.see.truetransact.uicomponent.CPanel();
        panGahanCustomerDetails1 = new com.see.truetransact.uicomponent.CPanel();
        srpLockerRentSIApplication = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoansAvailedAgainstSecurity = new com.see.truetransact.uicomponent.CTable();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDeleteDetails = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCorporateCustomer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Self Help Group");
        setMinimumSize(new java.awt.Dimension(1000, 700));
        setPreferredSize(new java.awt.Dimension(1000, 700));

        panCorporateCustomer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panCorporateCustomer.setMinimumSize(new java.awt.Dimension(844, 490));
        panCorporateCustomer.setPreferredSize(new java.awt.Dimension(844, 490));
        panCorporateCustomer.setLayout(new java.awt.GridBagLayout());

        tabCorpCust.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabCorpCustFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tabCorpCustFocusLost(evt);
            }
        });
        tabCorpCust.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabCorpCustStateChanged(evt);
            }
        });
        tabCorpCust.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabCorpCustMouseClicked(evt);
            }
        });

        panCompanyAuthPersonInfo.setMinimumSize(new java.awt.Dimension(800, 380));
        panCompanyAuthPersonInfo.setPreferredSize(new java.awt.Dimension(800, 380));
        panCompanyAuthPersonInfo.setLayout(new java.awt.GridBagLayout());

        panGahanData.setMaximumSize(new java.awt.Dimension(430, 525));
        panGahanData.setMinimumSize(new java.awt.Dimension(500, 540));
        panGahanData.setPreferredSize(new java.awt.Dimension(500, 525));

        panCustomerDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Owner Member Details"));
        panCustomerDetails.setMinimumSize(new java.awt.Dimension(470, 160));
        panCustomerDetails.setPreferredSize(new java.awt.Dimension(430, 180));
        panCustomerDetails.setLayout(new java.awt.GridBagLayout());

        panMemberButton.setMaximumSize(new java.awt.Dimension(125, 25));
        panMemberButton.setMinimumSize(new java.awt.Dimension(125, 25));
        panMemberButton.setPreferredSize(new java.awt.Dimension(125, 25));
        panMemberButton.setLayout(new java.awt.GridBagLayout());

        btnMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMember.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMember.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMember.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMemberButton.add(btnMember, gridBagConstraints);

        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemberNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemberNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 1);
        panMemberButton.add(txtMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 2);
        panCustomerDetails.add(panMemberButton, gridBagConstraints);

        lblConstitution.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblConstitution, gridBagConstraints);

        cboConstitution.setMinimumSize(new java.awt.Dimension(100, 22));
        cboConstitution.setPopupWidth(260);
        cboConstitution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboConstitutionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(cboConstitution, gridBagConstraints);

        txtMemberType.setMinimumSize(new java.awt.Dimension(70, 21));
        txtMemberType.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(txtMemberType, gridBagConstraints);

        lblHouseName.setText("HouseName");
        lblHouseName.setFont(new java.awt.Font("MS Sans Serif", 0, 12)); // NOI18N
        lblHouseName.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblHouseName, gridBagConstraints);

        lblHouseNameVal.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblHouseNameVal, gridBagConstraints);

        lblPlace.setText("Place");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblPlace, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblPlaceVal, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblCity, gridBagConstraints);

        lblCityVal.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblCityVal.setMinimumSize(new java.awt.Dimension(70, 10));
        lblCityVal.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblCityVal, gridBagConstraints);

        lbkPinCode.setText("PinCode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lbkPinCode, gridBagConstraints);

        lblPinCodeVal.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        lblPinCodeVal.setMinimumSize(new java.awt.Dimension(70, 10));
        lblPinCodeVal.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblPinCodeVal, gridBagConstraints);

        lblOwnerMemberNo.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 4);
        panCustomerDetails.add(lblOwnerMemberNo, gridBagConstraints);

        lblMemberType.setText("Member Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblMemberType, gridBagConstraints);

        lblMemberNameVal.setForeground(new java.awt.Color(0, 51, 255));
        lblMemberNameVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMemberNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMemberNameVal.setMaximumSize(new java.awt.Dimension(100, 18));
        lblMemberNameVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblMemberNameVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCustomerDetails.add(lblMemberNameVal, gridBagConstraints);

        panOwnerMemberNumber.setMaximumSize(new java.awt.Dimension(125, 2));
        panOwnerMemberNumber.setMinimumSize(new java.awt.Dimension(125, 2));
        panOwnerMemberNumber.setPreferredSize(new java.awt.Dimension(125, 2));
        panOwnerMemberNumber.setLayout(new java.awt.GridBagLayout());

        btnOwnerMemberNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOwnerMemberNumber.setMaximumSize(new java.awt.Dimension(21, 21));
        btnOwnerMemberNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOwnerMemberNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOwnerMemberNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOwnerMemberNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOwnerMemberNumber.add(btnOwnerMemberNumber, gridBagConstraints);

        txtOwnerMemberNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOwnerMemberNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOwnerMemberNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 1);
        panOwnerMemberNumber.add(txtOwnerMemberNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        panCustomerDetails.add(panOwnerMemberNumber, gridBagConstraints);

        lblOwnerMemberNumber.setText("Owner Member/Customer ID");
        lblOwnerMemberNumber.setMaximumSize(new java.awt.Dimension(164, 2));
        lblOwnerMemberNumber.setMinimumSize(new java.awt.Dimension(164, 2));
        lblOwnerMemberNumber.setPreferredSize(new java.awt.Dimension(164, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 1, 2);
        panCustomerDetails.add(lblOwnerMemberNumber, gridBagConstraints);

        panPropertyDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Property Details"));
        panPropertyDetails.setMinimumSize(new java.awt.Dimension(470, 145));
        panPropertyDetails.setPreferredSize(new java.awt.Dimension(470, 145));

        txtTotalArea.setAllowNumber(true);
        txtTotalArea.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotalArea.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalAreaActionPerformed(evt);
            }
        });
        txtTotalArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotalAreaFocusLost(evt);
            }
        });

        lblTotalArea.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalArea.setText("Total Area In Cents");

        txtSurveryNo.setMinimumSize(new java.awt.Dimension(100, 21));

        lblSurveryNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSurveryNo.setText("Survey No");

        lblVillage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVillage.setText("Village");

        cboVillage.setMinimumSize(new java.awt.Dimension(100, 21));
        cboVillage.setPopupWidth(150);

        lblNature.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNature.setText("Nature");
        lblNature.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        lblRight.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRight.setText("Right");
        lblRight.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        cboRight.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRight.setPopupWidth(180);

        lblResurvey.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblResurvey.setText("ReSurvey No");

        cboNature.setMinimumSize(new java.awt.Dimension(100, 21));
        cboNature.setPopupWidth(150);

        txtresurvey.setAllowAll(true);
        txtresurvey.setMinimumSize(new java.awt.Dimension(100, 21));

        txtHector.setAllowNumber(true);
        txtHector.setMinimumSize(new java.awt.Dimension(100, 21));
        txtHector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHectorActionPerformed(evt);
            }
        });
        txtHector.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHectorFocusLost(evt);
            }
        });

        lblHector.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHector.setText("In Hector");

        lblOwnerNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOwnerNo.setText("Owner No");

        txtOwnerNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtOwnerNo.setMinimumSize(new java.awt.Dimension(100, 21));

        btnOwnerNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOwnerNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnOwnerNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOwnerNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOwnerNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOwnerNoActionPerformed(evt);
            }
        });

        lblPropertyMemberNameVal.setForeground(new java.awt.Color(0, 51, 255));
        lblPropertyMemberNameVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPropertyMemberNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblPropertyMemberNameVal.setMaximumSize(new java.awt.Dimension(100, 18));
        lblPropertyMemberNameVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblPropertyMemberNameVal.setPreferredSize(new java.awt.Dimension(100, 18));

        txtARS.setMaximumSize(new java.awt.Dimension(100, 21));
        txtARS.setMinimumSize(new java.awt.Dimension(100, 21));
        txtARS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtARSActionPerformed(evt);
            }
        });
        txtARS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtARSFocusLost(evt);
            }
        });

        lblARS.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblARS.setText("ARS");

        cLabel1.setText("Owner No 2");

        btnOwnerNo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOwnerNo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOwnerNo2ActionPerformed(evt);
            }
        });

        lblOwnerTwoName.setForeground(new java.awt.Color(0, 51, 255));
        lblOwnerTwoName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N

        javax.swing.GroupLayout panPropertyDetailsLayout = new javax.swing.GroupLayout(panPropertyDetails);
        panPropertyDetails.setLayout(panPropertyDetailsLayout);
        panPropertyDetailsLayout.setHorizontalGroup(
            panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblTotalArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(txtTotalArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(lblVillage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(cboVillage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lblSurveryNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                                .addGap(94, 94, 94)
                                .addComponent(lblARS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(txtARS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblHector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2)
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSurveryNo, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(lblNature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboNature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panPropertyDetailsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblResurvey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtresurvey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addComponent(lblOwnerNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtOwnerNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOwnerNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                                .addComponent(lblPropertyMemberNameVal, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOwnerNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnOwnerNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblOwnerTwoName, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                                .addGap(137, 137, 137)
                                .addComponent(lblRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panPropertyDetailsLayout.setVerticalGroup(
            panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboVillage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSurveryNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblVillage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSurveryNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(4, 4, 4)
                .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblARS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtARS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblHector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtHector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(lblTotalArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnOwnerNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboNature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblOwnerNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtOwnerNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(lblNature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPropertyMemberNameVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnOwnerNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblOwnerTwoName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panPropertyDetailsLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblResurvey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtresurvey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panPropertyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtOwnerNo2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panDocumentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Document Details"));
        panDocumentDetails.setMinimumSize(new java.awt.Dimension(450, 80));
        panDocumentDetails.setPreferredSize(new java.awt.Dimension(450, 80));
        panDocumentDetails.setLayout(new java.awt.GridBagLayout());

        lblDocumentNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocumentNo.setText("Document No");
        lblDocumentNo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(lblDocumentNo, gridBagConstraints);

        lblDocumentType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocumentType.setText("Document Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(lblDocumentType, gridBagConstraints);

        lblDocumentDt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocumentDt.setText("Document Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(lblDocumentDt, gridBagConstraints);

        lblRegisteredOffice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRegisteredOffice.setText("Registered Office");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(lblRegisteredOffice, gridBagConstraints);

        cboDocumentType.setMinimumSize(new java.awt.Dimension(100, 22));
        cboDocumentType.setPopupWidth(150);
        cboDocumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDocumentTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(cboDocumentType, gridBagConstraints);

        txtDocumentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDocumentNo.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(txtDocumentNo, gridBagConstraints);

        tdtDocumentDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDocumentDt.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(tdtDocumentDt, gridBagConstraints);

        txtRegisteredOffice.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRegisteredOffice.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(txtRegisteredOffice, gridBagConstraints);

        panProperty.setMinimumSize(new java.awt.Dimension(250, 26));
        panProperty.setPreferredSize(new java.awt.Dimension(250, 26));
        panProperty.setLayout(new java.awt.GridBagLayout());

        btnNew_Property.setText("New");
        btnNew_Property.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_PropertyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProperty.add(btnNew_Property, gridBagConstraints);

        btnlSave_Property.setText("Save");
        btnlSave_Property.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlSave_PropertyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProperty.add(btnlSave_Property, gridBagConstraints);

        btnDelete_Property.setText("Delete");
        btnDelete_Property.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_PropertyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProperty.add(btnDelete_Property, gridBagConstraints);

        panCustomerJoint.setMinimumSize(new java.awt.Dimension(315, 28));
        panCustomerJoint.setPreferredSize(new java.awt.Dimension(315, 28));
        panCustomerJoint.setLayout(new java.awt.GridBagLayout());

        btnNew_Member.setText("New");
        btnNew_Member.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_MemberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerJoint.add(btnNew_Member, gridBagConstraints);

        btnToMain_Member.setText("To Main");
        btnToMain_Member.setMaximumSize(new java.awt.Dimension(100, 27));
        btnToMain_Member.setMinimumSize(new java.awt.Dimension(100, 27));
        btnToMain_Member.setPreferredSize(new java.awt.Dimension(100, 27));
        btnToMain_Member.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToMain_MemberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 3);
        panCustomerJoint.add(btnToMain_Member, gridBagConstraints);

        btnDeleteMember.setText("Delete");
        btnDeleteMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteMemberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 4);
        panCustomerJoint.add(btnDeleteMember, gridBagConstraints);

        panProperty1.setMinimumSize(new java.awt.Dimension(250, 25));
        panProperty1.setPreferredSize(new java.awt.Dimension(200, 25));
        panProperty1.setLayout(new java.awt.GridBagLayout());

        btnNew_Property1.setText("New");
        btnNew_Property1.setMaximumSize(new java.awt.Dimension(61, 27));
        btnNew_Property1.setMinimumSize(new java.awt.Dimension(61, 27));
        btnNew_Property1.setPreferredSize(new java.awt.Dimension(61, 27));
        btnNew_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_Property1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 5);
        panProperty1.add(btnNew_Property1, gridBagConstraints);

        btnlSave_Property1.setText("Save");
        btnlSave_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlSave_Property1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 5);
        panProperty1.add(btnlSave_Property1, gridBagConstraints);

        btnDelete_Property1.setText("Delete");
        btnDelete_Property1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_Property1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProperty1.add(btnDelete_Property1, gridBagConstraints);

        panItemsSubmitting.setMaximumSize(new java.awt.Dimension(260, 41));
        panItemsSubmitting.setMinimumSize(new java.awt.Dimension(260, 41));
        panItemsSubmitting.setPreferredSize(new java.awt.Dimension(260, 41));

        lblItemsSubmitting.setText("Items Submitting");

        btnItemsSubmittingAdd.setText("Add");
        btnItemsSubmittingAdd.setMaximumSize(new java.awt.Dimension(50, 27));
        btnItemsSubmittingAdd.setMinimumSize(new java.awt.Dimension(50, 27));
        btnItemsSubmittingAdd.setPreferredSize(new java.awt.Dimension(50, 27));
        btnItemsSubmittingAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemsSubmittingAddActionPerformed(evt);
            }
        });

        cboItemsSubmitting.setMaximumSize(new java.awt.Dimension(100, 21));
        cboItemsSubmitting.setMinimumSize(new java.awt.Dimension(100, 21));

        btnItemsSubmittingRemove.setText("Remove");
        btnItemsSubmittingRemove.setMaximumSize(new java.awt.Dimension(50, 27));
        btnItemsSubmittingRemove.setMinimumSize(new java.awt.Dimension(50, 27));
        btnItemsSubmittingRemove.setPreferredSize(new java.awt.Dimension(50, 27));
        btnItemsSubmittingRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemsSubmittingRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panItemsSubmittingLayout = new javax.swing.GroupLayout(panItemsSubmitting);
        panItemsSubmitting.setLayout(panItemsSubmittingLayout);
        panItemsSubmittingLayout.setHorizontalGroup(
            panItemsSubmittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panItemsSubmittingLayout.createSequentialGroup()
                .addGroup(panItemsSubmittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panItemsSubmittingLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnItemsSubmittingAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblItemsSubmitting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panItemsSubmittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboItemsSubmitting, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panItemsSubmittingLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnItemsSubmittingRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panItemsSubmittingLayout.setVerticalGroup(
            panItemsSubmittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panItemsSubmittingLayout.createSequentialGroup()
                .addGroup(panItemsSubmittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblItemsSubmitting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboItemsSubmitting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panItemsSubmittingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnItemsSubmittingRemove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnItemsSubmittingAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout panGahanDataLayout = new javax.swing.GroupLayout(panGahanData);
        panGahanData.setLayout(panGahanDataLayout);
        panGahanDataLayout.setHorizontalGroup(
            panGahanDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panGahanDataLayout.createSequentialGroup()
                .addGroup(panGahanDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panGahanDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panGahanDataLayout.createSequentialGroup()
                            .addComponent(panPropertyDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(37, 37, 37))
                        .addGroup(panGahanDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panGahanDataLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(panCustomerDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panGahanDataLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(panGahanDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panDocumentDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panGahanDataLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(panProperty, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(panItemsSubmitting, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panGahanDataLayout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(panCustomerJoint, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panGahanDataLayout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(panProperty1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panGahanDataLayout.setVerticalGroup(
            panGahanDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panGahanDataLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(panCustomerDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panCustomerJoint, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(panPropertyDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panGahanDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panProperty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panItemsSubmitting, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panDocumentDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panProperty1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panCompanyAuthPersonInfo.add(panGahanData, gridBagConstraints);

        panGahanTableData.setMaximumSize(new java.awt.Dimension(400, 525));
        panGahanTableData.setMinimumSize(new java.awt.Dimension(500, 525));
        panGahanTableData.setPreferredSize(new java.awt.Dimension(500, 525));
        panGahanTableData.setLayout(new java.awt.GridBagLayout());

        panPledgeDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Pledge Details"));
        panPledgeDetails.setMinimumSize(new java.awt.Dimension(430, 80));
        panPledgeDetails.setPreferredSize(new java.awt.Dimension(430, 80));
        panPledgeDetails.setLayout(new java.awt.GridBagLayout());

        lblPledgeNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPledgeNo.setText("Pledge No");
        lblPledgeNo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPledgeDetails.add(lblPledgeNo, gridBagConstraints);

        lblPledge.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPledge.setText("Pledge Type");
        lblPledge.setMaximumSize(new java.awt.Dimension(90, 18));
        lblPledge.setMinimumSize(new java.awt.Dimension(90, 18));
        lblPledge.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 8);
        panPledgeDetails.add(lblPledge, gridBagConstraints);

        lblPledgeDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPledgeDate.setText("Pledge Date");
        lblPledgeDate.setMaximumSize(new java.awt.Dimension(90, 18));
        lblPledgeDate.setMinimumSize(new java.awt.Dimension(90, 18));
        lblPledgeDate.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 7);
        panPledgeDetails.add(lblPledgeDate, gridBagConstraints);

        lblPledgeAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPledgeAmount.setText("Pledge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 6);
        panPledgeDetails.add(lblPledgeAmount, gridBagConstraints);

        cboPledge.setMinimumSize(new java.awt.Dimension(100, 22));
        cboPledge.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPledgeDetails.add(cboPledge, gridBagConstraints);

        tdtPledgeDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtPledgeDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtPledgeDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPledgeDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPledgeDetails.add(tdtPledgeDate, gridBagConstraints);

        txtPledgeAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPledgeAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPledgeAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panPledgeDetails.add(txtPledgeAmount, gridBagConstraints);

        txtPledgeNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPledgeNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPledgeNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPledgeDetails.add(txtPledgeNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        panGahanTableData.add(panPledgeDetails, gridBagConstraints);

        panOherDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Details"));
        panOherDetails.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
        panOherDetails.setMinimumSize(new java.awt.Dimension(400, 150));
        panOherDetails.setPreferredSize(new java.awt.Dimension(400, 150));
        panOherDetails.setLayout(new java.awt.GridBagLayout());

        tdtGahanExpDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtGahanExpDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtGahanExpDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtGahanExpDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 2);
        panOherDetails.add(tdtGahanExpDt, gridBagConstraints);

        rdoGahanReleasedYes.setText("Yes");
        rdoGahanReleasedYes.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoGahanReleasedYes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoGahanReleasedYes.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoGahanReleasedYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGahanReleasedYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panOherDetails.add(rdoGahanReleasedYes, gridBagConstraints);

        rdoGahanReleasedNo.setText("No");
        rdoGahanReleasedNo.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoGahanReleasedNo.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoGahanReleasedNo.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoGahanReleasedNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGahanReleasedNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOherDetails.add(rdoGahanReleasedNo, gridBagConstraints);

        tdtGahanReleasExpDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtGahanReleasExpDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panOherDetails.add(tdtGahanReleasExpDate, gridBagConstraints);

        lblGahanExpDt.setText("Gahan Expiry Date");
        lblGahanExpDt.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panOherDetails.add(lblGahanExpDt, gridBagConstraints);

        lblGahanReleasedDate.setText("Gahan Released ");
        lblGahanReleasedDate.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panOherDetails.add(lblGahanReleasedDate, gridBagConstraints);

        lblGahanReleasedExpiryDate.setText("Gahan Released Date");
        lblGahanReleasedExpiryDate.setFont(new java.awt.Font("MS Sans Serif", 0, 12)); // NOI18N
        lblGahanReleasedExpiryDate.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOherDetails.add(lblGahanReleasedExpiryDate, gridBagConstraints);

        lblAvailSecurityValue.setText("Available Security Value");
        lblAvailSecurityValue.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOherDetails.add(lblAvailSecurityValue, gridBagConstraints);

        txtAvailSecurityValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panOherDetails.add(txtAvailSecurityValue, gridBagConstraints);

        lblRemarks.setText("Remarks");
        lblRemarks.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOherDetails.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(130, 21));
        txtRemarks.setPreferredSize(new java.awt.Dimension(130, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 2);
        panOherDetails.add(txtRemarks, gridBagConstraints);

        lblGahanReleaseNo.setText("Ghan Release No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panOherDetails.add(lblGahanReleaseNo, gridBagConstraints);

        txtGahanReleaseNo.setAllowAll(true);
        txtGahanReleaseNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        panOherDetails.add(txtGahanReleaseNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        panGahanTableData.add(panOherDetails, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(395, 290));
        cPanel2.setPreferredSize(new java.awt.Dimension(395, 290));
        cPanel2.setLayout(new java.awt.GridBagLayout());

        cPanel1.setMinimumSize(new java.awt.Dimension(395, 290));
        cPanel1.setPreferredSize(new java.awt.Dimension(395, 290));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        gahanTableScrollPan.setMaximumSize(new java.awt.Dimension(390, 82));
        gahanTableScrollPan.setMinimumSize(new java.awt.Dimension(390, 82));
        gahanTableScrollPan.setPreferredSize(new java.awt.Dimension(390, 82));

        gahanTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        gahanTable.setMaximumSize(new java.awt.Dimension(385, 150));
        gahanTable.setMinimumSize(new java.awt.Dimension(385, 150));
        gahanTable.setPreferredScrollableViewportSize(new java.awt.Dimension(385, 260));
        gahanTable.setPreferredSize(new java.awt.Dimension(385, 150));
        gahanTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gahanTableMouseClicked(evt);
            }
        });
        gahanTableScrollPan.setViewportView(gahanTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 10, 2);
        cPanel1.add(gahanTableScrollPan, gridBagConstraints);

        gahanTableScrollPan2.setMaximumSize(new java.awt.Dimension(390, 90));
        gahanTableScrollPan2.setMinimumSize(new java.awt.Dimension(390, 90));
        gahanTableScrollPan2.setPreferredSize(new java.awt.Dimension(390, 90));

        tblGahanLandDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblGahanLandDetails.setMaximumSize(new java.awt.Dimension(150, 150));
        tblGahanLandDetails.setMinimumSize(new java.awt.Dimension(150, 150));
        tblGahanLandDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(385, 260));
        tblGahanLandDetails.setPreferredSize(new java.awt.Dimension(150, 150));
        tblGahanLandDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGahanLandDetailsMouseClicked(evt);
            }
        });
        gahanTableScrollPan2.setViewportView(tblGahanLandDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 10, 2);
        cPanel1.add(gahanTableScrollPan2, gridBagConstraints);

        documentScrollPan.setMinimumSize(new java.awt.Dimension(390, 90));
        documentScrollPan.setPreferredSize(new java.awt.Dimension(390, 90));

        tblDocument.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDocument.setMaximumSize(new java.awt.Dimension(150, 150));
        tblDocument.setMinimumSize(new java.awt.Dimension(150, 150));
        tblDocument.setPreferredScrollableViewportSize(new java.awt.Dimension(200, 60));
        tblDocument.setPreferredSize(new java.awt.Dimension(150, 150));
        tblDocument.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDocumentMouseClicked(evt);
            }
        });
        documentScrollPan.setViewportView(tblDocument);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 3, 2);
        cPanel1.add(documentScrollPan, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        cPanel2.add(cPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panGahanTableData.add(cPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panCompanyAuthPersonInfo.add(panGahanTableData, gridBagConstraints);

        tabCorpCust.addTab("Gahan Customer Details", panCompanyAuthPersonInfo);

        panCompanyAuthPersonInfo1.setMinimumSize(new java.awt.Dimension(290, 130));
        panCompanyAuthPersonInfo1.setPreferredSize(new java.awt.Dimension(590, 265));
        panCompanyAuthPersonInfo1.setLayout(new java.awt.GridBagLayout());

        panGahanCustomerDetails1.setMinimumSize(new java.awt.Dimension(286, 245));
        panGahanCustomerDetails1.setPreferredSize(new java.awt.Dimension(286, 245));
        panGahanCustomerDetails1.setLayout(new java.awt.GridBagLayout());

        srpLockerRentSIApplication.setMinimumSize(new java.awt.Dimension(810, 335));

        tblLoansAvailedAgainstSecurity.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "LocNo", "Name", "Exp Date", "Rent", "Service Tax", "ProdType", "Title 8", "AvailbleBalance", "SI A/c No"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblLoansAvailedAgainstSecurity.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        srpLockerRentSIApplication.setViewportView(tblLoansAvailedAgainstSecurity);

        panGahanCustomerDetails1.add(srpLockerRentSIApplication, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCompanyAuthPersonInfo1.add(panGahanCustomerDetails1, gridBagConstraints);

        tabCorpCust.addTab("Loans Availed Against This Security", panCompanyAuthPersonInfo1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCorporateCustomer.add(tabCorpCust, gridBagConstraints);

        getContentPane().add(panCorporateCustomer, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace30);

        btnDeleteDetails.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDeleteDetails.setToolTipText("Enquiry Of Closed  Corporates");
        btnDeleteDetails.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDeleteDetails.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDeleteDetails.setEnabled(false);
        btnDeleteDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDetailsActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDeleteDetails);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrCorporateCustomer.add(mnuProcess);

        setJMenuBar(mbrCorporateCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOwnerMemberNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOwnerMemberNumberActionPerformed
        // TODO add your handling code here:
        viewType = OWNER_ID;
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnOwnerMemberNumberActionPerformed

    private void txtOwnerMemberNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOwnerMemberNumberFocusLost
        // TODO add your handling code here:
        //Added BY Suresh
        if (txtOwnerMemberNumber.getText().length() > 0) {
            HashMap executeMap = new HashMap();
            executeMap.put("CUSTOMER_ID OR MEMBER_NO", txtOwnerMemberNumber.getText());
            executeMap.put("BRANCH_CODE", getSelectedBranchID());
            List lst = ClientUtil.executeQuery("getSelectLoanAccInfoList", executeMap);
            if (lst != null && lst.size() > 0) {
                viewType = OWNER_ID;
                executeMap = (HashMap) lst.get(0);
                fillData(executeMap);
                lst = null;
                executeMap = null;
            } else {
                ClientUtil.displayAlert("Invalid Owner Member Number");
                txtOwnerMemberNumber.setText("");
            }
        }
    }//GEN-LAST:event_txtOwnerMemberNumberFocusLost

    private void txtMemberNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemberNoFocusLost
        // TODO add your handling code here:
        //Added BY Suresh
        if (txtMemberNo.getText().length() > 0) {
            HashMap executeMap = new HashMap();
            executeMap.put("CUSTOMER_ID OR MEMBER_NO", txtMemberNo.getText());
            executeMap.put("BRANCH_CODE", getSelectedBranchID());
            List lst = ClientUtil.executeQuery("getSelectLoanAccInfoList", executeMap);
            if (lst != null && lst.size() > 0) {
                viewType = SHAREID;
                executeMap = (HashMap) lst.get(0);
                fillData(executeMap);
                lst = null;
                executeMap = null;
            } else {
                ClientUtil.displayAlert("Invalid Customer Number");
                txtMemberNo.setText("");
            }
        }
    }//GEN-LAST:event_txtMemberNoFocusLost

    private void tdtGahanExpDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtGahanExpDtFocusLost
        // TODO add your handling code here:
        String dt = CommonUtil.convertObjToStr(tdtGahanExpDt.getDateValue());
        if (dt.length() > 0) {
            Date curdt = (Date) currDt.clone();
            Date date = DateUtil.getDateMMDDYYYY(dt);
            Date diffdate = (Date) curdt.clone();
            diffdate.setDate(date.getDate());
            diffdate.setMonth(date.getMonth());
            diffdate.setYear(date.getYear());
            if (DateUtil.dateDiff(curdt, diffdate) < 0) {
                ClientUtil.displayAlert("It should be greater than CurrentDate");
                return;
            }
        }
    }//GEN-LAST:event_tdtGahanExpDtFocusLost

    private void txtPledgeAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPledgeAmountFocusLost
        // TODO add your handling code here:
        int pledge = CommonUtil.convertObjToInt(txtPledgeAmount.getText());
        int row = tblLoansAvailedAgainstSecurity.getRowCount();
        int loan = 0;
        int security = 0;
        if (row > 0) {
            for (int i = 0; i < row; i++) {
                loan = loan + CommonUtil.convertObjToInt(tblLoansAvailedAgainstSecurity.getValueAt(i, 4));
            }
            int securityValue = pledge - loan;
            txtAvailSecurityValue.setText(CommonUtil.convertObjToStr(new Integer(securityValue)));
        } else {
            txtAvailSecurityValue.setText(CommonUtil.convertObjToStr(new Integer(pledge)));
        }
    }//GEN-LAST:event_txtPledgeAmountFocusLost

    private void txtPledgeNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPledgeNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPledgeNoActionPerformed

    private void rdoGahanReleasedNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGahanReleasedNoActionPerformed
        // TODO add your handling code here:
        if (rdoGahanReleasedNo.isSelected() == true) {
            rdoGahanReleasedYes.setSelected(false);
            tdtGahanReleasExpDate.setDateValue("");
            tdtGahanReleasExpDate.setEnabled(false);
            txtGahanReleaseNo.setEnabled(false);
        }
    }//GEN-LAST:event_rdoGahanReleasedNoActionPerformed

    private void rdoGahanReleasedYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGahanReleasedYesActionPerformed
        // TODO add your handling code here:
        if (rdoGahanReleasedYes.isSelected() == true) {
            rdoGahanReleasedNo.setSelected(false);
            tdtGahanReleasExpDate.setEnabled(true);
            txtGahanReleaseNo.setEnabled(true);
            HashMap hmap = new HashMap();
            hmap.put("CUST_ID", txtMemberNo.getText());
            hmap.put("DOC_GEN_ID", observable.getDocumentGenId());
            List list = ClientUtil.executeQuery("checkingLoanOutStanding", hmap);
            List mdsList = ClientUtil.executeQuery("checkingMdsOutStanding", hmap);
            if (list != null && list.size() > 0) {
                ClientUtil.displayAlert("Can not release since loan is not closed");
                rdoGahanReleasedYes.setSelected(false);
                 rdoGahanReleasedNo.setSelected(true);
                return;
            } else if (mdsList != null && mdsList.size() > 0) {
                ClientUtil.displayAlert("Can not release since MDS installments are pending");
                rdoGahanReleasedYes.setSelected(false);
                 rdoGahanReleasedNo.setSelected(true);
                return;
            }
        }
    }//GEN-LAST:event_rdoGahanReleasedYesActionPerformed

    private void btnToMain_MemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToMain_MemberActionPerformed
        // TODO add your handling code here:
        int row = gahanTable.getSelectedRow();
        observable.setMain(row);
        txtMemberNo.setText(CommonUtil.convertObjToStr(gahanTable.getValueAt(0, 1)));
        lblMemberNameVal.setText(CommonUtil.convertObjToStr(gahanTable.getValueAt(0, 0)));

    }//GEN-LAST:event_btnToMain_MemberActionPerformed

    private void btnDelete_PropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_PropertyActionPerformed
        // TODO add your handling code here:

        if (tblGahanLandDetails.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to Delete", "Error", JOptionPane.ERROR_MESSAGE);
            //f=false;
            // tabedit=false;
            return;
        } else {


            int row = tblGahanLandDetails.getSelectedRow();
            System.out.println("rowwww11 " + row);
            System.out.println("");
            String RECORD_NO = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 0));
            observable.setRecordNo(RECORD_NO);
            observable.setGahanaPropertyTable(row);
//        cboVillage.setSelectedItem("CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 0))   "+CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 0)));
            txtSurveryNo.setText("");
            txtARS.setText("");
            txtTotalArea.setText("");
            txtHector.setText("");
            txtresurvey.setText("");
            lblPropertyMemberNameVal.setText("");
            lblOwnerTwoName.setText("");
            for (int i = row; i < tblGahanLandDetails.getRowCount(); i++) {
                System.out.println("row" + row + "i" + i + "RECORD_NO" + RECORD_NO);
                HashMap amap = new HashMap();
                //   String RECORD_NO1=CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 0));

                //amap.put("SNO",RECORD_NO);
                //   System.out.println("mmmm iiirr"+tblGahanLandDetails.getValueAt(i, 0));
                String a = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 0));
                String village = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 1));
                String sno = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 2));
                String ars = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 3));
                String area = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 4));
                String nature = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 6));
                String right = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 7));
                String resurvey = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(i, 5));

                amap.put("SNO", RECORD_NO);
                amap.put("VILLAGE", village);
                amap.put("SARVEYNO", sno);
                amap.put("ARS", ars);
                amap.put("AREA", area);
                amap.put("RESARVEYNO", resurvey);
                amap.put("RIGHT", right);
                amap.put("NATURE", nature);

                System.out.println("amaaaaa" + amap);

                observable.setRecordNo(RECORD_NO);
                observable.setGahanaPropertyTable(i);

                //  observable.setChangeLandDetails(amap,i);
                observable.setLandDetail(amap);

                RECORD_NO = RECORD_NO + 1;
                System.out.println("bufff   " + bufferList + "  dfjd no  " + RECORD_NO);
            }


            cboVillage.setSelectedItem("");
            txtSurveryNo.setText("");
            txtARS.setText("");
            txtTotalArea.setText("");
            txtHector.setText("");
            txtresurvey.setText("");
            txtHector.setText("");
            cboVillage.setEnabled(true);
            txtSurveryNo.setEnabled(true);
            txtARS.setEnabled(true);
            txtTotalArea.setEnabled(true);
            cboRight.setSelectedItem("");
            cboNature.setSelectedItem("");
            cboRight.setEnabled(true);
            cboNature.setEnabled(true);
            // bufferList.remove(row); 
            //getTableData();
        }
        cboVillage.setSelectedItem("");
        txtSurveryNo.setText("");
        txtARS.setText("");
        txtTotalArea.setText("");
        txtHector.setText("");
        txtresurvey.setText("");
        txtHector.setText("");
        cboVillage.setEnabled(true);
        txtSurveryNo.setEnabled(true);
        txtARS.setEnabled(true);
        txtTotalArea.setEnabled(true);
        cboRight.setSelectedItem("");
        cboNature.setSelectedItem("");
        cboRight.setEnabled(true);
        cboNature.setEnabled(true);
        //Added By Suresh
        btnNew_Property.setEnabled(true);
        btnlSave_Property.setEnabled(false);
        btnDelete_Property.setEnabled(false);
        ClientUtil.enableDisable(panPropertyDetails, false);
    }//GEN-LAST:event_btnDelete_PropertyActionPerformed

    private void tblGahanLandDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGahanLandDetailsMouseClicked
        // TODO add your handling code here:
        int row = tblGahanLandDetails.getSelectedRow();
        cboVillage.setSelectedItem(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 1)));
        txtSurveryNo.setText(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 2)));
        txtARS.setText(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 3)));
        txtTotalArea.setText(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 4)));
        txtHector.setText(ToHector(txtTotalArea.getText()));
        txtresurvey.setText(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 5)));
        cboNature.setSelectedItem(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 6)));
        cboRight.setSelectedItem(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 7)));
        txtOwnerNo.setText(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 8)));
        txtOwnerNo2.setText(CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(row, 9)));
        btnNew_Property.setEnabled(false);
        btnlSave_Property.setEnabled(true);
        btnDelete_Property.setEnabled(true);
    }//GEN-LAST:event_tblGahanLandDetailsMouseClicked

    private void gahanTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gahanTableMouseClicked
        // TODO add your handling code here:
        btnDeleteMember.setEnabled(true);
        btnToMain_Member.setEnabled(false);
        int row = gahanTable.getSelectedRow();
        String custid = CommonUtil.convertObjToStr(gahanTable.getValueAt(row, 1));
        HashMap hashmap = new HashMap();
        hashmap.put("CUST_ID", custid);


        List list = ClientUtil.executeQuery("getCustAddr", hashmap);
        hashmap = null;
        hashmap = (HashMap) list.get(0);
        if (list.size() > 0) {
            lblHouseNameVal.setText(CommonUtil.convertObjToStr(hashmap.get("STREET")));
            lblPlaceVal.setText(CommonUtil.convertObjToStr(hashmap.get("AREA")));
            lblCityVal.setText(CommonUtil.convertObjToStr(hashmap.get("CITY")));
            lblPinCodeVal.setText(CommonUtil.convertObjToStr(hashmap.get("PIN_CODE")));
        }
        String type = CommonUtil.convertObjToStr(gahanTable.getValueAt(row, 3));
        System.out.println("type--" + type);
        if (type != null && !type.equals("") && type.equals("JOINT")) {
            btnToMain_Member.setEnabled(true);
        }
    }//GEN-LAST:event_gahanTableMouseClicked

    private void btnlSave_PropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlSave_PropertyActionPerformed
        // TODO add your handling code here:
        String village = CommonUtil.convertObjToStr(cboVillage.getSelectedItem());
        String sno = txtSurveryNo.getText();
        String area = txtTotalArea.getText();
        String ars=txtARS.getText();
        String nature = CommonUtil.convertObjToStr(cboNature.getSelectedItem());
        String right = CommonUtil.convertObjToStr(cboRight.getSelectedItem());
        String resurvey = txtresurvey.getText();
        String ownerNo = txtOwnerNo.getText();
        String ownerNo2 = txtOwnerNo2.getText();
        int n = tblGahanLandDetails.getRowCount();
        //         int selectrow=tblGahanLandDetails.getSelectedRow();
        StringBuffer message = new StringBuffer("");
        if(txtSurveryNo.getText().equals("")){
            message.append("Enter the Survey Number");
            message.append("\n");
        }
        if (message.length() > 0) {
            displayAlert(message.toString());
            return;
        }
        
        String row = CommonUtil.convertObjToStr(new Integer(tblGahanLandDetails.getRowCount() + 1));
        HashMap hmap = new HashMap();
        if (tblGahanLandDetails.getSelectedRow() >= 0) {
            System.out.println("hiiiii");
            int num = tblGahanLandDetails.getSelectedRow();
            String no = CommonUtil.convertObjToStr(tblGahanLandDetails.getValueAt(tblGahanLandDetails.getSelectedRow(), 0));
            hmap.put("SNO", no);
            hmap.put("VILLAGE", village);
            hmap.put("SARVEYNO", sno);
            hmap.put("ARS", ars);
            hmap.put("AREA", area);
            hmap.put("RESARVEYNO", resurvey);
            hmap.put("RIGHT", right);
            hmap.put("NATURE", nature);
            hmap.put("OWNERNO", ownerNo);
            hmap.put("OWNERNO2", ownerNo2);

            observable.setChangeLandDetails(hmap, num);
        } else {
            System.out.println("    gfhfghgfhgf");
            hmap.put("SNO", row);
            hmap.put("VILLAGE", village);
            hmap.put("SARVEYNO", sno);
            hmap.put("ARS", ars);
            hmap.put("AREA", area);
            hmap.put("RESARVEYNO", resurvey);
            hmap.put("RIGHT", right);
            hmap.put("NATURE", nature);
            hmap.put("OWNERNO", ownerNo);
            hmap.put("OWNERNO2", ownerNo2);
            observable.setLandDetail(hmap);
        }
        observable.setCboNature(nature);
        observable.setCboRight(right);
        cboNature.setSelectedItem("");
        cboRight.setSelectedItem("");
        cboVillage.setSelectedItem("");
        txtSurveryNo.setText("");
        txtARS.setText("");
        txtTotalArea.setText("");
        txtHector.setText("");
        cboVillage.setEnabled(false);
        txtSurveryNo.setEnabled(false);
        txtARS.setEnabled(false);
        txtTotalArea.setEnabled(false);
        txtHector.setText("");
        txtresurvey.setText("");
        txtOwnerNo.setText("");
        txtOwnerNo2.setText("");
        lblPropertyMemberNameVal.setText("");
        lblOwnerTwoName.setText("");
        //Added By Suresh
        btnNew_Property.setEnabled(true);
        btnlSave_Property.setEnabled(false);
        btnDelete_Property.setEnabled(false);
        ClientUtil.enableDisable(panPropertyDetails, false);
    }//GEN-LAST:event_btnlSave_PropertyActionPerformed

    private void btnNew_PropertyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_PropertyActionPerformed
        // TODO add your handling code here:
        cboVillage.setSelectedItem("");
        txtSurveryNo.setText("");
        txtTotalArea.setText("");
        txtHector.setText("");
        txtresurvey.setText("");
        txtHector.setText("");
        cboVillage.setEnabled(true);
        txtSurveryNo.setEnabled(true);
        txtTotalArea.setEnabled(true);
        cboRight.setSelectedItem("");
        cboNature.setSelectedItem("");
        cboRight.setEnabled(true);
        cboNature.setEnabled(true);
        btnNew_Property.setEnabled(false);
        btnlSave_Property.setEnabled(true);
        btnDelete_Property.setEnabled(false);
        ClientUtil.enableDisable(panPropertyDetails, true);
    }//GEN-LAST:event_btnNew_PropertyActionPerformed

    private void cboConstitutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboConstitutionActionPerformed
        // TODO add your handling code here:
        String constitution = CommonUtil.convertObjToStr(((ComboBoxModel) cboConstitution.getModel()).getKeyForSelected());

        // Add your handling code here:

        if (constitution.length() > 0) {
            cboConstitutionActionPerformed(constitution);
            validateConstitutionCustID();
        }
    }//GEN-LAST:event_cboConstitutionActionPerformed
    private void cboConstitutionActionPerformed(String constitution) {
        if (constitution.equals(JOINT_ACCOUNT)) {
            setMemberNewOnlyEnable();
        } else {
            // To delete all the Customer(Excluding Main) Records
            // when Constitution is not Joint Account
            //            checkJointAccntHolderForData();
            setMemberEnableDisable(false);
        }
    }

    private void validateConstitutionCustID() {
    }
    private void btnMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberActionPerformed
        // TODO add your handling code here:
        viewType = SHAREID;
        HashMap sourceMap = new HashMap();
        sourceMap.put("GAHAN_CUSTOMER", "GAHAN_CUSTOMER");
        new CheckCustomerIdUI(this, sourceMap);
    }//GEN-LAST:event_btnMemberActionPerformed

    private void btnDeleteMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteMemberActionPerformed
        // TODO add your handling code here:
        //                if (poaUI.checkCustIDExistInJointAcctAndPoA(CommonUtil.convertObjToStr(tblBorrowerTabCTable.getValueAt(tblBorrowerTabCTable.getSelectedRow(), 1)))){
        //            btnDeleteBorrowerActionPerformed();
        //        }

        int row = gahanTable.getSelectedRow();
        String cusid = CommonUtil.convertObjToStr(gahanTable.getValueAt(row, 1));
        observable.setDeleteCust(cusid);
        String main = CommonUtil.convertObjToStr(gahanTable.getValueAt(row, 3));
        String m = "MAIN";
        if (!main.equals(m)) {
            observable.setGahanaTable(row);
        }

    }//GEN-LAST:event_btnDeleteMemberActionPerformed

    private void btnDeleteBorrowerActionPerformed() {
        updateOBFields();
        setMemberNewOnlyEnable();
        observable.ttNotifyObservers();
    }

    private void setMemberNewOnlyEnable() {
        btnNew_Member.setEnabled(true);
        btnDeleteMember.setEnabled(false);
        btnToMain_Member.setEnabled(false);
    }

    private void setMemberEnableDisable(boolean flag) {
        btnNew_Member.setEnabled(flag);
        btnDeleteMember.setEnabled(flag);
        btnToMain_Member.setEnabled(flag);
    }

    private void btnNew_MemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_MemberActionPerformed
        // Add your handling code here:
        if (gahanTable.getRowCount() != 0) {
            viewType = JOINT_CUST_ID;
            new CheckCustomerIdUI(this);
        } else {
            viewType = JOINT_CUST_ID;
            new CheckCustomerIdUI(this);
        }
    }//GEN-LAST:event_btnNew_MemberActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(150);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnDeleteDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDetailsActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW_MODE);
        popUp(100);
        btnCheck();
        observable.setStatus();
    }//GEN-LAST:event_btnDeleteDetailsActionPerformed

    private void tabCorpCustMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabCorpCustMouseClicked
        // TODO add your handling code here:
        tabCorpFocused = true;
    }//GEN-LAST:event_tabCorpCustMouseClicked

    private void tabCorpCustFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabCorpCustFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tabCorpCustFocusLost

    private void tabCorpCustFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabCorpCustFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tabCorpCustFocusGained

    private void tabCorpCustStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabCorpCustStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tabCorpCustStateChanged

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    /**
     * Populates the Joint Account Holder data in the Screen for the row passed
     * as argument.
     *
     * @param int rowSelected is passed as argument
     */
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        viewType = REJECT;
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        super.removeEditLock(observable.getDocumentGenId());
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        viewType = AUTHORIZE;
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        super.removeEditLock(observable.getDocumentGenId());
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void authorizeStatus(String authorizeStatus) {
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        if (isFilled) {
            //This line added by Rajesh
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
//            Date curdt = currDt.clone();
            String genid = observable.getDocumentGenId();
            whereMap.put("DOCUMENT_GEN_ID", genid);
            whereMap.put("AUTHORIZEDT", currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            System.out.println("wheremap is" + whereMap);
            if (viewType == AUTHORIZE) {
                ClientUtil.execute("AuthorizeGahanaCust", whereMap);
                ClientUtil.execute("AuthorizeGahanaDoc", whereMap);
                ClientUtil.execute("AuthorizeGahanaDocTab", whereMap);
            } else {
                ClientUtil.execute("RejectGahanaCust", whereMap);
                ClientUtil.execute("RejectGahanaDoc", whereMap);
                ClientUtil.execute("RejectGahanaDocTab", whereMap);
            }
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.displayDetails("Gahan Customer");
                this.dispose();
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                authorizeListUI.setFocusToTable();
                authorizeListUI.displayDetails("Gahan Customer");
                this.dispose();
            }
            btnCancelActionPerformed(null);
            isFilled = false;
        } else {
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSelectGahanaAuthList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeGahana");
            isFilled = false;
            setModified(true);
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            lblStatus.setText(observable.getLblStatus());
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setModified(false);
        photoSignMap = new HashMap();
        totAuthPerMap = new HashMap();
        setModified(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        viewType = 0;
        setButtonEnableDisable();
        observable.setStatus();
        setAuthCustIdEnableDisable(false);
        observable.resetFields();
        setLables();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        btnMember.setEnabled(false);
        btnOwnerMemberNumber.setEnabled(false);
        resetAddresDetails();
        super.removeEditLock(observable.getDocumentGenId());
        isFilled = false;
        lblPropertyMemberNameVal.setText("");
        lblOwnerTwoName.setText("");
        bufferList.clear();
        getTableData();
        //Added By Suresh
        btnNew_Property.setEnabled(false);
        btnDelete_Property.setEnabled(false);
        btnlSave_Property.setEnabled(false);
        btnNew_Property1.setEnabled(false);
        btnDelete_Property1.setEnabled(false);
        btnlSave_Property1.setEnabled(false);
        itemsSubmittingList = null;
        if (fromNewAuthorizeUI) {
	           this.dispose();
               newauthorizeListUI.setFocusToTable();
          }
         if (fromAuthorizeUI) {
	           this.dispose();
               authorizeListUI.setFocusToTable();
          }
    }//GEN-LAST:event_btnCancelActionPerformed
    public void setLables() {
    }

    public void resetAddresDetails() {
        lblHouseNameVal.setText("");
        lblPlaceVal.setText("");
        lblCityVal.setText("");
        lblPinCodeVal.setText("");
        lblMemberNameVal.setText("");
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        setModified(false);
        try {

            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panCompanyAuthPersonInfo);
            updateOBFields();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                String DelRemarks = COptionPane.showInputDialog(this, "Delete Remarks");
                System.out.println("######REMARKS" + DelRemarks);
                //               observable.setDeletedRemarks(DelRemarks);
            }
            if (rdoGahanReleasedYes.isSelected()) {
                if (tdtGahanReleasExpDate.getDateValue().equals("")) {
                    ClientUtil.displayAlert("Enter Gahan Release expiry date");
                    return;
                }
            }
            
            String pledgeItem = CommonUtil.convertObjToStr(cboPledge.getSelectedItem());            
            if(pledgeItem != null && !pledgeItem.equals("") && (pledgeItem.equals("Gehan") || pledgeItem.equals("Continuing Guaranty"))){
                int noOfRow = CommonUtil.convertObjToInt(tblDocument.getRowCount());
                if(noOfRow<=0){
                    displayAlert("For Pledge type "+pledgeItem+" Please enter document details");
                    return;
                }
                
            }
            SimpleDateFormat format1= new SimpleDateFormat("dd/MM/yyyy");
            Date curDate=(Date) currDt.clone();
            String pldgeDate=tdtPledgeDate.getDateValue();
            if (tdtPledgeDate.getDateValue().toString().equals("")) {
              ClientUtil.showMessageWindow("Pledge Date should not be empty");
                 return;   
            }
            java.util.Date utilPldgeDte=format1.parse(pldgeDate);
            if(curDate.compareTo(utilPldgeDte)<0)
            {
                ClientUtil.showMessageWindow("Pledge Date should not be greater than Current Date");
                 return; 
            }
            System.out.println("tblDocument.getRowCount()"+tblDocument.getRowCount());
            
            for(int i=0;i<tblDocument.getRowCount();i++)
            {
            String docdate=tblDocument.getValueAt(i, 3).toString();
                System.out.println("docdate:::::"+docdate); 
               
                java.util.Date utilDocDt=format1.parse(docdate);
//                String pldgeDate=tdtPledgeDate.getDateValue();
                
                if((utilPldgeDte.compareTo(utilDocDt))<0)
                {
                    ClientUtil.showMessageWindow("Document Date should not be greater than Pledge Date");
                    panDocumentDetails.setEnabled(true);
                    txtDocumentNo.setEnabled(true);
                    tdtDocumentDt.setEnabled(true);
                    txtRegisteredOffice.setEnabled(true);
                    cboDocumentType.setEnabled(true);
                    return;
                }
            }
            if (pledgeItem != null && !pledgeItem.equals("") && (pledgeItem.equals("Gehan") || pledgeItem.equals("Continuing Guaranty"))) {
                if (tblGahanLandDetails.getRowCount() <= 0) {
                    displayAlert("Save the gahan details");                   
                    return;
                }
            }
            
            //To check mandtoryness of the Customer Personal panel and diplay appropriate
            //error message, else proceed
            //            mandatoryMessage = objCustomerUISupport.checkMandatory(CLASSNAME,panCompanyAuthPersonInfo);
            //            mandatoryMessage += objCustomerUISupport.checkMandatory(CLASSNAME,panKYC);
            //--- For mandatory check for Intro details
            //            final String INTRO = CommonUtil.convertObjToStr(observable.getCbmIntroType().getKeyForSelected());
            //             if (observable.getCbmIntroType().getKeyForSelected()!= "INTRO_NOT_APPLICABLE");
            //            mandatoryMessage = mandatoryMessage + (introducerUI.mandatoryCheck(INTRO));

            //             if(chkSuspendCust.isSelected()||chkRevokeCust.isSelected())
            //                 observable.setIsCustSuspended(true);
            //--- If there is no main Customer alert him.
            //            if(txtAuthCustId.getText().trim().length()==0){
            //                ResourceBundle corpResourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.customer.CorporateCustomerRB", ProxyParameters.LANGUAGE);
            //                mandatoryMessage = mandatoryMessage + corpResourceBundle.getString("CheckForMainCust");
            //            }
            //            if(rdoITDec_Pan.isSelected() && txtPanNumber.getText().length()<=0){
            //                 ClientUtil.displayAlert("Enter Pan Number");
            //                      return;
            //             }
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                ClientUtil.displayAlert(mandatoryMessage);
            } else {
                String alertMsg = "BlockedMsg";
                int optionSelected = 2;
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {

                    /**
                     * Checks up whether the Name entered comes under blocked
                     * list, if so an alert is given with yes no option,if user
                     * wants to save that customer with he can click on yes and
                     * addup that customer, if user does not want to do so, he
                     * can click on No, so that he can change the information of
                     * the customer
                     */
                    //                    if(isBlocked()){
                    //                        optionSelected = observable.showAlertWindow(alertMsg);
                    //                    }
                    if (optionSelected == 1) {
                        return;
                    }
                }
                saveAction();
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().containsKey("CUST_ID")) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print the Gahan Customer Details?", CommonConstants.WARNINGTITLE, COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo == 0) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = new HashMap();
                        paramMap.put("DocumentGenId", CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("CUST_ID")));
                        //paramMap.put("TransDt", observable.getCurrDt()); 
                        //paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        ttIntgration.setParam(paramMap);
                        ttIntgration.integrationForPrint("GahanDocumentPrint", false);
                    }
                }

                //                btnContactNew.setEnabled(false);
                //                btnContactAdd.setEnabled(false);
                //btnReject.setEnabled(true);
                //btnAuthorize.setEnabled(true);
                //btnException.setEnabled(true);
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void saveAction() {
        HashMap resultMap = observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if(observable.getProxyReturnMap().containsKey("CUST_ID")){
                ClientUtil.showMessageWindow("Registered Document ID is : "+CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("CUST_ID")));
            }
            HashMap map = new HashMap();
            map.put("depositNo", observable.getProxyReturnMap().get("CUST_ID"));
            map.put("status", "DELETED");
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            String id = CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("CUST_ID"));
            lst.add("CUSTOMER ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getResult() == ClientConstants.ACTIONTYPE_NEW) {
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CUST_ID")) {
                        lockMap.put("CUSTOMER ID", observable.getProxyReturnMap().get("CUST_ID"));
                    }
                }
            }
            btnCancelActionPerformed(null);
            isFilled = false;
        }
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(EDIT_DELETE);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp(EDIT_DELETE);
        setButtonEnableDisable();
        panCustomerJoint.setEnabled(false);
        panProperty.setEnabled(false);
        btnOwnerMemberNumber.setEnabled(true);
        btnMember.setEnabled(true);
        //Added By Suresh
        ClientUtil.enableDisable(panPropertyDetails, false);
        btnNew_Property.setEnabled(true);
        btnlSave_Property.setEnabled(false);
        btnDelete_Property.setEnabled(false);
        ClientUtil.enableDisable(panDocumentDetails, false);
        btnNew_Property1.setEnabled(true);
        btnlSave_Property1.setEnabled(false);
        btnDelete_Property1.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        ClientUtil.enableDisable(this, true);
        setMemberEnableDisable(false);
        btnMember.setEnabled(true);
        btnOwnerMemberNumber.setEnabled(true);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        //Added By Suresh
        ClientUtil.enableDisable(panDocumentDetails, false);
        ClientUtil.enableDisable(panPropertyDetails, false);
        btnNew_Property.setEnabled(true);
        btnDelete_Property.setEnabled(false);
        btnlSave_Property.setEnabled(false);
        btnNew_Property1.setEnabled(true);
        btnDelete_Property1.setEnabled(false);
        btnlSave_Property1.setEnabled(false);
        btnItemsSubmittingAdd.setEnabled(true);
        lblPropertyMemberNameVal.setText("");
        lblOwnerTwoName.setText("");
        itemsSubmittingList = new ArrayList();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnNew_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_Property1ActionPerformed
        cboDocumentType.setSelectedItem("");
        txtDocumentNo.setText("");
        txtRegisteredOffice.setText("");
        tdtDocumentDt.setDateValue(null);
        cboDocumentType.setEnabled(true);
        txtDocumentNo.setEnabled(true);
        txtRegisteredOffice.setEnabled(true);
        tdtDocumentDt.setEnabled(true);
        ClientUtil.enableDisable(panDocumentDetails, true);
        btnNew_Property1.setEnabled(false);
        btnlSave_Property1.setEnabled(true);
        btnDelete_Property1.setEnabled(false);
    }//GEN-LAST:event_btnNew_Property1ActionPerformed

    public String ToHector(String cents) {
        double hector = 0.00;
        double cent = 0.00;
        String hectors = "";
        if (!cents.equals("")) {
            cent = CommonUtil.convertObjToDouble(cents);
            hector = cent * (0.004046853);
            hectors = df.format(hector);
        }
        return hectors;
    }

    public String ToCent(String hectors) {
        double cent = 0.00;
        double hector = 0.00;
        String cents = "";
        if (!hectors.equals("")) {
            hector = CommonUtil.convertObjToDouble(hectors);
            cent = hector * (247.105407259);
            cents = df.format(cent);
        }
        return cents;

    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void btnlSave_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlSave_Property1ActionPerformed
        // TODO add your handling code here:
        try{

        StringBuffer message = new StringBuffer("");
        if (txtDocumentNo.getText().equals("")) {
            message.append("Enter the Document Number");
            message.append("\n");
        }
        if (tdtDocumentDt.getDateValue().toString().equals("")) {
            message.append("Choose the Document Date");
            message.append("\n");
        }
        if (txtRegisteredOffice.getText().equals("")) {
            message.append("Enter the Registered Office");
            message.append("\n");
        }

        if (cboDocumentType.getSelectedIndex() == 0) {
            message.append("Enter the Document Type");
            message.append("\n");
        }


        if (message.length() > 0) {
            displayAlert(message.toString());
            return;
        }
        java.util.Date utilPldgedt=null;
        String docType = CommonUtil.convertObjToStr(cboDocumentType.getSelectedItem());
        String dno = txtDocumentNo.getText();
        String offic = txtRegisteredOffice.getText();
        String docDate = tdtDocumentDt.getDateValue();
        System.out.println("docDate"+docDate);
        SimpleDateFormat format1= new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date utilDocDate=format1.parse(docDate);
        Date curdt = (Date) currDt.clone();
        String pldgeDte=tdtPledgeDate.getDateValue();
            System.out.println("pldgeDte"+pldgeDte);
        if(!pldgeDte.equals(""))
        utilPldgedt=format1.parse(pldgeDte);
            System.out.println("curdt"+curdt);
            System.out.println("curdt.compareTo(utilDocDate)"+curdt.compareTo(utilDocDate));
            if(curdt.compareTo(utilDocDate)<0)
            {
                ClientUtil.showMessageWindow("Document Date should not be greater than current date");
                return;
            }
           if(utilPldgedt!=null)
               
           {
            if(utilPldgedt.compareTo(utilDocDate)<0)
            {
                ClientUtil.showMessageWindow("Document Date should not be greater than Pledge Date");
                return;
            }
           }
                
            
        // int slno=tblDocument.getRowCount();
        //         int selectrow=tblGahanLandDetails.getSelectedRow();

        String row = CommonUtil.convertObjToStr(new Integer(tblDocument.getRowCount() + 1));
        System.out.println("rowwww " + row);
        HashMap hmap = new HashMap();
        if (tblDocument.getSelectedRow() >= 0) {
            // ty
            int num = tblDocument.getSelectedRow();
            String slno = CommonUtil.convertObjToStr(tblDocument.getValueAt(tblDocument.getSelectedRow(), 0));
            hmap.put("SNO", slno);
            hmap.put("DOC_NO", dno);
            hmap.put("DOC_TYPE", docType);
            hmap.put("DOC_DATE", docDate);
            hmap.put("REG_OFFIC", offic);
            bufferList.set(num, hmap);
        } else {
            hmap.put("SNO", row);
            hmap.put("DOC_NO", dno);
            hmap.put("DOC_TYPE", docType);
            hmap.put("DOC_DATE", docDate);
            hmap.put("REG_OFFIC", offic);
            bufferList.add(hmap);
        }
        cboDocumentType.setSelectedItem("");
        txtDocumentNo.setText("");
        txtRegisteredOffice.setText("");
        tdtDocumentDt.setDateValue(null);
        cboDocumentType.setEnabled(false);
        txtDocumentNo.setEnabled(false);
        txtRegisteredOffice.setEnabled(false);
        tdtDocumentDt.setEnabled(false);
        getTableData();
        btnNew_Property1.setEnabled(true);
        btnlSave_Property1.setEnabled(false);
        btnDelete_Property1.setEnabled(false);
        ClientUtil.enableDisable(panDocumentDetails, false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnlSave_Property1ActionPerformed

    private void btnDelete_Property1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_Property1ActionPerformed
        // TODO add your handling code here:
        if (tblDocument.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to Delete", "Error", JOptionPane.ERROR_MESSAGE);
            //f=false;
            // tabedit=false;
            return;
        } else {
            int row = tblDocument.getSelectedRow();
            System.out.println("ROWWWWW>>>" + row + ">>>>>>>>>>" + bufferList.size());

            // drmOB=(DeathReliefMasterOB)buffer1(row);
//           tdtDrfFromDt.setDateValue(tblInterest.getValueAt(row,0).toString());
//           txtToDt.setText(tblInterest.getValueAt(row, 1).toString());
//           txtInterestRate.setText(tblInterest.getValueAt(row, 2).toString()); 
            cboDocumentType.setSelectedItem("");
            txtDocumentNo.setText("");
            txtRegisteredOffice.setText("");
            tdtDocumentDt.setDateValue(null);
            cboDocumentType.setEnabled(false);
            txtDocumentNo.setEnabled(false);
            txtRegisteredOffice.setEnabled(false);
            tdtDocumentDt.setEnabled(false);

            HashMap temp = new HashMap();
            temp = (HashMap) bufferList.get(row);
            System.out.println("temp  " + temp);
            int no = Integer.parseInt(temp.get("SNO").toString());
            bufferList.remove(row);
            System.out.println("no  " + no + "bufferList   " + bufferList);
            System.out.println("row+1 " + (row + 1) + " bufferList.size  " + bufferList.size());
            for (int i = row; i < bufferList.size(); i++) {
                HashMap amap = new HashMap();
                amap = (HashMap) bufferList.get(i);
                bufferList.remove(i);
                amap.put("SNO", no);
                bufferList.add(i, amap);


                no = no + 1;
                System.out.println("bufff   " + bufferList + "  dfjd no  " + no);
            }
            // bufferList.remove(row); 
            getTableData();
            btnNew_Property1.setEnabled(true);
            btnlSave_Property1.setEnabled(false);
            btnDelete_Property1.setEnabled(false);
            ClientUtil.enableDisable(panDocumentDetails, false);
        }
    }//GEN-LAST:event_btnDelete_Property1ActionPerformed

    private void tblDocumentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDocumentMouseClicked
        int row = tblDocument.getSelectedRow();
        txtDocumentNo.setText(CommonUtil.convertObjToStr(tblDocument.getValueAt(row, 1)));
        cboDocumentType.setSelectedItem(CommonUtil.convertObjToStr(tblDocument.getValueAt(row, 2)));

        // txtTotalArea.setText(CommonUtil.convertObjToStr(tblDocument.getValueAt(row, 3)));
        tdtDocumentDt.setDateValue(CommonUtil.convertObjToStr(tblDocument.getValueAt(row, 3)));
        txtRegisteredOffice.setText(CommonUtil.convertObjToStr(tblDocument.getValueAt(row, 4)));
        btnNew_Property1.setEnabled(false);
        btnlSave_Property1.setEnabled(true);
        btnDelete_Property1.setEnabled(true);
    }//GEN-LAST:event_tblDocumentMouseClicked

    private void txtTotalAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalAreaFocusLost
        // TODO add your handling code here:
        if (hr == 0) {
            if (!txtTotalArea.getText().equals("")) {
                String hec = ToHector(txtTotalArea.getText());
                ct = 1;
                if (valueSet == false) {
                    txtHector.setText(hec);
                }
                valueSet = true;
                cboNature.requestFocus(true);
            }
        }else {
            hr = 0;
        }
    }//GEN-LAST:event_txtTotalAreaFocusLost

    private void txtHectorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHectorFocusLost
        // TODO add your handling code here:
        if (ct == 0) {
            if (!txtHector.getText().equals("")) {
                String cent = ToCent(txtHector.getText());
                hr = 1;
                if(valueSet==false){
                    txtTotalArea.setText(cent);
                }
                valueSet = true;
                cboNature.requestFocus(true);
            }
        } else {
            ct = 0;
        }
    }//GEN-LAST:event_txtHectorFocusLost

    private void txtTotalAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalAreaActionPerformed
        // TODO add your handling code here:
        valueSet = false;
        ct = 1;
        hr = 0;
    }//GEN-LAST:event_txtTotalAreaActionPerformed

    private void txtHectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHectorActionPerformed
        // TODO add your handling code here:
        valueSet = false;
        hr = 1;
        ct = 0;
    }//GEN-LAST:event_txtHectorActionPerformed

    private void cboDocumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDocumentTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboDocumentTypeActionPerformed

    private void btnOwnerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOwnerNoActionPerformed
        // TODO add your handling code here:
        viewType = OWNER_NO;
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnOwnerNoActionPerformed

    private void txtARSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtARSActionPerformed
        // TODO add your handling code here
        valueSet = false;
    }//GEN-LAST:event_txtARSActionPerformed

    private void txtARSFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtARSFocusLost
        // TODO add your handling code here:
        txtTotalArea.setText(CommonUtil.convertObjToStr(df.format(CommonUtil.convertObjToDouble(txtARS.getText())*2.47)));
        if(valueSet==false){
            txtTotalAreaFocusLost(null);
        }
        valueSet = true;
    }//GEN-LAST:event_txtARSFocusLost

    private void tdtPledgeDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPledgeDateFocusLost
        // TODO add your handling code here:
        int gahanPeriod = TrueTransactMain.GAHAN_PERIOD;
        String pldgDate = tdtPledgeDate.getDateValue();
        Date dat = null;
        Date formatteddate = null;
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        try {
            dat = df.parse(pldgDate);
            Calendar date = Calendar.getInstance();            
            date.setTime(dat);            
            Format f = new SimpleDateFormat("dd/mm/yyyy");            
            date.add(Calendar.YEAR, gahanPeriod);            
            tdtGahanExpDt.setDateValue(f.format(date.getTime()));            
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
    }//GEN-LAST:event_tdtPledgeDateFocusLost

    private void btnOwnerNo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOwnerNo2ActionPerformed
        // TODO add your handling code here:
        // Added by nithya on 30-08-2018 for KD 191 - Add in Gahan Customer owner property name one more members
        viewType = OWNER_NO2;
        new CheckCustomerIdUI(this);       
    }//GEN-LAST:event_btnOwnerNo2ActionPerformed

    private void btnItemsSubmittingAddActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        // TODO add your handling code here:
        if (!CommonUtil.convertObjToStr(cboItemsSubmitting.getSelectedItem()).equals("")) {
            String submittingItem = CommonUtil.convertObjToStr(cboItemsSubmitting.getSelectedItem());
            if (!itemsSubmittingList.contains(submittingItem)) {
                itemsSubmittingList.add(submittingItem);
                observable.setItemsSubmittingList(itemsSubmittingList);
                cboItemsSubmitting.setSelectedItem("");
            } else {
                ClientUtil.displayAlert("Item already added , Select other item if required");
                cboItemsSubmitting.setSelectedItem("");
                return;
            }
        } else {
            ClientUtil.displayAlert("Please Select any item to add");
            return;
        }

    }

	private void btnItemsSubmittingRemoveActionPerformed(java.awt.event.ActionEvent evt) {                                                         
	   new CheckGahanCustomerUI(itemsSubmittingList);
	}
     /**
     * To display customer list popup for Edit & Delete options
     */
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap where = new HashMap();
        where.put("BRANCH_CODE", getSelectedBranchID());
        if (field == EDIT_DELETE || field == ENQUIRY) {
            ArrayList lst = new ArrayList();
            String id = CommonUtil.convertObjToStr(observable.getDocumentGenId());
            int n = observable.getActionType();
            if (n == ClientConstants.ACTIONTYPE_DELETE) {
                viewMap.put(CommonConstants.MAP_NAME, "getGahanCustomerForDelete");
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "getGahanCustomer");
            }
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        new com.see.truetransact.ui.common.viewall.ViewAll(this, viewMap).show();
    }

    /**
     * To get data based on customer id received from the popup and populate
     * into the screen
     */
    public void fillData(Object obj) {
        final HashMap hash = (HashMap) obj;
        System.out.println("hash####" + hash);
        observable.setLoanSecurityTableData(hash);
        String genid = CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID"));
        HashMap hashmap1 = new HashMap();
        hashmap1.put("DOCUMENT_GEN_ID", genid);
        List list1 = ClientUtil.executeQuery("getAuthorized", hashmap1);
        System.out.println("viewtype is"+viewType);
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                 btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
        if (viewType == SHAREID) {
            if (hash.containsKey("CUSTOMER ID")) {
                hash.put("CUST_ID", hash.get("CUSTOMER ID"));
            }
            if (hash.containsKey("NAME")) {
                hash.put("NAME", hash.get("NAME"));
            }
            if (hash.get("CUST_TYPE").equals("Individual")) {
                hash.put("CUST_TYPE", "INDIVIDUAL");
            }
            observable.setTxtMemberNo(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
            System.out.println("@#@#$%%%^ member no : "+CommonUtil.convertObjToStr(hash.get("MEMBER_NO"))+" "+CommonUtil.convertObjToStr(hash.get("NAME")));
            lblMemberNameVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")) +" "+CommonUtil.convertObjToStr(hash.get("NAME")));
            observable.getCbmConstitution().setKeyForSelected(CommonUtil.convertObjToStr(hash.get("CUST_TYPE")));
            observable.setJointCustomerId(hash);
            String custid = observable.getTxtMemberNo();
            HashMap hashmap = new HashMap();
            hashmap.put("CUST_ID", custid);
            List list = ClientUtil.executeQuery("getCustAddr", hashmap);
            hashmap = null;
            if (list.size() > 0) {
                hashmap = (HashMap) list.get(0);
                lblHouseNameVal.setText(CommonUtil.convertObjToStr(hashmap.get("STREET")));
                lblPlaceVal.setText(CommonUtil.convertObjToStr(hashmap.get("AREA")));
                lblCityVal.setText(CommonUtil.convertObjToStr(hashmap.get("CITY")));
                lblPinCodeVal.setText(CommonUtil.convertObjToStr(hashmap.get("PIN_CODE")));
                txtMemberType.setText(CommonUtil.convertObjToStr(hashmap.get("SHARE_TYPE")));
            }
        }

        if (viewType == OWNER_ID) {
            if (hash.containsKey("CUST_ID")) {
                hash.put("CUSTOMER ID", hash.get("CUST_ID"));
            }
            observable.setTxtOwnerMemberNumber(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            txtOwnerMemberNumber.setText(observable.getTxtOwnerMemberNumber());
        }
        if (viewType == OWNER_NO) {
            if (hash.containsKey("CUST_ID")) {
                hash.put("CUSTOMER ID", hash.get("CUST_ID"));
            }
            observable.setTxtOwnerNo(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            txtOwnerNo.setText(observable.getTxtOwnerNo());
            //Added By Suresh
            lblPropertyMemberNameVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NO")));
        }
        if (viewType == OWNER_NO2) {// Added by nithya on 30-08-2018 for KD 191 - Add in Gahan Customer owner property name one more members
            if (hash.containsKey("CUST_ID")) {
                hash.put("CUSTOMER ID", hash.get("CUST_ID"));
            }
            observable.setTxtOwnerNo2(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            txtOwnerNo2.setText(observable.getTxtOwnerNo2());            
            lblOwnerTwoName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
        }

        if (viewType == JOINT_CUST_ID) {
            observable.setJointCustomerId(hash);

        }
        if (viewType == EDIT_DELETE || viewType == AUTHORIZE || viewType == REJECT || viewType == ENQUIRY) {
            if (viewType == AUTHORIZE || viewType == REJECT) {
                ClientUtil.enableDisable(panDocumentDetails, false);
                ClientUtil.enableDisable(panPropertyDetails, false);
                btnNew_Property.setEnabled(false);
                btnDelete_Property.setEnabled(false);
                btnlSave_Property.setEnabled(false);
                ClientUtil.enableDisable(panProperty, false);
                if (viewType == AUTHORIZE){
	                btnAuthorize.setEnabled(true);
	                btnAuthorize.requestFocusInWindow();
               }
            } else {
                ClientUtil.enableDisable(panDocumentDetails, true);
                ClientUtil.enableDisable(panCustomerDetails, true);
                ClientUtil.enableDisable(panPropertyDetails, true);
//                ClientUtil.enableDisable(panRightDetails,true);
                ClientUtil.enableDisable(panPledgeDetails, true);
                ClientUtil.enableDisable(panOherDetails, true);
                btnNew_Property.setEnabled(true);
                btnDelete_Property.setEnabled(true);
                btnlSave_Property.setEnabled(true);
            }

            List lst = ClientUtil.executeQuery("getCustdetails", hash);
            HashMap hmap = new HashMap();
            hmap = (HashMap) lst.get(0);
            System.out.println("$$$$$$$$$$$$$$$$$ hmap : "+hash);
            observable.setTxtMemberNo(CommonUtil.convertObjToStr(hmap.get("CUST_ID")));
            observable.setTxtOwnerMemberNumber(CommonUtil.convertObjToStr(hash.get("OWNER_MEMBER")));
            //Added By Suresh
            lblMemberNameVal.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO"))+"  " +CommonUtil.convertObjToStr(hmap.get("NAME")));
            observable.getCbmConstitution().setKeyForSelected(CommonUtil.convertObjToStr(hmap.get("CONSTITUTION")));
            txtMemberType.setText(CommonUtil.convertObjToStr(hmap.get("SHARE_TYPE")));
            observable.setMainCustTable(hash);
            String s = "JOINT_ACCOUNT";
            if (hmap.get("CONSTITUTION").equals(s)) {
                observable.setTableData(hash);
            }
            String custid = observable.getTxtMemberNo();
            HashMap hashmap = new HashMap();
            hashmap.put("CUST_ID", custid);
            List list = ClientUtil.executeQuery("getCustAddr", hashmap);
            hashmap = null;
            if (list.size() > 0) {
                hashmap = (HashMap) list.get(0);
                lblHouseNameVal.setText(CommonUtil.convertObjToStr(hashmap.get("STREET")));
                lblPlaceVal.setText(CommonUtil.convertObjToStr(hashmap.get("AREA")));
                lblCityVal.setText(CommonUtil.convertObjToStr(hashmap.get("CITY")));
                lblPinCodeVal.setText(CommonUtil.convertObjToStr(hashmap.get("PIN_CODE")));
            }
            String custno = txtMemberNo.getText();
            String docgen = CommonUtil.convertObjToStr(hash.get("DOCUMENT_GEN_ID"));
            observable.setDocumentGenId(docgen);
            HashMap map = new HashMap();
            map.put("DOCUMENT_GEN_ID", docgen);
            System.out.println("$$$$$$$$$$$$$$$$$ hmap 11111 : "+hash);
            List st = ClientUtil.executeQuery("getGahanDocDetails", map);
            System.out.println("$$$$$$$$$$$$$$$$$ hmap after : "+hash);
            List proplist = ClientUtil.executeQuery("getPropDetails", map);
            List docDetails = ClientUtil.executeQuery("getDocDetails", map);
            bufferList.clear();
            for (int i = 0; i < docDetails.size(); i++) {
                HashMap docMap = new HashMap();
                HashMap temp = new HashMap();
                docMap = (HashMap) docDetails.get(i);
                temp.put("DOC_NO", CommonUtil.convertObjToStr(docMap.get("DOCUMENT_NO")));
                temp.put("DOC_TYPE", CommonUtil.convertObjToStr(docMap.get("DOCUMENT_TYPE")));
                temp.put("REG_OFFIC", CommonUtil.convertObjToStr(docMap.get("REGISTRED_OFFICE")));
                temp.put("SNO", CommonUtil.convertObjToStr(docMap.get("SL_NO")));
                temp.put("DOC_DATE", CommonUtil.convertObjToStr(docMap.get("DOCUMENT_DT")));
                bufferList.add(temp);
            }
            getTableData();
            map = null;
            System.out.println("print"+st);//Jeffin
            hmap = (HashMap) st.get(0);
            observable.populateDocOB(hmap);
            if (observable.isGahanReleaseNo() == true) {
                tdtGahanReleasExpDate.setEnabled(false);
                txtGahanReleaseNo.setEnabled(false);
            }
            hashmap1 = null;
            hashmap1 = (HashMap) list1.get(0);
            String authorized = CommonUtil.convertObjToStr(hashmap1.get("AUTHORIZE_STATUS"));
            if (authorized.equalsIgnoreCase("AUTHORIZED")) {
                txtDocumentNo.setEnabled(false);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
            }
            int pledge = CommonUtil.convertObjToInt(observable.getTxtPledgeAmount());
            int n = tblLoansAvailedAgainstSecurity.getRowCount();
            int amount = 0;
            int security = 0;

            if (n > 0) {

                for (int i = 0; i < n; i++) {
                    amount = amount + CommonUtil.convertObjToInt(tblLoansAvailedAgainstSecurity.getValueAt(i, 4));

                }

                int tot = pledge - amount;

                txtAvailSecurityValue.setText(CommonUtil.convertObjToStr(new Integer(tot)));

            } else {
                txtAvailSecurityValue.setText(CommonUtil.convertObjToStr(new Integer(pledge)));
            }

            hmap = null;
            if (proplist.size() > 0) {
                hmap = (HashMap) proplist.get(0);
            }
            txtSurveryNo.setEnabled(false);
            //            observable.populatePropOB(hmap);
            observable.setPropertyTableData(proplist);
            if (viewType == EDIT_DELETE) {
                super.removeEditLock(observable.getDocumentGenId());
            }
            update(null, null);
            isFilled = true;
            proplist = null;
            st = null;
            list = null;
        }
        if(rejectFlag==1){
           btnReject.setEnabled(false);
       }
    }

    private void JointAcctDisplay(String custId) {
    }

    private void CustInfoDisplay(String custId) {
    }

    private void updateCustomerDetails() {
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(false);
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnAuthorize.setEnabled(btnAuthorize.isEnabled());
        // btnReject.setEnabled(false);
        btnReject.setEnabled(btnAuthorize.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnDeleteDetails.setEnabled(!btnDeleteDetails.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    /**
     * To enable or disable the Authorized Customer ID text box based on whether
     * the Customer is an existing customer or not
     *
     */
    private void setAuthCustIdEnableDisable(boolean authCustId) {
    }

    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteDetails;
    private com.see.truetransact.uicomponent.CButton btnDeleteMember;
    private com.see.truetransact.uicomponent.CButton btnDelete_Property;
    private com.see.truetransact.uicomponent.CButton btnDelete_Property1;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnItemsSubmittingAdd;
    private com.see.truetransact.uicomponent.CButton btnItemsSubmittingRemove;
    private com.see.truetransact.uicomponent.CButton btnMember;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_Member;
    private com.see.truetransact.uicomponent.CButton btnNew_Property;
    private com.see.truetransact.uicomponent.CButton btnNew_Property1;
    private com.see.truetransact.uicomponent.CButton btnOwnerMemberNumber;
    private com.see.truetransact.uicomponent.CButton btnOwnerNo;
    private com.see.truetransact.uicomponent.CButton btnOwnerNo2;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnToMain_Member;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnlSave_Property;
    private com.see.truetransact.uicomponent.CButton btnlSave_Property1;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CComboBox cboConstitution;
    private com.see.truetransact.uicomponent.CComboBox cboDocumentType;
    private com.see.truetransact.uicomponent.CComboBox cboItemsSubmitting;
    private com.see.truetransact.uicomponent.CComboBox cboNature;
    private com.see.truetransact.uicomponent.CComboBox cboPledge;
    private com.see.truetransact.uicomponent.CComboBox cboRight;
    private com.see.truetransact.uicomponent.CComboBox cboVillage;
    private com.see.truetransact.uicomponent.CScrollPane documentScrollPan;
    private com.see.truetransact.uicomponent.CTable gahanTable;
    private com.see.truetransact.uicomponent.CScrollPane gahanTableScrollPan;
    private com.see.truetransact.uicomponent.CScrollPane gahanTableScrollPan2;
    private com.see.truetransact.uicomponent.CLabel lbkPinCode;
    private com.see.truetransact.uicomponent.CLabel lblARS;
    private com.see.truetransact.uicomponent.CLabel lblAvailSecurityValue;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCityVal;
    private com.see.truetransact.uicomponent.CLabel lblConstitution;
    private com.see.truetransact.uicomponent.CLabel lblDocumentDt;
    private com.see.truetransact.uicomponent.CLabel lblDocumentNo;
    private com.see.truetransact.uicomponent.CLabel lblDocumentType;
    private com.see.truetransact.uicomponent.CLabel lblGahanExpDt;
    private com.see.truetransact.uicomponent.CLabel lblGahanReleaseNo;
    private com.see.truetransact.uicomponent.CLabel lblGahanReleasedDate;
    private com.see.truetransact.uicomponent.CLabel lblGahanReleasedExpiryDate;
    private com.see.truetransact.uicomponent.CLabel lblHector;
    private com.see.truetransact.uicomponent.CLabel lblHouseName;
    private com.see.truetransact.uicomponent.CLabel lblHouseNameVal;
    private com.see.truetransact.uicomponent.CLabel lblItemsSubmitting;
    private com.see.truetransact.uicomponent.CLabel lblMemberNameVal;
    private com.see.truetransact.uicomponent.CLabel lblMemberType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNature;
    private com.see.truetransact.uicomponent.CLabel lblOwnerMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblOwnerMemberNumber;
    private com.see.truetransact.uicomponent.CLabel lblOwnerNo;
    private com.see.truetransact.uicomponent.CLabel lblOwnerTwoName;
    private com.see.truetransact.uicomponent.CLabel lblPinCodeVal;
    private com.see.truetransact.uicomponent.CLabel lblPlace;
    private com.see.truetransact.uicomponent.CLabel lblPlaceVal;
    private com.see.truetransact.uicomponent.CLabel lblPledge;
    private com.see.truetransact.uicomponent.CLabel lblPledgeAmount;
    private com.see.truetransact.uicomponent.CLabel lblPledgeDate;
    private com.see.truetransact.uicomponent.CLabel lblPledgeNo;
    private com.see.truetransact.uicomponent.CLabel lblPropertyMemberNameVal;
    private com.see.truetransact.uicomponent.CLabel lblRegisteredOffice;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblResurvey;
    private com.see.truetransact.uicomponent.CLabel lblRight;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSurveryNo;
    private com.see.truetransact.uicomponent.CLabel lblTotalArea;
    private com.see.truetransact.uicomponent.CLabel lblVillage;
    private com.see.truetransact.uicomponent.CMenuBar mbrCorporateCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCompanyAuthPersonInfo;
    private com.see.truetransact.uicomponent.CPanel panCompanyAuthPersonInfo1;
    private com.see.truetransact.uicomponent.CPanel panCorporateCustomer;
    private com.see.truetransact.uicomponent.CPanel panCustomerDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomerJoint;
    private com.see.truetransact.uicomponent.CPanel panDocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panGahanCustomerDetails1;
    private com.see.truetransact.uicomponent.CPanel panGahanData;
    private com.see.truetransact.uicomponent.CPanel panGahanTableData;
    private com.see.truetransact.uicomponent.CPanel panItemsSubmitting;
    private com.see.truetransact.uicomponent.CPanel panMemberButton;
    private com.see.truetransact.uicomponent.CPanel panOherDetails;
    private com.see.truetransact.uicomponent.CPanel panOwnerMemberNumber;
    private com.see.truetransact.uicomponent.CPanel panPledgeDetails;
    private com.see.truetransact.uicomponent.CPanel panProperty;
    private com.see.truetransact.uicomponent.CPanel panProperty1;
    private com.see.truetransact.uicomponent.CPanel panPropertyDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgExistingCust;
    private com.see.truetransact.uicomponent.CRadioButton rdoGahanReleasedNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoGahanReleasedYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGender;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMaritalStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpLockerRentSIApplication;
    private com.see.truetransact.uicomponent.CTabbedPane tabCorpCust;
    private com.see.truetransact.uicomponent.CTable tblDocument;
    private com.see.truetransact.uicomponent.CTable tblGahanLandDetails;
    private com.see.truetransact.uicomponent.CTable tblLoansAvailedAgainstSecurity;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDocumentDt;
    private com.see.truetransact.uicomponent.CDateField tdtGahanExpDt;
    private com.see.truetransact.uicomponent.CDateField tdtGahanReleasExpDate;
    private com.see.truetransact.uicomponent.CDateField tdtPledgeDate;
    private com.see.truetransact.uicomponent.CTextField txtARS;
    private com.see.truetransact.uicomponent.CTextField txtAvailSecurityValue;
    private javax.swing.JTextField txtDocumentNo;
    private com.see.truetransact.uicomponent.CTextField txtGahanReleaseNo;
    private com.see.truetransact.uicomponent.CTextField txtHector;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private javax.swing.JTextField txtMemberType;
    private com.see.truetransact.uicomponent.CTextField txtOwnerMemberNumber;
    private com.see.truetransact.uicomponent.CTextField txtOwnerNo;
    private com.see.truetransact.uicomponent.CTextField txtOwnerNo2;
    private com.see.truetransact.uicomponent.CTextField txtPledgeAmount;
    private com.see.truetransact.uicomponent.CTextField txtPledgeNo;
    private javax.swing.JTextField txtRegisteredOffice;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSurveryNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalArea;
    private com.see.truetransact.uicomponent.CTextField txtresurvey;
    // End of variables declaration//GEN-END:variables
}
