/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * NewBorrowingOB.java
 *
 * Created on September 12, 2011, 12:27 PM
 */

package com.see.truetransact.ui.borrowings;
import org.apache.log4j.Logger;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.transferobject.borrowings.master.BorrowingsTO;
import com.see.truetransact.transferobject.borrowings.master.BorrowingsChequeTO;
import com.ibatis.db.sqlmap.SqlMap;
import java.util.Date;
import com.see.truetransact.clientutil.EnhancedTableModel;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB ; //trans details
/**
 *
 * @author  user
 */

public class NewBorrowingOB extends CObservable{
    private String txtBorrowingNo = "", cboAgency = "",
            txtBorrowingRefNo = "", cboType = "", txtaDescription = "",
            cboPrinRepFrq = "",
            cboIntRepFrq = "", txtMorotorium = "", txaSecurityDet = "",
            txtprinGrpHead = "", txtintGrpHead = "", txtpenalGrpHead = "",
            txtchargeGrpHead = "", txtPenalIntRate = null, cboMultiDis = "", cboRenReq = "";
    Date tdtDateExpiry = null;
    Double txtAmtSanctioned, txtAmtBorrowed, txtRateInterest, txtnoofInstall;
    private String txtSanctionOrderNo = "";
    Date tdtSanctionDate = null;
    String tdtDateSanctioned = "";
    String chkGovtLoan = "";
    public String getChkGovtLoan() {
        return chkGovtLoan;
    }
    public void setChkGovtLoan(String chkGovtLoan) {
        this.chkGovtLoan = chkGovtLoan;
    }

    public String getTdtDateSanctioned() {
        return tdtDateSanctioned;
    }

    public void setTdtDateSanctioned(String tdtDateSanctioned) {
        this.tdtDateSanctioned = tdtDateSanctioned;
    }

    public Date getTdtSanctionDate() {
        return tdtSanctionDate;
    }

    public void setTdtSanctionDate(Date tdtSanctionDate) {
        this.tdtSanctionDate = tdtSanctionDate;
    }

    public String getTxtSanctionOrderNo() {
        return txtSanctionOrderNo;
    }

    public void setTxtSanctionOrderNo(String txtSanctionOrderNo) {
        this.txtSanctionOrderNo = txtSanctionOrderNo;
    }
    
    private static SqlMap sqlMap = null;
    // private LinkedHashMap allowedTransactionDetailsTO = null; //trans details
    private LinkedHashMap transactionDetailsTO = null; //trans details
    private LinkedHashMap deletedTransactionDetailsTO = null; //trans details
    private LinkedHashMap allowedTransactionDetailsTO = null; //trans details
    private TransactionOB transactionOB; //trans details
    private HashMap authMap = new HashMap(); //trans details
    private HashMap _authorizeMap;//trans details
    
    //cheque details
    private LinkedHashMap deletedChequeMap;
    private LinkedHashMap chequeMap;
    private ArrayList IncVal = new ArrayList();
    final ArrayList tableTitle = new ArrayList();
    private EnhancedTableModel tblCheckBookTable;
    private boolean newData = false;
    private String txtSBInternalAccNo="";
    private Date tdtChequeIssueDt=null;
    private String txtFromNO="";
    private String txtToNO="";
    private String txtNoOfCheques="";
    private String txtSlNo="";
    private Date currDt=null; //trans details
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs"; //trans details
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs"; //trans details
    private Double txtAmount; //trans details
    private ComboBoxModel cbmBrAgency;//Model for ui combobox Agency
    private ComboBoxModel cbmBrType;//Model for ui combobox Type
    private ComboBoxModel cbmBrPrinRepFrq;
    private ComboBoxModel cbmBrIntRepFrq;
    private final static Logger log = Logger.getLogger(NewBorrowingOB.class);//Creating Instace of Log
    private ProxyFactory proxy;
    private int _actionType;//This vairable is used to type of action 1-New 2-Edit 3-Delete
    private int _result;
    private static NewBorrowingOB objNewBorrowingOB;//Singleton Object Reference
    private HashMap map;//HashMap for setting key,values for Jndi,home,remote
    private HashMap lookUpHash;//HashMap used for puting a mapname with a key which is to be used for filling ComboBoxModel
    private ArrayList key,value;//ArrayList for populating the ComboBoxModel key-Storing keys value-for Storing Values
    private HashMap keyValue;//Which holds the resultset values as key,value pairs for populatine ComboBoxModel
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    
    void setCboAgency(String cboAgency){
        this.cboAgency = cboAgency;
        setChanged();
    }
    
