/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * MandatoryConfiguration.java
 *
 * Created on July 24, 2003, 2:59 PM
 */

package com.see.truetransact.uimandatory;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.io.FileWriter;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import com.see.truetransact.uicomponent.CScrollPane;
import com.see.truetransact.uicomponent.CPanel;
import com.see.adminsupport.AdminConstants;
import com.see.truetransact.uicomponent.COptionPane;
/** This class is used to configure the mandatory fields of a screen, by populating
 * the input elements of the screen from a HashMap & providing radiobutton based
 * selection
 * @author karthik
 */
public class MandatoryConfiguration extends javax.swing.JFrame{
    private UIMandatoryHashMap uiMandatoryHashMap;
    // private MandatoryDemo mandatoryDemo;
    private UIMandatoryField uiMandatoryField;
    
    private HashMap mandatoryMap;
    private Set mandatoryKeySet;
    private Iterator mandatoryKeyIterator;
    
    private CScrollPane cScrollPane1;
    private CPanel cPanel1;
    private JLabel[] jLabels;
    private ButtonGroup[] jButtonGroups;
    private JRadioButton[] jRadioButtons;
    private JButton jButtonSubmit;
    private int mandatoryKeysLength;
    private String className;
    private String filePath;
    
    /** Creates a new instance of MandatoryConfiguration */
    public MandatoryConfiguration(String className, String filePath) {
        this.className = className;
        this.filePath = filePath;
        refreshMandatoryHashMap();
        initComponents();
    }
    
    /** Load the UI class & MandatoryHashMap class of UI. Compare both the HashMaps &
     * update with the help of setMandatoryMap() method
     */
    private void refreshMandatoryHashMap(){
        //className = "com.see.truetransact.uimandatory.MandatoryDemo";
        try{
            uiMandatoryField = (UIMandatoryField) Class.forName(className).newInstance();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            uiMandatoryHashMap = (UIMandatoryHashMap) Class.forName(className.substring(0,className.length()-2)+"HashMap").newInstance();
        }catch(Exception e){
            System.out.println("Mandatory HashMap is null");
        }
        
        setMandatoryMap();
        mandatoryKeySet = mandatoryMap.keySet();
        mandatoryKeyIterator = mandatoryKeySet.iterator();
        mandatoryKeysLength = mandatoryKeySet.size();
    }
    
    /** This is called from refreshMandatoryHashMap() to check whether HashMap in UI
     * Class & MandatoryHashMap class for UI matches. If not create a new
     * MandatoryHashMap
     */
    private void setMandatoryMap(){
        mandatoryMap = uiMandatoryField.getMandatoryHashMap();
        if( uiMandatoryHashMap != null ){
            System.out.println("uiMandatoryHashMap exists");
            final HashMap fileMap = uiMandatoryHashMap.getMandatoryHashMap();
            HashMap screenHashMap = mandatoryMap;
            Set screenHashMapKeySet = screenHashMap.keySet();
            Iterator screenHashMapKeyIterator = screenHashMapKeySet.iterator();
            int screenHashMapKeysLength = screenHashMapKeySet.size();
            String componentName;
            Object obj;
            for(int i = screenHashMapKeysLength,j=0; i > 0;i--,j++){
                componentName = (String)screenHashMapKeyIterator.next();
                obj =  fileMap.get(componentName);
                if(obj != null){
                    screenHashMap.put(componentName, obj);
                }
            }
            mandatoryMap = screenHashMap;
        }
    }
    
