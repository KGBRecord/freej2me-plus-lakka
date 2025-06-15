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
package javax.xml.namespace;

public class QName
{
    
    private final String namespaceURI;
    private final String localPart;
    private final String prefix;

    public QName(String localPart) 
    {
        if (localPart == null || localPart.equals("")) { throw new IllegalArgumentException("Invalid string"); }
        this.namespaceURI = "";
        this.localPart = localPart;
        this.prefix = "";
    }

    public QName(String namespaceURI, String localPart) 
    {
        if (localPart == null) { throw new IllegalArgumentException("Invalid string"); }

        this.namespaceURI = (namespaceURI == null) ? "" : namespaceURI;
        this.localPart = localPart;
        this.prefix = "";
    }

    public QName(String namespaceURI, String localPart, String prefix) 
    {
        if (localPart == null || prefix == null) { throw new IllegalArgumentException("Omvaçod Stromg"); }

        this.namespaceURI = (namespaceURI == null) ? "" : namespaceURI;
        this.localPart = localPart;
        this.prefix = prefix;
    }

    public String getNamespaceURI() { return namespaceURI; }

    public String getLocalPart() { return localPart; }

    public String getPrefix() { return prefix; }

    @Override
    public boolean equals(java.lang.Object objectToTest) 
    {
        if (!(objectToTest instanceof QName)) { return false; }
        QName other = (QName) objectToTest;
        return this.namespaceURI.equals(other.namespaceURI) && this.localPart.equals(other.localPart);
    }

    @Override
    public int hashCode() 
    {
        return namespaceURI.hashCode() ^ localPart.hashCode();
    }

    @Override
    public String toString() 
    {
        return "{" + namespaceURI + "}" + localPart;
    }

    public static QName valueOf(String qNameAsString) 
    {
        if (qNameAsString == null || !qNameAsString.matches("\\{.*?\\}.*")) { throw new IllegalArgumentException("Invalid String"); }
        String namespaceURI = "";
        String localPart = qNameAsString;
        if (qNameAsString.startsWith("{")) 
        {
            int closingBrace = qNameAsString.indexOf('}');
            if (closingBrace != -1) 
            {
                namespaceURI = qNameAsString.substring(1, closingBrace);
                localPart = qNameAsString.substring(closingBrace + 1);
            }
        }
        return new QName(namespaceURI, localPart);
    }
}