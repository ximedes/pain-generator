package com.chessix.sepa.pain;

import com.chessix.sepa.pain.impl.DocumentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentServiceTest {

	private final static long MILLIS_IN_DAY = 1000L * 60L * 60L * 24L;
	private final static Random RANDOM = new Random(0);

	private DocumentService documentService;
	private XPathFactory xPathFactory;
	private FirstTransactions firstTransactions;
	private RecurringTransactions recurringTransactions;

	@Before
	public void init() {
		documentService = new DocumentServiceImpl();
		List<Transaction> transactions = Arrays.asList(createTransaction(0.01, "jr."), createTransaction(99.99, "sr."));
		firstTransactions = new FirstTransactions();
		firstTransactions.setCollectionDate(new Date(System.currentTimeMillis() + (MILLIS_IN_DAY * 5)));
		firstTransactions.getTransactions().addAll(transactions);

		recurringTransactions = new RecurringTransactions();
		recurringTransactions.getTransactions().addAll(transactions);
		recurringTransactions.setCollectionDate(new Date(System.currentTimeMillis() + (MILLIS_IN_DAY * 2)));
	}

	@Test
	public void testGeneratePain00800202() throws Exception {
		Transaction t = new Transaction(100.00 * 2, null, "s", null);// abuse Transaction to get same rounding as used for control sum
		String result = documentService.generatePain00800202(createCreditor(), createInitiatingParty(), firstTransactions, recurringTransactions);
		xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		String txCount = xPath.evaluate("//*[name()='NbOfTxs']", new InputSource(new StringReader(result)));
		String amount = xPath.evaluate("//*[name()='CtrlSum']", new InputSource(new StringReader(result)));
		assertEquals(t.getAmount(), new BigDecimal(amount));
		assertEquals(firstTransactions.getTransactions().size() + recurringTransactions.getTransactions().size(), Integer.parseInt(txCount));
		validate(new StreamSource(new StringReader(result)), "pain00800202.xsd");
	}

	@Test
	public void testGeneratePain00800102() throws Exception {
		Transaction t = new Transaction(100.00 * 2, null, "s", null);// abuse Transaction to get same rounding as used for control sum
		String result = documentService.generatePain00800102(createCreditor(), createInitiatingParty(), firstTransactions, recurringTransactions);
		xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		String txCount = xPath.evaluate("//*[name()='NbOfTxs']", new InputSource(new StringReader(result)));
		String amount = xPath.evaluate("//*[name()='CtrlSum']", new InputSource(new StringReader(result)));
		assertEquals(t.getAmount(), new BigDecimal(amount));
		assertEquals(firstTransactions.getTransactions().size() + recurringTransactions.getTransactions().size(), Integer.parseInt(txCount));
		validate(new StreamSource(new StringReader(result)), "pain.008.001.02.xsd");
	}


	@Test
	public void testGeneratePain00800202FirstOnly() throws Exception {
		Transaction t = new Transaction(100.00 * 1, null, "s", null);// abuse Transaction to get same rounding as used for control sum
		String result = documentService.generatePain00800202(createCreditor(), createInitiatingParty(), firstTransactions, null);
		xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		String txCount = xPath.evaluate("//*[name()='NbOfTxs']", new InputSource(new StringReader(result)));
		String amount = xPath.evaluate("//*[name()='CtrlSum']", new InputSource(new StringReader(result)));
		assertEquals(t.getAmount(), new BigDecimal(amount));
		assertEquals(firstTransactions.getTransactions().size(), Integer.parseInt(txCount));
		validate(new StreamSource(new StringReader(result)), "pain00800202.xsd");
	}

	@Test
	public void testGeneratePain00800102FirstOnly() throws Exception {
		Transaction t = new Transaction(100.00 * 1, null, "s", null);// abuse Transaction to get same rounding as used for control sum
		String result = documentService.generatePain00800102(createCreditor(), createInitiatingParty(), firstTransactions, null);
		xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		String txCount = xPath.evaluate("//*[name()='NbOfTxs']", new InputSource(new StringReader(result)));
		String amount = xPath.evaluate("//*[name()='CtrlSum']", new InputSource(new StringReader(result)));
		assertEquals(t.getAmount(), new BigDecimal(amount));
		assertEquals(firstTransactions.getTransactions().size(), Integer.parseInt(txCount));
		validate(new StreamSource(new StringReader(result)), "pain.008.001.02.xsd");
	}

	@Test
	public void testGeneratePain00800202RecurringOnly() throws Exception {
		Transaction t = new Transaction(100.00 * 1, null, "s", null);// abuse Transaction to get same rounding as used for control sum
		String result = documentService.generatePain00800202(createCreditor(), createInitiatingParty(), null, recurringTransactions);
		xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		String txCount = xPath.evaluate("//*[name()='NbOfTxs']", new InputSource(new StringReader(result)));
		String amount = xPath.evaluate("//*[name()='CtrlSum']", new InputSource(new StringReader(result)));
		assertEquals(t.getAmount(), new BigDecimal(amount));
		assertEquals(recurringTransactions.getTransactions().size(), Integer.parseInt(txCount));
		validate(new StreamSource(new StringReader(result)), "pain00800202.xsd");
	}

	@Test
	public void testGeneratePain00800102RecurringOnly() throws Exception {
		Transaction t = new Transaction(100.00 * 1, null, "s", null);// abuse Transaction to get same rounding as used for control sum
		String result = documentService.generatePain00800102(createCreditor(), createInitiatingParty(), null, recurringTransactions);
		xPathFactory = XPathFactory.newInstance();
		XPath xPath = xPathFactory.newXPath();
		String txCount = xPath.evaluate("//*[name()='NbOfTxs']", new InputSource(new StringReader(result)));
		String amount = xPath.evaluate("//*[name()='CtrlSum']", new InputSource(new StringReader(result)));
		assertEquals(t.getAmount(), new BigDecimal(amount));
		assertEquals(recurringTransactions.getTransactions().size(), Integer.parseInt(txCount));
		validate(new StreamSource(new StringReader(result)), "pain.008.001.02.xsd");
	}


	@Test
	public void testGenerateAndValidatePain00800202() throws Exception {
		File tempFile = File.createTempFile("pain008-", ".xml");
		FileOutputStream fos = new FileOutputStream(tempFile);
		try {
			documentService.generatePain00800202(fos, createCreditor(), createInitiatingParty(), firstTransactions, recurringTransactions);
		} finally {
			fos.close();
		}
		assertPainFile(tempFile, "pain00800202.xsd");
	}

	private void assertPainFile(File tempFile, String xsdFile) throws IOException, SAXException {
		assertTrue(tempFile.exists());
		assertTrue(tempFile.length() > 0);
		validate(new StreamSource(tempFile), xsdFile);
	}

	@Test
	public void testGenerateAndValidatePain00800102() throws Exception {
		File tempFile = File.createTempFile("pain008-", ".xml");
		FileOutputStream fos = new FileOutputStream(tempFile);
		try {
			documentService.generatePain00800102(fos, createCreditor(), createInitiatingParty(), firstTransactions, recurringTransactions);
		} finally {
			fos.close();
		}
		assertPainFile(tempFile, "pain.008.001.02.xsd");
	}

	@Test
	public void testGenerateAndValidatePain00100103() throws Exception {
		File tempFile = File.createTempFile("pain008-", ".xml");
		FileOutputStream fos = new FileOutputStream(tempFile);
		try {
			Creditor creditor = createCreditor();
			creditor.setCountryCode("NL");
			creditor.setAddressLine1("De straat 70");
			creditor.setAddressLine2("1234 AB, HAARLEM");
			Debtor debtor = new Debtor("NL13TEST0123456789", "TESTNL2A", "Jan Modaal");
			List<Transaction> transactions = Arrays.asList(createCreditTransaction(), createCreditTransaction(), createCreditTransaction());
			documentService.generatePain00100103(fos, createInitiatingParty(), debtor, transactions, new Date());
		} finally {
			fos.close();
		}
		assertPainFile(tempFile, "pain.001.001.03.xsd");
	}

	private Transaction createCreditTransaction() {
		Creditor creditor = new Creditor("NL13TEST0123456789", "TESTNL2A", "Creditor");
		creditor.setCountryCode("NL");
		creditor.setAddressLine1("De straat 70");
		creditor.setAddressLine2("1234 AB, HAARLEM");
		Transaction tx = new Transaction(12.50, creditor, "E2EID-" + nextLong(100000000000L, 1000000000000L));
		tx.setRemittance("We owned you this, have fun!");
		return tx;
	}

	private long nextLong(long least, long bound) {
		return ((RANDOM.nextLong() % (bound - least)) + least);
	}

	private void validate(StreamSource source, String xsdFile) throws IOException, SAXException {
		SchemaFactory fac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL xsdUrl = this.getClass().getResource("/schemas/" + xsdFile);
		Schema schema = fac.newSchema(xsdUrl);
		Validator validator = schema.newValidator();
		validator.validate(source);
	}

	private Creditor createCreditor() {
		return new Creditor("NL13TEST0123456789", "TESTNL2A", "Creditor", "NL00ZZZ405365330000");
	}

	private Debtor createDebtor(String postfix) {
		Mandate m = new Mandate("MNDT123456-" + postfix, new Date(System.currentTimeMillis() - (MILLIS_IN_DAY * 14)));
		return new Debtor("NL13TEST0123456789", "TESTNL2A", "Jan Modaal " + postfix, m);
	}

	private InitiatingParty createInitiatingParty() {
		return new InitiatingParty("PartySquad");
	}

	private Transaction createTransaction(double amount, String namePostfix) {
		return new Transaction(amount, createDebtor(namePostfix), "E2EID12345", "REM12345");
	}


}
