/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 1-07-2015
 */
package com.see.truetransact.ui.payroll.pfInterestApplication;


import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.*;
import java.util.logging.Level;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;





/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class PFIntersestApplicationUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

     private String viewType = new String();
     Rounding rd = new Rounding();
    private HashMap mandatoryMap;
    private PFInterestApplicationOB observable;
   // DirectorBoardOB ob;
    final String AUTHORIZE="Authorize";
    private final static Logger log = Logger.getLogger(PFIntersestApplicationUI.class);
    java.util.ResourceBundle resourceBundle;
    String txtid=null;
    Date CurDate = null;
        
    /** Creates new form CustomerIdChangeUI */
    public PFIntersestApplicationUI() {
        initComponents();
        initStartUp();
         CurDate = ClientUtil.getCurrentDate();
    }
    
    private void initStartUp(){
      setMandatoryHashMap();
        setFieldNames();
        observable = new PFInterestApplicationOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
    }
    
    private void setMaxLength() {
     
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMemno", new Boolean(true));
        mandatoryMap.put("txtDesig", new Boolean(true));
        mandatoryMap.put("txtPriority", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
       public void initTableData() {
        // model=new javax.swing.table.DefaultTableModel();
        tblPfIntDetails.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "Select", "Emp_Name", "Employee_Id", "InsterestAmt","From Date"}
        ) {
            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false, false,false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 0 || columnIndex == 3) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
      //  setWidthColumns();

//        tblTransaction.setCellSelectionEnabled(true);
//        tblTransaction.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
//            public void propertyChange(java.beans.PropertyChangeEvent evt) {
//                tblTransactionPropertyChange(evt);
//            }
//        });
    
       
    }
    private Object[][] setTableData() {
        HashMap whereMap = null;
        List lst = observable.getTableValue();
        if (lst != null && lst.size() > 0) {
            Object totalList[][] = new Object[lst.size()][16];
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    HashMap Map = (HashMap) lst.get(i);
                    totalList[i][0] = false;
                    totalList[i][1] = CommonUtil.convertObjToStr(Map.get("EMP_NAME"));
                    totalList[i][2] = CommonUtil.convertObjToStr(Map.get("EMP_ID"));
                    totalList[i][3] = CommonUtil.convertObjToStr(Map.get("INTEREST"));
                    totalList[i][4] = CommonUtil.convertObjToStr(Map.get("INT_CALC_DT"));
                }
            }
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");
            observable.resetForm();

        }
        return null;

    }

     private void intTableEnableDisable() {
          EnhancedTableModel tblmdl = new EnhancedTableModel();
          tblPfIntDetails.setModel(tblmdl);
         
    }
     
        private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1 * 100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100 * 100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10 * 100;
        }
        return returnVal;
    }
    /****************** NEW METHODS *****************/
    
//    private void updateAuthorizeStatus(String authorizeStatus) {
//        if (viewType != AUTHORIZE){
//            HashMap mapParam = new HashMap();
//            HashMap whereMap = new HashMap();
//            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
//            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            whereMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
//            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
//            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
//            mapParam.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsAuthorize");
//            viewType = AUTHORIZE;
//            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
//            //            isFilled = false;
//            authorizeUI.show();
//            btnSave.setEnabled(false);
//            observable.setStatus();
//            lblStatus.setText(observable.getLblStatus());
//        } else if (viewType == AUTHORIZE){
//            ArrayList arrList = new ArrayList();
//            HashMap authorizeMap = new HashMap();
//            HashMap singleAuthorizeMap = new HashMap();
//            singleAuthorizeMap.put("STATUS", authorizeStatus);
//            singleAuthorizeMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
//            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
//            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
//            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            String presentBranch = ((ComboBoxModel)cboTransBran.getModel()).getKeyForSelected().toString();
//            if (presentBranch.lastIndexOf("-")!=-1)
//            presentBranch = presentBranch.substring(0,presentBranch.lastIndexOf("-"));
//            presentBranch= presentBranch.trim();
//            singleAuthorizeMap.put("PRESENT_BRANCH_CODE",presentBranch);
//            singleAuthorizeMap.put("EMP_ID", observable.getTxtEmpID());
//            arrList.add(singleAuthorizeMap);         
//            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
//            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
//            authorize(authorizeMap,observable.getTxtEmpTransferID());
//            viewType = "";
//            super.setOpenForEditBy(observable.getStatusBy());
//        }
//    }
    
