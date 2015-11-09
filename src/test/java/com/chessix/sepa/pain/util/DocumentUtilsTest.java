package com.chessix.sepa.pain.util;

import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class DocumentUtilsTest {

	@Test
	public void testToXmlDate() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(2014, Calendar.DECEMBER, 15, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);

		XMLGregorianCalendar actual = DocumentUtils.toXmlDate(cal.getTime());

		assertEquals("2014-12-15", actual.toXMLFormat());
	}
}
