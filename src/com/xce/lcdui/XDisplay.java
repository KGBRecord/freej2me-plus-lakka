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
package com.xce.lcdui;

import com.skt.m.Graphics2D;
import org.recompile.mobile.Mobile;
import org.recompile.mobile.PlatformImage;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class XDisplay 
{
    public static int width;
    public static int height2;

    public static PlatformImage platformImage;

    public static void refresh(int x, int y, int width, int height) 
    {
        Mobile.getPlatform().flushGraphics(platformImage, x, y, width, height);
    }

    public static void drawImageEx(
            Graphics gfx, Image image,
            int tx, int ty,
            Image srcImage,
            int sx, int sy,
            int sw, int sh,
            int mode) 
    {
        Graphics2D.getGraphics2D(gfx).drawImage(tx, ty, srcImage, sx, sy, sw, sh, mode);
    }
}
