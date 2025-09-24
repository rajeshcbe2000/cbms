 /*
 * ElectronicReconciliationMRB.java
 *
 * Created on Sep 30, 2019, 04:45 PM
 * @author Sathiya
 *
 */
package com.see.truetransact.ui.transaction.electronicreconciliation;

import java.util.ListResourceBundle;

public class ElectronicReconciliationMRB extends ListResourceBundle {

    public ElectronicReconciliationMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"cboProductID", "Product ID should be a proper value!!!"},
        {"txtDebitGL", "Debit GL should not be empty!!!"},
        {"btnDebitGL", "Debit GL should not be empty!!!"},
        {"txtTotalAmount", "Total Amount should not be empty!!!"},
        {"txtNarration", "Narration should not be empty!!!"},
        {"tdtEffectiveDt", "Effective Date should not be empty!!!"},
        {"txtBeneficiaryName", "Beneficiary Name should not be empty!!!"},};
}
