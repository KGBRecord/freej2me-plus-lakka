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
package javax.microedition.m2g;

import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGElement;

public class SVGImage extends ScalableImage 
{

    protected SVGImage() { }

    public static SVGImage createEmptyImage(ExternalResourceHandler handler) { return new SVGImage(); }

    public Document getDocument() { return null; }

    public int getViewportWidth() { return 100;  }

    public int getViewportHeight() { return 100; }

    public void setViewportWidth(int width) 
    {
        if (width < 0) { throw new IllegalArgumentException("Width cannot be negative."); }
    }

    public void setViewportHeight(int height) 
    {
        if (height < 0) { throw new IllegalArgumentException("Height cannot be negative."); }
    }

    public void requestCompleted(String URI, InputStream resourceData) throws IOException 
    {
        if (URI == null) { throw new NullPointerException("URI cannot be null."); }
    }

    public void activate() { }

    public void dispatchMouseEvent(String type, int x, int y) throws DOMException 
    {
        if (type == null) { throw new NullPointerException("Type cannot be null."); }
        if (x < 0 || y < 0) { throw new IllegalArgumentException("Coordinates cannot be negative."); }
    }

    public void focusOn(SVGElement element) throws DOMException 
    {
        if (element == null) { throw new NullPointerException("Element cannot be null."); }
    }

    public void incrementTime(float seconds) 
    {
        if (seconds < 0) { throw new IllegalArgumentException("Time increment cannot be negative."); }
    }
}