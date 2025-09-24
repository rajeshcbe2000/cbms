/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * MandatoryCheck.java
 *
 * Created on July 31, 2003, 10:18 AM
 */

package com.see.truetransact.uimandatory;
import java.awt.Container;
import java.awt.Component;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import com.see.truetransact.uicomponent.CDateField;
import java.util.ListResourceBundle;
/** This class is used to check the fields of a screen, whether mandatory or not &
 * return a message based on that
 * @author karthik,bala
 */
public class MandatoryCheck {
    private UIMandatoryHashMap uiMandatoryHashMap;
    private ArrayList components;
    private ListResourceBundle mandatoryResourceBundle;
    private StringBuffer errorMessage;
    private HashMap mandatoryMap;
    private String componentName;
    private int componentsSize;
    /** Creates a new instance of MandatoryCheck */
    public MandatoryCheck() {
    }
    
    /** To check the Mandatoryness of a field & return proper message
     * @param className Class Name of the UI Screen
     * @param container The container which holds all the components. For a JFrame it is contentPane.
     * @return
     */    
    public String checkMandatory(String className, Container container){
        try{
            className = className.substring(0,className.length()-2);
            uiMandatoryHashMap = (UIMandatoryHashMap)Class.forName(className+"HashMap").newInstance();
            mandatoryResourceBundle = (ListResourceBundle)Class.forName(className+"MRB").newInstance();
        }catch(Exception exception){
            //exception.printStackTrace();
        }
        
        mandatoryMap = uiMandatoryHashMap.getMandatoryHashMap();
        final Set mandatoryKeySet = mandatoryMap.keySet();
        final Iterator mandatoryKeyIterator = mandatoryKeySet.iterator();
        final int mandatoryKeysLength = mandatoryKeySet.size();
        errorMessage = new StringBuffer();
        
//        System.out.println("### mandatoryMap : "+mandatoryMap);
        components = findComponentList(container, null);
//        System.out.println("### Components : "+components);
        componentsSize = components.size();
        
        Component component = null;
        for(int i = mandatoryKeysLength,j=0; i > 0; i--,j++){
            componentName = (String)mandatoryKeyIterator.next();
            component = findComponent(componentName);
            checkComponent(component);
        }
        return errorMessage.toString();
    }
    
    /** To put the Mandatory marks of a field
     * @param className Class Name of the UI Screen
     * @param container The container which holds all the components. For a JFrame it is contentPane.
     * @return
     */    
    public String putMandatoryMarks(String className, Container container){
        try{
            className = className.substring(0,className.length()-2);
            uiMandatoryHashMap = (UIMandatoryHashMap)Class.forName(className+"HashMap").newInstance();
            mandatoryResourceBundle = (ListResourceBundle)Class.forName(className+"MRB").newInstance();
        }catch(Exception exception){
            //exception.printStackTrace();
        }
        
        mandatoryMap = uiMandatoryHashMap.getMandatoryHashMap();
        final Set mandatoryKeySet = mandatoryMap.keySet();
        final Iterator mandatoryKeyIterator = mandatoryKeySet.iterator();
        final int mandatoryKeysLength = mandatoryKeySet.size();
        errorMessage = new StringBuffer();
        
//        System.out.println("%%%% mandatoryMap : "+mandatoryMap);
        components = findComponentListAll(container, null);
//        System.out.println("%%%% Components : "+components);
        componentsSize = components.size();
        
        Component component = null;
        for(int i = mandatoryKeysLength,j=0; i > 0; i--,j++){
            componentName = (String)mandatoryKeyIterator.next();
            component = findComponent1(componentName);
            if (component != null) {
                component = findComponent("lbl"+component.getName().substring(3,component.getName().length()));
                if (component != null) {
                    JLabel j1 = (JLabel)component;
                    j1.setText(j1.getText()+" *");
                }
            }
        }
        return errorMessage.toString();
    }
    
