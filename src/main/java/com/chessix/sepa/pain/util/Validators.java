package com.chessix.sepa.pain.util;

import java.math.BigInteger;

public class Validators {

    private static final BigInteger B97 = BigInteger.valueOf(97);

    /**
     * Checks if this is a valid BIC string
     *
     * @param bic the BIC to check
     * @return true if valid
     */
    public static boolean validBIC(String bic) {
        return bic != null && bic.matches("[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}");
    }

    /**
     * Checks if this is a valid IBAN including Mod-97 check
     *
     * @param iban the IBAN to check
     * @return true if valid
     */
    public static boolean validIBAN(String iban) {

        if (iban != null && iban.matches("[A-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}")) {

            // move first 4 characters to end
            String nabi = iban.substring(4).toUpperCase() + iban.substring(0, 4).toUpperCase();
            // Replace characters with numbers a = 10, b = 11 ... z = 35
            StringBuilder sb = new StringBuilder();
            for (char c : nabi.toCharArray()) {
                if (c >= 'A' && c <= 'Z') {
                    int i = 10 + (c - 'A');
                    sb.append(i);
                } else {
                    sb.append(c);
                }
            }
            try {
                return new BigInteger(sb.toString()).mod(B97).equals(BigInteger.ONE);
            } catch (NumberFormatException e) {
                return false;
            }


        }
        return false;
    }

}
