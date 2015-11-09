package com.chessix.sepa.pain;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Main interface for generating PAIN documents.
 */
/*
 * This software is licensed under the MIT license, see the license.txt file in the root of the project or the META-INF directory of the jar file.
 */
public interface DocumentService {
    /**
     * Generates a formatted PAIN.008.002.02 string
     * @return a formatted String containing the PAIN008.02.02 xml
     */
    public String generatePain00800202(Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions);


    /**
     * Writes a formatted PAIN.008.002.02 to the specified OutputStream. Will not close the stream.
     */
    public void generatePain00800202(OutputStream outputStream, Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions);


    /**
     * Generates a PAIN.008.001.02 string
     * @return a formatted String containing the PAIN008.02.02 xml
     */
    public String generatePain00800102(Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions);


    /**
     * Writes a PAIN.008.001.02 to the specified OutputStream. Will not close the stream.
     */
    public void generatePain00800102(OutputStream outputStream, Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions);

	/**
	 * Writes a PAIN.001.001.03 to the specified OutputStream. Will not close the stream.
	 */
	public void generatePain00100103(OutputStream outputStream, InitiatingParty initiatingParty, Debtor debtor, List<Transaction> transactions, Date executionDate);


}
