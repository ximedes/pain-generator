package com.chessix.sepa.pain;

/**
 * The creditor; receiving party
 */
public class Creditor extends AccountHolder {

    /**
     * The creditor identifier
     */
    private String identifier;

    /**
     * Constructs a Creditor for direct debit
     *
     * @param iban       The International Bank Account Number
     * @param bic        The Bank Identification Code
     * @param name       The creditor (Initiating party) name
     * @param identifier The creditor identifier
     * @throws IllegalArgumentException if any property is invalid
     */
    public Creditor(String iban, String bic, String name, String identifier) {
        super(iban, bic, name);
        setIdentifier(identifier);
    }

	/**
	 * Constructs a Creditor for credit transfer
	 *
	 * @param iban       The International Bank Account Number
	 * @param bic        The Bank Identification Code
	 * @param name       The creditor (Initiating party) name
	 * @throws IllegalArgumentException if any property is invalid
	 */
	public Creditor(String iban, String bic, String name) {
		super(iban, bic, name);
	}


	/**
     * Returns the creditor's identification
     *
     * @return identification
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets and validates the creditor's identifier.
     * Required and should not be more than 35 characters in length
     *
     * @param identifier
     */
    public void setIdentifier(String identifier) {
        if (identifier == null || identifier.trim().isEmpty() || identifier.length() > 35) {
            throw new IllegalArgumentException("Creditor identifier invalid");
        }
        this.identifier = identifier;
    }
}
