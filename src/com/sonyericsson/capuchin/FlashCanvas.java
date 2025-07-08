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
package com.sonyericsson.capuchin;

import javax.microedition.lcdui.Canvas;

public class FlashCanvas extends Canvas 
{
    private FlashImage image;

    public FlashCanvas(FlashImage image) 
    {
        if (image == null) { throw new NullPointerException("image cannot be null"); }

        this.image = image;
    }

    public int getDelay() { return 16; } // Assume a 16ms delay

    protected void paint(javax.microedition.lcdui.Graphics g) { }
}