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
package com.nttdocomo.ui;

import java.awt.Color;

public class Graphics extends org.recompile.mobile.PlatformGraphics
{ 

	public static final int AQUA = Color.CYAN.getRGB();
	public static final int BLACK = Color.BLACK.getRGB();
	public static final int BLUE = Color.BLUE.getRGB();
	public static final int FUCHSIA = Color.MAGENTA.getRGB();
	public static final int GRAY = Color.GRAY.getRGB();
	public static final int GREEN = Color.GREEN.getRGB();
	public static final int LIME = Color.GREEN.brighter().getRGB();
	public static final int MAROON = new Color(128, 0, 0).getRGB();
	public static final int NAVY = new Color(0, 0, 128).getRGB();
	public static final int OLIVE = new Color(128, 128, 0).getRGB();
	public static final int PURPLE = new Color(128, 0, 128).getRGB();
	public static final int RED = Color.RED.getRGB();
	public static final int SILVER = Color.LIGHT_GRAY.getRGB();
	public static final int TEAL = new Color(0, 128, 128).getRGB();
	public static final int WHITE = Color.WHITE.getRGB();
	public static final int YELLOW = Color.YELLOW.getRGB();

	protected Graphics() { super(); }

	public Graphics(org.recompile.mobile.PlatformImage image) { super(image); }
}