    private void initComponents(){
        cScrollPane1 = new com.see.truetransact.uicomponent.CScrollPane();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        jLabels = new JLabel[mandatoryKeysLength];
        jButtonGroups = new ButtonGroup[mandatoryKeysLength];
        jRadioButtons = new JRadioButton[mandatoryKeysLength*2];
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        
        java.awt.GridBagConstraints gridBagConstraints;
        getContentPane().setLayout(new java.awt.GridBagLayout());
        cScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        cScrollPane1.setPreferredSize(new java.awt.Dimension(400, 400));
        cPanel1.setLayout(new java.awt.GridBagLayout());
        
        String componentName;
        String displayName = null;
        int panelSize = 0;
        for(int i = mandatoryKeysLength,j=0; i > 0;i--,j++){
            componentName = (String)mandatoryKeyIterator.next();
            //System.out.println(componentName);
            jLabels[j] = new JLabel();
            //jLabels[j].setText(componentName.substring(componentName.indexOf("_")+1));
            if( componentName.startsWith("rdo")){
                displayName = componentName.substring(0,componentName.indexOf("_"));
                displayName = displayName.substring(3);
            }
            else{
                displayName = componentName.substring(3);
            }
            
            jLabels[j].setText(displayName);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = j;
            cPanel1.add(jLabels[j], gridBagConstraints);
            
            jButtonGroups[j] = new ButtonGroup();
            
            jRadioButtons[j] = new JRadioButton();
            if( ((Boolean)mandatoryMap.get(componentName)).booleanValue() )
                jRadioButtons[j].setSelected(true);
            jRadioButtons[j].setText("True");
            jRadioButtons[j].setToolTipText(componentName);
            jButtonGroups[j].add(jRadioButtons[j]);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = j;
            cPanel1.add(jRadioButtons[j], gridBagConstraints);
            
            jRadioButtons[mandatoryKeysLength+j] = new JRadioButton();
            if( !((Boolean)mandatoryMap.get(componentName)).booleanValue() )
                jRadioButtons[mandatoryKeysLength+j].setSelected(true);
            jRadioButtons[mandatoryKeysLength+j].setText("False");
            jRadioButtons[mandatoryKeysLength+j].setToolTipText(componentName);
            jButtonGroups[j].add(jRadioButtons[mandatoryKeysLength+j]);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 2;
            gridBagConstraints.gridy = j;
            cPanel1.add(jRadioButtons[mandatoryKeysLength+j], gridBagConstraints);
            
            panelSize += 25;
        }
        jButtonSubmit = new JButton();
        jButtonSubmit.setText("Submit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = mandatoryKeysLength;
        cPanel1.add(jButtonSubmit, gridBagConstraints);
        jButtonSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSubmitActionPerformed(evt);
            }
        });
        System.out.println("panel size "+panelSize);
        cPanel1.setPreferredSize(new java.awt.Dimension(400, panelSize));
        
        cScrollPane1.setViewportView(cPanel1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(cScrollPane1, gridBagConstraints);
        pack();
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {
        this.dispose();
    }
    
    /** This method is used to create the MandatoryHashMap file & compile it with the
     * help of MandatoryHashMapGenerator
     * @param evt
     */
    private void jButtonSubmitActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            HashMap modifiedHashMap = new HashMap();
            //MandatoryResourceBundle mandatoryResourceBundle = new MandatoryResourceBundle();
            //String resourceBundleFile = mandatoryResourceBundle.getString("mandatory_file_path");
            for(int i = mandatoryKeysLength,j=0; i > 0;i--,j++){
                if( jRadioButtons[j].isSelected() ){
                    modifiedHashMap.put(jRadioButtons[j].getToolTipText(),new Boolean(true));
                }
                else
                    modifiedHashMap.put(jRadioButtons[j].getToolTipText(),new Boolean(false));
            }
            
            MandatoryHashMapGenerator mandatoryHashMapGenerator = new MandatoryHashMapGenerator();
            boolean mandatoryHashMapFileCreated = mandatoryHashMapGenerator.generateHashMap(modifiedHashMap, className, filePath);
            displayDialog(mandatoryHashMapFileCreated);
            //System.out.println("MandatoryHashMapFileCreated : "+mandatoryHashMapFileCreated);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void displayDialog(boolean fileCreated){
        String[] options = { AdminConstants.OK};
        String message = null;
        if( fileCreated ){
            message = AdminConstants.MANDATORYMESSAGESUCCESS;
        }
        else{
            message = AdminConstants.MANDATORYMESSAGEFAILURE;
        }
       
        COptionPane.showOptionDialog(null, message, AdminConstants.INFORMATIONTITLE,
        COptionPane.DEFAULT_OPTION, COptionPane.INFORMATION_MESSAGE,
        null, options, options[0]);
    }
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        new MandatoryConfiguration("com.see.truetransact.uimandatory.MandatoryDemo","f:/tt").show();
    }
}
