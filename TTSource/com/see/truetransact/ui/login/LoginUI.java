/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * loginUI.java
 *
 * Created on October 20, 2003, 11:07 AM
 */

package com.see.truetransact.ui.login;

import java.util.HashMap;
import java.util.Date;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.passwordrules.PasswordRules;

import com.see.truetransact.ui.login.newpasswd.ChangePasswordUI;
import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;



/** This is the entry point to TT. Only by entering proper UserID & password an user
 * can log on to the system.
 * @author karthik, Bala
 *
 */
public class LoginUI extends com.see.truetransact.uicomponent.CDialog implements java.util.Observer{
  //  private static final long serialVersionUID = 1L;
    private BufferedImage bufferedImage=null;
    private HashMap authentication;
    private LoginOB observable;
    private boolean isShowable = true;
    private int wrongPwdCount = 0;
    private static TrueTransactMain frm = null;
    public static boolean loginVisible = false;
//    private boolean loginExpired = false;
    private boolean switchEnglish = false;
    private String language = "";
    private String country = "";
    private String oldLanguage = "";
    private String selectedLanguage = "";
    private String selectedCountry = "";
    private java.awt.event.AWTEventListener loginKeyListener;
    
   
    /** Creates new form LoginUI */

    public LoginUI() throws Exception{
        try {
   bufferedImage = ImageIO.read(getClass().getResource("/com/see/truetransact/ui/images/TT_core_banking_login.jpg"));
        //  loginVisible = true;
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");                  
            //Changed by sreekrishnan
            String osName = System.getProperty("os.name");            
            if (osName != null) {
                if (osName.indexOf("Windows") != -1) {
                    System.out.println("Windows Operating System!!!");
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
                }else{
                    System.out.println("Unix or Linux Operating System!!!");
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                }
            }
            //if (CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")) {
            //    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            //} else {
            //    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //}
            
            UIManager.put("ComboBox.disabledBackground", new java.awt.Color(220,220,220));
            UIManager.put("ComboBox.disabledForeground", new java.awt.Color(51,51,51));
            UIManager.put("TabbedPane.selectedForeground", java.awt.Color.blue);
            initComponents();
//            txtBank.setAllowAll(true);
//            txtBranch.setAllowAll(true);
//        if ((new java.util.Date()).getTime() > (1113180263046l + (20 * 24 * 60 * 60 * 1000))) {
//            throw new Exception ("License Expired.");
//        }
                        
     
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//      //  Determine the new location of the window
//        int w = this.getSize().width;
//        int h = this.getSize().height;
//        int x = ((dim.width - w) / 2);
//        int y = ((dim.height - h) / 2);
//        
//         //Move the window
//        this.setLocation(x, y);
          Image image = (new ImageIcon(bufferedImage)).getImage();
          //System.out.println("image:"+image);
          loginBG_jXImagePanel.setImage(image);
          
            observable = new LoginOB();
            observable.addObserver(this);
//            clearObservable();
            update(observable, null);
            com.see.truetransact.clientproxy.ProxyParameters.BRANCH_ID = txtBranch.getText();
            internationalize();
            loginKeyListener();
            HashMap cbmsMap = new HashMap();
            cbmsMap.put("CBMS_KEY", "RELEASE_VERSION");
            List list = ClientUtil.executeQuery("getSelectCbmsParameterValues", cbmsMap);
            if(list != null && list.size()>0){
                cbmsMap = (HashMap)list.get(0);
                lblVersion.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(cbmsMap.get("CBMS_VALUE"))));
            }
//            Toolkit.getDefaultToolkit().addAWTEventListener(
//                loginKeyListener, java.awt.AWTEvent.KEY_EVENT_MASK);
            
            
//        Toolkit.getDefaultToolkit().addAWTEventListener(new java.awt.event.AWTEventListener()
//        {
////            public void eventDispatched(java.awt.AWTEvent event)
////            {
////                String eventText = event.toString();
////                if(eventText.indexOf("PRESSED") != -1 || eventText.indexOf("RELEASED") != -1)
////                {
//////                    System.out.println("#### eventText : "+eventText);
////                    showLoginExpiry();
////                }
////            }
//                public void eventDispatched(java.awt.AWTEvent event)
//                {
//                    String eventText = event.toString();
//                    if (event instanceof java.awt.event.KeyEvent) {
//                        java.awt.event.KeyEvent keyEvent = (java.awt.event.KeyEvent) event;
////                        System.out.println("#### keyEvent.getKeyCode() : "+keyEvent.getKeyCode());
////                        System.out.println("#### keyEvent.getKeyText(123) : "+keyEvent.getKeyText(123));
//                        if (keyEvent.getKeyText(keyEvent.getKeyCode()).equals("F12") && eventText.indexOf("PRESSED") != -1) {
////                            System.out.println("#### true : ");
//                            System.out.println("#### true : ");
//                            switchEnglish = !switchEnglish;
//                            internationalize();
//                        }
//                    }
//                }
//        }, java.awt.AWTEvent.MOUSE_EVENT_MASK + java.awt.AWTEvent.KEY_EVENT_MASK);
        
//        lblDate.setVisible(false);
//        lblCurrDate.setVisible(false);
            
        } catch (Exception exc) {
            isShowable = false;
            exc.printStackTrace();
//            Toolkit.getDefaultToolkit().removeAWTEventListener(loginKeyListener);
            this.dispose();
            System.exit(0);
        }
    }
    
    private void  clearObservable(){
        observable.setTxtUserId("");
        observable.setPwdPassword("");
        observable.setTxtBank("");
        observable.setTxtBranch("");
    }
    
    private void loginKeyListener() {
        loginKeyListener = new java.awt.event.AWTEventListener() {
            public void eventDispatched(java.awt.AWTEvent event)
            {
                String eventText = event.toString();
                if (event instanceof java.awt.event.KeyEvent) {
                    java.awt.event.KeyEvent keyEvent = (java.awt.event.KeyEvent) event;
//                        System.out.println("#### keyEvent.getKeyCode() : "+keyEvent.getKeyCode());
//                        System.out.println("#### keyEvent.getKeyText(123) : "+keyEvent.getKeyText(123));
                    if (keyEvent.getKeyText(keyEvent.getKeyCode()).equals("F12") && eventText.indexOf("PRESSED") != -1) {
//                            System.out.println("#### true : ");
                        //System.out.println("#### true : ");
                        switchEnglish = !switchEnglish;
                        internationalize();
                    }
                }
            }
        };
    }
    
    /** Creates new form LoginUI */
    public LoginUI(boolean loginExpired) throws Exception {
        //        this.loginExpired = loginExpired;
        try {
            LoginExpiryUI loginExpiryUI = new LoginExpiryUI(frm);
            frm.showDialog(loginExpiryUI);
        } catch (Exception exc) {
            isShowable = false;
            exc.printStackTrace();
//            Toolkit.getDefaultToolkit().removeAWTEventListener(loginKeyListener);
            this.dispose();
            System.exit(0);
        }
    }
    
    /** To set the authentication value. */
    public void setAuthentication(HashMap authentication){
        this.authentication = authentication;
    }
    
    /** To get the authentication value. This is used by Main screen to load based on
     * the value
     */
    public HashMap getAuthentication(){
        return this.authentication;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        loginBG_jXImagePanel = new org.jdesktop.swingx.JXImagePanel();
        lblUserID = new javax.swing.JLabel();
        txtUserID = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        lblBranch = new javax.swing.JLabel();
        txtBank = new javax.swing.JTextField();
        txtBranch = new javax.swing.JTextField();
        pwdPassword = new javax.swing.JPasswordField();
        btnLogon = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblVersion = new com.see.truetransact.uicomponent.CLabel();
        lblSplashImg = new com.see.truetransact.uicomponent.CLabel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CBMS++ Login");
        setMinimumSize(new java.awt.Dimension(755, 315));
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(null);

        loginBG_jXImagePanel.setBackground(new java.awt.Color(255, 255, 255));
        loginBG_jXImagePanel.setMaximumSize(new java.awt.Dimension(749, 301));
        loginBG_jXImagePanel.setMinimumSize(new java.awt.Dimension(749, 301));
        loginBG_jXImagePanel.setPreferredSize(new java.awt.Dimension(749, 301));
        loginBG_jXImagePanel.setLayout(null);

        lblUserID.setText("  UserID");
        loginBG_jXImagePanel.add(lblUserID);
        lblUserID.setBounds(460, 80, 50, 14);

        txtUserID.setMaximumSize(new java.awt.Dimension(160, 20));
        txtUserID.setMinimumSize(new java.awt.Dimension(160, 20));
        txtUserID.setPreferredSize(new java.awt.Dimension(160, 20));
        loginBG_jXImagePanel.add(txtUserID);
        txtUserID.setBounds(520, 80, 160, 20);

        lblPassword.setText("        Password");
        loginBG_jXImagePanel.add(lblPassword);
        lblPassword.setBounds(440, 110, 70, 20);

        lblBranch.setText("         Bank & Branch");
        loginBG_jXImagePanel.add(lblBranch);
        lblBranch.setBounds(420, 140, 100, 14);

        txtBank.setMaximumSize(new java.awt.Dimension(90, 20));
        txtBank.setMinimumSize(new java.awt.Dimension(90, 20));
        txtBank.setPreferredSize(new java.awt.Dimension(90, 20));
        loginBG_jXImagePanel.add(txtBank);
        txtBank.setBounds(520, 140, 90, 20);

        txtBranch.setMaximumSize(new java.awt.Dimension(60, 20));
        txtBranch.setMinimumSize(new java.awt.Dimension(60, 20));
        txtBranch.setPreferredSize(new java.awt.Dimension(60, 20));
        loginBG_jXImagePanel.add(txtBranch);
        txtBranch.setBounds(620, 140, 60, 20);

        pwdPassword.setMaximumSize(new java.awt.Dimension(160, 20));
        pwdPassword.setMinimumSize(new java.awt.Dimension(160, 20));
        pwdPassword.setPreferredSize(new java.awt.Dimension(160, 20));
        loginBG_jXImagePanel.add(pwdPassword);
        pwdPassword.setBounds(520, 110, 160, 20);

        btnLogon.setText("Log on");
        btnLogon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogonActionPerformed(evt);
            }
        });
        loginBG_jXImagePanel.add(btnLogon);
        btnLogon.setBounds(520, 170, 80, 27);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        loginBG_jXImagePanel.add(btnCancel);
        btnCancel.setBounds(610, 170, 80, 27);

        lblVersion.setText("9.2.2.9");
        loginBG_jXImagePanel.add(lblVersion);
        lblVersion.setBounds(260, 150, 70, 18);

        lblSplashImg.setBackground(new java.awt.Color(34, 104, 149));
        lblSplashImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_core_banking_login.jpg"))); // NOI18N
        lblSplashImg.setMaximumSize(new java.awt.Dimension(794, 290));
        lblSplashImg.setMinimumSize(new java.awt.Dimension(794, 290));
        lblSplashImg.setOpaque(true);
        loginBG_jXImagePanel.add(lblSplashImg);
        lblSplashImg.setBounds(0, 0, 750, 300);

        getContentPane().add(loginBG_jXImagePanel);
        loginBG_jXImagePanel.setBounds(0, 0, 749, 301);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void show() {
        if (isShowable)
            super.show();
    }
        private void pwdPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdPasswordActionPerformed
            // Add your handling code here:
            btnLogonActionPerformed(evt);
    }//GEN-LAST:event_pwdPasswordActionPerformed
            private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
                // Add your handling code here:
                HashMap userMap = new HashMap();
                userMap.put("LOGIN_STATUS", "CANCEL");
                this.setAuthentication(userMap);
                loginVisible = false;
                System.exit(0);
    }//GEN-LAST:event_btnCancelActionPerformed
    @SuppressWarnings("unchecked")
    private void btnLogonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogonActionPerformed
        try{
        //System.out.println("1 btnLogon Pressed...");
        if (/*txtBank.getText().length()>0 && */txtBranch.getText().length()>0) {
            //System.out.println("2 Inside If..."+txtBranch.getText());
            updateUIFields();
            //System.out.println("3 After updateUIFields()...");
            ProxyParameters.BANK_ID = txtBank.getText();
            ProxyParameters.BRANCH_ID = txtBranch.getText();
            PasswordRules rule = new PasswordRules();
            //System.out.println("4 After PasswordRules() object creation...");
            ConfigPasswordTO objRule = rule.getPasswordConfig();
            //System.out.println("5 After rule.getPasswordConfig() object creation...");
            java.util.List list = ClientUtil.executeQuery("getServerTime", new HashMap());
            //System.out.println("6 After ClientUtil.executeQuery...");
            //Date amcFromDt = objRule.getAmcFromDt();
            TrueTransactMain.amcToDate = objRule.getAmcToDt();
            int amcAlertTime = CommonUtil.convertObjToInt(objRule.getAmcAlertTime());
            TrueTransactMain.amcAlertTime = amcAlertTime;
			TrueTransactMain.pendingTxnAllowedDays = CommonUtil.convertObjToInt(objRule.getPendingTxnAllowedDays());
                        if(objRule.getAmcToDt() != null && amcAlertTime >0 && DateUtil.dateDiff(objRule.getAmcToDt(), ClientUtil.getCurrentDate())>0){
                ClientUtil.showAlertWindow("Product usage period is over, Please contact Administrator");
            }
            if (list!=null && list.size()>0) {
                Date serverDt = (Date) list.get(0);
                Date currDt = new Date();
                java.util.GregorianCalendar source = new java.util.GregorianCalendar();
                source.setTime(serverDt);
                
                java.util.GregorianCalendar target = new java.util.GregorianCalendar();
                target.setTime(currDt);
                
                //            long days = (target.getTimeInMillis() - source.getTimeInMillis()) / (24*60);
                long diff = target.getTimeInMillis() - source.getTimeInMillis();
                double diffDaysInFloat = diff / (24 * 60 * 60 * 1000.0);
                double diffHoursInFloat = diff / (60 * 60 * 1000.0);
                double diffMinutesInFloat = diff / (60 * 1000.0);
                double diffSecondsInFloat = diff / 1000.0;
                long diffDaysInLong = diff / (24 * 60 * 60 * 1000);
                long diffHoursInLong = diff / (60 * 60 * 1000);
                long diffMinutesInLong = diff / (60 * 1000);
                long diffSecondsInLong = diff / 1000;
                
                long diffDays = diffDaysInLong;
                long diffHours = (long)((diffDaysInFloat - diffDaysInLong) * 24);
                long diffMinutes = (long)((diffHoursInFloat - diffHoursInLong) * 60);
                long diffSeconds = (long)((diffMinutesInFloat - diffMinutesInLong) * 60);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                String negativeDaysChar = "";
                String negativeHoursChar = "";
                String negativeMinutesChar = "";
                String negativeSecondsChar = "";
                if (diffDays<0) {
                    negativeDaysChar = "-";
                }
                if (diffHours<0) {
                    negativeHoursChar = "-";
                }
                if (diffMinutes<0) {
                    negativeMinutesChar = "-";
                }
                if (diffSeconds<0) {
                    negativeSecondsChar = "-";
                }
                diffDays = Math.abs(diffDays);
                diffHours = Math.abs(diffHours);
                diffMinutes = Math.abs(diffMinutes);
                diffSeconds = Math.abs(diffSeconds);
                if (diffDays>0 || diffHours>0 || diffMinutes>5) {
//                    ClientUtil.showMessageWindow("<html>Server time is : <b>"+dateFormat.format(serverDt)+
//                    "</b><br>Difference is (dd days hh:mm:ss) : "+negativeDaysChar+diffDays+" days "+
//                    negativeHoursChar+CommonUtil.lpad(String.valueOf(diffHours), 2, '0')+":"+
//                    negativeMinutesChar+CommonUtil.lpad(String.valueOf(diffMinutes), 2, '0')+":"+
//                    negativeSecondsChar+CommonUtil.lpad(String.valueOf(diffSeconds), 2, '0')+
//                    "<br><br>Change the system date & time to server date & time</html>");
                    list = null;
//                    System.exit(0);
                }
            }
            if (wrongPwdCount >=(CommonUtil.convertObjToInt(objRule.getNoOfAttempts()))) {
                HashMap userMap = new HashMap();
                userMap.put(CommonConstants.USER_ID, observable.getTxtUserId());
                userMap.put(CommonConstants.BANK_ID, observable.getTxtBank());
                userMap.put(CommonConstants.BRANCH_ID, observable.getTxtBranch());
                //System.out.println("userMap : " + userMap);
                ClientUtil.execute("lockUserAccount", userMap);
                com.see.truetransact.uicomponent.COptionPane.showMessageDialog(this, "User is temporarily suspended.");
                System.exit(0);
            }
            
            //System.out.println("txtUserID.getText():" + txtUserID.getText());
            //System.out.println("observable.txtUserID:" + observable.getTxtUserId());
            HashMap USERINFO = observable.validateUser();
            String dbDriverName = "";
            String sessionId = "";
            if (USERINFO != null && USERINFO.containsKey("DB_DRIVER_NAME")) {
                dbDriverName = (String) USERINFO.get("DB_DRIVER_NAME");
            }
            if (USERINFO != null && USERINFO.containsKey(CommonConstants.SESSION_ID)) {
                sessionId = (String) USERINFO.get(CommonConstants.SESSION_ID);
            }
            if( USERINFO != null ){
                USERINFO = (HashMap) ((java.util.ArrayList) USERINFO.get("LoginDetails")).get(0);
                //System.out.println("User info " + USERINFO);
                USERINFO.put(CommonConstants.HEAD_OFFICE, objRule.getCboBranches());
                USERINFO.put(CommonConstants.SESSION_ID, sessionId);
                setAuthentication(USERINFO);
                wrongPwdCount = 0;
                USERINFO.put("DB_DRIVER_NAME", dbDriverName);
            } else {
                wrongPwdCount++;
                setAuthentication(null);
            }
            //        if(USERINFO != null ){
            //            String loginStatus = (String) USERINFO.get("LOGIN_STATUS");
            //            if (loginStatus != null && loginStatus.equalsIgnoreCase("CANCEL")) {
            //                System.exit(0);
    /*            } else if (loginStatus != null && loginStatus.equalsIgnoreCase("LOGIN")) {
                    com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "User already logged in");
                    System.exit(0);
                } else if ( loginStatus != null &&
                            loginStatus.equalsIgnoreCase("LOGOUT") &&
                            DateUtil.getStringDate(
                                (Date) USERINFO.get("LAST_LOGOUT_DT")).equals(
                                    DateUtil.getStringDate(new Date()))) {
                    com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Transaction is not allowed.");
                    System.exit(0);*/
            //            } else {
            if( USERINFO != null ){
                if (frm==null) {
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    frm = new TrueTransactMain();
                    Dimension frameSize = frm.getSize();
                    frm.setSize(screenSize.width, screenSize.height-30);
                    Date currDt = new Date();
                    USERINFO.put("CURR_DATE", currDt);
                    frm.setUserInfo(USERINFO);
                    
                    HashMap whereMap = new HashMap();
                    whereMap.put("USERID", USERINFO.get(CommonConstants.USER_ID).toString());
                    try{
                        whereMap.put(CommonConstants.IP_ADDR, java.net.InetAddress.getLocalHost().getHostAddress());
                    }
                    catch(Exception e){
                        System.out.println("IP_ADDR");
                    }
                    whereMap.put("CURR_DATE", currDt);
                    ClientUtil.execute("updateUserLoginStatus", whereMap);
                    
                    HashMap info = (HashMap) ClientUtil.executeQuery("getBranchUserInfo", whereMap).get(0);
                    frm.setBranchInfo(info);
                    
                    info = (HashMap) ClientUtil.executeQuery("getSelectBankTOList", null).get(0);
                    frm.setBankInfo(info);
                    info.put("YEAREND_PROCESS_DT", objRule.getYearEndProcessDt());
//                    System.out.println("#$#$ BANKINFO:"+frm.BANKINFO);
                    
                    whereMap.clear();
                    whereMap.put("GROUP_ID", (String) USERINFO.get("USER_GROUP"));
                    whereMap.put("ROLE_ID", (String) USERINFO.get("USER_ROLE"));
                    
                    info = (HashMap) ClientUtil.executeQuery("getSelectRoleMasterTOList", whereMap).get(0);
                    frm.setRoleInfo(info);
                    
                    list = ClientUtil.executeQuery("getSelectCbmsParameterValues", null);
                    if(list != null && list.size()>0){
                        HashMap cbmsParameterMap = new HashMap();
                        for(int i=0;i<list.size();i++){
                            HashMap cbmsMap = (HashMap)list.get(i);
                            cbmsParameterMap.put(CommonUtil.convertObjToStr(cbmsMap.get("CBMS_KEY")),CommonUtil.convertObjToStr(cbmsMap.get("CBMS_VALUE")));
                        }
                        frm.setCBMSPARAMETERS(cbmsParameterMap);
                    }
                    List searchPathList = ClientUtil.executeQuery("getSearchPath", null);//Added by Revathi 09-11-2023 for identify Postgres DB searchpath refer by Rajesh

//                    String level_Id=CommonUtil.convertObjToStr(info.get("LEVEL_ID"));
//                    System.out.println("level_Id:::::"+level_Id);
                    HashMap level= new HashMap();
                    level=(HashMap)ClientUtil.executeQuery("getSelectLevelMasterTO1", info).get(0);
                    frm.setLEVEL_ID(level);
                    frm.setDayEndType(objRule.getDayEndType());
                    frm.setlogOutTime(CommonUtil.convertObjToDouble(objRule.getLogOutTime()));
                    frm.startMain();
                    //            frm.show();
                    
                    PasswordRules rules = new PasswordRules();
                    ConfigPasswordTO objRules = rules.getPasswordConfig();
                    
                    if (objRules != null ) {
                        //System.out.println("@@@@objRules"+objRules);
//                      frm.setPanAmount(CommonUtil.convertObjToDouble(objRules.getPanAmount()).doubleValue());
                        TrueTransactMain.language = selectedLanguage;
                        TrueTransactMain.country = selectedCountry;
                        TrueTransactMain.CASHIER_AUTH_ALLOWED=CommonUtil.convertObjToStr(objRules.getCashierAuthAllowed());
                        TrueTransactMain.MULTI_SHARE_ALLOWED=CommonUtil.convertObjToStr(objRules.getMultiShareAllowed());
                        TrueTransactMain.TOKEN_NO_REQ = CommonUtil.convertObjToStr(objRules.getTokenNoReq());
                        TrueTransactMain.SERVICE_TAX_REQ=CommonUtil.convertObjToStr(objRules.getServiceTaxReq());
                        TrueTransactMain.GAHAN_PERIOD=CommonUtil.convertObjToInt(objRules.getGahanPeriod());
                        TrueTransactMain.SENIOR_CITIZEN_AGE=CommonUtil.convertObjToInt(objRules.getSeniorCitizenAge());
                    //System.out.println("TrueTransactMain.CASHIER_AUTH_ALLOWED==="+TrueTransactMain.CASHIER_AUTH_ALLOWED);
                        TrueTransactMain.PANAMT=CommonUtil.convertObjToDouble(objRules.getPanAmount()).doubleValue();
                      Date lastPwdDt = null;
                        Date nextPwdDt = null;
                        if (USERINFO.get("LAST_PWD_CHANGE") != null) {
                            lastPwdDt = (Date) USERINFO.get("LAST_PWD_CHANGE");
                            
                            if (CommonUtil.convertObjToInt(objRules.getPasswordExpiry()) > 0) {
                                nextPwdDt = DateUtil.addDays(lastPwdDt, objRules.getPasswordExpiry().intValue());
                            }
                        }
                        
                        if ( ( lastPwdDt == null && CommonUtil.convertObjToStr(objRules.getChangePwdFirst()).equals("T")) ||
                        ( nextPwdDt != null && ( nextPwdDt.before(ClientUtil.getCurrentDate()) ||
                        nextPwdDt.equals(ClientUtil.getCurrentDate())))) {
                            frm.showDialog(new ChangePasswordUI());
                            //FOR IMIDATE LOGOUTFOR THOS CODE
                            HashMap whereMap1=new HashMap();
                            whereMap1.put("USERID", txtUserID.getText());
                            whereMap1.put("BRANCHCODE", txtBranch.getText());
                            whereMap1.put("LOGINSTATUS", "LOGOUT");
                            whereMap1.put("DATE_TIME", new Date());
                            ClientUtil.execute("loginHistory", whereMap1);
                            System.exit(0);
                            //frm.show();
                            
                        } else {
                            frm.show();
                        }
                    } else {
                        frm.show();
                    }
                    loginVisible = false;
//                    Toolkit.getDefaultToolkit().removeAWTEventListener(loginKeyListener);
                    this.dispose();
                } else {
//                    Toolkit.getDefaultToolkit().removeAWTEventListener(loginKeyListener);
                    this.dispose();
                    frm.show();
                    loginVisible = false;
                }
            }
        }
        }catch(Exception e){
            e.printStackTrace();
            
        }
        //        if (loginExpired) {
        //            HashMap whereMap = new HashMap();
        //            whereMap.put("USERID", USERINFO.get(CommonConstants.USER_ID).toString());
        //            try{
        //                whereMap.put(CommonConstants.IP_ADDR, java.net.InetAddress.getLocalHost().getHostAddress());
        //            }
        //            catch(Exception e){
        //                System.out.println("IP_ADDR");
        //            }
        //            Date currDt = new Date();
        //            whereMap.put("CURR_DATE", currDt);
        //            ClientUtil.execute("updateUserLoginStatus", whereMap);
        //        }
    }//GEN-LAST:event_btnLogonActionPerformed
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        loginVisible = false;
        System.exit(0);
    }//GEN-LAST:event_exitForm
    private void internationalize() {
//        final LoginRB resourceBundle = new LoginRB();
//        lblUserID.setText(resourceBundle.getString("lblUserID"));
//        lblPassword.setText(resourceBundle.getString("lblPassword"));
//        lblDate.setText(resourceBundle.getString("lblDate"));
//        lblBranch.setText(resourceBundle.getString("lblBranch"));
////        lblCurrDate.setText(DateUtil.getStringDate(ClientUtil.getCurrentDate()));
//        btnLogon.setText(resourceBundle.getString("btnLogon"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
        String[] obj = {"English","Malayalam",
                "Hindi"};//, "Tamil"};
                //showInternalInputDialog
        String option = "English";
        if (oldLanguage.equals("")) {
            option = (String)javax.swing.JOptionPane.showInputDialog(this, "Select a Language", "Language", 3, null, obj, "English");
            if (option == null) {
                System.out.println("Application Terminated...!!!");
                System.exit(0);
            }
            setLanguageAndCountry(option, 1);
            //System.out.println("#@$# selectedLanguage : "+selectedLanguage+" / selectedCountry : "+selectedCountry);
            oldLanguage = option;
        } else {
            option = oldLanguage;
        }
        if (switchEnglish) {
            option = "English";
        }
        setLanguageAndCountry(option, 2);
        //System.out.println("#@$# language : "+language+" / country : "+country);
        final java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.login.LoginRB", new java.util.Locale(language, country));

        lblUserID.setText(resourceBundle.getString("lblUserID"));
        lblPassword.setText(resourceBundle.getString("lblPassword"));
//        lblDate.setText(resourceBundle.getString("lblDate"));
        lblBranch.setText(resourceBundle.getString("lblBranch"));
        btnLogon.setText(resourceBundle.getString("btnLogon"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        ClientUtil.setLanguage(this, resourceBundle.getLocale());        
    }
    
    private void setLanguageAndCountry(String option, int opt) {
        String coun = "";
        String lang = "";
        if (option.equals("English") ) {
            coun = "US";
            lang = "en";
        } else if (option.equals("Hindi") ) {
            coun = "IN";
            lang = "hi";
        } else if (option.equals("Tamil") ) {
            coun = "IN";
            lang = "ta";
        } else if (option.equals("Malayalam") ) {
            coun = "IN";
            lang = "ml";
        }
        //System.out.println("#@$# lang : "+lang+" / coun : "+coun);
        if (opt==1) {
            this.selectedLanguage = lang;
            this.selectedCountry = coun;
        } else if(opt==2 || opt==3) {
            this.language = lang;
            this.country = coun;
        }
    }
    
    public void update(java.util.Observable o, Object arg) {
        txtUserID.setText(observable.getTxtUserId());
        pwdPassword.setText(observable.getPwdPassword());
        txtBank.setText(observable.getTxtBank());
        txtBranch.setText(observable.getTxtBranch());
    }
    
    public void updateUIFields() {
        observable.setTxtUserId(txtUserID.getText());
        observable.setPwdPassword(new String(pwdPassword.getPassword()));
        observable.setTxtBank(txtBank.getText());
        observable.setTxtBranch(txtBranch.getText().toUpperCase());
    }
    
    public static void main(String str[]) throws Exception {
        LoginUI loginUI = new LoginUI();
        
        Dimension frameSize = loginUI.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        if (frameSize.height > screenSize.height){
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width){
            frameSize.width = screenSize.width;
        }
        loginUI.setLocation((screenSize.width - frameSize.width) / 2,
        (screenSize.height - frameSize.height) / 2 - 60);
        loginUI.show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnLogon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblBranch;
    private javax.swing.JLabel lblPassword;
    private com.see.truetransact.uicomponent.CLabel lblSplashImg;
    private javax.swing.JLabel lblUserID;
    private com.see.truetransact.uicomponent.CLabel lblVersion;
    private org.jdesktop.swingx.JXImagePanel loginBG_jXImagePanel;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JTextField txtBank;
    private javax.swing.JTextField txtBranch;
    private javax.swing.JTextField txtUserID;
    // End of variables declaration//GEN-END:variables
}
