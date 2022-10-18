package com.chessix.sepa.pain.impl;

import com.chessix.sepa.pain.*;
import com.chessix.sepa.pain.util.DocumentUtils;
import generated.pain00800102.AccountIdentification4Choice;
import generated.pain00800102.ActiveOrHistoricCurrencyAndAmount;
import generated.pain00800102.BranchAndFinancialInstitutionIdentification4;
import generated.pain00800102.CashAccount16;
import generated.pain00800102.ChargeBearerType1Code;
import generated.pain00800102.CustomerDirectDebitInitiationV02;
import generated.pain00800102.DirectDebitTransaction6;
import generated.pain00800102.DirectDebitTransactionInformation9;
import generated.pain00800102.Document;
import generated.pain00800102.FinancialInstitutionIdentification7;
import generated.pain00800102.GenericPersonIdentification1;
import generated.pain00800102.GroupHeader39;
import generated.pain00800102.LocalInstrument2Choice;
import generated.pain00800102.MandateRelatedInformation6;
import generated.pain00800102.ObjectFactory;
import generated.pain00800102.Party6Choice;
import generated.pain00800102.PartyIdentification32;
import generated.pain00800102.PaymentIdentification1;
import generated.pain00800102.PaymentInstructionInformation4;
import generated.pain00800102.PaymentMethod2Code;
import generated.pain00800102.PaymentTypeInformation20;
import generated.pain00800102.PersonIdentification5;
import generated.pain00800102.PersonIdentificationSchemeName1Choice;
import generated.pain00800102.RemittanceInformation5;
import generated.pain00800102.SequenceType1Code;
import generated.pain00800102.ServiceLevel8Choice;

