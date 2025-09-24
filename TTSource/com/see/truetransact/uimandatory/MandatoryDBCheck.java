/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * MandatoryDBCheck.java
 *
 * Created on April 5, 2004, 11:18 AM
 */

package com.see.truetransact.uimandatory;

import java.awt.Container;
import java.awt.Component;

import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.ListResourceBundle;

import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextArea;

import com.see.truetransact.uicomponent.CDateField;
import com.see.truetransact.clientutil.ClientUtil;

/** This class is used to check the fields of a screen, whether mandatory or not &
 * return a message based on that
 * @author Bala
 */
public class MandatoryDBCheck {
    private UIMandatoryHashMap uiMandatoryHashMap;
    private ArrayList components;
    private ListResourceBundle mandatoryResourceBundle;
    private StringBuffer errorMessage;
    private HashMap mandatoryMap;
    private String componentName;
    private int componentsSize;
    
    /** Creates a new instance of MandatoryCheck */
    public MandatoryDBCheck() {
    }
    
    /** Enable Disable fields.
     * @param className Class Name of the UI Screen
     * @param container The container which holds all the components. For a JFrame it is contentPane.
     * @return
     */    
    public void setComponentInit (String className, Container container){
        try{
            mandatoryMap = ClientUtil.getScreenConfigList(className);
            
            if (className.endsWith("UI")) {
                className = className.substring(0,className.length()-2);
            }
            
            mandatoryResourceBundle = (ListResourceBundle)Class.forName(className+"MRB").newInstance();
        }catch(Exception exception){
            //exception.printStackTrace();
        }
        
        final Set mandatoryKeySet = mandatoryMap.keySet();
        final Iterator mandatoryKeyIterator = mandatoryKeySet.iterator();
        final int mandatoryKeysLength = mandatoryKeySet.size();
        errorMessage = new StringBuffer();
        
        components = findComponentList(container, null);
        componentsSize = components.size();
        
        Component component = null;
        for(int i = mandatoryKeysLength,j=0; i > 0; i--,j++){
            componentName = (String)mandatoryKeyIterator.next();
            component = findComponent(componentName);
            
            if (component != null) {
                component.setEnabled(isEnabled(component.getName()));
                //component.setVisible(isEditable(component.getName()));
            }
        }
    }
    
    /** To check the Mandatoryness of a field & return proper message
     * @param className Class Name of the UI Screen
     * @param container The container which holds all the components. For a JFrame it is contentPane.
     * @return
     */    
    public String checkMandatory(String className, Container container){
        try{
            mandatoryMap = ClientUtil.getScreenConfigList(className);
            
            if (className.endsWith("UI")) {
                className = className.substring(0,className.length()-2);
            }
            
            mandatoryResourceBundle = (ListResourceBundle)Class.forName(className+"MRB").newInstance();
        }catch(Exception exception){
            //exception.printStackTrace();
        }
        
        final Set mandatoryKeySet = mandatoryMap.keySet();
        final Iterator mandatoryKeyIterator = mandatoryKeySet.iterator();
        final int mandatoryKeysLength = mandatoryKeySet.size();
        errorMessage = new StringBuffer();
        
        components = findComponentList(container, null);
        componentsSize = components.size();
        
        Component component = null;
        for(int i = mandatoryKeysLength,j=0; i > 0; i--,j++){
            componentName = (String)mandatoryKeyIterator.next();
            component = findComponent(componentName);
            checkComponent(component);
        }
        return errorMessage.toString();
    }
    
    /** To find & return all the components in the given container recursively
     * @param cont
     * @param outList
     * @return
     */    
    private ArrayList findComponentList(Container cont, ArrayList outList) {
        if (outList == null) {
            outList = new ArrayList();
        }
        if (cont == null ) {
            return outList;
        }
        
        Component children[] = cont.getComponents();
        //System.out.println("children size" + children.length);
        for (int i = 0; i < children.length; i++) {
            
            if ( !( children[i] instanceof javax.swing.JTextField ||
                children[i] instanceof javax.swing.JCheckBox ||
                children[i] instanceof javax.swing.JRadioButton ||
                children[i] instanceof javax.swing.JList ||
                children[i] instanceof javax.swing.JComboBox ||
                children[i] instanceof CDateField ||
                children[i] instanceof javax.swing.JTextArea )
            ) {
                findComponentList((Container) children[i], outList);
            }
            else{
                outList.add(children[i]);
            }
        }
        return outList;
    }
    
    /** To find a component based on componentName & return it
     * @param componentName
     * @return
     */    
    private Component findComponent(String componentName){
        Component comp = null;
        for(int i = componentsSize,j = 0; i > 0; i--, j++){
            comp = (Component)components.get(j);
            //System.out.println("Comp Name "+componentName+" is "+comp);
            if( (comp.getName()).equals(componentName) ){
                break;
            }
        }
        return comp;
    }
    
