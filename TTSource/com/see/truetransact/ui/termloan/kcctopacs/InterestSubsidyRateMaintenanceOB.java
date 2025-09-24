/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestSubsidyRateMaintenanceOB.java
 *
 * Created on May 24, 2004, 11:15 AM
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.TrueTransactMain;

import com.see.truetransact.clientutil.CMandatoryDialog;

import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyRateMaintenanceGroupTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyRateMaintenanceCategotyTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyRateMaintenanceProdTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyRateMaintenanceTypeTO;

import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Iterator;

import java.util.Set;

import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;

import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author rahul
 */
public class InterestSubsidyRateMaintenanceOB extends CObservable {

    private ProxyFactory proxy = null;
    /*
     * To Ste the Titles for the Tables...
     */
    ArrayList productTabTitle = new ArrayList();
    ArrayList categoryTabTitle = new ArrayList();
    ArrayList interestTabTitle = new ArrayList();
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private ArrayList key;
    private ArrayList value;
    private EnhancedTableModel tblProduct;
    private EnhancedTableModel tblCategory;
    private EnhancedTableModel tblInterestTable;
    private ArrayList IncVal = new ArrayList();
    private HashMap _authorizeMap;
    private String txtGroupName = "";
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmInstitution = null;
    private String lblroiGroupId = "";
    private String cboInstitution = "";
    private String txtRefno = "";
    private String tdtDateReceived = "";
    private String tdtFromDate = "";
    private String tdtToDate = "";
    private Double txtSubsidyInt = null;
    private Double txtRateInterest = null;
    private Double txtCustRateInt = null;
    private Double txtMiscellaneousInt = null;
    private String txtRemarks = "";
    private boolean rdoInterestSubsidy;
    private boolean rdoInterestSubvention;
    final ArrayList tableTitle = new ArrayList();
    private LinkedHashMap rateTypemap;
    private LinkedHashMap categoryeMap;
    private LinkedHashMap productMap;
    private ArrayList productTabRow;
    private ArrayList categoryTabRow;
    private InterestSubsidyRateMaintenanceTypeTO existInterestMaintenanceRateTO = null;
    Date curDate = null;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private String lblRoiGroupId = "";
    private String interRateType = "";
    private String AuthorizeStatus;
    public boolean isTableSet = false;
    private boolean TypenewData = false;
    private String tdtDate = "";
    private String txtPenalInterest = "";
    private String cboProdType = "";
    private boolean rdoInterestType_Debit = false;
    private boolean rdoInterestType_Credit = false;

