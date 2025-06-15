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

import javax.xml.namespace.QName;

public class FaultDetailException extends Exception 
{

    private final QName faultDetailName;
    private final Object faultDetail;

    public FaultDetailException(QName faultDetailName, Object faultDetail) 
    {
        super();
        this.faultDetailName = faultDetailName;
        this.faultDetail = faultDetail;
    }

    public Object getFaultDetail() { return faultDetail; }

    public QName getFaultDetailName() { return faultDetailName; }
}
