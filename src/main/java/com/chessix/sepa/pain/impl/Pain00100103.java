package com.chessix.sepa.pain.impl;

import com.chessix.sepa.pain.Debtor;
import com.chessix.sepa.pain.InitiatingParty;
import com.chessix.sepa.pain.Transaction;
import com.chessix.sepa.pain.util.DocumentUtils;
import generated.pain00100103.AccountIdentification4Choice;
import generated.pain00100103.ActiveOrHistoricCurrencyAndAmount;
import generated.pain00100103.AmountType3Choice;
import generated.pain00100103.BranchAndFinancialInstitutionIdentification4;
import generated.pain00100103.CashAccount16;
import generated.pain00100103.ChargeBearerType1Code;
import generated.pain00100103.CreditTransferTransactionInformation10;
import generated.pain00100103.CustomerCreditTransferInitiationV03;
import generated.pain00100103.Document;
import generated.pain00100103.FinancialInstitutionIdentification7;
import generated.pain00100103.GroupHeader32;
import generated.pain00100103.ObjectFactory;
import generated.pain00100103.PartyIdentification32;
import generated.pain00100103.PaymentIdentification1;
import generated.pain00100103.PaymentInstructionInformation3;
import generated.pain00100103.PaymentMethod3Code;
import generated.pain00100103.PaymentTypeInformation19;
import generated.pain00100103.PostalAddress6;
import generated.pain00100103.RemittanceInformation5;
import generated.pain00100103.ServiceLevel8Choice;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Pain00100103 {
	private Document document;
	private InitiatingParty initiatingParty;
	private Debtor debtor;
	private Date executionDate;
	private List<Transaction> transactions;

	public Pain00100103(InitiatingParty initiatingParty, Debtor debtor, List<Transaction> transactions, Date executionDate) {
		this.initiatingParty = initiatingParty;
		this.transactions = transactions;
		this.debtor = debtor;
		this.executionDate = executionDate;
		initializeDocument();
	}

	void generate(OutputStream outputStream) throws JAXBException {
		createMarshaller().marshal(new ObjectFactory().createDocument(document), outputStream);
	}

	private Marshaller createMarshaller() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		return marshaller;
	}

	private void initializeDocument() {
		document = new Document();
		CustomerCreditTransferInitiationV03 sct = new CustomerCreditTransferInitiationV03();
		// Group Header
		GroupHeader32 hdr = new GroupHeader32();
		hdr.setMsgId(DocumentUtils.createUniqueId());
		hdr.setCreDtTm(DocumentUtils.toXmlDateTimeUTC(new Date()));
		hdr.setNbOfTxs("" + transactions.size());
		PartyIdentification32 party = new PartyIdentification32();
		party.setNm(initiatingParty.getName());
		hdr.setInitgPty(party);
		sct.setGrpHdr(hdr);

		// Payment Information
		PaymentInstructionInformation3 pi = new PaymentInstructionInformation3();
		pi.setPmtInfId(DocumentUtils.createUniqueId());
		pi.setPmtMtd(PaymentMethod3Code.TRF);
		PaymentTypeInformation19 pti = new PaymentTypeInformation19();
		ServiceLevel8Choice serviceLevel8Choice = new ServiceLevel8Choice();
		serviceLevel8Choice.setCd("SEPA");
		pti.setSvcLvl(serviceLevel8Choice);
		pi.setPmtTpInf(pti);
		pi.setReqdExctnDt(DocumentUtils.toXmlDate(executionDate));

		// Debtor
		PartyIdentification32 deb = new PartyIdentification32();
		deb.setNm(debtor.getName());
		pi.setDbtr(deb);
		CashAccount16 account = new CashAccount16();
		AccountIdentification4Choice iban = new AccountIdentification4Choice();
		iban.setIBAN(debtor.getIban());
		account.setId(iban);
		pi.setDbtrAcct(account);
		BranchAndFinancialInstitutionIdentification4 debBank = new BranchAndFinancialInstitutionIdentification4();
		FinancialInstitutionIdentification7 debBic = new FinancialInstitutionIdentification7();
		debBic.setBIC(debtor.getBic());
		debBank.setFinInstnId(debBic);
		pi.setDbtrAgt(debBank);

		pi.setChrgBr(ChargeBearerType1Code.SLEV);
		BigDecimal totalAmount = new BigDecimal("0.00");
		for(Transaction t : transactions) {
			totalAmount = totalAmount.add(t.getAmount());
			// Credit Transfer
			CreditTransferTransactionInformation10 ct = new CreditTransferTransactionInformation10();
			PaymentIdentification1 paymentIdentification1 = new PaymentIdentification1();
			paymentIdentification1.setEndToEndId(t.getEndToEndId());
			ct.setPmtId(paymentIdentification1);
			// Amount
			AmountType3Choice amountHolder = new AmountType3Choice();
			ActiveOrHistoricCurrencyAndAmount amount = new ActiveOrHistoricCurrencyAndAmount();
			amount.setCcy("EUR");
			amount.setValue(t.getAmount());
			amountHolder.setInstdAmt(amount);
			ct.setAmt(amountHolder);
			// Account
			BranchAndFinancialInstitutionIdentification4 crBicHolder = new BranchAndFinancialInstitutionIdentification4();
			FinancialInstitutionIdentification7 crBic = new FinancialInstitutionIdentification7();
			crBic.setBIC(t.getCreditor().getBic());
			crBicHolder.setFinInstnId(crBic);
			ct.setCdtrAgt(crBicHolder);
			PartyIdentification32 creditorParty = new PartyIdentification32();
			creditorParty.setNm(t.getCreditor().getName());

			// Address
			if(t.getCreditor().getCountryCode() != null || t.getCreditor().getAddressLine1() != null || t.getCreditor().getAddressLine2() != null) {
				PostalAddress6 address = new PostalAddress6();
				address.setCtry(t.getCreditor().getCountryCode());
				address.getAdrLine().add(t.getCreditor().getAddressLine1());
				address.getAdrLine().add(t.getCreditor().getAddressLine2());
				creditorParty.setPstlAdr(address);
			}

			ct.setCdtr(creditorParty);
			CashAccount16 crAccount = new CashAccount16();
			AccountIdentification4Choice crIban = new AccountIdentification4Choice();
			crIban.setIBAN(t.getCreditor().getIban());
			crAccount.setId(crIban);
			ct.setCdtrAcct(crAccount);
			// Remittance
			RemittanceInformation5 rmtinfo = new RemittanceInformation5();
			rmtinfo.getUstrd().add(DocumentUtils.filter(t.getRemittance()));
			ct.setRmtInf(rmtinfo);
			pi.getCdtTrfTxInf().add(ct);
		}
		sct.getGrpHdr().setCtrlSum(totalAmount);
		sct.getPmtInf().add(pi);
		document.setCstmrCdtTrfInitn(sct);
	}
}
