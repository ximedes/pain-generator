package com.chessix.sepa.pain;


import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DialectHandlerTest {

	private DialectHandler defaultDialectHandler = new DialectHandler(Dialect.DEFAULT);
	private DialectHandler raboDialectHandler = new DialectHandler(Dialect.RABOBANK);

	@Test
	public void testGetCreDtTm() throws ParseException {
		// Prepare
		Date date = new Date(1484929215302L);

		// Execute
		XMLGregorianCalendar defaultCreDtTmD = defaultDialectHandler.getCreDtTm(date);
		XMLGregorianCalendar raboCreDtTmD = raboDialectHandler.getCreDtTm(date);

		// Verify
		assertEquals("2017-01-20T16:20:15.302Z", defaultCreDtTmD.toString());
		assertEquals("2017-01-20T16:20:15", raboCreDtTmD.toString());
	}

}