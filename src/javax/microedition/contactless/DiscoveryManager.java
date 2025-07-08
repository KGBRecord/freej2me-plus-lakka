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
package javax.microedition.contactless;

import javax.microedition.contactless.ndef.NDEFRecordListener;
import javax.microedition.contactless.ndef.NDEFRecordType;

public class DiscoveryManager 
{

    private static DiscoveryManager instance;

    private DiscoveryManager() { }

    public static DiscoveryManager getInstance() 
    {
        if (instance == null) { instance = new DiscoveryManager(); }
        return instance;
    }

    public void addNDEFRecordListener(NDEFRecordListener listener, NDEFRecordType recordType) 
            throws ContactlessException, IllegalStateException { }

    public void addTargetListener(TargetListener listener, TargetType targetType) 
            throws ContactlessException, IllegalStateException { }

    public void addTransactionListener(TransactionListener listener) 
            throws ContactlessException { }

    public String getProperty(String name) { return null; }

    public static TargetType[] getSupportedTargetTypes() { return null; }

    public void removeNDEFRecordListener(NDEFRecordListener listener, NDEFRecordType recordType) { }

    public void removeTargetListener(TargetListener listener, TargetType targetType) { }

    public void removeTransactionListener(TransactionListener listener) { }

    public void setProperty(String name, String value) { }
}