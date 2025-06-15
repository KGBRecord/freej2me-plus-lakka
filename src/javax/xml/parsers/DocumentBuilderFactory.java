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
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public abstract class DocumentBuilderFactory 
{

    protected DocumentBuilderFactory() { }

    public static DocumentBuilderFactory newInstance() 
    {
        throw new UnsupportedOperationException("Factory method not implemented.");
    }

    public abstract DocumentBuilder newDocumentBuilder() throws ParserConfigurationException;

    public abstract void setFeature(String name, boolean value) throws ParserConfigurationException;

    public abstract boolean getFeature(String name) throws ParserConfigurationException;

    public abstract void setAttribute(String name, Object value) throws IllegalArgumentException;

    public abstract Object getAttribute(String name) throws IllegalArgumentException;

    public abstract void setNamespaceAware(boolean awareness);

    public abstract boolean isNamespaceAware();

    public abstract void setValidating(boolean validating);

    public abstract boolean isValidating();

    public void setIgnoringComments(boolean ignoreComments) { }

    public boolean isIgnoringComments() 
    {
        return false;
    }

    public void setExpandEntityReferences(boolean expandEntityRef) { }

    public boolean isExpandEntityReferences() 
    {
        return true;
    }

    public void setIgnoringElementContentWhitespace(boolean whitespace) { }

    public boolean isIgnoringElementContentWhitespace() 
    {
        return false;
    }

    public void setCoalescing(boolean coalescing) { }

    public boolean isCoalescing() 
    {
        return false;
    }

    public abstract void setEntityResolver(EntityResolver er);

    public abstract void setErrorHandler(ErrorHandler eh);
}