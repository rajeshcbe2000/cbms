/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 15-06-2015
 */
package com.see.truetransact.ui.product.commission;

import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.transferobject.product.commission.CommissionMainTO;
import com.see.truetransact.transferobject.product.commission.CommissionTO;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.tds.tdsconfig.TDSConfigTO;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author Ashok Vijayakumar
 */
public class DailyCommissionOB extends CObservable {

    Date curDate = null;
    private String txtFrom = "";
    private String txtTo = "";
    private String txtCommId = "";
    private String txtAmt = "";
    private String txtPenal = "";
    private String cboProductType = "";
    private String cboCommTypeVal = "";
    private ComboBoxModel cbmProductType;
    private ComboBoxModel cbmCommType;
    private ArrayList key, value;
    private ProxyFactory proxy;
    private static DailyCommissionOB objDailyCommissionOB;
    private HashMap map, keyValue, lookUpHash;
    private final static Logger log = Logger.getLogger(DailyCommissionOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int _result, _actionType;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final String YES = "Y";
    private final String NO = "N";
    private EnhancedTableModel tblDetails;
    final ArrayList detailsTitle = new ArrayList();
    LinkedHashMap detailsTO = new LinkedHashMap();
    private ArrayList rowDataForTransDetails = null;
    LinkedHashMap transactiondetailMap = new LinkedHashMap();
    int noOfDeletedTransTOs = 0;
    int transactionSerialNo = 1;
    private String type = "";
//    private String tdsCeAchdId="";

    /**
     * Consturctor Declaration for TDSConfigOB
     */
    private DailyCommissionOB() {
        try {
            curDate = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropDown();
            setTransDetailsTitle();
            tblDetails = new EnhancedTableModel(null, detailsTitle);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    static {
        try {
            log.info("Creating ParameterOB...");
            objDailyCommissionOB = new DailyCommissionOB();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setTransDetailsTitle() throws Exception {
        detailsTitle.add("Sl.No");
        detailsTitle.add("From");
        detailsTitle.add("To");
        detailsTitle.add("Amount");
        detailsTitle.add("Penal");
    }
    // Sets the HashMap required to set JNDI,Home and Remote

    private void setOperationMap() throws Exception {
        map = new HashMap();
        map.put(CommonConstants.JNDI, "DailyCommissionJNDI");
        map.put(CommonConstants.HOME, "product.dailyCommission.DailyCommissionHome");
        map.put(CommonConstants.REMOTE, "product.dailyCommission.DailyCommissionBean");
    }

    /**
     * Creating instance for ComboboxModel cbmTokenType
     */
    private void initUIComboBoxModel() {
        cbmCommType = new ComboBoxModel();
        cbmProductType = new ComboBoxModel();
    }

    /*
     * Filling up the the ComboBox in the UI
     */
    private void fillDropDown() throws Exception {
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.JNDI, "LookUpJNDI");
        lookUpHash.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookUpHash.put(CommonConstants.REMOTE, "common.lookup.LookUp");

        final HashMap param = new HashMap();

        param.put(CommonConstants.MAP_NAME, "getDepositProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        //        System.out.println("**paramMap :"+param);
        HashMap lookupValues = ClientUtil.populateLookupData(param);

        getKeyValue((HashMap) lookupValues.get(CommonConstants.DATA));
        cbmProductType = new ComboBoxModel(key, value);

    }

    /**
     * Populates two ArrayList key,value
     */
    private void getKeyValue(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public EnhancedTableModel getTblDetails() {
        return tblDetails;
    }

    public void setTblDetails(EnhancedTableModel tblDetails) {
        this.tblDetails = tblDetails;
    }

    public LinkedHashMap getTransactiondetailMap() {
        return transactiondetailMap;
    }

    public void setTransactiondetailMap(LinkedHashMap transactiondetailMap) {
        this.transactiondetailMap = transactiondetailMap;
    }

    /**
     * Returns an instance of TokenConfigOB.
     *
     * @return TokenConfigOB
     */
    public static DailyCommissionOB getInstance() throws Exception {
        return objDailyCommissionOB;
    }

    public ComboBoxModel getCbmCommType() {
        return cbmCommType;
    }

    public void setCbmCommType(ComboBoxModel cbmCommType) {
        this.cbmCommType = cbmCommType;
    }

    public ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }

    public void setCbmProductType(ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }

    public String getCboCommTypeVal() {
        return cboCommTypeVal;
    }

    public void setCboCommTypeVal(String cboCommTypeVal) {
        this.cboCommTypeVal = cboCommTypeVal;
    }

    public String getCboProductType() {
        return cboProductType;
    }

    public void setCboProductType(String cboProductType) {
        this.cboProductType = cboProductType;
    }

    public String getTxtAmt() {
        return txtAmt;
    }

    public void setTxtAmt(String txtAmt) {
        this.txtAmt = txtAmt;
    }

    public String getTxtCommId() {
        return txtCommId;
    }

    public void setTxtCommId(String txtCommId) {
        this.txtCommId = txtCommId;
    }

    public String getTxtFrom() {
        return txtFrom;
    }

    public void setTxtFrom(String txtFrom) {
        this.txtFrom = txtFrom;
    }

    public String getTxtPenal() {
        return txtPenal;
    }

    public void setTxtPenal(String txtPenal) {
        this.txtPenal = txtPenal;
    }

    public String getTxtTo() {
        return txtTo;
    }

    public void setTxtTo(String txtTo) {
        this.txtTo = txtTo;
    }

    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }

    public int getActionType() {
        return _actionType;
    }

    public void setActionType(int actionType) {
        _actionType = actionType;
        setStatus();
        setChanged();
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    public int getResult() {
        return _result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Clear up all the Fields of UI thru OB *
     */
    public void resetForm() {

        setTxtCommId("");
        setCboCommTypeVal("");
        setCboProductType("");
        notifyObservers();

    }

    /*
     * Populates the TO object by executing a Query
     */
    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            mapData = proxy.executeQuery(whereMap, map);

            System.out.println("mapData XXXXXXXXXx --" + mapData);
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW && mapData.containsKey("COMM_ID")) {
                setTxtCommId(CommonUtil.convertObjToStr(mapData.get("COMM_ID")));
            }

//            CommissionTO commissionTO =
//                    (CommissionTO) ((List) mapData.get("CommissionTO")).get(0);

        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e, true);

        }
    }

    public void setPopulatedData(HashMap whereMap) {
        whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            //  tblDetails = new EnhancedTableModel(null, detailsTitle);


            if (mapData != null && mapData.size() > 0) {
                CommissionMainTO commissionMainTO = (CommissionMainTO) ((List) mapData.get("COMMISSION_MAIN")).get(0);
                if (commissionMainTO != null) {
                    setTxtCommId(commissionMainTO.getCommId());
                    setCboProductType(commissionMainTO.getProd_id());
                    setCboCommTypeVal(commissionMainTO.getPeriodIn());
                }
                if (rowDataForTransDetails == null) {
                    rowDataForTransDetails = new ArrayList();
                }
                List li = (List) mapData.get("COMMISSION_DET");
                System.out.println("liiiiiiiiiiiii :" + li);
                transactionSerialNo = li.size();
                for (int i = 0; i < li.size(); i++) {
                    CommissionTO objcomTo = (CommissionTO) li.get(i);
                    System.out.println("objcomTo --------- :" + objcomTo);
                    rowDataForTransDetails.add(setTableValuesForTransactionDetails(i + 1, objcomTo));
//     detailsTO.put(String.valueOf(transactionSerialNo), objcomTo);
//                setTransactiondetailMap(detailsTO);
//                transactionSerialNo++;
                }
                System.out.println("rowDataForTransDetails ::" + rowDataForTransDetails);
                tblDetails.setDataArrayList(rowDataForTransDetails, detailsTitle);
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e, true);

        }
    }
    /*
     * Executes Query using the TO object
     */

    public void execute(String command) {
        try {
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            term.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            System.out.println("details toooooooooooooo :" + detailsTO);
            term.put("TYPE_ID", getType());
            if (getType().equals("COMMISSION")) {
                term.put("COMMISSION_TO", setCommissionMainTO());
                term.put("COMMISSION_TO_DET", detailsTO);
            } else if (getType().equals("ROI")) {
                term.put("ROI_TO", setCommissionMainTO());
                term.put("ROI_TO_DET", detailsTO);
            }
            System.out.println("term======== " + term);;
            HashMap proxyResultMap = proxy.execute(term, map);
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * Return an ArrayList by executing Query *
     */
    public ArrayList getResultList() {
        ArrayList list = (ArrayList) ClientUtil.executeQuery("getSelectTDSDates", null);
        return list;
    }

    public CommissionTO setCommissionTO() {
        log.info("CommissionTO()");
        CommissionTO objCommissionTO = new CommissionTO();
        try {
            objCommissionTO.setCommId(getTxtCommId());
            objCommissionTO.setFromPeriod(getTxtFrom());
            objCommissionTO.setToperiod(getTxtTo());
            objCommissionTO.setAmt(CommonUtil.convertObjToDouble(getTxtAmt()));
            objCommissionTO.setPenal(CommonUtil.convertObjToDouble(getTxtPenal()));

        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        return objCommissionTO;
    }

    public CommissionMainTO setCommissionMainTO() {
        log.info("CommissionTO()");
        CommissionMainTO objmainTO = new CommissionMainTO();
        try {
            objmainTO.setCommId(getTxtCommId());
            objmainTO.setProd_id(getCboProductType());
            objmainTO.setEffect_Date(curDate);
            objmainTO.setStatus("NEW");
            objmainTO.setPeriodIn(getCboCommTypeVal());
            objmainTO.setStatusBy(TrueTransactMain.USER_ID);
            objmainTO.setBranchCode(ProxyParameters.BRANCH_ID);
            objmainTO.setCommand("INSERT");
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        return objmainTO;
    }

    public void saveTransactionDetails(boolean tableTransactionMousePressed, int selectedRow, boolean isRemitDup, int rowCnt) {
        log.info("Inside saveTransactionDetails()");
        try {
            CommissionTO objTransactionTO = setCommissionTO();
            if (detailsTO == null) {
                detailsTO = new LinkedHashMap();
            }
            if (rowDataForTransDetails == null) {
                rowDataForTransDetails = new ArrayList();
            }
            if (tableTransactionMousePressed) {
                rowDataForTransDetails.set(selectedRow, setTableValuesForTransactionDetails(selectedRow + 1, objTransactionTO));
                detailsTO.put(String.valueOf(selectedRow + 1), objTransactionTO);
                setTransactionOB(objTransactionTO);
            } else {

                rowDataForTransDetails.add(setTableValuesForTransactionDetails(transactionSerialNo, objTransactionTO));
                detailsTO.put(String.valueOf(transactionSerialNo), objTransactionTO);
                setTransactiondetailMap(detailsTO);
                transactionSerialNo++;
            }

            objTransactionTO = null;
            tblDetails.setDataArrayList(rowDataForTransDetails, detailsTitle);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private void setTransactionOB(CommissionTO objCommissionTO) throws Exception {
        ////System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>objTransactionTO : " + objTransactionTO);
        setTxtAmt(CommonUtil.convertObjToStr(objCommissionTO.getAmt()));
        setTxtPenal(CommonUtil.convertObjToStr(objCommissionTO.getPenal()));
        setTxtFrom(CommonUtil.convertObjToStr(objCommissionTO.getFromPeriod()));
        setTxtTo(CommonUtil.convertObjToStr(objCommissionTO.getToperiod()));
    }

    private ArrayList setTableValuesForTransactionDetails(int serial_No_OR_selectedRow, CommissionTO objCommissionTO) {
        ArrayList singleRow = new ArrayList();
        singleRow.add(CommonUtil.convertObjToStr(serial_No_OR_selectedRow));
        singleRow.add(CommonUtil.convertObjToStr(objCommissionTO.getFromPeriod()));
        singleRow.add(CommonUtil.convertObjToStr(objCommissionTO.getToperiod()));
        singleRow.add(CommonUtil.convertObjToStr(objCommissionTO.getAmt()));
        singleRow.add(CommonUtil.convertObjToStr(objCommissionTO.getPenal()));
        return singleRow;

    }

    public void deleteTransDetails(int row) {
        log.info("Inside deleteTransDetails()");
        int size = detailsTO.size();
        try {
            CommissionTO objTransactionTO;
            if (size == 1 || row + 1 == size) {
                objTransactionTO = (CommissionTO) detailsTO.remove(String.valueOf((row + 1)));
            } else {
                objTransactionTO = (CommissionTO) detailsTO.get(String.valueOf((row + 1)));
                for (int i = row + 1; i < size; i++) {
                    detailsTO.put(String.valueOf(i), (CommissionTO) detailsTO.remove(String.valueOf((i + 1))));
                }
            }
            if ((CommonUtil.convertObjToStr(objTransactionTO.getStatus()).length() > 0)
                    && (objTransactionTO.getStatus() != null)
                    && !(CommonUtil.convertObjToStr(objTransactionTO.getStatus()).equals(""))) {
                if (detailsTO == null) {
                    detailsTO = new LinkedHashMap();
                }
                detailsTO.put(String.valueOf(noOfDeletedTransTOs++), objTransactionTO);
                setTransactiondetailMap(detailsTO);
            }
            objTransactionTO = null;

            rowDataForTransDetails.remove(row);
            System.out.println("rowDataForTransDetails :" + rowDataForTransDetails);
            /*
             * Orders the serial no in the arraylist (tableData) after the
             * removal of selected Row in the table
             */
            for (int i = 0, j = rowDataForTransDetails.size(); i < j; i++) {
                ((ArrayList) rowDataForTransDetails.get(i)).set(0, String.valueOf(i + 1));
            }

            tblDetails.setDataArrayList(rowDataForTransDetails, detailsTitle);
            transactionSerialNo--;

        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    public void resetObjects() {
        resetForm();
        resetTransactionDetails();
        tblDetails = new EnhancedTableModel(null, detailsTitle);
        detailsTO = new LinkedHashMap();
        transactionSerialNo = 1;
        rowDataForTransDetails = new ArrayList();
        notifyObservers();
    }

    public void resettableData() {
        tblDetails = new EnhancedTableModel(null, detailsTitle);
        detailsTO = new LinkedHashMap();
        transactionSerialNo = 1;
        rowDataForTransDetails = new ArrayList();
        notifyObservers();
    }

    public void resetTransactionDetails() {
        setTxtAmt("");
        setTxtTo("");
        setTxtPenal("");
        setTxtFrom("");
    }
}