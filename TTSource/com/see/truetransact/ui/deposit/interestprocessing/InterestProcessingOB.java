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
package com.see.truetransact.ui.deposit.interestprocessing;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.*;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.deposit.interestprocessing.ThriftBenCalculationTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.CTable;
import java.util.Date;

/**
 *
 * @author nikhil
 */
public class InterestProcessingOB extends CObservable {

    Date curDate = null;
    private ProxyFactory proxy;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap map;
    private HashMap getDepositDetailsMap;
    private TransactionOB transactionOB;
    private int actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private EnhancedTableModel tblDepositInterestCalculation;
    HashMap data = new HashMap();
    private String cboDepositProduct = "";
    private String txtReserveFundGl = "";
    private String txtReservePercent = "";
    private String txtDebitGl = "";
    private String txtPayableGl = "";
    private String txtTotalAmount = "";
    private String tdtFromPeriod = "";
    private String tdtToPeriod = "";
    private String txtDepositInterestPercent = "";
    private String txtResolutionNo = "";
    private String tdtResolutionDate = "";
    private String txtRemarks = "";
    private ArrayList interestCalcColoumn = new ArrayList();
    private String txtDrfTransAmount = "";
    private String cboDrfTransProdID = "";
    private ArrayList key, value;
    private HashMap _authorizeMap;
    private String lblDrfTransAddressCont = "";
    private String rdoShareDividendCalculation = "";
    private String chkDueAmtPayment = "";
    InterestProcessingRB objShareDividendCalculationRB = new InterestProcessingRB();
    double totCurrent = 0;
    double totSaleAmount = 0;
    Date currDt = null;
    List interestDetailsList = new ArrayList();
    private ComboBoxModel cbmDepositProduct;
    private CTable _tblData;
    private HashMap dataHash;
    private ArrayList data1;
    private int dataSize;
    private ArrayList _heading;
    private boolean _isAvailable = true;
    private ComboBoxModel cbmSchemName;
    private String txtProdId=null;

