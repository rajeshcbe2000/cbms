/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TextUI.java
 *
 * Created on March 14, 2012, 11:32 AM
 */

package com.see.truetransact.ui.common.viewall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CScrollPane;
import com.see.truetransact.uicomponent.CButton;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uivalidation.CurrencyValidation;
import  com.see.truetransact.uicomponent.CInternalFrame;
import  com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.deposit.interestapplication.DepositInterestApplicationUI;
import com.see.truetransact.clientutil.ClientUtil;

public class TextUI{
    private CDialog dialogUI; //Main frame
    private CTextField ta; // TextField
    private CLabel showLabel;
    private CButton btnQuit; // Quit Program
    private GridBagConstraints gridBagConstraints;
    private CPanel  panel;
    private CInternalFrame objCInternalFrame;
    private TransDetailsUI objTransDetailsUI;
    private DepositInterestApplicationUI objDepositInterestApplicationUI;
    private String sourceFrom ="";
    private HashMap allMap;
    public TextUI(CInternalFrame sourceFrame,Object objTransDetailsUI,HashMap dataMap){ //Constructor
        // Create Frame.
        if(allMap==null)
            allMap=new HashMap();
        this.allMap=dataMap;
//        System.out.println("######## dataMap :"+dataMap);
        String title=CommonUtil.convertObjToStr(dataMap.get("TITLE"));
        Double calcAmt=CommonUtil.convertObjToDouble(dataMap.get("CALCULATED_AMT"));
        Double tolerance_amt=CommonUtil.convertObjToDouble(dataMap.get("TOLERANCE_AMT"));
        Double selected_amt=CommonUtil.convertObjToDouble(dataMap.get("SELECTED_AMT"));
//        System.out.println("######## tolerance_amt  :"+tolerance_amt);
//        System.out.println("######## calcAmt        :"+calcAmt);
//        System.out.println("######## selected_amt   :"+selected_amt);
        if(sourceFrame !=null)
            this.objCInternalFrame=sourceFrame;
        dialogUI = new CDialog(objCInternalFrame,true);
        dialogUI.setTitle(title);
        if(objTransDetailsUI instanceof CPanel)
            this.objTransDetailsUI=(TransDetailsUI)objTransDetailsUI;
        sourceFrom=title;
        panel=new CPanel();
        panel.setLayout(new java.awt.GridBagLayout());

        panel.setBorder(new javax.swing.border.TitledBorder(title));
        panel.setMaximumSize(new java.awt.Dimension(250, 120));
        panel.setMinimumSize(new java.awt.Dimension(250, 120));
        panel.setName("panTransDetail");
        panel.setPreferredSize(new java.awt.Dimension(250, 120));
        
        dialogUI.setMaximumSize(new java.awt.Dimension(360,150));
         dialogUI.setPreferredSize(new java.awt.Dimension(380,150));
        dialogUI.setMinimumSize(new java.awt.Dimension(380,150));
        dialogUI.setTitle(title);
	dialogUI.getContentPane().setLayout(new GridBagLayout());
        
        // Create Scrolling Text Area in Swing
                
        showLabel = new CLabel();
        showLabel.setText(title);
      
        showLabel.setMinimumSize(new java.awt.Dimension(200,21));
        showLabel.setPreferredSize(new java.awt.Dimension(200,21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panel.add(showLabel,gridBagConstraints);
        
        
        ta = new CTextField();
        ta.setValidation(new CurrencyValidation(14, 2));
        ta.setMinimumSize(new java.awt.Dimension(100,21));
        ta.setPreferredSize(new java.awt.Dimension(100,21));
       
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panel.add(ta,gridBagConstraints);
        
        btnQuit = new CButton();
       btnQuit.setText("OK");
        btnQuit.setFocusable(true);
        btnQuit.setMinimumSize(new java.awt.Dimension(100,21));
        btnQuit.setPreferredSize(new java.awt.Dimension(100,21));
        
        btnQuit.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					btnQuitActionPerformed(e);
				}
			}
		);
       
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panel.add(btnQuit,gridBagConstraints); 
		// Create Quit Button
        btnQuit = new CButton();
        btnQuit.setText("OK");
        btnQuit.setFocusable(true);
          btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               btnQuitActionPerformed(evt);
            }
        });
        launchFrame();        
        setTxtData(String.valueOf(selected_amt));
        dialogUI.show();
    }
    
    private boolean calToleranceAmt(String changedAmt){
         Double calcAmt=CommonUtil.convertObjToDouble(allMap.get("CALCULATED_AMT"));
        Double tolerance_amt=CommonUtil.convertObjToDouble(allMap.get("TOLERANCE_AMT"));
        double selected_amt=Double.parseDouble(changedAmt);
//        System.out.println("###### tolerance_amt :"+tolerance_amt);
//        System.out.println("###### Entered Amt   :"+selected_amt);
//        System.out.println("###### calcAmt       :"+calcAmt);
        double minTolerance=calcAmt.doubleValue()-tolerance_amt.doubleValue();
        if(minTolerance<0)
            minTolerance=0;
         double maxTolerance=calcAmt.doubleValue()+tolerance_amt.doubleValue();
         double toaldemand=0.0;
         double clearBal=0.0;
         if(!allMap.get("TITLE").equals("Other Bank Interest")&&!allMap.get("TITLE").equals("Other Bank Penal")){
         if(allMap.containsKey("TOTAL_DEMAND")){
             toaldemand=CommonUtil.convertObjToDouble(allMap.get("TOTAL_DEMAND")).doubleValue();
             double clearbal=CommonUtil.convertObjToDouble(allMap.get("CLEAR_BALANCE")).doubleValue();
             if (clearbal<0) {
                 clearbal=clearbal*-1;
             }
             if(selected_amt>toaldemand && selected_amt>clearbal){
                 ClientUtil.showMessageWindow("Changing Amount should be greater than Rs. "+clearBal);
                 ta.setText(String.valueOf(calcAmt));
                 return false;
             } else if(minTolerance > selected_amt || maxTolerance< selected_amt){
                 ClientUtil.showMessageWindow("Minimum Changing Amount should be greater than or equal to  Rs. "+minTolerance+"  &  "+"Maximum Changing Amount should be less than or equal to Rs. "+maxTolerance);
                 ta.setText(String.valueOf(calcAmt));
                 return false;
             }
         }
         else if(minTolerance > selected_amt || maxTolerance< selected_amt){
            ClientUtil.showMessageWindow("Minimum Changing Amount should be greater than or equal to  Rs. "+minTolerance+"  &  "+"Maximum Changing Amount should be less than or equal to Rs. "+maxTolerance);
            ta.setText(String.valueOf(calcAmt));
            return false;
        }}
         return true;
    }
    
    private void taFocusLost(java.awt.event.FocusEvent evt) {
//        System.out.println(" txt focus lost");
    }
         
   private void  btnQuitActionPerformed(ActionEvent e){
       System.out.println("calling parent   ");
      if(! calToleranceAmt(ta.getText()))
	 return;
     if(objTransDetailsUI instanceof CPanel)
       objTransDetailsUI.modifyTransDatas(this);
     if(objCInternalFrame instanceof CInternalFrame)
         objCInternalFrame.modifyTransData(this);
        dialogUI.dispose();  
    }
    
    public void setTxtData(String data){
        ta.setText(data);
    }
    
     public String  getTxtData(){
      return ta.getText();
    }

    public void launchFrame(){ // Create Layout
        dialogUI.getContentPane().add(panel);
        dialogUI.pack(); // Adjusts frame to size of components
    }
    
    public static void main(String args[]){
    }
}

