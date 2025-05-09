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
    private static long curTimeMillis = 0;
    private static long curNanoTime = 0;
    private static long lastMillisTime = System.currentTimeMillis();
    private static long lastNanoTime = System.nanoTime();

    public static void drawSleep(long millis) 
    {
        if (Mobile.unlockFramerateHack > 0 || MobilePlatform.pressedKeys[19]) 
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
        if (Mobile.unlockFramerateHack > 1 || MobilePlatform.pressedKeys[19]) 
        {
            // Do not sleep
        } 
        else 
        {
            try { Thread.sleep(millis); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    public static long currentTimeMillis() 
    {
        long now = System.currentTimeMillis();
        long elapsedMillis = now - lastMillisTime;

        if (MobilePlatform.pressedKeys[19]) // Fast-Forward Key
        {
            curTimeMillis += elapsedMillis * 999;
        } 
        else if (Mobile.unlockFramerateHack > 2) 
        {
            curTimeMillis += elapsedMillis * (Mobile.limitFPS == 0 ? 999 : (float) Mobile.limitFPS / 10f);
        } 
        else 
        {
            curTimeMillis += elapsedMillis;
        }

        lastMillisTime = now;
        return curTimeMillis;
    }

    public static long nanoTime() 
    {
        long now = System.nanoTime();
        long elapsedNanos = now - lastNanoTime;

        if (MobilePlatform.pressedKeys[19]) 
        {
            curNanoTime += elapsedNanos * 999;
        } 
        else if (Mobile.unlockFramerateHack > 2) 
        {
            curNanoTime += elapsedNanos * (Mobile.limitFPS == 0 ? 999 : (float) Mobile.limitFPS / 10f);
        } 
        else 
        {
            curNanoTime += elapsedNanos;
        }

        lastNanoTime = now;
        return curNanoTime;
    }
}