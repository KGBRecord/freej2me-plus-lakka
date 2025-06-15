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
package javax.xml.parsers;

import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public abstract class DocumentBuilder 
{

    protected DocumentBuilder() { }

    public abstract DOMImplementation getDOMImplementation();

    public abstract boolean isNamespaceAware();

    public abstract boolean isValidating();

    public abstract Document newDocument();

    public abstract Document parse(InputSource is) throws SAXException, IOException;

    public Document parse(InputStream is) throws SAXException, IOException 
    {
        if (is == null) 
        {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        return parse(new InputSource(is));
    }

    public abstract void setEntityResolver(EntityResolver er);

    public abstract void setErrorHandler(ErrorHandler eh);
}