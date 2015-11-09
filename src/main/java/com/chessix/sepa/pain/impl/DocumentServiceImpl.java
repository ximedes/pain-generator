package com.chessix.sepa.pain.impl;

import com.chessix.sepa.pain.*;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Main implementation for generating PAIN files.
 */
/*
 * This software is licensed under the MIT license, see the license.txt file in the root of the project or the META-INF directory of the jar file.
 */
public class DocumentServiceImpl implements DocumentService {

    private static final String EXCEPTION_MESSAGE = "PAIN generation failed";

    @Override
    public String generatePain00800202(Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions) {
        Pain00800202 pain00800202 = new Pain00800202(creditor, initiatingParty, firstTransactions, recurringTransactions);
        StringWriter writer = new StringWriter();
        try {
            pain00800202.generate(writer);
        } catch (JAXBException e) {
            throw new IllegalStateException(EXCEPTION_MESSAGE, e);
        }
        return writer.toString();
    }

    @Override
    public void generatePain00800202(OutputStream outputStream, Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions) {
        Pain00800202 pain00800202 = new Pain00800202(creditor, initiatingParty, firstTransactions, recurringTransactions);
        try {
            pain00800202.generate(outputStream);
        } catch (JAXBException e) {
            throw new IllegalStateException(EXCEPTION_MESSAGE, e);
        }
    }

    @Override
    public String generatePain00800102(Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions) {
        Pain00800102 pain00800102 = new Pain00800102(creditor, initiatingParty, firstTransactions, recurringTransactions);
        StringWriter writer = new StringWriter();
        try {
            generate(pain00800102.createDocument(), writer);
        } catch (JAXBException e) {
            throw new IllegalStateException(EXCEPTION_MESSAGE, e);
        } catch (XMLStreamException e) {
            throw new IllegalStateException(EXCEPTION_MESSAGE, e);
        }
        return writer.toString();
    }

    @Override
    public void generatePain00800102(OutputStream outputStream, Creditor creditor, InitiatingParty initiatingParty, FirstTransactions firstTransactions, RecurringTransactions recurringTransactions) {
        Pain00800102 pain00800102 = new Pain00800102(creditor, initiatingParty, firstTransactions, recurringTransactions);
        try {
            generate(pain00800102.createDocument(), outputStream);
        } catch (JAXBException e) {
            throw new IllegalStateException(EXCEPTION_MESSAGE, e);
        } catch (XMLStreamException e) {
            throw new IllegalStateException(EXCEPTION_MESSAGE, e);
        }
    }

	@Override
	public void generatePain00100103(OutputStream outputStream, InitiatingParty initiatingParty, Debtor debtor, List<Transaction> transactions, Date executionDate) {
		Pain00100103 pain00100103 = new Pain00100103(initiatingParty, debtor, transactions, executionDate);
		try {
			pain00100103.generate(outputStream);
		} catch (JAXBException e) {
			throw new IllegalStateException(EXCEPTION_MESSAGE, e);
		}
	}

	void generate(JAXBElement document, OutputStream outputStream) throws JAXBException, XMLStreamException {
        XMLOutputFactory xml = XMLOutputFactory.newFactory();
        XMLStreamWriter xmlStreamWriter = xml.createXMLStreamWriter(outputStream, "UTF-8");
        xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
        createMarshaller(document.getDeclaredType()).marshal(document, xmlStreamWriter);
        xmlStreamWriter.writeEndDocument();
    }

    void generate(JAXBElement document, Writer writer) throws JAXBException, XMLStreamException {
        XMLOutputFactory xml = XMLOutputFactory.newFactory();
        XMLStreamWriter xmlStreamWriter = xml.createXMLStreamWriter(writer);
        xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
        createMarshaller(document.getDeclaredType()).marshal(document, xmlStreamWriter);
        xmlStreamWriter.writeEndDocument();
    }


    private Marshaller createMarshaller(Class document) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(document);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        return marshaller;
    }

}