    /**
     * Getter for property tblInterestTable.
     *
     * @return Value of property tblInterestTable.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInterestTable() {
        return tblInterestTable;
    }

    /**
     * Setter for property tblInterestTable.
     *
     * @param tblInterestTable New value of property tblInterestTable.
     */
    public void setTblInterestTable(com.see.truetransact.clientutil.EnhancedTableModel tblInterestTable) {
        this.tblInterestTable = tblInterestTable;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    public String getTdtDateReceived() {
        return tdtDateReceived;
    }

    public void setTdtDateReceived(String tdtDateReceived) {
        this.tdtDateReceived = tdtDateReceived;
    }

    public String getTdtFromDate() {
        return tdtFromDate;
    }

    public void setTdtFromDate(String tdtFromDate) {
        this.tdtFromDate = tdtFromDate;
    }

    public String getTdtToDate() {
        return tdtToDate;
    }

    public void setTdtToDate(String tdtToDate) {
        this.tdtToDate = tdtToDate;
    }

    public String getCboInstitution() {
        return cboInstitution;
    }

    public void setCboInstitution(String cboInstitution) {
        this.cboInstitution = cboInstitution;
    }

    public ComboBoxModel getCbmInstitution() {
        return cbmInstitution;
    }

    public void setCbmInstitution(ComboBoxModel cbmInstitution) {
        this.cbmInstitution = cbmInstitution;
    }

    public String getLblroiGroupId() {
        return lblroiGroupId;
    }

    public void setLblroiGroupId(String lblroiGroupId) {
        this.lblroiGroupId = lblroiGroupId;
    }

    public Double getTxtCustRateInt() {
        return txtCustRateInt;
    }

    public void setTxtCustRateInt(Double txtCustRateInt) {
        this.txtCustRateInt = txtCustRateInt;
    }

    public Double getTxtRateInterest() {
        return txtRateInterest;
    }

    public void setTxtRateInterest(Double txtRateInterest) {
        this.txtRateInterest = txtRateInterest;
    }

    public String getTxtRefno() {
        return txtRefno;
    }

    public void setTxtRefno(String txtRefno) {
        this.txtRefno = txtRefno;
    }

    public String getTxtRemarks() {
        return txtRemarks;
    }

    public void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    public Double getTxtSubsidyInt() {
        return txtSubsidyInt;
    }

    public void setTxtSubsidyInt(Double txtSubsidyInt) {
        this.txtSubsidyInt = txtSubsidyInt;
    }

    public boolean isRdoInterestSubsidy() {
        return rdoInterestSubsidy;
    }

    public void setRdoInterestSubsidy(boolean rdoInterestSubsidy) {
        this.rdoInterestSubsidy = rdoInterestSubsidy;
    }

    public boolean isRdoInterestSubvention() {
        return rdoInterestSubvention;
    }

    public void setRdoInterestSubvention(boolean rdoInterestSubvention) {
        this.rdoInterestSubvention = rdoInterestSubvention;
    }

    public Double getTxtMiscellaneousInt() {
        return txtMiscellaneousInt;
    }

    public void setTxtMiscellaneousInt(Double txtMiscellaneousInt) {
        this.txtMiscellaneousInt = txtMiscellaneousInt;
    }

    public int getRowCount() {
        int rowCount = 0;
        rowCount = tblInterestTable.getRowCount();

        return rowCount;
    }
    private final static Logger log = Logger.getLogger(InterestSubsidyRateMaintenanceUI.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    /**
     * To get the Value of Column Title and Dialogue Box...
     */
    //    final InterestMaintenanceRB objInterestMaintenanceRB = new InterestMaintenanceRB();
    java.util.ResourceBundle objInterestMaintenanceRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.deposit.interestmaintenance.InterestMaintenanceRB", ProxyParameters.LANGUAGE);
    private static InterestSubsidyRateMaintenanceOB interestRateMaintenanceOB;

    static {
        try {
            log.info("In InwardClearingOB Declaration");
            interestRateMaintenanceOB = new InterestSubsidyRateMaintenanceOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public static InterestSubsidyRateMaintenanceOB getInstance() {
        return interestRateMaintenanceOB;
    }

    /**
     * Creates a new instance of InterestMaintenanceOB
     */
    public InterestSubsidyRateMaintenanceOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
    }

    private void initianSetup() throws Exception {
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        fillDropdown();     //__ To Fill all the Combo Boxes
        setChargeTabTitle();    //__ To set the Title of Table in Charges Tab...
        tblProduct = new EnhancedTableModel(null, productTabTitle) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        tblCategory = new EnhancedTableModel(null, categoryTabTitle) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");

        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "InterestSubsidyRateMaintenanceJNDI");
        operationMap.put(CommonConstants.HOME, "InterestSubsidyRateMaintenanceHome");
        operationMap.put(CommonConstants.REMOTE, "InterestSubsidyRateMaintenance");
    }

    /*
     * To set the Column title in Table(s)...
     */
    private void setChargeTabTitle() throws Exception {
        log.info("In setChargeTabTitle...");
        productTabTitle.add(objInterestMaintenanceRB.getString("tblProd1"));
        productTabTitle.add(objInterestMaintenanceRB.getString("tblProd2"));
        productTabTitle.add(objInterestMaintenanceRB.getString("tblProd3"));
        categoryTabTitle.add(objInterestMaintenanceRB.getString("tblCategory1"));
        categoryTabTitle.add(objInterestMaintenanceRB.getString("tblCategory2"));
        categoryTabTitle.add(objInterestMaintenanceRB.getString("tblCategory3"));
        setTableTile();
        tblInterestTable = new EnhancedTableModel(null, tableTitle);
    }

    public void setTableTile() {
        tableTitle.add("From Date");
        tableTitle.add("To Date");
        tableTitle.add("Total Roi");
        tableTitle.add("Cust Roi");
        tableTitle.add("Institution");
        tableTitle.add("Subsidy/Subvention");
        tableTitle.add("Subsidy Rate");
        tableTitle.add("Ref No.");
        tableTitle.add("Auth Status");
    }

    public void resetSubsidytableValues() {
        tblInterestTable.setDataArrayList(null, tableTitle);
    }

    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception {
        log.info("In fillDropdown()");
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME, null);
        final ArrayList lookup_keys = new ArrayList();
        lookup_keys.add("PRODUCTTYPE");
        lookup_keys.add("SUBSIDY.INSTITUTIONS");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
        key.add("BILLS");
        value.add("Bills");
        cbmProdType = new ComboBoxModel(key, value);

        cbmProdType.removeKeyAndElement("GL");
        cbmProdType.removeKeyAndElement("SA");
        cbmProdType.removeKeyAndElement("TD");
        cbmProdType.removeKeyAndElement("OA");
        cbmProdType.removeKeyAndElement("BILLS");

        getKeyValue((HashMap) keyValue.get("SUBSIDY.INSTITUTIONS"));
        cbmInstitution = new ComboBoxModel(key, value);
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("In getKeyValue...");
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    // Returns the Current Value of Action type...
    public int getActionType() {
        return actionType;
    }

    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    public void resetintTable() {
        setCboInstitution("");
        setTxtRefno("");
        setTdtDateReceived("");
        setTdtFromDate("");
        setTdtToDate("");
        setTxtRemarks("");
        setTxtRefno("");
        setTxtRemarks("");
        setTxtSubsidyInt(0.0);
        setTxtRateInterest(0.0);
        setTxtMiscellaneousInt(0.0);
        setTxtCustRateInt(0.0);
        setRdoInterestSubsidy(false);
        setRdoInterestSubvention(false);
    }

    public void resetTableValues() {
        tblInterestTable.setDataArrayList(null, tableTitle);
    }

    public void doAction() {
        TTException exception = null;
        try {
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (get_authorizeMap() == null) {
            data.put("SubsidyGroupDetailsData", setSubsidyGroupDetailsData());
            setSubsidyGroupCategoryData();
            setSubsidyGroupProductData();
            if (rateTypemap != null && rateTypemap.size() > 0) {
                data.put("SubsidyInterestDetails", rateTypemap);
            }
            if (categoryeMap != null && categoryeMap.size() > 0) {
                data.put("SubsidyCategoryDetails", categoryeMap);
            }
            if (productMap != null && productMap.size() > 0) {
                data.put("SubsidyProductDetails", productMap);
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        StringBuffer str = new StringBuffer();
        if (productMap.size() <= 0) {
            str.append(objInterestMaintenanceRB.getString("PROD_WARNING") + "\n");
        }
        if (categoryeMap.size() <= 0) {
            str.append(objInterestMaintenanceRB.getString("CATEGORY_WARNING") + "\n");
        }
        if ((actionType != ClientConstants.ACTIONTYPE_DELETE) && str.toString().length() > 0) {
            setWarning(str.toString());
            setResult(ClientConstants.ACTIONTYPE_FAILED);
        } else {
            System.out.println("data in InterestSubsidy OB : " + data);
            HashMap proxyResultMap = proxy.execute(data, operationMap);
            setProxyReturnMap(proxyResultMap);
            _authorizeMap = null;
            setResult(getActionType());
        }
    }

    private void setSubsidyGroupCategoryData() {
        try {
            categoryeMap = new LinkedHashMap();
            Boolean bln;
            ArrayList arrRow;
            ArrayList categoryId = new ArrayList();
            ArrayList tabData = tblCategory.getDataArrayList();
            ArrayList selectedList = new ArrayList();
            for (int i = 0, j = tblCategory.getRowCount(); i < j; i++) {
                bln = (Boolean) tblCategory.getValueAt(i, 0);
                if (bln.booleanValue() == true) {
                    InterestSubsidyRateMaintenanceCategotyTO SubsidycatTO = new InterestSubsidyRateMaintenanceCategotyTO();
                    arrRow = (ArrayList) tabData.get(i);
                    final String CATEGORY = CommonUtil.convertObjToStr(arrRow.get(1));
                    SubsidycatTO.setCategoryId(CATEGORY);
                    SubsidycatTO.setStatus(getAction());
                    SubsidycatTO.setRoiGroupId(getLblRoiGroupId());
                    categoryeMap.put(SubsidycatTO.getCategoryId(), SubsidycatTO);
                }
            }
        } catch (Exception e) {
            log.info("Error In setcategoryDetailsData()");
            e.printStackTrace();
        }
    }

    public InterestSubsidyRateMaintenanceGroupTO setSubsidyGroupDetailsData() {
        final InterestSubsidyRateMaintenanceGroupTO SubsidyGroupTO = new InterestSubsidyRateMaintenanceGroupTO();
        try {
            SubsidyGroupTO.setRoiGroupId(CommonUtil.convertObjToStr(getLblRoiGroupId()));
            SubsidyGroupTO.setRoiGroupName(getTxtGroupName());
            SubsidyGroupTO.setStatus(getAction());
            SubsidyGroupTO.setCreatedBy(TrueTransactMain.USER_ID);
            SubsidyGroupTO.setCreatedDt(curDate);
            SubsidyGroupTO.setStatusBy(TrueTransactMain.USER_ID);
            SubsidyGroupTO.setStatusDt(curDate);
            SubsidyGroupTO.setProductType(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
            if (rdoInterestType_Debit) {
                SubsidyGroupTO.setIntType("DEBIT");
            } else {
                SubsidyGroupTO.setIntType("CREDIT");
            }
        } catch (Exception e) {
            log.info("Error In setSubsidyGroupDetailsData()");
            e.printStackTrace();
        }
        return SubsidyGroupTO;
    }

    private void setSubsidyGroupProductData() {
        try {
            productMap = new LinkedHashMap();
            Boolean bln;
            ArrayList arrRow;
            ArrayList prodId = new ArrayList();
            ArrayList tabData = tblProduct.getDataArrayList();
            ArrayList selectedList = new ArrayList();
            for (int i = 0, j = tblProduct.getRowCount(); i < j; i++) {
                bln = (Boolean) tblProduct.getValueAt(i, 0);
                if (bln.booleanValue() == true) {
                    InterestSubsidyRateMaintenanceProdTO SubsidyProdTO = new InterestSubsidyRateMaintenanceProdTO();
                    arrRow = (ArrayList) tabData.get(i);
                    final String PRODID = CommonUtil.convertObjToStr(arrRow.get(1));
                    SubsidyProdTO.setProdId(PRODID);
                    SubsidyProdTO.setStatus(getAction());
                    SubsidyProdTO.setRoiGroupId(getLblRoiGroupId());
                    productMap.put(SubsidyProdTO.getProdId(), SubsidyProdTO);
                }
            }
        } catch (Exception e) {
            log.info("Error In setProductDetailsData()");
            e.printStackTrace();
        }
    }

    private String getCommand() throws Exception {
        log.info("In getCommand()");
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
            default:
        }
        return command;
    }

    private void setWarning(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    public void resetProductTab() {
        try {
            ArrayList data = tblProduct.getDataArrayList();
            for (int i = data.size(); i > 0; i--) {
                tblProduct.removeRow(i - 1);
            }
            setChanged();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetProductTab():");
            parseException.logException(e, true);
        }

    }

    public void resetCategoryTab() {
        try {
            ArrayList data = tblCategory.getDataArrayList();
            for (int i = data.size(); i > 0; i--) {
                tblCategory.removeRow(i - 1);
            }
            setChanged();
            notifyObservers();
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetCategoryTab():");
            parseException.logException(e, true);
        }
    }

    private String getAction() {
        String action = null;
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }

    public void addDataToinstSubsidytable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final InterestSubsidyRateMaintenanceTypeTO rateTypeTo = new InterestSubsidyRateMaintenanceTypeTO();

            if (rateTypemap == null) {
                rateTypemap = new LinkedHashMap();
            }
            if (tblInterestTable.getRowCount() > 0) {
                Date currentFromDt = DateUtil.getDateMMDDYYYY(getTdtFromDate());
                for (int i = 0; i < tblInterestTable.getRowCount(); i++) {
                    String TemppreFromDt = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 0));
                    Date preFromDt = DateUtil.getDateMMDDYYYY(TemppreFromDt);
                    String tempTodate = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i, 1));
                    Date todate = DateUtil.getDateMMDDYYYY(tempTodate);
                    if (currentFromDt.compareTo(preFromDt) > 0 && todate == null) {
                        String instName = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getRowCount() - 1, 4));
                        String refno = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(tblInterestTable.getRowCount() - 1, 7));
                        if (instName.length() > 0) {
                            Date end = setEndDate(instName + refno);
                            String finEndDate = CommonUtil.convertObjToStr(end);
                            for (int j = 0; j < tblInterestTable.getRowCount(); j++) {
                                String tdt = CommonUtil.convertObjToStr(tblInterestTable.getValueAt(j, 1));
                                Date toodt = DateUtil.getDateMMDDYYYY(tdt);
                                if (toodt == null) {
                                    tblInterestTable.setValueAt(finEndDate, i, 1);
                                }
                            }
                        }
                    } else {
                        tblInterestTable.setValueAt("", tblInterestTable.getRowCount() - 1, 1);
                    }
                }
            }

            if (isTypenewData()) {
                ArrayList data = tblInterestTable.getDataArrayList();
            }
            rateTypeTo.setRoiGroupId(getLblroiGroupId());   // Only Edit Mode
            rateTypeTo.setTotalroi(CommonUtil.convertObjToDouble(getTxtRateInterest()));
            rateTypeTo.setCustroi(CommonUtil.convertObjToDouble(getTxtCustRateInt()));
            rateTypeTo.setInstituteName(CommonUtil.convertObjToStr(getCboInstitution()));
            rateTypeTo.setRefno(CommonUtil.convertObjToStr(getTxtRefno()));
            rateTypeTo.setRemarks(getTxtRemarks());
            rateTypeTo.setReceivedDate(DateUtil.getDateMMDDYYYY(getTdtDateReceived()));
            rateTypeTo.setRoiDate(DateUtil.getDateMMDDYYYY(getTdtFromDate()));
            rateTypeTo.setMiscInt(CommonUtil.convertObjToDouble(getTxtMiscellaneousInt()));
            rateTypeTo.setRoiEndDate(DateUtil.getDateMMDDYYYY(getTdtToDate()));
            rateTypeTo.setAuthorizedStatus(CommonUtil.convertObjToStr(getAuthorizeStatus()));

            if (rdoInterestSubsidy) {
                rateTypeTo.setSubsidyORsubvention("SUBSIDY");
            } else {
                rateTypeTo.setSubsidyORsubvention("SUBVENTION");
            }
            rateTypeTo.setRoi(getTxtSubsidyInt());
            rateTypeTo.setStatus(getAction()); 
            rateTypeTo.setCreateDt(getProperDateFormat(curDate));            
