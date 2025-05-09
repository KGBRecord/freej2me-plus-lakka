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

public class MIDletEnhancements 
{
    // TODO: Make ALL these dependent on a future "Fast-Forward" button

    // These sleep calls tend to affect game framerate more often than not
    public static void drawSleep(long millis) 
    {
        if (Mobile.unlockFramerateHack > 0) 
        {
            // Do not sleep
        }
        else 
        {
            try { Thread.sleep(millis); } 
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    public static void sleep(long millis) 
    {
        if (Mobile.unlockFramerateHack > 1) 
        {
            // Do not sleep
        }
        else 
        {
            try { Thread.sleep(millis); } 
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    // These are far more useful for Fast-Forwarding
    public static long currentTimeMillis() 
    { 
        if(Mobile.unlockFramerateHack > 2)
        {
            return (long) (System.currentTimeMillis() * (Mobile.limitFPS == 0 ? 999 : Mobile.limitFPS/10f)); 
        }
        else { return System.currentTimeMillis(); }
    }

    public static long nanoTime() 
    {
        if(Mobile.unlockFramerateHack > 2) 
        {
            return (long) (System.nanoTime() * (Mobile.limitFPS == 0 ? 999 : Mobile.limitFPS/10f));
        }
        else { return System.nanoTime(); }
    }
}