//    public void authorize(HashMap map,String id) {
//        System.out.println("Authorize Map : " + map);
//        
//        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
//            observable.set_authorizeMap(map);
//            observable.doAction();
//            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                   super.setOpenForEditBy(observable.getStatusBy());
//                   super.removeEditLock(id);
//             }
//            btnCancelActionPerformed(null);
//            observable.setStatus();
//            observable.setResultStatus();
//            lblStatus.setText(observable.getLblStatus());
//            
//        }
//    }
//    public void authorize(HashMap map) {
//        System.out.println("Authorize Map : " + map);
//        
//        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
//            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
//            observable.set_authorizeMap(map);
//            observable.doAction();
//            btnCancelActionPerformed(null);
//            observable.setStatus();
//            observable.setResultStatus();
//            lblStatus.setText(observable.getLblStatus());
//            
//        }
//    }
//    public void setHelpMessage() {
//    }
    
    /************ END OF NEW METHODS ***************/
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoApplType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoApplType1 = new com.see.truetransact.uicomponent.CButtonGroup();
        panEmpTransfer = new com.see.truetransact.uicomponent.CPanel();
        panTranferDetails = new com.see.truetransact.uicomponent.CPanel();
        lblIntRate = new com.see.truetransact.uicomponent.CLabel();
        txtIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblToAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblFromAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtToAccNo = new com.see.truetransact.uicomponent.CTextField();
        txtFromAccNo = new com.see.truetransact.uicomponent.CTextField();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        btnFromAccount = new com.see.truetransact.uicomponent.CButton();
        btnToAccount = new com.see.truetransact.uicomponent.CButton();
        btnViewData = new com.see.truetransact.uicomponent.CButton();
        panIntDetails = new com.see.truetransact.uicomponent.CPanel();
        srpIntDetail = new com.see.truetransact.uicomponent.CScrollPane();
        tblPfIntDetails = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        txtTotAmt = new com.see.truetransact.uicomponent.CTextField();
        btnProcessData = new com.see.truetransact.uicomponent.CButton();
        lblIntPaybleGLHead = new com.see.truetransact.uicomponent.CLabel();
        txtIntPaybleGLHead = new com.see.truetransact.uicomponent.CTextField();
        btnIntPaybleGLHead = new com.see.truetransact.uicomponent.CButton();
        txtIntPaybleGLHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cboPfType = new com.see.truetransact.uicomponent.CComboBox();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panEmpTransfer.setMaximumSize(new java.awt.Dimension(650, 520));
        panEmpTransfer.setMinimumSize(new java.awt.Dimension(510, 520));
        panEmpTransfer.setPreferredSize(new java.awt.Dimension(510, 520));
        panEmpTransfer.setLayout(new java.awt.GridBagLayout());

        panTranferDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("PF Interest Application"));
        panTranferDetails.setMinimumSize(new java.awt.Dimension(600, 450));
        panTranferDetails.setName("panMaritalStatus"); // NOI18N
        panTranferDetails.setPreferredSize(new java.awt.Dimension(600, 450));
        panTranferDetails.setLayout(new java.awt.GridBagLayout());

        lblIntRate.setText("Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTranferDetails.add(lblIntRate, gridBagConstraints);

        txtIntRate.setAllowAll(true);
        txtIntRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTranferDetails.add(txtIntRate, gridBagConstraints);

        lblToAccNo.setText("To Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 4, 4);
        panTranferDetails.add(lblToAccNo, gridBagConstraints);

        lblFromAccNo.setText("From Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panTranferDetails.add(lblFromAccNo, gridBagConstraints);

        txtToAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 4);
        panTranferDetails.add(txtToAccNo, gridBagConstraints);

        txtFromAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 23);
        panTranferDetails.add(txtFromAccNo, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panTranferDetails.add(tdtFromDate, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panTranferDetails.add(lblFromDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTranferDetails.add(tdtToDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTranferDetails.add(lblToDate, gridBagConstraints);

        btnFromAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAccount.setToolTipText("From Account");
        btnFromAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTranferDetails.add(btnFromAccount, gridBagConstraints);

        btnToAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAccount.setToolTipText("From Account");
        btnToAccount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTranferDetails.add(btnToAccount, gridBagConstraints);

        btnViewData.setText("View");
        btnViewData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewDataActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 8);
        panTranferDetails.add(btnViewData, gridBagConstraints);

        panIntDetails.setMinimumSize(new java.awt.Dimension(525, 250));
        panIntDetails.setPreferredSize(new java.awt.Dimension(525, 250));
        panIntDetails.setRequestFocusEnabled(false);

        srpIntDetail.setMinimumSize(new java.awt.Dimension(450, 210));
        srpIntDetail.setPreferredSize(new java.awt.Dimension(450, 210));

        tblPfIntDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblPfIntDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPfIntDetailsMouseClicked(evt);
            }
        });
        tblPfIntDetails.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblPfIntDetailsPropertyChange(evt);
            }
        });
        srpIntDetail.setViewportView(tblPfIntDetails);

        panIntDetails.add(srpIntDetail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTranferDetails.add(panIntDetails, gridBagConstraints);

        panSelectAll.setMaximumSize(new java.awt.Dimension(200, 27));
        panSelectAll.setMinimumSize(new java.awt.Dimension(200, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(200, 27));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panTranferDetails.add(panSelectAll, gridBagConstraints);

        lblTotal.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        panTranferDetails.add(lblTotal, gridBagConstraints);

        txtTotAmt.setEnabled(false);
        txtTotAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panTranferDetails.add(txtTotAmt, gridBagConstraints);

        btnProcessData.setText("Process");
        btnProcessData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessDataActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panTranferDetails.add(btnProcessData, gridBagConstraints);

        lblIntPaybleGLHead.setText("Interest Payable GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 1, 4);
        panTranferDetails.add(lblIntPaybleGLHead, gridBagConstraints);

        txtIntPaybleGLHead.setAllowAll(true);
        txtIntPaybleGLHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntPaybleGLHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntPaybleGLHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        panTranferDetails.add(txtIntPaybleGLHead, gridBagConstraints);

        btnIntPaybleGLHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntPaybleGLHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntPaybleGLHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntPaybleGLHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntPaybleGLHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTranferDetails.add(btnIntPaybleGLHead, gridBagConstraints);

        txtIntPaybleGLHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtIntPaybleGLHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        panTranferDetails.add(txtIntPaybleGLHeadDesc, gridBagConstraints);

        cLabel1.setText("PF Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panTranferDetails.add(cLabel1, gridBagConstraints);

        cboPfType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPfType.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTranferDetails.add(cboPfType, gridBagConstraints);

        panEmpTransfer.add(panTranferDetails, new java.awt.GridBagConstraints());

        getContentPane().add(panEmpTransfer, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
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
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace30);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

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

        mbrCustomer.setName("mbrCustomer"); // NOI18N

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
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
    //    popUp("Enquiry");
    }//GEN-LAST:event_btnViewActionPerformed
                            
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }
        
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
    observable.setStatus();
    System.out.println("in edit btn");
     popUp("Edit");
    lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
//        deletescreenLock();
        viewType = "CANCEL";
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        intTableEnableDisable();
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
         updateOBFields();
        System.out.println("in save");
  
        StringBuffer str  =  new StringBuffer();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTranferDetails);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
           displayAlert(mandatoryMessage);
            System.out.println("in save1");
                   }
        else{
            System.out.println("in save2");
            savePerformed();
            
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
//        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(tdtToDate.getDateValue()).equalsIgnoreCase("")) {
            tdtToDate.setDateValue(DateUtil.getStringDate(CurDate));
        } else {
//            ClientUtil.validateFromDate(tdtFromDate, tdtToDate.getDateValue());
        }
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        //ClientUtil.validateToDate(tdtToDate, tdtFromDate.getDateValue());
       
    }//GEN-LAST:event_tdtToDateFocusLost

    private void btnFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountActionPerformed
        // TODO add your handling code here:
       popUp("FROMACC_NUM");
     
    }//GEN-LAST:event_btnFromAccountActionPerformed
   
    private void btnToAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccountActionPerformed
        // TODO add your handling code here:
        popUp("TOACC_NUM");
       
    }//GEN-LAST:event_btnToAccountActionPerformed

    private void btnViewDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewDataActionPerformed
          Date frmDt = DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
          Date toDt = DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
          if(frmDt.compareTo(toDt)>0){
              ClientUtil.showMessageWindow("To Date should be grater than from date");
                            return;
          }
          if(txtIntRate.getText()==null || CommonUtil.convertObjToDouble(txtIntRate.getText())<=0){
              ClientUtil.showMessageWindow("Please Enter a valid rate");
                            return;
          }
        intTableEnableDisable();
            updateOBFields();
            observable.getData();
           if (observable.getTableValue() != null && observable.getTableValue().size() > 0) {
                initTableData();
        }
    }//GEN-LAST:event_btnViewDataActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        double totAmount = 0;
        for (int i = 0; i < tblPfIntDetails.getRowCount(); i++) {
            tblPfIntDetails.setValueAt(new Boolean(flag), i, 0);
             totAmount = totAmount + CommonUtil.convertObjToDouble(tblPfIntDetails.getValueAt(i, 3)).doubleValue();
        }
        long roundOffType = getRoundOffType(CommonUtil.convertObjToStr("Nearest Value"));
        if (roundOffType != 0) {
            totAmount = rd.getNearest((long) (totAmount * roundOffType), roundOffType) / roundOffType;
        }
        txtTotAmt.setText(CommonUtil.convertObjToStr(totAmount));
        tblPfIntDetails.revalidate();
        tblPfIntDetails.repaint();
        if (chkSelectAll.isSelected() == false) {
            txtTotAmt.setText("" );
        }
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnProcessDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessDataActionPerformed
        if (tblPfIntDetails.getRowCount() > 0 && CommonUtil.convertObjToDouble(txtTotAmt.getText()) > 0) {
            updateOBFields();
            int rowcnt =tblPfIntDetails.getRowCount();
            ArrayList toObList = new ArrayList();
            double totAmount = 0;
            for (int i = 0; i < rowcnt; i++) {
                ArrayList oneList = new ArrayList();
                    String chk = CommonUtil.convertObjToStr(tblPfIntDetails.getValueAt(i, 0));
                     String empNa = CommonUtil.convertObjToStr(tblPfIntDetails.getValueAt(i, 1));
                      String empId = CommonUtil.convertObjToStr(tblPfIntDetails.getValueAt(i, 2));
                    double intAmt = CommonUtil.convertObjToDouble(tblPfIntDetails.getValueAt(i,3));
                    System.out.println("intAmt -- :"+intAmt);
                    if (chk.equals("true")) {
                        if (intAmt <= 0) {
                            ClientUtil.displayAlert("Interest amount is not correct For Employ Number " + CommonUtil.convertObjToStr(oneList.get(1))
                                    + "\n Please correct it or Disselect the number");
                            return;
                        } else {
                            oneList.add(chk);
                            oneList.add(empNa);
                            oneList.add(empId);
                            oneList.add(intAmt);
                            oneList.add(CommonUtil.getProperDate(CurDate, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblPfIntDetails.getValueAt(i,4)))));
                            totAmount += intAmt;
                            toObList.add(oneList);
                        }
                    }

            }
            if (totAmount <= 0) {
                ClientUtil.displayAlert("Select atlesat  one Row!!");
                return;
            } else if(txtIntPaybleGLHead.getText()==null || txtIntPaybleGLHead.getText().length()<=0){
                 ClientUtil.displayAlert("Select the Account Head");
                return;
            }
        else {
                HashMap toData = new HashMap();
                toData.put("INT_PROCESS_DETAILS", toObList);
                toData.put("TOT_INT_AMT", totAmount);
                toData.put("DR_ACHD",txtIntPaybleGLHead.getText());
                System.out.println("toData --- :"+toData);
                observable.doAction(toData);
                if(observable.getProxyReturnMap()!=null){
                    String payrollId = CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("PAYROLL_ID"));
                    if(payrollId.length()>0){
                        ClientUtil.showMessageWindow("Payroll ID:"+payrollId);
                        displayTransDetail(observable.getProxyReturnMap());
                        btnCancelActionPerformed(null);
                    }
                }
            }
        } else {
            ClientUtil.displayAlert("Select atlesat  one Row!!");
            return;
        }
    }//GEN-LAST:event_btnProcessDataActionPerformed

    private void txtIntPaybleGLHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntPaybleGLHeadFocusLost
        // TODO add your handling code here:
        if (!(txtIntPaybleGLHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtIntPaybleGLHead, "product.deposits.getAcctHeadList");
            txtIntPaybleGLHeadDesc.setText(getAccHeadDesc(txtIntPaybleGLHead.getText()));
        }
    }//GEN-LAST:event_txtIntPaybleGLHeadFocusLost

    private void btnIntPaybleGLHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntPaybleGLHeadActionPerformed
        // Add your handling code here:
        popUp("INT_ACHD");
    }//GEN-LAST:event_btnIntPaybleGLHeadActionPerformed

    
    public void calc() {
        double totAmt = 0;
        if (tblPfIntDetails.getRowCount() > 0) {
            for (int i = 0; i < tblPfIntDetails.getRowCount(); i++) {
                if ((Boolean) tblPfIntDetails.getValueAt(i, 0)) {
                    totAmt = totAmt + CommonUtil.convertObjToDouble(tblPfIntDetails.getValueAt(i, 3).toString()).doubleValue();                 
                }
            }            
            txtTotAmt.setText((String.valueOf(totAmt)));
        }
    }
    
    
    private void tblPfIntDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPfIntDetailsMouseClicked
