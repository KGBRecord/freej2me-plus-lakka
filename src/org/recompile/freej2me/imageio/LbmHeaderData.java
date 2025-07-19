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
package org.recompile.freej2me.imageio;

public class LbmHeaderData 
{
    public final int bitDepth;

    public final int width;
    public final int height;

    public final int bytesPerPlane;
    public final boolean enableAlpha;

    public LbmHeaderData(int bitDepth, int width, int height, int bytesPerPlane, boolean enableAlpha) 
    {
        this.bitDepth = bitDepth;
        this.width = width;
        this.height = height;
        this.bytesPerPlane = bytesPerPlane;
        this.enableAlpha = enableAlpha;
    }
}
