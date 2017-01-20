package com.chessix.sepa.pain;

import com.chessix.sepa.pain.util.DocumentUtils;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;

/**
 * This class handles all the values that are different between dialects
 */
public class DialectHandler {

    private Dialect dialect;

    public DialectHandler(Dialect dialect) {
        this.dialect = dialect;
    }

    public XMLGregorianCalendar getCreDtTm(Date date) {
        switch (dialect) {
            case RABOBANK:
                XMLGregorianCalendar xmlGregorianCalendar = DocumentUtils.toXmlDateTimeUTC(date);
                xmlGregorianCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
                xmlGregorianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
                return xmlGregorianCalendar;
            case ING:
            case DEFAULT:
            default:
                return DocumentUtils.toXmlDateTimeUTC(date);
        }
    }
}