//            rateTypeTo.setCreateDt(curDate);
            rateTypemap.put(CommonUtil.convertObjToStr(rateTypeTo.getInstituteName() + CommonUtil.convertObjToStr(rateTypeTo.getRefno())), rateTypeTo);
//            System.out.println("&&&&&&&&&&rateTypemap:" + rateTypemap);
            updateinstSubsidyTable(rowSel, rateTypeTo);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateinstSubsidyTable(int rowSel, InterestSubsidyRateMaintenanceTypeTO rateTypeTo) throws Exception {
        Object selectedRow;
        Object sRow;
        StringBuffer finrow = new StringBuffer();
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblInterestTable.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblInterestTable.getDataArrayList().get(j)).get(4);
            sRow = ((ArrayList) tblInterestTable.getDataArrayList().get(j)).get(7);
            finrow.append(selectedRow);
            finrow.append(sRow);
            if ((CommonUtil.convertObjToStr(rateTypeTo.getInstituteName()) + CommonUtil.convertObjToStr(rateTypeTo.getRefno())).equals(CommonUtil.convertObjToStr(finrow))) {
                rowExists = true;
                ArrayList SubRow = new ArrayList();
                ArrayList data = tblInterestTable.getDataArrayList();
                data.remove(rowSel);
                SubRow.add(CommonUtil.convertObjToStr(rateTypeTo.getRoiDate()));
                SubRow.add(CommonUtil.convertObjToStr(rateTypeTo.getRoiEndDate()));
                SubRow.add((CommonUtil.convertObjToStr(rateTypeTo.getTotalroi())));
                SubRow.add((CommonUtil.convertObjToStr(rateTypeTo.getCustroi())));
                SubRow.add(rateTypeTo.getInstituteName());
                SubRow.add(rateTypeTo.getSubsidyORsubvention());
                SubRow.add(rateTypeTo.getRoi());
                SubRow.add(rateTypeTo.getRefno());
//                SubRow.add(CommonUtil.convertObjToStr(rateTypeTo.getReceivedDate()));
                SubRow.add(rateTypeTo.getAuthorizedStatus());
                tblInterestTable.insertRow(rowSel, SubRow);
                SubRow = null;
            }
            finrow = new StringBuffer();
        }
        if (!rowExists) {
            ArrayList SubRow = new ArrayList();
            SubRow.add(CommonUtil.convertObjToStr(rateTypeTo.getRoiDate()));
            SubRow.add(CommonUtil.convertObjToStr(rateTypeTo.getRoiEndDate()));
            SubRow.add((CommonUtil.convertObjToStr(rateTypeTo.getTotalroi())));
            SubRow.add((CommonUtil.convertObjToStr(rateTypeTo.getCustroi())));
            SubRow.add(rateTypeTo.getInstituteName());
            SubRow.add(rateTypeTo.getSubsidyORsubvention());
            SubRow.add(rateTypeTo.getRoi());
            SubRow.add(rateTypeTo.getRefno());
//            SubRow.add(CommonUtil.convertObjToStr(rateTypeTo.getReceivedDate()));            
            SubRow.add(rateTypeTo.getAuthorizedStatus());
            tblInterestTable.insertRow(tblInterestTable.getRowCount(), SubRow);
            SubRow = null;
        }
    }
    
    public void deleteSudsidyTableData(String val, int row, Date frmDt) {
        deletedUpdationDate(frmDt, row);
        InterestSubsidyRateMaintenanceTypeTO rateTypeTo = (InterestSubsidyRateMaintenanceTypeTO) rateTypemap.get(val);
        Object obj;
        obj = val;
        System.out.println("Before Delete rateTypemap :"+rateTypemap);
        rateTypemap.remove(val);
        System.out.println("After Delete rateTypemap :"+rateTypemap);
        resetSubsidytableValues();
        try {
            populateSubsidyTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public Date setEndDate(String val) {
        InterestSubsidyRateMaintenanceTypeTO rateTypeTo = (InterestSubsidyRateMaintenanceTypeTO) rateTypemap.get(val);
        Date FromDate = DateUtil.getDateMMDDYYYY(getTdtFromDate());
        Date End = settdtenddate(FromDate);
        rateTypeTo.setRoiEndDate(End);
        rateTypemap.put(val, rateTypeTo);
        return End;
    }

    private void populateSubsidyTable() throws Exception {
        ArrayList DataList = new ArrayList();
        DataList = new ArrayList(rateTypemap.keySet());
        ArrayList addList = new ArrayList(rateTypemap.keySet());
        int length = DataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            InterestSubsidyRateMaintenanceTypeTO rateTypeTo = (InterestSubsidyRateMaintenanceTypeTO) rateTypemap.get(addList.get(i));
            IncVal.add(rateTypeTo);
            incTabRow.add(CommonUtil.convertObjToStr(rateTypeTo.getRoiDate()));
            incTabRow.add(CommonUtil.convertObjToStr(rateTypeTo.getRoiEndDate()));
            incTabRow.add((CommonUtil.convertObjToStr(rateTypeTo.getTotalroi())));
            incTabRow.add((CommonUtil.convertObjToStr(rateTypeTo.getCustroi())));
            incTabRow.add(rateTypeTo.getInstituteName());
            incTabRow.add(rateTypeTo.getSubsidyORsubvention());
            incTabRow.add(rateTypeTo.getRoi());
//            incTabRow.add(CommonUtil.convertObjToStr(rateTypeTo.getReceivedDate()));
//            incTabRow.add(rateTypeTo.getInstituteName());
            incTabRow.add(rateTypeTo.getRefno());
            incTabRow.add(rateTypeTo.getAuthorizedStatus());
            tblInterestTable.addRow(incTabRow);
        }
        notifyObservers();
    }

    public void populateSubsidyTableDetails(String row, int currow) {
        try {
            resetintTable();
            final InterestSubsidyRateMaintenanceTypeTO rateTypeTo = (InterestSubsidyRateMaintenanceTypeTO) rateTypemap.get(row);
            populateSubsidyTableData(rateTypeTo, currow);

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateSubsidyTableData(InterestSubsidyRateMaintenanceTypeTO rateTypeTo, int currow) throws Exception {
        setTxtRateInterest(rateTypeTo.getTotalroi());
        setTxtCustRateInt(rateTypeTo.getCustroi());
        setTxtMiscellaneousInt(rateTypeTo.getMiscInt());
        setCboInstitution(rateTypeTo.getInstituteName());
        setTxtRefno(rateTypeTo.getRefno());
        setTdtDateReceived(CommonUtil.convertObjToStr(rateTypeTo.getReceivedDate()));
        setTdtFromDate(CommonUtil.convertObjToStr(rateTypeTo.getRoiDate()));
        setTdtToDate(CommonUtil.convertObjToStr(rateTypeTo.getRoiEndDate()));
        setTxtSubsidyInt(rateTypeTo.getRoi());
        setTxtRemarks(rateTypeTo.getRemarks());
        if (tblInterestTable.getValueAt(currow, 5).equals("SUBSIDY")) {
            setRdoInterestSubsidy(true);
            setRdoInterestSubvention(false);
        } else {
            setRdoInterestSubvention(true);
            setRdoInterestSubsidy(false);
        }
        setChanged();
    }

    public Date settdtenddate(Date frmDt) {
        Date endDate = null;
        if (tblInterestTable.getRowCount() >= 1) {
            String fromDate = CommonUtil.convertObjToStr(frmDt);
            Date fin_end_dt = DateUtil.getDateMMDDYYYY(fromDate);
            endDate = new Date(fin_end_dt.getTime() - 1 * 24 * 3600 * 1000);
        }
        return endDate;
    }

    public void updatecustroi(String insnm, Double edcustroi,Double misInt) {
        InterestSubsidyRateMaintenanceTypeTO rateTypeTo = (InterestSubsidyRateMaintenanceTypeTO) rateTypemap.get(insnm);
        rateTypeTo.setCustroi(edcustroi);
        rateTypeTo.setMiscInt(misInt);
        rateTypemap.put(insnm, rateTypeTo);
    }

    public void setAuthStatus(String insnm, String authstatus) {
        InterestSubsidyRateMaintenanceTypeTO rateTypeTo = (InterestSubsidyRateMaintenanceTypeTO) rateTypemap.get(insnm);
        rateTypeTo.setAuthorizedStatus(authstatus);
        rateTypemap.put(insnm, rateTypeTo);
    }

    public void setTodate(String insnm, Date toodate) {
        InterestSubsidyRateMaintenanceTypeTO rateTypeTo = (InterestSubsidyRateMaintenanceTypeTO) rateTypemap.get(insnm);
        rateTypeTo.setRoiEndDate(toodate);
        rateTypemap.put(insnm, rateTypeTo);
    }

    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, operationMap);
            System.out.println("#@@%@@#%@#data" + data);
            if (data.containsKey("objSubsidyRoiDetailsTO")) {
                InterestSubsidyRateMaintenanceGroupTO SubsidyGroupTO = (InterestSubsidyRateMaintenanceGroupTO) ((List) data.get("objSubsidyRoiDetailsTO")).get(0);
                populateSubsidyRoiData(SubsidyGroupTO);
            }
            if (data.containsKey("SUBSIDY_INT_LIST")) {
                rateTypemap = (LinkedHashMap) data.get("SUBSIDY_INT_LIST");
                ArrayList addList = new ArrayList(rateTypemap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    InterestSubsidyRateMaintenanceTypeTO rateTypeTo = (InterestSubsidyRateMaintenanceTypeTO) rateTypemap.get(addList.get(i));
//                    setTxtMiscellaneousInt(rateTypeTo.getMiscInt());
                    ArrayList SubRow = new ArrayList();
                    SubRow.add(CommonUtil.convertObjToStr(rateTypeTo.getRoiDate()));
                    SubRow.add(CommonUtil.convertObjToStr(rateTypeTo.getRoiEndDate()));
                    SubRow.add((CommonUtil.convertObjToStr(rateTypeTo.getTotalroi())));
                    SubRow.add((CommonUtil.convertObjToStr(rateTypeTo.getCustroi())));
                    SubRow.add(rateTypeTo.getInstituteName());
                    SubRow.add(rateTypeTo.getSubsidyORsubvention());
                    SubRow.add(rateTypeTo.getRoi());
                    SubRow.add(rateTypeTo.getRefno());
//                    SubRow.add(CommonUtil.convertObjToStr(rateTypeTo.getReceivedDate()));
                    SubRow.add(rateTypeTo.getAuthorizedStatus());
                    tblInterestTable.addRow(SubRow);
                }
            }

            if (data.containsKey("CATEGORY_LIST")) {
                categoryeMap = (LinkedHashMap) data.get("CATEGORY_LIST");
                getCategoryData();
                ArrayList addList = new ArrayList(categoryeMap.keySet());
                for (int k = 0; k < addList.size(); k++) {
                    String Category = "";
                    Category = CommonUtil.convertObjToStr(addList.get(k));
                    for (int i = 0; i < tblCategory.getRowCount(); i++) {
                        String cat = CommonUtil.convertObjToStr(tblCategory.getValueAt(i, 1));
                        if (cat.equals(Category)) {
                            tblCategory.setValueAt(true, i, 0);
                        }
                    }
                }
            }
            if (data.containsKey("PRODUCT_LIST")) {
                productMap = (LinkedHashMap) data.get("PRODUCT_LIST");
                String prodid = CommonUtil.convertObjToStr(data.get("PRODUCT_TYPE"));
                getProductData(prodid);
                ArrayList addList = new ArrayList(productMap.keySet());
                for (int k = 0; k < addList.size(); k++) {
                    String Product = "";
                    Product = CommonUtil.convertObjToStr(addList.get(k));
                    for (int i = 0; i < tblProduct.getRowCount(); i++) {
                        String prod = CommonUtil.convertObjToStr(tblProduct.getValueAt(i, 1));
                        if (prod.equals(Product)) {
                            tblProduct.setValueAt(true, i, 0);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private void populateSubsidyRoiData(InterestSubsidyRateMaintenanceGroupTO SubsidyGroupTO) throws Exception {
        this.setLblRoiGroupId(CommonUtil.convertObjToStr(SubsidyGroupTO.getRoiGroupId()));
        this.setTxtGroupName(CommonUtil.convertObjToStr(SubsidyGroupTO.getRoiGroupName()));
        setCboProdType((String) getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(SubsidyGroupTO.getProductType())));
        String intType = CommonUtil.convertObjToStr(SubsidyGroupTO.getIntType());
        if (intType.equals("DEBIT")) {
            setRdoInterestType_Debit(true);
            setRdoInterestType_Credit(false);
        } else {
            setRdoInterestType_Credit(true);
            setRdoInterestType_Debit(false);
        }
        setChanged();
        notifyObservers();
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

    String getInterRateType() {
        return this.interRateType;
    }

    /**
     * To display the data in the Table when Some row is selected...
     */
    EnhancedTableModel getTblInterest() {
        return this.tblInterestTable;
    }

    public void resetForm() {
        log.info("In resetForm()");
        setCboProdType("");
        setTxtGroupName("");
        setRdoInterestType_Debit(false);
        setRdoInterestType_Credit(false);
        rateTypemap = null;
        categoryeMap = null;
        productMap = null;
    }

    public void resetLable() {
        setLblRoiGroupId("");
    }

    /**
     * To set and change the Status of the lable Status
     */
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public int getResult() {
        return this.result;
    }

    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    /**
     * To reset the Value of lblStatus after each save action...
     */
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    // Setter method for txtGroupName
    void setTxtGroupName(String txtGroupName) {
        this.txtGroupName = txtGroupName;
        setChanged();
    }
    // Getter method for txtGroupName

    String getTxtGroupName() {
        return this.txtGroupName;
    }

    // Setter method for tdtDate
    void setTdtDate(String tdtDate) {
        this.tdtDate = tdtDate;
        setChanged();
    }
    // Getter method for tdtDate

    String getTdtDate() {
        return this.tdtDate;
    }

    // Setter method for txtPenalInterest
    void setTxtPenalInterest(String txtPenalInterest) {
        this.txtPenalInterest = txtPenalInterest;
        setChanged();
    }
    // Getter method for txtPenalInterest

    String getTxtPenalInterest() {
        return this.txtPenalInterest;
    }

    // Setter method for txtPenalInterest
    void setLblRoiGroupId(String lblRoiGroupId) {
        this.lblRoiGroupId = lblRoiGroupId;
        setChanged();
    }
    // Getter method for txtPenalInterest

    String getLblRoiGroupId() {
        return this.lblRoiGroupId;
    }

    /**
     * To get the data for the Product table "InterMaintenance.getProductData"
     * is in InterMaintenanceMap...
     */
    public void getProductData(String prodId) {
        try {
            log.info("getProductData()");
            Iterator iterator = null;
            HashMap map = new HashMap();
            if (prodId.length() > 0) {
                final List resultList = ClientUtil.executeQuery("InterMaintenance.getProductData" + prodId, null);
                int length = resultList.size();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        map = (HashMap) resultList.get(i);
                        productTabRow = new ArrayList();
                        productTabRow.add(new Boolean(false));
                        iterator = map.values().iterator();
                        while (iterator.hasNext()) {
                            productTabRow.add(CommonUtil.convertObjToStr(iterator.next()));
                        }
                        tblProduct.insertRow(0, productTabRow);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error in getProductData()");
        }
    }

    void setTblProduct(EnhancedTableModel tblProduct) {
        this.tblProduct = tblProduct;
        setChanged();
    }

    EnhancedTableModel getTblProduct() {
        return this.tblProduct;
    }

    public void deletedUpdationDate(Date delfrmDt, int row) {
        ArrayList singleList = (ArrayList) tblInterestTable.getDataArrayList().get(row);
        ArrayList totTabList = (ArrayList) tblInterestTable.getDataArrayList();
        Date from_dt = null;
        from_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleList.get(0)));
        from_dt = DateUtil.addDays(from_dt, -1);
        for (int i = totTabList.size() - 1; i >= 0; i--) {
            singleList = (ArrayList) totTabList.get(i);
            if (row > i) {
                Date toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(singleList.get(1)));
                if (toDate != null && DateUtil.dateDiff(from_dt, toDate) == 0) {
                    tblInterestTable.setValueAt("", i, 1);
                }
            }
        }
    }

    public void getCategoryData() {
        try {
            log.info("getCategoryData()");
            Iterator iterator = null;
            HashMap map = new HashMap();
            final List resultList = ClientUtil.executeQuery("InterMaintenance.getCategoryData", null);
            int length = resultList.size();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    map = (HashMap) resultList.get(i);
                    categoryTabRow = new ArrayList();
                    categoryTabRow.add(new Boolean(false));
                    iterator = map.values().iterator();
                    while (iterator.hasNext()) {
                        categoryTabRow.add(CommonUtil.convertObjToStr(iterator.next()));
                    }
                    tblCategory.insertRow(0, categoryTabRow);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error in getCategoryData()");
        }
    }

    void setTblCategory(EnhancedTableModel tblCategory) {
        this.tblCategory = tblCategory;
        setChanged();
    }

    EnhancedTableModel getTblCategory() {
        return this.tblCategory;
    }

    // Setter method for cbmProdType
    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
        setChanged();
    }

    // Getter method for cbmProdType
    ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    // Setter method for cboProdType
    void setCboProdType(String cboProdType) {
        this.cboProdType = cboProdType;
        setChanged();
    }
    // Getter method for cboProdType

    String getCboProdType() {
        return this.cboProdType;
    }

    public static void main(String st[]) {
        ArrayList list = new ArrayList();
        list.add(new Integer("23"));
        list.add(new Integer("2"));
        list.add(new Integer("12"));
        Object obj[] = list.toArray();
        java.util.Arrays.sort(obj);
        System.out.println(obj[0].toString() + ":" + obj[1].toString());
    }

    public boolean getIsTableSet() {
        return isTableSet;
    }

    public void setIsTableSet(boolean isTableSet) {
        this.isTableSet = isTableSet;
    }

    /**
     * Getter for property AuthorizeStatus.
     *
     * @return Value of property AuthorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return AuthorizeStatus;
    }

    /**
     * Setter for property AuthorizeStatus.
     *
     * @param AuthorizeStatus New value of property AuthorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String AuthorizeStatus) {
        this.AuthorizeStatus = AuthorizeStatus;
    }

    public boolean isTypenewData() {
        return TypenewData;
    }

    public void setTypenewData(boolean TypenewData) {
        this.TypenewData = TypenewData;
    }

    /**
     * Getter for property rdoInterestType_Debit.
     *
     * @return Value of property rdoInterestType_Debit.
     */
    public boolean isRdoInterestType_Debit() {
        return rdoInterestType_Debit;
    }

    /**
     * Setter for property rdoInterestType_Debit.
     *
     * @param rdoInterestType_Debit New value of property rdoInterestType_Debit.
     */
    public void setRdoInterestType_Debit(boolean rdoInterestType_Debit) {
        this.rdoInterestType_Debit = rdoInterestType_Debit;
        setChanged();
    }

    /**
     * Getter for property rdoInterestType_Credit.
     *
     * @return Value of property rdoInterestType_Credit.
     */
    public boolean isRdoInterestType_Credit() {
        return rdoInterestType_Credit;
    }

    /**
     * Setter for property rdoInterestType_Credit.
     *
     * @param rdoInterestType_Credit New value of property
     * rdoInterestType_Credit.
     */
    public void setRdoInterestType_Credit(boolean rdoInterestType_Credit) {
        this.rdoInterestType_Credit = rdoInterestType_Credit;
        setChanged();
    }
}