    String getCboAgency(){
        return this.cboAgency;
    }
    void setTxtBorrowingRefNo(String txtBorrowingRefNo){
        this.txtBorrowingRefNo = txtBorrowingRefNo;
        setChanged();
    }
    
    String getTxtBorrowingRefNo(){
        return this.txtBorrowingRefNo;
    }
    void setCboType(String cboType){
        this.cboType = cboType;
        setChanged();
    }
    String getTxtPenalIntRate(){
        return this.txtPenalIntRate;
    }
    void setTxtPenalIntRate(String txtPenIntRate){
        this.txtPenalIntRate = txtPenIntRate;
        setChanged();
    }
    String getCboMultiDisbursal(){
        return this.cboMultiDis;
    }
    void setCboMultiDisbursal(String CboMultiDisbursal){
        this.cboMultiDis = CboMultiDisbursal;
        setChanged();
    }
    String getCboRenReq(){
        return this.cboRenReq;
    }
    void setCboRenReq(String cboRenReq){
        this.cboRenReq = cboRenReq;
        setChanged();
    }
    
    String getCboType(){
        return this.cboType;
    }
    void setTxtaDescription(String txtaDescription){
        this.txtaDescription = txtaDescription;
        setChanged();
    }
    
    String getTxtaDescription(){
        return this.txtaDescription;
    }
    void setTxtRateInterest(Double txtRateInterest){
        this.txtRateInterest = txtRateInterest;
        setChanged();
    }
    
    Double getTxtRateInterest(){
        return this.txtRateInterest;
    }
    void setTxtnoofInstall(Double txtnoofInstall){
        this.txtnoofInstall = txtnoofInstall;
        setChanged();
    }
    
    Double getTxtnoofInstall(){
        return this.txtnoofInstall;
    }
    void setCboPrinRepFrq(String cboPrinRepFrq){
        this.cboPrinRepFrq = cboPrinRepFrq;
        setChanged();
    }
    
    String getCboPrinRepFrq(){
        return this.cboPrinRepFrq;
    }
    void setCboIntRepFrq(String cboIntRepFrq){
        this.cboIntRepFrq = cboIntRepFrq;
        setChanged();
    }
    
    String getCboIntRepFrq(){
        return this.cboIntRepFrq;
    }
    void setTxtMorotorium(String txtMorotorium){
        this.txtMorotorium = txtMorotorium;
        setChanged();
    }
    
    String getTxtMorotorium(){
        return this.txtMorotorium;
    }
    void setTxaSecurityDet(String txaSecurityDet){
        this.txaSecurityDet = txaSecurityDet;
        setChanged();
    }
    
    String getTxaSecurityDet(){
        return this.txaSecurityDet;
    }
    void setTxtprinGrpHead(String txtprinGrpHead){
        this.txtprinGrpHead = txtprinGrpHead;
        setChanged();
    }
    
    String getTxtprinGrpHead(){
        return this.txtprinGrpHead;
    }
    void setTxtintGrpHead(String txtintGrpHead){
        this.txtintGrpHead = txtintGrpHead;
        setChanged();
    }
    
    String getTxtintGrpHead(){
        return this.txtintGrpHead;
    }
    void setTxtpenalGrpHead(String txtpenalGrpHead){
        this.txtpenalGrpHead = txtpenalGrpHead;
        setChanged();
    }
    
    String getTxtpenalGrpHead(){
        return this.txtpenalGrpHead;
    }
    void setTxtchargeGrpHead(String txtchargeGrpHead){
        this.txtchargeGrpHead = txtchargeGrpHead;
        setChanged();
    }
    
    String getTxtchargeGrpHead(){
        return this.txtchargeGrpHead;
    }
   
    void setDateExpiry(Date tdtDateExpiry){
        this.tdtDateExpiry = tdtDateExpiry;
        setChanged();
    }
    
    Date getDateExpiry(){
        return this.tdtDateExpiry;
    }
    void setAmtSanctioned(Double txtAmtSanctioned){
        this.txtAmtSanctioned = txtAmtSanctioned;
        setChanged();
    }
    
    Double getAmtSanctioned(){
        return this.txtAmtSanctioned;
    }
    void settxtAmtBorrowed(Double txtAmtBorrowed){
        this.txtAmtBorrowed = txtAmtBorrowed;
        setChanged();
    }
    
