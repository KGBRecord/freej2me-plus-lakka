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

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public abstract class SAXParserFactory 
{

    protected SAXParserFactory() { }

    public static SAXParserFactory newInstance() throws FactoryConfigurationError 
    {
        throw new UnsupportedOperationException("Factory method not implemented.");
    }

    public abstract SAXParser newSAXParser() throws ParserConfigurationException, SAXException;

    public abstract void setFeature(String name, boolean value) 
            throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;

    public abstract boolean getFeature(String name) 
            throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException;

    public void setNamespaceAware(boolean awareness) { }

    public boolean isNamespaceAware() 
    {
        return false;
    }

    public void setValidating(boolean validating) { }

    public boolean isValidating() 
    {
        return false;
    }
}