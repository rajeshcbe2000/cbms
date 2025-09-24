/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.clientutil;

import java.util.ArrayList;
import com.see.truetransact.clientexception.ComboBoxModelException;

/**
 * Extends the ability of javax.swing.DefaultComboBoxModel to achieve the index - key lookup.
 * e.g. the selected index is 2, and the corresponding keyValue is temp project id.
 * Creation date: (11/28/2001 2:55:20 PM)
 * @author Bala
 */
public class ComboBoxModel extends EnhancedComboBoxModel implements Cloneable {
    private ArrayList keys = new ArrayList(); // Stores all the Keys as an ArrayList
    private final String STRLENTH = "The length of the keys is shorter than the length of the items";
    
    /**
     * ComboBoxModel empty constructor .
     */
    public ComboBoxModel() {
        super();
    }
    /**
     * ComboBoxModel Object array constructor.
     * @param items java.lang.Object[]
     */
    public ComboBoxModel(Object[] items) {
        super(items);
    }
    /**
     * ComboBoxModel Key array and object array constructor.
     * Key used for internal value and Item used to display in the UI
     * @param theKeys java.lang.Object[]
     * @param items java.lang.Object[]
     */
    public ComboBoxModel(Object[] theKeys, Object[] items) throws ComboBoxModelException{
        super(items);
        if (items.length > theKeys.length)
            throw new ComboBoxModelException(STRLENTH);
        for (int i = 0; i < theKeys.length; i++){
            getKeys().add(theKeys[i]);
        }
    }
    /**
     * ComboBoxModel constructor with ArrayList and Object
     * @param theKeys ArrayList
     * @param items java.lang.Object[]
     */
    public ComboBoxModel(ArrayList theKeys, Object[] items) {
        super(items);
        setKeys(theKeys);
    }
    /**
     * ComboBoxModel constructor only for Item.
     * @param v java.util.ArrayList
     */
    public ComboBoxModel(ArrayList v) {
        super(v);
    }
    /**
     * ComboBoxModel constructor for Keys & Items.
     * @param v java.util.ArrayList
     */
    public ComboBoxModel(ArrayList theKeys, ArrayList v) {
        super(v);
        setKeys(theKeys);
    }
    /**
     * Add Key used to set key for all existing Objects
     * Creation date: (12/19/2001 1:38:46 PM)
     */
    public void addKey(int index, Object obj) {
        keys.add(index, obj);
    }
    
    /**
     * Add Key and Element used to set key and element for all existing Objects
     * Creation date: (12/19/2001 1:38:46 PM)
     */
    public void addKeyAndElement(Object key, Object val) {
        keys.add(key);
        addElement(val);
    }
    
    /**
     * Add Key and Element used to set key and element for all existing Objects
     * Creation date: (12/19/2001 1:38:46 PM)
     */
    public void removeKeyAndElement(Object key) {
        removeElementAt(keys.indexOf(key));
        keys.remove(key);
    }
    
    /**
     * Clone is used for replicate the object
     */
    public Object clone() {
        Object obj = null;
        
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace();
        }
        return obj;
    }
    
    /**
     * Checks the Object available or not
     * @return boolean
     * @param data java.lang.Object
     */
    public boolean containsElement(Object data) {
        if (data == null) {
            return false;
        }
        
        for (int i = 0; i < getSize(); i++){
            if (getElementAt(i) == data
            || ((String)getElementAt(i)).equalsIgnoreCase((String)data)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Returns the Key for the give Index
     */
    public Object getKey(int index) {
        int size = getKeys().size();
        if (size > 0 && index < size && index != -1)
            return getKeys().get(index);
        else
            return null;
    }
    
    /** 
     * Gives the Key for the Selected Item in the Combobox
     */
    public Object getKeyForSelected() {
        return getKey(getIndexOf(getSelectedItem()));
    }
    
    /**
     * Give the Data for the Given input Key
     */
    public Object getDataForKey(Object objKey) {
        return getElementAt(keys.indexOf(objKey));
    }
    
    /**
     * Sets Key for the Selected Item.
     */
    public void setKeyForSelected(Object objKey) {
        setSelectedItem(getElementAt(keys.indexOf(objKey)));
    }
    
    /**
     * Separately sets Key for the Combobox Model
     * @param newKeys java.util.ArrayList
     */
    private void setKeys(ArrayList newKeys) {
        keys = newKeys;
    }
    /**
     * Returns all the Keys as an ArrayList
     * @return java.util.ArrayList
     */
    public ArrayList getKeys() {
        if (keys == null)
            keys = new ArrayList();
        return keys;
    }
    
    /**
     * Removes Key from the key array for the given index
     * Creation date: (12/19/2001 1:40:21 PM)
     */
    public void removeKey(int index) {
        keys.remove(index);
    }    
}
