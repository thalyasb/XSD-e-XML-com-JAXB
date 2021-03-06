package br.gov.ac.sefaz.validador;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ValidatorErrorHandler implements ErrorHandler {


    public void warning(SAXParseException exception) throws SAXException {
        System.out.println("\nWARNING");
        exception.printStackTrace();
    }

    public void error(SAXParseException exception) throws SAXException {
        System.out.println("\nERROR");
        exception.printStackTrace();
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("\nFATAL ERROR");
        exception.printStackTrace();
    }

}
