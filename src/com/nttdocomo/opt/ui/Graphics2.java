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
package com.nttdocomo.opt.ui;

import org.recompile.mobile.Mobile;

// This class is used by some DoJa jars, but there's zero documentation on it
public abstract class Graphics2 extends org.recompile.mobile.PlatformGraphics
{ 
	public Graphics2(org.recompile.mobile.PlatformImage image) { super(image); }

	public void setRenderMode(int operation, int arg1, int arg2) 
	{
		Mobile.log(Mobile.LOG_WARNING, Graphics2.class.getPackage().getName() + "." + Graphics2.class.getSimpleName() + ": " + "setRenderMode not implemented " + operation + " " + arg1 + " " + arg2);
	}
}