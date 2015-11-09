package com.chessix.sepa.pain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class TransactionBatch {
    private List<Transaction> transactions = new ArrayList<Transaction>();
    private Date collectionDate;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Returns the required collection date
     *
     * @return the required collection date
     */
    public Date getCollectionDate() {
        return collectionDate;
    }

    /**
     * Sets the required collection date
     * Normally this should be at least 2 working days in the future for recurring direct debits and 5 working days for first payments
     * Depending on the bank a payment can be declined or a new date is set when it does not comply with regulations
     *
     * @param collectionDate the collection date to set.
     */
    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }
}
