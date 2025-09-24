/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SteppedComboBoxUI.java
 */
package com.see.truetransact.uicomponent;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author Admin
 */
class SteppedComboBoxUI extends BasicComboBoxUI {
  protected ComboPopup createPopup() {
    BasicComboPopup popup = new BasicComboPopup(comboBox) {

      public void show() {
        Dimension popupSize = ((CComboBox) comboBox)
            .getPopupSize();
        popupSize
            .setSize(popupSize.width,
                getPopupHeightForRowCount(comboBox
                    .getMaximumRowCount()));
        Rectangle popupBounds = computePopupBounds(0, comboBox
            .getBounds().height, popupSize.width, popupSize.height);
        scroller.setMaximumSize(popupBounds.getSize());
        scroller.setPreferredSize(popupBounds.getSize());
        scroller.setMinimumSize(popupBounds.getSize());
        list.invalidate();
        int selectedIndex = comboBox.getSelectedIndex();
        if (selectedIndex == -1) {
          list.clearSelection();
        } else {
          list.setSelectedIndex(selectedIndex);
        }
        list.ensureIndexIsVisible(list.getSelectedIndex());
        setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());

        show(comboBox, popupBounds.x, popupBounds.y);
      }
    };
    popup.getAccessibleContext().setAccessibleParent(comboBox);
    return popup;
  }
}

