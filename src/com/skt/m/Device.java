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

import org.recompile.mobile.Mobile;

public class Device 
{
    private static boolean backlightEnabled = true;

    public static void setBacklightEnabled(boolean flag) 
    {
        backlightEnabled = flag;

        /*if (flag) {
            Mobile.getDisplay().flashBacklight(Integer.MAX_VALUE);
        } else {
            Mobile.getDisplay().flashBacklight(0);
        }*/
    }

    public static boolean isBacklightEnabled() { return backlightEnabled; }

    public static boolean isKeyToneEnabled() { return false; }

    public static void setKeyToneEnabled(boolean flag) 
    {

    }

    public static void enableRestoreLCD(boolean flag) 
    {

    }

    public static void setColorMode(int mode) 
    {

    }

    public static void setKeyRepeatTime(int delay, int interval) 
    {

    }

    public static void invokeWapBrowser(String url) 
    {
        /*
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }*/
    }
}