    /** To check whether any one RadioButton of a given button group is selected or not
     * @param groupName
     * @return
     */    
    private boolean isButtonGroupSelected(String groupName){
        Component comp;
        for(int i = componentsSize,j = 0; i > 0; i--, j++){
            comp = (Component)components.get(j);
            if( (comp.getName()).startsWith(groupName) ){
                if( ((JRadioButton)comp).isSelected() ){
                    return true;
                }
            }
        }
        return false;
    }
    
    /** To check the Mandatoryness of a component. If a component is Mandatory & not a
     * proper value entered, then add the error message to a StringBuffer,which will be
     * used to return back error message by checkMandatory()
     * @param component
     */    
    private void checkComponent(Component component){
        if( component != null ){
            //System.out.println(component.toString());
            //System.out.println(componentName);
            if( component instanceof JTextField ){
                textFieldCheck(component);
            }
            else if( component instanceof JCheckBox ){
                checkBoxCheck(component);
            }
            else if( component instanceof JComboBox ){
                comboBoxCheck(component);
            }
            else if( component instanceof JList ){
                listCheck(component);
            }
            else if( component instanceof JTextArea ){
                textAreaCheck(component);
            }
            else if( component instanceof JRadioButton ){
                radioButtonCheck(component);
            }
            else if( component instanceof CDateField ){
                dateFieldCheck(component);
            }
        }
    }
    
    private boolean isMandatory(String componentName) {
        boolean result = false;
        String propVal = null;
        HashMap map = ((HashMap)mandatoryMap.get(componentName));
        if (map.containsKey("PROP01")) {
            propVal = (String) map.get("PROP01");
        }

        if (propVal != null && 
            propVal.equalsIgnoreCase("YES")) {
                result = true;
        }
        return result;
    }
    
    // Checking for Enabled or not.
    private boolean isEnabled(String componentName) {
        boolean result = true;
        String propVal = null;
        HashMap map = ((HashMap)mandatoryMap.get(componentName));
        if (map.containsKey("PROP02")) {
            propVal = (String) map.get("PROP02");
        }

        if (propVal != null && 
            propVal.equalsIgnoreCase("NO")) {
                result = false;
        }
        return result;
    }
    
    // Checking for Editable or not.
    private boolean isEditable (String componentName) {
        boolean result = true;
        String propVal = null;
        HashMap map = ((HashMap)mandatoryMap.get(componentName));
        if (map.containsKey("PROP03")) {
            propVal = (String) map.get("PROP03");
        }

        if (propVal != null && 
            propVal.equalsIgnoreCase("NO")) {
                result = false;
        }
        return result;
    }
    
    /** To check the mandatoryness of a TextField and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void textFieldCheck(Component component){
        final JTextField jTextField = (JTextField)component;
        if( jTextField.isEditable() && 
                isMandatory(jTextField.getName()) &&
                0 == jTextField.getText().length() ){
            addErrorMessage();
        }
    }
    
    /** To check the mandatoryness of a JComboBox and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void comboBoxCheck(Component component){
        final JComboBox jComboBox = (JComboBox)component;
        if( isMandatory(jComboBox.getName()) ){
            Object object = jComboBox.getSelectedItem();
            if( object != null ){
                String selectedItem = (String)object;
                if( selectedItem.equals("") ){
                    addErrorMessage();
                }
            }
            else{
                addErrorMessage();
            }
        }
    }
    
    /** To check the mandatoryness of a JCheckBox and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void checkBoxCheck(Component component){
        final JCheckBox jCheckBox = (JCheckBox)component;
        if(  isMandatory(jCheckBox.getName())  &&
        !( jCheckBox.isSelected()) ){
            addErrorMessage();
        }
    }
    
    /** To check the mandatoryness of a JListBox and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void listCheck(Component component){
        final JList jList = (JList)component;
        if( isMandatory(jList.getName()) &&
        jList.getSelectedValue() == null ){
            addErrorMessage();
        }
    }
    
    /** To check the mandatoryness of a JTextArea and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void textAreaCheck(Component component){
        final JTextArea jTextArea = (JTextArea)component;
        if( isMandatory(jTextArea.getName()) &&
        0 == jTextArea.getText().length() ){
            addErrorMessage();
        }
    }
    
     private void dateFieldCheck(Component component){
        final CDateField cDateField = (CDateField)component;
        if( isMandatory(cDateField.getName()) &&
        0 == cDateField.getDateValue().length() ){
            addErrorMessage();
        }
    }
    
    /** To check the mandatoryness of a JRadioButton and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void radioButtonCheck(Component component){
        final JRadioButton jRadioButton = (JRadioButton)component;
        if( isMandatory(jRadioButton.getName()) ){
            final String groupName = componentName.substring(0,componentName.lastIndexOf("_"));
            //System.out.println("groupName : "+groupName);
            if( !isButtonGroupSelected(groupName) ){
                addErrorMessage();
            }
        }
    }
    
    /** To add the error message by getting value from ResourceBundle */    
    private void addErrorMessage(){
        errorMessage.append(mandatoryResourceBundle.getString(componentName)).append("\n");
    }
}
