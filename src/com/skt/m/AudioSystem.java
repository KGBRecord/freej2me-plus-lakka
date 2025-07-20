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
package com.skt.m;

import com.skt.m.impl.AudioClipImpl;

import java.io.IOException;

public class AudioSystem 
{

    private static AudioClipImpl clip = new AudioClipImpl();

    public static int getMaxVolume(String format) throws UnsupportedFormatException 
    {
        return 5;
    }

    public static AudioClip getAudioClip(String format) throws UnsupportedFormatException 
    {
        return clip;
    }

    public static int getVolume(String format) throws UnsupportedFormatException 
    {
        return clip.getVolume();
    }

    public static void setVolume(String format, int level) throws UnsupportedFormatException 
    {
        clip.setVolume(level);
    }
}
