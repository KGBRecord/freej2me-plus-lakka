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

public abstract class ScalableImage 
{

    protected ScalableImage() { }

    public static ScalableImage createImage(InputStream stream, ExternalResourceHandler handler) throws IOException 
    {
        if (stream == null) { throw new NullPointerException("InputStream cannot be null."); }
        // TODO: Create image from inputStream
        return null; 
    }

    public static ScalableImage createImage(String url, ExternalResourceHandler handler) throws IOException 
    {
        if (url == null) { throw new NullPointerException("URL cannot be null."); }
        // TODO: Create image from the URL
        return null; 
    }

    public abstract int getViewportWidth();

    public abstract int getViewportHeight();

    public abstract void setViewportWidth(int width);

    public abstract void setViewportHeight(int height);

    public abstract void requestCompleted(String URI, InputStream resourceData) throws IOException;
}