package com.chessix.sepa.pain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Transaction {

    private String endToEndId;
    private String remittance;
    private BigDecimal amount;
    private Debtor debtor;
	private Creditor creditor;

	/**
	 * Creates an direct debit transaction (Pain.008)
	 * @param amount
	 * @param debtor
	 * @param endToEndId
	 * @param remittance
	 */
    public Transaction(double amount, Debtor debtor, String endToEndId, String remittance) {
        setEndToEndId(endToEndId);
        setRemittance(remittance);
        setAmount(amount);
        setDebtor(debtor);
    }

	/**
	 * Creates a credit transfer transaction (Pain.001)
	 * @param amount the amount in EUR
	 * @param creditor the beneficiary party
	 * @param endToEndId
	 */
	public Transaction(double amount, Creditor creditor, String endToEndId) {
		setEndToEndId(endToEndId);
		setAmount(amount);
		setCreditor(creditor);
	}

	public Creditor getCreditor() {
		return creditor;
	}

	public void setCreditor(Creditor creditor) {
		this.creditor = creditor;
	}

	public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    /**
     * Returns the transaction amount in EUR. Rounded to 2 digits after separator
     *
     * @return the transaction amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount in EUR. Will round and scale amounts if required.
     * e.g. setAmount(10) = 10.00 EUR
     * setAmount(10.1) = 10.10 EUR
     * setAmount(10.1234) = 10.12 EUR
     * Min: 0.01
     * Max: 999999999.99
     *
     * @param amount
     */
    public void setAmount(double amount) {
        if ((amount < 0.01) || (amount > 999999999.99)) {
            throw new IllegalArgumentException("Transaction amount must be between 0.01 and 999999999.99 EUR");
        }
        this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    /**
     * Sets and validates the end to end identification
     *
     * @param endToEndId is required and may no be longer than 35 characters
     */
    public void setEndToEndId(String endToEndId) {
        if (endToEndId == null || endToEndId.trim().isEmpty() || endToEndId.length() > 35) {
            throw new IllegalArgumentException("EndToEnd identification is required and cannot be longer than 35 characters");
        }
        this.endToEndId = endToEndId;
    }

    public String getRemittance() {
        return remittance;
    }

    /**
     * Sets and validates the remittance information (used as unstructured text in PAIN)
     * Usually shows up as a description on the debtor's bank statement
     *
     * @param remittance not required, should not be longer than 140 characters
     */
    public void setRemittance(String remittance) {
        if (remittance != null && remittance.length() > 140) {
            throw new IllegalArgumentException("Remittance invalid");
        }
        this.remittance = remittance;
    }
}
