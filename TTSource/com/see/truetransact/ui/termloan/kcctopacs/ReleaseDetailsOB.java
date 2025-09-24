/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReleaseDetailsOB.java
 * 
 * Created on Thu Apr 18 10:51:55 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.transferobject.termloan.kcctopacs.ReleaseDetailsTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclClassificationTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclAmtSlabWiseDetTO;

/**
 *
 * @author Suresh R
 */
public class ReleaseDetailsOB extends CObservable {

    private String txtEditTermLoanNo = "";
    private String tdRequestedDt = "";
    private String tdtDueDt = "";
    private String txtAmountReleased = "";
    private String txtAmountRequested = "";
    private String cboPrincipalFrequency = "";
    private String tdtReleaseDt = "";
    private String txtRepaymentPeriod = "";
    private String cboRepaymentPeriod = "";
    private String txtNoOFInst = "";
    private String txtRateOfInterest = "";
    private String txtPenalInterest = "";
    private String cboCrop = "";
    private String cboLoanCategory = "";
    private String cboCategory = "";
    private String txtAmount = "";
    private String txtSlabNoOfMembers = "";
    private String cboSubCategory = "";
    private String txtRemarks = "";
    private String cboIntFrequency = "";
    private String txtNCLSanctionNo = "";
    private String tdtDueDate = "";
    private String txtUnUsedAmount = "";
    private String txtSanctionAmount = "";
    private String txtCustomerID = "";
    private String txtOperativeAcNo = "";
    private String txtKCCAcNo = "";
    private String txtTotMembers = "";
    private String txtSmallFarmers = "";
    private String txtMarginalFarmers = "";
    private String txtWomen = "";
    private String txtOthersMain = "";
    private String txtScheduleCaste = "";
    private String txtScheduleTribe = "";
    private String txtMinorityCommunity = "";
    private String txtTenantFarmers = "";
    private String txtOralLessees = "";
    private String txtOthers = "";
    private String txtMisc1 = "";
    private String txtMisc2 = "";
    private String cboFromAmount = "";
    private String cboToAmount = "";
    private String txtNoOfMembers = "";
    private String lblReleaseNo = "";
    private String lblTotalInterestPayable = "";
    private String lblTotalAmountPayable = "";
    private String slNo = "";
    private ComboBoxModel cbmInstFreq, cbmPrincipalFrequency,
            cbmIntFrequency, cbmAccountName, cbmFromAmount, cbmToAmount, cbmCrop, cbmCategory, cbmSubCategory;
    private ReleaseDetailsTO objReleaseDetailsTO;
    private boolean newData = false;
    private boolean slabNewData = false;
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(ReleaseDetailsOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblAmtSlabWise, tblMemberDetails;
    final ArrayList memTabTitle = new ArrayList();
    final ArrayList loantabTitle = new ArrayList();
    private LinkedHashMap membermap;
    private LinkedHashMap loanmap;

    /**
     * Creates a new instance of TDS ReleaseDetailsOB
     */
    public ReleaseDetailsOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "ReleaseDetailsJNDI");
            map.put(CommonConstants.HOME, "ReleaseDetailsHome");
            map.put(CommonConstants.REMOTE, "ReleaseDetails");
            fillDropdown();
            setMemTile();
            tblMemberDetails = new EnhancedTableModel(null, memTabTitle);
            setloanTile();
            tblAmtSlabWise = new EnhancedTableModel(null, loantabTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setMemTile() {
        memTabTitle.add("Sl.no");
        memTabTitle.add("Category");
        memTabTitle.add("Sub Category");
        memTabTitle.add("No Of Members");
        memTabTitle.add("Amount");
    }

    public void setloanTile() {
        loantabTitle.add("From Amount");
        loantabTitle.add("To Amount");
        loantabTitle.add("No Of Members");
    }

    private void fillDropdown() throws Exception {
        try {
            lookupMap = new HashMap();
            lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
            lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
            lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
            lookupMap.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PERIOD");
            lookup_keys.add("LOAN.FREQUENCY");
            lookup_keys.add("NCL_LOAN_SLAB_AMOUNT");
            lookup_keys.add("CROP_TYPE");
            lookup_keys.add("FARMER.SUB_CATEGORY");
            lookup_keys.add("FARMER.CATEGORY");
            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap) keyValue.get("PERIOD"));
            cbmInstFreq = new ComboBoxModel(key, value);
            makeNull();

//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap) keyValue.get("LOAN.FREQUENCY"));
            key.add("On Maturity");
            value.add("On Maturity");
            cbmPrincipalFrequency = new ComboBoxModel(key, value);
            cbmIntFrequency = new ComboBoxModel(key, value);
            makeNull();

//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap) keyValue.get("CROP_TYPE"));
            cbmCrop = new ComboBoxModel(key, value);
            makeNull();

//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap) keyValue.get("NCL_LOAN_SLAB_AMOUNT"));
            cbmFromAmount = new ComboBoxModel(key, value);
            cbmToAmount = new ComboBoxModel(key, value);
            makeNull();

            param = new java.util.HashMap();
            param.put(CommonConstants.MAP_NAME, "Charges.getProductDataAD");
            param.put(CommonConstants.PARAMFORQUERY, null);
            HashMap lookupValues = com.see.truetransact.clientutil.ClientUtil.populateLookupData(param);
            fillData((HashMap) lookupValues.get(CommonConstants.DATA));
            cbmAccountName = new ComboBoxModel(key, value);
            makeNull();

//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap) keyValue.get("FARMER.CATEGORY"));
            cbmCategory = new ComboBoxModel(key, value);
            makeNull();

//            lookupMap.put(CommonConstants.PARAMFORQUERY, lookup_keys);
//            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookupMap);
            getKeyValue((HashMap) keyValue.get("FARMER.SUB_CATEGORY"));
            cbmSubCategory = new ComboBoxModel(key, value);
            makeNull();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To get data for comboboxes
     */
    private HashMap populateData(HashMap obj) throws Exception {
        keyValue = proxy.executeQuery(obj, lookupMap);
        log.info("Got HashMap");
        return keyValue;
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    private void makeNull() {
        key = null;
        value = null;
    }

    /**
     * To retrieve a particular customer's accountclosing record
     */
    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#### After DAO Data : " + data);
            HashMap resultMap = new HashMap();
            if (data.containsKey("TRANSFER_TRANS_LIST")) {
                List transList = (List) data.get("TRANSFER_TRANS_LIST");
                resultMap.put("TRANSFER_TRANS_LIST", transList);
                setProxyReturnMap(resultMap);
            }
            if (data.containsKey("NCLReleaseData")) {
                objReleaseDetailsTO = (ReleaseDetailsTO) ((List) data.get("NCLReleaseData")).get(0);
                populateReleaseDetails(objReleaseDetailsTO);
            }
            if (data.containsKey("MEMBER_DATA")) {
                membermap = (LinkedHashMap) data.get("MEMBER_DATA");
                ArrayList addList = new ArrayList(membermap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclClassificationTO memberTO = (NclClassificationTO) membermap.get(addList.get(i));
                    ArrayList MemRow = new ArrayList();
                    MemRow.add(String.valueOf(i + 1));
                    MemRow.add(memberTO.getCategory());
                    MemRow.add(memberTO.getSubcategory());
                    MemRow.add(memberTO.getTotMembers());
                    MemRow.add(ClientUtil.convertObjToCurrency(memberTO.getAmt()));
//                    MemRow.add(memberTO.getAmt());
                    tblMemberDetails.addRow(MemRow);
                }
            }
            if (data.containsKey("LOAN_DATA")) {
                loanmap = (LinkedHashMap) data.get("LOAN_DATA");
                ArrayList addList = new ArrayList(loanmap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(addList.get(i));
                    ArrayList LoanRow = new ArrayList();
//                    LoanRow.add(loanTO.getFromAmt());
//                    LoanRow.add(loanTO.getToAmt());
                    LoanRow.add(ClientUtil.convertObjToCurrency(loanTO.getFromAmt()));
                    LoanRow.add(ClientUtil.convertObjToCurrency(loanTO.getToAmt()));
                    LoanRow.add(loanTO.getSlabNoOfMembers());
                    tblAmtSlabWise.addRow(LoanRow);
                }
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateReleaseDetails(ReleaseDetailsTO objReleaseDetailsTO) throws Exception {
        this.setLblReleaseNo(CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseNo()));
        this.setTxtNCLSanctionNo(CommonUtil.convertObjToStr(objReleaseDetailsTO.getNclSanctionNo()));
//        this.setTxtCustomerID(CommonUtil.convertObjToStr(objReleaseDetailsTO.getCustId()));        
        this.setTxtAmountRequested(CommonUtil.convertObjToStr(objReleaseDetailsTO.getAmountRequested()));
        this.setTdRequestedDt(CommonUtil.convertObjToStr(objReleaseDetailsTO.getRequestedDate()));
        this.setTxtAmountReleased(CommonUtil.convertObjToStr(objReleaseDetailsTO.getAmountReleased()));
        this.setTdtReleaseDt(CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseDate()));
        this.setTxtRepaymentPeriod(CommonUtil.convertObjToStr(objReleaseDetailsTO.getRepaymentPeriod()));
        this.setCboRepaymentPeriod(CommonUtil.convertObjToStr(objReleaseDetailsTO.getRepaymentPeriodType()));
        this.setTdtDueDt(CommonUtil.convertObjToStr(objReleaseDetailsTO.getReleaseNo()));
        this.setTxtNoOFInst(CommonUtil.convertObjToStr(objReleaseDetailsTO.getNoOfInst()));
        this.setCboPrincipalFrequency(CommonUtil.convertObjToStr(objReleaseDetailsTO.getPrincipalFreqType()));
        this.setCboIntFrequency(CommonUtil.convertObjToStr(objReleaseDetailsTO.getIntFreqType()));
        this.setTxtRateOfInterest(CommonUtil.convertObjToStr(objReleaseDetailsTO.getRoi()));
        this.setTxtPenalInterest(CommonUtil.convertObjToStr(objReleaseDetailsTO.getPenalInt()));
        this.setCboLoanCategory(CommonUtil.convertObjToStr(objReleaseDetailsTO.getLoanCategory()));
        this.setCboSubCategory(CommonUtil.convertObjToStr(objReleaseDetailsTO.getSubCategory()));
        this.setCboCrop(CommonUtil.convertObjToStr(objReleaseDetailsTO.getCrop()));
        this.setLblTotalInterestPayable(CommonUtil.convertObjToStr(objReleaseDetailsTO.getTotalIntPayable()));
        this.setLblTotalAmountPayable(CommonUtil.convertObjToStr(objReleaseDetailsTO.getTotalAmtPayable()));
        this.setTxtRemarks(CommonUtil.convertObjToStr(objReleaseDetailsTO.getRemarks()));
        setChanged();
        notifyObservers();
    }

    public void resetMemberTable() {
        setCboCategory("");
        setCboSubCategory("");
        setTxtAmount("");
        setTxtNoOfMembers("");
    }

    public void resetLoanTable() {
        setCboToAmount("");
        setCboFromAmount("");
        setTxtSlabNoOfMembers("");
    }

    public void addDataToMemberDetailsTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final NclClassificationTO memberTO = new NclClassificationTO();
            if (membermap == null) {
                membermap = new LinkedHashMap();
            }
            int slno = 0;
            int nums[] = new int[50];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblMemberDetails.getDataArrayList();
                slno = serialNo(data);
            } else {
                if (isNewData()) {
                    ArrayList data = tblMemberDetails.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblMemberDetails.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }
            memberTO.setNclSanctionNo(getLblReleaseNo());
            memberTO.setTotMembers(CommonUtil.convertObjToDouble(getTxtNoOfMembers()));
            //Changed By Kannan
            memberTO.setAmt(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(getTxtAmount())));
//            memberTO.setAmt(CommonUtil.convertObjToDouble(getTxtAmount()));
            memberTO.setCategory(getCboCategory());
            memberTO.setSubcategory(getCboSubCategory());
            memberTO.setSlNo(String.valueOf(slno));
            membermap.put(slno, memberTO);
            String sno = String.valueOf(slno);
            updateMemberTable(rowSel, sno,memberTO);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateMemberTable(int rowSel, String sno,NclClassificationTO memberTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblMemberDetails.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblMemberDetails.getDataArrayList().get(j)).get(0);
            if (sno.equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList MemRow = new ArrayList();
                ArrayList data = tblMemberDetails.getDataArrayList();
                data.remove(rowSel);
                MemRow.add(sno);
                MemRow.add(CommonUtil.convertObjToStr(getCboCategory()));
                MemRow.add(CommonUtil.convertObjToStr(getCboSubCategory()));
                MemRow.add(CommonUtil.convertObjToDouble(txtNoOfMembers));
//                MemRow.add(CommonUtil.convertObjToDouble(txtAmount));
                MemRow.add(memberTO.getAmt()); //Changed By Kannan
                tblMemberDetails.insertRow(rowSel, MemRow);
                MemRow = null;
            }
        }
        if (!rowExists) {
            ArrayList MemRow = new ArrayList();
            MemRow.add(String.valueOf(sno));
            MemRow.add(CommonUtil.convertObjToStr(getCboCategory()));
            MemRow.add(CommonUtil.convertObjToStr(getCboSubCategory()));
            MemRow.add(CommonUtil.convertObjToDouble(txtNoOfMembers));
            MemRow.add(memberTO.getAmt());//Changed By Kannan
//            MemRow.add(CommonUtil.convertObjToDouble(txtAmount));
            tblMemberDetails.insertRow(tblMemberDetails.getRowCount(), MemRow);
            MemRow = null;
        }
    }

    public int serialNo(ArrayList data) {
        final int dataSize = data.size();
        int nums[] = new int[50];
        int max = nums[0];
        int slno = 0;
        int a = 0;
        slno = dataSize + 1;
        for (int i = 0; i < data.size(); i++) {
            a = CommonUtil.convertObjToInt(tblMemberDetails.getValueAt(i, 0));
            nums[i] = a;
            if (nums[i] > max) {
                max = nums[i];
            }
            slno = max + 1;
        }
        return slno;
    }

    public void deleteMemberTableData(int val, int row) {
        NclClassificationTO memberTO = (NclClassificationTO) membermap.get(val);
        Object obj;
        obj = val;
        membermap.remove(val);
        resetMembertableValues();
        try {
            populateMemberTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateMemberTable() throws Exception {
        ArrayList DataList = new ArrayList();
        DataList = new ArrayList(membermap.keySet());
        ArrayList addList = new ArrayList(membermap.keySet());
        int length = DataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            NclClassificationTO memberTO = (NclClassificationTO) membermap.get(addList.get(i));
            IncVal.add(memberTO);
            incTabRow.add(memberTO.getSlNo());
            incTabRow.add(memberTO.getCategory());
            incTabRow.add(memberTO.getSubcategory());
            incTabRow.add(memberTO.getTotMembers());
            incTabRow.add(memberTO.getAmt());
            tblMemberDetails.addRow(incTabRow);
        }
        notifyObservers();
    }

    public void deleteloanTableData(String val, int row) {
        NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(val);
        Object obj;
        obj = val;
        loanmap.remove(val);
        resetLoantableValues();
        try {
            populateloanTable();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateloanTable() throws Exception {
        ArrayList DataList = new ArrayList();
        DataList = new ArrayList(loanmap.keySet());
        ArrayList addList = new ArrayList(loanmap.keySet());
        int length = DataList.size();
        for (int i = 0; i < length; i++) {
            ArrayList incTabRow = new ArrayList();
            NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(addList.get(i));
            IncVal.add(loanTO);
            incTabRow.add(loanTO.getFromAmt());
            incTabRow.add(loanTO.getToAmt());
            incTabRow.add(loanTO.getSlabNoOfMembers());
            tblAmtSlabWise.addRow(incTabRow);
        }
        notifyObservers();
    }

    public void populateMemberTableDetails(int row) {
        try {
            resetMemberTable();
            final NclClassificationTO memberTO = (NclClassificationTO) membermap.get(row);
            populateMemberTableData(memberTO);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateMemberTableData(NclClassificationTO memberTO) throws Exception {
        setTxtTotMembers(CommonUtil.convertObjToStr(memberTO.getCountMembers()));
        setCboCategory(memberTO.getCategory());
        setCboSubCategory(memberTO.getSubcategory());
        setTxtAmount(CommonUtil.convertObjToStr(memberTO.getAmt()));
        setTxtNoOfMembers(CommonUtil.convertObjToStr(memberTO.getTotMembers()));
        setChanged();
        notifyObservers();
    }

    public void populateLoanTableDetails(String row) {
        try {
            resetLoanTable();
            final NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(row);
            populateLoanTableData(loanTO);

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void populateLoanTableData(NclAmtSlabWiseDetTO loanTO) throws Exception {
        String frmAmt = loanTO.getFromAmt();
        frmAmt = frmAmt.replace(".00", "");
        frmAmt = frmAmt.replace(",", "");        
        String ToAmt = loanTO.getToAmt();
        ToAmt = ToAmt.replace(".00", "");
        ToAmt = ToAmt.replace(",", "");
        setCboFromAmount(ClientUtil.convertObjToCurrency(frmAmt));
        setCboToAmount(ClientUtil.convertObjToCurrency(ToAmt));
        
//        setCboFromAmount(ClientUtil.convertObjToCurrency(loanTO.getFromAmt()));
//        setCboToAmount(ClientUtil.convertObjToCurrency(loanTO.getToAmt()));
        setTxtSlabNoOfMembers(CommonUtil.convertObjToStr(loanTO.getSlabNoOfMembers()));
        setChanged();
        notifyObservers();
    }

    public void addDataToLoanAmtSlabWiseTable(int rowSelected, boolean updateMode) {
        try {
            int rowSel = rowSelected;
            final NclAmtSlabWiseDetTO loanTO = new NclAmtSlabWiseDetTO();

            if (loanmap == null) {
                loanmap = new LinkedHashMap();
            }

            if (isSlabNewData()) {
                ArrayList data = tblAmtSlabWise.getDataArrayList();
            }
            //Changed By Kannan
            loanTO.setNclSanctionNo(getLblReleaseNo());        
            loanTO.setFromAmt(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(getCboFromAmount())));
            loanTO.setToAmt(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(getCboToAmount())));
            loanTO.setSlabNoOfMembers(CommonUtil.convertObjToDouble(getTxtSlabNoOfMembers()));
            String key=loanTO.getFromAmt();
            key=key.replace(".00", "");
            key=key.replace(",", "");
            loanmap.put(key, loanTO);            
//            loanmap.put(CommonUtil.convertObjToStr(loanTO.getFromAmt()), loanTO);
            updateLoanAmtSlabWiseTable(rowSel, loanTO);
            
//            loanTO.setNclSanctionNo(getLblReleaseNo());
//            //Changed By Kannan
//            loanTO.setFromAmt(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(getCboFromAmount())));
//            loanTO.setToAmt(CommonUtil.convertObjToStr(ClientUtil.convertObjToCurrency(getCboToAmount())));
////            loanTO.setFromAmt(CommonUtil.convertObjToDouble(getCboFromAmount()));
////            loanTO.setToAmt(CommonUtil.convertObjToDouble(getCboToAmount()));
//            loanTO.setSlabNoOfMembers(CommonUtil.convertObjToDouble(getTxtSlabNoOfMembers()));
//            loanmap.put(CommonUtil.convertObjToStr(loanTO.getFromAmt()), loanTO);
//            updateLoanAmtSlabWiseTable(rowSel, loanTO);
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void updateLoanAmtSlabWiseTable(int rowSel, NclAmtSlabWiseDetTO loanTO) throws Exception {
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append        
        for (int i = tblAmtSlabWise.getRowCount(), j = 0; i > 0; i--, j++) {
            selectedRow = ((ArrayList) tblAmtSlabWise.getDataArrayList().get(j)).get(0);
            if (CommonUtil.convertObjToStr(loanTO.getFromAmt()).equals(CommonUtil.convertObjToStr(selectedRow))) {
                rowExists = true;
                ArrayList LoanRow = new ArrayList();
                ArrayList data = tblAmtSlabWise.getDataArrayList();
                data.remove(rowSel);
                LoanRow.add(loanTO.getFromAmt());
                LoanRow.add(loanTO.getToAmt());
                LoanRow.add(loanTO.getSlabNoOfMembers());
                tblAmtSlabWise.insertRow(rowSel, LoanRow);
                LoanRow = null;
            }
        }

        if (!rowExists) {
            ArrayList LoanRow = new ArrayList();
            LoanRow.add(loanTO.getFromAmt());
            LoanRow.add(loanTO.getToAmt());
            LoanRow.add(loanTO.getSlabNoOfMembers());
            tblAmtSlabWise.insertRow(tblAmtSlabWise.getRowCount(), LoanRow);
            LoanRow = null;
        }
    }

    public void resetMembertableValues() {
        tblMemberDetails.setDataArrayList(null, memTabTitle);
    }

    public void resetLoantableValues() {
        tblAmtSlabWise.setDataArrayList(null, loantabTitle);
    }

    public void doAction() {
        try {
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                //Added By Kannan
                convertProperDataForMemberDetails();
                convertProperDataForAmtSlabDetails();
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        final HashMap data = new HashMap();
        data.put("COMMAND", getCommand());
        if (get_authorizeMap() == null) {
            data.put("NCLReleaseData", setNCLLoanReleaseData());
            if (membermap != null && membermap.size() > 0) {
                data.put("MemberDetails", membermap);
            }
            if (loanmap != null && loanmap.size() > 0) {
                data.put("LoanSlabDetails", loanmap);
            }
        } else {
            data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
        }
        System.out.println("data in ReleaseNCLLoan OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        setProxyReturnMap(proxyResultMap);
        _authorizeMap = null;
        setResult(getActionType());
    }

    /**
     * To populate data into the screen
     */
    public ReleaseDetailsTO setNCLLoanReleaseData() {

        final ReleaseDetailsTO objReleaseDetailsTO = new ReleaseDetailsTO();
        try {
            objReleaseDetailsTO.setReleaseNo(getLblReleaseNo());
            objReleaseDetailsTO.setNclSanctionNo(getTxtNCLSanctionNo());
//            objReleaseDetailsTO.setCustId(getTxtCustomerID());            
            objReleaseDetailsTO.setAmountRequested(CommonUtil.convertObjToDouble(getTxtAmountRequested()));
            objReleaseDetailsTO.setRequestedDate(DateUtil.getDateMMDDYYYY(getTdRequestedDt()));
            objReleaseDetailsTO.setAmountReleased(CommonUtil.convertObjToDouble(getTxtAmountReleased()));
            objReleaseDetailsTO.setReleaseDate(DateUtil.getDateMMDDYYYY(getTdtReleaseDt()));
            objReleaseDetailsTO.setRepaymentPeriod(CommonUtil.convertObjToDouble(getTxtRepaymentPeriod()));
            objReleaseDetailsTO.setRepaymentPeriodType(getCboRepaymentPeriod());
            objReleaseDetailsTO.setDueDate(DateUtil.getDateMMDDYYYY(getTdtDueDt()));
            objReleaseDetailsTO.setNoOfInst(CommonUtil.convertObjToDouble(getTxtNoOFInst()));
            objReleaseDetailsTO.setPrincipalFreqType(getCboPrincipalFrequency());
            objReleaseDetailsTO.setIntFreqType(getCboIntFrequency());
            objReleaseDetailsTO.setRoi(CommonUtil.convertObjToDouble(getTxtRateOfInterest()));
            objReleaseDetailsTO.setPenalInt(CommonUtil.convertObjToDouble(getTxtPenalInterest()));
            objReleaseDetailsTO.setLoanCategory(getCboLoanCategory());
            objReleaseDetailsTO.setSubCategory(getCboSubCategory());
            objReleaseDetailsTO.setCrop(getCboCrop());
            objReleaseDetailsTO.setTotalIntPayable(CommonUtil.convertObjToDouble(getLblTotalInterestPayable()));
            objReleaseDetailsTO.setTotalAmtPayable(CommonUtil.convertObjToDouble(getLblTotalAmountPayable()));
            objReleaseDetailsTO.setRemarks(getTxtRemarks());
            objReleaseDetailsTO.setCommand(getAction());
            objReleaseDetailsTO.setStatus(getAction());
            objReleaseDetailsTO.setStatusBy(TrueTransactMain.USER_ID);
            objReleaseDetailsTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
        } catch (Exception e) {
            log.info("Error In setMDSChangeofMemberTOData()");
            e.printStackTrace();
        }
        return objReleaseDetailsTO;
    }

    public void convertProperDataForMemberDetails(){
        ArrayList addList = new ArrayList(membermap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclClassificationTO memberTO = (NclClassificationTO) membermap.get(addList.get(i));
                    String Amt=memberTO.getAmt();
                    Amt=Amt.replace(",", "");
                    memberTO.setAmt(Amt);
                     membermap.put(addList.get(i), memberTO);                    
    }                          
    }
    
     public void convertProperDataForAmtSlabDetails(){
        ArrayList addList = new ArrayList(loanmap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    NclAmtSlabWiseDetTO loanTO = (NclAmtSlabWiseDetTO) loanmap.get(addList.get(i));
                    String frmAmt=loanTO.getFromAmt();
                    String toAmt=loanTO.getToAmt();
                    frmAmt=frmAmt.replace(",", "");
                    toAmt=toAmt.replace(",", "");
                    loanTO.setFromAmt(frmAmt);
                    loanTO.setToAmt(toAmt);                    
                    loanmap.put(addList.get(i), loanTO);                    
    }                 
    }
    
    
    private String getCommand() {
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
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        return command;
    }

    private String getAction() {
        String action = null;
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
        return action;
    }

    public void resetForm() {
        setLblReleaseNo("");
        setTxtNCLSanctionNo("");
        setTxtCustomerID("");
        setTxtAmountRequested("");
        setTdRequestedDt("");
        setTxtAmountReleased("");
        setTdtReleaseDt("");
        setTxtRepaymentPeriod("");
        setCboRepaymentPeriod("");
        setTdtDueDt("");
        setTxtNoOFInst("");
        setCboPrincipalFrequency("");
        setCboIntFrequency("");
        setTxtRateOfInterest("");
        setTxtPenalInterest("");
        setCboLoanCategory("");
        setCboSubCategory("");
        setCboCrop("");
        setLblTotalInterestPayable("");
        setLblTotalAmountPayable("");
        setTxtRemarks("");
    }

    public void resetTableValues() {
        tblMemberDetails.setDataArrayList(null, memTabTitle);
        tblAmtSlabWise.setDataArrayList(null, loantabTitle);
    }

    public boolean isSlabNewData() {
        return slabNewData;
    }

    public void setSlabNewData(boolean slabNewData) {
        this.slabNewData = slabNewData;
    }

    public EnhancedTableModel getTblMemberDetails() {
        return tblMemberDetails;
    }

    public void setTblMemberDetails(EnhancedTableModel tblMemberDetails) {
        this.tblMemberDetails = tblMemberDetails;
    }

    public ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }

    public void setCbmCategory(ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
    }

    public ComboBoxModel getCbmSubCategory() {
        return cbmSubCategory;
    }

    public void setCbmSubCategory(ComboBoxModel cbmSubCategory) {
        this.cbmSubCategory = cbmSubCategory;
    }

    public ComboBoxModel getCbmCrop() {
        return cbmCrop;
    }

    public void setCbmCrop(ComboBoxModel cbmCrop) {
        this.cbmCrop = cbmCrop;
    }

    public String getTxtSlabNoOfMembers() {
        return txtSlabNoOfMembers;
    }

    public void setTxtSlabNoOfMembers(String txtSlabNoOfMembers) {
        this.txtSlabNoOfMembers = txtSlabNoOfMembers;
    }

    public String getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(String txtAmount) {
        this.txtAmount = txtAmount;
    }

    public String getCboCategory() {
        return cboCategory;
    }

    public void setCboCategory(String cboCategory) {
        this.cboCategory = cboCategory;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public EnhancedTableModel getTblAmtSlabWise() {
        return tblAmtSlabWise;
    }

    public void setTblAmtSlabWise(EnhancedTableModel tblAmtSlabWise) {
        this.tblAmtSlabWise = tblAmtSlabWise;
    }

    public boolean isNewData() {
        return newData;
    }

    public void setNewData(boolean newData) {
        this.newData = newData;
    }

    public ComboBoxModel getCbmFromAmount() {
        return cbmFromAmount;
    }

    public void setCbmFromAmount(ComboBoxModel cbmFromAmount) {
        this.cbmFromAmount = cbmFromAmount;
    }

    public ComboBoxModel getCbmToAmount() {
        return cbmToAmount;
    }

    public void setCbmToAmount(ComboBoxModel cbmToAmount) {
        this.cbmToAmount = cbmToAmount;
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

    public String getLblTotalAmountPayable() {
        return lblTotalAmountPayable;
    }

    public void setLblTotalAmountPayable(String lblTotalAmountPayable) {
        this.lblTotalAmountPayable = lblTotalAmountPayable;
    }

    public String getLblTotalInterestPayable() {
        return lblTotalInterestPayable;
    }

    public void setLblTotalInterestPayable(String lblTotalInterestPayable) {
        this.lblTotalInterestPayable = lblTotalInterestPayable;
    }

    public String getLblReleaseNo() {
        return lblReleaseNo;
    }

    public void setLblReleaseNo(String lblReleaseNo) {
        this.lblReleaseNo = lblReleaseNo;
    }

    public ComboBoxModel getCbmAccountName() {
        return cbmAccountName;
    }

    public void setCbmAccountName(ComboBoxModel cbmAccountName) {
        this.cbmAccountName = cbmAccountName;
    }

    public ComboBoxModel getCbmIntFrequency() {
        return cbmIntFrequency;
    }

    public void setCbmIntFrequency(ComboBoxModel cbmIntFrequency) {
        this.cbmIntFrequency = cbmIntFrequency;
    }

    public ComboBoxModel getCbmInstFreq() {
        return cbmInstFreq;
    }

    public void setCbmInstFreq(ComboBoxModel cbmInstFreq) {
        this.cbmInstFreq = cbmInstFreq;
    }

    public ComboBoxModel getCbmPrincipalFrequency() {
        return cbmPrincipalFrequency;
    }

    public void setCbmPrincipalFrequency(ComboBoxModel cbmPrincipalFrequency) {
        this.cbmPrincipalFrequency = cbmPrincipalFrequency;
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
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

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    // Setter method for txtEditTermLoanNo
    void setTxtEditTermLoanNo(String txtEditTermLoanNo) {
        this.txtEditTermLoanNo = txtEditTermLoanNo;
        setChanged();
    }
    // Getter method for txtEditTermLoanNo

    String getTxtEditTermLoanNo() {
        return this.txtEditTermLoanNo;
    }

    // Setter method for tdRequestedDt
    void setTdRequestedDt(String tdRequestedDt) {
        this.tdRequestedDt = tdRequestedDt;
        setChanged();
    }
    // Getter method for tdRequestedDt

    String getTdRequestedDt() {
        return this.tdRequestedDt;
    }

    // Setter method for tdtDueDt
    void setTdtDueDt(String tdtDueDt) {
        this.tdtDueDt = tdtDueDt;
        setChanged();
    }
    // Getter method for tdtDueDt

    String getTdtDueDt() {
        return this.tdtDueDt;
    }

    // Setter method for txtAmountReleased
    void setTxtAmountReleased(String txtAmountReleased) {
        this.txtAmountReleased = txtAmountReleased;
        setChanged();
    }
    // Getter method for txtAmountReleased

    String getTxtAmountReleased() {
        return this.txtAmountReleased;
    }

    // Setter method for txtAmountRequested
    void setTxtAmountRequested(String txtAmountRequested) {
        this.txtAmountRequested = txtAmountRequested;
        setChanged();
    }
    // Getter method for txtAmountRequested

    String getTxtAmountRequested() {
        return this.txtAmountRequested;
    }

    // Setter method for cboPrincipalFrequency
    void setCboPrincipalFrequency(String cboPrincipalFrequency) {
        this.cboPrincipalFrequency = cboPrincipalFrequency;
        setChanged();
    }
    // Getter method for cboPrincipalFrequency

    String getCboPrincipalFrequency() {
        return this.cboPrincipalFrequency;
    }

    // Setter method for tdtReleaseDt
    void setTdtReleaseDt(String tdtReleaseDt) {
        this.tdtReleaseDt = tdtReleaseDt;
        setChanged();
    }
    // Getter method for tdtReleaseDt

    String getTdtReleaseDt() {
        return this.tdtReleaseDt;
    }

    // Setter method for txtRepaymentPeriod
    void setTxtRepaymentPeriod(String txtRepaymentPeriod) {
        this.txtRepaymentPeriod = txtRepaymentPeriod;
        setChanged();
    }
    // Getter method for txtRepaymentPeriod

    String getTxtRepaymentPeriod() {
        return this.txtRepaymentPeriod;
    }

    // Setter method for cboRepaymentPeriod
    void setCboRepaymentPeriod(String cboRepaymentPeriod) {
        this.cboRepaymentPeriod = cboRepaymentPeriod;
        setChanged();
    }
    // Getter method for cboRepaymentPeriod

    String getCboRepaymentPeriod() {
        return this.cboRepaymentPeriod;
    }

    // Setter method for txtNoOFInst
    void setTxtNoOFInst(String txtNoOFInst) {
        this.txtNoOFInst = txtNoOFInst;
        setChanged();
    }
    // Getter method for txtNoOFInst

    String getTxtNoOFInst() {
        return this.txtNoOFInst;
    }

    // Setter method for txtRateOfInterest
    void setTxtRateOfInterest(String txtRateOfInterest) {
        this.txtRateOfInterest = txtRateOfInterest;
        setChanged();
    }
    // Getter method for txtRateOfInterest

    String getTxtRateOfInterest() {
        return this.txtRateOfInterest;
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

    // Setter method for cboCrop
    void setCboCrop(String cboCrop) {
        this.cboCrop = cboCrop;
        setChanged();
    }
    // Getter method for cboCrop

    String getCboCrop() {
        return this.cboCrop;
    }

    // Setter method for cboLoanCategory
    void setCboLoanCategory(String cboLoanCategory) {
        this.cboLoanCategory = cboLoanCategory;
        setChanged();
    }
    // Getter method for cboLoanCategory

    String getCboLoanCategory() {
        return this.cboLoanCategory;
    }

    // Setter method for cboSubCategory
    void setCboSubCategory(String cboSubCategory) {
        this.cboSubCategory = cboSubCategory;
        setChanged();
    }
    // Getter method for cboSubCategory

    String getCboSubCategory() {
        return this.cboSubCategory;
    }

    // Setter method for txtRemarks
    void setTxtRemarks(String txtRemarks) {
        this.txtRemarks = txtRemarks;
        setChanged();
    }
    // Getter method for txtRemarks

    String getTxtRemarks() {
        return this.txtRemarks;
    }

    // Setter method for cboIntFrequency
    void setCboIntFrequency(String cboIntFrequency) {
        this.cboIntFrequency = cboIntFrequency;
        setChanged();
    }
    // Getter method for cboIntFrequency

    String getCboIntFrequency() {
        return this.cboIntFrequency;
    }

    // Setter method for txtNCLSanctionNo
    void setTxtNCLSanctionNo(String txtNCLSanctionNo) {
        this.txtNCLSanctionNo = txtNCLSanctionNo;
        setChanged();
    }
    // Getter method for txtNCLSanctionNo

    String getTxtNCLSanctionNo() {
        return this.txtNCLSanctionNo;
    }

    // Setter method for tdtDueDate
    void setTdtDueDate(String tdtDueDate) {
        this.tdtDueDate = tdtDueDate;
        setChanged();
    }
    // Getter method for tdtDueDate

    String getTdtDueDate() {
        return this.tdtDueDate;
    }

    // Setter method for txtUnUsedAmount
    void setTxtUnUsedAmount(String txtUnUsedAmount) {
        this.txtUnUsedAmount = txtUnUsedAmount;
        setChanged();
    }
    // Getter method for txtUnUsedAmount

    String getTxtUnUsedAmount() {
        return this.txtUnUsedAmount;
    }

    // Setter method for txtSanctionAmount
    void setTxtSanctionAmount(String txtSanctionAmount) {
        this.txtSanctionAmount = txtSanctionAmount;
        setChanged();
    }
    // Getter method for txtSanctionAmount

    String getTxtSanctionAmount() {
        return this.txtSanctionAmount;
    }

    // Setter method for txtCustomerID
    void setTxtCustomerID(String txtCustomerID) {
        this.txtCustomerID = txtCustomerID;
        setChanged();
    }
    // Getter method for txtCustomerID

    String getTxtCustomerID() {
        return this.txtCustomerID;
    }

    // Setter method for txtOperativeAcNo
    void setTxtOperativeAcNo(String txtOperativeAcNo) {
        this.txtOperativeAcNo = txtOperativeAcNo;
        setChanged();
    }
    // Getter method for txtOperativeAcNo

    String getTxtOperativeAcNo() {
        return this.txtOperativeAcNo;
    }

    // Setter method for txtKCCAcNo
    void setTxtKCCAcNo(String txtKCCAcNo) {
        this.txtKCCAcNo = txtKCCAcNo;
        setChanged();
    }
    // Getter method for txtKCCAcNo

    String getTxtKCCAcNo() {
        return this.txtKCCAcNo;
    }

    // Setter method for txtTotMembers
    void setTxtTotMembers(String txtTotMembers) {
        this.txtTotMembers = txtTotMembers;
        setChanged();
    }
    // Getter method for txtTotMembers

    String getTxtTotMembers() {
        return this.txtTotMembers;
    }

    // Setter method for txtSmallFarmers
    void setTxtSmallFarmers(String txtSmallFarmers) {
        this.txtSmallFarmers = txtSmallFarmers;
        setChanged();
    }
    // Getter method for txtSmallFarmers

    String getTxtSmallFarmers() {
        return this.txtSmallFarmers;
    }

    // Setter method for txtMarginalFarmers
    void setTxtMarginalFarmers(String txtMarginalFarmers) {
        this.txtMarginalFarmers = txtMarginalFarmers;
        setChanged();
    }
    // Getter method for txtMarginalFarmers

    String getTxtMarginalFarmers() {
        return this.txtMarginalFarmers;
    }

    // Setter method for txtWomen
    void setTxtWomen(String txtWomen) {
        this.txtWomen = txtWomen;
        setChanged();
    }
    // Getter method for txtWomen

    String getTxtWomen() {
        return this.txtWomen;
    }

    // Setter method for txtOthersMain
    void setTxtOthersMain(String txtOthersMain) {
        this.txtOthersMain = txtOthersMain;
        setChanged();
    }
    // Getter method for txtOthersMain

    String getTxtOthersMain() {
        return this.txtOthersMain;
    }

    // Setter method for txtScheduleCaste
    void setTxtScheduleCaste(String txtScheduleCaste) {
        this.txtScheduleCaste = txtScheduleCaste;
        setChanged();
    }
    // Getter method for txtScheduleCaste

    String getTxtScheduleCaste() {
        return this.txtScheduleCaste;
    }

    // Setter method for txtScheduleTribe
    void setTxtScheduleTribe(String txtScheduleTribe) {
        this.txtScheduleTribe = txtScheduleTribe;
        setChanged();
    }
    // Getter method for txtScheduleTribe

    String getTxtScheduleTribe() {
        return this.txtScheduleTribe;
    }

    // Setter method for txtMinorityCommunity
    void setTxtMinorityCommunity(String txtMinorityCommunity) {
        this.txtMinorityCommunity = txtMinorityCommunity;
        setChanged();
    }
    // Getter method for txtMinorityCommunity

    String getTxtMinorityCommunity() {
        return this.txtMinorityCommunity;
    }

    // Setter method for txtTenantFarmers
    void setTxtTenantFarmers(String txtTenantFarmers) {
        this.txtTenantFarmers = txtTenantFarmers;
        setChanged();
    }
    // Getter method for txtTenantFarmers

    String getTxtTenantFarmers() {
        return this.txtTenantFarmers;
    }

    // Setter method for txtOralLessees
    void setTxtOralLessees(String txtOralLessees) {
        this.txtOralLessees = txtOralLessees;
        setChanged();
    }
    // Getter method for txtOralLessees

    String getTxtOralLessees() {
        return this.txtOralLessees;
    }

    // Setter method for txtOthers
    void setTxtOthers(String txtOthers) {
        this.txtOthers = txtOthers;
        setChanged();
    }
    // Getter method for txtOthers

    String getTxtOthers() {
        return this.txtOthers;
    }

    // Setter method for txtMisc1
    void setTxtMisc1(String txtMisc1) {
        this.txtMisc1 = txtMisc1;
        setChanged();
    }
    // Getter method for txtMisc1

    String getTxtMisc1() {
        return this.txtMisc1;
    }

    // Setter method for txtMisc2
    void setTxtMisc2(String txtMisc2) {
        this.txtMisc2 = txtMisc2;
        setChanged();
    }
    // Getter method for txtMisc2

    String getTxtMisc2() {
        return this.txtMisc2;
    }

    // Setter method for cboFromAmount
    void setCboFromAmount(String cboFromAmount) {
        this.cboFromAmount = cboFromAmount;
        setChanged();
    }
    // Getter method for cboFromAmount

    String getCboFromAmount() {
        return this.cboFromAmount;
    }

    // Setter method for cboToAmount
    void setCboToAmount(String cboToAmount) {
        this.cboToAmount = cboToAmount;
        setChanged();
    }
    // Getter method for cboToAmount

    String getCboToAmount() {
        return this.cboToAmount;
    }

    // Setter method for txtNoOfMembers
    void setTxtNoOfMembers(String txtNoOfMembers) {
        this.txtNoOfMembers = txtNoOfMembers;
        setChanged();
    }
    // Getter method for txtNoOfMembers

    String getTxtNoOfMembers() {
        return this.txtNoOfMembers;
    }
}