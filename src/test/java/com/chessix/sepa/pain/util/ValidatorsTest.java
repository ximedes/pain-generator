package com.chessix.sepa.pain.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorsTest {

    @Test
    public void testValidBIC() throws Exception {
        assertFalse(Validators.validBIC(null));
        assertFalse(Validators.validBIC(""));
        assertFalse(Validators.validBIC("ABN"));
        assertFalse(Validators.validBIC("ABN123"));
        assertTrue(Validators.validBIC("RABONL2U"));
        assertTrue(Validators.validBIC("RABONL2UXXX"));
        assertTrue(Validators.validBIC("TESTNL2A"));
    }

    @Test
    public void testValidIBAN() throws Exception {
        assertFalse(Validators.validIBAN(null));
        assertFalse(Validators.validIBAN(""));
        assertFalse(Validators.validIBAN("123456789"));
        assertFalse(Validators.validIBAN("NL13TEST0123456788"));
        assertTrue(Validators.validIBAN("NL13TEST0123456789"));
    }
}
