package com.chessix.sepa.pain;

import java.util.Date;

/**
 * The electronic representation of a mandate signed by a debtor
 */
public class Mandate {

    private String mandateId;
    private Date signed;

    public Mandate(String mandateId, Date signed) {
        setMandateId(mandateId);
        setSigned(signed);
    }

    public String getMandateId() {
        return mandateId;
    }

    /**
     * Sets and validates the mandate identifier.
     *
     * @param mandateId the identifier, may not be null and may not be longer than 35 characters
     */
    public void setMandateId(String mandateId) {
        if (mandateId == null || mandateId.trim().isEmpty() || mandateId.length() > 35) {
            throw new IllegalArgumentException("Mandate identifier invalid");
        }
        this.mandateId = mandateId;
    }

    public Date getSigned() {
        return signed;
    }

    /**
     * Sets the date this mandate was signed
     *
     * @param signed required date of singing
     */
    public void setSigned(Date signed) {
        if (signed == null) {
            throw new IllegalArgumentException("Mandate signature date is required");
        }
        this.signed = signed;
    }
}
