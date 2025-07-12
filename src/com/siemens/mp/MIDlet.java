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
package com.siemens.mp;

import org.recompile.mobile.Mobile;

public abstract class MIDlet 
{ 

	public MIDlet() { }

	public String getAppProperty(String key) { return Mobile.midlet.getAppProperty(key); }

	public final void notifyDestroyed() { Mobile.midlet.notifyDestroyed(); }

	public final void notifyPaused() { Mobile.midlet.notifyPaused(); }

	public final boolean platformRequest(String URL) { return Mobile.midlet.platformRequest(URL); }

	public static String[] getSupportedProtocols() { return new String[0]; }
}