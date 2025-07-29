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
import java.util.ArrayList;
import java.util.List;

// TODO: This class is not yet complete
public abstract class PalettedImage extends Image 
{
    protected Palette palette;
    protected int transparentIndex = 0, colorTableSize = 0;

    protected PalettedImage() { }

    protected PalettedImage(int width, int height)
    {
        super(width, height);
        // Palette will be set by the app
        this.colorTableSize = 256; // Max amount of colors, the app must be able to set any valid palette
    }

    protected PalettedImage(byte[] data) 
    {
        super(data, 0, data.length);
        this.palette = extractPalette(data);
        this.colorTableSize = palette.getEntryCount();
        this.palette.setImage(this);
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
        setCanvas(createImage(data, 0, data.length).getCanvas());
        this.palette = extractPalette(data);
        this.colorTableSize = palette.getEntryCount();
        this.palette.setImage(this);
    }

    public void changeData(InputStream in) 
    {
        try 
        { 
            byte[] tmpData = new byte[in.available()];
            in.read(tmpData, 0, in.available());
            setCanvas(createImage(tmpData, 0, tmpData.length).getCanvas());
            this.palette = extractPalette(tmpData);
            this.colorTableSize = palette.getEntryCount();
            this.palette.setImage(this);
        }
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

    public void setPalette(Palette palette) 
    { 
        if(palette.getEntryCount() < colorTableSize) { throw new IllegalArgumentException("New palette has less entries than the current one"); }
        
        /*  TODO: Doesn't seem needed here
        List<Integer> originalColors = new ArrayList<Integer>();
        List<Integer> newColors = new ArrayList<Integer>();
        // Update the image's data based on the new palette first
        for(int i = 0; i < palette.getEntryCount(); i++) 
        {
            if(this.palette.getEntry(i) != palette.getEntry(i)) 
            { 
                originalColors.add(this.palette.getEntry(i));
                newColors.add(palette.getEntry(i));
            }
        }

        int[] originalPalette = new int[originalColors.size()];
        int[] newPalette = new int[newColors.size()];
        for (int i = 0; i < originalColors.size(); i++) 
        {
            originalPalette[i] = originalColors.get(i);
            newPalette[i] = newColors.get(i); 
        }
        
        updateImagePalette(originalPalette, newPalette);
        */

        palette.setImage(this);
        this.palette = palette; 
    }

    public int getTransparentColor() { throw new UnsupportedOperationException("getTransparentColor() cannot be called on PalettedImage."); }

    public int getTransparentIndex() { return transparentIndex; }

    public void setTransparentIndex(int index) { this.transparentIndex = index; }

    public Graphics getGraphics() { throw new UnsupportedOperationException("getGraphics() is not supported for PalettedImage."); }

}