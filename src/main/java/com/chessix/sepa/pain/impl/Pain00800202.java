package com.chessix.sepa.pain.impl;

import com.chessix.sepa.pain.Creditor;
import com.chessix.sepa.pain.FirstTransactions;
import com.chessix.sepa.pain.InitiatingParty;
import com.chessix.sepa.pain.RecurringTransactions;
import com.chessix.sepa.pain.Transaction;
import com.chessix.sepa.pain.util.DocumentUtils;
import generated.pain008.AccountIdentificationSEPA;
import generated.pain008.ActiveOrHistoricCurrencyAndAmountSEPA;
import generated.pain008.ActiveOrHistoricCurrencyCodeEUR;
import generated.pain008.BranchAndFinancialInstitutionIdentificationSEPA1;
import generated.pain008.CashAccountSEPA1;
import generated.pain008.CashAccountSEPA2;
import generated.pain008.ChargeBearerTypeSEPACode;
import generated.pain008.CustomerDirectDebitInitiationV02;
import generated.pain008.DirectDebitTransactionInformationSDD;
import generated.pain008.DirectDebitTransactionSDD;
import generated.pain008.Document;
import generated.pain008.FinancialInstitutionIdentificationSEPA1;
import generated.pain008.GroupHeaderSDD;
import generated.pain008.IdentificationSchemeNameSEPA;
import generated.pain008.LocalInstrumentSEPA;
import generated.pain008.LocalInstrumentSEPACode;
import generated.pain008.MandateRelatedInformationSDD;
import generated.pain008.ObjectFactory;
import generated.pain008.PartyIdentificationSEPA1;
import generated.pain008.PartyIdentificationSEPA2;
import generated.pain008.PartyIdentificationSEPA3;
import generated.pain008.PartyIdentificationSEPA5;
import generated.pain008.PartySEPA2;
import generated.pain008.PaymentIdentificationSEPA;
import generated.pain008.PaymentInstructionInformationSDD;
import generated.pain008.PaymentMethod2Code;
import generated.pain008.PaymentTypeInformationSDD;
import generated.pain008.PersonIdentificationSEPA2;
import generated.pain008.RemittanceInformationSEPA1Choice;
import generated.pain008.RestrictedPersonIdentificationSEPA;
import generated.pain008.RestrictedPersonIdentificationSchemeNameSEPA;
import generated.pain008.SequenceType1Code;
import generated.pain008.ServiceLevelSEPA;
import generated.pain008.ServiceLevelSEPACode;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pain00800202 {

    private Creditor creditor;
    private InitiatingParty initiatingParty;
    private Document document;
    private String messageId;
    private FirstTransactions firstTransactions;
    private RecurringTransactions recurringTransactions;


    Pain00800202(Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions) {
        this(creditor, initiatingParty, firstTransactions, recurringTransactions, DocumentUtils.createUniqueId());
    }

    Pain00800202(Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions, String messageId) {
        this.creditor = creditor;
        this.messageId = messageId;
        this.initiatingParty = initiatingParty;
        this.firstTransactions = firstTransactions;
        this.recurringTransactions = recurringTransactions;
        initializeDocument();
    }

    void generate(OutputStream outputStream) throws JAXBException {
        createMarshaller().marshal(new ObjectFactory().createDocument(document), outputStream);
    }

    void generate(Writer writer) throws JAXBException {
        createMarshaller().marshal(new ObjectFactory().createDocument(document), writer);
    }

    private Marshaller createMarshaller() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        return marshaller;
    }

    private void initializeDocument() {
        document = new Document();
        CustomerDirectDebitInitiationV02 customerDirectDebitInitiationV02 = new CustomerDirectDebitInitiationV02();
        customerDirectDebitInitiationV02.setGrpHdr(createHeader());

        customerDirectDebitInitiationV02.getPmtInf().addAll(createPayments(creditor, firstTransactions, recurringTransactions));

        document.setCstmrDrctDbtInitn(customerDirectDebitInitiationV02);
    }

    private List<PaymentInstructionInformationSDD> createPayments(Creditor creditor, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions) {
        List<PaymentInstructionInformationSDD> result = new ArrayList<PaymentInstructionInformationSDD>();
        if (firstTransactions != null) {
            result.add(createPaymentInstructionInformation(true, firstTransactions.getTransactions(), creditor, firstTransactions.getCollectionDate()));
        }
        if (recurringTransactions != null) {
            result.add(createPaymentInstructionInformation(false, recurringTransactions.getTransactions(), creditor, recurringTransactions.getCollectionDate()));
        }
        return result;
    }

    private PaymentInstructionInformationSDD createPaymentInstructionInformation(boolean first, List<Transaction> transactions, Creditor creditor, Date collectionDate) {
        PaymentInstructionInformationSDD paymentInstructionInformation = new PaymentInstructionInformationSDD();
        paymentInstructionInformation.setBtchBookg(Boolean.FALSE);
        paymentInstructionInformation.setPmtInfId(DocumentUtils.createUniqueId());
        paymentInstructionInformation.setPmtMtd(PaymentMethod2Code.DD);
        PaymentTypeInformationSDD paymentTypeInformation = new PaymentTypeInformationSDD();
        ServiceLevelSEPA serviceLevelChoice = new ServiceLevelSEPA();
        serviceLevelChoice.setCd(ServiceLevelSEPACode.SEPA);
        paymentTypeInformation.setSvcLvl(serviceLevelChoice);
        LocalInstrumentSEPA localInstrumentChoice = new LocalInstrumentSEPA();
        localInstrumentChoice.setCd(LocalInstrumentSEPACode.CORE);
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
        paymentInstructionInformation.setChrgBr(ChargeBearerTypeSEPACode.SLEV);
        for (Transaction t : transactions) {
            paymentInstructionInformation.getDrctDbtTxInf().add(fillDirectDebitTransactionInformation(t, creditor));
        }
        return paymentInstructionInformation;
    }

    private DirectDebitTransactionInformationSDD fillDirectDebitTransactionInformation(Transaction transaction, Creditor creditor) {
        DirectDebitTransactionInformationSDD directDebitTransactionInformation = new DirectDebitTransactionInformationSDD();
        PaymentIdentificationSEPA paymentId = new PaymentIdentificationSEPA();
        paymentId.setEndToEndId(transaction.getEndToEndId());
        directDebitTransactionInformation.setPmtId(paymentId);
        ActiveOrHistoricCurrencyAndAmountSEPA amount = new ActiveOrHistoricCurrencyAndAmountSEPA();
        amount.setCcy(ActiveOrHistoricCurrencyCodeEUR.EUR);
        amount.setValue(transaction.getAmount());
        directDebitTransactionInformation.setInstdAmt(amount);
        directDebitTransactionInformation.setDrctDbtTx(addMandate(transaction, creditor));
        directDebitTransactionInformation.setDbtrAgt(createBIC(transaction.getDebtor().getBic()));
        directDebitTransactionInformation.setDbtr(fillDebtorName(transaction));
        directDebitTransactionInformation.setDbtrAcct(fillDebtorAccount(transaction));
        directDebitTransactionInformation.setRmtInf(fillRemittanceInformation(transaction.getRemittance()));
        return directDebitTransactionInformation;
    }

    private RemittanceInformationSEPA1Choice fillRemittanceInformation(String remittance) {
        if (remittance == null) {
            return null;
        }
        RemittanceInformationSEPA1Choice remittanceInformation = new RemittanceInformationSEPA1Choice();
        remittanceInformation.setUstrd(remittance);
        return remittanceInformation;
    }

    private PartyIdentificationSEPA2 fillDebtorName(Transaction transaction) {
        PartyIdentificationSEPA2 debtor = new PartyIdentificationSEPA2();
        debtor.setNm(transaction.getDebtor().getName());
        return debtor;
    }

    private CashAccountSEPA2 fillDebtorAccount(Transaction transaction) {
        CashAccountSEPA2 debtorAccount = new CashAccountSEPA2();
        AccountIdentificationSEPA debtorAccountId = new AccountIdentificationSEPA();
        debtorAccountId.setIBAN(transaction.getDebtor().getIban());
        debtorAccount.setId(debtorAccountId);
        return debtorAccount;
    }

    private DirectDebitTransactionSDD addMandate(Transaction transaction, Creditor creditor) {
        DirectDebitTransactionSDD directDebitTransaction = new DirectDebitTransactionSDD();
        MandateRelatedInformationSDD mandateRelatedInformation = new MandateRelatedInformationSDD();
        mandateRelatedInformation.setMndtId(transaction.getDebtor().getMandate().getMandateId());
        mandateRelatedInformation.setDtOfSgntr(DocumentUtils.toXmlDate(transaction.getDebtor().getMandate().getSigned()));
        mandateRelatedInformation.setAmdmntInd(false);
        directDebitTransaction.setMndtRltdInf(mandateRelatedInformation);
        PartyIdentificationSEPA3 creditorSchemeIdentification = new PartyIdentificationSEPA3();
        PartySEPA2 partyChoice = new PartySEPA2();
        PersonIdentificationSEPA2 personIdentification = new PersonIdentificationSEPA2();
        RestrictedPersonIdentificationSEPA restrictedPersonIdentification = new RestrictedPersonIdentificationSEPA();
        RestrictedPersonIdentificationSchemeNameSEPA restrictedPersonIdentificationSchemeName = new RestrictedPersonIdentificationSchemeNameSEPA();
        restrictedPersonIdentificationSchemeName.setPrtry(IdentificationSchemeNameSEPA.SEPA);
        restrictedPersonIdentification.setSchmeNm(restrictedPersonIdentificationSchemeName);
        restrictedPersonIdentification.setId(creditor.getIdentifier());
        personIdentification.setOthr(restrictedPersonIdentification);
        partyChoice.setPrvtId(personIdentification);
        creditorSchemeIdentification.setId(partyChoice);
        directDebitTransaction.setCdtrSchmeId(creditorSchemeIdentification);
        return directDebitTransaction;
    }

    private BranchAndFinancialInstitutionIdentificationSEPA1 createBIC(String bic) {
        BranchAndFinancialInstitutionIdentificationSEPA1 creditorAgent = new BranchAndFinancialInstitutionIdentificationSEPA1();
        FinancialInstitutionIdentificationSEPA1 financialInstitutionIdentification = new FinancialInstitutionIdentificationSEPA1();
        financialInstitutionIdentification.setBIC(bic);
        creditorAgent.setFinInstnId(financialInstitutionIdentification);
        return creditorAgent;
    }

    private CashAccountSEPA1 fillCreditorAccount(Creditor creditor) {
        CashAccountSEPA1 cashAccountSEPA1 = new CashAccountSEPA1();
        AccountIdentificationSEPA id = new AccountIdentificationSEPA();
        id.setIBAN(creditor.getIban());
        cashAccountSEPA1.setId(id);
        return cashAccountSEPA1;
    }

    private PartyIdentificationSEPA5 fillCreditor(Creditor creditor) {
        PartyIdentificationSEPA5 partyIdentificationSEPA5 = new PartyIdentificationSEPA5();
        partyIdentificationSEPA5.setNm(creditor.getName());
        return partyIdentificationSEPA5;
    }

    private GroupHeaderSDD createHeader() {
        GroupHeaderSDD groupHeader = new GroupHeaderSDD();
        groupHeader.setMsgId(messageId);
        groupHeader.setCreDtTm(DocumentUtils.toXmlDateTimeUTC(new Date()));
        int tx = 0;
        if (firstTransactions != null) {
            tx += firstTransactions.getTransactions().size();
        }
        if (recurringTransactions != null) {
            tx += recurringTransactions.getTransactions().size();
        }
        groupHeader.setNbOfTxs("" + tx);
        groupHeader.setCtrlSum(calculateControlSum());
        groupHeader.setInitgPty(createPartyIdentification());
        return groupHeader;
    }

    private BigDecimal calculateControlSum() {
        BigDecimal total = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        if (firstTransactions != null) {
            for (Transaction t : firstTransactions.getTransactions()) {
                total = total.add(t.getAmount());
            }
        }
        if (recurringTransactions != null) {
            for (Transaction t : recurringTransactions.getTransactions()) {
                total = total.add(t.getAmount());
            }
        }
        return total;
    }

    private PartyIdentificationSEPA1 createPartyIdentification() {
        if (initiatingParty == null) {
            return null;
        }
        PartyIdentificationSEPA1 party = new PartyIdentificationSEPA1();
        party.setNm(initiatingParty.getName());
        return party;
    }

}
