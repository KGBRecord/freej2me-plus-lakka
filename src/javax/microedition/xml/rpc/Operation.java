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
package javax.microedition.xml.rpc;

import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

public class Operation 
{

    public static final String SOAPACTION_URI_PROPERTY = "SOAPAction";

    protected Operation() { }

    public static Operation newInstance(QName name, Element input, Element output) 
    {
        return new Operation();
    }

    public static Operation newInstance(QName name, Element input, Element output, FaultDetailHandler faultDetailHandler) 
    {
        return new Operation();
    }

    public void setProperty(String name, String value) 
    {
        if (name == null || value == null) 
        {
            throw new IllegalArgumentException("Property name and value cannot be null");
        }
    }

    public Object invoke(Object inParams) throws JAXRPCException 
    {
        return null;
    }
}
