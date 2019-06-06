package com.amadeus.tec.h2o2pmml.xml.bind;

import java.io.StringWriter;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

/**
 * @author ahockkoon
 * JAXB-based XML marshaller utility class
 */
public class JAXBMarshaller {
	
	
	/**
	 * @param jaxbElement Object to be marshall into XML
	 * @param stringWriter StringWriter objectt that will contain the XML
	 * @throws DataBindingException
	 */
	public static void marshal(Object jaxbElement, StringWriter stringWriter) throws DataBindingException{
		JAXB.marshal(jaxbElement, stringWriter);
	}

}
