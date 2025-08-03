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
package com.nttdocomo.lang;

public final class XString extends XObject 
{
    private final String str;

    public XString(String str) 
    {
        if (str == null) { throw new NullPointerException("Input string cannot be null"); }

        this.str = str;
    }

    public int length() { return str.length(); }

    public XString concat(XString xStr) 
    {
        if (xStr == null) { throw new NullPointerException("XString to concatenate cannot be null"); }
        
        return new XString(this.str + xStr.str);
    }

    public String getString() { return str; }

    @Override
    public String toString() { return "[XString] " + str; }

    @Override
    public boolean equals(Object obj) { return this == obj; }

    @Override
    public int hashCode() { return System.identityHashCode(this); }
}