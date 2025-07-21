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
package org.recompile.mobile;

import javax.microedition.lcdui.Font;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.font.TextAttribute;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import java.util.Hashtable;
import java.util.Map;

import org.recompile.mobile.Mobile;

public class PlatformFont
{
	private FontMetrics metrics;

	private static Graphics gc;

	public java.awt.Font awtFont;

	public static File textfontDir = new File("freej2me_system" + File.separatorChar + "customFont" + File.separatorChar);

	public PlatformFont(Font font)
	{
		if(!textfontDir.isDirectory()) 
		{
			try 
			{
				textfontDir.mkdirs();
				File dummyFile = new File(textfontDir.getPath() + File.separatorChar + "Put your ttf font here");
				dummyFile.createNewFile();
			}
			catch(Exception e) { Mobile.log(Mobile.LOG_ERROR, PlatformFont.class.getPackage().getName() + "." + PlatformFont.class.getSimpleName() + ": " + "Failed to create custom font dir:" + e.getMessage()); }
		}

		/* Get the first ttf font in the directory, if there's any */
		String[] fontfiles = textfontDir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File f, String textfont) {
				String lowerCaseFont = textfont.toLowerCase();
				return lowerCaseFont.endsWith(".ttf") || 
						lowerCaseFont.endsWith(".otf") || 
						lowerCaseFont.endsWith(".ttc");
			}
		});

		if (Mobile.useCustomTextFont && fontfiles != null && fontfiles.length > 0) // Load a custom font if enabled, and there is one
		{
            try 
			{
                awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(textfontDir, fontfiles[0])).deriveFont(font.getStyle(), font.getPointSize());
            } 
			catch (Exception e) // If there's an issue loading it, we can still fallback to the default
			{
				Mobile.log(Mobile.LOG_ERROR, PlatformFont.class.getPackage().getName() + "." + PlatformFont.class.getSimpleName() + ": " + "Failed to load custom font:" + e.getMessage());
				// Fallback
				String fontFace = java.awt.Font.SANS_SERIF;
				if(font.getFace() == Font.FACE_MONOSPACE) { fontFace = java.awt.Font.MONOSPACED; }
				else if(font.getFace() == Font.FACE_PROPORTIONAL) { fontFace = java.awt.Font.DIALOG; }

				awtFont = new java.awt.Font(fontFace, font.getStyle(), font.getPointSize());
            }
        }
		else if(!Mobile.useCustomTextFont) // If the user is not going to use custom fonts, or there are no custom fonts in the directory, load the defaults
		{
			// We'll use SansSerif for SYSTEM
			String fontFace = java.awt.Font.SANS_SERIF;
			if(font.getFace() == Font.FACE_MONOSPACE) { fontFace = java.awt.Font.MONOSPACED; }
			else if(font.getFace() == Font.FACE_PROPORTIONAL) { fontFace = java.awt.Font.DIALOG; }

			awtFont = new java.awt.Font(fontFace, font.getStyle(), font.getPointSize());
		}
		
		// This section is font independent

		// Standard java doesn't handle underlining the same way, so do it here
		if((font.getStyle() & Font.STYLE_UNDERLINED) > 0)
		{
			Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>(1);
			map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

			awtFont = awtFont.deriveFont(map);
		}

		if(gc == null) { System.out.println("newg"); gc = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics(); }
		gc.setFont(awtFont);
		metrics = gc.getFontMetrics();
	}

	// DoJa Font
	public PlatformFont(com.nttdocomo.ui.Font font)
	{
		if(!textfontDir.isDirectory()) 
		{
			try 
			{
				textfontDir.mkdirs();
				File dummyFile = new File(textfontDir.getPath() + File.separatorChar + "Put your ttf font here");
				dummyFile.createNewFile();
			}
			catch(Exception e) { Mobile.log(Mobile.LOG_ERROR, PlatformFont.class.getPackage().getName() + "." + PlatformFont.class.getSimpleName() + ": " + "Failed to create custom font dir:" + e.getMessage()); }
		}

		/* Get the first ttf font in the directory, if there's any */
		String[] fontfiles = textfontDir.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File f, String textfont) {
				String lowerCaseFont = textfont.toLowerCase();
				return lowerCaseFont.endsWith(".ttf") || 
						lowerCaseFont.endsWith(".otf") || 
						lowerCaseFont.endsWith(".ttc");
			}
		});

		if (Mobile.useCustomTextFont && fontfiles != null && fontfiles.length > 0) // Load a custom font if enabled, and there is one
		{
            try 
			{
                awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(textfontDir, fontfiles[0])).deriveFont(convertDoJaToLCDUIStyle(font.getStyle()), font.getPointSize());
            } 
			catch (Exception e) // If there's an issue loading it, we can still fallback to the default
			{
				Mobile.log(Mobile.LOG_ERROR, PlatformFont.class.getPackage().getName() + "." + PlatformFont.class.getSimpleName() + ": " + "Failed to load custom font:" + e.getMessage());
				// Fallback
				String fontFace = java.awt.Font.SANS_SERIF;
				if(convertDoJaToLCDUIFace(font.getFace())  == com.nttdocomo.ui.Font.FACE_MONOSPACE) { fontFace = java.awt.Font.MONOSPACED; }
				else if(convertDoJaToLCDUIFace(font.getFace()) == Font.FACE_PROPORTIONAL) { fontFace = java.awt.Font.DIALOG; }

				awtFont = new java.awt.Font(fontFace, convertDoJaToLCDUIStyle(font.getStyle()), font.getPointSize());
            }
        }
		else if(!Mobile.useCustomTextFont) // If the user is not going to use custom fonts, or there are no custom fonts in the directory, load the defaults
		{
			// We'll use SansSerif for SYSTEM
			String fontFace = java.awt.Font.SANS_SERIF;
			if(convertDoJaToLCDUIFace(font.getFace())  == com.nttdocomo.ui.Font.FACE_MONOSPACE) { fontFace = java.awt.Font.MONOSPACED; }
			else if(convertDoJaToLCDUIFace(font.getFace()) == com.nttdocomo.ui.Font.FACE_PROPORTIONAL) { fontFace = java.awt.Font.DIALOG; }

			awtFont = new java.awt.Font(fontFace, convertDoJaToLCDUIStyle(font.getStyle()), font.getPointSize());
		}
		
		// This section is font independent

		// Standard java doesn't handle underlining the same way, so do it here
		if((font.getStyle() & Font.STYLE_UNDERLINED) > 0)
		{
			Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>(1);
			map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

			awtFont = awtFont.deriveFont(map);
		}

		if(gc == null) { gc = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics(); }
		gc.setFont(awtFont);
		metrics = gc.getFontMetrics();
	}

	public int stringWidth(String str) { return metrics.stringWidth(str); }

	public int getHeight() { return metrics.getHeight(); }

	public int getAscent() { return metrics.getAscent(); }

	public int convertDoJaToLCDUIStyle(int doJaStyle) 
	{
		switch(doJaStyle) 
		{
			case com.nttdocomo.ui.Font.STYLE_BOLD: return Font.STYLE_BOLD;
			case com.nttdocomo.ui.Font.STYLE_BOLDITALIC: return Font.STYLE_BOLD | Font.STYLE_ITALIC;
			case com.nttdocomo.ui.Font.STYLE_ITALIC: return Font.STYLE_ITALIC; 
			case com.nttdocomo.ui.Font.STYLE_PLAIN:
			default: return doJaStyle;
		}
	}

	public int convertDoJaToLCDUIFace(int doJaFace) 
	{
		switch(doJaFace) 
		{
			case com.nttdocomo.ui.Font.FACE_MONOSPACE: return Font.FACE_MONOSPACE;
			case com.nttdocomo.ui.Font.FACE_PROPORTIONAL: return Font.FACE_PROPORTIONAL;
			case com.nttdocomo.ui.Font.FACE_SYSTEM: return Font.FACE_SYSTEM; 
			default: return doJaFace;
		}
	}
}
