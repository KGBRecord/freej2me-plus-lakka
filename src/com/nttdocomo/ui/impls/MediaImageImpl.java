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
package com.nttdocomo.ui.impls;

import java.io.InputStream;

import com.nttdocomo.io.ConnectionException;
import com.nttdocomo.ui.UIException;
import com.nttdocomo.ui.Image;

public class MediaImageImpl implements com.nttdocomo.ui.MediaImage 
{
	private ImageImpl doJaImage;

	public MediaImageImpl(int width, int height) { doJaImage = new ImageImpl(width, height); }

	public MediaImageImpl(String str) { doJaImage = new ImageImpl(str); }

	public MediaImageImpl(InputStream inputStream) { doJaImage = new ImageImpl(inputStream); }

	public MediaImageImpl(byte[] data) { doJaImage = new ImageImpl(data, 0, data.length); }

    public int getHeight() { return doJaImage.getHeight(); }

    public int getWidth() { return doJaImage.getWidth(); }
    
    public Image getImage() { return doJaImage; }

    public void dispose() { doJaImage = null; }

	public void setExifData(com.nttdocomo.ui.ExifData exif) { }

	public com.nttdocomo.ui.ExifData getExifData() { return null; }

	public void use() throws com.nttdocomo.io.ConnectionException { }

	public void use(com.nttdocomo.ui.MediaResource overwritten, boolean useOnce) throws com.nttdocomo.io.ConnectionException { }

	public void unuse() { }

}