//        int selRow = tblPfIntDetails.getSelectedRow();
//        boolean checked = false;
//        String st = CommonUtil.convertObjToStr(tblPfIntDetails.getValueAt(tblPfIntDetails.getSelectedRow(), 0));
//        System.out.println("st ---------- :" + st);
//        String amt = txtTotAmt.getText();
//        System.out.println("amt ZZ :" + amt);
//        double totamt = CommonUtil.convertObjToDouble(amt);
//        if (st != null && st.equals("true")) {
//          //     table.setValueAt(new Boolean(flag), i, 0);
//            totamt += CommonUtil.convertObjToDouble(tblPfIntDetails.getValueAt(selRow, 3));
//            txtTotAmt.setText(CommonUtil.convertObjToStr(totamt));
//        } else {
//            totamt -= CommonUtil.convertObjToDouble(tblPfIntDetails.getValueAt(selRow, 3));
//            if (totamt > 0) {
//                txtTotAmt.setText(CommonUtil.convertObjToStr(totamt));
//            } else {
//                txtTotAmt.setText("");
//            }
//            chkSelectAll.setSelected(false);
//        }
//        long roundOffType = getRoundOffType(CommonUtil.convertObjToStr("Nearest Value"));
//        if (roundOffType != 0) {
//            totamt = rd.getNearest((long) (totamt * roundOffType), roundOffType) / roundOffType;
//        }
        
        calc();
        
    }//GEN-LAST:event_tblPfIntDetailsMouseClicked

    private void tblPfIntDetailsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblPfIntDetailsPropertyChange
        // TODO add your handling code here:
        calc();
    }//GEN-LAST:event_tblPfIntDetailsPropertyChange
       public String getAccHeadDesc(String accHeadID) {
        HashMap map1 = new HashMap();
        map1.put("ACCHD_ID", accHeadID);
        List list1 = ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        if (!list1.isEmpty()) {
            HashMap map2 = new HashMap();
            map2 = (HashMap) list1.get(0);
            String accHeadDesc = map2.get("AC_HD_DESC").toString();
            return accHeadDesc;
        } else {
            return "";
        }
    }    
    /** To populate Comboboxes */
    private void initComponentData() {
        cboPfType.setModel(observable.getCbmPfType());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
        System.out.println("viewType  :"+viewType);
        HashMap viewMap = new HashMap();
        HashMap whereMap =  new HashMap();
        whereMap.put("PF_TYPE",CommonUtil.convertObjToStr(observable.getCbmPfType().getKeyForSelected()));
        if(currAction.equals("FROMACC_NUM"))
        {
             viewMap.put(CommonConstants.MAP_NAME, "getEmployeeDetailsForPF");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        if(currAction.equals("TOACC_NUM"))
        {
             viewMap.put(CommonConstants.MAP_NAME, "getEmployeeDetailsForPF");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
         if (currAction.equals("INT_ACHD")) {
            viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
        }
        new ViewAll(this, viewMap).show();
    }

    
    /** Called by the Popup window created thru popUp method */
   public void fillData(Object map) {
       try{
           setModified(true);
       HashMap hash = (HashMap) map;
           System.out.println("hash-----------------:"+hash);
           if (viewType != null) {
               if (viewType.equals("FROMACC_NUM")) {
                   txtFromAccNo.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEEID")));
               }
               if (viewType.equals("TOACC_NUM")) {
                   txtToAccNo.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEEID")));
               }
               if (viewType.equals("INT_ACHD")) {
                   txtIntPaybleGLHead.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD")));
                   txtIntPaybleGLHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESCRIPTION")));
               }
           }
       } catch (Exception e) {
           log.error(e);
       }
       
   }
    
    
  private void enableDisable(boolean yesno){
      ClientUtil.enableDisable(this, yesno);
  }
    
   private void setButtonEnableDisable() {
       btnNew.setEnabled(!btnNew.isEnabled());
     btnEdit.setEnabled(!btnEdit.isEnabled());
//       btnDelete.setEnabled(!btnDelete.isEnabled());
       mitNew.setEnabled(btnNew.isEnabled());
       mitEdit.setEnabled(btnEdit.isEnabled());
//       mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
       btnCancel.setEnabled(!btnNew.isEnabled());
       mitSave.setEnabled(btnSave.isEnabled());
       mitCancel.setEnabled(btnCancel.isEnabled());
        
//        btnAuthorize.setEnabled(btnNew.isEnabled());
//      btnReject.setEnabled(btnNew.isEnabled());
//       btnException.setEnabled(btnNew.isEnabled());
       btnView.setEnabled(!btnView.isEnabled());
   }
    
    public void update(Observable observed, Object arg) {
        txtFromAccNo.setText(observable.getTxtFromNo());
        txtToAccNo.setText(observable.getTxtToNo());
        tdtFromDate.setDateValue(observable.getTxtFromDate());
        tdtToDate.setDateValue(observable.getTxtToDate());
        txtIntRate.setText(observable.getTxtIntRate());
        lblStatus.setText(observable.getLblStatus());
    }

    public void updateOBFields() {
        observable.setTxtFromDate(tdtFromDate.getDateValue());
        observable.setTxtToDate(tdtToDate.getDateValue());
        observable.setTxtFromNo(txtFromAccNo.getText());
        observable.setTxtToNo(txtToAccNo.getText());
        observable.setTxtIntRate(txtIntRate.getText());
    }
    
   

    private void savePerformed(){
        updateOBFields();
       // observable.doAction();
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
               
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("DIRECTOR_ID")) {
                      
                    }
                    displayTransDetail(observable.getProxyReturnMap());
                }
            }
            if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT) {
                // lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
            }
            // setEditLockMap(lockMap);
            ////  setEditLock();
            //  deletescreenLock();
        }
        
        observable.resetForm();
        //  observable.resetFormcpy();
        enableDisable(false);
        setButtonEnableDisable();
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.enableDisable(this, false);
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
        //__ Make the Screen Closable..
        setModified(false);
        ClientUtil.clearAll(this);
        observable.ttNotifyObservers();
