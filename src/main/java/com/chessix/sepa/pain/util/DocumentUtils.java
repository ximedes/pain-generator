package com.chessix.sepa.pain.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

public class DocumentUtils {

    private static DatatypeFactory factory = null;
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

	private static final char[] validChars ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/-?:().,'+ ".toCharArray();

    static {
        try {
			Arrays.sort(validChars);
            factory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new IllegalArgumentException("Could not instantiate DatatypeFactory", e);
        }
    }

    /**
     * Returns a XMLGregorianCalender (xml:dateTime) in UTC
     *
     * @param date the date to convert
     * @return dateTime in UTC e.g. 2012-03-14T12:34.567Z
     */
    public static XMLGregorianCalendar toXmlDateTimeUTC(Date date) {
        if (date != null) {
            GregorianCalendar cal = new GregorianCalendar(UTC);
            cal.setTime(date);
            return factory.newXMLGregorianCalendar(cal);
        } else {
            return null;
        }
    }

    /**
     * Returns a XMLGregorianCalender (xml:date) in current timezone based upon the given date only
     *
     * @param date the date to convert
     * @return date e.g. 2012-03-14
     */
    public static XMLGregorianCalendar toXmlDate(Date date) {
        if (date != null) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            XMLGregorianCalendar xmlGregorianCalendar = factory.newXMLGregorianCalendar(cal);
            xmlGregorianCalendar.setHour(DatatypeConstants.FIELD_UNDEFINED);
            xmlGregorianCalendar.setMinute(DatatypeConstants.FIELD_UNDEFINED);
            xmlGregorianCalendar.setSecond(DatatypeConstants.FIELD_UNDEFINED);
            xmlGregorianCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
            xmlGregorianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            return xmlGregorianCalendar;
        } else {
            return null;
        }
    }

    /**
     * Generates an uppercase UUID without separators (length = 32)
     */
    public static String createUniqueId() {
        UUID uuid = UUID.randomUUID();
        String messageId = uuid.toString().replace("-", "").toUpperCase();
        return messageId;
    }

	/**
	 * Filters all characters which are not allowed in a SEPA xml file and replaces them with a ?
	 * @param input
	 * @return filtered output
	 */
	public static String filter(String input) {
		 if(input == null) {
			 return null;
		 }
		StringBuilder filtered = new StringBuilder();
		for(char c : input.toCharArray()) {
			if(Arrays.binarySearch(validChars, c) < 0) {
				filtered.append('?');
			} else {
				filtered.append(c);
			}
		}
		return filtered.toString();
	}

}
