package com.chessix.sepa.pain;

/**
 * The debtor; paying party
 */
public class Debtor extends AccountHolder {
    private Mandate mandate;

    /**
     * Constructs a Debtor for a direct debit
     *
     * @param iban    The International Bank Account Number
     * @param bic     The Bank Identification Code
     * @param name    The debtor's name
     * @param mandate The debtor's mandate
     * @throws IllegalArgumentException if any property is invalid
     */
    public Debtor(String iban, String bic, String name, Mandate mandate) {
        super(iban, bic, name);
        setMandate(mandate);
    }

	/**
	 * Constructs a Debtor for credit transfer
	 *
	 * @param iban    The International Bank Account Number
	 * @param bic     The Bank Identification Code
	 * @param name    The debtor's name
	 * @throws IllegalArgumentException if any property is invalid
	 */
	public Debtor(String iban, String bic, String name) {
		super(iban, bic, name);
	}

    /**
     * Returns this debtor's mandate
     *
     * @return
     */
    public Mandate getMandate() {
        return mandate;
    }

    /**
     * Sets this debtor's mandate
     *
     * @param mandate
     */

    public void setMandate(Mandate mandate) {
        if (mandate == null) {
            throw new IllegalArgumentException("Mandate is required");
        }
        this.mandate = mandate;
    }
}