    Double gettxtAmtBorrowed(){
        return this.txtAmtBorrowed;
    }
    /** Creates a new instance of NewBorrowingOB */
    public NewBorrowingOB() {
        try {
            
            currDt=ClientUtil.getCurrentDate(); //trans details
            //cheque details
            setTableTile();
            tblCheckBookTable = new EnhancedTableModel(null, tableTitle);
            proxy = ProxyFactory.createProxy();
            setOperationMap();
            initUIComboBoxModel();
            fillDropdown();
        }catch(Exception e){
            //parseException.logException(e,true);
            System.out.println("Error in NewBorrowingOB():"+e);
        }
    }
    static {
        try {
            log.info("Creating ParameterOB...");
            objNewBorrowingOB = new NewBorrowingOB();
        } catch(Exception e) {
            // parseException.logException(e,true);
            System.out.println("Error in static():"+e);
        }
    }
    private void setTableTile() throws Exception{
        tableTitle.add("Sl No");
        tableTitle.add("Issue Dt");
        tableTitle.add("From No");
        tableTitle.add("To No");
        tableTitle.add("Quantity");
        IncVal = new ArrayList();
    }
    private void initUIComboBoxModel(){
        cbmBrAgency = new ComboBoxModel();
        cbmBrType= new ComboBoxModel();
        cbmBrPrinRepFrq= new ComboBoxModel();
    }
    // Sets the HashMap required to set JNDI,Home and Remote
    private void setOperationMap() throws Exception{
        map = new HashMap();
        map.put(CommonConstants.JNDI, "BorrowingsJNDI");
        map.put(CommonConstants.HOME, "borrwings.BorrwingHome");
        map.put(CommonConstants.REMOTE, "borrwings.Borrwing");
    }
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBrAgency() {
        return cbmBrAgency;
    }
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBrType() {
        return cbmBrType;
    }
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBrPrinRepFrq() {
        return cbmBrPrinRepFrq;
    }
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIntRepFrq() {
        return cbmBrIntRepFrq;
    }
    
    public static NewBorrowingOB getInstance()throws Exception{
        return objNewBorrowingOB;
    }
    public int getActionType(){
        return _actionType;
    }
    
