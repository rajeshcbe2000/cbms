 /*
 * ElectronicReconciliationRB.java
 *
 * Created on Sep 30, 2019, 04:45 PM
 * @author Sathiya
 *
 */
package com.see.truetransact.ui.transaction.electronicreconciliation;

import java.util.ListResourceBundle;

public class ElectronicReconciliationRB extends ListResourceBundle {

    public ElectronicReconciliationRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"lblProductID", "Product ID"},
        {"lblNarration", "Narration"},
        {"lblDebitGL", "Debit GL"},
        {"lblTotalAmount", "Total Amount"},
        {"lblEffectiveDt", "Effective Date"},
        {"lblBeneficiaryName", "3rd Beneficiary Name"},
        {"btnBrowse", "Browse"},
        {"lblTotalTransactionAmt", "Total Amout Rs."}
    };
}
