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

import javax.microedition.lcdui.Graphics;
import java.io.IOException;
import java.io.InputStream;

public class FlashImage 
{
    public static final int COMMAND_PAUSE = 3;
    public static final int COMMAND_PLAY = 1;
    public static final int COMMAND_RESUME = 4;
    public static final int COMMAND_STOP = 2;
    public static final int KEY_PRESSED = 0;
    public static final int KEY_RELEASED = 3;
    public static final int KEY_REPEATED = 1;
    public static final int POINTER_DRAGGED = 1;
    public static final int POINTER_PRESSED = 2;
    public static final int POINTER_RELEASED = 3;

    private ExternalResourceHandler externalRH;

    private FlashImage(ExternalResourceHandler externalRH) { this.externalRH = externalRH; }

    public static FlashImage createImage(InputStream flashContent, ExternalResourceHandler externalRH) throws IOException 
    {
        if (flashContent == null) { throw new NullPointerException("flashContent cannot be null"); }
        return new FlashImage(externalRH);
    }

    public void dispatchCommand(int command) { }

    public void dispatchKeyEvent(int keyCode, int type) { }

    public void dispatchPointerEvent(int x, int y, int type) { }

    public int render(Graphics g, int x, int y, int width, int height) 
    {
        if (g == null) { throw new NullPointerException("Graphics cannot be null"); }
        if (width < 0 || height < 0) { throw new IllegalArgumentException("Width and height cannot be negative"); }
        return 0;
    }

    public void resourceAvailable(String URI, InputStream resourceData) throws IOException 
    {
        if (URI == null) { throw new NullPointerException("URI cannot be null"); }
    }

    public void setFlashDataRequestListener(FlashDataRequestListener requestListener) { }

    public void setFlashEventManager(FlashEventManager eventManager) { }
}