    public void setActionType(int actionType) {
        _actionType = actionType;
        //  System.out.println("setActionType IN"+actionType);
        setStatus();
        setChanged();
    }
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
    }
    /**
     * Setter for property cbmTokenType.
     * @param cbmTokenType New value of property cbmTokenType.
     */
    public void setCbmBrAgency(com.see.truetransact.clientutil.ComboBoxModel cbmBrAgency) {
        this.cbmBrAgency = cbmBrAgency;
    }
    public void setCbmBrType(com.see.truetransact.clientutil.ComboBoxModel cbmBrType) {
        this.cbmBrType = cbmBrType;
    }
    public void setCbmBrPrinRepFrq(com.see.truetransact.clientutil.ComboBoxModel cbmBrPrinRepFrq) {
        this.cbmBrPrinRepFrq = cbmBrPrinRepFrq;
    }
    public void setCbmBrIntRepFrq(com.see.truetransact.clientutil.ComboBoxModel cbmBrIntRepFrq) {
        this.cbmBrIntRepFrq = cbmBrIntRepFrq;
    }
    
    /** Populates two ArrayList key,value */
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    public void populateData(HashMap whereMap) {
        HashMap mapData=null;
        try {
            mapData = proxy.executeQuery(whereMap, map);
            BorrowingsTO objTO =(BorrowingsTO) ((List) mapData.get("BorrowingTO")).get(0);
            setBorrowingTO(objTO);
            //trans details
            if(mapData.containsKey("TRANSACTION_LIST")){
                List list = (List) mapData.get("TRANSACTION_LIST");
                if (!list.isEmpty()) {
                    transactionOB.setDetails(list);
                }
            }
            //end..
            //cheque details
            if(mapData.containsKey("chequeListTO")){
                if( chequeMap == null ){
                    chequeMap = new LinkedHashMap();
                }
                chequeMap = (LinkedHashMap)mapData.get("chequeListTO");
                ArrayList addList =new ArrayList(chequeMap.keySet());
                for(int i=0;i<addList.size();i++){
                    BorrowingsChequeTO  objBorrowingsChequeTO = (BorrowingsChequeTO)  chequeMap.get(addList.get(i));
                    objBorrowingsChequeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    ArrayList incTabRow = new ArrayList();
                    incTabRow.add(CommonUtil.convertObjToStr(objBorrowingsChequeTO.getSlNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objBorrowingsChequeTO.getIssueDt()));
                    incTabRow.add(CommonUtil.convertObjToStr(objBorrowingsChequeTO.getFromNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objBorrowingsChequeTO.getToNo()));
                    incTabRow.add(CommonUtil.convertObjToStr(objBorrowingsChequeTO.getNoOfCheques()));
                    tblCheckBookTable.addRow(incTabRow);
                }
            }
        } catch( Exception e ) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            // parseException.logException(e,true);
            System.out.println("Error in populateData():"+e);
            
        }
    }
    public void setResult(int result) {
        _result = result;
        setResultStatus();
        setChanged();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
    }
    public int getResult(){
        return _result;
    }
    private void setBorrowingTO(BorrowingsTO objTO){
        setBorrowingNo(objTO.getBorrowingNo());
        setCboAgency(CommonUtil.convertObjToStr(getCbmBrAgency().getDataForKey(objTO.getAgencyCode())));
        setTxtBorrowingRefNo(objTO.getBorrowingrefNo());
        setCboType(CommonUtil.convertObjToStr(getCbmBrType().getDataForKey(objTO.getType())));
        setTxtaDescription(objTO.getDescription());
        setTxtRateInterest(objTO.getRateofInt());
        setTxtnoofInstall(objTO.getNoofInstallments());
        setCboPrinRepFrq(objTO.getPrinRepFrq());
        setCboIntRepFrq(objTO.getIntRepFrq());
        setTxtMorotorium(objTO.getMorotorium());
        setTxaSecurityDet(objTO.getSecDetails());
        setTxtprinGrpHead(objTO.getPrinGrpHead());
        setTxtintGrpHead(objTO.getIntGrpHead());
        setTxtpenalGrpHead(objTO.getPenGrpHead());
        setTxtchargeGrpHead(objTO.getChargeGrpHead());
        setTdtDateSanctioned(CommonUtil.convertObjToStr(objTO.getSanctionDate()));
        setDateExpiry(objTO.getSanctionExpDate());
        setAmtSanctioned(objTO.getSanctionAmt());
        settxtAmtBorrowed(objTO.getAmtBorrowed());
        setTxtPenalIntRate(objTO.getPenalIntRate());
        setCboMultiDisbursal(objTO.getMultiDis());
        setCboRenReq(objTO.getRenReq());
        setTxtSanctionOrderNo(objTO.getSanOrderNo());
        setTdtSanctionDate(objTO.getSanDate());
        setChkGovtLoan(objTO.getGovtLoan());
        notifyObservers();
    }
    
    //update cheque details
    private void updateChequeDetails(int rowSel, String sno, BorrowingsChequeTO objTO)  throws Exception{
        Object selectedRow;
        boolean rowExists = false;
        //If row already exists update it, else create a new row & append
        for(int i = tblCheckBookTable.getRowCount(), j = 0; i >0; i--,j++){
            selectedRow = ((ArrayList)tblCheckBookTable.getDataArrayList().get(j)).get(0);
            if(sno.equals(CommonUtil.convertObjToStr(selectedRow))){
                rowExists = true;
                ArrayList IncParRow = new ArrayList();
                ArrayList data = tblCheckBookTable.getDataArrayList();
                data.remove(rowSel);
                IncParRow.add(sno);
                IncParRow.add(getTdtChequeIssueDt());
                IncParRow.add(getTxtFromNO());
                IncParRow.add(getTxtToNO());
                setTxtNoOfCheques(String.valueOf(Double.parseDouble(getTxtFromNO())-Double.parseDouble(getTxtToNO())));
                IncParRow.add(getTxtNoOfCheques());
                tblCheckBookTable.insertRow(rowSel,IncParRow);
                IncParRow = null;
            }
        }
        if(!rowExists){
            ArrayList IncParRow = new ArrayList();
            IncParRow.add(sno);
            IncParRow.add(getTdtChequeIssueDt());
            IncParRow.add(getTxtFromNO());
            IncParRow.add(getTxtToNO());
            IncParRow.add(getTxtNoOfCheques());
            tblCheckBookTable.insertRow(tblCheckBookTable.getRowCount(),IncParRow);
            IncParRow = null;
        }
    }
    
    //populate cheque details
    public void populateChequeDetails(String row){
        try{
            resetChequeDetails();
            final BorrowingsChequeTO objTO = (BorrowingsChequeTO) chequeMap.get(row);
            populateTableData(objTO);
            
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTableData(BorrowingsChequeTO objTO)  throws Exception{
        setTxtSlNo(CommonUtil.convertObjToStr(objTO.getSlNo()));
        setTdtChequeIssueDt(objTO.getIssueDt());
        setTxtFromNO(CommonUtil.convertObjToStr(objTO.getFromNo()));
        setTxtToNO(CommonUtil.convertObjToStr(objTO.getToNo()));
        setTxtNoOfCheques(CommonUtil.convertObjToStr(objTO.getNoOfCheques()));
        
    }
    
    public void deleteTableData(String val, int row){
        if(deletedChequeMap== null){
            deletedChequeMap = new LinkedHashMap();
        }
        BorrowingsChequeTO objTO = (BorrowingsChequeTO) chequeMap.get(val);
        objTO.setStatus(CommonConstants.STATUS_DELETED);
        deletedChequeMap.put(CommonUtil.convertObjToStr(tblCheckBookTable.getValueAt(row,0)),chequeMap.get(val));
        Object obj;
        obj=val;
        chequeMap.remove(val);
        resetTableValues();
        try{
            populateTable();
        }
        catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void populateTable()  throws Exception{
        ArrayList incDataList = new ArrayList();
        incDataList = new ArrayList(chequeMap.keySet());
        ArrayList addList =new ArrayList(chequeMap.keySet());
        int length = incDataList.size();
        for(int i=0; i<length; i++){
            ArrayList incTabRow = new ArrayList();
            BorrowingsChequeTO objTO = (BorrowingsChequeTO) chequeMap.get(addList.get(i));
            IncVal.add(objTO);
            if(!objTO.getStatus().equals("DELETED")){
                incTabRow.add(CommonUtil.convertObjToStr(objTO.getSlNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTO.getIssueDt()));
                incTabRow.add(CommonUtil.convertObjToStr(objTO.getFromNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTO.getToNo()));
                incTabRow.add(CommonUtil.convertObjToStr(objTO.getNoOfCheques()));
                tblCheckBookTable.addRow(incTabRow);
            }
        }
        notifyObservers();
    }
    
    public int serialNo(ArrayList data){
        final int dataSize = data.size();
        int nums[]= new int[150];
        int max=nums[0];
        int slno=0;
        int a=0;
        slno=dataSize+1;
        for(int i=0;i<data.size();i++){
            a=CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(i,0));
            nums[i]=a;
            if(nums[i]>max)
                max=nums[i];
            slno=max+1;
        }
        System.out.println("sl no...."+slno);
        return slno;
    }
    
    //reset cheque details
    public void resetChequeDetails() {
        // setTdtChequeIssueDt(null);
        setTxtFromNO("");
        setTxtToNO("");
        setTxtNoOfCheques("");
    }
    
    //add cheque details table
    public void addToTable(int rowSelected, boolean updateMode){
        try{
            int rowSel=rowSelected;
            final BorrowingsChequeTO objTO = new BorrowingsChequeTO();
            if( chequeMap == null ){
                chequeMap = new LinkedHashMap();
            }
            if(getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                if(isNewData()){
                    objTO.setStatusDt(currDt);
                    objTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTO.setStatus(CommonConstants.STATUS_CREATED);
                }else{
                    objTO.setStatusDt(currDt);
                    objTO.setStatusBy(TrueTransactMain.USER_ID);
                    objTO.setStatus(CommonConstants.STATUS_MODIFIED);
                }
            } else {
                objTO.setStatusDt(currDt);
                objTO.setStatusBy(TrueTransactMain.USER_ID);
                objTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            int slno = 0;
            int nums[] = new int[150];
            int max = nums[0];
            if (!updateMode) {
                ArrayList data = tblCheckBookTable.getDataArrayList();
                slno = serialNo(data);
            } else {
                if (isNewData()) {
                    ArrayList data = tblCheckBookTable.getDataArrayList();
                    slno = serialNo(data);
                } else {
                    int b = CommonUtil.convertObjToInt(tblCheckBookTable.getValueAt(rowSelected, 0));
                    slno = b;
                }
            }
            objTO.setSlNo(String.valueOf(slno));
            objTO.setBorrowingNo(getTxtSBInternalAccNo());
            objTO.setIssueDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getTdtChequeIssueDt())));
            objTO.setFromNo(getTxtFromNO());
            objTO.setToNo(getTxtToNO());
            objTO.setNoOfCheques(getTxtNoOfCheques());
            chequeMap.put(objTO.getSlNo(),objTO);
            String sno=String.valueOf(slno);
            updateChequeDetails(rowSel,sno,objTO);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    
    
    /** Resets all the UI Fields */
    public void resetForm(){
        setBorrowingNo("");
        setCboAgency("");
        setTxtBorrowingRefNo("");
        setCboType("");
        setTxtaDescription("");
        setTxtRateInterest(null);
        setTxtnoofInstall(null);
        setCboPrinRepFrq("");
        setCboIntRepFrq("");
        setTxtMorotorium("");
        setTxaSecurityDet("");
        setTxtprinGrpHead("");
        setTxtintGrpHead("");
        setTxtpenalGrpHead("");
        setTxtchargeGrpHead("");
        setTdtDateSanctioned("");
        setDateExpiry(null);
        setAmtSanctioned(null);
        settxtAmtBorrowed(null);
        setTxtAmount(null);
        setTxtPenalIntRate("");
        setCboMultiDisbursal("NO");
        setCboRenReq("NO");
        setTxtFromNO("");
        setTxtNoOfCheques("");
        setTxtSlNo("");
        setTxtToNO("");
        setChkGovtLoan("");
        chequeMap = null;
        deletedChequeMap= null;
        notifyObservers();
    }
    
    public void resetTableValues(){
        tblCheckBookTable.setDataArrayList(null,tableTitle);
    }
    
    public void setBorrowingNo(java.lang.String txtBorrowingNo) {
        this.txtBorrowingNo = txtBorrowingNo;
        setChanged();
    }
    public String getBorrowingNo() {
        return txtBorrowingNo;
        
    }
    
    /* Executes Query using the TO object */
    public void execute(String command) {
        try {
            System.out.println("GET BOPRRR NO IN EDIT :="+geBorrowingTO(command));    
            HashMap term = new HashMap();
            term.put(CommonConstants.MODULE, getModule());
            term.put(CommonConstants.SCREEN, getScreen());
            term.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            term.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            term.put("BorrowingsTO", geBorrowingTO(command));
            //cheque details
            if(chequeMap!=null && chequeMap.size()>0 ){
                term.put("chequeBookDetails",chequeMap);
            }
            if(deletedChequeMap!=null && deletedChequeMap.size()>0 ){
                term.put("deletedChequeBookDetails",deletedChequeMap);
            }
            //end..
            //trans details authorize
            if( _actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null){
                term.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
            }
            //trans end..
            //trans details
            if (transactionDetailsTO == null)
                transactionDetailsTO = new LinkedHashMap();
            if (deletedTransactionDetailsTO != null) {
                transactionDetailsTO.put(DELETED_TRANS_TOs,deletedTransactionDetailsTO);
                deletedTransactionDetailsTO = null;
            }
            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
            allowedTransactionDetailsTO = null;
            term.put("TransactionTO",transactionDetailsTO);
            if(getAuthMap() != null && getAuthMap().size() > 0 ){
                if( getAuthMap() != null){
                    term.put(CommonConstants.AUTHORIZEMAP, getAuthMap());
                }
                if(allowedTransactionDetailsTO!=null && allowedTransactionDetailsTO.size()>0){
                    if (transactionDetailsTO == null){
                        transactionDetailsTO = new LinkedHashMap();
                    }
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs,allowedTransactionDetailsTO);
                    term.put("TransactionTO",transactionDetailsTO);
                    allowedTransactionDetailsTO = null;
                }
                authMap = null;
            }
            HashMap proxyReturnMap = proxy.execute(term, map);
            setProxyReturnMap(proxyReturnMap);
            //end..
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            System.out.println("Error in execute():"+e);
        }
    }
    private BorrowingsTO geBorrowingTO(String command){
        BorrowingsTO objTO = new BorrowingsTO();
        objTO.setCommand(command);
        objTO.setBorrowingNo(getBorrowingNo());
        objTO.setAgencyCode(getCboAgency());
        objTO.setBorrowingrefNo(getTxtBorrowingRefNo());
        objTO.setType(getCboType());
        objTO.setDescription(getTxtaDescription());
        System.out.println("getDateSanctioned()========"+getTdtDateSanctioned());
        objTO.setSanctionDate(getProperDate(DateUtil.getDateMMDDYYYY(getTdtDateSanctioned())));
        System.out.println("objTO.getSanctionDate====="+objTO.getSanctionDate());
        objTO.setSanctionAmt(getAmtSanctioned());
        objTO.setAmtBorrowed(gettxtAmtBorrowed());
        objTO.setRateofInt(getTxtRateInterest());
        objTO.setNoofInstallments(getTxtnoofInstall());
        objTO.setPrinRepFrq(getCboPrinRepFrq());
        objTO.setIntRepFrq(getCboIntRepFrq());
        objTO.setMorotorium(getTxtMorotorium());
        objTO.setSanctionExpDate(getDateExpiry());
        objTO.setSecDetails(getTxaSecurityDet());
        objTO.setPrinGrpHead(getTxtprinGrpHead());
        objTO.setIntGrpHead(getTxtintGrpHead());
        objTO.setPenGrpHead(getTxtpenalGrpHead());
        objTO.setChargeGrpHead(getTxtchargeGrpHead());
        objTO.setPenalIntRate(getTxtPenalIntRate());
        objTO.setMultiDis(getCboMultiDisbursal());
        objTO.setRenReq(getCboRenReq());
        objTO.setSanOrderNo(getTxtSanctionOrderNo());
        //objTO.setSanctionDate(getTdtSanctionDate());
        System.out.println("getTxtAmount"+getTxtAmount());
        objTO.setAmount(getTxtAmount());
        objTO.setGovtLoan(getChkGovtLoan());
        if(command.equals(CommonConstants.TOSTATUS_INSERT)){
            objTO.setAuthorizeStatus(null);
            objTO.setAuthorizeBy("");
            objTO.setCreatedBy(TrueTransactMain.USER_ID);
            objTO.setAuthorizeDte(null);
            objTO.setStatus("CREATED");
        }
        
        return objTO;
    }
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDt.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    /* Filling up the the ComboBox in the UI*/
    private void fillDropdown() throws Exception{
        try{
            //This part is fill the agency dropdown
            log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("BORROWING_AGENCY");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("BORROWING_AGENCY"));
            cbmBrAgency = new ComboBoxModel(key,value);
            
            //This part is fill the type dropdown
            final ArrayList lookup_keys1 = new ArrayList();
            lookup_keys1.add("BORROWING_TYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys1);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap)keyValue.get("BORROWING_TYPE"));
            cbmBrType = new ComboBoxModel(key,value);
            
            //This part is fill the principal and interest repayment dropdowns
            final ArrayList lookup_keys2 = new ArrayList();
            final ArrayList lookup_Val2 = new ArrayList();
            lookup_keys2.add("");
            lookup_keys2.add("1"); lookup_keys2.add("2"); lookup_keys2.add("3");
            lookup_keys2.add("4");lookup_keys2.add("5");
            lookup_Val2.add("");
            lookup_Val2.add("Daily");lookup_Val2.add("Monthly");lookup_Val2.add("Quarterly");
            lookup_Val2.add("Yearly"); lookup_Val2.add("Bullet Repayment");
            cbmBrPrinRepFrq = new ComboBoxModel(lookup_keys2,lookup_Val2);
            cbmBrIntRepFrq= new ComboBoxModel(lookup_keys2,lookup_Val2);
        }catch(NullPointerException e){
            System.out.println("Error in fillDropdown():"+e);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /**
     * Getter for property txtSBInternalAccNo.
     * @return Value of property txtSBInternalAccNo.
     */
    public String getTxtSBInternalAccNo() {
        return txtSBInternalAccNo;
    }
    
    /**
     * Setter for property txtSBInternalAccNo.
     * @param txtSBInternalAccNo New value of property txtSBInternalAccNo.
     */
    public void setTxtSBInternalAccNo(String txtSBInternalAccNo) {
        this.txtSBInternalAccNo = txtSBInternalAccNo;
    }
    
    /**
     * Getter for property tdtChequeIssueDt.
     * @return Value of property tdtChequeIssueDt.
     */
    public Date getTdtChequeIssueDt() {
        return tdtChequeIssueDt;
    }
    
    /**
     * Setter for property tdtChequeIssueDt.
     * @param tdtChequeIssueDt New value of property tdtChequeIssueDt.
     */
    public void setTdtChequeIssueDt(Date tdtChequeIssueDt) {
        this.tdtChequeIssueDt = tdtChequeIssueDt;
    }
    
    /**
     * Getter for property txtFromNO.
     * @return Value of property txtFromNO.
     */
    public String getTxtFromNO() {
        return txtFromNO;
    }
    
    /**
     * Setter for property txtFromNO.
     * @param txtFromNO New value of property txtFromNO.
     */
    public void setTxtFromNO(String txtFromNO) {
        this.txtFromNO = txtFromNO;
    }
    
    /**
     * Getter for property txtToNO.
     * @return Value of property txtToNO.
     */
    public String getTxtToNO() {
        return txtToNO;
    }
    
    /**
     * Setter for property txtToNO.
     * @param txtToNO New value of property txtToNO.
     */
    public void setTxtToNO(String txtToNO) {
        this.txtToNO = txtToNO;
    }
    
    /**
     * Getter for property txtNoOfCheques.
     * @return Value of property txtNoOfCheques.
     */
    public String getTxtNoOfCheques() {
        return txtNoOfCheques;
    }
    
    /**
     * Setter for property txtNoOfCheques.
     * @param txtNoOfCheques New value of property txtNoOfCheques.
     */
    public void setTxtNoOfCheques(String txtNoOfCheques) {
        this.txtNoOfCheques = txtNoOfCheques;
    }
    
    /**
     * Getter for property newData.
     * @return Value of property newData.
     */
    public boolean isNewData() {
        return newData;
    }
    
    /**
     * Setter for property newData.
     * @param newData New value of property newData.
     */
    public void setNewData(boolean newData) {
        this.newData = newData;
    }
    
    /**
     * Getter for property tblCheckBookTable.
     * @return Value of property tblCheckBookTable.
     */
    public EnhancedTableModel getTblCheckBookTable() {
        return tblCheckBookTable;
    }
    
    /**
     * Setter for property tblCheckBookTable.
     * @param tblCheckBookTable New value of property tblCheckBookTable.
     */
    public void setTblCheckBookTable(EnhancedTableModel tblCheckBookTable) {
        this.tblCheckBookTable = tblCheckBookTable;
    }
    
    /**
     * Getter for property txtSlNo.
     * @return Value of property txtSlNo.
     */
    public String getTxtSlNo() {
        return txtSlNo;
    }
    
    /**
     * Setter for property txtSlNo.
     * @param txtSlNo New value of property txtSlNo.
     */
    public void setTxtSlNo(String txtSlNo) {
        this.txtSlNo = txtSlNo;
    }
    
    /**
     * Getter for property lblStatus.
     * @return Value of property lblStatus.
     */
    public String getLblStatus() {
        return lblStatus;
    }
    
    /**
     * Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
    }
    
    /**
     * Getter for property transactionOB.
     * @return Value of property transactionOB.
     */
    public TransactionOB getTransactionOB() {
        return transactionOB;
    }
    
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }
    
    /**
     * Getter for property currDt.
     * @return Value of property currDt.
     */
    public Date getCurrDt() {
        return currDt;
    }
    
    /**
     * Setter for property currDt.
     * @param currDt New value of property currDt.
     */
    public void setCurrDt(Date currDt) {
        this.currDt = currDt;
    }
    
    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }
    
    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    /**
     * Getter for property transactionDetailsTO.
     * @return Value of property transactionDetailsTO.
     */
    public LinkedHashMap getTransactionDetailsTO() {
        return transactionDetailsTO;
    }
    
    /**
     * Setter for property transactionDetailsTO.
     * @param transactionDetailsTO New value of property transactionDetailsTO.
     */
    public void setTransactionDetailsTO(LinkedHashMap transactionDetailsTO) {
        this.transactionDetailsTO = transactionDetailsTO;
    }
    
    /**
     * Getter for property deletedTransactionDetailsTO.
     * @return Value of property deletedTransactionDetailsTO.
     */
    public LinkedHashMap getDeletedTransactionDetailsTO() {
        return deletedTransactionDetailsTO;
    }
    
    /**
     * Setter for property deletedTransactionDetailsTO.
     * @param deletedTransactionDetailsTO New value of property deletedTransactionDetailsTO.
     */
    public void setDeletedTransactionDetailsTO(LinkedHashMap deletedTransactionDetailsTO) {
        this.deletedTransactionDetailsTO = deletedTransactionDetailsTO;
    }
    
    /**
     * Getter for property authMap.
     * @return Value of property authMap.
     */
    public HashMap getAuthMap() {
        return authMap;
    }
    
    /**
     * Setter for property authMap.
     * @param authMap New value of property authMap.
     */
    public void setAuthMap(HashMap authMap) {
        this.authMap = authMap;
    }
    
    
    
    /**
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public HashMap get_authorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    
    /**
     * Getter for property txtAmount.
     * @return Value of property txtAmount.
     */
    public Double getTxtAmount() {
        return txtAmount;
    }
    
    /**
     * Setter for property txtAmount.
     * @param txtAmount New value of property txtAmount.
     */
    public void setTxtAmount(Double txtAmount) {
        this.txtAmount = txtAmount;
    }
    
}
