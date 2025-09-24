/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * JavaCAutoComboBox.java
 */

package com.see.truetransact.ui.indend.reservedepreciationachd;

import java.awt.Insets;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 *
 * @author Revathi L
 */
public class JavaCAutoComboBox extends JComboBox {

    private class AutoTextFieldEditor extends BasicComboBoxEditor {

        private JavaAutoCTextField getAutoTextFieldEditor() {
            return (JavaAutoCTextField) editor;
        }

        AutoTextFieldEditor(java.util.List list) {
            editor = new JavaAutoCTextField(list, JavaCAutoComboBox.this);
        }
    }

    public JavaCAutoComboBox(java.util.List list) {
        isFired = false;
        autoTextFieldEditor = new AutoTextFieldEditor(list);
        setEditable(true);
        setModel(new DefaultComboBoxModel(list.toArray()) {
            protected void fireContentsChanged(Object obj, int i, int j) {
                if (!isFired) {
                    super.fireContentsChanged(obj, i, j);
                }
            }
        });
        setEditor(autoTextFieldEditor);
        Insets m = getInsets();
        m.bottom = 0;
        m.top = 0;
        m.left = 0;
        m.right = 0;
    }

    public boolean isCaseSensitive() {
        return autoTextFieldEditor.getAutoTextFieldEditor().isCaseSensitive();
    }

    public void setCaseSensitive(boolean flag) {
        autoTextFieldEditor.getAutoTextFieldEditor().setCaseSensitive(flag);
    }

    public boolean isStrict() {
        return autoTextFieldEditor.getAutoTextFieldEditor().isStrict();
    }

    public void setStrict(boolean flag) {
        autoTextFieldEditor.getAutoTextFieldEditor().setStrict(flag);
    }

    public java.util.List getDataList() {
        return autoTextFieldEditor.getAutoTextFieldEditor().getDataList();
    }

    public void setDataList(java.util.List list) {
        autoTextFieldEditor.getAutoTextFieldEditor().setDataList(list);
        setModel(new DefaultComboBoxModel(list.toArray()));
    }

    void setSelectedValue(Object obj) {
        if (isFired) {
            return;
        } else {
            isFired = true;
            setSelectedItem(obj);
            fireItemStateChanged(new ItemEvent(this, 701, selectedItemReminder, 1));
            isFired = false;
            return;
        }
    }

    protected void fireActionEvent() {
        if (!isFired) {
            super.fireActionEvent();
        }
    }
    private AutoTextFieldEditor autoTextFieldEditor;
    private boolean isFired;
}