    /** To check the Mandatoryness of a field & return proper message
     * @param className Class Name of the UI Screen
     * @param container The container which holds all the components. For a JFrame it is contentPane.
     * @return
     */    
    public String checkMandatory(String className, Container container, HashMap objMandatoryHashMap){
        try{
            className = className.substring(0,className.length()-2);
            mandatoryResourceBundle = (ListResourceBundle)Class.forName(className+"MRB").newInstance();
        }catch(Exception exception){
            //exception.printStackTrace();
        }
        
        mandatoryMap = objMandatoryHashMap;
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
    
    /** To put the Mandatory marks of a field
     * @param className Class Name of the UI Screen
     * @param container The container which holds all the components. For a JFrame it is contentPane.
     * @return
     */    
    public String putMandatoryMarks(String className, Container container, HashMap objMandatoryHashMap){
        try{
            className = className.substring(0,className.length()-2);
            mandatoryResourceBundle = (ListResourceBundle)Class.forName(className+"MRB").newInstance();
        }catch(Exception exception){
            //exception.printStackTrace();
        }
        
        mandatoryMap = objMandatoryHashMap;
        final Set mandatoryKeySet = mandatoryMap.keySet();
        final Iterator mandatoryKeyIterator = mandatoryKeySet.iterator();
        final int mandatoryKeysLength = mandatoryKeySet.size();
        errorMessage = new StringBuffer();
        
//        System.out.println("%%%% mandatoryMap : "+mandatoryMap);
        components = findComponentListAll(container, null);
//        System.out.println("%%%% Components : "+components);
        componentsSize = components.size();
        
        Component component = null;
        for(int i = mandatoryKeysLength,j=0; i > 0; i--,j++){
            componentName = (String)mandatoryKeyIterator.next();
            component = findComponent1(componentName);
            if (component != null) {
                component = findComponent("lbl"+component.getName().substring(3,component.getName().length()));
                if (component != null) {
                    JLabel j1 = (JLabel)component;
                    j1.setText(j1.getText()+" *");
                }
            }
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
    
    /** To find & return all the components in the given container recursively
     * @param cont
     * @param outList
     * @return
     */    
    private ArrayList findComponentListAll(Container cont, ArrayList outList) {
        if (outList == null) {
            outList = new ArrayList();
        }
        if (cont == null ) {
            return outList;
        }
        
        Component children[] = cont.getComponents();
        //System.out.println("children size" + children.length);
        for (int i = 0; i < children.length; i++) {
//            System.out.println("#### component name : "+children[i].getName()+
//                ":"+(children[i] instanceof javax.swing.JLabel));
            if ( !( children[i] instanceof javax.swing.JTextField ||
            children[i] instanceof javax.swing.JCheckBox ||
            children[i] instanceof javax.swing.JLabel ||
            children[i] instanceof javax.swing.JRadioButton ||
            children[i] instanceof javax.swing.JList ||
            children[i] instanceof javax.swing.JComboBox ||
            children[i] instanceof CDateField ||
            children[i] instanceof javax.swing.JTextArea )
            ) {
              findComponentListAll((Container) children[i], outList);
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
        Component comp;
        for(int i = componentsSize,j = 0; i > 0; i--, j++){
            comp = (Component)components.get(j);
            //System.out.println("Comp Name "+componentName+" is "+comp);
            if( comp.getName()!=null && (comp.getName()).equals(componentName) ){
                return comp;
            }
        }
        return null;
    }
    
    /** To find a component based on componentName & return it
     * @param componentName
     * @return
     */    
    private Component findComponent1(String componentName){
        Component comp;
        for(int i = componentsSize,j = 0; i > 0; i--, j++){
            comp = (Component)components.get(j);
            //System.out.println("Comp Name "+componentName+" is "+comp);
            if( comp.getName()!=null && (comp.getName()).equals(componentName) ){
                if (((Boolean)mandatoryMap.get(comp.getName())).booleanValue())
                return comp;
            }
        }
        return null;
    }
    
    /** To check whether any one RadioButton of a given button group is selected or not
     * @param groupName
     * @return
     */    
    private boolean isButtonGroupSelected(String groupName){
        Component comp;
        for(int i = componentsSize,j = 0; i > 0; i--, j++){
            comp = (Component)components.get(j);
            if( comp.getName()!=null && (comp.getName()).startsWith(groupName) ){
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
                JTextFieldCheck(component);
            }
            else if( component instanceof JCheckBox ){
                JCheckBoxCheck(component);
            }
            else if( component instanceof JComboBox ){
                JComboBoxCheck(component);
            }
            else if( component instanceof JList ){
                JListCheck(component);
            }
            else if( component instanceof JTextArea ){
                JTextAreaCheck(component);
            }
            else if( component instanceof JRadioButton ){
                JRadioButtonCheck(component);
            }
            else if( component instanceof CDateField ){
                CDateFieldCheck(component);
            }
        }
    }
    /** To check the mandatoryness of a TextField and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void JTextFieldCheck(Component component){
        final JTextField jTextField = (JTextField)component;
        //
        if(jTextField.isEnabled() && ((Boolean)mandatoryMap.get(jTextField.getName())).booleanValue() &&
        0 == jTextField.getText().trim().length() ){
            addErrorMessage();
        }
    }
    
    /** To check the mandatoryness of a JComboBox and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void JComboBoxCheck(Component component){
        final JComboBox jComboBox = (JComboBox)component;
        if( ((Boolean)mandatoryMap.get(jComboBox.getName())).booleanValue() ){
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
    private void JCheckBoxCheck(Component component){
        final JCheckBox jCheckBox = (JCheckBox)component;
        if( ((Boolean)mandatoryMap.get(jCheckBox.getName())).booleanValue() &&
        !( jCheckBox.isSelected()) ){
            addErrorMessage();
        }
    }
    
    /** To check the mandatoryness of a JListBox and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void JListCheck(Component component){
        final JList jList = (JList)component;
        if( ((Boolean)mandatoryMap.get(jList.getName())).booleanValue() &&
        jList.getSelectedValue() == null ){
            addErrorMessage();
        }
    }
    
    /** To check the mandatoryness of a JTextArea and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void JTextAreaCheck(Component component){
        final JTextArea jTextArea = (JTextArea)component;
        if( ((Boolean)mandatoryMap.get(jTextArea.getName())).booleanValue() &&
        0 == jTextArea.getText().trim().length() ){
            addErrorMessage();
        }
    }
    
     private void CDateFieldCheck(Component component){
        final CDateField cDateField = (CDateField)component;
        if( ((Boolean)mandatoryMap.get(cDateField.getName())).booleanValue() &&
        0 == cDateField.getDateValue().length() ){
            addErrorMessage();
        }
    }
    
    /** To check the mandatoryness of a JRadioButton and add an error message if it fails
     * to satisfy
     * @param component
     */    
    private void JRadioButtonCheck(Component component){
        final JRadioButton jRadioButton = (JRadioButton)component;
        if( ((Boolean)mandatoryMap.get(jRadioButton.getName())).booleanValue() ){
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
