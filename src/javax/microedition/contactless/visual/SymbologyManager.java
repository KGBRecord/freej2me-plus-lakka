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
package javax.microedition.contactless.visual;

import javax.microedition.contactless.ContactlessException;

public class SymbologyManager 
{

    public static String[] getGenerateSymbologies() 
    {
        // Implementation to return supported symbologies for image generation
        return new String[]{
            "aztec-code",
            "code-16k",
            "code-39",
            "code-49",
            "code-93",
            "code-128",
            "codebar",
            "data-matrix",
            "ean-upc",
            "interleaved-2-of-5",
            "maxicode",
            "pdf417",
            "qr-code"
        };
    }

    public static Class<?>[] getImageClasses() 
    {
        return new Class<?>[]
        {
            javax.microedition.lcdui.Image.class,
            com.nttdocomo.ui.Image.class
        };
    }

    public static ImageProperties getImageProperties(String symbologyName) throws ContactlessException 
    {
        if (symbologyName == null) { throw new NullPointerException("Symbology name cannot be null."); }

        throw new ContactlessException("Symbology not supported: " + symbologyName);
    }

    public static String[] getReadSymbologies() 
    {
        return new String[]
        {
            "aztec-code",
            "code-16k",
            "code-39",
            "code-49",
            "code-93",
            "code-128",
            "codebar",
            "data-matrix",
            "ean-upc",
            "interleaved-2-of-5",
            "maxicode",
            "pdf417",
            "qr-code"
        };
    }
}