//            System.out.println("save aaa");
//            updateOBFields();
//            observable.doAction() ;
//            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                HashMap lockMap = new HashMap();
//                ArrayList lst = new ArrayList();
////                lst.add("EMP_TRANSFER_ID");
////                lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
////                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
//                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//                    if (observable.getProxyReturnMap()!=null) {
////                        if (observable.getProxyReturnMap().containsKey("LEAVE_ID")) {
////                            //lockMap.put("EMP_TRANSFER_ID",observable.getProxyReturnMap().get("LEAVE_ID"));
////                        }
//                    }
//                }
////                if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
////                    lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
////                }
////                setEditLockMap(lockMap);
////                setEditLock();
////                deletescreenLock();
//            }
//            
//            observable.resetForm();
//  //          enableDisable(false);
////            setButtonEnableDisable();
//            lblStatus.setText(observable.getLblStatus());
//            ClientUtil.enableDisable(this, false);
//            observable.setResultStatus();
//            lblStatus.setText(observable.getLblStatus());
//            //__ Make the Screen Closable..
//            setModified(false);
//            ClientUtil.clearAll(this);
//            observable.ttNotifyObservers();
        }
    
   private void setFieldNames() {
      
    }
    
   private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        HashMap transIdMap = new HashMap();
        String actNum = "";
        for (int i=0; i<keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List)proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"CASH");
                }
                cashCount++;
                transType = "CASH";
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("SINGLE_TRANS_ID");
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                      transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                }
                transferCount++;
                transType = "TRANSFER";
            }
        }
        if(cashCount>0){
            displayStr+=cashDisplayStr;
        }
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        ClientUtil.showMessageWindow(""+displayStr);
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
           Date curDate1=(Date) CurDate.clone();
            paramMap.put("TransDt", curDate1);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            //Added By Suresh
            if(transType.equals("CASH")){
                paramMap.put("TransId", transId);
            ttIntgration.setParam(paramMap);
            String reportName = "";
                reportName = "DividentPayment";
            ttIntgration.integrationForPrint(reportName, false);
            }
            if(transType.equals("TRANSFER")){
                Object keys1[] = transIdMap.keySet().toArray();
                for (int i=0; i<keys1.length; i++) {
                    paramMap.put("TransId", keys1[i]);
                    ttIntgration.setParam(paramMap);
                    if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                        ttIntgration.integrationForPrint("ReceiptPayment");
                    }
                }
            }
        }
    }
       
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", ClientUtil.getCurrentDate());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnFromAccount;
    private com.see.truetransact.uicomponent.CButton btnIntPaybleGLHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnProcessData;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnToAccount;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewData;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboPfType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblFromAccNo;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblIntPaybleGLHead;
    private com.see.truetransact.uicomponent.CLabel lblIntRate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToAccNo;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panEmpTransfer;
    private com.see.truetransact.uicomponent.CPanel panIntDetails;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTranferDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType1;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpIntDetail;
    private com.see.truetransact.uicomponent.CTable tblPfIntDetails;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtFromAccNo;
    private com.see.truetransact.uicomponent.CTextField txtIntPaybleGLHead;
    private com.see.truetransact.uicomponent.CTextField txtIntPaybleGLHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntRate;
    private com.see.truetransact.uicomponent.CTextField txtToAccNo;
    private com.see.truetransact.uicomponent.CTextField txtTotAmt;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        PFIntersestApplicationUI dirBrd = new PFIntersestApplicationUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(dirBrd);
        j.show();
        dirBrd.show();
//        empTran.show();
    

}
    public class SimpleTableModel extends AbstractTableModel {

        private ArrayList dataVector;
        private ArrayList headingVector;

        public SimpleTableModel(ArrayList dataVector, ArrayList headingVector) {
            this.dataVector = dataVector;
            this.headingVector = headingVector;
        }

        public int getColumnCount() {
            return headingVector.size();
        }

        public int getRowCount() {
            return dataVector.size();
        }

        public Object getValueAt(int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            return rowVector.get(col);
        }

        public String getColumnName(int column) {
            return headingVector.get(column).toString();
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col == 0 && (CommonUtil.convertObjToStr(getValueAt(row, col + 3)).equals("Y"))) {
                return false;
            } else {
                if (col != 0) {
                    //return false;
                    if (col == 2 && (CommonUtil.convertObjToStr(getValueAt(row, col + 2)).equals("Y"))) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }

        }

        public void setValueAt(Object aValue, int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            rowVector.set(col, aValue);
        }
    }
}
