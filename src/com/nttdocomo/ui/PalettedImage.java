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

import java.io.InputStream;

import org.recompile.mobile.PlatformImage;

// TODO: This class is not yet complete
public abstract class PalettedImage extends Image 
{
    protected Image image;
    protected Palette palette;
    protected byte[] imageData;
    protected int transparentIndex = -1;

    protected PalettedImage() 
    {
        super();
        this.palette = new Palette(256); // Palette has 256 colors
    }

    public static PalettedImage createPalettedImage(byte[] data) 
    {
        return new PalettedImageImpl(data);
    }

    public static PalettedImage createPalettedImage(InputStream in) 
    {
        try 
        {
            byte[] tmpData = new byte[in.available()];
            in.read(tmpData, 0, in.available());
            return new PalettedImageImpl(tmpData);
        }
        catch (Exception e) { return null; }
    }

    public static PalettedImage createPalettedImage(int width, int height) 
    {
        return new PalettedImageImpl(width, height);
    }

    public void changeData(byte[] data) 
    {
        this.imageData = data;
        this.palette = extractPalette(data);
    }

    public void changeData(InputStream in) 
    {
        try { in.read(imageData, 0, in.available()); }
        catch (Exception e) { }
    }

    public static Palette extractPalette(byte[] imageData) 
    {
        if (imageData.length < 4) { throw new IllegalArgumentException("Image data is too short."); }
    
        // Check for GIF signature. If it's not GIF, it should be a Microsoft BMP signature, and if not, we don't support it
        if (imageData[0] == 'G' && imageData[1] == 'I' && imageData[2] == 'F') { return extractPaletteFromGIF(imageData); }
        else if (imageData[0] == 'B' && imageData[1] == 'M') { return extractPaletteFromBMP(imageData); } 
        else { throw new UnsupportedOperationException("Unsupported image format."); }
    }
    
    private static Palette extractPaletteFromGIF(byte[] gifData) 
    {
        int colorTableSize = 0;
        if ((gifData[10] & 0x80) != 0) // Check if global color table flag is set
        {
            // Then we determine the size of the color table, as not all gifs will use the whole 256 color palette
            final int bitsPerPixel = (gifData[10] & 0x07) + 1; 
            colorTableSize = 1 << bitsPerPixel;

            final int[] colors = new int[colorTableSize];
            final int headerOffset = 13;

            for (int i = 0; i < colorTableSize; i++) 
            {
                int r = gifData[headerOffset + (i * 3)];
                int g = gifData[headerOffset + (i * 3) + 1];
                int b = gifData[headerOffset + (i * 3) + 2];
                colors[i] = (0xFF << 24) | (r << 16) | (g << 8) | b;
            }

            return new Palette(colors);
        } 
        else { throw new UnsupportedOperationException("No global color table present in GIF."); }
    }

    private static Palette extractPaletteFromBMP(byte[] bmpData) 
    {
        final int colorTableOffset = 54; // The color table starts at offset 54, that is, immediately after the header
        final int bitDepth = bmpData[28] & 0xFF; // Bit depth's header position on Windows/MS BMP
        int maxPaletteSize;

        // Determine the max palette size based on the header's retrieved bit depth
        switch (bitDepth) 
        {
            case 1:
                maxPaletteSize = 2;
                break;
            case 4:
                maxPaletteSize = 16;
                break;
            case 8:
                maxPaletteSize = 256;
                break;
            default: // No support for 16/24/32-bit BMP, as we are limited to a 256 color palette (so the max must be 8bits per pixel)
                throw new UnsupportedOperationException("Unsupported BMP bit depth: " + bitDepth);
        }

        // Read the number of colors from the BMP header (it can be any value up to 2^bitDepth, or 0 to default)
        int actualPaletteSize = bmpData[46] & 0xFF;
        if (actualPaletteSize == 0) { actualPaletteSize = maxPaletteSize; } // If it's 0, default to 2^bitDepth colors

        actualPaletteSize = Math.min(actualPaletteSize, maxPaletteSize);

        // That out of the way, we might as well check if the BMP's color table is valid in regards to the data length
        if (bmpData.length < colorTableOffset + (actualPaletteSize * 4)) 
        {
            throw new IllegalArgumentException("BMP data does not contain a valid color table.");
        }

        int[] colors = new int[actualPaletteSize];

        // Read the color table
        for (int i = 0; i < actualPaletteSize; i++) 
        {
            int b = bmpData[colorTableOffset + (i * 4)];
            int g = bmpData[colorTableOffset + (i * 4) + 1];
            int r = bmpData[colorTableOffset + (i * 4) + 2];
            colors[i] = (0xFF << 24) | (r << 16) | (g << 8) | b;
        }

        return new Palette(colors);
    }

    public Palette getPalette() { return palette; }

    public void setPalette(Palette palette) { this.palette = palette; }

    public int getTransparentIndex() { return transparentIndex; }

    public void setTransparentIndex(int index) { this.transparentIndex = index; }

    public void setTransparentEnabled(boolean enabled) { }

    public Graphics getGraphics() { throw new UnsupportedOperationException("getGraphics() is not supported for PalettedImage."); }

    public int getWidth() { return super.getWidth(); }

    public int getHeight() { return super.getWidth(); }
}