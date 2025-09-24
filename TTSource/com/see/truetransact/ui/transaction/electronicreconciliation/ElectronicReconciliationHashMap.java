 /*
 * ElectronicReconciliationHashMap.java
 *
 * Created on Sep 30, 2019, 04:45 PM
 * @author Sathiya
 *
 */
package com.see.truetransact.ui.transaction.electronicreconciliation;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ElectronicReconciliationHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public ElectronicReconciliationHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtDebitGL", new Boolean(true));
        mandatoryMap.put("txtTotalAmount", new Boolean(true));
        mandatoryMap.put("txtNarration", new Boolean(true));
        mandatoryMap.put("tdtEffectiveDt", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}
