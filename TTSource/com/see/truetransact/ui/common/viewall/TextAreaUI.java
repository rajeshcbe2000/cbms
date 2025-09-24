/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TextAreaUI.java
 *
 * Created on March 6, 2012, 3:00 PM
 */

package com.see.truetransact.ui.common.viewall;

import com.see.truetransact.uicomponent.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TextAreaUI{
    private CDialog f; //Main frame
    private CTextArea ta; // Text area
    private CLabel ta1;
	private CScrollPane sbrText; // Scroll pane for text area
    private CScrollPane sbrText1;
    private CButton btnQuit; // Quit Program
    
    
    public TextAreaUI(CInternalFrame parent, String title,String data, byte[] photoByteArray){ //Constructor
        // Create Frame
        boolean showImg = false;
        f = new CDialog(parent, true);
        f.setTitle(title);
		f.getContentPane().setLayout(new FlowLayout());
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // Create Scrolling Text Area in Swing
        ta = new CTextArea();
        ta.setEditable(false);
        ta.setMinimumSize(new java.awt.Dimension(280,150));
        ta.setPreferredSize(new java.awt.Dimension(280,150));
		ta.setLineWrap(true);
		sbrText = new CScrollPane();
                sbrText.setViewportView(ta);
                
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sbrText.setMinimumSize(new java.awt.Dimension(280,150));
        sbrText.setPreferredSize(new java.awt.Dimension(280,150));
        
        // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
        ta1 = new CLabel();
        //ta1.setEditable(false);
        ta1.setMinimumSize(new java.awt.Dimension(280,150));
        ta1.setPreferredSize(new java.awt.Dimension(280,150));
		//ta1.setLineWrap(true);
		sbrText1 = new CScrollPane();
                sbrText1.setViewportView(ta1);
                
		sbrText1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sbrText1.setMinimumSize(new java.awt.Dimension(280,150));
        sbrText1.setPreferredSize(new java.awt.Dimension(280,150));
        
        //End
		// Create Quit Button
        btnQuit = new CButton();
        btnQuit.setText("OK");
        btnQuit.setFocusable(true);
        btnQuit.requestFocus(true); 
        btnQuit.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					f.dispose();   
				}
			}
		);
            
        setTxtData(data);        
        if (photoByteArray != null) {
            ta1.setIcon(new javax.swing.ImageIcon(photoByteArray));
            showImg = true;
        }else{
           ta1.setVisible(false); 
        }
         launchFrame(showImg);   
    }
    public void setTxtData(String data){
        ta.setText(data);
    }

    public void launchFrame(boolean showImg){ // Create Layout
        // Add text area and button to frame
		f.getContentPane().add(sbrText);
         if (showImg) { // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            f.getContentPane().add(sbrText1);
             }
        f.getContentPane().add(btnQuit);
		
		 // Close when the close button is clicked
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Display Frame
        f.pack(); // Adjusts frame to size of components
        f.show();
    }
    
    public static void main(String args[]){
//        TextAreaUI gui = new TextAreaUI("Particulares","       $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    }
}