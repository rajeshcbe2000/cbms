/*
 * TransactionUI.java
 *
 * Created on January 19, 2005, 1:41 PM
 */

package com.see.truetransact.ui.common.transaction;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.ui.deposit.closing.DepositClosingUI;
import com.see.truetransact.ui.directorboardmeeting.DirectorBoardUI;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.List;
import java.util.HashMap;
import java.util.Date;
import java.util.Observer;
import java.util.Observable;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.operativeaccount.AccountClosingUI;
import com.see.truetransact.ui.remittance.RemittanceIssueUI;
import com.see.truetransact.uicomponent.CInternalFrame;

/**
 *
 * @author  152691
 */
public class TransactionUI extends com.see.truetransact.uicomponent.CPanel implements Observer, UIMandatoryField{
    /*
    The below three variables are set from the calling UI.
    They will be assigned to the text fields in the New button click
     */
    private int callingUiMode = -1;
    private String callingApplicantName = "";
    private String callingDepositeAmount = "";
    private String callingAmount = "";
    private String callingInst1 = "";
    private String callingInst2 = "";
    private String callingProdID = "";
    private String callingAccNo = "";
    private String callingLtdDepositNo = "";
    private String callingStatus = "";
    private String callingDepIntRate = "";
    private String callingIntAmt = "";
    private String callingClosingType = "";
    private String callingTransProdType = "";
    private String callingTransType = "";
    private String callingTransAcctNo = "";
    private String callingParticulars = "";
    public static String NO_RECORDS = "noRecords";
    public static String SAVE_TX_DETAILS = "saveInTxDetailsTable";
    public static String NOT_TALLY = "tally";
    private String transactionMode = CommonConstants.DEBIT ; // Can be DEBIT or CREDIT, set in calling screen
    private String sourceAccountNumber = "" ; //Account Number selected in the calling UI
    TransactionRB resourceBundle = new TransactionRB();
    private javax.swing.JLabel lblMsg = new  javax.swing.JLabel();
    private javax.swing.JLabel lblStatus = new  javax.swing.JLabel();
    int viewType=-1;
    private String transactionType = ""; //CASH / TRANSFER
    private int selectedRowValue;
    private String loanActnum;
    private boolean btnSaveTransactionDetailsFlag = false;
    private boolean tabletransactionMousePressed = false;
    private boolean btnDeleteTransactionDetailsFlag = false;
    private boolean isRemitDup = false;
    private boolean isTranTblClicked = false;
    boolean isFilled = false;
    final int EDIT=0, DELETE=1,PAYEE=2,DEBIT =3, TRANS_PROD = 4, ACC_NUM=5;
    private String sourceScreen = "";
    HashMap mandatoryMap = null;
    private TransactionOB observable;
    private String txt_accno=null;
    private int saveEnableValue=0;
    private TransDetailsUI transDetails = null;
    private AccountClosingUI accountClosing = null;
    private RemittanceIssueUI remittanceIssue = null;
    private double chargeAmt = 0.0;
    private boolean depFlag;
    private double totalAmt = 0.0;
    private String depProdId = null;
    private String excessLoanAmt = "";
    private String closingDepositStatus = "";
    private String chitType = "";
    private String schemeName = "";
    private String PROD_ID = "PROD_ID";
    private int  totAmount;
    //added by Anju 15/5/14
    private Double advanceCreditIntAmt=new Double(0);
    private String addIntLoanAmt="0.0";
    private CInternalFrame parantUI=null;
    private HashMap serviceTax_Map = null;
    private Date currDt = null;
    
    private double mdsWaiveAmt = 0.0;
  
    /** Creates new form TransactionUI */
    public TransactionUI() {
        try {
            currDt = ClientUtil.getCurrentDate();
            initComponents();
            transDetails = new TransDetailsUI(panDetails);
            initStartup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addToScreen(com.see.truetransact.uicomponent.CPanel panLabelValues) {
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLabelValues.add(this, gridBagConstraints);
    }
    
    public String getMessage(String key){
        return resourceBundle.getString(key);
    }
    
    public void fillData(Object param) {
        isFilled = true;
        final HashMap hash = (HashMap) param;
        //System.out.println("branch@@@@@@@@@@2"+hash);
        String prodType=((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
        if (viewType != -1) {
            if (viewType == TRANS_PROD) {
                final String productId=(String)hash.get(PROD_ID);
                txtTransProductId.setText(productId);
                if(prodType.equals("GL")|| prodType.equals("RM")){
                    if(hash.containsKey("A/C Head")){
                        hash.put("A/C HEAD",hash.get("A/C Head"));
                    }
                    if(hash.containsKey("A/C Head Description")){
                        hash.put("A/C HEAD DESCRIPTION",hash.get("A/C Head Description"));
                    }
                    txtDebitAccNo.setText((String)hash.get("A/C HEAD"));
                    lblCustomerNameValue.setText((String)hash.get("A/C HEAD DESCRIPTION"));
                    observable.setTxtApplicantsName((String)hash.get("A/C HEAD DESCRIPTION"));
                    txtApplicantsName.setText(observable.getTxtApplicantsName());
                    txtChequeNo.setEnabled(false);
                }
                if (getSourceScreen().equals("SHARE_SCREEN") && prodType.equals("SH")){
                   txtTransProductId.setText(CommonUtil.convertObjToStr(hash.get("SHARE_TYPE"))); 
                }
            }else if (viewType == ACC_NUM) {
                String accountNum=null;
                if(prodType.equals("TD")){
                  //Changed By Suresh
                    accountNum=CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                    if(accountNum.lastIndexOf("_")==-1){
                        hash.put("ACCOUNTNO", hash.get("ACCOUNTNO")+"_1");
                    }
                    HashMap supMap = new HashMap();
                    supMap.put("DEPOSIT_NO", accountNum);
                    List lstSupName = ClientUtil.executeQuery("getDepositAmount", supMap);
                    HashMap supMap1 = new HashMap();
                    supMap1 = (HashMap) lstSupName.get(0);
                    double depositAmt =CommonUtil.convertObjToDouble(supMap1.get("DEPOSIT_AMT"));
                    double availablebal = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
                    double closingAmt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
                    String type=CommonUtil.convertObjToStr(hash.get("TYPE"));
                    if(type.equals("DAILY") && closingAmt > availablebal){
                        ClientUtil.showAlertWindow("Amount Exceeds the new Deposit Amount...\n"+
                        "Deposit Amount is : "+ String.valueOf(CommonUtil.convertObjToDouble(txtTransactionAmt.getText()))+"\n"+
                        "Account balance is : "+ String.valueOf(availablebal));
                        txtDebitAccNo.setText("");
                        return;
                    }
                   
                    if ((availablebal!=0.0)  ||  (CommonUtil.convertObjToDouble(callingDepositeAmount) != null)  && depositAmt != (CommonUtil.convertObjToDouble(callingDepositeAmount))) {
                        if (!type.equals("DAILY") ){
                            
                            ClientUtil.showAlertWindow("Amount Exceeds the new Deposit Amount...\n"
                                    + "Deposit Amount is : " + String.valueOf(CommonUtil.convertObjToDouble(callingDepositeAmount)) + "\n"
                                    + "Remainig Amount is : " + String.valueOf(availablebal)  + "\n"
                            + "And  Account balance should be zero" );
                            txtDebitAccNo.setText("");
                            return;
                        }
                    }
                   
                    
                    accountNum=CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                    txtApplicantsName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERNAME")));
                }else{
                    ////Can't add inter-branch operative accounts in mds receipt entry  prodType.equals("OA")
                    //changed by Nidhin 27/03/2014
                    if((getSourceScreen().equals("MDS_RECEIPT")|| getSourceScreen().equals("GDS_RECEIPT"))&& ( prodType.equals("TL") || prodType.equals("AD")|| prodType.equals("TD"))) {
                        String initatedBranch=CommonUtil.convertObjToStr(ProxyParameters.BRANCH_ID);
                        //System.out.println("initatedBranch = "+initatedBranch +" BBB==="+hash.get("BRANCH_CODE"));
                        //System.out.println("branch@@@@@@@@@@2"+hash);
                        hash.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                        if(!CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")).equals(initatedBranch)){
                            ClientUtil.displayAlert("Other Branch Account Not able to select for MDS Receipt");
                            txtDebitAccNo.setText("");
                            return ;
                        }
                    }
                    accountNum=CommonUtil.convertObjToStr(hash.get("ACT_NUM"));
                    txtApplicantsName.setText(CommonUtil.convertObjToStr(hash.get("CUSTNAME")));
                }
                if(CommonUtil.convertObjToStr(accountNum).length()>0 && !prodType.equals("INV") && !prodType.equals("BRW"))
                    observable.checkAcNoWithoutProdType(accountNum);
                txtDebitAccNo.setText(accountNum);
                //System.out.println("@@@@@@@lonaccountnum"+accountNum);
                txtApplicantsName.setEditable(false);
                observable.setTxtApplicantsName(txtApplicantsName.getText());
                observable.setTxtDebitAccNo(accountNum);
                //Added By Suresh
                if(!prodType.equals("INV") && !prodType.equals("BRW"))
                transDetails.setTransDetails(
                CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected()),
                ProxyParameters.BRANCH_ID, accountNum);
                if(!prodType.equals("RM"))
                    observable.setLblCustomerNameVal(null,CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected()));
                
                lblCustomerNameValue.setText(observable.getLblCustomerNameVal());
                String cboselected=CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected()));
                if (cboselected.equals("TL"))
                    txtTransactionAmt.setText(getCallingAmount());
                    //txtTransactionAmt.setText(transDetails.getLimitAmount());
                observable.setLimitAmount(transDetails.getLimitAmount());
                //System.out.println(transDetails.getLimitAmount()+"@@@@important"+CommonUtil.convertObjToStr(hash.get("LIMIT")));
                if(cboselected.equals("OA")){
                    observable.setLasttxtAmount(txtTransactionAmt.getText());
                    observable.checkOA(accountNum);
                }
               String act_num= txtDebitAccNo.getText();
               HashMap lockmap=new HashMap();
                lockmap.put("ACCOUNTNO",act_num);
                List lockList=ClientUtil.executeQuery("getLockStatusForAccounts", lockmap);
                lockmap=null;
                if(lockList!=null && lockList.size()>0){
                lockmap=(HashMap)lockList.get(0);
                String lockStatus=CommonUtil.convertObjToStr(lockmap.get("LOCK_STATUS"));
                if(lockStatus.equals("Y")){
                    ClientUtil.displayAlert("Account is locked");
                   txtDebitAccNo.setText("");
                }
                }
                transactionType = CommonUtil.convertObjToStr(observable.getCbmTransactionType().getKeyForSelected());
                if (!prodType.equals("GL")) {
                    //System.out.println("transactionType : " + transactionType);
                    if (transactionType.equals("TRANSFER")) {
                        observable.checkAcNoWithoutProdType(observable.getTxtDebitAccNo());
                        observable.setSelectedTxnBranchId(observable.getSelectedBranchID());
                        observable.setSelectedTxnType(transactionType);
                    } else {
                        observable.setSelectedTxnBranchId(ProxyParameters.BRANCH_ID);
                        observable.setSelectedTxnType(transactionType);
                    }
                }
                //Added by sreekrishnan for interbranch day-end checking
                if(!txtDebitAccNo.getText().equals("") && txtDebitAccNo.getText().length() >0 && 
            	(getSourceScreen().equals("DRF TRANSACTION") || getSourceScreen().equals("DRF RECOVERY") || 
            	getSourceScreen().equals("SHARE_SCREEN") || getSourceScreen().equals("SHARE_DIVIDEND_PAYMENT") ||
            	getSourceScreen().equals("MDS_APPLICATION") || /*getSourceScreen().equals("MDS_PRIZED_PAYMENT") || */
            	getSourceScreen().equals("MDS_MEMBER_RECEIPT") || 
            	/*getSourceScreen().equals("LOAN_ACT_CLOSING") ||*/ getSourceScreen().equals("ACT_CLOSING") ||
            	/*getSourceScreen().equals("TERM_DEPOSIT") || getSourceScreen().equals("MULTIPLE_TERM_DEPOSIT") ||*/
            	getSourceScreen().equals("RENTTRANS") || getSourceScreen().equals("RENT_REGISTER"))){
                if(observable.getSelectedBranchID()!=null && !ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())){
                    Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(observable.getSelectedBranchID());
                    Date currentDate = (Date) currDt.clone();
                    //System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                    if(selectedBranchDt == null){
                        ClientUtil.showAlertWindow("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed");
                        txtDebitAccNo.setText("");
                        return;
                    }else if(DateUtil.dateDiff(currentDate, selectedBranchDt)!=0){
                        ClientUtil.showAlertWindow("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed");
                        txtDebitAccNo.setText("");
                        return;
                    }else {
                        System.out.println("Continue for interbranch trasactions ...");
                    }
                }
                }
                // KD 196
                if (!txtDebitAccNo.getText().equals("") && txtDebitAccNo.getText().length() > 0
                        && (getSourceScreen().equals("LOAN_ACT_CLOSING") || getSourceScreen().equals("MDS_PRIZED_PAYMENT"))) {
                    if (observable.getSelectedBranchID() != null && !ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())) {
                        Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(observable.getSelectedBranchID());
                        Date currentDate = (Date) currDt.clone();    
                        System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                        if (selectedBranchDt == null) {
                            ClientUtil.showAlertWindow("BOD is not completed for the selected branch " + "\n" + "Interbranch Transaction Not allowed");
                            txtDebitAccNo.setText("");
                            return;
                        } else if (DateUtil.dateDiff(currentDate, selectedBranchDt) < 0) {
                            ClientUtil.showAlertWindow("Application Date is different in the Selected branch " + "\n" + "Interbranch Transaction Not allowed");
                            txtDebitAccNo.setText("");
                            return;
                        } else {
                            System.out.println("Continue for interbranch trasactions ...");
                        }
                    }
                }
                
                if ((getSourceScreen().equals("MDS_RECEIPT") || getSourceScreen().equals("GDS_RECEIPT") || getSourceScreen().equals("MDS_APPLICATION") || getSourceScreen().equals("TERM_DEPOSIT")) && prodType.equals("AD")) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                    whereMap.put("ACT_NUM", txtDebitAccNo.getText());
                    List list = (List) ClientUtil.executeQuery("getBalanceAD", whereMap);
                    if (list != null && list.size() > 0) {
                        HashMap resultMap = (HashMap) list.get(0);

                        double availableBalance = CommonUtil.convertObjToDouble(resultMap.get("AV_BALANCE")).doubleValue();
                        double limitAmt = availableBalance;
                        //System.out.println("callingDepositeAmount XXXXXXXX :" + txtTransactionAmt.getText() + "limitAmt :" + limitAmt);
                        if ((CommonUtil.convertObjToDouble(txtTransactionAmt.getText()) != null) && limitAmt < (CommonUtil.convertObjToDouble(txtTransactionAmt.getText()))) {
                            //System.out.println("limitAmt : amount : " + limitAmt + ":" + limitAmt);
                            ClientUtil.showMessageWindow("Transaction Amount Exceeds the Limit Amount");
                            txtDebitAccNo.setText("");
                            return;

                        }
                    }
                }
                if (getSourceScreen().equals("SHARE_SCREEN") && prodType.equals("SH")){
                    txtDebitAccNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));     
                    lblCustomerNameValue.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    txtApplicantsName.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER")));
                    txtApplicantsName.setEditable(false);
                    observable.setTxtApplicantsName(txtApplicantsName.getText());
                    observable.setTxtDebitAccNo(accountNum);
                }
            }else if ( viewType==EDIT ) {
                btnNewTxDetails.setEnabled(true);
                setTableIssueEnableDisable(true);
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                observable.setStatus("EDIT");
            } else if ( viewType==DELETE ) {
                observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
                setTableIssueEnableDisable(false);
                observable.setStatus("DELETE");
            }
             }     
    }
    
    private void setTableIssueEnableDisable(boolean flag){
        tblTransDetails.setEnabled(flag);
    }
    
    private void initComponentData() {
        cboProductType.setModel(observable.getCbmProductType());
        cboTransType.setModel(observable.getCbmTransactionType());
        cboInstrumentType.setModel(observable.getCbmInstrumentType());
        tblTransDetails.setModel(observable.getTblTransDetails());
        txtApplicantsName.setAllowAll(true);
    }
    
    private void setObservable() {
        observable = new TransactionOB();
        observable.addObserver(this);
    }
    
    public TransactionOB getTransactionOB(){
        return observable ;
    }
    private void initStartup() {
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMaximumLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panTransDetails);
        cancelAction(false);
        setMainEnableDisable(false);
        setMandatoryHashMap();
        setHelpMessage();
        setDebitAccHDButtonEnableDisable(false);
        txtTransProductId.setEditable(false);
        txtDebitAccNo.setEditable(true);
        lblTokenNo.setVisible(false);
        lblTokenNo.setEnabled(false);
        txtTokenNo.setVisible(false);
        txtTokenNo.setEnabled(false);
        txtTokenNo.setEditable(false);
        
    }
    
    //Added By Suresh
    public void setProdType(){
        try{
            //System.out.println("bkjbbj>>>>"+getSourceScreen());
            if(getSourceScreen().equals("BORROW_REPAYMENT") || getSourceScreen().equals("BORROW_DISBURSE") || getSourceScreen().equals("TERM_DEPOSIT") || getSourceScreen().equals("MULTIPLE_TERM_DEPOSIT") || getSourceScreen().equals("NEW_BORROW")){
                observable.addInvestmentProduct();
                cboProductType.setModel(observable.getCbmProductType());
            }
            if(getSourceScreen().equals("INVESTMENT_TRANS") || getSourceScreen().equals("INVESTMENT_CHARGE") || getSourceScreen().equals("INVESTMENT")){
                observable.addBorrowingProduct();
                cboProductType.setModel(observable.getCbmProductType());
            }
            if(getSourceScreen().equals("SHARE_DIVIDEND_PAYMENT")){
                //observable.addOparativeProduct();
//                observable.removeTermLoanForDividend();
                //After discussion with Soji removed Term loan option from share divedend payment screen by Jeffin John
                observable.getCbmProductType().removeKeyAndElement("TL");
                cboProductType.setModel(observable.getCbmProductType());
            }
            if(getSourceScreen().equals("RTGS_REMITTANCE")){//Added By Suresh R   17-Nov-2017
                observable.addRTGSNEFTProduct();
                cboProductType.setModel(observable.getCbmProductType());
            }            
            if(getSourceScreen().equals("TERM_DEPOSIT")){ // Added by nithya on 20-11-2020 for KD-2434
              observable.getCbmProductType().removeKeyAndElement("TD");
              cboProductType.setModel(observable.getCbmProductType());  
            }
            
            if(getSourceScreen().equals("LOCKER_ISSUE")){
                observable.getCbmProductType().removeKeyAndElement("TD");
                cboProductType.setModel(observable.getCbmProductType());
            }
            
            
        }catch(Exception e){
            
        }
    }
    
    public void setResetProdType(){
        try{
            observable.fillDropdown();
            cboProductType.setModel(observable.getCbmProductType());
            cboTransType.setModel(observable.getCbmTransactionType());
            cboInstrumentType.setModel(observable.getCbmInstrumentType());
        }catch(Exception e){
            
        }
    }
    
    public String getAccount_AvailBalance(){
        if(CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected()).equals("AD")){
            if( getTransDetails().getAvBalance().length()>0){
                return getTransDetails().getAvBalance();
            }else{
                return "0.00";
            }
        } else if( getTransDetails().getAvailableBalance().length()>0)
            return getTransDetails().getAvailableBalance();
        else
            return "0.00";
    }
    
    public void resetObjects(){
        callingUiMode = -1 ;
        observable.resetObjects();
        tblTransDetails.setModel(observable.getTblTransDetails());
        transDetails.setTransDetails(null,null,null);
        commonReset();
        setCallingTransType("");
        setCallingTransProdType("");
        setCallingTransAcctNo("");
        setCallingProdID("");        
        update(null, null);
    }
    
    
    public void cancelAction(boolean flag){
        ClientUtil.enableDisable(this, flag);
        ClientUtil.enableDisable(panDebitAccHead, flag);
        observable.resetForm();
        //System.out.println("In cancelAction()  flag = "+flag);
        btnNewTxDetails.setEnabled(flag);
        btnDebitAccNo.setEnabled(false);
        btnTransProductId.setEnabled(false);
        transDetails.setTransDetails(null,null,null);
        update(null, null);
    }
    
    public void okAction(boolean flag){
        ClientUtil.enableDisable(this, flag);
        ClientUtil.enableDisable(panDebitAccHead, flag);
        observable.resetForm();
        //System.out.println("In cancelAction()  flag = "+flag);
        btnNewTxDetails.setEnabled(flag);
        btnDebitAccNo.setEnabled(true);
        //        btnTransProductId.setEnabled(false);
        btnSaveTxDetails.setEnabled(false);
        btnDeleteTxDetails.setEnabled(false);
        transDetails.setTransDetails(null,null,null);
        update(null, null);
    }
    
    public void setButtonEnableDisable(boolean flag) {
        btnNewTxDetails.setEnabled(flag);
        btnSaveTxDetails.setEnabled(!flag);
        btnDeleteTxDetails.setEnabled(!flag);
    }
    
    
    
    public void setMainEnableDisable(boolean flag) {
        btnNewTxDetails.setEnabled(flag);
        btnSaveTxDetails.setEnabled(flag);
        btnDeleteTxDetails.setEnabled(flag);
    }
    
    private void setMaximumLength() {
        txtApplicantsName.setMaxLength(300);
        txtTransactionAmt.setValidation(new CurrencyValidation(14,2));
        txtChequeNo.setMaxLength(10);
        txtChequeNo2.setValidation(new NumericValidation(10,0));
        txtChequeNo.setAllowAll(true);
        txtParticulars.setMaxLength(50);
        txtParticulars.setAllowAll(true);//Added By Sathiya 11-03-2014
    }
    
    
   /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblTransactionAmt.setText(resourceBundle.getString("lblTransactionAmt"));
        btnNewTxDetails.setText(resourceBundle.getString("btnNewTxDetails"));
        btnDeleteTxDetails.setText(resourceBundle.getString("btnDeleteTxDetails"));
        lblDebitAccNo.setText(resourceBundle.getString("lblDebitAccNo"));
        lblApplicantsName.setText(resourceBundle.getString("lblApplicantsName"));
        ((javax.swing.border.TitledBorder)panTransDetails.getBorder()).setTitle(resourceBundle.getString("panTransDetails"));
        lblTransProductId.setText(resourceBundle.getString("lblTransProductId"));
        btnSaveTxDetails.setText(resourceBundle.getString("btnSaveTxDetails"));
        lblTransType.setText(resourceBundle.getString("lblTransType"));
        lblTotalTransactionAmt.setText(resourceBundle.getString("lblTotalTransactionAmt"));
        btnTransProductId.setText(resourceBundle.getString("btnTransProductId"));
        lblChequeDate.setText(resourceBundle.getString("lblChequeDate"));
        lblChequeNo.setText(resourceBundle.getString("lblChequeNo"));
        lblInstrumentType.setText(resourceBundle.getString("lblInstrumentType"));
        //        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        try {
            txtApplicantsName.setText(observable.getTxtApplicantsName());
            cboTransType.setSelectedItem(observable.getCboTransType());
            cboInstrumentType.setSelectedItem(observable.getCbmInstrumentType().getDataForKey(observable.getCboInstrumentType()));
            txtTransactionAmt.setText(observable.getTxtTransactionAmt());
            
            txtTransProductId.setText(observable.getTxtTransProductId());
            //            cboProductType.setSelectedItem(observable.getCboProductType());
            txtDebitAccNo.setText(observable.getTxtDebitAccNo());
            txtChequeNo.setText(observable.getTxtChequeNo());
            txtChequeNo2.setText(observable.getTxtChequeNo2());
            tdtChequeDate.setDateValue(observable.getTdtChequeDate());
            //System.out.println("");
            lblTotalTransactionAmtVal.setText(observable.getLblTotalTransactionAmtVal());
            lblCustomerNameValue.setText(observable.getLblCustomerNameVal());
            txtTokenNo.setText(observable.getTokenNo());
            String transactionType = CommonUtil.convertObjToStr(observable.getCbmTransactionType().getKeyForSelected());
            String prodType = CommonUtil.convertObjToStr(observable.getCbmProductType().getKeyForSelected());
            txtParticulars.setText(observable.getParticulars());            
            if (transactionType.equals("TRANSFER") && prodType.length() > 0 && !prodType.equals("RM") && !prodType.equals("INV") && !prodType.equals("BRW")) {
                transDetails.setTransDetails(prodType, ProxyParameters.BRANCH_ID, observable.getTxtDebitAccNo());
            } else {
                transDetails.setTransDetails(null,
                null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtApplicantsName(txtApplicantsName.getText());
        observable.setCboTransType((String) cboTransType.getSelectedItem());
        observable.setCboInstrumentType(CommonUtil.convertObjToStr(((ComboBoxModel) cboInstrumentType.getModel()).getKeyForSelected()));
        observable.setTxtTransactionAmt(txtTransactionAmt.getText());
        observable.setTxtTransProductId(txtTransProductId.getText());
        observable.setCboProductType((String) cboProductType.getSelectedItem());
        observable.setTxtDebitAccNo(txtDebitAccNo.getText());
        observable.setTxtChequeNo(txtChequeNo.getText());
        observable.setTxtChequeNo2(txtChequeNo2.getText());
        observable.setTdtChequeDate(tdtChequeDate.getDateValue());
        observable.setLblTotalTransactionAmtVal(lblTotalTransactionAmtVal.getText());
        observable.setLblCustomerNameVal(lblCustomerNameValue.getText(), null);
        observable.setTokenNo(txtTokenNo.getText());
        observable.setParticulars(txtParticulars.getText());        
        //System.out.println("observable.toString : " + observable.toString());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtApplicantsName", new Boolean(true));
        mandatoryMap.put("cboTransType", new Boolean(true));
        mandatoryMap.put("txtTransactionAmt", new Boolean(true));
        mandatoryMap.put("txtTransProductId", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtDebitAccNo", new Boolean(true));
        mandatoryMap.put("txtChequeNo", new Boolean(true));
        mandatoryMap.put("txtChequeNo2", new Boolean(true));
        mandatoryMap.put("txtTotalTransfer", new Boolean(true));
        mandatoryMap.put("txtTotalCash", new Boolean(true));
        mandatoryMap.put("tdtChequeDate", new Boolean(true));
        mandatoryMap.put("txtTotalTransactionAmt", new Boolean(true));
        if (TrueTransactMain.TOKEN_NO_REQ.equals("Y")) {
        	mandatoryMap.put("txtTokenNo", new Boolean(true));
        }
        mandatoryMap.put("txtParticulars", new Boolean(false));
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
        TransactionMRB objMandatoryRB = new TransactionMRB();
        txtApplicantsName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtApplicantsName"));
        cboTransType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransType"));
        cboInstrumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentType"));
        txtTransactionAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransactionAmt"));
        txtTransProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransProductId"));
        cboProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
        txtDebitAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDebitAccNo"));
        txtChequeNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChequeNo"));
        txtChequeNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChequeNo2"));
        tdtChequeDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtChequeDate")); 
        if (TrueTransactMain.TOKEN_NO_REQ.equals("Y")) {
        	txtTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenNo"));
        }
    }
    
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnDebitAccNo.setName("btnDebitAccNo");
        btnDeleteTxDetails.setName("btnDeleteTxDetails");
        btnNewTxDetails.setName("btnNewTxDetails");
        btnSaveTxDetails.setName("btnSaveTxDetails");
        btnTransProductId.setName("btnTransProductId");
        cboProductType.setName("cboProductType");
        cboTransType.setName("cboTransType");
        cboInstrumentType.setName("cboInstrumentType");
        lblApplicantsName.setName("lblApplicantsName");
        lblChequeDate.setName("lblChequeDate");
        lblChequeNo.setName("lblChequeNo");
        //        lblCustomerName.setName("lblCustomerName");
        lblInstrumentType.setName("lblInstrumentType");
        lblDebitAccNo.setName("lblDebitAccNo");
        lblProductType.setName("lblProductType");
        lblTotalTransactionAmt.setName("lblTotalTransactionAmt");
        lblTransactionAmt.setName("lblTransactionAmt");
        lblTransProductId.setName("lblTransProductId");
        lblTransType.setName("lblTransType");
        panDebitAccHead.setName("panDebitAccHead");
        panDebitAccNo.setName("panDebitAccNo");
        panLeftSubTxDetails.setName("panLeftSubTxDetails");
        panRightSubTxDetails.setName("panRightSubTxDetails");
        panTable.setName("panTable");
        panTotalTransactiosn.setName("panTotalTransactiosn");
        panTransDetails.setName("panTransDetails");
        panTransaction.setName("panTransaction");
        panbtnTxDetails.setName("panbtnTxDetails");
        sptTransDetails.setName("sptTransDetails");
        srpTransDetails.setName("srpTransDetails");
        tblTransDetails.setName("tblTransDetails");
        tdtChequeDate.setName("tdtChequeDate");
        txtApplicantsName.setName("txtApplicantsName");
        txtTokenNo.setName("txtTokenNo");
        txtChequeNo.setName("txtChequeNo");
        txtChequeNo2.setName("txtChequeNo2");
        txtDebitAccNo.setName("txtDebitAccNo");
        lblTotalTransactionAmtVal.setName("txtTotalTransactionAmt");
        txtTransProductId.setName("txtTransProductId");
        txtTransactionAmt.setName("txtTransactionAmt");
        txtParticulars.setName("txtParticulars"); 
        lblParticulars.setName("lblParticulars");
    }
    
    /* To display an alert message if any of the mandatory fields is not inputed */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void setNewSaveDeleteTransEnable() {
        btnNewTxDetails.setEnabled(true);
        btnSaveTxDetails.setEnabled(true);
        btnDeleteTxDetails.setEnabled(true);
    }
    
    private void setSelectRow(int SelectedRow){
        selectedRowValue = SelectedRow;
    }
    
    private int  getSelectRow(){
        return selectedRowValue;
    }
    
    
    private void setTableTransDetailsEnableDisable(boolean flag) {
        tblTransDetails.setEnabled(flag);
    }
    
    public void setPanTransactionDetailsEnableDisable(boolean flag){
        //System.out.println("flag : " + flag);
        //System.out.println("panLeftSubTxDetails ---- " + panLeftSubTxDetails.isEnabled());
        ClientUtil.enableDisable(panLeftSubTxDetails,flag);
        ClientUtil.enableDisable(panRightSubTxDetails,flag);
    }
    
    private void transferValidation() {
        TransactionMRB objMRB = new TransactionMRB();
        StringBuffer strB = new StringBuffer();
        if (!(txtChequeNo.getText().length() > 0) || !(tdtChequeDate.getDateValue().length() > 0) || !(txtTransProductId.getText().length() > 0) || !(txtDebitAccNo.getText().length() > 0)) {
            if(!(txtChequeNo.getText().length() > 0)) {
                strB.append(objMRB.getString("txtChequeNo"));
                strB.append(objMRB.getString("txtChequeNo2"));
                strB.append("\n");
            }
            if (!(tdtChequeDate.getDateValue().length() > 0)) {
                strB.append(objMRB.getString("tdtChequeDate"));
                strB.append("\n");
            }
            if(!(txtDebitAccNo.getText().length() > 0)) {
                strB.append(objMRB.getString("txtDebitAccNo"));
                strB.append("\n");
            }
            if(!(txtTransProductId.getText().length() > 0)) {
                strB.append(objMRB.getString("txtTransProductId"));
                strB.append("\n");
            }
            displayAlert(CommonUtil.convertObjToStr(strB));
        }
        strB = null;
        objMRB = null;
    }
    
    private void resetTotalTransactionAmt() {
        observable.setLblTotalTransactionAmtVal("");
        lblTotalTransactionAmtVal.setText(observable.getLblTotalTransactionAmtVal());
    }
    
    private void setSaveDeleteTransDisable() {
        btnNewTxDetails.setEnabled(true);
        btnSaveTxDetails.setEnabled(false);
        btnDeleteTxDetails.setEnabled(false);
    }
    
    private void populateInstrumentType() {
        ComboBoxModel objModel = new ComboBoxModel();
        objModel.addKeyAndElement("", "");
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected());
        if (!prodType.equals("")) {
            if (prodType.equals("OA") || prodType.equals("AD")) {
                objModel.addKeyAndElement("WITHDRAW_SLIP", observable.getCbmInstrumentType().getDataForKey("WITHDRAW_SLIP"));
                objModel.addKeyAndElement("CHEQUE", observable.getCbmInstrumentType().getDataForKey("CHEQUE"));
            }
            if(getSourceScreen().equals("ACT_CLOSING")&&prodType.equals("AB")){
                objModel.addKeyAndElement("CHEQUE", observable.getCbmInstrumentType().getDataForKey("CHEQUE"));
            }
            if ( getSourceScreen().equals("LOAN_ACT_CLOSING") && (prodType.equals("TL")
            ||prodType.equals("ATL")|| prodType.equals("TD"))){
                if(prodType.equals("TD"))
                    ClientUtil.showMessageWindow("Deposit Debit is Not Possible");
                else
                    ClientUtil.showMessageWindow("Loan Debit is Not Possible");
                cboProductType.setSelectedIndex(0);
                return;
            }
            if(prodType.equals("GL")){
                if((getCallingUiMode() != ClientConstants.ACTIONTYPE_AUTHORIZE) &&
                (getCallingUiMode() != ClientConstants.ACTIONTYPE_REJECT) && (getCallingUiMode() != ClientConstants.ACTIONTYPE_EDIT)
                && (getCallingUiMode() != ClientConstants.ACTIONTYPE_DELETE)){
                    btnTransProductId.setEnabled(false);
                    txtTransProductId.setEnabled(false);
                    lblDebitAccNo.setText("Account Head");
                   // lblCustomerName.setText("A/C Head Desc");
                    txtTransProductId.setText("");
                }
            }else{
                if((getCallingUiMode() != ClientConstants.ACTIONTYPE_AUTHORIZE) &&
                (getCallingUiMode() != ClientConstants.ACTIONTYPE_REJECT) && (getCallingUiMode() != ClientConstants.ACTIONTYPE_EDIT)
                && (getCallingUiMode() != ClientConstants.ACTIONTYPE_DELETE)){
                    btnTransProductId.setEnabled(true);
                    lblDebitAccNo.setText(resourceBundle.getString("lblDebitAccNo"));
                    //lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
                }
            }
            if(((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) && prodType.equals("RM")){
                objModel.addKeyAndElement("PO", observable.getCbmInstrumentType().getDataForKey("PO"));
                objModel.addKeyAndElement("DD", observable.getCbmInstrumentType().getDataForKey("DD"));
            }
            objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
            cboInstrumentType.setModel(objModel);
            cboInstrumentType.setSelectedItem(((ComboBoxModel)cboInstrumentType.getModel()).getDataForKey(observable.getCboInstrumentType()));
        }
    }
    
    private void populateInstrumentTypeCash(){
        //System.out.println("$$$$$$$$$$$observable.getCboInstrumentType() : " + observable.getCboInstrumentType());
        ComboBoxModel objModel = new ComboBoxModel();
        objModel.addKeyAndElement("", "");
        objModel.addKeyAndElement("WITHDRAW_SLIP", observable.getCbmInstrumentType().getDataForKey("WITHDRAW_SLIP"));
        objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
        cboInstrumentType.setModel(objModel);
        cboInstrumentType.setSelectedItem(((ComboBoxModel)cboInstrumentType.getModel()).getDataForKey(observable.getCboInstrumentType()));
    }
    
    private void instrumentTypeFocus() {
        //System.out.println("observable.getActionType() : " + observable.getActionType());
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && cboInstrumentType.getSelectedIndex() > 0) {
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected());
            String instrumentType = CommonUtil.convertObjToStr(((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected());
            if (instrumentType.equals("VOUCHER") || instrumentType.equals("WITHDRAW_SLIP")) {
                txtChequeNo.setText("");
                txtChequeNo2.setText("");
                tdtChequeDate.setDateValue(DateUtil.getStringDate(currDt));
                tdtChequeDate.setEnabled(false);
                ClientUtil.enableDisable(cPanel1,false);
            } else {
                tdtChequeDate.setDateValue(DateUtil.getStringDate(currDt));
                tdtChequeDate.setEnabled(false);
                ClientUtil.enableDisable(cPanel1,true);
            }
            if((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))){
                if(cboTransType.getSelectedItem().equals("Cash")){
                    txtChequeNo.setEnabled(true);
                    txtChequeNo2.setEnabled(true);
                }else{
                    if(instrumentType.equals("PO")){
                        ClientUtil.enableDisable(cPanel1,true);
                        //                    txtChequeNo2.setText("");
                        txtChequeNo.setText("PO");
                        txtChequeNo.setEnabled(false);
                        txtChequeNo2.setEnabled(true);
                    }else if(instrumentType.equals("DD")){
                        ClientUtil.enableDisable(cPanel1,true);
                        //                    txtChequeNo2.setText("");
                        txtChequeNo.setText("DD");
                        txtChequeNo.setEnabled(false);
                        txtChequeNo2.setEnabled(true);
                    }
                }
            }
        }else{
            txtChequeNo.setText("");
            txtChequeNo2.setText("");
        }
    }
    
    private void popUp(int field) {
        if (field == PAYEE || field == DEBIT) {
            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getActionType()]);
        } else {
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE){//Edit=0 and Delete=1
            viewMap.put(CommonConstants.MAP_NAME, "viewRemitIssue");
        }else if(field==TRANS_PROD){
            HashMap where_map = new HashMap();
            where_map.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
            
            if(getSourceScreen().equals("DEPOSITS") && prodType.equals("RM")){
                
            }else if(getSourceScreen().equals("SHARE_SCREEN") && prodType.equals("SH")){
                viewMap.put(CommonConstants.MAP_NAME, "getSelectShareProductMap");
            }else
                viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + observable.getCbmProductType().getKeyForSelected().toString());
            if( getSourceScreen().equals("BORROW_REPAYMENT") || getSourceScreen().equals("BORROW_DISBURSE")){
                where_map.put("OTHER_BANK_SB/CA", "OTHER_BANK_SB/CA");
            }
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
            //            viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList" + observable.getCbmProductType().getKeyForSelected().toString());
        }else if(field==ACC_NUM){
            HashMap where_map = new HashMap();
            
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
            if(getSourceScreen().equals("DEPOSITS") && prodType.equals("TD")){
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + observable.getCbmProductType().getKeyForSelected().toString());
                where_map.put(PROD_ID, txtTransProductId.getText());
                where_map.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
                prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
            }else if(getSourceScreen().equals("SHARE_SCREEN") && prodType.equals("SH")){
                where_map.put("SHARE_TYPE",txtTransProductId.getText());
                viewMap.put(CommonConstants.MAP_NAME, "getMemeberShipDetailsWhileNotMember");
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Remittance.getAccountData" + observable.getCbmProductType().getKeyForSelected().toString());
                //                HashMap where_map = new HashMap();
                where_map.put(CommonConstants.PRODUCT_ID, txtTransProductId.getText());
                where_map.put(PROD_ID, txtTransProductId.getText());
                where_map.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
                prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
            }
                       
