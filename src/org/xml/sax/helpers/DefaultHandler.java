/*
	This file is part of FreeJ2ME.

	FreeJ2ME is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	FreeJ2ME is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with FreeJ2ME.  If not, see http://www.gnu.org/licenses/
*/
package org.xml.sax.helpers;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;

public class DefaultHandler implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler
{

    public DefaultHandler() { super(); }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException { return null; }

    public void notationDecl(String name, String publicId, String systemId) throws SAXException { }

    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException { }

    public void setDocumentLocator(Locator locator) { }

    public void startDocument() throws SAXException { }

    public void endDocument() throws SAXException { }

    public void startPrefixMapping(String prefix, String uri) throws SAXException { }

    public void endPrefixMapping(String prefix) throws SAXException { }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException { }

    public void endElement(String uri, String localName, String qName) throws SAXException { }

    public void characters(char[] ch, int start, int length) throws SAXException { }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException { }

    public void processingInstruction(String target, String data) throws SAXException { }

    public void skippedEntity(String name) throws SAXException { }

    public void warning(SAXParseException e) throws SAXException { }

    public void error(SAXParseException e) throws SAXException { }

    public void fatalError(SAXParseException e) throws SAXException { throw e; }
}