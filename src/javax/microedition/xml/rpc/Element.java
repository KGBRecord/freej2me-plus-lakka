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


import javax.microedition.xml.rpc.Type;
import javax.xml.namespace.QName;

public class Element extends Type 
{
    public static final int UNBOUNDED = -1;

    public final QName name;
    public final Type contentType;
    public final boolean isNillable;
    public final boolean isArray;
    public final boolean isOptional;
    public final int minOccurs;
    public final int maxOccurs;

    public Element(QName name, Type type) 
    {
        this(name, type, 1, 1, false);
    }

    public Element(QName name, Type type, int minOccurs, int maxOccurs, boolean nillable) 
    {
        super();
        if (minOccurs < 0 || name == null || type == null || type instanceof Element) { throw new IllegalArgumentException("Received illegal argument for RPC Element"); }
        this.name = name;
        this.contentType = type;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
        this.isNillable = nillable;
        this.isOptional = (minOccurs == 0);
        this.isArray = (maxOccurs > 1);
    }
}