import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pain00800102 {

    private final Creditor creditor;
    private final InitiatingParty initiatingParty;
    private Document document;
    private final String messageId;
    private final List<TransactionBatch> subBatches;
    private final boolean useBatchBooking;

    private final DialectHandler dialectHandler;

    Pain00800102(Creditor creditor, InitiatingParty initiatingParty, List<TransactionBatch> subBatches, boolean useBatchBooking, Dialect dialect) {
        this(creditor, initiatingParty, subBatches, DocumentUtils.createUniqueId(), useBatchBooking, dialect);
    }

    Pain00800102(Creditor creditor, InitiatingParty initiatingParty, List<TransactionBatch> subBatches, String messageId, boolean useBatchBooking, Dialect dialect) {
        this.creditor = creditor;
        this.messageId = messageId;
        this.initiatingParty = initiatingParty;
        this.subBatches = subBatches;
        this.useBatchBooking = useBatchBooking;

        dialectHandler = new DialectHandler(dialect);
    }

    JAXBElement createDocument() {
        initializeDocument();
        return new ObjectFactory().createDocument(document);
    }

    private void initializeDocument() {
        document = new Document();
        CustomerDirectDebitInitiationV02 customerDirectDebitInitiationV02 = new CustomerDirectDebitInitiationV02();
        customerDirectDebitInitiationV02.setGrpHdr(createHeader());

        customerDirectDebitInitiationV02.getPmtInf().addAll(createPayments(creditor, subBatches));

        document.setCstmrDrctDbtInitn(customerDirectDebitInitiationV02);
    }

    private List<PaymentInstructionInformation4> createPayments(Creditor creditor, List<TransactionBatch> batches) {
        List<PaymentInstructionInformation4> result = new ArrayList<PaymentInstructionInformation4>();
        batches.stream().filter(batch -> batch instanceof FirstTransactions)
                .forEach(firstTransactions -> {
                    result.add(createPaymentInstructionInformation(true, firstTransactions.getTransactions(), creditor, firstTransactions.getCollectionDate()));
                });
        batches.stream().filter(batch -> batch instanceof RecurringTransactions)
                .forEach(recurringTransactions -> {
                    result.add(createPaymentInstructionInformation(false, recurringTransactions.getTransactions(), creditor, recurringTransactions.getCollectionDate()));
                });
        return result;
    }

    private PaymentInstructionInformation4 createPaymentInstructionInformation(boolean first, List<Transaction> transactions, Creditor creditor, Date collectionDate) {
        PaymentInstructionInformation4 paymentInstructionInformation = new PaymentInstructionInformation4();
        paymentInstructionInformation.setBtchBookg(useBatchBooking);
        paymentInstructionInformation.setPmtInfId(DocumentUtils.createUniqueId());
        paymentInstructionInformation.setPmtMtd(PaymentMethod2Code.DD);
        PaymentTypeInformation20 paymentTypeInformation = new PaymentTypeInformation20();
        ServiceLevel8Choice serviceLevelChoice = new ServiceLevel8Choice();
        serviceLevelChoice.setCd("SEPA");
        paymentTypeInformation.setSvcLvl(serviceLevelChoice);
        LocalInstrument2Choice localInstrumentChoice = new LocalInstrument2Choice();
        localInstrumentChoice.setCd("CORE");
        paymentTypeInformation.setLclInstrm(localInstrumentChoice);
        if (first) {
            paymentTypeInformation.setSeqTp(SequenceType1Code.FRST);
        } else {
            paymentTypeInformation.setSeqTp(SequenceType1Code.RCUR);
        }
        paymentInstructionInformation.setPmtTpInf(paymentTypeInformation);
        paymentInstructionInformation.setReqdColltnDt(DocumentUtils.toXmlDate(collectionDate));
        paymentInstructionInformation.setCdtr(fillCreditor(creditor));
        paymentInstructionInformation.setCdtrAcct(fillCreditorAccount(creditor));
        paymentInstructionInformation.setCdtrAgt(createBIC(creditor.getBic()));
        paymentInstructionInformation.setChrgBr(ChargeBearerType1Code.SLEV);
        for (Transaction t : transactions) {
            paymentInstructionInformation.getDrctDbtTxInf().add(fillDirectDebitTransactionInformation(t, creditor));
        }
        return paymentInstructionInformation;
    }

    private DirectDebitTransactionInformation9 fillDirectDebitTransactionInformation(Transaction transaction, Creditor creditor) {
        DirectDebitTransactionInformation9 directDebitTransactionInformation = new DirectDebitTransactionInformation9();
        PaymentIdentification1 paymentId = new PaymentIdentification1();
        paymentId.setEndToEndId(transaction.getEndToEndId());
        directDebitTransactionInformation.setPmtId(paymentId);
        ActiveOrHistoricCurrencyAndAmount amount = new ActiveOrHistoricCurrencyAndAmount();
        amount.setCcy("EUR");
        amount.setValue(transaction.getAmount());
        directDebitTransactionInformation.setInstdAmt(amount);
        directDebitTransactionInformation.setDrctDbtTx(addMandate(transaction, creditor));
        directDebitTransactionInformation.setDbtrAgt(createBIC(transaction.getDebtor().getBic()));
        directDebitTransactionInformation.setDbtr(fillDebtorName(transaction));
        directDebitTransactionInformation.setDbtrAcct(fillDebtorAccount(transaction));
        directDebitTransactionInformation.setRmtInf(fillRemittanceInformation(transaction.getRemittance()));
        return directDebitTransactionInformation;
    }

    private RemittanceInformation5 fillRemittanceInformation(String remittance) {
        if (remittance == null) {
            return null;
        }
        RemittanceInformation5 remittanceInformation = new RemittanceInformation5();
        remittanceInformation.getUstrd().add(remittance);
        return remittanceInformation;
    }

    private PartyIdentification32 fillDebtorName(Transaction transaction) {
        PartyIdentification32 debtor = new PartyIdentification32();
        debtor.setNm(transaction.getDebtor().getName());
        return debtor;
    }

    private CashAccount16 fillDebtorAccount(Transaction transaction) {
        CashAccount16 debtorAccount = new CashAccount16();
        AccountIdentification4Choice debtorAccountId = new AccountIdentification4Choice();
        debtorAccountId.setIBAN(transaction.getDebtor().getIban());
        debtorAccount.setId(debtorAccountId);
        return debtorAccount;
    }

    private DirectDebitTransaction6 addMandate(Transaction transaction, Creditor creditor) {
        DirectDebitTransaction6 directDebitTransaction = new DirectDebitTransaction6();
        MandateRelatedInformation6 mandateRelatedInformation = new MandateRelatedInformation6();
        mandateRelatedInformation.setMndtId(transaction.getDebtor().getMandate().getMandateId());
        mandateRelatedInformation.setDtOfSgntr(DocumentUtils.toXmlDate(transaction.getDebtor().getMandate().getSigned()));
        directDebitTransaction.setMndtRltdInf(mandateRelatedInformation);
        PartyIdentification32 creditorSchemeIdentification = new PartyIdentification32();
        Party6Choice partyChoice = new Party6Choice();
        PersonIdentification5 personIdentification = new PersonIdentification5();
        GenericPersonIdentification1 p = new GenericPersonIdentification1();
        p.setId(creditor.getIdentifier());
        PersonIdentificationSchemeName1Choice s = new PersonIdentificationSchemeName1Choice();
        s.setPrtry("SEPA");
        p.setSchmeNm(s);
        personIdentification.getOthr().add(p);
        partyChoice.setPrvtId(personIdentification);
        creditorSchemeIdentification.setId(partyChoice);
        directDebitTransaction.setCdtrSchmeId(creditorSchemeIdentification);
        return directDebitTransaction;
    }

    private BranchAndFinancialInstitutionIdentification4 createBIC(String bic) {
        BranchAndFinancialInstitutionIdentification4 creditorAgent = new BranchAndFinancialInstitutionIdentification4();
        FinancialInstitutionIdentification7 financialInstitutionIdentification = new FinancialInstitutionIdentification7();
        financialInstitutionIdentification.setBIC(bic);
        creditorAgent.setFinInstnId(financialInstitutionIdentification);
        return creditorAgent;
    }

    private CashAccount16 fillCreditorAccount(Creditor creditor) {
        CashAccount16 cashAccountSEPA1 = new CashAccount16();
        AccountIdentification4Choice id = new AccountIdentification4Choice();
        id.setIBAN(creditor.getIban());
        cashAccountSEPA1.setId(id);
        return cashAccountSEPA1;
    }

    private PartyIdentification32 fillCreditor(Creditor creditor) {
        PartyIdentification32 partyIdentificationSEPA5 = new PartyIdentification32();
        partyIdentificationSEPA5.setNm(creditor.getName());
        return partyIdentificationSEPA5;
    }

    private GroupHeader39 createHeader() {
        GroupHeader39 groupHeader = new GroupHeader39();
        groupHeader.setMsgId(messageId);
        groupHeader.setCreDtTm(dialectHandler.getCreDtTm(new Date()));
        int tx = 0;

        tx += subBatches.stream()
                .mapToLong(batch -> batch.getTransactions().size())
                .sum();
        groupHeader.setNbOfTxs("" + tx);
        groupHeader.setCtrlSum(calculateControlSum());
        groupHeader.setInitgPty(createPartyIdentification());
        return groupHeader;
    }

    private BigDecimal calculateControlSum() {
        return subBatches.stream()
                .flatMap(batch -> batch.getTransactions().stream())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private PartyIdentification32 createPartyIdentification() {
        if (initiatingParty == null) {
            return null;
        }
        PartyIdentification32 party = new PartyIdentification32();
        party.setNm(initiatingParty.getName());
        return party;
    }

}