//            if(!prodType.equals("TL"))
//                where_map.put(CommonConstants.ACT_NUM, getSourceAccountNumber());// From calling UI//commented by rishad 28/07/16(no restriction based on customer any one map )
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "OperativeAcctProduct.getSelectAcctHeadTOList");
        }
        new ViewAll(this, "Transaction", viewMap).show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame f = new javax.swing.JFrame();
        TransactionUI ch = new TransactionUI();
        f.getContentPane().add(ch);
        ch.show();
        ch.setSize(900, 600);
        f.show();
        f.setSize(900, 600);
    }
    
    /**
     * Getter for property lblMsg.
     * @return Value of property lblMsg.
     */
    public javax.swing.JLabel  getLblMsg() {
        return lblMsg;
    }
    
    /**
     * Setter for property lblMsg.
     * @param lblMsg New value of property lblMsg.
     */
    public void setLblMsg(javax.swing.JLabel lblMsg) {
        this.lblMsg = lblMsg;
    }
    
    private void setDebitAccHDButtonEnableDisable(boolean flag) {
        btnTransProductId.setEnabled(flag);
        btnDebitAccNo.setEnabled(flag);
        txtTransProductId.setEnabled(flag);
        txtDebitAccNo.setEnabled(flag);
        cboProductType.setEnabled(flag);
        if(!flag){
            txtApplicantsName.setEnabled(true);
            txtApplicantsName.setEditable(true);
        }else{
            txtApplicantsName.setEnabled(false);
            txtApplicantsName.setEditable(false);
        }
    }
    
    private void whenCashIsSelectedDisable(boolean flag) {
        //        txtChequeNo.setEditable(flag);
        //        txtChequeNo2.setEditable(flag);
        //        tdtChequeDate.setEnabled(flag);
        //               cboProductType.setEnabled(flag);
        //              cboInstrumentType.setEnabled(flag);
    }
    
    
    
    private String validateCashSelection(){
        MandatoryCheck objMandatory = new MandatoryCheck();
        TransactionHashMap objMap = new TransactionHashMap();
        HashMap mandatoryMap = objMap.getMandatoryHashMap();
        String prodType=((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
        String transType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
        //System.out.println("transType : " + transType + " ......... getTransactionMode = " + getTransactionMode());
        if (getTransactionMode().equals(CommonConstants.CREDIT)) {
            mandatoryMap.put("cboInstrumentType", new Boolean(false));
            mandatoryMap.put("tdtChequeDate", new Boolean(false));
            mandatoryMap.put("txtChequeNo", new Boolean(false));
            mandatoryMap.put("txtChequeNo2", new Boolean(false));
        }else if (getTransactionMode().equals(CommonConstants.DEBIT) && transType.equals("CASH")) {
            mandatoryMap.put("cboInstrumentType", new Boolean(false));
            mandatoryMap.put("tdtChequeDate", new Boolean(false));
            mandatoryMap.put("txtChequeNo", new Boolean(false));
            mandatoryMap.put("txtChequeNo2", new Boolean(false));
            if(TrueTransactMain.TOKEN_NO_REQ.equals("Y") && (getSourceScreen().equals("DEPOSITS") || getSourceScreen().equals("ACT_CLOSING")  || advanceCreditIntAmt.doubleValue()>0|| getSourceScreen().equals("REMITPAYMENT")))
                mandatoryMap.put("txtTokenNo",new Boolean(true));
        }else{
            mandatoryMap.put("cboInstrumentType", new Boolean(true));
            mandatoryMap.put("tdtChequeDate", new Boolean(true));
            mandatoryMap.put("txtTransProductId", new Boolean(true));
            mandatoryMap.put("cboProductType", new Boolean(true));
            mandatoryMap.put("txtDebitAccNo", new Boolean(true));
            if(prodType.equals("GL"))
                mandatoryMap.put("txtTransProductId", new Boolean(false));
            if(((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) && prodType.equals("RM")){
                mandatoryMap.put("cboInstrumentType", new Boolean(true));
                mandatoryMap.put("txtChequeNo", new Boolean(true));
                mandatoryMap.put("txtChequeNo2", new Boolean(true));
            }
            
        }
        if(getSourceScreen() == "REMITPAYMENT"){
            mandatoryMap.put("cboInstrumentType", new Boolean(false));
            mandatoryMap.put("tdtChequeDate", new Boolean(false));
        }
        if (getSourceScreen().equals("DEPOSITS") && prodType.equals("RM")){
            mandatoryMap.remove("txtTransProductId");
            mandatoryMap.put("cboInstrumentType", new Boolean(true));
            mandatoryMap.put("txtChequeNo", new Boolean(true));
            mandatoryMap.put("txtChequeNo2", new Boolean(true));
        }else if(getSourceScreen().equals("DEPOSITS") || observable.isDepFlag() && !prodType.equals("RM")){
            mandatoryMap.remove("cboInstrumentType");
            mandatoryMap.remove("tdtChequeDate");
            mandatoryMap.remove("txtTransProductId");
        }
        if ((getSourceScreen().equals("ACT_CLOSING")) && (prodType.equals("RM"))){
            mandatoryMap.remove("cboInstrumentType");
            mandatoryMap.remove("tdtChequeDate");
            mandatoryMap.remove("txtTransProductId");
        }
        if(transType.equals("TRANSFER")) {
            mandatoryMap.put("cboInstrumentType", new Boolean(true));
            mandatoryMap.put("tdtChequeDate", new Boolean(true));
            mandatoryMap.put("txtTransProductId", new Boolean(true));
            mandatoryMap.put("cboProductType", new Boolean(true));
            mandatoryMap.put("txtDebitAccNo", new Boolean(true));
        }    
        String mandatoryMessage = objMandatory.checkMandatory(getClass().getName(),panTransaction, mandatoryMap);
        //System.out.println("mandatoryMessage : " + mandatoryMessage);
        return mandatoryMessage;
    }
    
    public void removeGoldAuctionProdType(boolean auctionType){
        if(auctionType){
         observable.getCbmProductType().removeKeyAndElement("AB");
         observable.getCbmProductType().removeKeyAndElement("AD");
         observable.getCbmProductType().removeKeyAndElement("OA");
         observable.getCbmProductType().removeKeyAndElement("SA");
         observable.getCbmProductType().removeKeyAndElement("TD");
         observable.getCbmProductType().removeKeyAndElement("TL");
        }
        else{
           setResetProdType(); 
        }
    } 
    private void transactionTypeActionPerformed(String transType) {
        if (transType.equals("CASH")) {
            //            observable.whenCashIsSelectedClear();
            ClientUtil.enableDisable(panRightSubTxDetails, false);
            
            setDebitAccHDButtonEnableDisable(false);
            //            whenCashIsSelectedDisable(false);
            cboInstrumentType.setEnabled(true);
            txtDebitAccNo.setText("");
            cboInstrumentType.setEnabled(false);
            //            cboProductType.setSelectedItem("");
            //            observable.setCboProductType("");
            observable.getCbmProductType().setKeyForSelected("");
            txtTransProductId.setText("");
            tdtChequeDate.setDateValue("");
            cboInstrumentType.setSelectedItem("");
            lblCustomerNameValue.setText("");
            txtDebitAccNo.setEnabled(false);
            txtDebitAccNo.setEditable(false);
            btnTransProductId.setEnabled(false);
            btnDebitAccNo.setEnabled(false);
            //            txtApplicantsName.setText("");
            if(callingApplicantName !=null &&callingApplicantName.length()>0)
                txtApplicantsName.setText(getCallingApplicantName());
            if(getSourceScreen().equals("ACT_CLOSING")|| advanceCreditIntAmt.doubleValue()>0 ||getSourceScreen().equals("DEPOSITS") ||getSourceScreen().equals("REMITPAYMENT")){
                lblTokenNo.setVisible(true);
                lblTokenNo.setEnabled(true);
                txtTokenNo.setVisible(true);
                txtTokenNo.setEnabled(true);
                txtTokenNo.setEditable(true);
            }else{
                lblTokenNo.setVisible(false);
                lblTokenNo.setEnabled(false);
                txtTokenNo.setVisible(false);
                txtTokenNo.setEnabled(false);
                txtTokenNo.setEditable(false);
            }
            if((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) {
                ComboBoxModel objModel = new ComboBoxModel();
                objModel.addKeyAndElement("", observable.getCbmInstrumentType().getDataForKey(""));
                objModel.addKeyAndElement("WITHDRAW_SLIP", observable.getCbmInstrumentType().getDataForKey("WITHDRAW_SLIP"));
                objModel.addKeyAndElement("CHEQUE", observable.getCbmInstrumentType().getDataForKey("CHEQUE"));
                objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
                cboInstrumentType.setModel(objModel);
                cboInstrumentType.setSelectedItem(((ComboBoxModel)cboInstrumentType.getModel()).getDataForKey(observable.getCboInstrumentType()));
                cboInstrumentType.setEnabled(true);
                tdtChequeDate.setDateValue(CommonUtil.convertObjToStr(currDt));
                txtChequeNo.setEnabled(true);
                txtChequeNo2.setEnabled(true);
            }
            //            if(((getSourceScreen().equals("REMITISSUE")) || (getSourceScreen().equals("REMITPAYMENT"))) && ((getCallingUiMode() == ClientConstants.ACTIONTYPE_NEW) || (getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT))){
            //                if((tblTransDetails.getRowCount() == 0) || (tblTransDetails.getRowCount() == 1)){
            //
            //                }else{
            //                    ClientUtil.showAlertWindow("Cash cannot be selected");
            //                    cboTransType.setSelectedItem("");
            //                }
            //            }    
            txtParticulars.setEnabled(true);
            observable.setSelectedTxnBranchId(ProxyParameters.BRANCH_ID);
            observable.setSelectedTxnType(transactionType);
            cboInstrumentType.setSelectedItem(observable.getCbmInstrumentType().getDataForKey("WITHDRAW_SLIP"));
            cboInstrumentType.setEnabled(true);
                    
        } else {	
            setDebitAccHDButtonEnableDisable(true);
            whenCashIsSelectedDisable(true);
            txtDebitAccNo.setEnabled(true);
            txtDebitAccNo.setEditable(true);
            String status = getCallingStatus();
            if(status.equals("LIEN")){
                tdtChequeDate.setEnabled(false);
            }else{
                ClientUtil.enableDisable(panRightSubTxDetails, true);
                //          populateInstrumentTypeCash();    // commented by Rajesh.
                setDebitAccHDButtonEnableDisable(true);
                whenCashIsSelectedDisable(true);
                String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
                addingRMDeposits();
                if(prodType.equals("OA"))
                    getOperativeAccNo();
                getMaturedDetails(transType);
                if(getSourceScreen() == "REMITPAYMENT"){
                    cboInstrumentType.setEnabled(false);
                    ClientUtil.enableDisable(cPanel1,false);
                    tdtChequeDate.setEnabled(false);
                }
            }
            if((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) {
                String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
                if (prodType != null && !prodType.equals("RM")) {
                    if (getSourceScreen().equals("ACT_CLOSING")) {
                        ComboBoxModel objModel = new ComboBoxModel();
                        objModel.addKeyAndElement("", observable.getCbmInstrumentType().getDataForKey(""));
                       	objModel.addKeyAndElement("CHEQUE", observable.getCbmInstrumentType().getDataForKey("CHEQUE"));
                        objModel.addKeyAndElement("VOUCHER", observable.getCbmInstrumentType().getDataForKey("VOUCHER"));
                        cboInstrumentType.setModel(objModel);
                        cboInstrumentType.setSelectedItem(((ComboBoxModel) cboInstrumentType.getModel()).getDataForKey(observable.getCboInstrumentType()));
                        cboInstrumentType.setEnabled(true);
                        tdtChequeDate.setDateValue(CommonUtil.convertObjToStr(currDt));
                        txtChequeNo.setEnabled(true);
                        txtChequeNo2.setEnabled(true);
                    } else {
                        tdtChequeDate.setDateValue(CommonUtil.convertObjToStr(currDt));
                        cboInstrumentType.setEnabled(false);
                        tdtChequeDate.setEnabled(false);
                        cboInstrumentType.setSelectedItem("Voucher");
                    }
                    
                }else{
                    txtTransProductId.setEnabled(false);
                    cboInstrumentType.setEnabled(true);
                    cboInstrumentType.setSelectedItem("");
                }
            }
            lblTokenNo.setVisible(false);
            lblTokenNo.setEnabled(false);
            txtTokenNo.setVisible(false);
            txtTokenNo.setEnabled(false);
            txtTokenNo.setEditable(false);
            txtParticulars.setEnabled(true);
        }
    }
    
    private boolean checkForCashLmt() {
        
        boolean val = false;
        if(getSourceScreen().equals("REMITISSUE")) {
            String transType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
            double amt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
            HashMap dataMap = new HashMap();
            dataMap.put("PRODUCT_ID", getCallingProdID());
            List lst = (List) ClientUtil.executeQuery("getPanValidationAmt", dataMap);
            dataMap = null;
            if(lst != null)
                if(lst.size() > 0){
                    double cashLmt = CommonUtil.convertObjToDouble(lst.get(0)).doubleValue();
                    if((amt > cashLmt) && (transType.equals("CASH"))){
                        val = true;
                    }else{
                        val = false;
                    }
                }
        }
        return val;
    }
    
    private boolean checkForDepositCashLmt() {
        boolean val = false;
        if(getSourceScreen().equals("DEPOSITS")) {
            String transType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
            double amt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
            HashMap dataMap = new HashMap();
            dataMap.put(PROD_ID, getCallingProdID());
            List lst = ClientUtil.executeQuery("getLimitAmountForDepProd", dataMap);
            if(lst != null && lst.size()>0){
                dataMap = (HashMap)lst.get(0);
                double cashLimit = CommonUtil.convertObjToDouble(dataMap.get("MAX_AMT_CASH")).doubleValue();
                if((amt > cashLimit) && (transType.equals("CASH"))){
                    val = true;
                }else{
                    val = false;
                }
            }
        }
        return val;
    }
    
    private boolean transferNewDeposit() {
        boolean val = false;
        if(getSourceScreen().equals("DEPOSITS")) {
            String prodType = ((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            if(prodType.equals("TD")){
                double amt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
                HashMap dataMap = new HashMap();
                String actNum = txtDebitAccNo.getText();
                if(actNum.lastIndexOf("_")!=-1)
                    actNum = actNum.substring(0,actNum.lastIndexOf("_"));
                dataMap.put("DEPOSIT_NO", actNum);
                List lst = (List) ClientUtil.executeQuery("getDepAmtForOldDeposit", dataMap);
                if(lst != null && lst.size() > 0){
                    dataMap = (HashMap)lst.get(0);
                    double cashLmt = CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT_AMT")).doubleValue();
                    if(amt > cashLmt){
                        val = true;
                    }else{
                        val = false;
                    }
                }
            }
        }
        return val;
    }


    public void enableDisableTransType(boolean flag) {
        cboTransType.setEnabled(flag);
    }
    /**
     * Getter for property outputTO.
     * @return Value of property outputTO.
     */
    public java.util.LinkedHashMap getOutputTO() {
        //System.out.println(observable.getAllowedTransactionDetailsTO());
        return observable.getAllowedTransactionDetailsTO();
    }
    
    //     /**
    //     * Getter for property outputTO.
    //     * @return Value of property outputTO.
    //     */
    //    public java.util.LinkedHashMap getDupOutputTO() {
    //        //System.out.println(observable.getAllowedTransactionDetailsTO());
    //        return observable.getAllowedTransactionDetailsTO();
    //    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panTransDetails = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        sptTransDetails = new com.see.truetransact.uicomponent.CSeparator();
        panLeftSubTxDetails = new com.see.truetransact.uicomponent.CPanel();
        lblApplicantsName = new com.see.truetransact.uicomponent.CLabel();
        txtApplicantsName = new com.see.truetransact.uicomponent.CTextField();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        cboTransType = new com.see.truetransact.uicomponent.CComboBox();
        lblTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTransactionAmt = new com.see.truetransact.uicomponent.CTextField();
        lblTransProductId = new com.see.truetransact.uicomponent.CLabel();
        panDebitAccHead = new com.see.truetransact.uicomponent.CPanel();
        txtTransProductId = new com.see.truetransact.uicomponent.CTextField();
        btnTransProductId = new com.see.truetransact.uicomponent.CButton();
        lblDebitAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        panDebitAccNo = new com.see.truetransact.uicomponent.CPanel();
        txtDebitAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnDebitAccNo = new com.see.truetransact.uicomponent.CButton();
        lblCustomerNameValue = new com.see.truetransact.uicomponent.CLabel();
        panRightSubTxDetails = new com.see.truetransact.uicomponent.CPanel();
        lblChequeNo = new com.see.truetransact.uicomponent.CLabel();
        lblChequeDate = new com.see.truetransact.uicomponent.CLabel();
        tdtChequeDate = new com.see.truetransact.uicomponent.CDateField();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        txtChequeNo = new com.see.truetransact.uicomponent.CTextField();
        txtChequeNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentType = new com.see.truetransact.uicomponent.CLabel();
        cboInstrumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblTokenNo = new com.see.truetransact.uicomponent.CLabel();
        txtTokenNo = new com.see.truetransact.uicomponent.CTextField();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
        panbtnTxDetails = new com.see.truetransact.uicomponent.CPanel();
        btnNewTxDetails = new com.see.truetransact.uicomponent.CButton();
        btnSaveTxDetails = new com.see.truetransact.uicomponent.CButton();
        btnDeleteTxDetails = new com.see.truetransact.uicomponent.CButton();
        tabTransDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srpTransDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransDetails = new com.see.truetransact.uicomponent.CTable();
        panTotalTransactiosn = new com.see.truetransact.uicomponent.CPanel();
        lblTotalTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panDetails = new com.see.truetransact.uicomponent.CPanel();

        setLayout(new java.awt.GridBagLayout());

        panTransDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Details"));
        panTransDetails.setMinimumSize(new java.awt.Dimension(825, 215));
        panTransDetails.setPreferredSize(new java.awt.Dimension(825, 215));
        panTransDetails.setLayout(new java.awt.GridBagLayout());

        panTransaction.setMinimumSize(new java.awt.Dimension(545, 240));
        panTransaction.setPreferredSize(new java.awt.Dimension(545, 240));
        panTransaction.setLayout(new java.awt.GridBagLayout());

        sptTransDetails.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.weighty = 1.0;
        panTransaction.add(sptTransDetails, gridBagConstraints);

        panLeftSubTxDetails.setMinimumSize(new java.awt.Dimension(270, 225));
        panLeftSubTxDetails.setPreferredSize(new java.awt.Dimension(270, 225));
        panLeftSubTxDetails.setLayout(new java.awt.GridBagLayout());

        lblApplicantsName.setText("Applicants Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(lblApplicantsName, gridBagConstraints);

        txtApplicantsName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtApplicantsName.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                txtApplicantsNameMouseMoved(evt);
            }
        });
        txtApplicantsName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApplicantsNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(txtApplicantsName, gridBagConstraints);

        lblTransType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(lblTransType, gridBagConstraints);

        cboTransType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboTransType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTransType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTransTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(cboTransType, gridBagConstraints);

        lblTransactionAmt.setText("Transaction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(lblTransactionAmt, gridBagConstraints);

        txtTransactionAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransactionAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransactionAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(txtTransactionAmt, gridBagConstraints);

        lblTransProductId.setText("Prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(lblTransProductId, gridBagConstraints);

        panDebitAccHead.setLayout(new java.awt.GridBagLayout());

        txtTransProductId.setAllowAll(true);
        txtTransProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitAccHead.add(txtTransProductId, gridBagConstraints);

        btnTransProductId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTransProductId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTransProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDebitAccHead.add(btnTransProductId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeftSubTxDetails.add(panDebitAccHead, gridBagConstraints);

        lblDebitAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(lblDebitAccNo, gridBagConstraints);

        lblProductType.setText("Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(lblProductType, gridBagConstraints);

        cboProductType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.setPopupWidth(150);
        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLeftSubTxDetails.add(cboProductType, gridBagConstraints);

        panDebitAccNo.setLayout(new java.awt.GridBagLayout());

        txtDebitAccNo.setAllowAll(true);
        txtDebitAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDebitAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDebitAccNoActionPerformed(evt);
            }
        });
        txtDebitAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDebitAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitAccNo.add(txtDebitAccNo, gridBagConstraints);

        btnDebitAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDebitAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panDebitAccNo.add(btnDebitAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeftSubTxDetails.add(panDebitAccNo, gridBagConstraints);

        lblCustomerNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameValue.setFont(new java.awt.Font("MS Sans Serif", 0, 12)); // NOI18N
        lblCustomerNameValue.setMinimumSize(new java.awt.Dimension(200, 15));
        lblCustomerNameValue.setPreferredSize(new java.awt.Dimension(200, 15));
        lblCustomerNameValue.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lblCustomerNameValueMouseMoved(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panLeftSubTxDetails.add(lblCustomerNameValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTransaction.add(panLeftSubTxDetails, gridBagConstraints);

        panRightSubTxDetails.setMinimumSize(new java.awt.Dimension(195, 145));
        panRightSubTxDetails.setPreferredSize(new java.awt.Dimension(195, 115));
        panRightSubTxDetails.setLayout(new java.awt.GridBagLayout());

        lblChequeNo.setText("Cheque No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubTxDetails.add(lblChequeNo, gridBagConstraints);

        lblChequeDate.setText("Cheque Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubTxDetails.add(lblChequeDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panRightSubTxDetails.add(tdtChequeDate, gridBagConstraints);

        cPanel1.setLayout(new java.awt.GridBagLayout());

        txtChequeNo.setMinimumSize(new java.awt.Dimension(30, 21));
        txtChequeNo.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(txtChequeNo, gridBagConstraints);

        txtChequeNo2.setMinimumSize(new java.awt.Dimension(80, 21));
        txtChequeNo2.setPreferredSize(new java.awt.Dimension(80, 21));
        txtChequeNo2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChequeNo2FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 25);
        cPanel1.add(txtChequeNo2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panRightSubTxDetails.add(cPanel1, gridBagConstraints);

        lblInstrumentType.setText("Inst Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubTxDetails.add(lblInstrumentType, gridBagConstraints);

        cboInstrumentType.setMinimumSize(new java.awt.Dimension(101, 21));
        cboInstrumentType.setPreferredSize(new java.awt.Dimension(101, 21));
        cboInstrumentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInstrumentTypeActionPerformed(evt);
            }
        });
        cboInstrumentType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboInstrumentTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panRightSubTxDetails.add(cboInstrumentType, gridBagConstraints);

        lblTokenNo.setText("Token No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightSubTxDetails.add(lblTokenNo, gridBagConstraints);

        txtTokenNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTokenNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTokenNoActionPerformed(evt);
            }
        });
        txtTokenNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokenNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 2);
        panRightSubTxDetails.add(txtTokenNo, gridBagConstraints);

        lblParticulars.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblParticulars.setText("Particulars");
        lblParticulars.setMinimumSize(new java.awt.Dimension(92, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRightSubTxDetails.add(lblParticulars, gridBagConstraints);

        txtParticulars.setMinimumSize(new java.awt.Dimension(100, 21));
        txtParticulars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtParticularsActionPerformed(evt);
            }
        });
        txtParticulars.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtParticularsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 2, 2);
        panRightSubTxDetails.add(txtParticulars, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(panRightSubTxDetails, gridBagConstraints);

        panbtnTxDetails.setMinimumSize(new java.awt.Dimension(127, 75));
        panbtnTxDetails.setPreferredSize(new java.awt.Dimension(127, 75));
        panbtnTxDetails.setLayout(new java.awt.GridBagLayout());

        btnNewTxDetails.setText("New");
        btnNewTxDetails.setEnabled(false);
        btnNewTxDetails.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnNewTxDetails.setNextFocusableComponent(txtApplicantsName);
        btnNewTxDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewTxDetailsActionPerformed(evt);
            }
        });
        btnNewTxDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnNewTxDetailsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panbtnTxDetails.add(btnNewTxDetails, gridBagConstraints);

        btnSaveTxDetails.setText("Save");
        btnSaveTxDetails.setEnabled(false);
        btnSaveTxDetails.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnSaveTxDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTxDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panbtnTxDetails.add(btnSaveTxDetails, gridBagConstraints);

        btnDeleteTxDetails.setText("Delete");
        btnDeleteTxDetails.setEnabled(false);
        btnDeleteTxDetails.setMargin(new java.awt.Insets(2, 5, 2, 5));
        btnDeleteTxDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTxDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panbtnTxDetails.add(btnDeleteTxDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(panbtnTxDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panTransDetails.add(panTransaction, gridBagConstraints);

        tabTransDetails.setFont(new java.awt.Font("MS Sans Serif", 0, 11)); // NOI18N
        tabTransDetails.setMinimumSize(new java.awt.Dimension(340, 162));
        tabTransDetails.setPreferredSize(new java.awt.Dimension(340, 162));

        panTable.setMinimumSize(new java.awt.Dimension(200, 133));
        panTable.setPreferredSize(new java.awt.Dimension(200, 133));
        panTable.setLayout(new java.awt.GridBagLayout());

        srpTransDetails.setMinimumSize(new java.awt.Dimension(285, 120));
        srpTransDetails.setPreferredSize(new java.awt.Dimension(285, 120));

        tblTransDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblTransDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblTransDetailsMousePressed(evt);
            }
        });
        srpTransDetails.setViewportView(tblTransDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTable.add(srpTransDetails, gridBagConstraints);

        panTotalTransactiosn.setLayout(new java.awt.GridBagLayout());

        lblTotalTransactionAmt.setText("Total Amout");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTotalTransactiosn.add(lblTotalTransactionAmt, gridBagConstraints);

        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        panTotalTransactiosn.add(lblTotalTransactionAmtVal, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panTable.add(panTotalTransactiosn, gridBagConstraints);

        tabTransDetails.addTab("Transactions", panTable);

        panDetails.setMinimumSize(new java.awt.Dimension(330, 133));
        panDetails.setPreferredSize(new java.awt.Dimension(330, 133));
        panDetails.setLayout(new java.awt.GridBagLayout());
        tabTransDetails.addTab("Details", panDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTransDetails.add(tabTransDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panTransDetails, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtTokenNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokenNoFocusLost
        // TODO add your handling code here:
        txtTokenNoActionPerformed(null);
    }//GEN-LAST:event_txtTokenNoFocusLost
    
    private void txtTokenNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTokenNoActionPerformed
        // TODO add your handling code here:
        if ( getSourceScreen().equals("DEPOSITS")||getSourceScreen().equals("ACT_CLOSING") ||advanceCreditIntAmt.doubleValue()>0 || getSourceScreen().equals("REMITPAYMENT")){
            String tokenNo = CommonUtil.convertObjToStr(txtTokenNo.getText());
            if (!tokenNo.equals("")) {
                HashMap tokenWhereMap = new HashMap();
                
                // Separating Serias No and Token No
                char[] chrs = tokenNo.toCharArray();
                StringBuffer seriesNo = new StringBuffer();
                int i=0;
                for (int j= chrs.length; i < j; i++ ) {
                    if (Character.isDigit(chrs[i]))
                        break;
                    else
                        seriesNo.append(chrs[i]);
                }
                
                tokenWhereMap.put("SERIES_NO", seriesNo.toString());
                tokenWhereMap.put("TOKEN_NO", CommonUtil.convertObjToInt(tokenNo.substring(i)));
                tokenWhereMap.put("USER_ID", ProxyParameters.USER_ID);
                tokenWhereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                tokenWhereMap.put("CURRENT_DT", currDt.clone());
                
                List lst = ClientUtil.executeQuery("validateTokenNo", tokenWhereMap);
                
                if (((Integer) lst.get(0)).intValue() == 0) {
                    txtTokenNo.setText("");
                    COptionPane.showMessageDialog(this, resourceBundle.getString("tokenMsg"));
                }
                //                if (lst!=null && lst.size()>0 && ((Integer) lst.get(0)).intValue() == 0) {
                //                    txtTokenNo.setText("");
                //                    COptionPane.showMessageDialog(this, resourceBundle.getString("tokenMsg"));
                //                }else{
                //                    txtTokenNo.setText("");
                //                    COptionPane.showMessageDialog(this, resourceBundle.getString("tokenMsg"));
                //                }
            }
        }
    }//GEN-LAST:event_txtTokenNoActionPerformed
    public boolean isAccountNumberLinkedwithATMProd(String actNumber) {
        boolean flag = false;
        if(actNumber != null && !actNumber.equals("")){
            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NUM",actNumber);
            whereMap.put("CURR_DT",ClientUtil.getCurrentDate());
            List unAuthList = ClientUtil.executeQuery("getIsAccountLinkedwitATM", whereMap);
            if(unAuthList != null && unAuthList.size() >0){
                ClientUtil.showMessageWindow("This account is linked with ATM Product, Not allowed for any Credit/Debit Transactions...!");
                txtDebitAccNo.setText("");
                flag = true;
            }
        }
        return flag;
    }
    private void txtDebitAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebitAccNoFocusLost
        // TODO add your handling code here:
        txt_accno=txtDebitAccNo.getText();
        //Added By Suresh
        if (CommonUtil.convertObjToStr(txt_accno).length() > 0) {
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProductType).getModel())).getKeyForSelected());
            transactionType = CommonUtil.convertObjToStr(observable.getCbmTransactionType().getKeyForSelected());
            if (!prodType.equals("GL")) {
                observable.checkAcNoWithoutProdType(txt_accno);
                isAccountNumberLinkedwithATMProd(CommonUtil.convertObjToStr(txt_accno));
                txtDebitAccNo.setText(observable.getTxtDebitAccNo());
                txt_accno = observable.getTxtDebitAccNo();
                txtTransProductId.setText(observable.getTxtTransProductId());
                //System.out.println("transactionType : " + transactionType);
                if (transactionType.equals("TRANSFER")) {
                    observable.setSelectedTxnBranchId(observable.getSelectedBranchID());
                    observable.setSelectedTxnType(transactionType);
                    transDetails.setTransDetails(CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected()),
                    ProxyParameters.BRANCH_ID, observable.getTxtDebitAccNo());
                } else {
                    observable.setSelectedTxnBranchId(ProxyParameters.BRANCH_ID);
                    observable.setSelectedTxnType(transactionType);
                }
                //System.out.println("prodTypeprodTypeprodType"+prodType);
                if (prodType.equals("TD")) {
                    String accountNo = "";
                    //Changed By Suresh
                    accountNo = txtDebitAccNo.getText();
                    HashMap supMap = new HashMap();
                    supMap.put("DEPOSIT_NO", accountNo+"_1");
                    List lstSupName = ClientUtil.executeQuery("getDepositAmount", supMap);
                    HashMap supMap1 = new HashMap();
                    supMap1 = (HashMap) lstSupName.get(0);
                    double reminAmt = CommonUtil.convertObjToDouble(supMap1.get("DEPOSIT_AMT"));
                    double closingAmt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
                    if (closingAmt > reminAmt && (CommonUtil.convertObjToStr(supMap1.get("BEHAVES_LIKE")).equals("DAILY"))) {
                        ClientUtil.showAlertWindow("Amount Exceeds the new Deposit Amount...\n"
                                + "Deposit Amount is : " + String.valueOf(CommonUtil.convertObjToDouble(txtTransactionAmt.getText())) + "\n"
                                + "Account balance is : " + String.valueOf(reminAmt));
                        txtDebitAccNo.setText("");
                        return;
                    }
                    if ((CommonUtil.convertObjToDouble(callingDepositeAmount) != null) && reminAmt != (CommonUtil.convertObjToDouble(callingDepositeAmount))) {
                        if (!(CommonUtil.convertObjToStr(supMap1.get("BEHAVES_LIKE")).equals("DAILY"))) {

                            ClientUtil.showAlertWindow("Amount Exceeds the new Deposit Amount...\n"
                                    + "Deposit Amount is : " + String.valueOf(CommonUtil.convertObjToDouble(callingDepositeAmount)) + "\n"
                                    + "Remainig Amount is : " + String.valueOf(reminAmt));
                            txtDebitAccNo.setText("");
                            return;
                        }
                    }
                    
                }
               
                viewType = ACC_NUM;
                 if (prodType.equals("TD")) {
                     txt_accno=txt_accno+"_1";
                     txtDebitAccNo.setText(txt_accno);
                 }
                HashMap txtDebitAccno = new HashMap();
                txtDebitAccno.put("ACT_NUM", txt_accno);
                if (!prodType.equals("RM") && !prodType.equals("GL")) {
                    if (validateAccountNo(prodType, txt_accno)) {
                        ClientUtil.showMessageWindow("Invalid No  Plz Enter correct number");
                        txtDebitAccNo.setText("");
                        return;
                    }
                } else {
                    return;
                }
               // fillData(txtDebitAccno);         
                observable.setLblCustomerNameVal(null,observable.getCboProductType());//added by shihad for mantis 10339 on 16.02.2015        
                txtApplicantsName.setText(observable.getLblCustomerNameVal());        
                lblCustomerNameValue.setText(observable.getLblCustomerNameVal());
                observable.setTxtApplicantsName(txtApplicantsName.getText());
                //Added by sreekrishnan for interbranch day-end checking
                if(!txtDebitAccNo.getText().equals("") && txtDebitAccNo.getText().length() >0 && 
            	(getSourceScreen().equals("DRF TRANSACTION") || getSourceScreen().equals("DRF RECOVERY") || 
            	getSourceScreen().equals("SHARE_SCREEN") || getSourceScreen().equals("SHARE_DIVIDEND_PAYMENT") ||
            	getSourceScreen().equals("MDS_APPLICATION") ||/* getSourceScreen().equals("MDS_PRIZED_PAYMENT") || */
            	getSourceScreen().equals("MDS_MEMBER_RECEIPT") || 
            	/*getSourceScreen().equals("LOAN_ACT_CLOSING") || */getSourceScreen().equals("ACT_CLOSING") ||
            	/*getSourceScreen().equals("TERM_DEPOSIT") || getSourceScreen().equals("MULTIPLE_TERM_DEPOSIT") ||*/
            	getSourceScreen().equals("RENTTRANS") || getSourceScreen().equals("RENT_REGISTER"))){
                if(observable.getSelectedBranchID()!=null && !ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())){
                    Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(observable.getSelectedBranchID());
                    Date currentDate = (Date) currDt.clone();
                    //System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                    if(selectedBranchDt == null){
                        ClientUtil.showAlertWindow("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed");
                        txtDebitAccNo.setText("");
                        return;
                    }else if(DateUtil.dateDiff(currentDate, selectedBranchDt)!=0){
                        ClientUtil.showAlertWindow("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed");
                        txtDebitAccNo.setText("");
                        return;
                    }else {
                        System.out.println("Continue for interbranch trasactions ...");
                    }
                }
                }
               // KD 196
                if (!txtDebitAccNo.getText().equals("") && txtDebitAccNo.getText().length() > 0
                        && (getSourceScreen().equals("LOAN_ACT_CLOSING") || getSourceScreen().equals("MDS_PRIZED_PAYMENT"))) {
                    if (observable.getSelectedBranchID() != null && !ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())) {
                        Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(observable.getSelectedBranchID());
                        Date currentDate = (Date) currDt.clone();   
                        System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                        if (selectedBranchDt == null) {
                            ClientUtil.showAlertWindow("BOD is not completed for the selected branch " + "\n" + "Interbranch Transaction Not allowed");
                            txtDebitAccNo.setText("");
                            return;
                        } else if (DateUtil.dateDiff(currentDate, selectedBranchDt) < 0) {
                            ClientUtil.showAlertWindow("Application Date is different in the Selected branch " + "\n" + "Interbranch Transaction Not allowed");
                            txtDebitAccNo.setText("");
                            return;
                        } else {
                            System.out.println("Continue for interbranch trasactions ...");
                        }
                    }
                }
            }else if(prodType.equals("GL")){ //Added BY Suresh
                viewType = TRANS_PROD;
                HashMap whereMap = new HashMap();
                whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                whereMap.put("AC_HD_ID",txtDebitAccNo.getText());
                List GLlist=ClientUtil.executeQuery("InterMaintenance.getProductDataGL",whereMap);
                if(GLlist !=null && GLlist.size()>0){
                    whereMap = (HashMap)GLlist.get(0);
                    fillData(whereMap);
                }else{
                    ClientUtil.displayAlert("Invalid Account Head !!! ");
                    txtDebitAccNo.setText("");
                    return;
                }
                observable.setSelectedTxnBranchId(ProxyParameters.BRANCH_ID);
                observable.setSelectedTxnType(transactionType);
            }
            if ((getSourceScreen().equals("MDS_RECEIPT") || getSourceScreen().equals("GDS_RECEIPT")) && prodType.equals("AD")) {
                HashMap whereMap = new HashMap();
                whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                whereMap.put("ACT_NUM", txtDebitAccNo.getText());
                List list = (List) ClientUtil.executeQuery("getBalanceAD", whereMap);
                if (list != null && list.size() > 0) {
                    HashMap resultMap = (HashMap) list.get(0);

                    double availableBalance = CommonUtil.convertObjToDouble(resultMap.get("AV_BALANCE")).doubleValue();
                    double limitAmt = availableBalance;
                    //System.out.println("callingDepositeAmount  :" + txtTransactionAmt.getText() + "limitAmt :" + limitAmt);
                    if ((CommonUtil.convertObjToDouble(txtTransactionAmt.getText()) != null) && limitAmt < (CommonUtil.convertObjToDouble(txtTransactionAmt.getText()))) {
                        //System.out.println("limitAmt : amount : " + limitAmt + ":" + limitAmt);
                        ClientUtil.showMessageWindow("Transaction Amount Exceeds the Limit Amount");
                        txtDebitAccNo.setText("");
                        return;

                    }
                }
            }
        }
    }//GEN-LAST:event_txtDebitAccNoFocusLost
    
    private void txtChequeNo2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChequeNo2FocusLost
        // TODO add your handling code here:
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProductType.getModel()).getKeyForSelected());
        if(((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) && prodType.equals("RM")){
            HashMap dataMap = new HashMap();
            dataMap.put("INSTRUMENT_NO1", txtChequeNo.getText());
            dataMap.put("INSTRUMENT_NO2", txtChequeNo2.getText());
            List lst = ClientUtil.executeQuery("checkValidInsNum", dataMap);
            if(lst != null && lst.size() > 0){
                dataMap = (HashMap) lst.get(0);
                String authStatus = CommonUtil.convertObjToStr(dataMap.get("AUTHORIZE_STATUS"));
                if(authStatus.equalsIgnoreCase("")){
                    ClientUtil.showAlertWindow("Instrument "+txtChequeNo.getText()+"-"+txtChequeNo2.getText()+" already issued and pending for Authorization");
                    txtChequeNo2.setText("");
                }else if(dataMap.get("AUTHORIZE_STATUS").equals("AUTHORIZED")){
                    ClientUtil.showAlertWindow("Instrument "+txtChequeNo.getText()+"-"+txtChequeNo2.getText()+" already issued");
                    txtChequeNo2.setText("");
                }
            }else{
                //DO NOTHING
            }
        }
    }//GEN-LAST:event_txtChequeNo2FocusLost
    
    private void btnNewTxDetailsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnNewTxDetailsFocusLost
        // TODO add your handling code here:
        //        btnNewTxDetailsActionPerformed(null);
    }//GEN-LAST:event_btnNewTxDetailsFocusLost
    
    private void txtTransactionAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransactionAmtFocusLost
        // TODO add your handling code here:
             if(getSourceScreen().equals("SHG_TRANSACTION") && txtTransactionAmt.getText().length()>0){
                 double toatalSHGTransAmt = CommonUtil.convertObjToDouble(getCallingAmount()).doubleValue();
                 double excessAmt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue() - toatalSHGTransAmt;
                 if(CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue() < toatalSHGTransAmt){
                     ClientUtil.showMessageWindow("Transaction Amount Should Be Equal Or Greater than Rs."+toatalSHGTransAmt);
                     txtTransactionAmt.setText("");
                 }
                 if(CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue() > toatalSHGTransAmt){
                     ClientUtil.showMessageWindow("Excess Amount Rs. "+excessAmt+" will be Credit Back to the KOD Account ");
                 }
             }
    }//GEN-LAST:event_txtTransactionAmtFocusLost
    
    private void txtApplicantsNameMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtApplicantsNameMouseMoved
        // TODO add your handling code here:
        txtApplicantsName.setToolTipText(observable.getTxtApplicantsName());
    }//GEN-LAST:event_txtApplicantsNameMouseMoved
    
    private void lblCustomerNameValueMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblCustomerNameValueMouseMoved
        // TODO add your handling code here:
        lblCustomerNameValue.setToolTipText(observable.getLblCustomerNameVal());
    }//GEN-LAST:event_lblCustomerNameValueMouseMoved
    
    private void txtApplicantsNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApplicantsNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApplicantsNameActionPerformed
    
    private void txtDebitAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDebitAccNoActionPerformed
        // TODO add your handling code here:
        //        txt_accno=txtDebitAccNo.getText();
        //        String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
        //        if(prodType.equals("TD"))
        //            txt_accno = txt_accno +"_1";
        //        viewType = ACC_NUM;
        //        HashMap txtDebitAccno=new HashMap();
        //        txtDebitAccno.put("ACT_NUM",txt_accno);
        //        if(!prodType.equals("GL")){
        //        if(validateAccountNo(prodType,txt_accno)){
        //            ClientUtil.showMessageWindow("Invalid No  Plz EnterCurrect number");
        //            return;
        //        }
        //        }
        //        fillData(txtDebitAccno);
        //        txtApplicantsName.setText(observable.getLblCustomerNameVal());
        //        observable.setTxtApplicantsName(txtApplicantsName.getText());
    }//GEN-LAST:event_txtDebitAccNoActionPerformed
    
    private void cboInstrumentTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInstrumentTypeFocusLost
        //instrumentTypeFocus();
    }//GEN-LAST:event_cboInstrumentTypeFocusLost
    
    private void cboInstrumentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInstrumentTypeActionPerformed
        instrumentTypeFocus();
    }//GEN-LAST:event_cboInstrumentTypeActionPerformed
    
    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        txtTransProductId.setText("");
        txtDebitAccNo.setText("");
        String transType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
        if (transType.equals("TRANSFER")){
            //System.out.println("Firing...." + observable.getCboInstrumentType());
            populateInstrumentType();
            if((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) {
                String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
                if(prodType!=null && !prodType.equals("RM")){
                    cboInstrumentType.setSelectedItem("Voucher");
                    tdtChequeDate.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
                    if(getSourceScreen().equals("ACT_CLOSING")&&prodType.equals("AB")){
                        cboInstrumentType.setEnabled(true);
                    }else{
                    	cboInstrumentType.setEnabled(false);
                    }
                    tdtChequeDate.setEnabled(false);
                }else{
                    txtTransProductId.setEnabled(false);
                    cboInstrumentType.setEnabled(true);
                    cboInstrumentType.setSelectedItem("");
                }
            }
            //Added By Suresh
            if(getSourceScreen().equals("INVESTMENT_TRANS") || getSourceScreen().equals("INVESTMENT_CHARGE") || getSourceScreen().equals("INVESTMENT")){
                String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
                if(prodType!=null && prodType.equals("BRW")){
                    txtTransProductId.setEnabled(false);
                    btnTransProductId.setEnabled(false);
                }else{
                    if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && !prodType.equals("GL")){
                        txtTransProductId.setEnabled(true);
                        btnTransProductId.setEnabled(true);
                    }
                }
            }
            //             if(((getSourceScreen().equals("REMITISSUE")) || (getSourceScreen().equals("REMITPAYMENT"))) && ((getCallingUiMode() == ClientConstants.ACTIONTYPE_NEW) || (getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT))){
            //                if((tblTransDetails.getRowCount() == 0) || (tblTransDetails.getRowCount() == 1)){
            //
            //                }else{
            //                    String prodType=((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            //                    if(!prodType.equals("GL")){
            //                        ClientUtil.showAlertWindow("Select only GL");
            //                        cboProductType.setSelectedItem("");
            //                    }
            //                }
            //            }
        }
        getMaturedDetails(transType);
    }//GEN-LAST:event_cboProductTypeActionPerformed
    private boolean validateAccountNo(String prodType,String AccountNo){
        HashMap map=new HashMap();
        map.put("ACC_NUM",AccountNo);
        
            map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        if(prodType.equals("TL") ||prodType.equals("AD")|| prodType.equals("TD")|| prodType.equals("OA") || prodType.equals("GL")){
            List lst= ClientUtil.executeQuery("getAccountNumberName"+ prodType, map);
            if(lst== null || lst.isEmpty()){
               
                return true;
            }else{
                  map =(HashMap)lst.get(0);
                ////Can't add inter-branch operative accounts in mds receipt entry  prodType.equals("OA")
                //changed by Nidhin 27/03/2014
//                if(getSourceScreen().equals("MDS_RECEIPT")){
//                    if(( prodType.equals("TL") || prodType.equals("AD")|| prodType.equals("TD")) || prodType.equals("OA")){                
//                        String initatedBranch=CommonUtil.convertObjToStr(ProxyParameters.BRANCH_ID);
//                        if(!CommonUtil.convertObjToStr(map.get("BRANCH_CODE")).equals(initatedBranch)){
//                            ClientUtil.displayAlert("Other Branch Account Not able to select for MDS Receipt");
//                            txtDebitAccNo.setText("");
//                             return true;
//                        }
//                    }
//                }
            }
        }
        return false;
    }
    private void btnDeleteTxDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTxDetailsActionPerformed
        if (tblTransDetails.getSelectedRow() >= 0) {
            btnDeleteTransactionDetailsFlag = true;
            observable.deleteTransDetails(getSelectRow());
            setPanTransactionDetailsEnableDisable(false);
            setDebitAccHDButtonEnableDisable(false);
            setSaveDeleteTransDisable();
            observable.resetTransactionDetails();
            update(null, null);
            if (tblTransDetails.getRowCount() == 0) {
                // If all rows are deleted reset total transaction amount
                resetTotalTransactionAmt();
            }
            tabletransactionMousePressed = false;
            if(getCallingStatus().equals("REMIT_DUP")){
                remittanceIssue.setDupChrAndTaxValues();
                remittanceIssue.setDupChrAndTaxEnableDisable(false);
                btnSaveTransactionDetailsFlag = true;
            }
            if(getCallingStatus().equals("REMIT_REV")){
                remittanceIssue.setRevChrAndTaxValues();
                remittanceIssue.setRevChrAndTaxEnableDisable(false);
                btnSaveTransactionDetailsFlag = true;
            }
        }
    }//GEN-LAST:event_btnDeleteTxDetailsActionPerformed
    
    private void cboTransTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTransTypeActionPerformed
        transactionType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
        if (transactionType.length() > 0 && cboTransType.isEnabled()) {
            transactionTypeActionPerformed(transactionType);
        }
        if(getSourceScreen().equals("DEPOSITS") && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && transactionType.equals("TRANSFER")){
            getOperativeAccNo();
        }
    }//GEN-LAST:event_cboTransTypeActionPerformed
    
    private void tblTransDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransDetailsMousePressed
       btnDeleteTxDetails.setEnabled(true);
        if (tblTransDetails.getSelectedRow() >= 0 && observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
            addingRMDeposits();
            btnDeleteTxDetails.setEnabled(true);
            btnSaveTransactionDetailsFlag = false;
            tabletransactionMousePressed = true;
            setNewSaveDeleteTransEnable();
            setTableTransDetailsEnableDisable(true);
            
            setSelectRow(tblTransDetails.getSelectedRow());
            observable.setSelectedRowValue(tblTransDetails.getSelectedRow());
            observable.populateSelectedTransDetails(String.valueOf(observable.getSelectedRowValue()+1));
            update(null, null);
            setPanTransactionDetailsEnableDisable(true);
            btnSaveTxDetails.setEnabled(true);
            transactionType = CommonUtil.convertObjToStr(observable.getCbmTransactionType().getKeyForSelected());
            //System.out.println("transactionType : " + transactionType);
            if(transactionType.equals("TRANSFER")){
                cboProductType.setEnabled(true);
                if (transactionType.length() > 0) {
                    transactionTypeActionPerformed(transactionType);
                }
            }
            else{
                cboProductType.setEnabled(false);
            }
        }
        if(saveEnableValue!=0)
            callingUiMode=1;
        //System.out.println("callingUiMode : " + callingUiMode);
       if(callingUiMode ==-1 || callingUiMode==ClientConstants.ACTIONTYPE_CANCEL){
            ClientUtil.enableDisable(panTransaction, false);
            ClientUtil.disableAll(panbtnTxDetails, false);
        }
        else{
            //System.out.println("Coming to enable now....");
            ClientUtil.enableDisable(panTransaction, true);
            ClientUtil.disableAll(panbtnTxDetails, true);
            btnDeleteTxDetails.setEnabled(true);
            btnNewTxDetails.setEnabled(false);
        }
        //Added BY Suresh
        if (getSourceScreen().equals("LOAN_REPAYMENT") || getSourceScreen().equals("INTEREST_SUBSIDY") || getSourceScreen().equals("RTGS_REMITTANCE")) {
            txtTransactionAmt.setEnabled(false);
            cboTransType.setEnabled(false);
            ClientUtil.enableDisable(panRightSubTxDetails, true);
            btnSaveTxDetails.setEnabled(true);
        }
        if((getCallingUiMode() == ClientConstants.ACTIONTYPE_AUTHORIZE) || (getCallingUiMode() == ClientConstants.ACTIONTYPE_REJECT)){
            ClientUtil.enableDisable(panLeftSubTxDetails, false);
            ClientUtil.enableDisable(panRightSubTxDetails, false);
            ClientUtil.enableDisable(panbtnTxDetails, false);
            btnTransProductId.setEnabled(false);
            btnDebitAccNo.setEnabled(false);
            btnSaveTxDetails.setEnabled(false);
            btnTransProductId.setEnabled(false);
            btnDebitAccNo.setEnabled(false);
            if(getSourceScreen().equals("REMITISSUE")){
                setIsTranTblClicked(true);
            }
        }
        if(((getSourceScreen().equals("REMITISSUE")) || (getSourceScreen().equals("REMITPAYMENT")) ||
        (getSourceScreen().equals("CHARGES_SERVICETAX"))) && (getCallingUiMode() == (ClientConstants.ACTIONTYPE_EDIT))){
            transactionType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
            if (transactionType.equals("CASH")) {
                cboInstrumentType.setEnabled(false);
                tdtChequeDate.setEnabled(false);
                ClientUtil.enableDisable(cPanel1, false);
            }else{
                cboInstrumentType.setEnabled(true);
                tdtChequeDate.setEnabled(true);
                ClientUtil.enableDisable(cPanel1, true);
                txtTransProductId.setEnabled(false);
            }
            enableDisableTransType(false);
            cboProductType.setEnabled(false);
            btnTransProductId.setEnabled(false);
            btnDebitAccNo.setEnabled(false);
            txtDebitAccNo.setEnabled(false);
            txtApplicantsName.setEditable(true);
            txtApplicantsName.setEnabled(true);
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
            if(prodType.equals("TD"))
                ClientUtil.enableDisable(this,false);
        }
        if(((getSourceScreen().equals("REMITISSUE")) ||(getSourceScreen().equals("REMITPAYMENT"))||(getSourceScreen().equals("DEPOSITS"))||
        (getSourceScreen().equals("CHARGES_SERVICETAX"))) && (getCallingUiMode() == (ClientConstants.ACTIONTYPE_DELETE))){
            ClientUtil.enableDisable(panTransaction,false);
            ClientUtil.enableDisable(panbtnTxDetails,false);
            btnTransProductId.setEnabled(false);
            btnDebitAccNo.setEnabled(false);
            btnSaveTxDetails.setEnabled(false);
            btnSaveTransactionDetailsFlag = true;
        }
        if(getSourceScreen()!=null && getSourceScreen().equals("AccountClosingUI")){
            ClientUtil.enableDisable(panLeftSubTxDetails, false);
            ClientUtil.enableDisable(panRightSubTxDetails, false);
            btnTransProductId.setEnabled(false);
            btnDebitAccNo.setEnabled(false);
        }
        if((getCallingStatus().equals("REMIT_DUP")) || (getCallingStatus().equals("REMIT_REV"))){
            int rowCnt = tblTransDetails.getRowCount();
            if((tblTransDetails.getSelectedRow() == rowCnt-1) && (getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT) &&
            (tblTransDetails.getSelectedRow() != 0)){
                if((remittanceIssue.getRemittanceIssueOB().isDupInEdit()) || (remittanceIssue.getRemittanceIssueOB().isRevInEdit())){
                    //                     ClientUtil.enableDisable(panTransaction,false);
                    ClientUtil.enableDisable(panRightSubTxDetails,false);
                    cboTransType.setEnabled(false);
                    cboProductType.setEnabled(false);
                    ClientUtil.enableDisable(panDebitAccHead,false);
                    ClientUtil.enableDisable(panDebitAccNo,false);
                    //                tblTransDetails.setEnabled(false);
                    btnDeleteTxDetails.setEnabled(true);
                    txtTransactionAmt.setEditable(false);
                    txtTransactionAmt.setEnabled(false);
                    if(getCallingStatus().equals("REMIT_DUP"))
                        remittanceIssue.setDupChrAndTaxEnableDisable(true);
                    if(getCallingStatus().equals("REMIT_REV"))
                        remittanceIssue.setRevChrAndTaxEnableDisable(true);
                    if((remittanceIssue.getRemittanceIssueOB().isDupInEdit()) || (remittanceIssue.getRemittanceIssueOB().isRevInEdit())){
                        btnDeleteTxDetails.setEnabled(true);
                        setCallingApplicantName(txtApplicantsName.getText());
                    }
                }else{
                    cboTransType.setEnabled(true);
                }
            }else{
                //                tblTransDetails.setRowSelectionAllowed(fa
                remittanceIssue.setDupChrAndTaxEnableDisable(false);
                remittanceIssue.setRevChrAndTaxEnableDisable(false);
                ClientUtil.enableDisable(panTransaction,false);
                
                //                btnSaveTxDetails.setEnabled(false);
            }
            remittanceIssue.setDuplicationEnableDisable(false);
            txtDebitAccNo.setEditable(false);
        }
        //        if(((getSourceScreen().equals("REMITISSUE")) || (getSourceScreen().equals("REMITPAYMENT"))) && ((getCallingUiMode() == (ClientConstants.ACTIONTYPE_AUTHORIZE))
        //        || (getCallingUiMode() == ClientConstants.ACTIONTYPE_REJECT))){
        //            btnTransProductId.setEnabled(false);
        //            btnDebitAccNo.setEnabled(false);
        //        }
        //        cashLimitForDeposits();
        observable.ttNotifyObservers();
        depositsEditMode();
        if((getCallingUiMode() == ClientConstants.ACTIONTYPE_AUTHORIZE) || (getCallingUiMode() == ClientConstants.ACTIONTYPE_REJECT)){
            if (getSourceScreen().equals("ACT_CLOSING") || advanceCreditIntAmt.doubleValue()>0 ||getSourceScreen().equals("DEPOSITS") || getSourceScreen().equals("REMITPAYMENT")){
                if(transactionType.equals("CASH")){
                    txtTokenNo.setVisible(true);
                    txtTokenNo.setEnabled(false);
                    lblTokenNo.setVisible(true);
                    lblTokenNo.setEnabled(true);
                }
            }
        }
        if((getSourceScreen().equals("DEPOSITS")) && (getCallingUiMode() == (ClientConstants.ACTIONTYPE_DELETE))||
        (getCallingUiMode() == (ClientConstants.ACTIONTYPE_VIEW))){
            ClientUtil.enableDisable(panTransaction,false);
            ClientUtil.enableDisable(panbtnTxDetails,false);
            btnTransProductId.setEnabled(false);
            txtTransProductId.setEnabled(false);
            btnTransProductId.setEnabled(false);
            btnDebitAccNo.setEnabled(false);
            btnSaveTxDetails.setEnabled(false);
            cboProductType.setEnabled(false);
            btnSaveTransactionDetailsFlag = true;
        }
        if (getSourceScreen().equals("ACT_CLOSING") || getSourceScreen().equals("LOAN_ACT_CLOSING"))
            if (getCallingUiMode() != ClientConstants.ACTIONTYPE_NEW) {
                if(getSourceScreen().equals("ACT_CLOSING")){
                    if(getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT){
                        String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
                        cboInstrumentType.setEnabled(false);
                        tdtChequeDate.setEnabled(false);
                        ClientUtil.enableDisable(cPanel1, false);
                        enableDisableTransType(false);
                        cboProductType.setEnabled(false);
                        btnTransProductId.setEnabled(false);
                        btnDebitAccNo.setEnabled(false);
                        txtDebitAccNo.setEditable(false);
                        txtDebitAccNo.setEnabled(false);
                        txtApplicantsName.setEditable(true);
                        txtApplicantsName.setEnabled(true);
                        if(prodType.equals("RM")){
                            ClientUtil.enableDisable(panRightSubTxDetails, true);
                            txtDebitAccNo.setEnabled(true);
                            txtDebitAccNo.setEditable(true);
                            txtChequeNo.setEnabled(true);
                            txtChequeNo.setEditable(true);
                            txtChequeNo2.setEnabled(true);
                            txtChequeNo2.setEditable(true);
                            cboInstrumentType.setEnabled(false);
                        }
                    }else{
                        enableDisableForActClosing(false);
                        btnSaveTxDetails.setEnabled(false);
                    }
                }else{
                    enableDisableForActClosing(false);
                    btnSaveTxDetails.setEnabled(false);
                }
                
                btnSaveTransactionDetailsFlag = true;
            } else
                btnSaveTransactionDetailsFlag = false;
        
        if((getSourceScreen().equals("REMITISSUE")) && (getCallingUiMode() == ClientConstants.ACTIONTYPE_NEW)){
            if((tblTransDetails.getRowCount() > 1)){
                cboTransType.setEnabled(false);
                cboProductType.setEnabled(false);
            }
        }
        if(getSourceScreen().equals("MDS_APPLICATION")){
            ClientUtil.enableDisable(panLeftSubTxDetails,false);
            btnDebitAccNo.setEnabled(false);
            btnTransProductId.setEnabled(false);
            if(!getChitType().equals("") && getChitType().equals("THALAYAL") && !getSchemeName().equals("")){
                ClientUtil.enableDisable(panLeftSubTxDetails,false);
                btnDebitAccNo.setEnabled(false);
                btnTransProductId.setEnabled(false);                
                txtTransProductId.setEnabled(false);                
                btnTransProductId.setEnabled(false);                
                btnDebitAccNo.setEnabled(false);                
            }
        }
        if(getSourceScreen().equals("MDS_PRIZED_PAYMENT")){
            ClientUtil.enableDisable(panLeftSubTxDetails,false);
            btnDebitAccNo.setEnabled(false);
            btnTransProductId.setEnabled(false);
            if(!getChitType().equals("") && getChitType().equals("THALAYAL") && !getSchemeName().equals("")){
                ClientUtil.enableDisable(panLeftSubTxDetails,false);
                btnDebitAccNo.setEnabled(false);
                btnTransProductId.setEnabled(false);                
                txtTransProductId.setEnabled(false);                
                btnTransProductId.setEnabled(false);                
                btnDebitAccNo.setEnabled(false);                
            }
        }
        if(getSourceScreen().equals("MDS_COMMENCEMENT")){
            ClientUtil.enableDisable(panLeftSubTxDetails,false);
            btnDebitAccNo.setEnabled(false);
            btnTransProductId.setEnabled(false);
            ClientUtil.enableDisable(panLeftSubTxDetails,false);
            btnDebitAccNo.setEnabled(false);
            btnTransProductId.setEnabled(false);                
            txtTransProductId.setEnabled(false);                
            btnTransProductId.setEnabled(false);                
            btnDebitAccNo.setEnabled(false);                
            btnSaveTxDetails.setEnabled(true);                
        }
        if(getSourceScreen().equals("RECOVERY_LIST_TALLY")){
            ClientUtil.enableDisable(panLeftSubTxDetails,true);
            txtTransactionAmt.setEnabled(false);
            cboTransType.setEnabled(false);
        }
        //Added By Suresh
        if(getSourceScreen().equals("INVESTMENT_TRANS") || getSourceScreen().equals("INVESTMENT_CHARGE") || getSourceScreen().equals("INVESTMENT")){
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
            if(prodType!=null && prodType.equals("BRW")){
                txtTransProductId.setEnabled(false);
                btnTransProductId.setEnabled(false);
            }else{
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                    txtTransProductId.setEnabled(true);
                    btnTransProductId.setEnabled(true);
                }
            }
        }
        
        if((getCallingUiMode() == ClientConstants.ACTIONTYPE_AUTHORIZE) || (getCallingUiMode() == ClientConstants.ACTIONTYPE_REJECT)
            || (getCallingUiMode() == ClientConstants.ACTIONTYPE_VIEW) || (getCallingUiMode() == ClientConstants.ACTIONTYPE_EXCEPTION)
            || (getCallingUiMode() == ClientConstants.ACTIONTYPE_DELETE)){
            if(getSourceScreen().equals("SHG_TRANSACTION")){
                ClientUtil.enableDisable(panLeftSubTxDetails,false);
                btnDebitAccNo.setEnabled(false);
                btnTransProductId.setEnabled(false);
                ClientUtil.enableDisable(panLeftSubTxDetails,false);
                btnDebitAccNo.setEnabled(false);
                btnTransProductId.setEnabled(false);
                txtTransProductId.setEnabled(false);
                btnTransProductId.setEnabled(false);
                btnDebitAccNo.setEnabled(false);
                btnSaveTxDetails.setEnabled(false);
                ClientUtil.enableDisable(panTransaction,false);
            }
        }
        if((getSourceScreen().equals("INDEND_REGISTER"))){
            //System.out.println("getCallingUiMode() : " + getCallingUiMode());
            //System.out.println("getSaveEnableValue() : " + getSaveEnableValue());
            if((getCallingUiMode()==(ClientConstants.ACTIONTYPE_NEW))){
                    if((tblTransDetails.getRowCount() > 1)){
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    cboProductType.setEnabled(true);
                    txtParticulars.setEnabled(true);
                    cboTransType.setEnabled(true);
                    setButtonEnableDisable(false);
                }
            }
        }
        if((getSourceScreen().equals("GROUP_LOAN_PAYMENT"))||
          (getSourceScreen().equals("GROUP_LOAN"))){
            //System.out.println("getCallingUiMode() : " + getCallingUiMode());
            //System.out.println("getSaveEnableValue() : " + getSaveEnableValue());
            if((getCallingUiMode()==(ClientConstants.ACTIONTYPE_NEW))){
                    if((tblTransDetails.getRowCount() > 1)){
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    cboProductType.setEnabled(true);
                    txtParticulars.setEnabled(true);
                    cboTransType.setEnabled(true);
                    setButtonEnableDisable(false);
                }
            }
        }
        if((getSourceScreen().equals("INVESTMENT_MULTIPLE_MASTER"))){
            //System.out.println("getCallingUiMode() : " + getCallingUiMode());
            //System.out.println("getSaveEnableValue() : " + getSaveEnableValue());
            if((getCallingUiMode()==(ClientConstants.ACTIONTYPE_NEW))){
                    if((tblTransDetails.getRowCount() > 1)){
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    cboProductType.setEnabled(true);
                    txtParticulars.setEnabled(true);
                    cboTransType.setEnabled(true);
                    setButtonEnableDisable(false);
                }
            }
        }
        if((getSourceScreen().equals("INVESTMENT_TRANS"))){
            //System.out.println("getCallingUiMode() : " + getCallingUiMode());
            //System.out.println("getSaveEnableValue() : " + getSaveEnableValue());
            if((getCallingUiMode()==(ClientConstants.ACTIONTYPE_NEW))){
                    if((tblTransDetails.getRowCount() > 1)){
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    cboProductType.setEnabled(true);
                    txtParticulars.setEnabled(true);
                    cboTransType.setEnabled(true);
                    setButtonEnableDisable(false);
                }
            }
        }
        if((getSourceScreen().equals("PAY_ROLL_MASTER"))){
            //System.out.println("getCallingUiMode() : " + getCallingUiMode());
            //System.out.println("getSaveEnableValue() : " + getSaveEnableValue());
            if((getCallingUiMode()==(ClientConstants.ACTIONTYPE_NEW))){
                    if((tblTransDetails.getRowCount() > 1)){
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    cboProductType.setEnabled(true);
                    txtParticulars.setEnabled(true);
                    //txtApplicantsName.setEnabled(false);
                    cboTransType.setEnabled(true);
                    setButtonEnableDisable(false);
                }
            }
        }
       if((getSourceScreen().equals("SHARE_SCREEN"))){            
            if((getCallingUiMode()==(ClientConstants.ACTIONTYPE_NEW))){
                    if((tblTransDetails.getRowCount() > 1)){
                        //System.out.println("getCallingUiMode() : " + getCallingUiMode());
                        //System.out.println("getSaveEnableValue() : " + getSaveEnableValue());
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    ClientUtil.enableDisable(panLeftSubTxDetails,true);
                    cboProductType.setEnabled(true);
                    txtParticulars.setEnabled(true);
                    cboTransType.setEnabled(true);
                    setButtonEnableDisable(false);
                }
            }
        }
    }//GEN-LAST:event_tblTransDetailsMousePressed
    
    private void resetFields()
    {
     txtDebitAccNo.setText("");
     lblCustomerNameValue.setText("");
    
    }
    private void btnSaveTxDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTxDetailsActionPerformed
       
        try{
            // Added By Jeffin John on 20/01/2015 for Mantis ID :10026
            String prodTypeHere = ((ComboBoxModel) cboProductType.getModel()).getKeyForSelected().toString();
            String transTypeHere = ((ComboBoxModel) cboTransType.getModel()).getKeyForSelected().toString();
            String acctNo = CommonUtil.convertObjToStr(txtDebitAccNo.getText());
            double amount = CommonUtil.convertObjToDouble(txtTransactionAmt.getText());
            if (amount <= 0) {
                ClientUtil.showAlertWindow("Transaction amount should be greater than Zero!!!!");
                return;
            } else {
                if (transTypeHere != null && !transTypeHere.equals("") && transTypeHere.equals("TRANSFER")) {
                    //System.out.println("getTransactionMode() :: " + getTransactionMode());
                    if (getSourceScreen() != null && !getSourceScreen().equals("")
                            && (getSourceScreen().equals("TERM_DEPOSIT") || getSourceScreen().equals("MULTIPLE_TERM_DEPOSIT")
                            || (getSourceScreen().equals("LOAN_ACT_CLOSING") && getTransactionMode().equalsIgnoreCase("CREDIT")) || getSourceScreen().equals("MDS_APPLICATION")
                            || getSourceScreen().equals("MDS_RECEIPT") || getSourceScreen().equals("GDS_RECEIPT")|| getSourceScreen().equals("LOCKER_ISSUE")
                            || getSourceScreen().equals("LOCKER_SURRENDER") || getSourceScreen().equals("RENT_REGISTER")
                            || getSourceScreen().equals("RENTTRANS") || getSourceScreen().equals("MDS_MEMBER_RECEIPT") || getSourceScreen().equals("Loan_Register"))) {
                      
                        
                        if(getSourceScreen().equals("MDS_RECEIPT")){
                            amount = amount - getMdsWaiveAmt();
                            System.out.println("amount - mds receiept:: " + amount);
                        }
                        
                        if (prodTypeHere != null && !prodTypeHere.equals("") && prodTypeHere.equals("OA")) {
                            checkValidation(prodTypeHere, transTypeHere, acctNo, amount);
                        }
                        if (prodTypeHere != null && !prodTypeHere.equals("") && prodTypeHere.equals("SA")) {
                            checkSuspanceValidation(prodTypeHere, transTypeHere, acctNo, amount);
                        }
                    }
                    if (getSourceScreen() != null && getSourceScreen().equals("SHARE_SCREEN")) {
                        if(!getTransactionMode().equals((CommonConstants.CREDIT))){
                        if (prodTypeHere != null && !prodTypeHere.equals("") && prodTypeHere.equals("OA")) {
                            checkValidation(prodTypeHere, transTypeHere, acctNo, amount);
                        }
                        if (prodTypeHere != null && !prodTypeHere.equals("") && prodTypeHere.equals("SA")) {
                            checkSuspanceValidation(prodTypeHere, transTypeHere, acctNo, amount);
                        }
                        }
                    }
                }
                
                if (transTypeHere != null && !transTypeHere.equals("") && transTypeHere.equals("CASH")) {
                    if (getSourceScreen() != null && (getSourceScreen().equals("MDS_RECEIPT") || getSourceScreen().equals("MDS_MEMBER_RECEIPT"))) {
                        double receiptLimit = 0.0;
                        if (TrueTransactMain.CBMSPARAMETERS.containsKey("MSS_CASH_RECEIPT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("MSS_CASH_RECEIPT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("MSS_CASH_RECEIPT_LIMIT")) > 0) {
                            receiptLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("MSS_CASH_RECEIPT_LIMIT"));
                            if (amount > receiptLimit) {
                                ClientUtil.showMessageWindow("Receipt Exceeds Limit !!" + receiptLimit);
                                return;
                            }
                        }
                    } else if (getSourceScreen() != null && (getSourceScreen().equals("MDS_PRIZED_PAYMENT"))) {
                        double paymentLimit = 0.0;
                        if (TrueTransactMain.CBMSPARAMETERS.containsKey("MSS_CASH_PAYMENT_LIMIT") && TrueTransactMain.CBMSPARAMETERS.get("MSS_CASH_PAYMENT_LIMIT") != null && CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("MSS_CASH_PAYMENT_LIMIT")) > 0) {
                            paymentLimit = CommonUtil.convertObjToDouble(TrueTransactMain.CBMSPARAMETERS.get("MSS_CASH_PAYMENT_LIMIT"));
                            if (amount > paymentLimit) {
                                ClientUtil.showMessageWindow("Payment Exceeds Limit !!" + paymentLimit);
                                return;
                            }
                        }
                    }
                }
                
            }
            //End of the code
            //Added by sreekrishnan 
           if (getSourceScreen().equals("SHARE_SCREEN")) {
               String transType = ((ComboBoxModel)cboTransType.getModel()).getKeyForSelected().toString(); 
               //System.out.println("getTransactionMode() : " + getTransactionMode());
               if(!getTransactionMode().equals((CommonConstants.CREDIT))){
                  checkShareWithdrawalValidation(acctNo,amount); 
               }               
           }
            if((getSourceScreen().equals("INDEND_REGISTER")) || (getSourceScreen().equals("LOAN_ACT_CLOSING")) ){  
                double totAmt = 0.0;
                if(tblTransDetails.getRowCount()>0){
                    String transType = ((ComboBoxModel)cboTransType.getModel()).getKeyForSelected().toString(); 
                    //System.out.println("transType() : " + transType);
                    for(int i=0;i<tblTransDetails.getRowCount();i++){ 
                        totAmt = totAmt + CommonUtil.convertObjToDouble(tblTransDetails.getValueAt(i, 1)).doubleValue();
                        //System.out.println("totAmt()totAmt : " + totAmt);
                        //System.out.println("CommonUtil.convertObjToStr(tblTransDetails.getValueAt(i, 1)) : " + CommonUtil.convertObjToStr(tblTransDetails.getValueAt(i, 1)));
                        if(transType.equals("CASH")||CommonUtil.convertObjToStr(tblTransDetails.getValueAt(i, 1)).equals("Cash")){
                            ClientUtil.showAlertWindow("Cash Transaction not allowed for multiple Transaction!!"); 
                            cboTransType.setSelectedItem(" ");
                            return;
                    }   }                
                }        
            }
            
            
             if (getSourceScreen() != null && !getSourceScreen().equals("")
                            && (getSourceScreen().equals("ACT_CLOSING"))){
                 if(getSourceAccountNumber().equals(txtDebitAccNo.getText())){
                     ClientUtil.showMessageWindow("Amount cannot be transfered to closing account number!!"); 
                     txtDebitAccNo.setText("");
                     lblCustomerNameValue.setText("");
                     return;
                 }
             }
            
            
             //added by anju 5/5/14
             if(getSourceScreen().equals("BORROW_DISBURSE")&& (totAmount==0)) {
                
               ClientUtil.showAlertWindow("Total Amount cannot be empty");
               txtTransactionAmt.setText(""); 
               cboTransType.setSelectedItem(" ");
               return;
                }
             
            String prodType=((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
            String transType = ((ComboBoxModel)cboTransType.getModel()).getKeyForSelected().toString();
            //System.out.println("transType"+transType);
            setCallingTransType(transType);
            setCallingTransProdType(prodType);
            setCallingTransAcctNo(txtDebitAccNo.getText());
            //System.out.println(prodType);
             if (prodType.equals("TD") && (getSourceScreen().equals("MDS_PRIZED_PAYMENT"))) {
                  if(observable.getSelectedBranchID()!=null && !ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())){
                      ClientUtil.displayAlert("Inter branch not allowed");
                      txtDebitAccNo.setText("");
                      return;
                  }
                 
             }
            if (prodType.equals("TL")) {
                checkDocumentDetail();
            }
            //Added by Suresh R on 26-Aug-2019, Validating for Available balance in Debit Account Ref. By Jithesh
            if (getSourceScreen().equals("KCC_RENEWAL") && transType.equals("TRANSFER") && !prodType.equals("GL") && !prodType.equals("TD")) {
                if (!prodType.equals("AD") && CommonUtil.convertObjToDouble(transDetails.getAvailableBalance()).doubleValue()
                        < CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue()) {
                    ClientUtil.showMessageWindow("Insufficient Balance in Debit Account !!!");
                    return;
                } else if (prodType.equals("AD") && CommonUtil.convertObjToDouble(transDetails.getAvBalance()).doubleValue()
                        < CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue()) {
                    ClientUtil.showMessageWindow("Insufficient Balance in Debit Account !!!");
                    return;
                }
            }
            
            //The following code added by Kannan AR on 21-Apr-2017, for validating Available balance in Debit account ref. abi 
            if (getSourceScreen().equals("RTGS_REMITTANCE") && transType.equals("TRANSFER") && getTransactionMode().equals("DEBIT") && !prodType.equals("GL") && !prodType.equals("TD") && getCallingTransAcctNo().length() > 0) {
                if (prodType.equals("SA")) {
                    boolean negYN = suspenseNegativeValue(prodType,acctNo);
                    if (!negYN && CommonUtil.convertObjToDouble(transDetails.getCBalance()).doubleValue()
                            < CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue()) {
                        ClientUtil.showMessageWindow("Insufficient Balance in suspense account !!!");
                        return;
                    }
                }else{
                if (!prodType.equals("AD") && CommonUtil.convertObjToDouble(transDetails.getAvailableBalance()).doubleValue()
                        < CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue()) {
                    ClientUtil.showMessageWindow("Insufficient Balance in Debit Account(SB/CA...)!!!");
                    return;
                } else if (prodType.equals("AD") && CommonUtil.convertObjToDouble(transDetails.getAvBalance()).doubleValue()
                        < CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue()) {
                    ClientUtil.showMessageWindow("Insufficient Balance in Debit Account(OD...)!!!");
                    return;
                }
                
                   // Added by Nithya for KD-3883
                   if (prodType.equals("OA")) {
                        HashMap availableBalCheckMap = new HashMap();
                        availableBalCheckMap.put("ACT_NUM", txtDebitAccNo.getText());
                        List availableBalLst = ClientUtil.executeQuery("getOABalanceForStanding", availableBalCheckMap);
                        if (availableBalLst != null && availableBalLst.size() > 0) {
                            HashMap balMap = (HashMap) availableBalLst.get(0);
                            if (balMap.containsKey("TOTAL_BALANCE") && null != balMap.get("TOTAL_BALANCE") && !balMap.get("TOTAL_BALANCE").equals("")) {
                                Double availableActBal = CommonUtil.convertObjToDouble(balMap.get("TOTAL_BALANCE"));
                                if (availableActBal < CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue()) {
                                    int yesNo = ClientUtil.confirmationAlert("Minimum balance not maintained, Do you want to continue?");
                                    if(yesNo != 0){
                                        return;
                                    }
                                }
                            }
                        }
                    } 
                
                
            }
            }
            
            //String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTransaction);
            String mandatoryMessage = "" ;
            mandatoryMessage+=validateCashSelection();
            if(CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue() <= 0){
                mandatoryMessage+=resourceBundle.getString("transactionAmt");
            }
             if (getSourceScreen().equals("SHG_TRANSACTION")){
                 double accountAvailBalance= new Double(getAccount_AvailBalance()).doubleValue();
                 
                 String transAmt=CommonUtil.convertObjToStr(txtTransactionAmt.getText());
                 double transAmtDoubleValue=0;
                 if(transAmt.length()>0){
                     transAmtDoubleValue= new Double(transAmt).doubleValue();
                 }else
                     transAmtDoubleValue=new Double("0.00").doubleValue();
                 
                 if(transAmtDoubleValue>accountAvailBalance){
                     ClientUtil.showMessageWindow("Transaction Amount Should not Exceed Available Balance!!!");
                     return;
                 }
             }
            if(!txtDebitAccNo.getText().equals("") && txtDebitAccNo.getText().length() >0 && 
            (getSourceScreen().equals("DRF TRANSACTION") || getSourceScreen().equals("DRF RECOVERY") || 
            getSourceScreen().equals("SHARE_SCREEN") || getSourceScreen().equals("SHARE_DIVIDEND_PAYMENT") ||
            getSourceScreen().equals("MDS_APPLICATION") || /*getSourceScreen().equals("MDS_PRIZED_PAYMENT") || */
            getSourceScreen().equals("MDS_MEMBER_RECEIPT") || 
            /*getSourceScreen().equals("LOAN_ACT_CLOSING") ||*/ getSourceScreen().equals("ACT_CLOSING") ||
            /*getSourceScreen().equals("TERM_DEPOSIT") || getSourceScreen().equals("MULTIPLE_TERM_DEPOSIT") ||*/
            getSourceScreen().equals("RENTTRANS") || getSourceScreen().equals("RENT_REGISTER"))){
                if(observable.getSelectedBranchID()!=null && !ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())){
                    Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(observable.getSelectedBranchID());
                    Date currentDate = (Date) currDt.clone();
                    //System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                    if(selectedBranchDt == null){
                        ClientUtil.displayAlert("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed");
                        txtDebitAccNo.setText("");
                        return;
                    }else if(DateUtil.dateDiff(currentDate, selectedBranchDt)!=0){
                        ClientUtil.displayAlert("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed");
                        txtDebitAccNo.setText("");
                        return;
                    }else {
                        System.out.println("Continue for interbranch trasactions ...");
                    }
                }
            }
            
            // KD 196
            if (!txtDebitAccNo.getText().equals("") && txtDebitAccNo.getText().length() > 0
                        && (getSourceScreen().equals("LOAN_ACT_CLOSING") || getSourceScreen().equals("MDS_PRIZED_PAYMENT"))) {
                System.out.println("ProxyParameters.BRANCH_ID :: "+ ProxyParameters.BRANCH_ID);
                System.out.println("observable.getSelectedBranchID() :: " + observable.getSelectedBranchID());
                    if (observable.getSelectedBranchID() != null && !ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())) {
                        Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(observable.getSelectedBranchID());
                        Date currentDate = (Date) currDt.clone();    
                        System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
                        if (selectedBranchDt == null) {
                            ClientUtil.showAlertWindow("BOD is not completed for the selected branch " + "\n" + "Interbranch Transaction Not allowed");
                            txtDebitAccNo.setText("");
                            return;
                        } else if (DateUtil.dateDiff(currentDate, selectedBranchDt) < 0) {
                            ClientUtil.showAlertWindow("Application Date is different in the Selected branch " + "\n" + "Interbranch Transaction Not allowed");
                            txtDebitAccNo.setText("");
                            return;
                        } else {
                            System.out.println("Continue for interbranch trasactions ...");
                        }
                    }
                }
            
            if (getSourceScreen().equals("SHARE_SCREEN") && transType.equals("TRANSFER") && prodType.equalsIgnoreCase("TL") && !(txtDebitAccNo.getText().equals("")) && txtDebitAccNo.getText().length() > 0) { // Added by nithya on 21-12-2019 for KD-1061
                double totalLoanBalAmount = 0.0;
                double debitAmt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
                HashMap actMap = new HashMap();
                actMap.put("ACT_NUM", txtDebitAccNo.getText());
                actMap.put("CURRDATE", currDt.clone());
                List creditList = ClientUtil.executeQuery("getTotalLoanBalance", actMap);
                if (creditList != null && creditList.size() > 0) {
                    actMap = (HashMap) creditList.get(0);
                    if (actMap.containsKey("TOTAL_LOAN_BALANCE") && actMap.get("TOTAL_LOAN_BALANCE") != null) {
                        totalLoanBalAmount = CommonUtil.convertObjToDouble(actMap.get("TOTAL_LOAN_BALANCE"));
                    }
                }
                if(debitAmt > totalLoanBalAmount){
                    ClientUtil.showMessageWindow("withdrawing amount exceeds loan balance amount (" + totalLoanBalAmount + ")");
                    return;
                }
            }
            
            //            if (getSourceScreen().equals("LOAN_ACT_CLOSING")||getSourceScreen().equals("REMITISSUE")){
            //                 double avbal=0.0;
            //                    double shadowDeb=0.0;
            //                     double enteredAmt= CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
            //                     ArrayList tableVal=transDetails.getTblDataArrayList();
            //                     System.out.println("tableVal"+tableVal);
            //                     for(int k=0;k<tableVal.size();k++){
            //                ArrayList balList=((ArrayList)tableVal.get(k));
            //                if(balList.get(0).equals("Available Balance")){
            //                    String str= CommonUtil.convertObjToStr(balList.get(1));
            //                    str = str.replaceAll(",","");
            //                    avbal=CommonUtil.convertObjToDouble(str).doubleValue();
            //                }
            //                if(balList.get(0).equals("Shadow Debit")){
            //                    String shawdowStr= CommonUtil.convertObjToStr(balList.get(1));
            //                    shawdowStr = shawdowStr.replaceAll(",","");
            //                    shadowDeb=CommonUtil.convertObjToDouble(shawdowStr).doubleValue();
            //                }
            //            }
            //                    if(enteredAmt>(avbal-shadowDeb)){
            //                        int c = ClientUtil.confirmationAlert("Entered Amount Exceeds The Available Balance,Do you want to continue?");
            //                        int d= 0;
            //                        if(c!=d)
            //                            return;
            //                    }
            //
            //            }
            String instType = "";
            if(getSourceScreen().equals("DEPOSITS") && prodType.equals("RM"))
                instType = ((ComboBoxModel)cboInstrumentType.getModel()).getKeyForSelected().toString();
            
            if (mandatoryMessage.length() > 0){
                displayAlert(mandatoryMessage);
            }else if(getSourceScreen().equals("DEPOSITS") && prodType.equals("TD") && txtDebitAccNo.getText().length() == 0){
                ClientUtil.displayAlert("AccNo should not be empty!!!");
                return;
            }else if(getSourceScreen().equals("DEPOSITS") && prodType.equals("RM") && (instType.equals("") || instType.equalsIgnoreCase("voucher"))){
                ClientUtil.displayAlert("Voucher should not select!!!");
                return;
            }else {
                if(checkForCashLmt()){
                    ClientUtil.showAlertWindow("Amount exceeds Cash limit cannot perform Cash Transaction");
                    //}else if(cashLimitForDeposits()){
                    //ClientUtil.showAlertWindow("Amount is exceeding for Cash limit");
                }else if(transferNewDeposit()){
                    ClientUtil.showAlertWindow("Transaction Amount Exceeding For New Deposit Amount");
                    txtTransactionAmt.setEnabled(true);
                    return;
                    //                }else if(checkForDepositCashLmt()){
                    //                    ClientUtil.showAlertWindow("Amount exceeds Cash limit cannot perform Cash Transaction");
                    //                    return;
                }else{
                    updateOBFields();
                    if((tabletransactionMousePressed == false) && ((getCallingStatus().equals("REMIT_DUP")) || (getCallingStatus().equals("REMIT_REV")))){
                        isRemitDup = true;
                        //                        isRemitRev = true;
                    }
                    observable.saveTransactionDetails(tabletransactionMousePressed,getSelectRow(),isRemitDup, tblTransDetails.getRowCount());
                    observable.resetForm();
                    //                    tabletransactionMousePressed = false;
                    commonReset();
                    //                    if(((getSourceScreen().equals("REMITISSUE")) || (getSourceScreen().equals("REMITPAYMENT"))) && (getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT)){
                    //                        btnNewTxDetails.setEnabled(true);
                    //                        btnSaveTxDetails.setEnabled(true);
                    //                    }
                    if((getSourceScreen().equals("REMITISSUE")) && (getCallingUiMode() == ClientConstants.ACTIONTYPE_NEW)){
                        if((transType.equalsIgnoreCase("TRANSFER")) && (prodType.equalsIgnoreCase("GL"))){
                            btnNewTxDetails.setEnabled(true);
                        }else{
                            btnNewTxDetails.setEnabled(false);
                        }
                        //                        btnSaveTxDetails.setEnabled(true);
                    }
                    if((getSourceScreen().equals("ACT_CLOSING")) && (getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT)){
                        btnNewTxDetails.setEnabled(false);
                        btnSaveTxDetails.setEnabled(false);
                    }
                    if((getCallingStatus().equals("REMIT_DUP")) || (getCallingStatus().equals("REMIT_REV"))){
                        //                        remittanceIssue.setDuplicationEnableDisable(true);
                        if((remittanceIssue.getRemittanceIssueOB().isDupInEdit()) || (remittanceIssue.getRemittanceIssueOB().isRevInEdit())){
                            btnNewTxDetails.setEnabled(false);
                            btnSaveTxDetails.setEnabled(false);
                        }
                        remittanceIssue.setDupChrAndTaxEnableDisable(false);
                        remittanceIssue.setRevChrAndTaxEnableDisable(false);
                    }
                    
                    if(isRemitDup){
                        //                        remittanceIssue.setDuplicationEnableDisable(false);
                    }
                    isRemitDup = false;
                    observable.ttNotifyObservers();
                    update(null, null);
                }
            }
        }catch(Exception E){E.printStackTrace();}
       btnDeleteTxDetails.setEnabled(true); 
        //System.out.println("getSourceScreen()============== :"+getSourceScreen());
        if (getSourceScreen().equals("DEPOSITS")){
            DepositClosingUI objDepUi = (DepositClosingUI) getParantUI();
            if(objDepUi!=null)
            objDepUi.btnSave.requestFocus();
        }
        if (getSourceScreen().equals("TERM_DEPOSIT")){
            TermDepositUI objTrmDepUi = (TermDepositUI) getParantUI();
            objTrmDepUi.btnSave.requestFocus();
        }
        if (getSourceScreen().equals("DIRECTORBOARD")){
            DirectorBoardUI objDirUi = (DirectorBoardUI) getParantUI();
            if(objDirUi!=null)
            objDirUi.btnSave.requestFocus();
        }         
    }//GEN-LAST:event_btnSaveTxDetailsActionPerformed
   
    public void enableDisableForActClosing(boolean value) {
        ClientUtil.enableDisable(panLeftSubTxDetails,value);
        ClientUtil.enableDisable(panRightSubTxDetails,value);
        ClientUtil.enableDisable(panbtnTxDetails,value);
    }
    
    private void checkDocumentDetail()throws Exception {
        String AccNo="";
        if(txtDebitAccNo.getText().lastIndexOf("_")!=(-1))
            AccNo = txtDebitAccNo.getText().substring(0,txtDebitAccNo.getText().lastIndexOf("_"));//.lastIndexOf("_")
        AccNo=txtDebitAccNo.getText();
        //System.out.println(AccNo);
        List lst = observable.getDocumentDetailses("getSelectTermLoanDocumentTO",AccNo);
        String str="";
        //System.out.println(lst);
        String doc_form_no=null;
        String is_submited=null;
        HashMap hash=new HashMap();
        //System.out.println("### lst in UI : "+lst);
        if(lst !=null)
            if (lst.size()>0)
                for(int i=0;i<lst.size();i++){
                    hash=(HashMap)lst.get(i);
                    is_submited=hash.get("IS_SUBMITTED")==null ? "N" : (String) hash.get("IS_SUBMITTED");
                    //System.out.println("### is_submited : "+is_submited);
                    doc_form_no=(String)hash.get("DOC_DESC");
                    if(is_submited!=null)
                        if(!is_submited.equals("Y")) {
                            str=str+doc_form_no+"\n";
                        }
                }
        //System.out.println("welcome bugcorrection"+str);
        if(str.length()>0)
            ClientUtil.displayAlert(str+"notsubmited");
    }
    private void commonReset(){
        tabletransactionMousePressed = false ;
        ClientUtil.enableDisable(this, false);
        ClientUtil.enableDisable(panDebitAccHead, false);
        setButtonEnableDisable(true);
        btnDebitAccNo.setEnabled(false);
        btnTransProductId.setEnabled(false);
        btnSaveTransactionDetailsFlag = true;
    }
    
    private void depositsEditMode(){
        if (getSourceScreen().equals("DEPOSITS")) {
            btnSaveTxDetails.setEnabled(true);
            String transType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
            double transAmt = CommonUtil.convertObjToDouble(getCallingAmount()).doubleValue();
            HashMap prodMap = new HashMap();
            prodMap.put(PROD_ID,getCallingProdID());
            List lst = ClientUtil.executeQuery("getLimitAmountForDepProd", prodMap);
            if(lst!=null && lst.size()>0){
                prodMap = (HashMap)lst.get(0);
                double limitAmt = CommonUtil.convertObjToDouble(prodMap.get("MAX_AMT_CASH")).doubleValue();
                totalAmt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
                //System.out.println("limitAmt:"+limitAmt +"transAmt:"+transAmt);
                transactionType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
                if((totalAmt > limitAmt)){
                    cboTransType.setEnabled(false);
                    cboProductType.setEnabled(true);
                    btnTransProductId.setEnabled(true);
                    btnDebitAccNo.setEnabled(true);
                    txtDebitAccNo.setEnabled(true);
                }else{
                    cboTransType.setEnabled(true);
                }
                //                if (transactionType.equals("CASH")) {
                //                    cboInstrumentType.setEnabled(false);
                //                    tdtChequeDate.setEnabled(false);
                //                    ClientUtil.enableDisable(cPanel1, false);
                //                    btnTransProductId.setEnabled(false);
                //                    btnDebitAccNo.setEnabled(false);
                //                }else{
                //                    cboInstrumentType.setEnabled(true);
                //                    tdtChequeDate.setEnabled(true);
                //                    ClientUtil.enableDisable(cPanel1, true);
                //                    btnTransProductId.setEnabled(true);
                //                    btnDebitAccNo.setEnabled(true);
                //                }
                double cashLmt = CommonUtil.convertObjToDouble(lst.get(0)).doubleValue();
                txtTransProductId.setText(observable.getTxtTransProductId());
                if((getCallingUiMode() == ClientConstants.ACTIONTYPE_AUTHORIZE) || (getCallingUiMode() == ClientConstants.ACTIONTYPE_REJECT)){
                    ClientUtil.enableDisable(panLeftSubTxDetails, false);
                    ClientUtil.enableDisable(panRightSubTxDetails, false);
                    ClientUtil.enableDisable(panbtnTxDetails, false);
                    btnTransProductId.setEnabled(false);
                    btnDebitAccNo.setEnabled(false);
                    btnSaveTxDetails.setEnabled(false);
                    btnTransProductId.setEnabled(false);
                    btnDebitAccNo.setEnabled(false);
                }else{
                    String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
                    //System.out.println("$$$$$$$$prodType :"+prodType);
                    if(transactionType.equals("CASH")){
                        btnTransProductId.setEnabled(false);
                        btnDebitAccNo.setEnabled(false);
                        txtDebitAccNo.setEditable(false);
                    }else if(transactionType.equals("TRANSFER")){
                        ClientUtil.enableDisable(panRightSubTxDetails, true);
                        if(prodType.equals("GL")){
                            txtTransProductId.setText("");
                            btnTransProductId.setEnabled(false);
                            btnDebitAccNo.setEnabled(true);
                            txtDebitAccNo.setEditable(true);
                        }else if(prodType.equals("RM")){
                            txtDebitAccNo.setEnabled(true);
                            txtDebitAccNo.setEditable(true);
                            btnTransProductId.setEnabled(false);
                            btnDebitAccNo.setEnabled(false);
                            txtChequeNo.setEnabled(false);
                        }
                    }
                }
            }
        }
    }
    
    private void btnDebitAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitAccNoActionPerformed
        String prodType=((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
        //        if(getSourceScreen().equals("DEPOSITS") && prodType.equals("RM")){
        //            popUp(TRANS_PROD);
        //        }else
        if ((cboProductType.getSelectedIndex() > 0 && txtTransProductId.getText().trim().length() > 0)
        || !(prodType.equalsIgnoreCase("GL"))){
            popUp(ACC_NUM);
        } else{
            popUp(TRANS_PROD);
        }
        //System.out.println("Screen"+getSourceScreen());
    }//GEN-LAST:event_btnDebitAccNoActionPerformed
    
    private void btnTransProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransProductIdActionPerformed
        if (cboProductType.getSelectedIndex() > 0)
            popUp(TRANS_PROD);
    }//GEN-LAST:event_btnTransProductIdActionPerformed
    
    public void updateTransactions() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
            setCallingParams();
    }
    private void getMaturedDetails(String transType){
        String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
        if (getSourceScreen().equals("DEPOSITS")) {
            //System.out.println("$$$$$$$$prodType :"+prodType);
            if(!getCallingClosingType().equals(CommonConstants.TRANSFER_OUT_CLOSURE)){
                if(transType.equals("TRANSFER")){
                    if(prodType.equals("GL")){
                                  HashMap maturedHeadMap = new HashMap();
                                  maturedHeadMap.put(PROD_ID,getCallingProdID());
                                  List lst = ClientUtil.executeQuery("getMaturedAccountHead",maturedHeadMap);
                                  if(lst!=null && lst.size()>0){
                                      maturedHeadMap = (HashMap)lst.get(0);
                                      if(maturedHeadMap !=null && CommonUtil.convertObjToStr(maturedHeadMap.get("TRANS_MATURED_DEP")).equals("Y") &&
                                      maturedHeadMap.get("MATURITY_DEPOSIT")!=null){
                                          HashMap whereMap =new HashMap();
                                          whereMap.put("DEPOSITNO",getCallingLtdDepositNo());
                                          whereMap.put("DEPOSITSUBNO","1");
                                         List maturityDtList=ClientUtil.executeQuery("getDepositParams", whereMap);
                                         if(maturityDtList !=null && maturityDtList.size()>0){
                                             whereMap=(HashMap)maturityDtList.get(0);
                                            Date addMatDt=DateUtil.addDays((Date)whereMap.get("MATURITY_DT"),
                                             CommonUtil.convertObjToInt(maturedHeadMap.get("AFTER_NO_DAYS")));
                                             
                                             if(DateUtil.dateDiff(addMatDt,(Date) currDt.clone())<0){
                                                 ClientUtil.showMessageWindow("Transfer to Maturity Account Head on After"+CommonUtil.convertObjToStr(addMatDt));
                                                 return;
                                             }
                                         }
//                                          if(CommonUtil.convertObjToStr(whereMap.get("TRANS_MATURED_DEP")))
                                          txtDebitAccNo.setText(CommonUtil.convertObjToStr(maturedHeadMap.get("MATURITY_DEPOSIT")));
                                          lblCustomerNameValue.setText(CommonUtil.convertObjToStr(maturedHeadMap.get("MATURITY_DEPOSIT")));
                                          cboInstrumentType.setSelectedItem("Voucher");
                                          tdtChequeDate.setDateValue(CommonUtil.convertObjToStr(currDt));
                                          cboInstrumentType.setEnabled(false);
                                          txtTransProductId.setEnabled(false);
                                          tdtChequeDate.setEnabled(false);
                                          txtDebitAccNo.setEnabled(false);
                                          btnDebitAccNo.setEnabled(false);
                                          txtTransProductId.setText("");
                                          btnTransProductId.setEnabled(false);
                                      }
                                  }
                                  else{
                                      ClientUtil.displayAlert("Account Head is not set...");
                        btnTransProductId.setEnabled(false);
                        txtDebitAccNo.setEnabled(true);
                        btnDebitAccNo.setEnabled(true);
                        txtTransProductId.setText("");
                        txtDebitAccNo.setText("");
                        lblCustomerNameValue.setText("");
                                  }
                    }else if(prodType.equals("OA")){
                        HashMap operativeMap = new HashMap();
                        operativeMap.put("DEPOSIT_NO",getCallingAccNo());
                        List lst = ClientUtil.executeQuery("getCustomeridFoeDep", operativeMap);
                        if(lst !=null && lst.size()>0){
                            operativeMap = (HashMap)lst.get(0);
                            lst = ClientUtil.executeQuery("getCustomerAccNoFoeDep",operativeMap);
                            if(lst !=null && lst.size()>0){
                                operativeMap = (HashMap)lst.get(0);
                                txtTransProductId.setText(CommonUtil.convertObjToStr(operativeMap.get(PROD_ID)));
                                observable.setTxtTransProductId(CommonUtil.convertObjToStr(operativeMap.get(PROD_ID)));
                                txtDebitAccNo.setText(CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                                lblCustomerNameValue.setText(CommonUtil.convertObjToStr(operativeMap.get("ACCT_NAME")));
                                //                            panRightSubTxDetails.setVisible(false);
                                txtTransProductId.setEnabled(true);
                                btnTransProductId.setEnabled(true);
                                btnDebitAccNo.setEnabled(true);
                                btnTransProductId.setEnabled(true);
                                txtDebitAccNo.setEnabled(true);
                            }else{
                                txtDebitAccNo.setText("");
                                lblCustomerNameValue.setText("");
                                txtTransProductId.setText("");
                                cboInstrumentType.setEnabled(true);
                                txtTransProductId.setEnabled(true);
                                tdtChequeDate.setEnabled(true);
                                txtDebitAccNo.setEnabled(true);
                                btnDebitAccNo.setEnabled(true);
                                btnTransProductId.setEnabled(true);
                            }
                        }else{
                            txtDebitAccNo.setText("");
                            lblCustomerNameValue.setText("");
                            txtTransProductId.setText("");
                            cboInstrumentType.setEnabled(true);
                            txtTransProductId.setEnabled(true);
                            tdtChequeDate.setEnabled(true);
                            txtDebitAccNo.setEnabled(true);
                            btnDebitAccNo.setEnabled(true);
                            btnTransProductId.setEnabled(true);
                        }
                    }else if(prodType.equals("TL") || prodType.equals("AD")){
                        txtDebitAccNo.setText("");
                        lblCustomerNameValue.setText("");
                        txtTransProductId.setText("");
                        cboInstrumentType.setEnabled(true);
                        txtTransProductId.setEnabled(true);
                        tdtChequeDate.setEnabled(true);
                        txtDebitAccNo.setEnabled(true);
                        btnDebitAccNo.setEnabled(true);
                        btnTransProductId.setEnabled(true);
                    }else if(prodType.equals("TD")){
                        //                    ClientUtil.displayAlert("Not Possible to Credit deposit itself");
                        txtTransProductId.setText("");
                        txtDebitAccNo.setText("");
                        lblCustomerNameValue.setText("");
                        //                    btnTransProductId.setEnabled(false);
                        btnDebitAccNo.setEnabled(true);
                        txtDebitAccNo.setEnabled(false);
                    }else if(prodType.equals("TL")||prodType.equals("OA")||prodType.equals("AD")){
                        txtTransProductId.setText("");
                        txtDebitAccNo.setText("");
                        lblCustomerNameValue.setText("");
                        lblDebitAccNo.setText("Account No.");
                      //  lblCustomerName.setText("Customer Name");
                        btnTransProductId.setEnabled(true);
                        btnDebitAccNo.setEnabled(true);
                    }else if(prodType.equals("GL")){
                        txtTransProductId.setText("");
                        txtDebitAccNo.setText("");
                        lblCustomerNameValue.setText("");
                        lblDebitAccNo.setText("Account Head");
                      //  lblCustomerName.setText("A/C Head Desc");
                        btnTransProductId.setEnabled(false);
                        btnDebitAccNo.setEnabled(true);
                    }else if(prodType.equals("RM")){
                        txtTransProductId.setText("");
                        txtDebitAccNo.setText("");
                        lblCustomerNameValue.setText("");
                        lblDebitAccNo.setText("Favouring Name");
                       // lblCustomerName.setText("Favouring Name");
                        btnTransProductId.setEnabled(false);
                        btnDebitAccNo.setEnabled(false);
                        txtDebitAccNo.setEnabled(true);
                    }
                }else{
                    txtDebitAccNo.setText("");
                    cboInstrumentType.setSelectedItem("");
                    tdtChequeDate.setDateValue("");
                }
            }else if(getCallingClosingType().equals(CommonConstants.TRANSFER_OUT_CLOSURE)){
                HashMap transferOutMap = new HashMap();
                transferOutMap.put(PROD_ID,getCallingProdID());
                List lst = ClientUtil.executeQuery("getDepositClosingHeads", transferOutMap);
                if(lst!=null && lst.size()>0){
                    transferOutMap = (HashMap)lst.get(0);
                    txtTransProductId.setEnabled(false);
                    btnTransProductId.setEnabled(false);
                    cboTransType.setEnabled(false);
                    btnDebitAccNo.setEnabled(true);
                    txtDebitAccNo.setText(CommonUtil.convertObjToStr(transferOutMap.get("TRANSFEROUT_ACHD")));
                }
            }
        }
        if (getSourceScreen().equals("ACT_CLOSING")) {
            if(prodType.equals("RM")){
                txtTransProductId.setText("");
                txtDebitAccNo.setText("");
                lblCustomerNameValue.setText("");
                lblDebitAccNo.setText("Favouring Name");
               // lblCustomerName.setText("Favouring Name");
                btnTransProductId.setEnabled(false);
                btnDebitAccNo.setEnabled(false);
                txtDebitAccNo.setEnabled(true);
            }
        }
    }
    
    public void displayMode(){
        txtApplicantsName.setEnabled(false);
        cboTransType.setEnabled(false);
        txtTransactionAmt.setEnabled(false);
        cboProductType.setEnabled(false);
        txtTransProductId.setEnabled(false);
        txtDebitAccNo.setEnabled(false);
        cboInstrumentType.setEnabled(false);
        txtChequeNo.setEnabled(false);
        txtChequeNo2.setEnabled(false);
        tdtChequeDate.setEnabled(false);
        btnDebitAccNo.setEnabled(false);
        txtParticulars.setEnabled(false);
    }
    
    private boolean cashLimitForDeposits(){
        boolean value = false;
        if (getSourceScreen().equals("DEPOSITS")) {
            if(!getCallingClosingType().equals(CommonConstants.TRANSFER_OUT_CLOSURE)){
                String transType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
                txtTransProductId.setText(observable.getTxtTransProductId());
                double transAmt = CommonUtil.convertObjToDouble(getCallingAmount()).doubleValue();
                HashMap prodMap = new HashMap();
                prodMap.put(PROD_ID,getCallingProdID());
                List lst = ClientUtil.executeQuery("getLimitAmountForDepProd", prodMap);
                if(lst.size()>0){
                    prodMap = (HashMap)lst.get(0);
                    double limitAmt = CommonUtil.convertObjToDouble(prodMap.get("MAX_AMT_CASH")).doubleValue();
                    totalAmt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
                    //System.out.println("limitAmt for transactionUI :"+limitAmt);
                    //System.out.println("transAmt for transactionUI :"+totalAmt);
                    if (totalAmt >= limitAmt){
                        cboTransType.setSelectedItem(observable.getCbmTransactionType().getDataForKey("TRANSFER"));
                        cboTransType.setEnabled(false);
                    }else{
                        cboTransType.setEnabled(true);
                        cboTransType.setSelectedItem(observable.getCbmTransactionType().getDataForKey("CASH"));
                    }
                    double cashLmt = CommonUtil.convertObjToDouble(lst.get(0)).doubleValue();
                    if(lst != null)
                        if((totalAmt > limitAmt) && (transType.equals("CASH"))){
                            value = true;
                        }else{
                            value = false;
                        }
                }
            }else if (getCallingClosingType().equals(CommonConstants.TRANSFER_OUT_CLOSURE)){
                cboTransType.setSelectedItem(observable.getCbmTransactionType().getDataForKey("TRANSFER"));
                cboProductType.setSelectedItem("General Ledger");
            }
        }
        return value;
    }
    
    public HashMap getAccClosingDetails(HashMap transMap){
        btnNewTxDetailsActionPerformed(null);
        HashMap depositMap = new HashMap();
        HashMap getNameMap = new HashMap();
        String depositNo = null;
        String subNo =null;
        getNameMap.put("LIEN_AC_NO",transMap.get("ACCOUNT NUMBER"));
        List lst = ClientUtil.executeQuery("getDepositNOLien", getNameMap);
        if(lst.size()>0){
            getNameMap = (HashMap)lst.get(0);
            transMap.put("DEPOSIT_NO",getNameMap.get("DEPOSIT_NO"));
            getNameMap.put("ACCT_NUM",transMap.get("ACCOUNT NUMBER"));
            lst = ClientUtil.executeQuery("getLoanCustName", getNameMap);
            if(lst.size()>0){
                getNameMap = (HashMap)lst.get(0);
                txtApplicantsName.setText(CommonUtil.convertObjToStr(getNameMap.get("CONTACT_PERSON")));
                cboProductType.setSelectedItem("Deposits");
                cboTransType.setSelectedItem("Transfer");
                txtTransactionAmt.setText(String.valueOf(transMap.get("AMOUNT")));
                depositNo = CommonUtil.convertObjToStr(transMap.get("DEPOSIT_NO"));
                //System.out.println("###depositNo : "+depositNo);
                getNameMap.put("DEPOSIT_NO",transMap.get("DEPOSIT_NO"));
                lst = ClientUtil.executeQuery("getProdIdForLienDep",getNameMap);
                if(lst.size()>0){
                    getNameMap = (HashMap)lst.get(0);
                    subNo = CommonUtil.convertObjToStr(getNameMap.get("DEPOSIT_SUB_NO"));
                    //System.out.println("###depositNo : "+subNo);
                    String depSubNo = depositNo + "_" + subNo;
                    transMap.put("DEPOSIT_NO",depSubNo);
                    //System.out.println("###transMap : "+transMap);
                    txtDebitAccNo.setText(CommonUtil.convertObjToStr(transMap.get("DEPOSIT_NO")));
                    txtTransProductId.setText(CommonUtil.convertObjToStr(getNameMap.get(PROD_ID)));
                    transMap.put(PROD_ID,getNameMap.get(PROD_ID));
                    lst = ClientUtil.executeQuery("getAccHeadForLienDep", getNameMap);
                    if(lst.size()>0){
                        getNameMap = (HashMap)lst.get(0);
                        cboInstrumentType.setSelectedItem("Voucher");
                        tdtChequeDate.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
                        observable.setDepFlag(true);
                        txtParticulars.setText("LTD Deposit"); //Added By Sathiya 11-03-2014 while closing deposit closing screen loan transaction all details set
                        btnSaveTxDetailsActionPerformed(null);
                    }
                }
            }
        }
        return transMap;
    }
    
    private void getLienDetails(){
        if (getSourceScreen().equals("DEPOSITS")){
            String status = getCallingStatus();
            HashMap accHeadMap = new HashMap();
            double rateOfInt = 0.0;
            double rateDepInt = 0.0;
            double penalInt = 0.0;
            accHeadMap.put("DEPOSIT_NO",getCallingAccNo());
            //System.out.println("*****depositNo : "+getCallingAccNo());
            List lst = ClientUtil.executeQuery("getAccountHeadForLTD", accHeadMap);
            //System.out.println("*****lst : "+lst);
            if(lst.size()>0 && lst!=null){
                accHeadMap = (HashMap)lst.get(0);
                //System.out.println("*****accHeadMap : "+accHeadMap);
                HashMap lienMap = new HashMap();
                lienMap.put("LIEN_AC_NO",accHeadMap.get("LIEN_AC_NO"));
                lst = ClientUtil.executeQuery("getDepositLienStatusLTD", lienMap);
                //System.out.println("*****lst : "+lst);
                if(lst!=null && lst.size()>0){
                    lienMap = (HashMap)lst.get(0);
                    if(accHeadMap.get("BEHAVES_LIKE").equals("LOANS_AGAINST_DEPOSITS")&&
                    !lienMap.get("STATUS").equals("UNLIENED")){
                        HashMap loanMap = new HashMap();
                        loanMap.put("ACCT_NUM",accHeadMap.get("LIEN_AC_NO"));
                        lst = ClientUtil.executeQuery("getLoanAmountForLTDDep",loanMap);
                        if(lst!=null && lst.size()>0){
                            loanMap = (HashMap)lst.get(0);
                            //System.out.println("*****loanMap : "+loanMap);
                            accountClosing = new AccountClosingUI("TermLoan");
                            accountClosing.setViewType(3);
                            accountClosing.statusDep();
                            HashMap maturedHeadMap = new HashMap();
                            depProdId = CommonUtil.convertObjToStr(loanMap.get(PROD_ID));
                            maturedHeadMap.put(PROD_ID,loanMap.get(PROD_ID));
                            lst = ClientUtil.executeQuery("getMaturedAccountHead",maturedHeadMap);
                            if(lst!=null && lst.size()>0){
                                maturedHeadMap = (HashMap)lst.get(0);
                                if(maturedHeadMap !=null && maturedHeadMap.size()>0 && CommonUtil.convertObjToStr(maturedHeadMap.get("TRANS_MATURED_DEP")).equals("Y")){
                                    txtTransProductId.setText(CommonUtil.convertObjToStr(maturedHeadMap.get("MATURITY_DEPOSIT")));
                                    txtDebitAccNo.setText(CommonUtil.convertObjToStr(maturedHeadMap.get("MATURITY_DEPOSIT")));
                                }
                            }
                            HashMap loansFacilityMap = new HashMap();
                            loansFacilityMap.put("ACT_NUM",accHeadMap.get("LIEN_AC_NO"));

                            lst = ClientUtil.executeQuery("getLoansFacilityDet", loansFacilityMap);
                            if(lst!=null && lst.size()>0){
                                loansFacilityMap = (HashMap)lst.get(0);
                            }
                            HashMap lonasBorrowMap = new HashMap();
                            lonasBorrowMap.put("ACT_NUM", accHeadMap.get("LIEN_AC_NO"));
                            lst = ClientUtil.executeQuery("getActDataTL", lonasBorrowMap);
                            if(lst!=null && lst.size()>0){
                                lonasBorrowMap = (HashMap)lst.get(0);
                                //System.out.println("####lonasBorrowMap: "+lonasBorrowMap);
                                HashMap intMainMap = new HashMap();
                                intMainMap.put(PROD_ID, loanMap.get(PROD_ID));
                                intMainMap.put("CATEGORY_ID", lonasBorrowMap.get("CATEGORY"));
                                intMainMap.put("AMOUNT", CommonUtil.convertObjToDouble(lonasBorrowMap.get("AMOUNT")));
                                intMainMap.put("DEPOSIT_DT", lonasBorrowMap.get("CREATE_DT"));
                                intMainMap.put("PRODUCT_TYPE","TL");
                                
                                lst = ClientUtil.executeQuery("icm.getInterestRates", intMainMap);
                                if(lst!=null && lst.size()>0){
                                    intMainMap = (HashMap)lst.get(0);
                                    //System.out.println("####Deposit Interest Closing intMainMap : "+intMainMap);
                                    penalInt = CommonUtil.convertObjToDouble(intMainMap.get("ROI")).doubleValue();
                                    if(!accHeadMap.get("DEPOSIT_BEHAVES_LIKE").equals("") && accHeadMap.get("DEPOSIT_BEHAVES_LIKE").equals("DAILY")){
                                        System.out.println("####Deposit Interest Closing penalInt: "+penalInt);
                                       // rateOfInt = penalInt;  
                                        if (getSourceScreen().equals("DEPOSITS")) { //Added by nithya for KD-3211
                                            rateDepInt = CommonUtil.convertObjToDouble(getCallingDepIntRate()).doubleValue();
                                            rateOfInt = penalInt + rateDepInt;
                                        } else {
                                        rateOfInt = penalInt;                                        
                                        }                                      
                                    }else{
                                        if(getCallingClosingType().equals(CommonConstants.PREMATURE_CLOSURE)){
                                            rateDepInt = CommonUtil.convertObjToDouble(getCallingDepIntRate()).doubleValue();
                                            //System.out.println("####Deposit Interest Closing penalInt: "+penalInt);
                                            rateOfInt = penalInt + rateDepInt;
                                        }else{
                                            rateDepInt = CommonUtil.convertObjToDouble(getCallingDepIntRate()).doubleValue();
                                            //System.out.println("####Deposit Interest Closing penalInt: "+rateDepInt);
                                            rateOfInt = rateDepInt + penalInt;
                                        }
                                    }
                                }
                                HashMap closingMap = new HashMap();
                                closingMap.put("ACCOUNT NUMBER",accHeadMap.get("LIEN_AC_NO"));
                                closingMap.put(PROD_ID,loanMap.get(PROD_ID));
                                closingMap.put("RATE_OF_INT",new Double(rateOfInt));
                                closingMap.put("CREATE_DT",loansFacilityMap.get("MIN(DAY_END_DT)"));
                                closingMap.put("DEPOSIT_CLOSING_DETAILS","DEPOSIT_CLOSING_DETAILS");
                                double transAmt = CommonUtil.convertObjToDouble(getCallingAmount()).doubleValue();
                                closingMap.put("DEPOSIT_AMOUNT",new Double(transAmt));
                                closingMap.put("DEPOSIT_CLOSING_SCREEN","DEPOSIT_CLOSING_SCREEN");
                                if(getCallingClosingType().equals(CommonConstants.PREMATURE_CLOSURE))
                                    closingMap.put("DEPOSIT_PREMATURE_CLOSER","DEPOSIT_PREMATURE_CLOSER");
                                else
                                    closingMap.put("NORMAL_CLOSER","NORMAL_CLOSER");
                                HashMap transMap = new HashMap();
                                transMap.put(PROD_ID,loanMap.get(PROD_ID));
                                transMap.put("ACCOUNT NUMBER",accHeadMap.get("LIEN_AC_NO"));
                                transMap.put("CREATE_DT",loansFacilityMap.get("MIN(DAY_END_DT)"));
                                transMap.put("RATE_OF_INT",new Double(rateOfInt));
                                transMap.put("DEPOSIT_CLOSING_DETAILS","DEPOSIT_CLOSING_DETAILS");
                                HashMap prodMap = accountClosing.transactionActionType(transMap);
                                
                                accountClosing.fillData(closingMap);
                                double interestAmt=0;
                                double balanceAmt = CommonUtil.convertObjToDouble(accountClosing.getBalanceAmt()).doubleValue();
                                chargeAmt = CommonUtil.convertObjToDouble(accountClosing.getChargeAmt()).doubleValue();
                                //System.out.println("callingamount"+transAmt);
                                //System.out.println("balanceAmt :"+balanceAmt);
                                //System.out.println("chargeAmt :"+chargeAmt);
                                 interestAmt=accountClosing.getCalculateInt();
                                //System.out.println("interestAmt :"+interestAmt);
                                setServiceTax_Map(accountClosing.serviceTax_Map);
                            if(balanceAmt<0){
                                balanceAmt=balanceAmt*-1;
								}
                                if(getCallingClosingType().equals(CommonConstants.PREMATURE_CLOSURE)){
                                    interestAmt=accountClosing.getCalculateInt();
//                                    if(interestAmt>0)
//                                        chargeAmt=chargeAmt+interestAmt;
//                                    else
//                                        chargeAmt=chargeAmt+interestAmt;
                                } else{
                                    // The following If Block commented because Interest amount already added in chargeAmt
//                                    if(interestAmt>0)
//                                        chargeAmt=chargeAmt+interestAmt;
//                                    interestAmt = chargeAmt - balanceAmt;
                                }
                                double totalAmt = transAmt - chargeAmt;
                                if(getCallingClosingType().equals(CommonConstants.PREMATURE_CLOSURE)){
                                   totalAmt = transAmt - chargeAmt-interestAmt; 
                                }
                                //System.out.println("totalAmt :"+totalAmt);
//                                setCallingAmount(String.valueOf(chargeAmt));
                                setCallingAccNo(CommonUtil.convertObjToStr(accHeadMap.get("LIEN_AC_NO")));
//                              //  setCallingProdID(CommonUtil.convertObjToStr(loanMap.get(PROD_ID)));comment by abi
                                setCallingStatus(getCallingStatus());
                                String addStr="";
                                double addIntLnAmt=0;
                                    if(getAddIntLoanAmt()!=null&&!getAddIntLoanAmt().equals("")&&!getAddIntLoanAmt().equals("0.0")){
                                        addStr= "\nLoan Additional Int Amount : "+getAddIntLoanAmt();
                                        addIntLnAmt=CommonUtil.convertObjToDouble(getAddIntLoanAmt());
                                    }
                                if(totalAmt<=0){
                                    
                                    if(((transAmt-(balanceAmt+interestAmt+addIntLnAmt)))==0.0){
                                        setExcessLoanAmt("CLOSE");
                                        setClosingDepositStatus("PARTIALLY_CLOSING");
                                        setCallingAmount(String.valueOf(chargeAmt+addIntLnAmt));
                                        ClientUtil.showMessageWindow("Loan Account No : "+accHeadMap.get("LIEN_AC_NO")+
                                        "\nLoan Principal Amount : "+balanceAmt+
                                        "\nRate Of Interest      : "+rateOfInt+
                                        "\nInterest Amount       : "+interestAmt+
                                        "\nLoan Total Amount     : "+(chargeAmt+addIntLnAmt)+
                                        "\nRemaining Amount      : "+(totalAmt-addIntLnAmt)+addStr);
                                        txtTransactionAmt.setText(String.valueOf(totalAmt-addIntLnAmt));
                                        ClientUtil.enableDisable(panLeftSubTxDetails,false);
                                        ClientUtil.enableDisable(panRightSubTxDetails,false);
                                    }else{
                                        if((transAmt-(balanceAmt+interestAmt+addIntLnAmt))<0){
                                            setExcessLoanAmt("DONT_CLOSE");
                                            setClosingDepositStatus("PARTIALLY_CLOSING");
                                            setCallingAmount(String.valueOf(chargeAmt));
//                                            ClientUtil.showMessageWindow("<html>"+"Loan Account No : "+accHeadMap.get("LIEN_AC_NO")+
//                                            "\nLoan Principal Amount : "+balanceAmt+
//                                            "\nRate Of Interest      : "+rateOfInt+
//                                            "\nInterest Amount       : "+interestAmt+
//                                            "\nLoan Total Amount     : "+(balanceAmt+interestAmt)+
//                                            "\nDeposit Amount on Maturity: "+transAmt+
//                                            "\nRemaining Loan Amount      : "+(transAmt - (balanceAmt+interestAmt))* -1+"\n"+
//                                            "<br><br><b>Loan can not be closed...</b>"+"</html>");                                            
                                            ClientUtil.showMessageWindow("Loan Account No : "+accHeadMap.get("LIEN_AC_NO")+
                                            "\nLoan Principal Amount : "+balanceAmt+
                                            "\nRate Of Interest      : "+rateOfInt+
                                            "\nInterest Amount       : "+interestAmt+
                                            "\nLoan Total Amount     : "+(balanceAmt+interestAmt)+
                                            "\nDeposit Amount on Maturity: "+transAmt+
                                            "\nRemaining Loan Amount      : "+(transAmt - (balanceAmt+interestAmt+addIntLnAmt))* -1+addStr+"\n"+" "+
                                            "LOAN CAN NOT BE CLOSED...");
                                            txtTransactionAmt.setText(String.valueOf(totalAmt));
                                            ClientUtil.enableDisable(panLeftSubTxDetails,false);
                                            ClientUtil.enableDisable(panRightSubTxDetails,false);
                                        }else{
                                            setExcessLoanAmt("CLOSE");
                                            setClosingDepositStatus("PARTIALLY_CLOSING");
                                            setCallingAmount(String.valueOf(chargeAmt+addIntLnAmt));
                                            ClientUtil.showMessageWindow("Loan Account No : "+accHeadMap.get("LIEN_AC_NO")+
                                            "\nLoan Principal Amount : "+balanceAmt+
                                            "\nRate Of Interest      : "+rateOfInt+
                                            "\nInterest Amount       : "+interestAmt+
                                            "\nLoan Total Amount     : "+(balanceAmt+interestAmt+addIntLnAmt)+
                                            "\nRemaining Amount      : "+(transAmt - (balanceAmt+interestAmt+addIntLnAmt))+addStr);
                                            txtTransactionAmt.setText(String.valueOf(transAmt - (balanceAmt+interestAmt+addIntLnAmt)));
                                        }
                                    }
                                }else{                                    
                                    setExcessLoanAmt("CLOSE");
                                    setClosingDepositStatus("FULLY_CLOSING");
                                    if(getCallingClosingType().equals(CommonConstants.PREMATURE_CLOSURE)){
                                        interestAmt=accountClosing.getCalculateInt();
                                        if(interestAmt<0)
                                            chargeAmt=chargeAmt+interestAmt;
//                                        else
//                                            chargeAmt=chargeAmt+interestAmt;
                                    }else{
                                        //					if(interestAmt<0){
                                        interestAmt = chargeAmt - balanceAmt;
                                        //                                        }
                                    }
                                    totalAmt = transAmt - chargeAmt;
                                    if (getCallingClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                                        if (interestAmt > 0) {
                                            totalAmt = transAmt - chargeAmt - interestAmt;
                                            chargeAmt = chargeAmt + interestAmt;
                                        }
                                        
                                    }
                                    //System.out.println("chargeAmt 111 :"+chargeAmt);
                                    setCallingAmount(String.valueOf(chargeAmt+addIntLnAmt));
                                    ClientUtil.showMessageWindow("Loan Account No : "+accHeadMap.get("LIEN_AC_NO")+
                                    "\nLoan Principal Amount : "+balanceAmt+
                                    "\nRate Of Interest      : "+rateOfInt+
                                    "\nInterest Amount       : "+interestAmt+
                                    "\nLoan Total Amount     : "+(chargeAmt+addIntLnAmt)+
                                    "\nRemaining Amount      : "+(totalAmt-addIntLnAmt)+addStr);
                                    txtTransactionAmt.setText(String.valueOf(totalAmt-addIntLnAmt));
                                }
                                HashMap updateLoanMap=new HashMap();
                                updateLoanMap.put("EXCESS_AMT",new Double(interestAmt));
                                updateLoanMap.put("LIEN_AC_NO",accHeadMap.get("LIEN_AC_NO"));
                                ClientUtil.execute("updateInterestAmtTL", updateLoanMap);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void getOperativeAccNo(){
        if (getSourceScreen().equals("DEPOSITS") && !getCallingClosingType().equals(CommonConstants.TRANSFER_OUT_CLOSURE)) {
            HashMap operativeMap = new HashMap();
            operativeMap.put("DEPOSIT_NO",getCallingAccNo());
            List lst = ClientUtil.executeQuery("getCustomeridFoeDep", operativeMap);
            if(lst!=null && lst.size()>0){
                operativeMap = (HashMap)lst.get(0);
                lst = ClientUtil.executeQuery("getCustomerAccNoFoeDep",operativeMap);
                if(lst!= null && lst.size()>0){
                    operativeMap = (HashMap)lst.get(0);
                    cboProductType.setSelectedItem("Operative Account");
                    cboTransType.setSelectedItem("Transfer");
                    txtTransProductId.setText(CommonUtil.convertObjToStr(operativeMap.get(PROD_ID)));
                    observable.setTxtTransProductId(CommonUtil.convertObjToStr(operativeMap.get(PROD_ID)));
                    txtDebitAccNo.setText(CommonUtil.convertObjToStr(operativeMap.get("ACT_NUM")));
                    lblCustomerNameValue.setText(CommonUtil.convertObjToStr(operativeMap.get("ACCT_NAME")));
                    //                    panRightSubTxDetails.setVisible(false);
                }
            }
          
        }
        
        if(getSourceScreen().equals("LOCKER_SURRENDER")) {
            HashMap mh=new HashMap();
            mh.put("REMARKS",getCallingAccNo());
           
            List s=ClientUtil.executeQuery("getTransDetails", mh);
            if(s!=null && s.size()>0) {
                mh=null;
                mh=(HashMap)s.get(0);
                String prodid=CommonUtil.convertObjToStr(mh.get("PRODUCT_ID"));
                String protype=CommonUtil.convertObjToStr(mh.get("PROD_TYPE"));
                String  cust=CommonUtil.convertObjToStr(mh.get("CUSTOMER_ID_CR"));
                String cname=CommonUtil.convertObjToStr(mh.get("CUSTOMER_NAME"));
                cboTransType.setSelectedItem("Transfer");
            cboProductType.setSelectedItem("Operative Account");
             txtDebitAccNo.setText(cust);
              lblCustomerNameValue.setText(cname);
               txtTransProductId.setText(prodid);
               //transactionUI.
              
        }
        }
    
        if(getSourceScreen().equals("LOCKER_ISSUE")) {
       
        cboTransType.setSelectedItem("Transfer");
       // cboProductType.setSelectedItem("Operative Account");
             String ptype=getCallingTransProdType();
           cboProductType.setSelectedItem(ptype);
        String pid=getCallingProdID();
        String acno=getCallingTransAcctNo();
           txtTransProductId.setText( pid);
           txtDebitAccNo.setText(acno);
           String cno=getCallingAccNo();
            lblCustomerNameValue.setText(cno);
       
    }       
    if(getSourceScreen().equals("DIRECTORBOARD")) {// nithya
        HashMap directBoardMap = new HashMap();
        //getTransDetailsForDirectorBoardSittingFee
        directBoardMap.put("MEMBER_NO",getCallingAccNo());
        List directBoardLst = ClientUtil.executeQuery("getTransDetailsForDirectorBoardSittingFee", directBoardMap);
        if(directBoardLst != null && directBoardLst.size() > 0 ){
            HashMap directBoardTransMap = (HashMap)directBoardLst.get(0);           
            String transProdid = CommonUtil.convertObjToStr(directBoardTransMap.get("TRANS_PROD_ID"));
            String transProtype = CommonUtil.convertObjToStr(directBoardTransMap.get("TRANS_PROD_TYPE")); 
            if(directBoardTransMap.get("TRANS_ACC_NO") != null && !directBoardTransMap.get("TRANS_ACC_NO").equals("")){
                String transActNo = CommonUtil.convertObjToStr(directBoardTransMap.get("TRANS_ACC_NO"));
                cboTransType.setSelectedItem("Transfer");
                cboProductType.setSelectedItem(((ComboBoxModel) cboProductType.getModel()).getDataForKey(transProtype));
                txtDebitAccNo.setText(transActNo);
                txtTransProductId.setText(transProdid);
                txtTransactionAmt.setText(getCallingAmount());
            }else{
            cboTransType.setSelectedItem("Cash");
           }             
        }else{
            cboTransType.setSelectedItem("Cash");
        }       
    }    
    }
    private void addingRMDeposits(){
        if((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) {
            ComboBoxModel objDepModel = new ComboBoxModel();
            objDepModel.addKeyAndElement(" ", observable.getCbmProductType().getDataForKey(" "));
            objDepModel.addKeyAndElement("OA", observable.getCbmProductType().getDataForKey("OA"));
            objDepModel.addKeyAndElement("AD", observable.getCbmProductType().getDataForKey("AD"));
            objDepModel.addKeyAndElement("TD", observable.getCbmProductType().getDataForKey("TD"));
            //            objDepModel.addKeyAndElement("TL", observable.getCbmProductType().getDataForKey("TL"));
            objDepModel.addKeyAndElement("GL", observable.getCbmProductType().getDataForKey("GL"));
            objDepModel.addKeyAndElement("SA", observable.getCbmProductType().getDataForKey("SA"));
            objDepModel.addKeyAndElement("RM", "Remittance");
            objDepModel.addKeyAndElement("AB", observable.getCbmProductType().getDataForKey("AB"));
            if ((getSourceScreen().equals("DEPOSITS"))) {
                objDepModel.addKeyAndElement("TL", observable.getCbmProductType().getDataForKey("TL"));
            }
            //            objDepModel.addKeyAndElement("RM", observable.getCbmProductType().getDataForKey("RM"));
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductType).getModel())).getKeyForSelected());
            observable.setCbmProductType(objDepModel);
            cboProductType.setModel(observable.getCbmProductType());
            cboProductType.setSelectedItem(((ComboBoxModel)cboProductType.getModel()).getDataForKey(prodType));
        }
    }
    private void btnNewTxDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewTxDetailsActionPerformed

        //added by anju 15/5/2014
        if(getSourceScreen().equals("BORROW_DISBURSE")&& (totAmount==0)) {
         
               ClientUtil.showAlertWindow("Total Amount cannot be empty");
               txtTransactionAmt.setText(""); 
               cboTransType.setSelectedItem(" ");
               return;
               
        }        
       
        ClientUtil.enableDisable(this, true);
        //System.out.println("getsourcescree"+getSourceScreen());
        setButtonEnableDisable(false);
        tabletransactionMousePressed = false;
        whenCashIsSelectedDisable(false);
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.ttNotifyObservers();
        ((com.see.truetransact.clientutil.ComboBoxModel)cboTransType.getModel()).setKeyForSelected(getCallingTransType());
        setExcessLoanAmt("CLOSE");
        setClosingDepositStatus("FULLY_CLOSING");
        addingRMDeposits();
        if(setCallingParams())//regarding loan only with out balance closing
            return;
        getLienDetails();
        cashLimitForDeposits();
        if(!getSourceScreen().equals("DEPOSITS"))
        getOperativeAccNo();
        double transAmt = CommonUtil.convertObjToDouble(getCallingAmount()).doubleValue();
        if(getSourceScreen().equals("REMITPAYMENT")){
            HashMap dataMap = new HashMap();
            dataMap.put("PRODUCT_ID", getCallingProdID());
            List lst = (List) ClientUtil.executeQuery("getPanValidationAmt", dataMap);
            dataMap = null;
            if(lst != null)
                if(lst.size() > 0){
                    double cashLmt = CommonUtil.convertObjToDouble(lst.get(0)).doubleValue();
                    if(transAmt > cashLmt){
                        cboTransType.setSelectedItem(observable.getCbmTransactionType().getDataForKey("TRANSFER"));
                        cboTransType.setEnabled(false);
                    }
                }
        }

        if (getSourceScreen().equals("ACT_CLOSING")|| getSourceScreen().equals("LOAN_ACT_CLOSING"))
            if (getCallingUiMode() == ClientConstants.ACTIONTYPE_NEW) {
                enableDisableForActClosing(true);
                cboTransType.setSelectedItem(observable.getCbmTransactionType().getDataForKey("CASH"));
                cboProductType.setEnabled(false);
                txtTransProductId.setEnabled(false);
                txtDebitAccNo.setEnabled(false);
                if (getSourceScreen().equals("ACT_CLOSING")) {
                    cboInstrumentType.setEnabled(true);
                } else {
                    cboInstrumentType.setEnabled(false);
                } 
                txtChequeNo.setEnabled(false);
                txtChequeNo2.setEnabled(false);
                tdtChequeDate.setEnabled(false);
                txtTokenNo.setEnabled(false);
                
            }
            else {
                enableDisableForActClosing(false);
            }
        if(((getSourceScreen().equals("REMITISSUE")) || (getSourceScreen().equals("REMITPAYMENT"))) && ((getCallingUiMode() == ClientConstants.ACTIONTYPE_NEW) || (getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT))){
            if((tblTransDetails.getRowCount() >= 1)){
                cboTransType.setSelectedItem(observable.getCbmTransactionType().getDataForKey("TRANSFER"));
                cboProductType.setSelectedItem(observable.getCbmProductType().getDataForKey("GL"));
                cboTransType.setEnabled(false);
                cboProductType.setEnabled(false);
            }
        }
        
        if(getSourceScreen().equals("SHG_TRANSACTION")){
            cboTransType.setSelectedItem("Transfer");
            cboProductType.setSelectedItem("Advances");
            cboInstrumentType.setSelectedItem("Cheque");
            txtDebitAccNo.setText(getCallingTransAcctNo());
            txtTransProductId.setText(getCallingProdID());
            if(txtDebitAccNo.getText().length()>0)
                txtDebitAccNoFocusLost(null);
            cboTransType.setEnabled(false);
            txtApplicantsName.setEnabled(false);
            btnTransProductId.setEnabled(false);
            
            txtDebitAccNo.setEnabled(false);
            ClientUtil.enableDisable(panLeftSubTxDetails,false);
            cboProductType.setEnabled(true);
            btnDebitAccNo.setEnabled(true);
            txtTransactionAmt.setEnabled(true);
            //txtTransactionAmt.setText("");
        }
        
        if(getSourceScreen().equals("INVESTMENT")){
            cboTransType.setSelectedItem(observable.getCbmTransactionType().getDataForKey("TRANSFER"));
            cboProductType.setSelectedItem(observable.getCbmProductType().getDataForKey("GL"));
            cboInstrumentType.setSelectedItem("Voucher");
            cboInstrumentType.setEnabled(false);
            cboTransType.setEnabled(false);
            cboProductType.setEnabled(false);
            txtDebitAccNo.setText(getCallingTransAcctNo());
            txtDebitAccNo.setEnabled(false);
            ClientUtil.enableDisable(panLeftSubTxDetails,false);
            btnDebitAccNo.setEnabled(false);
            btnTransProductId.setEnabled(false);
        }
        
        if(getSourceScreen().equals("INVESTMENT_CHARGE")){
            cboTransType.setSelectedItem(observable.getCbmTransactionType().getDataForKey("TRANSFER"));
            cboProductType.setSelectedItem(observable.getCbmProductType().getDataForKey("GL"));
            cboInstrumentType.setSelectedItem("Voucher");
            cboInstrumentType.setEnabled(false);
            cboProductType.setEnabled(false);
            txtDebitAccNo.setText(getCallingTransAcctNo());
            ClientUtil.enableDisable(panLeftSubTxDetails,true);
            ClientUtil.enableDisable(panRightSubTxDetails,true);
            txtApplicantsName.setEnabled(false);
            cboTransType.setEnabled(false);
            btnDebitAccNo.setEnabled(true);
        }
        
        if(getSourceScreen().equals("MDS_COMMENCEMENT")){
            cboTransType.setSelectedItem("Transfer");
            cboProductType.setSelectedItem("General Ledger");
            cboTransType.setEnabled(false);
            HashMap schemeMap = new HashMap();
            schemeMap.put("SCHEME_NAME",getSchemeName());
            List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead",schemeMap);
            if(lst!=null && lst.size()>0){
                schemeMap = (HashMap)lst.get(0);
                if(schemeMap.containsKey("SUSPENSE_GL_ACCNO") && schemeMap.get("SUSPENSE_GL_ACCNO") != null && schemeMap.get("SUSPENSE_GL_ACCNO").equals("ACC_NO")){
                   cboProductType.setSelectedItem("Suspense Account");
                   txtTransProductId.setText(CommonUtil.convertObjToStr(schemeMap.get("SUSPENSE_PROD_ID")));
                   txtDebitAccNo.setText(CommonUtil.convertObjToStr(schemeMap.get("SUSPENSE_ACC_NO")));
                }else{
                txtDebitAccNo.setText(CommonUtil.convertObjToStr(schemeMap.get("SUSPENSE_HEAD")));
                txtApplicantsName.setText(CommonUtil.convertObjToStr(schemeMap.get("SUSPENSE_HEAD")));
                lblCustomerNameValue.setText(CommonUtil.convertObjToStr(schemeMap.get("SUSPENSE_HEAD")));
                }
            }
            txtApplicantsName.setEnabled(false);
            cboTransType.setEnabled(false);
            cboProductType.setEnabled(false);
            txtDebitAccNo.setEnabled(false);
            txtTransProductId.setEnabled(false);
            btnTransProductId.setEnabled(false);
            btnDebitAccNo.setEnabled(false);
            txtTransactionAmt.setEnabled(false);
        }
        
        if(getSourceScreen().equals("RECOVERY_LIST_TALLY")){
            try{
            txtTransactionAmt.setEnabled(false);
            cboTransType.setSelectedItem("Transfer");
            cboTransType.setEnabled(false);
                cboProductType.removeAllItems();
                observable.setProductType();
                cboProductType.setModel(observable.getCbmProductType());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(getSourceScreen().equals("MDS_APPLICATION")){
            if((!getChitType().equals("") && getChitType().equals("THALAYAL") && !getSchemeName().equals("")) ||
            (!getChitType().equals("") && getChitType().equals("MUNNAL") && !getSchemeName().equals(""))){
                cboTransType.setSelectedItem("Transfer");
                cboProductType.setSelectedItem("General Ledger");
                HashMap schemeMap = new HashMap();
                schemeMap.put("SCHEME_NAME",getSchemeName());
                List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead",schemeMap);
                if(lst!=null && lst.size()>0){
                    schemeMap = (HashMap)lst.get(0);
                    if(!getChitType().equals("") && getChitType().equals("THALAYAL") && !getSchemeName().equals("")){
                        txtDebitAccNo.setText(CommonUtil.convertObjToStr(schemeMap.get("THALAYAL_REP_PAY_HEAD")));
                        txtApplicantsName.setText(CommonUtil.convertObjToStr(schemeMap.get("THALAYAL_REP_PAY_HEAD")));
                        lblCustomerNameValue.setText(CommonUtil.convertObjToStr(schemeMap.get("THALAYAL_REP_PAY_HEAD")));
                    }else if(!getChitType().equals("") && getChitType().equals("MUNNAL") && !getSchemeName().equals("")){
                        txtDebitAccNo.setText(CommonUtil.convertObjToStr(schemeMap.get("MUNNAL_BONUS_HEAD")));
                        txtApplicantsName.setText(CommonUtil.convertObjToStr(schemeMap.get("MUNNAL_BONUS_HEAD")));
                        lblCustomerNameValue.setText(CommonUtil.convertObjToStr(schemeMap.get("MUNNAL_BONUS_HEAD")));
                    }
                }
                txtApplicantsName.setEnabled(false);
                cboTransType.setEnabled(false);
                cboProductType.setEnabled(false);
                txtDebitAccNo.setEnabled(false);
                txtTransProductId.setEnabled(false);
                btnTransProductId.setEnabled(false);
                btnDebitAccNo.setEnabled(false);
                txtTransactionAmt.setEnabled(false);
            }else{
                txtApplicantsName.setEnabled(true);
                cboTransType.setEnabled(true);
                cboProductType.setEnabled(true);
                txtDebitAccNo.setEnabled(false);
                txtTransProductId.setEnabled(false);
                //                btnTransProductId.setEnabled(true);
                //                btnDebitAccNo.setEnabled(true);
                txtTransactionAmt.setEnabled(true);
            }
        }
         if(getSourceScreen().equals("TERM_DEPOSIT") || getSourceScreen().equals("MULTIPLE_TERM_DEPOSIT") || getSourceScreen().equals("MDS_PRIZED_PAYMENT")){
             txtTransactionAmt.setEnabled(false);
        }
        if(getSourceScreen().equals("INVESTMENT_TRANS")){
            txtTransactionAmt.setEnabled(false);
//          observable.removeProductElements();//commented By Nidhin 12-06-2014
        }
        if(getSourceScreen().equals("GROUP_LOAN")){
            cboTransType.setSelectedItem(observable.getCbmTransactionType().getDataForKey("CASH"));
            txtTransactionAmt.setText(getCallingAmount());
            txtApplicantsName.setText("GROUP LOAN");
        }
        if(getSourceScreen().equals("INDEND_REGISTER")){
            txtTransactionAmt.setText(getCallingAmount()); 
            //System.out.println("get particulars@#%@%#%#@%#@"+getCallingParticulars());
            txtParticulars.setText(getCallingParticulars());
        }
        if(getSourceScreen().equals("INVESTMENT_MULTIPLE_MASTER")){
            txtTransactionAmt.setText(getCallingAmount());            
        }
        if(getSourceScreen().equals("PAY_ROLL_MASTER")){
            txtTransactionAmt.setText(getCallingAmount());            
        }
        
		txtParticulars.setEnabled(true);//Added By Sathiya 11-03-2014 
        if (getSourceScreen().equals("MDS_RECEIPT")) {//Added by Shihad 09-09-2014 Modified by sreekrishn
            if(!(getCallingTransType()!=null && getCallingTransProdType().length()>0)){
                cboTransType.setSelectedItem("Cash");
            }
            if(getCallingTransProdType()!=null && getCallingTransProdType().length()>0){
                cboProductType.setSelectedItem(getCallingTransProdType());    
            }
            if(getCallingTransAcctNo()!=null && getCallingTransAcctNo().length()>0){
                txtDebitAccNo.setText(getCallingTransAcctNo());    
            }
            if(getCallingApplicantName()!=null && getCallingApplicantName().length()>0){
                txtApplicantsName.setText(getCallingApplicantName());    
            }
        }
        
        if (getSourceScreen().equals("GDS_RECEIPT")) {
            if ((!getChitType().equals("") && getChitType().equals("THALAYAL") && !getSchemeName().equals(""))
                    || (!getChitType().equals("") && getChitType().equals("MUNNAL") && !getSchemeName().equals(""))) {
                cboTransType.setSelectedItem("Transfer");
                cboProductType.setSelectedItem("General Ledger");
                HashMap schemeMap = new HashMap();
                schemeMap.put("SCHEME_NAME", getSchemeName());
                List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", schemeMap);
                if (lst != null && lst.size() > 0) {
                    schemeMap = (HashMap) lst.get(0);
                    if (!getChitType().equals("") && getChitType().equals("THALAYAL") && !getSchemeName().equals("")) {
                        txtDebitAccNo.setText(CommonUtil.convertObjToStr(schemeMap.get("THALAYAL_REP_PAY_HEAD")));
                        txtApplicantsName.setText(CommonUtil.convertObjToStr(schemeMap.get("THALAYAL_REP_PAY_HEAD")));
                        lblCustomerNameValue.setText(CommonUtil.convertObjToStr(schemeMap.get("THALAYAL_REP_PAY_HEAD")));
                    } else if (!getChitType().equals("") && getChitType().equals("MUNNAL") && !getSchemeName().equals("")) {
                        txtDebitAccNo.setText(CommonUtil.convertObjToStr(schemeMap.get("MUNNAL_BONUS_HEAD")));
                        txtApplicantsName.setText(CommonUtil.convertObjToStr(schemeMap.get("MUNNAL_BONUS_HEAD")));
                        lblCustomerNameValue.setText(CommonUtil.convertObjToStr(schemeMap.get("MUNNAL_BONUS_HEAD")));
                    }
                }
                txtApplicantsName.setEnabled(false);
                cboTransType.setEnabled(false);
                cboProductType.setEnabled(false);
                txtDebitAccNo.setEnabled(false);
                txtTransProductId.setEnabled(false);
                btnTransProductId.setEnabled(false);
                btnDebitAccNo.setEnabled(false);
                txtTransactionAmt.setEnabled(false);
            } else {
                if (!(getCallingTransType() != null && getCallingTransProdType().length() > 0)) {
                    cboTransType.setSelectedItem("Cash");
                }
                if (getCallingTransProdType() != null && getCallingTransProdType().length() > 0) {
                    cboProductType.setSelectedItem(getCallingTransProdType());
                }
                if (getCallingTransAcctNo() != null && getCallingTransAcctNo().length() > 0) {
                    txtDebitAccNo.setText(getCallingTransAcctNo());
                }
                if (getCallingApplicantName() != null && getCallingApplicantName().length() > 0) {
                    txtApplicantsName.setText(getCallingApplicantName());
                }
            }
        }
        
        
        
        
       //Added by sreekrishnan 
       if (getSourceScreen().equals("MDS_MEMBER_RECEIPT")) {
            if(getCallingTransProdType()!=null && getCallingTransProdType().length()>0){
                cboProductType.setSelectedItem(observable.getCbmProductType().getDataForKey(getCallingTransProdType()));    
            }
            if(getCallingProdID()!=null && getCallingProdID().length()>0){
                txtTransProductId.setText(getCallingProdID());    
            }
            if(getCallingTransAcctNo()!=null && getCallingTransAcctNo().length()>0){
                txtDebitAccNo.setText(getCallingTransAcctNo());    
            }
            if(getCallingApplicantName()!=null && getCallingApplicantName().length()>0){
                txtApplicantsName.setText(getCallingApplicantName());    
            }
        }        
       if(getSourceScreen().equals("MDS_PRIZED_PAYMENT")){
            if((!getChitType().equals("") && getChitType().equals("THALAYAL") && !getSchemeName().equals(""))){
                cboTransType.setSelectedItem("Transfer");
                cboProductType.setSelectedItem("General Ledger");
                HashMap schemeMap = new HashMap();
                schemeMap.put("SCHEME_NAME",getSchemeName());
                List lst = ClientUtil.executeQuery("getThalayalHeadForChittal",schemeMap);
                if(lst!=null && lst.size()>0){
                    schemeMap = (HashMap)lst.get(0);
                    if(!getChitType().equals("") && getChitType().equals("THALAYAL") && !getSchemeName().equals("")){
                        txtDebitAccNo.setText(CommonUtil.convertObjToStr(schemeMap.get("THALAYAL_REP_PAY_HEAD")));
                        txtApplicantsName.setText(CommonUtil.convertObjToStr(schemeMap.get("AC_HD_DESC")));
                        lblCustomerNameValue.setText(CommonUtil.convertObjToStr(schemeMap.get("AC_HD_DESC")));
                    }
                }
                txtApplicantsName.setEnabled(false);
                cboTransType.setEnabled(false);
                cboProductType.setEnabled(false);
                txtDebitAccNo.setEnabled(false);
                txtTransProductId.setEnabled(false);
                btnTransProductId.setEnabled(false);
                btnDebitAccNo.setEnabled(false);
                txtTransactionAmt.setEnabled(false);
            }else if((!getChitType().equals("") && getChitType().equals("MUNNAL") && !getSchemeName().equals(""))){//KD-3672
                cboTransType.setSelectedItem("Transfer");
                cboProductType.setSelectedItem("General Ledger");
                HashMap schemeMap = new HashMap();
                schemeMap.put("SCHEME_NAME",getSchemeName());
                List lst = ClientUtil.executeQuery("getMunnalHeadForChittal",schemeMap);
                if(lst!=null && lst.size()>0){
                    schemeMap = (HashMap)lst.get(0);
                    if(!getChitType().equals("") && getChitType().equals("MUNNAL") && !getSchemeName().equals("")){
                        txtDebitAccNo.setText(CommonUtil.convertObjToStr(schemeMap.get("MUNNAL_REP_PAY_HEAD")));
                        txtApplicantsName.setText(CommonUtil.convertObjToStr(schemeMap.get("AC_HD_DESC")));
                        lblCustomerNameValue.setText(CommonUtil.convertObjToStr(schemeMap.get("AC_HD_DESC")));
                    }
                }
                txtApplicantsName.setEnabled(false);
                cboTransType.setEnabled(false);
                cboProductType.setEnabled(false);
                txtDebitAccNo.setEnabled(false);
                txtTransProductId.setEnabled(false);
                btnTransProductId.setEnabled(false);
                btnDebitAccNo.setEnabled(false);
                txtTransactionAmt.setEnabled(false);
            }else{
                txtApplicantsName.setEnabled(true);
                cboTransType.setEnabled(true);
                cboProductType.setEnabled(true);
                txtDebitAccNo.setEnabled(false);
                txtTransProductId.setEnabled(false);
                //                btnTransProductId.setEnabled(true);
                //                btnDebitAccNo.setEnabled(true);
                txtTransactionAmt.setEnabled(true);
            }
        }
        //Added By Suresh
        if (getSourceScreen().equals("RTGS_REMITTANCE")) {
            txtTransactionAmt.setEnabled(false);
            cboInstrumentType.setSelectedItem("Voucher");
            cboTransType.setEnabled(false);
            tdtChequeDate.setEnabled(true);
        }
       //Added by sreekrishnan 
       if (getSourceScreen().equals("SHARE_SCREEN")) {
            setResetProdType(); // Added by nithya as part of mantis : 5822 [ each time in click of new product type "share" is duplicating ]
            observable.getCbmProductType().addKeyAndElement("SH","Share");
        }
        if (getSourceScreen().equals("LOAN_CLOSING_FROM_RENEAL")) {
            cboTransType.setSelectedItem("Transfer");
            txtDebitAccNo.setText(getCallingAccNo());
            txtDebitAccNo.requestFocus();
            cboTransType.setEnabled(false);
            cboProductType.setEnabled(false);
            btnDebitAccNo.setEnabled(false);
            txtTransProductId.setEnabled(false);
            btnTransProductId.setEnabled(false); 
            txtDebitAccNo.setEnabled(false);
            grabFocus();
        }
        if (getSourceScreen().equals("DEPOSIT_LOAN_CLOSING_FROM_RENEAL")) {
            cboTransType.setSelectedItem("Transfer");
            txtDebitAccNo.setText(getCallingAccNo());
            txtDebitAccNo.requestFocus();
            grabFocus();
        }
        
        
    }//GEN-LAST:event_btnNewTxDetailsActionPerformed
    
    public boolean setCallingParams(){
        txtApplicantsName.setText(getCallingApplicantName());
        double callingAmt = CommonUtil.convertObjToDouble(getCallingAmount()).doubleValue();
        //System.out.println("calling amount"+callingAmt);
        double setAmt = CommonUtil.convertObjToDouble(lblTotalTransactionAmtVal.getText()).doubleValue();
        if((getSourceScreen().equals("REMITISSUE")) && ((getCallingStatus().equals("REMIT_DUP")) || (getCallingStatus().equals("REMIT_REV")))){
            setAmt = 0;
            if(getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT){
                ClientUtil.enableDisable(panRightSubTxDetails,false);
                //                     cboTransType.setEnabled(false);
                cboProductType.setEnabled(false);
                ClientUtil.enableDisable(panDebitAccHead,false);
                ClientUtil.enableDisable(panDebitAccNo,false);
                if((remittanceIssue.getRemittanceIssueOB().isDupInEdit()) || (remittanceIssue.getRemittanceIssueOB().isRevInEdit())){
                    btnDeleteTxDetails.setEnabled(false);
                    cboTransType.setEnabled(false);
                    txtTransactionAmt.setEditable(false);
                    txtTransactionAmt.setEnabled(false);
                }
                
            }
            
            
        }
        if((getSourceScreen().equals("ACT_CLOSING")|| getSourceScreen().equals("LOAN_ACT_CLOSING")) && (callingAmt - setAmt==0)) {
            ClientUtil.showMessageWindow("Account Balance is Nil "+"\n"+"Click Main Save"); //loan outstanding balance is nil
            enableDisableForActClosing(false);
            btnSaveTxDetails.setEnabled(false);
            btnDeleteTxDetails.setEnabled(false);
            return true;
        }
        //System.out.println("setamount"+setAmt);
        txtTransactionAmt.setText(String.valueOf(callingAmt - setAmt));
        txtChequeNo.setText(getCallingInst1());
        txtChequeNo2.setText(getCallingInst2());
        return false;
    }
    private void txtParticularsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtParticularsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParticularsActionPerformed

    private void txtParticularsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtParticularsFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtParticularsFocusLost
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        
        System.exit(0);
    }//GEN-LAST:event_exitForm
private void tblTransDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransDetailsMouseClicked
btnDeleteTxDetails.setEnabled(true);
}//GEN-LAST:event_tblTransDetailsMouseClicked
    
    /**
     * Getter for property btnSaveTransactionDetailsFlag.
     * @return Value of property btnSaveTransactionDetailsFlag.
     * Is used from the calling function to identify if there is any unsaved data
     */
    public boolean isBtnSaveTransactionDetailsFlag() {
        return btnSaveTransactionDetailsFlag;
    }
    
    public void setBtnSaveTransactionDetailsFlag(boolean flag) {
        this.btnSaveTransactionDetailsFlag = flag;
    }
    
    /**
     * Getter for property sourceAccountNumber.
     * @return Value of property sourceAccountNumber.
     */
    public java.lang.String getSourceAccountNumber() {
        return sourceAccountNumber;
    }
    
    /**
     * Setter for property sourceAccountNumber.
     * @param sourceAccountNumber New value of property sourceAccountNumber.
     *
     */
    public void setSourceAccountNumber(java.lang.String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }
    
    /**
     * Getter for property transactionMode.
     * @return Value of property transactionMode.
     */
    public java.lang.String getTransactionMode() {
        return transactionMode;
    }
    
    /**
     * Setter for property transactionMode.
     * @param transactionMode New value of property transactionMode.
     */
    public void setTransactionMode(java.lang.String transactionMode) {
        this.transactionMode = transactionMode;
    }
    
    /**
     * Getter for property callingUiMode.
     * @return Value of property callingUiMode.
     */
    public int getCallingUiMode() {
        return callingUiMode;
    }
    
    /**
     * Setter for property callingUiMode.
     * @param callingUiMode New value of property callingUiMode.
     */
    public void setCallingUiMode(int callingUiMode) {
        this.callingUiMode = callingUiMode;
    }
    
    /**
     * Getter for property callingApplicantName.
     * @return Value of property callingApplicantName.
     */
    public java.lang.String getCallingApplicantName() {
        return callingApplicantName;
    }
    
    /**
     * Setter for property callingApplicantName.
     * @param callingApplicantName New value of property callingApplicantName.
     */
    public void setCallingApplicantName(java.lang.String callingApplicantName) {
        this.callingApplicantName = callingApplicantName;
    }
    
    /**
     * Getter for property callingAmount.
     * @return Value of property callingAmount.
     */
    public java.lang.String getCallingAmount() {
        return callingAmount;
    }
    
    /**
     * Setter for property callingAmount.
     * @param callingAmount New value of property callingAmount.
     */
    public void setCallingAmount(java.lang.String callingAmount) {
        this.callingAmount = callingAmount;
    }
    
    /**
     * Getter for property callingInst1.
     * @return Value of property callingInst1.
     */
    public java.lang.String getCallingInst1() {
        return callingInst1;
    }
    
    /**
     * Setter for property callingInst1.
     * @param callingInst1 New value of property callingInst1.
     */
    public void setCallingInst1(java.lang.String callingInst1) {
        this.callingInst1 = callingInst1;
    }
    
    /**
     * Getter for property callingInst2.
     * @return Value of property callingInst2.
     */
    public java.lang.String getCallingInst2() {
        return callingInst2;
    }
    
    /**
     * Setter for property callingInst2.
     * @param callingInst2 New value of property callingInst2.
     */
    public void setCallingInst2(java.lang.String callingInst2) {
        this.callingInst2 = callingInst2;
    }
    
    /**
     * Getter for property sourceScreen.
     * @return Value of property sourceScreen.
     */
    public java.lang.String getSourceScreen() {
        return sourceScreen;
    }
    
    /**
     * Setter for property sourceScreen.
     * @param sourceScreen New value of property sourceScreen.
     */
    public void setSourceScreen(java.lang.String sourceScreen) {
        this.sourceScreen = sourceScreen;
    }
    
    /**
     * Getter for property procOutTo.
     * @return Value of property procOutTo.
     */
    public java.util.HashMap getProcOutTo() {
        return observable.getProcChargeMap();
    }
    
    /**
     * Getter for property loanActnum.
     * @return Value of property loanActnum.
     */
    public java.lang.String getLoanActnum() {
        return observable.getLoneActNum();
    }
    
    /**
     * Setter for property loanActnum.
     * @param loanActnum New value of property loanActnum.
     */
    public void setLoanActnum(java.lang.String loanActnum) {
        this.loanActnum = loanActnum;
    }
    
    /**
     * Getter for property saveEnableValue.
     * @return Value of property saveEnableValue.
     */
    public int getSaveEnableValue() {
        return saveEnableValue;
    }
    
    /**
     * Setter for property saveEnableValue.
     * @param saveEnableValue New value of property saveEnableValue.
     */
    public void setSaveEnableValue(int saveEnableValue) {
        this.saveEnableValue = saveEnableValue;
    }
    
    /**
     * Getter for property callingProdID.
     * @return Value of property callingProdID.
     */
    public java.lang.String getCallingProdID() {
        return callingProdID;
    }
    
    /**
     * Setter for property callingProdID.
     * @param callingProdID New value of property callingProdID.
     */
    public void setCallingProdID(java.lang.String callingProdID) {
        this.callingProdID = callingProdID;
    }
    
    /**
     * Getter for property callingAccNo.
     * @return Value of property callingAccNo.
     */
    public java.lang.String getCallingAccNo() {
        return callingAccNo;
    }
    
    /**
     * Setter for property callingAccNo.
     * @param callingAccNo New value of property callingAccNo.
     */
    public void setCallingAccNo(java.lang.String callingAccNo) {
        this.callingAccNo = callingAccNo;
    }
    
    /**
     * Getter for property callingStatus.
     * @return Value of property callingStatus.
     */
    public java.lang.String getCallingStatus() {
        return callingStatus;
    }
    
    /**
     * Setter for property callingStatus.
     * @param callingStatus New value of property callingStatus.
     */
    public void setCallingStatus(java.lang.String callingStatus) {
        this.callingStatus = callingStatus;
    }
    
    /**
     * Getter for property depFlag.
     * @return Value of property depFlag.
     */
    public boolean isDepFlag() {
        return depFlag;
    }
    
    /**
     * Setter for property depFlag.
     * @param depFlag New value of property depFlag.
     */
    public void setDepFlag(boolean depFlag) {
        this.depFlag = depFlag;
    }
    
    /**
     * Getter for property callingDepIntRate.
     * @return Value of property callingDepIntRate.
     */
    public java.lang.String getCallingDepIntRate() {
        return callingDepIntRate;
    }
    
    /**
     * Setter for property callingDepIntRate.
     * @param callingDepIntRate New value of property callingDepIntRate.
     */
    public void setCallingDepIntRate(java.lang.String callingDepIntRate) {
        this.callingDepIntRate = callingDepIntRate;
    }
    
    public java.lang.String getCallingParticulars() {
        return callingParticulars;
    }
    
    /**
     * Setter for property callingIntAmt.
     * @param callingIntAmt New value of property callingIntAmt.
     */
    public void setCallingParticulars(java.lang.String callingParticulars) {
        this.callingParticulars = callingParticulars;
    }
    
    /**
     * Getter for property callingIntAmt.
     * @return Value of property callingIntAmt.
     */
    public java.lang.String getCallingIntAmt() {
        return callingIntAmt;
    }
    
    /**
     * Setter for property callingIntAmt.
     * @param callingIntAmt New value of property callingIntAmt.
     */
    public void setCallingIntAmt(java.lang.String callingIntAmt) {
        this.callingIntAmt = callingIntAmt;
    }
    
    /**
     * Getter for property callingClosingType.
     * @return Value of property callingClosingType.
     */
    public java.lang.String getCallingClosingType() {
        return callingClosingType;
    }
    
    /**
     * Setter for property callingClosingType.
     * @param callingClosingType New value of property callingClosingType.
     */
    public void setCallingClosingType(java.lang.String callingClosingType) {
        this.callingClosingType = callingClosingType;
    }
    
    /**
     * Getter for property accountClosing.
     * @return Value of property accountClosing.
     */
    public com.see.truetransact.ui.operativeaccount.AccountClosingUI getAccountClosing() {
        return accountClosing;
    }
    
    /**
     * Setter for property accountClosing.
     * @param accountClosing New value of property accountClosing.
     */
    public void setAccountClosing(com.see.truetransact.ui.operativeaccount.AccountClosingUI accountClosing) {
        this.accountClosing = accountClosing;
    }
    
    /**
     * Getter for property callingTransProdType.
     * @return Value of property callingTransProdType.
     */
    public java.lang.String getCallingTransProdType() {
        return callingTransProdType;
    }
   
    //Added by Anju 15/5/2014
    public int getTotAmount() {
        return totAmount;
    }

    public void setTotAmount(int totAmount) {
        this.totAmount = totAmount;
    }
    
   
    /**
     * Setter for property callingTransProdType.
     * @param callingTransProdType New value of property callingTransProdType.
     */
    public void setCallingTransProdType(java.lang.String callingTransProdType) {
        this.callingTransProdType = callingTransProdType;
    }
    
    /**
     * Getter for property callingTransType.
     * @return Value of property callingTransType.
     */
    public java.lang.String getCallingTransType() {
        return callingTransType;
    }
    
    /**
     * Setter for property callingTransType.
     * @param callingTransType New value of property callingTransType.
     */
    public void setCallingTransType(java.lang.String callingTransType) {
        this.callingTransType = callingTransType;
    }
    
    /**
     * Getter for property callingTransAcctNo.
     * @return Value of property callingTransAcctNo.
     */
    public java.lang.String getCallingTransAcctNo() {
        return callingTransAcctNo;
    }
    
    /**
     * Setter for property callingTransAcctNo.
     * @param callingTransAcctNo New value of property callingTransAcctNo.
     */
    public void setCallingTransAcctNo(java.lang.String callingTransAcctNo) {
        this.callingTransAcctNo = callingTransAcctNo;
    }
    
    /**
     * Getter for property remittanceIssue.
     * @return Value of property remittanceIssue.
     */
    public com.see.truetransact.ui.remittance.RemittanceIssueUI getRemittanceIssue() {
        return remittanceIssue;
    }
    
    /**
     * Setter for property remittanceIssue.
     * @param remittanceIssue New value of property remittanceIssue.
     */
    public void setRemittanceIssue(com.see.truetransact.ui.remittance.RemittanceIssueUI remittanceIssue) {
        this.remittanceIssue = remittanceIssue;
    }
    
    /**
     * Getter for property isTranTblClicked.
     * @return Value of property isTranTblClicked.
     */
    public boolean isIsTranTblClicked() {
        return isTranTblClicked;
    }
    
    /**
     * Setter for property isTranTblClicked.
     * @param isTranTblClicked New value of property isTranTblClicked.
     */
    public void setIsTranTblClicked(boolean isTranTblClicked) {
        this.isTranTblClicked = isTranTblClicked;
    }
    
    /**
     * Getter for property excessLoanAmt.
     * @return Value of property excessLoanAmt.
     */
    public java.lang.String getExcessLoanAmt() {
        return excessLoanAmt;
    }
    
    /**
     * Setter for property excessLoanAmt.
     * @param excessLoanAmt New value of property excessLoanAmt.
     */
    public void setExcessLoanAmt(java.lang.String excessLoanAmt) {
        this.excessLoanAmt = excessLoanAmt;
    }
    
    /**
     * Getter for property closingDepositStatus.
     * @return Value of property closingDepositStatus.
     */
    public java.lang.String getClosingDepositStatus() {
        return closingDepositStatus;
    }
    
    /**
     * Setter for property closingDepositStatus.
     * @param closingDepositStatus New value of property closingDepositStatus.
     */
    public void setClosingDepositStatus(java.lang.String closingDepositStatus) {
        this.closingDepositStatus = closingDepositStatus;
    }
    
    /**
     * Getter for property chitType.
     * @return Value of property chitType.
     */
    public java.lang.String getChitType() {
        return chitType;
    }
    
    /**
     * Setter for property chitType.
     * @param chitType New value of property chitType.
     */
    public void setChitType(java.lang.String chitType) {
        this.chitType = chitType;
    }
    
    /**
     * Getter for property schemeName.
     * @return Value of property schemeName.
     */
    public java.lang.String getSchemeName() {
        return schemeName;
    }
    
    /**
     * Setter for property schemeName.
     * @param schemeName New value of property schemeName.
     */
    public void setSchemeName(java.lang.String schemeName) {
        this.schemeName = schemeName;
    }
    
    /**
     * Getter for property transDetails.
     * @return Value of property transDetails.
     */
    public com.see.truetransact.ui.transaction.common.TransDetailsUI getTransDetails() {
        return transDetails;
    }
    
    /**
     * Setter for property transDetails.
     * @param transDetails New value of property transDetails.
     */
    public void setTransDetails(com.see.truetransact.ui.transaction.common.TransDetailsUI transDetails) {
        this.transDetails = transDetails;
    }
    
    /**
     * Getter for property advanceCreditIntAmt.
     * @return Value of property advanceCreditIntAmt.
     */
    public java.lang.Double getAdvanceCreditIntAmt() {
        return advanceCreditIntAmt;
    }
    
    /**
     * Setter for property advanceCreditIntAmt.
     * @param advanceCreditIntAmt New value of property advanceCreditIntAmt.
     */
    public void setAdvanceCreditIntAmt(java.lang.Double advanceCreditIntAmt) {
        this.advanceCreditIntAmt = CommonUtil.convertObjToDouble(advanceCreditIntAmt);
    }
    
    /**
     * Getter for property callingLtdDepositNo.
     * @return Value of property callingLtdDepositNo.
     */
    public java.lang.String getCallingLtdDepositNo() {
        return callingLtdDepositNo;
    }
    
    /**
     * Setter for property callingLtdDepositNo.
     * @param callingLtdDepositNo New value of property callingLtdDepositNo.
     */
    public void setCallingLtdDepositNo(java.lang.String callingLtdDepositNo) {
        this.callingLtdDepositNo = callingLtdDepositNo;
    }

    public String getAddIntLoanAmt() {
        return addIntLoanAmt;
    }

    public void setAddIntLoanAmt(String addIntLoanAmt) {
        this.addIntLoanAmt = addIntLoanAmt;
    }

    public CInternalFrame getParantUI() {
        return parantUI;
    }

    public void setParantUI(CInternalFrame parantUI) {
        this.parantUI = parantUI;
    }

    public String getCallingDepositeAmount() {
        return callingDepositeAmount;
    }

    public void setCallingDepositeAmount(String callingDepositeAmount) {
        this.callingDepositeAmount = callingDepositeAmount;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }

    public double getMdsWaiveAmt() {
        return mdsWaiveAmt;
    }

    public void setMdsWaiveAmt(double mdsWaiveAmt) {
        this.mdsWaiveAmt = mdsWaiveAmt;
    }
    
        
    public void checkValidation(String prodType, String transType, String accNo, double amount) {
        HashMap inputMap = new HashMap();
        double availBal = 0.0;
        double shadowDebit = 0.0;
        double minBalWitChq = 0.0;
        double minBalWitOutChq = 0.0;
        String chqAllowed = null;
        HashMap detailedActMap = null;
        inputMap.put("ACC_NO", accNo);
        try {
            List actDetails = (List) ClientUtil.executeQuery("getOpActBalDetails", inputMap);
            if (actDetails == null || actDetails.size() <= 0) {
                ClientUtil.showMessageWindow("This is not issued/valid. A/c No:" + accNo);
                resetFields();
            }
            if (actDetails != null && actDetails.size() > 0) {
                detailedActMap = (HashMap) actDetails.get(0);
                if (detailedActMap != null && detailedActMap.size() > 0) {
                    availBal = CommonUtil.convertObjToDouble(detailedActMap.get("AVAILABLE_BALANCE"));
                    shadowDebit = CommonUtil.convertObjToDouble(detailedActMap.get("SHADOW_DEBIT"));
                    minBalWitChq = CommonUtil.convertObjToDouble(detailedActMap.get("MIN_BAL_W_CHK"));
                    minBalWitOutChq = CommonUtil.convertObjToDouble(detailedActMap.get("MIN_BAL_WT_CHK"));
                    chqAllowed = CommonUtil.convertObjToStr(detailedActMap.get("CHK_ALLOWED"));
                    double currentTxnAmt = CommonUtil.convertObjToDouble(txtTransactionAmt.getText()).doubleValue();
                    if (shadowDebit > 0 && (currentTxnAmt+shadowDebit) > availBal) {//Mantis id 0011040 changes done by Sathiya.
                        ClientUtil.showMessageWindow("Balance Insufficient for A/C No :" + accNo);
                        resetFields();
                    } else if (amount > availBal) {
                        ClientUtil.showMessageWindow("Balance Insufficient for A/C No :" + accNo);
                        resetFields();
                    } else if (availBal > amount) {
                        if (chqAllowed != null && !chqAllowed.equals("")) {
                            double aftDebitAmt = availBal - amount;
                            if (chqAllowed.equals("Y")) {
                                if (aftDebitAmt < minBalWitChq) {
                                    ClientUtil.showMessageWindow("Balance will fall under min balance, cannot do transaction with A/C No: " + accNo);
                                    resetFields();
                                }
                            } else {
                                if (aftDebitAmt < minBalWitOutChq) {
                                    ClientUtil.showMessageWindow("Balance will fall under min balance, cannot do transaction with A/C No: " + accNo);
                                    resetFields();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      public void checkSuspanceValidation(String prodType, String transType, String accNo, double amount) {
           HashMap wheMap = new HashMap();
            wheMap.put("PRODUCT_ID", prodType);
            wheMap.put("ACCT_NUM", accNo);
            //System.out.println("wheMap >>"+wheMap);
            List lt1 = ClientUtil.executeQuery("getNegativeAmtCheckForSA", wheMap);
            double totAmt =amount;
            if (lt1 != null && lt1.size() > 0) {
                HashMap tMap = (HashMap) lt1.get(0);
                String negYn = CommonUtil.convertObjToStr(tMap.get("NEG_AMT_YN"));
                double clAmt = CommonUtil.convertObjToDouble(tMap.get("CLEAR_BALANCE"));
                if (!negYn.equals("Y") && totAmt > clAmt) {
                    int result = ClientUtil.confirmationAlert("The account has _ve/Zero  balance.Do you Want to  continue?");
                    if (result == 0) {
                    } else {
                       resetFields();
                    }
                }
            }
      }
      
      
      private boolean suspenseNegativeValue(String prodType, String accNo){
          boolean negYn = false;
          HashMap wheMap = new HashMap();
            wheMap.put("PRODUCT_ID", prodType);
            wheMap.put("ACCT_NUM", accNo);
            List lt1 = ClientUtil.executeQuery("getNegativeAmtCheckForSA", wheMap);
            if (lt1 != null && lt1.size() > 0) {
                HashMap tMap = (HashMap) lt1.get(0);
                if(CommonUtil.convertObjToStr(tMap.get("NEG_AMT_YN")).equals("Y")){
                    negYn = true;
                }
            }          
          return negYn;          
      }
      
      
     public void checkShareWithdrawalValidation(String accNo, double amount) {
         //System.out.println("accNo#!@##!#"+accNo+"@#$@$@$@$@#$"+amount);
            HashMap wheMap = new HashMap();
            wheMap.put("SHARE_ACCT_NO", accNo);
            wheMap.put("DEBIT_AMOUNT", amount);
            List lt1 = ClientUtil.executeQuery("getAvailableShares", wheMap);
            if (lt1 != null && lt1.size() > 0) {
                ClientUtil.showMessageWindow("Share outstanding is less than requested debit amount!!");
                resetFields();
            }
      }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnDebitAccNo;
    private com.see.truetransact.uicomponent.CButton btnDeleteTxDetails;
    private com.see.truetransact.uicomponent.CButton btnNewTxDetails;
    private com.see.truetransact.uicomponent.CButton btnSaveTxDetails;
    private com.see.truetransact.uicomponent.CButton btnTransProductId;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentType;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CComboBox cboTransType;
    private com.see.truetransact.uicomponent.CLabel lblApplicantsName;
    private com.see.truetransact.uicomponent.CLabel lblChequeDate;
    private com.see.truetransact.uicomponent.CLabel lblChequeNo;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameValue;
    private com.see.truetransact.uicomponent.CLabel lblDebitAccNo;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentType;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblTokenNo;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTransProductId;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CLabel lblTransactionAmt;
    private com.see.truetransact.uicomponent.CPanel panDebitAccHead;
    private com.see.truetransact.uicomponent.CPanel panDebitAccNo;
    private com.see.truetransact.uicomponent.CPanel panDetails;
    private com.see.truetransact.uicomponent.CPanel panLeftSubTxDetails;
    private com.see.truetransact.uicomponent.CPanel panRightSubTxDetails;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTotalTransactiosn;
    private com.see.truetransact.uicomponent.CPanel panTransDetails;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panbtnTxDetails;
    private com.see.truetransact.uicomponent.CSeparator sptTransDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTransDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabTransDetails;
    private com.see.truetransact.uicomponent.CTable tblTransDetails;
    private com.see.truetransact.uicomponent.CDateField tdtChequeDate;
    private com.see.truetransact.uicomponent.CTextField txtApplicantsName;
    private com.see.truetransact.uicomponent.CTextField txtChequeNo;
    private com.see.truetransact.uicomponent.CTextField txtChequeNo2;
    private com.see.truetransact.uicomponent.CTextField txtDebitAccNo;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    private com.see.truetransact.uicomponent.CTextField txtTokenNo;
    private com.see.truetransact.uicomponent.CTextField txtTransProductId;
    private com.see.truetransact.uicomponent.CTextField txtTransactionAmt;
    // End of variables declaration//GEN-END:variables
    
}