    public InterestProcessingOB(int param) {
        try {
            
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "InterestProcessingJNDI");
            map.put(CommonConstants.HOME, "serverside.deposit.interestprocessing.InterestProcessingHome");
            map.put(CommonConstants.REMOTE, "serverside.deposit.interestprocessing.InterestProcessing");
            createDividendCalcTransTable();
            fillDropdown();

        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void fillDropdown() throws Exception {
        HashMap whereMap = new HashMap();
        List productList = ClientUtil.executeQuery("getThriftBenevolentDepsositProduct", whereMap);
        HashMap param = new HashMap();
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        if (productList != null && productList.size() > 0) {
            for (int i = 0; i < productList.size(); i++) {
                param = (HashMap) productList.get(i);
                if (param != null && param.size() > 0) {
                    key.add(CommonUtil.convertObjToStr(param.get("LOOKUP_REF_ID")));
                    value.add(CommonUtil.convertObjToStr(param.get("LOOKUP_DESC")));
                }
            }
        }
        this.cbmDepositProduct = new ComboBoxModel(key, value);
        param = null;
        key = new ArrayList();
        value = new ArrayList();
        key = null;
        value = null;
    }

    public void resetDividendCalcDetails() {
        resetForm();
        setChanged();
        ttNotifyObservers();
    }

    public void resetForm() {
        setTxtDebitGl("");
        setTxtDepositInterestPercent("");
        setTxtPayableGl("");
        setTxtTotalAmount("");
        setTxtRemarks("");
        setTxtResolutionNo("");
        setcboDepositProduct("");
        setTdtResolutionDate("");
        setTdtFromPeriod("");
        setTdtToPeriod("");
        setTxtProdId("");
        resetDividendCalcListTable();
        setChanged();
        ttNotifyObservers();
    }

    private void createDividendCalcTransTable() throws Exception {
        interestCalcColoumn = new ArrayList();
        interestCalcColoumn.add("Deposit No");
        interestCalcColoumn.add("Name");
        interestCalcColoumn.add("Outstanding Amount");
        interestCalcColoumn.add("Product ID");
        interestCalcColoumn.add("Interest Amount");
        tblDepositInterestCalculation = new EnhancedTableModel(null, interestCalcColoumn);
    }

    public void resetDividendCalcListTable() {
        for (int i = tblDepositInterestCalculation.getRowCount(); i > 0; i--) {
            tblDepositInterestCalculation.removeRow(0);
        }
    }

    public void insertTableData() {
        try {
            HashMap whereMap = new HashMap();
            whereMap.put("BEHAVES_LIKE", getCbmDepositProduct().getKeyForSelected());
            whereMap.put("PROD_ID",getTxtProdId());
            whereMap.put("ROI",CommonUtil.convertObjToDouble( getTxtDepositInterestPercent()));
            String behavesLike = CommonUtil.convertObjToStr(getCbmDepositProduct().getKeyForSelected());
            if (behavesLike.equals("BENEVOLENT")) {
                whereMap.put("RESERVE_PERCENT", CommonUtil.convertObjToDouble(getTxtReservePercent()));
            }
            whereMap.put("TO_DATE", getTdtToPeriod());
            whereMap.put("FROM_DATE", getTdtFromPeriod());
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            
            HashMap dataMap = new HashMap();
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
              if (behavesLike.equals("THRIFT")) {
                    List depositIntList = ClientUtil.executeQuery("getThriftInterestProcessing", whereMap);
           if(depositIntList!=null&&depositIntList.size()>0)
           {
            for (int i = 0; i < depositIntList.size(); i++) {
                  dataMap = (HashMap)  depositIntList.get(i);
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("ACT_NUM"));
                    rowList.add(dataMap.get("NAME"));
                    rowList.add(dataMap.get("INTEREST"));
                    tableList.add(rowList);
            }
              }
                    interestCalcColoumn = new ArrayList();
                    interestCalcColoumn.add("Deposit No");
                    interestCalcColoumn.add("Name");
                  //  interestCalcColoumn.add("Outstanding Amount");
                   // interestCalcColoumn.add("Product ID");
                    interestCalcColoumn.add("Interest Amount");
                    tblDepositInterestCalculation = new EnhancedTableModel(null, interestCalcColoumn);
                    tblDepositInterestCalculation = new EnhancedTableModel((ArrayList) tableList, interestCalcColoumn);
              }
             if (behavesLike.equals("BENEVOLENT")) {
                 
                       List depositIntList = ClientUtil.executeQuery("getThriftInterestProcessing", whereMap);
           if(depositIntList!=null&&depositIntList.size()>0)
           {
            for (int i = 0; i < depositIntList.size(); i++) {
                  dataMap = (HashMap)  depositIntList.get(i);
                  double RESERVEINTEREST=CommonUtil.convertObjToDouble(dataMap.get("INTEREST"))*CommonUtil.convertObjToDouble(getTxtReservePercent())/100;
                  RESERVEINTEREST = Math.round(RESERVEINTEREST); // Added by nithya on 26-03-2018 for 0009567: Deposit Account --> Interest Processing -->Benevolent Deposit - Interest Part round off needed.
                  double MEMBERINTEREST=CommonUtil.convertObjToDouble(dataMap.get("INTEREST"))-RESERVEINTEREST;
                    rowList = new ArrayList();
                    rowList.add(dataMap.get("ACT_NUM"));
                    rowList.add(dataMap.get("NAME"));
                    rowList.add(dataMap.get("INTEREST"));
                    rowList.add(CommonUtil.convertObjToStr(RESERVEINTEREST));
                    rowList.add(CommonUtil.convertObjToStr(MEMBERINTEREST));
                    tableList.add(rowList);
            }
              }
                    interestCalcColoumn = new ArrayList();
                    interestCalcColoumn.add("Deposit No");
                    interestCalcColoumn.add("Name");
                    interestCalcColoumn.add("Interest Amount");
                    interestCalcColoumn.add("Reserve Fund");
                    interestCalcColoumn.add("MemberInterest");
                    tblDepositInterestCalculation = new EnhancedTableModel((ArrayList)tableList , interestCalcColoumn);
                } 
           }
//            HashMap proxyResultMap = proxy.executeQuery(whereMap, map);
//            if (proxyResultMap != null && proxyResultMap.size() > 0) {
//                System.out.println("proxyResultMap" + proxyResultMap);
//                ArrayList detailesList = new ArrayList();
//                List dataTableList = (ArrayList) proxyResultMap.get("detailedList");
//                if (dataTableList != null && dataTableList.size() > 0) {
//                    int size = dataTableList.size();
//                    for (int i = 0; i < size; i++) {
//                        ArrayList dataArrayList = new ArrayList();
//                        List dataList = (List) dataTableList.get(i);
//                        int listSize = dataList.size();
//                        for (int j = 0; j < listSize; j++) {
//                            dataArrayList.add(CommonUtil.convertObjToStr(dataList.get(j)));
//                        }
//                        detailesList.add(dataArrayList);
//                    }
//                }
//                if (behavesLike.equals("BENEVOLENT")) {
//                    interestCalcColoumn = new ArrayList();
//                    interestCalcColoumn.add("Deposit No");
//                    interestCalcColoumn.add("Name");
//                    interestCalcColoumn.add("Outstanding Amount");
//                    interestCalcColoumn.add("Product ID");
//                    interestCalcColoumn.add("Interest Amount");
//                    interestCalcColoumn.add("Reserve Fund");
//                    tblDepositInterestCalculation = new EnhancedTableModel((ArrayList) detailesList, interestCalcColoumn);
//                } else {
//                    interestCalcColoumn = new ArrayList();
//                    interestCalcColoumn.add("Deposit No");
//                    interestCalcColoumn.add("Name");
//                  //  interestCalcColoumn.add("Outstanding Amount");
//                    interestCalcColoumn.add("Product ID");
//                    interestCalcColoumn.add("Interest Amount");
//                    tblDepositInterestCalculation = new EnhancedTableModel(null, interestCalcColoumn);
//                    tblDepositInterestCalculation = new EnhancedTableModel((ArrayList) detailesList, interestCalcColoumn);
//                }
//            }
         catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
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
        System.out.println("command : " + command);
        return command;
    }
private ThriftBenCalculationTO setThriftBenMasterDetails() {
        HashMap  thriftBenMap = new HashMap();
         ThriftBenCalculationTO objThriftBenCalculationTo=null;
          objThriftBenCalculationTo = new ThriftBenCalculationTO();
        try {
            objThriftBenCalculationTo.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            objThriftBenCalculationTo.setAuthorizeDate(curDate);
            objThriftBenCalculationTo.setBranchCode(getSelectedBranchID());
            if (getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                objThriftBenCalculationTo.setCreatedDt(currDt);
                objThriftBenCalculationTo.setCreatedBy(TrueTransactMain.USER_ID);
                objThriftBenCalculationTo.setStatus(CommonConstants.STATUS_CREATED);
            } else if (getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                objThriftBenCalculationTo.setStatus(CommonConstants.STATUS_MODIFIED);
            } else if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                objThriftBenCalculationTo.setStatus(CommonConstants.STATUS_DELETED);
            }
            objThriftBenCalculationTo.setStatusBy(TrueTransactMain.USER_ID);
            objThriftBenCalculationTo.setStatusDate(curDate);
            objThriftBenCalculationTo.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objThriftBenCalculationTo.setTxtIntPercent(CommonUtil.convertObjToDouble(txtReservePercent));
            objThriftBenCalculationTo.setTxtRemarks(txtRemarks);
            objThriftBenCalculationTo.setTxtDebitGl(txtDebitGl);
            objThriftBenCalculationTo.setTxtReservefundPercent(CommonUtil.convertObjToDouble(txtReservePercent));
            objThriftBenCalculationTo.setTdtFromPeriod(DateUtil.getDateMMDDYYYY(getTdtFromPeriod()));
            objThriftBenCalculationTo.setTdtToPeriod(DateUtil.getDateMMDDYYYY(getTdtToPeriod()));
            objThriftBenCalculationTo.setTxtTotalAmount(CommonUtil.convertObjToDouble(txtTotalAmount));
            objThriftBenCalculationTo.setProdId(txtProdId);

        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        return objThriftBenCalculationTo;
    }
    public HashMap doAction() {
        HashMap proxyResultMap = new HashMap();
        try {
            if (data == null) {
                data = new HashMap();
            }
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                data.put("COMMAND", getCommand());
                data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                data.put("PERCENT", getTxtDepositInterestPercent());
                data.put("FROM_DT", getTdtFromPeriod());
                data.put("TO_DT", getTdtToPeriod());
                data.put("RESOLUTION_DT", getTdtResolutionDate());
                data.put("RESOLUTION_NO", getTxtResolutionNo());
                data.put("REMARKS", getTxtRemarks());
                data.put("DEBIT_GL", getTxtDebitGl());
                data.put("PAYABLE_GL", getTxtPayableGl());
                data.put("TOTAL_AMT", getTxtTotalAmount());
                if (getTxtReserveFundGl() != null && getTxtReservePercent() != null && !getTxtReserveFundGl().equals("") && !getTxtReservePercent().equals("")) {
                    data.put("RESERVE_FUND_GL", getTxtReserveFundGl());
                    data.put("RESERVE_FUND_PERCENT", getTxtReservePercent());
                }
                data.put("BEHAVES_LIKE", getCbmDepositProduct().getKeyForSelected());
                data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                if (getInterestDetailsList() != null && getInterestDetailsList().size() > 0) {
                    data.put("DEPOSIT_INTEREST_DETAILS", getInterestDetailsList());
                }
                data.put("DEPOSIT_INT_MASTER", setThriftBenMasterDetails());                
                proxyResultMap = proxy.execute(data, map);
                setProxyReturnMap(proxyResultMap);
                setGetDepositDetailsMap(proxyResultMap);
                if (proxyResultMap != null && proxyResultMap.size() > 0) {
                    
                }
                data = null;
                setResult(actionType);
                actionType = ClientConstants.ACTIONTYPE_CANCEL;
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
        return proxyResultMap;
    }

    public String getTxtProdId() {
        return txtProdId;
    }

    public void setTxtProdId(String txtProdId) {
        this.txtProdId = txtProdId;
    }
   
    public ComboBoxModel getCbmSchemName() {
        return cbmSchemName;
    }

    public void setCbmSchemName(ComboBoxModel cbmSchemName) {
        this.cbmSchemName = cbmSchemName;
    }
    
    /**
     * Getter for property transactionOB.
     *
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    /**
     * Getter for property actionType.
     *
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     *
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public void ttNotifyObservers() {
        notifyObservers();
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
     * Getter for property allowedTransactionDetailsTO.
     *
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     *
     * @param allowedTransactionDetailsTO New value of property
     * allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property txtDrfTransAmount.
     *
     * @return Value of property txtDrfTransAmount.
     */
    public java.lang.String getTxtDrfTransAmount() {
        return txtDrfTransAmount;
    }

    /**
     * Setter for property txtDrfTransAmount.
     *
     * @param txtDrfTransAmount New value of property txtDrfTransAmount.
     */
    public void setTxtDrfTransAmount(java.lang.String txtDrfTransAmount) {
        this.txtDrfTransAmount = txtDrfTransAmount;
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

    /**
     * Getter for property lblDrfTransAddressCont.
     *
     * @return Value of property lblDrfTransAddressCont.
     */
    public java.lang.String getLblDrfTransAddressCont() {
        return lblDrfTransAddressCont;
    }

    /**
     * Setter for property lblDrfTransAddressCont.
     *
     * @param lblDrfTransAddressCont New value of property
     * lblDrfTransAddressCont.
     */
    public void setLblDrfTransAddressCont(java.lang.String lblDrfTransAddressCont) {
        this.lblDrfTransAddressCont = lblDrfTransAddressCont;
    }

    /**
     * Getter for property rdoShareDividendCalculation.
     *
     * @return Value of property rdoShareDividendCalculation.
     */
    public java.lang.String getRdoShareDividendCalculation() {
        return rdoShareDividendCalculation;
    }

    /**
     * Setter for property rdoShareDividendCalculation.
     *
     * @param rdoShareDividendCalculation New value of property
     * rdoShareDividendCalculation.
     */
    public void setRdoShareDividendCalculation(java.lang.String rdoShareDividendCalculation) {
        this.rdoShareDividendCalculation = rdoShareDividendCalculation;
    }

    /**
     * Getter for property chkDueAmtPayment.
     *
     * @return Value of property chkDueAmtPayment.
     */
    public java.lang.String getChkDueAmtPayment() {
        return chkDueAmtPayment;
    }

    /**
     * Setter for property chkDueAmtPayment.
     *
     * @param chkDueAmtPayment New value of property chkDueAmtPayment.
     */
    public void setChkDueAmtPayment(java.lang.String chkDueAmtPayment) {
        this.chkDueAmtPayment = chkDueAmtPayment;
    }

    /**
     * Getter for property cbmDrfTransProdID.
     *
     * @return Value of property cbmDrfTransProdID.
     */
//    public com.see.truetransact.clientutil.ComboBoxModel getCbmDrfTransProdID() {
//        return cbmDrfTransProdID;
//    }
//    
//    /**
//     * Setter for property cbmDrfTransProdID.
//     * @param cbmDrfTransProdID New value of property cbmDrfTransProdID.
//     */
//    public void setCbmDrfTransProdID(com.see.truetransact.clientutil.ComboBoxModel cbmDrfTransProdID) {
//        this.cbmDrfTransProdID = cbmDrfTransProdID;
//    }
    /**
     * Getter for property cboDrfTransProdID.
     *
     * @return Value of property cboDrfTransProdID.
     */
    public java.lang.String getCboDrfTransProdID() {
        return cboDrfTransProdID;
    }

    /**
     * Setter for property cboDrfTransProdID.
     *
     * @param cboDrfTransProdID New value of property cboDrfTransProdID.
     */
    public void setCboDrfTransProdID(java.lang.String cboDrfTransProdID) {
        this.cboDrfTransProdID = cboDrfTransProdID;
    }

    /**
     * Getter for property drfTransID.
     *
     * @return Value of property drfTransID.
     */
//    public java.lang.String getDrfTransID() {
//        return drfTransID;
//    }
//    
//    /**
//     * Setter for property drfTransID.
//     * @param drfTransID New value of property drfTransID.
//     */
//    public void setDrfTransID(java.lang.String drfTransID) {
//        this.drfTransID = drfTransID;
//    }
    /**
     * Getter for property cboShareClass.
     *
     * @return Value of property cboShareClass.
     */
    public java.lang.String getcboDepositProduct() {
        return cboDepositProduct;
    }

    /**
     * Setter for property cboShareClass.
     *
     * @param cboShareClass New value of property cboShareClass.
     */
    public void setcboDepositProduct(java.lang.String cboShareClass) {
        this.cboDepositProduct = cboShareClass;
    }

    /**
     * Getter for property txtDebitGl.
     *
     * @return Value of property txtDebitGl.
     */
    public java.lang.String getTxtDebitGl() {
        return txtDebitGl;
    }

    /**
     * Setter for property txtDebitGl.
     *
     * @param txtDebitGl New value of property txtDebitGl.
     */
    public void setTxtDebitGl(java.lang.String txtDebitGl) {
        this.txtDebitGl = txtDebitGl;
    }

    /**
     * Getter for property txtPayableGl.
     *
     * @return Value of property txtPayableGl.
     */
    public java.lang.String getTxtPayableGl() {
        return txtPayableGl;
    }

    /**
     * Setter for property txtPayableGl.
     *
     * @param txtPayableGl New value of property txtPayableGl.
     */
    public void setTxtPayableGl(java.lang.String txtPayableGl) {
        this.txtPayableGl = txtPayableGl;
    }

    /**
     * Getter for property tdtFromPeriod.
     *
     * @return Value of property tdtFromPeriod.
     */
    public java.lang.String getTdtFromPeriod() {
        return tdtFromPeriod;
    }

    /**
     * Setter for property tdtFromPeriod.
     *
     * @param tdtFromPeriod New value of property tdtFromPeriod.
     */
    public void setTdtFromPeriod(java.lang.String tdtFromPeriod) {
        this.tdtFromPeriod = tdtFromPeriod;
    }

    /**
     * Getter for property tdtToPeriod.
     *
     * @return Value of property tdtToPeriod.
     */
    public java.lang.String getTdtToPeriod() {
        return tdtToPeriod;
    }

    /**
     * Setter for property tdtToPeriod.
     *
     * @param tdtToPeriod New value of property tdtToPeriod.
     */
    public void setTdtToPeriod(java.lang.String tdtToPeriod) {
        this.tdtToPeriod = tdtToPeriod;
    }

    /**
     * Getter for property txtDividendPercent.
     *
     * @return Value of property txtDividendPercent.
     */
    public java.lang.String getTxtDepositInterestPercent() {
        return txtDepositInterestPercent;
    }

    /**
     * Setter for property txtDividendPercent.
     *
     * @param txtDividendPercent New value of property txtDividendPercent.
     */
    public void setTxtDepositInterestPercent(java.lang.String txtDepositInterestPercent) {
        this.txtDepositInterestPercent = txtDepositInterestPercent;
    }

    /**
     * Getter for property txtResolutionNo.
     *
     * @return Value of property txtResolutionNo.
     */
    public java.lang.String getTxtResolutionNo() {
        return txtResolutionNo;
    }

    /**
     * Setter for property txtResolutionNo.
     *
     * @param txtResolutionNo New value of property txtResolutionNo.
     */
    public void setTxtResolutionNo(java.lang.String txtResolutionNo) {
        this.txtResolutionNo = txtResolutionNo;
    }

    /**
     * Getter for property tdtResolutionDate.
     *
     * @return Value of property tdtResolutionDate.
     */
    public java.lang.String getTdtResolutionDate() {
        return tdtResolutionDate;
    }

    /**
     * Setter for property tdtResolutionDate.
     *
     * @param tdtResolutionDate New value of property tdtResolutionDate.
     */
    public void setTdtResolutionDate(java.lang.String tdtResolutionDate) {
        this.tdtResolutionDate = tdtResolutionDate;
    }

    /**
     * Getter for property txtRemarks.
     *
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }

    /**
     * Setter for property txtRemarks.
     *
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    /**
     * Getter for property dividendID.
     *
     * @return Value of property dividendID.
     */
//    public java.lang.String getDividendID() {
//        return dividendID;
//    }
//    
//    /**
//     * Setter for property dividendID.
//     * @param dividendID New value of property dividendID.
//     */
//    public void setDividendID(java.lang.String dividendID) {
//        this.dividendID = dividendID;
//    }
//    
    /**
     * Getter for property tblDepositInterestCalculation.
     *
     * @return Value of property tblDepositInterestCalculation.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDepositInterestCalculation() {
        return tblDepositInterestCalculation;
    }

    /**
     * Setter for property tblDepositInterestCalculation.
     *
     * @param tblDepositInterestCalculation New value of property
     * tblDepositInterestCalculation.
     */
    public void setTblDepositInterestCalculation(com.see.truetransact.clientutil.EnhancedTableModel tblDepositInterestCalculation) {
        this.tblDepositInterestCalculation = tblDepositInterestCalculation;
    }

    /**
     * Getter for property cbmShareClass.
     *
     * @return Value of property cbmShareClass.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepositProduct() {
        return cbmDepositProduct;
    }

    /**
     * Setter for property cbmShareClass.
     *
     * @param cbmShareClass New value of property cbmShareClass.
     */
    public void setCbmDepositProduct(com.see.truetransact.clientutil.ComboBoxModel cbmDepositProduct) {
        this.cbmDepositProduct = cbmDepositProduct;
    }

    /**
     * Getter for property getShareDetailsMap.
     *
     * @return Value of property getShareDetailsMap.
     */
    public java.util.HashMap getGetDepositDetailsMap() {
        return getDepositDetailsMap;
    }

    /**
     * Setter for property getShareDetailsMap.
     *
     * @param getShareDetailsMap New value of property getShareDetailsMap.
     */
    public void setGetDepositDetailsMap(java.util.HashMap getDepositDetailsMap) {
        this.getDepositDetailsMap = getDepositDetailsMap;
    }

    /**
     * Getter for property txtTotalAmount.
     *
     * @return Value of property txtTotalAmount.
     */
    public java.lang.String getTxtTotalAmount() {
        return txtTotalAmount;
    }

    /**
     * Setter for property txtTotalAmount.
     *
     * @param txtTotalAmount New value of property txtTotalAmount.
     */
    public void setTxtTotalAmount(java.lang.String txtTotalAmount) {
        this.txtTotalAmount = txtTotalAmount;
    }

    public List getInterestDetailsList() {
        return interestDetailsList;
    }

    public void setInterestDetailsList(List interestDetailsList) {
        this.interestDetailsList = interestDetailsList;
    }

    public String getTxtReserveFundGl() {
        return txtReserveFundGl;
    }

    public void setTxtReserveFundGl(String txtReserveFundGl) {
        this.txtReserveFundGl = txtReserveFundGl;
    }

    public String getTxtReservePercent() {
        return txtReservePercent;
    }

    public void setTxtReservePercent(String txtReservePercent) {
        this.txtReservePercent = txtReservePercent;
    }
}